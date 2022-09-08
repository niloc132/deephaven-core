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
        name = "dhinternal.io.deephaven.proto.notebook_pb.ByteRange",
        namespace = JsPackage.GLOBAL)
public class ByteRange {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsOverlay
        static ByteRange.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        double getEnd();

        @JsProperty
        double getStart();

        @JsProperty
        void setEnd(double end);

        @JsProperty
        void setStart(double start);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsOverlay
        static ByteRange.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        double getEnd();

        @JsProperty
        double getStart();

        @JsProperty
        void setEnd(double end);

        @JsProperty
        void setStart(double start);
    }

    public static native ByteRange deserializeBinary(Uint8Array bytes);

    public static native ByteRange deserializeBinaryFromReader(ByteRange message, Object reader);

    public static native void serializeBinaryToWriter(ByteRange message, Object writer);

    public static native ByteRange.ToObjectReturnType toObject(
            boolean includeInstance, ByteRange msg);

    public native double getEnd();

    public native double getStart();

    public native Uint8Array serializeBinary();

    public native void setEnd(double value);

    public native void setStart(double value);

    public native ByteRange.ToObjectReturnType0 toObject();

    public native ByteRange.ToObjectReturnType0 toObject(boolean includeInstance);
}
