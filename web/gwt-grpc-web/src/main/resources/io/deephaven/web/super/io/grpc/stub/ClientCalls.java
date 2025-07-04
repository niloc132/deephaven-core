package io.grpc.stub;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;

import javax.annotation.Nullable;
import java.util.Iterator;

/**
 * Simplified implementation of ClientCalls that provides only async calls, the rest are stubs that only throw to be compiled out.
 */
public final class ClientCalls {
    static final CallOptions.Key<StubType> STUB_TYPE_OPTION = CallOptions.Key.create("internal-stub-type");

    private ClientCalls() {
    }

    public static <ReqT, RespT> void asyncUnaryCall(ClientCall<ReqT, RespT> call, ReqT req, StreamObserver<RespT> responseObserver) {
        Preconditions.checkNotNull(responseObserver, "responseObserver");
        asyncUnaryRequestCall(call, req, responseObserver, false);
    }

    public static <ReqT, RespT> void asyncServerStreamingCall(ClientCall<ReqT, RespT> call, ReqT req, StreamObserver<RespT> responseObserver) {
        Preconditions.checkNotNull(responseObserver, "responseObserver");
        asyncUnaryRequestCall(call, req, responseObserver, true);
    }

    public static <ReqT, RespT> StreamObserver<ReqT> asyncClientStreamingCall(ClientCall<ReqT, RespT> call, StreamObserver<RespT> responseObserver) {
        Preconditions.checkNotNull(responseObserver, "responseObserver");
        return asyncStreamingRequestCall(call, responseObserver, false);
    }

    public static <ReqT, RespT> StreamObserver<ReqT> asyncBidiStreamingCall(ClientCall<ReqT, RespT> call, StreamObserver<RespT> responseObserver) {
        Preconditions.checkNotNull(responseObserver, "responseObserver");
        return asyncStreamingRequestCall(call, responseObserver, true);
    }

    private static <ReqT, RespT> void asyncUnaryRequestCall(ClientCall<ReqT, RespT> call, ReqT req, StreamObserver<RespT> responseObserver, boolean streamingResponse) {
        asyncUnaryRequestCall(call, req, new StreamObserverToCallListenerAdapter(responseObserver, new CallToStreamObserverAdapter(call, streamingResponse)));
    }

    private static <ReqT, RespT> void asyncUnaryRequestCall(ClientCall<ReqT, RespT> call, ReqT req, StartableListener<RespT> responseListener) {
        startCall(call, responseListener);

        try {
            call.sendMessage(req);
            call.halfClose();
        } catch (Error | RuntimeException e) {
            throw cancelThrow(call, e);
        }
    }

    private static RuntimeException cancelThrow(ClientCall<?, ?> call, Throwable t) {
        try {
            call.cancel((String)null, t);
        } catch (Error | RuntimeException e) {
//            logger.log(Level.SEVERE, "RuntimeException encountered while closing call", e);
        }

        if (t instanceof RuntimeException) {
            throw (RuntimeException)t;
        } else if (t instanceof Error) {
            throw (Error)t;
        } else {
            throw new AssertionError(t);
        }
    }

    private static <ReqT, RespT> StreamObserver<ReqT> asyncStreamingRequestCall(ClientCall<ReqT, RespT> call, StreamObserver<RespT> responseObserver, boolean streamingResponse) {
        CallToStreamObserverAdapter<ReqT> adapter = new CallToStreamObserverAdapter<ReqT>(call, streamingResponse);
        startCall(call, new StreamObserverToCallListenerAdapter(responseObserver, adapter));
        return adapter;
    }

    private static <ReqT, RespT> void startCall(ClientCall<ReqT, RespT> call, StartableListener<RespT> responseListener) {
        call.start(responseListener, new Metadata());
        responseListener.onStart();
    }

    private abstract static class StartableListener<T> extends ClientCall.Listener<T> {
        private StartableListener() {
        }

        abstract void onStart();
    }

    private static final class CallToStreamObserverAdapter<ReqT> extends ClientCallStreamObserver<ReqT> {
        private boolean frozen;
        private final ClientCall<ReqT, ?> call;
        private final boolean streamingResponse;
        private Runnable onReadyHandler;
        private int initialRequest = 1;
        private boolean autoRequestEnabled = true;
        private boolean aborted = false;
        private boolean completed = false;

        CallToStreamObserverAdapter(ClientCall<ReqT, ?> call, boolean streamingResponse) {
            this.call = call;
            this.streamingResponse = streamingResponse;
        }

        private void freeze() {
            this.frozen = true;
        }

        public void onNext(ReqT value) {
            Preconditions.checkState(!this.aborted, "Stream was terminated by error, no further calls are allowed");
            Preconditions.checkState(!this.completed, "Stream is already completed, no further calls are allowed");
            this.call.sendMessage(value);
        }

        public void onError(Throwable t) {
            this.call.cancel("Cancelled by client with StreamObserver.onError()", t);
            this.aborted = true;
        }

        public void onCompleted() {
            this.call.halfClose();
            this.completed = true;
        }

        public boolean isReady() {
            return this.call.isReady();
        }

        public void setOnReadyHandler(Runnable onReadyHandler) {
            if (this.frozen) {
                throw new IllegalStateException("Cannot alter onReadyHandler after call started. Use ClientResponseObserver");
            } else {
                this.onReadyHandler = onReadyHandler;
            }
        }

        public void disableAutoInboundFlowControl() {
            this.disableAutoRequestWithInitial(1);
        }

        public void disableAutoRequestWithInitial(int request) {
            if (this.frozen) {
                throw new IllegalStateException("Cannot disable auto flow control after call started. Use ClientResponseObserver");
            } else {
                Preconditions.checkArgument(request >= 0, "Initial requests must be non-negative");
                this.initialRequest = request;
                this.autoRequestEnabled = false;
            }
        }

        public void request(int count) {
            if (!this.streamingResponse && count == 1) {
                this.call.request(2);
            } else {
                this.call.request(count);
            }

        }

        public void setMessageCompression(boolean enable) {
            this.call.setMessageCompression(enable);
        }

        public void cancel(@Nullable String message, @Nullable Throwable cause) {
            this.call.cancel(message, cause);
        }
    }

    private static final class StreamObserverToCallListenerAdapter<ReqT, RespT> extends StartableListener<RespT> {
        private final StreamObserver<RespT> observer;
        private final CallToStreamObserverAdapter<ReqT> adapter;
        private boolean firstResponseReceived;

        StreamObserverToCallListenerAdapter(StreamObserver<RespT> observer, CallToStreamObserverAdapter<ReqT> adapter) {
            this.observer = observer;
            this.adapter = adapter;
            if (observer instanceof ClientResponseObserver) {
                ClientResponseObserver<ReqT, RespT> clientResponseObserver = (ClientResponseObserver)observer;
                clientResponseObserver.beforeStart(adapter);
            }

            adapter.freeze();
        }

        public void onHeaders(Metadata headers) {
        }

        public void onMessage(RespT message) {
            if (this.firstResponseReceived && !this.adapter.streamingResponse) {
                throw Status.INTERNAL.withDescription("More than one responses received for unary or client-streaming call").asRuntimeException();
            } else {
                this.firstResponseReceived = true;
                this.observer.onNext(message);
                if (this.adapter.streamingResponse && this.adapter.autoRequestEnabled) {
                    this.adapter.request(1);
                }

            }
        }

        public void onClose(Status status, Metadata trailers) {
            if (status.isOk()) {
                this.observer.onCompleted();
            } else {
                this.observer.onError(status.asRuntimeException(trailers));
            }

        }

        public void onReady() {
            if (this.adapter.onReadyHandler != null) {
                this.adapter.onReadyHandler.run();
            }

        }

        void onStart() {
            if (this.adapter.initialRequest > 0) {
                this.adapter.request(this.adapter.initialRequest);
            }

        }
    }

    public static <ReqT, RespT> RespT blockingUnaryCall(ClientCall<ReqT, RespT> call, ReqT req) {
        throw new UnsupportedOperationException("blockingUnaryCall");
    }

    public static <ReqT, RespT> RespT blockingUnaryCall(Channel channel, MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, ReqT req) {
        throw new UnsupportedOperationException("blockingUnaryCall");
    }

    public static <ReqT, RespT> Iterator<RespT> blockingServerStreamingCall(ClientCall<ReqT, RespT> call, ReqT req) {
        throw new UnsupportedOperationException("blockingServerStreamingCall");
    }

    public static <ReqT, RespT> Iterator<RespT> blockingServerStreamingCall(Channel channel, MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, ReqT req) {
        throw new UnsupportedOperationException("blockingServerStreamingCall");
    }

    public static <ReqT, RespT> ListenableFuture<RespT> futureUnaryCall(ClientCall<ReqT, RespT> call, ReqT req) {
        throw new UnsupportedOperationException("futureUnaryCall");
    }

    static enum StubType {
        BLOCKING,
        FUTURE,
        ASYNC;
    }
}