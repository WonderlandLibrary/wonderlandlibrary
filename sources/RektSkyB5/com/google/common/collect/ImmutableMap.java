/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableEnumMap;
import com.google.common.collect.ImmutableMapEntry;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableMapKeySet;
import com.google.common.collect.ImmutableMapValues;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.RegularImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
public abstract class ImmutableMap<K, V>
implements Map<K, V>,
Serializable {
    private static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Map.Entry[0];
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    private transient ImmutableCollection<V> values;
    private transient ImmutableSetMultimap<K, V> multimapView;

    public static <K, V> ImmutableMap<K, V> of() {
        return ImmutableBiMap.of();
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
        return ImmutableBiMap.of(k1, v1);
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
        return new RegularImmutableMap(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2));
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return new RegularImmutableMap(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3));
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new RegularImmutableMap(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4));
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new RegularImmutableMap(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5));
    }

    static <K, V> ImmutableMapEntry.TerminalEntry<K, V> entryOf(K key, V value) {
        CollectPreconditions.checkEntryNotNull(key, value);
        return new ImmutableMapEntry.TerminalEntry<K, V>(key, value);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    static void checkNoConflict(boolean safe, String conflictDescription, Map.Entry<?, ?> entry1, Map.Entry<?, ?> entry2) {
        if (!safe) {
            throw new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2);
        }
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
            ImmutableMap kvMap = (ImmutableMap)map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        } else if (map instanceof EnumMap) {
            return ImmutableMap.copyOfEnumMapUnsafe(map);
        }
        Map.Entry<?, ?>[] entries = map.entrySet().toArray(EMPTY_ENTRY_ARRAY);
        switch (entries.length) {
            case 0: {
                return ImmutableMap.of();
            }
            case 1: {
                Map.Entry<?, ?> onlyEntry = entries[0];
                return ImmutableMap.of(onlyEntry.getKey(), onlyEntry.getValue());
            }
        }
        return new RegularImmutableMap(entries);
    }

    private static <K, V> ImmutableMap<K, V> copyOfEnumMapUnsafe(Map<? extends K, ? extends V> map) {
        return ImmutableMap.copyOfEnumMap((EnumMap)map);
    }

    private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(Map<K, ? extends V> original) {
        EnumMap<K, V> copy = new EnumMap<K, V>(original);
        for (Map.Entry<K, V> entry : copy.entrySet()) {
            CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(copy);
    }

    ImmutableMap() {
    }

    @Override
    @Deprecated
    public final V put(K k2, V v2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final V remove(Object o2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return ((ImmutableCollection)this.values()).contains(value);
    }

    @Override
    public abstract V get(@Nullable Object var1);

    @Override
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
        return result == null ? (this.entrySet = this.createEntrySet()) : result;
    }

    abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();

    @Override
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> result = this.keySet;
        return result == null ? (this.keySet = this.createKeySet()) : result;
    }

    ImmutableSet<K> createKeySet() {
        return new ImmutableMapKeySet(this);
    }

    @Override
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> result = this.values;
        return result == null ? (this.values = new ImmutableMapValues(this)) : result;
    }

    @Beta
    public ImmutableSetMultimap<K, V> asMultimap() {
        ImmutableSetMultimap<K, V> result = this.multimapView;
        return result == null ? (this.multimapView = this.createMultimapView()) : result;
    }

    private ImmutableSetMultimap<K, V> createMultimapView() {
        ImmutableMap<K, ImmutableSet<V>> map = this.viewMapValuesAsSingletonSets();
        return new ImmutableSetMultimap<K, V>(map, map.size(), null);
    }

    private ImmutableMap<K, ImmutableSet<V>> viewMapValuesAsSingletonSets() {
        return new MapViewOfValuesAsSingletonSets(this);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        return Maps.equalsImpl(this, object);
    }

    abstract boolean isPartialView();

    @Override
    public int hashCode() {
        return ((ImmutableSet)this.entrySet()).hashCode();
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }

    static class SerializedForm
    implements Serializable {
        private final Object[] keys;
        private final Object[] values;
        private static final long serialVersionUID = 0L;

        SerializedForm(ImmutableMap<?, ?> map) {
            this.keys = new Object[map.size()];
            this.values = new Object[map.size()];
            int i2 = 0;
            for (Map.Entry entry : map.entrySet()) {
                this.keys[i2] = entry.getKey();
                this.values[i2] = entry.getValue();
                ++i2;
            }
        }

        Object readResolve() {
            Builder<Object, Object> builder = new Builder<Object, Object>();
            return this.createMap(builder);
        }

        Object createMap(Builder<Object, Object> builder) {
            for (int i2 = 0; i2 < this.keys.length; ++i2) {
                builder.put(this.keys[i2], this.values[i2]);
            }
            return builder.build();
        }
    }

    private static final class MapViewOfValuesAsSingletonSets<K, V>
    extends ImmutableMap<K, ImmutableSet<V>> {
        private final ImmutableMap<K, V> delegate;

        MapViewOfValuesAsSingletonSets(ImmutableMap<K, V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }

        @Override
        public int size() {
            return this.delegate.size();
        }

        @Override
        public boolean containsKey(@Nullable Object key) {
            return this.delegate.containsKey(key);
        }

        @Override
        public ImmutableSet<V> get(@Nullable Object key) {
            V outerValue = this.delegate.get(key);
            return outerValue == null ? null : ImmutableSet.of(outerValue);
        }

        @Override
        boolean isPartialView() {
            return false;
        }

        @Override
        ImmutableSet<Map.Entry<K, ImmutableSet<V>>> createEntrySet() {
            return new ImmutableMapEntrySet<K, ImmutableSet<V>>(){

                @Override
                ImmutableMap<K, ImmutableSet<V>> map() {
                    return MapViewOfValuesAsSingletonSets.this;
                }

                @Override
                public UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> iterator() {
                    final Iterator backingIterator = ((ImmutableSet)MapViewOfValuesAsSingletonSets.this.delegate.entrySet()).iterator();
                    return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>(){

                        @Override
                        public boolean hasNext() {
                            return backingIterator.hasNext();
                        }

                        @Override
                        public Map.Entry<K, ImmutableSet<V>> next() {
                            final Map.Entry backingEntry = (Map.Entry)backingIterator.next();
                            return new AbstractMapEntry<K, ImmutableSet<V>>(){

                                @Override
                                public K getKey() {
                                    return backingEntry.getKey();
                                }

                                @Override
                                public ImmutableSet<V> getValue() {
                                    return ImmutableSet.of(backingEntry.getValue());
                                }
                            };
                        }
                    };
                }
            };
        }
    }

    public static class Builder<K, V> {
        ImmutableMapEntry.TerminalEntry<K, V>[] entries;
        int size;

        public Builder() {
            this(4);
        }

        Builder(int initialCapacity) {
            this.entries = new ImmutableMapEntry.TerminalEntry[initialCapacity];
            this.size = 0;
        }

        private void ensureCapacity(int minCapacity) {
            if (minCapacity > this.entries.length) {
                this.entries = ObjectArrays.arraysCopyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
            }
        }

        public Builder<K, V> put(K key, V value) {
            this.ensureCapacity(this.size + 1);
            ImmutableMapEntry.TerminalEntry<K, V> entry = ImmutableMap.entryOf(key, value);
            this.entries[this.size++] = entry;
            return this;
        }

        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
            return this.put(entry.getKey(), entry.getValue());
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            this.ensureCapacity(this.size + map.size());
            for (Map.Entry<K, V> entry : map.entrySet()) {
                this.put(entry);
            }
            return this;
        }

        public ImmutableMap<K, V> build() {
            switch (this.size) {
                case 0: {
                    return ImmutableMap.of();
                }
                case 1: {
                    return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
                }
            }
            return new RegularImmutableMap(this.size, this.entries);
        }
    }
}

