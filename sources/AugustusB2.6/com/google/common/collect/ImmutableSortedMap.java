// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NavigableSet;
import java.util.Collection;
import java.util.Set;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.AbstractMap;
import java.util.function.Consumer;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.Arrays;
import java.util.Objects;
import java.util.SortedMap;
import com.google.common.annotations.Beta;
import java.util.Map;
import com.google.common.base.Preconditions;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.function.Function;
import javax.annotation.CheckForNull;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.util.NavigableMap;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public final class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V>
{
    private static final Comparator<Comparable> NATURAL_ORDER;
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP;
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;
    @CheckForNull
    private transient ImmutableSortedMap<K, V> descendingMap;
    private static final long serialVersionUID = 0L;
    
    public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> comparator, final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction);
    }
    
    public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> comparator, final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction, mergeFunction);
    }
    
    static <K, V> ImmutableSortedMap<K, V> emptyMap(final Comparator<? super K> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return of();
        }
        return new ImmutableSortedMap<K, V>(ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
    }
    
    public static <K, V> ImmutableSortedMap<K, V> of() {
        return (ImmutableSortedMap<K, V>)ImmutableSortedMap.NATURAL_EMPTY_MAP;
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1) {
        return of(Ordering.natural(), k1, v1);
    }
    
    private static <K, V> ImmutableSortedMap<K, V> of(final Comparator<? super K> comparator, final K k1, final V v1) {
        return new ImmutableSortedMap<K, V>(new RegularImmutableSortedSet<K>(ImmutableList.of(k1), Preconditions.checkNotNull(comparator)), ImmutableList.of(v1));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {
        return fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7) {
        return fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6), ImmutableMap.entryOf(k7, v7));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {
        return fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6), ImmutableMap.entryOf(k7, v7), ImmutableMap.entryOf(k8, v8));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9) {
        return fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6), ImmutableMap.entryOf(k7, v7), ImmutableMap.entryOf(k8, v8), ImmutableMap.entryOf(k9, v9));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10) {
        return fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6), ImmutableMap.entryOf(k7, v7), ImmutableMap.entryOf(k8, v8), ImmutableMap.entryOf(k9, v9), ImmutableMap.entryOf(k10, v10));
    }
    
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        final Ordering<K> naturalOrder = (Ordering<K>)(Ordering)ImmutableSortedMap.NATURAL_ORDER;
        return copyOfInternal(map, (Comparator<? super K>)naturalOrder);
    }
    
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Map<? extends K, ? extends V> map, final Comparator<? super K> comparator) {
        return (ImmutableSortedMap<K, V>)copyOfInternal((Map<?, ?>)map, (Comparator<? super Object>)Preconditions.checkNotNull(comparator));
    }
    
    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        final Ordering<K> naturalOrder = (Ordering<K>)(Ordering)ImmutableSortedMap.NATURAL_ORDER;
        return copyOf(entries, (Comparator<? super K>)naturalOrder);
    }
    
    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, final Comparator<? super K> comparator) {
        return fromEntries((Comparator<? super K>)Preconditions.checkNotNull((Comparator<? super K>)comparator), false, entries);
    }
    
    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(final SortedMap<K, ? extends V> map) {
        Comparator<? super K> comparator = map.comparator();
        if (comparator == null) {
            comparator = (Comparator<? super K>)ImmutableSortedMap.NATURAL_ORDER;
        }
        if (map instanceof ImmutableSortedMap) {
            final ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap<K, V>)(ImmutableSortedMap)map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        return fromEntries(comparator, true, (Iterable<? extends Map.Entry<? extends K, ? extends V>>)map.entrySet());
    }
    
    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(final Map<? extends K, ? extends V> map, final Comparator<? super K> comparator) {
        boolean sameComparator = false;
        if (map instanceof SortedMap) {
            final SortedMap<?, ?> sortedMap = (SortedMap<?, ?>)(SortedMap)map;
            final Comparator<?> comparator2 = sortedMap.comparator();
            sameComparator = ((comparator2 == null) ? (comparator == ImmutableSortedMap.NATURAL_ORDER) : comparator.equals(comparator2));
        }
        if (sameComparator && map instanceof ImmutableSortedMap) {
            final ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap<K, V>)(ImmutableSortedMap)map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        return fromEntries(comparator, sameComparator, (Iterable<? extends Map.Entry<? extends K, ? extends V>>)map.entrySet());
    }
    
    private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> fromEntries(final Map.Entry<K, V>... entries) {
        return fromEntries(Ordering.natural(), false, entries, entries.length);
    }
    
    private static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> comparator, final boolean sameComparator, final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        final Map.Entry<K, V>[] entryArray = Iterables.toArray(entries, ImmutableSortedMap.EMPTY_ENTRY_ARRAY);
        return fromEntries(comparator, sameComparator, entryArray, entryArray.length);
    }
    
    private static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> comparator, final boolean sameComparator, final Map.Entry<K, V>[] entryArray, final int size) {
        switch (size) {
            case 0: {
                return emptyMap(comparator);
            }
            case 1: {
                final Map.Entry<K, V> onlyEntry = Objects.requireNonNull(entryArray[0]);
                return of(comparator, onlyEntry.getKey(), onlyEntry.getValue());
            }
            default: {
                final Object[] keys = new Object[size];
                final Object[] values = new Object[size];
                if (sameComparator) {
                    for (int i = 0; i < size; ++i) {
                        final Map.Entry<K, V> entry = Objects.requireNonNull(entryArray[i]);
                        final Object key = entry.getKey();
                        final Object value = entry.getValue();
                        CollectPreconditions.checkEntryNotNull(key, value);
                        keys[i] = key;
                        values[i] = value;
                    }
                }
                else {
                    Arrays.sort(entryArray, 0, size, new Comparator<Map.Entry<K, V>>() {
                        @Override
                        public int compare(@CheckForNull final Map.Entry<K, V> e1, @CheckForNull final Map.Entry<K, V> e2) {
                            Objects.requireNonNull(e1);
                            Objects.requireNonNull(e2);
                            return comparator.compare(e1.getKey(), e2.getKey());
                        }
                    });
                    final Map.Entry<K, V> firstEntry = Objects.requireNonNull(entryArray[0]);
                    K prevKey = firstEntry.getKey();
                    keys[0] = prevKey;
                    values[0] = firstEntry.getValue();
                    CollectPreconditions.checkEntryNotNull(keys[0], values[0]);
                    for (int j = 1; j < size; ++j) {
                        final Map.Entry<K, V> prevEntry = Objects.requireNonNull(entryArray[j - 1]);
                        final Map.Entry<K, V> entry2 = Objects.requireNonNull(entryArray[j]);
                        final K key2 = entry2.getKey();
                        final V value2 = entry2.getValue();
                        CollectPreconditions.checkEntryNotNull(key2, value2);
                        keys[j] = key2;
                        values[j] = value2;
                        ImmutableMap.checkNoConflict(comparator.compare((Object)prevKey, (Object)key2) != 0, "key", prevEntry, entry2);
                        prevKey = key2;
                    }
                }
                return new ImmutableSortedMap<K, V>(new RegularImmutableSortedSet<K>(new RegularImmutableList<K>(keys), comparator), new RegularImmutableList<V>(values));
            }
        }
    }
    
    public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
        return new Builder<K, V>(Ordering.natural());
    }
    
    public static <K, V> Builder<K, V> orderedBy(final Comparator<K> comparator) {
        return new Builder<K, V>(comparator);
    }
    
    public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
        return new Builder<K, V>(Ordering.natural().reverse());
    }
    
    ImmutableSortedMap(final RegularImmutableSortedSet<K> keySet, final ImmutableList<V> valueList) {
        this(keySet, valueList, null);
    }
    
    ImmutableSortedMap(final RegularImmutableSortedSet<K> keySet, final ImmutableList<V> valueList, @CheckForNull final ImmutableSortedMap<K, V> descendingMap) {
        this.keySet = keySet;
        this.valueList = valueList;
        this.descendingMap = descendingMap;
    }
    
    @Override
    public int size() {
        return this.valueList.size();
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        final ImmutableList<K> keyList = (ImmutableList<K>)this.keySet.asList();
        for (int i = 0; i < this.size(); ++i) {
            action.accept(keyList.get(i), this.valueList.get(i));
        }
    }
    
    @CheckForNull
    @Override
    public V get(@CheckForNull final Object key) {
        final int index = this.keySet.indexOf(key);
        return (index == -1) ? null : this.valueList.get(index);
    }
    
    @Override
    boolean isPartialView() {
        return this.keySet.isPartialView() || this.valueList.isPartialView();
    }
    
    @Override
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return super.entrySet();
    }
    
    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        class EntrySet extends ImmutableMapEntrySet<K, V>
        {
            @Override
            public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                return (UnmodifiableIterator<Map.Entry<K, V>>)this.asList().iterator();
            }
            
            @Override
            public Spliterator<Map.Entry<K, V>> spliterator() {
                return (Spliterator<Map.Entry<K, V>>)this.asList().spliterator();
            }
            
            @Override
            public void forEach(final Consumer<? super Map.Entry<K, V>> action) {
                this.asList().forEach((Consumer<? super Map.Entry<K, V>>)action);
            }
            
            @Override
            ImmutableList<Map.Entry<K, V>> createAsList() {
                return new ImmutableAsList<Map.Entry<K, V>>() {
                    @Override
                    public Map.Entry<K, V> get(final int index) {
                        return new AbstractMap.SimpleImmutableEntry<K, V>((K)ImmutableSortedMap.this.keySet.asList().get(index), (V)ImmutableSortedMap.this.valueList.get(index));
                    }
                    
                    @Override
                    public Spliterator<Map.Entry<K, V>> spliterator() {
                        return CollectSpliterators.indexed(this.size(), 1297, this::get);
                    }
                    
                    @Override
                    ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
                        return (ImmutableCollection<Map.Entry<K, V>>)EntrySet.this;
                    }
                };
            }
            
            @Override
            ImmutableMap<K, V> map() {
                return (ImmutableMap<K, V>)ImmutableSortedMap.this;
            }
        }
        return (ImmutableSet<Map.Entry<K, V>>)(this.isEmpty() ? ImmutableSet.of() : new EntrySet());
    }
    
    @Override
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }
    
    @Override
    ImmutableSet<K> createKeySet() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableCollection<V> values() {
        return this.valueList;
    }
    
    @Override
    ImmutableCollection<V> createValues() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public Comparator<? super K> comparator() {
        return this.keySet().comparator();
    }
    
    @Override
    public K firstKey() {
        return this.keySet().first();
    }
    
    @Override
    public K lastKey() {
        return this.keySet().last();
    }
    
    private ImmutableSortedMap<K, V> getSubMap(final int fromIndex, final int toIndex) {
        if (fromIndex == 0 && toIndex == this.size()) {
            return this;
        }
        if (fromIndex == toIndex) {
            return emptyMap(this.comparator());
        }
        return new ImmutableSortedMap<K, V>(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
    }
    
    @Override
    public ImmutableSortedMap<K, V> headMap(final K toKey) {
        return this.headMap(toKey, false);
    }
    
    @Override
    public ImmutableSortedMap<K, V> headMap(final K toKey, final boolean inclusive) {
        return this.getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(toKey), inclusive));
    }
    
    @Override
    public ImmutableSortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return this.subMap(fromKey, true, toKey, false);
    }
    
    @Override
    public ImmutableSortedMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
        Preconditions.checkNotNull(fromKey);
        Preconditions.checkNotNull(toKey);
        Preconditions.checkArgument(this.comparator().compare((Object)fromKey, (Object)toKey) <= 0, "expected fromKey <= toKey but %s > %s", fromKey, toKey);
        return this.headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
    }
    
    @Override
    public ImmutableSortedMap<K, V> tailMap(final K fromKey) {
        return this.tailMap(fromKey, true);
    }
    
    @Override
    public ImmutableSortedMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
        return this.getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(fromKey), inclusive), this.size());
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> lowerEntry(final K key) {
        return this.headMap(key, false).lastEntry();
    }
    
    @CheckForNull
    @Override
    public K lowerKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.lowerEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> floorEntry(final K key) {
        return this.headMap(key, true).lastEntry();
    }
    
    @CheckForNull
    @Override
    public K floorKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.floorEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> ceilingEntry(final K key) {
        return this.tailMap(key, true).firstEntry();
    }
    
    @CheckForNull
    @Override
    public K ceilingKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.ceilingEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> higherEntry(final K key) {
        return this.tailMap(key, false).firstEntry();
    }
    
    @CheckForNull
    @Override
    public K higherKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.higherEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> firstEntry() {
        return this.isEmpty() ? null : this.entrySet().asList().get(0);
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> lastEntry() {
        return this.isEmpty() ? null : this.entrySet().asList().get(this.size() - 1);
    }
    
    @Deprecated
    @CheckForNull
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final Map.Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CheckForNull
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final Map.Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> result = this.descendingMap;
        if (result != null) {
            return result;
        }
        if (this.isEmpty()) {
            return result = emptyMap((Comparator<? super K>)Ordering.from(this.comparator()).reverse());
        }
        return result = new ImmutableSortedMap<K, V>((RegularImmutableSortedSet)this.keySet.descendingSet(), this.valueList.reverse(), this);
    }
    
    @Override
    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet;
    }
    
    @Override
    public ImmutableSortedSet<K> descendingKeySet() {
        return this.keySet.descendingSet();
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm((ImmutableSortedMap<Object, Object>)this);
    }
    
    static {
        NATURAL_ORDER = Ordering.natural();
        NATURAL_EMPTY_MAP = new ImmutableSortedMap<Comparable, Object>((RegularImmutableSortedSet<Comparable>)ImmutableSortedSet.emptySet((Comparator<? super Comparable>)Ordering.natural()), ImmutableList.of());
    }
    
    public static class Builder<K, V> extends ImmutableMap.Builder<K, V>
    {
        private final Comparator<? super K> comparator;
        
        public Builder(final Comparator<? super K> comparator) {
            this.comparator = Preconditions.checkNotNull(comparator);
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> put(final K key, final V value) {
            super.put(key, value);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> put(final Map.Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            super.putAll(map);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Beta
        @Override
        public Builder<K, V> putAll(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            super.putAll(entries);
            return this;
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        @Beta
        @DoNotCall("Always throws UnsupportedOperationException")
        @Override
        public final Builder<K, V> orderEntriesByValue(final Comparator<? super V> valueComparator) {
            throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
        }
        
        @Override
        Builder<K, V> combine(final ImmutableMap.Builder<K, V> other) {
            super.combine(other);
            return this;
        }
        
        @Override
        public ImmutableSortedMap<K, V> build() {
            return this.buildOrThrow();
        }
        
        @Override
        public ImmutableSortedMap<K, V> buildOrThrow() {
            switch (this.size) {
                case 0: {
                    return ImmutableSortedMap.emptyMap(this.comparator);
                }
                case 1: {
                    final Map.Entry<K, V> onlyEntry = Objects.requireNonNull(this.entries[0]);
                    return (ImmutableSortedMap<K, V>)of(this.comparator, onlyEntry.getKey(), onlyEntry.getValue());
                }
                default: {
                    return (ImmutableSortedMap<K, V>)fromEntries(this.comparator, false, (Map.Entry<Object, Object>[])this.entries, this.size);
                }
            }
        }
    }
    
    private static class SerializedForm<K, V> extends ImmutableMap.SerializedForm<K, V>
    {
        private final Comparator<? super K> comparator;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableSortedMap<K, V> sortedMap) {
            super(sortedMap);
            this.comparator = sortedMap.comparator();
        }
        
        @Override
        ImmutableSortedMap.Builder<K, V> makeBuilder(final int size) {
            return new ImmutableSortedMap.Builder<K, V>(this.comparator);
        }
    }
}
