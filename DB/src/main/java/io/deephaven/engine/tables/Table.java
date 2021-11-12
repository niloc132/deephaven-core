/*
 * Copyright (c) 2016-2021 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.engine.tables;

import io.deephaven.api.*;
import io.deephaven.api.agg.Aggregation;
import io.deephaven.api.agg.Array;
import io.deephaven.api.filter.Filter;
import io.deephaven.base.Function;
import io.deephaven.engine.rowset.TrackingRowSet;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.tables.live.NotificationQueue;
import io.deephaven.engine.tables.remote.AsyncMethod;
import io.deephaven.engine.tables.select.MatchPair;
import io.deephaven.engine.tables.select.WouldMatchPair;
import io.deephaven.engine.tables.utils.LayoutHintBuilder;
import io.deephaven.engine.util.liveness.LivenessNode;
import io.deephaven.engine.v2.*;
import io.deephaven.engine.v2.by.AggregationFormulaStateFactory;
import io.deephaven.engine.v2.by.AggregationIndexStateFactory;
import io.deephaven.engine.v2.by.AggregationStateFactory;
import io.deephaven.engine.v2.by.ComboAggregateFactory;
import io.deephaven.engine.v2.iterators.*;
import io.deephaven.engine.v2.select.SelectColumn;
import io.deephaven.engine.v2.select.SelectFilter;
import io.deephaven.qst.table.TableSpec;
import io.deephaven.util.datastructures.LongSizedDataStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * A Deephaven table.
 */
public interface Table extends
        LongSizedDataStructure,
        LivenessNode,
        NotificationQueue.Dependency,
        DynamicNode,
        SystemicObject,
        TableOperations<Table, Table> {

    Table[] ZERO_LENGTH_TABLE_ARRAY = new Table[0];

    static Table of(TableSpec table) {
        return TableCreatorImpl.create(table);
    }

    /**
     * Explicitly ensure that any work needed to make a table indexable, iterable, or queryable has been done, and
     * return the coalesced child table if appropriate.
     *
     * @return This table, or a fully-coalesced child
     */
    Table coalesce();

    // -----------------------------------------------------------------------------------------------------------------
    // Metadata
    // -----------------------------------------------------------------------------------------------------------------

    @AsyncMethod
    TableDefinition getDefinition();

    /**
     * Provides column metadata in Table form. Convenience method, behaves exactly the same as
     * getDefinition().getColumnDefinitionsTable().
     *
     * @return A Table of metadata about this Table's columns.
     */
    @AsyncMethod
    Table getMeta();

    @AsyncMethod
    String getDescription();

    /**
     * Determines whether this Table contains a column for each string in the specified array of {@code columnNames}.
     *
     * @param columnNames The array of column names to be checked for inclusion in this table. Must not be {@code null}.
     * @return {@code true} if this Table contains a column for each and every string in the {@code columnNames} array;
     *         {@code false} if any element of {@code columnNames} is <b>not</b> the name of a column in this table
     */
    @AsyncMethod
    boolean hasColumns(String... columnNames);

    /**
     * Determines whether this Table contains a column for each string in the specified collection of
     * {@code columnNames}.
     *
     * @param columnNames The collection of column names to be checked for inclusion in this table. Must not be
     *        {@code null}.
     * @return {@code true} if this Table contains a column for each and every string in the {@code columnNames}
     *         collection; {@code false} if any element of {@code columnNames} is <b>not</b> the name of a column in
     *         this table
     */
    @AsyncMethod
    boolean hasColumns(Collection<String> columnNames);

    @Override
    @AsyncMethod
    boolean isRefreshing();

    /**
     * @return The {@link TrackingRowSet} that exposes the row keys present in this Table
     */
    TrackingRowSet getRowSet();

    /**
     * @return {@link #size() Size} if it is currently known without subsequent steps to coalesce the Table, else
     *         {@link io.deephaven.util.QueryConstants#NULL_LONG null}
     */
    long sizeForInstrumentation();

    /**
     * Returns {@code true} if this table has no rows (i.e. {@code size() == 0}).
     *
     * @return {@code true} if this table has no rows
     */
    boolean isEmpty();

    // -----------------------------------------------------------------------------------------------------------------
    // Attributes
    // -----------------------------------------------------------------------------------------------------------------

    String DO_NOT_MAKE_REMOTE_ATTRIBUTE = "DoNotMakeRemote";
    String INPUT_TABLE_ATTRIBUTE = "InputTable";
    String KEY_COLUMNS_ATTRIBUTE = "keyColumns";
    String UNIQUE_KEYS_ATTRIBUTE = "uniqueKeys";
    String SORTABLE_COLUMNS_ATTRIBUTE = "SortableColumns";
    String FILTERABLE_COLUMNS_ATTRIBUTE = "FilterableColumns";
    String LAYOUT_HINTS_ATTRIBUTE = "LayoutHints";
    String TOTALS_TABLE_ATTRIBUTE = "TotalsTable";
    String TABLE_DESCRIPTION_ATTRIBUTE = "TableDescription";
    String COLUMN_RENDERERS_ATTRIBUTE = "ColumnRenderers";
    String COLUMN_DESCRIPTIONS_ATTRIBUTE = "ColumnDescriptions";
    String ADD_ONLY_TABLE_ATTRIBUTE = "AddOnly";
    /**
     * <p>
     * If this attribute is present with value {@code true}, this Table is a "stream table".
     * <p>
     * A stream table is a sequence of additions that represent rows newly received from a stream; on the cycle after
     * the stream table is refreshed the rows are removed. Note that this means any particular row of data (not to be
     * confused with an rowSet key) never exists for more than one cycle.
     * <p>
     * Most operations are supported as normal on stream tables, but aggregation operations are treated specially,
     * producing aggregate results that are valid over the entire observed stream from the time the operation is
     * initiated. These semantics necessitate a few exclusions, i.e. unsupported operations:
     * <ol>
     * <li>{@link #by(SelectColumn...) by()} as a rowSet-aggregation is unsupported. This means any of the overloads for
     * {@link #by(AggregationStateFactory, SelectColumn...)} or {@link #by(Collection, Collection)} using
     * {@link AggregationIndexStateFactory}, {@link AggregationFormulaStateFactory}, or {@link Array}.
     * {@link ComboAggregateFactory#AggArray(String...)}, and
     * {@link ComboAggregateFactory#AggFormula(String, String, String...)} are also unsupported.
     * <li>{@link #byExternal(boolean, String...) byExternal()} is unsupported</li>
     * <li>{@link #rollup(ComboAggregateFactory, boolean, SelectColumn...) rollup()} is unsupported if
     * {@code includeConstituents == true}</li>
     * <li>{@link #treeTable(String, String) treeTable()} is unsupported</li>
     * </ol>
     * <p>
     * To disable these semantics, a {@link #dropStream()} method is offered.
     */
    String STREAM_TABLE_ATTRIBUTE = "StreamTable";
    /**
     * The query engine may set or read this attribute to determine if a table is sorted by a particular column.
     */
    String SORTED_COLUMNS_ATTRIBUTE = "SortedColumns";
    String SYSTEMIC_TABLE_ATTRIBUTE = "SystemicTable";
    // TODO: Might be good to take a pass through these and see what we can condense into
    // TODO: TreeTableInfo and RollupInfo to reduce the attribute noise.
    String ROLLUP_LEAF_ATTRIBUTE = "RollupLeaf";
    String HIERARCHICAL_CHILDREN_TABLE_MAP_ATTRIBUTE = "HierarchicalChildrenTableMap";
    String HIERARCHICAL_SOURCE_TABLE_ATTRIBUTE = "HierarchicalSourceTable";
    String TREE_TABLE_FILTER_REVERSE_LOOKUP_ATTRIBUTE = "TreeTableFilterReverseLookup";
    String HIERARCHICAL_SOURCE_INFO_ATTRIBUTE = "HierarchicalSourceTableInfo";
    String REVERSE_LOOKUP_ATTRIBUTE = "ReverseLookup";
    String PREPARED_RLL_ATTRIBUTE = "PreparedRll";
    String PREDEFINED_ROLLUP_ATTRIBUTE = "PredefinedRollup";
    String SNAPSHOT_VIEWPORT_TYPE = "Snapshot";
    /**
     * This attribute is used internally by TableTools.merge to detect successive merges. Its presence indicates that it
     * is safe to decompose the table into its multiple constituent parts.
     */
    String MERGED_TABLE_ATTRIBUTE = "MergedTable";
    /**
     * <p>
     * This attribute is applied to source tables, and takes on Boolean values.
     * <ul>
     * <li>True for post-{@link #coalesce()} source tables and their children if the source table is empty.</li>
     * <li>False for post-{@link #coalesce()} source tables and their children if the source table is non-empty.</li>
     * <li>Missing for all other tables.</li>
     * </ul>
     */
    String EMPTY_SOURCE_TABLE_ATTRIBUTE = "EmptySourceTable";
    /**
     * This attribute stores a reference to a table that is the parent table for a Preview Table.
     */
    String PREVIEW_PARENT_TABLE = "PreviewParentTable";
    /**
     * Set this attribute for tables that should not be displayed in the UI.
     */
    String NON_DISPLAY_TABLE = "NonDisplayTable";
    /**
     * Set this attribute to load a plugin for this table in the Web Client
     */
    String PLUGIN_NAME = "PluginName";

    /**
     * Set the value of an attribute.
     *
     * @param key the name of the attribute
     * @param object the value
     */
    @AsyncMethod
    void setAttribute(@NotNull String key, @Nullable Object object);

    /**
     * Get the value of the specified attribute.
     *
     * @param key the name of the attribute
     * @return the value, or null if there was none.
     */
    @AsyncMethod
    @Nullable
    Object getAttribute(@NotNull String key);

    /**
     * Get a set of all the attributes that have values for this table.
     *
     * @return a set of names
     */
    @AsyncMethod
    @NotNull
    Set<String> getAttributeNames();

    /**
     * Check if the specified attribute exists in this table.
     *
     * @param name the name of the attribute
     * @return true if the attribute exists
     */
    @AsyncMethod
    boolean hasAttribute(@NotNull String name);

    /**
     * Get all of the attributes from the table.
     *
     * @return A map containing all of the attributes.
     */
    @AsyncMethod
    Map<String, Object> getAttributes();

    /**
     * Get all attributes from the desired table except the items that appear in excluded.
     *
     * @param excluded A set of attributes to exclude from the result
     * @return All of the table's attributes except the ones present in excluded
     */
    @AsyncMethod
    Map<String, Object> getAttributes(Collection<String> excluded);

    // -----------------------------------------------------------------------------------------------------------------
    // ColumnSources for fetching data by row key
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves a {@code ColumnSource}. It is conveniently cast to @{code ColumnSource<T>} using the type that caller
     * expects. This differs from {@link #getColumnSource(String, Class)} which uses the provided {@link Class} object
     * to verify that the data type is a subclass of the expected class.
     *
     * @param sourceName The name of the column
     * @param <T> The target type, as a type parameter. Inferred from context.
     * @return The column source for {@code sourceName}, parameterized by {@code T}
     */
    <T> ColumnSource<T> getColumnSource(String sourceName);

    /**
     * Retrieves a {@code ColumnSource} and {@link ColumnSource#cast casts} it to the target class {@code clazz}.
     *
     * @param sourceName The name of the column
     * @param clazz The target type
     * @param <T> The target type, as a type parameter. Intended to be inferred from {@code clazz}.
     * @return The column source for {@code sourceName}, parameterized by {@code T}
     */
    <T> ColumnSource<T> getColumnSource(String sourceName, Class<? extends T> clazz);

    Map<String, ? extends ColumnSource<?>> getColumnSourceMap();

    Collection<? extends ColumnSource<?>> getColumnSources();

    // -----------------------------------------------------------------------------------------------------------------
    // DataColumns for fetching data by row position; generally much less efficient than ColumnSource
    // -----------------------------------------------------------------------------------------------------------------

    DataColumn[] getColumns();

    DataColumn getColumn(int columnIndex);

    DataColumn getColumn(String columnName);

    // -----------------------------------------------------------------------------------------------------------------
    // Column Iterators
    // -----------------------------------------------------------------------------------------------------------------

    <TYPE> Iterator<TYPE> columnIterator(@NotNull String columnName);

    ByteColumnIterator byteColumnIterator(@NotNull String columnName);

    CharacterColumnIterator characterColumnIterator(@NotNull String columnName);

    DoubleColumnIterator doubleColumnIterator(@NotNull String columnName);

    FloatColumnIterator floatColumnIterator(@NotNull String columnName);

    IntegerColumnIterator integerColumnIterator(@NotNull String columnName);

    LongColumnIterator longColumnIterator(@NotNull String columnName);

    ShortColumnIterator shortColumnIterator(@NotNull String columnName);

    // -----------------------------------------------------------------------------------------------------------------
    // Convenience data fetching; highly inefficient
    // -----------------------------------------------------------------------------------------------------------------

    Object[] getRecord(long rowNo, String... columnNames);

    // -----------------------------------------------------------------------------------------------------------------
    // Filter Operations
    // -----------------------------------------------------------------------------------------------------------------

    @AsyncMethod
    Table where(SelectFilter... filters);

    @AsyncMethod
    Table where(String... filters);

    @Override
    @AsyncMethod
    Table where(Collection<? extends Filter> filters);

    @AsyncMethod
    Table where();

    @AsyncMethod
    Table wouldMatch(String... expressions);

    /**
     * A table operation that applies the supplied predicate to each row in the table and produces columns containing
     * the pass/fail result of the predicate application. This is similar to {@link #where(String...)} except that
     * instead of selecting only rows that meet the criteria, new columns are added with the result of the comparison.
     *
     * @return a table with new columns containing the filter result for each row.
     */
    @AsyncMethod
    Table wouldMatch(WouldMatchPair... matchers);

    /**
     * Filters this table based on the set of values in the rightTable. Note that when the right table ticks, all of the
     * rows in the left table are going to be re-evaluated, thus the intention is that the right table is fairly slow
     * moving compared with the left table.
     *
     * @param rightTable the filtering table.
     * @param inclusion whether things included in rightTable should be passed through (they are exluded if false)
     * @param columnsToMatch the columns to match between the two tables
     * @return a new table filtered on right table
     */
    Table whereIn(GroupStrategy groupStrategy, Table rightTable, boolean inclusion, MatchPair... columnsToMatch);

    Table whereIn(Table rightTable, boolean inclusion, MatchPair... columnsToMatch);

    Table whereIn(Table rightTable, boolean inclusion, String... columnsToMatch);

    Table whereIn(Table rightTable, String... columnsToMatch);

    Table whereIn(Table rightTable, MatchPair... columnsToMatch);

    Table whereNotIn(Table rightTable, String... columnsToMatch);

    Table whereNotIn(Table rightTable, MatchPair... columnsToMatch);

    Table whereIn(GroupStrategy groupStrategy, Table rightTable, String... columnsToMatch);

    Table whereIn(GroupStrategy groupStrategy, Table rightTable, MatchPair... columnsToMatch);

    Table whereNotIn(GroupStrategy groupStrategy, Table rightTable, String... columnsToMatch);

    Table whereNotIn(GroupStrategy groupStrategy, Table rightTable, MatchPair... columnsToMatch);

    Table whereIn(GroupStrategy groupStrategy, Table rightTable, boolean inclusion, String... columnsToMatch);

    @Override
    Table whereIn(Table rightTable, Collection<? extends JoinMatch> columnsToMatch);

    @Override
    Table whereNotIn(Table rightTable, Collection<? extends JoinMatch> columnsToMatch);

    /**
     * Filters according to an expression in disjunctive normal form.
     * <p>
     * The input is an array of clauses, which in turn are a collection of filters.
     *
     * @param filtersToApply each inner collection is a set of filters, all of must which match for the clause to be
     *        true. If any one of the collections in the array evaluates to true, the row is part of the output table.
     * @return a new table, with the filters applied.
     */
    @SuppressWarnings("unchecked")
    @AsyncMethod
    Table whereOneOf(Collection<SelectFilter>... filtersToApply);

    /**
     * Applies the provided filters to the table disjunctively.
     *
     * @param filtersToApplyStrings an Array of filters to apply
     * @return a new table, with the filters applied
     */
    @AsyncMethod
    Table whereOneOf(String... filtersToApplyStrings);

    @AsyncMethod
    Table whereOneOf();

    /**
     * Get a {@link Table} that contains a sub-set of the rows from {@code this}.
     *
     * @param rowSet The {@link TrackingRowSet row set} for the result.
     * @return A new sub-table
     */
    Table getSubTable(TrackingRowSet rowSet);

    // -----------------------------------------------------------------------------------------------------------------
    // Column Selection Operations
    // -----------------------------------------------------------------------------------------------------------------

    Table select(SelectColumn... columns);

    Table select(String... columns);

    @Override
    Table select(Collection<? extends Selectable> columns);

    Table select();

    @AsyncMethod
    Table selectDistinct(SelectColumn... columns);

    @AsyncMethod
    Table selectDistinct(String... columns);

    @AsyncMethod
    Table selectDistinct(Collection<String> columns);

    @AsyncMethod
    Table selectDistinct();

    Table update(SelectColumn... newColumns);

    Table update(String... newColumns);

    @Override
    Table update(Collection<? extends Selectable> columns);

    /**
     * DO NOT USE -- this API is in flux and may change or disappear in the future.
     */
    SelectValidationResult validateSelect(String... columns);

    /**
     * DO NOT USE -- this API is in flux and may change or disappear in the future.
     */
    SelectValidationResult validateSelect(SelectColumn... columns);

    /**
     * Compute column formulas on demand.
     *
     * <p>
     * Lazy update defers computation until required for a set of values, and caches the results for a set of input
     * values. This uses less RAM than an update statement when you have a smaller set of unique values. Less
     * computation than an updateView is needed, because the results are saved in a cache.
     * </p>
     *
     * <p>
     * If you have many unique values, you should instead use an update statement, which will have more memory efficient
     * structures. Values are never removed from the lazyUpdate cache, so it should be used judiciously on a ticking
     * table.
     * </p>
     *
     * @param newColumns the columns to add
     * @return a new Table with the columns added; to be computed on demand
     */
    Table lazyUpdate(SelectColumn... newColumns);

    Table lazyUpdate(String... newColumns);

    Table lazyUpdate(Collection<String> newColumns);

    @AsyncMethod
    Table view(SelectColumn... columns);

    @AsyncMethod
    Table view(String... columns);

    @Override
    @AsyncMethod
    Table view(Collection<? extends Selectable> columns);

    @AsyncMethod
    Table updateView(SelectColumn... newColumns);

    @AsyncMethod
    Table updateView(String... newColumns);

    @Override
    @AsyncMethod
    Table updateView(Collection<? extends Selectable> columns);

    @AsyncMethod
    Table dropColumns(String... columnNames);

    @AsyncMethod
    Table dropColumnFormats();

    @AsyncMethod
    Table dropColumns(Collection<String> columnNames);

    Table renameColumns(MatchPair... pairs);

    Table renameColumns(String... columns);

    Table renameColumns(Collection<String> columns);

    Table renameAllColumns(UnaryOperator<String> renameFunction);

    @AsyncMethod
    Table formatColumns(String... columnFormats);

    @AsyncMethod
    Table formatRowWhere(String condition, String formula);

    @AsyncMethod
    Table formatColumnWhere(String columnName, String condition, String formula);

    /**
     * Produce a new table with the specified columns moved to the leftmost position. Columns can be renamed with the
     * usual syntax, i.e. {@code "NewColumnName=OldColumnName")}.
     *
     * @param columnsToMove The columns to move to the left (and, optionally, to rename)
     * @return The new table, with the columns rearranged as explained above {@link #moveColumns(int, String...)}
     */
    @AsyncMethod
    Table moveUpColumns(String... columnsToMove);

    /**
     * Produce a new table with the specified columns moved to the rightmost position. Columns can be renamed with the
     * usual syntax, i.e. {@code "NewColumnName=OldColumnName")}.
     *
     * @param columnsToMove The columns to move to the right (and, optionally, to rename)
     * @return The new table, with the columns rearranged as explained above {@link #moveColumns(int, String...)}
     */
    @AsyncMethod
    Table moveDownColumns(String... columnsToMove);

    /**
     * Produce a new table with the specified columns moved to the specified {@code rowSet}. Column indices begin at 0.
     * Columns can be renamed with the usual syntax, i.e. {@code "NewColumnName=OldColumnName")}.
     *
     * @param index The rowSet to which the specified columns should be moved
     * @param columnsToMove The columns to move to the specified rowSet (and, optionally, to rename)
     * @return The new table, with the columns rearranged as explained above
     */
    @AsyncMethod
    Table moveColumns(int index, String... columnsToMove);

    @AsyncMethod
    Table moveColumns(int index, boolean moveToEnd, String... columnsToMove);

    /**
     * Produce a new table with the same columns as this table, but with a new column presenting the specified DateTime
     * column as a Long column (with each DateTime represented instead as the corresponding number of nanos since the
     * epoch).
     * <p>
     * NOTE: This is a really just an updateView(), and behaves accordingly for column ordering and (re)placement. This
     * doesn't work on data that has been brought fully into memory (e.g. via select()). Use a view instead.
     *
     * @param dateTimeColumnName Name of date time column
     * @param nanosColumnName Name of nanos column
     * @return The new table, constructed as explained above.
     */
    @AsyncMethod
    Table dateTimeColumnAsNanos(String dateTimeColumnName, String nanosColumnName);

    /**
     * @param columnName name of column to convert from DateTime to nanos
     * @return The result of dateTimeColumnAsNanos(columnName, columnName).
     */
    @AsyncMethod
    Table dateTimeColumnAsNanos(String columnName);

    // -----------------------------------------------------------------------------------------------------------------
    // Slice Operations
    // -----------------------------------------------------------------------------------------------------------------

    @AsyncMethod
    Table head(long size);

    @AsyncMethod
    Table tail(long size);

    /**
     * Extracts a subset of a table by row position.
     * <p>
     * If both firstPosition and lastPosition are positive, then the rows are counted from the beginning of the table.
     * The firstPosition is inclusive, and the lastPosition is exclusive. The {@link #head}(N) call is equivalent to
     * slice(0, N). The firstPosition must be less than or equal to the lastPosition.
     * <p>
     * If firstPosition is positive and lastPosition is negative, then the firstRow is counted from the beginning of the
     * table, inclusively. The lastPosition is counted from the end of the table. For example, slice(1, -1) includes all
     * rows but the first and last. If the lastPosition would be before the firstRow, the result is an emptyTable.
     * <p>
     * If firstPosition is negative, and lastPosition is zero, then the firstRow is counted from the end of the table,
     * and the end of the slice is the size of the table. slice(-N, 0) is equivalent to {@link #tail}(N).
     * <p>
     * If the firstPosition is nega tive and the lastPosition is negative, they are both counted from the end of the
     * table. For example, slice(-2, -1) returns the second to last row of the table.
     *
     * @param firstPositionInclusive the first position to include in the result
     * @param lastPositionExclusive the last position to include in the result
     * @return a new Table, which is the request subset of rows from the original table
     */
    @AsyncMethod
    Table slice(long firstPositionInclusive, long lastPositionExclusive);

    /**
     * Provides a head that selects a dynamic number of rows based on a percent.
     *
     * @param percent the fraction of the table to return (0..1), the number of rows will be rounded up. For example if
     *        there are 3 rows, headPct(50) returns the first two rows.
     */
    @AsyncMethod
    Table headPct(double percent);

    @AsyncMethod
    Table tailPct(double percent);

    // -----------------------------------------------------------------------------------------------------------------
    // GroupingStrategy used for various join and aggregation operations
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * GroupStrategy is used for joins and aggregation operations that can choose one of several ways to make use of
     * grouping information.
     */
    enum GroupStrategy {
        DEFAULT, LINEAR, USE_EXISTING_GROUPS, CREATE_GROUPS,
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Join Operations
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a table that has one column for each original table's columns, and one column corresponding to each of
     * the input table (right table) columns listed in the columns to add (or all the columns whose names don't overlap
     * with the name of a column from the source table if the columnsToAdd is length zero). The new columns (those
     * corresponding to the input table) contain an aggregation of all values from the left side that match the join
     * criteria. Consequently, the types of all right side columns not involved in a join criteria, is an array of the
     * original column type. If the two tables have columns with matching names then the method will fail with an
     * exception unless the columns with corresponding names are found in one of the matching criteria.
     * <p>
     * <p>
     * NOTE: leftJoin operation does not involve an actual data copy, or an in-memory table creation. In order to
     * produce an actual in memory table you need to apply a select call on the join result.
     *
     * @param rightTable input table
     * @param columnsToMatch match criteria
     * @param columnsToAdd columns to add
     * @return a table that has one column for each original table's columns, and one column corresponding to each
     *         column listed in columnsToAdd. If columnsToAdd.length==0 one column corresponding to each column of the
     *         input table (right table) columns whose names don't overlap with the name of a column from the source
     *         table is added. The new columns (those corresponding to the input table) contain an aggregation of all
     *         values from the left side that match the join criteria.
     */
    Table leftJoin(Table rightTable, MatchPair[] columnsToMatch, MatchPair[] columnsToAdd);

    Table leftJoin(Table rightTable, Collection<? extends JoinMatch> columnsToMatch,
            Collection<? extends JoinAddition> columnsToAdd);

    Table leftJoin(Table rightTable, Collection<String> columnsToMatch);

    Table leftJoin(Table rightTable, String columnsToMatch, String columnsToAdd);

    Table leftJoin(Table rightTable, String columnsToMatch);

    Table leftJoin(Table rightTable);

    Table exactJoin(Table rightTable, MatchPair[] columnsToMatch, MatchPair[] columnsToAdd);

    @Override
    Table exactJoin(Table rightTable, Collection<? extends JoinMatch> columnsToMatch,
            Collection<? extends JoinAddition> columnsToAdd);

    Table exactJoin(Table rightTable, String columnsToMatch, String columnsToAdd);

    Table exactJoin(Table rightTable, String columnsToMatch);

    /**
     * Rules for the inexact matching performed on the final column to match by in {@link #aj} and {@link #raj}.
     */
    enum AsOfMatchRule {
        LESS_THAN_EQUAL, LESS_THAN, GREATER_THAN_EQUAL, GREATER_THAN;

        public static AsOfMatchRule of(AsOfJoinRule rule) {
            switch (rule) {
                case LESS_THAN_EQUAL:
                    return Table.AsOfMatchRule.LESS_THAN_EQUAL;
                case LESS_THAN:
                    return Table.AsOfMatchRule.LESS_THAN;
            }
            throw new IllegalStateException("Unexpected rule " + rule);
        }

        public static AsOfMatchRule of(ReverseAsOfJoinRule rule) {
            switch (rule) {
                case GREATER_THAN_EQUAL:
                    return Table.AsOfMatchRule.GREATER_THAN_EQUAL;
                case GREATER_THAN:
                    return Table.AsOfMatchRule.GREATER_THAN;
            }
            throw new IllegalStateException("Unexpected rule " + rule);
        }
    }

    /**
     * Looks up the columns in the rightTable that meet the match conditions in the columnsToMatch list. Matching is
     * done exactly for the first n-1 columns and via a binary search for the last match pair. The columns of the
     * original table are returned intact, together with the columns from rightTable defined in a comma separated list
     * "columnsToAdd"
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch A comma separated list of match conditions ("leftColumn=rightColumn" or
     *        "columnFoundInBoth")
     * @param columnsToAdd A comma separated list with the columns from the left side that need to be added to the right
     *        side as a result of the match.
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table aj(Table rightTable, MatchPair[] columnsToMatch, MatchPair[] columnsToAdd, AsOfMatchRule asOfMatchRule);

    /**
     * Looks up the columns in the rightTable that meet the match conditions in the columnsToMatch list. Matching is
     * done exactly for the first n-1 columns and via a binary search for the last match pair. The columns of the
     * original table are returned intact, together with the columns from rightTable defined in a comma separated list
     * "columnsToAdd"
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch A comma separated list of match conditions ("leftColumn=rightColumn" or
     *        "columnFoundInBoth")
     * @param columnsToAdd A comma separated list with the columns from the left side that need to be added to the right
     *        side as a result of the match.
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table aj(Table rightTable, MatchPair[] columnsToMatch, MatchPair[] columnsToAdd);

    Table aj(Table rightTable, Collection<? extends JoinMatch> columnsToMatch,
            Collection<? extends JoinAddition> columnsToAdd);

    Table aj(Table rightTable, Collection<? extends JoinMatch> columnsToMatch,
            Collection<? extends JoinAddition> columnsToAdd, AsOfJoinRule asOfJoinRule);

    /**
     * Looks up the columns in the rightTable that meet the match conditions in the columnsToMatch list. Matching is
     * done exactly for the first n-1 columns and via a binary search for the last match pair. The columns of the
     * original table are returned intact, together with all the columns from rightTable.
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch A comma separated list of match conditions ("leftColumn=rightColumn" or
     *        "columnFoundInBoth")
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table aj(Table rightTable, Collection<String> columnsToMatch);

    Table aj(Table rightTable, String columnsToMatch, String columnsToAdd);

    Table aj(Table rightTable, String columnsToMatch);

    /**
     * Just like .aj(), but the matching on the last column is in reverse order, so that you find the row after the
     * given timestamp instead of the row before.
     * <p>
     * Looks up the columns in the rightTable that meet the match conditions in the columnsToMatch list. Matching is
     * done exactly for the first n-1 columns and via a binary search for the last match pair. The columns of the
     * original table are returned intact, together with the columns from rightTable defined in a comma separated list
     * "columnsToAdd"
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch A comma separated list of match conditions ("leftColumn=rightColumn" or
     *        "columnFoundInBoth")
     * @param columnsToAdd A comma separated list with the columns from the left side that need to be added to the right
     *        side as a result of the match.
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table raj(Table rightTable, MatchPair[] columnsToMatch, MatchPair[] columnsToAdd, AsOfMatchRule asOfMatchRule);

    /**
     * Just like .aj(), but the matching on the last column is in reverse order, so that you find the row after the
     * given timestamp instead of the row before.
     * <p>
     * Looks up the columns in the rightTable that meet the match conditions in the columnsToMatch list. Matching is
     * done exactly for the first n-1 columns and via a binary search for the last match pair. The columns of the
     * original table are returned intact, together with the columns from rightTable defined in a comma separated list
     * "columnsToAdd"
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch A comma separated list of match conditions ("leftColumn=rightColumn" or
     *        "columnFoundInBoth")
     * @param columnsToAdd A comma separated list with the columns from the left side that need to be added to the right
     *        side as a result of the match.
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table raj(Table rightTable, MatchPair[] columnsToMatch, MatchPair[] columnsToAdd);

    Table raj(Table rightTable, Collection<? extends JoinMatch> columnsToMatch,
            Collection<? extends JoinAddition> columnsToAdd);

    Table raj(Table rightTable, Collection<? extends JoinMatch> columnsToMatch,
            Collection<? extends JoinAddition> columnsToAdd, ReverseAsOfJoinRule reverseAsOfJoinRule);

    /**
     * Just like .aj(), but the matching on the last column is in reverse order, so that you find the row after the
     * given timestamp instead of the row before.
     * <p>
     * Looks up the columns in the rightTable that meet the match conditions in the columnsToMatch list. Matching is
     * done exactly for the first n-1 columns and via a binary search for the last match pair. The columns of the
     * original table are returned intact, together with the all columns from rightTable.
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch A comma separated list of match conditions ("leftColumn=rightColumn" or
     *        "columnFoundInBoth")
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table raj(Table rightTable, Collection<String> columnsToMatch);

    /**
     * Just like .aj(), but the matching on the last column is in reverse order, so that you find the row after the
     * given timestamp instead of the row before.
     * <p>
     * Looks up the columns in the rightTable that meet the match conditions in the columnsToMatch list. Matching is
     * done exactly for the first n-1 columns and via a binary search for the last match pair. The columns of the
     * original table are returned intact, together with the columns from rightTable defined in a comma separated list
     * "columnsToAdd"
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch A comma separated list of match conditions ("leftColumn=rightColumn" or
     *        "columnFoundInBoth")
     * @param columnsToAdd A comma separated list with the columns from the left side that need to be added to the right
     *        side as a result of the match.
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table raj(Table rightTable, String columnsToMatch, String columnsToAdd);

    Table raj(Table rightTable, String columnsToMatch);

    Table naturalJoin(Table rightTable, MatchPair[] columnsToMatch, MatchPair[] columnsToAdd);

    @Override
    Table naturalJoin(Table rightTable, Collection<? extends JoinMatch> columnsToMatch,
            Collection<? extends JoinAddition> columnsToAdd);

    Table naturalJoin(Table rightTable, String columnsToMatch, String columnsToAdd);

    Table naturalJoin(Table rightTable, String columnsToMatch);

    /**
     * Perform a cross join with the right table.
     * <p>
     * Returns a table that is the cartesian product of left rows X right rows, with one column for each of the left
     * table's columns, and one column corresponding to each of the right table's columns. The rows are ordered first by
     * the left table then by the right table.
     * <p>
     * To efficiently produce updates, the bits that represent a key for a given row are split into two. Unless
     * specified, join reserves 16 bits to represent a right row. When there are too few bits to represent all of the
     * right rows for a given aggregation group the table will shift a bit from the left side to the right side. The
     * default of 16 bits was carefully chosen because it results in an efficient implementation to process live
     * updates.
     * <p>
     * An {@link io.deephaven.engine.v2.utils.OutOfKeySpaceException} is thrown when the total number of bits needed to
     * express the result table exceeds that needed to represent Long.MAX_VALUE. There are a few work arounds: - If the
     * left table is sparse, consider flattening the left table. - If there are no key-columns and the right table is
     * sparse, consider flattening the right table. - If the maximum size of a right table's group is small, you can
     * reserve fewer bits by setting numRightBitsToReserve on initialization.
     * <p>
     * Note: If you can prove that a given group has at most one right-row then you should prefer using
     * {@link #naturalJoin}.
     *
     * @param rightTable The right side table on the join.
     * @return a new table joined according to the specification with zero key-columns and includes all right columns
     */
    Table join(Table rightTable);

    /**
     * Perform a cross join with the right table.
     * <p>
     * Returns a table that is the cartesian product of left rows X right rows, with one column for each of the left
     * table's columns, and one column corresponding to each of the right table's columns. The rows are ordered first by
     * the left table then by the right table.
     * <p>
     * To efficiently produce updates, the bits that represent a key for a given row are split into two. Unless
     * specified, join reserves 16 bits to represent a right row. When there are too few bits to represent all of the
     * right rows for a given aggregation group the table will shift a bit from the left side to the right side. The
     * default of 16 bits was carefully chosen because it results in an efficient implementation to process live
     * updates.
     * <p>
     * An {@link io.deephaven.engine.v2.utils.OutOfKeySpaceException} is thrown when the total number of bits needed to
     * express the result table exceeds that needed to represent Long.MAX_VALUE. There are a few work arounds: - If the
     * left table is sparse, consider flattening the left table. - If there are no key-columns and the right table is
     * sparse, consider flattening the right table. - If the maximum size of a right table's group is small, you can
     * reserve fewer bits by setting numRightBitsToReserve on initialization.
     * <p>
     * Note: If you can prove that a given group has at most one right-row then you should prefer using
     * {@link #naturalJoin}.
     *
     * @param rightTable The right side table on the join.
     * @param numRightBitsToReserve The number of bits to reserve for rightTable groups.
     * @return a new table joined according to the specification with zero key-columns and includes all right columns
     */
    Table join(Table rightTable, int numRightBitsToReserve);

    Table join(Table rightTable, String columnsToMatch);

    /**
     * Perform a cross join with the right table.
     * <p>
     * Returns a table that is the cartesian product of left rows X right rows, with one column for each of the left
     * table's columns, and one column corresponding to each of the right table's columns that are not key-columns. The
     * rows are ordered first by the left table then by the right table. If columnsToMatch is non-empty then the product
     * is filtered by the supplied match conditions.
     * <p>
     * To efficiently produce updates, the bits that represent a key for a given row are split into two. Unless
     * specified, join reserves 16 bits to represent a right row. When there are too few bits to represent all of the
     * right rows for a given aggregation group the table will shift a bit from the left side to the right side. The
     * default of 16 bits was carefully chosen because it results in an efficient implementation to process live
     * updates.
     * <p>
     * An {@link io.deephaven.engine.v2.utils.OutOfKeySpaceException} is thrown when the total number of bits needed to
     * express the result table exceeds that needed to represent Long.MAX_VALUE. There are a few work arounds: - If the
     * left table is sparse, consider flattening the left table. - If there are no key-columns and the right table is
     * sparse, consider flattening the right table. - If the maximum size of a right table's group is small, you can
     * reserve fewer bits by setting numRightBitsToReserve on initialization.
     * <p>
     * Note: If you can prove that a given group has at most one right-row then you should prefer using
     * {@link #naturalJoin}.
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch A comma separated list of match conditions ("leftColumn=rightColumn" or
     *        "columnFoundInBoth")
     * @param numRightBitsToReserve The number of bits to reserve for rightTable groups.
     * @return a new table joined according to the specification in columnsToMatch and includes all non-key-columns from
     *         the right table
     */
    Table join(Table rightTable, String columnsToMatch, int numRightBitsToReserve);

    Table join(Table rightTable, String columnsToMatch, String columnsToAdd);

    /**
     * Perform a cross join with the right table.
     * <p>
     * Returns a table that is the cartesian product of left rows X right rows, with one column for each of the left
     * table's columns, and one column corresponding to each of the right table's columns that are included in the
     * columnsToAdd argument. The rows are ordered first by the left table then by the right table. If columnsToMatch is
     * non-empty then the product is filtered by the supplied match conditions.
     * <p>
     * To efficiently produce updates, the bits that represent a key for a given row are split into two. Unless
     * specified, join reserves 16 bits to represent a right row. When there are too few bits to represent all of the
     * right rows for a given aggregation group the table will shift a bit from the left side to the right side. The
     * default of 16 bits was carefully chosen because it results in an efficient implementation to process live
     * updates.
     * <p>
     * An {@link io.deephaven.engine.v2.utils.OutOfKeySpaceException} is thrown when the total number of bits needed to
     * express the result table exceeds that needed to represent Long.MAX_VALUE. There are a few work arounds: - If the
     * left table is sparse, consider flattening the left table. - If there are no key-columns and the right table is
     * sparse, consider flattening the right table. - If the maximum size of a right table's group is small, you can
     * reserve fewer bits by setting numRightBitsToReserve on initialization.
     * <p>
     * Note: If you can prove that a given group has at most one right-row then you should prefer using
     * {@link #naturalJoin}.
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch A comma separated list of match conditions ("leftColumn=rightColumn" or
     *        "columnFoundInBoth")
     * @param columnsToAdd A comma separated list with the columns from the right side that need to be added to the left
     *        side as a result of the match.
     * @param numRightBitsToReserve The number of bits to reserve for rightTable groups.
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table join(Table rightTable, String columnsToMatch, String columnsToAdd, int numRightBitsToReserve);

    /**
     * Perform a cross join with the right table.
     * <p>
     * Returns a table that is the cartesian product of left rows X right rows, with one column for each of the left
     * table's columns, and one column corresponding to each of the right table's columns that are included in the
     * columnsToAdd argument. The rows are ordered first by the left table then by the right table. If columnsToMatch is
     * non-empty then the product is filtered by the supplied match conditions.
     * <p>
     * To efficiently produce updates, the bits that represent a key for a given row are split into two. Unless
     * specified, join reserves 16 bits to represent a right row. When there are too few bits to represent all of the
     * right rows for a given aggregation group the table will shift a bit from the left side to the right side. The
     * default of 16 bits was carefully chosen because it results in an efficient implementation to process live
     * updates.
     * <p>
     * An {@link io.deephaven.engine.v2.utils.OutOfKeySpaceException} is thrown when the total number of bits needed to
     * express the result table exceeds that needed to represent Long.MAX_VALUE. There are a few work arounds: - If the
     * left table is sparse, consider flattening the left table. - If there are no key-columns and the right table is
     * sparse, consider flattening the right table. - If the maximum size of a right table's group is small, you can
     * reserve fewer bits by setting numRightBitsToReserve on initialization.
     * <p>
     * Note: If you can prove that a given group has at most one right-row then you should prefer using
     * {@link #naturalJoin}.
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch An array of match pair conditions ("leftColumn=rightColumn" or "columnFoundInBoth")
     * @param columnsToAdd An array of the columns from the right side that need to be added to the left side as a
     *        result of the match.
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table join(Table rightTable, MatchPair[] columnsToMatch, MatchPair[] columnsToAdd);

    /**
     * Perform a cross join with the right table.
     * <p>
     * Returns a table that is the cartesian product of left rows X right rows, with one column for each of the left
     * table's columns, and one column corresponding to each of the right table's columns that are included in the
     * columnsToAdd argument. The rows are ordered first by the left table then by the right table. If columnsToMatch is
     * non-empty then the product is filtered by the supplied match conditions.
     * <p>
     * To efficiently produce updates, the bits that represent a key for a given row are split into two. Unless
     * specified, join reserves 16 bits to represent a right row. When there are too few bits to represent all of the
     * right rows for a given aggregation group the table will shift a bit from the left side to the right side. The
     * default of 16 bits was carefully chosen because it results in an efficient implementation to process live
     * updates.
     * <p>
     * An {@link io.deephaven.engine.v2.utils.OutOfKeySpaceException} is thrown when the total number of bits needed to
     * express the result table exceeds that needed to represent Long.MAX_VALUE. There are a few work arounds: - If the
     * left table is sparse, consider flattening the left table. - If there are no key-columns and the right table is
     * sparse, consider flattening the right table. - If the maximum size of a right table's group is small, you can
     * reserve fewer bits by setting numRightBitsToReserve on initialization.
     * <p>
     * Note: If you can prove that a given group has at most one right-row then you should prefer using
     * {@link #naturalJoin}.
     *
     * @param rightTable The right side table on the join.
     * @param columnsToMatch An array of match pair conditions ("leftColumn=rightColumn" or "columnFoundInBoth")
     * @param columnsToAdd An array of the columns from the right side that need to be added to the left side as a
     *        result of the match.
     * @param numRightBitsToReserve The number of bits to reserve for rightTable groups.
     * @return a new table joined according to the specification in columnsToMatch and columnsToAdd
     */
    Table join(Table rightTable, MatchPair[] columnsToMatch, MatchPair[] columnsToAdd, int numRightBitsToReserve);

    @Override
    Table join(Table rightTable, Collection<? extends JoinMatch> columnsToMatch,
            Collection<? extends JoinAddition> columnsToAdd);

    @Override
    Table join(Table rightTable, Collection<? extends JoinMatch> columnsToMatch,
            Collection<? extends JoinAddition> columnsToAdd, int numRightBitsToReserve);

    // -----------------------------------------------------------------------------------------------------------------
    // Aggregation Operations
    // -----------------------------------------------------------------------------------------------------------------

    @AsyncMethod
    Table by(AggregationStateFactory aggregationStateFactory, SelectColumn... groupByColumns);

    @AsyncMethod
    Table by(AggregationStateFactory aggregationStateFactory, String... groupByColumns);

    @AsyncMethod
    Table by(AggregationStateFactory aggregationStateFactory);

    @AsyncMethod
    Table by(SelectColumn... groupByColumns);

    @AsyncMethod
    Table by(String... groupByColumns);

    @AsyncMethod
    Table by();

    @Override
    @AsyncMethod
    Table by(Collection<? extends Selectable> groupByColumns);

    @Override
    @AsyncMethod
    Table by(Collection<? extends Selectable> groupByColumns, Collection<? extends Aggregation> aggregations);

    Table headBy(long nRows, SelectColumn... groupByColumns);

    Table headBy(long nRows, String... groupByColumns);

    Table headBy(long nRows, Collection<String> groupByColumns);

    Table tailBy(long nRows, SelectColumn... groupByColumns);

    Table tailBy(long nRows, String... groupByColumns);

    Table tailBy(long nRows, Collection<String> groupByColumns);

    /**
     * Groups data according to groupByColumns and applies formulaColumn to each of columns not altered by the grouping
     * operation. <code>columnParamName</code> is used as place-holder for the name of each column inside
     * <code>formulaColumn</code>.
     *
     * @param formulaColumn Formula applied to each column
     * @param columnParamName The parameter name used as a placeholder for each column
     * @param groupByColumns The grouping columns {@link Table#by(SelectColumn[])}
     */
    @AsyncMethod
    Table applyToAllBy(String formulaColumn, String columnParamName, SelectColumn... groupByColumns);

    /**
     * Groups data according to groupByColumns and applies formulaColumn to each of columns not altered by the grouping
     * operation.
     *
     * @param formulaColumn Formula applied to each column, uses parameter <i>each</i> to refer to each colum it being
     *        applied to
     * @param groupByColumns The grouping columns {@link Table#by(SelectColumn...)}
     */
    @AsyncMethod
    Table applyToAllBy(String formulaColumn, SelectColumn... groupByColumns);

    /**
     * Groups data according to groupByColumns and applies formulaColumn to each of columns not altered by the grouping
     * operation.
     *
     * @param formulaColumn Formula applied to each column, uses parameter <i>each</i> to refer to each colum it being
     *        applied to
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table applyToAllBy(String formulaColumn, String... groupByColumns);

    @AsyncMethod
    Table applyToAllBy(String formulaColumn, String groupByColumn);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the sum for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table sumBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the sum for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table sumBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the sum for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table sumBy(Collection<String> groupByColumns);

    /**
     * Produces a single row table with the sum of each column.
     * <p>
     * When the input table is empty, zero output rows are produced.
     */
    @AsyncMethod
    Table sumBy();

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the sum of the absolute values for
     * the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table absSumBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the sum of the absolute values for
     * the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table absSumBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the sum of the absolute values for
     * the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table absSumBy(Collection<String> groupByColumns);

    /**
     * Produces a single row table with the absolute sum of each column.
     * <p>
     * When the input table is empty, zero output rows are produced.
     */
    @AsyncMethod
    Table absSumBy();

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the average for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table avgBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the average for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table avgBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the average for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table avgBy(Collection<String> groupByColumns);

    /**
     * Produces a single row table with the average of each column.
     * <p>
     * When the input table is empty, zero output rows are produced.
     */
    @AsyncMethod
    Table avgBy();

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the weighted average using
     * weightColumn for the rest of the fields
     *
     * @param weightColumn the column to use for the weight
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table wavgBy(String weightColumn, SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the weighted average using
     * weightColumn for the rest of the fields
     *
     * @param weightColumn the column to use for the weight
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table wavgBy(String weightColumn, String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the weighted average using
     * weightColumn for the rest of the fields
     *
     * @param weightColumn the column to use for the weight
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table wavgBy(String weightColumn, Collection<String> groupByColumns);

    /**
     * Produces a single row table with the weighted average using weightColumn for the rest of the fields
     * <p>
     * When the input table is empty, zero output rows are produced.
     *
     * @param weightColumn the column to use for the weight
     */
    @AsyncMethod
    Table wavgBy(String weightColumn);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the weighted sum using weightColumn
     * for the rest of the fields
     * <p>
     * If the weight column is a floating point type, all result columns will be doubles. If the weight column is an
     * integral type, all integral input columns will have long results and all floating point input columns will have
     * double results.
     *
     * @param weightColumn the column to use for the weight
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table wsumBy(String weightColumn, SelectColumn... groupByColumns);

    /**
     * Computes the weighted sum for all rows in the table using weightColumn for the rest of the fields
     * <p>
     * If the weight column is a floating point type, all result columns will be doubles. If the weight column is an
     * integral type, all integral input columns will have long results and all floating point input columns will have
     * double results.
     *
     * @param weightColumn the column to use for the weight
     */
    @AsyncMethod
    Table wsumBy(String weightColumn);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the weighted sum using weightColumn
     * for the rest of the fields
     * <p>
     * If the weight column is a floating point type, all result columns will be doubles. If the weight column is an
     * integral type, all integral input columns will have long results and all floating point input columns will have
     * double results.
     *
     * @param weightColumn the column to use for the weight
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table wsumBy(String weightColumn, String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the weighted sum using weightColumn
     * for the rest of the fields
     * <p>
     * If the weight column is a floating point type, all result columns will be doubles. If the weight column is an
     * integral type, all integral input columns will have long results and all floating point input columns will have
     * double results.
     *
     * @param weightColumn the column to use for the weight
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table wsumBy(String weightColumn, Collection<String> groupByColumns);

    @AsyncMethod
    Table stdBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the standard deviation for the rest
     * of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table stdBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the standard deviation for the rest
     * of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table stdBy(Collection<String> groupByColumns);

    /**
     * Produces a single row table with the standard deviation of each column.
     * <p>
     * When the input table is empty, zero output rows are produced.
     */
    Table stdBy();

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the variance for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table varBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the variance for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table varBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the variance for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table varBy(Collection<String> groupByColumns);

    /**
     * Produces a single row table with the variance of each column.
     * <p>
     * When the input table is empty, zero output rows are produced.
     */
    Table varBy();

    /**
     * Groups the data column according to <code>groupByColumns</code> and retrieves the last for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table lastBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and retrieves the last for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table lastBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and retrieves the last for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table lastBy(Collection<String> groupByColumns);

    /**
     * Returns the last row of the given table.
     */
    @AsyncMethod
    Table lastBy();

    /**
     * Groups the data column according to <code>groupByColumns</code> and retrieves the first for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table firstBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and retrieves the first for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table firstBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and retrieves the first for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table firstBy(Collection<String> groupByColumns);

    /**
     * Returns the first row of the given table.
     */
    @AsyncMethod
    Table firstBy();

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the min for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table minBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the min for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table minBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the min for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)}
     */
    @AsyncMethod
    Table minBy(Collection<String> groupByColumns);

    /**
     * Produces a single row table with the minimum of each column.
     * <p>
     * When the input table is empty, zero output rows are produced.
     */
    @AsyncMethod
    Table minBy();

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the max for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)} }
     */
    @AsyncMethod
    Table maxBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the max for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)} }
     */
    @AsyncMethod
    Table maxBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the max for the rest of the fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)} }
     */
    @AsyncMethod
    Table maxBy(Collection<String> groupByColumns);

    /**
     * Produces a single row table with the maximum of each column.
     * <p>
     * When the input table is empty, zero output rows are produced.
     */
    @AsyncMethod
    Table maxBy();

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the median for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)} }
     */
    @AsyncMethod
    Table medianBy(SelectColumn... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the median for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)} }
     */
    @AsyncMethod
    Table medianBy(String... groupByColumns);

    /**
     * Groups the data column according to <code>groupByColumns</code> and computes the median for the rest of the
     * fields
     *
     * @param groupByColumns The grouping columns {@link Table#by(String...)} }
     */
    @AsyncMethod
    Table medianBy(Collection<String> groupByColumns);

    /**
     * Produces a single row table with the median of each column.
     * <p>
     * When the input table is empty, zero output rows are produced.
     */
    @AsyncMethod
    Table medianBy();

    @AsyncMethod
    Table countBy(String countColumnName, SelectColumn... groupByColumns);

    @AsyncMethod
    Table countBy(String countColumnName, String... groupByColumns);

    @AsyncMethod
    Table countBy(String countColumnName, Collection<String> groupByColumns);

    @AsyncMethod
    Table countBy(String countColumnName);

    /**
     * If this table is a stream table, i.e. it has {@link #STREAM_TABLE_ATTRIBUTE} set to {@code true}, return a child
     * without the attribute, restoring standard semantics for aggregation operations.
     *
     * @return A non-stream child table, or this table if it is not a stream table
     */
    @AsyncMethod
    Table dropStream();

    // -----------------------------------------------------------------------------------------------------------------
    // Disaggregation Operations
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Ungroups a table by converting arrays into columns.
     *
     * @param nullFill indicates if the ungrouped table should allow disparate sized arrays filling shorter columns with
     *        null values. If set to false, then all arrays should be the same length.
     * @param columnsToUngroup the columns to ungroup
     * @return the ungrouped table
     */
    Table ungroup(boolean nullFill, String... columnsToUngroup);

    Table ungroup(String... columnsToUngroup);

    Table ungroupAllBut(String... columnsNotToUngroup);

    Table ungroup();

    Table ungroup(boolean nullFill);

    // -----------------------------------------------------------------------------------------------------------------
    // ByExternal Operations
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Create a {@link TableMap} from this table, keyed by the specified columns.
     *
     * <p>
     * The returned TableMap contains each row in this table in exactly one of the tables within the map. If you have
     * exactly one key column the TableMap is keyed by the value in that column. If you have zero key columns, then the
     * TableMap is keyed by {@code io.deephaven.datastructures.util.SmartKey.EMPTY} (and will contain this table as the
     * value). If you have multiple key columns, then the TableMap is keyed by a
     * {@code io.deephaven.datastructures.util.SmartKey}. The SmartKey will have one value for each of your column
     * values, in the order specified by keyColumnNames.
     * </p>
     *
     * <p>
     * For example if you have a Table keyed by a String column named USym, and a DateTime column named Expiry; a value
     * could be retrieved from the TableMap with
     * {@code tableMap.get(new SmartKey("SPY";, DateTimeUtils.convertDateTime("2020-06-19T16:15:00 NY")))}. For a table
     * with an Integer column named Bucket, you simply use the desired value as in {@code tableMap.get(1)}.
     * </p>
     *
     * @param dropKeys if true, drop key columns in the output Tables
     * @param keyColumnNames the name of the key columns to use.
     * @return a TableMap keyed by keyColumnNames
     */
    @AsyncMethod
    TableMap byExternal(boolean dropKeys, String... keyColumnNames);

    /**
     * Create a {@link TableMap} from this table, keyed by the specified columns.
     *
     * <p>
     * The returned TableMap contains each row in this table in exactly one of the tables within the map. If you have
     * exactly one key column the TableMap is keyed by the value in that column. If you have zero key columns, then the
     * TableMap is keyed by {@code io.deephaven.datastructures.util.SmartKey.EMPTY} (and will contain this table as the
     * value). If you have multiple key columns, then the TableMap is keyed by a
     * {@code io.deephaven.datastructures.util.SmartKey}. The SmartKey will have one value for each of your column
     * values, in the order specified by keyColumnNames.
     * </p>
     *
     * <p>
     * For example if you have a Table keyed by a String column named USym, and a DateTime column named Expiry; a value
     * could be retrieved from the TableMap with
     * {@code tableMap.get(new SmartKey("SPY";, DateTimeUtils.convertDateTime("2020-06-19T16:15:00 NY")))}. For a table
     * with an Integer column named Bucket, you simply use the desired value as in {@code tableMap.get(1)}.
     * </p>
     *
     * @param keyColumnNames the name of the key columns to use.
     * @return a TableMap keyed by keyColumnNames
     */
    @AsyncMethod
    TableMap byExternal(String... keyColumnNames);

    // -----------------------------------------------------------------------------------------------------------------
    // Hierarchical table operations (rollup and treeTable).
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Create a rollup table.
     * <p>
     * A rollup table aggregates by the specified columns, and then creates a hierarchical table which re-aggregates
     * using one less aggregation column on each level. The column that is no longer part of the aggregation key is
     * replaced with null on each level.
     *
     * @param comboAggregateFactory the ComboAggregateFactory describing the aggregation
     * @param columns the columns to group by
     * @return a hierarchical table with the rollup applied
     */
    @AsyncMethod
    Table rollup(ComboAggregateFactory comboAggregateFactory, Collection<String> columns);

    /**
     * Create a rollup table.
     * <p>
     * A rollup table aggregates by the specified columns, and then creates a hierarchical table which re-aggregates
     * using one less aggregation column on each level. The column that is no longer part of the aggregation key is
     * replaced with null on each level.
     *
     * @param comboAggregateFactory the ComboAggregateFactory describing the aggregation
     * @param includeConstituents set to true to include the constituent rows at the leaf level
     * @param columns the columns to group by
     * @return a hierarchical table with the rollup applied
     */
    @AsyncMethod
    Table rollup(ComboAggregateFactory comboAggregateFactory, boolean includeConstituents,
            Collection<String> columns);

    /**
     * Create a rollup table.
     * <p>
     * A rollup table aggregates by the specified columns, and then creates a hierarchical table which re-aggregates
     * using one less aggregation column on each level. The column that is no longer part of the aggregation key is
     * replaced with null on each level.
     *
     * @param comboAggregateFactory the ComboAggregateFactory describing the aggregation
     * @param columns the columns to group by
     * @return a hierarchical table with the rollup applied
     */
    @AsyncMethod
    Table rollup(ComboAggregateFactory comboAggregateFactory, String... columns);

    /**
     * Create a rollup table.
     * <p>
     * A rollup table aggregates by the specified columns, and then creates a hierarchical table which re-aggregates
     * using one less aggregation column on each level. The column that is no longer part of the aggregation key is
     * replaced with null on each level.
     *
     * @param comboAggregateFactory the ComboAggregateFactory describing the aggregation
     * @param columns the columns to group by
     * @param includeConstituents set to true to include the constituent rows at the leaf level
     * @return a hierarchical table with the rollup applied
     */
    @AsyncMethod
    Table rollup(ComboAggregateFactory comboAggregateFactory, boolean includeConstituents, String... columns);

    /**
     * Create a rollup table.
     * <p>
     * A rollup table aggregates by the specified columns, and then creates a hierarchical table which re-aggregates
     * using one less aggregation column on each level. The column that is no longer part of the aggregation key is
     * replaced with null on each level.
     *
     * @param comboAggregateFactory the ComboAggregateFactory describing the aggregation
     * @param columns the columns to group by
     * @return a hierarchical table with the rollup applied
     */
    @AsyncMethod
    Table rollup(ComboAggregateFactory comboAggregateFactory, SelectColumn... columns);

    /**
     * Create a rollup table.
     * <p>
     * A rollup table aggregates all rows of the table.
     *
     * @param comboAggregateFactory the ComboAggregateFactory describing the aggregation
     * @return a hierarchical table with the rollup applied
     */
    @AsyncMethod
    Table rollup(ComboAggregateFactory comboAggregateFactory);

    /**
     * Create a rollup table.
     * <p>
     * A rollup table aggregates all rows of the table.
     *
     * @param comboAggregateFactory the ComboAggregateFactory describing the aggregation
     * @param includeConstituents set to true to include the constituent rows at the leaf level
     * @return a hierarchical table with the rollup applied
     */
    @AsyncMethod
    Table rollup(ComboAggregateFactory comboAggregateFactory, boolean includeConstituents);

    @AsyncMethod
    Table rollup(ComboAggregateFactory comboAggregateFactory, boolean includeConstituents, SelectColumn... columns);

    /**
     * Create a hierarchical tree table.
     * <p>
     * The structure of the table is encoded by an "id" and a "parent" column. The id column should represent a unique
     * identifier for a given row, and the parent column indicates which row is the parent for a given row. Rows that
     * have a null parent, are shown in the main table. It is possible for rows to be "orphaned", if their parent
     * reference is non-null and does not exist in the table.
     *
     * @param idColumn the name of a column containing a unique identifier for a particular row in the table
     * @param parentColumn the name of a column containing the parent's identifier, null for elements that are part of
     *        the root table
     * @return a hierarchical table grouped according to the parentColumn
     */
    @AsyncMethod
    Table treeTable(String idColumn, String parentColumn);

    // -----------------------------------------------------------------------------------------------------------------
    // Sort Operations
    // -----------------------------------------------------------------------------------------------------------------

    @AsyncMethod
    Table sort(SortPair... sortPairs);

    @AsyncMethod
    Table sort(String... columnsToSortBy);

    @AsyncMethod
    Table sortDescending(String... columnsToSortBy);

    @Override
    @AsyncMethod
    Table sort(Collection<SortColumn> columnsToSortBy);

    @AsyncMethod
    Table reverse();

    /**
     * <p>
     * Disallow sorting on all but the specified columns.
     * </p>
     *
     * @param allowedSortingColumns The columns on which sorting is allowed.
     * @return The same table this was invoked on.
     */
    @AsyncMethod
    Table restrictSortTo(String... allowedSortingColumns);

    /**
     * <p>
     * Clear all sorting restrictions that was applied to the current table.
     * </p>
     *
     * <p>
     * Note that this table operates on the table it was invoked on and does not create a new table. So in the following
     * code <code>T1 = baseTable.where(...)
     * T2 = T1.restrictSortTo("C1")
     * T3 = T2.clearSortingRestrictions()
     * </code>
     * <p>
     * T1 == T2 == T3 and the result has no restrictions on sorting.
     * </p>
     *
     * @return The same table this was invoked on.
     */
    @AsyncMethod
    Table clearSortingRestrictions();

    // -----------------------------------------------------------------------------------------------------------------
    // Snapshot Operations
    // -----------------------------------------------------------------------------------------------------------------

    Table snapshot(Table baseTable, boolean doInitialSnapshot, String... stampColumns);

    Table snapshot(Table baseTable, String... stampColumns);

    Table snapshotIncremental(Table rightTable, boolean doInitialSnapshot, String... stampColumns);

    Table snapshotIncremental(Table rightTable, String... stampColumns);

    Table snapshotHistory(Table rightTable);

    @Override
    Table snapshot(Table baseTable, boolean doInitialSnapshot, Collection<ColumnName> stampColumns);

    // -----------------------------------------------------------------------------------------------------------------
    // Miscellaneous Operations
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Applies a function to this table.
     * <p>
     * This is useful if you have a reference to a table or a proxy and want to run a series of operations against the
     * table without each individual operation resulting in an RMI.
     *
     * @param function the function to run, its single argument will be this table
     * @param <R> the return type of function
     * @return the return value of function
     * @implNote If the UGP is not required the {@link Function.Unary#call(Object)} method should be annotated with
     *           {@link AsyncMethod}.
     */
    <R> R apply(Function.Unary<R, Table> function);

    /**
     * Return true if this table is guaranteed to be flat. The rowSet of a flat table will be from 0...numRows-1.
     */
    @AsyncMethod
    boolean isFlat();

    /**
     * Creates a version of this table with a flat rowSet (V2 only).
     */
    @AsyncMethod
    Table flatten();

    /**
     * Set the table's key columns.
     *
     * @return The same table this method was invoked on, with the keyColumns attribute set
     */
    @AsyncMethod
    Table withKeys(String... columns);

    /**
     * Set the table's key columns and indicate that each key set will be unique.
     *
     * @return The same table this method was invoked on, with the keyColumns and unique attributes set
     */
    @AsyncMethod
    Table withUniqueKeys(String... columns);

    @AsyncMethod
    Table layoutHints(String hints);

    @AsyncMethod
    Table layoutHints(LayoutHintBuilder builder);

    @AsyncMethod
    Table withTableDescription(String description);

    /**
     * Add a description for a specific column. You may use {@link #withColumnDescription(Map)} to set several
     * descriptions at once.
     *
     * @param column the name of the column
     * @param description the column description
     * @return a copy of the source table with the description applied
     */
    @AsyncMethod
    Table withColumnDescription(String column, String description);

    /**
     * Add a set of column descriptions to the table.
     *
     * @param descriptions a map of Column name to Column description.
     * @return a copy of the table with the descriptions applied.
     */
    @AsyncMethod
    Table withColumnDescription(Map<String, String> descriptions);

    /**
     * Sets parameters for the default totals table display.
     *
     * @param builder a {@link TotalsTableBuilder} object
     * @return a table with the totals applied
     */
    @AsyncMethod
    Table setTotalsTable(TotalsTableBuilder builder);

    /**
     * Sets renderers for columns.
     *
     * @param builder a builder that creates the packed string for the attribute
     * @return The same table with the ColumnRenderes attribute set
     */
    @AsyncMethod
    Table setColumnRenderers(ColumnRenderersBuilder builder);

    // -----------------------------------------------------------------------------------------------------------------
    // Resource Management
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Release resources held by this table, possibly destructively. This may render the table unsuitable or unsafe for
     * further use.
     *
     * @apiNote In practice, implementations usually just invoke {@link #releaseCachedResources()}.
     */
    void close();

    /**
     * Attempt to release cached resources held by this table. Unlike {@link #close()}, this must not render the table
     * unusable for subsequent read operations. Implementations should be sure to call
     * {@code super.releaseCachedResources()}.
     */
    void releaseCachedResources();

    // -----------------------------------------------------------------------------------------------------------------
    // Methods for refreshing tables
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * <p>
     * Wait for updates to this Table.
     * <p>
     * In some implementations, this call may also terminate in case of interrupt or spurious wakeup (see
     * java.util.concurrent.locks.Condition#await()).
     *
     * @throws InterruptedException In the event this thread is interrupted
     */
    void awaitUpdate() throws InterruptedException;

    /**
     * <p>
     * Wait for updates to this Table.
     * <p>
     * In some implementations, this call may also terminate in case of interrupt or spurious wakeup (see
     * java.util.concurrent.locks.Condition#await()).
     *
     * @param timeout The maximum time to wait in milliseconds.
     * @return false if the timeout elapses without notification, true otherwise.
     * @throws InterruptedException In the event this thread is interrupted
     */
    boolean awaitUpdate(long timeout) throws InterruptedException;

    /**
     * Subscribe for updates to this table. {@code listener} will be invoked via the {@link NotificationQueue}
     * associated with this Table.
     *
     * @param listener listener for updates
     */
    void listenForUpdates(ShiftObliviousListener listener);

    /**
     * Subscribe for updates to this table. After the optional initial image, {@code listener} will be invoked via the
     * {@link NotificationQueue} associated with this Table.
     *
     * @param listener listener for updates
     * @param replayInitialImage true to process updates for all initial rows in the table plus all changes; false to
     *        only process changes
     */
    void listenForUpdates(ShiftObliviousListener listener, boolean replayInitialImage);

    /**
     * Subscribe for updates to this table. {@code listener} will be invoked via the {@link NotificationQueue}
     * associated with this Table.
     *
     * @param listener listener for updates
     */
    void listenForUpdates(Listener listener);

    /**
     * Unsubscribe the supplied listener.
     *
     * @param listener listener for updates
     */
    void removeUpdateListener(ShiftObliviousListener listener);

    /**
     * Unsubscribe the supplied listener.
     *
     * @param listener listener for updates
     */
    void removeUpdateListener(Listener listener);

    /**
     * @return true if this table is in a failure state.
     */
    boolean isFailed();

}
