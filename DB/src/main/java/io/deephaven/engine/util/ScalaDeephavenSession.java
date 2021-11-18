/*
 * Copyright (c) 2016-2021 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.engine.util;

import io.deephaven.engine.exceptions.CancellationException;
import io.deephaven.engine.updategraph.UpdateGraphProcessor;
import io.deephaven.engine.tables.select.QueryScope;
import io.deephaven.engine.util.scripts.ScriptPathLoader;
import io.deephaven.engine.util.scripts.ScriptPathLoaderState;
import io.deephaven.internal.log.LoggerFactory;
import io.deephaven.io.logger.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import scala.Option;
import scala.collection.JavaConverters;
import scala.reflect.internal.Names;
import scala.reflect.internal.util.Position;
import scala.runtime.AbstractFunction1;
import scala.runtime.BoxedUnit;
import scala.tools.nsc.GenericRunnerSettings;
import scala.tools.nsc.interpreter.IMain;
import scala.tools.nsc.interpreter.NamedParamClass;
import scala.tools.nsc.interpreter.ReplReporter;
import scala.tools.nsc.interpreter.Results;
import scala.tools.nsc.settings.MutableSettings;
import scala.util.Either;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Interactive Console Session using a Scala Interpreter
 */
public class ScalaDeephavenSession extends AbstractScriptSession implements ScriptSession {
    private static final Logger log = LoggerFactory.getLogger(ScalaDeephavenSession.class);

    public static String SCRIPT_TYPE = "Scala";

    private final IMain interpreter;
    private final ErrorHandler errorHandler;
    private Reporter reporter;

    private class ErrorHandler extends AbstractFunction1<String, BoxedUnit> {
        String message;

        @Override
        public BoxedUnit apply(String message) {
            log.error().append("Interpreter Error: ").append(message).endl();
            this.message = message;
            return BoxedUnit.UNIT;
        }
    }

    private static class Reporter extends ReplReporter {
        String lastError;
        String lastUntruncated;

        Reporter(IMain intp) {
            super(intp);
        }

        void resetLast() {
            lastError = lastUntruncated = null;
        }

        @Override
        public void printUntruncatedMessage(String msg) {
            lastUntruncated = msg;
            super.printUntruncatedMessage(msg);
        }

        @Override
        public void error(Position pos, String msg) {
            lastError = msg;
            super.error(pos, msg);
        }
    }

    public ScalaDeephavenSession(@SuppressWarnings("unused") boolean runInitScripts, boolean isDefaultScriptSession) {
        super(null, isDefaultScriptSession);

        errorHandler = new ErrorHandler();
        GenericRunnerSettings settings = new GenericRunnerSettings(errorHandler);
        ((MutableSettings.BooleanSetting) settings.usejavacp()).v_$eq(true);
        interpreter = new IMain(settings, new PrintWriter(System.out)) {
            @Override
            public ReplReporter reporter() {
                if (reporter == null) {
                    reporter = new Reporter(this);
                }
                return reporter;
            }

            @Override
            public Either<Results.Result, Request> compile(String line, boolean synthetic) {
                Either<Results.Result, Request> compileResult = super.compile(line, synthetic);
                System.out.println("Compile Result: " + compileResult);
                return compileResult;
            }
        };

        setVariable("log", log);

        // Our first valueOfTerm will try to evaluate the Java classes, but there is a scala problem with Generics and
        // inners.
        interpreter.beSilentDuring(() -> {
            interpreter.valueOfTerm("log");
            return null;
        });
    }

    @Override
    protected QueryScope newQueryScope() {
        return new QueryScope.SynchronizedScriptSessionImpl(this);
    }

    @NotNull
    @Override
    public Object getVariable(String name) throws QueryScope.MissingVariableException {
        Option<Object> value = interpreter.valueOfTerm(name);
        if (value.isEmpty())
            throw new QueryScope.MissingVariableException("No binding for: " + name);
        return value.get();
    }

    @Override
    public <T> T getVariable(String name, T defaultValue) {
        Option<Object> value = interpreter.valueOfTerm(name);
        // noinspection unchecked
        return value.isDefined() ? (T) value.get() : defaultValue;
    }

    @Override
    protected void evaluate(String command, @Nullable String scriptName) {
        errorHandler.message = null;
        reporter.resetLast();
        log.info().append("Evaluating command: ").append(command).endl();
        Results.Result result;
        try {
            result = UpdateGraphProcessor.DEFAULT.exclusiveLock()
                    .computeLockedInterruptibly(() -> interpreter.interpret(command));
        } catch (InterruptedException e) {
            throw new CancellationException(e.getMessage() != null ? e.getMessage() : "Query interrupted", e);
        }

        if (!(result instanceof Results.Success$)) {
            if (result instanceof Results.Error$) {
                if (reporter.lastError != null) {
                    throw new RuntimeException("Could not evaluate command: " + reporter.lastError);
                } else if (reporter.lastUntruncated != null) {
                    throw new RuntimeException("Could not evaluate command: " + reporter.lastUntruncated);
                } else {
                    throw new RuntimeException("Could not evaluate command, unknown error!");
                }
            } else if (result instanceof Results.Incomplete$) {
                throw new IllegalStateException("Incomplete line");
            } else {
                throw new IllegalStateException(
                        "Bad result type: " + result.getClass().getCanonicalName() + " (" + result + ")");
            }
        }
    }

    @Override
    public Map<String, Object> getVariables() {
        Collection<Names.TermName> termNames = JavaConverters.asJavaCollection(interpreter.definedTerms());
        Map<String, Object> variableMap = new HashMap<>();
        for (Names.TermName termName : termNames) {
            final String name = termName.toString();
            final Option<Object> value = interpreter.valueOfTerm(name);
            variableMap.put(name, value.get());
        }
        return Collections.unmodifiableMap(variableMap);
    }

    @Override
    public Set<String> getVariableNames() {
        return Collections.unmodifiableSet(JavaConverters.asJavaCollection(interpreter.definedTerms())
                .stream()
                .map(Names.TermName::toString)
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean hasVariableName(String name) {
        return interpreter.valueOfTerm(name).isDefined();
    }

    @Override
    public void setVariable(String name, @Nullable Object newValue) {
        final Object oldValue = getVariable(name, null);
        if (newValue == null) {
            interpreter.beQuietDuring(
                    () -> interpreter.bind(new NamedParamClass(name, Object.class.getCanonicalName(), null)));
        } else {
            final String type = newValue.getClass().getCanonicalName();
            interpreter.beQuietDuring(() -> interpreter.bind(new NamedParamClass(name, type, newValue)));
        }
        notifyVariableChange(name, oldValue, newValue);
    }

    @Override
    public String scriptType() {
        return SCRIPT_TYPE;
    }

    @Override
    public void onApplicationInitializationBegin(Supplier<ScriptPathLoader> pathLoader,
            ScriptPathLoaderState scriptLoaderState) {}

    @Override
    public void onApplicationInitializationEnd() {}

    @Override
    public void setScriptPathLoader(Supplier<ScriptPathLoader> scriptPathLoader, boolean caching) {}

    @Override
    public void clearScriptPathLoader() {}

    @Override
    public boolean setUseOriginalScriptLoaderState(boolean useOriginal) {
        return true;
    }
}
