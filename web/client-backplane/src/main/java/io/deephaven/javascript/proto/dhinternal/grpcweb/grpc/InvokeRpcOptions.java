//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.javascript.proto.dhinternal.grpcweb.grpc;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(
        isNative = true,
        name = "dhinternal.grpcWeb.grpc.InvokeRpcOptions",
        namespace = JsPackage.GLOBAL)
public interface InvokeRpcOptions<TRequest, TResponse>
        extends io.deephaven.javascript.proto.dhinternal.grpcweb.invoke.InvokeRpcOptions {
}
