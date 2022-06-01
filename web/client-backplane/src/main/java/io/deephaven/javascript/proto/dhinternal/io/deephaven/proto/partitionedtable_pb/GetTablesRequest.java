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
        name = "dhinternal.io.deephaven.proto.partitionedtable_pb.GetTablesRequest",
        namespace = JsPackage.GLOBAL)
public class GetTablesRequest {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface TicketFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static GetTablesRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType of(Object o) {
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
            static GetTablesRequest.ToObjectReturnType.TicketFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            GetTablesRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(GetTablesRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<GetTablesRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<GetTablesRequest.ToObjectReturnType.TicketFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static GetTablesRequest.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        Object getResultId();

        @JsProperty
        double getRow();

        @JsProperty
        GetTablesRequest.ToObjectReturnType.TicketFieldType getTicket();

        @JsProperty
        void setResultId(Object resultId);

        @JsProperty
        void setRow(double row);

        @JsProperty
        void setTicket(GetTablesRequest.ToObjectReturnType.TicketFieldType ticket);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface TicketFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static GetTablesRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType of(
                        Object o) {
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
            static GetTablesRequest.ToObjectReturnType0.TicketFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            GetTablesRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    GetTablesRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<GetTablesRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<GetTablesRequest.ToObjectReturnType0.TicketFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static GetTablesRequest.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        Object getResultId();

        @JsProperty
        double getRow();

        @JsProperty
        GetTablesRequest.ToObjectReturnType0.TicketFieldType getTicket();

        @JsProperty
        void setResultId(Object resultId);

        @JsProperty
        void setRow(double row);

        @JsProperty
        void setTicket(GetTablesRequest.ToObjectReturnType0.TicketFieldType ticket);
    }

    public static native GetTablesRequest deserializeBinary(Uint8Array bytes);

    public static native GetTablesRequest deserializeBinaryFromReader(
            GetTablesRequest message, Object reader);

    public static native void serializeBinaryToWriter(GetTablesRequest message, Object writer);

    public static native GetTablesRequest.ToObjectReturnType toObject(
            boolean includeInstance, GetTablesRequest msg);

    public native void clearResultId();

    public native void clearRow();

    public native void clearTicket();

    public native int getKeysCase();

    public native Ticket getResultId();

    public native double getRow();

    public native Ticket getTicket();

    public native boolean hasResultId();

    public native boolean hasRow();

    public native boolean hasTicket();

    public native Uint8Array serializeBinary();

    public native void setResultId();

    public native void setResultId(Ticket value);

    public native void setRow(double value);

    public native void setTicket();

    public native void setTicket(Ticket value);

    public native GetTablesRequest.ToObjectReturnType0 toObject();

    public native GetTablesRequest.ToObjectReturnType0 toObject(boolean includeInstance);
}
