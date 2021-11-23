/*
 * Copyright (c) 2016-2021 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.replicators;

import io.deephaven.compilertools.ReplicatePrimitiveCode;

import java.io.IOException;
import java.util.List;

import static io.deephaven.compilertools.ReplicatePrimitiveCode.*;
import static io.deephaven.compilertools.ReplicatePrimitiveCode.floatToAllFloatingPoints;

public class ReplicatePrimitiveLibTests {
    public static void main(String[] args) throws IOException {
        charToAllButBoolean("engine/table/src/test/java/io/deephaven/libs/primitives/TestCharPrimitives.java");
        shortToAllIntegralTypes("engine/table/src/main/test/io/deephaven/libs/primitives/TestShortNumericPrimitives.java");
        floatToAllFloatingPoints("engine/table/src/main/test/io/deephaven/libs/primitives/TestFloatNumericPrimitives.java");
        floatToAllFloatingPoints("engine/table/src/main/test/io/deephaven/libs/primitives/TestFloatFpPrimitives.java");
    }
}
