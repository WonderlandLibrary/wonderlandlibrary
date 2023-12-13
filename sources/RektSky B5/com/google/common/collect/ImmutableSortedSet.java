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
import com.google.common.collect.DescendingImmutableSortedSet;
import com.google.common.collect.EmptyImmutableSortedSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSetFauxverideShim;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Ordering;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.SortedIterable;
import com.google.common.collect.SortedIterables;
import com.google.common.collect.UnmodifiableIterator;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
public abstract class ImmutableSortedSet<E>
extends ImmutableSortedSetFauxverideShim<E>
implements NavigableSet<E>,
SortedIterable<E> {
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final ImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new EmptyImmutableSortedSet<Comparable>(NATURAL_ORDER);
    final transient Comparator<? super E> comparator;
    @GwtIncompatible(value="NavigableSet")
    transient ImmutableSortedSet<E> descendingSet;

    private static <E> ImmutableSortedSet<E> emptySet() {
        return NATURAL_EMPTY_SET;
    }

    static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
        if (NATURAL_ORDER.equals(comparator)) {
            return ImmutableSortedSet.emptySet();
        }
        return new EmptyImmutableSortedSet<E>(comparator);
    }

    public static <E> ImmutableSortedSet<E> of() {
        return ImmutableSortedSet.emptySet();
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) {
        return new RegularImmutableSortedSet<E>(ImmutableList.of(element), Ordering.natural());
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) {
        return ImmutableSortedSet.construct(Ordering.natural(), 2, e1, e2);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
        return ImmutableSortedSet.construct(Ordering.natural(), 3, e1, e2, e3);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
        return ImmutableSortedSet.construct(Ordering.natural(), 4, e1, e2, e3, e4);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
        return ImmutableSortedSet.construct(Ordering.natural(), 5, e1, e2, e3, e4, e5);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E ... remaining) {
        Comparable[] contents = new Comparable[6 + remaining.length];
        contents[0] = e1;
        contents[1] = e2;
        contents[2] = e3;
        contents[3] = e4;
        contents[4] = e5;
        contents[5] = e6;
        System.arraycopy(remaining, 0, contents, 6, remaining.length);
        return ImmutableSortedSet.construct(Ordering.natural(), contents.length, contents);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements) {
        return ImmutableSortedSet.construct(Ordering.natural(), elements.length, (Object[])elements.clone());
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
        Ordering naturalOrder = Ordering.natural();
        return ImmutableSortedSet.copyOf(naturalOrder, elements);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
        Ordering naturalOrder = Ordering.natural();
        return ImmutableSortedSet.copyOf(naturalOrder, elements);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
        Ordering naturalOrder = Ordering.natural();
        return ImmutableSortedSet.copyOf(naturalOrder, elements);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
        return ((Builder)new Builder<E>(comparator).addAll((Iterator)elements)).build();
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
        ImmutableSortedSet original;
        Preconditions.checkNotNull(comparator);
        boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
        if (hasSameComparator && elements instanceof ImmutableSortedSet && !(original = (ImmutableSortedSet)elements).isPartialView()) {
            return original;
        }
        Object[] array = Iterables.toArray(elements);
        return ImmutableSortedSet.construct(comparator, array.length, array);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) {
        return ImmutableSortedSet.copyOf(comparator, elements);
    }

    public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
        Comparator<E> comparator = SortedIterables.comparator(sortedSet);
        ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
        if (list.isEmpty()) {
            return ImmutableSortedSet.emptySet(comparator);
        }
        return new RegularImmutableSortedSet<E>(list, comparator);
    }

    static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n2, E ... contents) {
        if (n2 == 0) {
            return ImmutableSortedSet.emptySet(comparator);
        }
        ObjectArrays.checkElementsNotNull((Object[])contents, n2);
        Arrays.sort(contents, 0, n2, comparator);
        int uniques = 1;
        for (int i2 = 1; i2 < n2; ++i2) {
            E cur = contents[i2];
            E prev = contents[uniques - 1];
            if (comparator.compare(cur, prev) == 0) continue;
            contents[uniques++] = cur;
        }
        Arrays.fill(contents, uniques, n2, null);
        return new RegularImmutableSortedSet<E>(ImmutableList.asImmutableList(contents, uniques), comparator);
    }

    public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
        return new Builder<E>(comparator);
    }

    public static <E extends Comparable<?>> Builder<E> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    public static <E extends Comparable<?>> Builder<E> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    int unsafeCompare(Object a2, Object b2) {
        return ImmutableSortedSet.unsafeCompare(this.comparator, a2, b2);
    }

    static int unsafeCompare(Comparator<?> comparator, Object a2, Object b2) {
        Comparator<?> unsafeComparator = comparator;
        return unsafeComparator.compare(a2, b2);
    }

    ImmutableSortedSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    @Override
    public abstract UnmodifiableIterator<E> iterator();

    @Override
    public ImmutableSortedSet<E> headSet(E toElement) {
        return this.headSet((Object)toElement, false);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) {
        return this.headSetImpl(Preconditions.checkNotNull(toElement), inclusive);
    }

    @Override
    public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
        return this.subSet((Object)fromElement, true, (Object)toElement, false);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        Preconditions.checkNotNull(fromElement);
        Preconditions.checkNotNull(toElement);
        Preconditions.checkArgument(this.comparator.compare(fromElement, toElement) <= 0);
        return this.subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public ImmutableSortedSet<E> tailSet(E fromElement) {
        return this.tailSet((Object)fromElement, true);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) {
        return this.tailSetImpl(Preconditions.checkNotNull(fromElement), inclusive);
    }

    abstract ImmutableSortedSet<E> headSetImpl(E var1, boolean var2);

    abstract ImmutableSortedSet<E> subSetImpl(E var1, boolean var2, E var3, boolean var4);

    abstract ImmutableSortedSet<E> tailSetImpl(E var1, boolean var2);

    @Override
    @GwtIncompatible(value="NavigableSet")
    public E lower(E e2) {
        return Iterators.getNext(((ImmutableSortedSet)this.headSet((Object)e2, false)).descendingIterator(), null);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public E floor(E e2) {
        return Iterators.getNext(((ImmutableSortedSet)this.headSet((Object)e2, true)).descendingIterator(), null);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public E ceiling(E e2) {
        return Iterables.getFirst(this.tailSet((Object)e2, true), null);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public E higher(E e2) {
        return Iterables.getFirst(this.tailSet((Object)e2, false), null);
    }

    @Override
    public E first() {
        return this.iterator().next();
    }

    @Override
    public E last() {
        return this.descendingIterator().next();
    }

    @Override
    @Deprecated
    @GwtIncompatible(value="NavigableSet")
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    @GwtIncompatible(value="NavigableSet")
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> result = this.descendingSet;
        if (result == null) {
            result = this.descendingSet = this.createDescendingSet();
            result.descendingSet = this;
        }
        return result;
    }

    @GwtIncompatible(value="NavigableSet")
    ImmutableSortedSet<E> createDescendingSet() {
        return new DescendingImmutableSortedSet(this);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public abstract UnmodifiableIterator<E> descendingIterator();

    abstract int indexOf(@Nullable Object var1);

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override
    Object writeReplace() {
        return new SerializedForm<E>(this.comparator, this.toArray());
    }

    private static class SerializedForm<E>
    implements Serializable {
        final Comparator<? super E> comparator;
        final Object[] elements;
        private static final long serialVersionUID = 0L;

        public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
            this.comparator = comparator;
            this.elements = elements;
        }

        Object readResolve() {
            return ((Builder)new Builder<E>(this.comparator).add(this.elements)).build();
        }
    }

    public static final class Builder<E>
    extends ImmutableSet.Builder<E> {
        private final Comparator<? super E> comparator;

        public Builder(Comparator<? super E> comparator) {
            this.comparator = Preconditions.checkNotNull(comparator);
        }

        @Override
        public Builder<E> add(E element) {
            super.add((Object)element);
            return this;
        }

        @Override
        public Builder<E> add(E ... elements) {
            super.add((Object[])elements);
            return this;
        }

        @Override
        public Builder<E> addAll(Iterable<? extends E> elements) {
            super.addAll((Iterable)elements);
            return this;
        }

        @Override
        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll((Iterator)elements);
            return this;
        }

        @Override
        public ImmutableSortedSet<E> build() {
            Object[] contentsArray = this.contents;
            ImmutableSortedSet<Object> result = ImmutableSortedSet.construct(this.comparator, this.size, contentsArray);
            this.size = result.size();
            return result;
        }
    }
}

