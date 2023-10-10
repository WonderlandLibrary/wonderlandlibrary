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
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.TransformedIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Iterables {
    private Iterables() {
    }

    public static <T> Iterable<T> unmodifiableIterable(Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        if (iterable instanceof UnmodifiableIterable || iterable instanceof ImmutableCollection) {
            return iterable;
        }
        return new UnmodifiableIterable(iterable);
    }

    @Deprecated
    public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> iterable) {
        return Preconditions.checkNotNull(iterable);
    }

    public static int size(Iterable<?> iterable) {
        return iterable instanceof Collection ? ((Collection)iterable).size() : Iterators.size(iterable.iterator());
    }

    public static boolean contains(Iterable<?> iterable, @Nullable Object element) {
        if (iterable instanceof Collection) {
            Collection collection = (Collection)iterable;
            return Collections2.safeContains(collection, element);
        }
        return Iterators.contains(iterable.iterator(), element);
    }

    public static boolean removeAll(Iterable<?> removeFrom, Collection<?> elementsToRemove) {
        return removeFrom instanceof Collection ? ((Collection)removeFrom).removeAll(Preconditions.checkNotNull(elementsToRemove)) : Iterators.removeAll(removeFrom.iterator(), elementsToRemove);
    }

    public static boolean retainAll(Iterable<?> removeFrom, Collection<?> elementsToRetain) {
        return removeFrom instanceof Collection ? ((Collection)removeFrom).retainAll(Preconditions.checkNotNull(elementsToRetain)) : Iterators.retainAll(removeFrom.iterator(), elementsToRetain);
    }

    public static <T> boolean removeIf(Iterable<T> removeFrom, Predicate<? super T> predicate) {
        if (removeFrom instanceof RandomAccess && removeFrom instanceof List) {
            return Iterables.removeIfFromRandomAccessList((List)removeFrom, Preconditions.checkNotNull(predicate));
        }
        return Iterators.removeIf(removeFrom.iterator(), predicate);
    }

    private static <T> boolean removeIfFromRandomAccessList(List<T> list, Predicate<? super T> predicate) {
        int from;
        int to = 0;
        for (from = 0; from < list.size(); ++from) {
            T element = list.get(from);
            if (predicate.apply(element)) continue;
            if (from > to) {
                try {
                    list.set(to, element);
                }
                catch (UnsupportedOperationException e2) {
                    Iterables.slowRemoveIfForRemainingElements(list, predicate, to, from);
                    return true;
                }
            }
            ++to;
        }
        list.subList(to, list.size()).clear();
        return from != to;
    }

    private static <T> void slowRemoveIfForRemainingElements(List<T> list, Predicate<? super T> predicate, int to, int from) {
        int n2;
        for (n2 = list.size() - 1; n2 > from; --n2) {
            if (!predicate.apply(list.get(n2))) continue;
            list.remove(n2);
        }
        for (n2 = from - 1; n2 >= to; --n2) {
            list.remove(n2);
        }
    }

    @Nullable
    static <T> T removeFirstMatching(Iterable<T> removeFrom, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        Iterator<T> iterator = removeFrom.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (!predicate.apply(next)) continue;
            iterator.remove();
            return next;
        }
        return null;
    }

    public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
        if (iterable1 instanceof Collection && iterable2 instanceof Collection) {
            Collection collection1 = (Collection)iterable1;
            Collection collection2 = (Collection)iterable2;
            if (collection1.size() != collection2.size()) {
                return false;
            }
        }
        return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
    }

    public static String toString(Iterable<?> iterable) {
        return Iterators.toString(iterable.iterator());
    }

    public static <T> T getOnlyElement(Iterable<T> iterable) {
        return Iterators.getOnlyElement(iterable.iterator());
    }

    @Nullable
    public static <T> T getOnlyElement(Iterable<? extends T> iterable, @Nullable T defaultValue) {
        return Iterators.getOnlyElement(iterable.iterator(), defaultValue);
    }

    @GwtIncompatible(value="Array.newInstance(Class, int)")
    public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
        Collection<T> collection = Iterables.toCollection(iterable);
        T[] array = ObjectArrays.newArray(type, collection.size());
        return collection.toArray(array);
    }

    static Object[] toArray(Iterable<?> iterable) {
        return Iterables.toCollection(iterable).toArray();
    }

    private static <E> Collection<E> toCollection(Iterable<E> iterable) {
        return iterable instanceof Collection ? (ArrayList<E>)iterable : Lists.newArrayList(iterable.iterator());
    }

    public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
        if (elementsToAdd instanceof Collection) {
            Collection<? extends T> c2 = Collections2.cast(elementsToAdd);
            return addTo.addAll(c2);
        }
        return Iterators.addAll(addTo, Preconditions.checkNotNull(elementsToAdd).iterator());
    }

    public static int frequency(Iterable<?> iterable, @Nullable Object element) {
        if (iterable instanceof Multiset) {
            return ((Multiset)iterable).count(element);
        }
        if (iterable instanceof Set) {
            return ((Set)iterable).contains(element) ? 1 : 0;
        }
        return Iterators.frequency(iterable.iterator(), element);
    }

    public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return Iterators.cycle(iterable);
            }

            @Override
            public String toString() {
                return iterable.toString() + " (cycled)";
            }
        };
    }

    public static <T> Iterable<T> cycle(T ... elements) {
        return Iterables.cycle(Lists.newArrayList(elements));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> a2, Iterable<? extends T> b2) {
        return Iterables.concat(ImmutableList.of(a2, b2));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> a2, Iterable<? extends T> b2, Iterable<? extends T> c2) {
        return Iterables.concat(ImmutableList.of(a2, b2, c2));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> a2, Iterable<? extends T> b2, Iterable<? extends T> c2, Iterable<? extends T> d2) {
        return Iterables.concat(ImmutableList.of(a2, b2, c2, d2));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> ... inputs) {
        return Iterables.concat(ImmutableList.copyOf(inputs));
    }

    public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> inputs) {
        Preconditions.checkNotNull(inputs);
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return Iterators.concat(Iterables.iterators(inputs));
            }
        };
    }

    private static <T> Iterator<Iterator<? extends T>> iterators(Iterable<? extends Iterable<? extends T>> iterables) {
        return new TransformedIterator<Iterable<? extends T>, Iterator<? extends T>>(iterables.iterator()){

            @Override
            Iterator<? extends T> transform(Iterable<? extends T> from) {
                return from.iterator();
            }
        };
    }

    public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(size > 0);
        return new FluentIterable<List<T>>(){

            @Override
            public Iterator<List<T>> iterator() {
                return Iterators.partition(iterable.iterator(), size);
            }
        };
    }

    public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(size > 0);
        return new FluentIterable<List<T>>(){

            @Override
            public Iterator<List<T>> iterator() {
                return Iterators.paddedPartition(iterable.iterator(), size);
            }
        };
    }

    public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(predicate);
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return Iterators.filter(unfiltered.iterator(), predicate);
            }
        };
    }

    @GwtIncompatible(value="Class.isInstance")
    public static <T> Iterable<T> filter(final Iterable<?> unfiltered, final Class<T> type) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(type);
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return Iterators.filter(unfiltered.iterator(), type);
            }
        };
    }

    public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.any(iterable.iterator(), predicate);
    }

    public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.all(iterable.iterator(), predicate);
    }

    public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.find(iterable.iterator(), predicate);
    }

    @Nullable
    public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, @Nullable T defaultValue) {
        return Iterators.find(iterable.iterator(), predicate, defaultValue);
    }

    public static <T> Optional<T> tryFind(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.tryFind(iterable.iterator(), predicate);
    }

    public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.indexOf(iterable.iterator(), predicate);
    }

    public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(fromIterable);
        Preconditions.checkNotNull(function);
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return Iterators.transform(fromIterable.iterator(), function);
            }
        };
    }

    public static <T> T get(Iterable<T> iterable, int position) {
        Preconditions.checkNotNull(iterable);
        return (T)(iterable instanceof List ? ((List)iterable).get(position) : Iterators.get(iterable.iterator(), position));
    }

    @Nullable
    public static <T> T get(Iterable<? extends T> iterable, int position, @Nullable T defaultValue) {
        Preconditions.checkNotNull(iterable);
        Iterators.checkNonnegative(position);
        if (iterable instanceof List) {
            List<T> list = Lists.cast(iterable);
            return position < list.size() ? list.get(position) : defaultValue;
        }
        Iterator<? extends T> iterator = iterable.iterator();
        Iterators.advance(iterator, position);
        return Iterators.getNext(iterator, defaultValue);
    }

    @Nullable
    public static <T> T getFirst(Iterable<? extends T> iterable, @Nullable T defaultValue) {
        return Iterators.getNext(iterable.iterator(), defaultValue);
    }

    public static <T> T getLast(Iterable<T> iterable) {
        if (iterable instanceof List) {
            List list = (List)iterable;
            if (list.isEmpty()) {
                throw new NoSuchElementException();
            }
            return Iterables.getLastInNonemptyList(list);
        }
        return Iterators.getLast(iterable.iterator());
    }

    @Nullable
    public static <T> T getLast(Iterable<? extends T> iterable, @Nullable T defaultValue) {
        if (iterable instanceof Collection) {
            Collection<T> c2 = Collections2.cast(iterable);
            if (c2.isEmpty()) {
                return defaultValue;
            }
            if (iterable instanceof List) {
                return Iterables.getLastInNonemptyList(Lists.cast(iterable));
            }
        }
        return Iterators.getLast(iterable.iterator(), defaultValue);
    }

    private static <T> T getLastInNonemptyList(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(numberToSkip >= 0, "number to skip cannot be negative");
        if (iterable instanceof List) {
            final List list = (List)iterable;
            return new FluentIterable<T>(){

                @Override
                public Iterator<T> iterator() {
                    int toSkip = Math.min(list.size(), numberToSkip);
                    return list.subList(toSkip, list.size()).iterator();
                }
            };
        }
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                final Iterator iterator = iterable.iterator();
                Iterators.advance(iterator, numberToSkip);
                return new Iterator<T>(){
                    boolean atStart = true;

                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public T next() {
                        Object result = iterator.next();
                        this.atStart = false;
                        return result;
                    }

                    @Override
                    public void remove() {
                        CollectPreconditions.checkRemove(!this.atStart);
                        iterator.remove();
                    }
                };
            }
        };
    }

    public static <T> Iterable<T> limit(final Iterable<T> iterable, final int limitSize) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(limitSize >= 0, "limit is negative");
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return Iterators.limit(iterable.iterator(), limitSize);
            }
        };
    }

    public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
        if (iterable instanceof Queue) {
            return new FluentIterable<T>(){

                @Override
                public Iterator<T> iterator() {
                    return new ConsumingQueueIterator((Queue)iterable);
                }

                @Override
                public String toString() {
                    return "Iterables.consumingIterable(...)";
                }
            };
        }
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return Iterators.consumingIterator(iterable.iterator());
            }

            @Override
            public String toString() {
                return "Iterables.consumingIterable(...)";
            }
        };
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection)iterable).isEmpty();
        }
        return !iterable.iterator().hasNext();
    }

    @Beta
    public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterables, final Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterables, "iterables");
        Preconditions.checkNotNull(comparator, "comparator");
        FluentIterable iterable = new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return Iterators.mergeSorted(Iterables.transform(iterables, Iterables.toIterator()), comparator);
            }
        };
        return new UnmodifiableIterable(iterable);
    }

    private static <T> Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
        return new Function<Iterable<? extends T>, Iterator<? extends T>>(){

            @Override
            public Iterator<? extends T> apply(Iterable<? extends T> iterable) {
                return iterable.iterator();
            }
        };
    }

    private static class ConsumingQueueIterator<T>
    extends AbstractIterator<T> {
        private final Queue<T> queue;

        private ConsumingQueueIterator(Queue<T> queue) {
            this.queue = queue;
        }

        @Override
        public T computeNext() {
            try {
                return this.queue.remove();
            }
            catch (NoSuchElementException e2) {
                return this.endOfData();
            }
        }
    }

    private static final class UnmodifiableIterable<T>
    extends FluentIterable<T> {
        private final Iterable<T> iterable;

        private UnmodifiableIterable(Iterable<T> iterable) {
            this.iterable = iterable;
        }

        @Override
        public Iterator<T> iterator() {
            return Iterators.unmodifiableIterator(this.iterable.iterator());
        }

        @Override
        public String toString() {
            return this.iterable.toString();
        }
    }
}

