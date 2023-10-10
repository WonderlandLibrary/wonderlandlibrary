/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.cache.AbstractCache;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

@Beta
public abstract class AbstractLoadingCache<K, V>
extends AbstractCache<K, V>
implements LoadingCache<K, V> {
    protected AbstractLoadingCache() {
    }

    @Override
    public V getUnchecked(K key) {
        try {
            return this.get(key);
        }
        catch (ExecutionException e2) {
            throw new UncheckedExecutionException(e2.getCause());
        }
    }

    @Override
    public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
        LinkedHashMap result = Maps.newLinkedHashMap();
        for (K key : keys) {
            if (result.containsKey(key)) continue;
            result.put(key, this.get(key));
        }
        return ImmutableMap.copyOf(result);
    }

    @Override
    public final V apply(K key) {
        return this.getUnchecked(key);
    }

    @Override
    public void refresh(K key) {
        throw new UnsupportedOperationException();
    }
}

