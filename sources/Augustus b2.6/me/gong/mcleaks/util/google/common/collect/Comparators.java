// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Iterator;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Comparator;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class Comparators
{
    private Comparators() {
    }
    
    public static <T, S extends T> Comparator<Iterable<S>> lexicographical(final Comparator<T> comparator) {
        return (Comparator<Iterable<S>>)new LexicographicalOrdering((Comparator<? super Object>)Preconditions.checkNotNull(comparator));
    }
    
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
}
