package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.hierarchicaltable_pb;

import elemental2.core.JsArray;
import elemental2.core.Uint8Array;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.table_pb.comboaggregaterequest.Aggregate;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.ticket_pb.Ticket;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.RollupRequest",
        namespace = JsPackage.GLOBAL)
public class RollupRequest {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface AggregationsListFieldType {
            @JsOverlay
            static RollupRequest.ToObjectReturnType.AggregationsListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            String getColumnName();

            @JsProperty
            JsArray<String> getMatchPairsList();

            @JsProperty
            double getPercentile();

            @JsProperty
            double getType();

            @JsProperty
            boolean isAvgMedian();

            @JsProperty
            void setAvgMedian(boolean avgMedian);

            @JsProperty
            void setColumnName(String columnName);

            @JsProperty
            void setMatchPairsList(JsArray<String> matchPairsList);

            @JsOverlay
            default void setMatchPairsList(String[] matchPairsList) {
                setMatchPairsList(Js.<JsArray<String>>uncheckedCast(matchPairsList));
            }

            @JsProperty
            void setPercentile(double percentile);

            @JsProperty
            void setType(double type);
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ResultViewIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static RollupRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType of(
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
            static RollupRequest.ToObjectReturnType.ResultViewIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            RollupRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    RollupRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<RollupRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<RollupRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static RollupRequest.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<RollupRequest.ToObjectReturnType.AggregationsListFieldType> getAggregationsList();

        @JsProperty
        JsArray<String> getGroupByColumnsList();

        @JsProperty
        RollupRequest.ToObjectReturnType.ResultViewIdFieldType getResultViewId();

        @JsProperty
        Object getSourceId();

        @JsProperty
        boolean isIncludeConstituents();

        @JsOverlay
        default void setAggregationsList(
                RollupRequest.ToObjectReturnType.AggregationsListFieldType[] aggregationsList) {
            setAggregationsList(
                    Js.<JsArray<RollupRequest.ToObjectReturnType.AggregationsListFieldType>>uncheckedCast(
                            aggregationsList));
        }

        @JsProperty
        void setAggregationsList(
                JsArray<RollupRequest.ToObjectReturnType.AggregationsListFieldType> aggregationsList);

        @JsProperty
        void setGroupByColumnsList(JsArray<String> groupByColumnsList);

        @JsOverlay
        default void setGroupByColumnsList(String[] groupByColumnsList) {
            setGroupByColumnsList(Js.<JsArray<String>>uncheckedCast(groupByColumnsList));
        }

        @JsProperty
        void setIncludeConstituents(boolean includeConstituents);

        @JsProperty
        void setResultViewId(RollupRequest.ToObjectReturnType.ResultViewIdFieldType resultViewId);

        @JsProperty
        void setSourceId(Object sourceId);
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface AggregationsListFieldType {
            @JsOverlay
            static RollupRequest.ToObjectReturnType0.AggregationsListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            String getColumnName();

            @JsProperty
            JsArray<String> getMatchPairsList();

            @JsProperty
            double getPercentile();

            @JsProperty
            double getType();

            @JsProperty
            boolean isAvgMedian();

            @JsProperty
            void setAvgMedian(boolean avgMedian);

            @JsProperty
            void setColumnName(String columnName);

            @JsProperty
            void setMatchPairsList(JsArray<String> matchPairsList);

            @JsOverlay
            default void setMatchPairsList(String[] matchPairsList) {
                setMatchPairsList(Js.<JsArray<String>>uncheckedCast(matchPairsList));
            }

            @JsProperty
            void setPercentile(double percentile);

            @JsProperty
            void setType(double type);
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ResultViewIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static RollupRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType of(
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
            static RollupRequest.ToObjectReturnType0.ResultViewIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            RollupRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    RollupRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<RollupRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<RollupRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsOverlay
        static RollupRequest.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<RollupRequest.ToObjectReturnType0.AggregationsListFieldType> getAggregationsList();

        @JsProperty
        JsArray<String> getGroupByColumnsList();

        @JsProperty
        RollupRequest.ToObjectReturnType0.ResultViewIdFieldType getResultViewId();

        @JsProperty
        Object getSourceId();

        @JsProperty
        boolean isIncludeConstituents();

        @JsOverlay
        default void setAggregationsList(
                RollupRequest.ToObjectReturnType0.AggregationsListFieldType[] aggregationsList) {
            setAggregationsList(
                    Js.<JsArray<RollupRequest.ToObjectReturnType0.AggregationsListFieldType>>uncheckedCast(
                            aggregationsList));
        }

        @JsProperty
        void setAggregationsList(
                JsArray<RollupRequest.ToObjectReturnType0.AggregationsListFieldType> aggregationsList);

        @JsProperty
        void setGroupByColumnsList(JsArray<String> groupByColumnsList);

        @JsOverlay
        default void setGroupByColumnsList(String[] groupByColumnsList) {
            setGroupByColumnsList(Js.<JsArray<String>>uncheckedCast(groupByColumnsList));
        }

        @JsProperty
        void setIncludeConstituents(boolean includeConstituents);

        @JsProperty
        void setResultViewId(RollupRequest.ToObjectReturnType0.ResultViewIdFieldType resultViewId);

        @JsProperty
        void setSourceId(Object sourceId);
    }

    public static native RollupRequest deserializeBinary(Uint8Array bytes);

    public static native RollupRequest deserializeBinaryFromReader(
            RollupRequest message, Object reader);

    public static native void serializeBinaryToWriter(RollupRequest message, Object writer);

    public static native RollupRequest.ToObjectReturnType toObject(
            boolean includeInstance, RollupRequest msg);

    public native Aggregate addAggregations();

    public native Aggregate addAggregations(Aggregate value, double index);

    public native Aggregate addAggregations(Aggregate value);

    public native String addGroupByColumns(String value, double index);

    public native String addGroupByColumns(String value);

    public native void clearAggregationsList();

    public native void clearGroupByColumnsList();

    public native void clearResultViewId();

    public native void clearSourceId();

    public native JsArray<Aggregate> getAggregationsList();

    public native JsArray<String> getGroupByColumnsList();

    public native boolean getIncludeConstituents();

    public native Ticket getResultViewId();

    public native Ticket getSourceId();

    public native boolean hasResultViewId();

    public native boolean hasSourceId();

    public native Uint8Array serializeBinary();

    @JsOverlay
    public final void setAggregationsList(Aggregate[] value) {
        setAggregationsList(Js.<JsArray<Aggregate>>uncheckedCast(value));
    }

    public native void setAggregationsList(JsArray<Aggregate> value);

    public native void setGroupByColumnsList(JsArray<String> value);

    @JsOverlay
    public final void setGroupByColumnsList(String[] value) {
        setGroupByColumnsList(Js.<JsArray<String>>uncheckedCast(value));
    }

    public native void setIncludeConstituents(boolean value);

    public native void setResultViewId();

    public native void setResultViewId(Ticket value);

    public native void setSourceId();

    public native void setSourceId(Ticket value);

    public native RollupRequest.ToObjectReturnType0 toObject();

    public native RollupRequest.ToObjectReturnType0 toObject(boolean includeInstance);
}
