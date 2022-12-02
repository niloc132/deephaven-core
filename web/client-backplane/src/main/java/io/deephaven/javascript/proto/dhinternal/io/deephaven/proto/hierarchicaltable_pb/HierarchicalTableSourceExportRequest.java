package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.hierarchicaltable_pb;

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
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.HierarchicalTableSourceExportRequest",
        namespace = JsPackage.GLOBAL)
public class HierarchicalTableSourceExportRequest {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ResultIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static HierarchicalTableSourceExportRequest.ToObjectReturnType.ResultIdFieldType.GetTicketUnionType of(
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
            static HierarchicalTableSourceExportRequest.ToObjectReturnType.ResultIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableSourceExportRequest.ToObjectReturnType.ResultIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    HierarchicalTableSourceExportRequest.ToObjectReturnType.ResultIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<HierarchicalTableSourceExportRequest.ToObjectReturnType.ResultIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<HierarchicalTableSourceExportRequest.ToObjectReturnType.ResultIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static HierarchicalTableSourceExportRequest.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        HierarchicalTableSourceExportRequest.ToObjectReturnType.ResultIdFieldType getResultId();

        @JsProperty
        Object getViewId();

        @JsProperty
        void setResultId(
                HierarchicalTableSourceExportRequest.ToObjectReturnType.ResultIdFieldType resultId);

        @JsProperty
        void setViewId(Object viewId);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ResultIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static HierarchicalTableSourceExportRequest.ToObjectReturnType0.ResultIdFieldType.GetTicketUnionType of(
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
            static HierarchicalTableSourceExportRequest.ToObjectReturnType0.ResultIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableSourceExportRequest.ToObjectReturnType0.ResultIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    HierarchicalTableSourceExportRequest.ToObjectReturnType0.ResultIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<HierarchicalTableSourceExportRequest.ToObjectReturnType0.ResultIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<HierarchicalTableSourceExportRequest.ToObjectReturnType0.ResultIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static HierarchicalTableSourceExportRequest.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        HierarchicalTableSourceExportRequest.ToObjectReturnType0.ResultIdFieldType getResultId();

        @JsProperty
        Object getViewId();

        @JsProperty
        void setResultId(
                HierarchicalTableSourceExportRequest.ToObjectReturnType0.ResultIdFieldType resultId);

        @JsProperty
        void setViewId(Object viewId);
    }

    public static native HierarchicalTableSourceExportRequest deserializeBinary(Uint8Array bytes);

    public static native HierarchicalTableSourceExportRequest deserializeBinaryFromReader(
            HierarchicalTableSourceExportRequest message, Object reader);

    public static native void serializeBinaryToWriter(
            HierarchicalTableSourceExportRequest message, Object writer);

    public static native HierarchicalTableSourceExportRequest.ToObjectReturnType toObject(
            boolean includeInstance, HierarchicalTableSourceExportRequest msg);

    public native void clearResultId();

    public native void clearViewId();

    public native Ticket getResultId();

    public native Ticket getViewId();

    public native boolean hasResultId();

    public native boolean hasViewId();

    public native Uint8Array serializeBinary();

    public native void setResultId();

    public native void setResultId(Ticket value);

    public native void setViewId();

    public native void setViewId(Ticket value);

    public native HierarchicalTableSourceExportRequest.ToObjectReturnType0 toObject();

    public native HierarchicalTableSourceExportRequest.ToObjectReturnType0 toObject(
            boolean includeInstance);
}
