package io.deephaven.web.grpc;

import elemental2.dom.URL;
import io.deephaven.proto.backplane.script.grpc.ConsoleServiceGrpc;
import io.deephaven.proto.backplane.script.grpc.GetConsoleTypesRequest;
import io.deephaven.proto.backplane.script.grpc.GetConsoleTypesResponse;
import io.deephaven.web.grpc.impl.GrpcTransportFactory;
import io.deephaven.web.grpc.impl.MultiplexedWebsocketTransport;
import io.grpc.stub.StreamObserver;

public class SampleClient {
    public void call() {
        GrpcTransportFactory factory = new MultiplexedWebsocketTransport.Factory();
        GwtGrpcWebChannel channel = new GwtGrpcWebChannel(factory, new URL("http://localhost:10000"));
        ConsoleServiceGrpc.ConsoleServiceStub consoleServiceStub = ConsoleServiceGrpc.newStub(channel);

        GetConsoleTypesRequest build = GetConsoleTypesRequest.newBuilder().build();
        consoleServiceStub.getConsoleTypes(build, new StreamObserver<GetConsoleTypesResponse>() {
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
