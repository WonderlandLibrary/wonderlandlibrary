/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;

@GwtCompatible
public interface Cache<K, V> {
    @Nullable
    public V getIfPresent(Object var1);

    public V get(K var1, Callable<? extends V> var2) throws ExecutionException;

    public ImmutableMap<K, V> getAllPresent(Iterable<?> var1);

    public void put(K var1, V var2);

    public void putAll(Map<? extends K, ? extends V> var1);

    public void invalidate(Object var1);

    public void invalidateAll(Iterable<?> var1);

    public void invalidateAll();

    public long size();

    public CacheStats stats();

    public ConcurrentMap<K, V> asMap();

    public void cleanUp();
}

