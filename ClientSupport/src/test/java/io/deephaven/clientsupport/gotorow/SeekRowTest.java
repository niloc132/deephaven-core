//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.clientsupport.gotorow;

import io.deephaven.engine.table.Table;
import io.deephaven.engine.testutil.junit4.EngineCleanup;
import io.deephaven.engine.util.TableTools;
import org.junit.Rule;
import org.junit.Test;

import static io.deephaven.engine.util.TableTools.intCol;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SeekRowTest {

    @Rule
    public final EngineCleanup framework = new EngineCleanup();

    /**
     * Helper to run SeekRow and validate that the discovered row is what was expected. Validates the
     * {@code expectedPosition} before running, to ensure test data makes sense.
     *
     * @param seekValue the value to search for
     * @param t the table to search
     * @param seekForward true to seek forward, false to seek backward
     * @param currentPosition the position to start searching
     * @param expectedPosition the next expected position of the seek value
     */
    private static void assertSeekPosition(int seekValue, Table t, boolean seekForward, int currentPosition,
            int expectedPosition) {
        if (expectedPosition != -1) {
            // Confirm that the expected position matches
            assertEquals(seekValue, t.flatten().getColumnSource("num").getInt(expectedPosition));
        } else {
            // Confirm that the value actually doesn't exist
            assertTrue(t.where("num" + "=" + seekValue).isEmpty());
        }
        // Actually perform the requested assertion
        SeekRow seek = new SeekRow(currentPosition, "num", seekValue, false, false, !seekForward);
        assertEquals(expectedPosition, seek.seek(t));
    }

    @Test
    public void emptyTable() {
        Table t = TableTools.newTable(intCol("num"));

        assertSeekPosition(1, t, true, 0, -1);
        assertSeekPosition(1, t, false, 0, -1);

        // repeat with sorted
        t = t.sort("num");
        assertSeekPosition(1, t, true, 0, -1);
        assertSeekPosition(1, t, false, 0, -1);
    }

    @Test
    public void singleRow() {
        Table t = TableTools.newTable(intCol("num", 1));
        assertSeekPosition(1, t, true, 0, 0);
        assertSeekPosition(1, t, false, 0, 0);

        assertSeekPosition(100, t, false, 0, -1);
        assertSeekPosition(100, t, true, 0, -1);

        // repeat with sorted
        t = t.sort("num");
        assertSeekPosition(1, t, true, 0, 0);
        assertSeekPosition(1, t, false, 0, 0);

        assertSeekPosition(100, t, false, 0, -1);
        assertSeekPosition(100, t, true, 0, -1);

        // repeat with sorted descending
        t = t.sortDescending("num");
        assertSeekPosition(1, t, true, 0, 0);
        assertSeekPosition(1, t, false, 0, 0);

        assertSeekPosition(100, t, false, 0, -1);
        assertSeekPosition(100, t, true, 0, -1);
    }

    /**
     * Helper to seek from every row in a table, and assert that a valid value can be found in a valid position from
     * each.
     * 
     * @param seekValue the value to search for
     * @param t the table to search
     * @param forwardPosition expected positions when searching forward, indexed on starting row
     * @param backwardPosition expected positions when searching backwards, indexed on starting row
     */
    private static void assertSeekPositionAllRows(int seekValue, Table t, int[] forwardPosition,
            int[] backwardPosition) {
        assertEquals(t.size(), forwardPosition.length);
        assertEquals(t.size(), backwardPosition.length);
        for (int i = 0; i < t.size(); i++) {
            // seek from the current position, confirm we get the expected position
            assertSeekPosition(seekValue, t, true, i, forwardPosition[i]);
            assertSeekPosition(seekValue, t, false, i, backwardPosition[i]);
        }
    }

    /**
     * Helper to verify that a given value can not be found no matter which row is started from
     * 
     * @param impossibleValue a value that isn't present in the table
     * @param t the table to search
     */
    public static void assertNotFound(int impossibleValue, Table t) {
        // ensure we can't find values that don't exist
        for (int i = 0; i < t.size(); i++) {
            assertSeekPosition(impossibleValue, t, true, i, -1);
            assertSeekPosition(impossibleValue, t, false, i, -1);
        }
    }

    @Test
    public void ascendingSortedContentSeek() {
        Table t = TableTools.newTable(intCol("num", 1, 1, 2, 3, 3, 4, 4));

        int seekValue = 3;
        int[] forwardPosition = {3, 3, 3, 4, 3, 3, 3};
        int[] backwardPosition = {4, 4, 4, 4, 3, 4, 4};
        assertSeekPositionAllRows(seekValue, t, forwardPosition, backwardPosition);
        assertNotFound(6, t);

        // repeat, with table actually marked as sorted
        t = t.sort("num");
        assertSeekPositionAllRows(seekValue, t, forwardPosition, backwardPosition);
        assertNotFound(6, t);
    }

    @Test
    public void descendingSortedContentSeek() {
        Table t = TableTools.newTable(intCol("num", 4, 4, 3, 3, 2, 1, 1));
        int seekValue = 3;
        int[] forwardPosition = {2, 2, 3, 2, 2, 2, 2};
        int[] backwardPosition = {3, 3, 3, 2, 3, 3, 3};
        assertSeekPositionAllRows(seekValue, t, forwardPosition, backwardPosition);
        assertNotFound(6, t);

        t = t.sortDescending("num");
        assertSeekPositionAllRows(seekValue, t, forwardPosition, backwardPosition);
        assertNotFound(6, t);
    }

    @Test
    public void unsortedContentSeek() {
        // TODO
    }
}
