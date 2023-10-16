package io.deephaven.server.table.stats;

import gnu.trove.map.hash.TObjectLongHashMap;
import io.deephaven.chunk.CharChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.table.ChunkSource;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.impl.util.ColumnHolder;
import io.deephaven.engine.util.TableTools;
import io.deephaven.util.QueryConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CharacterChunkedComparableStats implements ChunkedComparableStatsKernel<Character> {
    @Override
    public Table processChunks(final RowSet index, final ColumnSource<?> columnSource, boolean usePrev, int maxUnique) {
        long count = 0;
        int uniqueCount = 0;

        final TObjectLongHashMap<Comparable<?>> countValues = new TObjectLongHashMap<>();
        boolean useSet = false;
        final Set<Comparable<?>> uniqueValues = new HashSet<>();

        try (final ChunkSource.GetContext getContext = columnSource.makeGetContext(CHUNK_SIZE)) {
            final RowSequence.Iterator okIt = index.getRowSequenceIterator();

            while (okIt.hasMore()) {
                // Grab up to the next CHUNK_SIZE rows
                final RowSequence nextKeys = okIt.getNextRowSequenceWithLength(CHUNK_SIZE);

                final CharChunk<? extends Values> chunk = (usePrev ? columnSource.getPrevChunk(getContext, nextKeys)
                        : columnSource.getChunk(getContext, nextKeys)).asCharChunk();
                final int chunkSize = chunk.size();
                for (int ii = 0; ii < chunkSize; ii++) {
                    final char val = chunk.get(ii);

                    if (val == QueryConstants.NULL_CHAR) {
                        continue;
                    }

                    count++;

                    if (useSet) {
                        uniqueValues.add((Comparable<?>) val);
                    } else if (uniqueCount > maxUnique) {
                        // we no longer need to track counts for these items; fall back to a Set
                        uniqueValues.addAll(countValues.keySet());
                        countValues.clear();
                        uniqueValues.add((Comparable<?>) val);
                        useSet = true;
                    } else if (countValues.adjustOrPutValue((Comparable<?>) val, 1, 1) == 1) {
                        uniqueCount++;
                    }
                }
            }

            final int numUnique;
            final Map<String, Long> valueCounts;
            if (useSet) {
                numUnique = uniqueValues.size();
                valueCounts = Collections.emptyMap();
            } else {
                numUnique = countValues.size();
                if (numUnique < maxUnique) {
                    valueCounts = new LinkedHashMap<>();
                    final ArrayList<Comparable<?>> sortedKeys = new ArrayList<>(countValues.keySet());
                    sortedKeys.sort(null);
                    sortedKeys.forEach(key -> valueCounts.put(Objects.toString(key), countValues.get(key)));
                } else {
                    valueCounts = Collections.emptyMap();
                }
            }

            return TableTools.newTable(
                    TableTools.longCol("Count", count),
                    TableTools.longCol("Count", index.size()),
                    TableTools.intCol("NumUnique", numUnique),
                    new ColumnHolder<>("UniqueKeys", String[].class, String.class, false,
                            valueCounts.keySet().toArray(String[]::new)),
                    new ColumnHolder<>("UniqueValues", long[].class, long.class, false,
                            valueCounts.values().stream().mapToLong(Long::longValue).toArray()));
        }
    }
}
