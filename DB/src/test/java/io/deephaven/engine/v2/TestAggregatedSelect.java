/*
 * Copyright (c) 2016-2021 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.engine.v2;

import io.deephaven.base.FileUtils;
import io.deephaven.engine.tables.ColumnDefinition;
import io.deephaven.engine.tables.*;
import io.deephaven.engine.tables.dbarrays.DoubleVector;
import io.deephaven.engine.tables.dbarrays.ObjectVector;
import io.deephaven.engine.tables.dbarrays.Vector;
import io.deephaven.engine.tables.utils.ArrayUtils;
import io.deephaven.libs.primitives.DoubleNumericPrimitives;
import io.deephaven.engine.tables.utils.TableTools;
import io.deephaven.engine.tables.utils.ParquetTools;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.nio.file.Files;

import static io.deephaven.engine.tables.utils.TableTools.*;

public class TestAggregatedSelect extends TestCase {

    public TestAggregatedSelect() {
        super("TestAggregatedSelect()");
    }

    private static File tableDirectory;

    @Before
    public void setUp() {
        try {
            tableDirectory = Files.createTempDirectory("TestAggregatedSelect").toFile();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @After
    public void tearDown() {
        FileUtils.deleteRecursivelyOnNFS(tableDirectory);
    }

    public Table createTestTable() {
        FileUtils.deleteRecursively(tableDirectory);

        TableDefinition tableDefinition = TableDefinition.of(
                ColumnDefinition.ofString("USym"),
                ColumnDefinition.ofDouble("Bid"),
                ColumnDefinition.ofDouble("BidSize"));

        final int size = 40;

        String[] symbol = new String[size];
        double[] bid = new double[size];
        double[] bidSize = new double[size];

        for (int ii = 0; ii < size; ++ii) {
            symbol[ii] = (ii < 8) ? "ABC" : "XYZ";
            bid[ii] = (ii < 15) ? 98 : 99;
            bidSize[ii] = ii;
        }

        tableDirectory.mkdirs();
        final File dest = new File(tableDirectory, "Table.parquet");
        ParquetTools.writeTable(
                newTable(stringCol("USym", symbol), doubleCol("Bid", bid), doubleCol("BidSize", bidSize)),
                dest,
                tableDefinition);
        return ParquetTools.readTable(dest);
    }

    Table doAggregatedQuery() {
        Table t = createTestTable();
        Table t2 = t.by("USym", "Bid").by("USym");
        return t2;
    }

    double avgConsecutive(int start, int end) {
        double count = (end - start) + 1;
        double sumEnd = (end * (end + 1)) / 2;
        double sumStart = (start * (start + 1)) / 2;
        return (sumEnd - sumStart) / count;
    }

    public void testSelectType() {
        Table table = createTestTable();
        Table selectedTable = table.select();

        String[] colNames = {"USym", "Bid", "BidSize"};
        for (String colName : colNames) {
            DataColumn dcFresh = table.getColumn(colName);
            DataColumn dcSelected = selectedTable.getColumn(colName);
            TestCase.assertEquals(dcFresh.getType(), dcSelected.getType());
            TestCase.assertEquals(dcFresh.getComponentType(), dcSelected.getComponentType());
        }
    }

    public void testUngroup() {
        Table freshTable = doAggregatedQuery();

        Table t1 = freshTable.dropColumns("BidSize");
        TableTools.show(t1);

        System.out.println("Avg Check 1");
        Table t3 = t1.update("Bid=avg(Bid)");
        TableTools.show(t3);

        System.out.println("Group Check");
        Table t2 = t1.ungroup();
        TableTools.show(t2);

        System.out.println("After Select");
        t2 = t1.select();

        String[] colNames = {"Bid", "USym"};
        for (String colName : colNames) {
            DataColumn dcFresh = t1.getColumn(colName);
            DataColumn dcSelected = t2.getColumn(colName);
            TestCase.assertEquals(dcFresh.getType(), dcSelected.getType());
            TestCase.assertEquals(dcFresh.getComponentType(), dcSelected.getComponentType());
        }

        t2 = t2.ungroup();
        TableTools.show(t2);

        System.out.println("With nested array:");
        Table s1 = freshTable.dropColumns("Bid");

        Table s1s = s1.select();
        colNames[0] = "BidSize";
        for (String colName : colNames) {
            DataColumn dcFresh = s1.getColumn(colName);
            DataColumn dcSelected = s1s.getColumn(colName);
            TestCase.assertEquals(dcFresh.getType(), dcSelected.getType());
            TestCase.assertEquals(dcFresh.getComponentType(), dcSelected.getComponentType());
        }

        TableTools.show(s1);
        System.out.println("ungrouped:");
        s1 = s1.ungroup();
        TableTools.show(s1);

        Table s2 = s1.update("BidSize=avg(BidSize)");
        TableTools.show(s2);

        Table s3 = s1.select();
        Table s4 = s3.update("BidSize=avg(BidSize)");
        TableTools.show(s4);
    }

    public void testSerializedAggregation() throws IOException, ClassNotFoundException {
        Table withDiskBackedColumns = doAggregatedQuery();
        Table toBeSerialized = withDiskBackedColumns.select();

        DataColumn dc1 = withDiskBackedColumns.getColumn("Bid");
        DataColumn dc2 = toBeSerialized.getColumn("Bid");
        TestCase.assertEquals(dc1.getType(), dc2.getType());
        TestCase.assertEquals(dc1.getComponentType(), dc2.getComponentType());

        dc1 = withDiskBackedColumns.getColumn("BidSize");
        dc2 = toBeSerialized.getColumn("BidSize");
        TestCase.assertEquals(dc1.getType(), dc2.getType());
        TestCase.assertEquals(dc1.getComponentType(), dc2.getComponentType());

        dc1 = withDiskBackedColumns.getColumn("USym");
        dc2 = toBeSerialized.getColumn("USym");
        TestCase.assertEquals(dc1.getType(), dc2.getType());
        TestCase.assertEquals(dc1.getComponentType(), dc2.getComponentType());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(toBeSerialized);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Table result = (Table) objectInputStream.readObject();

        TableTools.show(result);

        dumpColumn(result.getColumn("Bid"));
        dumpColumn(result.getColumn("BidSize"));

        // noinspection unchecked
        DataColumn<DoubleVector> bidColumn = result.getColumn("Bid");
        // noinspection unchecked
        DataColumn<ObjectVector<DoubleVector>> bidSizeColumn = result.getColumn("BidSize");

        TestCase.assertTrue(DoubleVector.class.isAssignableFrom(bidColumn.getType()));
        TestCase.assertEquals(double.class, bidColumn.getComponentType());
        TestCase.assertEquals(2, bidColumn.size());

        TestCase.assertTrue(ObjectVector.class.isAssignableFrom(bidSizeColumn.getType()));
        // The tables strip this of its desired Type, instead it becomes an object.
        TestCase.assertTrue(DoubleVector.class.isAssignableFrom(bidSizeColumn.getComponentType()));
        TestCase.assertEquals(2, bidSizeColumn.size());

        int[] expectedSize = {1, 2};
        for (int ii = 0; ii < bidColumn.size(); ++ii) {
            DoubleVector bidArray = bidColumn.get(ii);
            ObjectVector<DoubleVector> bidSizeArray = bidSizeColumn.get(ii);

            TestCase.assertEquals(expectedSize[ii], bidArray.size());
            TestCase.assertEquals(expectedSize[ii], bidSizeArray.size());

            TestCase.assertTrue(double.class.isAssignableFrom(bidArray.getComponentType()));
            TestCase.assertTrue(DoubleVector.class.isAssignableFrom(bidSizeArray.getComponentType()));

            for (int jj = 0; jj < bidSizeArray.size(); ++jj) {
                DoubleVector bidSizeInnerArray = bidSizeArray.get(jj);
                TestCase.assertTrue(double.class.isAssignableFrom(bidSizeInnerArray.getComponentType()));
            }
        }

        TestCase.assertEquals(98.0, DoubleNumericPrimitives.avg(bidColumn.get(0)));
        TestCase.assertEquals(98.5, DoubleNumericPrimitives.avg(bidColumn.get(1)));
        TestCase.assertEquals(avgConsecutive(0, 7), DoubleNumericPrimitives.avg(bidSizeColumn.get(0).get(0)));

        Table checkPrimitives = result.update("BidAvg=avg(Bid)");
        TableTools.show(checkPrimitives);
    }

    private void dumpColumn(DataColumn dc) {
        boolean isArray = Vector.class.isAssignableFrom(dc.getType());
        System.out.println("Column Type: " + dc.getType().toString() + (isArray ? " (Array)" : "") + ", ComponentType: "
                + dc.getComponentType());

        for (int ii = 0; ii < dc.size(); ++ii) {
            String prefix = dc.getName() + "[" + ii + "]";
            if (isArray) {
                Vector vector = (Vector) dc.get(ii);
                dumpArray(prefix, vector);
            } else {
                System.out.println(prefix + ":" + dc.get(ii).toString());
            }
        }
    }

    private void dumpArray(String prefix, Vector vector) {
        System.out.println(prefix + ": Array of " + vector.getComponentType().toString());
        String prefixsp = new String(new char[prefix.length()]).replace('\0', ' ');
        final boolean containsArrays = Vector.class.isAssignableFrom(vector.getComponentType());
        final ArrayUtils.ArrayAccessor<?> arrayAccessor = ArrayUtils.getArrayAccessor(vector.toArray());
        for (int jj = 0; jj < vector.size(); ++jj) {
            if (containsArrays) {
                dumpArray(prefix + "[" + jj + "] ", (Vector) arrayAccessor.get(jj));
            } else {
                System.out.println(prefixsp + "[" + jj + "]: " + arrayAccessor.get(jj).toString());
            }
        }
    }
}
