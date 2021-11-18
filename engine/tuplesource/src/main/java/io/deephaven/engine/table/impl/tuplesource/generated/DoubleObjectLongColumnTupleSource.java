package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.DoubleChunk;
import io.deephaven.engine.chunk.LongChunk;
import io.deephaven.engine.chunk.ObjectChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.tuple.generated.DoubleObjectLongTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Double, Object, and Long.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DoubleObjectLongColumnTupleSource extends AbstractTupleSource<DoubleObjectLongTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link DoubleObjectLongColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<DoubleObjectLongTuple, Double, Object, Long> FACTORY = new Factory();

    private final ColumnSource<Double> columnSource1;
    private final ColumnSource<Object> columnSource2;
    private final ColumnSource<Long> columnSource3;

    public DoubleObjectLongColumnTupleSource(
            @NotNull final ColumnSource<Double> columnSource1,
            @NotNull final ColumnSource<Object> columnSource2,
            @NotNull final ColumnSource<Long> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final DoubleObjectLongTuple createTuple(final long indexKey) {
        return new DoubleObjectLongTuple(
                columnSource1.getDouble(indexKey),
                columnSource2.get(indexKey),
                columnSource3.getLong(indexKey)
        );
    }

    @Override
    public final DoubleObjectLongTuple createPreviousTuple(final long indexKey) {
        return new DoubleObjectLongTuple(
                columnSource1.getPrevDouble(indexKey),
                columnSource2.getPrev(indexKey),
                columnSource3.getPrevLong(indexKey)
        );
    }

    @Override
    public final DoubleObjectLongTuple createTupleFromValues(@NotNull final Object... values) {
        return new DoubleObjectLongTuple(
                TypeUtils.unbox((Double)values[0]),
                values[1],
                TypeUtils.unbox((Long)values[2])
        );
    }

    @Override
    public final DoubleObjectLongTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new DoubleObjectLongTuple(
                TypeUtils.unbox((Double)values[0]),
                values[1],
                TypeUtils.unbox((Long)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final DoubleObjectLongTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, tuple.getFirstElement());
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) tuple.getSecondElement());
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationIndexKey, tuple.getThirdElement());
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final DoubleObjectLongTuple tuple) {
        return new SmartKey(
                TypeUtils.box(tuple.getFirstElement()),
                tuple.getSecondElement(),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final DoubleObjectLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return tuple.getSecondElement();
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final DoubleObjectLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return tuple.getSecondElement();
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<Values> [] chunks) {
        WritableObjectChunk<DoubleObjectLongTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        DoubleChunk<Values> chunk1 = chunks[0].asDoubleChunk();
        ObjectChunk<Object, Values> chunk2 = chunks[1].asObjectChunk();
        LongChunk<Values> chunk3 = chunks[2].asLongChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new DoubleObjectLongTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link DoubleObjectLongColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<DoubleObjectLongTuple, Double, Object, Long> {

        private Factory() {
        }

        @Override
        public TupleSource<DoubleObjectLongTuple> create(
                @NotNull final ColumnSource<Double> columnSource1,
                @NotNull final ColumnSource<Object> columnSource2,
                @NotNull final ColumnSource<Long> columnSource3
        ) {
            return new DoubleObjectLongColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
