package io.deephaven.server.console;

import com.github.f4b6a3.uuid.UuidCreator;
import dagger.Module;
import dagger.Provides;
import io.deephaven.UncheckedDeephavenException;
import io.deephaven.configuration.CacheDir;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.context.QueryCompiler;
import io.deephaven.engine.context.QueryScope;
import io.deephaven.engine.table.impl.OperationInitializationThreadPool;
import io.deephaven.engine.updategraph.UpdateGraph;
import io.deephaven.engine.updategraph.impl.PeriodicUpdateGraph;
import io.deephaven.engine.util.ScriptSession;
import io.deephaven.internal.log.LoggerFactory;
import io.deephaven.io.logger.Logger;
import io.deephaven.server.auth.AuthorizationProvider;
import io.deephaven.util.thread.ThreadInitializationFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

@Module
public interface ExecutionContextModule {
    @Provides
    @Singleton
    static QueryCompiler provideQueryCompiler() {
        // TODO(deephaven-core#1713): Introduce instance-id concept
        // TODO how important is it to destroy this on stop?
        Path classCacheLocation = CacheDir.get().resolve("script-session-classes");
        final UUID scriptCacheId = UuidCreator.getRandomBased();
        File classCacheDirectory = classCacheLocation.resolve(UuidCreator.toString(scriptCacheId)).toFile();
        if (!classCacheDirectory.mkdirs()) {
            throw new UncheckedDeephavenException(
                    "Failed to create class cache directory " + classCacheDirectory.getAbsolutePath());
        }
        return QueryCompiler.create(classCacheDirectory, Thread.currentThread().getContextClassLoader());
    }


    // This assumes that there is exactly one script session - if we allow more than one, this needs to be scoped
    @Provides
    @Singleton
    static QueryScope provideQueryScope(ScriptSession scriptSession) {
        return scriptSession.newQueryScope();
    }

    @Provides
    @Singleton
    static ExecutionContext provideAndOpenExecContext(
            @Named(PeriodicUpdateGraph.DEFAULT_UPDATE_GRAPH_NAME) UpdateGraph updateGraph,
            AuthorizationProvider authorizationProvider,
            ThreadInitializationFactory threadInitializer,
            QueryCompiler queryCompiler,
            QueryScope queryScope) {
        ExecutionContext executionContext = ExecutionContext.newBuilder()
                .markSystemic()
                .newQueryLibrary()
                .setQueryScope(queryScope)
                .setQueryCompiler(queryCompiler)
                .setUpdateGraph(updateGraph)
                .setOperationInitializer(new OperationInitializationThreadPool(threadInitializer))
                .build()
                .withAuthContext(authorizationProvider.getInstanceAuthContext());

        // Open it to install it, leave it open for the duration of the main thread
        executionContext.open();

        return executionContext;
    }
}
