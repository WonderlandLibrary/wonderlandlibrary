// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.Spliterator;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableSortedSet<E> extends ImmutableSortedSet<E>
{
    static final RegularImmutableSortedSet<Comparable> NATURAL_EMPTY_SET;
    private final transient ImmutableList<E> elements;
    
    RegularImmutableSortedSet(final ImmutableList<E> elements, final Comparator<? super E> comparator) {
        super(comparator);
        this.elements = elements;
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.elements.iterator();
    }
    
    @GwtIncompatible
    @Override
    public UnmodifiableIterator<E> descendingIterator() {
        return this.elements.reverse().iterator();
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return this.asList().spliterator();
    }
    
    @Override
    public void forEach(final Consumer<? super E> action) {
        this.elements.forEach(action);
    }
    
    @Override
    public int size() {
        return this.elements.size();
    }
    
    @Override
    public boolean contains(@Nullable final Object o) {
        try {
            return o != null && this.unsafeBinarySearch(o) >= 0;
        }
        catch (ClassCastException e) {
            return false;
        }
    }
    
    @Override
    public boolean containsAll(Collection<?> targets) {
        if (targets instanceof Multiset) {
            targets = (Collection<?>)((Multiset)targets).elementSet();
        }
        if (!SortedIterables.hasSameComparator(this.comparator(), targets) || targets.size() <= 1) {
            return super.containsAll(targets);
        }
        final PeekingIterator<E> thisIterator = Iterators.peekingIterator((Iterator<? extends E>)this.iterator());
        final Iterator<?> thatIterator = targets.iterator();
        Object target = thatIterator.next();
        try {
            while (thisIterator.hasNext()) {
                final int cmp = this.unsafeCompare(thisIterator.peek(), target);
                if (cmp < 0) {
                    thisIterator.next();
                }
                else if (cmp == 0) {
                    if (!thatIterator.hasNext()) {
                        return true;
                    }
                    target = thatIterator.next();
                }
                else {
                    if (cmp > 0) {
                        return false;
                    }
                    continue;
                }
            }
        }
        catch (NullPointerException e) {
            return false;
        }
        catch (ClassCastException e2) {
            return false;
        }
        return false;
    }
    
    private int unsafeBinarySearch(final Object key) throws ClassCastException {
        return Collections.binarySearch(this.elements, key, this.unsafeComparator());
    }
    
    @Override
    boolean isPartialView() {
        return this.elements.isPartialView();
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        return this.elements.copyIntoArray(dst, offset);
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        final Set<?> that = (Set<?>)object;
        if (this.size() != that.size()) {
            return false;
        }
        if (this.isEmpty()) {
            return true;
        }
        if (SortedIterables.hasSameComparator(this.comparator, that)) {
            final Iterator<?> otherIterator = that.iterator();
            try {
                for (final Object element : this) {
                    final Object otherElement = otherIterator.next();
                    if (otherElement == null || this.unsafeCompare(element, otherElement) != 0) {
                        return false;
                    }
                }
                return true;
            }
            catch (ClassCastException e) {
                return false;
            }
            catch (NoSuchElementException e2) {
                return false;
            }
        }
        return this.containsAll(that);
    }
    
    @Override
    public E first() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.elements.get(0);
    }
    
    @Override
    public E last() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.elements.get(this.size() - 1);
    }
    
    @Override
    public E lower(final E element) {
        final int index = this.headIndex(element, false) - 1;
        return (index == -1) ? null : this.elements.get(index);
    }
    
    @Override
    public E floor(final E element) {
        final int index = this.headIndex(element, true) - 1;
        return (index == -1) ? null : this.elements.get(index);
    }
    
    @Override
    public E ceiling(final E element) {
        final int index = this.tailIndex(element, true);
        return (index == this.size()) ? null : this.elements.get(index);
    }
    
    @Override
    public E higher(final E element) {
        final int index = this.tailIndex(element, false);
        return (index == this.size()) ? null : this.elements.get(index);
    }
    
    @Override
    ImmutableSortedSet<E> headSetImpl(final E toElement, final boolean inclusive) {
        return this.getSubSet(0, this.headIndex(toElement, inclusive));
    }
    
    int headIndex(final E toElement, final boolean inclusive) {
        return SortedLists.binarySearch((List<? extends E>)this.elements, Preconditions.checkNotNull(toElement), this.comparator(), inclusive ? SortedLists.KeyPresentBehavior.FIRST_AFTER : SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    }
    
    @Override
    ImmutableSortedSet<E> subSetImpl(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return this.tailSetImpl(fromElement, fromInclusive).headSetImpl(toElement, toInclusive);
    }
    
    @Override
    ImmutableSortedSet<E> tailSetImpl(final E fromElement, final boolean inclusive) {
        return this.getSubSet(this.tailIndex(fromElement, inclusive), this.size());
    }
    
    int tailIndex(final E fromElement, final boolean inclusive) {
        return SortedLists.binarySearch((List<? extends E>)this.elements, Preconditions.checkNotNull(fromElement), this.comparator(), inclusive ? SortedLists.KeyPresentBehavior.FIRST_PRESENT : SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    }
    
    Comparator<Object> unsafeComparator() {
        return (Comparator<Object>)this.comparator;
    }
    
    RegularImmutableSortedSet<E> getSubSet(final int newFromIndex, final int newToIndex) {
        if (newFromIndex == 0 && newToIndex == this.size()) {
            return this;
        }
        if (newFromIndex < newToIndex) {
            return new RegularImmutableSortedSet<E>(this.elements.subList(newFromIndex, newToIndex), this.comparator);
        }
        return ImmutableSortedSet.emptySet(this.comparator);
    }
    
    @Override
    int indexOf(@Nullable final Object target) {
        if (target == null) {
            return -1;
        }
        int position;
        try {
            position = SortedLists.binarySearch(this.elements, target, this.unsafeComparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
        }
        catch (ClassCastException e) {
            return -1;
        }
        return (position >= 0) ? position : -1;
    }
    
    @Override
    ImmutableList<E> createAsList() {
        return (this.size() <= 1) ? this.elements : new ImmutableSortedAsList<E>(this, this.elements);
    }
    
    @Override
    ImmutableSortedSet<E> createDescendingSet() {
        final Ordering<E> reversedOrder = Ordering.from(this.comparator).reverse();
        return (ImmutableSortedSet<E>)(this.isEmpty() ? ImmutableSortedSet.emptySet((Comparator<? super Object>)reversedOrder) : new RegularImmutableSortedSet<Object>((ImmutableList<Object>)this.elements.reverse(), (Comparator<? super Object>)reversedOrder));
    }
    
    static {
        NATURAL_EMPTY_SET = new RegularImmutableSortedSet<Comparable>((ImmutableList<Comparable>)ImmutableList.of(), Ordering.natural());
    }
}
