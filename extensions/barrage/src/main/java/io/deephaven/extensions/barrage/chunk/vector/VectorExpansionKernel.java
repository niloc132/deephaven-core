//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.extensions.barrage.chunk.vector;

import io.deephaven.chunk.ChunkType;
import io.deephaven.extensions.barrage.chunk.ExpansionKernel;
import io.deephaven.vector.ByteVector;
import io.deephaven.vector.CharVector;
import io.deephaven.vector.DoubleVector;
import io.deephaven.vector.FloatVector;
import io.deephaven.vector.IntVector;
import io.deephaven.vector.LongVector;
import io.deephaven.vector.ObjectVector;
import io.deephaven.vector.ShortVector;
import io.deephaven.vector.Vector;

public interface VectorExpansionKernel<T extends Vector<T>> extends ExpansionKernel<T> {

    static Class<?> getComponentType(final Class<?> type, final Class<?> componentType) {
        if (ByteVector.class.isAssignableFrom(type)) {
            return byte.class;
        }
        if (CharVector.class.isAssignableFrom(type)) {
            return char.class;
        }
        if (DoubleVector.class.isAssignableFrom(type)) {
            return double.class;
        }
        if (FloatVector.class.isAssignableFrom(type)) {
            return float.class;
        }
        if (IntVector.class.isAssignableFrom(type)) {
            return int.class;
        }
        if (LongVector.class.isAssignableFrom(type)) {
            return long.class;
        }
        if (ShortVector.class.isAssignableFrom(type)) {
            return short.class;
        }
        if (ObjectVector.class.isAssignableFrom(type)) {
            return componentType != null ? componentType : Object.class;
        }
        throw new IllegalStateException("Unexpected vector type: " + type.getCanonicalName());
    }

    /**
     * @return a kernel that expands a {@code Chunk<VectorT>} to pair of {@code LongChunk, Chunk<T>}
     */
    @SuppressWarnings("unchecked")
    static <T extends Vector<T>> VectorExpansionKernel<T> makeExpansionKernel(
            final ChunkType chunkType, final Class<?> componentType) {
        switch (chunkType) {
            case Char:
                return (VectorExpansionKernel<T>) CharVectorExpansionKernel.INSTANCE;
            case Byte:
                return (VectorExpansionKernel<T>) ByteVectorExpansionKernel.INSTANCE;
            case Short:
                return (VectorExpansionKernel<T>) ShortVectorExpansionKernel.INSTANCE;
            case Int:
                return (VectorExpansionKernel<T>) IntVectorExpansionKernel.INSTANCE;
            case Long:
                return (VectorExpansionKernel<T>) LongVectorExpansionKernel.INSTANCE;
            case Float:
                return (VectorExpansionKernel<T>) FloatVectorExpansionKernel.INSTANCE;
            case Double:
                return (VectorExpansionKernel<T>) DoubleVectorExpansionKernel.INSTANCE;
            default:
                return (VectorExpansionKernel<T>) new ObjectVectorExpansionKernel<>(componentType);
        }
    }
}
