//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.simplepivot;

import com.google.common.collect.Sets;
import io.deephaven.api.agg.spec.AggSpec;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.liveness.LivenessArtifact;
import io.deephaven.engine.rowset.RowSetFactory;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.MultiJoinFactory;
import io.deephaven.engine.table.PartitionedTable;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.impl.ListenerRecorder;
import io.deephaven.engine.table.impl.MergedListener;
import io.deephaven.engine.table.impl.QueryTable;
import io.deephaven.engine.util.TableTools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class SimplePivotTable extends LivenessArtifact {
    /**
     * Factory instance, which can be bound to a variable for clients to fetch and invoke.
     */
    public static final Factory FACTORY = new Factory();
    private static final String PIVOT_COL_PREFIX = "PIVOT_C_";

    private final PartitionedTable partitionedTable;
    private final List<String> columnColNames;
    private final List<String> rowColNames;
    private final String valueColName;
    private final MergedListener mergedListener;

    private Table multiJoined;
    private final List<ListenerRecorder> recorders = new ArrayList<>();

    private final List<Runnable> subscribers = new CopyOnWriteArrayList<>();

    public static class Factory {
        public SimplePivotTable create(Table table, List<String> columnColNames, List<String> rowColNames,
                String valueColName, AggSpec aggSpec) {
            // Validate that all column names are present in the table
            HashSet<String> allColumnNames = new HashSet<>(table.getDefinition().getColumnNames());
            if (!allColumnNames.containsAll(columnColNames)) {
                throw new IllegalArgumentException("Column names not found in table: "
                        + Sets.difference(new HashSet<>(columnColNames), allColumnNames));
            }
            if (!allColumnNames.containsAll(rowColNames)) {
                throw new IllegalArgumentException("Column names not found in table: "
                        + Sets.difference(new HashSet<>(rowColNames), allColumnNames));
            }
            if (!allColumnNames.contains(valueColName)) {
                throw new IllegalArgumentException("Column name not found in table: " + valueColName);
            }
            if (columnColNames.contains(valueColName) || rowColNames.contains(valueColName)) {
                throw new IllegalArgumentException(
                        "Value column name cannot be in grouping column names: " + valueColName);
            }

            return new SimplePivotTable(table, columnColNames, rowColNames, valueColName, aggSpec);
        }
    }

    public SimplePivotTable(Table table, List<String> columnColNames, List<String> rowColNames, String valueColName,
            AggSpec aggSpec) {
        this.columnColNames = columnColNames;
        this.rowColNames = rowColNames;
        this.valueColName = valueColName;

        List<String> byColumns = new ArrayList<>(columnColNames);
        byColumns.addAll(rowColNames);
        List<String> allColumns = new ArrayList<>(byColumns);
        allColumns.add(valueColName);
        if (table.getDefinition().getColumnNames().size() != allColumns.size()) {
            table = table.view(allColumns.toArray(String[]::new));
        }
        Table agg = table.aggAllBy(aggSpec, byColumns);
        partitionedTable = agg.partitionBy(columnColNames.toArray(String[]::new));
        ExecutionContext context = ExecutionContext.getContext();

        // Initial join on current data - if not refreshing, this is the only time it will run
        multiJoin();

        if (table.isRefreshing()) {
            manage(partitionedTable);

            // Listen to changes in the table, to see if we need to recreate columns
            ListenerRecorder partitionedTableListenerRecorder =
                    new ListenerRecorder("pivot table listener", partitionedTable.table(), null);
            recorders.add(partitionedTableListenerRecorder);
            mergedListener = new MergedListener(recorders, List.of(), "pivot table listener", null) {
                @Override
                protected void process() {
                    context.apply(() -> {
                        if (partitionedTableListenerRecorder.getShifted().nonempty()
                                || partitionedTableListenerRecorder.getRemoved().isNonempty()
                                || partitionedTableListenerRecorder.getModified().isNonempty()) {
                            // Any removal or modification means we must rebuild the multijoin. We presently use
                            // row key for column names, so shifts also force a rebuild
                            if (partitionedTableListenerRecorder.getShifted().nonempty()) {
                                System.out.println("shifted: " + partitionedTableListenerRecorder.getShifted().nonempty() + ", " + partitionedTableListenerRecorder.getShifted().size() + ", " + partitionedTableListenerRecorder.getShifted());
                            }
                            if (partitionedTableListenerRecorder.getRemoved().isNonempty()) {
                                System.out.println("removed: " + partitionedTableListenerRecorder.getRemoved());
                            }
                            if (partitionedTableListenerRecorder.getModified().isNonempty()) {
                                System.out.println("modified: " + partitionedTableListenerRecorder.getModified());
                            }
                            System.out.println("starting full rebuild");
                            multiJoin();
                        } else if (partitionedTableListenerRecorder.getAdded().isNonempty()) {
                            ColumnSource<Table> newTablesSource = partitionedTable.table()
                                    .getColumnSource(partitionedTable.constituentColumnName(), Table.class);
                            List<Table> tables = new ArrayList<>();
                            if (multiJoined != null) {
                                // If the table hasn't ticked yet
                                tables.add(multiJoined);
                            }

                            // Build an array of all row columns that we will multijoin on - the last slot is empty for
                            // the value column
                            String[] cols = new String[rowColNames.size() + 1];
                            for (int j = 0; j < rowColNames.size(); j++) {
                                cols[j] = rowColNames.get(j);
                            }

                            // Collect the tables, rename the value column, drop non-row columns
                            // TODO chunk this
                            partitionedTableListenerRecorder.getAdded().forAllRowKeys(rowKey -> {
                                Table newTable = newTablesSource.get(rowKey);
                                cols[cols.length - 1] = PIVOT_COL_PREFIX + rowKey + '=' + valueColName;
                                tables.add(newTable.view(cols));
                            });
                            replaceMultiJoinedTable(tables);
                        }
                    });

                }

                @Override
                protected boolean canExecute(long step) {
                    synchronized (recorders) {
                        return recorders.stream().allMatch(lr -> lr.satisfied(step));
                    }
                }
            };
            partitionedTableListenerRecorder.setMergedListener(mergedListener);
            partitionedTable.table().addUpdateListener(partitionedTableListenerRecorder);
        } else {
            mergedListener = null;
        }
    }


    private synchronized void replaceMultiJoinedTable(List<Table> tablesToJoin) {
        Table replacement = tablesToJoin.isEmpty() ? emptyTable() :
                MultiJoinFactory.of(rowColNames.toArray(String[]::new), tablesToJoin.toArray(Table[]::new)).table();

        // If replacement is refreshing, manage the result and add to our recorder listener list
        if (replacement.isRefreshing()) {
            manage(replacement);

            synchronized (recorders) {
                ListenerRecorder multiJoinedTableListener =
                        new ListenerRecorder("pivot table listener", replacement, null);
                multiJoinedTableListener.setMergedListener(mergedListener);
                replacement.addUpdateListener(multiJoinedTableListener);
                if (recorders.size() > 1) {
                    recorders.set(1, multiJoinedTableListener);
                } else {
                    recorders.add(multiJoinedTableListener);
                }
            }
        }

        if (multiJoined != null && multiJoined.isRefreshing()) {
            unmanage(multiJoined);
        }
        multiJoined = replacement;

        for (Runnable subscriber : subscribers) {
            subscriber.run();
        }
    }

    private Table emptyTable() {
        return TableTools.newTable(partitionedTable.constituentDefinition()).view(rowColNames.toArray(String[]::new));
    }

    private void multiJoin() {
        // confirm we can safely read data
        if (partitionedTable.table().isRefreshing()) {
            partitionedTable.table().getUpdateGraph().checkInitiateSerialTableOperation();
        }

        // Create a column name from the key in the partitioned table
        List<Table> tables = new ArrayList<>(partitionedTable.table().intSize());
        ColumnSource<Table> tableColumnSource =
                partitionedTable.table().getColumnSource(partitionedTable.constituentColumnName(), Table.class);
        // Build an array of all row columns that we will multijoin on - the last slot is empty for
        // the value column
        String[] cols = new String[rowColNames.size() + 1];
        for (int j = 0; j < rowColNames.size(); j++) {
            cols[j] = rowColNames.get(j);
        }

        // For each constituent table, rename the value column to include its key name
        partitionedTable.table().getRowSet().forAllRowKeys(key -> {
            cols[cols.length - 1] = PIVOT_COL_PREFIX + key + '=' + valueColName;
            tables.add(tableColumnSource.get(key).view(cols));
        });

        // Multi-join each of the renamed tables
        replaceMultiJoinedTable(tables);
    }

    public Table getTable() {
        return multiJoined;
    }

    public Table getColumnKeys() {
        // TODO sort this by keys? or let the client do it? we need order to be stable
        return partitionedTable.table().view(partitionedTable.keyColumnNames().toArray(String[]::new));
    }

    public synchronized void subscribe(Runnable callback) {
        subscribers.add(callback);
        callback.run();
    }
}
