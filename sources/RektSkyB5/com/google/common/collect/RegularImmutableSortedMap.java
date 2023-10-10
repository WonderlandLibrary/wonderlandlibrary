/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
final class RegularImmutableSortedMap<K, V>
extends ImmutableSortedMap<K, V> {
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;

    RegularImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
        this.keySet = keySet;
        this.valueList = valueList;
    }

    RegularImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, ImmutableSortedMap<K, V> descendingMap) {
        super(descendingMap);
        this.keySet = keySet;
        this.valueList = valueList;
    }

    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new EntrySet();
    }

    @Override
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    @Override
    public ImmutableCollection<V> values() {
        return this.valueList;
    }

    @Override
    public V get(@Nullable Object key) {
        int index = this.keySet.indexOf(key);
        return index == -1 ? null : (V)this.valueList.get(index);
    }

    private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
        if (fromIndex == 0 && toIndex == this.size()) {
            return this;
        }
        if (fromIndex == toIndex) {
            return RegularImmutableSortedMap.emptyMap(this.comparator());
        }
        return RegularImmutableSortedMap.from(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
    }

    @Override
    public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
        return this.getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(toKey), inclusive));
    }

    @Override
    public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return this.getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(fromKey), inclusive), this.size());
    }

    @Override
    ImmutableSortedMap<K, V> createDescendingMap() {
        return new RegularImmutableSortedMap<K, V>((RegularImmutableSortedSet)this.keySet.descendingSet(), this.valueList.reverse(), this);
    }

    private class EntrySet
    extends ImmutableMapEntrySet<K, V> {
        private EntrySet() {
        }

        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.asList().iterator();
        }

        @Override
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new ImmutableAsList<Map.Entry<K, V>>(){
                private final ImmutableList<K> keyList;
                {
                    this.keyList = ((ImmutableCollection)((Object)RegularImmutableSortedMap.this.keySet())).asList();
                }

                @Override
                public Map.Entry<K, V> get(int index) {
                    return Maps.immutableEntry(this.keyList.get(index), RegularImmutableSortedMap.this.valueList.get(index));
                }

                @Override
                ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
                    return EntrySet.this;
                }
            };
        }

        @Override
        ImmutableMap<K, V> map() {
            return RegularImmutableSortedMap.this;
        }
    }
}

