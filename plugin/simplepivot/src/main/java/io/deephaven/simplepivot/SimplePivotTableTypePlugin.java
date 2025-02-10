//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.simplepivot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import io.deephaven.engine.table.Table;
import io.deephaven.plugin.type.ObjectCommunicationException;
import io.deephaven.plugin.type.ObjectType;
import io.deephaven.plugin.type.ObjectTypeBase;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Plugin type for SimplePivotTable, allowing clients to connect and subscribe to updates, view the data. The initial
 * payload includes the key table and a JSON schema. The schema specifies the columns from the original table that are
 * used as row and column keys, and whether the table has totals.
 * <p>
 * The key table contains an integer column named {@literal __PIVOT_COLUMN} and each of the other {@code columnColNames}
 * columns. Clients subscribe to this table to match the corresponding column key values in the actual pivot table, sent
 * in later updates.
 * <p>
 * Each later update includes the pivot table itself, and may also include a totals table if the pivot is configured to
 * have one. Both will be resent when new columns are added. The pivot table includes columns for each of the
 * {@code rowColNames} columns, and each key found in {@literal __PIVOT_COLUMN} of the key table will be prefixed with
 * {@literal PIVOT_C_} as another column in the pivot table. If totals are enabled, there will be a
 * {@literal __TOTALS_COLUMN} as well, representing the total of each row. The totals table, if present, will not have
 * any row columns, but will have each of the other columns found in the pivot table.
 * <p>
 * The plugin will send replacement pivot tables as needed, but this update will race with the subscription to the key
 * table. Clients should wait for these to be consistent to render accurately - if a column prefixed with
 * {@literal PIVOT_C_} is found that does not match a value in the key table, an update will be along soon to specify
 * the key values. Likewise, if a row is found in the key table that does not match a column of the pivot table, a new
 * pivot table will arrive soon.
 * <p>
 * The pivot and totals table may also "tick/tock" with each other slightly.
 */
@AutoService(ObjectType.class)
public class SimplePivotTableTypePlugin extends ObjectTypeBase {
    public static class SimplePivotSchema {
        public List<String> columnColNames;
        public List<String> rowColNames;
        public String pivotDescription;
        public boolean hasTotals;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Runnable subscription;

    @Override
    public MessageStream compatibleClientConnection(Object object, MessageStream connection)
            throws ObjectCommunicationException {
        SimplePivotTable pivotTable = (SimplePivotTable) object;

        // Send column keys on startup, will "tick-tock" with the table, client has to figure it out
        ByteBuffer schemaPayload = null;
        try {
            schemaPayload = ByteBuffer.wrap(objectMapper.writeValueAsBytes(new SimplePivotSchema() {
                {
                    columnColNames = pivotTable.getColumnColNames();
                    rowColNames = pivotTable.getRowColNames();
                    hasTotals = pivotTable.getTotalsTable() != null;
                    pivotDescription = pivotTable.getPivotDescription();
                }
            }));
        } catch (JsonProcessingException e) {
            throw new ObjectCommunicationException("Failed to serialize schema", e);
        }
        connection.onData(schemaPayload, pivotTable.getColumnKeys());

        // Subscribe to updates
        this.subscription = pivotTable.subscribe(() -> {
            // Send current multijoined table
            try {
                // Safe to access both tables within a callback
                Table totalsTable = pivotTable.getTotalsTable();
                if (totalsTable == null) {
                    connection.onData(ByteBuffer.allocate(0), pivotTable.getTable());
                } else {
                    connection.onData(ByteBuffer.allocate(0), pivotTable.getTable(), totalsTable);
                }
            } catch (ObjectCommunicationException e) {
                e.printStackTrace();
                subscription.run();
            }
        });

        return new MessageStream() {
            @Override
            public void onData(ByteBuffer payload, Object... references) throws ObjectCommunicationException {
                // client sends no messages at this time
            }

            @Override
            public void onClose() {
                // shut down our subscription
                subscription.run();
            }
        };
    }

    @Override
    public String name() {
        return "simplepivot.SimplePivotTable";
    }

    @Override
    public boolean isType(Object object) {
        return object instanceof SimplePivotTable;
    }
}
