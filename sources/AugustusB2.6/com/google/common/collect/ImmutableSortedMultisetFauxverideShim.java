// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.ToIntFunction;
import java.util.function.Function;
import com.google.errorprone.annotations.DoNotCall;
import java.util.stream.Collector;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class ImmutableSortedMultisetFauxverideShim<E> extends ImmutableMultiset<E>
{
    @Deprecated
    @DoNotCall("Use toImmutableSortedMultiset.")
    public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Use toImmutableSortedMultiset.")
    public static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(final Function<? super T, ? extends E> elementFunction, final ToIntFunction<? super T> countFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Use naturalOrder.")
    public static <E> ImmutableSortedMultiset.Builder<E> builder() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
    public static <E> ImmutableSortedMultiset<E> of(final E element) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
    public static <E> ImmutableSortedMultiset<E> of(final E e1, final E e2) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
    public static <E> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
    public static <E> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3, final E e4) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
    public static <E> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
    public static <E> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E... remaining) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
    public static <E> ImmutableSortedMultiset<E> copyOf(final E[] elements) {
        throw new UnsupportedOperationException();
    }
}
