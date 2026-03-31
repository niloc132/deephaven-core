# Gap 4: `RangeJoinGrpcImpl` — Aggregation formulas/filters not validated

**Severity:** High
**File:** `server/src/main/java/io/deephaven/server/table/ops/RangeJoinGrpcImpl.java`

Same pattern as Gap 3. Aggregations in the range-join request are adapted and passed through unvalidated. Any `AggregationCountWhere`, `AggregationFormula`, or `AggregationColumns` with `AggSpecFormula` reach the engine without `ColumnExpressionValidator` validation.

```java
// RangeJoinGrpcImpl.java:create — no formula validation
final Collection<? extends io.deephaven.api.agg.Aggregation> aggregations = request.getAggregationsList()
        .stream().map(AggregationAdapter::adapt).collect(Collectors.toList());
return leftTable.rangeJoin(rightTable, exactMatches, rangeMatch, aggregations);
```

---

_Notes:_

RangeJoin doesn't accept AggFormula, so this isn't a vulnerability at this time.
