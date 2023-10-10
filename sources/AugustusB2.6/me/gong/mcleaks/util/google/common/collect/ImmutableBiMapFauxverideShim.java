// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.function.Function;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

@GwtIncompatible
abstract class ImmutableBiMapFauxverideShim<K, V> extends ImmutableMap<K, V>
{
    @Deprecated
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        throw new UnsupportedOperationException();
    }
}
