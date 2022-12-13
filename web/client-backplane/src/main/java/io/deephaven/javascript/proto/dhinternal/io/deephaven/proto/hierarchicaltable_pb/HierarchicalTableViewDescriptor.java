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
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.HierarchicalTableViewDescriptor",
        namespace = JsPackage.GLOBAL)
public class HierarchicalTableViewDescriptor {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface GetSchemaHeaderUnionType {
        @JsOverlay
        static HierarchicalTableViewDescriptor.GetSchemaHeaderUnionType of(Object o) {
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

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface SetSchemaHeaderValueUnionType {
        @JsOverlay
        static HierarchicalTableViewDescriptor.SetSchemaHeaderValueUnionType of(Object o) {
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

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface GetSchemaHeaderUnionType {
            @JsOverlay
            static HierarchicalTableViewDescriptor.ToObjectReturnType.GetSchemaHeaderUnionType of(
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

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface RollupFieldType {
            @JsOverlay
            static HierarchicalTableViewDescriptor.ToObjectReturnType.RollupFieldType create() {
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
                setOutputInputColumnPairsList(
                        Js.<JsArray<String>>uncheckedCast(outputInputColumnPairsList));
            }
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface TreeFieldType {
            @JsOverlay
            static HierarchicalTableViewDescriptor.ToObjectReturnType.TreeFieldType create() {
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

        @JsOverlay
        static HierarchicalTableViewDescriptor.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<String> getExpandByColumnsList();

        @JsProperty
        HierarchicalTableViewDescriptor.ToObjectReturnType.RollupFieldType getRollup();

        @JsProperty
        HierarchicalTableViewDescriptor.ToObjectReturnType.GetSchemaHeaderUnionType getSchemaHeader();

        @JsProperty
        HierarchicalTableViewDescriptor.ToObjectReturnType.TreeFieldType getTree();

        @JsProperty
        void setExpandByColumnsList(JsArray<String> expandByColumnsList);

        @JsOverlay
        default void setExpandByColumnsList(String[] expandByColumnsList) {
            setExpandByColumnsList(Js.<JsArray<String>>uncheckedCast(expandByColumnsList));
        }

        @JsProperty
        void setRollup(HierarchicalTableViewDescriptor.ToObjectReturnType.RollupFieldType rollup);

        @JsProperty
        void setSchemaHeader(
                HierarchicalTableViewDescriptor.ToObjectReturnType.GetSchemaHeaderUnionType schemaHeader);

        @JsOverlay
        default void setSchemaHeader(String schemaHeader) {
            setSchemaHeader(
                    Js.<HierarchicalTableViewDescriptor.ToObjectReturnType.GetSchemaHeaderUnionType>uncheckedCast(
                            schemaHeader));
        }

        @JsOverlay
        default void setSchemaHeader(Uint8Array schemaHeader) {
            setSchemaHeader(
                    Js.<HierarchicalTableViewDescriptor.ToObjectReturnType.GetSchemaHeaderUnionType>uncheckedCast(
                            schemaHeader));
        }

        @JsProperty
        void setTree(HierarchicalTableViewDescriptor.ToObjectReturnType.TreeFieldType tree);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface GetSchemaHeaderUnionType {
            @JsOverlay
            static HierarchicalTableViewDescriptor.ToObjectReturnType0.GetSchemaHeaderUnionType of(
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

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface RollupFieldType {
            @JsOverlay
            static HierarchicalTableViewDescriptor.ToObjectReturnType0.RollupFieldType create() {
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
                setOutputInputColumnPairsList(
                        Js.<JsArray<String>>uncheckedCast(outputInputColumnPairsList));
            }
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface TreeFieldType {
            @JsOverlay
            static HierarchicalTableViewDescriptor.ToObjectReturnType0.TreeFieldType create() {
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

        @JsOverlay
        static HierarchicalTableViewDescriptor.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<String> getExpandByColumnsList();

        @JsProperty
        HierarchicalTableViewDescriptor.ToObjectReturnType0.RollupFieldType getRollup();

        @JsProperty
        HierarchicalTableViewDescriptor.ToObjectReturnType0.GetSchemaHeaderUnionType getSchemaHeader();

        @JsProperty
        HierarchicalTableViewDescriptor.ToObjectReturnType0.TreeFieldType getTree();

        @JsProperty
        void setExpandByColumnsList(JsArray<String> expandByColumnsList);

        @JsOverlay
        default void setExpandByColumnsList(String[] expandByColumnsList) {
            setExpandByColumnsList(Js.<JsArray<String>>uncheckedCast(expandByColumnsList));
        }

        @JsProperty
        void setRollup(HierarchicalTableViewDescriptor.ToObjectReturnType0.RollupFieldType rollup);

        @JsProperty
        void setSchemaHeader(
                HierarchicalTableViewDescriptor.ToObjectReturnType0.GetSchemaHeaderUnionType schemaHeader);

        @JsOverlay
        default void setSchemaHeader(String schemaHeader) {
            setSchemaHeader(
                    Js.<HierarchicalTableViewDescriptor.ToObjectReturnType0.GetSchemaHeaderUnionType>uncheckedCast(
                            schemaHeader));
        }

        @JsOverlay
        default void setSchemaHeader(Uint8Array schemaHeader) {
            setSchemaHeader(
                    Js.<HierarchicalTableViewDescriptor.ToObjectReturnType0.GetSchemaHeaderUnionType>uncheckedCast(
                            schemaHeader));
        }

        @JsProperty
        void setTree(HierarchicalTableViewDescriptor.ToObjectReturnType0.TreeFieldType tree);
    }

    public static native HierarchicalTableViewDescriptor deserializeBinary(Uint8Array bytes);

    public static native HierarchicalTableViewDescriptor deserializeBinaryFromReader(
            HierarchicalTableViewDescriptor message, Object reader);

    public static native void serializeBinaryToWriter(
            HierarchicalTableViewDescriptor message, Object writer);

    public static native HierarchicalTableViewDescriptor.ToObjectReturnType toObject(
            boolean includeInstance, HierarchicalTableViewDescriptor msg);

    public native String addExpandByColumns(String value, double index);

    public native String addExpandByColumns(String value);

    public native void clearExpandByColumnsList();

    public native void clearRollup();

    public native void clearTree();

    public native int getDetailsCase();

    public native JsArray<String> getExpandByColumnsList();

    public native RollupDescriptorDetails getRollup();

    public native HierarchicalTableViewDescriptor.GetSchemaHeaderUnionType getSchemaHeader();

    public native String getSchemaHeader_asB64();

    public native Uint8Array getSchemaHeader_asU8();

    public native TreeDescriptorDetails getTree();

    public native boolean hasRollup();

    public native boolean hasTree();

    public native Uint8Array serializeBinary();

    public native void setExpandByColumnsList(JsArray<String> value);

    @JsOverlay
    public final void setExpandByColumnsList(String[] value) {
        setExpandByColumnsList(Js.<JsArray<String>>uncheckedCast(value));
    }

    public native void setRollup();

    public native void setRollup(RollupDescriptorDetails value);

    public native void setSchemaHeader(
            HierarchicalTableViewDescriptor.SetSchemaHeaderValueUnionType value);

    @JsOverlay
    public final void setSchemaHeader(String value) {
        setSchemaHeader(
                Js.<HierarchicalTableViewDescriptor.SetSchemaHeaderValueUnionType>uncheckedCast(value));
    }

    @JsOverlay
    public final void setSchemaHeader(Uint8Array value) {
        setSchemaHeader(
                Js.<HierarchicalTableViewDescriptor.SetSchemaHeaderValueUnionType>uncheckedCast(value));
    }

    public native void setTree();

    public native void setTree(TreeDescriptorDetails value);

    public native HierarchicalTableViewDescriptor.ToObjectReturnType0 toObject();

    public native HierarchicalTableViewDescriptor.ToObjectReturnType0 toObject(
            boolean includeInstance);
}
