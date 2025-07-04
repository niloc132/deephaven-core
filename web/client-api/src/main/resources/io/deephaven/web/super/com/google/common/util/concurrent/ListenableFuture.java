//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.google.common.util.concurrent;

import java.util.concurrent.Executor;

public interface ListenableFuture<V> {
    void addListener(Runnable listener, Executor executor);
}
