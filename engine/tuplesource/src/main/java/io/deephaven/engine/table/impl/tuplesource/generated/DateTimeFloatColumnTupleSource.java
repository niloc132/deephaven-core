package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.FloatChunk;
import io.deephaven.engine.chunk.ObjectChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.TwoColumnTupleSourceFactory;
import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtils;
import io.deephaven.engine.tuple.generated.LongFloatTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types DateTime and Float.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DateTimeFloatColumnTupleSource extends AbstractTupleSource<LongFloatTuple> {

    /** {@link TwoColumnTupleSourceFactory} instance to create instances of {@link DateTimeFloatColumnTupleSource}. **/
    public static final TwoColumnTupleSourceFactory<LongFloatTuple, DateTime, Float> FACTORY = new Factory();

    private final ColumnSource<DateTime> columnSource1;
    private final ColumnSource<Float> columnSource2;

    public DateTimeFloatColumnTupleSource(
            @NotNull final ColumnSource<DateTime> columnSource1,
            @NotNull final ColumnSource<Float> columnSource2
    ) {
        super(columnSource1, columnSource2);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
    }

    @Override
    public final LongFloatTuple createTuple(final long rowKey) {
        return new LongFloatTuple(
                DateTimeUtils.nanos(columnSource1.get(rowKey)),
                columnSource2.getFloat(rowKey)
        );
    }

    @Override
    public final LongFloatTuple createPreviousTuple(final long rowKey) {
        return new LongFloatTuple(
                DateTimeUtils.nanos(columnSource1.getPrev(rowKey)),
                columnSource2.getPrevFloat(rowKey)
        );
    }

    @Override
    public final LongFloatTuple createTupleFromValues(@NotNull final Object... values) {
        return new LongFloatTuple(
                DateTimeUtils.nanos((DateTime)values[0]),
                TypeUtils.unbox((Float)values[1])
        );
    }

    @Override
    public final LongFloatTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new LongFloatTuple(
                DateTimeUtils.nanos((DateTime)values[0]),
                TypeUtils.unbox((Float)values[1])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final LongFloatTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationRowKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationRowKey, (ELEMENT_TYPE) DateTimeUtils.nanosToTime(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationRowKey, tuple.getSecondElement());
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final LongFloatTuple tuple) {
        return new SmartKey(
                DateTimeUtils.nanosToTime(tuple.getFirstElement()),
                TypeUtils.box(tuple.getSecondElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final LongFloatTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtils.nanosToTime(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return TypeUtils.box(tuple.getSecondElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 2 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final LongFloatTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtils.nanosToTime(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return TypeUtils.box(tuple.getSecondElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 2 element tuple: " + elementIndex);
    }

    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<Values> [] chunks) {
        WritableObjectChunk<LongFloatTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ObjectChunk<DateTime, Values> chunk1 = chunks[0].asObjectChunk();
        FloatChunk<Values> chunk2 = chunks[1].asFloatChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new LongFloatTuple(DateTimeUtils.nanos(chunk1.get(ii)), chunk2.get(ii)));
        }
        destination.setSize(chunkSize);
    }

    /** {@link TwoColumnTupleSourceFactory} for instances of {@link DateTimeFloatColumnTupleSource}. **/
    private static final class Factory implements TwoColumnTupleSourceFactory<LongFloatTuple, DateTime, Float> {

        private Factory() {
        }

        @Override
        public TupleSource<LongFloatTuple> create(
                @NotNull final ColumnSource<DateTime> columnSource1,
                @NotNull final ColumnSource<Float> columnSource2
        ) {
            return new DateTimeFloatColumnTupleSource(
                    columnSource1,
                    columnSource2
            );
        }
    }
}
