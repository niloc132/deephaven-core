package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.notebook_pb;

import elemental2.core.Uint8Array;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.notebook_pb.MoveItemRequest",
        namespace = JsPackage.GLOBAL)
public class MoveItemRequest {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsOverlay
        static MoveItemRequest.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        String getNewpath();

        @JsProperty
        String getOldpath();

        @JsProperty
        void setNewpath(String newpath);

        @JsProperty
        void setOldpath(String oldpath);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsOverlay
        static MoveItemRequest.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        String getNewpath();

        @JsProperty
        String getOldpath();

        @JsProperty
        void setNewpath(String newpath);

        @JsProperty
        void setOldpath(String oldpath);
    }

    public static native MoveItemRequest deserializeBinary(Uint8Array bytes);

    public static native MoveItemRequest deserializeBinaryFromReader(
            MoveItemRequest message, Object reader);

    public static native void serializeBinaryToWriter(MoveItemRequest message, Object writer);

    public static native MoveItemRequest.ToObjectReturnType toObject(
            boolean includeInstance, MoveItemRequest msg);

    public native String getNewpath();

    public native String getOldpath();

    public native Uint8Array serializeBinary();

    public native void setNewpath(String value);

    public native void setOldpath(String value);

    public native MoveItemRequest.ToObjectReturnType0 toObject();

    public native MoveItemRequest.ToObjectReturnType0 toObject(boolean includeInstance);
}
