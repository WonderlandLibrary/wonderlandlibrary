/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.AbstractNavigableMap;
import com.google.common.collect.BiMap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingNavigableSet;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ForwardingSortedMap;
import com.google.common.collect.ForwardingSortedSet;
import com.google.common.collect.ImmutableEntry;
import com.google.common.collect.ImmutableEnumMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Ordering;
import com.google.common.collect.Platform;
import com.google.common.collect.Sets;
import com.google.common.collect.SortedMapDifference;
import com.google.common.collect.Synchronized;
import com.google.common.collect.TransformedIterator;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Maps {
    static final Joiner.MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");

    private Maps() {
    }

    static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
        return EntryFunction.KEY;
    }

    static <V> Function<Map.Entry<?, V>, V> valueFunction() {
        return EntryFunction.VALUE;
    }

    static <K, V> Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> entryIterator) {
        return Iterators.transform(entryIterator, Maps.<K>keyFunction());
    }

    static <K, V> Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> entryIterator) {
        return Iterators.transform(entryIterator, Maps.<V>valueFunction());
    }

    static <K, V> UnmodifiableIterator<V> valueIterator(final UnmodifiableIterator<Map.Entry<K, V>> entryIterator) {
        return new UnmodifiableIterator<V>(){

            @Override
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            @Override
            public V next() {
                return ((Map.Entry)entryIterator.next()).getValue();
            }
        };
    }

    @GwtCompatible(serializable=true)
    @Beta
    public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> map) {
        if (map instanceof ImmutableEnumMap) {
            ImmutableEnumMap result = (ImmutableEnumMap)map;
            return result;
        }
        if (map.isEmpty()) {
            return ImmutableMap.of();
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Preconditions.checkNotNull(entry.getKey());
            Preconditions.checkNotNull(entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(new EnumMap<K, V>(map));
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap();
    }

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return new HashMap(Maps.capacity(expectedSize));
    }

    static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
            return expectedSize + 1;
        }
        if (expectedSize < 0x40000000) {
            return expectedSize + expectedSize / 3;
        }
        return Integer.MAX_VALUE;
    }

    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap<K, V>(map);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
        return new LinkedHashMap<K, V>(map);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new MapMaker().makeMap();
    }

    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap();
    }

    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
        return new TreeMap<K, V>(map);
    }

    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) {
        return new TreeMap(comparator);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
        return new EnumMap(Preconditions.checkNotNull(type));
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
        return new EnumMap<K, V>(map);
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap();
    }

    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
        if (left instanceof SortedMap) {
            SortedMap sortedLeft = (SortedMap)left;
            SortedMapDifference<? extends K, ? extends V> result = Maps.difference(sortedLeft, right);
            return result;
        }
        return Maps.difference(left, right, Equivalence.equals());
    }

    @Beta
    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence) {
        Preconditions.checkNotNull(valueEquivalence);
        HashMap<K, V> onlyOnLeft = Maps.newHashMap();
        HashMap<? extends K, ? extends V> onlyOnRight = new HashMap<K, V>(right);
        HashMap<K, V> onBoth = Maps.newHashMap();
        HashMap<K, V> differences = Maps.newHashMap();
        Maps.doDifference(left, right, valueEquivalence, onlyOnLeft, onlyOnRight, onBoth, differences);
        return new MapDifferenceImpl<K, V>(onlyOnLeft, onlyOnRight, onBoth, differences);
    }

    private static <K, V> void doDifference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
        for (Map.Entry<K, V> entry : left.entrySet()) {
            K leftKey = entry.getKey();
            V leftValue = entry.getValue();
            if (right.containsKey(leftKey)) {
                V rightValue = onlyOnRight.remove(leftKey);
                if (valueEquivalence.equivalent(leftValue, rightValue)) {
                    onBoth.put(leftKey, leftValue);
                    continue;
                }
                differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
                continue;
            }
            onlyOnLeft.put(leftKey, leftValue);
        }
    }

    private static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        if (map instanceof SortedMap) {
            return Collections.unmodifiableSortedMap((SortedMap)map);
        }
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);
        Comparator<K> comparator = Maps.orNaturalOrder(left.comparator());
        TreeMap<K, V> onlyOnLeft = Maps.newTreeMap(comparator);
        TreeMap<? extends K, ? extends V> onlyOnRight = Maps.newTreeMap(comparator);
        onlyOnRight.putAll(right);
        TreeMap<K, V> onBoth = Maps.newTreeMap(comparator);
        TreeMap<K, V> differences = Maps.newTreeMap(comparator);
        Maps.doDifference(left, right, Equivalence.equals(), onlyOnLeft, onlyOnRight, onBoth, differences);
        return new SortedMapDifferenceImpl<K, V>(onlyOnLeft, onlyOnRight, onBoth, differences);
    }

    static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> comparator) {
        if (comparator != null) {
            return comparator;
        }
        return Ordering.natural();
    }

    @Beta
    public static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
        if (set instanceof SortedSet) {
            return Maps.asMap((SortedSet)set, function);
        }
        return new AsMapView<K, V>(set, function);
    }

    @Beta
    public static <K, V> SortedMap<K, V> asMap(SortedSet<K> set, Function<? super K, V> function) {
        return Platform.mapsAsMapSortedSet(set, function);
    }

    static <K, V> SortedMap<K, V> asMapSortedIgnoreNavigable(SortedSet<K> set, Function<? super K, V> function) {
        return new SortedAsMapView<K, V>(set, function);
    }

    @Beta
    @GwtIncompatible(value="NavigableMap")
    public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> set, Function<? super K, V> function) {
        return new NavigableAsMapView<K, V>(set, function);
    }

    static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(Set<K> set, final Function<? super K, V> function) {
        return new TransformedIterator<K, Map.Entry<K, V>>(set.iterator()){

            @Override
            Map.Entry<K, V> transform(K key) {
                return Maps.immutableEntry(key, function.apply(key));
            }
        };
    }

    private static <E> Set<E> removeOnlySet(final Set<E> set) {
        return new ForwardingSet<E>(){

            @Override
            protected Set<E> delegate() {
                return set;
            }

            @Override
            public boolean add(E element) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(Collection<? extends E> es) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
        return new ForwardingSortedSet<E>(){

            @Override
            protected SortedSet<E> delegate() {
                return set;
            }

            @Override
            public boolean add(E element) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(Collection<? extends E> es) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SortedSet<E> headSet(E toElement) {
                return Maps.removeOnlySortedSet(super.headSet(toElement));
            }

            @Override
            public SortedSet<E> subSet(E fromElement, E toElement) {
                return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
            }

            @Override
            public SortedSet<E> tailSet(E fromElement) {
                return Maps.removeOnlySortedSet(super.tailSet(fromElement));
            }
        };
    }

    @GwtIncompatible(value="NavigableSet")
    private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
        return new ForwardingNavigableSet<E>(){

            @Override
            protected NavigableSet<E> delegate() {
                return set;
            }

            @Override
            public boolean add(E element) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(Collection<? extends E> es) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SortedSet<E> headSet(E toElement) {
                return Maps.removeOnlySortedSet(super.headSet(toElement));
            }

            @Override
            public SortedSet<E> subSet(E fromElement, E toElement) {
                return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
            }

            @Override
            public SortedSet<E> tailSet(E fromElement) {
                return Maps.removeOnlySortedSet(super.tailSet(fromElement));
            }

            @Override
            public NavigableSet<E> headSet(E toElement, boolean inclusive) {
                return Maps.removeOnlyNavigableSet(super.headSet(toElement, inclusive));
            }

            @Override
            public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
                return Maps.removeOnlyNavigableSet(super.tailSet(fromElement, inclusive));
            }

            @Override
            public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
                return Maps.removeOnlyNavigableSet(super.subSet(fromElement, fromInclusive, toElement, toInclusive));
            }

            @Override
            public NavigableSet<E> descendingSet() {
                return Maps.removeOnlyNavigableSet(super.descendingSet());
            }
        };
    }

    @Beta
    public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) {
        return Maps.toMap(keys.iterator(), valueFunction);
    }

    @Beta
    public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
        Preconditions.checkNotNull(valueFunction);
        LinkedHashMap<K, V> builder = Maps.newLinkedHashMap();
        while (keys.hasNext()) {
            K key = keys.next();
            builder.put(key, valueFunction.apply(key));
        }
        return ImmutableMap.copyOf(builder);
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) {
        return Maps.uniqueIndex(values.iterator(), keyFunction);
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        while (values.hasNext()) {
            V value = values.next();
            builder.put(keyFunction.apply(value), value);
        }
        return builder.build();
    }

    @GwtIncompatible(value="java.util.Properties")
    public static ImmutableMap<String, String> fromProperties(Properties properties) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        Enumeration<?> e2 = properties.propertyNames();
        while (e2.hasMoreElements()) {
            String key = (String)e2.nextElement();
            builder.put(key, properties.getProperty(key));
        }
        return builder.build();
    }

    @GwtCompatible(serializable=true)
    public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable K key, @Nullable V value) {
        return new ImmutableEntry<K, V>(key, value);
    }

    static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> entrySet) {
        return new UnmodifiableEntrySet<K, V>(Collections.unmodifiableSet(entrySet));
    }

    static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V>(){

            @Override
            public K getKey() {
                return entry.getKey();
            }

            @Override
            public V getValue() {
                return entry.getValue();
            }
        };
    }

    @Beta
    public static <A, B> Converter<A, B> asConverter(BiMap<A, B> bimap) {
        return new BiMapConverter<A, B>(bimap);
    }

    public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
        return Synchronized.biMap(bimap, null);
    }

    public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) {
        return new UnmodifiableBiMap<K, V>(bimap, null);
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function) {
        return Maps.transformEntries(fromMap, Maps.asEntryTransformer(function));
    }

    public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> fromMap, Function<? super V1, V2> function) {
        return Maps.transformEntries(fromMap, Maps.asEntryTransformer(function));
    }

    @GwtIncompatible(value="NavigableMap")
    public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> fromMap, Function<? super V1, V2> function) {
        return Maps.transformEntries(fromMap, Maps.asEntryTransformer(function));
    }

    public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        if (fromMap instanceof SortedMap) {
            return Maps.transformEntries((SortedMap)fromMap, transformer);
        }
        return new TransformedEntriesMap<K, V1, V2>(fromMap, transformer);
    }

    public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        return Platform.mapsTransformEntriesSortedMap(fromMap, transformer);
    }

    @GwtIncompatible(value="NavigableMap")
    public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesNavigableMap<K, V1, V2>(fromMap, transformer);
    }

    static <K, V1, V2> SortedMap<K, V2> transformEntriesIgnoreNavigable(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesSortedMap<K, V1, V2>(fromMap, transformer);
    }

    static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return new EntryTransformer<K, V1, V2>(){

            @Override
            public V2 transformEntry(K key, V1 value) {
                return function.apply(value);
            }
        };
    }

    static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> transformer, final K key) {
        Preconditions.checkNotNull(transformer);
        return new Function<V1, V2>(){

            @Override
            public V2 apply(@Nullable V1 v1) {
                return transformer.transformEntry(key, v1);
            }
        };
    }

    static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
        Preconditions.checkNotNull(transformer);
        return new Function<Map.Entry<K, V1>, V2>(){

            @Override
            public V2 apply(Map.Entry<K, V1> entry) {
                return transformer.transformEntry(entry.getKey(), entry.getValue());
            }
        };
    }

    static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> transformer, final Map.Entry<K, V1> entry) {
        Preconditions.checkNotNull(transformer);
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V2>(){

            @Override
            public K getKey() {
                return entry.getKey();
            }

            @Override
            public V2 getValue() {
                return transformer.transformEntry(entry.getKey(), entry.getValue());
            }
        };
    }

    static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
        Preconditions.checkNotNull(transformer);
        return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>(){

            @Override
            public Map.Entry<K, V2> apply(Map.Entry<K, V1> entry) {
                return Maps.transformEntry(transformer, entry);
            }
        };
    }

    static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> keyPredicate) {
        return Predicates.compose(keyPredicate, Maps.<K>keyFunction());
    }

    static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> valuePredicate) {
        return Predicates.compose(valuePredicate, Maps.<V>valueFunction());
    }

    public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof SortedMap) {
            return Maps.filterKeys((SortedMap)unfiltered, keyPredicate);
        }
        if (unfiltered instanceof BiMap) {
            return Maps.filterKeys((BiMap)unfiltered, keyPredicate);
        }
        Preconditions.checkNotNull(keyPredicate);
        Predicate<Map.Entry<? super K, ?>> entryPredicate = Maps.keyPredicateOnEntries(keyPredicate);
        return unfiltered instanceof AbstractFilteredMap ? Maps.filterFiltered((AbstractFilteredMap)unfiltered, entryPredicate) : new FilteredKeyMap<K, V>(Preconditions.checkNotNull(unfiltered), keyPredicate, entryPredicate);
    }

    public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        return Maps.filterEntries(unfiltered, Maps.keyPredicateOnEntries(keyPredicate));
    }

    @GwtIncompatible(value="NavigableMap")
    public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        return Maps.filterEntries(unfiltered, Maps.keyPredicateOnEntries(keyPredicate));
    }

    public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        Preconditions.checkNotNull(keyPredicate);
        return Maps.filterEntries(unfiltered, Maps.keyPredicateOnEntries(keyPredicate));
    }

    public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        if (unfiltered instanceof SortedMap) {
            return Maps.filterValues((SortedMap)unfiltered, valuePredicate);
        }
        if (unfiltered instanceof BiMap) {
            return Maps.filterValues((BiMap)unfiltered, valuePredicate);
        }
        return Maps.filterEntries(unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }

    public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        return Maps.filterEntries(unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }

    @GwtIncompatible(value="NavigableMap")
    public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        return Maps.filterEntries(unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }

    public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        return Maps.filterEntries(unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }

    public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        if (unfiltered instanceof SortedMap) {
            return Maps.filterEntries((SortedMap)unfiltered, entryPredicate);
        }
        if (unfiltered instanceof BiMap) {
            return Maps.filterEntries((BiMap)unfiltered, entryPredicate);
        }
        Preconditions.checkNotNull(entryPredicate);
        return unfiltered instanceof AbstractFilteredMap ? Maps.filterFiltered((AbstractFilteredMap)unfiltered, entryPredicate) : new FilteredEntryMap<K, V>(Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        return Platform.mapsFilterSortedMap(unfiltered, entryPredicate);
    }

    static <K, V> SortedMap<K, V> filterSortedIgnoreNavigable(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        return unfiltered instanceof FilteredEntrySortedMap ? Maps.filterFiltered((FilteredEntrySortedMap)unfiltered, entryPredicate) : new FilteredEntrySortedMap<K, V>(Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    @GwtIncompatible(value="NavigableMap")
    public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        return unfiltered instanceof FilteredEntryNavigableMap ? Maps.filterFiltered((FilteredEntryNavigableMap)unfiltered, entryPredicate) : new FilteredEntryNavigableMap<K, V>(Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(entryPredicate);
        return unfiltered instanceof FilteredEntryBiMap ? Maps.filterFiltered((FilteredEntryBiMap)unfiltered, entryPredicate) : new FilteredEntryBiMap<K, V>(unfiltered, entryPredicate);
    }

    private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        return new FilteredEntryMap(map.unfiltered, Predicates.and(map.predicate, entryPredicate));
    }

    private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Predicate<? super Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
        return new FilteredEntrySortedMap<K, V>(map.sortedMap(), predicate);
    }

    @GwtIncompatible(value="NavigableMap")
    private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Predicate<? super Map.Entry<K, V>> predicate = Predicates.and(((FilteredEntryNavigableMap)map).entryPredicate, entryPredicate);
        return new FilteredEntryNavigableMap(((FilteredEntryNavigableMap)map).unfiltered, predicate);
    }

    private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Predicate<? super Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
        return new FilteredEntryBiMap<K, V>(map.unfiltered(), predicate);
    }

    @GwtIncompatible(value="NavigableMap")
    public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, V> map) {
        Preconditions.checkNotNull(map);
        if (map instanceof UnmodifiableNavigableMap) {
            return map;
        }
        return new UnmodifiableNavigableMap<K, V>(map);
    }

    @Nullable
    private static <K, V> Map.Entry<K, V> unmodifiableOrNull(@Nullable Map.Entry<K, V> entry) {
        return entry == null ? null : Maps.unmodifiableEntry(entry);
    }

    @GwtIncompatible(value="NavigableMap")
    public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
        return Synchronized.navigableMap(navigableMap);
    }

    static <V> V safeGet(Map<?, V> map, @Nullable Object key) {
        Preconditions.checkNotNull(map);
        try {
            return map.get(key);
        }
        catch (ClassCastException e2) {
            return null;
        }
        catch (NullPointerException e3) {
            return null;
        }
    }

    static boolean safeContainsKey(Map<?, ?> map, Object key) {
        Preconditions.checkNotNull(map);
        try {
            return map.containsKey(key);
        }
        catch (ClassCastException e2) {
            return false;
        }
        catch (NullPointerException e3) {
            return false;
        }
    }

    static <V> V safeRemove(Map<?, V> map, Object key) {
        Preconditions.checkNotNull(map);
        try {
            return map.remove(key);
        }
        catch (ClassCastException e2) {
            return null;
        }
        catch (NullPointerException e3) {
            return null;
        }
    }

    static boolean containsKeyImpl(Map<?, ?> map, @Nullable Object key) {
        return Iterators.contains(Maps.keyIterator(map.entrySet().iterator()), key);
    }

    static boolean containsValueImpl(Map<?, ?> map, @Nullable Object value) {
        return Iterators.contains(Maps.valueIterator(map.entrySet().iterator()), value);
    }

    static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> c2, Object o2) {
        if (!(o2 instanceof Map.Entry)) {
            return false;
        }
        return c2.contains(Maps.unmodifiableEntry((Map.Entry)o2));
    }

    static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> c2, Object o2) {
        if (!(o2 instanceof Map.Entry)) {
            return false;
        }
        return c2.remove(Maps.unmodifiableEntry((Map.Entry)o2));
    }

    static boolean equalsImpl(Map<?, ?> map, Object object) {
        if (map == object) {
            return true;
        }
        if (object instanceof Map) {
            Map o2 = (Map)object;
            return map.entrySet().equals(o2.entrySet());
        }
        return false;
    }

    static String toStringImpl(Map<?, ?> map) {
        StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
        STANDARD_JOINER.appendTo(sb, map);
        return sb.append('}').toString();
    }

    static <K, V> void putAllImpl(Map<K, V> self, Map<? extends K, ? extends V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            self.put(entry.getKey(), entry.getValue());
        }
    }

    @Nullable
    static <K> K keyOrNull(@Nullable Map.Entry<K, ?> entry) {
        return entry == null ? null : (K)entry.getKey();
    }

    @Nullable
    static <V> V valueOrNull(@Nullable Map.Entry<?, V> entry) {
        return entry == null ? null : (V)entry.getValue();
    }

    @GwtIncompatible(value="NavigableMap")
    static abstract class DescendingMap<K, V>
    extends ForwardingMap<K, V>
    implements NavigableMap<K, V> {
        private transient Comparator<? super K> comparator;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient NavigableSet<K> navigableKeySet;

        DescendingMap() {
        }

        abstract NavigableMap<K, V> forward();

        @Override
        protected final Map<K, V> delegate() {
            return this.forward();
        }

        @Override
        public Comparator<? super K> comparator() {
            Comparator<? super K> result = this.comparator;
            if (result == null) {
                Comparator forwardCmp = this.forward().comparator();
                if (forwardCmp == null) {
                    forwardCmp = Ordering.natural();
                }
                result = this.comparator = DescendingMap.reverse(forwardCmp);
            }
            return result;
        }

        private static <T> Ordering<T> reverse(Comparator<T> forward) {
            return Ordering.from(forward).reverse();
        }

        @Override
        public K firstKey() {
            return this.forward().lastKey();
        }

        @Override
        public K lastKey() {
            return this.forward().firstKey();
        }

        @Override
        public Map.Entry<K, V> lowerEntry(K key) {
            return this.forward().higherEntry(key);
        }

        @Override
        public K lowerKey(K key) {
            return this.forward().higherKey(key);
        }

        @Override
        public Map.Entry<K, V> floorEntry(K key) {
            return this.forward().ceilingEntry(key);
        }

        @Override
        public K floorKey(K key) {
            return this.forward().ceilingKey(key);
        }

        @Override
        public Map.Entry<K, V> ceilingEntry(K key) {
            return this.forward().floorEntry(key);
        }

        @Override
        public K ceilingKey(K key) {
            return this.forward().floorKey(key);
        }

        @Override
        public Map.Entry<K, V> higherEntry(K key) {
            return this.forward().lowerEntry(key);
        }

        @Override
        public K higherKey(K key) {
            return this.forward().lowerKey(key);
        }

        @Override
        public Map.Entry<K, V> firstEntry() {
            return this.forward().lastEntry();
        }

        @Override
        public Map.Entry<K, V> lastEntry() {
            return this.forward().firstEntry();
        }

        @Override
        public Map.Entry<K, V> pollFirstEntry() {
            return this.forward().pollLastEntry();
        }

        @Override
        public Map.Entry<K, V> pollLastEntry() {
            return this.forward().pollFirstEntry();
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            return this.forward();
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> result = this.entrySet;
            return result == null ? (this.entrySet = this.createEntrySet()) : result;
        }

        abstract Iterator<Map.Entry<K, V>> entryIterator();

        Set<Map.Entry<K, V>> createEntrySet() {
            return new EntrySet<K, V>(){

                @Override
                Map<K, V> map() {
                    return DescendingMap.this;
                }

                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return DescendingMap.this.entryIterator();
                }
            };
        }

        @Override
        public Set<K> keySet() {
            return this.navigableKeySet();
        }

        @Override
        public NavigableSet<K> navigableKeySet() {
            NavigableSet<K> result = this.navigableKeySet;
            return result == null ? (this.navigableKeySet = new NavigableKeySet(this)) : result;
        }

        @Override
        public NavigableSet<K> descendingKeySet() {
            return this.forward().navigableKeySet();
        }

        @Override
        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return this.forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
        }

        @Override
        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return this.forward().tailMap(toKey, inclusive).descendingMap();
        }

        @Override
        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return this.forward().headMap(fromKey, inclusive).descendingMap();
        }

        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }

        @Override
        public SortedMap<K, V> headMap(K toKey) {
            return this.headMap(toKey, false);
        }

        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            return this.tailMap(fromKey, true);
        }

        @Override
        public Collection<V> values() {
            return new Values(this);
        }

        @Override
        public String toString() {
            return this.standardToString();
        }
    }

    static abstract class EntrySet<K, V>
    extends Sets.ImprovedAbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        abstract Map<K, V> map();

        @Override
        public int size() {
            return this.map().size();
        }

        @Override
        public void clear() {
            this.map().clear();
        }

        @Override
        public boolean contains(Object o2) {
            if (o2 instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry)o2;
                Object key = entry.getKey();
                V value = Maps.safeGet(this.map(), key);
                return Objects.equal(value, entry.getValue()) && (value != null || this.map().containsKey(key));
            }
            return false;
        }

        @Override
        public boolean isEmpty() {
            return this.map().isEmpty();
        }

        @Override
        public boolean remove(Object o2) {
            if (this.contains(o2)) {
                Map.Entry entry = (Map.Entry)o2;
                return this.map().keySet().remove(entry.getKey());
            }
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c2) {
            try {
                return super.removeAll(Preconditions.checkNotNull(c2));
            }
            catch (UnsupportedOperationException e2) {
                return Sets.removeAllImpl(this, c2.iterator());
            }
        }

        @Override
        public boolean retainAll(Collection<?> c2) {
            try {
                return super.retainAll(Preconditions.checkNotNull(c2));
            }
            catch (UnsupportedOperationException e2) {
                HashSet keys = Sets.newHashSetWithExpectedSize(c2.size());
                for (Object o2 : c2) {
                    if (!this.contains(o2)) continue;
                    Map.Entry entry = (Map.Entry)o2;
                    keys.add(entry.getKey());
                }
                return this.map().keySet().retainAll(keys);
            }
        }
    }

    static class Values<K, V>
    extends AbstractCollection<V> {
        final Map<K, V> map;

        Values(Map<K, V> map) {
            this.map = Preconditions.checkNotNull(map);
        }

        final Map<K, V> map() {
            return this.map;
        }

        @Override
        public Iterator<V> iterator() {
            return Maps.valueIterator(this.map().entrySet().iterator());
        }

        @Override
        public boolean remove(Object o2) {
            try {
                return super.remove(o2);
            }
            catch (UnsupportedOperationException e2) {
                for (Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (!Objects.equal(o2, entry.getValue())) continue;
                    this.map().remove(entry.getKey());
                    return true;
                }
                return false;
            }
        }

        @Override
        public boolean removeAll(Collection<?> c2) {
            try {
                return super.removeAll(Preconditions.checkNotNull(c2));
            }
            catch (UnsupportedOperationException e2) {
                HashSet<K> toRemove = Sets.newHashSet();
                for (Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (!c2.contains(entry.getValue())) continue;
                    toRemove.add(entry.getKey());
                }
                return this.map().keySet().removeAll(toRemove);
            }
        }

        @Override
        public boolean retainAll(Collection<?> c2) {
            try {
                return super.retainAll(Preconditions.checkNotNull(c2));
            }
            catch (UnsupportedOperationException e2) {
                HashSet<K> toRetain = Sets.newHashSet();
                for (Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (!c2.contains(entry.getValue())) continue;
                    toRetain.add(entry.getKey());
                }
                return this.map().keySet().retainAll(toRetain);
            }
        }

        @Override
        public int size() {
            return this.map().size();
        }

        @Override
        public boolean isEmpty() {
            return this.map().isEmpty();
        }

        @Override
        public boolean contains(@Nullable Object o2) {
            return this.map().containsValue(o2);
        }

        @Override
        public void clear() {
            this.map().clear();
        }
    }

    @GwtIncompatible(value="NavigableMap")
    static class NavigableKeySet<K, V>
    extends SortedKeySet<K, V>
    implements NavigableSet<K> {
        NavigableKeySet(NavigableMap<K, V> map) {
            super(map);
        }

        @Override
        NavigableMap<K, V> map() {
            return (NavigableMap)this.map;
        }

        @Override
        public K lower(K e2) {
            return this.map().lowerKey(e2);
        }

        @Override
        public K floor(K e2) {
            return this.map().floorKey(e2);
        }

        @Override
        public K ceiling(K e2) {
            return this.map().ceilingKey(e2);
        }

        @Override
        public K higher(K e2) {
            return this.map().higherKey(e2);
        }

        @Override
        public K pollFirst() {
            return Maps.keyOrNull(this.map().pollFirstEntry());
        }

        @Override
        public K pollLast() {
            return Maps.keyOrNull(this.map().pollLastEntry());
        }

        @Override
        public NavigableSet<K> descendingSet() {
            return this.map().descendingKeySet();
        }

        @Override
        public Iterator<K> descendingIterator() {
            return this.descendingSet().iterator();
        }

        @Override
        public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
            return this.map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
        }

        @Override
        public NavigableSet<K> headSet(K toElement, boolean inclusive) {
            return this.map().headMap(toElement, inclusive).navigableKeySet();
        }

        @Override
        public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
            return this.map().tailMap(fromElement, inclusive).navigableKeySet();
        }

        @Override
        public SortedSet<K> subSet(K fromElement, K toElement) {
            return this.subSet(fromElement, true, toElement, false);
        }

        @Override
        public SortedSet<K> headSet(K toElement) {
            return this.headSet(toElement, false);
        }

        @Override
        public SortedSet<K> tailSet(K fromElement) {
            return this.tailSet(fromElement, true);
        }
    }

    static class SortedKeySet<K, V>
    extends KeySet<K, V>
    implements SortedSet<K> {
        SortedKeySet(SortedMap<K, V> map) {
            super(map);
        }

        @Override
        SortedMap<K, V> map() {
            return (SortedMap)super.map();
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.map().comparator();
        }

        @Override
        public SortedSet<K> subSet(K fromElement, K toElement) {
            return new SortedKeySet(this.map().subMap(fromElement, toElement));
        }

        @Override
        public SortedSet<K> headSet(K toElement) {
            return new SortedKeySet(this.map().headMap(toElement));
        }

        @Override
        public SortedSet<K> tailSet(K fromElement) {
            return new SortedKeySet(this.map().tailMap(fromElement));
        }

        @Override
        public K first() {
            return this.map().firstKey();
        }

        @Override
        public K last() {
            return this.map().lastKey();
        }
    }

    static class KeySet<K, V>
    extends Sets.ImprovedAbstractSet<K> {
        final Map<K, V> map;

        KeySet(Map<K, V> map) {
            this.map = Preconditions.checkNotNull(map);
        }

        Map<K, V> map() {
            return this.map;
        }

        @Override
        public Iterator<K> iterator() {
            return Maps.keyIterator(this.map().entrySet().iterator());
        }

        @Override
        public int size() {
            return this.map().size();
        }

        @Override
        public boolean isEmpty() {
            return this.map().isEmpty();
        }

        @Override
        public boolean contains(Object o2) {
            return this.map().containsKey(o2);
        }

        @Override
        public boolean remove(Object o2) {
            if (this.contains(o2)) {
                this.map().remove(o2);
                return true;
            }
            return false;
        }

        @Override
        public void clear() {
            this.map().clear();
        }
    }

    @GwtCompatible
    static abstract class ImprovedAbstractMap<K, V>
    extends AbstractMap<K, V> {
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Set<K> keySet;
        private transient Collection<V> values;

        ImprovedAbstractMap() {
        }

        abstract Set<Map.Entry<K, V>> createEntrySet();

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> result = this.entrySet;
            return result == null ? (this.entrySet = this.createEntrySet()) : result;
        }

        @Override
        public Set<K> keySet() {
            Set<K> result = this.keySet;
            return result == null ? (this.keySet = this.createKeySet()) : result;
        }

        Set<K> createKeySet() {
            return new KeySet(this);
        }

        @Override
        public Collection<V> values() {
            Collection<V> result = this.values;
            return result == null ? (this.values = this.createValues()) : result;
        }

        Collection<V> createValues() {
            return new Values(this);
        }
    }

    @GwtIncompatible(value="NavigableMap")
    static class UnmodifiableNavigableMap<K, V>
    extends ForwardingSortedMap<K, V>
    implements NavigableMap<K, V>,
    Serializable {
        private final NavigableMap<K, V> delegate;
        private transient UnmodifiableNavigableMap<K, V> descendingMap;

        UnmodifiableNavigableMap(NavigableMap<K, V> delegate) {
            this.delegate = delegate;
        }

        UnmodifiableNavigableMap(NavigableMap<K, V> delegate, UnmodifiableNavigableMap<K, V> descendingMap) {
            this.delegate = delegate;
            this.descendingMap = descendingMap;
        }

        @Override
        protected SortedMap<K, V> delegate() {
            return Collections.unmodifiableSortedMap(this.delegate);
        }

        @Override
        public Map.Entry<K, V> lowerEntry(K key) {
            return Maps.unmodifiableOrNull(this.delegate.lowerEntry(key));
        }

        @Override
        public K lowerKey(K key) {
            return this.delegate.lowerKey(key);
        }

        @Override
        public Map.Entry<K, V> floorEntry(K key) {
            return Maps.unmodifiableOrNull(this.delegate.floorEntry(key));
        }

        @Override
        public K floorKey(K key) {
            return this.delegate.floorKey(key);
        }

        @Override
        public Map.Entry<K, V> ceilingEntry(K key) {
            return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(key));
        }

        @Override
        public K ceilingKey(K key) {
            return this.delegate.ceilingKey(key);
        }

        @Override
        public Map.Entry<K, V> higherEntry(K key) {
            return Maps.unmodifiableOrNull(this.delegate.higherEntry(key));
        }

        @Override
        public K higherKey(K key) {
            return this.delegate.higherKey(key);
        }

        @Override
        public Map.Entry<K, V> firstEntry() {
            return Maps.unmodifiableOrNull(this.delegate.firstEntry());
        }

        @Override
        public Map.Entry<K, V> lastEntry() {
            return Maps.unmodifiableOrNull(this.delegate.lastEntry());
        }

        @Override
        public final Map.Entry<K, V> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public final Map.Entry<K, V> pollLastEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            UnmodifiableNavigableMap<K, V> result = this.descendingMap;
            return result == null ? (this.descendingMap = new UnmodifiableNavigableMap<K, V>(this.delegate.descendingMap(), this)) : result;
        }

        @Override
        public Set<K> keySet() {
            return this.navigableKeySet();
        }

        @Override
        public NavigableSet<K> navigableKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
        }

        @Override
        public NavigableSet<K> descendingKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
        }

        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }

        @Override
        public SortedMap<K, V> headMap(K toKey) {
            return this.headMap(toKey, false);
        }

        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            return this.tailMap(fromKey, true);
        }

        @Override
        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return Maps.unmodifiableNavigableMap(this.delegate.subMap(fromKey, fromInclusive, toKey, toInclusive));
        }

        @Override
        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return Maps.unmodifiableNavigableMap(this.delegate.headMap(toKey, inclusive));
        }

        @Override
        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return Maps.unmodifiableNavigableMap(this.delegate.tailMap(fromKey, inclusive));
        }
    }

    static final class FilteredEntryBiMap<K, V>
    extends FilteredEntryMap<K, V>
    implements BiMap<K, V> {
        private final BiMap<V, K> inverse;

        private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> forwardPredicate) {
            return new Predicate<Map.Entry<V, K>>(){

                @Override
                public boolean apply(Map.Entry<V, K> input) {
                    return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
                }
            };
        }

        FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate) {
            super(delegate, predicate);
            this.inverse = new FilteredEntryBiMap<K, V>(delegate.inverse(), FilteredEntryBiMap.inversePredicate(predicate), this);
        }

        private FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate, BiMap<V, K> inverse) {
            super(delegate, predicate);
            this.inverse = inverse;
        }

        BiMap<K, V> unfiltered() {
            return (BiMap)this.unfiltered;
        }

        @Override
        public V forcePut(@Nullable K key, @Nullable V value) {
            Preconditions.checkArgument(this.apply(key, value));
            return this.unfiltered().forcePut(key, value);
        }

        @Override
        public BiMap<V, K> inverse() {
            return this.inverse;
        }

        @Override
        public Set<V> values() {
            return this.inverse.keySet();
        }
    }

    @GwtIncompatible(value="NavigableMap")
    private static class FilteredEntryNavigableMap<K, V>
    extends AbstractNavigableMap<K, V> {
        private final NavigableMap<K, V> unfiltered;
        private final Predicate<? super Map.Entry<K, V>> entryPredicate;
        private final Map<K, V> filteredDelegate;

        FilteredEntryNavigableMap(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
            this.unfiltered = Preconditions.checkNotNull(unfiltered);
            this.entryPredicate = entryPredicate;
            this.filteredDelegate = new FilteredEntryMap<K, V>(unfiltered, entryPredicate);
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.unfiltered.comparator();
        }

        @Override
        public NavigableSet<K> navigableKeySet() {
            return new NavigableKeySet<K, V>(this){

                @Override
                public boolean removeAll(Collection<?> c2) {
                    return Iterators.removeIf(FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.in(c2))));
                }

                @Override
                public boolean retainAll(Collection<?> c2) {
                    return Iterators.removeIf(FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c2)))));
                }
            };
        }

        @Override
        public Collection<V> values() {
            return new FilteredMapValues<K, V>(this, this.unfiltered, this.entryPredicate);
        }

        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
        }

        @Override
        Iterator<Map.Entry<K, V>> descendingEntryIterator() {
            return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
        }

        @Override
        public int size() {
            return this.filteredDelegate.size();
        }

        @Override
        @Nullable
        public V get(@Nullable Object key) {
            return this.filteredDelegate.get(key);
        }

        @Override
        public boolean containsKey(@Nullable Object key) {
            return this.filteredDelegate.containsKey(key);
        }

        @Override
        public V put(K key, V value) {
            return this.filteredDelegate.put(key, value);
        }

        @Override
        public V remove(@Nullable Object key) {
            return this.filteredDelegate.remove(key);
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m2) {
            this.filteredDelegate.putAll(m2);
        }

        @Override
        public void clear() {
            this.filteredDelegate.clear();
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return this.filteredDelegate.entrySet();
        }

        @Override
        public Map.Entry<K, V> pollFirstEntry() {
            return Iterables.removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
        }

        @Override
        public Map.Entry<K, V> pollLastEntry() {
            return Iterables.removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
        }

        @Override
        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return Maps.filterEntries(this.unfiltered.subMap(fromKey, fromInclusive, toKey, toInclusive), this.entryPredicate);
        }

        @Override
        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return Maps.filterEntries(this.unfiltered.headMap(toKey, inclusive), this.entryPredicate);
        }

        @Override
        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return Maps.filterEntries(this.unfiltered.tailMap(fromKey, inclusive), this.entryPredicate);
        }
    }

    private static class FilteredEntrySortedMap<K, V>
    extends FilteredEntryMap<K, V>
    implements SortedMap<K, V> {
        FilteredEntrySortedMap(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
        }

        SortedMap<K, V> sortedMap() {
            return (SortedMap)this.unfiltered;
        }

        @Override
        public SortedSet<K> keySet() {
            return (SortedSet)super.keySet();
        }

        @Override
        SortedSet<K> createKeySet() {
            return new SortedKeySet();
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }

        @Override
        public K firstKey() {
            return (K)this.keySet().iterator().next();
        }

        @Override
        public K lastKey() {
            SortedMap<K, V> headMap = this.sortedMap();
            K key;
            while (!this.apply(key = headMap.lastKey(), this.unfiltered.get(key))) {
                headMap = this.sortedMap().headMap(key);
            }
            return key;
        }

        @Override
        public SortedMap<K, V> headMap(K toKey) {
            return new FilteredEntrySortedMap<K, V>(this.sortedMap().headMap(toKey), this.predicate);
        }

        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return new FilteredEntrySortedMap<K, V>(this.sortedMap().subMap(fromKey, toKey), this.predicate);
        }

        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            return new FilteredEntrySortedMap<K, V>(this.sortedMap().tailMap(fromKey), this.predicate);
        }

        class SortedKeySet
        extends FilteredEntryMap.KeySet
        implements SortedSet<K> {
            SortedKeySet() {
            }

            @Override
            public Comparator<? super K> comparator() {
                return FilteredEntrySortedMap.this.sortedMap().comparator();
            }

            @Override
            public SortedSet<K> subSet(K fromElement, K toElement) {
                return (SortedSet)FilteredEntrySortedMap.this.subMap(fromElement, toElement).keySet();
            }

            @Override
            public SortedSet<K> headSet(K toElement) {
                return (SortedSet)FilteredEntrySortedMap.this.headMap(toElement).keySet();
            }

            @Override
            public SortedSet<K> tailSet(K fromElement) {
                return (SortedSet)FilteredEntrySortedMap.this.tailMap(fromElement).keySet();
            }

            @Override
            public K first() {
                return FilteredEntrySortedMap.this.firstKey();
            }

            @Override
            public K last() {
                return FilteredEntrySortedMap.this.lastKey();
            }
        }
    }

    static class FilteredEntryMap<K, V>
    extends AbstractFilteredMap<K, V> {
        final Set<Map.Entry<K, V>> filteredEntrySet;

        FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
        }

        @Override
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return new EntrySet();
        }

        @Override
        Set<K> createKeySet() {
            return new KeySet();
        }

        class KeySet
        extends com.google.common.collect.Maps$KeySet<K, V> {
            KeySet() {
                super(FilteredEntryMap.this);
            }

            @Override
            public boolean remove(Object o2) {
                if (FilteredEntryMap.this.containsKey(o2)) {
                    FilteredEntryMap.this.unfiltered.remove(o2);
                    return true;
                }
                return false;
            }

            @Override
            private boolean removeIf(Predicate<? super K> keyPredicate) {
                return Iterables.removeIf(FilteredEntryMap.this.unfiltered.entrySet(), Predicates.and(FilteredEntryMap.this.predicate, Maps.keyPredicateOnEntries(keyPredicate)));
            }

            @Override
            public boolean removeAll(Collection<?> c2) {
                return this.removeIf(Predicates.in(c2));
            }

            @Override
            public boolean retainAll(Collection<?> c2) {
                return this.removeIf(Predicates.not(Predicates.in(c2)));
            }

            @Override
            public Object[] toArray() {
                return Lists.newArrayList(this.iterator()).toArray();
            }

            @Override
            public <T> T[] toArray(T[] array) {
                return Lists.newArrayList(this.iterator()).toArray(array);
            }
        }

        private class EntrySet
        extends ForwardingSet<Map.Entry<K, V>> {
            private EntrySet() {
            }

            @Override
            protected Set<Map.Entry<K, V>> delegate() {
                return FilteredEntryMap.this.filteredEntrySet;
            }

            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new TransformedIterator<Map.Entry<K, V>, Map.Entry<K, V>>(FilteredEntryMap.this.filteredEntrySet.iterator()){

                    @Override
                    Map.Entry<K, V> transform(final Map.Entry<K, V> entry) {
                        return new ForwardingMapEntry<K, V>(){

                            @Override
                            protected Map.Entry<K, V> delegate() {
                                return entry;
                            }

                            @Override
                            public V setValue(V newValue) {
                                Preconditions.checkArgument(FilteredEntryMap.this.apply(this.getKey(), newValue));
                                return super.setValue(newValue);
                            }
                        };
                    }
                };
            }
        }
    }

    private static class FilteredKeyMap<K, V>
    extends AbstractFilteredMap<K, V> {
        Predicate<? super K> keyPredicate;

        FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.keyPredicate = keyPredicate;
        }

        @Override
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return Sets.filter(this.unfiltered.entrySet(), this.predicate);
        }

        @Override
        Set<K> createKeySet() {
            return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
        }

        @Override
        public boolean containsKey(Object key) {
            return this.unfiltered.containsKey(key) && this.keyPredicate.apply(key);
        }
    }

    private static final class FilteredMapValues<K, V>
    extends Values<K, V> {
        Map<K, V> unfiltered;
        Predicate<? super Map.Entry<K, V>> predicate;

        FilteredMapValues(Map<K, V> filteredMap, Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
            super(filteredMap);
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }

        @Override
        public boolean remove(Object o2) {
            return Iterables.removeFirstMatching(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(Predicates.equalTo(o2)))) != null;
        }

        @Override
        private boolean removeIf(Predicate<? super V> valuePredicate) {
            return Iterables.removeIf(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(valuePredicate)));
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return this.removeIf(Predicates.in(collection));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return this.removeIf(Predicates.not(Predicates.in(collection)));
        }

        @Override
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }

        @Override
        public <T> T[] toArray(T[] array) {
            return Lists.newArrayList(this.iterator()).toArray(array);
        }
    }

    private static abstract class AbstractFilteredMap<K, V>
    extends ImprovedAbstractMap<K, V> {
        final Map<K, V> unfiltered;
        final Predicate<? super Map.Entry<K, V>> predicate;

        AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }

        boolean apply(@Nullable Object key, @Nullable V value) {
            Object k2 = key;
            return this.predicate.apply(Maps.immutableEntry(k2, value));
        }

        @Override
        public V put(K key, V value) {
            Preconditions.checkArgument(this.apply(key, value));
            return this.unfiltered.put(key, value);
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> map) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                Preconditions.checkArgument(this.apply(entry.getKey(), entry.getValue()));
            }
            this.unfiltered.putAll(map);
        }

        @Override
        public boolean containsKey(Object key) {
            return this.unfiltered.containsKey(key) && this.apply(key, this.unfiltered.get(key));
        }

        @Override
        public V get(Object key) {
            V value = this.unfiltered.get(key);
            return value != null && this.apply(key, value) ? (V)value : null;
        }

        @Override
        public boolean isEmpty() {
            return this.entrySet().isEmpty();
        }

        @Override
        public V remove(Object key) {
            return this.containsKey(key) ? (V)this.unfiltered.remove(key) : null;
        }

        @Override
        Collection<V> createValues() {
            return new FilteredMapValues<K, V>(this, this.unfiltered, this.predicate);
        }
    }

    @GwtIncompatible(value="NavigableMap")
    private static class TransformedEntriesNavigableMap<K, V1, V2>
    extends TransformedEntriesSortedMap<K, V1, V2>
    implements NavigableMap<K, V2> {
        TransformedEntriesNavigableMap(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMap, transformer);
        }

        @Override
        public Map.Entry<K, V2> ceilingEntry(K key) {
            return this.transformEntry(this.fromMap().ceilingEntry(key));
        }

        @Override
        public K ceilingKey(K key) {
            return this.fromMap().ceilingKey(key);
        }

        @Override
        public NavigableSet<K> descendingKeySet() {
            return this.fromMap().descendingKeySet();
        }

        @Override
        public NavigableMap<K, V2> descendingMap() {
            return Maps.transformEntries(this.fromMap().descendingMap(), this.transformer);
        }

        @Override
        public Map.Entry<K, V2> firstEntry() {
            return this.transformEntry(this.fromMap().firstEntry());
        }

        @Override
        public Map.Entry<K, V2> floorEntry(K key) {
            return this.transformEntry(this.fromMap().floorEntry(key));
        }

        @Override
        public K floorKey(K key) {
            return this.fromMap().floorKey(key);
        }

        @Override
        public NavigableMap<K, V2> headMap(K toKey) {
            return this.headMap(toKey, false);
        }

        @Override
        public NavigableMap<K, V2> headMap(K toKey, boolean inclusive) {
            return Maps.transformEntries(this.fromMap().headMap(toKey, inclusive), this.transformer);
        }

        @Override
        public Map.Entry<K, V2> higherEntry(K key) {
            return this.transformEntry(this.fromMap().higherEntry(key));
        }

        @Override
        public K higherKey(K key) {
            return this.fromMap().higherKey(key);
        }

        @Override
        public Map.Entry<K, V2> lastEntry() {
            return this.transformEntry(this.fromMap().lastEntry());
        }

        @Override
        public Map.Entry<K, V2> lowerEntry(K key) {
            return this.transformEntry(this.fromMap().lowerEntry(key));
        }

        @Override
        public K lowerKey(K key) {
            return this.fromMap().lowerKey(key);
        }

        @Override
        public NavigableSet<K> navigableKeySet() {
            return this.fromMap().navigableKeySet();
        }

        @Override
        public Map.Entry<K, V2> pollFirstEntry() {
            return this.transformEntry(this.fromMap().pollFirstEntry());
        }

        @Override
        public Map.Entry<K, V2> pollLastEntry() {
            return this.transformEntry(this.fromMap().pollLastEntry());
        }

        @Override
        public NavigableMap<K, V2> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return Maps.transformEntries(this.fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
        }

        @Override
        public NavigableMap<K, V2> subMap(K fromKey, K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }

        @Override
        public NavigableMap<K, V2> tailMap(K fromKey) {
            return this.tailMap(fromKey, true);
        }

        @Override
        public NavigableMap<K, V2> tailMap(K fromKey, boolean inclusive) {
            return Maps.transformEntries(this.fromMap().tailMap(fromKey, inclusive), this.transformer);
        }

        @Nullable
        private Map.Entry<K, V2> transformEntry(@Nullable Map.Entry<K, V1> entry) {
            return entry == null ? null : Maps.transformEntry(this.transformer, entry);
        }

        @Override
        protected NavigableMap<K, V1> fromMap() {
            return (NavigableMap)super.fromMap();
        }
    }

    static class TransformedEntriesSortedMap<K, V1, V2>
    extends TransformedEntriesMap<K, V1, V2>
    implements SortedMap<K, V2> {
        protected SortedMap<K, V1> fromMap() {
            return (SortedMap)this.fromMap;
        }

        TransformedEntriesSortedMap(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMap, transformer);
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.fromMap().comparator();
        }

        @Override
        public K firstKey() {
            return this.fromMap().firstKey();
        }

        @Override
        public SortedMap<K, V2> headMap(K toKey) {
            return Maps.transformEntries(this.fromMap().headMap(toKey), this.transformer);
        }

        @Override
        public K lastKey() {
            return this.fromMap().lastKey();
        }

        @Override
        public SortedMap<K, V2> subMap(K fromKey, K toKey) {
            return Maps.transformEntries(this.fromMap().subMap(fromKey, toKey), this.transformer);
        }

        @Override
        public SortedMap<K, V2> tailMap(K fromKey) {
            return Maps.transformEntries(this.fromMap().tailMap(fromKey), this.transformer);
        }
    }

    static class TransformedEntriesMap<K, V1, V2>
    extends ImprovedAbstractMap<K, V2> {
        final Map<K, V1> fromMap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;

        TransformedEntriesMap(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMap = Preconditions.checkNotNull(fromMap);
            this.transformer = Preconditions.checkNotNull(transformer);
        }

        @Override
        public int size() {
            return this.fromMap.size();
        }

        @Override
        public boolean containsKey(Object key) {
            return this.fromMap.containsKey(key);
        }

        @Override
        public V2 get(Object key) {
            V1 value = this.fromMap.get(key);
            return (V2)(value != null || this.fromMap.containsKey(key) ? this.transformer.transformEntry(key, value) : null);
        }

        @Override
        public V2 remove(Object key) {
            return this.fromMap.containsKey(key) ? (V2)this.transformer.transformEntry((K)key, (V1)this.fromMap.remove(key)) : null;
        }

        @Override
        public void clear() {
            this.fromMap.clear();
        }

        @Override
        public Set<K> keySet() {
            return this.fromMap.keySet();
        }

        @Override
        protected Set<Map.Entry<K, V2>> createEntrySet() {
            return new EntrySet<K, V2>(){

                @Override
                Map<K, V2> map() {
                    return TransformedEntriesMap.this;
                }

                @Override
                public Iterator<Map.Entry<K, V2>> iterator() {
                    return Iterators.transform(TransformedEntriesMap.this.fromMap.entrySet().iterator(), Maps.asEntryToEntryFunction(TransformedEntriesMap.this.transformer));
                }
            };
        }
    }

    public static interface EntryTransformer<K, V1, V2> {
        public V2 transformEntry(@Nullable K var1, @Nullable V1 var2);
    }

    private static class UnmodifiableBiMap<K, V>
    extends ForwardingMap<K, V>
    implements BiMap<K, V>,
    Serializable {
        final Map<K, V> unmodifiableMap;
        final BiMap<? extends K, ? extends V> delegate;
        BiMap<V, K> inverse;
        transient Set<V> values;
        private static final long serialVersionUID = 0L;

        UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, @Nullable BiMap<V, K> inverse) {
            this.unmodifiableMap = Collections.unmodifiableMap(delegate);
            this.delegate = delegate;
            this.inverse = inverse;
        }

        @Override
        protected Map<K, V> delegate() {
            return this.unmodifiableMap;
        }

        @Override
        public V forcePut(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BiMap<V, K> inverse() {
            BiMap<K, V> result = this.inverse;
            return result == null ? (this.inverse = new UnmodifiableBiMap<V, K>(this.delegate.inverse(), this)) : result;
        }

        @Override
        public Set<V> values() {
            Set<V> result = this.values;
            return result == null ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
        }
    }

    private static final class BiMapConverter<A, B>
    extends Converter<A, B>
    implements Serializable {
        private final BiMap<A, B> bimap;
        private static final long serialVersionUID = 0L;

        BiMapConverter(BiMap<A, B> bimap) {
            this.bimap = Preconditions.checkNotNull(bimap);
        }

        @Override
        protected B doForward(A a2) {
            return BiMapConverter.convert(this.bimap, a2);
        }

        @Override
        protected A doBackward(B b2) {
            return BiMapConverter.convert(this.bimap.inverse(), b2);
        }

        private static <X, Y> Y convert(BiMap<X, Y> bimap, X input) {
            Object output = bimap.get(input);
            Preconditions.checkArgument(output != null, "No non-null mapping present for input: %s", input);
            return (Y)output;
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (object instanceof BiMapConverter) {
                BiMapConverter that = (BiMapConverter)object;
                return this.bimap.equals(that.bimap);
            }
            return false;
        }

        public int hashCode() {
            return this.bimap.hashCode();
        }

        public String toString() {
            return "Maps.asConverter(" + this.bimap + ")";
        }
    }

    static class UnmodifiableEntrySet<K, V>
    extends UnmodifiableEntries<K, V>
    implements Set<Map.Entry<K, V>> {
        UnmodifiableEntrySet(Set<Map.Entry<K, V>> entries) {
            super(entries);
        }

        @Override
        public boolean equals(@Nullable Object object) {
            return Sets.equalsImpl(this, object);
        }

        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    static class UnmodifiableEntries<K, V>
    extends ForwardingCollection<Map.Entry<K, V>> {
        private final Collection<Map.Entry<K, V>> entries;

        UnmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
            this.entries = entries;
        }

        @Override
        protected Collection<Map.Entry<K, V>> delegate() {
            return this.entries;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            final Iterator delegate = super.iterator();
            return new UnmodifiableIterator<Map.Entry<K, V>>(){

                @Override
                public boolean hasNext() {
                    return delegate.hasNext();
                }

                @Override
                public Map.Entry<K, V> next() {
                    return Maps.unmodifiableEntry((Map.Entry)delegate.next());
                }
            };
        }

        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }

        @Override
        public <T> T[] toArray(T[] array) {
            return this.standardToArray(array);
        }
    }

    @GwtIncompatible(value="NavigableMap")
    private static final class NavigableAsMapView<K, V>
    extends AbstractNavigableMap<K, V> {
        private final NavigableSet<K> set;
        private final Function<? super K, V> function;

        NavigableAsMapView(NavigableSet<K> ks, Function<? super K, V> vFunction) {
            this.set = Preconditions.checkNotNull(ks);
            this.function = Preconditions.checkNotNull(vFunction);
        }

        @Override
        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return Maps.asMap(this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
        }

        @Override
        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return Maps.asMap(this.set.headSet(toKey, inclusive), this.function);
        }

        @Override
        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return Maps.asMap(this.set.tailSet(fromKey, inclusive), this.function);
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.set.comparator();
        }

        @Override
        @Nullable
        public V get(@Nullable Object key) {
            if (Collections2.safeContains(this.set, key)) {
                Object k2 = key;
                return this.function.apply(k2);
            }
            return null;
        }

        @Override
        public void clear() {
            this.set.clear();
        }

        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return Maps.asMapEntryIterator(this.set, this.function);
        }

        @Override
        Iterator<Map.Entry<K, V>> descendingEntryIterator() {
            return this.descendingMap().entrySet().iterator();
        }

        @Override
        public NavigableSet<K> navigableKeySet() {
            return Maps.removeOnlyNavigableSet(this.set);
        }

        @Override
        public int size() {
            return this.set.size();
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            return Maps.asMap(this.set.descendingSet(), this.function);
        }
    }

    private static class SortedAsMapView<K, V>
    extends AsMapView<K, V>
    implements SortedMap<K, V> {
        SortedAsMapView(SortedSet<K> set, Function<? super K, V> function) {
            super(set, function);
        }

        @Override
        SortedSet<K> backingSet() {
            return (SortedSet)super.backingSet();
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.backingSet().comparator();
        }

        @Override
        public Set<K> keySet() {
            return Maps.removeOnlySortedSet((SortedSet)this.backingSet());
        }

        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return Maps.asMap(this.backingSet().subSet(fromKey, toKey), this.function);
        }

        @Override
        public SortedMap<K, V> headMap(K toKey) {
            return Maps.asMap(this.backingSet().headSet(toKey), this.function);
        }

        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            return Maps.asMap(this.backingSet().tailSet(fromKey), this.function);
        }

        @Override
        public K firstKey() {
            return (K)this.backingSet().first();
        }

        @Override
        public K lastKey() {
            return (K)this.backingSet().last();
        }
    }

    private static class AsMapView<K, V>
    extends ImprovedAbstractMap<K, V> {
        private final Set<K> set;
        final Function<? super K, V> function;

        Set<K> backingSet() {
            return this.set;
        }

        AsMapView(Set<K> set, Function<? super K, V> function) {
            this.set = Preconditions.checkNotNull(set);
            this.function = Preconditions.checkNotNull(function);
        }

        @Override
        public Set<K> createKeySet() {
            return Maps.removeOnlySet(this.backingSet());
        }

        @Override
        Collection<V> createValues() {
            return Collections2.transform(this.set, this.function);
        }

        @Override
        public int size() {
            return this.backingSet().size();
        }

        @Override
        public boolean containsKey(@Nullable Object key) {
            return this.backingSet().contains(key);
        }

        @Override
        public V get(@Nullable Object key) {
            if (Collections2.safeContains(this.backingSet(), key)) {
                Object k2 = key;
                return this.function.apply(k2);
            }
            return null;
        }

        @Override
        public V remove(@Nullable Object key) {
            if (this.backingSet().remove(key)) {
                Object k2 = key;
                return this.function.apply(k2);
            }
            return null;
        }

        @Override
        public void clear() {
            this.backingSet().clear();
        }

        @Override
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return new EntrySet<K, V>(){

                @Override
                Map<K, V> map() {
                    return AsMapView.this;
                }

                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return Maps.asMapEntryIterator(AsMapView.this.backingSet(), AsMapView.this.function);
                }
            };
        }
    }

    static class SortedMapDifferenceImpl<K, V>
    extends MapDifferenceImpl<K, V>
    implements SortedMapDifference<K, V> {
        SortedMapDifferenceImpl(SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, MapDifference.ValueDifference<V>> differences) {
            super(onlyOnLeft, onlyOnRight, onBoth, differences);
        }

        @Override
        public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
            return (SortedMap)super.entriesDiffering();
        }

        @Override
        public SortedMap<K, V> entriesInCommon() {
            return (SortedMap)super.entriesInCommon();
        }

        @Override
        public SortedMap<K, V> entriesOnlyOnLeft() {
            return (SortedMap)super.entriesOnlyOnLeft();
        }

        @Override
        public SortedMap<K, V> entriesOnlyOnRight() {
            return (SortedMap)super.entriesOnlyOnRight();
        }
    }

    static class ValueDifferenceImpl<V>
    implements MapDifference.ValueDifference<V> {
        private final V left;
        private final V right;

        static <V> MapDifference.ValueDifference<V> create(@Nullable V left, @Nullable V right) {
            return new ValueDifferenceImpl<V>(left, right);
        }

        private ValueDifferenceImpl(@Nullable V left, @Nullable V right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public V leftValue() {
            return this.left;
        }

        @Override
        public V rightValue() {
            return this.right;
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (object instanceof MapDifference.ValueDifference) {
                MapDifference.ValueDifference that = (MapDifference.ValueDifference)object;
                return Objects.equal(this.left, that.leftValue()) && Objects.equal(this.right, that.rightValue());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.left, this.right);
        }

        public String toString() {
            return "(" + this.left + ", " + this.right + ")";
        }
    }

    static class MapDifferenceImpl<K, V>
    implements MapDifference<K, V> {
        final Map<K, V> onlyOnLeft;
        final Map<K, V> onlyOnRight;
        final Map<K, V> onBoth;
        final Map<K, MapDifference.ValueDifference<V>> differences;

        MapDifferenceImpl(Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
            this.onlyOnLeft = Maps.unmodifiableMap(onlyOnLeft);
            this.onlyOnRight = Maps.unmodifiableMap(onlyOnRight);
            this.onBoth = Maps.unmodifiableMap(onBoth);
            this.differences = Maps.unmodifiableMap(differences);
        }

        @Override
        public boolean areEqual() {
            return this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty();
        }

        @Override
        public Map<K, V> entriesOnlyOnLeft() {
            return this.onlyOnLeft;
        }

        @Override
        public Map<K, V> entriesOnlyOnRight() {
            return this.onlyOnRight;
        }

        @Override
        public Map<K, V> entriesInCommon() {
            return this.onBoth;
        }

        @Override
        public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
            return this.differences;
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof MapDifference) {
                MapDifference other = (MapDifference)object;
                return this.entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && this.entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && this.entriesInCommon().equals(other.entriesInCommon()) && this.entriesDiffering().equals(other.entriesDiffering());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.entriesOnlyOnLeft(), this.entriesOnlyOnRight(), this.entriesInCommon(), this.entriesDiffering());
        }

        public String toString() {
            if (this.areEqual()) {
                return "equal";
            }
            StringBuilder result = new StringBuilder("not equal");
            if (!this.onlyOnLeft.isEmpty()) {
                result.append(": only on left=").append(this.onlyOnLeft);
            }
            if (!this.onlyOnRight.isEmpty()) {
                result.append(": only on right=").append(this.onlyOnRight);
            }
            if (!this.differences.isEmpty()) {
                result.append(": value differences=").append(this.differences);
            }
            return result.toString();
        }
    }

    private static enum EntryFunction implements Function<Map.Entry<?, ?>, Object>
    {
        KEY{

            @Override
            @Nullable
            public Object apply(Map.Entry<?, ?> entry) {
                return entry.getKey();
            }
        }
        ,
        VALUE{

            @Override
            @Nullable
            public Object apply(Map.Entry<?, ?> entry) {
                return entry.getValue();
            }
        };

    }
}

