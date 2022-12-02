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
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.TreeRequest",
        namespace = JsPackage.GLOBAL)
public class TreeRequest {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ResultViewIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static TreeRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType of(
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
            static TreeRequest.ToObjectReturnType.ResultViewIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            TreeRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    TreeRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<TreeRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<TreeRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static TreeRequest.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        String getIdentifierColumn();

        @JsProperty
        String getParentIdentifierColumn();

        @JsProperty
        TreeRequest.ToObjectReturnType.ResultViewIdFieldType getResultViewId();

        @JsProperty
        Object getSourceId();

        @JsProperty
        void setIdentifierColumn(String identifierColumn);

        @JsProperty
        void setParentIdentifierColumn(String parentIdentifierColumn);

        @JsProperty
        void setResultViewId(TreeRequest.ToObjectReturnType.ResultViewIdFieldType resultViewId);

        @JsProperty
        void setSourceId(Object sourceId);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ResultViewIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static TreeRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType of(
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
            static TreeRequest.ToObjectReturnType0.ResultViewIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            TreeRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    TreeRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<TreeRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<TreeRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static TreeRequest.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        String getIdentifierColumn();

        @JsProperty
        String getParentIdentifierColumn();

        @JsProperty
        TreeRequest.ToObjectReturnType0.ResultViewIdFieldType getResultViewId();

        @JsProperty
        Object getSourceId();

        @JsProperty
        void setIdentifierColumn(String identifierColumn);

        @JsProperty
        void setParentIdentifierColumn(String parentIdentifierColumn);

        @JsProperty
        void setResultViewId(TreeRequest.ToObjectReturnType0.ResultViewIdFieldType resultViewId);

        @JsProperty
        void setSourceId(Object sourceId);
    }

    public static native TreeRequest deserializeBinary(Uint8Array bytes);

    public static native TreeRequest deserializeBinaryFromReader(TreeRequest message, Object reader);

    public static native void serializeBinaryToWriter(TreeRequest message, Object writer);

    public static native TreeRequest.ToObjectReturnType toObject(
            boolean includeInstance, TreeRequest msg);

    public native void clearResultViewId();

    public native void clearSourceId();

    public native String getIdentifierColumn();

    public native String getParentIdentifierColumn();

    public native Ticket getResultViewId();

    public native Ticket getSourceId();

    public native boolean hasResultViewId();

    public native boolean hasSourceId();

    public native Uint8Array serializeBinary();

    public native void setIdentifierColumn(String value);

    public native void setParentIdentifierColumn(String value);

    public native void setResultViewId();

    public native void setResultViewId(Ticket value);

    public native void setSourceId();

    public native void setSourceId(Ticket value);

    public native TreeRequest.ToObjectReturnType0 toObject();

    public native TreeRequest.ToObjectReturnType0 toObject(boolean includeInstance);
}
