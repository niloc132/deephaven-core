package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.FloatChunk;
import io.deephaven.engine.chunk.IntChunk;
import io.deephaven.engine.chunk.ObjectChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.tuple.generated.FloatObjectIntTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Float, Object, and Integer.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class FloatObjectIntegerColumnTupleSource extends AbstractTupleSource<FloatObjectIntTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link FloatObjectIntegerColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<FloatObjectIntTuple, Float, Object, Integer> FACTORY = new Factory();

    private final ColumnSource<Float> columnSource1;
    private final ColumnSource<Object> columnSource2;
    private final ColumnSource<Integer> columnSource3;

    public FloatObjectIntegerColumnTupleSource(
            @NotNull final ColumnSource<Float> columnSource1,
            @NotNull final ColumnSource<Object> columnSource2,
            @NotNull final ColumnSource<Integer> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final FloatObjectIntTuple createTuple(final long rowKey) {
        return new FloatObjectIntTuple(
                columnSource1.getFloat(rowKey),
                columnSource2.get(rowKey),
                columnSource3.getInt(rowKey)
        );
    }

    @Override
    public final FloatObjectIntTuple createPreviousTuple(final long rowKey) {
        return new FloatObjectIntTuple(
                columnSource1.getPrevFloat(rowKey),
                columnSource2.getPrev(rowKey),
                columnSource3.getPrevInt(rowKey)
        );
    }

    @Override
    public final FloatObjectIntTuple createTupleFromValues(@NotNull final Object... values) {
        return new FloatObjectIntTuple(
                TypeUtils.unbox((Float)values[0]),
                values[1],
                TypeUtils.unbox((Integer)values[2])
        );
    }

    @Override
    public final FloatObjectIntTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new FloatObjectIntTuple(
                TypeUtils.unbox((Float)values[0]),
                values[1],
                TypeUtils.unbox((Integer)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final FloatObjectIntTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationRowKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationRowKey, tuple.getFirstElement());
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationRowKey, (ELEMENT_TYPE) tuple.getSecondElement());
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationRowKey, tuple.getThirdElement());
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final FloatObjectIntTuple tuple) {
        return new SmartKey(
                TypeUtils.box(tuple.getFirstElement()),
                tuple.getSecondElement(),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final FloatObjectIntTuple tuple, int elementIndex) {
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
    public final Object exportElementReinterpreted(@NotNull final FloatObjectIntTuple tuple, int elementIndex) {
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
        WritableObjectChunk<FloatObjectIntTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        FloatChunk<Values> chunk1 = chunks[0].asFloatChunk();
        ObjectChunk<Object, Values> chunk2 = chunks[1].asObjectChunk();
        IntChunk<Values> chunk3 = chunks[2].asIntChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new FloatObjectIntTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link FloatObjectIntegerColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<FloatObjectIntTuple, Float, Object, Integer> {

        private Factory() {
        }

        @Override
        public TupleSource<FloatObjectIntTuple> create(
                @NotNull final ColumnSource<Float> columnSource1,
                @NotNull final ColumnSource<Object> columnSource2,
                @NotNull final ColumnSource<Integer> columnSource3
        ) {
            return new FloatObjectIntegerColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
