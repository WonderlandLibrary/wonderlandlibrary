// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Map;
import java.util.function.BinaryOperator;
import com.google.errorprone.annotations.DoNotCall;
import java.util.stream.Collector;
import java.util.function.Function;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class ImmutableSortedMapFauxverideShim<K, V> extends ImmutableMap<K, V>
{
    @Deprecated
    @DoNotCall("Use toImmutableSortedMap")
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Use toImmutableSortedMap")
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Use naturalOrder")
    public static <K, V> ImmutableSortedMap.Builder<K, V> builder() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Use naturalOrder (which does not accept an expected size)")
    public static <K, V> ImmutableSortedMap.Builder<K, V> builderWithExpectedSize(final int expectedSize) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass a key of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass keys of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass keys of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass keys of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass keys of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass keys of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass keys of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass keys of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass keys of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass keys of type Comparable")
    public static <K, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("ImmutableSortedMap.ofEntries not currently available; use ImmutableSortedMap.copyOf")
    public static <K, V> ImmutableSortedMap<K, V> ofEntries(final Map.Entry<? extends K, ? extends V>... entries) {
        throw new UnsupportedOperationException();
    }
}
