// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Comparator;
import java.util.function.Supplier;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.stream.Collector;
import java.util.function.Function;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
final class CollectCollectors
{
    static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(ImmutableBiMap.Builder::new, (builder, input) -> builder.put(keyFunction.apply((Object)input), valueFunction.apply((Object)input)), ImmutableBiMap.Builder::combine, ImmutableBiMap.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return Collector.of(ImmutableList::builder, ImmutableList.Builder::add, ImmutableList.Builder::combine, ImmutableList.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(ImmutableMap.Builder::new, (builder, input) -> builder.put(keyFunction.apply((Object)input), valueFunction.apply((Object)input)), ImmutableMap.Builder::combine, ImmutableMap.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        return Collector.of(ImmutableSet::builder, ImmutableSet.Builder::add, ImmutableSet.Builder::combine, ImmutableSet.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> comparator, final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(() -> new ImmutableSortedMap.Builder((Comparator<? super Object>)comparator), (builder, input) -> builder.put(keyFunction.apply((Object)input), valueFunction.apply((Object)input)), ImmutableSortedMap.Builder::combine, ImmutableSortedMap.Builder::build, Collector.Characteristics.UNORDERED);
    }
    
    static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(final Comparator<? super E> comparator) {
        Preconditions.checkNotNull(comparator);
        return Collector.of(() -> new ImmutableSortedSet.Builder((Comparator<? super Object>)comparator), ImmutableSortedSet.Builder::add, ImmutableSortedSet.Builder::combine, ImmutableSortedSet.Builder::build, new Collector.Characteristics[0]);
    }
}
