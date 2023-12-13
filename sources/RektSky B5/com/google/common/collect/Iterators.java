/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.TransformedIterator;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.UnmodifiableListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Iterators {
    static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR = new UnmodifiableListIterator<Object>(){

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public Object previous() {
            throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return -1;
        }
    };
    private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator<Object>(){

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            CollectPreconditions.checkRemove(false);
        }
    };

    private Iterators() {
    }

    public static <T> UnmodifiableIterator<T> emptyIterator() {
        return Iterators.emptyListIterator();
    }

    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return EMPTY_LIST_ITERATOR;
    }

    static <T> Iterator<T> emptyModifiableIterator() {
        return EMPTY_MODIFIABLE_ITERATOR;
    }

    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        if (iterator instanceof UnmodifiableIterator) {
            return (UnmodifiableIterator)iterator;
        }
        return new UnmodifiableIterator<T>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }

    @Deprecated
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator) {
        return Preconditions.checkNotNull(iterator);
    }

    public static int size(Iterator<?> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            ++count;
        }
        return count;
    }

    public static boolean contains(Iterator<?> iterator, @Nullable Object element) {
        return Iterators.any(iterator, Predicates.equalTo(element));
    }

    public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) {
        return Iterators.removeIf(removeFrom, Predicates.in(elementsToRemove));
    }

    public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean modified = false;
        while (removeFrom.hasNext()) {
            if (!predicate.apply(removeFrom.next())) continue;
            removeFrom.remove();
            modified = true;
        }
        return modified;
    }

    public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) {
        return Iterators.removeIf(removeFrom, Predicates.not(Predicates.in(elementsToRetain)));
    }

    public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
        while (iterator1.hasNext()) {
            Object o2;
            if (!iterator2.hasNext()) {
                return false;
            }
            Object o1 = iterator1.next();
            if (Objects.equal(o1, o2 = iterator2.next())) continue;
            return false;
        }
        return !iterator2.hasNext();
    }

    public static String toString(Iterator<?> iterator) {
        return Collections2.STANDARD_JOINER.appendTo(new StringBuilder().append('['), iterator).append(']').toString();
    }

    public static <T> T getOnlyElement(Iterator<T> iterator) {
        T first = iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("expected one element but was: <" + first);
        for (int i2 = 0; i2 < 4 && iterator.hasNext(); ++i2) {
            sb.append(", " + iterator.next());
        }
        if (iterator.hasNext()) {
            sb.append(", ...");
        }
        sb.append('>');
        throw new IllegalArgumentException(sb.toString());
    }

    @Nullable
    public static <T> T getOnlyElement(Iterator<? extends T> iterator, @Nullable T defaultValue) {
        return iterator.hasNext() ? Iterators.getOnlyElement(iterator) : defaultValue;
    }

    @GwtIncompatible(value="Array.newInstance(Class, int)")
    public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
        ArrayList<? extends T> list = Lists.newArrayList(iterator);
        return Iterables.toArray(list, type);
    }

    public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
        Preconditions.checkNotNull(addTo);
        Preconditions.checkNotNull(iterator);
        boolean wasModified = false;
        while (iterator.hasNext()) {
            wasModified |= addTo.add(iterator.next());
        }
        return wasModified;
    }

    public static int frequency(Iterator<?> iterator, @Nullable Object element) {
        return Iterators.size(Iterators.filter(iterator, Predicates.equalTo(element)));
    }

    public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new Iterator<T>(){
            Iterator<T> iterator = Iterators.emptyIterator();
            Iterator<T> removeFrom;

            @Override
            public boolean hasNext() {
                if (!this.iterator.hasNext()) {
                    this.iterator = iterable.iterator();
                }
                return this.iterator.hasNext();
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.removeFrom = this.iterator;
                return this.iterator.next();
            }

            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.removeFrom != null);
                this.removeFrom.remove();
                this.removeFrom = null;
            }
        };
    }

    public static <T> Iterator<T> cycle(T ... elements) {
        return Iterators.cycle(Lists.newArrayList(elements));
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> a2, Iterator<? extends T> b2) {
        return Iterators.concat(ImmutableList.of(a2, b2).iterator());
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> a2, Iterator<? extends T> b2, Iterator<? extends T> c2) {
        return Iterators.concat(ImmutableList.of(a2, b2, c2).iterator());
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> a2, Iterator<? extends T> b2, Iterator<? extends T> c2, Iterator<? extends T> d2) {
        return Iterators.concat(ImmutableList.of(a2, b2, c2, d2).iterator());
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> ... inputs) {
        return Iterators.concat(ImmutableList.copyOf(inputs).iterator());
    }

    public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> inputs) {
        Preconditions.checkNotNull(inputs);
        return new Iterator<T>(){
            Iterator<? extends T> current = Iterators.emptyIterator();
            Iterator<? extends T> removeFrom;

            @Override
            public boolean hasNext() {
                boolean currentHasNext;
                while (!(currentHasNext = Preconditions.checkNotNull(this.current).hasNext()) && inputs.hasNext()) {
                    this.current = (Iterator)inputs.next();
                }
                return currentHasNext;
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.removeFrom = this.current;
                return this.current.next();
            }

            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.removeFrom != null);
                this.removeFrom.remove();
                this.removeFrom = null;
            }
        };
    }

    public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size) {
        return Iterators.partitionImpl(iterator, size, false);
    }

    public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size) {
        return Iterators.partitionImpl(iterator, size, true);
    }

    private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(size > 0);
        return new UnmodifiableIterator<List<T>>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public List<T> next() {
                int count;
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                Object[] array = new Object[size];
                for (count = 0; count < size && iterator.hasNext(); ++count) {
                    array[count] = iterator.next();
                }
                for (int i2 = count; i2 < size; ++i2) {
                    array[i2] = null;
                }
                List<Object> list = Collections.unmodifiableList(Arrays.asList(array));
                return pad || count == size ? list : list.subList(0, count);
            }
        };
    }

    public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(predicate);
        return new AbstractIterator<T>(){

            @Override
            protected T computeNext() {
                while (unfiltered.hasNext()) {
                    Object element = unfiltered.next();
                    if (!predicate.apply(element)) continue;
                    return element;
                }
                return this.endOfData();
            }
        };
    }

    @GwtIncompatible(value="Class.isInstance")
    public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> type) {
        return Iterators.filter(unfiltered, Predicates.instanceOf(type));
    }

    public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
        return Iterators.indexOf(iterator, predicate) != -1;
    }

    public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (predicate.apply(element)) continue;
            return false;
        }
        return true;
    }

    public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) {
        return (T)Iterators.filter(iterator, predicate).next();
    }

    @Nullable
    public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, @Nullable T defaultValue) {
        return Iterators.getNext(Iterators.filter(iterator, predicate), defaultValue);
    }

    public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate) {
        UnmodifiableIterator<T> filteredIterator = Iterators.filter(iterator, predicate);
        return filteredIterator.hasNext() ? Optional.of(filteredIterator.next()) : Optional.absent();
    }

    public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate");
        int i2 = 0;
        while (iterator.hasNext()) {
            T current = iterator.next();
            if (predicate.apply(current)) {
                return i2;
            }
            ++i2;
        }
        return -1;
    }

    public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(function);
        return new TransformedIterator<F, T>(fromIterator){

            @Override
            T transform(F from) {
                return function.apply(from);
            }
        };
    }

    public static <T> T get(Iterator<T> iterator, int position) {
        Iterators.checkNonnegative(position);
        int skipped = Iterators.advance(iterator, position);
        if (!iterator.hasNext()) {
            throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
        }
        return iterator.next();
    }

    static void checkNonnegative(int position) {
        if (position < 0) {
            throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
        }
    }

    @Nullable
    public static <T> T get(Iterator<? extends T> iterator, int position, @Nullable T defaultValue) {
        Iterators.checkNonnegative(position);
        Iterators.advance(iterator, position);
        return Iterators.getNext(iterator, defaultValue);
    }

    @Nullable
    public static <T> T getNext(Iterator<? extends T> iterator, @Nullable T defaultValue) {
        return iterator.hasNext() ? iterator.next() : defaultValue;
    }

    public static <T> T getLast(Iterator<T> iterator) {
        T current;
        do {
            current = iterator.next();
        } while (iterator.hasNext());
        return current;
    }

    @Nullable
    public static <T> T getLast(Iterator<? extends T> iterator, @Nullable T defaultValue) {
        return iterator.hasNext() ? Iterators.getLast(iterator) : defaultValue;
    }

    public static int advance(Iterator<?> iterator, int numberToAdvance) {
        int i2;
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(numberToAdvance >= 0, "numberToAdvance must be nonnegative");
        for (i2 = 0; i2 < numberToAdvance && iterator.hasNext(); ++i2) {
            iterator.next();
        }
        return i2;
    }

    public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(limitSize >= 0, "limit is negative");
        return new Iterator<T>(){
            private int count;

            @Override
            public boolean hasNext() {
                return this.count < limitSize && iterator.hasNext();
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                ++this.count;
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        return new UnmodifiableIterator<T>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                Object next = iterator.next();
                iterator.remove();
                return next;
            }

            public String toString() {
                return "Iterators.consumingIterator(...)";
            }
        };
    }

    @Nullable
    static <T> T pollNext(Iterator<T> iterator) {
        if (iterator.hasNext()) {
            T result = iterator.next();
            iterator.remove();
            return result;
        }
        return null;
    }

    static void clear(Iterator<?> iterator) {
        Preconditions.checkNotNull(iterator);
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    public static <T> UnmodifiableIterator<T> forArray(T ... array) {
        return Iterators.forArray(array, 0, array.length, 0);
    }

    static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int offset, int length, int index) {
        Preconditions.checkArgument(length >= 0);
        int end = offset + length;
        Preconditions.checkPositionIndexes(offset, end, array.length);
        Preconditions.checkPositionIndex(index, length);
        if (length == 0) {
            return Iterators.emptyListIterator();
        }
        return new AbstractIndexedListIterator<T>(length, index){

            @Override
            protected T get(int index) {
                return array[offset + index];
            }
        };
    }

    public static <T> UnmodifiableIterator<T> singletonIterator(final @Nullable T value) {
        return new UnmodifiableIterator<T>(){
            boolean done;

            @Override
            public boolean hasNext() {
                return !this.done;
            }

            @Override
            public T next() {
                if (this.done) {
                    throw new NoSuchElementException();
                }
                this.done = true;
                return value;
            }
        };
    }

    public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
        Preconditions.checkNotNull(enumeration);
        return new UnmodifiableIterator<T>(){

            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public T next() {
                return enumeration.nextElement();
            }
        };
    }

    public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        return new Enumeration<T>(){

            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public T nextElement() {
                return iterator.next();
            }
        };
    }

    public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
        if (iterator instanceof PeekingImpl) {
            PeekingImpl peeking = (PeekingImpl)iterator;
            return peeking;
        }
        return new PeekingImpl<T>(iterator);
    }

    @Deprecated
    public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator) {
        return Preconditions.checkNotNull(iterator);
    }

    @Beta
    public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterators, "iterators");
        Preconditions.checkNotNull(comparator, "comparator");
        return new MergingIterator<T>(iterators, comparator);
    }

    static <T> ListIterator<T> cast(Iterator<T> iterator) {
        return (ListIterator)iterator;
    }

    private static class MergingIterator<T>
    extends UnmodifiableIterator<T> {
        final Queue<PeekingIterator<T>> queue;

        public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
            Comparator heapComparator = new Comparator<PeekingIterator<T>>(){

                @Override
                public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2) {
                    return itemComparator.compare(o1.peek(), o2.peek());
                }
            };
            this.queue = new PriorityQueue<PeekingIterator<T>>(2, heapComparator);
            for (Iterator<T> iterator : iterators) {
                if (!iterator.hasNext()) continue;
                this.queue.add(Iterators.peekingIterator(iterator));
            }
        }

        @Override
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override
        public T next() {
            PeekingIterator<T> nextIter = this.queue.remove();
            T next = nextIter.next();
            if (nextIter.hasNext()) {
                this.queue.add(nextIter);
            }
            return next;
        }
    }

    private static class PeekingImpl<E>
    implements PeekingIterator<E> {
        private final Iterator<? extends E> iterator;
        private boolean hasPeeked;
        private E peekedElement;

        public PeekingImpl(Iterator<? extends E> iterator) {
            this.iterator = Preconditions.checkNotNull(iterator);
        }

        @Override
        public boolean hasNext() {
            return this.hasPeeked || this.iterator.hasNext();
        }

        @Override
        public E next() {
            if (!this.hasPeeked) {
                return this.iterator.next();
            }
            E result = this.peekedElement;
            this.hasPeeked = false;
            this.peekedElement = null;
            return result;
        }

        @Override
        public void remove() {
            Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
            this.iterator.remove();
        }

        @Override
        public E peek() {
            if (!this.hasPeeked) {
                this.peekedElement = this.iterator.next();
                this.hasPeeked = true;
            }
            return this.peekedElement;
        }
    }
}

