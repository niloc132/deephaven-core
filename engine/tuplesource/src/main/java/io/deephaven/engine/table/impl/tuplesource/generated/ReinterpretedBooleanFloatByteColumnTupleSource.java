package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.ByteChunk;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.FloatChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.tuple.generated.ByteFloatByteTuple;
import io.deephaven.util.BooleanUtils;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Byte, Float, and Byte.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReinterpretedBooleanFloatByteColumnTupleSource extends AbstractTupleSource<ByteFloatByteTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link ReinterpretedBooleanFloatByteColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<ByteFloatByteTuple, Byte, Float, Byte> FACTORY = new Factory();

    private final ColumnSource<Byte> columnSource1;
    private final ColumnSource<Float> columnSource2;
    private final ColumnSource<Byte> columnSource3;

    public ReinterpretedBooleanFloatByteColumnTupleSource(
            @NotNull final ColumnSource<Byte> columnSource1,
            @NotNull final ColumnSource<Float> columnSource2,
            @NotNull final ColumnSource<Byte> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final ByteFloatByteTuple createTuple(final long rowKey) {
        return new ByteFloatByteTuple(
                columnSource1.getByte(rowKey),
                columnSource2.getFloat(rowKey),
                columnSource3.getByte(rowKey)
        );
    }

    @Override
    public final ByteFloatByteTuple createPreviousTuple(final long rowKey) {
        return new ByteFloatByteTuple(
                columnSource1.getPrevByte(rowKey),
                columnSource2.getPrevFloat(rowKey),
                columnSource3.getPrevByte(rowKey)
        );
    }

    @Override
    public final ByteFloatByteTuple createTupleFromValues(@NotNull final Object... values) {
        return new ByteFloatByteTuple(
                BooleanUtils.booleanAsByte((Boolean)values[0]),
                TypeUtils.unbox((Float)values[1]),
                TypeUtils.unbox((Byte)values[2])
        );
    }

    @Override
    public final ByteFloatByteTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new ByteFloatByteTuple(
                TypeUtils.unbox((Byte)values[0]),
                TypeUtils.unbox((Float)values[1]),
                TypeUtils.unbox((Byte)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final ByteFloatByteTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationRowKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationRowKey, (ELEMENT_TYPE) BooleanUtils.byteAsBoolean(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationRowKey, tuple.getSecondElement());
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationRowKey, tuple.getThirdElement());
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final ByteFloatByteTuple tuple) {
        return new SmartKey(
                BooleanUtils.byteAsBoolean(tuple.getFirstElement()),
                TypeUtils.box(tuple.getSecondElement()),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final ByteFloatByteTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return BooleanUtils.byteAsBoolean(tuple.getFirstElement());
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
    public final Object exportElementReinterpreted(@NotNull final ByteFloatByteTuple tuple, int elementIndex) {
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
        WritableObjectChunk<ByteFloatByteTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ByteChunk<Values> chunk1 = chunks[0].asByteChunk();
        FloatChunk<Values> chunk2 = chunks[1].asFloatChunk();
        ByteChunk<Values> chunk3 = chunks[2].asByteChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new ByteFloatByteTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link ReinterpretedBooleanFloatByteColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<ByteFloatByteTuple, Byte, Float, Byte> {

        private Factory() {
        }

        @Override
        public TupleSource<ByteFloatByteTuple> create(
                @NotNull final ColumnSource<Byte> columnSource1,
                @NotNull final ColumnSource<Float> columnSource2,
                @NotNull final ColumnSource<Byte> columnSource3
        ) {
            return new ReinterpretedBooleanFloatByteColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
