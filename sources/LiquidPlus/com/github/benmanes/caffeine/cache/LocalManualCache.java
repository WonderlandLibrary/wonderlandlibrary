/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LocalCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

interface LocalManualCache<K, V>
extends Cache<K, V> {
    public LocalCache<K, V> cache();

    @Override
    default public long estimatedSize() {
        return this.cache().estimatedSize();
    }

    @Override
    default public void cleanUp() {
        this.cache().cleanUp();
    }

    @Override
    default public @Nullable V getIfPresent(Object key) {
        return this.cache().getIfPresent(key, true);
    }

    @Override
    default public @Nullable V get(K key, Function<? super K, ? extends V> mappingFunction) {
        return this.cache().computeIfAbsent((K)key, mappingFunction);
    }

    @Override
    default public Map<K, V> getAllPresent(Iterable<?> keys2) {
        return this.cache().getAllPresent(keys2);
    }

    @Override
    default public Map<K, V> getAll(Iterable<? extends K> keys2, Function<Iterable<? extends K>, Map<K, V>> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        LinkedHashSet<K> keysToLoad = new LinkedHashSet<K>();
        Map<K, V> found = this.cache().getAllPresent(keys2);
        LinkedHashMap<K, V> result = new LinkedHashMap<K, V>(found.size());
        for (K key : keys2) {
            V value = found.get(key);
            if (value == null) {
                keysToLoad.add(key);
            }
            result.put(key, value);
        }
        if (keysToLoad.isEmpty()) {
            return found;
        }
        this.bulkLoad(keysToLoad, result, mappingFunction);
        return Collections.unmodifiableMap(result);
    }

    default public void bulkLoad(Set<K> keysToLoad, Map<K, V> result, Function<Iterable<? extends @NonNull K>, @NonNull Map<K, V>> mappingFunction) {
        boolean success = false;
        long startTime = this.cache().statsTicker().read();
        try {
            Map<Object, Object> loaded = mappingFunction.apply(keysToLoad);
            loaded.forEach((key, value) -> this.cache().put(key, value, false));
            for (K key2 : keysToLoad) {
                V value2 = loaded.get(key2);
                if (value2 == null) {
                    result.remove(key2);
                    continue;
                }
                result.put(key2, value2);
            }
            success = !loaded.isEmpty();
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new CompletionException(e);
        }
        finally {
            long loadTime = this.cache().statsTicker().read() - startTime;
            if (success) {
                this.cache().statsCounter().recordLoadSuccess(loadTime);
            } else {
                this.cache().statsCounter().recordLoadFailure(loadTime);
            }
        }
    }

    @Override
    default public void put(K key, V value) {
        this.cache().put(key, value);
    }

    @Override
    default public void putAll(Map<? extends K, ? extends V> map) {
        this.cache().putAll(map);
    }

    @Override
    default public void invalidate(Object key) {
        this.cache().remove(key);
    }

    @Override
    default public void invalidateAll(Iterable<?> keys2) {
        this.cache().invalidateAll(keys2);
    }

    @Override
    default public void invalidateAll() {
        this.cache().clear();
    }

    @Override
    default public CacheStats stats() {
        return this.cache().statsCounter().snapshot();
    }

    @Override
    default public ConcurrentMap<K, V> asMap() {
        return this.cache();
    }
}

