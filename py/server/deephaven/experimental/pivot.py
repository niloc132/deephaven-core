#
# Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
#
import jpy

from deephaven.agg import Aggregation
from deephaven.jcompat import j_array_list
from deephaven._wrapper import JObjectWrapper
from deephaven.table import Table

_JPivotTable = jpy.get_type("io.deephaven.simplepivot.SimplePivotTable")


class Pivot(JObjectWrapper):
    j_object_type = _JPivotTable
    def __init__(self, j_pivot_table):
        self.j_pivot_table = j_pivot_table

    @property
    def j_object(self) -> jpy.JType:
        return self.j_pivot_table


def create_pivot(table: Table, col_column_names: list[str], row_column_names: list[str], value_col_name: str, agg: Aggregation, includeTotals:bool) -> Pivot:
    """
    Creates a new pivot table widget from the given table and the specified parameters.
    :param table:
    :param col_column_names:
    :param row_column_names:
    :param value_col_name:
    :param agg:
    :param includeTotals:
    :return:
    """
    return Pivot(j_pivot_table=_JPivotTable.FACTORY.create(table.j_table, j_array_list(col_column_names), j_array_list(row_column_names), value_col_name, agg.j_agg_spec, includeTotals))


"""
Factory instance that can be imported as a plugin to be accessible to clients. Do not import this into the global scope
if you do not want clients to access it.
"""
PIVOT_FACTORY = _JPivotTable.FACTORY