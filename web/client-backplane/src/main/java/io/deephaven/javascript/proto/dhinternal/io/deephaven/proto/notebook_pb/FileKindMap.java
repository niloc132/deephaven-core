package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.notebook_pb;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.notebook_pb.FileKindMap",
        namespace = JsPackage.GLOBAL)
public interface FileKindMap {
    @JsOverlay
    static FileKindMap create() {
        return Js.uncheckedCast(JsPropertyMap.of());
    }

    @JsProperty(name = "DIRECTORY")
    int getDIRECTORY();

    @JsProperty(name = "FILE")
    int getFILE();

    @JsProperty(name = "UNKNOWN")
    int getUNKNOWN();

    @JsProperty(name = "DIRECTORY")
    void setDIRECTORY(int DIRECTORY);

    @JsProperty(name = "FILE")
    void setFILE(int FILE);

    @JsProperty(name = "UNKNOWN")
    void setUNKNOWN(int UNKNOWN);
}
