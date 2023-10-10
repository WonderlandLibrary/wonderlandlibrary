// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.EnumMap;
import javax.annotation.CheckForNull;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.Collection;
import java.util.function.ToIntFunction;
import java.util.function.Function;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import com.google.common.annotations.GwtIncompatible;
import java.util.stream.Collector;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class CollectCollectors
{
    private static final Collector<Object, ?, ImmutableList<Object>> TO_IMMUTABLE_LIST;
    private static final Collector<Object, ?, ImmutableSet<Object>> TO_IMMUTABLE_SET;
    @GwtIncompatible
    private static final Collector<Range<Comparable<?>>, ?, ImmutableRangeSet<Comparable<?>>> TO_IMMUTABLE_RANGE_SET;
    
    static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return (Collector<E, ?, ImmutableList<E>>)CollectCollectors.TO_IMMUTABLE_LIST;
    }
    
    static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        return (Collector<E, ?, ImmutableSet<E>>)CollectCollectors.TO_IMMUTABLE_SET;
    }
    
    static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(final Comparator<? super E> comparator) {
        Preconditions.checkNotNull(comparator);
        return Collector.of(() -> new ImmutableSortedSet.Builder((Comparator<? super Object>)comparator), ImmutableSortedSet.Builder::add, ImmutableSortedSet.Builder::combine, ImmutableSortedSet.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
        return (Collector<E, ?, ImmutableSet<E>>)EnumSetAccumulator.TO_IMMUTABLE_ENUM_SET;
    }
    
    @GwtIncompatible
    static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
        return (Collector<Range<E>, ?, ImmutableRangeSet<E>>)CollectCollectors.TO_IMMUTABLE_RANGE_SET;
    }
    
    static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(final Function<? super T, ? extends E> elementFunction, final ToIntFunction<? super T> countFunction) {
        Preconditions.checkNotNull(elementFunction);
        Preconditions.checkNotNull(countFunction);
        return Collector.of((Supplier<?>)LinkedHashMultiset::create, (multiset, t) -> multiset.add(Preconditions.checkNotNull((Object)elementFunction.apply((Object)t)), countFunction.applyAsInt((Object)t)), (multiset1, multiset2) -> {
            multiset1.addAll(multiset2);
            return multiset1;
        }, multiset -> ImmutableMultiset.copyFromEntries((Collection<? extends Multiset.Entry<?>>)multiset.entrySet()), new Collector.Characteristics[0]);
    }
    
    static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(final Function<? super T, E> elementFunction, final ToIntFunction<? super T> countFunction, final Supplier<M> multisetSupplier) {
        Preconditions.checkNotNull(elementFunction);
        Preconditions.checkNotNull(countFunction);
        Preconditions.checkNotNull(multisetSupplier);
        return (Collector<T, ?, M>)Collector.of((Supplier<?>)multisetSupplier, (ms, t) -> ms.add(elementFunction.apply((Object)t), countFunction.applyAsInt((Object)t)), (ms1, ms2) -> {
            ms1.addAll(ms2);
            return ms1;
        }, new Collector.Characteristics[0]);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(ImmutableMap.Builder::new, (builder, input) -> builder.put(keyFunction.apply((Object)input), valueFunction.apply((Object)input)), ImmutableMap.Builder::combine, ImmutableMap.Builder::build, new Collector.Characteristics[0]);
    }
    
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        Preconditions.checkNotNull(mergeFunction);
        return Collectors.collectingAndThen((Collector<T, ?, Map>)Collectors.toMap((Function<? super T, ?>)keyFunction, valueFunction, mergeFunction, (Supplier<R>)LinkedHashMap::new), ImmutableMap::copyOf);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> comparator, final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(() -> new ImmutableSortedMap.Builder((Comparator<? super Object>)comparator), (builder, input) -> builder.put(keyFunction.apply((Object)input), valueFunction.apply((Object)input)), ImmutableSortedMap.Builder::combine, ImmutableSortedMap.Builder::build, Collector.Characteristics.UNORDERED);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> comparator, final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        Preconditions.checkNotNull(mergeFunction);
        return Collectors.collectingAndThen((Collector<T, ?, SortedMap>)Collectors.toMap((Function<? super T, ?>)keyFunction, valueFunction, mergeFunction, () -> new TreeMap((Comparator<? super Object>)comparator)), ImmutableSortedMap::copyOfSorted);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(ImmutableBiMap.Builder::new, (builder, input) -> builder.put(keyFunction.apply((Object)input), valueFunction.apply((Object)input)), ImmutableBiMap.Builder::combine, ImmutableBiMap.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        final Object o;
        final String s;
        final String s2;
        final Enum key;
        final Object newValue;
        return Collector.of(() -> new EnumMapAccumulator((v1, v2) -> {
            // new(java.lang.IllegalArgumentException.class)
            String.valueOf(v1);
            String.valueOf(v2);
            new IllegalArgumentException(new StringBuilder(27 + String.valueOf(s).length() + String.valueOf(s2).length()).append("Multiple values for key: ").append(s).append(", ").append(s2).toString());
            throw o;
        }), (accum, t) -> {
            key = (Enum)keyFunction.apply((Object)t);
            newValue = valueFunction.apply((Object)t);
            accum.put(Preconditions.checkNotNull(key, "Null key for input %s", t), Preconditions.checkNotNull(newValue, "Null value for input %s", t));
        }, EnumMapAccumulator::combine, EnumMapAccumulator::toImmutableMap, Collector.Characteristics.UNORDERED);
    }
    
    static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        Preconditions.checkNotNull(mergeFunction);
        final Enum key;
        final Object newValue;
        return Collector.of(() -> new EnumMapAccumulator((BinaryOperator<Object>)mergeFunction), (accum, t) -> {
            key = (Enum)keyFunction.apply((Object)t);
            newValue = valueFunction.apply((Object)t);
            accum.put(Preconditions.checkNotNull(key, "Null key for input %s", t), Preconditions.checkNotNull(newValue, "Null value for input %s", t));
        }, EnumMapAccumulator::combine, EnumMapAccumulator::toImmutableMap, new Collector.Characteristics[0]);
    }
    
    @GwtIncompatible
    static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(final Function<? super T, Range<K>> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(ImmutableRangeMap::builder, (builder, input) -> builder.put(keyFunction.apply((Object)input), valueFunction.apply((Object)input)), ImmutableRangeMap.Builder::combine, ImmutableRangeMap.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction, (Object)"keyFunction");
        Preconditions.checkNotNull(valueFunction, (Object)"valueFunction");
        return Collector.of(ImmutableListMultimap::builder, (builder, t) -> builder.put(keyFunction.apply((Object)t), valueFunction.apply((Object)t)), ImmutableListMultimap.Builder::combine, ImmutableListMultimap.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valuesFunction);
        final Function<Object, Object> keyFunction2 = input -> Preconditions.checkNotNull((Object)keyFunction.apply((Object)input));
        final Function<Object, Stream> valueFunction = input -> ((Stream)valuesFunction.apply((Object)input)).peek(Preconditions::checkNotNull);
        final MultimapBuilder.ListMultimapBuilder<Object, Object> arrayListValues = MultimapBuilder.linkedHashKeys().arrayListValues();
        Objects.requireNonNull(arrayListValues);
        return Collectors.collectingAndThen((Collector<T, ?, Multimap>)flatteningToMultimap((Function<? super Object, ?>)keyFunction2, (Function<? super Object, ? extends Stream<?>>)valueFunction, (Supplier<R>)arrayListValues::build), ImmutableListMultimap::copyOf);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction, (Object)"keyFunction");
        Preconditions.checkNotNull(valueFunction, (Object)"valueFunction");
        return Collector.of(ImmutableSetMultimap::builder, (builder, t) -> builder.put(keyFunction.apply((Object)t), valueFunction.apply((Object)t)), ImmutableSetMultimap.Builder::combine, ImmutableSetMultimap.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valuesFunction);
        final Function<Object, Object> keyFunction2 = input -> Preconditions.checkNotNull((Object)keyFunction.apply((Object)input));
        final Function<Object, Stream> valueFunction = input -> ((Stream)valuesFunction.apply((Object)input)).peek(Preconditions::checkNotNull);
        final MultimapBuilder.SetMultimapBuilder<Object, Object> linkedHashSetValues = MultimapBuilder.linkedHashKeys().linkedHashSetValues();
        Objects.requireNonNull(linkedHashSetValues);
        return Collectors.collectingAndThen((Collector<T, ?, Multimap>)flatteningToMultimap((Function<? super Object, ?>)keyFunction2, (Function<? super Object, ? extends Stream<?>>)valueFunction, (Supplier<R>)linkedHashSetValues::build), ImmutableSetMultimap::copyOf);
    }
    
    static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final Supplier<M> multimapSupplier) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        Preconditions.checkNotNull(multimapSupplier);
        return (Collector<T, ?, M>)Collector.of((Supplier<?>)multimapSupplier, (multimap, input) -> multimap.put(keyFunction.apply((Object)input), valueFunction.apply((Object)input)), (multimap1, multimap2) -> {
            multimap1.putAll(multimap2);
            return multimap1;
        }, new Collector.Characteristics[0]);
    }
    
    static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends Stream<? extends V>> valueFunction, final Supplier<M> multimapSupplier) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        Preconditions.checkNotNull(multimapSupplier);
        final Object key;
        final Collection<Object> valuesForKey;
        final Stream stream;
        final Collection<Object> obj;
        return (Collector<T, ?, M>)Collector.of((Supplier<?>)multimapSupplier, (multimap, input) -> {
            key = keyFunction.apply((Object)input);
            valuesForKey = multimap.get(key);
            stream = (Stream)valueFunction.apply((Object)input);
            Objects.requireNonNull(obj);
            stream.forEachOrdered(obj::add);
        }, (multimap1, multimap2) -> {
            multimap1.putAll(multimap2);
            return multimap1;
        }, new Collector.Characteristics[0]);
    }
    
    static {
        TO_IMMUTABLE_LIST = Collector.of(ImmutableList::builder, ImmutableList.Builder::add, ImmutableList.Builder::combine, ImmutableList.Builder::build, new Collector.Characteristics[0]);
        TO_IMMUTABLE_SET = Collector.of(ImmutableSet::builder, ImmutableSet.Builder::add, ImmutableSet.Builder::combine, ImmutableSet.Builder::build, new Collector.Characteristics[0]);
        TO_IMMUTABLE_RANGE_SET = Collector.of(ImmutableRangeSet::builder, ImmutableRangeSet.Builder::add, ImmutableRangeSet.Builder::combine, ImmutableRangeSet.Builder::build, new Collector.Characteristics[0]);
    }
    
    private static final class EnumSetAccumulator<E extends Enum<E>>
    {
        static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET;
        @CheckForNull
        private EnumSet<E> set;
        
        void add(final E e) {
            if (this.set == null) {
                this.set = EnumSet.of(e);
            }
            else {
                this.set.add(e);
            }
        }
        
        EnumSetAccumulator<E> combine(final EnumSetAccumulator<E> other) {
            if (this.set == null) {
                return other;
            }
            if (other.set == null) {
                return this;
            }
            this.set.addAll((Collection<?>)other.set);
            return this;
        }
        
        ImmutableSet<E> toImmutableSet() {
            return (this.set == null) ? ImmutableSet.of() : ImmutableEnumSet.asImmutable(this.set);
        }
        
        static {
            TO_IMMUTABLE_ENUM_SET = Collector.of(EnumSetAccumulator::new, EnumSetAccumulator::add, EnumSetAccumulator::combine, EnumSetAccumulator::toImmutableSet, Collector.Characteristics.UNORDERED);
        }
    }
    
    private static class EnumMapAccumulator<K extends Enum<K>, V>
    {
        private final BinaryOperator<V> mergeFunction;
        @CheckForNull
        private EnumMap<K, V> map;
        
        EnumMapAccumulator(final BinaryOperator<V> mergeFunction) {
            this.map = null;
            this.mergeFunction = mergeFunction;
        }
        
        void put(final K key, final V value) {
            if (this.map == null) {
                this.map = new EnumMap<K, V>(key.getDeclaringClass());
            }
            this.map.merge(key, value, (BiFunction<? super V, ? super V, ? extends V>)this.mergeFunction);
        }
        
        EnumMapAccumulator<K, V> combine(final EnumMapAccumulator<K, V> other) {
            if (this.map == null) {
                return other;
            }
            if (other.map == null) {
                return this;
            }
            other.map.forEach(this::put);
            return this;
        }
        
        ImmutableMap<K, V> toImmutableMap() {
            return (this.map == null) ? ImmutableMap.of() : ImmutableEnumMap.asImmutable(this.map);
        }
    }
}
