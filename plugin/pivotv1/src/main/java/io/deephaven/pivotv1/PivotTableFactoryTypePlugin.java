//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.pivotv1;

import com.google.auto.service.AutoService;
import io.deephaven.plugin.type.ObjectCommunicationException;
import io.deephaven.plugin.type.ObjectType;
import io.deephaven.plugin.type.ObjectTypeBase;

import java.nio.ByteBuffer;

@AutoService(ObjectType.class)
public class PivotTableFactoryTypePlugin extends ObjectTypeBase {
    @Override
    public MessageStream compatibleClientConnection(Object object, MessageStream connection)
            throws ObjectCommunicationException {
        // Send a no-op reply, signalling ready to be used
        connection.onData(ByteBuffer.allocate(0));
        return new MessageStream() {
            @Override
            public void onData(ByteBuffer payload, Object... references) throws ObjectCommunicationException {
                // Read the table object and the params to the request, respond with a new pivot instance
                // TODO
            }

            @Override
            public void onClose() {

            }
        };
    }

    @Override
    public String name() {
        return "pivotv1.PivotTable.Factory";
    }

    @Override
    public boolean isType(Object object) {
        return object == PivotTable.Factory.class;
    }
}
