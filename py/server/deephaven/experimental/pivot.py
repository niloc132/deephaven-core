#
# Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
#
import jpy

from typing import Union

from deephaven.agg import Aggregation
from deephaven.jcompat import j_array_list, to_sequence
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


def create_pivot(table: Table, col_column_names: Union[str,list[str]], row_column_names: Union[str,list[str]], value_col_name: str, agg: Aggregation, include_totals:bool = False, pivot_description: Union[str] = None) -> Pivot:
    """
    Creates a new pivot table widget from the given table and the specified parameters.

    :param table: The table to pivot
    :param col_column_names: The column to read from the table to produce the pivot columns
    :param row_column_names: The column to read from the table to produce the pivot rows
    :param value_col_name: The column name to aggregate to produce the pivot cells
    :param agg: The aggregation to use for the pivot cells
    :param include_totals: True to include a totals row, column, and grand totals cell
    :param pivot_description: Optional description for the pivot cells, if unspecified, the pivot will use f"{agg} of {value_col_name}".
    :return: A Pivot widget instance
    """
    return Pivot(j_pivot_table=_JPivotTable.FACTORY.create(
        table.j_table,
        j_array_list(to_sequence(col_column_names)),
        j_array_list(to_sequence(row_column_names)),
        value_col_name,
        agg.j_agg_spec,
        include_totals,
        pivot_description
    ))


"""
Factory instance that can be imported as a plugin to be accessible to clients. Do not import this into the global scope
if you do not want clients to access it.
"""
PIVOT_FACTORY = _JPivotTable.FACTORY