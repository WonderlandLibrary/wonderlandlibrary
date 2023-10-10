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
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.CartesianList;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingNavigableSet;
import com.google.common.collect.ForwardingSortedSet;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableEnumSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.Platform;
import com.google.common.collect.Synchronized;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Sets {
    private Sets() {
    }

    @GwtCompatible(serializable=true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E ... otherElements) {
        return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
    }

    @GwtCompatible(serializable=true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
        if (elements instanceof ImmutableEnumSet) {
            return (ImmutableEnumSet)elements;
        }
        if (elements instanceof Collection) {
            Collection collection = (Collection)elements;
            if (collection.isEmpty()) {
                return ImmutableSet.of();
            }
            return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
        }
        Iterator<E> itr = elements.iterator();
        if (itr.hasNext()) {
            EnumSet<Enum> enumSet = EnumSet.of((Enum)itr.next());
            Iterators.addAll(enumSet, itr);
            return ImmutableEnumSet.asImmutable(enumSet);
        }
        return ImmutableSet.of();
    }

    public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
        EnumSet<E> set = EnumSet.noneOf(elementType);
        Iterables.addAll(set, iterable);
        return set;
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet();
    }

    public static <E> HashSet<E> newHashSet(E ... elements) {
        HashSet<E> set = Sets.newHashSetWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet(Maps.capacity(expectedSize));
    }

    public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
        return elements instanceof Collection ? new HashSet<E>(Collections2.cast(elements)) : Sets.newHashSet(elements.iterator());
    }

    public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
        HashSet<E> set = Sets.newHashSet();
        Iterators.addAll(set, elements);
        return set;
    }

    public static <E> Set<E> newConcurrentHashSet() {
        return Sets.newSetFromMap(new ConcurrentHashMap());
    }

    public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
        Set<E> set = Sets.newConcurrentHashSet();
        Iterables.addAll(set, elements);
        return set;
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
        return new LinkedHashSet(Maps.capacity(expectedSize));
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedHashSet<E>(Collections2.cast(elements));
        }
        LinkedHashSet<E> set = Sets.newLinkedHashSet();
        Iterables.addAll(set, elements);
        return set;
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet();
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
        TreeSet<E> set = Sets.newTreeSet();
        Iterables.addAll(set, elements);
        return set;
    }

    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
        return new TreeSet<E>(Preconditions.checkNotNull(comparator));
    }

    public static <E> Set<E> newIdentityHashSet() {
        return Sets.newSetFromMap(Maps.newIdentityHashMap());
    }

    @GwtIncompatible(value="CopyOnWriteArraySet")
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet();
    }

    @GwtIncompatible(value="CopyOnWriteArraySet")
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
        Collection<? extends E> elementsCollection = elements instanceof Collection ? Collections2.cast(elements) : Lists.newArrayList(elements);
        return new CopyOnWriteArraySet<E>(elementsCollection);
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
        if (collection instanceof EnumSet) {
            return EnumSet.complementOf((EnumSet)collection);
        }
        Preconditions.checkArgument(!collection.isEmpty(), "collection is empty; use the other version of this method");
        Class type = ((Enum)collection.iterator().next()).getDeclaringClass();
        return Sets.makeComplementByHand(collection, type);
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
        Preconditions.checkNotNull(collection);
        return collection instanceof EnumSet ? EnumSet.complementOf((EnumSet)collection) : Sets.makeComplementByHand(collection, type);
    }

    private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
        EnumSet<E> result = EnumSet.allOf(type);
        result.removeAll(collection);
        return result;
    }

    public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
        return Platform.newSetFromMap(map);
    }

    public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        final SetView<? extends E> set2minus1 = Sets.difference(set2, set1);
        return new SetView<E>(){

            @Override
            public int size() {
                return set1.size() + set2minus1.size();
            }

            @Override
            public boolean isEmpty() {
                return set1.isEmpty() && set2.isEmpty();
            }

            @Override
            public Iterator<E> iterator() {
                return Iterators.unmodifiableIterator(Iterators.concat(set1.iterator(), set2minus1.iterator()));
            }

            @Override
            public boolean contains(Object object) {
                return set1.contains(object) || set2.contains(object);
            }

            @Override
            public <S extends Set<E>> S copyInto(S set) {
                set.addAll(set1);
                set.addAll(set2);
                return set;
            }

            @Override
            public ImmutableSet<E> immutableCopy() {
                return ((ImmutableSet.Builder)((ImmutableSet.Builder)new ImmutableSet.Builder().addAll((Iterable)set1)).addAll((Iterable)set2)).build();
            }
        };
    }

    public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        final Predicate<?> inSet2 = Predicates.in(set2);
        return new SetView<E>(){

            @Override
            public Iterator<E> iterator() {
                return Iterators.filter(set1.iterator(), inSet2);
            }

            @Override
            public int size() {
                return Iterators.size(this.iterator());
            }

            @Override
            public boolean isEmpty() {
                return !this.iterator().hasNext();
            }

            @Override
            public boolean contains(Object object) {
                return set1.contains(object) && set2.contains(object);
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return set1.containsAll(collection) && set2.containsAll(collection);
            }
        };
    }

    public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        final Predicate<?> notInSet2 = Predicates.not(Predicates.in(set2));
        return new SetView<E>(){

            @Override
            public Iterator<E> iterator() {
                return Iterators.filter(set1.iterator(), notInSet2);
            }

            @Override
            public int size() {
                return Iterators.size(this.iterator());
            }

            @Override
            public boolean isEmpty() {
                return set2.containsAll(set1);
            }

            @Override
            public boolean contains(Object element) {
                return set1.contains(element) && !set2.contains(element);
            }
        };
    }

    public static <E> SetView<E> symmetricDifference(Set<? extends E> set1, Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        return Sets.difference(Sets.union(set1, set2), Sets.intersection(set1, set2));
    }

    public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
        if (unfiltered instanceof SortedSet) {
            return Sets.filter((SortedSet)unfiltered, predicate);
        }
        if (unfiltered instanceof FilteredSet) {
            FilteredSet filtered = (FilteredSet)unfiltered;
            Predicate<? super E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
            return new FilteredSet<E>((Set)filtered.unfiltered, combinedPredicate);
        }
        return new FilteredSet<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }

    public static <E> SortedSet<E> filter(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
        return Platform.setsFilterSortedSet(unfiltered, predicate);
    }

    static <E> SortedSet<E> filterSortedIgnoreNavigable(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredSet) {
            FilteredSet filtered = (FilteredSet)((Object)unfiltered);
            Predicate<? super E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
            return new FilteredSortedSet<E>((SortedSet)filtered.unfiltered, combinedPredicate);
        }
        return new FilteredSortedSet<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }

    @GwtIncompatible(value="NavigableSet")
    public static <E> NavigableSet<E> filter(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredSet) {
            FilteredSet filtered = (FilteredSet)((Object)unfiltered);
            Predicate<? super E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
            return new FilteredNavigableSet<E>((NavigableSet)filtered.unfiltered, combinedPredicate);
        }
        return new FilteredNavigableSet<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }

    public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) {
        return CartesianSet.create(sets);
    }

    public static <B> Set<List<B>> cartesianProduct(Set<? extends B> ... sets) {
        return Sets.cartesianProduct(Arrays.asList(sets));
    }

    @GwtCompatible(serializable=false)
    public static <E> Set<Set<E>> powerSet(Set<E> set) {
        return new PowerSet<E>(set);
    }

    static int hashCodeImpl(Set<?> s2) {
        int hashCode = 0;
        for (Object o2 : s2) {
            hashCode += o2 != null ? o2.hashCode() : 0;
            hashCode = ~(~hashCode);
        }
        return hashCode;
    }

    static boolean equalsImpl(Set<?> s2, @Nullable Object object) {
        if (s2 == object) {
            return true;
        }
        if (object instanceof Set) {
            Set o2 = (Set)object;
            try {
                return s2.size() == o2.size() && s2.containsAll(o2);
            }
            catch (NullPointerException ignored) {
                return false;
            }
            catch (ClassCastException ignored) {
                return false;
            }
        }
        return false;
    }

    @GwtIncompatible(value="NavigableSet")
    public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
        if (set instanceof ImmutableSortedSet || set instanceof UnmodifiableNavigableSet) {
            return set;
        }
        return new UnmodifiableNavigableSet<E>(set);
    }

    @GwtIncompatible(value="NavigableSet")
    public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> navigableSet) {
        return Synchronized.navigableSet(navigableSet);
    }

    static boolean removeAllImpl(Set<?> set, Iterator<?> iterator) {
        boolean changed = false;
        while (iterator.hasNext()) {
            changed |= set.remove(iterator.next());
        }
        return changed;
    }

    static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        if (collection instanceof Multiset) {
            collection = ((Multiset)collection).elementSet();
        }
        if (collection instanceof Set && collection.size() > set.size()) {
            return Iterators.removeAll(set.iterator(), collection);
        }
        return Sets.removeAllImpl(set, collection.iterator());
    }

    @GwtIncompatible(value="NavigableSet")
    static class DescendingSet<E>
    extends ForwardingNavigableSet<E> {
        private final NavigableSet<E> forward;

        DescendingSet(NavigableSet<E> forward) {
            this.forward = forward;
        }

        @Override
        protected NavigableSet<E> delegate() {
            return this.forward;
        }

        @Override
        public E lower(E e2) {
            return this.forward.higher(e2);
        }

        @Override
        public E floor(E e2) {
            return this.forward.ceiling(e2);
        }

        @Override
        public E ceiling(E e2) {
            return this.forward.floor(e2);
        }

        @Override
        public E higher(E e2) {
            return this.forward.lower(e2);
        }

        @Override
        public E pollFirst() {
            return this.forward.pollLast();
        }

        @Override
        public E pollLast() {
            return this.forward.pollFirst();
        }

        @Override
        public NavigableSet<E> descendingSet() {
            return this.forward;
        }

        @Override
        public Iterator<E> descendingIterator() {
            return this.forward.iterator();
        }

        @Override
        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
            return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
        }

        @Override
        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
            return this.forward.tailSet(toElement, inclusive).descendingSet();
        }

        @Override
        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
            return this.forward.headSet(fromElement, inclusive).descendingSet();
        }

        @Override
        public Comparator<? super E> comparator() {
            Comparator forwardComparator = this.forward.comparator();
            if (forwardComparator == null) {
                return Ordering.natural().reverse();
            }
            return DescendingSet.reverse(forwardComparator);
        }

        private static <T> Ordering<T> reverse(Comparator<T> forward) {
            return Ordering.from(forward).reverse();
        }

        @Override
        public E first() {
            return this.forward.last();
        }

        @Override
        public SortedSet<E> headSet(E toElement) {
            return this.standardHeadSet(toElement);
        }

        @Override
        public E last() {
            return this.forward.first();
        }

        @Override
        public SortedSet<E> subSet(E fromElement, E toElement) {
            return this.standardSubSet(fromElement, toElement);
        }

        @Override
        public SortedSet<E> tailSet(E fromElement) {
            return this.standardTailSet(fromElement);
        }

        @Override
        public Iterator<E> iterator() {
            return this.forward.descendingIterator();
        }

        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }

        @Override
        public <T> T[] toArray(T[] array) {
            return this.standardToArray(array);
        }

        @Override
        public String toString() {
            return this.standardToString();
        }
    }

    @GwtIncompatible(value="NavigableSet")
    static final class UnmodifiableNavigableSet<E>
    extends ForwardingSortedSet<E>
    implements NavigableSet<E>,
    Serializable {
        private final NavigableSet<E> delegate;
        private transient UnmodifiableNavigableSet<E> descendingSet;
        private static final long serialVersionUID = 0L;

        UnmodifiableNavigableSet(NavigableSet<E> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }

        @Override
        protected SortedSet<E> delegate() {
            return Collections.unmodifiableSortedSet(this.delegate);
        }

        @Override
        public E lower(E e2) {
            return this.delegate.lower(e2);
        }

        @Override
        public E floor(E e2) {
            return this.delegate.floor(e2);
        }

        @Override
        public E ceiling(E e2) {
            return this.delegate.ceiling(e2);
        }

        @Override
        public E higher(E e2) {
            return this.delegate.higher(e2);
        }

        @Override
        public E pollFirst() {
            throw new UnsupportedOperationException();
        }

        @Override
        public E pollLast() {
            throw new UnsupportedOperationException();
        }

        @Override
        public NavigableSet<E> descendingSet() {
            UnmodifiableNavigableSet<E> result = this.descendingSet;
            if (result == null) {
                result = this.descendingSet = new UnmodifiableNavigableSet<E>(this.delegate.descendingSet());
                result.descendingSet = this;
            }
            return result;
        }

        @Override
        public Iterator<E> descendingIterator() {
            return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
        }

        @Override
        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.subSet(fromElement, fromInclusive, toElement, toInclusive));
        }

        @Override
        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
        }

        @Override
        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
        }
    }

    private static final class PowerSet<E>
    extends AbstractSet<Set<E>> {
        final ImmutableMap<E, Integer> inputSet;

        PowerSet(Set<E> input) {
            ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
            int i2 = 0;
            for (E e2 : Preconditions.checkNotNull(input)) {
                builder.put(e2, i2++);
            }
            this.inputSet = builder.build();
            Preconditions.checkArgument(this.inputSet.size() <= 30, "Too many elements to create power set: %s > 30", this.inputSet.size());
        }

        @Override
        public int size() {
            return 1 << this.inputSet.size();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Iterator<Set<E>> iterator() {
            return new AbstractIndexedListIterator<Set<E>>(this.size()){

                @Override
                protected Set<E> get(int setBits) {
                    return new SubSet(PowerSet.this.inputSet, setBits);
                }
            };
        }

        @Override
        public boolean contains(@Nullable Object obj) {
            if (obj instanceof Set) {
                Set set = (Set)obj;
                return ((AbstractCollection)((Object)this.inputSet.keySet())).containsAll(set);
            }
            return false;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof PowerSet) {
                PowerSet that = (PowerSet)obj;
                return this.inputSet.equals(that.inputSet);
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return ((ImmutableSet)this.inputSet.keySet()).hashCode() << this.inputSet.size() - 1;
        }

        @Override
        public String toString() {
            return "powerSet(" + this.inputSet + ")";
        }
    }

    private static final class SubSet<E>
    extends AbstractSet<E> {
        private final ImmutableMap<E, Integer> inputSet;
        private final int mask;

        SubSet(ImmutableMap<E, Integer> inputSet, int mask) {
            this.inputSet = inputSet;
            this.mask = mask;
        }

        @Override
        public Iterator<E> iterator() {
            return new UnmodifiableIterator<E>(){
                final ImmutableList<E> elements;
                int remainingSetBits;
                {
                    this.elements = ((ImmutableCollection)((Object)SubSet.this.inputSet.keySet())).asList();
                    this.remainingSetBits = SubSet.this.mask;
                }

                @Override
                public boolean hasNext() {
                    return this.remainingSetBits != 0;
                }

                @Override
                public E next() {
                    int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
                    if (index == 32) {
                        throw new NoSuchElementException();
                    }
                    this.remainingSetBits &= ~(1 << index);
                    return this.elements.get(index);
                }
            };
        }

        @Override
        public int size() {
            return Integer.bitCount(this.mask);
        }

        @Override
        public boolean contains(@Nullable Object o2) {
            Integer index = this.inputSet.get(o2);
            return index != null && (this.mask & 1 << index) != 0;
        }
    }

    private static final class CartesianSet<E>
    extends ForwardingCollection<List<E>>
    implements Set<List<E>> {
        private final transient ImmutableList<ImmutableSet<E>> axes;
        private final transient CartesianList<E> delegate;

        static <E> Set<List<E>> create(List<? extends Set<? extends E>> sets) {
            ImmutableList.Builder axesBuilder = new ImmutableList.Builder(sets.size());
            for (Set<E> set : sets) {
                ImmutableSet<E> copy = ImmutableSet.copyOf(set);
                if (copy.isEmpty()) {
                    return ImmutableSet.of();
                }
                axesBuilder.add(copy);
            }
            ImmutableCollection axes = axesBuilder.build();
            ImmutableList immutableList = new ImmutableList<List<E>>((ImmutableList)axes){
                final /* synthetic */ ImmutableList val$axes;
                {
                    this.val$axes = immutableList;
                }

                @Override
                public int size() {
                    return this.val$axes.size();
                }

                @Override
                public List<E> get(int index) {
                    return ((ImmutableSet)this.val$axes.get(index)).asList();
                }

                @Override
                boolean isPartialView() {
                    return true;
                }
            };
            return new CartesianSet(axes, new CartesianList(immutableList));
        }

        private CartesianSet(ImmutableList<ImmutableSet<E>> axes, CartesianList<E> delegate) {
            this.axes = axes;
            this.delegate = delegate;
        }

        @Override
        protected Collection<List<E>> delegate() {
            return this.delegate;
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (object instanceof CartesianSet) {
                CartesianSet that = (CartesianSet)object;
                return this.axes.equals(that.axes);
            }
            return super.equals(object);
        }

        @Override
        public int hashCode() {
            int adjust = this.size() - 1;
            for (int i2 = 0; i2 < this.axes.size(); ++i2) {
                adjust *= 31;
                adjust = ~(~adjust);
            }
            int hash = 1;
            for (Set set : this.axes) {
                hash = 31 * hash + this.size() / set.size() * set.hashCode();
                hash = ~(~hash);
            }
            return ~(~(hash += adjust));
        }
    }

    @GwtIncompatible(value="NavigableSet")
    private static class FilteredNavigableSet<E>
    extends FilteredSortedSet<E>
    implements NavigableSet<E> {
        FilteredNavigableSet(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }

        NavigableSet<E> unfiltered() {
            return (NavigableSet)this.unfiltered;
        }

        @Override
        @Nullable
        public E lower(E e2) {
            return Iterators.getNext(this.headSet(e2, false).descendingIterator(), null);
        }

        @Override
        @Nullable
        public E floor(E e2) {
            return Iterators.getNext(this.headSet(e2, true).descendingIterator(), null);
        }

        @Override
        public E ceiling(E e2) {
            return Iterables.getFirst(this.tailSet(e2, true), null);
        }

        @Override
        public E higher(E e2) {
            return Iterables.getFirst(this.tailSet(e2, false), null);
        }

        @Override
        public E pollFirst() {
            return Iterables.removeFirstMatching(this.unfiltered(), this.predicate);
        }

        @Override
        public E pollLast() {
            return Iterables.removeFirstMatching(this.unfiltered().descendingSet(), this.predicate);
        }

        @Override
        public NavigableSet<E> descendingSet() {
            return Sets.filter(this.unfiltered().descendingSet(), this.predicate);
        }

        @Override
        public Iterator<E> descendingIterator() {
            return Iterators.filter(this.unfiltered().descendingIterator(), this.predicate);
        }

        @Override
        public E last() {
            return this.descendingIterator().next();
        }

        @Override
        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
            return Sets.filter(this.unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
        }

        @Override
        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
            return Sets.filter(this.unfiltered().headSet(toElement, inclusive), this.predicate);
        }

        @Override
        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
            return Sets.filter(this.unfiltered().tailSet(fromElement, inclusive), this.predicate);
        }
    }

    private static class FilteredSortedSet<E>
    extends FilteredSet<E>
    implements SortedSet<E> {
        FilteredSortedSet(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }

        @Override
        public Comparator<? super E> comparator() {
            return ((SortedSet)this.unfiltered).comparator();
        }

        @Override
        public SortedSet<E> subSet(E fromElement, E toElement) {
            return new FilteredSortedSet<E>(((SortedSet)this.unfiltered).subSet(fromElement, toElement), this.predicate);
        }

        @Override
        public SortedSet<E> headSet(E toElement) {
            return new FilteredSortedSet<E>(((SortedSet)this.unfiltered).headSet(toElement), this.predicate);
        }

        @Override
        public SortedSet<E> tailSet(E fromElement) {
            return new FilteredSortedSet<E>(((SortedSet)this.unfiltered).tailSet(fromElement), this.predicate);
        }

        @Override
        public E first() {
            return this.iterator().next();
        }

        @Override
        public E last() {
            SortedSet sortedUnfiltered = (SortedSet)this.unfiltered;
            Object element;
            while (!this.predicate.apply(element = sortedUnfiltered.last())) {
                sortedUnfiltered = sortedUnfiltered.headSet(element);
            }
            return element;
        }
    }

    private static class FilteredSet<E>
    extends Collections2.FilteredCollection<E>
    implements Set<E> {
        FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }

        @Override
        public boolean equals(@Nullable Object object) {
            return Sets.equalsImpl(this, object);
        }

        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    public static abstract class SetView<E>
    extends AbstractSet<E> {
        private SetView() {
        }

        public ImmutableSet<E> immutableCopy() {
            return ImmutableSet.copyOf(this);
        }

        public <S extends Set<E>> S copyInto(S set) {
            set.addAll(this);
            return set;
        }
    }

    static abstract class ImprovedAbstractSet<E>
    extends AbstractSet<E> {
        ImprovedAbstractSet() {
        }

        @Override
        public boolean removeAll(Collection<?> c2) {
            return Sets.removeAllImpl(this, c2);
        }

        @Override
        public boolean retainAll(Collection<?> c2) {
            return super.retainAll(Preconditions.checkNotNull(c2));
        }
    }
}

