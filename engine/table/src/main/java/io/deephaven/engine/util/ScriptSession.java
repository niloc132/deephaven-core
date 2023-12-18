/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.util;

import io.deephaven.configuration.Configuration;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.context.QueryScope;
import io.deephaven.engine.liveness.LivenessNode;
import io.deephaven.engine.liveness.ReleasableLivenessManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Interface for interactive console script sessions. This class is insufficient by itself to offer details about
 * changes to the scope to an application - be sure when using it to call {@link #observeScopeChanges()} when anything
 * might have changed (including over time to catch other threads that might not directly call it. Initialization
 * scripts and applications will also not be started by using this, but must be passed to
 * {@link #evaluateScript(String, String)} or one of its overloads.
 */
public interface ScriptSession extends ReleasableLivenessManager, LivenessNode {

    /**
     * Retrieve a variable from the script session's bindings.
     * <p/>
     * Please use {@link ScriptSession#getVariable(String, Object)} if you expect the variable may not exist.
     *
     * @param name the variable to retrieve
     * @return the variable
     * @throws QueryScope.MissingVariableException if the variable does not exist
     */
    @NotNull
    Object getVariable(String name) throws QueryScope.MissingVariableException;

    /**
     * Retrieve a variable from the script session's bindings. If the variable is not present, return defaultValue.
     *
     * If the variable is present, but is not of type (T), a ClassCastException may result.
     *
     * @param name the variable to retrieve
     * @param defaultValue the value to use when no value is present in the session's scope
     * @param <T> the type of the variable
     * @return the value of the variable, or defaultValue if not present
     */
    <T> T getVariable(String name, T defaultValue);

    /**
     * A {@link VariableProvider} instance, for services like autocomplete which may want a limited "just the variables"
     * view of our session state.
     *
     * @return a VariableProvider instance backed by the global/binding context of this script session.
     */
    VariableProvider getVariableProvider();

    /**
     * Obtain an {@link ExecutionContext} instance for the current script session. This is the execution context that is
     * used when executing scripts.
     *
     * Usages: ScopeTicketResolver uses the update graph as a lock on the script session to read variables from the
     * script session consistent. This is probably not the correct lock for this.
     *
     * ApplicationInjector opens the execution context for the duration of a script to be invoked. This might be
     * important for script-based applications (so as to be tied to the script session), except that
     * ApplicationFactory.visit(ScriptApplication) will call ScriptSession.evaluateScript, which specifies its own exec
     * context (though probably the same exec context). Provided ScriptSession keeps an internal ExecContext, this outer
     * wrap is probably not necessary.
     */
//    default ExecutionContext getExecutionContext() {
//        throw new UnsupportedOperationException("getExecContext()");
//    }

    void initialize(ExecutionContext executionContext);

    QueryScope newQueryScope();

    class Changes {
        public RuntimeException error = null;

        // TODO(deephaven-core#1781): Close gaps between proto "CustomType" fields

        public Map<String, String> created = new LinkedHashMap<>();
        public Map<String, String> updated = new LinkedHashMap<>();
        public Map<String, String> removed = new LinkedHashMap<>();

        public boolean isEmpty() {
            return error == null && created.isEmpty() && updated.isEmpty() && removed.isEmpty();
        }

        public void throwIfError() {
            if (error != null) {
                throw error;
            }
        }
    }

    interface Listener {
        void onScopeChanges(ScriptSession scriptSession, Changes changes);
    }

    /**
     * Observe (and report via {@link Listener#onScopeChanges(ScriptSession, Changes) onScopeChanges}) any changes to
     * this ScriptSession's {@link QueryScope} that may have been made externally, rather than during
     * {@link #evaluateScript script evaluation}.
     * 
     * @apiNote This method should be regarded as an unstable API
     */
    void observeScopeChanges();

    /**
     * Evaluates the script and manages liveness of objects that are exported to the user. This method should be called
     * from the serial executor as it manipulates static state.
     *
     * @param script the code to execute
     * @return the changes made to the exportable objects
     */
    default Changes evaluateScript(String script) {
        return evaluateScript(script, null);
    }

    /**
     * Evaluates the script and manages liveness of objects that are exported to the user. This method should be called
     * from the serial executor as it manipulates static state.
     *
     * @param script the code to execute
     * @param scriptName an optional script name, which may be ignored by the implementation, or used improve error
     *        messages or for other internal purposes
     * @return the changes made to the exportable objects
     */
    Changes evaluateScript(String script, @Nullable String scriptName);

    /**
     * Evaluates the script and manages liveness of objects that are exported to the user. This method should be called
     * from the serial executor as it manipulates static state.
     *
     * @param scriptPath the path to the script to execute
     * @return the changes made to the exportable objects
     */
    Changes evaluateScript(Path scriptPath);

    /**
     * Retrieves all of the variables present in the session's scope (e.g., Groovy binding, Python globals()).
     *
     * @return an unmodifiable map with variable names as the keys, and the Objects as the result
     */
    Map<String, Object> getVariables();

    /**
     * Retrieves all of the variable names present in the session's scope
     *
     * @return an unmodifiable set of variable names
     */
    Set<String> getVariableNames();

    /**
     * Check if the scope has the given variable name
     *
     * @param name the variable name
     * @return True iff the scope has the given variable name
     */
    boolean hasVariableName(String name);

    /**
     * Inserts a value into the script's scope.
     *
     * @param name the variable name to set
     * @param value the new value of the variable
     */
    void setVariable(String name, @Nullable Object value);

    /**
     * @return a textual description of this script session's language for use in messages.
     */
    String scriptType();

    /**
     * If this script session can throw unserializable exceptions, this method is responsible for turning those
     * exceptions into something suitable for sending back to a client.
     *
     * @param e the exception to (possibly) sanitize
     * @return the sanitized exception
     */
    default Throwable sanitizeThrowable(Throwable e) {
        return e;
    }

    /**
     * Asks the session to remove any wrapping that exists on scoped objects so that clients can fetch them. Defaults to
     * returning the object itself.
     *
     * @param object the scoped object
     * @return an obj which can be consumed by a client
     */
    default Object unwrapObject(Object object) {
        return object;
    }

    /**
     * Script file to loadable on startup. Provide through {@link RunScripts} factory methods.
     */
    interface InitScript {
        /**
         * Path to the script to run, will be evaluated first as a file system path, then as a classpath element.
         */
        String scriptPath();

        /**
         * The language that this script was written in, so that it can be filtered from a list (e.g. from a service
         * loader) to only those that can run a specific ScriptSession instance.
         */
        String scriptLanguage();

        /**
         * Defines the order in which scripts will be loaded - lower numbers are loaded first. Values 0-100 are reserved
         * for deephaven-core scripts.
         */
        int priority();
    }


    class RunScripts {
        public static RunScripts of(String language, Iterable<ScriptSession.InitScript> initScripts) {
            List<InitScript> paths = StreamSupport.stream(initScripts.spliterator(), false)
                    .filter(script -> script.scriptLanguage().equals(language))
                    .sorted(Comparator.comparingInt(InitScript::priority))
                    .collect(Collectors.toList());
            return new RunScripts(paths);
        }

        public static RunScripts none() {
            return new RunScripts(Collections.emptyList());
        }

        public static RunScripts serviceLoader(String language) {
            return of(language, ServiceLoader.load(ScriptSession.InitScript.class));
        }

        @Deprecated
        public static RunScripts oldConfiguration(String scriptType, String configPropertyName) {
            List<InitScript> list = new ArrayList<>();
            String[] paths = Configuration.getInstance().getProperty(configPropertyName).split(",");
            for (int i = 0; i < paths.length; i++) {
                String path = paths[i];
                final int priority = i;
                InitScript initScript = new InitScript() {
                    @Override
                    public String scriptPath() {
                        return path;
                    }

                    @Override
                    public String scriptLanguage() {
                        return scriptType;
                    }

                    @Override
                    public int priority() {
                        return priority;
                    }
                };
                list.add(initScript);
            }
            return new RunScripts(list);
        }

        private final List<InitScript> scripts;

        public RunScripts(List<InitScript> scripts) {
            this.scripts = List.copyOf(scripts);
        }

        public List<InitScript> scripts() {
            return scripts;
        }

        public List<String> paths() {
            return scripts.stream().map(InitScript::scriptPath).collect(Collectors.toList());
        }
    }
}
