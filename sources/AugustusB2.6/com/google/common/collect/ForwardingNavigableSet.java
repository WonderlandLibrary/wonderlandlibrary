// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import com.google.common.annotations.Beta;
import java.util.Iterator;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableSet;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class ForwardingNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>
{
    protected ForwardingNavigableSet() {
    }
    
    @Override
    protected abstract NavigableSet<E> delegate();
    
    @CheckForNull
    @Override
    public E lower(@ParametricNullness final E e) {
        return this.delegate().lower(e);
    }
    
    @CheckForNull
    protected E standardLower(@ParametricNullness final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.headSet(e, false).descendingIterator(), (E)null);
    }
    
    @CheckForNull
    @Override
    public E floor(@ParametricNullness final E e) {
        return this.delegate().floor(e);
    }
    
    @CheckForNull
    protected E standardFloor(@ParametricNullness final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.headSet(e, true).descendingIterator(), (E)null);
    }
    
    @CheckForNull
    @Override
    public E ceiling(@ParametricNullness final E e) {
        return this.delegate().ceiling(e);
    }
    
    @CheckForNull
    protected E standardCeiling(@ParametricNullness final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.tailSet(e, true).iterator(), (E)null);
    }
    
    @CheckForNull
    @Override
    public E higher(@ParametricNullness final E e) {
        return this.delegate().higher(e);
    }
    
    @CheckForNull
    protected E standardHigher(@ParametricNullness final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.tailSet(e, false).iterator(), (E)null);
    }
    
    @CheckForNull
    @Override
    public E pollFirst() {
        return this.delegate().pollFirst();
    }
    
    @CheckForNull
    protected E standardPollFirst() {
        return Iterators.pollNext(this.iterator());
    }
    
    @CheckForNull
    @Override
    public E pollLast() {
        return this.delegate().pollLast();
    }
    
    @CheckForNull
    protected E standardPollLast() {
        return Iterators.pollNext(this.descendingIterator());
    }
    
    @ParametricNullness
    protected E standardFirst() {
        return this.iterator().next();
    }
    
    @ParametricNullness
    protected E standardLast() {
        return this.descendingIterator().next();
    }
    
    @Override
    public NavigableSet<E> descendingSet() {
        return this.delegate().descendingSet();
    }
    
    @Override
    public Iterator<E> descendingIterator() {
        return this.delegate().descendingIterator();
    }
    
    @Override
    public NavigableSet<E> subSet(@ParametricNullness final E fromElement, final boolean fromInclusive, @ParametricNullness final E toElement, final boolean toInclusive) {
        return this.delegate().subSet(fromElement, fromInclusive, toElement, toInclusive);
    }
    
    @Beta
    protected NavigableSet<E> standardSubSet(@ParametricNullness final E fromElement, final boolean fromInclusive, @ParametricNullness final E toElement, final boolean toInclusive) {
        return this.tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive);
    }
    
    @Override
    protected SortedSet<E> standardSubSet(@ParametricNullness final E fromElement, @ParametricNullness final E toElement) {
        return this.subSet(fromElement, true, toElement, false);
    }
    
    @Override
    public NavigableSet<E> headSet(@ParametricNullness final E toElement, final boolean inclusive) {
        return this.delegate().headSet(toElement, inclusive);
    }
    
    protected SortedSet<E> standardHeadSet(@ParametricNullness final E toElement) {
        return this.headSet(toElement, false);
    }
    
    @Override
    public NavigableSet<E> tailSet(@ParametricNullness final E fromElement, final boolean inclusive) {
        return this.delegate().tailSet(fromElement, inclusive);
    }
    
    protected SortedSet<E> standardTailSet(@ParametricNullness final E fromElement) {
        return this.tailSet(fromElement, true);
    }
    
    @Beta
    protected class StandardDescendingSet extends Sets.DescendingSet<E>
    {
        public StandardDescendingSet(final ForwardingNavigableSet this$0) {
            super(this$0);
        }
    }
}
