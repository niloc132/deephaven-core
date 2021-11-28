package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
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
import io.deephaven.engine.tuple.generated.LongLongLongTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Long, Long, and Long.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReinterpretedDateTimeReinterpretedDateTimeReinterpretedDateTimeColumnTupleSource extends AbstractTupleSource<LongLongLongTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link ReinterpretedDateTimeReinterpretedDateTimeReinterpretedDateTimeColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<LongLongLongTuple, Long, Long, Long> FACTORY = new Factory();

    private final ColumnSource<Long> columnSource1;
    private final ColumnSource<Long> columnSource2;
    private final ColumnSource<Long> columnSource3;

    public ReinterpretedDateTimeReinterpretedDateTimeReinterpretedDateTimeColumnTupleSource(
            @NotNull final ColumnSource<Long> columnSource1,
            @NotNull final ColumnSource<Long> columnSource2,
            @NotNull final ColumnSource<Long> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final LongLongLongTuple createTuple(final long rowKey) {
        return new LongLongLongTuple(
                columnSource1.getLong(rowKey),
                columnSource2.getLong(rowKey),
                columnSource3.getLong(rowKey)
        );
    }

    @Override
    public final LongLongLongTuple createPreviousTuple(final long rowKey) {
        return new LongLongLongTuple(
                columnSource1.getPrevLong(rowKey),
                columnSource2.getPrevLong(rowKey),
                columnSource3.getPrevLong(rowKey)
        );
    }

    @Override
    public final LongLongLongTuple createTupleFromValues(@NotNull final Object... values) {
        return new LongLongLongTuple(
                DateTimeUtils.nanos((DateTime)values[0]),
                DateTimeUtils.nanos((DateTime)values[1]),
                DateTimeUtils.nanos((DateTime)values[2])
        );
    }

    @Override
    public final LongLongLongTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new LongLongLongTuple(
                TypeUtils.unbox((Long)values[0]),
                TypeUtils.unbox((Long)values[1]),
                TypeUtils.unbox((Long)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final LongLongLongTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationRowKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationRowKey, (ELEMENT_TYPE) DateTimeUtils.nanosToTime(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationRowKey, (ELEMENT_TYPE) DateTimeUtils.nanosToTime(tuple.getSecondElement()));
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationRowKey, (ELEMENT_TYPE) DateTimeUtils.nanosToTime(tuple.getThirdElement()));
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final LongLongLongTuple tuple) {
        return new SmartKey(
                DateTimeUtils.nanosToTime(tuple.getFirstElement()),
                DateTimeUtils.nanosToTime(tuple.getSecondElement()),
                DateTimeUtils.nanosToTime(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final LongLongLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtils.nanosToTime(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return DateTimeUtils.nanosToTime(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return DateTimeUtils.nanosToTime(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final LongLongLongTuple tuple, int elementIndex) {
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
        WritableObjectChunk<LongLongLongTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        LongChunk<Values> chunk1 = chunks[0].asLongChunk();
        LongChunk<Values> chunk2 = chunks[1].asLongChunk();
        LongChunk<Values> chunk3 = chunks[2].asLongChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new LongLongLongTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link ReinterpretedDateTimeReinterpretedDateTimeReinterpretedDateTimeColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<LongLongLongTuple, Long, Long, Long> {

        private Factory() {
        }

        @Override
        public TupleSource<LongLongLongTuple> create(
                @NotNull final ColumnSource<Long> columnSource1,
                @NotNull final ColumnSource<Long> columnSource2,
                @NotNull final ColumnSource<Long> columnSource3
        ) {
            return new ReinterpretedDateTimeReinterpretedDateTimeReinterpretedDateTimeColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
