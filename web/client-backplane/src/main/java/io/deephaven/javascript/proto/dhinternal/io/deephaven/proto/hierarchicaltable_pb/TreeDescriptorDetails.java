package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.hierarchicaltable_pb;

import elemental2.core.Uint8Array;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.TreeDescriptorDetails",
        namespace = JsPackage.GLOBAL)
public class TreeDescriptorDetails {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsOverlay
        static TreeDescriptorDetails.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        String getRowDepthColumn();

        @JsProperty
        String getRowExpandableColumn();

        @JsProperty
        void setRowDepthColumn(String rowDepthColumn);

        @JsProperty
        void setRowExpandableColumn(String rowExpandableColumn);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsOverlay
        static TreeDescriptorDetails.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        String getRowDepthColumn();

        @JsProperty
        String getRowExpandableColumn();

        @JsProperty
        void setRowDepthColumn(String rowDepthColumn);

        @JsProperty
        void setRowExpandableColumn(String rowExpandableColumn);
    }

    public static native TreeDescriptorDetails deserializeBinary(Uint8Array bytes);

    public static native TreeDescriptorDetails deserializeBinaryFromReader(
            TreeDescriptorDetails message, Object reader);

    public static native void serializeBinaryToWriter(TreeDescriptorDetails message, Object writer);

    public static native TreeDescriptorDetails.ToObjectReturnType toObject(
            boolean includeInstance, TreeDescriptorDetails msg);

    public native String getRowDepthColumn();

    public native String getRowExpandableColumn();

    public native Uint8Array serializeBinary();

    public native void setRowDepthColumn(String value);

    public native void setRowExpandableColumn(String value);

    public native TreeDescriptorDetails.ToObjectReturnType0 toObject();

    public native TreeDescriptorDetails.ToObjectReturnType0 toObject(boolean includeInstance);
}
