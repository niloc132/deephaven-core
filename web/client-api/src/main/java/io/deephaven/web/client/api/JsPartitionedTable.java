package io.deephaven.web.client.api;

import elemental2.core.JsArray;
import elemental2.core.JsSet;
import elemental2.dom.CustomEvent;
import elemental2.dom.CustomEventInit;
import elemental2.dom.Event;
import elemental2.promise.Promise;
import io.deephaven.javascript.proto.dhinternal.arrow.flight.flatbuf.message_generated.org.apache.arrow.flatbuf.Message;
import io.deephaven.javascript.proto.dhinternal.arrow.flight.flatbuf.message_generated.org.apache.arrow.flatbuf.MessageHeader;
import io.deephaven.javascript.proto.dhinternal.arrow.flight.flatbuf.schema_generated.org.apache.arrow.flatbuf.Field;
import io.deephaven.javascript.proto.dhinternal.arrow.flight.flatbuf.schema_generated.org.apache.arrow.flatbuf.Schema;
import io.deephaven.javascript.proto.dhinternal.flatbuffers.ByteBuffer;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.partitionedtable_pb.GetTableRequest;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.partitionedtable_pb.MergeRequest;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.partitionedtable_pb.PartitionedTableDescriptor;
import io.deephaven.web.client.api.barrage.BarrageUtils;
import io.deephaven.web.client.api.barrage.def.ColumnDefinition;
import io.deephaven.web.client.api.subscription.SubscriptionTableData;
import io.deephaven.web.client.api.subscription.TableSubscription;
import io.deephaven.web.client.api.widget.JsWidget;
import io.deephaven.web.client.state.ClientTableState;
import io.deephaven.web.shared.data.RangeSet;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsType(namespace = "dh", name = "PartitionedTable")
public class JsPartitionedTable extends HasEventHandling {
    public static final String EVENT_KEYADDED = "keyadded",
            EVENT_DISCONNECT = JsTable.EVENT_DISCONNECT,
            EVENT_RECONNECT = JsTable.EVENT_RECONNECT,
            EVENT_RECONNECTFAILED = JsTable.EVENT_RECONNECTFAILED;

    private final WorkerConnection connection;
    private final JsWidget widget;
    private List<String> keyColumnTypes;
    private PartitionedTableDescriptor descriptor;
    private JsTable keys;
    private TableSubscription subscription;

    /*
     Represents the sorta-kinda memoized results, tables that we've already locally fetched from the tablemap,
     and if all references to a table are released, entries here will be replaced with unresolved instances so
     we don't leak server references or memory.
     Keys are Object[], even with one element, so that we can easily hash without an extra wrapper object. Since
     columns are consistent with a PartitionedTable, we will not worry about "foo" vs ["foo"] as being different
     entries.
    */
    private final Map<JsArray<Any>, JsLazy<Promise<ClientTableState>>> tables = new HashMap<>();


    @JsIgnore
    public JsPartitionedTable(WorkerConnection connection, JsWidget widget) {
        this.connection = connection;
        this.widget = widget;
    }

    @JsIgnore
    public Promise<JsPartitionedTable> refetch() {
        if (keys != null) {
            keys.close();
        }
        if (subscription != null) {
            subscription.close();
        }
        return widget.refetch().then(w -> {
            descriptor = PartitionedTableDescriptor.deserializeBinary(w.getDataAsU8());

            keyColumnTypes = new ArrayList<>();
            ColumnDefinition[] columnDefinitions = BarrageUtils.readColumnDefinitions(BarrageUtils.readSchemaMessage(descriptor.getConstituentDefinitionSchema_asU8()));
            for (int i = 0; i < columnDefinitions.length; i++) {
                ColumnDefinition columnDefinition = columnDefinitions[i];
                if (descriptor.getKeyColumnNamesList().indexOf(columnDefinition.getName()) != -1) {
                    keyColumnTypes.add(columnDefinition.getType());
                }
            }

            return w.getExportedObjects()[0].fetch();
        }).then(result -> {
            keys = (JsTable) result;
            subscription = keys.subscribe(JsArray.asJsArray(keys.findColumns(descriptor.getKeyColumnNamesList().asArray(new String[0]))));
            subscription.addEventListener(TableSubscription.EVENT_UPDATED, this::handleKeys);

            //TODO consider being kinder and don't return until the snapshot is back from the server
            return Promise.resolve(this);
        });
    }

    private void handleKeys(Event update) {
        //noinspection unchecked
        CustomEvent<SubscriptionTableData.UpdateEventData> event = (CustomEvent<SubscriptionTableData.UpdateEventData>) update;

        // We're only interested in added rows, send an event indicating the new keys that are available
        SubscriptionTableData.UpdateEventData eventData = event.detail;
        RangeSet added = eventData.getAdded().getRange();
        added.indexIterator().forEachRemaining((long index) -> {
            // extract the key to use
            JsArray<Any> key = eventData.getColumns().map((c, p1, p2) -> eventData.getData(index, c));
            populateLazyTable(key, index);
            CustomEventInit init = CustomEventInit.create();
            init.setDetail(key);
            fireEvent(EVENT_KEYADDED, init);
        });
    }

    private void populateLazyTable(JsArray<Any> key, long index) {
        tables.put(key, JsLazy.of(() -> {
            // If we've entered this lambda, the JsLazy is being used, so we need to go ahead and get the tablehandle
            final ClientTableState entry = connection.newState((c, cts, metadata) -> {

                //TODO make this parallel
                //TODO stop leaking this table
                connection.newTable(
                        descriptor.getKeyColumnNamesList().asArray(new String[0]),
                        keyColumnTypes.toArray(new String[0]),
                        key.map((p0, p1, p2) -> JsArray.of((Object)p0).asArray(new Object[0])).asArray(new Object[0][]),
                        null,
                        this
                ).then(table -> {
                    GetTableRequest getTableRequest = new GetTableRequest();
                    getTableRequest.setPartitionedTable(widget.getTicket());
                    getTableRequest.setTicket(table.getHandle().makeTicket());
                    getTableRequest.setResultId(cts.getHandle().makeTicket());
                    connection.partitionedTableServiceClient().getTable(getTableRequest, connection.metadata(), c::apply);
                    return null;
                });
            },
            "tablemap key " + key);

            // later, when the CTS is released, remove this "table" from the map and replace with an unresolved JsLazy
            entry.onRunning(
                    ignore -> {},
                    ignore -> {},
                    () -> populateLazyTable(key, index)
            );

            // we'll make a table to return later, this func here just produces the JsLazy of the CTS
            return entry.refetch(this, connection.metadata());

        }));
    }


    public Promise<JsTable> getTable(Object key) {
        // Wrap non-arrays in an array so we are consistent with how we track keys
        if (!JsArray.isArray(key)) {
            key = JsArray.of(key);
        }
        // Every caller gets a fresh table instance, and when all are closed, the CTS will be released.
        // See #populateLazyTable for how that is tracked.
        //noinspection SuspiciousMethodCalls - we know it is an array, see above
        final JsLazy<Promise<ClientTableState>> entry = tables.get(key);
        if (entry == null) {
            // key doesn't even exist, just hand back a null table
            return Promise.resolve((JsTable) null);
        }
        return entry.get().then(cts -> Promise.resolve(new JsTable(cts.getConnection(), cts)));
    }

    public Promise<JsTable> getMergedTable() {
        return connection.newState((c, cts, metadata) -> {
                    MergeRequest requestMessage = new MergeRequest();
                    requestMessage.setPartitionedTable(widget.getTicket());
                    requestMessage.setResultId(cts.getHandle().makeTicket());
                    connection.partitionedTableServiceClient().merge(requestMessage, connection.metadata(), c::apply);
                }, "tablemap merged table")
                .refetch(this, connection.metadata())
                .then(cts -> Promise.resolve(new JsTable(cts.getConnection(), cts)));
    }

    public JsSet<Object> getKeys() {
        if (keys.getColumns().length == 1) {
            return new JsSet<>(tables.keySet().stream().map(arr -> arr.getAt(0)).toArray());
        }
        return new JsSet<>(tables.keySet().toArray());
    }

    @JsProperty(name = "size")
    public int size() {
        return tables.size();
    }

    public void close() {
        if (keys != null) {
            keys.close();
        }
        if (subscription != null) {
            subscription.close();
        }
    }

}
