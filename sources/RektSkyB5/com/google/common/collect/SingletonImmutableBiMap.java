/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
final class SingletonImmutableBiMap<K, V>
extends ImmutableBiMap<K, V> {
    final transient K singleKey;
    final transient V singleValue;
    transient ImmutableBiMap<V, K> inverse;

    SingletonImmutableBiMap(K singleKey, V singleValue) {
        CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
        this.singleKey = singleKey;
        this.singleValue = singleValue;
    }

    private SingletonImmutableBiMap(K singleKey, V singleValue, ImmutableBiMap<V, K> inverse) {
        this.singleKey = singleKey;
        this.singleValue = singleValue;
        this.inverse = inverse;
    }

    SingletonImmutableBiMap(Map.Entry<? extends K, ? extends V> entry) {
        this(entry.getKey(), entry.getValue());
    }

    @Override
    public V get(@Nullable Object key) {
        return this.singleKey.equals(key) ? (V)this.singleValue : null;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return this.singleKey.equals(key);
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return this.singleValue.equals(value);
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
    }

    @Override
    ImmutableSet<K> createKeySet() {
        return ImmutableSet.of(this.singleKey);
    }

    @Override
    public ImmutableBiMap<V, K> inverse() {
        ImmutableBiMap<V, K> result = this.inverse;
        if (result == null) {
            this.inverse = new SingletonImmutableBiMap<K, V>(this.singleValue, this.singleKey, this);
            return this.inverse;
        }
        return result;
    }
}

