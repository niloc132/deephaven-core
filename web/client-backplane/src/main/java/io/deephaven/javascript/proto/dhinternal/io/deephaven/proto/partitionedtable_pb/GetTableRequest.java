package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.partitionedtable_pb;

import elemental2.core.Uint8Array;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.ticket_pb.Ticket;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.partitionedtable_pb.GetTableRequest",
        namespace = JsPackage.GLOBAL)
public class GetTableRequest {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface TicketFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static GetTableRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType of(Object o) {
                    return Js.cast(o);
                }

                @JsOverlay
                default String asString() {
                    return Js.asString(this);
                }

                @JsOverlay
                default Uint8Array asUint8Array() {
                    return Js.cast(this);
                }

                @JsOverlay
                default boolean isString() {
                    return (Object) this instanceof String;
                }

                @JsOverlay
                default boolean isUint8Array() {
                    return (Object) this instanceof Uint8Array;
                }
            }

            @JsOverlay
            static GetTableRequest.ToObjectReturnType.TicketFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            GetTableRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(GetTableRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<GetTableRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<GetTableRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static GetTableRequest.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        Object getResultId();

        @JsProperty
        String getRow();

        @JsProperty
        GetTableRequest.ToObjectReturnType.TicketFieldType getTicket();

        @JsProperty
        void setResultId(Object resultId);

        @JsProperty
        void setRow(String row);

        @JsProperty
        void setTicket(GetTableRequest.ToObjectReturnType.TicketFieldType ticket);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface TicketFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static GetTableRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType of(Object o) {
                    return Js.cast(o);
                }

                @JsOverlay
                default String asString() {
                    return Js.asString(this);
                }

                @JsOverlay
                default Uint8Array asUint8Array() {
                    return Js.cast(this);
                }

                @JsOverlay
                default boolean isString() {
                    return (Object) this instanceof String;
                }

                @JsOverlay
                default boolean isUint8Array() {
                    return (Object) this instanceof Uint8Array;
                }
            }

            @JsOverlay
            static GetTableRequest.ToObjectReturnType0.TicketFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            GetTableRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(GetTableRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<GetTableRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<GetTableRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static GetTableRequest.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        Object getResultId();

        @JsProperty
        String getRow();

        @JsProperty
        GetTableRequest.ToObjectReturnType0.TicketFieldType getTicket();

        @JsProperty
        void setResultId(Object resultId);

        @JsProperty
        void setRow(String row);

        @JsProperty
        void setTicket(GetTableRequest.ToObjectReturnType0.TicketFieldType ticket);
    }

    public static native GetTableRequest deserializeBinary(Uint8Array bytes);

    public static native GetTableRequest deserializeBinaryFromReader(
            GetTableRequest message, Object reader);

    public static native void serializeBinaryToWriter(GetTableRequest message, Object writer);

    public static native GetTableRequest.ToObjectReturnType toObject(
            boolean includeInstance, GetTableRequest msg);

    public native void clearResultId();

    public native void clearRow();

    public native void clearTicket();

    public native int getKeysCase();

    public native Ticket getResultId();

    public native String getRow();

    public native Ticket getTicket();

    public native boolean hasResultId();

    public native boolean hasRow();

    public native boolean hasTicket();

    public native Uint8Array serializeBinary();

    public native void setResultId();

    public native void setResultId(Ticket value);

    public native void setRow(String value);

    public native void setTicket();

    public native void setTicket(Ticket value);

    public native GetTableRequest.ToObjectReturnType0 toObject();

    public native GetTableRequest.ToObjectReturnType0 toObject(boolean includeInstance);
}
