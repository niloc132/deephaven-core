# Audit: ColumnExpressionValidator Coverage for gRPC Table Operations

## Scope
All gRPC service paths (excluding `ConsoleService`) that pass user-provided strings from protobuf messages to the engine as column expressions, formulas, or filter strings — examining whether `ColumnExpressionValidator` is invoked before execution.

---

## ✅ Operations Correctly Validated

| gRPC Operation(s) | Class | What's validated |
|---|---|---|
| `Update`, `LazyUpdate`, `View`, `UpdateView`, `Select` | `UpdateOrSelectGrpcImpl` | `columnSpecs` → `validateColumnExpressions()` |
| `Filter` (structured `Condition`) | `FilterTableGrpcImpl` | `WhereFilter` list → `validateWhereFilters()` |
| `UnstructuredFilter` (raw strings) | `UnstructuredFilterTableGrpcImpl` | filter strings → `validateSelectFilters()` |
| `HeadBy`, `TailBy` | `HeadOrTailByGrpcImpl` | `groupByColumnSpecs` → `validateColumnExpressions()` |
| `Aggregate` — `CountWhere` agg | `AggregateGrpcImpl` | filter strings → `validateSelectFilters()` |
| `Aggregate` — `Formula` agg (type `RAW`) | `AggregateGrpcImpl` | formula string → `validateColumnExpressions()` |
| `UpdateBy` — `RollingFormula` | `UpdateByGrpcImpl` | formula string → `validateColumnExpressions()` |
| `UpdateBy` — `CountWhere` / `RollingCountWhere` | `UpdateByGrpcImpl` | filter strings → `validateSelectFilters()` |
| `HierarchicalTable#apply` — filters | `HierarchicalTableServiceGrpcImpl` | `WhereFilter` list → `validateWhereFilters()` |
| `HierarchicalTable#apply` — updateViews | `HierarchicalTableServiceGrpcImpl` | column specs → `validateColumnExpressions()` |
| `HierarchicalTable#apply` — formatViews | `HierarchicalTableServiceGrpcImpl` | column specs → `validateColumnExpressions()` |

## ✅ Operations That Don't Accept Arbitrary Expressions (Safe by Design)

| gRPC Operation(s) | Class | Why safe |
|---|---|---|
| `EmptyTable` | `EmptyTableGrpcImpl` | Only takes `size` (long) |
| `TimeTable` | `TimeTableGrpcImpl` | Only takes period/start-time |
| `MergeTables` | `MergeTablesGrpcImpl` | Only table references |
| `DropColumns` | `DropColumnsGrpcImpl` | Column names; fails if column doesn't exist |
| `SelectDistinct` | `SelectDistinctGrpcImpl` | Explicitly checks column names exist; rejects unknowns |
| `Flatten`, `Snapshot`, `MetaTable`, `FetchTable` | Various | No user-provided strings |
| `Slice`, `Head`, `Tail` | `SliceGrpcImpl`, `HeadOrTailGrpcImpl` | Numeric parameters only |
| `Ungroup` | `UngroupGrpcImpl` | Column names via `ColumnName.of()` (validated) |
| `WhereIn` | `WhereInGrpcImpl` | Uses `JoinMatch.parse()` — column name pairs only |
| `NaturalJoin`, `ExactJoin`, `CrossJoin`, `LeftJoin`, `AsOfJoin` | `JoinTablesGrpcImpl` | Uses `MatchPairFactory` — column name pairs only |
| `Aj`, `Raj` | `AjRajGrpcImpl` | `JoinMatch.parse()`, `JoinAddition.parse()` — column name pairs |
| `SnapshotWhen` | `SnapshotWhenTableGrpcImpl` | `JoinAddition.parse()` — column name pairs |
| `MultiJoin` | `MultiJoinGrpcImpl` | Column match/add strings — column name pairs |
| `CreateInputTable` | `CreateInputTableGrpcImpl` | Schema-based; no formulas |
| `ApplyPreviewColumns` | `ApplyPreviewColumnsGrpcImpl` | No user expressions |
| `ColumnStatistics` | `ColumnStatisticsGrpcImpl` | Takes a column name, looked up from definition |
| `RunChartDownsample` | `RunChartDownsampleGrpcImpl` | Takes column names only |
| `ComboAggregate` | `ComboAggregateGrpcImpl` | `matchPairs` are column name pairs via `Pair.parse()`; `groupByColumns` validated via `NameValidator`; `columnName` is a column reference |
| `PartitionBy`, `Merge`, `GetTable` | `PartitionedTableServiceGrpcImpl` | Column names only |
| `Tree` | `HierarchicalTableServiceGrpcImpl` | `identifierColumn` / `parentIdentifierColumn` are column names |

---

## ❌ Gaps Found — Missing ColumnExpressionValidator

See individual gap files for details:

| # | File | Location | Attack Vector | Severity |
|---|---|---|---|---|
| 1 | [gap-1-aggregate-all.md](gap-1-aggregate-all.md) | `AggregateAllGrpcImpl` | `AggSpecFormula.formula` string | High |
| 2 | [gap-2-aggregate-columns-spec.md](gap-2-aggregate-columns-spec.md) | `AggregateGrpcImpl` (COLUMNS type) | `AggSpecFormula.formula` inside `AggregationColumns` | High |
| 3 | [gap-3-rollup-aggregations.md](gap-3-rollup-aggregations.md) | `HierarchicalTableServiceGrpcImpl.rollup()` | Formula/filter strings in aggregations | High |
| 4 | [gap-4-range-join-aggregations.md](gap-4-range-join-aggregations.md) | `RangeJoinGrpcImpl` | Formula/filter strings in aggregations | High |
| 5 | [gap-5-sort-absolute-injection.md](gap-5-sort-absolute-injection.md) | `SortTableGrpcImpl` | Column name injection via `isAbsolute` sort | ~~Medium~~ Not exploitable (`updateView` is lazy) |

## ⚠️ Code Quality Note (Not a Security Gap)

### `AggregateGrpcImpl` — Missing `break` in switch (fall-through)
**File:** `AggregateGrpcImpl.java:validateFormulas`, lines ~83-96

The `RAW` case in the selectable type switch falls through to `TYPE_NOT_SET`:
```java
case RAW:
    // ... validation happens ...
    expressionValidator.validateColumnExpressions(...);
case TYPE_NOT_SET:  // ← fall-through!
    break;
```
The validation *does* execute for `RAW`, so this is not a security bypass, but it's confusing and should have an explicit `break` after the `RAW` case.

