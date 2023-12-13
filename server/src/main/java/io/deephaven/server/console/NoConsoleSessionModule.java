/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.server.console;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import io.deephaven.engine.util.NoLanguageDeephavenSession;
import io.deephaven.engine.util.ScriptSession;

import javax.inject.Named;

@Module(includes = InitScriptsModule.ServiceLoader.class)
public class NoConsoleSessionModule {
    @Provides
    @IntoMap
    @StringKey("none")
    ScriptSession bindScriptSession(NoLanguageDeephavenSession noLanguageSession) {
        return noLanguageSession;
    }

    @Provides
    NoLanguageDeephavenSession bindNoLanguageSession() {
        return new NoLanguageDeephavenSession();
    }
}
