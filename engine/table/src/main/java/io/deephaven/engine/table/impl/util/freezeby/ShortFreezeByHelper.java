/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
/*
 * ---------------------------------------------------------------------------------------------------------------------
 * AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY - for any changes edit CharFreezeByHelper and regenerate
 * ---------------------------------------------------------------------------------------------------------------------
 */
/*
 * Copyright (c) 2020 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.engine.table.impl.util.freezeby;

import io.deephaven.chunk.attributes.ChunkLengths;
import io.deephaven.chunk.attributes.ChunkPositions;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.sources.ShortArraySource;
import io.deephaven.chunk.*;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.chunkattributes.RowKeys;

class ShortFreezeByHelper implements FreezeByOperator.FreezeByHelper {
    private final ShortArraySource resultSource;
    private final FreezeByCountOperator rowCount;

    ShortFreezeByHelper(WritableColumnSource resultSource, FreezeByCountOperator rowCount) {
        this.resultSource = (ShortArraySource)resultSource;
        this.rowCount = rowCount;
    }

    @Override
    public void addChunk(Chunk<? extends Values> values, IntChunk<ChunkPositions> startPositions, IntChunk<RowKeys> destinations, IntChunk<ChunkLengths> length) {
        final ShortChunk asShort = values.asShortChunk();
        for (int ii = 0; ii < startPositions.size(); ++ii) {
            final int position = startPositions.get(ii);
            final int destination = destinations.get(position);
            // if there is just row churn for a key, we don't want to re-snapshot the value (adds/removes just get ignored)
            if (rowCount.wasDestinationEmpty(destination)) {
                resultSource.set(destination, asShort.get(position));
            }
        }
    }


    @Override
    public void addChunk(Chunk<? extends Values> values, long destination) {
        if (rowCount.wasDestinationEmpty(destination)) {
            final ShortChunk asShort = values.asShortChunk();
            resultSource.set(destination, asShort.get(0));
        }
    }

    @Override
    public void clearIndex(RowSequence removed) {
        // region clearIndex
        // endregion clearIndex
    }
}
