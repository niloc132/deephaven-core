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
        public interface ResultRollupTableIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static RollupRequest.ToObjectReturnType.ResultRollupTableIdFieldType.GetTicketUnionType of(
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
            static RollupRequest.ToObjectReturnType.ResultRollupTableIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            RollupRequest.ToObjectReturnType.ResultRollupTableIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    RollupRequest.ToObjectReturnType.ResultRollupTableIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<RollupRequest.ToObjectReturnType.ResultRollupTableIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<RollupRequest.ToObjectReturnType.ResultRollupTableIdFieldType.GetTicketUnionType>uncheckedCast(
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
        RollupRequest.ToObjectReturnType.ResultRollupTableIdFieldType getResultRollupTableId();

        @JsProperty
        Object getSourceTableId();

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
        void setResultRollupTableId(
                RollupRequest.ToObjectReturnType.ResultRollupTableIdFieldType resultRollupTableId);

        @JsProperty
        void setSourceTableId(Object sourceTableId);
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
        public interface ResultRollupTableIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static RollupRequest.ToObjectReturnType0.ResultRollupTableIdFieldType.GetTicketUnionType of(
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
            static RollupRequest.ToObjectReturnType0.ResultRollupTableIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            RollupRequest.ToObjectReturnType0.ResultRollupTableIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    RollupRequest.ToObjectReturnType0.ResultRollupTableIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<RollupRequest.ToObjectReturnType0.ResultRollupTableIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<RollupRequest.ToObjectReturnType0.ResultRollupTableIdFieldType.GetTicketUnionType>uncheckedCast(
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
        RollupRequest.ToObjectReturnType0.ResultRollupTableIdFieldType getResultRollupTableId();

        @JsProperty
        Object getSourceTableId();

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
        void setResultRollupTableId(
                RollupRequest.ToObjectReturnType0.ResultRollupTableIdFieldType resultRollupTableId);

        @JsProperty
        void setSourceTableId(Object sourceTableId);
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

    public native void clearResultRollupTableId();

    public native void clearSourceTableId();

    public native JsArray<Aggregate> getAggregationsList();

    public native JsArray<String> getGroupByColumnsList();

    public native boolean getIncludeConstituents();

    public native Ticket getResultRollupTableId();

    public native Ticket getSourceTableId();

    public native boolean hasResultRollupTableId();

    public native boolean hasSourceTableId();

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

    public native void setResultRollupTableId();

    public native void setResultRollupTableId(Ticket value);

    public native void setSourceTableId();

    public native void setSourceTableId(Ticket value);

    public native RollupRequest.ToObjectReturnType0 toObject();

    public native RollupRequest.ToObjectReturnType0 toObject(boolean includeInstance);
}
