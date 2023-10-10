// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Comparator;
import java.util.Queue;
import java.util.NoSuchElementException;
import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import java.util.function.Consumer;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.Spliterator;
import java.util.Set;
import com.google.common.annotations.GwtIncompatible;
import java.util.Iterator;
import com.google.common.base.Predicate;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Iterables
{
    private Iterables() {
    }
    
    public static <T> Iterable<T> unmodifiableIterable(final Iterable<? extends T> iterable) {
        Preconditions.checkNotNull(iterable);
        if (iterable instanceof UnmodifiableIterable || iterable instanceof ImmutableCollection) {
            final Iterable<T> result = (Iterable<T>)iterable;
            return result;
        }
        return new UnmodifiableIterable<T>((Iterable)iterable);
    }
    
    @Deprecated
    public static <E> Iterable<E> unmodifiableIterable(final ImmutableCollection<E> iterable) {
        return Preconditions.checkNotNull(iterable);
    }
    
    public static int size(final Iterable<?> iterable) {
        return (iterable instanceof Collection) ? ((Collection)iterable).size() : Iterators.size(iterable.iterator());
    }
    
    public static boolean contains(final Iterable<?> iterable, @CheckForNull final Object element) {
        if (iterable instanceof Collection) {
            final Collection<?> collection = (Collection<?>)(Collection)iterable;
            return Collections2.safeContains(collection, element);
        }
        return Iterators.contains(iterable.iterator(), element);
    }
    
    @CanIgnoreReturnValue
    public static boolean removeAll(final Iterable<?> removeFrom, final Collection<?> elementsToRemove) {
        return (removeFrom instanceof Collection) ? ((Collection)removeFrom).removeAll(Preconditions.checkNotNull(elementsToRemove)) : Iterators.removeAll(removeFrom.iterator(), elementsToRemove);
    }
    
    @CanIgnoreReturnValue
    public static boolean retainAll(final Iterable<?> removeFrom, final Collection<?> elementsToRetain) {
        return (removeFrom instanceof Collection) ? ((Collection)removeFrom).retainAll(Preconditions.checkNotNull(elementsToRetain)) : Iterators.retainAll(removeFrom.iterator(), elementsToRetain);
    }
    
    @CanIgnoreReturnValue
    public static <T> boolean removeIf(final Iterable<T> removeFrom, final Predicate<? super T> predicate) {
        if (removeFrom instanceof Collection) {
            return ((Collection)removeFrom).removeIf(predicate);
        }
        return Iterators.removeIf(removeFrom.iterator(), predicate);
    }
    
    @CheckForNull
    static <T> T removeFirstMatching(final Iterable<T> removeFrom, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        final Iterator<T> iterator = removeFrom.iterator();
        while (iterator.hasNext()) {
            final T next = iterator.next();
            if (predicate.apply((Object)next)) {
                iterator.remove();
                return next;
            }
        }
        return null;
    }
    
    public static boolean elementsEqual(final Iterable<?> iterable1, final Iterable<?> iterable2) {
        if (iterable1 instanceof Collection && iterable2 instanceof Collection) {
            final Collection<?> collection1 = (Collection<?>)(Collection)iterable1;
            final Collection<?> collection2 = (Collection<?>)(Collection)iterable2;
            if (collection1.size() != collection2.size()) {
                return false;
            }
        }
        return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
    }
    
    public static String toString(final Iterable<?> iterable) {
        return Iterators.toString(iterable.iterator());
    }
    
    @ParametricNullness
    public static <T> T getOnlyElement(final Iterable<T> iterable) {
        return Iterators.getOnlyElement(iterable.iterator());
    }
    
    @ParametricNullness
    public static <T> T getOnlyElement(final Iterable<? extends T> iterable, @ParametricNullness final T defaultValue) {
        return Iterators.getOnlyElement(iterable.iterator(), defaultValue);
    }
    
    @GwtIncompatible
    public static <T> T[] toArray(final Iterable<? extends T> iterable, final Class<T> type) {
        return toArray(iterable, (T[])ObjectArrays.newArray((Class<T>)type, 0));
    }
    
    static <T> T[] toArray(final Iterable<? extends T> iterable, final T[] array) {
        final Collection<? extends T> collection = castOrCopyToCollection(iterable);
        return collection.toArray(array);
    }
    
    static Object[] toArray(final Iterable<?> iterable) {
        return castOrCopyToCollection(iterable).toArray();
    }
    
    private static <E> Collection<E> castOrCopyToCollection(final Iterable<E> iterable) {
        return (Collection<E>)((iterable instanceof Collection) ? ((Collection)iterable) : Lists.newArrayList((Iterator<?>)iterable.iterator()));
    }
    
    @CanIgnoreReturnValue
    public static <T> boolean addAll(final Collection<T> addTo, final Iterable<? extends T> elementsToAdd) {
        if (elementsToAdd instanceof Collection) {
            final Collection<? extends T> c = (Collection<? extends T>)(Collection)elementsToAdd;
            return addTo.addAll(c);
        }
        return Iterators.addAll(addTo, Preconditions.checkNotNull(elementsToAdd).iterator());
    }
    
    public static int frequency(final Iterable<?> iterable, @CheckForNull final Object element) {
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
        return new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.cycle(iterable);
            }
            
            @Override
            public Spliterator<T> spliterator() {
                return (Spliterator<T>)Stream.generate(() -> iterable).flatMap((Function<? super Object, ? extends Stream<?>>)Streams::stream).spliterator();
            }
            
            @Override
            public String toString() {
                return String.valueOf(iterable.toString()).concat(" (cycled)");
            }
        };
    }
    
    @SafeVarargs
    public static <T> Iterable<T> cycle(final T... elements) {
        return cycle(Lists.newArrayList(elements));
    }
    
    public static <T> Iterable<T> concat(final Iterable<? extends T> a, final Iterable<? extends T> b) {
        return (Iterable<T>)FluentIterable.concat((Iterable<?>)a, (Iterable<?>)b);
    }
    
    public static <T> Iterable<T> concat(final Iterable<? extends T> a, final Iterable<? extends T> b, final Iterable<? extends T> c) {
        return (Iterable<T>)FluentIterable.concat((Iterable<?>)a, (Iterable<?>)b, (Iterable<?>)c);
    }
    
    public static <T> Iterable<T> concat(final Iterable<? extends T> a, final Iterable<? extends T> b, final Iterable<? extends T> c, final Iterable<? extends T> d) {
        return (Iterable<T>)FluentIterable.concat((Iterable<?>)a, (Iterable<?>)b, (Iterable<?>)c, (Iterable<?>)d);
    }
    
    @SafeVarargs
    public static <T> Iterable<T> concat(final Iterable<? extends T>... inputs) {
        return (Iterable<T>)FluentIterable.concat((Iterable<?>[])inputs);
    }
    
    public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> inputs) {
        return (Iterable<T>)FluentIterable.concat((Iterable<? extends Iterable<?>>)inputs);
    }
    
    public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(size > 0);
        return new FluentIterable<List<T>>() {
            @Override
            public Iterator<List<T>> iterator() {
                return (Iterator<List<T>>)Iterators.partition(iterable.iterator(), size);
            }
        };
    }
    
    public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(size > 0);
        return new FluentIterable<List<T>>() {
            @Override
            public Iterator<List<T>> iterator() {
                return (Iterator<List<T>>)Iterators.paddedPartition(iterable.iterator(), size);
            }
        };
    }
    
    public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> retainIfTrue) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(retainIfTrue);
        return new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return (Iterator<T>)Iterators.filter(unfiltered.iterator(), retainIfTrue);
            }
            
            @Override
            public void forEach(final Consumer<? super T> action) {
                Preconditions.checkNotNull(action);
                unfiltered.forEach(a -> {
                    if (retainIfTrue.test(a)) {
                        action.accept((Object)a);
                    }
                });
            }
            
            @Override
            public Spliterator<T> spliterator() {
                return CollectSpliterators.filter(unfiltered.spliterator(), retainIfTrue);
            }
        };
    }
    
    @GwtIncompatible
    public static <T> Iterable<T> filter(final Iterable<?> unfiltered, final Class<T> desiredType) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(desiredType);
        return filter(unfiltered, Predicates.instanceOf(desiredType));
    }
    
    public static <T> boolean any(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.any(iterable.iterator(), predicate);
    }
    
    public static <T> boolean all(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.all(iterable.iterator(), predicate);
    }
    
    @ParametricNullness
    public static <T> T find(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.find(iterable.iterator(), predicate);
    }
    
    @CheckForNull
    public static <T> T find(final Iterable<? extends T> iterable, final Predicate<? super T> predicate, @CheckForNull final T defaultValue) {
        return Iterators.find(iterable.iterator(), predicate, defaultValue);
    }
    
    public static <T> Optional<T> tryFind(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.tryFind(iterable.iterator(), predicate);
    }
    
    public static <T> int indexOf(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.indexOf(iterable.iterator(), predicate);
    }
    
    public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final com.google.common.base.Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(fromIterable);
        Preconditions.checkNotNull(function);
        return new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.transform(fromIterable.iterator(), (com.google.common.base.Function<? super Object, ? extends T>)function);
            }
            
            @Override
            public void forEach(final Consumer<? super T> action) {
                Preconditions.checkNotNull(action);
                fromIterable.forEach(f -> action.accept((Object)function.apply(f)));
            }
            
            @Override
            public Spliterator<T> spliterator() {
                return CollectSpliterators.map(fromIterable.spliterator(), (Function<? super Object, ? extends T>)function);
            }
        };
    }
    
    @ParametricNullness
    public static <T> T get(final Iterable<T> iterable, final int position) {
        Preconditions.checkNotNull(iterable);
        return (iterable instanceof List) ? ((List)iterable).get(position) : Iterators.get(iterable.iterator(), position);
    }
    
    @ParametricNullness
    public static <T> T get(final Iterable<? extends T> iterable, final int position, @ParametricNullness final T defaultValue) {
        Preconditions.checkNotNull(iterable);
        Iterators.checkNonnegative(position);
        if (iterable instanceof List) {
            final List<? extends T> list = Lists.cast(iterable);
            return (position < list.size()) ? list.get(position) : defaultValue;
        }
        final Iterator<? extends T> iterator = iterable.iterator();
        Iterators.advance(iterator, position);
        return Iterators.getNext(iterator, defaultValue);
    }
    
    @ParametricNullness
    public static <T> T getFirst(final Iterable<? extends T> iterable, @ParametricNullness final T defaultValue) {
        return Iterators.getNext(iterable.iterator(), defaultValue);
    }
    
    @ParametricNullness
    public static <T> T getLast(final Iterable<T> iterable) {
        if (!(iterable instanceof List)) {
            return Iterators.getLast(iterable.iterator());
        }
        final List<T> list = (List<T>)(List)iterable;
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return getLastInNonemptyList(list);
    }
    
    @ParametricNullness
    public static <T> T getLast(final Iterable<? extends T> iterable, @ParametricNullness final T defaultValue) {
        if (iterable instanceof Collection) {
            final Collection<? extends T> c = (Collection<? extends T>)(Collection)iterable;
            if (c.isEmpty()) {
                return defaultValue;
            }
            if (iterable instanceof List) {
                return getLastInNonemptyList((List<T>)Lists.cast((Iterable<T>)iterable));
            }
        }
        return Iterators.getLast(iterable.iterator(), defaultValue);
    }
    
    @ParametricNullness
    private static <T> T getLastInNonemptyList(final List<T> list) {
        return list.get(list.size() - 1);
    }
    
    public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(numberToSkip >= 0, (Object)"number to skip cannot be negative");
        return new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                if (iterable instanceof List) {
                    final List<T> list = (List<T>)(List)iterable;
                    final int toSkip = Math.min(list.size(), numberToSkip);
                    return list.subList(toSkip, list.size()).iterator();
                }
                final Iterator<T> iterator = iterable.iterator();
                Iterators.advance(iterator, numberToSkip);
                return new Iterator<T>(this) {
                    boolean atStart = true;
                    
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }
                    
                    @ParametricNullness
                    @Override
                    public T next() {
                        final T result = iterator.next();
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
            
            @Override
            public Spliterator<T> spliterator() {
                if (iterable instanceof List) {
                    final List<T> list = (List<T>)(List)iterable;
                    final int toSkip = Math.min(list.size(), numberToSkip);
                    return list.subList(toSkip, list.size()).spliterator();
                }
                return (Spliterator<T>)Streams.stream((Iterable<Object>)iterable).skip(numberToSkip).spliterator();
            }
        };
    }
    
    public static <T> Iterable<T> limit(final Iterable<T> iterable, final int limitSize) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(limitSize >= 0, (Object)"limit is negative");
        return new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.limit(iterable.iterator(), limitSize);
            }
            
            @Override
            public Spliterator<T> spliterator() {
                return (Spliterator<T>)Streams.stream((Iterable<Object>)iterable).limit(limitSize).spliterator();
            }
        };
    }
    
    public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return (iterable instanceof Queue) ? new ConsumingQueueIterator<T>((Queue<T>)iterable) : Iterators.consumingIterator(iterable.iterator());
            }
            
            @Override
            public String toString() {
                return "Iterables.consumingIterable(...)";
            }
        };
    }
    
    public static boolean isEmpty(final Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection)iterable).isEmpty();
        }
        return !iterable.iterator().hasNext();
    }
    
    @Beta
    public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterables, final Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterables, (Object)"iterables");
        Preconditions.checkNotNull(comparator, (Object)"comparator");
        final Iterable<T> iterable = new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return (Iterator<T>)Iterators.mergeSorted(Iterables.transform((Iterable<Object>)iterables, (com.google.common.base.Function<? super Object, ? extends Iterator<?>>)Iterables.toIterator()), (Comparator<? super Object>)comparator);
            }
        };
        return new UnmodifiableIterable<T>((Iterable)iterable);
    }
    
    static <T> com.google.common.base.Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
        return new com.google.common.base.Function<Iterable<? extends T>, Iterator<? extends T>>() {
            @Override
            public Iterator<? extends T> apply(final Iterable<? extends T> iterable) {
                return iterable.iterator();
            }
        };
    }
    
    private static final class UnmodifiableIterable<T> extends FluentIterable<T>
    {
        private final Iterable<? extends T> iterable;
        
        private UnmodifiableIterable(final Iterable<? extends T> iterable) {
            this.iterable = iterable;
        }
        
        @Override
        public Iterator<T> iterator() {
            return (Iterator<T>)Iterators.unmodifiableIterator((Iterator<?>)this.iterable.iterator());
        }
        
        @Override
        public void forEach(final Consumer<? super T> action) {
            this.iterable.forEach(action);
        }
        
        @Override
        public Spliterator<T> spliterator() {
            return (Spliterator<T>)this.iterable.spliterator();
        }
        
        @Override
        public String toString() {
            return this.iterable.toString();
        }
    }
}
