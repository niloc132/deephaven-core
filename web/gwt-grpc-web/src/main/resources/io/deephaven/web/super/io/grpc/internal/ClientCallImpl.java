package io.grpc.internal;

import io.grpc.ClientCall;
import io.grpc.Metadata;

import javax.annotation.Nullable;

final class ClientCallImpl<ReqT, RespT> extends ClientCall<ReqT, RespT> {
    @Override
    public void start(Listener<RespT> responseListener, Metadata headers) {

    }

    @Override
    public void request(int numMessages) {

    }

    @Override
    public void cancel(@Nullable String message, @Nullable Throwable cause) {

    }

    @Override
    public void halfClose() {

    }

    @Override
    public void sendMessage(ReqT message) {

    }
}
