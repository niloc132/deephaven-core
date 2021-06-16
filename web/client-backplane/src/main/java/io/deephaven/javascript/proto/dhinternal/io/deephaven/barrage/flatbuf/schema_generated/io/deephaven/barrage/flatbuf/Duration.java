package io.deephaven.javascript.proto.dhinternal.io.deephaven.barrage.flatbuf.schema_generated.io.deephaven.barrage.flatbuf;

import io.deephaven.javascript.proto.dhinternal.flatbuffers.Builder;
import io.deephaven.javascript.proto.dhinternal.flatbuffers.ByteBuffer;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(
    isNative = true,
    name =
        "dhinternal.io.deephaven.barrage.flatbuf.Schema_generated.io.deephaven.barrage.flatbuf.Duration",
    namespace = JsPackage.GLOBAL)
public class Duration {
  public static native void addUnit(Builder builder, int unit);

  public static native double createDuration(Builder builder, int unit);

  public static native double endDuration(Builder builder);

  public static native Duration getRootAsDuration(ByteBuffer bb, Duration obj);

  public static native Duration getRootAsDuration(ByteBuffer bb);

  public static native Duration getSizePrefixedRootAsDuration(ByteBuffer bb, Duration obj);

  public static native Duration getSizePrefixedRootAsDuration(ByteBuffer bb);

  public static native void startDuration(Builder builder);

  public ByteBuffer bb;
  public double bb_pos;

  public native Duration __init(double i, ByteBuffer bb);

  public native int unit();
}
