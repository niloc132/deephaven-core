# Gap 1: `AggregateAllGrpcImpl` — `AggSpecFormula` not validated

**Severity:** High
**File:** `server/src/main/java/io/deephaven/server/table/ops/AggregateAllGrpcImpl.java`

When the `AggSpec` type is `FORMULA`, the formula string and paramToken are passed directly to `parent.aggAllBy()` with **no** `ColumnExpressionValidator` call. The adapter (`AggSpecAdapter.adapt(AggSpecFormula)`) converts `formula.getFormula()` straight to `AggSpec.formula(formula, paramToken)`.

```java
// AggregateAllGrpcImpl.java — no validation of formula content
final AggSpec spec = AggSpecAdapter.adapt(request.getSpec()); // may contain AggSpecFormula
return parent.aggAllBy(spec, request.getGroupByColumnsList());
```

---

_Notes:_

Confirmed vulnerable.
