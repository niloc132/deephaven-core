package io.deephaven.web.grpc;

import elemental2.dom.URL;
import io.deephaven.web.grpc.impl.GrpcTransport;
import io.deephaven.web.grpc.impl.GrpcTransportFactory;
import io.deephaven.web.grpc.impl.GrpcTransportOptions;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

import javax.annotation.Nullable;

/**
 * Browser implementation of gRPC channel, supporting a transport API. Default implementations are h2 via fetch or multiplexed streams over a websocket, and the API is exposed to JS, allowing client code to offer its own transport as well (e.g. node.js offers its own h2).
 */
public class GwtGrpcWebChannel extends Channel {
    private final GrpcTransportFactory factory;
    private final URL server;

    public GwtGrpcWebChannel(GrpcTransportFactory factory, URL server) {
        this.factory = factory;
        this.server = server;
    }

    @Override
    public <RequestT, ResponseT> ClientCall<RequestT, ResponseT> newCall(MethodDescriptor<RequestT, ResponseT> methodDescriptor, CallOptions callOptions) {
        boolean supportsClientStreaming = factory.getSupportsClientStreaming();
        if (!supportsClientStreaming) {
            // We must return a wrapper instance that makes a pair of calls - "open" to stream results from the server, and "next" to send each update to the server
            return new ClientCall<RequestT, ResponseT>() {
                @Override
                public void start(Listener<ResponseT> responseListener, Metadata headers) {

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
                public void sendMessage(RequestT message) {

                }
            };
        }

        // The transport factory supports full bidi streams
        return new ClientCall<RequestT, ResponseT>() {
            private GrpcTransport transport;
            @Override
            public void start(Listener<ResponseT> responseListener, Metadata headers) {

                GrpcTransportOptions options = new GrpcTransportOptions();

                options.url = new URL(server);
                options.url.pathname = methodDescriptor.getFullMethodName();

                options.onEnd = error -> {
                    //TODO map to an error
                };
                options.onHeaders = (responseHeaders, status) -> {
                    //TODO status
                    responseListener.onHeaders(new Metadata());
                };
                options.onChunk = chunk -> {
                    //TODO parse chunks into messages
//                    methodDescriptor.getResponseMarshaller().parse(new ByteBufferInputStream(...));
                    responseListener.onMessage(null);
                };

                transport = factory.create(options);
            }

            @Override
            public void request(int numMessages) {
                // no-op, browsers can't do any kind of backpressure
            }

            @Override
            public void cancel(@Nullable String message, @Nullable Throwable cause) {
                transport.cancel();
            }

            @Override
            public void halfClose() {
                transport.finishSend();
            }

            @Override
            public void sendMessage(RequestT message) {
                //TODO serialize message
//                Drainable stream = (Drainable) methodDescriptor.getRequestMarshaller().stream(message);
//                stream.drainTo()
                transport.sendMessage(null);
            }
        };
    }

    @Override
    public String authority() {
        return server.host;
    }
}