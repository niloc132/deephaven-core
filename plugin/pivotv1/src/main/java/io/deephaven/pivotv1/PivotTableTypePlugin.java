//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.pivotv1;

import com.google.auto.service.AutoService;
import io.deephaven.plugin.type.ObjectCommunicationException;
import io.deephaven.plugin.type.ObjectType;
import io.deephaven.plugin.type.ObjectTypeBase;

import java.nio.ByteBuffer;

/**
 * Initial payload includes the table with keys, all later payloads are the pivot itself, repeated for new keys being
 * added.
 */
@AutoService(ObjectType.class)
public class PivotTableTypePlugin extends ObjectTypeBase {
    @Override
    public MessageStream compatibleClientConnection(Object object, MessageStream connection)
            throws ObjectCommunicationException {
        PivotTable pivotTable = (PivotTable) object;
        // Subscribe to updates
        pivotTable.subscribe(() -> {
            // Send current multijoined table
            try {
                connection.onData(ByteBuffer.allocate(0), pivotTable.getTable());
            } catch (ObjectCommunicationException e) {
                // Disconnect
                // TODO
            }
        });
        // Send column keys on startup, will "tick-tock" with the table, client has to figure it out
        connection.onData(ByteBuffer.allocate(0), pivotTable.getColumnKeys());

        return new MessageStream() {
            @Override
            public void onData(ByteBuffer payload, Object... references) throws ObjectCommunicationException {
                // client sends no messages
            }

            @Override
            public void onClose() {
                // shut down our subscription
                // TODO
            }
        };
    }

    @Override
    public String name() {
        return "pivotv1.PivotTable";
    }

    @Override
    public boolean isType(Object object) {
        return object instanceof PivotTable;
    }
}
