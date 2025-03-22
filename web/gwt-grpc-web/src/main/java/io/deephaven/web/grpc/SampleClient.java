package io.deephaven.web.grpc;

import elemental2.dom.URL;
import io.deephaven.web.grpc.impl.GrpcTransportFactory;
import io.deephaven.web.grpc.impl.MultiplexedWebsocketTransport;

public class SampleClient {
    public void call() {
        GrpcTransportFactory factory = new MultiplexedWebsocketTransport.Factory();
        GwtGrpcWebChannel channel = new GwtGrpcWebChannel(factory, new URL("http://localhost:10000"));
//        ConsoleServiceGrpc.ConsoleServiceStub consoleServiceStub = ConsoleServiceGrpc.newStub(channel);
//
//        consoleServiceStub.getConsoleTypes(GetConsoleTypesRequest.newBuilder().build(), new StreamObserver<GetConsoleTypesResponse>() {
//            @Override
//            public void onNext(GetConsoleTypesResponse getConsoleTypesResponse) {
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//        });
    }
}
