package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.console_pb;

import elemental2.core.Uint8Array;
import io.deephaven.javascript.proto.dhinternal.arrow.flight.protocol.flight_pb.Ticket;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
    isNative = true,
    name = "dhinternal.io.deephaven.proto.console_pb.StartConsoleRequest",
    namespace = JsPackage.GLOBAL)
public class StartConsoleRequest {
  @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
  public interface ToObjectReturnType {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ResukltIdFieldType {
      @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
      public interface GetTicketUnionType {
        @JsOverlay
        static StartConsoleRequest.ToObjectReturnType.ResukltIdFieldType.GetTicketUnionType of(
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
      static StartConsoleRequest.ToObjectReturnType.ResukltIdFieldType create() {
        return Js.uncheckedCast(JsPropertyMap.of());
      }

      @JsProperty
      StartConsoleRequest.ToObjectReturnType.ResukltIdFieldType.GetTicketUnionType getTicket();

      @JsProperty
      void setTicket(
          StartConsoleRequest.ToObjectReturnType.ResukltIdFieldType.GetTicketUnionType ticket);

      @JsOverlay
      default void setTicket(String ticket) {
        setTicket(
            Js
                .<StartConsoleRequest.ToObjectReturnType.ResukltIdFieldType.GetTicketUnionType>
                    uncheckedCast(ticket));
      }

      @JsOverlay
      default void setTicket(Uint8Array ticket) {
        setTicket(
            Js
                .<StartConsoleRequest.ToObjectReturnType.ResukltIdFieldType.GetTicketUnionType>
                    uncheckedCast(ticket));
      }
    }

    @JsOverlay
    static StartConsoleRequest.ToObjectReturnType create() {
      return Js.uncheckedCast(JsPropertyMap.of());
    }

    @JsProperty
    StartConsoleRequest.ToObjectReturnType.ResukltIdFieldType getResukltId();

    @JsProperty
    String getSessionType();

    @JsProperty
    void setResukltId(StartConsoleRequest.ToObjectReturnType.ResukltIdFieldType resukltId);

    @JsProperty
    void setSessionType(String sessionType);
  }

  @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
  public interface ToObjectReturnType0 {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ResukltIdFieldType {
      @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
      public interface GetTicketUnionType {
        @JsOverlay
        static StartConsoleRequest.ToObjectReturnType0.ResukltIdFieldType.GetTicketUnionType of(
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
      static StartConsoleRequest.ToObjectReturnType0.ResukltIdFieldType create() {
        return Js.uncheckedCast(JsPropertyMap.of());
      }

      @JsProperty
      StartConsoleRequest.ToObjectReturnType0.ResukltIdFieldType.GetTicketUnionType getTicket();

      @JsProperty
      void setTicket(
          StartConsoleRequest.ToObjectReturnType0.ResukltIdFieldType.GetTicketUnionType ticket);

      @JsOverlay
      default void setTicket(String ticket) {
        setTicket(
            Js
                .<StartConsoleRequest.ToObjectReturnType0.ResukltIdFieldType.GetTicketUnionType>
                    uncheckedCast(ticket));
      }

      @JsOverlay
      default void setTicket(Uint8Array ticket) {
        setTicket(
            Js
                .<StartConsoleRequest.ToObjectReturnType0.ResukltIdFieldType.GetTicketUnionType>
                    uncheckedCast(ticket));
      }
    }

    @JsOverlay
    static StartConsoleRequest.ToObjectReturnType0 create() {
      return Js.uncheckedCast(JsPropertyMap.of());
    }

    @JsProperty
    StartConsoleRequest.ToObjectReturnType0.ResukltIdFieldType getResukltId();

    @JsProperty
    String getSessionType();

    @JsProperty
    void setResukltId(StartConsoleRequest.ToObjectReturnType0.ResukltIdFieldType resukltId);

    @JsProperty
    void setSessionType(String sessionType);
  }

  public static native StartConsoleRequest deserializeBinary(Uint8Array bytes);

  public static native StartConsoleRequest deserializeBinaryFromReader(
      StartConsoleRequest message, Object reader);

  public static native void serializeBinaryToWriter(StartConsoleRequest message, Object writer);

  public static native StartConsoleRequest.ToObjectReturnType toObject(
      boolean includeInstance, StartConsoleRequest msg);

  public native void clearResukltId();

  public native Ticket getResukltId();

  public native String getSessionType();

  public native boolean hasResukltId();

  public native Uint8Array serializeBinary();

  public native void setResukltId();

  public native void setResukltId(Ticket value);

  public native void setSessionType(String value);

  public native StartConsoleRequest.ToObjectReturnType0 toObject();

  public native StartConsoleRequest.ToObjectReturnType0 toObject(boolean includeInstance);
}
