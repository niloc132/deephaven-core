# Deephaven JS API documentation


## The `dh` namespace
The Deephaven JS API provides familiar JS constructs to interact with the Deephaven gRPC service and Flight/Barrage APIs.
The following documentation details the available class types, methods, properties, and events. "Namespace" here refers
to the fact that using any of these objects requires an "dh." prefix.

### Class `IdeConnection`
Presently, this is the entrypoint into the Deephaven JS API - by creating an instance of this with the server URL and some
options, JS applications can run code on the server, and interact with available exportable objects.

#### Constructor
 * `new dh.IdeConnection(String serverUrl, IdeConnectionOptions options)` - creates a new instance, from which console sessions can be made. `options` are optional.

#### Methods
 * `addEventListener(String eventType, Function eventListener)`
 * `removeEventListener(String eventType, Function eventListener)`
 * `onLogMessage(function(LogItem) logHandler):function()` - Register a callback function to handle any log messages that are emitted on the server. Returns a function
 which can be invoked to remove this log handler. Any log handler registered in this way will receive as many old log messages as are presently available.
 * `startSession(String scriptLanguage):Promise<IdeSession>` - Creates and returns a new IdeSession instance, which can be used to run code on the server.
 * `close()` closes the current connection, releasing any resources on the server or client.

#### Properties
 * `String serverUrl` - The url used when connecting to the server. Read-only.
 * `String serviceId` - The name of the service that should be authenticated to on the server. Read-only.

### Class `IdeConnectionOptions`
Presently optional and not used by the server, this allows the client to specify some authentication details.

#### Properties
 * `String authToken` - base 64 encoded auth token
 * `String serviceId` - The service ID to use for the connection

### Class `IdeSession`

#### Methods
 * `getTable(String tableName):Promise<Table>` - Load the named table, with columns and size information
 already fully populated.
 * `getFigure(String figureName):Promise<Figure>` - Load the named Figure, including its tables and tablemaps as needed.
 * `getTreeTable(String treeTableName):Promise<TreeTable>` - Loads the named tree table or roll-up table, with column
 data populated. All nodes are collapsed by default, and size is presently not available until the viewport is first
 set.
 * `getObject(VariableDefinition def):Promise<Object>` - Loads the given object from the server, following the correct
 semantics of providing that particular object.
 * `newTable(String[] columnNames, String[] columnTypes, String[][] data, String userTimeZone):Promise<Table>` - Creates
 a new table with the given names and types, and string data for rows and columns. The userTimeZone, if given, will be
 used to parse any datetime column values.
 * `mergeTables(Table[] tables):Promise<Table>` - Merges the given tables into a single table. Assumes all tables have the
 same structure.
 * `subscribeToFieldUpdates(function(VariableChanges)):function()`
 * `bindTableToVariable(Table table, String variableName):Promise<void>`
 * `runCode(String code):CommandResult`
 * `onLogMessage(function(LogItem) logHandler):function()`
<!-- Autocomplete, finish this and publish later
 * `openDocument(DidOpenTextDocumentParams openDocParams)`
 * `changeDocument(DidChangeTextDocumentParams changeDocParams)`
 * `getCompletionItems(CompletionParams autoCompleteParams):Promise<CompletionItem[]>>`
 * `closeDocument(DidCloseTextDocumentParams closeDocumentParams)` -->
 * `emptyTable(Number size, Object<String, String>= columns):Promise<Table>` - Creates an empty table with the specified
 number of rows. Optionally columns and types may be specified, but all values will be null.
 * `timeTable(Number periodNanos, DateWrapper= startTime` - Creates a new table that ticks automatically every
 "periodNanos" nanoseconds. A start time may be provided, if so the table will be populated with the interval from the
 specified date until now.
 * `close():void`

### Class `LogItem`
A log entry sent from the server.
#### Properties
 * `number micros` - Timestamp of the message in microseconds since Jan 1, 1970 UTC.
 * `String logLevel` - The level of the log message, enabling the client to ignore messages.
 * `String message` - The log message written on the server.

### Class `CommandResult`
Indicates the result of code run on the server.

#### Properties
 * `VariableChanges changes` - Describes changes made in the course of this command.
 * `String error` - If the command failed, the error message will be provided here.

### Class `VariableChanges`
Describes changes in the current set of variables in the script session. Note that variables that changed value without
changing type will be included as `updated`, but if a new value with one type replaces an old value with a different type,
this will be included as an entry in both `removed` and `created` to indicate the old and new types.

### Properties
 * `VariableDefinition[] created` - The variables that were created by this operation, or have a new type.
 * `VariableDefinition[] updated` - The variables that changed value during this operation.
 * `VariableDefinition[] removed` - The variables that no longer exist after this operation, or were replaced by some
 variable with a different type.

### Class `VariableDefinition`
A format to describe a variable available to be read from the server. Application fields are optional, and only populated
when a variable is provided by application mode.

APIs which take a VariableDefinition must at least be provided an object with a `type` and `id` field.

#### Properties
 * `String type` - The type of the variable, one of `dh.VariableType`.
 * `String title` - The name of the variable, to be used when rendering it to a user.
 * `String id` - An opaque identifier for this variable.
 * `String description` - Optional description for the variable's contents, typically used to provide more detail that
 wouldn't be reasonable to put in the title.
 * `String applicationId` - The ID of the application which provided this variable.
 * `String applicationName` - The name of the application which provided this variable.

### Class `VariableType`
A set of string constants that can be used to describe the different objects the JS API can export.
#### Static Properties
 * `TABLE`
 * `TREETABLE`
 * `TABLEMAP`
 * `FIGURE`
 * `OTHERWIDGET`
 * `PANDAS`

### Class `Table`
Provides access to data in a table. Note that several methods present
their response through Promises. This allows the client to both avoid actually connecting to the server until necessary,
and also will permit some changes not to inform the UI right away that they have taken place.

#### Properties
 * `Column[] columns` - Read-only. The columns that are present on this table.  This is always all possible columns.
 If you specify fewer columns in .setViewport(), you will get only those columns in your ViewportData.
 * `Number size` - Read-only. The total count of rows in the table. The size can and will change; see the `sizechanged` 
 event for details. Size will be negative in exceptional cases (eg. the table is uncoalesced, see the `isUncoalesced` property 
 for details). 
 * `Number totalSize` - Read-only. The total count of the rows in the table, excluding any filters. Unlike `size`, 
 changes to this value will not result in any event.
 * `Sort[] sort` - Read-only. An ordered list of Sorts to apply to the table.  To update, call applySort().  Note that
 this getter will return the new value immediately, even though it may take a little time to update on the server.
 You may listen for the `sortchanged` event to know when to update the UI.
 * `FilterCondition[] filter` - Read-only. An ordered list of Filters to apply to the table.  To update, call applyFilter().
 Note that this getter will return the new value immediately, even though it may take a little time to update on the
 server. You may listen for the `filterchanged` event to know when to update the UI.
 * `String[] customColumns` - Read-only. An ordered list of custom column formulas to add to the table, either adding
 new columns or replacing existing ones. To update, call `applyCustomColumns()`.
 * `boolean hasInputTable` - Read-only. True if this table represents a user Input Table (created by InputTable.newInputTable).
 When true, you may call .inputTable() to add or remove data from the underlying table.
 * `TotalsTableConfig totalsTableConfig` - The default configuration to be used when building a `TotalsTable` for this table.
 * `boolean isUncoalesced` - Read-only. True if this table is uncoalesced. Set a viewport or filter on the partition columns 
 to coalesce the table. Check the `isPartitionColumn` property on the table columns to retrieve the partition columns.
 Size will be unavailable until table is coalesced.
 * `boolean isClosed` - Read-only. True if this table has been closed.

#### Methods
 * `applySort(Sort[]):Sort[]` - Replace the currently set sort on this table. Returns the previously set value. Note
 that the sort property will immediately return the new value, but you may receive update events using the old sort
 before the new sort is applied, and the `sortchanged` event fires. Reusing existing, applied sorts may enable
 this to perform better on the server.  The `updated` event will also fire, but `rowadded` and `rowremoved` will not.
 * `applyFilter(FilterCondition[]):FilterCondition[]` - Replace the currently set filters on the table. Returns the previously set value.
 Note that the filter property will immediately return the new value, but you may receive update events using the old
 filter before the new one is applied, and the `filterchanged` event fires. Reusing existing, applied filters may enable
 this to perform better on the server.  The `updated` event will also fire, but `rowadded` and `rowremoved` will not.
 * `applyCustomColumns(String[]):String[]` - Replace the current custom columns with a new set. These columns can be
 used when adding new filter and sort operations to the table, as long as they are present.
 * `setViewport(Number firstRow, Number lastRow, Column[]= columns, Number= updateIntervalMs):TableViewportSubscription` - 
 If the columns parameter is not provided, all columns will be used. If the updateIntervalMs parameter is not provided, 
 a default of one second will be used. Until this is called, no data will be available. Invoking this will result in events
 to be fired once data becomes available, starting with an `updated` event and a `rowadded` event per row in that range.
 The returned object allows the viewport to be closed when no longer needed.
 * `subscribe(Column[] columns, Number= updateIntervalMs):TableSubscription` - Creates a subscription to the specified 
 columns, across all rows in the table. The optional parameter updateIntervalMs may be specified to indicate how often the
 server should send updates, defaulting to one second if omitted. Useful for charts or taking a snapshot of the table 
 atomically. The initial snapshot will arrive in a single event, but later changes will be sent as updates. However, this 
 may still be very expensive to run from a browser for very large tables. Each call to subscribe creates a new subscription,
 which must have `close()` called on it to stop it, and all events are fired from the TableSubscription instance.
 * `getViewportData():Promise<ViewportData>` - Gets the currently visible viewport. If the current set of operations
 has not yet resulted in data, it will not resolve until that data is ready.
 * `copy():Promise<Table>` - Creates a new copy of this table, so it can be sorted and filtered separately, and maintain
 a different viewport.
 * `addEventListener(String type, function listener):Function` - Listen for events on this table.
 Returns a cleanup function.
 * `removeEventListener(String type, function listener)`  - Removes an event listener added to this table.
 * `close()` - Indicates that this Table instance will no longer be used, and its connection to the server can be
 cleaned up.
 * `selectDistinct(Column[]):Promise<Table>` - Returns a new table containing the distinct tuples of values from the
 given columns that are present in the original table. This table can be manipulated as any other table. Sorting is
 often desired as the default sort is the order of appearance of values from the original table.
 * `findColumn(String named):Column` - Retrieve a column by the given name.  You should prefer to always retrieve
 a new Column instance instead of caching a returned value.
 * `findColumns(String[] named):Column[]` - Retrieve multiple columns specified by the given names.
<!-- unsupported features, inputtable, totals tables, column statistics
 * `inputTable():Promise<InputTable>` - If .hasInputTable is true, you may call this method to gain access to an
 InputTable object which can be used to mutate the data within the table.  If the table is not an Input Table, the
 promise will be immediately rejected.
 * `getTotalsTable(TotalsTableConfig= config):Promise<TotalsTable>` - Returns a promise that will resolve to a Totals 
 Table of this table. This table will obey the configurations provided as a parameter, or will use the table's default 
 if no parameter is provided, and be updated once per second as necessary. Note that multiple calls to this method will
 each produce a new TotalsTable which must have close() called on it when not in use.
 * `getGrandTotalsTable(TotalsTableConfig= config):Promise<TotalsTable>` - Returns a promise that will resolve to a Totals Table
 of this table, ignoring any filters. See `getTotalsTable()` above for more specifics.
 * `getColumnStatistics(Column column):Promise<ColumnStatistics>` - Returns a promise that will resolve to ColumnStatistics for
 the column of this table.
-->
 * `freeze():Promise<Table>` - Returns a "frozen" version of this table (a server-side snapshot of the entire source
 table). Viewports on the frozen table will not update. This does not change the original table, and the new table will 
 not have any of the client side sorts/filters/columns. New client side sorts/filters/columns can be added to the frozen 
 copy.
<!-- unsupported features, rollup, treetable
 * `rollup(RollupConfig):Promise<TreeTable>` - Returns a promise that will resolve to a new roll-up `TreeTable` of this table.
 Multiple calls to this method will each produce a new `TreeTable` which must have close() called on it when not in use.
 * `treeTable(TreeTableConfig):Promise<TreeTable>` - Returns a promise that will resolve to a new `TreeTable` of this table. 
 Multiple calls to this method will each produce a new `TreeTable` which must have close() called on it when not in use. 
-->
 * `join(String joinType, Table rightTable, String[] columnsToMatch, String[]= columnsToAdd):Promise<Table>` - Deprecated.
 Returns a promise that will be resolved with a newly created table holding the results of the join operation. The last
 parameter is optional, and if not specified or empty, all columns from the right table will be added to the output.
 Callers are responsible for ensuring that there are no duplicates - a match pair can be passed instead of a name to
 specify the new name for the column. Supported `joinType` values:
   * "Join"
   * "Natural"
   * "AJ"
   * "ReverseAJ"
   * "ExactJoin"
   * "LeftJoin"
 * `asOfJoin(Table rightTable, String[] columnsToMatch, String[]= columnsToAdd, String= asOfMatchRule):Promise<Table>` -
 Returns a promise that will be resolved with the newly created table holding the results of the specified as-of join
 operation. The `columnsToAdd` parameter is optional, not specifying it will result in all columns from the right table
 being added to the output. The `asOfMatchRule` is optional, defaults to "LESS_THAN_EQUAL" - the allowed values are:
   * "LESS_THAN_EQUAL"
   * "LESS_THAN"
   * "GREATER_THAN_EQUAL"
   * "GREATER_THAN"
 * `crossJoin(Table rightTable, String[] columnsToMatch, String[]= columnsToAdd, number= reserveBits):Promise<Table>` -
 Returns a promise that will be resolved with the newly created table holding the results of the specified cross join
 operation. The `columnsToAdd` parameter is optional, not specifying it will result in all columns from the right table
 being added to the output. The `reserveBits` optional parameter lets the client control how the key space is distributed
 between the rows in the two tables, see the Java `Table` class for details.
 * `exactJoin(Table rightTable, String[] columnsToMatch, String[]= columnsToAdd):Promise<Table>` - Returns a promise that
 will be resolved with the newly created table holding the results of the specified exact join operation. The
 `columnsToAdd` parameter is optional, not specifying it will result in all columns from the right table being added to
 the output.
 * `leftJoin(Table rightTable, String[] columnsToMatch, String[]= columnsToAdd):Promise<Table>` - Returns a promise that
 will be resolved with the newly created table holding the results of the specified left join operation. The
 `columnsToAdd` parameter is optional, not specifying it will result in all columns from the right table being added to
 the output.
 * `naturalJoin(Table rightTable, String[] columnsToMatch, String[]= columnsToAdd, String= asOfMatchRule):Promise<Table>` -
 Returns a promise that will be resolved with the newly created table holding the results of the specified natural join
 operation. The `columnsToAdd` parameter is optional, not specifying it will result in all columns from the right table
 being added to the output.
<!-- unsupported features, byExternal/TableMap
 * `byExternal(String[] keys, boolean= dropKeys):Promise<TableMap>` - Creates a new TableMap from the contents of the
 current table, partitioning data based on the specified keys.
 * `getAttributes():String[]` - returns an array listing the attributes that are set on this table, minus some of those already
 available via other Table properties. Keys that represent an attribute that can be read in JS can be passed to 
 `getAttribute(key)`.
-->
 * `getAttribute(String key):Any` - returns null if no property exists, a string if it is an easily serializable property,
 or a `Promise<Table>` that will either resolve with a table or error out if the object can't be passed to JS.

#### Static functions
 * `reverse():Sort` - Returns a Sort than can be used to reverse a table. This can be passed into n array in applySort.
 Note that Tree Tables do not support reverse.

#### Events
 * `sizechanged` - The table size has updated, so live scrollbars and the like can be updated accordingly.
 * `updated` - event.detail is the currently visible window, the same as if getViewportData() was called and resolved. 
 Listening to this event removes the need to listen to the finer grained events below for data changes. In contrast, 
 using the finer grained events may enable only updating the specific rows which saw a change.
 * `rowadded` - Finer grained visibility into data being added, rather than just seeing the currently visible viewport.
 Provides the row being added, and the offset it will exist at.
 * `rowremoved` - Finer grained visibility into data being removed, rather than just seeing the currently visible viewport.
 Provides the row being removed, and the offset it used to exist at.
 * `rowupdated` - Finer grained visibility into data being updated, rather than just seeing the currently visible viewport.
 Provides the row being updated and the offset it exists at. 
 * `sortchanged` - Indicates that a sort has occurred, and that the UI should be replaced with the current viewport.
 * `filterchanged` - Indicates that a filter has occurred, and that the UI should be replaced with the current viewport.
 * `customcolumnschanged` - Indicates that columns for this table have changed, and column headers should be updated.
 * `requestfailed` - Indicates that an error occurred on this table on the server or while communicating with it. The 
 message will provide more insight, but recent operations were likely unsuccessful and may need to be reapplied.

### Class `Column`
Describes the structure of the column, and if desired can be used to get access to the data to be rendered in this
column.

#### Methods
 * `get(Row):Any` - Returns the value for this column in the given row. Type will be consistent with the type of the
 Column.
 * `filter():FilterValue` - Creates a new value for use in filters based on this column. Used either as a parameter to
 another filter operation, or as a builder to create a filter operation.
 * `sort():Sort` - Creates a sort builder object, to be used when sorting by this column.

#### Properties
 * `Number index` - Deprecated, do not use. Internal index of the column in the table, to be used as a key on the Row.
 * `String type` - Type of the row data that can be found in this column.
 * `String name` - Label for this column.
 * `boolean isPartitionColumn` - True if this column is a partition column. Partition columns are used for filtering
 uncoalesced tables (see `isUncoalesced` property on `Table`) 
 * `String constituentType` - If this column is part of a roll-up tree table, represents the type of the row data that 
   can be found in this column for leaf nodes if includeConstituents is enabled. Otherwise, it is `null`.

### Class `Sort`
Describes a Sort present on the table. No visible constructor, created through the use of Column.sort(), will be tied to
that particular column data. Sort instances are immutable, and use a builder pattern to make modifications. All methods
return a new Sort instance. 

#### Methods
 * `asc():Sort` - Builds a Sort instance to sort values in ascending order.
 * `desc():Sort` - Builds a Sort instance to sort values in descending order.
 * `abs():Sort` - Builds a Sort instance which takes the absolute value before applying order.

#### Properties
 * `Column column` - The column which is sorted.
 * `String direction` - The direction of this sort, either `ASC`, `DESC`, or `REVERSE`.
 * `boolean isAbs` - True if the absolute value of the column should be used when sorting; defaults to false.

### Class `FilterValue`
Describes data that can be filtered, either a column reference or a literal value. Used this way, the type of a value
can be specified so that values which are ambiguous or not well supported in JS will not be confused with Strings or imprecise numbers (e.g., nanosecond-precision date values). Additionally, once wrapped in this way, methods can be
called on these value literal instances. These instances are immutable - any method called on them returns a new
instance.

#### Static factory methods
 * `ofString(?):FilterValue` - Constructs a string for the filter API from the given parameter.
 * `ofNumber(?):FilterValue` - Constructs a number for the filter API from the given parameter. Can also be used on the 
 values returned from `Row.get` for DateTime values. To create a filter with a date, use `dh.DateWrapper.ofJsDate` or
 `dh.i18n.DateTimeFormat.parse`. To create a filter with a 64-bit long integer, use `dh.LongWrapper.ofString`.
 * `ofBoolean(?):FilterValue` - Constructs a boolean for the filter API from the given parameter.
 
#### Methods
 * `eq(FilterValue):FilterCondition` - Returns a filter condition checking if the current value is equal to the given 
 parameter.
 * `eqIgnoreCase(FilterValue):FilterCondition` - Returns a filter condition checking if the current value is equal to 
 the given parameter, ignoring differences of upper vs lower case.
 * `notEq(FilterValue):FilterCondition` - Returns a filter condition checking if the current value is not equal to the 
 given parameter.
 * `notEqIgnoreCase(FilterValue):FilterCondition` - Returns a filter condition checking if the current value is not 
 equal to the given parameter, ignoring differences of upper vs lower case.
 * `greaterThan(FilterValue):FilterCondition` - Returns a filter condition checking if the current value is greater 
 than the given parameter.
 * `lessThan(FilterValue):FilterCondition` - Returns a filter condition checking if the current value is less than the 
 given parameter.
 * `greaterThanOrEqualTo(FilterValue):FilterCondition` - Returns a filter condition checking if the current value is 
 greater than or equal to the given parameter.
 * `lessThanOrEqualTo(FilterValue):FilterCondition` - Returns a filter condition checking if the current value is less 
 than or equal to the given parameter.
 * `in(FilterValue[]):FilterCondition` - Returns a filter condition checking if the current value is in the given set 
 of values.
 * `inIgnoreCase(FilterValue[]):FilterCondition` - Returns a filter condition checking if the current value is in the 
 given set of values, ignoring differences of upper vs lower case.
 * `notIn(FilterValue[]):FilterCondition` - Returns a filter condition checking that the current value is not in the 
 given set of values.
 * `notInIgnoreCase(FilterValue[]):FilterCondition` - Returns a filter condition checking that the current value is not 
 in the given set of values, ignoring differences of upper vs lower case.
 * `contains(FilterValue):FilterCondition` - Returns a filter condition checking if the given value contains the given
 string value.
 * `containsIgnoreCase(FilterValue):FilterCondition` - Returns a filter condition checking if the given value contains
 the given string value, ignoring differences of upper vs lower case.
 * `matches(FilterValue):FilterCondition` - Returns a filter condition checking if the given value matches the provided
 regular expressions string. Regex patterns use Java regex syntax.
 * `matchesIgnoreCase(FilterValue):FilterCondition` - Returns a filter condition checking if the given value matches 
 the provided regular expressions string, ignoring differences of upper vs lower case. Regex patterns use Java regex
 syntax.
 * `isTrue():FilterCondition` - Returns a filter condition checking if the current value is a true boolean.
 * `isFalse():FilterCondition` - Returns a filter condition checking if the current value is a false boolean.
 * `isNull():FilterCondition` - Returns a filter condition checking if the current value is a null value.
 * `invoke(String, ...?):FilterCondition` - Returns a filter condition invoking the given method on the current value, 
 with the given parameters. Currently supported functions that can be invoked on a String:
  
     * `startsWith` - Returns true if the current string value starts with the supplied string argument.
     * `endsWith` - Returns true if the current string value ends with the supplied string argument.
     * `matches` - Returns true if the current string value matches the supplied string argument used as a Java regular 
     expression.
     * `contains` - Returns true if the current string value contains the supplied string argument. When invoking against
     a constant, this should be avoided in favor of FilterValue.contains.
 
### Class `FilterCondition`
Describes a filter which can be applied to a table. Replacing these instances may be more expensive than reusing them.
These instances are immutable - all operations that compose them to build bigger expressions return a new instance.

#### Methods
 * `not():FilterCondition` - Returns the opposite of this condition.
 * `and(...FilterCondition):FilterCondition` - Returns a condition representing the current condition logically ANDed
 with the other parameters.
 * `or(...FilterCondition):FilterCondition` - Returns a condition representing the current condition logically ORed with
 the other parameters.
 * `toString():String` - Returns a string suitable for debugging showing the details of this condition.

<!-- Non-functional, column instances can't be provided from a filter
#### Properties
 * `Column[] columns` - The columns tested in some way by this filter (not yet supported).
-->
 
#### Static functions
 * `search(FilterValue, FilterValue[]=):FilterCondition` - Returns a filter condition which will check if the given
 value can be found in any supported column on whatever table this FilterCondition is passed to. This FilterCondition is
 somewhat unique in that it need not be given a column instance, but will adapt to any table. On numeric columns, with a
 value passed in which can be parsed as a number, the column will be filtered to numbers which equal, or can be
 "rounded" effectively to this number. On String columns, the given value will match any column which contains this
 string in a case-insensitive search. An optional second argument can be passed, an array of `FilterValue` from the 
 columns to limit this search to (see `Column.filter()`).
 * `invoke(String, ...FilterValue):FilterCondition` - Returns a filter condition invoking a static function with the 
 given parameters. Currently supported Deephaven static functions:
     * `inRange` - Given three comparable values, returns true if the first is less than the second but greater than the 
     third.
     * `isInf` - Returns true if the given number is "infinity".
     * `isNaN` - Returns true if the given number is "not a number".
     * `isNormal` - Returns true if the given number is not null, is not infinity, and is not "not a number".
     * `startsWith` - Returns true if the first string starts with the second string.
     * `endsWith` - Returns true if the first string ends with the second string.
     * `matches` - Returns true if the first string argument matches the second string used as a Java regular expression.
     * `contains` - Returns true if the first string argument contains the second string as a substring.
     * `in` - Returns true if the first string argument can be found in the second array argument. Note that the array
     can only be specified as a column reference at this time - typically the `FilterValue.in` method should be used in
     other cases.


### Class `ViewportData`
Contains data in the current viewport. Also contains the offset to this data, so that the actual row number may be
determined. Do not assume that the first row in `rows` is the first visible row, because extra rows may be provided for easier
scrolling without going to the server.

### Properties
 * `Number offset` - The index of the first returned row.
 * `Row[] rows` - An array of rows of data.
 * `Column[] columns` - A list of columns describing the data types in each row.

### Class `Row`
This object may be pooled internally or discarded and not updated. Do not retain references to it. Instead, request the 
viewport again.

#### Methods
 * `get(Column column):Any` - Returns the data for the given column's cell.
 * `getFormat(Column column):Format` - Returns the format object for the given columns' cell.
 
### Class `Format`
This object may be pooled internally or discarded and not updated. Do not retain references to it.

#### Properties 
 * `String color` - Color to apply to the text, in `#rrggbb` format.
 * `String backgroundColor` - Color to apply to the cell's background, in `#rrggbb` format.
 * `String formatString` - The format string to apply to the value of this cell (see https://docs.deephaven.io/latest/Content/writeQueries/formatTables.htm#Formatting_Tables).
 * `String numberFormat` - DEPRECATED - use `formatString` instead. Number format string to apply to the value in this cell (see https://docs.deephaven.io/latest/Content/writeQueries/formatTables.htm#Formatting_Tables).

<!--
### Class `TableMap`
Represents a set of Tables each corresponding to some key. The keys are available locally, but a call must be made to 
the server to get each Table. All tables will have the same structure.

The available TableMap instances are not listed in the QueryInfo object as tables are - the application must know the
name of the TableMaps that will be available.

Note: Currently, keys can only be String values.

#### Methods
 * `getKeys():String[]` - The set of all currently known keys. This is kept up to date, so getting the list after adding
 an event listener for `keyadded` will ensure no keys are missed.
 * `size():Number` - The count of known keys.
 * `close()` - Indicates that this TableMap will no longer be used, removing subcriptions to updated keys, etc. This
 will not affect tables in use.
 * `getTable(String):Promise<Table>` - Fetches the table with the given key.

#### Events
 * `keyadded` - Indicates that a new key has been added to the array of keys, which can now be fetched with getTable.
 * `requestfailed` - Indicates that an error has occurred while communicating with the server.

### Class `InputTable`
Represents a User Input Table, which can have data added to it from other sources.

You may add rows using dictionaries of key-value tuples (representing columns by name), add tables containing all the key/value 
columns to add, or delete tables containing the keys to delete. Each operation is atomic, and will either succeed completely 
or fail completely. To guarantee order of operations, apply an operation and wait for the response before sending the next operation.

Each table has one or more key columns, where each unique combination of keys will appear at most once in the table.

To view the results of the Input Table, you should use standard table operations on the InputTable's source Table object.

#### Properties
 * `String[] keys` - A list of the key columns, by name.
 * `Column[] keyColumns` - A list of the key Column objects.
 * `String[] values` - A list of the value columns, by name.
 * `Column[] valueColumns` - A list of the value Column objects.
 * `Table table` - The source table for this Input Table.

#### Methods
 * `addRow(Object from):Promise<InputTable>` - Adds a single row to the table. For each key or value column name in the
  Input Table, we retrieve that javascript property at that name and validate it can be put into the given column type.
 * `addRows(Object[] from):Promise<InputTable>` - Add multiple rows to a table. 
 * `addTable(Table from):Promise<InputTable>` - Add an entire table to this Input Table.  Only column names that match
 the definition of the input table will be copied, and all key columns must have values filled in.  This only copies
 the current state of the source table; future updates to the source table will not be reflected in the Input Table.
 The returned promise will be resolved to the same InputTable instance this method was called upon once the server returns.
 * `addTables(Table[] from):Promise<InputTable>` - Add multiple tables to this Input Table.
 * `deleteTable(Table from):Promise<InputTable>` - Deletes an entire table from this Input Table. Key columns must match
 the Input Table.
 * `deleteTables(Table[] from):Promise<InputTable>` - Delete multiple tables from this Input Table.

### Class `TotalsTable`
A simplistic Table, providing access to aggregation of the table it is sourced from. This table is always automatically subscribed to its parent, and adopts changes automatically from it. 
This class has limited methods found on Table. Instances of this type always have a size of one when no groupBy is set on the config, but may potentially contain as
few as zero rows, or as many as the parent table if each row gets its own group.

When using the `groupBy` feature, it may be desireable to also provide a row to the user with all values across all
rows. To achieve this, request the same Totals Table again, but remove the `groupBy` setting.

#### Properties
 * `TotalsTableConfig totalsTableConfig` - Read-only. The configuration used when creating this Totals Table. 
 * `Column[] columns` - Read-only. The columns present on this table. Note that this may not include all columns in the parent
 table, and in cases where a given column has more than one aggregation applied, the column name will have a suffix
 indicating the aggregation used. This suffixed name will be of the form `columnName + '__' + aggregationName`.
 * `Number size` - Read-only. The total number of rows in this table. This may change as the base table's configuration,
 filter, or contents change.
 * `Sort[] sort` - Read-only. An ordered list of Sorts to apply to the table.  To update, call applySort().  Note that
 this getter will return the new value immediately, even though it may take a little time to update on the server.
 You may listen for the `sortchanged` event to know when to update the UI.
 * `FilterCondition[] filter` - Read-only. An ordered list of Filters to apply to the table.  To update, call applyFilter().
 Note that this getter will return the new value immediately, even though it may take a little time to update on the
 server. You may listen for the `filterchanged` event to know when to update the UI.
 * `String[] customColumns` - Read-only. An ordered list of custom column formulas to add to the table, either adding
 new columns or replacing existing ones. To update, call `applyCustomColumns()`.

#### Methods
 * `getViewportData():Promise<ViewportData>` - Gets the currently visible viewport. If the current set of operations
 has not yet resulted in data, it will not resolve until that data is ready.
 * `setViewport(Number firstRow, Number lastRow, Column[]= columns)` - Specifies the range of items to pass to the 
 client and update as they change. If the columns parameter is not provided, all columns will be used. Until this is 
 called, no data will be available. Invoking this will result in events to be fired once data becomes available, 
 starting with an `updated` event and one `rowadded` event per row in that range.
 * `findColumn(String named):Column` - Retrieve a column by the given name.  You should prefer to always retrieve
 a new Column instance instead of caching a returned value.
 * `findColumns(String[] named):Column[]` - Retrieve multiple columns specified by the given names.
 * `close()` - Indicates that the table will no longer be used, and resources used to provide it can be freed up on the
 server.
 * `applySort(Sort[]):Sort[]` - Replace the currently set sort on this table. Returns the previously set value. Note
 that the sort property will immediately return the new value, but you may receive update events using the old sort
 before the new sort is applied, and the `sortchanged` event fires. Reusing existing, applied sorts may enable
 this to perform better on the server.  The `updated` event will also fire, but `rowadded` and `rowremoved` will not.
 * `applyFilter(FilterCondition[]):FilterCondition[]` - Replace the currently set filters on the table. Returns the previously set value.
 Note that the filter property will immediately return the new value, but you may receive update events using the old
 filter before the new one is applied, and the `filterchanged` event fires. Reusing existing, applied filters may enable
 this to perform better on the server.  The `updated` event will also fire, but `rowadded` and `rowremoved` will not.
 * `applyCustomColumns(String[]):String[]` - Replace the current custom columns with a new set. These columns can be
 used when adding new filter and sort operations to the table, as long as they are present.
 * `addEventListener(String type, function listener):Function` - Listen for events on the main connection.  Returns a
 convenience function to remove the event listener.
 * `removeEventListener(String type, function listener)` - Allow for manual "normal" event handler removal as well.
 
#### Events
All events that can fire on a Table can also fire from a TotalsTable, and can be interacted with in the same way.

### Class `TreeTable`
Similar to a table, a Tree Table provides access to subscribed viewport data on the current hierarchy. A different Row type is used
within that viewport, showing the depth of that node within the tree and indicating details about whether or not it
has children or is expanded. The Tree Table itself then provides the ability to change if a row is expanded or not.
Methods used to control or check if a row should be expanded or not can be invoked on a TreeRow instance, or on the
number of the row (thus allowing for expanding/collapsing rows which are not currently visible in the viewport).

Events and viewports are somewhat different than tables, due to the expense of computing the expanded/collapsed rows
and count of children at each level of the hierarchy, and differences in the data that is available.

 * There is no `totalSize` property.
 * The viewport is not un-set when changes are made to filter or sort, but changes will continue to be streamed in. It 
 is suggested that the viewport be changed to the desired position (usually the first N rows) after any filter/sort
 change is made. Likewise, `getViewportData()` will always return the most recent data, and will not wait if
 a new operation is pending.
 * Custom columns are not directly supported. If the `TreeTable` was created client-side, the original Table can have
 custom columns applied, and the `TreeTable` can be recreated.
 * The `totalsTableConfig` property is instead a method, and returns a promise so the config can be fetched 
 asynchronously.
 * Totals Tables for trees vary in behavior between hierarchical tables and roll-up tables. This behavior is based on
 the original flat table used to produce the Tree Table - for a hierarchical table (i.e. Table.treeTable in the query
 config), the totals will include non-leaf nodes (since they are themselves actual rows in the table), but in a roll-up
 table, the totals only include leaf nodes (as non-leaf nodes are generated through grouping the contents of the 
 original table). Roll-ups also have the `isIncludeConstituents` property, indicating that a `Column` in the tree may 
 have a `constituentType` property reflecting that the type of cells where `hasChildren` is false will be different from 
 usual.

#### Properties
 * `Number size` - The current number of rows given the table's contents and the various expand/collapse states of each
 node. (No totalSize is provided at this time; its definition becomes unclear between roll-up and tree tables, especially
 when considering collapse/expand states).
 * `Sort[] sort` - The current sort configuration of this Tree Table.
 * `FilterCondition[] filter` - The current filter configuration of this Tree Table.
 * `Column[] columns` - The columns that can be shown in this Tree Table.
 * `boolean isIncludeConstituents` - True if this is a roll-up and will provide the original rows that make up each grouping.

#### Methods
 * `expand(Number|TreeRow)` - Expands the given node, so that its children are visible when they are in the viewport. 
 The parameter can be the row index, or the row object itself. Equivalent to `setExpanded(row, true)`.
 * `collapse(Number|TreeRow)` - Collapses the given node, so that its children and descendents are not visible in the
 size or the viewport. The parameter can be the row index, or the row object itself. Equivalent to 
 `setExpanded(row, false)`.
 * `setExpanded(Number|TreeRow, boolean)` - Specifies if the given node should be expanded or collapsed. If this node has
 children, and the value is changed, the size of the table will change.
 * `isExpanded(Number|TreeRow):boolean` - Returns true if the given row is expanded, false otherwise. Equivalent to
 `TreeRow.isExpanded`, if an instance of the row is available.
 * `setViewport(Number firstRow, Number lastRow, Column[]= columns)`
 * `getViewportData():Promise<ViewportData>` -
 * `close()` - Indicates that the table will no longer be used, and server resources can be freed.
 * `applySort(Sort[]):Sort[]` - Applies the given sort to all levels of the tree. Returns the previous sort in use.
 * `applyFilter(FilterCondition[]):FilterCondition[]` - Applies the given filter to the contents of the tree in
 such a way that if any node is visible, then any parent node will be visible as well even if that parent node would not
 normally be visible due to the filter's condition. Returns the previous sort in use.
 * `findColumn(String):Column` - Returns a column with the given name, or throws an exception if it cannot be found.
 * `findColumns(String[]):Column[]` - Returns an array with all of the named columns in order, or throws an exception
 if one cannot be found.
 * `getTotalsTableConfig():Promise<TotalsTableConfig>` - Fetches the Totals config for the table that this Tree Table
 was based on, so that the content of the flat table can be aggregated. 
 * `getTotalsTable(TotalsTableConfig):Promise<TotalsTable>` - Constructs a new Totals Table from the given config, based
 on the current filter status of this Tree Table. If null or not provided, the default config (see 
 `getTotalsTableConfig()`) will be used.
 * `getGrandTotalsTable(TotalsTableConfig):Promise<TotalsTable>` - Constructs a new Totals Table from the given config,
 ignoring any filters set on this Tree Table. If null or not provided, the default config (see `getTotalsTableConfig()`)
 will be used.
 * `addEventListener(String type, function listener):Function` - Listen for events on this Tree Table.
 Returns a cleanup function.
 * `removeEventListener(String type, function listener)` - Removes an event listener from this Tree Table.
 * `copy():Promise<TreeTable>` - Creates a new copy of this treetable, so it can be sorted and filtered separately, and
 maintain a different viewport. Unlike Table, this will _not_ copy the filter or sort, since tree table viewport
 semantics differ, and without a viewport set, the treetable doesn't evaluate these settings, and they aren't readable
 on the properties. Expanded state is also not copied.

#### Events
 * `updated` - event.detail is the currently visible viewport data based on the active viewport configuration.

### Class `TreeRow` extends `Row`
Like `Row`, `TreeRow` represents visible rows in the table, but with additional properties to reflect the tree structure.

#### Properties
 * `boolean isExpanded` - True if this node is currently expanded to show its children; false otherwise. Those children
 will be the rows below this one with a greater depth than this one.
 * `boolean hasChildren` - True if this node has children and can be expanded; false otherwise. Note that this value may
 change when the table updates, depending on the table's configuration.
 * `Number depth` - The number of levels above this node; zero for top level nodes. Generally used by the UI to indent
 the row and its expand/collapse icon.
-->

### Class `TableViewportSubscription`
This object serves as a "handle" to a subscription, allowing it to be acted on directly or canceled outright. If you retain
an instance of this, you have two choices - either only use it to call `close()` on it to stop the table's viewport without
creating a new one, or listen directly to this object instead of the table for data events, and always call `close()` when
finished. Calling any method on this object other than close() will result in it continuing to live on after `setViewport`
is called on the original table, or after the table is modified.

#### Methods
 * `close()` - Stops this viewport from running, stopping all events on itself and on the table that created it.
 * `setViewport(Number firstRow, Number lastRow, Column[]= columns)` - Changes the rows and columns set on this viewport.
 This cannot be used to change the update interval.
 * `getViewportData():Promise<ViewportData>` - Gets the data currently visible in this viewport.
 * `addEventListener(String type, function listener):Function` - Listens for events on this viewport subscription.
 Returns a cleanup function.
 * `removeEventListener(String type, function listener)` - Removes an event listener on this viewport subscription. 

### Class `TableSubscription`
Represents a subscription to the table on the server. Changes made to the table will not be reflected here - the subscription
must be closed and a new one optioned to see those changes. The event model is slightly different from viewports to make it 
less expensive to compute for large tables.

#### Properties
 * `Column[] columns` - The columns that were subscribed to when this subscription was created.
 
#### Methods
 * `close()` - Stops the subscription on the server.
 * `addEventListener(String type, function listener):Function` - Listens for events on this table subscription.
 Returns a cleanup function.
 * `removeEventListener(String type, function listener)` - Removes an event listener on this table subscription.
 
#### Events
 * `updated` - Indicates that some new data is available on the client, either an initial snapshot or a delta update.
 The `detail` field of the event will contain a TableSubscriptionEventData detailing what has changed, or allowing access
 to the entire range of items currently in the subscribed columns.

### Class `TableSubscriptionEventData`
This class supports two ways of reading the table - checking the changes made since the last update, and reading all
data currently in the table. While it is more expensive to always iterate over every single row in the table,
it may in some cases actually be cheaper than maintaining state separately and updating only the changes, though both
options should be considered.

The RangeSet objects allow iterating over the LongWrapper indexes in the table. Note that these "indexes" are not necessarily 
contiguous and may be negative, and represent some internal state on the server, allowing it to keep track of data efficiently.
Those LongWrapper objects can be passed to the various methods on this instance to read specific rows or cells out of the table.

#### Properties
 * `Row[] rows` - A lazily computed array of all rows in the entire table. 
 * `RangeSet added` - The ordered set of row indexes added since the last update.
 * `RangeSet removed` - The ordered set of row indexes removed since the last update.
 * `RangeSet updated` - The ordered set of row indexes updated since the last update.

#### Methods
 * `get(LongWrapper index):Row` - Reads a row object from the table, from which any subscribed column can be read.
 * `getData(LongWrapper index, Column column):Object` - Reads a specific cell from the table, from the specified row and
 column.
 * `getFormat(LongWrapper index, Column column):Format` - Reads the Format to use for a cell from the specified row and
 column.
 
### Class `RangeSet`
This class allows iteration over non-contiguous indexes. In the future, this will support the EcmaScript 2015 Iteration
protocol, but for now has one method which returns an iterator, and also supports querying the size. Additionally,
we may add support for creating RangeSet objects to better serve some use cases.

#### Properties
 * `Number size` - The total count of items contained in this collection. In some cases this can be expensive to compute,
 and generally should not be needed except for debugging purposes, or preallocating space (i.e., do not call this property
 each time through a loop).
 
#### Methods
 * `iterator():Iterator<LongWrapper>` - Returns a new iterator over all indexes in this collection. 
 
### Protocol `Iterator<T>`
This is part of EcmaScript 2015, documented here for completeness. It supports a single method, `next()`, which returns
an object with a `boolean` named `done` (true if there are no more items to return; false otherwise), and optionally
some `T` instance, `value`, if there was at least one remaining item.

<!--
### Class `ColumnStatistics`
This class holds the results of a call to generate statistics on a table column.

#### Methods
 * `getType(String name):String` - Gets the format type for a statistic. A null return value means that the column formatting should be used.

#### Properties
 * `Map<String, Double> statisticsMap` - A map of each statistic's name to its value.
 * `Map<String, Double> uniqueValues` - A map of each unique value's name to the count of how many times it occurred in the column.  This map will be empty for tables containing more than 19 unique values.

### Class `TotalsTableConfig`
Describes how a Totals Table will be generated from its parent table. Each table has a default (which may be null)
indicating how that table was configured when it was declared, and each Totals Table has a similar property describing
how it was created. Both the `Table.getTotalsTable` and `Table.getGrandTotalsTable` methods take this config as an
optional parameter - without it, the table's default will be used, or if null, a default instance of `TotalsTableConfig`
will be supplied.

This class has a no-arg constructor, allowing an instance to be made with the default values provided. However, any JS
object can be passed in to the methods which accept instances of this type, provided their values adhere to the expected
formats.

#### Properties
 * `boolean showTotalsByDefault` - Specifies if a Totals Table should be expanded by default in the UI. Defaults to 
 false.
 * `boolean showGrandTotalsByDefault` - Specifies if a Grand Totals Table should be expanded by default in the UI. 
 Defaults to false.
 * `String defaultOperation` - Specifies the default operation for columns that do not have a specific operation applied; defaults to "Sum".
 * `Object.<String[]> operationMap` - Mapping from each column name to the aggregation(s) that should be applied to that 
 column in the resulting Totals Table. If a column is omitted, the defaultOperation is used.
 * `String[] groupBy` - Groupings to use when generating the Totals Table. One row will exist for each unique set of
 values observed in these columns. See also `Table.selectDistinct`.
 
### Class `RollupConfig`
Describes a grouping and aggregations for a roll-up table. Pass to the `Table.rollup` function to create a roll-up table.

#### Properties
 * `String[] groupingColumns` - Ordered list of columns to group by to form the hierarchy of the resulting roll-up table.
 * `Object.<String[]> aggregations` - Mapping from each aggregation name to the ordered list of columns it should be 
   applied to in the resulting roll-up table.
 * `boolean includeConstituents` - Optional parameter indicating if an extra leaf node should be added at the bottom of 
 the hierarchy, showing the rows in the underlying table which make up that grouping. Since these values might be a different
 type from the rest of the column, any client code must check if TreeRow.hasChildren = false, and if so, interpret those
 values as if they were Column.constituentType instead of Column.type. Defaults to false.
 * `boolean includeDescriptions` - Optional parameter indicating if original column descriptions should be included. Defaults to true.

### Class `RollupTableConfig (deprecated)`
`RollupTableConfig` has been deprecated. Use `RollupConfig` instead.
 
### Class `TreeTableConfig`
Like TotalsTableConfig, `TreeTableConfig` supports an operation map indicating how to aggregate the data, as well as an array of column names
which will be the layers in the roll-up tree, grouped at each level. An additional optional value can be provided
describing the strategy the engine should use when grouping the rows.

#### Properties
 * `String idColumn` - The column representing the unique ID for each item 
 * `String parentColumn` - The column representing the parent ID for each item
 * `boolean promoteOrphansToRoot` - Optional parameter indicating if items with an invalid parent ID should be promoted to root. Defaults to false.


### Enum `AggregationOperation`
This enum describes the name of each supported operation/aggregation type when creating a `TreeTable`.
 * `COUNT` - The total number of values in the specified column. Can apply to any type. String value is "Count".
 * `MIN` - The minimum value in the specified column. Can apply to any column type which is Comparable in Java. String 
 value is "Min".
 * `MAX` - The maximum value in the specified column. Can apply to any column type which is Comparable in Java. String
 value is "Max".
 * `SUM` - The sum of all values in the specified column. Can only apply to numeric types. String value is "Sum".
 * `ABS_SUM` - The sum of all values, as their distance from zero, in the specified column. Can only apply to numeric types. String value is “AbsSum”.
 * `VAR` - The variance of all values in the specified column. Can only apply to numeric types. String value is "Var".
 * `AVG` - The average of all values in the specified column. Can only apply to numeric types. String value is "Avg".
 * `STD` - The standard deviation of all values in the specified column. Can only apply to numeric types. String value is
 "Std".
 * `FIRST` - The first value in the specified column. Can apply to any type. String value is "First".
 * `LAST` - The last value in the specified column. Can apply to any type. String value is "Last".
 * `COUNT_DISTINCT` - Return the number of unique values in each group. Can apply to any type. String value is "CountDistinct".
 * `DISTINCT` - Collect the distinct items from the column. Can apply to any type. String value is "Distinct".
 * `SKIP` - Indicates that this column should not be aggregated. String value is "Skip".
-->

<!--
## The `dh.lsp` namespace:

### Class `DidChangeTextDocumentParams`

#### Properties
 * `VersionedTextDocumentIdentifier textDocument`

### Class `TextDocumentIdentifier`

#### Properties 
 * `String uri`
 
### Class `VersionedTextDocumentIdentifier` extends `TextDocumentIdentifier`

#### Properties 
 * `number version`
 * `String uri`
 
### Class `DidCloseTextDocumentParams` 

#### Properties
 * `TextDocumentIdentifier textDocument`

### Class `DidOpenTextDocumentParams`

#### Properties
 * `TextDocumentItem textDocument`
 
### Class `TextDocumentItem`

#### Properties
 * `String uri`
 * `String languageId`
 * `number version`
 * `String text`
 
### Class `CompletionParams`

#### Properties
 * `CompletionContext context`

### Class `CompletionContext`

#### Properties
 * `number triggerKind`
 * `String triggerCharacter`

### Class `TextDocumentContentChangeEvent`

#### Properties
 * `DocumentRange range`
 * `number rangeLength`
 * `String text`
 
### Class `DocumentRange`

#### Properties
 * `Position start`
 * `Position end`
 
#### Methods
 * `isInside(Position innerStart, Position innerEnd):boolean`
 
### Class `Position`

#### Properties

 * `number line`
 * `number character`
 
#### Methods
 * `lessThan(Position pos): boolean`
 * `lessOrEqual(Position pos): boolean`
 * `greaterThan(Position pos): boolean`
 * `greaterOrEqual(Position pos): boolean`
 * `copy(): boolean`
 
### Class `CompletionItem`
#### Properties
 * `String[] commitCharacters`
 * `String[] additionalTextEdits`
 * `number start`
 * `number length`
-->

## The `dh.plot` namespace:

### Class `Figure`
Provides the details for a figure.

The Deephaven JS API supports automatic lossless downsampling of time-series data, when that data is plotted in one or
more line series. Using a scatter plot or a X-axis of some type other than DBDateTime will prevent this feature
from being applied to a series. To enable this feature, invoke `Axis.range(...)` to specify the length in pixels of the
axis on the screen, and the range of values that are visible, and the server will use that width (and range, if any) to
reduce the number of points sent to the client.

Downsampling can also be controlled when calling either `Figure.subscribe()` or `Series.subscribe()` - both can be given
an optional `dh.plot.DownsampleOptions` argument. Presently only two valid values exist, `DEFAULT`, and `DISABLE`, 
and if no argument is specified, `DEFAULT` is assumed. If there are more than 30,000 rows in a table, downsampling will
be encouraged - data will not load without calling `subscribe(DISABLE)` or enabling downsampling via `Axis.range(...)`.
If there are more than 200,000 rows, data will refuse to load without downsampling and `subscribe(DISABLE)` would have
no effect.

Downsampled data looks like normal data, except that select items have been removed if they would be redundant in the UI
given the current configuration. Individual rows are intact, so that a tooltip or some other UI item is sure to be 
accurate and consistent, and at least the highest and lowest value for each axis will be retained as well, to ensure that
the "important" values are visible.

Four events exist to help with interacting with downsampled data, all fired from the `Figure` instance itself. First,
`downsampleneeded` indicates that more than 30,000 rows would be fetched, and so specifying downsampling is no longer
optional - it must either be enabled (calling `axis.range(...)`), or disabled. If the figure is configured for downsampling,
when a change takes place that requires that the server perform some downsampling work, the `downsamplestarted` event
will first be fired, which can be used to present a brief loading message, indicating to the user why data is not ready
yet - when the server side process is complete, `downsamplefinished` will be fired. These events will repeat when the
range changes, such as when zooming, panning, or resizing the figure. Finally, `downsamplefailed` indicates that something
when wrong when downsampling, or possibly that downsampling cannot be disabled due to the number of rows in the table.

#### Properties
 * `String title` - Read-only. The title of the figure.
 * `Chart[] charts` - Read-only. The charts to draw.
 <!-- **Not for MVP:**
 * `String titleFont` - Read-only. The font for the title of this figure.
 * `boolean isResizable` - Read-only. True if this figure is resizable.
 * `boolean isDefaultTheme` - Read-only. True if this figure is using the default theme.
 * `Number updateInterval` - Read-only. The update interval for this figure in ms.
 * `Number cols` - Read-only. Number of columns used to arrange charts in this figure. See `Chart#colspan`.
 * `Number rows` - Read-only. Number of rows used to arrange charts in this figure. See `Chart#rowspan`. -->
 
#### Methods
 * `close()` - Close the figure, and clean up subscriptions.
 * `addEventListener(String type, function listener):Function` - listen for events on this figure. Returns a cleanup function
 * `removeEventListener(String type, function listener)` - Remove event listener. Alternative to cleanup function returned from addEventListener
 * `subscribe()` - Enable updates for all series in this figure.
 * `unsubscribe()` - Disable updates for all series in this figure.

#### Events
 * `updated` - The data within this figure was updated. `event.detail` is `FigureUpdateEventData`
 * `downsamplestarted` - The API is updating how downsampling works on this Figure, probably in response to a call to 
 `Axis.range()` or subscribe(). The `event.detail` value is an array of `Series` instances which are affected by this.
 * `downsamplefinished` - Downsampling has finished on the given `Series` instances, and data will arrive shortly. The
 `event.detail` value is the array of `Series` instances.
 * `downsamplefailed` - Downsampling failed for some reason on one or more series. The `event.detail` object has three
 properties, the `message` string describing what went wrong, the `size` number showing the full size of the table, and 
 the `series` property, an array of `Series` instances affected.
 * `downsampleneeded` - There are too many points to be drawn in the table which backs these series, and downsampling should
 be enabled. As an alternative, downsampling can be explicitly disabled, provided there are less than 200,000 rows in 
 the table.
 * `seriesadded` - A series used within this figure was added as part of a multi-series in a chart. The series instance is
 the detail for this event.

### Class `FigureUpdateEventData`
Indicates that an update has happened to one of the tables which back this figure. To use this data, iterate
through the `Series` instances and check the `sources` attribute of each to get the specific data to draw.

#### Properties
 * `Series[] series` - the series objects affected by this update. 
 
#### Methods
 * `getArray(Series, String):Any[]` - returns the entire array of contents for the given series and the
 specific data source in that series.

### Class `Chart`
Provide the details for a chart.

#### Properties
 * `String title` - Read-only. The title of the chart.
 * `ChartType chartType` - Read-only. The type of this chart, see `ChartType` enum for more details.
 * `Series[] series` - Read-only. The series data for display in this chart.
 * `MultiSeries[] multiSeries` - Read-only. The multi-series data for display in this chart
 * `Axis[] axes` - Read-only. The axes used in this chart.
 <!-- **Not for MVP:**
 * `Number colspan` - Read-only. The number of columns in the figure this chart should occupy.
 * `Number rowspan` - Read-only. The number of rows in the figure this chart should occupy.
 * `String chartType` - Read-only. The type of chart this is.
 * `String title` - Read-only. The title for this chart.
 * `String titleFont` - Read-only. The font to use for the title of this chart.
 * `String titleColor` - Read-only. The color to use for the title of this chart. Null for default client color.
 * `boolean isShownLegend` - Read-only. True to show the legend.
 * `String legendFont` - Read-only. The font to use for the legend.
 * `String legendColor` - Read-only. The color to use for the legend.
 * `boolean is3d` - Read-only. True if this chart is 3D. -->
 
#### Events
 * `seriesadded` - a new series was added to this chart as part of a multi-series. The series instance is the detail for 
 this event.

### Class `Series`
Provides access to the data for displaying in a figure.

#### Properties
 * `String name` - Read-only. The name for this series.
 * `SeriesPlotStyle plotStyle` - Read-only. The plotting style to use for this series. See `SeriesPlotStyle` enum for more details.
 * `Axis[] axes` - Read-only. The axes used in this series.
 * `SeriesDataSource[] sources` - Read-only. Contains details on how to access data within the chart for this series.
 keyed with the way that this series uses the axis.
 * `MultiSeries multiSeries` - indicates that this series belongs to a MultiSeries, null otherwise
 <!-- **Not for MVP:**
 * `String color` - Read-only. The color to use for this series, or null to use default palette.
 * `boolean linesVisible` - Read-only. True if lines are visible for this series.
 * `boolean shapesVisible` - Read-only. True if shapes are visible for this series.
 * `boolean isGradientVisible` - Read-only. True if gradient is visible for this series.
 * `String lineColor` - Read-only. The color to use for the line of this series. Null to use client default.
 * `String lineStyle` - Read-only. The style of line to use for this series. Null to use client default.
 * `String pointLabelFormat` - Read-only. The format to use for the point labels in this series.
 * `String xTooltipPattern` - Read-only. The format pattern for the x tooltip.
 * `String yTooltipPattern` - Read-only. The format pattern for the y tooltip.
 * `String shapeLabel` - Read-only. The label for the shape of this Series. Null for client default.
 * `Number shapeSize` - Read-only. The size of the shape for this series. Null for client default.
 * `String shapeColor` - Read-only. The color for the shape of this series. Null for client default.
 * `String shape` - Read-only. The shape to use for this series. Null for client default. -->
 
#### Methods
 * `subscribe()` - Enable updates for this Series.
 * `unsubscribe()` - Disable updates for this Series.
 
#### Plot Styles
 * `bar`, `stacked_bar`, `line`, `area`, `stacked_area`, `pie`, `histogram`, `ohlc`, `scatter`, `step`, `error_bar`

### Class `MultiSeries`
Describes a template that will be used to make new series instances when a new table added to a plotBy.

#### Properties
 * `SeriesPlotStyle plotStyle` - Read-only. The plotting style to use for the series that will be created. See `SeriesPlotStyle` enum for more details.
 * `String name` - Read-only. The name for this multi-series.

### Class `Axis`
Defines one axis used with by series. These instances will be found both on the Chart and the Series instances, and
may be shared between Series instances.

#### Properties
 * `String id` - Read-only. The unique id for this axis.
 * `String label` - Read-only. The label for this axis.
 * `AxisType type` - Read-only. The type for this axis, indicating how it will be drawn. See `AxisType` enum for more details.
 * `AxisPosition position` - Read-only. The position for this axis. See `AxisPosition` enum for more details.
 * `AxisFormatType formatType` - Read-only. The type for this axis. See `AxisFormatType` enum for more details.
 * `String formatPattern` - Read-only. The format pattern to use with this axis. Use the type to determine which type of formatter to use.
  * `BusinessCalendar businessCalendar` - The calendar with the business hours and holidays to transform plot data against. Defaults to null, or no transform.
 <!-- **Non-MVP:** 
 * `String format` - TODO: Review this prop with colin, doesn't make sense currently. Do we need way to get formatter or just `NumberFormat.getFormat(formatPattern)`?
 * `boolean isLog` - Read-only. True if this axis is logarithmic.
 * `String labelFont` - Read-only. The font to use for the axis label.
 * `String ticksFont` - Read-only. The font to use for the axis ticks.
 * `String color` - Read-only. The color to use for this axis, null to use client's default theme.
 * `Number minRange` - Read-only. The minimum range to display on this axis. Null to fit to data.
 * `Number maxRange` - Read-only. The maximum range to display on this axis. Null to fit to data.
 * `boolean isMinorTicksVisible` - Read-only.  True to display the minor ticks on this axis.
 * `Number gapBetweenMajorTicks` - Read-only. The gap between major ticks on this axis. Null for client to use default.
 * `Number[] getMajorTickLocations` - Read-only. The locations of the major ticks. Null for client to use default.
 * `String axisTransform` - Read-only. The transformation to perform on this axis.
 * `Number tickLabelAngle` - Read-only. The angle to display the tick labels. Null for client to use default.
 * `boolean isInvert` - Read-only. True if this axis is inverted.
 * `boolean isTimeAxis` - Read-only. True if this axis -->
 
### Methods
 * `range(number= widthInPixels, DateWrapper= optionalMinDate, DateWrapper= optionalMaxDate)` - Indicates that this axis
 is only `widthInPixels` wide, so any extra data can be downsampled out, if this can be done losslessly. The second two
 arguments represent the current zoom range of this axis, and if provided, most of the data outside of this range will
 be filtered out automatically and the visible width mapped to that range. When the UI zooms, pans, or resizes, this method
 should be called again to update these three values to ensure that data is correct and current.
 
### Class `SeriesDataSource`
Describes how to access and display data required within a series.

#### Properties
 * `Axis axis` - the axis that this source should be drawn on.
 * `SourceType type` - the feature of this series represented by this source. See the `SourceType` enum for more details.
 * `String columnType` - the type of data stored in the underlying table's Column.
 
### Enum `ChartType`
This enum describes what kind of chart is being drawn. This may limit what kinds of series can be found on it, or how
those series should be rendered.
 * `XY` - The chart will be drawn on a cartesian plane, with series items being plotting with X and Y positions, as lines,
 scattered points, areas, error bars, etc.
 * `PIE` - The chart will be a pie chart, with only pie series rendered on it.
 * `OHLC` - Similar to an XY plot, except that this will be TIME instead of X, and OPEN/HIGH/LOW/CLOSE instead of Y.
 * `CATEGORY` - Similar to a XY chart, except one of the axis will be based on a category instead of being numeric.
 * `XYZ` - Similar to XY, this chart will have three numeric dimensions, plotting each with an X, Y, and Z value.
 * `CATEGORY_3D` - Similar to a CATEGORY chart, this will have two category axes, and one numeric axis.
 
### Enum `AxisFormatType`
 * `CATEGORY` - Indicates that this axis will have discrete values rather than be on a continuous numeric axis.
 * `NUMBER` - Indicates that the values are numeric, and should be plotted on a continuous axis.
 
### Enum `AxisPosition`
 * `TOP` - The axis should be drawn at the top of the chart.
 * `BOTTOM` - The axis should be drawn at the bottom of the chart.
 * `LEFT` - The axis should be drawn at the left side of the chart.
 * `RIGHT` - The axis should be drawn at the right side of the chart.
 * `NONE` - No position makes sense for this axis, or the position is apparent from the axis type.
 
### Enum `AxisType`
 * `X` - Indicates that this is an X-axis, typically drawn on the bottom or top of the chart, depending on position attribute
 * `Y` - Indicates that this is a Y-Axis, typically drawn on the left or right of the chart, depending on position attribute
 * `Z` - Indicates that this is a Z-Axis, typically used as a "third" axis on a projection that makes it appear to be set at
 a right angle to both X and Y.
 * `SHAPE` - Indicates that this axis is used to represent that items when drawn as a point may have a specialized shape
 * `SIZE` - Indicates that this axis is used to represent that items when drawn as a point may have a specific size
 * `LABEL` - Indicates that this axis is used to represent that items when drawn as a point may have label specified from
 the underlying data
 * `COLOR` - Indicates that this axis is used to represent that items when drawn as a point may have a specific color

### Enum `SeriesPlotStyle`
 * `BAR` - Series items should each be draw as bars. Each item will have a unique category value to be drawn on the CATEGORY
 axis, and a numeric value drawn on the NUMBER axis.
 * `STACKED_BAR` - Like BAR, except there may be more than one such series, and they will share axes, and each subsequent
 series should have its items drawn on top of the previous one.
 * `LINE` - Series items will be drawn as points connected by a line, with two NUMBER axes.
 * `AREA` - Series items will be drawn as points connected by a line with a filled area under the line.
 * `STACKED_AREA` - Like AREA
 * `PIE`- Series items should each be draw as pie slices. Each item will have a unique category value to be drawn on the
 CATEGORY axis, and a numeric value drawn on the NUMBER axis.
 * `HISTOGRAM` - Series items with 6 data sources, three on X and three on Y, to represent the start/end/mid of each item. 
 * `OHLC` - Stands for "Open/High/Low/Close" series. Five numeric data sources exist, four on one axis (OPEN, HIGH, LOW, CLOSE),
 and TIME on the other axis.
 * `SCATTER` - Series items will be individually drawn as points, one two or three NUMBER axes
 <!-- not for MVP * `STEP` - like LINE, except the connecting lines are only parallel to the two axes - after each point a horizontal line is
 drawn until the line is directly below the subsequent point, then a vertical line is drawn up to that point. -->
 * `ERROR_BAR` - Series items with 6 data sources, three on X and three on Y, to represent the low/mid/high of each item.
 
### SourceType
This enum describes the source it is in, and how this aspect of the data in the series should be used to render the item.
For example, a point in a error-bar plot might have a X value, three Y values (Y, Y_LOW, Y_HIGH), and some COLOR per item - 
the three SeriesDataSources all would share the same Axis instance, but would have different SourceType enums set. The
exact meaning of each source type will depend on the series that they are in. 
 * `X` - LINE, AREA, STACKED_LINE, STACKED_AREA, ERROR_BAR, HISTOGRAM, SCATTER, STEP. Also used in PIE, but only to 
 identify the correct axis.
 * `Y` - LINE, AREA, STACKED_LINE, STACKED_AREA, ERROR_BAR, HISTOGRAM, SCATTER, STEP. Also used in PIE, but only to
  identify the correct axis.
 * `Z` - STACKED_AREA, SCATTER
 * `X_LOW` - ERROR_BAR, HISTOGRAM
 * `X_HIGH` - ERROR_BAR, HISTOGRAM
 * `Y_LOW` - ERROR_BAR, HISTOGRAM
 * `Y_HIGH` - ERROR_BAR, HISTOGRAM
 * `TIME` - OHLC
 * `OPEN` - OHLC
 * `HIGH` - OHLC
 * `LOW` - OHLC
 * `CLOSE` - OHLC
<!--
 * `SHAPE` - can be used in any series
 * `SIZE` - can be used in any series
 * `LABEL` - can be used in any series
 * `COLOR` - can be used in any series
-->
 
## The `dh.i18n` namespace:
 
### Class `NumberFormat`
Utility class to parse and format numbers, using the same format patterns as are supported by the standard Java
implementation used in the Deephaven server and swing client.

#### Constructor
 * `new dh.i18n.NumberFormat(pattern)` - Creates a new number format instance. This generally should be avoided in favor
 of the static `getFormat` function, which will create and cache an instance so that later calls share the same
 instance.
  
#### Methods
 * `parse(String text):Number` - Parses the given text using this instance's pattern into a JS Number.
 * `format(Object number):String` - Formats the specified number (or Java `long` value) using this instance's pattern.

#### Static functions
 * `getFormat(String pattern):NumberFormat` - Returns a number format instance matching the specified format. If this
 format has not been specified before, a new instance will be created and stored for later reuse.
 * `parse(String pattern, String text):Number` - Parses the given text using the cached format matching the given
 pattern.
 * `format(String pattern, Object number):String` - Formats the specified number (or Java `long` value) using the 
 cached format matching the given pattern string.

### Class `DateTimeFormat`
Utility class to parse and format various date/time values, using the same format patterns as are supported by the 
standard Java implementation used in the Deephaven server and swing client.

As Deephaven internally uses nanosecond precision to record dates, this API expects nanoseconds in most use cases, with
the one exception of the JS `Date` type, which is not capable of more precision than milliseconds. Note, however, that
when passing nanoseconds as a JS `Number` there is likely to be some loss of precision, though this is still supported
for easier interoperability with other JS code. The values returned by `parse()` will be an opaque object wrapping
the full precision of the specified date, However, this object supports `toString()` and `valueOf()` to return a string
representation of that value, as well as a `asNumber()` to return a JS `Number` value and a `asDate()` to return a JS
`Date` value. 

Caveats: 
 * The `D` format (for "day of year") is not supported by this implementation at this time.
 * The `%t` format for short timezone code is not supported by this implementation at this time, though `z` will work
 as expected in the browser to emit the user's own timezone. 

#### Constructor
 * `new dh.i18n.DateTimeFormat(String pattern)` - Creates a new date/time format instance. This generally should be avoided in favor
 of the static `getFormat` function, which will create and cache an instance so that later calls share the same
 instance.

#### Methods
 * `format(Object date, TimeZone tz):String` - Takes a variety of objects to interpret as a date, and formats them using this
 instance's pattern. Inputs can include a `String` value of a number expressed in nanoseconds, a `Number` value expressed
 in nanoseconds, a JS `Date` object (necessarily in milliseconds), or a wrapped Java `long` value, expressed in 
 nanoseconds. A `TimeZone` object can optionally be provided to format this date as the current date/time in that timezone.
 * `parse(String text, TimeZone tz):Object` - Parses the given string using this instance's pattern, and returns a wrapped Java
 `long` value in nanoseconds. A `TimeZone` object can optionally be provided to parse to a desired timezone.
 * `parseAsDate(String text):Date` - Parses the given string using this instance's pattern, and returns a JS `Date`
 object in milliseconds.

#### Static functions
 * `getFormat(String pattern):DateTimeFormat` - Returns a date format instance matching the specified format. If this
 format has not been specified before, a new instance will be created and stored for later reuse.
 * `format(String pattern, Object date, TimeZone tz):String` - Accepts a variety of input objects to interpret as a date, 
 and formats them using the specified pattern. A `TimeZone` object can optionally be provided to format this date as the 
 current date/time in that timezone.See the instance method for more details on input objects. 
 * `parse(String pattern, String text, TimeZone tz):Object` - Parses the given input string using the provided pattern, and returns
 a wrapped Java `long` value in nanoseconds. A `TimeZone` object can optionally be provided to parse to a desired timezone.
 * `parseAsDate(String pattern, String text):Date` - Parses the given input string using the provided pattern, and
 returns a JS `Date` object in milliseconds.

### Class `TimeZone`
Represents the timezones supported by Deephaven. Can be used to format dates, taking into account the offset changing
throughout the year (potentially changing each year). These instances mostly are useful at this time to pass to 
the `DateTimeFormat.format()` methods, though also support a few properties at this time to see details about each instance.

The following timezone codes are supported when getting a timezone object - instances appearing in the same line will
return the same details:

 * `GMT`/`UTC`
 * `Asia/Tokyo`
 * `Asia/Seoul`
 * `Asia/Hong_Kong`
 * `Asia/Singapore`
 * `Asia/Calcutta`/`Asia/Kolkata`
 * `Europe/Berlin`
 * `Europe/London`
 * `America/Sao_Paulo`
 * `America/St_Johns`
 * `America/Halifax`
 * `America/New_York`
 * `America/Chicago`
 * `America/Denver`
 * `America/Los_Angeles`
 * `America/Anchorage`
 * `Pacific/Honolulu`
 
 A Timezone object can also be created from an abbreviation.  The following abbreviations are supported:
 
  * `UTC`
  * `GMT`
  * `Z`
  * `NY`
  * `ET`
  * `EST`
  * `EDT`
  * `MN`
  * `CT`
  * `CST`
  * `CDT`
  * `MT`
  * `MST`
  * `MDT`
  * `PT`
  * `PST`
  * `PDT`
  * `HI`
  * `HST`
  * `HDT`
  * `BT`
  * `BRST`
  * `BRT`
  * `KR`
  * `KST`
  * `HK`
  * `HKT`
  * `JP`
  * `JST`
  * `AT`
  * `AST`
  * `ADT`
  * `NF`
  * `NST`
  * `NDT`
  * `AL`
  * `AKST`
  * `AKDT`
  * `IN`
  * `IST`
  * `CE`
  * `CET`
  * `CEST`
  * `SG`
  * `SGT`
  * `LON`
  * `BST`
  * `MOS`
  * `SHG`
  * `CH`
  * `NL`
  * `TW`
  * `SYD`
  * `AEST`
  * `AEDT`
 
#### Static functions:
 * `getTimeZone(String):TimeZone` - Factory method which creates timezone instances from one of the supported keys.

#### Properties:
 * `String id` - the timezone code that represents this `TimeZone`, usually the same key as was use to create this instance.
 * `Number standardOffset` - returns the standard offset of this timezone, in minutes.
 
## The `dh.calendar` namespace:
 
### Class `BusinessCalendar`
Defines a calendar with business hours and holidays.

#### Properties
  * `String name` - Read-only. The name of the calendar.
  * `TimeZone timeZone` - Read-only. The time zone of this calendar.
  * `DayOfWeek[] businessDays` - Read-only. The days of the week that are business days.
  * `BusinessPeriod[] businessPeriods` - Read-only. The business periods that are open on a business day.
  * `Holiday[] holidays` - Read-only. All holidays defined for this calendar.

### Enum `DayOfWeek`
  * `SUNDAY`, `MONDAY`, `TUESDAY`, `WEDNESDAY`, `THURSDAY`, `FRIDAY`, `SATURDAY`

### Class `BusinessPeriod`
  * `String open` - Read-only. Time in "HH:mm" format of when the business opens on a business day.
  * `String close` - Read-only. Time in "HH:mm" format of when the business closes on a business day.

### Class `Holiday`
  * `Date date` - Read-only. The date of the Holiday.
  * `BusinessPeriod[] businessPeriods` - Read-only. The business periods that are open on the holiday.
