package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.hierarchicaltable_pb;

import elemental2.core.JsArray;
import elemental2.core.Uint8Array;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.RollupDescriptorDetails",
        namespace = JsPackage.GLOBAL)
public class RollupDescriptorDetails {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsOverlay
        static RollupDescriptorDetails.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        String getKeyWidthColumn();

        @JsProperty
        double getLeafNodeType();

        @JsProperty
        JsArray<String> getOutputInputColumnPairsList();

        @JsProperty
        void setKeyWidthColumn(String keyWidthColumn);

        @JsProperty
        void setLeafNodeType(double leafNodeType);

        @JsProperty
        void setOutputInputColumnPairsList(JsArray<String> outputInputColumnPairsList);

        @JsOverlay
        default void setOutputInputColumnPairsList(String[] outputInputColumnPairsList) {
            setOutputInputColumnPairsList(Js.<JsArray<String>>uncheckedCast(outputInputColumnPairsList));
        }
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsOverlay
        static RollupDescriptorDetails.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        String getKeyWidthColumn();

        @JsProperty
        double getLeafNodeType();

        @JsProperty
        JsArray<String> getOutputInputColumnPairsList();

        @JsProperty
        void setKeyWidthColumn(String keyWidthColumn);

        @JsProperty
        void setLeafNodeType(double leafNodeType);

        @JsProperty
        void setOutputInputColumnPairsList(JsArray<String> outputInputColumnPairsList);

        @JsOverlay
        default void setOutputInputColumnPairsList(String[] outputInputColumnPairsList) {
            setOutputInputColumnPairsList(Js.<JsArray<String>>uncheckedCast(outputInputColumnPairsList));
        }
    }

    public static native RollupDescriptorDetails deserializeBinary(Uint8Array bytes);

    public static native RollupDescriptorDetails deserializeBinaryFromReader(
            RollupDescriptorDetails message, Object reader);

    public static native void serializeBinaryToWriter(RollupDescriptorDetails message, Object writer);

    public static native RollupDescriptorDetails.ToObjectReturnType toObject(
            boolean includeInstance, RollupDescriptorDetails msg);

    public native String addOutputInputColumnPairs(String value, double index);

    public native String addOutputInputColumnPairs(String value);

    public native void clearOutputInputColumnPairsList();

    public native String getKeyWidthColumn();

    public native int getLeafNodeType();

    public native JsArray<String> getOutputInputColumnPairsList();

    public native Uint8Array serializeBinary();

    public native void setKeyWidthColumn(String value);

    public native void setLeafNodeType(int value);

    public native void setOutputInputColumnPairsList(JsArray<String> value);

    @JsOverlay
    public final void setOutputInputColumnPairsList(String[] value) {
        setOutputInputColumnPairsList(Js.<JsArray<String>>uncheckedCast(value));
    }

    public native RollupDescriptorDetails.ToObjectReturnType0 toObject();

    public native RollupDescriptorDetails.ToObjectReturnType0 toObject(boolean includeInstance);
}
