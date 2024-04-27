/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
public interface CacheLoader<K, V>
extends AsyncCacheLoader<K, V> {
    public @Nullable V load(@NonNull K var1) throws Exception;

    default public @NonNull Map<@NonNull K, @NonNull V> loadAll(@NonNull Iterable<? extends @NonNull K> keys2) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    default public @NonNull CompletableFuture<V> asyncLoad(@NonNull K key, @NonNull Executor executor) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(executor);
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.load(key);
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    default public @NonNull CompletableFuture<Map<@NonNull K, @NonNull V>> asyncLoadAll(@NonNull Iterable<? extends K> keys2, @NonNull Executor executor) {
        Objects.requireNonNull(keys2);
        Objects.requireNonNull(executor);
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.loadAll(keys2);
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    default public @Nullable V reload(@NonNull K key, @NonNull V oldValue) throws Exception {
        return this.load(key);
    }

    @Override
    default public @NonNull CompletableFuture<V> asyncReload(@NonNull K key, @NonNull V oldValue, @NonNull Executor executor) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(executor);
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.reload(key, oldValue);
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executor);
    }
}

