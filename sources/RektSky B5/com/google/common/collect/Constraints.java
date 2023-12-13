/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Constraint;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ForwardingListIterator;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ForwardingSortedSet;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible
final class Constraints {
    private Constraints() {
    }

    public static <E> Collection<E> constrainedCollection(Collection<E> collection, Constraint<? super E> constraint) {
        return new ConstrainedCollection<E>(collection, constraint);
    }

    public static <E> Set<E> constrainedSet(Set<E> set, Constraint<? super E> constraint) {
        return new ConstrainedSet<E>(set, constraint);
    }

    public static <E> SortedSet<E> constrainedSortedSet(SortedSet<E> sortedSet, Constraint<? super E> constraint) {
        return new ConstrainedSortedSet<E>(sortedSet, constraint);
    }

    public static <E> List<E> constrainedList(List<E> list, Constraint<? super E> constraint) {
        return list instanceof RandomAccess ? new ConstrainedRandomAccessList<E>(list, constraint) : new ConstrainedList<E>(list, constraint);
    }

    private static <E> ListIterator<E> constrainedListIterator(ListIterator<E> listIterator, Constraint<? super E> constraint) {
        return new ConstrainedListIterator<E>(listIterator, constraint);
    }

    static <E> Collection<E> constrainedTypePreservingCollection(Collection<E> collection, Constraint<E> constraint) {
        if (collection instanceof SortedSet) {
            return Constraints.constrainedSortedSet((SortedSet)collection, constraint);
        }
        if (collection instanceof Set) {
            return Constraints.constrainedSet((Set)collection, constraint);
        }
        if (collection instanceof List) {
            return Constraints.constrainedList((List)collection, constraint);
        }
        return Constraints.constrainedCollection(collection, constraint);
    }

    private static <E> Collection<E> checkElements(Collection<E> elements, Constraint<? super E> constraint) {
        ArrayList<E> copy = Lists.newArrayList(elements);
        for (Object element : copy) {
            constraint.checkElement(element);
        }
        return copy;
    }

    static class ConstrainedListIterator<E>
    extends ForwardingListIterator<E> {
        private final ListIterator<E> delegate;
        private final Constraint<? super E> constraint;

        public ConstrainedListIterator(ListIterator<E> delegate, Constraint<? super E> constraint) {
            this.delegate = delegate;
            this.constraint = constraint;
        }

        @Override
        protected ListIterator<E> delegate() {
            return this.delegate;
        }

        @Override
        public void add(E element) {
            this.constraint.checkElement(element);
            this.delegate.add(element);
        }

        @Override
        public void set(E element) {
            this.constraint.checkElement(element);
            this.delegate.set(element);
        }
    }

    static class ConstrainedRandomAccessList<E>
    extends ConstrainedList<E>
    implements RandomAccess {
        ConstrainedRandomAccessList(List<E> delegate, Constraint<? super E> constraint) {
            super(delegate, constraint);
        }
    }

    @GwtCompatible
    private static class ConstrainedList<E>
    extends ForwardingList<E> {
        final List<E> delegate;
        final Constraint<? super E> constraint;

        ConstrainedList(List<E> delegate, Constraint<? super E> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }

        @Override
        protected List<E> delegate() {
            return this.delegate;
        }

        @Override
        public boolean add(E element) {
            this.constraint.checkElement(element);
            return this.delegate.add(element);
        }

        @Override
        public void add(int index, E element) {
            this.constraint.checkElement(element);
            this.delegate.add(index, element);
        }

        @Override
        public boolean addAll(Collection<? extends E> elements) {
            return this.delegate.addAll(Constraints.checkElements(elements, this.constraint));
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> elements) {
            return this.delegate.addAll(index, Constraints.checkElements(elements, this.constraint));
        }

        @Override
        public ListIterator<E> listIterator() {
            return Constraints.constrainedListIterator(this.delegate.listIterator(), this.constraint);
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            return Constraints.constrainedListIterator(this.delegate.listIterator(index), this.constraint);
        }

        @Override
        public E set(int index, E element) {
            this.constraint.checkElement(element);
            return this.delegate.set(index, element);
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return Constraints.constrainedList(this.delegate.subList(fromIndex, toIndex), this.constraint);
        }
    }

    private static class ConstrainedSortedSet<E>
    extends ForwardingSortedSet<E> {
        final SortedSet<E> delegate;
        final Constraint<? super E> constraint;

        ConstrainedSortedSet(SortedSet<E> delegate, Constraint<? super E> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }

        @Override
        protected SortedSet<E> delegate() {
            return this.delegate;
        }

        @Override
        public SortedSet<E> headSet(E toElement) {
            return Constraints.constrainedSortedSet(this.delegate.headSet(toElement), this.constraint);
        }

        @Override
        public SortedSet<E> subSet(E fromElement, E toElement) {
            return Constraints.constrainedSortedSet(this.delegate.subSet(fromElement, toElement), this.constraint);
        }

        @Override
        public SortedSet<E> tailSet(E fromElement) {
            return Constraints.constrainedSortedSet(this.delegate.tailSet(fromElement), this.constraint);
        }

        @Override
        public boolean add(E element) {
            this.constraint.checkElement(element);
            return this.delegate.add(element);
        }

        @Override
        public boolean addAll(Collection<? extends E> elements) {
            return this.delegate.addAll(Constraints.checkElements(elements, this.constraint));
        }
    }

    static class ConstrainedSet<E>
    extends ForwardingSet<E> {
        private final Set<E> delegate;
        private final Constraint<? super E> constraint;

        public ConstrainedSet(Set<E> delegate, Constraint<? super E> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }

        @Override
        protected Set<E> delegate() {
            return this.delegate;
        }

        @Override
        public boolean add(E element) {
            this.constraint.checkElement(element);
            return this.delegate.add(element);
        }

        @Override
        public boolean addAll(Collection<? extends E> elements) {
            return this.delegate.addAll(Constraints.checkElements(elements, this.constraint));
        }
    }

    static class ConstrainedCollection<E>
    extends ForwardingCollection<E> {
        private final Collection<E> delegate;
        private final Constraint<? super E> constraint;

        public ConstrainedCollection(Collection<E> delegate, Constraint<? super E> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }

        @Override
        protected Collection<E> delegate() {
            return this.delegate;
        }

        @Override
        public boolean add(E element) {
            this.constraint.checkElement(element);
            return this.delegate.add(element);
        }

        @Override
        public boolean addAll(Collection<? extends E> elements) {
            return this.delegate.addAll(Constraints.checkElements(elements, this.constraint));
        }
    }
}

