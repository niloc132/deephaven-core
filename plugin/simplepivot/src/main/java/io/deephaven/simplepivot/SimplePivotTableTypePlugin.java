//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.simplepivot;

import com.google.auto.service.AutoService;
import io.deephaven.engine.table.Table;
import io.deephaven.plugin.type.ObjectCommunicationException;
import io.deephaven.plugin.type.ObjectType;
import io.deephaven.plugin.type.ObjectTypeBase;

import java.nio.ByteBuffer;

/**
 * Initial payload includes the table with keys, all later payloads are the pivot itself, repeated for new keys being
 * added.
 */
@AutoService(ObjectType.class)
public class SimplePivotTableTypePlugin extends ObjectTypeBase {
    private Runnable subscription;

    @Override
    public MessageStream compatibleClientConnection(Object object, MessageStream connection)
            throws ObjectCommunicationException {
        SimplePivotTable pivotTable = (SimplePivotTable) object;

        // Send column keys on startup, will "tick-tock" with the table, client has to figure it out
        connection.onData(ByteBuffer.allocate(0), pivotTable.getColumnKeys());
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
