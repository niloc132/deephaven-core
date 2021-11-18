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
import io.deephaven.engine.tuple.generated.ByteByteByteTuple;
import io.deephaven.util.BooleanUtils;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Boolean, Boolean, and Byte.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class BooleanBooleanReinterpretedBooleanColumnTupleSource extends AbstractTupleSource<ByteByteByteTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link BooleanBooleanReinterpretedBooleanColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<ByteByteByteTuple, Boolean, Boolean, Byte> FACTORY = new Factory();

    private final ColumnSource<Boolean> columnSource1;
    private final ColumnSource<Boolean> columnSource2;
    private final ColumnSource<Byte> columnSource3;

    public BooleanBooleanReinterpretedBooleanColumnTupleSource(
            @NotNull final ColumnSource<Boolean> columnSource1,
            @NotNull final ColumnSource<Boolean> columnSource2,
            @NotNull final ColumnSource<Byte> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final ByteByteByteTuple createTuple(final long indexKey) {
        return new ByteByteByteTuple(
                BooleanUtils.booleanAsByte(columnSource1.getBoolean(indexKey)),
                BooleanUtils.booleanAsByte(columnSource2.getBoolean(indexKey)),
                columnSource3.getByte(indexKey)
        );
    }

    @Override
    public final ByteByteByteTuple createPreviousTuple(final long indexKey) {
        return new ByteByteByteTuple(
                BooleanUtils.booleanAsByte(columnSource1.getPrevBoolean(indexKey)),
                BooleanUtils.booleanAsByte(columnSource2.getPrevBoolean(indexKey)),
                columnSource3.getPrevByte(indexKey)
        );
    }

    @Override
    public final ByteByteByteTuple createTupleFromValues(@NotNull final Object... values) {
        return new ByteByteByteTuple(
                BooleanUtils.booleanAsByte((Boolean)values[0]),
                BooleanUtils.booleanAsByte((Boolean)values[1]),
                BooleanUtils.booleanAsByte((Boolean)values[2])
        );
    }

    @Override
    public final ByteByteByteTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new ByteByteByteTuple(
                BooleanUtils.booleanAsByte((Boolean)values[0]),
                BooleanUtils.booleanAsByte((Boolean)values[1]),
                TypeUtils.unbox((Byte)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final ByteByteByteTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) BooleanUtils.byteAsBoolean(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) BooleanUtils.byteAsBoolean(tuple.getSecondElement()));
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) BooleanUtils.byteAsBoolean(tuple.getThirdElement()));
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final ByteByteByteTuple tuple) {
        return new SmartKey(
                BooleanUtils.byteAsBoolean(tuple.getFirstElement()),
                BooleanUtils.byteAsBoolean(tuple.getSecondElement()),
                BooleanUtils.byteAsBoolean(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final ByteByteByteTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return BooleanUtils.byteAsBoolean(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return BooleanUtils.byteAsBoolean(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return BooleanUtils.byteAsBoolean(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final ByteByteByteTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return BooleanUtils.byteAsBoolean(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return BooleanUtils.byteAsBoolean(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<Values> [] chunks) {
        WritableObjectChunk<ByteByteByteTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ObjectChunk<Boolean, Values> chunk1 = chunks[0].asObjectChunk();
        ObjectChunk<Boolean, Values> chunk2 = chunks[1].asObjectChunk();
        ByteChunk<Values> chunk3 = chunks[2].asByteChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new ByteByteByteTuple(BooleanUtils.booleanAsByte(chunk1.get(ii)), BooleanUtils.booleanAsByte(chunk2.get(ii)), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link BooleanBooleanReinterpretedBooleanColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<ByteByteByteTuple, Boolean, Boolean, Byte> {

        private Factory() {
        }

        @Override
        public TupleSource<ByteByteByteTuple> create(
                @NotNull final ColumnSource<Boolean> columnSource1,
                @NotNull final ColumnSource<Boolean> columnSource2,
                @NotNull final ColumnSource<Byte> columnSource3
        ) {
            return new BooleanBooleanReinterpretedBooleanColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
