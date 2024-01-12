/*
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.context;

import io.deephaven.engine.liveness.LivenessReferent;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

public class EmptyQueryScope implements QueryScope {
    public final static EmptyQueryScope INSTANCE = new EmptyQueryScope();

    private EmptyQueryScope() {}

    @Override
    public Set<String> getParamNames() {
        return Collections.emptySet();
    }

    @Override
    public boolean hasParamName(String name) {
        return false;
    }

    @Override
    public <T> QueryScopeParam<T> createParam(String name) throws MissingVariableException {
        throw new MissingVariableException("Missing variable " + name);
    }

    @Override
    public <T> T readParamValue(String name) throws MissingVariableException {
        throw new MissingVariableException("Missing variable " + name);
    }

    @Override
    public <T> T readParamValue(String name, T defaultValue) {
        return defaultValue;
    }

    @Override
    public <T> void putParam(String name, T value) {
        throw new IllegalStateException("EmptyQueryScope cannot create parameters");
    }

    @Override
    public boolean tryManage(@NotNull LivenessReferent referent) {
        throw new UnsupportedOperationException("tryManage");
    }

    @Override
    public boolean tryUnmanage(@NotNull LivenessReferent referent) {
        throw new UnsupportedOperationException("tryUnmanage");
    }

    @Override
    public boolean tryUnmanage(@NotNull Stream<? extends LivenessReferent> referents) {
        throw new UnsupportedOperationException("tryUnmanage");
    }

    @Override
    public boolean tryRetainReference() {
        throw new UnsupportedOperationException("tryRetainReference");
    }

    @Override
    public void dropReference() {
        throw new UnsupportedOperationException("dropReference");
    }

    @Override
    public WeakReference<? extends LivenessReferent> getWeakReference() {
        throw new UnsupportedOperationException("getWeakReference");
    }
}
