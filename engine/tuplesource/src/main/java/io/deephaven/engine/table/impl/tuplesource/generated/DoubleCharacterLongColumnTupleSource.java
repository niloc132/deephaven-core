package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.CharChunk;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.DoubleChunk;
import io.deephaven.engine.chunk.LongChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.tuple.generated.DoubleCharLongTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Double, Character, and Long.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DoubleCharacterLongColumnTupleSource extends AbstractTupleSource<DoubleCharLongTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link DoubleCharacterLongColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<DoubleCharLongTuple, Double, Character, Long> FACTORY = new Factory();

    private final ColumnSource<Double> columnSource1;
    private final ColumnSource<Character> columnSource2;
    private final ColumnSource<Long> columnSource3;

    public DoubleCharacterLongColumnTupleSource(
            @NotNull final ColumnSource<Double> columnSource1,
            @NotNull final ColumnSource<Character> columnSource2,
            @NotNull final ColumnSource<Long> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final DoubleCharLongTuple createTuple(final long indexKey) {
        return new DoubleCharLongTuple(
                columnSource1.getDouble(indexKey),
                columnSource2.getChar(indexKey),
                columnSource3.getLong(indexKey)
        );
    }

    @Override
    public final DoubleCharLongTuple createPreviousTuple(final long indexKey) {
        return new DoubleCharLongTuple(
                columnSource1.getPrevDouble(indexKey),
                columnSource2.getPrevChar(indexKey),
                columnSource3.getPrevLong(indexKey)
        );
    }

    @Override
    public final DoubleCharLongTuple createTupleFromValues(@NotNull final Object... values) {
        return new DoubleCharLongTuple(
                TypeUtils.unbox((Double)values[0]),
                TypeUtils.unbox((Character)values[1]),
                TypeUtils.unbox((Long)values[2])
        );
    }

    @Override
    public final DoubleCharLongTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new DoubleCharLongTuple(
                TypeUtils.unbox((Double)values[0]),
                TypeUtils.unbox((Character)values[1]),
                TypeUtils.unbox((Long)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final DoubleCharLongTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, tuple.getFirstElement());
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationIndexKey, tuple.getSecondElement());
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationIndexKey, tuple.getThirdElement());
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final DoubleCharLongTuple tuple) {
        return new SmartKey(
                TypeUtils.box(tuple.getFirstElement()),
                TypeUtils.box(tuple.getSecondElement()),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final DoubleCharLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return TypeUtils.box(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final DoubleCharLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return TypeUtils.box(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<Values> [] chunks) {
        WritableObjectChunk<DoubleCharLongTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        DoubleChunk<Values> chunk1 = chunks[0].asDoubleChunk();
        CharChunk<Values> chunk2 = chunks[1].asCharChunk();
        LongChunk<Values> chunk3 = chunks[2].asLongChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new DoubleCharLongTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link DoubleCharacterLongColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<DoubleCharLongTuple, Double, Character, Long> {

        private Factory() {
        }

        @Override
        public TupleSource<DoubleCharLongTuple> create(
                @NotNull final ColumnSource<Double> columnSource1,
                @NotNull final ColumnSource<Character> columnSource2,
                @NotNull final ColumnSource<Long> columnSource3
        ) {
            return new DoubleCharacterLongColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
