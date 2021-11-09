package io.deephaven.engine.v2.sources.chunk.util.factories;

import io.deephaven.engine.tables.dbarrays.Vector;
import io.deephaven.engine.v2.sources.chunk.*;
import io.deephaven.engine.v2.sources.chunk.Attributes.Any;
import io.deephaven.engine.v2.sources.chunk.page.ChunkPage;

import org.jetbrains.annotations.NotNull;
import java.util.function.IntFunction;

public interface ChunkFactory {
    @NotNull
    Object makeArray(int capacity);

    @NotNull
    <ATTR extends Any> Chunk<ATTR>[] makeChunkArray(int capacity);

    @NotNull
    <ATTR extends Any> Chunk<ATTR> getEmptyChunk();

    @NotNull
    <ATTR extends Any> ChunkChunk<ATTR> getEmptyChunkChunk();

    @NotNull
    <ATTR extends Any> Chunk<ATTR> chunkWrap(Object array);

    @NotNull
    <ATTR extends Any> Chunk<ATTR> chunkWrap(Object array, int offset, int capacity);

    @NotNull
    <ATTR extends Any> ChunkChunk<ATTR> chunkChunkWrap(Chunk<ATTR>[] array);

    @NotNull
    <ATTR extends Any> ChunkChunk<ATTR> chunkChunkWrap(Chunk<ATTR>[] array, int offset, int capacity);

    @NotNull
    Vector vectorWrap(Object array);

    @NotNull
    Vector vectorWrap(Object array, int offset, int capacity);

    @NotNull
    <ATTR extends Any> ChunkPage<ATTR> pageWrap(long beginRow, Object array, long mask);

    @NotNull
    <ATTR extends Any> ChunkPage<ATTR> pageWrap(long beginRow, Object array, int offset, int capacity, long mask);

    @NotNull
    <ATTR extends Any> ResettableReadOnlyChunk<ATTR> makeResettableReadOnlyChunk();

    @NotNull
    <ATTR extends Any> ResettableChunkChunk<ATTR> makeResettableChunkChunk();

    @NotNull
    <ATTR extends Any> WritableChunk<ATTR> makeWritableChunk(int capacity);

    @NotNull
    <ATTR extends Any> WritableChunkChunk<ATTR> makeWritableChunkChunk(int capacity);

    @NotNull
    <ATTR extends Any> WritableChunk<ATTR> writableChunkWrap(Object array, int offset, int capacity);

    @NotNull
    <ATTR extends Any> WritableChunkChunk<ATTR> writableChunkChunkWrap(WritableChunk<ATTR>[] array, int offset,
            int capacity);

    @NotNull
    <ATTR extends Any> ResettableWritableChunk<ATTR> makeResettableWritableChunk();

    @NotNull
    <ATTR extends Any> ResettableWritableChunkChunk<ATTR> makeResettableWritableChunkChunk();

    @NotNull
    IntFunction<Chunk[]> chunkArrayBuilder();

    @NotNull
    IntFunction<WritableChunk[]> writableChunkArrayBuilder();
}
