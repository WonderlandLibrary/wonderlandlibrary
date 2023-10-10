// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.stream.Collector;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

@GwtIncompatible
abstract class ImmutableSortedSetFauxverideShim<E> extends ImmutableSet<E>
{
    @Deprecated
    public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public static <E> ImmutableSortedSet.Builder<E> builder() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public static <E> ImmutableSortedSet<E> of(final E element) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3, final E e4) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public static <E> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E... remaining) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public static <E> ImmutableSortedSet<E> copyOf(final E[] elements) {
        throw new UnsupportedOperationException();
    }
}
