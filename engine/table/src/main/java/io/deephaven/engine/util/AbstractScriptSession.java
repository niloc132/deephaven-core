/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.util;

import io.deephaven.UncheckedDeephavenException;
import io.deephaven.api.util.NameValidator;
import io.deephaven.base.FileUtils;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.liveness.LivenessScope;
import io.deephaven.engine.liveness.LivenessScopeStack;
import io.deephaven.engine.table.PartitionedTable;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.TableDefinition;
import io.deephaven.engine.context.QueryScope;
import io.deephaven.engine.context.QueryScopeParam;
import io.deephaven.engine.table.hierarchical.HierarchicalTable;
import io.deephaven.plugin.type.ObjectType;
import io.deephaven.plugin.type.ObjectTypeLookup;
import io.deephaven.util.SafeCloseable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static io.deephaven.engine.table.Table.NON_DISPLAY_TABLE;

/**
 * This class exists to make all script sessions to be liveness artifacts, and provide a default implementation for
 * evaluateScript which handles liveness and diffs in a consistent way.
 */
public abstract class AbstractScriptSession<S extends AbstractScriptSession.Snapshot> extends LivenessScope
        implements ScriptSession, VariableProvider {

    private final ObjectTypeLookup objectTypeLookup;
    private final Listener changeListener;

    private S lastSnapshot;

    protected AbstractScriptSession(
            ObjectTypeLookup objectTypeLookup,
            @Nullable Listener changeListener) {
        this.objectTypeLookup = objectTypeLookup;
        this.changeListener = changeListener;
    }

    protected synchronized void publishInitial() {
        lastSnapshot = emptySnapshot();
        observeScopeChanges();
    }

    @Override
    public synchronized void observeScopeChanges() {
        final S beforeSnapshot = lastSnapshot;
        lastSnapshot = takeSnapshot();
        try (beforeSnapshot) {
            applyDiff(beforeSnapshot, lastSnapshot);
        }
    }

    protected interface Snapshot extends SafeCloseable {
    }

    protected abstract S emptySnapshot();

    protected abstract S takeSnapshot();

    protected abstract Changes createDiff(S from, S to, RuntimeException e);

    private void applyDiff(S from, S to) {
        if (changeListener != null) {
            final Changes diff = createDiff(from, to, null);
            changeListener.onScopeChanges(this, diff);
        }
    }

    @Override
    public synchronized final Changes evaluateScript(final String script, @Nullable final String scriptName) {
        // Observe scope changes and propagate to the listener before running the script, in case it is long-running
        observeScopeChanges();

        RuntimeException evaluateErr = null;
        final Changes diff;
        // Retain any objects which are created in the executed code, we'll release them when the script session
        // closes. Also modify the exec context to provide the current script session's query scope to any operations
        // started within this invocation.
        try (final S initialSnapshot = takeSnapshot();
                final SafeCloseable ignored = LivenessScopeStack.open(this, false);
                final SafeCloseable ignored2 = ExecutionContext.getContext().withQueryScope(newQueryScope()).open()) {

            try {
                // actually evaluate the script
                evaluate(script, scriptName);
            } catch (final RuntimeException err) {
                evaluateErr = err;
            }

            // Observe changes during this evaluation (potentially capturing external changes from other threads)
            observeScopeChanges();
            // Use the "last" snapshot created as a side effect of observeScopeChanges() as our "to"
            diff = createDiff(initialSnapshot, lastSnapshot, evaluateErr);
        }

        return diff;
    }

    protected void applyVariableChangeToDiff(final Changes diff, String name,
            @Nullable Object fromValue, @Nullable Object toValue) {
        if (fromValue == toValue) {
            return;
        }
        final String fromTypeName = getTypeNameIfDisplayable(fromValue).orElse(null);
        if (fromTypeName == null) {
            fromValue = null;
        }
        final String toTypeName = getTypeNameIfDisplayable(toValue).orElse(null);
        if (toTypeName == null) {
            toValue = null;
        }
        if (fromValue == toValue) {
            return;
        }
        if (fromValue == null) {
            diff.created.put(name, toTypeName);
            return;
        }
        if (toValue == null) {
            diff.removed.put(name, fromTypeName);
            return;
        }
        if (!fromTypeName.equals(toTypeName)) {
            diff.created.put(name, toTypeName);
            diff.removed.put(name, fromTypeName);
            return;
        }
        diff.updated.put(name, toTypeName);
    }

    private Optional<String> getTypeNameIfDisplayable(Object object) {
        if (object == null) {
            return Optional.empty();
        }
        // Should this be consolidated down into TypeLookup and brought into engine?
        if (object instanceof Table) {
            final Table table = (Table) object;
            if (table.hasAttribute(NON_DISPLAY_TABLE)) {
                return Optional.empty();
            }
            return Optional.of("Table");
        }
        if (object instanceof HierarchicalTable) {
            return Optional.of("HierarchicalTable");
        }
        if (object instanceof PartitionedTable) {
            return Optional.of("PartitionedTable");
        }
        return objectTypeLookup.findObjectType(object).map(ObjectType::name);
    }

    @Override
    public Changes evaluateScript(Path scriptPath) {
        try {
            final String script = FileUtils.readTextFile(scriptPath.toFile());
            return evaluateScript(script, scriptPath.toString());
        } catch (IOException err) {
            throw new UncheckedDeephavenException(
                    String.format("could not read script file %s: ", scriptPath.toString()), err);
        }
    }

    /**
     * Evaluates command in the context of the current ScriptSession.
     *
     * @param command the command to evaluate
     * @param scriptName an optional script name, which may be ignored by the implementation, or used improve error
     *        messages or for other internal purposes
     */
    protected abstract void evaluate(String command, @Nullable String scriptName);

    /**
     * @return a query scope for this session; only invoked during construction
     */
    public abstract QueryScope newQueryScope();

    @Override
    public Class<?> getVariableType(final String var) {
        final Object result = getVariable(var, null);
        if (result == null) {
            return null;
        } else if (result instanceof Table) {
            return Table.class;
        } else {
            return result.getClass();
        }
    }


    @Override
    public TableDefinition getTableDefinition(final String var) {
        Object o = getVariable(var, null);
        return o instanceof Table ? ((Table) o).getDefinition() : null;
    }

    @Override
    public VariableProvider getVariableProvider() {
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ScriptSession-based QueryScope implementation, with no remote scope or object reflection support
    // -----------------------------------------------------------------------------------------------------------------

    public abstract static class ScriptSessionQueryScope extends QueryScope {
        final ScriptSession scriptSession;

        public ScriptSessionQueryScope(ScriptSession scriptSession) {
            this.scriptSession = scriptSession;
        }

        @Override
        public void putObjectFields(Object object) {
            throw new UnsupportedOperationException();
        }

        public ScriptSession scriptSession() {
            return scriptSession;
        }
    }

    public static class UnsynchronizedScriptSessionQueryScope extends ScriptSessionQueryScope {
        public UnsynchronizedScriptSessionQueryScope(@NotNull final ScriptSession scriptSession) {
            super(scriptSession);
        }

        @Override
        public Set<String> getParamNames() {
            final Set<String> result = new LinkedHashSet<>();
            for (final String name : scriptSession.getVariableNames()) {
                if (NameValidator.isValidQueryParameterName(name)) {
                    result.add(name);
                }
            }
            return result;
        }

        @Override
        public boolean hasParamName(String name) {
            return NameValidator.isValidQueryParameterName(name) && scriptSession.hasVariableName(name);
        }

        @Override
        protected <T> QueryScopeParam<T> createParam(final String name)
                throws QueryScope.MissingVariableException {
            if (!NameValidator.isValidQueryParameterName(name)) {
                throw new QueryScope.MissingVariableException("Name " + name + " is invalid");
            }
            // noinspection unchecked
            return new QueryScopeParam<>(name, (T) scriptSession.getVariable(name));
        }

        @Override
        public <T> T readParamValue(final String name) throws QueryScope.MissingVariableException {
            if (!NameValidator.isValidQueryParameterName(name)) {
                throw new QueryScope.MissingVariableException("Name " + name + " is invalid");
            }
            // noinspection unchecked
            return (T) scriptSession.getVariable(name);
        }

        @Override
        public <T> T readParamValue(final String name, final T defaultValue) {
            if (!NameValidator.isValidQueryParameterName(name)) {
                return defaultValue;
            }
            return scriptSession.getVariable(name, defaultValue);
        }

        @Override
        public <T> void putParam(final String name, final T value) {
            scriptSession.setVariable(NameValidator.validateQueryParameterName(name), value);
        }
    }

    public static class SynchronizedScriptSessionQueryScope extends UnsynchronizedScriptSessionQueryScope {
        public SynchronizedScriptSessionQueryScope(@NotNull final ScriptSession scriptSession) {
            super(scriptSession);
        }

        @Override
        public synchronized Set<String> getParamNames() {
            return super.getParamNames();
        }

        @Override
        public synchronized boolean hasParamName(String name) {
            return super.hasParamName(name);
        }

        @Override
        protected synchronized <T> QueryScopeParam<T> createParam(final String name)
                throws QueryScope.MissingVariableException {
            return super.createParam(name);
        }

        @Override
        public synchronized <T> T readParamValue(final String name) throws QueryScope.MissingVariableException {
            return super.readParamValue(name);
        }

        @Override
        public synchronized <T> T readParamValue(final String name, final T defaultValue) {
            return super.readParamValue(name, defaultValue);
        }

        @Override
        public synchronized <T> void putParam(final String name, final T value) {
            super.putParam(name, value);
        }
    }
}
