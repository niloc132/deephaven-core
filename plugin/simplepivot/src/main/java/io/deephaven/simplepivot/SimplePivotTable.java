//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.simplepivot;

import com.google.common.collect.Sets;
import io.deephaven.api.agg.spec.AggSpec;
import io.deephaven.base.verify.Assert;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.context.StandaloneQueryScope;
import io.deephaven.engine.liveness.LivenessArtifact;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.MultiJoinFactory;
import io.deephaven.engine.table.PartitionedTable;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.impl.ListenerRecorder;
import io.deephaven.engine.table.impl.MergedListener;
import io.deephaven.engine.util.TableTools;
import io.deephaven.util.type.ArrayTypeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 *
 */
public class SimplePivotTable extends LivenessArtifact {
    /**
     * Factory instance, which can be bound to a variable for clients to fetch and invoke.
     */
    public static final Factory FACTORY = new Factory();
    public static final String PIVOT_COL_PREFIX = "PIVOT_C_";
    public static final String PIVOT_COLUMN = "__PIVOT_COLUMN";
    public static final String TOTALS_COLUMN = "__TOTALS_COLUMN";

    private final List<String> rowColNames;
    private final List<String> columnColNames;
    private final Table totalsRow;

    private final Table totalsCol;
    private final Table totalsCell;
    private final PartitionedTable partitionedTable;
    private final Table constituentTable;
    private final ColumnSource<Integer> pivotIdColumn;
    private final ColumnSource<Table> constituentColumn;

    private final String valueColName;
    private final MergedListener mergedListener;

    private Table multiJoined;
    private Table multiJoinedTotals;
    private final List<ListenerRecorder> recorders = new ArrayList<>();

    private final List<Runnable> subscribers = new CopyOnWriteArrayList<>();

    /**
     * Factory for creating a SimplePivotTable.
     * <p>
     * Having a separate factory type and an instance of that factory lets us provide an instance of the factory
     * that clients can interact with through a plugin.
     */
    public static class Factory {
        public SimplePivotTable create(Table table, List<String> columnColNames, List<String> rowColNames,
                String valueColName, AggSpec aggSpec, boolean includeTotals) {
            // Validate that all column names are present in the table
            Set<String> allColumnNames = table.getDefinition().getColumnNameMap().keySet();
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

            return new SimplePivotTable(table, columnColNames, rowColNames, valueColName, aggSpec, includeTotals);
        }
    }

    public SimplePivotTable(Table table, List<String> columnColNames, List<String> rowColNames, String valueColName,
            AggSpec aggSpec, boolean includeTotals) {
        this.rowColNames = List.copyOf(rowColNames);
        this.columnColNames = List.copyOf(columnColNames);
        this.valueColName = valueColName;

        List<String> byColumns = new ArrayList<>(columnColNames);
        byColumns.addAll(rowColNames);
        List<String> allColumns = new ArrayList<>(byColumns);
        allColumns.add(valueColName);
        if (table.getDefinition().getColumnNames().size() != allColumns.size()) {
            table = table.view(allColumns.toArray(String[]::new));
        }
        Table agg = table.sort(rowColNames.toArray(String[]::new)).aggAllBy(aggSpec, byColumns);
        partitionedTable = agg.partitionBy(columnColNames.toArray(String[]::new));

        ExecutionContext context = ExecutionContext.getContext();
        StandaloneQueryScope localScope = new StandaloneQueryScope();
        localScope.putParam("rowColNames", rowColNames);
        localScope.putParam("aggSpec", aggSpec);
        localScope.putParam("nextColumnId", new AtomicInteger(0));
        constituentTable = context.withQueryScope(localScope).apply(() -> partitionedTable.table().update(PIVOT_COLUMN + "=nextColumnId.getAndIncrement()")).sort(columnColNames.toArray(String[]::new));
        pivotIdColumn = constituentTable.getColumnSource(PIVOT_COLUMN, int.class);
        constituentColumn = constituentTable.getColumnSource(partitionedTable.constituentColumnName(), Table.class);

        if (includeTotals) {
            totalsRow = context.withQueryScope(localScope).apply(() -> constituentTable.update(partitionedTable.constituentColumnName() + " = "+partitionedTable.constituentColumnName()+".dropColumns(rowColNames).aggAllBy(aggSpec)"));

            List<String> rowColNamesWithTotal = new ArrayList<>(rowColNames);
            rowColNamesWithTotal.add(TOTALS_COLUMN + "=" + valueColName);
            totalsCol = agg.view(rowColNamesWithTotal.toArray(String[]::new)).aggAllBy(aggSpec, rowColNames.toArray(String[]::new));
            totalsCell = table.view(TOTALS_COLUMN + "=" + valueColName).aggAllBy(aggSpec);
        } else {
            totalsRow = null;

            totalsCol = null;
            totalsCell = null;
        }

        if (table.isRefreshing()) {
            manage(partitionedTable);

            // Listen to changes in the table, to see if we need to recreate columns
            ListenerRecorder partitionedTableListenerRecorder =
                    new ListenerRecorder("pivot(constituentTable) table listener recorder", constituentTable, null);
            recorders.add(partitionedTableListenerRecorder);
            mergedListener = new MergedListener(recorders, List.of(), "pivot table listener", null) {
                @Override
                protected void process() {
                    context.apply(() -> {
                        if (partitionedTableListenerRecorder.getRemoved().isNonempty() || partitionedTableListenerRecorder.getModified().isNonempty()) {
                            // Any removal/modify must be rebuilt from scratch - shifts should keep the same table
                            multiJoin();
                        } else if (partitionedTableListenerRecorder.getAdded().isNonempty()) {

                            List<Table> tables = new ArrayList<>();
                            Assert.neqNull(multiJoined, "multiJoined");
                            tables.add(multiJoined);

                            // Build an array of all row columns that we will multijoin on - the last slot is empty for
                            // the value column
                            String[] cols = new String[rowColNames.size() + 1];
                            for (int j = 0; j < rowColNames.size(); j++) {
                                cols[j] = rowColNames.get(j);
                            }

                            // Collect the tables, rename the value column, drop non-row columns
                            // TODO chunk this
                            partitionedTableListenerRecorder.getAdded().forAllRowKeys(rowKey -> {
                                Table newTable = constituentColumn.get(rowKey);
                                int pivot = pivotIdColumn.get(rowKey);
                                // Rewrite this value each pass through this loop
                                cols[cols.length - 1] = PIVOT_COL_PREFIX + pivot + '=' + valueColName;
                                tables.add(newTable.view(cols));
                            });
                            replaceMultiJoinedTable(tables);
                        }
                    });
                }

                @Override
                protected boolean canExecute(long step) {
                    synchronized (recorders) {
//                        System.out.println(recorders);
                        return recorders.stream().allMatch(lr -> lr.satisfied(step));
                    }
                }
            };
            partitionedTableListenerRecorder.setMergedListener(mergedListener);
            if (includeTotals) {
                for (Table t : List.of(totalsRow, totalsCell, totalsCol)) {
                    manage(t);
                    ListenerRecorder r = new ListenerRecorder("pivot(totals) table listener recorder", t, null);
                    r.setMergedListener(mergedListener);
                    t.addUpdateListener(r);
                    recorders.add(r);
                }
            }
            constituentTable.addUpdateListener(partitionedTableListenerRecorder);
        } else {
            mergedListener = null;
        }

        // Initial join on current data - if not refreshing, this is the only time it will run
        multiJoin();
    }


    private synchronized void replaceMultiJoinedTable(List<Table> tablesToJoin) {
        Table replacement = tablesToJoin.isEmpty() ? emptyTable() :
                MultiJoinFactory.of(rowColNames.toArray(String[]::new), tablesToJoin.toArray(Table[]::new)).table();

        if (totalsRow != null) {
            Table totalsReplacement;
            if (totalsRow.isEmpty()) {
                totalsReplacement = totalsCell;
            } else {
                List<Table> totalsRowsToJoin = new ArrayList<>(totalsRow.intSize() + 1);
                totalsRow.getRowSet().forAllRowKeys(key -> {
                    int pivot = pivotIdColumn.get(key);
                    totalsRowsToJoin.add(constituentColumn.get(key).view(PIVOT_COL_PREFIX + pivot + '=' + valueColName));
                });
                totalsRowsToJoin.add(totalsCell);
                totalsReplacement = MultiJoinFactory.of(ArrayTypeUtils.EMPTY_STRING_ARRAY, totalsRowsToJoin.toArray(Table[]::new)).table();
            }

            if (totalsReplacement.isRefreshing()) {
                manage(totalsReplacement);

                if (multiJoinedTotals != null) {
                    assert multiJoinedTotals.isRefreshing();
                    unmanage(multiJoinedTotals);
                }
            }
            multiJoinedTotals = totalsReplacement;
        }

        // If replacement is refreshing, manage the result and add to our recorder listener list
        if (replacement.isRefreshing()) {
            manage(replacement);

            synchronized (recorders) {
                ListenerRecorder multiJoinedTableListener =
                        new ListenerRecorder("pivot(multijoined) table listener recorder", replacement, null);
                multiJoinedTableListener.setMergedListener(mergedListener);
                replacement.addUpdateListener(multiJoinedTableListener);
                if (totalsCol == null) {
                    if (recorders.size() > 1) {
                        recorders.set(1, multiJoinedTableListener);
                    } else {
                        recorders.add(multiJoinedTableListener);
                    }
                } else {
                    if (recorders.size() > 4) {
                        recorders.set(4, multiJoinedTableListener);
                    } else {
                        recorders.add(multiJoinedTableListener);
                    }
                }
            }
        }

        if (multiJoined != null && multiJoined.isRefreshing()) {
            unmanage(multiJoined);
        }
        this.multiJoined = replacement;

        for (Runnable subscriber : subscribers) {
            subscriber.run();
        }
    }

    private Table emptyTable() {
        return TableTools.newTable(partitionedTable.constituentDefinition()).view(rowColNames.toArray(String[]::new));
    }

    private void multiJoin() {
        // confirm we can safely read data
        if (constituentTable.isRefreshing()) {
            constituentTable.getUpdateGraph().checkInitiateSerialTableOperation();
        }

        // Create a column name from the key in the partitioned table
        List<Table> tables = new ArrayList<>(constituentTable.intSize());
        // Build an array of all row columns that we will multijoin on - the last slot is empty for
        // the value column
        String[] cols = new String[rowColNames.size() + 1];
        for (int j = 0; j < rowColNames.size(); j++) {
            cols[j] = rowColNames.get(j);
        }

        // For each constituent table, rename the value column to include its key name
        constituentTable.getRowSet().forAllRowKeys(key -> {
            int pivot = pivotIdColumn.get(key);

            cols[cols.length - 1] = PIVOT_COL_PREFIX + pivot + '=' + valueColName;
            tables.add(constituentColumn.get(key).view(cols));
        });

        // Attach the totals column, if any
        if (totalsCol != null) {
            tables.add(totalsCol);
        }

        // Multi-join each of the renamed tables
        replaceMultiJoinedTable(tables);
    }

    public Table getTable() {
        return multiJoined;
    }

    public Table getTotalsTable() {
        return multiJoinedTotals;
    }

    public Table getColumnKeys() {
        return constituentTable.view(Stream.concat(Stream.of(PIVOT_COLUMN), partitionedTable.keyColumnNames().stream()).toArray(String[]::new));
    }

    public synchronized Runnable subscribe(Runnable callback) {
        subscribers.add(callback);
        callback.run();
        return () -> {
            synchronized (this) {
                subscribers.remove(callback);
            }
        };
    }

    public List<String> getRowColNames() {
        return rowColNames;
    }

    public List<String> getColumnColNames() {
        return columnColNames;
    }
}
