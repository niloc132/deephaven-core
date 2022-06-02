package io.deephaven.web.client.api.widget;

import elemental2.core.Uint8Array;
import elemental2.promise.Promise;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.object_pb.FetchObjectResponse;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.ticket_pb.Ticket;
import io.deephaven.web.client.api.Callbacks;
import io.deephaven.web.client.api.WorkerConnection;
import io.deephaven.web.shared.fu.JsBiConsumer;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

public class JsWidget {
    private final WorkerConnection connection;
    private final WidgetFetch fetch;
    private Ticket ticket;

    @JsFunction
    public interface WidgetFetch {
        void fetch(JsBiConsumer<Object, FetchObjectResponse> callback);
    }

    private FetchObjectResponse response;

    private JsWidgetExportedObject[] exportedObjects;

    public JsWidget(WorkerConnection connection, WidgetFetch fetch) {
        this.connection = connection;
        this.fetch = fetch;
    }

    @JsIgnore
    public Promise<JsWidget> refetch() {
        return Callbacks.grpcUnaryPromise(fetch::fetch)
                .then(response -> {
                    this.response = response;
                    return Promise.resolve(this);
                });
    }

    @JsProperty
    public String getType() {
        return response.getType();
    }

    @JsMethod
    public String getDataAsBase64() {
        return response.getData_asB64();
    }

    @JsMethod
    public Uint8Array getDataAsU8() {
        return response.getData_asU8();
    }

    @JsProperty
    public JsWidgetExportedObject[] getExportedObjects() {
        if (this.exportedObjects == null) {
            this.exportedObjects = response.getTypedExportIdList().asList().stream()
                    .map(ticket -> new JsWidgetExportedObject(connection, ticket))
                    .toArray(JsWidgetExportedObject[]::new);
        }

        return this.exportedObjects;
    }
}
