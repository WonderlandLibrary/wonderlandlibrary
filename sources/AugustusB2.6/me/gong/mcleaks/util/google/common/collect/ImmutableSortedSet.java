// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.io.Serializable;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.Spliterators;
import java.util.Spliterator;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.SortedSet;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Collection;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.util.stream.Collector;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Comparator;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.util.NavigableSet;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E> implements NavigableSet<E>, SortedIterable<E>
{
    static final int SPLITERATOR_CHARACTERISTICS = 1301;
    final transient Comparator<? super E> comparator;
    @LazyInit
    @GwtIncompatible
    transient ImmutableSortedSet<E> descendingSet;
    
    @Beta
    public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(final Comparator<? super E> comparator) {
        return CollectCollectors.toImmutableSortedSet(comparator);
    }
    
    static <E> RegularImmutableSortedSet<E> emptySet(final Comparator<? super E> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return (RegularImmutableSortedSet<E>)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
        }
        return new RegularImmutableSortedSet<E>(ImmutableList.of(), comparator);
    }
    
    public static <E> ImmutableSortedSet<E> of() {
        return (ImmutableSortedSet<E>)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(final E element) {
        return new RegularImmutableSortedSet<E>(ImmutableList.of(element), Ordering.natural());
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(final E e1, final E e2) {
        return construct(Ordering.natural(), 2, (E[])new Comparable[] { e1, e2 });
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3) {
        return construct(Ordering.natural(), 3, (E[])new Comparable[] { e1, e2, e3 });
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3, final E e4) {
        return construct(Ordering.natural(), 4, (E[])new Comparable[] { e1, e2, e3, e4 });
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        return construct(Ordering.natural(), 5, (E[])new Comparable[] { e1, e2, e3, e4, e5 });
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E... remaining) {
        final Comparable[] contents = new Comparable[6 + remaining.length];
        contents[0] = e1;
        contents[1] = e2;
        contents[2] = e3;
        contents[3] = e4;
        contents[4] = e5;
        contents[5] = e6;
        System.arraycopy(remaining, 0, contents, 6, remaining.length);
        return construct(Ordering.natural(), contents.length, (E[])contents);
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(final E[] elements) {
        return construct(Ordering.natural(), elements.length, (E[])elements.clone());
    }
    
    public static <E> ImmutableSortedSet<E> copyOf(final Iterable<? extends E> elements) {
        final Ordering<E> naturalOrder = Ordering.natural();
        return copyOf((Comparator<? super E>)naturalOrder, elements);
    }
    
    public static <E> ImmutableSortedSet<E> copyOf(final Collection<? extends E> elements) {
        final Ordering<E> naturalOrder = Ordering.natural();
        return copyOf((Comparator<? super E>)naturalOrder, elements);
    }
    
    public static <E> ImmutableSortedSet<E> copyOf(final Iterator<? extends E> elements) {
        final Ordering<E> naturalOrder = Ordering.natural();
        return copyOf((Comparator<? super E>)naturalOrder, elements);
    }
    
    public static <E> ImmutableSortedSet<E> copyOf(final Comparator<? super E> comparator, final Iterator<? extends E> elements) {
        return new Builder<E>(comparator).addAll(elements).build();
    }
    
    public static <E> ImmutableSortedSet<E> copyOf(final Comparator<? super E> comparator, final Iterable<? extends E> elements) {
        Preconditions.checkNotNull(comparator);
        final boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
        if (hasSameComparator && elements instanceof ImmutableSortedSet) {
            final ImmutableSortedSet<E> original = (ImmutableSortedSet<E>)(ImmutableSortedSet)elements;
            if (!original.isPartialView()) {
                return original;
            }
        }
        final E[] array = (E[])Iterables.toArray(elements);
        return construct(comparator, array.length, array);
    }
    
    public static <E> ImmutableSortedSet<E> copyOf(final Comparator<? super E> comparator, final Collection<? extends E> elements) {
        return copyOf(comparator, (Iterable<? extends E>)elements);
    }
    
    public static <E> ImmutableSortedSet<E> copyOfSorted(final SortedSet<E> sortedSet) {
        final Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
        final ImmutableList<E> list = ImmutableList.copyOf((Collection<? extends E>)sortedSet);
        if (list.isEmpty()) {
            return (ImmutableSortedSet<E>)emptySet((Comparator<? super Object>)comparator);
        }
        return new RegularImmutableSortedSet<E>(list, comparator);
    }
    
    static <E> ImmutableSortedSet<E> construct(final Comparator<? super E> comparator, final int n, final E... contents) {
        if (n == 0) {
            return (ImmutableSortedSet<E>)emptySet((Comparator<? super Object>)comparator);
        }
        ObjectArrays.checkElementsNotNull(contents, n);
        Arrays.sort(contents, 0, n, comparator);
        int uniques = 1;
        for (int i = 1; i < n; ++i) {
            final E cur = contents[i];
            final E prev = contents[uniques - 1];
            if (comparator.compare((Object)cur, (Object)prev) != 0) {
                contents[uniques++] = cur;
            }
        }
        Arrays.fill(contents, uniques, n, null);
        return new RegularImmutableSortedSet<E>(ImmutableList.asImmutableList(contents, uniques), comparator);
    }
    
    public static <E> Builder<E> orderedBy(final Comparator<E> comparator) {
        return new Builder<E>(comparator);
    }
    
    public static <E extends Comparable<?>> Builder<E> reverseOrder() {
        return new Builder<E>(Ordering.natural().reverse());
    }
    
    public static <E extends Comparable<?>> Builder<E> naturalOrder() {
        return new Builder<E>(Ordering.natural());
    }
    
    int unsafeCompare(final Object a, final Object b) {
        return unsafeCompare(this.comparator, a, b);
    }
    
    static int unsafeCompare(final Comparator<?> comparator, final Object a, final Object b) {
        final Comparator<Object> unsafeComparator = (Comparator<Object>)comparator;
        return unsafeComparator.compare(a, b);
    }
    
    ImmutableSortedSet(final Comparator<? super E> comparator) {
        this.comparator = comparator;
    }
    
    @Override
    public Comparator<? super E> comparator() {
        return this.comparator;
    }
    
    @Override
    public abstract UnmodifiableIterator<E> iterator();
    
    @Override
    public ImmutableSortedSet<E> headSet(final E toElement) {
        return this.headSet(toElement, false);
    }
    
    @GwtIncompatible
    @Override
    public ImmutableSortedSet<E> headSet(final E toElement, final boolean inclusive) {
        return this.headSetImpl(Preconditions.checkNotNull(toElement), inclusive);
    }
    
    @Override
    public ImmutableSortedSet<E> subSet(final E fromElement, final E toElement) {
        return this.subSet(fromElement, true, toElement, false);
    }
    
    @GwtIncompatible
    @Override
    public ImmutableSortedSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        Preconditions.checkNotNull(fromElement);
        Preconditions.checkNotNull(toElement);
        Preconditions.checkArgument(this.comparator.compare((Object)fromElement, (Object)toElement) <= 0);
        return this.subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
    }
    
    @Override
    public ImmutableSortedSet<E> tailSet(final E fromElement) {
        return this.tailSet(fromElement, true);
    }
    
    @GwtIncompatible
    @Override
    public ImmutableSortedSet<E> tailSet(final E fromElement, final boolean inclusive) {
        return this.tailSetImpl(Preconditions.checkNotNull(fromElement), inclusive);
    }
    
    abstract ImmutableSortedSet<E> headSetImpl(final E p0, final boolean p1);
    
    abstract ImmutableSortedSet<E> subSetImpl(final E p0, final boolean p1, final E p2, final boolean p3);
    
    abstract ImmutableSortedSet<E> tailSetImpl(final E p0, final boolean p1);
    
    @GwtIncompatible
    @Override
    public E lower(final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.headSet(e, false).descendingIterator(), (E)null);
    }
    
    @GwtIncompatible
    @Override
    public E floor(final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.headSet(e, true).descendingIterator(), (E)null);
    }
    
    @GwtIncompatible
    @Override
    public E ceiling(final E e) {
        return Iterables.getFirst((Iterable<? extends E>)this.tailSet(e, true), (E)null);
    }
    
    @GwtIncompatible
    @Override
    public E higher(final E e) {
        return Iterables.getFirst((Iterable<? extends E>)this.tailSet(e, false), (E)null);
    }
    
    @Override
    public E first() {
        return this.iterator().next();
    }
    
    @Override
    public E last() {
        return this.descendingIterator().next();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @GwtIncompatible
    @Override
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @GwtIncompatible
    @Override
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }
    
    @GwtIncompatible
    @Override
    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> result = this.descendingSet;
        if (result == null) {
            final ImmutableSortedSet<E> descendingSet = this.createDescendingSet();
            this.descendingSet = descendingSet;
            result = descendingSet;
            result.descendingSet = this;
        }
        return result;
    }
    
    @GwtIncompatible
    ImmutableSortedSet<E> createDescendingSet() {
        return new DescendingImmutableSortedSet<E>(this);
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return new Spliterators.AbstractSpliterator<E>((long)this.size(), 1365) {
            final UnmodifiableIterator<E> iterator = ImmutableSortedSet.this.iterator();
            
            @Override
            public boolean tryAdvance(final Consumer<? super E> action) {
                if (this.iterator.hasNext()) {
                    action.accept(this.iterator.next());
                    return true;
                }
                return false;
            }
            
            @Override
            public Comparator<? super E> getComparator() {
                return ImmutableSortedSet.this.comparator;
            }
        };
    }
    
    @GwtIncompatible
    @Override
    public abstract UnmodifiableIterator<E> descendingIterator();
    
    abstract int indexOf(@Nullable final Object p0);
    
    private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm((Comparator<? super Object>)this.comparator, this.toArray());
    }
    
    public static final class Builder<E> extends ImmutableSet.Builder<E>
    {
        private final Comparator<? super E> comparator;
        
        public Builder(final Comparator<? super E> comparator) {
            this.comparator = Preconditions.checkNotNull(comparator);
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E element) {
            super.add(element);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E... elements) {
            super.add(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        Builder<E> combine(final ArrayBasedBuilder<E> builder) {
            super.combine(builder);
            return this;
        }
        
        @Override
        public ImmutableSortedSet<E> build() {
            final E[] contentsArray = (E[])this.contents;
            final ImmutableSortedSet<E> result = ImmutableSortedSet.construct(this.comparator, this.size, contentsArray);
            this.size = result.size();
            return result;
        }
    }
    
    private static class SerializedForm<E> implements Serializable
    {
        final Comparator<? super E> comparator;
        final Object[] elements;
        private static final long serialVersionUID = 0L;
        
        public SerializedForm(final Comparator<? super E> comparator, final Object[] elements) {
            this.comparator = comparator;
            this.elements = elements;
        }
        
        Object readResolve() {
            return new Builder<Object>((Comparator<? super Object>)this.comparator).add((Object[])this.elements).build();
        }
    }
}
