//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.plot.util.functions;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * A serializable binary function. <br/>
 */
public interface SerializableBiFunction<T, U, R> extends BiFunction<T, U, R>, Serializable {
}
