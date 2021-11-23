package io.deephaven.replicators;

import io.deephaven.compilertools.ReplicatePrimitiveCode;

import java.io.IOException;

/**
 * Code generation for basic {@link RegionedColumnSource} implementations as well as well as the primary region
 * interfaces for some primitive types.
 */
public class ReplicateRegionsAndRegionedSources extends ReplicatePrimitiveCode {

    public static void main(String... args) throws IOException {
        charToAllButBooleanAndByte("engine/table/src/main/java/io/deephaven/engine/table/impl/sources/regioned/ColumnRegionChar.java");
        charToAllButBooleanAndByte(
                "engine/table/src/main/java/io/deephaven/engine/table/impl/sources/regioned/DeferredColumnRegionChar.java");
        charToAllButBooleanAndByte(
                "engine/table/src/main/java/io/deephaven/engine/table/impl/sources/regioned/ParquetColumnRegionChar.java");
        charToAllButBoolean("engine/table/src/main/java/io/deephaven/engine/table/impl/sources/regioned/RegionedColumnSourceChar.java");
    }
}
