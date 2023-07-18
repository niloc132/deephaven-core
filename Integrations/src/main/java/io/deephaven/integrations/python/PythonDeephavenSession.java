/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.integrations.python;

import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.exceptions.CancellationException;
import io.deephaven.engine.context.QueryScope;
import io.deephaven.engine.util.AbstractScriptSession;
import io.deephaven.engine.util.PythonEvaluator;
import io.deephaven.engine.util.PythonEvaluatorJpy;
import io.deephaven.engine.util.PythonScope;
import io.deephaven.engine.util.ScriptSession;
import io.deephaven.integrations.python.PythonDeephavenSession.PythonSnapshot;
import io.deephaven.internal.log.LoggerFactory;
import io.deephaven.io.logger.Logger;
import io.deephaven.plugin.type.ObjectTypeLookup;
import io.deephaven.plugin.type.ObjectTypeLookup.NoOp;
import io.deephaven.util.SafeCloseable;
import io.deephaven.util.annotations.ScriptApi;
import io.deephaven.util.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jpy.KeyError;
import org.jpy.PyDictWrapper;
import org.jpy.PyInputMode;
import org.jpy.PyLib.CallableKind;
import org.jpy.PyModule;
import org.jpy.PyObject;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A ScriptSession that uses a JPy cpython interpreter internally.
 * <p>
 * This is used for applications or the console; Python code running remotely uses WorkerPythonEnvironment for it's
 * supporting structures.
 */
public class PythonDeephavenSession extends AbstractScriptSession<PythonSnapshot> {
    private static final Logger log = LoggerFactory.getLogger(PythonDeephavenSession.class);

    public static String SCRIPT_TYPE = "Python";

    private final PythonEvaluator evaluator;
    private final PythonScope<PyObject> scope;
    private final PythonScriptSessionModule module;

    /**
     * Create a Python ScriptSession.
     *
     * @param objectTypeLookup the object type lookup
     * @param listener an optional listener that will be notified whenever the query scope changes
     * @param pythonEvaluator
     * @throws IOException if an IO error occurs running initialization scripts
     */
    public PythonDeephavenSession(
            final ObjectTypeLookup objectTypeLookup,
            @Nullable final Listener listener,
            final PythonEvaluatorJpy pythonEvaluator) throws IOException {
        super(objectTypeLookup, listener);

        evaluator = pythonEvaluator;
        scope = pythonEvaluator.getScope();
        // executionContext.getQueryLibrary().importClass(org.jpy.PyObject.class);
        // try (final SafeCloseable ignored = executionContext.open()) {
        module = (PythonScriptSessionModule) PyModule.importModule("deephaven.server.script_session")
                .createProxy(CallableKind.FUNCTION, PythonScriptSessionModule.class);
        // }

        publishInitial();
    }

    /**
     * Creates a Python "{@link ScriptSession}", for use where we should only be reading from the scope, such as an
     * IPython kernel session.
     */
    public PythonDeephavenSession(final PythonScope<?> scope) {
        super(NoOp.INSTANCE, null);

        this.scope = (PythonScope<PyObject>) scope;
        module = (PythonScriptSessionModule) PyModule.importModule("deephaven.server.script_session")
                .createProxy(CallableKind.FUNCTION, PythonScriptSessionModule.class);
        evaluator = null;

        publishInitial();
    }

    @Override
    public void initialize(ExecutionContext executionContext) {
        executionContext.getQueryLibrary().importClass(org.jpy.PyObject.class);
        // we initially needed it wrapping the session, but it may not serve a purpose
    }

    @Override
    @VisibleForTesting
    public QueryScope newQueryScope() {
        // depend on the GIL instead of local synchronization
        return new UnsynchronizedScriptSessionQueryScope(this);
    }

    @NotNull
    @Override
    public Object getVariable(String name) throws QueryScope.MissingVariableException {
        return scope
                .getValue(name)
                .orElseThrow(() -> new QueryScope.MissingVariableException("No variable for: " + name));
    }

    @Override
    public <T> T getVariable(String name, T defaultValue) {
        return scope
                .<T>getValueUnchecked(name)
                .orElse(defaultValue);
    }

    @SuppressWarnings("unused")
    @ScriptApi
    public void pushScope(PyObject pydict) {
        if (!pydict.isDict()) {
            throw new IllegalArgumentException("Expect a Python dict but got a" + pydict.repr());
        }
        scope.pushScope(pydict);
    }

    @SuppressWarnings("unused")
    @ScriptApi
    public void popScope() {
        scope.popScope();
    }

    @Override
    protected void evaluate(String command, String scriptName) {
        log.info().append("Evaluating command: " + command).endl();
        try {
            ExecutionContext.getContext().getUpdateGraph().exclusiveLock()
                    .doLockedInterruptibly(() -> evaluator.evalScript(command));
        } catch (InterruptedException e) {
            throw new CancellationException(e.getMessage() != null ? e.getMessage() : "Query interrupted", e);
        }
    }

    @Override
    public Map<String, Object> getVariables() {
        final Map<String, Object> outMap = new LinkedHashMap<>();
        scope.getEntriesMap().forEach((key, value) -> outMap.put(key, maybeUnwrap(value)));
        return outMap;
    }

    protected static class PythonSnapshot implements Snapshot, SafeCloseable {

        private final PyDictWrapper dict;

        public PythonSnapshot(PyDictWrapper dict) {
            this.dict = Objects.requireNonNull(dict);
        }

        @Override
        public void close() {
            dict.close();
        }
    }

    @Override
    protected PythonSnapshot emptySnapshot() {
        return new PythonSnapshot(PyObject.executeCode("dict()", PyInputMode.EXPRESSION).asDict());
    }

    @Override
    protected PythonSnapshot takeSnapshot() {
        return new PythonSnapshot(scope.mainGlobals().copy());
    }

    @Override
    protected Changes createDiff(PythonSnapshot from, PythonSnapshot to, RuntimeException e) {
        // TODO(deephaven-core#1775): multivariate jpy (unwrapped) return type into java
        // It would be great if we could push down the maybeUnwrap logic into create_change_list (it could handle the
        // unwrapping), but we are unable to tell jpy that we want to unwrap JType objects, but pass back python objects
        // as PyObject.
        try (PyObject changes = module.create_change_list(from.dict.unwrap(), to.dict.unwrap())) {
            final Changes diff = new Changes();
            diff.error = e;
            for (PyObject change : changes.asList()) {
                // unpack the tuple
                // (name, existing_value, new_value)
                final String name = change.call(String.class, "__getitem__", int.class, 0);
                final PyObject fromValue = change.call(PyObject.class, "__getitem__", int.class, 1);
                final PyObject toValue = change.call(PyObject.class, "__getitem__", int.class, 2);
                applyVariableChangeToDiff(diff, name, maybeUnwrap(fromValue), maybeUnwrap(toValue));
            }
            return diff;
        }
    }

    private Object maybeUnwrap(Object o) {
        if (o instanceof PyObject) {
            return maybeUnwrap((PyObject) o);
        }
        return o;
    }

    private Object maybeUnwrap(PyObject o) {
        if (o == null) {
            return null;
        }
        final Object javaObject = module.unwrap_to_java_type(o);
        if (javaObject != null) {
            return javaObject;
        }
        return o;
    }

    @Override
    public Set<String> getVariableNames() {
        return Collections.unmodifiableSet(scope.getKeys().collect(Collectors.toSet()));
    }

    @Override
    public boolean hasVariableName(String name) {
        return scope.containsKey(name);
    }

    @Override
    public synchronized void setVariable(String name, @Nullable Object newValue) {
        final PyDictWrapper globals = scope.mainGlobals();
        if (newValue == null) {
            try {
                globals.delItem(name);
            } catch (KeyError key) {
                // ignore
            }
        } else {
            if (!(newValue instanceof PyObject)) {
                newValue = PythonObjectWrapper.wrap(newValue);
            }
            globals.setItem(name, newValue);
        }

        // Observe changes from this "setVariable" (potentially capturing previous or concurrent external changes from
        // other threads)
        observeScopeChanges();
    }

    @Override
    public String scriptType() {
        return SCRIPT_TYPE;
    }

    // TODO core#41 move this logic into the python console instance or scope like this - can go further and move
    // isWidget too
    @Override
    public Object unwrapObject(Object object) {
        if (object instanceof PyObject) {
            final PyObject pyObject = (PyObject) object;
            final Object unwrapped = module.unwrap_to_java_type(pyObject);
            if (unwrapped != null) {
                return unwrapped;
            }
        }

        return object;
    }

    interface PythonScriptSessionModule extends Closeable {
        PyObject create_change_list(PyObject from, PyObject to);

        Object unwrap_to_java_type(PyObject object);

        void close();
    }
}
