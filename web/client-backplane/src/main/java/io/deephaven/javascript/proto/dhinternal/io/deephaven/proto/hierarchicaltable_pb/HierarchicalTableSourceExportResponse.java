package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.hierarchicaltable_pb;

import elemental2.core.Uint8Array;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.HierarchicalTableSourceExportResponse",
        namespace = JsPackage.GLOBAL)
public class HierarchicalTableSourceExportResponse {
    public static native HierarchicalTableSourceExportResponse deserializeBinary(Uint8Array bytes);

    public static native HierarchicalTableSourceExportResponse deserializeBinaryFromReader(
            HierarchicalTableSourceExportResponse message, Object reader);

    public static native void serializeBinaryToWriter(
            HierarchicalTableSourceExportResponse message, Object writer);

    public static native Object toObject(
            boolean includeInstance, HierarchicalTableSourceExportResponse msg);

    public native Uint8Array serializeBinary();

    public native Object toObject();

    public native Object toObject(boolean includeInstance);
}
