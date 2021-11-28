package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.DoubleChunk;
import io.deephaven.engine.chunk.IntChunk;
import io.deephaven.engine.chunk.LongChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtils;
import io.deephaven.engine.tuple.generated.DoubleLongIntTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Double, Long, and Integer.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DoubleReinterpretedDateTimeIntegerColumnTupleSource extends AbstractTupleSource<DoubleLongIntTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link DoubleReinterpretedDateTimeIntegerColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<DoubleLongIntTuple, Double, Long, Integer> FACTORY = new Factory();

    private final ColumnSource<Double> columnSource1;
    private final ColumnSource<Long> columnSource2;
    private final ColumnSource<Integer> columnSource3;

    public DoubleReinterpretedDateTimeIntegerColumnTupleSource(
            @NotNull final ColumnSource<Double> columnSource1,
            @NotNull final ColumnSource<Long> columnSource2,
            @NotNull final ColumnSource<Integer> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final DoubleLongIntTuple createTuple(final long rowKey) {
        return new DoubleLongIntTuple(
                columnSource1.getDouble(rowKey),
                columnSource2.getLong(rowKey),
                columnSource3.getInt(rowKey)
        );
    }

    @Override
    public final DoubleLongIntTuple createPreviousTuple(final long rowKey) {
        return new DoubleLongIntTuple(
                columnSource1.getPrevDouble(rowKey),
                columnSource2.getPrevLong(rowKey),
                columnSource3.getPrevInt(rowKey)
        );
    }

    @Override
    public final DoubleLongIntTuple createTupleFromValues(@NotNull final Object... values) {
        return new DoubleLongIntTuple(
                TypeUtils.unbox((Double)values[0]),
                DateTimeUtils.nanos((DateTime)values[1]),
                TypeUtils.unbox((Integer)values[2])
        );
    }

    @Override
    public final DoubleLongIntTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new DoubleLongIntTuple(
                TypeUtils.unbox((Double)values[0]),
                TypeUtils.unbox((Long)values[1]),
                TypeUtils.unbox((Integer)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final DoubleLongIntTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationRowKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationRowKey, tuple.getFirstElement());
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationRowKey, (ELEMENT_TYPE) DateTimeUtils.nanosToTime(tuple.getSecondElement()));
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationRowKey, tuple.getThirdElement());
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final DoubleLongIntTuple tuple) {
        return new SmartKey(
                TypeUtils.box(tuple.getFirstElement()),
                DateTimeUtils.nanosToTime(tuple.getSecondElement()),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final DoubleLongIntTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return DateTimeUtils.nanosToTime(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final DoubleLongIntTuple tuple, int elementIndex) {
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
        WritableObjectChunk<DoubleLongIntTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        DoubleChunk<Values> chunk1 = chunks[0].asDoubleChunk();
        LongChunk<Values> chunk2 = chunks[1].asLongChunk();
        IntChunk<Values> chunk3 = chunks[2].asIntChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new DoubleLongIntTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link DoubleReinterpretedDateTimeIntegerColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<DoubleLongIntTuple, Double, Long, Integer> {

        private Factory() {
        }

        @Override
        public TupleSource<DoubleLongIntTuple> create(
                @NotNull final ColumnSource<Double> columnSource1,
                @NotNull final ColumnSource<Long> columnSource2,
                @NotNull final ColumnSource<Integer> columnSource3
        ) {
            return new DoubleReinterpretedDateTimeIntegerColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
