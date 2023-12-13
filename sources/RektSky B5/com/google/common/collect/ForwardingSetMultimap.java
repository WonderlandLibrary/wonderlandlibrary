/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.SetMultimap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSetMultimap<K, V>
extends ForwardingMultimap<K, V>
implements SetMultimap<K, V> {
    @Override
    protected abstract SetMultimap<K, V> delegate();

    @Override
    public Set<Map.Entry<K, V>> entries() {
        return this.delegate().entries();
    }

    @Override
    public Set<V> get(@Nullable K key) {
        return this.delegate().get(key);
    }

    @Override
    public Set<V> removeAll(@Nullable Object key) {
        return this.delegate().removeAll(key);
    }

    @Override
    public Set<V> replaceValues(K key, Iterable<? extends V> values) {
        return this.delegate().replaceValues(key, values);
    }
}

