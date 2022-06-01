package io.deephaven.partitionedtable;

import com.google.auto.service.AutoService;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.protobuf.ByteString;
import io.deephaven.engine.table.PartitionedTable;
import io.deephaven.extensions.barrage.util.BarrageUtil;
import io.deephaven.plugin.type.ObjectType;
import io.deephaven.plugin.type.ObjectTypeClassBase;
import io.deephaven.proto.backplane.grpc.PartitionedTableDescriptor;
import io.deephaven.proto.flight.util.MessageHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

@AutoService(ObjectType.class)
public class PartitionedTableTypePlugin extends ObjectTypeClassBase<PartitionedTable> {
    public PartitionedTableTypePlugin(String name, Class<PartitionedTable> clazz) {
        super(name, clazz);
    }

    @Override
    public void writeToImpl(Exporter exporter, PartitionedTable object, OutputStream out) throws IOException {
        exporter.reference(object, false, true);
        // Send Schema wrapped in Message
        final FlatBufferBuilder builder = new FlatBufferBuilder();
        final int schemaOffset = BarrageUtil.makeSchemaPayload(builder, object.constituentDefinition(),
                Collections.emptyMap());
        builder.finish(MessageHelper.wrapInMessage(builder, schemaOffset,
                org.apache.arrow.flatbuf.MessageHeader.Schema));

        PartitionedTableDescriptor result = PartitionedTableDescriptor.newBuilder()
                .addAllKeyColumnNames(object.keyColumnNames())
                .setUniqueKeys(object.uniqueKeys())
                .setConstituentDefinitionSchema(ByteString.copyFrom(builder.dataBuffer()))
                .setConstituentColumnName(object.constituentColumnName())
                .setConstituentChangesPermitted(object.constituentChangesPermitted())
                .build();
        result.writeTo(out);
    }
}
