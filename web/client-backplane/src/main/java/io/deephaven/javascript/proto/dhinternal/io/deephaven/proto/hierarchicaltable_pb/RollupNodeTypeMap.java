package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.hierarchicaltable_pb;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.RollupNodeTypeMap",
        namespace = JsPackage.GLOBAL)
public interface RollupNodeTypeMap {
    @JsOverlay
    static RollupNodeTypeMap create() {
        return Js.uncheckedCast(JsPropertyMap.of());
    }

    @JsProperty(name = "AGGREGATED")
    int getAGGREGATED();

    @JsProperty(name = "CONSTITUENT")
    int getCONSTITUENT();

    @JsProperty(name = "UNDEFINED")
    int getUNDEFINED();

    @JsProperty(name = "AGGREGATED")
    void setAGGREGATED(int AGGREGATED);

    @JsProperty(name = "CONSTITUENT")
    void setCONSTITUENT(int CONSTITUENT);

    @JsProperty(name = "UNDEFINED")
    void setUNDEFINED(int UNDEFINED);
}
