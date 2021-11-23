package io.deephaven.replicators;

import io.deephaven.compilertools.ReplicatePrimitiveCode;

import java.io.IOException;

public class ReplicateUnboxerKernel {
    public static void main(String[] args) throws IOException {
        ReplicatePrimitiveCode
                .charToAllButBoolean("engine/table/src/main/java/io/deephaven/engine/table/impl/utils/unboxer/CharUnboxer.java");
    }
}
