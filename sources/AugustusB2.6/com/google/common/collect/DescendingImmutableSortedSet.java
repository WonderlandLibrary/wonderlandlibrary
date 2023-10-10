// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NavigableSet;
import java.util.Iterator;
import javax.annotation.CheckForNull;
import java.util.Comparator;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class DescendingImmutableSortedSet<E> extends ImmutableSortedSet<E>
{
    private final ImmutableSortedSet<E> forward;
    
    DescendingImmutableSortedSet(final ImmutableSortedSet<E> forward) {
        super(Ordering.from(forward.comparator()).reverse());
        this.forward = forward;
    }
    
    @Override
    public boolean contains(@CheckForNull final Object object) {
        return this.forward.contains(object);
    }
    
    @Override
    public int size() {
        return this.forward.size();
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.forward.descendingIterator();
    }
    
    @Override
    ImmutableSortedSet<E> headSetImpl(final E toElement, final boolean inclusive) {
        return this.forward.tailSet(toElement, inclusive).descendingSet();
    }
    
    @Override
    ImmutableSortedSet<E> subSetImpl(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
    }
    
    @Override
    ImmutableSortedSet<E> tailSetImpl(final E fromElement, final boolean inclusive) {
        return this.forward.headSet(fromElement, inclusive).descendingSet();
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public ImmutableSortedSet<E> descendingSet() {
        return this.forward;
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public UnmodifiableIterator<E> descendingIterator() {
        return this.forward.iterator();
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    ImmutableSortedSet<E> createDescendingSet() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @CheckForNull
    @Override
    public E lower(final E element) {
        return this.forward.higher(element);
    }
    
    @CheckForNull
    @Override
    public E floor(final E element) {
        return this.forward.ceiling(element);
    }
    
    @CheckForNull
    @Override
    public E ceiling(final E element) {
        return this.forward.floor(element);
    }
    
    @CheckForNull
    @Override
    public E higher(final E element) {
        return this.forward.lower(element);
    }
    
    @Override
    int indexOf(@CheckForNull final Object target) {
        final int index = this.forward.indexOf(target);
        if (index == -1) {
            return index;
        }
        return this.size() - 1 - index;
    }
    
    @Override
    boolean isPartialView() {
        return this.forward.isPartialView();
    }
}
