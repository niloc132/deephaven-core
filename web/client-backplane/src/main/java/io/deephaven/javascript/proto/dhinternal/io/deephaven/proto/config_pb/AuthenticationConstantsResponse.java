package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.config_pb;

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
        name = "dhinternal.io.deephaven.proto.config_pb.AuthenticationConstantsResponse",
        namespace = JsPackage.GLOBAL)
public class AuthenticationConstantsResponse {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ConfigValuesListFieldType {
            @JsOverlay
            static AuthenticationConstantsResponse.ToObjectReturnType.ConfigValuesListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            String getKey();

            @JsProperty
            String getStringValue();

            @JsProperty
            void setKey(String key);

            @JsProperty
            void setStringValue(String stringValue);
        }

        @JsOverlay
        static AuthenticationConstantsResponse.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<AuthenticationConstantsResponse.ToObjectReturnType.ConfigValuesListFieldType> getConfigValuesList();

        @JsOverlay
        default void setConfigValuesList(
                AuthenticationConstantsResponse.ToObjectReturnType.ConfigValuesListFieldType[] configValuesList) {
            setConfigValuesList(
                    Js.<JsArray<AuthenticationConstantsResponse.ToObjectReturnType.ConfigValuesListFieldType>>uncheckedCast(
                            configValuesList));
        }

        @JsProperty
        void setConfigValuesList(
                JsArray<AuthenticationConstantsResponse.ToObjectReturnType.ConfigValuesListFieldType> configValuesList);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ConfigValuesListFieldType {
            @JsOverlay
            static AuthenticationConstantsResponse.ToObjectReturnType0.ConfigValuesListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            String getKey();

            @JsProperty
            String getStringValue();

            @JsProperty
            void setKey(String key);

            @JsProperty
            void setStringValue(String stringValue);
        }

        @JsOverlay
        static AuthenticationConstantsResponse.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<AuthenticationConstantsResponse.ToObjectReturnType0.ConfigValuesListFieldType> getConfigValuesList();

        @JsOverlay
        default void setConfigValuesList(
                AuthenticationConstantsResponse.ToObjectReturnType0.ConfigValuesListFieldType[] configValuesList) {
            setConfigValuesList(
                    Js.<JsArray<AuthenticationConstantsResponse.ToObjectReturnType0.ConfigValuesListFieldType>>uncheckedCast(
                            configValuesList));
        }

        @JsProperty
        void setConfigValuesList(
                JsArray<AuthenticationConstantsResponse.ToObjectReturnType0.ConfigValuesListFieldType> configValuesList);
    }

    public static native AuthenticationConstantsResponse deserializeBinary(Uint8Array bytes);

    public static native AuthenticationConstantsResponse deserializeBinaryFromReader(
            AuthenticationConstantsResponse message, Object reader);

    public static native void serializeBinaryToWriter(
            AuthenticationConstantsResponse message, Object writer);

    public static native AuthenticationConstantsResponse.ToObjectReturnType toObject(
            boolean includeInstance, AuthenticationConstantsResponse msg);

    public native ConfigPair addConfigValues();

    public native ConfigPair addConfigValues(ConfigPair value, double index);

    public native ConfigPair addConfigValues(ConfigPair value);

    public native void clearConfigValuesList();

    public native JsArray<ConfigPair> getConfigValuesList();

    public native Uint8Array serializeBinary();

    @JsOverlay
    public final void setConfigValuesList(ConfigPair[] value) {
        setConfigValuesList(Js.<JsArray<ConfigPair>>uncheckedCast(value));
    }

    public native void setConfigValuesList(JsArray<ConfigPair> value);

    public native AuthenticationConstantsResponse.ToObjectReturnType0 toObject();

    public native AuthenticationConstantsResponse.ToObjectReturnType0 toObject(
            boolean includeInstance);
}
