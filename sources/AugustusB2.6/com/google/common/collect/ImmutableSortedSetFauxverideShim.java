// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.errorprone.annotations.DoNotCall;
import java.util.stream.Collector;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class ImmutableSortedSetFauxverideShim<E> extends CachingAsList<E>
{
    @Deprecated
    @DoNotCall("Use toImmutableSortedSet")
    public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Use naturalOrder")
    public static <E> ImmutableSortedSet.Builder<E> builder() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Use naturalOrder (which does not accept an expected size)")
    public static <E> ImmutableSortedSet.Builder<E> builderWithExpectedSize(final int expectedSize) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass a parameter of type Comparable")
    public static <E> ImmutableSortedSet<E> of(final E element) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass parameters of type Comparable")
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass parameters of type Comparable")
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass parameters of type Comparable")
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3, final E e4) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass parameters of type Comparable")
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass parameters of type Comparable")
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E... remaining) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Pass parameters of type Comparable")
    public static <E> ImmutableSortedSet<E> copyOf(final E[] elements) {
        throw new UnsupportedOperationException();
    }
}
