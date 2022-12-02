package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.hierarchicaltable_pb;

import elemental2.core.JsArray;
import elemental2.core.Uint8Array;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.table_pb.Condition;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.table_pb.SortDescriptor;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.ticket_pb.Ticket;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.HierarchicalTableViewRequest",
        namespace = JsPackage.GLOBAL)
public class HierarchicalTableViewRequest {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface FiltersListFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface AndFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.AndFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getFiltersList();

                @JsProperty
                void setFiltersList(JsArray<Object> filtersList);

                @JsOverlay
                default void setFiltersList(Object[] filtersList) {
                    setFiltersList(Js.<JsArray<Object>>uncheckedCast(filtersList));
                }
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface CompareFieldType {
                @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
                public interface LhsFieldType {
                    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
                    public interface LiteralFieldType {
                        @JsOverlay
                        static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType create() {
                            return Js.uncheckedCast(JsPropertyMap.of());
                        }

                        @JsProperty
                        double getDoubleValue();

                        @JsProperty
                        String getLongValue();

                        @JsProperty
                        String getNanoTimeValue();

                        @JsProperty
                        String getStringValue();

                        @JsProperty
                        boolean isBoolValue();

                        @JsProperty
                        void setBoolValue(boolean boolValue);

                        @JsProperty
                        void setDoubleValue(double doubleValue);

                        @JsProperty
                        void setLongValue(String longValue);

                        @JsProperty
                        void setNanoTimeValue(String nanoTimeValue);

                        @JsProperty
                        void setStringValue(String stringValue);
                    }

                    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
                    public interface ReferenceFieldType {
                        @JsOverlay
                        static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType create() {
                            return Js.uncheckedCast(JsPropertyMap.of());
                        }

                        @JsProperty
                        String getColumnName();

                        @JsProperty
                        void setColumnName(String columnName);
                    }

                    @JsOverlay
                    static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType create() {
                        return Js.uncheckedCast(JsPropertyMap.of());
                    }

                    @JsProperty
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType getLiteral();

                    @JsProperty
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType getReference();

                    @JsProperty
                    void setLiteral(
                            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType literal);

                    @JsProperty
                    void setReference(
                            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType reference);
                }

                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType getLhs();

                @JsProperty
                double getOperation();

                @JsProperty
                Object getRhs();

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setLhs(
                        HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType lhs);

                @JsProperty
                void setOperation(double operation);

                @JsProperty
                void setRhs(Object rhs);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface ContainsFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.ContainsFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                double getMatchType();

                @JsProperty
                Object getReference();

                @JsProperty
                String getSearchString();

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setMatchType(double matchType);

                @JsProperty
                void setReference(Object reference);

                @JsProperty
                void setSearchString(String searchString);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface InvokeFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.InvokeFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getArgumentsList();

                @JsProperty
                String getMethod();

                @JsProperty
                Object getTarget();

                @JsProperty
                void setArgumentsList(JsArray<Object> argumentsList);

                @JsOverlay
                default void setArgumentsList(Object[] argumentsList) {
                    setArgumentsList(Js.<JsArray<Object>>uncheckedCast(argumentsList));
                }

                @JsProperty
                void setMethod(String method);

                @JsProperty
                void setTarget(Object target);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface IsNullFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.IsNullFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                Object getReference();

                @JsProperty
                void setReference(Object reference);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface MatchesFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.MatchesFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                double getMatchType();

                @JsProperty
                Object getReference();

                @JsProperty
                String getRegex();

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setMatchType(double matchType);

                @JsProperty
                void setReference(Object reference);

                @JsProperty
                void setRegex(String regex);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface NotFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.NotFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                Object getFilter();

                @JsProperty
                void setFilter(Object filter);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface OrFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.OrFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getFiltersList();

                @JsProperty
                void setFiltersList(JsArray<Object> filtersList);

                @JsOverlay
                default void setFiltersList(Object[] filtersList) {
                    setFiltersList(Js.<JsArray<Object>>uncheckedCast(filtersList));
                }
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface Pb_inFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.Pb_inFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getCandidatesList();

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                double getMatchType();

                @JsProperty
                Object getTarget();

                @JsProperty
                void setCandidatesList(JsArray<Object> candidatesList);

                @JsOverlay
                default void setCandidatesList(Object[] candidatesList) {
                    setCandidatesList(Js.<JsArray<Object>>uncheckedCast(candidatesList));
                }

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setMatchType(double matchType);

                @JsProperty
                void setTarget(Object target);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface SearchFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.SearchFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getOptionalReferencesList();

                @JsProperty
                String getSearchString();

                @JsProperty
                void setOptionalReferencesList(JsArray<Object> optionalReferencesList);

                @JsOverlay
                default void setOptionalReferencesList(Object[] optionalReferencesList) {
                    setOptionalReferencesList(Js.<JsArray<Object>>uncheckedCast(optionalReferencesList));
                }

                @JsProperty
                void setSearchString(String searchString);
            }

            @JsOverlay
            static HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.AndFieldType getAnd();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType getCompare();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.ContainsFieldType getContains();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.InvokeFieldType getInvoke();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.IsNullFieldType getIsNull();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.MatchesFieldType getMatches();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.NotFieldType getNot();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.OrFieldType getOr();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.Pb_inFieldType getPb_in();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.SearchFieldType getSearch();

            @JsProperty
            void setAnd(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.AndFieldType and);

            @JsProperty
            void setCompare(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.CompareFieldType compare);

            @JsProperty
            void setContains(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.ContainsFieldType contains);

            @JsProperty
            void setInvoke(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.InvokeFieldType invoke);

            @JsProperty
            void setIsNull(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.IsNullFieldType isNull);

            @JsProperty
            void setMatches(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.MatchesFieldType matches);

            @JsProperty
            void setNot(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.NotFieldType not);

            @JsProperty
            void setOr(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.OrFieldType or);

            @JsProperty
            void setPb_in(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.Pb_inFieldType pb_in);

            @JsProperty
            void setSearch(
                    HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType.SearchFieldType search);
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ResultViewIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType of(
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
            static HierarchicalTableViewRequest.ToObjectReturnType.ResultViewIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    HierarchicalTableViewRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<HierarchicalTableViewRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<HierarchicalTableViewRequest.ToObjectReturnType.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface SortsListFieldType {
            @JsOverlay
            static HierarchicalTableViewRequest.ToObjectReturnType.SortsListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            String getColumnName();

            @JsProperty
            double getDirection();

            @JsProperty
            boolean isIsAbsolute();

            @JsProperty
            void setColumnName(String columnName);

            @JsProperty
            void setDirection(double direction);

            @JsProperty
            void setIsAbsolute(boolean isAbsolute);
        }

        @JsOverlay
        static HierarchicalTableViewRequest.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        Object getExistingViewId();

        @JsProperty
        JsArray<HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType> getFiltersList();

        @JsProperty
        Object getKeyTable();

        @JsProperty
        String getKeyTableExpandDescendantsColumn();

        @JsProperty
        HierarchicalTableViewRequest.ToObjectReturnType.ResultViewIdFieldType getResultViewId();

        @JsProperty
        JsArray<HierarchicalTableViewRequest.ToObjectReturnType.SortsListFieldType> getSortsList();

        @JsProperty
        void setExistingViewId(Object existingViewId);

        @JsOverlay
        default void setFiltersList(
                HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType[] filtersList) {
            setFiltersList(
                    Js.<JsArray<HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType>>uncheckedCast(
                            filtersList));
        }

        @JsProperty
        void setFiltersList(
                JsArray<HierarchicalTableViewRequest.ToObjectReturnType.FiltersListFieldType> filtersList);

        @JsProperty
        void setKeyTable(Object keyTable);

        @JsProperty
        void setKeyTableExpandDescendantsColumn(String keyTableExpandDescendantsColumn);

        @JsProperty
        void setResultViewId(
                HierarchicalTableViewRequest.ToObjectReturnType.ResultViewIdFieldType resultViewId);

        @JsProperty
        void setSortsList(
                JsArray<HierarchicalTableViewRequest.ToObjectReturnType.SortsListFieldType> sortsList);

        @JsOverlay
        default void setSortsList(
                HierarchicalTableViewRequest.ToObjectReturnType.SortsListFieldType[] sortsList) {
            setSortsList(
                    Js.<JsArray<HierarchicalTableViewRequest.ToObjectReturnType.SortsListFieldType>>uncheckedCast(
                            sortsList));
        }
    }

    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType0 {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface FiltersListFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface AndFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.AndFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getFiltersList();

                @JsProperty
                void setFiltersList(JsArray<Object> filtersList);

                @JsOverlay
                default void setFiltersList(Object[] filtersList) {
                    setFiltersList(Js.<JsArray<Object>>uncheckedCast(filtersList));
                }
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface CompareFieldType {
                @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
                public interface LhsFieldType {
                    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
                    public interface LiteralFieldType {
                        @JsOverlay
                        static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType create() {
                            return Js.uncheckedCast(JsPropertyMap.of());
                        }

                        @JsProperty
                        double getDoubleValue();

                        @JsProperty
                        String getLongValue();

                        @JsProperty
                        String getNanoTimeValue();

                        @JsProperty
                        String getStringValue();

                        @JsProperty
                        boolean isBoolValue();

                        @JsProperty
                        void setBoolValue(boolean boolValue);

                        @JsProperty
                        void setDoubleValue(double doubleValue);

                        @JsProperty
                        void setLongValue(String longValue);

                        @JsProperty
                        void setNanoTimeValue(String nanoTimeValue);

                        @JsProperty
                        void setStringValue(String stringValue);
                    }

                    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
                    public interface ReferenceFieldType {
                        @JsOverlay
                        static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType create() {
                            return Js.uncheckedCast(JsPropertyMap.of());
                        }

                        @JsProperty
                        String getColumnName();

                        @JsProperty
                        void setColumnName(String columnName);
                    }

                    @JsOverlay
                    static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType create() {
                        return Js.uncheckedCast(JsPropertyMap.of());
                    }

                    @JsProperty
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType getLiteral();

                    @JsProperty
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType getReference();

                    @JsProperty
                    void setLiteral(
                            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType literal);

                    @JsProperty
                    void setReference(
                            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType reference);
                }

                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType getLhs();

                @JsProperty
                double getOperation();

                @JsProperty
                Object getRhs();

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setLhs(
                        HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType lhs);

                @JsProperty
                void setOperation(double operation);

                @JsProperty
                void setRhs(Object rhs);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface ContainsFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.ContainsFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                double getMatchType();

                @JsProperty
                Object getReference();

                @JsProperty
                String getSearchString();

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setMatchType(double matchType);

                @JsProperty
                void setReference(Object reference);

                @JsProperty
                void setSearchString(String searchString);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface InvokeFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.InvokeFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getArgumentsList();

                @JsProperty
                String getMethod();

                @JsProperty
                Object getTarget();

                @JsProperty
                void setArgumentsList(JsArray<Object> argumentsList);

                @JsOverlay
                default void setArgumentsList(Object[] argumentsList) {
                    setArgumentsList(Js.<JsArray<Object>>uncheckedCast(argumentsList));
                }

                @JsProperty
                void setMethod(String method);

                @JsProperty
                void setTarget(Object target);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface IsNullFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.IsNullFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                Object getReference();

                @JsProperty
                void setReference(Object reference);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface MatchesFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.MatchesFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                double getMatchType();

                @JsProperty
                Object getReference();

                @JsProperty
                String getRegex();

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setMatchType(double matchType);

                @JsProperty
                void setReference(Object reference);

                @JsProperty
                void setRegex(String regex);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface NotFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.NotFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                Object getFilter();

                @JsProperty
                void setFilter(Object filter);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface OrFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.OrFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getFiltersList();

                @JsProperty
                void setFiltersList(JsArray<Object> filtersList);

                @JsOverlay
                default void setFiltersList(Object[] filtersList) {
                    setFiltersList(Js.<JsArray<Object>>uncheckedCast(filtersList));
                }
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface Pb_inFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.Pb_inFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getCandidatesList();

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                double getMatchType();

                @JsProperty
                Object getTarget();

                @JsProperty
                void setCandidatesList(JsArray<Object> candidatesList);

                @JsOverlay
                default void setCandidatesList(Object[] candidatesList) {
                    setCandidatesList(Js.<JsArray<Object>>uncheckedCast(candidatesList));
                }

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setMatchType(double matchType);

                @JsProperty
                void setTarget(Object target);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface SearchFieldType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.SearchFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                JsArray<Object> getOptionalReferencesList();

                @JsProperty
                String getSearchString();

                @JsProperty
                void setOptionalReferencesList(JsArray<Object> optionalReferencesList);

                @JsOverlay
                default void setOptionalReferencesList(Object[] optionalReferencesList) {
                    setOptionalReferencesList(Js.<JsArray<Object>>uncheckedCast(optionalReferencesList));
                }

                @JsProperty
                void setSearchString(String searchString);
            }

            @JsOverlay
            static HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.AndFieldType getAnd();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType getCompare();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.ContainsFieldType getContains();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.InvokeFieldType getInvoke();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.IsNullFieldType getIsNull();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.MatchesFieldType getMatches();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.NotFieldType getNot();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.OrFieldType getOr();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.Pb_inFieldType getPb_in();

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.SearchFieldType getSearch();

            @JsProperty
            void setAnd(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.AndFieldType and);

            @JsProperty
            void setCompare(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.CompareFieldType compare);

            @JsProperty
            void setContains(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.ContainsFieldType contains);

            @JsProperty
            void setInvoke(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.InvokeFieldType invoke);

            @JsProperty
            void setIsNull(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.IsNullFieldType isNull);

            @JsProperty
            void setMatches(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.MatchesFieldType matches);

            @JsProperty
            void setNot(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.NotFieldType not);

            @JsProperty
            void setOr(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.OrFieldType or);

            @JsProperty
            void setPb_in(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.Pb_inFieldType pb_in);

            @JsProperty
            void setSearch(
                    HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType.SearchFieldType search);
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface ResultViewIdFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface GetTicketUnionType {
                @JsOverlay
                static HierarchicalTableViewRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType of(
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
            static HierarchicalTableViewRequest.ToObjectReturnType0.ResultViewIdFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableViewRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType getTicket();

            @JsProperty
            void setTicket(
                    HierarchicalTableViewRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType ticket);

            @JsOverlay
            default void setTicket(String ticket) {
                setTicket(
                        Js.<HierarchicalTableViewRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }

            @JsOverlay
            default void setTicket(Uint8Array ticket) {
                setTicket(
                        Js.<HierarchicalTableViewRequest.ToObjectReturnType0.ResultViewIdFieldType.GetTicketUnionType>uncheckedCast(
                                ticket));
            }
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface SortsListFieldType {
            @JsOverlay
            static HierarchicalTableViewRequest.ToObjectReturnType0.SortsListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            String getColumnName();

            @JsProperty
            double getDirection();

            @JsProperty
            boolean isIsAbsolute();

            @JsProperty
            void setColumnName(String columnName);

            @JsProperty
            void setDirection(double direction);

            @JsProperty
            void setIsAbsolute(boolean isAbsolute);
        }

        @JsOverlay
        static HierarchicalTableViewRequest.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        Object getExistingViewId();

        @JsProperty
        JsArray<HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType> getFiltersList();

        @JsProperty
        Object getKeyTable();

        @JsProperty
        String getKeyTableExpandDescendantsColumn();

        @JsProperty
        HierarchicalTableViewRequest.ToObjectReturnType0.ResultViewIdFieldType getResultViewId();

        @JsProperty
        JsArray<HierarchicalTableViewRequest.ToObjectReturnType0.SortsListFieldType> getSortsList();

        @JsProperty
        void setExistingViewId(Object existingViewId);

        @JsOverlay
        default void setFiltersList(
                HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType[] filtersList) {
            setFiltersList(
                    Js.<JsArray<HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType>>uncheckedCast(
                            filtersList));
        }

        @JsProperty
        void setFiltersList(
                JsArray<HierarchicalTableViewRequest.ToObjectReturnType0.FiltersListFieldType> filtersList);

        @JsProperty
        void setKeyTable(Object keyTable);

        @JsProperty
        void setKeyTableExpandDescendantsColumn(String keyTableExpandDescendantsColumn);

        @JsProperty
        void setResultViewId(
                HierarchicalTableViewRequest.ToObjectReturnType0.ResultViewIdFieldType resultViewId);

        @JsProperty
        void setSortsList(
                JsArray<HierarchicalTableViewRequest.ToObjectReturnType0.SortsListFieldType> sortsList);

        @JsOverlay
        default void setSortsList(
                HierarchicalTableViewRequest.ToObjectReturnType0.SortsListFieldType[] sortsList) {
            setSortsList(
                    Js.<JsArray<HierarchicalTableViewRequest.ToObjectReturnType0.SortsListFieldType>>uncheckedCast(
                            sortsList));
        }
    }

    public static native HierarchicalTableViewRequest deserializeBinary(Uint8Array bytes);

    public static native HierarchicalTableViewRequest deserializeBinaryFromReader(
            HierarchicalTableViewRequest message, Object reader);

    public static native void serializeBinaryToWriter(
            HierarchicalTableViewRequest message, Object writer);

    public static native HierarchicalTableViewRequest.ToObjectReturnType toObject(
            boolean includeInstance, HierarchicalTableViewRequest msg);

    public native Condition addFilters();

    public native Condition addFilters(Condition value, double index);

    public native Condition addFilters(Condition value);

    public native SortDescriptor addSorts();

    public native SortDescriptor addSorts(SortDescriptor value, double index);

    public native SortDescriptor addSorts(SortDescriptor value);

    public native void clearExistingViewId();

    public native void clearFiltersList();

    public native void clearKeyTable();

    public native void clearKeyTableExpandDescendantsColumn();

    public native void clearResultViewId();

    public native void clearSortsList();

    public native Ticket getExistingViewId();

    public native JsArray<Condition> getFiltersList();

    public native Ticket getKeyTable();

    public native String getKeyTableExpandDescendantsColumn();

    public native Ticket getResultViewId();

    public native JsArray<SortDescriptor> getSortsList();

    public native boolean hasExistingViewId();

    public native boolean hasKeyTable();

    public native boolean hasKeyTableExpandDescendantsColumn();

    public native boolean hasResultViewId();

    public native Uint8Array serializeBinary();

    public native void setExistingViewId();

    public native void setExistingViewId(Ticket value);

    @JsOverlay
    public final void setFiltersList(Condition[] value) {
        setFiltersList(Js.<JsArray<Condition>>uncheckedCast(value));
    }

    public native void setFiltersList(JsArray<Condition> value);

    public native void setKeyTable();

    public native void setKeyTable(Ticket value);

    public native void setKeyTableExpandDescendantsColumn(String value);

    public native void setResultViewId();

    public native void setResultViewId(Ticket value);

    public native void setSortsList(JsArray<SortDescriptor> value);

    @JsOverlay
    public final void setSortsList(SortDescriptor[] value) {
        setSortsList(Js.<JsArray<SortDescriptor>>uncheckedCast(value));
    }

    public native HierarchicalTableViewRequest.ToObjectReturnType0 toObject();

    public native HierarchicalTableViewRequest.ToObjectReturnType0 toObject(boolean includeInstance);
}
