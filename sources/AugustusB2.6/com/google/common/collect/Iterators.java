// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ListIterator;
import com.google.common.annotations.Beta;
import java.util.Comparator;
import java.util.Enumeration;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Arrays;
import java.util.NoSuchElementException;
import com.google.common.annotations.GwtIncompatible;
import java.util.List;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import javax.annotation.CheckForNull;
import com.google.common.primitives.Ints;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Iterators
{
    private Iterators() {
    }
    
    static <T> UnmodifiableIterator<T> emptyIterator() {
        return (UnmodifiableIterator<T>)emptyListIterator();
    }
    
    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return (UnmodifiableListIterator<T>)ArrayItr.EMPTY;
    }
    
    static <T> Iterator<T> emptyModifiableIterator() {
        return (Iterator<T>)EmptyModifiableIterator.INSTANCE;
    }
    
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<? extends T> iterator) {
        Preconditions.checkNotNull(iterator);
        if (iterator instanceof UnmodifiableIterator) {
            final UnmodifiableIterator<T> result = (UnmodifiableIterator<T>)(UnmodifiableIterator)iterator;
            return result;
        }
        return new UnmodifiableIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @ParametricNullness
            @Override
            public T next() {
                return iterator.next();
            }
        };
    }
    
    @Deprecated
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final UnmodifiableIterator<T> iterator) {
        return Preconditions.checkNotNull(iterator);
    }
    
    public static int size(final Iterator<?> iterator) {
        long count = 0L;
        while (iterator.hasNext()) {
            iterator.next();
            ++count;
        }
        return Ints.saturatedCast(count);
    }
    
    public static boolean contains(final Iterator<?> iterator, @CheckForNull final Object element) {
        if (element == null) {
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    return true;
                }
            }
        }
        else {
            while (iterator.hasNext()) {
                if (element.equals(iterator.next())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @CanIgnoreReturnValue
    public static boolean removeAll(final Iterator<?> removeFrom, final Collection<?> elementsToRemove) {
        Preconditions.checkNotNull(elementsToRemove);
        boolean result = false;
        while (removeFrom.hasNext()) {
            if (elementsToRemove.contains(removeFrom.next())) {
                removeFrom.remove();
                result = true;
            }
        }
        return result;
    }
    
    @CanIgnoreReturnValue
    public static <T> boolean removeIf(final Iterator<T> removeFrom, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean modified = false;
        while (removeFrom.hasNext()) {
            if (predicate.apply((Object)removeFrom.next())) {
                removeFrom.remove();
                modified = true;
            }
        }
        return modified;
    }
    
    @CanIgnoreReturnValue
    public static boolean retainAll(final Iterator<?> removeFrom, final Collection<?> elementsToRetain) {
        Preconditions.checkNotNull(elementsToRetain);
        boolean result = false;
        while (removeFrom.hasNext()) {
            if (!elementsToRetain.contains(removeFrom.next())) {
                removeFrom.remove();
                result = true;
            }
        }
        return result;
    }
    
    public static boolean elementsEqual(final Iterator<?> iterator1, final Iterator<?> iterator2) {
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            final Object o1 = iterator1.next();
            final Object o2 = iterator2.next();
            if (!Objects.equal(o1, o2)) {
                return false;
            }
        }
        return !iterator2.hasNext();
    }
    
    public static String toString(final Iterator<?> iterator) {
        final StringBuilder sb = new StringBuilder().append('[');
        boolean first = true;
        while (iterator.hasNext()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(iterator.next());
        }
        return sb.append(']').toString();
    }
    
    @ParametricNullness
    public static <T> T getOnlyElement(final Iterator<T> iterator) {
        final T first = iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }
        final StringBuilder sb = new StringBuilder().append("expected one element but was: <").append(first);
        for (int i = 0; i < 4 && iterator.hasNext(); ++i) {
            sb.append(", ").append(iterator.next());
        }
        if (iterator.hasNext()) {
            sb.append(", ...");
        }
        sb.append('>');
        throw new IllegalArgumentException(sb.toString());
    }
    
    @ParametricNullness
    public static <T> T getOnlyElement(final Iterator<? extends T> iterator, @ParametricNullness final T defaultValue) {
        return iterator.hasNext() ? getOnlyElement((Iterator<T>)iterator) : defaultValue;
    }
    
    @GwtIncompatible
    public static <T> T[] toArray(final Iterator<? extends T> iterator, final Class<T> type) {
        final List<T> list = (List<T>)Lists.newArrayList((Iterator<?>)iterator);
        return Iterables.toArray((Iterable<? extends T>)list, type);
    }
    
    @CanIgnoreReturnValue
    public static <T> boolean addAll(final Collection<T> addTo, final Iterator<? extends T> iterator) {
        Preconditions.checkNotNull(addTo);
        Preconditions.checkNotNull(iterator);
        boolean wasModified = false;
        while (iterator.hasNext()) {
            wasModified |= addTo.add((T)iterator.next());
        }
        return wasModified;
    }
    
    public static int frequency(final Iterator<?> iterator, @CheckForNull final Object element) {
        int count = 0;
        while (contains(iterator, element)) {
            ++count;
        }
        return count;
    }
    
    public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new Iterator<T>() {
            Iterator<T> iterator = Iterators.emptyModifiableIterator();
            
            @Override
            public boolean hasNext() {
                return this.iterator.hasNext() || iterable.iterator().hasNext();
            }
            
            @ParametricNullness
            @Override
            public T next() {
                if (!this.iterator.hasNext()) {
                    this.iterator = iterable.iterator();
                    if (!this.iterator.hasNext()) {
                        throw new NoSuchElementException();
                    }
                }
                return this.iterator.next();
            }
            
            @Override
            public void remove() {
                this.iterator.remove();
            }
        };
    }
    
    @SafeVarargs
    public static <T> Iterator<T> cycle(final T... elements) {
        return cycle(Lists.newArrayList(elements));
    }
    
    private static <I extends Iterator<?>> Iterator<I> consumingForArray(final I... elements) {
        return new UnmodifiableIterator<I>() {
            int index = 0;
            
            @Override
            public boolean hasNext() {
                return this.index < elements.length;
            }
            
            @Override
            public I next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final I result = java.util.Objects.requireNonNull(elements[this.index]);
                elements[this.index] = null;
                ++this.index;
                return result;
            }
        };
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T> a, final Iterator<? extends T> b) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        return concat((Iterator<? extends Iterator<? extends T>>)consumingForArray(a, b));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T> a, final Iterator<? extends T> b, final Iterator<? extends T> c) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        Preconditions.checkNotNull(c);
        return concat((Iterator<? extends Iterator<? extends T>>)consumingForArray(a, b, c));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T> a, final Iterator<? extends T> b, final Iterator<? extends T> c, final Iterator<? extends T> d) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        Preconditions.checkNotNull(c);
        Preconditions.checkNotNull(d);
        return concat((Iterator<? extends Iterator<? extends T>>)consumingForArray(a, b, c, d));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T>... inputs) {
        return (Iterator<T>)concatNoDefensiveCopy((Iterator<?>[])Arrays.copyOf((Iterator<? extends T>[])inputs, inputs.length));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> inputs) {
        return new ConcatenatedIterator<T>(inputs);
    }
    
    static <T> Iterator<T> concatNoDefensiveCopy(final Iterator<? extends T>... inputs) {
        for (final Iterator<? extends T> input : Preconditions.checkNotNull(inputs)) {
            Preconditions.checkNotNull(input);
        }
        return concat((Iterator<? extends Iterator<? extends T>>)consumingForArray((Iterator<? extends T>[])inputs));
    }
    
    public static <T> UnmodifiableIterator<List<T>> partition(final Iterator<T> iterator, final int size) {
        return partitionImpl(iterator, size, false);
    }
    
    public static <T> UnmodifiableIterator<List<T>> paddedPartition(final Iterator<T> iterator, final int size) {
        return partitionImpl(iterator, size, true);
    }
    
    private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(size > 0);
        return new UnmodifiableIterator<List<T>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public List<T> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final T[] array = (T[])new Object[size];
                int count;
                for (count = 0; count < size && iterator.hasNext(); ++count) {
                    array[count] = iterator.next();
                }
                for (int i = count; i < size; ++i) {
                    array[i] = null;
                }
                final List<T> list = Collections.unmodifiableList((List<? extends T>)Arrays.asList((T[])array));
                if (pad || count == size) {
                    return list;
                }
                return list.subList(0, count);
            }
        };
    }
    
    public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> retainIfTrue) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(retainIfTrue);
        return new AbstractIterator<T>() {
            @CheckForNull
            @Override
            protected T computeNext() {
                while (unfiltered.hasNext()) {
                    final T element = unfiltered.next();
                    if (retainIfTrue.apply(element)) {
                        return element;
                    }
                }
                return this.endOfData();
            }
        };
    }
    
    @GwtIncompatible
    public static <T> UnmodifiableIterator<T> filter(final Iterator<?> unfiltered, final Class<T> desiredType) {
        return filter(unfiltered, Predicates.instanceOf(desiredType));
    }
    
    public static <T> boolean any(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static <T> boolean all(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            final T element = iterator.next();
            if (!predicate.apply((Object)element)) {
                return false;
            }
        }
        return true;
    }
    
    @ParametricNullness
    public static <T> T find(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            final T t = iterator.next();
            if (predicate.apply((Object)t)) {
                return t;
            }
        }
        throw new NoSuchElementException();
    }
    
    @CheckForNull
    public static <T> T find(final Iterator<? extends T> iterator, final Predicate<? super T> predicate, @CheckForNull final T defaultValue) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            final T t = (T)iterator.next();
            if (predicate.apply((Object)t)) {
                return t;
            }
        }
        return defaultValue;
    }
    
    public static <T> Optional<T> tryFind(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            final T t = iterator.next();
            if (predicate.apply((Object)t)) {
                return Optional.of(t);
            }
        }
        return Optional.absent();
    }
    
    public static <T> int indexOf(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate, (Object)"predicate");
        int i = 0;
        while (iterator.hasNext()) {
            final T current = iterator.next();
            if (predicate.apply((Object)current)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static <F, T> Iterator<T> transform(final Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(function);
        return new TransformedIterator<F, T>(fromIterator) {
            @ParametricNullness
            @Override
            T transform(@ParametricNullness final F from) {
                return function.apply(from);
            }
        };
    }
    
    @ParametricNullness
    public static <T> T get(final Iterator<T> iterator, final int position) {
        checkNonnegative(position);
        final int skipped = advance(iterator, position);
        if (!iterator.hasNext()) {
            throw new IndexOutOfBoundsException(new StringBuilder(91).append("position (").append(position).append(") must be less than the number of elements that remained (").append(skipped).append(")").toString());
        }
        return iterator.next();
    }
    
    @ParametricNullness
    public static <T> T get(final Iterator<? extends T> iterator, final int position, @ParametricNullness final T defaultValue) {
        checkNonnegative(position);
        advance(iterator, position);
        return getNext(iterator, defaultValue);
    }
    
    static void checkNonnegative(final int position) {
        if (position < 0) {
            throw new IndexOutOfBoundsException(new StringBuilder(43).append("position (").append(position).append(") must not be negative").toString());
        }
    }
    
    @ParametricNullness
    public static <T> T getNext(final Iterator<? extends T> iterator, @ParametricNullness final T defaultValue) {
        return iterator.hasNext() ? iterator.next() : defaultValue;
    }
    
    @ParametricNullness
    public static <T> T getLast(final Iterator<T> iterator) {
        T current;
        do {
            current = iterator.next();
        } while (iterator.hasNext());
        return current;
    }
    
    @ParametricNullness
    public static <T> T getLast(final Iterator<? extends T> iterator, @ParametricNullness final T defaultValue) {
        return iterator.hasNext() ? getLast((Iterator<T>)iterator) : defaultValue;
    }
    
    @CanIgnoreReturnValue
    public static int advance(final Iterator<?> iterator, final int numberToAdvance) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(numberToAdvance >= 0, (Object)"numberToAdvance must be nonnegative");
        int i;
        for (i = 0; i < numberToAdvance && iterator.hasNext(); ++i) {
            iterator.next();
        }
        return i;
    }
    
    public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(limitSize >= 0, (Object)"limit is negative");
        return new Iterator<T>() {
            private int count;
            
            @Override
            public boolean hasNext() {
                return this.count < limitSize && iterator.hasNext();
            }
            
            @ParametricNullness
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
        return new UnmodifiableIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @ParametricNullness
            @Override
            public T next() {
                final T next = iterator.next();
                iterator.remove();
                return next;
            }
            
            @Override
            public String toString() {
                return "Iterators.consumingIterator(...)";
            }
        };
    }
    
    @CheckForNull
    static <T> T pollNext(final Iterator<T> iterator) {
        if (iterator.hasNext()) {
            final T result = iterator.next();
            iterator.remove();
            return result;
        }
        return null;
    }
    
    static void clear(final Iterator<?> iterator) {
        Preconditions.checkNotNull(iterator);
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
    
    @SafeVarargs
    public static <T> UnmodifiableIterator<T> forArray(final T... array) {
        return forArray(array, 0, array.length, 0);
    }
    
    static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int offset, final int length, final int index) {
        Preconditions.checkArgument(length >= 0);
        final int end = offset + length;
        Preconditions.checkPositionIndexes(offset, end, array.length);
        Preconditions.checkPositionIndex(index, length);
        if (length == 0) {
            return emptyListIterator();
        }
        return new ArrayItr<T>(array, offset, length, index);
    }
    
    public static <T> UnmodifiableIterator<T> singletonIterator(@ParametricNullness final T value) {
        return new UnmodifiableIterator<T>() {
            boolean done;
            
            @Override
            public boolean hasNext() {
                return !this.done;
            }
            
            @ParametricNullness
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
        return new UnmodifiableIterator<T>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }
            
            @ParametricNullness
            @Override
            public T next() {
                return enumeration.nextElement();
            }
        };
    }
    
    public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        return new Enumeration<T>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }
            
            @ParametricNullness
            @Override
            public T nextElement() {
                return iterator.next();
            }
        };
    }
    
    public static <T> PeekingIterator<T> peekingIterator(final Iterator<? extends T> iterator) {
        if (iterator instanceof PeekingImpl) {
            final PeekingImpl<T> peeking = (PeekingImpl<T>)(PeekingImpl)iterator;
            return peeking;
        }
        return new PeekingImpl<T>(iterator);
    }
    
    @Deprecated
    public static <T> PeekingIterator<T> peekingIterator(final PeekingIterator<T> iterator) {
        return Preconditions.checkNotNull(iterator);
    }
    
    @Beta
    public static <T> UnmodifiableIterator<T> mergeSorted(final Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterators, (Object)"iterators");
        Preconditions.checkNotNull(comparator, (Object)"comparator");
        return new MergingIterator<T>(iterators, comparator);
    }
    
    static <T> ListIterator<T> cast(final Iterator<T> iterator) {
        return (ListIterator<T>)(ListIterator)iterator;
    }
    
    private enum EmptyModifiableIterator implements Iterator<Object>
    {
        INSTANCE;
        
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
        
        private static /* synthetic */ EmptyModifiableIterator[] $values() {
            return new EmptyModifiableIterator[] { EmptyModifiableIterator.INSTANCE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    private static final class ArrayItr<T> extends AbstractIndexedListIterator<T>
    {
        static final UnmodifiableListIterator<Object> EMPTY;
        private final T[] array;
        private final int offset;
        
        ArrayItr(final T[] array, final int offset, final int length, final int index) {
            super(length, index);
            this.array = array;
            this.offset = offset;
        }
        
        @ParametricNullness
        @Override
        protected T get(final int index) {
            return this.array[this.offset + index];
        }
        
        static {
            EMPTY = new ArrayItr<Object>(new Object[0], 0, 0, 0);
        }
    }
    
    private static class PeekingImpl<E> implements PeekingIterator<E>
    {
        private final Iterator<? extends E> iterator;
        private boolean hasPeeked;
        @CheckForNull
        private E peekedElement;
        
        public PeekingImpl(final Iterator<? extends E> iterator) {
            this.iterator = Preconditions.checkNotNull(iterator);
        }
        
        @Override
        public boolean hasNext() {
            return this.hasPeeked || this.iterator.hasNext();
        }
        
        @ParametricNullness
        @Override
        public E next() {
            if (!this.hasPeeked) {
                return (E)this.iterator.next();
            }
            final E result = NullnessCasts.uncheckedCastNullableTToT(this.peekedElement);
            this.hasPeeked = false;
            this.peekedElement = null;
            return result;
        }
        
        @Override
        public void remove() {
            Preconditions.checkState(!this.hasPeeked, (Object)"Can't remove after you've peeked at next");
            this.iterator.remove();
        }
        
        @ParametricNullness
        @Override
        public E peek() {
            if (!this.hasPeeked) {
                this.peekedElement = (E)this.iterator.next();
                this.hasPeeked = true;
            }
            return NullnessCasts.uncheckedCastNullableTToT(this.peekedElement);
        }
    }
    
    private static class MergingIterator<T> extends UnmodifiableIterator<T>
    {
        final Queue<PeekingIterator<T>> queue;
        
        public MergingIterator(final Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
            final Comparator<PeekingIterator<T>> heapComparator = new Comparator<PeekingIterator<T>>(this) {
                @Override
                public int compare(final PeekingIterator<T> o1, final PeekingIterator<T> o2) {
                    return itemComparator.compare(o1.peek(), o2.peek());
                }
            };
            this.queue = new PriorityQueue<PeekingIterator<T>>(2, heapComparator);
            for (final Iterator<? extends T> iterator : iterators) {
                if (iterator.hasNext()) {
                    this.queue.add(Iterators.peekingIterator(iterator));
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }
        
        @ParametricNullness
        @Override
        public T next() {
            final PeekingIterator<T> nextIter = this.queue.remove();
            final T next = nextIter.next();
            if (nextIter.hasNext()) {
                this.queue.add(nextIter);
            }
            return next;
        }
    }
    
    private static class ConcatenatedIterator<T> implements Iterator<T>
    {
        @CheckForNull
        private Iterator<? extends T> toRemove;
        private Iterator<? extends T> iterator;
        @CheckForNull
        private Iterator<? extends Iterator<? extends T>> topMetaIterator;
        @CheckForNull
        private Deque<Iterator<? extends Iterator<? extends T>>> metaIterators;
        
        ConcatenatedIterator(final Iterator<? extends Iterator<? extends T>> metaIterator) {
            this.iterator = (Iterator<? extends T>)Iterators.emptyIterator();
            this.topMetaIterator = Preconditions.checkNotNull(metaIterator);
        }
        
        @CheckForNull
        private Iterator<? extends Iterator<? extends T>> getTopMetaIterator() {
            while (this.topMetaIterator == null || !this.topMetaIterator.hasNext()) {
                if (this.metaIterators == null || this.metaIterators.isEmpty()) {
                    return null;
                }
                this.topMetaIterator = this.metaIterators.removeFirst();
            }
            return this.topMetaIterator;
        }
        
        @Override
        public boolean hasNext() {
            while (!Preconditions.checkNotNull(this.iterator).hasNext()) {
                this.topMetaIterator = this.getTopMetaIterator();
                if (this.topMetaIterator == null) {
                    return false;
                }
                this.iterator = (Iterator<? extends T>)this.topMetaIterator.next();
                if (!(this.iterator instanceof ConcatenatedIterator)) {
                    continue;
                }
                final ConcatenatedIterator<T> topConcat = (ConcatenatedIterator)this.iterator;
                this.iterator = topConcat.iterator;
                if (this.metaIterators == null) {
                    this.metaIterators = new ArrayDeque<Iterator<? extends Iterator<? extends T>>>();
                }
                this.metaIterators.addFirst(this.topMetaIterator);
                if (topConcat.metaIterators != null) {
                    while (!topConcat.metaIterators.isEmpty()) {
                        this.metaIterators.addFirst(topConcat.metaIterators.removeLast());
                    }
                }
                this.topMetaIterator = topConcat.topMetaIterator;
            }
            return true;
        }
        
        @ParametricNullness
        @Override
        public T next() {
            if (this.hasNext()) {
                this.toRemove = this.iterator;
                return (T)this.iterator.next();
            }
            throw new NoSuchElementException();
        }
        
        @Override
        public void remove() {
            if (this.toRemove == null) {
                throw new IllegalStateException("no calls to next() since the last call to remove()");
            }
            this.toRemove.remove();
            this.toRemove = null;
        }
    }
}
