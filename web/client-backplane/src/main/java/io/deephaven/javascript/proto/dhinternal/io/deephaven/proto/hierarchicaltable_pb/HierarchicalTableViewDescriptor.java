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

        @JsOverlay
        static HierarchicalTableViewDescriptor.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<String> getExpandByColumnsList();

        @JsProperty
        String getRollupKeyWidthColumn();

        @JsProperty
        double getRollupLeafNodeType();

        @JsProperty
        JsArray<String> getRollupOutputInputColumnPairsList();

        @JsProperty
        HierarchicalTableViewDescriptor.ToObjectReturnType.GetSchemaHeaderUnionType getSchemaHeader();

        @JsProperty
        String getTreeRowExpandableColumn();

        @JsProperty
        void setExpandByColumnsList(JsArray<String> expandByColumnsList);

        @JsOverlay
        default void setExpandByColumnsList(String[] expandByColumnsList) {
            setExpandByColumnsList(Js.<JsArray<String>>uncheckedCast(expandByColumnsList));
        }

        @JsProperty
        void setRollupKeyWidthColumn(String rollupKeyWidthColumn);

        @JsProperty
        void setRollupLeafNodeType(double rollupLeafNodeType);

        @JsProperty
        void setRollupOutputInputColumnPairsList(JsArray<String> rollupOutputInputColumnPairsList);

        @JsOverlay
        default void setRollupOutputInputColumnPairsList(String[] rollupOutputInputColumnPairsList) {
            setRollupOutputInputColumnPairsList(
                    Js.<JsArray<String>>uncheckedCast(rollupOutputInputColumnPairsList));
        }

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
        void setTreeRowExpandableColumn(String treeRowExpandableColumn);
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

        @JsOverlay
        static HierarchicalTableViewDescriptor.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<String> getExpandByColumnsList();

        @JsProperty
        String getRollupKeyWidthColumn();

        @JsProperty
        double getRollupLeafNodeType();

        @JsProperty
        JsArray<String> getRollupOutputInputColumnPairsList();

        @JsProperty
        HierarchicalTableViewDescriptor.ToObjectReturnType0.GetSchemaHeaderUnionType getSchemaHeader();

        @JsProperty
        String getTreeRowExpandableColumn();

        @JsProperty
        void setExpandByColumnsList(JsArray<String> expandByColumnsList);

        @JsOverlay
        default void setExpandByColumnsList(String[] expandByColumnsList) {
            setExpandByColumnsList(Js.<JsArray<String>>uncheckedCast(expandByColumnsList));
        }

        @JsProperty
        void setRollupKeyWidthColumn(String rollupKeyWidthColumn);

        @JsProperty
        void setRollupLeafNodeType(double rollupLeafNodeType);

        @JsProperty
        void setRollupOutputInputColumnPairsList(JsArray<String> rollupOutputInputColumnPairsList);

        @JsOverlay
        default void setRollupOutputInputColumnPairsList(String[] rollupOutputInputColumnPairsList) {
            setRollupOutputInputColumnPairsList(
                    Js.<JsArray<String>>uncheckedCast(rollupOutputInputColumnPairsList));
        }

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
        void setTreeRowExpandableColumn(String treeRowExpandableColumn);
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

    public native String addRollupOutputInputColumnPairs(String value, double index);

    public native String addRollupOutputInputColumnPairs(String value);

    public native void clearExpandByColumnsList();

    public native void clearRollupKeyWidthColumn();

    public native void clearRollupLeafNodeType();

    public native void clearRollupOutputInputColumnPairsList();

    public native void clearTreeRowExpandableColumn();

    public native JsArray<String> getExpandByColumnsList();

    public native String getRollupKeyWidthColumn();

    public native int getRollupLeafNodeType();

    public native JsArray<String> getRollupOutputInputColumnPairsList();

    public native HierarchicalTableViewDescriptor.GetSchemaHeaderUnionType getSchemaHeader();

    public native String getSchemaHeader_asB64();

    public native Uint8Array getSchemaHeader_asU8();

    public native String getTreeRowExpandableColumn();

    public native boolean hasRollupKeyWidthColumn();

    public native boolean hasRollupLeafNodeType();

    public native boolean hasTreeRowExpandableColumn();

    public native Uint8Array serializeBinary();

    public native void setExpandByColumnsList(JsArray<String> value);

    @JsOverlay
    public final void setExpandByColumnsList(String[] value) {
        setExpandByColumnsList(Js.<JsArray<String>>uncheckedCast(value));
    }

    public native void setRollupKeyWidthColumn(String value);

    public native void setRollupLeafNodeType(int value);

    public native void setRollupOutputInputColumnPairsList(JsArray<String> value);

    @JsOverlay
    public final void setRollupOutputInputColumnPairsList(String[] value) {
        setRollupOutputInputColumnPairsList(Js.<JsArray<String>>uncheckedCast(value));
    }

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

    public native void setTreeRowExpandableColumn(String value);

    public native HierarchicalTableViewDescriptor.ToObjectReturnType0 toObject();

    public native HierarchicalTableViewDescriptor.ToObjectReturnType0 toObject(
            boolean includeInstance);
}
