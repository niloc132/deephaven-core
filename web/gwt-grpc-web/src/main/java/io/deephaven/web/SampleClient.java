package io.deephaven.web;

import elemental2.dom.URL;
import io.deephaven.proto.backplane.script.grpc.ConsoleServiceGrpc;
import io.deephaven.proto.backplane.script.grpc.GetConsoleTypesRequest;
import io.deephaven.proto.backplane.script.grpc.GetConsoleTypesResponse;
import io.deephaven.web.grpc.GwtGrpcWebChannel;
import io.deephaven.web.grpc.impl.GrpcTransportFactory;
import io.deephaven.web.grpc.impl.MultiplexedWebsocketTransport;
import io.grpc.stub.StreamObserver;

public class SampleClient {
    public void call() {
        GrpcTransportFactory factory = new MultiplexedWebsocketTransport.Factory();
        ConsoleServiceGrpc.ConsoleServiceStub consoleServiceStub = ConsoleServiceGrpc.newStub(new GwtGrpcWebChannel(factory, new URL("http://localhost:10000")));

        consoleServiceStub.getConsoleTypes(GetConsoleTypesRequest.newBuilder().build(), new StreamObserver<GetConsoleTypesResponse>() {
            @Override
            public void onNext(GetConsoleTypesResponse getConsoleTypesResponse) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        });
    }
}
