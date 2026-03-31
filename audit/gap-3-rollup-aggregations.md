# Gap 3: `HierarchicalTableServiceGrpcImpl.rollup()` — Aggregation formulas/filters not validated

**Severity:** High
**File:** `server/src/main/java/io/deephaven/server/hierarchicaltable/HierarchicalTableServiceGrpcImpl.java`

Aggregations supplied to the rollup request are adapted via `AggregationAdapter::adapt` and passed directly to `sourceTable.rollup()`. This means:
- `AggregationCountWhere` filter strings
- `AggregationFormula` formula expressions
- `AggregationColumns` with `AggSpecFormula`

…all reach the engine without `ColumnExpressionValidator` validation.

```java
// HierarchicalTableServiceGrpcImpl.java:rollup — no formula validation
final Collection<? extends Aggregation> aggregations = request.getAggregationsList().stream()
        .map(AggregationAdapter::adapt)
        .collect(Collectors.toList());
final RollupTable result = sourceTable.rollup(aggregations, includeConstituents, groupByColumns);
```

---

_Notes:_
The server reports that formula() isn't supported for rollups, though that might just be a bug where it is missing for
gRPC, so at this time, we're not vulnerable.

