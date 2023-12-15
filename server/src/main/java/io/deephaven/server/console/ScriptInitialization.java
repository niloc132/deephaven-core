package io.deephaven.server.console;

import io.deephaven.base.FileUtils;
import io.deephaven.configuration.Configuration;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.liveness.LivenessScopeStack;
import io.deephaven.engine.util.AbstractScriptSession;
import io.deephaven.engine.util.ScriptFinder;
import io.deephaven.engine.util.ScriptSession;
import io.deephaven.internal.log.LoggerFactory;
import io.deephaven.io.logger.Logger;
import io.deephaven.server.appmode.ApplicationInjector;
import io.deephaven.server.plugin.PluginRegistration;
import io.deephaven.server.runner.DeephavenApiServer;
import io.deephaven.server.util.Scheduler;
import io.deephaven.uri.resolver.UriResolver;
import io.deephaven.uri.resolver.UriResolvers;
import io.deephaven.uri.resolver.UriResolversInstance;
import io.deephaven.util.SafeCloseable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * User application code and environment setup to run upon startup, including scheduling scope change checks, running
 * init scripts, loading plugins, and running applications.
 * <p>
 * </p>
 * Presently this is depended on by the ConsoleService implementation, but probably should be also an upstream
 * dependency of the ApplicationService impl too.
 */
@Singleton
public class ScriptInitialization {
    private static final Logger log = LoggerFactory.getLogger(ScriptInitialization.class);

    private static final long CHECK_SCOPE_CHANGES_INTERVAL_MILLIS =
            Configuration.getInstance().getLongForClassWithDefault(
                    DeephavenApiServer.class, "checkScopeChangesIntervalMillis", 100);

    @Inject
    public ScriptInitialization(
            @SuppressWarnings("unused") ExecutionContext executionContext,
            ScriptSession scriptSession,
            ScriptSession.RunScripts runScripts,
            PluginRegistration pluginRegistration,
            Scheduler scheduler,
            UriResolvers uriResolvers,
            ApplicationInjector applicationInjector) {

        log.info().append("Creating/Clearing Script Cache...").endl();
        //TODO finish

        // review note: this is now _before_ init scripts.
        pluginRegistration.registerAll();

        // There is no set time at which uri resolvers must be made available, except before user code starts.
        for (UriResolver resolver : uriResolvers.resolvers()) {
            log.debug().append("Found table resolver ").append(resolver.getClass().toString()).endl();
        }
        UriResolversInstance.init(uriResolvers);

        // Find specified init scripts and run each one.
        ScriptFinder scriptFinder = new ScriptFinder(".");
        try {
            for (String path : runScripts.paths()) {
                ScriptFinder.FileOrStream script = scriptFinder.findScriptEx(path);
                if (script.getFile().isPresent()) {
                    scriptSession.evaluateScript(script.getFile().get().getPath());
                } else if (script.getStream().isPresent()) {
                    scriptSession.evaluateScript(FileUtils.readTextFile(script.getStream().get()), path);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load script ", e);
        }

        // Inject the configured applications.
        try {
            applicationInjector.run();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Failed to start application", e);
        }

        // Finally, observe any changes that have taken place, and periodically take snapshots (if configured)
        checkScopeChanges(scheduler, scriptSession);
    }

    private static void checkScopeChanges(Scheduler scheduler, ScriptSession scriptSession) {
        try (SafeCloseable ignored = LivenessScopeStack.open()) {
            scriptSession.observeScopeChanges();
        }
        if (CHECK_SCOPE_CHANGES_INTERVAL_MILLIS > 0) {
            scheduler.runAfterDelay(CHECK_SCOPE_CHANGES_INTERVAL_MILLIS, () -> {
                checkScopeChanges(scheduler, scriptSession);
            });
        }
    }

}
