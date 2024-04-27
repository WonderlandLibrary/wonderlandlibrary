/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CompatibleWith
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface AsyncCache<K, V> {
    public @Nullable CompletableFuture<V> getIfPresent(@CompatibleWith(value="K") @NonNull Object var1);

    public @NonNull CompletableFuture<V> get(@NonNull K var1, @NonNull Function<? super K, ? extends V> var2);

    public @NonNull CompletableFuture<V> get(@NonNull K var1, @NonNull BiFunction<? super K, Executor, CompletableFuture<V>> var2);

    default public @NonNull CompletableFuture<Map<K, V>> getAll(@NonNull Iterable<? extends @NonNull K> keys2, @NonNull Function<Iterable<? extends @NonNull K>, @NonNull Map<K, V>> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    default public @NonNull CompletableFuture<Map<K, V>> getAll(@NonNull Iterable<? extends @NonNull K> keys2, @NonNull BiFunction<Iterable<? extends @NonNull K>, Executor, CompletableFuture<Map<K, V>>> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    public void put(@NonNull K var1, @NonNull CompletableFuture<V> var2);

    public @NonNull ConcurrentMap<@NonNull K, @NonNull CompletableFuture<V>> asMap();

    public @NonNull Cache<K, V> synchronous();
}

