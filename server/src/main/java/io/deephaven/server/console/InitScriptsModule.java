/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.server.console;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.deephaven.engine.table.impl.util.PerformanceQueries;
import io.deephaven.engine.util.GroovyDeephavenSession.Base;
import io.deephaven.engine.util.GroovyDeephavenSession.CountMetrics;
import io.deephaven.engine.util.ScriptSession;
import io.deephaven.engine.util.ScriptSession.InitScript;
import io.deephaven.engine.util.ScriptSession.RunScripts;

import java.util.Set;

public class InitScriptsModule {

    @Deprecated
    // Do we have a reason for this to exist? Marked deprecated, but I think we should remove it outright,
    // since it can't be installed in any project which already uses the GroovyConsoleSessionModule
    @Module
    public interface Explicit {
        @Binds
        @IntoSet
        InitScript bindsDbScripts(Base impl);

        @Binds
        @IntoSet
        InitScript bindsCountMetricsScripts(CountMetrics impl);

        @Binds
        @IntoSet
        InitScript bindsPerformanceQueriesScripts(PerformanceQueries.InitScript impl);

        @Provides
        static RunScripts providesRunScriptLogic(ScriptSession scriptSession, Set<InitScript> scripts) {
            return RunScripts.of(scriptSession.scriptType(), scripts);
        }
    }

    @Module
    public interface ServiceLoader {
        @Provides
        static RunScripts providesRunScriptLogic(ScriptSession scriptSession) {
            return RunScripts.serviceLoader(scriptSession.scriptType());
        }
    }

    @Module
    @Deprecated
    // As above, probably should be removed, and ServiceLoader promoted to ConsoleModule
    public interface OldConfig {
        @Provides
        static RunScripts providesRunScriptLogic(ScriptSession scriptSession) {
            return RunScripts.oldConfiguration(scriptSession.scriptType(),
                    scriptSession.getClass().getSimpleName() + ".initScripts");
        }
    }
}
