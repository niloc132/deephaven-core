/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.server.console.groovy;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import io.deephaven.engine.util.GroovyDeephavenSession;
import io.deephaven.engine.util.ScriptSession;
import io.deephaven.plugin.type.ObjectTypeLookup;
import io.deephaven.server.console.InitScriptsModule;

import javax.inject.Singleton;

@Module(includes = InitScriptsModule.ServiceLoader.class)
public class GroovyConsoleSessionModule {
    @Provides
    @Singleton
    @IntoMap
    @StringKey("groovy")
    ScriptSession bindScriptSession(final GroovyDeephavenSession groovySession) {
        return groovySession;
    }

    @Provides
    @Singleton
    GroovyDeephavenSession bindGroovySession(
            final ObjectTypeLookup lookup,
            final ScriptSession.Listener listener) {
        return new GroovyDeephavenSession(lookup, listener);
    }
}
