//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.plot.example_plots;

import io.deephaven.plot.Figure;
import io.deephaven.plot.FigureFactory;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.util.TableTools;


public class CatErrorPlotBy {

    public static void main(String[] args) {
        final String[] usym = {"A", "B", "A", "B"};
        final String[] cats = {"A", "B", "C", "D"};
        final double[] values = {5, 4, 4, 3};
        final double[] lowValues = {4, 3, 3, 2};
        final double[] highValues = {6, 5, 5, 4};

        Table t = TableTools.newTable(TableTools.col("USym", usym),
                TableTools.col("Cats", cats),
                TableTools.doubleCol("Values", values),
                TableTools.doubleCol("Low", lowValues),
                TableTools.doubleCol("High", highValues)).update("Cats = i % 2 == 0 ? null : Cats").update("A = `A`")
                .update("B = `B`");

        TableTools.show(t);

        Figure fig = FigureFactory.figure()
                .catPlotBy("Test1", t, "Cats", "Values", "A").show();

        ExamplePlotUtils.display(fig);
    }
}
