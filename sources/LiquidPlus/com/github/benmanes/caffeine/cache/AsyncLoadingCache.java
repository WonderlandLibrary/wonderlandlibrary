/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface AsyncLoadingCache<K, V>
extends AsyncCache<K, V> {
    public @NonNull CompletableFuture<V> get(@NonNull K var1);

    public @NonNull CompletableFuture<Map<K, V>> getAll(@NonNull Iterable<? extends @NonNull K> var1);

    @Override
    default public @NonNull ConcurrentMap<@NonNull K, @NonNull CompletableFuture<V>> asMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull LoadingCache<K, V> synchronous();
}

