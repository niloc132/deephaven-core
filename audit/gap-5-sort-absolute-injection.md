# Gap 5: `SortTableGrpcImpl` — Absolute sort column name injection

**Severity:** ~~Medium~~ → **Not exploitable** (downgraded after testing)
**File:** `server/src/main/java/io/deephaven/server/table/ops/SortTableGrpcImpl.java`

When a sort descriptor has `isAbsolute=True`, the column name from the gRPC request is **string-interpolated** into a formula via `AbsoluteSortColumnConventions.makeSelectable()`:

```java
// AbsoluteSortColumnConventions.java
public static Selectable makeSelectable(String absoluteColumnName, String baseColumnName) {
    return FormulaColumn.createFormulaColumn(absoluteColumnName, "abs(" + baseColumnName + ")");
}
```

However, this is applied via `result.updateView(absViews)`, which is **lazy** — the formula
is never compiled or evaluated until column values are actually read. The sort path fails
before that happens (e.g. `ColumnName.of()` rejects the malicious name), so the injected
code never executes.

If this were `update()` instead of `updateView()`, the formula would evaluate eagerly and
the injection would succeed. As written, this is a code smell (unsanitized string
interpolation into a formula) but not practically exploitable via this path.

---

_Notes:_

The formula is compiled to Java, but never run because it is an updateView(), and the downstream sort() fails to even
be called since the string fails the formula name validation check.
