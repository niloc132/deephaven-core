package io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.hierarchicaltable_pb;

import elemental2.core.JsArray;
import elemental2.core.Uint8Array;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.table_pb.Condition;
import io.deephaven.javascript.proto.dhinternal.io.deephaven.proto.table_pb.SortDescriptor;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven.proto.hierarchicaltable_pb.HierarchicalTableViewOperations",
        namespace = JsPackage.GLOBAL)
public class HierarchicalTableViewOperations {
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface ToObjectReturnType {
        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface FiltersListFieldType {
            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface AndFieldType {
                @JsOverlay
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.AndFieldType create() {
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
                        static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType create() {
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
                        static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType create() {
                            return Js.uncheckedCast(JsPropertyMap.of());
                        }

                        @JsProperty
                        String getColumnName();

                        @JsProperty
                        void setColumnName(String columnName);
                    }

                    @JsOverlay
                    static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType create() {
                        return Js.uncheckedCast(JsPropertyMap.of());
                    }

                    @JsProperty
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType getLiteral();

                    @JsProperty
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType getReference();

                    @JsProperty
                    void setLiteral(
                            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType literal);

                    @JsProperty
                    void setReference(
                            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType reference);
                }

                @JsOverlay
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType getLhs();

                @JsProperty
                double getOperation();

                @JsProperty
                Object getRhs();

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setLhs(
                        HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType.LhsFieldType lhs);

                @JsProperty
                void setOperation(double operation);

                @JsProperty
                void setRhs(Object rhs);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface ContainsFieldType {
                @JsOverlay
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.ContainsFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.InvokeFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.IsNullFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.MatchesFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.NotFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.OrFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.Pb_inFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.SearchFieldType create() {
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
            static HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.AndFieldType getAnd();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType getCompare();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.ContainsFieldType getContains();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.InvokeFieldType getInvoke();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.IsNullFieldType getIsNull();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.MatchesFieldType getMatches();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.NotFieldType getNot();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.OrFieldType getOr();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.Pb_inFieldType getPb_in();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.SearchFieldType getSearch();

            @JsProperty
            void setAnd(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.AndFieldType and);

            @JsProperty
            void setCompare(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.CompareFieldType compare);

            @JsProperty
            void setContains(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.ContainsFieldType contains);

            @JsProperty
            void setInvoke(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.InvokeFieldType invoke);

            @JsProperty
            void setIsNull(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.IsNullFieldType isNull);

            @JsProperty
            void setMatches(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.MatchesFieldType matches);

            @JsProperty
            void setNot(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.NotFieldType not);

            @JsProperty
            void setOr(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.OrFieldType or);

            @JsProperty
            void setPb_in(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.Pb_inFieldType pb_in);

            @JsProperty
            void setSearch(
                    HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType.SearchFieldType search);
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface SortsListFieldType {
            @JsOverlay
            static HierarchicalTableViewOperations.ToObjectReturnType.SortsListFieldType create() {
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
        static HierarchicalTableViewOperations.ToObjectReturnType create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType> getFiltersList();

        @JsProperty
        JsArray<HierarchicalTableViewOperations.ToObjectReturnType.SortsListFieldType> getSortsList();

        @JsOverlay
        default void setFiltersList(
                HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType[] filtersList) {
            setFiltersList(
                    Js.<JsArray<HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType>>uncheckedCast(
                            filtersList));
        }

        @JsProperty
        void setFiltersList(
                JsArray<HierarchicalTableViewOperations.ToObjectReturnType.FiltersListFieldType> filtersList);

        @JsProperty
        void setSortsList(
                JsArray<HierarchicalTableViewOperations.ToObjectReturnType.SortsListFieldType> sortsList);

        @JsOverlay
        default void setSortsList(
                HierarchicalTableViewOperations.ToObjectReturnType.SortsListFieldType[] sortsList) {
            setSortsList(
                    Js.<JsArray<HierarchicalTableViewOperations.ToObjectReturnType.SortsListFieldType>>uncheckedCast(
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
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.AndFieldType create() {
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
                        static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType create() {
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
                        static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType create() {
                            return Js.uncheckedCast(JsPropertyMap.of());
                        }

                        @JsProperty
                        String getColumnName();

                        @JsProperty
                        void setColumnName(String columnName);
                    }

                    @JsOverlay
                    static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType create() {
                        return Js.uncheckedCast(JsPropertyMap.of());
                    }

                    @JsProperty
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType getLiteral();

                    @JsProperty
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType getReference();

                    @JsProperty
                    void setLiteral(
                            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.LiteralFieldType literal);

                    @JsProperty
                    void setReference(
                            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType.ReferenceFieldType reference);
                }

                @JsOverlay
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType create() {
                    return Js.uncheckedCast(JsPropertyMap.of());
                }

                @JsProperty
                double getCaseSensitivity();

                @JsProperty
                HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType getLhs();

                @JsProperty
                double getOperation();

                @JsProperty
                Object getRhs();

                @JsProperty
                void setCaseSensitivity(double caseSensitivity);

                @JsProperty
                void setLhs(
                        HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType.LhsFieldType lhs);

                @JsProperty
                void setOperation(double operation);

                @JsProperty
                void setRhs(Object rhs);
            }

            @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
            public interface ContainsFieldType {
                @JsOverlay
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.ContainsFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.InvokeFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.IsNullFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.MatchesFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.NotFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.OrFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.Pb_inFieldType create() {
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
                static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.SearchFieldType create() {
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
            static HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType create() {
                return Js.uncheckedCast(JsPropertyMap.of());
            }

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.AndFieldType getAnd();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType getCompare();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.ContainsFieldType getContains();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.InvokeFieldType getInvoke();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.IsNullFieldType getIsNull();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.MatchesFieldType getMatches();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.NotFieldType getNot();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.OrFieldType getOr();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.Pb_inFieldType getPb_in();

            @JsProperty
            HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.SearchFieldType getSearch();

            @JsProperty
            void setAnd(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.AndFieldType and);

            @JsProperty
            void setCompare(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.CompareFieldType compare);

            @JsProperty
            void setContains(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.ContainsFieldType contains);

            @JsProperty
            void setInvoke(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.InvokeFieldType invoke);

            @JsProperty
            void setIsNull(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.IsNullFieldType isNull);

            @JsProperty
            void setMatches(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.MatchesFieldType matches);

            @JsProperty
            void setNot(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.NotFieldType not);

            @JsProperty
            void setOr(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.OrFieldType or);

            @JsProperty
            void setPb_in(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.Pb_inFieldType pb_in);

            @JsProperty
            void setSearch(
                    HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType.SearchFieldType search);
        }

        @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
        public interface SortsListFieldType {
            @JsOverlay
            static HierarchicalTableViewOperations.ToObjectReturnType0.SortsListFieldType create() {
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
        static HierarchicalTableViewOperations.ToObjectReturnType0 create() {
            return Js.uncheckedCast(JsPropertyMap.of());
        }

        @JsProperty
        JsArray<HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType> getFiltersList();

        @JsProperty
        JsArray<HierarchicalTableViewOperations.ToObjectReturnType0.SortsListFieldType> getSortsList();

        @JsOverlay
        default void setFiltersList(
                HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType[] filtersList) {
            setFiltersList(
                    Js.<JsArray<HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType>>uncheckedCast(
                            filtersList));
        }

        @JsProperty
        void setFiltersList(
                JsArray<HierarchicalTableViewOperations.ToObjectReturnType0.FiltersListFieldType> filtersList);

        @JsProperty
        void setSortsList(
                JsArray<HierarchicalTableViewOperations.ToObjectReturnType0.SortsListFieldType> sortsList);

        @JsOverlay
        default void setSortsList(
                HierarchicalTableViewOperations.ToObjectReturnType0.SortsListFieldType[] sortsList) {
            setSortsList(
                    Js.<JsArray<HierarchicalTableViewOperations.ToObjectReturnType0.SortsListFieldType>>uncheckedCast(
                            sortsList));
        }
    }

    public static native HierarchicalTableViewOperations deserializeBinary(Uint8Array bytes);

    public static native HierarchicalTableViewOperations deserializeBinaryFromReader(
            HierarchicalTableViewOperations message, Object reader);

    public static native void serializeBinaryToWriter(
            HierarchicalTableViewOperations message, Object writer);

    public static native HierarchicalTableViewOperations.ToObjectReturnType toObject(
            boolean includeInstance, HierarchicalTableViewOperations msg);

    public native Condition addFilters();

    public native Condition addFilters(Condition value, double index);

    public native Condition addFilters(Condition value);

    public native SortDescriptor addSorts();

    public native SortDescriptor addSorts(SortDescriptor value, double index);

    public native SortDescriptor addSorts(SortDescriptor value);

    public native void clearFiltersList();

    public native void clearSortsList();

    public native JsArray<Condition> getFiltersList();

    public native JsArray<SortDescriptor> getSortsList();

    public native Uint8Array serializeBinary();

    @JsOverlay
    public final void setFiltersList(Condition[] value) {
        setFiltersList(Js.<JsArray<Condition>>uncheckedCast(value));
    }

    public native void setFiltersList(JsArray<Condition> value);

    public native void setSortsList(JsArray<SortDescriptor> value);

    @JsOverlay
    public final void setSortsList(SortDescriptor[] value) {
        setSortsList(Js.<JsArray<SortDescriptor>>uncheckedCast(value));
    }

    public native HierarchicalTableViewOperations.ToObjectReturnType0 toObject();

    public native HierarchicalTableViewOperations.ToObjectReturnType0 toObject(
            boolean includeInstance);
}
