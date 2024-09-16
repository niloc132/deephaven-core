//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.extensions.barrage.chunk;

import io.deephaven.chunk.WritableChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.extensions.barrage.ColumnConversionMode;
import io.deephaven.util.QueryConstants;
import io.deephaven.util.annotations.FinalDefault;
import org.apache.arrow.flatbuf.Field;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.IOException;
import java.util.Iterator;
import java.util.PrimitiveIterator;

/**
 * Consumes Flight/Barrage streams and transforms them into WritableChunks.
 */
public interface ChunkReader<ReadChunkType extends WritableChunk<Values>> {
    interface Options {
        /**
         * @return whether we encode the validity buffer to express null values or {@link QueryConstants}'s NULL values.
         */
        boolean useDeephavenNulls();

        /**
         * @return the conversion mode to use for object columns
         */
        ColumnConversionMode columnConversionMode();

        /**
         * @return the ideal number of records to send per record batch
         */
        int batchSize();

        /**
         * @return the maximum number of bytes that should be sent in a single message.
         */
        int maxMessageSize();

        /**
         * Some Flight clients cannot handle modifications that have irregular column counts. These clients request that
         * the server wrap all columns in a list to enable each column having a variable length.
         *
         * @return true if the columns should be wrapped in a list
         */
        default boolean columnsAsList() {
            return false;
        }
    }

    /**
     * Reads the given DataInput to extract the next Arrow buffer as a Deephaven Chunk.
     *
     * @param fieldNodeIter iterator to read fields from the stream
     * @param bufferInfoIter iterator to read buffers from the stream
     * @param is input stream containing buffers to be read
     * @return a Chunk containing the data from the stream
     * @throws IOException if an error occurred while reading the stream
     */
    @FinalDefault
    default ReadChunkType readChunk(
            @NotNull Iterator<ChunkWriter.FieldNodeInfo> fieldNodeIter,
            @NotNull PrimitiveIterator.OfLong bufferInfoIter,
            @NotNull DataInput is) throws IOException {
        return readChunk(fieldNodeIter, bufferInfoIter, is, null, 0, 0);
    }

    /**
     * Reads the given DataInput to extract the next Arrow buffer as a Deephaven Chunk.
     * 
     * @param fieldNodeIter iterator to read fields from the stream
     * @param bufferInfoIter iterator to read buffers from the stream
     * @param is input stream containing buffers to be read
     * @param outChunk chunk to write to
     * @param outOffset offset within the outChunk to begin writing
     * @param totalRows total rows to write to the outChunk
     * @return a Chunk containing the data from the stream
     * @throws IOException if an error occurred while reading the stream
     */
    ReadChunkType readChunk(
            @NotNull Iterator<ChunkWriter.FieldNodeInfo> fieldNodeIter,
            @NotNull PrimitiveIterator.OfLong bufferInfoIter,
            @NotNull DataInput is,
            @Nullable WritableChunk<Values> outChunk,
            int outOffset,
            int totalRows) throws IOException;

    /**
     * Supports creation of {@link ChunkReader} instances to use when processing a flight stream. JVM implementations
     * for client and server should probably use {@link DefaultChunkReaderFactory#INSTANCE}.
     */
    interface Factory {

        /**
         * Returns a {@link ChunkReader} for the specified arguments.
         *
         * @param typeInfo the type of data to read into a chunk
         * @param options options for reading the stream
         * @return a ChunkReader based on the given options, factory, and type to read
         */
        <T extends WritableChunk<Values>> ChunkReader<T> newReader(
                @NotNull TypeInfo typeInfo,
                @NotNull Options options);
    }

    /**
     * Describes type info used by factory implementations when creating a ChunkReader.
     */
    class TypeInfo {
        private final Class<?> type;
        @Nullable
        private final Class<?> componentType;
        private final Field arrowField;

        public TypeInfo(
                @NotNull final Class<?> type,
                @Nullable final Class<?> componentType,
                @NotNull final Field arrowField) {
            this.type = type;
            this.componentType = componentType;
            this.arrowField = arrowField;
        }

        public Class<?> type() {
            return type;
        }

        @Nullable
        public Class<?> componentType() {
            return componentType;
        }

        public Field arrowField() {
            return arrowField;
        }
    }

    /**
     * Factory method to create a TypeInfo instance.
     *
     * @param type the Java type to be read into the chunk
     * @param componentType the Java type of nested components
     * @param arrowField the Arrow type to be read into the chunk
     * @return a TypeInfo instance
     */
    static TypeInfo typeInfo(
            @NotNull final Class<?> type,
            @Nullable final Class<?> componentType,
            @NotNull final Field arrowField) {
        return new TypeInfo(type, componentType, arrowField);
    }
}
