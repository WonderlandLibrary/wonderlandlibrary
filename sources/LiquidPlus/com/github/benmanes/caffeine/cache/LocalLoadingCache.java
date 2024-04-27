/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.LocalManualCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

interface LocalLoadingCache<K, V>
extends LocalManualCache<K, V>,
LoadingCache<K, V> {
    public static final Logger logger = Logger.getLogger(LocalLoadingCache.class.getName());

    public CacheLoader<? super K, V> cacheLoader();

    public Function<K, V> mappingFunction();

    public @Nullable Function<Iterable<? extends K>, Map<K, V>> bulkMappingFunction();

    @Override
    default public @Nullable V get(K key) {
        return this.cache().computeIfAbsent(key, this.mappingFunction());
    }

    @Override
    default public Map<K, V> getAll(Iterable<? extends K> keys2) {
        Function mappingFunction = this.bulkMappingFunction();
        return mappingFunction == null ? this.loadSequentially(keys2) : this.getAll(keys2, mappingFunction);
    }

    default public Map<K, V> loadSequentially(Iterable<? extends K> keys2) {
        LinkedHashSet<K> uniqueKeys = new LinkedHashSet<K>();
        for (K key : keys2) {
            uniqueKeys.add(key);
        }
        int count = 0;
        LinkedHashMap result = new LinkedHashMap(uniqueKeys.size());
        try {
            for (Object key : uniqueKeys) {
                ++count;
                V value = this.get(key);
                if (value == null) continue;
                result.put(key, value);
            }
        }
        catch (Throwable t) {
            this.cache().statsCounter().recordMisses(uniqueKeys.size() - count);
            throw t;
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    default public void refresh(K key) {
        Objects.requireNonNull(key);
        long[] writeTime = new long[1];
        long startTime = this.cache().statsTicker().read();
        Object oldValue = this.cache().getIfPresentQuietly(key, writeTime);
        CompletableFuture<V> refreshFuture = oldValue == null ? this.cacheLoader().asyncLoad(key, this.cache().executor()) : this.cacheLoader().asyncReload(key, oldValue, this.cache().executor());
        refreshFuture.whenComplete((newValue, error) -> {
            long loadTime = this.cache().statsTicker().read() - startTime;
            if (error != null) {
                logger.log(Level.WARNING, "Exception thrown during refresh", (Throwable)error);
                this.cache().statsCounter().recordLoadFailure(loadTime);
                return;
            }
            boolean[] discard = new boolean[1];
            this.cache().compute(key, (k, currentValue) -> {
                if (currentValue == null) {
                    return newValue;
                }
                if (currentValue == oldValue) {
                    long expectedWriteTime = writeTime[0];
                    if (this.cache().hasWriteTime()) {
                        this.cache().getIfPresentQuietly(key, writeTime);
                    }
                    if (writeTime[0] == expectedWriteTime) {
                        return newValue;
                    }
                }
                discard[0] = true;
                return currentValue;
            }, false, false, true);
            if (discard[0] && this.cache().hasRemovalListener()) {
                this.cache().notifyRemoval(key, newValue, RemovalCause.REPLACED);
            }
            if (newValue == null) {
                this.cache().statsCounter().recordLoadFailure(loadTime);
            } else {
                this.cache().statsCounter().recordLoadSuccess(loadTime);
            }
        });
    }

    public static <K, V> Function<K, V> newMappingFunction(CacheLoader<? super K, V> cacheLoader) {
        return key -> {
            try {
                return cacheLoader.load(key);
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            }
            catch (Exception e) {
                throw new CompletionException(e);
            }
        };
    }

    public static <K, V> @Nullable Function<Iterable<? extends K>, Map<K, V>> newBulkMappingFunction(CacheLoader<? super K, V> cacheLoader) {
        if (!LocalLoadingCache.hasLoadAll(cacheLoader)) {
            return null;
        }
        return keysToLoad -> {
            try {
                Map loaded = cacheLoader.loadAll((Iterable)keysToLoad);
                return loaded;
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            }
            catch (Exception e) {
                throw new CompletionException(e);
            }
        };
    }

    public static boolean hasLoadAll(CacheLoader<?, ?> loader) {
        try {
            Method classLoadAll = loader.getClass().getMethod("loadAll", Iterable.class);
            Method defaultLoadAll = CacheLoader.class.getMethod("loadAll", Iterable.class);
            return !classLoadAll.equals(defaultLoadAll);
        }
        catch (NoSuchMethodException | SecurityException e) {
            logger.log(Level.WARNING, "Cannot determine if CacheLoader can bulk load", e);
            return false;
        }
    }
}

