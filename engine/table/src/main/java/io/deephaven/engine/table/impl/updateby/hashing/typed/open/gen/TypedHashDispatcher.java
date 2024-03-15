//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Run ReplicateTypedHashers or ./gradlew replicateTypedHashers to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.updateby.hashing.typed.open.gen;

import io.deephaven.chunk.ChunkType;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.impl.updateby.hashing.UpdateByStateManagerTypedBase;
import java.util.Arrays;

/**
 * The TypedHashDispatcher returns a pre-generated and precompiled hasher instance suitable for the provided column sources, or null if there is not a precompiled hasher suitable for the specified sources.
 */
public class TypedHashDispatcher {
    private TypedHashDispatcher() {
        // static use only
    }

    public static UpdateByStateManagerTypedBase dispatch(ColumnSource[] tableKeySources,
            ColumnSource[] originalTableKeySources, int tableSize, double maximumLoadFactor,
            double targetLoadFactor) {
        final ChunkType[] chunkTypes = Arrays.stream(tableKeySources).map(ColumnSource::getChunkType).toArray(ChunkType[]::new);;
        if (chunkTypes.length == 1) {
            return dispatchSingle(chunkTypes[0], tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
        }
        if (chunkTypes.length == 2) {
            return dispatchDouble(chunkTypes[0], chunkTypes[1], tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
        }
        return null;
    }

    private static UpdateByStateManagerTypedBase dispatchSingle(ChunkType chunkType,
            ColumnSource[] tableKeySources, ColumnSource[] originalTableKeySources, int tableSize,
            double maximumLoadFactor, double targetLoadFactor) {
        switch (chunkType) {
            default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType);
            case Char: return new UpdateByHasherChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Byte: return new UpdateByHasherByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Short: return new UpdateByHasherShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Int: return new UpdateByHasherInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Long: return new UpdateByHasherLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Float: return new UpdateByHasherFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Double: return new UpdateByHasherDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Object: return new UpdateByHasherObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
        }
    }

    private static UpdateByStateManagerTypedBase dispatchDouble(ChunkType chunkType0,
            ChunkType chunkType1, ColumnSource[] tableKeySources,
            ColumnSource[] originalTableKeySources, int tableSize, double maximumLoadFactor,
            double targetLoadFactor) {
        switch (chunkType0) {
            default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType0);
            case Char:switch (chunkType1) {
                default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType1);
                case Char: return new UpdateByHasherCharChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Byte: return new UpdateByHasherCharByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Short: return new UpdateByHasherCharShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Int: return new UpdateByHasherCharInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Long: return new UpdateByHasherCharLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Float: return new UpdateByHasherCharFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Double: return new UpdateByHasherCharDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Object: return new UpdateByHasherCharObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            }
            case Byte:switch (chunkType1) {
                default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType1);
                case Char: return new UpdateByHasherByteChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Byte: return new UpdateByHasherByteByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Short: return new UpdateByHasherByteShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Int: return new UpdateByHasherByteInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Long: return new UpdateByHasherByteLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Float: return new UpdateByHasherByteFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Double: return new UpdateByHasherByteDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Object: return new UpdateByHasherByteObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            }
            case Short:switch (chunkType1) {
                default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType1);
                case Char: return new UpdateByHasherShortChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Byte: return new UpdateByHasherShortByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Short: return new UpdateByHasherShortShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Int: return new UpdateByHasherShortInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Long: return new UpdateByHasherShortLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Float: return new UpdateByHasherShortFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Double: return new UpdateByHasherShortDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Object: return new UpdateByHasherShortObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            }
            case Int:switch (chunkType1) {
                default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType1);
                case Char: return new UpdateByHasherIntChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Byte: return new UpdateByHasherIntByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Short: return new UpdateByHasherIntShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Int: return new UpdateByHasherIntInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Long: return new UpdateByHasherIntLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Float: return new UpdateByHasherIntFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Double: return new UpdateByHasherIntDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Object: return new UpdateByHasherIntObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            }
            case Long:switch (chunkType1) {
                default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType1);
                case Char: return new UpdateByHasherLongChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Byte: return new UpdateByHasherLongByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Short: return new UpdateByHasherLongShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Int: return new UpdateByHasherLongInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Long: return new UpdateByHasherLongLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Float: return new UpdateByHasherLongFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Double: return new UpdateByHasherLongDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Object: return new UpdateByHasherLongObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            }
            case Float:switch (chunkType1) {
                default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType1);
                case Char: return new UpdateByHasherFloatChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Byte: return new UpdateByHasherFloatByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Short: return new UpdateByHasherFloatShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Int: return new UpdateByHasherFloatInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Long: return new UpdateByHasherFloatLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Float: return new UpdateByHasherFloatFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Double: return new UpdateByHasherFloatDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Object: return new UpdateByHasherFloatObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            }
            case Double:switch (chunkType1) {
                default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType1);
                case Char: return new UpdateByHasherDoubleChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Byte: return new UpdateByHasherDoubleByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Short: return new UpdateByHasherDoubleShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Int: return new UpdateByHasherDoubleInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Long: return new UpdateByHasherDoubleLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Float: return new UpdateByHasherDoubleFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Double: return new UpdateByHasherDoubleDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Object: return new UpdateByHasherDoubleObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            }
            case Object:switch (chunkType1) {
                default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType1);
                case Char: return new UpdateByHasherObjectChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Byte: return new UpdateByHasherObjectByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Short: return new UpdateByHasherObjectShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Int: return new UpdateByHasherObjectInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Long: return new UpdateByHasherObjectLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Float: return new UpdateByHasherObjectFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Double: return new UpdateByHasherObjectDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
                case Object: return new UpdateByHasherObjectObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            }
        }
    }
}
