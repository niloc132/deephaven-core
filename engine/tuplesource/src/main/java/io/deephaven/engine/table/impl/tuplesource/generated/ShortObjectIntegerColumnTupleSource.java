package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.IntChunk;
import io.deephaven.engine.chunk.ObjectChunk;
import io.deephaven.engine.chunk.ShortChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.tuple.generated.ShortObjectIntTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Short, Object, and Integer.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ShortObjectIntegerColumnTupleSource extends AbstractTupleSource<ShortObjectIntTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link ShortObjectIntegerColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<ShortObjectIntTuple, Short, Object, Integer> FACTORY = new Factory();

    private final ColumnSource<Short> columnSource1;
    private final ColumnSource<Object> columnSource2;
    private final ColumnSource<Integer> columnSource3;

    public ShortObjectIntegerColumnTupleSource(
            @NotNull final ColumnSource<Short> columnSource1,
            @NotNull final ColumnSource<Object> columnSource2,
            @NotNull final ColumnSource<Integer> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final ShortObjectIntTuple createTuple(final long rowKey) {
        return new ShortObjectIntTuple(
                columnSource1.getShort(rowKey),
                columnSource2.get(rowKey),
                columnSource3.getInt(rowKey)
        );
    }

    @Override
    public final ShortObjectIntTuple createPreviousTuple(final long rowKey) {
        return new ShortObjectIntTuple(
                columnSource1.getPrevShort(rowKey),
                columnSource2.getPrev(rowKey),
                columnSource3.getPrevInt(rowKey)
        );
    }

    @Override
    public final ShortObjectIntTuple createTupleFromValues(@NotNull final Object... values) {
        return new ShortObjectIntTuple(
                TypeUtils.unbox((Short)values[0]),
                values[1],
                TypeUtils.unbox((Integer)values[2])
        );
    }

    @Override
    public final ShortObjectIntTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new ShortObjectIntTuple(
                TypeUtils.unbox((Short)values[0]),
                values[1],
                TypeUtils.unbox((Integer)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final ShortObjectIntTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationRowKey) {
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
    public final Object exportToExternalKey(@NotNull final ShortObjectIntTuple tuple) {
        return new SmartKey(
                TypeUtils.box(tuple.getFirstElement()),
                tuple.getSecondElement(),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final ShortObjectIntTuple tuple, int elementIndex) {
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
    public final Object exportElementReinterpreted(@NotNull final ShortObjectIntTuple tuple, int elementIndex) {
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
        WritableObjectChunk<ShortObjectIntTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ShortChunk<Values> chunk1 = chunks[0].asShortChunk();
        ObjectChunk<Object, Values> chunk2 = chunks[1].asObjectChunk();
        IntChunk<Values> chunk3 = chunks[2].asIntChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new ShortObjectIntTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link ShortObjectIntegerColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<ShortObjectIntTuple, Short, Object, Integer> {

        private Factory() {
        }

        @Override
        public TupleSource<ShortObjectIntTuple> create(
                @NotNull final ColumnSource<Short> columnSource1,
                @NotNull final ColumnSource<Object> columnSource2,
                @NotNull final ColumnSource<Integer> columnSource3
        ) {
            return new ShortObjectIntegerColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
