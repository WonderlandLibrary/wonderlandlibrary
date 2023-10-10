// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collector;
import java.util.Iterator;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Comparators
{
    private Comparators() {
    }
    
    @Beta
    public static <T, S extends T> Comparator<Iterable<S>> lexicographical(final Comparator<T> comparator) {
        return (Comparator<Iterable<S>>)new LexicographicalOrdering((Comparator<? super Object>)Preconditions.checkNotNull(comparator));
    }
    
    @Beta
    public static <T> boolean isInOrder(final Iterable<? extends T> iterable, final Comparator<T> comparator) {
        Preconditions.checkNotNull(comparator);
        final Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T prev = (T)it.next();
            while (it.hasNext()) {
                final T next = (T)it.next();
                if (comparator.compare(prev, next) > 0) {
                    return false;
                }
                prev = next;
            }
        }
        return true;
    }
    
    @Beta
    public static <T> boolean isInStrictOrder(final Iterable<? extends T> iterable, final Comparator<T> comparator) {
        Preconditions.checkNotNull(comparator);
        final Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T prev = (T)it.next();
            while (it.hasNext()) {
                final T next = (T)it.next();
                if (comparator.compare(prev, next) >= 0) {
                    return false;
                }
                prev = next;
            }
        }
        return true;
    }
    
    public static <T> Collector<T, ?, List<T>> least(final int k, final Comparator<? super T> comparator) {
        CollectPreconditions.checkNonnegative(k, "k");
        Preconditions.checkNotNull(comparator);
        return Collector.of(() -> TopKSelector.least(k, (Comparator<? super Object>)comparator), TopKSelector::offer, TopKSelector::combine, TopKSelector::topK, Collector.Characteristics.UNORDERED);
    }
    
    public static <T> Collector<T, ?, List<T>> greatest(final int k, final Comparator<? super T> comparator) {
        return (Collector<T, ?, List<T>>)least(k, (Comparator<? super Object>)comparator.reversed());
    }
    
    @Beta
    public static <T> Comparator<Optional<T>> emptiesFirst(final Comparator<? super T> valueComparator) {
        Preconditions.checkNotNull(valueComparator);
        return Comparator.comparing(o -> o.orElse(null), Comparator.nullsFirst((Comparator<? super Object>)valueComparator));
    }
    
    @Beta
    public static <T> Comparator<Optional<T>> emptiesLast(final Comparator<? super T> valueComparator) {
        Preconditions.checkNotNull(valueComparator);
        return Comparator.comparing(o -> o.orElse(null), Comparator.nullsLast((Comparator<? super Object>)valueComparator));
    }
    
    @Beta
    public static <T extends Comparable<? super T>> T min(final T a, final T b) {
        return (a.compareTo((Object)b) <= 0) ? a : b;
    }
    
    @ParametricNullness
    @Beta
    public static <T> T min(@ParametricNullness final T a, @ParametricNullness final T b, final Comparator<T> comparator) {
        return (comparator.compare(a, b) <= 0) ? a : b;
    }
    
    @Beta
    public static <T extends Comparable<? super T>> T max(final T a, final T b) {
        return (a.compareTo((Object)b) >= 0) ? a : b;
    }
    
    @ParametricNullness
    @Beta
    public static <T> T max(@ParametricNullness final T a, @ParametricNullness final T b, final Comparator<T> comparator) {
        return (comparator.compare(a, b) >= 0) ? a : b;
    }
}
