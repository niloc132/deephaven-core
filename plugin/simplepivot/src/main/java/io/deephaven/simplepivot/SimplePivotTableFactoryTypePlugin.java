//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.simplepivot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import io.deephaven.api.agg.spec.AggSpec;
import io.deephaven.engine.table.Table;
import io.deephaven.io.streams.ByteBufferInputStream;
import io.deephaven.plugin.type.ObjectCommunicationException;
import io.deephaven.plugin.type.ObjectType;
import io.deephaven.plugin.type.ObjectTypeBase;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * ObjectType plugin to enable clients to create PivotTables. Expose the {@link SimplePivotTable#FACTORY} instance to clients
 * (for example as a scope variable), and they will be able to fetch it and send pivot-creating messages to it.
 *
 * At this time, the plugin will accept JSON payloads - each request made to the server should contain a JSON payload describing the pivot to be created and a Table instance to apply to.
 * The JSON payload may optionally contain a requestId string - the server will reflect that ID, if present, back in the response payload, allowing for multiple raced requests.
 */
@AutoService(ObjectType.class)
public class SimplePivotTableFactoryTypePlugin extends ObjectTypeBase {
    public enum PivotAggregation {
        SUM(AggSpec.sum()),
        ABS_SUM(AggSpec.absSum()),
        AVG(AggSpec.avg()),
        MIN(AggSpec.min()),
        MAX(AggSpec.max()),
        MEDIAN(AggSpec.median()),
        COUNT_DISTINCT(AggSpec.countDistinct()),
        FIRST(AggSpec.first()),
        LAST(AggSpec.last()),
        STDDEV(AggSpec.std()),
        VAR(AggSpec.var()),
        T_DIGEST(AggSpec.tDigest()),
        UNIQUE(AggSpec.unique());

        private final AggSpec spec;

        PivotAggregation(AggSpec spec) {
            this.spec = spec;
        }

        public AggSpec getSpec() {
            return spec;
        }
    }

    public static class SimplePivotCreationRequest {
        public String requestId;
        public List<String> columnColNames;
        public List<String> rowColNames;
        public String valueColName;
        public PivotAggregation aggregation;
        public boolean hasTotals;
    }

    public static class SimplePivotCreationResponse {
        public String requestId;
        public String error;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public MessageStream compatibleClientConnection(Object object, MessageStream connection)
            throws ObjectCommunicationException {
        // Send a no-op reply, signalling ready to be used
        connection.onData(ByteBuffer.allocate(0));
        return new MessageStream() {
            @Override
            public void onData(ByteBuffer payload, Object... references) throws ObjectCommunicationException {
                SimplePivotCreationRequest request;
                try {
                    request = objectMapper.readValue((InputStream) new ByteBufferInputStream(payload), SimplePivotCreationRequest.class);
                } catch (IOException e) {
                    // If we can't read the request, we can't respond in the stream
                    throw new ObjectCommunicationException("Failed to deserialize json request payload", e);
                }
                SimplePivotCreationResponse response = new SimplePivotCreationResponse();
                response.requestId = request.requestId;
                if (references.length != 1 && !(references[0] instanceof Table)) {
                    response.error = "Expected a single table reference";
                    try {
                        connection.onData(ByteBuffer.wrap(objectMapper.writeValueAsBytes(response)));
                    } catch (JsonProcessingException e) {
                        throw new ObjectCommunicationException("Failed to serialize response while writing an error", e);
                    }
                    throw new ObjectCommunicationException("Expected a single table reference");
                }
                Table table = (Table) references[0];
                SimplePivotTable simplePivotTable = null;
                try {
                    if (table.isRefreshing()) {
                        simplePivotTable = table.getUpdateGraph().sharedLock().computeLocked(() -> {
                            return SimplePivotTable.FACTORY.create(table, request.columnColNames, request.rowColNames, request.valueColName, request.aggregation.getSpec(), request.hasTotals);
                        });
                    } else {
                        simplePivotTable = SimplePivotTable.FACTORY.create(table, request.columnColNames, request.rowColNames, request.valueColName, request.aggregation.getSpec(), request.hasTotals);
                    }
                } catch (Exception e) {
                    response.error = e.getMessage();
                    try {
                        connection.onData(ByteBuffer.wrap(objectMapper.writeValueAsBytes(response)));
                    } catch (JsonProcessingException e2) {
                        throw new ObjectCommunicationException("Failed to serialize response while writing an error", e2);
                    }
                    throw new ObjectCommunicationException("Failed to create pivot table", e);
                }

                try {
                    connection.onData(ByteBuffer.wrap(objectMapper.writeValueAsBytes(response)), simplePivotTable);
                } catch (JsonProcessingException e) {
                    throw new ObjectCommunicationException("Failed to serialize response while sending a pivot table", e);
                }
            }

            @Override
            public void onClose() {

            }
        };
    }

    @Override
    public String name() {
        return "simplepivot.SimplePivotTable.Factory";
    }

    @Override
    public boolean isType(Object object) {
        return object == SimplePivotTable.FACTORY;
    }
}
