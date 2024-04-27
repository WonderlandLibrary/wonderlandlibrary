/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.Ticker;
import com.github.benmanes.caffeine.cache.stats.StatsCounter;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

interface LocalCache<K, V>
extends ConcurrentMap<K, V> {
    public boolean isRecordingStats();

    public @NonNull StatsCounter statsCounter();

    public boolean hasRemovalListener();

    public RemovalListener<K, V> removalListener();

    public void notifyRemoval(@Nullable K var1, @Nullable V var2, RemovalCause var3);

    public @NonNull Executor executor();

    public boolean hasWriteTime();

    public @NonNull Ticker expirationTicker();

    public @NonNull Ticker statsTicker();

    public long estimatedSize();

    public @Nullable V getIfPresent(@NonNull Object var1, boolean var2);

    public @Nullable V getIfPresentQuietly(@NonNull Object var1, @NonNull long[] var2);

    public @NonNull Map<K, V> getAllPresent(@NonNull Iterable<?> var1);

    public @Nullable V put(@NonNull K var1, @NonNull V var2, boolean var3);

    @Override
    default public @Nullable V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return this.compute(key, remappingFunction, false, true, true);
    }

    public @Nullable V compute(K var1, BiFunction<? super K, ? super V, ? extends V> var2, boolean var3, boolean var4, boolean var5);

    @Override
    default public @Nullable V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return this.computeIfAbsent(key, mappingFunction, true, true);
    }

    public @Nullable V computeIfAbsent(K var1, Function<? super K, ? extends V> var2, boolean var3, boolean var4);

    default public void invalidateAll(Iterable<?> keys2) {
        for (Object key : keys2) {
            this.remove(key);
        }
    }

    public void cleanUp();

    default public <T, R> Function<? super T, ? extends R> statsAware(Function<? super T, ? extends R> mappingFunction, boolean recordLoad) {
        if (!this.isRecordingStats()) {
            return mappingFunction;
        }
        return key -> {
            Object value;
            this.statsCounter().recordMisses(1);
            long startTime = this.statsTicker().read();
            try {
                value = mappingFunction.apply(key);
            }
            catch (Error | RuntimeException e) {
                this.statsCounter().recordLoadFailure(this.statsTicker().read() - startTime);
                throw e;
            }
            long loadTime = this.statsTicker().read() - startTime;
            if (recordLoad) {
                if (value == null) {
                    this.statsCounter().recordLoadFailure(loadTime);
                } else {
                    this.statsCounter().recordLoadSuccess(loadTime);
                }
            }
            return value;
        };
    }

    default public <T, U, R> BiFunction<? super T, ? super U, ? extends R> statsAware(BiFunction<? super T, ? super U, ? extends R> remappingFunction) {
        return this.statsAware(remappingFunction, true, true, true);
    }

    default public <T, U, R> BiFunction<? super T, ? super U, ? extends R> statsAware(BiFunction<? super T, ? super U, ? extends R> remappingFunction, boolean recordMiss, boolean recordLoad, boolean recordLoadFailure) {
        if (!this.isRecordingStats()) {
            return remappingFunction;
        }
        return (t, u) -> {
            Object result;
            if (u == null && recordMiss) {
                this.statsCounter().recordMisses(1);
            }
            long startTime = this.statsTicker().read();
            try {
                result = remappingFunction.apply(t, u);
            }
            catch (Error | RuntimeException e) {
                if (recordLoadFailure) {
                    this.statsCounter().recordLoadFailure(this.statsTicker().read() - startTime);
                }
                throw e;
            }
            long loadTime = this.statsTicker().read() - startTime;
            if (recordLoad) {
                if (result == null) {
                    this.statsCounter().recordLoadFailure(loadTime);
                } else {
                    this.statsCounter().recordLoadSuccess(loadTime);
                }
            }
            return result;
        };
    }
}

