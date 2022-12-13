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
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.HierarchicalTableViewKeyTableDescriptor",
        namespace = JsPackage.GLOBAL)
public class HierarchicalTableViewKeyTableDescriptor {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface KeyTableFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType.KeyTableFieldType.GetTicketUnionType of(
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
            static HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType.KeyTableFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType.KeyTableFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType.KeyTableFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType.KeyTableFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType.KeyTableFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType.KeyTableFieldType getKeyTable();

        @JsProperty
        String getKeyTableActionColumn();

        @JsProperty
        void setKeyTable(
                HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType.KeyTableFieldType keyTable);

        @JsProperty
        void setKeyTableActionColumn(String keyTableActionColumn);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface KeyTableFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0.KeyTableFieldType.GetTicketUnionType of(
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
            static HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0.KeyTableFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0.KeyTableFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0.KeyTableFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0.KeyTableFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0.KeyTableFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0.KeyTableFieldType getKeyTable();

        @JsProperty
        String getKeyTableActionColumn();

        @JsProperty
        void setKeyTable(
                HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0.KeyTableFieldType keyTable);

        @JsProperty
        void setKeyTableActionColumn(String keyTableActionColumn);
    }

    public static native HierarchicalTableViewKeyTableDescriptor deserializeBinary(Uint8Array bytes);

    public static native HierarchicalTableViewKeyTableDescriptor deserializeBinaryFromReader(
            HierarchicalTableViewKeyTableDescriptor message, Object reader);

    public static native void serializeBinaryToWriter(
            HierarchicalTableViewKeyTableDescriptor message, Object writer);

    public static native HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType toObject(
            boolean includeInstance, HierarchicalTableViewKeyTableDescriptor msg);

    public native void clearKeyTable();

    public native void clearKeyTableActionColumn();

    public native Ticket getKeyTable();

    public native String getKeyTableActionColumn();

    public native boolean hasKeyTable();

    public native boolean hasKeyTableActionColumn();

    public native Uint8Array serializeBinary();

    public native void setKeyTable();

    public native void setKeyTable(Ticket value);

    public native void setKeyTableActionColumn(String value);

    public native HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0 toObject();

    public native HierarchicalTableViewKeyTableDescriptor.ToObjectReturnType0 toObject(
            boolean includeInstance);
}
