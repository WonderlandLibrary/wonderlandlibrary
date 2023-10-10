// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.BinaryOperator;
import com.google.errorprone.annotations.DoNotCall;
import java.util.stream.Collector;
import java.util.function.Function;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class ImmutableBiMapFauxverideShim<K, V> extends ImmutableMap<K, V>
{
    @Deprecated
    @DoNotCall("Use toImmutableBiMap")
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Use toImmutableBiMap")
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        throw new UnsupportedOperationException();
    }
}
