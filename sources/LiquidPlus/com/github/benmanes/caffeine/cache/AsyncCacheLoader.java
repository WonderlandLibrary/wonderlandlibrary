/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface AsyncCacheLoader<K, V> {
    public @NonNull CompletableFuture<V> asyncLoad(@NonNull K var1, @NonNull Executor var2);

    default public @NonNull CompletableFuture<Map<@NonNull K, @NonNull V>> asyncLoadAll(@NonNull Iterable<? extends @NonNull K> keys2, @NonNull Executor executor) {
        throw new UnsupportedOperationException();
    }

    default public @NonNull CompletableFuture<V> asyncReload(@NonNull K key, @NonNull V oldValue, @NonNull Executor executor) {
        return this.asyncLoad(key, executor);
    }
}

