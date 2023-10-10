/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
final class EmptyImmutableSortedMap<K, V>
extends ImmutableSortedMap<K, V> {
    private final transient ImmutableSortedSet<K> keySet;

    EmptyImmutableSortedMap(Comparator<? super K> comparator) {
        this.keySet = ImmutableSortedSet.emptySet(comparator);
    }

    EmptyImmutableSortedMap(Comparator<? super K> comparator, ImmutableSortedMap<K, V> descendingMap) {
        super(descendingMap);
        this.keySet = ImmutableSortedSet.emptySet(comparator);
    }

    @Override
    public V get(@Nullable Object key) {
        return null;
    }

    @Override
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ImmutableCollection<V> values() {
        return ImmutableList.of();
    }

    @Override
    public String toString() {
        return "{}";
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return ImmutableSet.of();
    }

    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        throw new AssertionError((Object)"should never be called");
    }

    @Override
    public ImmutableSetMultimap<K, V> asMultimap() {
        return ImmutableSetMultimap.of();
    }

    @Override
    public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
        Preconditions.checkNotNull(toKey);
        return this;
    }

    @Override
    public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
        Preconditions.checkNotNull(fromKey);
        return this;
    }

    @Override
    ImmutableSortedMap<K, V> createDescendingMap() {
        return new EmptyImmutableSortedMap(Ordering.from(this.comparator()).reverse(), this);
    }
}

