package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.ByteChunk;
import io.deephaven.engine.chunk.CharChunk;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.ShortChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.tuple.generated.ShortByteCharTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Short, Byte, and Character.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ShortByteCharacterColumnTupleSource extends AbstractTupleSource<ShortByteCharTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link ShortByteCharacterColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<ShortByteCharTuple, Short, Byte, Character> FACTORY = new Factory();

    private final ColumnSource<Short> columnSource1;
    private final ColumnSource<Byte> columnSource2;
    private final ColumnSource<Character> columnSource3;

    public ShortByteCharacterColumnTupleSource(
            @NotNull final ColumnSource<Short> columnSource1,
            @NotNull final ColumnSource<Byte> columnSource2,
            @NotNull final ColumnSource<Character> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final ShortByteCharTuple createTuple(final long indexKey) {
        return new ShortByteCharTuple(
                columnSource1.getShort(indexKey),
                columnSource2.getByte(indexKey),
                columnSource3.getChar(indexKey)
        );
    }

    @Override
    public final ShortByteCharTuple createPreviousTuple(final long indexKey) {
        return new ShortByteCharTuple(
                columnSource1.getPrevShort(indexKey),
                columnSource2.getPrevByte(indexKey),
                columnSource3.getPrevChar(indexKey)
        );
    }

    @Override
    public final ShortByteCharTuple createTupleFromValues(@NotNull final Object... values) {
        return new ShortByteCharTuple(
                TypeUtils.unbox((Short)values[0]),
                TypeUtils.unbox((Byte)values[1]),
                TypeUtils.unbox((Character)values[2])
        );
    }

    @Override
    public final ShortByteCharTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new ShortByteCharTuple(
                TypeUtils.unbox((Short)values[0]),
                TypeUtils.unbox((Byte)values[1]),
                TypeUtils.unbox((Character)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final ShortByteCharTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
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
    public final Object exportToExternalKey(@NotNull final ShortByteCharTuple tuple) {
        return new SmartKey(
                TypeUtils.box(tuple.getFirstElement()),
                TypeUtils.box(tuple.getSecondElement()),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final ShortByteCharTuple tuple, int elementIndex) {
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
    public final Object exportElementReinterpreted(@NotNull final ShortByteCharTuple tuple, int elementIndex) {
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
        WritableObjectChunk<ShortByteCharTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ShortChunk<Values> chunk1 = chunks[0].asShortChunk();
        ByteChunk<Values> chunk2 = chunks[1].asByteChunk();
        CharChunk<Values> chunk3 = chunks[2].asCharChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new ShortByteCharTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link ShortByteCharacterColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<ShortByteCharTuple, Short, Byte, Character> {

        private Factory() {
        }

        @Override
        public TupleSource<ShortByteCharTuple> create(
                @NotNull final ColumnSource<Short> columnSource1,
                @NotNull final ColumnSource<Byte> columnSource2,
                @NotNull final ColumnSource<Character> columnSource3
        ) {
            return new ShortByteCharacterColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
