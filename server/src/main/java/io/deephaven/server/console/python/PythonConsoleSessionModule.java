/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.server.console.python;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import io.deephaven.configuration.Configuration;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.updategraph.UpdateGraph;
import io.deephaven.engine.updategraph.impl.PeriodicUpdateGraph;
import io.deephaven.engine.util.PythonEvaluatorJpy;
import io.deephaven.engine.util.ScriptSession;
import io.deephaven.integrations.python.PythonDeephavenSession;
import io.deephaven.plugin.type.ObjectTypeLookup;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;

@Module
public class PythonConsoleSessionModule {
    @Provides
    @IntoMap
    @StringKey("python")
    ScriptSession bindScriptSession(PythonDeephavenSession pythonSession) {
        return pythonSession;
    }

    @Singleton
    @Provides
    PythonDeephavenSession bindPythonSession(
            final ObjectTypeLookup lookup,
            final ScriptSession.Listener listener,
            final PythonEvaluatorJpy pythonEvaluator) {
        try {
            return new PythonDeephavenSession(lookup, listener, pythonEvaluator);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to run python startup scripts", e);
        }
    }
}
