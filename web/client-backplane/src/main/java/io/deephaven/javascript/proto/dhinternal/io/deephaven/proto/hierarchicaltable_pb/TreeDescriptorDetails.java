package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.hierarchicaltable_pb;

import elemental2.core.Uint8Array;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.TreeDescriptorDetails",
        namespace = JsPackage.GLOBAL)
public class TreeDescriptorDetails {
    public static native TreeDescriptorDetails deserializeBinary(Uint8Array bytes);

    public static native TreeDescriptorDetails deserializeBinaryFromReader(
            TreeDescriptorDetails message, Object reader);

    public static native void serializeBinaryToWriter(TreeDescriptorDetails message, Object writer);

    public static native Object toObject(boolean includeInstance, TreeDescriptorDetails msg);

    public native Uint8Array serializeBinary();

    public native Object toObject();

    public native Object toObject(boolean includeInstance);
}
