package io.deephaven.server.table.stats;

import gnu.trove.map.hash.TCharLongHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import gnu.trove.set.TCharSet;
import gnu.trove.set.hash.TCharHashSet;
import io.deephaven.chunk.CharChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.datastructures.util.CollectionUtil;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.table.ChunkSource;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.impl.util.ColumnHolder;
import io.deephaven.engine.table.iterators.CharacterColumnIterator;
import io.deephaven.engine.table.iterators.ChunkedCharacterColumnIterator;
import io.deephaven.engine.table.iterators.ChunkedColumnIterator;
import io.deephaven.engine.table.iterators.ColumnIterator;
import io.deephaven.engine.util.TableTools;
import io.deephaven.util.QueryConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CharacterChunkedStats implements ChunkedStatsKernel {
    private final int maxUniqueToCollect;
    private final int maxUniqueToDisplay;

    public CharacterChunkedStats(int maxUniqueToCollect, int maxUniqueToDisplay) {
        this.maxUniqueToCollect = maxUniqueToCollect;
        this.maxUniqueToDisplay = maxUniqueToDisplay;
    }

    @Override
    public Table processChunks(final RowSet rowSet, final ColumnSource<?> columnSource, boolean usePrev) {
        long count = 0;
        int uniqueCount = 0;

        final TCharLongHashMap countValues = new TCharLongHashMap();
        boolean useSet = false;
        final TCharSet uniqueValues = new TCharHashSet();

        try (CharacterColumnIterator iterator =
                new ChunkedCharacterColumnIterator(usePrev ? columnSource.getPrevSource() : columnSource, rowSet)) {
            while (iterator.hasNext()) {
                char val = iterator.next();
                if (val == QueryConstants.NULL_CHAR) {
                    continue;
                }
                count++;

                if (countValues.adjustOrPutValue(val, 1, 1) == 1 && ++uniqueCount > maxUniqueToCollect) {
                    // we no longer want to track counts for these items; fall back to a Set to get at least a count
                    uniqueValues.addAll(countValues.keySet());
                    countValues.clear();
                    useSet = true;
                    break;
                }
            }
            while (iterator.hasNext()) {
                // items still remain, count non-nulls and uniques
                char val = iterator.next();
                if (val == QueryConstants.NULL_CHAR) {
                    continue;
                }
                count++;

                uniqueValues.add(val);
            }
        }


        if (useSet) {
            return TableTools.newTable(
                    TableTools.longCol("COUNT", count),
                    TableTools.longCol("SIZE", rowSet.size()),
                    TableTools.intCol("UNIQUE_VALUES", uniqueValues.size()));
        } else {
            List<Map.Entry<String, Long>> sorted = new ArrayList<>(countValues.size());

            countValues.forEachEntry((o, c) -> {
                sorted.add(Map.entry(Objects.toString(o), c));
                return true;
            });
            sorted.sort(Map.Entry.comparingByValue());

            int resultCount = Math.min(maxUniqueToDisplay, sorted.size());
            String[] uniqueKeys = new String[resultCount];
            long[] uniqueCounts = new long[resultCount];
            Iterator<Map.Entry<String, Long>> iter = sorted.iterator();
            for (int i = 0; i < resultCount && iter.hasNext(); i++) {
                Map.Entry<String, Long> entry = iter.next();
                uniqueKeys[i] = entry.getKey();
                uniqueCounts[i] = entry.getValue();
            }
            return TableTools.newTable(
                    TableTools.longCol("COUNT", count),
                    TableTools.longCol("SIZE", rowSet.size()),
                    TableTools.intCol("UNIQUE_VALUES", countValues.size()),
                    new ColumnHolder<>("UNIQUE_KEYS", String[].class, String.class, false, uniqueKeys),
                    new ColumnHolder<>("UNIQUE_COUNTS", long[].class, long.class, false, uniqueCounts));
        }
    }
}
