# Gap 2: `AggregateGrpcImpl` — `AggregationColumns` with `AggSpecFormula` not validated

**Severity:** High
**File:** `server/src/main/java/io/deephaven/server/table/ops/AggregateGrpcImpl.java`

The `validateFormulas()` method only inspects `countWhere` and `formula` (i.e., the `AggregationFormula` type). It does **not** inspect the `spec` inside an `AggregationColumns` when that spec is `AggSpecFormula`. The formula string inside the spec reaches the engine unvalidated.

```java
// AggregateGrpcImpl.java:validateFormulas — misses COLUMNS with AggSpecFormula
private void validateFormulas(Aggregation agg, Table parent, List<ColumnName> groupByColumns) {
    if (agg.hasCountWhere()) { /* validated */ }
    if (agg.hasFormula()) { /* validated */ }
    // ← if agg is COLUMNS with an AggSpecFormula spec, nothing is checked
}
```

---

_Notes:_

Confirmed vulnerable.
