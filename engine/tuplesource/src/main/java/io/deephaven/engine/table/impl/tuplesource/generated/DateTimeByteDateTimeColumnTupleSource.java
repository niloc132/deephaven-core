package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.ByteChunk;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.ObjectChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtils;
import io.deephaven.engine.tuple.generated.LongByteLongTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types DateTime, Byte, and DateTime.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DateTimeByteDateTimeColumnTupleSource extends AbstractTupleSource<LongByteLongTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link DateTimeByteDateTimeColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<LongByteLongTuple, DateTime, Byte, DateTime> FACTORY = new Factory();

    private final ColumnSource<DateTime> columnSource1;
    private final ColumnSource<Byte> columnSource2;
    private final ColumnSource<DateTime> columnSource3;

    public DateTimeByteDateTimeColumnTupleSource(
            @NotNull final ColumnSource<DateTime> columnSource1,
            @NotNull final ColumnSource<Byte> columnSource2,
            @NotNull final ColumnSource<DateTime> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final LongByteLongTuple createTuple(final long indexKey) {
        return new LongByteLongTuple(
                DateTimeUtils.nanos(columnSource1.get(indexKey)),
                columnSource2.getByte(indexKey),
                DateTimeUtils.nanos(columnSource3.get(indexKey))
        );
    }

    @Override
    public final LongByteLongTuple createPreviousTuple(final long indexKey) {
        return new LongByteLongTuple(
                DateTimeUtils.nanos(columnSource1.getPrev(indexKey)),
                columnSource2.getPrevByte(indexKey),
                DateTimeUtils.nanos(columnSource3.getPrev(indexKey))
        );
    }

    @Override
    public final LongByteLongTuple createTupleFromValues(@NotNull final Object... values) {
        return new LongByteLongTuple(
                DateTimeUtils.nanos((DateTime)values[0]),
                TypeUtils.unbox((Byte)values[1]),
                DateTimeUtils.nanos((DateTime)values[2])
        );
    }

    @Override
    public final LongByteLongTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new LongByteLongTuple(
                DateTimeUtils.nanos((DateTime)values[0]),
                TypeUtils.unbox((Byte)values[1]),
                DateTimeUtils.nanos((DateTime)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final LongByteLongTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtils.nanosToTime(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationIndexKey, tuple.getSecondElement());
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtils.nanosToTime(tuple.getThirdElement()));
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final LongByteLongTuple tuple) {
        return new SmartKey(
                DateTimeUtils.nanosToTime(tuple.getFirstElement()),
                TypeUtils.box(tuple.getSecondElement()),
                DateTimeUtils.nanosToTime(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final LongByteLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtils.nanosToTime(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return TypeUtils.box(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return DateTimeUtils.nanosToTime(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final LongByteLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtils.nanosToTime(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return TypeUtils.box(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return DateTimeUtils.nanosToTime(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<Values> [] chunks) {
        WritableObjectChunk<LongByteLongTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ObjectChunk<DateTime, Values> chunk1 = chunks[0].asObjectChunk();
        ByteChunk<Values> chunk2 = chunks[1].asByteChunk();
        ObjectChunk<DateTime, Values> chunk3 = chunks[2].asObjectChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new LongByteLongTuple(DateTimeUtils.nanos(chunk1.get(ii)), chunk2.get(ii), DateTimeUtils.nanos(chunk3.get(ii))));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link DateTimeByteDateTimeColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<LongByteLongTuple, DateTime, Byte, DateTime> {

        private Factory() {
        }

        @Override
        public TupleSource<LongByteLongTuple> create(
                @NotNull final ColumnSource<DateTime> columnSource1,
                @NotNull final ColumnSource<Byte> columnSource2,
                @NotNull final ColumnSource<DateTime> columnSource3
        ) {
            return new DateTimeByteDateTimeColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
