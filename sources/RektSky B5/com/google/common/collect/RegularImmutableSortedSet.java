/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedAsList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.SortedIterables;
import com.google.common.collect.SortedLists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
final class RegularImmutableSortedSet<E>
extends ImmutableSortedSet<E> {
    private final transient ImmutableList<E> elements;

    RegularImmutableSortedSet(ImmutableList<E> elements, Comparator<? super E> comparator) {
        super(comparator);
        this.elements = elements;
        Preconditions.checkArgument(!elements.isEmpty());
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.elements.iterator();
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public UnmodifiableIterator<E> descendingIterator() {
        return this.elements.reverse().iterator();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public boolean contains(Object o2) {
        try {
            return o2 != null && this.unsafeBinarySearch(o2) >= 0;
        }
        catch (ClassCastException e2) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> targets) {
        if (targets instanceof Multiset) {
            targets = ((Multiset)targets).elementSet();
        }
        if (!SortedIterables.hasSameComparator(this.comparator(), targets) || targets.size() <= 1) {
            return super.containsAll(targets);
        }
        PeekingIterator thisIterator = Iterators.peekingIterator(this.iterator());
        Iterator<?> thatIterator = targets.iterator();
        Object target = thatIterator.next();
        try {
            while (thisIterator.hasNext()) {
                int cmp = this.unsafeCompare(thisIterator.peek(), target);
                if (cmp < 0) {
                    thisIterator.next();
                    continue;
                }
                if (cmp == 0) {
                    if (!thatIterator.hasNext()) {
                        return true;
                    }
                    target = thatIterator.next();
                    continue;
                }
                if (cmp <= 0) continue;
                return false;
            }
        }
        catch (NullPointerException e2) {
            return false;
        }
        catch (ClassCastException e3) {
            return false;
        }
        return false;
    }

    private int unsafeBinarySearch(Object key) throws ClassCastException {
        return Collections.binarySearch(this.elements, key, this.unsafeComparator());
    }

    @Override
    boolean isPartialView() {
        return this.elements.isPartialView();
    }

    @Override
    int copyIntoArray(Object[] dst, int offset) {
        return this.elements.copyIntoArray(dst, offset);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        Set that = (Set)object;
        if (this.size() != that.size()) {
            return false;
        }
        if (SortedIterables.hasSameComparator(this.comparator, that)) {
            Iterator otherIterator = that.iterator();
            try {
                for (Object element : this) {
                    Object otherElement = otherIterator.next();
                    if (otherElement != null && this.unsafeCompare(element, otherElement) == 0) continue;
                    return false;
                }
                return true;
            }
            catch (ClassCastException e2) {
                return false;
            }
            catch (NoSuchElementException e3) {
                return false;
            }
        }
        return this.containsAll(that);
    }

    @Override
    public E first() {
        return this.elements.get(0);
    }

    @Override
    public E last() {
        return this.elements.get(this.size() - 1);
    }

    @Override
    public E lower(E element) {
        int index = this.headIndex(element, false) - 1;
        return index == -1 ? null : (E)this.elements.get(index);
    }

    @Override
    public E floor(E element) {
        int index = this.headIndex(element, true) - 1;
        return index == -1 ? null : (E)this.elements.get(index);
    }

    @Override
    public E ceiling(E element) {
        int index = this.tailIndex(element, true);
        return index == this.size() ? null : (E)this.elements.get(index);
    }

    @Override
    public E higher(E element) {
        int index = this.tailIndex(element, false);
        return index == this.size() ? null : (E)this.elements.get(index);
    }

    @Override
    ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
        return this.getSubSet(0, this.headIndex(toElement, inclusive));
    }

    int headIndex(E toElement, boolean inclusive) {
        return SortedLists.binarySearch(this.elements, Preconditions.checkNotNull(toElement), this.comparator(), inclusive ? SortedLists.KeyPresentBehavior.FIRST_AFTER : SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    }

    @Override
    ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return this.tailSetImpl(fromElement, fromInclusive).headSetImpl(toElement, toInclusive);
    }

    @Override
    ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
        return this.getSubSet(this.tailIndex(fromElement, inclusive), this.size());
    }

    int tailIndex(E fromElement, boolean inclusive) {
        return SortedLists.binarySearch(this.elements, Preconditions.checkNotNull(fromElement), this.comparator(), inclusive ? SortedLists.KeyPresentBehavior.FIRST_PRESENT : SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    }

    Comparator<Object> unsafeComparator() {
        return this.comparator;
    }

    ImmutableSortedSet<E> getSubSet(int newFromIndex, int newToIndex) {
        if (newFromIndex == 0 && newToIndex == this.size()) {
            return this;
        }
        if (newFromIndex < newToIndex) {
            return new RegularImmutableSortedSet<E>(this.elements.subList(newFromIndex, newToIndex), this.comparator);
        }
        return RegularImmutableSortedSet.emptySet(this.comparator);
    }

    @Override
    int indexOf(@Nullable Object target) {
        int position;
        if (target == null) {
            return -1;
        }
        try {
            position = SortedLists.binarySearch(this.elements, target, this.unsafeComparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
        }
        catch (ClassCastException e2) {
            return -1;
        }
        return position >= 0 ? position : -1;
    }

    @Override
    ImmutableList<E> createAsList() {
        return new ImmutableSortedAsList<E>(this, this.elements);
    }

    @Override
    ImmutableSortedSet<E> createDescendingSet() {
        return new RegularImmutableSortedSet<E>(this.elements.reverse(), Ordering.from(this.comparator).reverse());
    }
}

