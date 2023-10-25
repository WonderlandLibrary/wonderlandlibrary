/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.CollectSpliterators;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;

@GwtCompatible
public final class Collections2 {
    static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");

    private Collections2() {
    }

    public static <E> Collection<E> filter(Collection<E> unfiltered, com.google.common.base.Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredCollection) {
            return ((FilteredCollection)unfiltered).createCombined(predicate);
        }
        return new FilteredCollection<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }

    static boolean safeContains(Collection<?> collection, @Nullable Object object) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.contains(object);
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    static boolean safeRemove(Collection<?> collection, @Nullable Object object) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.remove(object);
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
        return new TransformedCollection<F, T>(fromCollection, function);
    }

    static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
        return Iterables.all(c, Predicates.in(self));
    }

    static String toStringImpl(final Collection<?> collection) {
        StringBuilder sb = Collections2.newStringBuilderForCollection(collection.size()).append('[');
        STANDARD_JOINER.appendTo(sb, Iterables.transform(collection, new Function<Object, Object>(){

            @Override
            public Object apply(Object input) {
                return input == collection ? "(this Collection)" : input;
            }
        }));
        return sb.append(']').toString();
    }

    static StringBuilder newStringBuilderForCollection(int size) {
        CollectPreconditions.checkNonnegative(size, "size");
        return new StringBuilder((int)Math.min((long)size * 8L, 0x40000000L));
    }

    static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection)iterable;
    }

    @Beta
    public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> elements) {
        return Collections2.orderedPermutations(elements, Ordering.natural());
    }

    @Beta
    public static <E> Collection<List<E>> orderedPermutations(Iterable<E> elements, Comparator<? super E> comparator) {
        return new OrderedPermutationCollection<E>(elements, comparator);
    }

    @Beta
    public static <E> Collection<List<E>> permutations(Collection<E> elements) {
        return new PermutationCollection<E>(ImmutableList.copyOf(elements));
    }

    private static boolean isPermutation(List<?> first, List<?> second) {
        if (first.size() != second.size()) {
            return false;
        }
        HashMultiset<?> firstMultiset = HashMultiset.create(first);
        HashMultiset<?> secondMultiset = HashMultiset.create(second);
        return firstMultiset.equals(secondMultiset);
    }

    private static boolean isPositiveInt(long n) {
        return n >= 0L && n <= Integer.MAX_VALUE;
    }

    private static class PermutationIterator<E>
    extends AbstractIterator<List<E>> {
        final List<E> list;
        final int[] c;
        final int[] o;
        int j;

        PermutationIterator(List<E> list) {
            this.list = new ArrayList<E>(list);
            int n = list.size();
            this.c = new int[n];
            this.o = new int[n];
            Arrays.fill(this.c, 0);
            Arrays.fill(this.o, 1);
            this.j = Integer.MAX_VALUE;
        }

        @Override
        protected List<E> computeNext() {
            if (this.j <= 0) {
                return (List)this.endOfData();
            }
            ImmutableList<E> next = ImmutableList.copyOf(this.list);
            this.calculateNextPermutation();
            return next;
        }

        void calculateNextPermutation() {
            block4: {
                int q;
                this.j = this.list.size() - 1;
                int s = 0;
                if (this.j == -1) {
                    return;
                }
                while (true) {
                    if ((q = this.c[this.j] + this.o[this.j]) < 0) {
                        this.switchDirection();
                        continue;
                    }
                    if (q != this.j + 1) break;
                    if (this.j != 0) {
                        ++s;
                        this.switchDirection();
                        continue;
                    }
                    break block4;
                    break;
                }
                Collections.swap(this.list, this.j - this.c[this.j] + s, this.j - q + s);
                this.c[this.j] = q;
            }
        }

        void switchDirection() {
            this.o[this.j] = -this.o[this.j];
            --this.j;
        }
    }

    private static final class PermutationCollection<E>
    extends AbstractCollection<List<E>> {
        final ImmutableList<E> inputList;

        PermutationCollection(ImmutableList<E> input) {
            this.inputList = input;
        }

        @Override
        public int size() {
            return IntMath.factorial(this.inputList.size());
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Iterator<List<E>> iterator() {
            return new PermutationIterator<E>(this.inputList);
        }

        @Override
        public boolean contains(@Nullable Object obj) {
            if (obj instanceof List) {
                List list = (List)obj;
                return Collections2.isPermutation(this.inputList, list);
            }
            return false;
        }

        @Override
        public String toString() {
            return "permutations(" + this.inputList + ")";
        }
    }

    private static final class OrderedPermutationIterator<E>
    extends AbstractIterator<List<E>> {
        List<E> nextPermutation;
        final Comparator<? super E> comparator;

        OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
            this.nextPermutation = Lists.newArrayList(list);
            this.comparator = comparator;
        }

        @Override
        protected List<E> computeNext() {
            if (this.nextPermutation == null) {
                return (List)this.endOfData();
            }
            ImmutableList<E> next = ImmutableList.copyOf(this.nextPermutation);
            this.calculateNextPermutation();
            return next;
        }

        void calculateNextPermutation() {
            int j = this.findNextJ();
            if (j == -1) {
                this.nextPermutation = null;
                return;
            }
            int l = this.findNextL(j);
            Collections.swap(this.nextPermutation, j, l);
            int n = this.nextPermutation.size();
            Collections.reverse(this.nextPermutation.subList(j + 1, n));
        }

        int findNextJ() {
            for (int k = this.nextPermutation.size() - 2; k >= 0; --k) {
                if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) >= 0) continue;
                return k;
            }
            return -1;
        }

        int findNextL(int j) {
            E ak = this.nextPermutation.get(j);
            for (int l = this.nextPermutation.size() - 1; l > j; --l) {
                if (this.comparator.compare(ak, this.nextPermutation.get(l)) >= 0) continue;
                return l;
            }
            throw new AssertionError((Object)"this statement should be unreachable");
        }
    }

    private static final class OrderedPermutationCollection<E>
    extends AbstractCollection<List<E>> {
        final ImmutableList<E> inputList;
        final Comparator<? super E> comparator;
        final int size;

        OrderedPermutationCollection(Iterable<E> input, Comparator<? super E> comparator) {
            this.inputList = Ordering.from(comparator).immutableSortedCopy(input);
            this.comparator = comparator;
            this.size = OrderedPermutationCollection.calculateSize(this.inputList, comparator);
        }

        private static <E> int calculateSize(List<E> sortedInputList, Comparator<? super E> comparator) {
            long permutations = 1L;
            int n = 1;
            int r = 1;
            while (n < sortedInputList.size()) {
                int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
                if (comparison < 0) {
                    permutations *= LongMath.binomial(n, r);
                    r = 0;
                    if (!Collections2.isPositiveInt(permutations)) {
                        return Integer.MAX_VALUE;
                    }
                }
                ++n;
                ++r;
            }
            if (!Collections2.isPositiveInt(permutations *= LongMath.binomial(n, r))) {
                return Integer.MAX_VALUE;
            }
            return (int)permutations;
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Iterator<List<E>> iterator() {
            return new OrderedPermutationIterator<E>(this.inputList, this.comparator);
        }

        @Override
        public boolean contains(@Nullable Object obj) {
            if (obj instanceof List) {
                List list = (List)obj;
                return Collections2.isPermutation(this.inputList, list);
            }
            return false;
        }

        @Override
        public String toString() {
            return "orderedPermutationCollection(" + this.inputList + ")";
        }
    }

    static class TransformedCollection<F, T>
    extends AbstractCollection<T> {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;

        TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
            this.fromCollection = Preconditions.checkNotNull(fromCollection);
            this.function = Preconditions.checkNotNull(function);
        }

        @Override
        public void clear() {
            this.fromCollection.clear();
        }

        @Override
        public boolean isEmpty() {
            return this.fromCollection.isEmpty();
        }

        @Override
        public Iterator<T> iterator() {
            return Iterators.transform(this.fromCollection.iterator(), this.function);
        }

        @Override
        public Spliterator<T> spliterator() {
            return CollectSpliterators.map(this.fromCollection.spliterator(), this.function);
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            Preconditions.checkNotNull(action);
            this.fromCollection.forEach(f -> action.accept((T)this.function.apply(f)));
        }

        @Override
        public boolean removeIf(Predicate<? super T> filter) {
            Preconditions.checkNotNull(filter);
            return this.fromCollection.removeIf((? super E element) -> filter.test((T)this.function.apply(element)));
        }

        @Override
        public int size() {
            return this.fromCollection.size();
        }
    }

    static class FilteredCollection<E>
    extends AbstractCollection<E> {
        final Collection<E> unfiltered;
        final com.google.common.base.Predicate<? super E> predicate;

        FilteredCollection(Collection<E> unfiltered, com.google.common.base.Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }

        FilteredCollection<E> createCombined(com.google.common.base.Predicate<? super E> newPredicate) {
            return new FilteredCollection<E>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }

        @Override
        public boolean add(E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }

        @Override
        public boolean addAll(Collection<? extends E> collection) {
            for (E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll(collection);
        }

        @Override
        public void clear() {
            Iterables.removeIf(this.unfiltered, this.predicate);
        }

        @Override
        public boolean contains(@Nullable Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                Object e = element;
                return this.predicate.apply(e);
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }

        @Override
        public boolean isEmpty() {
            return !Iterables.any(this.unfiltered, this.predicate);
        }

        @Override
        public Iterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        @Override
        public Spliterator<E> spliterator() {
            return CollectSpliterators.filter(this.unfiltered.spliterator(), this.predicate);
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            Preconditions.checkNotNull(action);
            this.unfiltered.forEach((? super T e) -> {
                if (this.predicate.test(e)) {
                    action.accept((E)e);
                }
            });
        }

        @Override
        public boolean remove(Object element) {
            return this.contains(element) && this.unfiltered.remove(element);
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return this.removeIf(collection::contains);
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return this.removeIf(element -> !collection.contains(element));
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            Preconditions.checkNotNull(filter);
            return this.unfiltered.removeIf(element -> this.predicate.apply(element) && filter.test((E)element));
        }

        @Override
        public int size() {
            return Iterators.size(this.iterator());
        }

        @Override
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }

        @Override
        public <T> T[] toArray(T[] array) {
            return Lists.newArrayList(this.iterator()).toArray(array);
        }
    }
}

