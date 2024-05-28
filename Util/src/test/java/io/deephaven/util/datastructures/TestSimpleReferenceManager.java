//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.util.datastructures;

import io.deephaven.base.reference.HardSimpleReference;
import io.deephaven.base.reference.SimpleReference;
import io.deephaven.util.mutable.MutableInt;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Unit tests for {@link SimpleReferenceManager}.
 */
public class TestSimpleReferenceManager {

    @Test
    public void testConcurrent() {
        doTest(true);
    }

    @Test
    public void testSerial() {
        doTest(false);
    }

    @SuppressWarnings({"NumberEquality", "PointlessArithmeticExpression"})
    private void doTest(final boolean concurrent) {
        final HardSimpleReference<Integer>[] items =
                IntStream.range(0, 1000).mapToObj(HardSimpleReference::new).toArray(HardSimpleReference[]::new);
        final SimpleReferenceManager<Integer, HardSimpleReference<Integer>> SUT =
                new SimpleReferenceManager<>(val -> items[val], concurrent);

        Arrays.stream(items, 0, 500).map(SimpleReference::get).forEach(SUT::add);

        int expectedSum = 500 * (499 + 0) / 2;
        testSumExpectations(SUT, expectedSum);

        Arrays.stream(items, 0, 500).forEach(item -> TestCase.assertSame(item.get(),
                SUT.getFirstItem((final Integer other) -> item.get() == other)));
        Arrays.stream(items, 0, 500).forEach(item -> TestCase.assertSame(item,
                SUT.getFirstReference((final Integer other) -> item.get() == other)));

        items[200].clear();
        expectedSum -= 200;
        TestCase.assertSame(items[199].get(), SUT.getFirstItem((final Integer other) -> items[199].get() == other));
        TestCase.assertNull(SUT.getFirstItem((final Integer other) -> items[200].get() == other));
        TestCase.assertSame(items[201].get(), SUT.getFirstItem((final Integer other) -> items[201].get() == other));
        testSumExpectations(SUT, expectedSum);

        items[300].clear();
        expectedSum -= 300;
        TestCase.assertSame(items[299], SUT.getFirstReference((final Integer other) -> items[299].get() == other));
        TestCase.assertNull(SUT.getFirstReference((final Integer other) -> items[300].get() == other));
        TestCase.assertSame(items[301], SUT.getFirstReference((final Integer other) -> items[301].get() == other));
        testSumExpectations(SUT, expectedSum);

        items[400].clear();
        expectedSum -= 400;
        testSumExpectations(SUT, expectedSum);

        Arrays.stream(items, 500, 1000).map(SimpleReference::get).forEach(SUT::add);
        expectedSum += 500 * (999 + 500) / 2;
        testSumExpectations(SUT, expectedSum);

        SUT.removeAll(Arrays.stream(Arrays.copyOfRange(items, 600, 700)).map(SimpleReference::get)
                .collect(Collectors.toList()));
        Arrays.stream(items, 700, 800).forEach(SimpleReference::clear);
        expectedSum -= 200 * (799 + 600) / 2;
        testSumExpectations(SUT, expectedSum);

        Arrays.stream(items, 0, 100).forEach(SimpleReference::clear);
        SUT.remove(items[0].get());
        expectedSum -= 100 * (99 + 0) / 2;
        testSumExpectations(SUT, expectedSum);
    }

    private void testSumExpectations(
            @NotNull final SimpleReferenceManager<Integer, ? extends SimpleReference<Integer>> SUT,
            final int expectedSum) {
        final MutableInt sum = new MutableInt();
        SUT.forEach((ref, item) -> {
            TestCase.assertSame(ref.get(), item);
            sum.add(item);
        });
        TestCase.assertEquals(expectedSum, sum.get());
    }
}
