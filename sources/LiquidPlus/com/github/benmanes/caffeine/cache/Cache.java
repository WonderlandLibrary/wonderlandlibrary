/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CompatibleWith
 *  org.checkerframework.checker.index.qual.NonNegative
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Policy;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Cache<K, V> {
    public @Nullable V getIfPresent(@CompatibleWith(value="K") @NonNull Object var1);

    public @Nullable V get(@NonNull K var1, @NonNull Function<? super K, ? extends V> var2);

    public @NonNull Map<@NonNull K, @NonNull V> getAllPresent(@NonNull Iterable<@NonNull ?> var1);

    default public @NonNull Map<K, V> getAll(@NonNull Iterable<? extends @NonNull K> keys2, @NonNull Function<Iterable<? extends @NonNull K>, @NonNull Map<K, V>> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    public void put(@NonNull K var1, @NonNull V var2);

    public void putAll(@NonNull Map<? extends @NonNull K, ? extends @NonNull V> var1);

    public void invalidate(@CompatibleWith(value="K") @NonNull Object var1);

    public void invalidateAll(@NonNull Iterable<@NonNull ?> var1);

    public void invalidateAll();

    public @NonNegative long estimatedSize();

    public @NonNull CacheStats stats();

    public @NonNull ConcurrentMap<@NonNull K, @NonNull V> asMap();

    public void cleanUp();

    public @NonNull Policy<K, V> policy();
}

