/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.EmptyImmutableSortedMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMapFauxverideShim;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.RegularImmutableSortedMap;
import com.google.common.collect.RegularImmutableSortedSet;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
public abstract class ImmutableSortedMap<K, V>
extends ImmutableSortedMapFauxverideShim<K, V>
implements NavigableMap<K, V> {
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap<Comparable, Object>(NATURAL_ORDER);
    private transient ImmutableSortedMap<K, V> descendingMap;
    private static final long serialVersionUID = 0L;

    static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return ImmutableSortedMap.of();
        }
        return new EmptyImmutableSortedMap(comparator);
    }

    static <K, V> ImmutableSortedMap<K, V> fromSortedEntries(Comparator<? super K> comparator, int size, Map.Entry<K, V>[] entries) {
        if (size == 0) {
            return ImmutableSortedMap.emptyMap(comparator);
        }
        ImmutableList.Builder keyBuilder = ImmutableList.builder();
        ImmutableList.Builder valueBuilder = ImmutableList.builder();
        for (int i2 = 0; i2 < size; ++i2) {
            Map.Entry<K, V> entry = entries[i2];
            keyBuilder.add(entry.getKey());
            valueBuilder.add(entry.getValue());
        }
        return new RegularImmutableSortedMap(new RegularImmutableSortedSet<K>(keyBuilder.build(), comparator), valueBuilder.build());
    }

    static <K, V> ImmutableSortedMap<K, V> from(ImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
        if (keySet.isEmpty()) {
            return ImmutableSortedMap.emptyMap(keySet.comparator());
        }
        return new RegularImmutableSortedMap((RegularImmutableSortedSet)keySet, valueList);
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return NATURAL_EMPTY_MAP;
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
        return ImmutableSortedMap.from(ImmutableSortedSet.of(k1), ImmutableList.of(v1));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
        return ImmutableSortedMap.fromEntries(Ordering.natural(), false, 2, ImmutableSortedMap.entryOf(k1, v1), ImmutableSortedMap.entryOf(k2, v2));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return ImmutableSortedMap.fromEntries(Ordering.natural(), false, 3, ImmutableSortedMap.entryOf(k1, v1), ImmutableSortedMap.entryOf(k2, v2), ImmutableSortedMap.entryOf(k3, v3));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ImmutableSortedMap.fromEntries(Ordering.natural(), false, 4, ImmutableSortedMap.entryOf(k1, v1), ImmutableSortedMap.entryOf(k2, v2), ImmutableSortedMap.entryOf(k3, v3), ImmutableSortedMap.entryOf(k4, v4));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ImmutableSortedMap.fromEntries(Ordering.natural(), false, 5, ImmutableSortedMap.entryOf(k1, v1), ImmutableSortedMap.entryOf(k2, v2), ImmutableSortedMap.entryOf(k3, v3), ImmutableSortedMap.entryOf(k4, v4), ImmutableSortedMap.entryOf(k5, v5));
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        Ordering naturalOrder = Ordering.natural();
        return ImmutableSortedMap.copyOfInternal(map, naturalOrder);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        return ImmutableSortedMap.copyOfInternal(map, Preconditions.checkNotNull(comparator));
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
        Comparator<Object> comparator = map.comparator();
        if (comparator == null) {
            comparator = NATURAL_ORDER;
        }
        return ImmutableSortedMap.copyOfInternal(map, comparator);
    }

    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        ImmutableSortedMap kvMap;
        boolean sameComparator = false;
        if (map instanceof SortedMap) {
            SortedMap sortedMap = (SortedMap)map;
            Comparator comparator2 = sortedMap.comparator();
            boolean bl = comparator2 == null ? comparator == NATURAL_ORDER : (sameComparator = comparator.equals(comparator2));
        }
        if (sameComparator && map instanceof ImmutableSortedMap && !(kvMap = (ImmutableSortedMap)map).isPartialView()) {
            return kvMap;
        }
        Map.Entry[] entries = map.entrySet().toArray(new Map.Entry[0]);
        return ImmutableSortedMap.fromEntries(comparator, sameComparator, entries.length, entries);
    }

    static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, int size, Map.Entry<K, V> ... entries) {
        for (int i2 = 0; i2 < size; ++i2) {
            Map.Entry<K, V> entry = entries[i2];
            entries[i2] = ImmutableSortedMap.entryOf(entry.getKey(), entry.getValue());
        }
        if (!sameComparator) {
            ImmutableSortedMap.sortEntries(comparator, size, entries);
            ImmutableSortedMap.validateEntries(size, entries, comparator);
        }
        return ImmutableSortedMap.fromSortedEntries(comparator, size, entries);
    }

    private static <K, V> void sortEntries(Comparator<? super K> comparator, int size, Map.Entry<K, V>[] entries) {
        Arrays.sort(entries, 0, size, Ordering.from(comparator).onKeys());
    }

    private static <K, V> void validateEntries(int size, Map.Entry<K, V>[] entries, Comparator<? super K> comparator) {
        for (int i2 = 1; i2 < size; ++i2) {
            ImmutableSortedMap.checkNoConflict(comparator.compare(entries[i2 - 1].getKey(), entries[i2].getKey()) != 0, "key", entries[i2 - 1], entries[i2]);
        }
    }

    public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
        return new Builder(comparator);
    }

    public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    ImmutableSortedMap() {
    }

    ImmutableSortedMap(ImmutableSortedMap<K, V> descendingMap) {
        this.descendingMap = descendingMap;
    }

    @Override
    public int size() {
        return ((AbstractCollection)this.values()).size();
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return ((ImmutableCollection)this.values()).contains(value);
    }

    @Override
    boolean isPartialView() {
        return this.keySet().isPartialView() || ((ImmutableCollection)this.values()).isPartialView();
    }

    @Override
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    @Override
    public abstract ImmutableSortedSet<K> keySet();

    @Override
    public abstract ImmutableCollection<V> values();

    @Override
    public Comparator<? super K> comparator() {
        return ((ImmutableSortedSet)this.keySet()).comparator();
    }

    @Override
    public K firstKey() {
        return (K)((ImmutableSortedSet)this.keySet()).first();
    }

    @Override
    public K lastKey() {
        return (K)((ImmutableSortedSet)this.keySet()).last();
    }

    @Override
    public ImmutableSortedMap<K, V> headMap(K toKey) {
        return this.headMap((Object)toKey, false);
    }

    @Override
    public abstract ImmutableSortedMap<K, V> headMap(K var1, boolean var2);

    @Override
    public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
        return this.subMap((Object)fromKey, true, (Object)toKey, false);
    }

    @Override
    public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        Preconditions.checkNotNull(fromKey);
        Preconditions.checkNotNull(toKey);
        Preconditions.checkArgument(this.comparator().compare(fromKey, toKey) <= 0, "expected fromKey <= toKey but %s > %s", fromKey, toKey);
        return ((ImmutableSortedMap)this.headMap((Object)toKey, toInclusive)).tailMap((Object)fromKey, fromInclusive);
    }

    @Override
    public ImmutableSortedMap<K, V> tailMap(K fromKey) {
        return this.tailMap((Object)fromKey, true);
    }

    @Override
    public abstract ImmutableSortedMap<K, V> tailMap(K var1, boolean var2);

    @Override
    public Map.Entry<K, V> lowerEntry(K key) {
        return ((ImmutableSortedMap)this.headMap((Object)key, false)).lastEntry();
    }

    @Override
    public K lowerKey(K key) {
        return Maps.keyOrNull(this.lowerEntry(key));
    }

    @Override
    public Map.Entry<K, V> floorEntry(K key) {
        return ((ImmutableSortedMap)this.headMap((Object)key, true)).lastEntry();
    }

    @Override
    public K floorKey(K key) {
        return Maps.keyOrNull(this.floorEntry(key));
    }

    @Override
    public Map.Entry<K, V> ceilingEntry(K key) {
        return ((ImmutableSortedMap)this.tailMap((Object)key, true)).firstEntry();
    }

    @Override
    public K ceilingKey(K key) {
        return Maps.keyOrNull(this.ceilingEntry(key));
    }

    @Override
    public Map.Entry<K, V> higherEntry(K key) {
        return ((ImmutableSortedMap)this.tailMap((Object)key, false)).firstEntry();
    }

    @Override
    public K higherKey(K key) {
        return Maps.keyOrNull(this.higherEntry(key));
    }

    @Override
    public Map.Entry<K, V> firstEntry() {
        return this.isEmpty() ? null : (Map.Entry)((ImmutableCollection)((Object)this.entrySet())).asList().get(0);
    }

    @Override
    public Map.Entry<K, V> lastEntry() {
        return this.isEmpty() ? null : (Map.Entry)((ImmutableCollection)((Object)this.entrySet())).asList().get(this.size() - 1);
    }

    @Override
    @Deprecated
    public final Map.Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final Map.Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> result = this.descendingMap;
        if (result == null) {
            result = this.descendingMap = this.createDescendingMap();
        }
        return result;
    }

    abstract ImmutableSortedMap<K, V> createDescendingMap();

    @Override
    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet();
    }

    @Override
    public ImmutableSortedSet<K> descendingKeySet() {
        return ((ImmutableSortedSet)this.keySet()).descendingSet();
    }

    @Override
    Object writeReplace() {
        return new SerializedForm(this);
    }

    private static class SerializedForm
    extends ImmutableMap.SerializedForm {
        private final Comparator<Object> comparator;
        private static final long serialVersionUID = 0L;

        SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
            super(sortedMap);
            this.comparator = sortedMap.comparator();
        }

        @Override
        Object readResolve() {
            Builder<Object, Object> builder = new Builder<Object, Object>(this.comparator);
            return this.createMap(builder);
        }
    }

    public static class Builder<K, V>
    extends ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;

        public Builder(Comparator<? super K> comparator) {
            this.comparator = Preconditions.checkNotNull(comparator);
        }

        @Override
        public Builder<K, V> put(K key, V value) {
            super.put(key, value);
            return this;
        }

        @Override
        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        @Override
        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            super.putAll(map);
            return this;
        }

        @Override
        public ImmutableSortedMap<K, V> build() {
            return ImmutableSortedMap.fromEntries(this.comparator, false, this.size, this.entries);
        }
    }
}

