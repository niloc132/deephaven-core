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
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.HierarchicalTableDescriptor",
        namespace = JsPackage.GLOBAL)
public class HierarchicalTableDescriptor {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface GetSnapshotDefinitionSchemaUnionType {
        @JsOverlay
        static HierarchicalTableDescriptor.GetSnapshotDefinitionSchemaUnionType of(Object o) {
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
    public interface SetSnapshotDefinitionSchemaValueUnionType {
        @JsOverlay
        static HierarchicalTableDescriptor.SetSnapshotDefinitionSchemaValueUnionType of(Object o) {
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
        public interface GetSnapshotDefinitionSchemaUnionType {
            @JsOverlay
            static HierarchicalTableDescriptor.ToObjectReturnType.GetSnapshotDefinitionSchemaUnionType of(
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
            static HierarchicalTableDescriptor.ToObjectReturnType.RollupFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            double getLeafNodeType();

            @JsProperty
            JsArray<String> getOutputInputColumnPairsList();

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

        @JsOverlay
        static HierarchicalTableDescriptor.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<String> getExpandByColumnsList();

        @JsProperty
        HierarchicalTableDescriptor.ToObjectReturnType.RollupFieldType getRollup();

        @JsProperty
        String getRowDepthColumn();

        @JsProperty
        String getRowExpandedColumn();

        @JsProperty
        HierarchicalTableDescriptor.ToObjectReturnType.GetSnapshotDefinitionSchemaUnionType getSnapshotDefinitionSchema();

        @JsProperty
        Object getTree();

        @JsProperty
        void setExpandByColumnsList(JsArray<String> expandByColumnsList);

        @JsOverlay
        default void setExpandByColumnsList(String[] expandByColumnsList) {
            setExpandByColumnsList(Js.<JsArray<String>>uncheckedCast(expandByColumnsList));
        }

        @JsProperty
        void setRollup(HierarchicalTableDescriptor.ToObjectReturnType.RollupFieldType rollup);

        @JsProperty
        void setRowDepthColumn(String rowDepthColumn);

        @JsProperty
        void setRowExpandedColumn(String rowExpandedColumn);

        @JsProperty
        void setSnapshotDefinitionSchema(
                HierarchicalTableDescriptor.ToObjectReturnType.GetSnapshotDefinitionSchemaUnionType snapshotDefinitionSchema);

        @JsOverlay
        default void setSnapshotDefinitionSchema(String snapshotDefinitionSchema) {
            setSnapshotDefinitionSchema(
                    Js.<HierarchicalTableDescriptor.ToObjectReturnType.GetSnapshotDefinitionSchemaUnionType>uncheckedCast(
                            snapshotDefinitionSchema));
        }

        @JsOverlay
        default void setSnapshotDefinitionSchema(Uint8Array snapshotDefinitionSchema) {
            setSnapshotDefinitionSchema(
                    Js.<HierarchicalTableDescriptor.ToObjectReturnType.GetSnapshotDefinitionSchemaUnionType>uncheckedCast(
                            snapshotDefinitionSchema));
        }

        @JsProperty
        void setTree(Object tree);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface GetSnapshotDefinitionSchemaUnionType {
            @JsOverlay
            static HierarchicalTableDescriptor.ToObjectReturnType0.GetSnapshotDefinitionSchemaUnionType of(Object o) {
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
            static HierarchicalTableDescriptor.ToObjectReturnType0.RollupFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            double getLeafNodeType();

            @JsProperty
            JsArray<String> getOutputInputColumnPairsList();

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

        @JsOverlay
        static HierarchicalTableDescriptor.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<String> getExpandByColumnsList();

        @JsProperty
        HierarchicalTableDescriptor.ToObjectReturnType0.RollupFieldType getRollup();

        @JsProperty
        String getRowDepthColumn();

        @JsProperty
        String getRowExpandedColumn();

        @JsProperty
        HierarchicalTableDescriptor.ToObjectReturnType0.GetSnapshotDefinitionSchemaUnionType getSnapshotDefinitionSchema();

        @JsProperty
        Object getTree();

        @JsProperty
        void setExpandByColumnsList(JsArray<String> expandByColumnsList);

        @JsOverlay
        default void setExpandByColumnsList(String[] expandByColumnsList) {
            setExpandByColumnsList(Js.<JsArray<String>>uncheckedCast(expandByColumnsList));
        }

        @JsProperty
        void setRollup(HierarchicalTableDescriptor.ToObjectReturnType0.RollupFieldType rollup);

        @JsProperty
        void setRowDepthColumn(String rowDepthColumn);

        @JsProperty
        void setRowExpandedColumn(String rowExpandedColumn);

        @JsProperty
        void setSnapshotDefinitionSchema(
                HierarchicalTableDescriptor.ToObjectReturnType0.GetSnapshotDefinitionSchemaUnionType snapshotDefinitionSchema);

        @JsOverlay
        default void setSnapshotDefinitionSchema(String snapshotDefinitionSchema) {
            setSnapshotDefinitionSchema(
                    Js.<HierarchicalTableDescriptor.ToObjectReturnType0.GetSnapshotDefinitionSchemaUnionType>uncheckedCast(
                            snapshotDefinitionSchema));
        }

        @JsOverlay
        default void setSnapshotDefinitionSchema(Uint8Array snapshotDefinitionSchema) {
            setSnapshotDefinitionSchema(
                    Js.<HierarchicalTableDescriptor.ToObjectReturnType0.GetSnapshotDefinitionSchemaUnionType>uncheckedCast(
                            snapshotDefinitionSchema));
        }

        @JsProperty
        void setTree(Object tree);
    }

    public static native HierarchicalTableDescriptor deserializeBinary(Uint8Array bytes);

    public static native HierarchicalTableDescriptor deserializeBinaryFromReader(
            HierarchicalTableDescriptor message, Object reader);

    public static native void serializeBinaryToWriter(
            HierarchicalTableDescriptor message, Object writer);

    public static native HierarchicalTableDescriptor.ToObjectReturnType toObject(
            boolean includeInstance, HierarchicalTableDescriptor msg);

    public native String addExpandByColumns(String value, double index);

    public native String addExpandByColumns(String value);

    public native void clearExpandByColumnsList();

    public native void clearRollup();

    public native void clearTree();

    public native int getDetailsCase();

    public native JsArray<String> getExpandByColumnsList();

    public native RollupDescriptorDetails getRollup();

    public native String getRowDepthColumn();

    public native String getRowExpandedColumn();

    public native HierarchicalTableDescriptor.GetSnapshotDefinitionSchemaUnionType getSnapshotDefinitionSchema();

    public native String getSnapshotDefinitionSchema_asB64();

    public native Uint8Array getSnapshotDefinitionSchema_asU8();

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

    public native void setRowDepthColumn(String value);

    public native void setRowExpandedColumn(String value);

    public native void setSnapshotDefinitionSchema(
            HierarchicalTableDescriptor.SetSnapshotDefinitionSchemaValueUnionType value);

    @JsOverlay
    public final void setSnapshotDefinitionSchema(String value) {
        setSnapshotDefinitionSchema(
                Js.<HierarchicalTableDescriptor.SetSnapshotDefinitionSchemaValueUnionType>uncheckedCast(
                        value));
    }

    @JsOverlay
    public final void setSnapshotDefinitionSchema(Uint8Array value) {
        setSnapshotDefinitionSchema(
                Js.<HierarchicalTableDescriptor.SetSnapshotDefinitionSchemaValueUnionType>uncheckedCast(
                        value));
    }

    public native void setTree();

    public native void setTree(TreeDescriptorDetails value);

    public native HierarchicalTableDescriptor.ToObjectReturnType0 toObject();

    public native HierarchicalTableDescriptor.ToObjectReturnType0 toObject(boolean includeInstance);
}
