// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.Consumer;
import java.io.Serializable;
import java.util.NoSuchElementException;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;
import com.google.common.math.IntMath;
import java.util.BitSet;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import com.google.common.base.Predicates;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Map;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collector;
import java.util.Iterator;
import java.util.Collection;
import java.util.EnumSet;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Sets
{
    private Sets() {
    }
    
    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(final E anElement, final E... otherElements) {
        return (ImmutableSet<E>)ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
    }
    
    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(final Iterable<E> elements) {
        if (elements instanceof ImmutableEnumSet) {
            return (ImmutableSet<E>)(ImmutableEnumSet)elements;
        }
        if (elements instanceof Collection) {
            final Collection<E> collection = (Collection<E>)(Collection)elements;
            if (collection.isEmpty()) {
                return ImmutableSet.of();
            }
            return (ImmutableSet<E>)ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
        }
        else {
            final Iterator<E> itr = elements.iterator();
            if (itr.hasNext()) {
                final EnumSet<E> enumSet = EnumSet.of(itr.next());
                Iterators.addAll(enumSet, (Iterator<? extends E>)itr);
                return (ImmutableSet<E>)ImmutableEnumSet.asImmutable(enumSet);
            }
            return ImmutableSet.of();
        }
    }
    
    public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
        return CollectCollectors.toImmutableEnumSet();
    }
    
    public static <E extends Enum<E>> EnumSet<E> newEnumSet(final Iterable<E> iterable, final Class<E> elementType) {
        final EnumSet<E> set = EnumSet.noneOf(elementType);
        Iterables.addAll(set, (Iterable<? extends E>)iterable);
        return set;
    }
    
    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }
    
    public static <E> HashSet<E> newHashSet(final E... elements) {
        final HashSet<E> set = newHashSetWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }
    
    public static <E> HashSet<E> newHashSet(final Iterable<? extends E> elements) {
        return (elements instanceof Collection) ? new HashSet<E>((Collection)elements) : newHashSet(elements.iterator());
    }
    
    public static <E> HashSet<E> newHashSet(final Iterator<? extends E> elements) {
        final HashSet<E> set = newHashSet();
        Iterators.addAll(set, elements);
        return set;
    }
    
    public static <E> HashSet<E> newHashSetWithExpectedSize(final int expectedSize) {
        return new HashSet<E>(Maps.capacity(expectedSize));
    }
    
    public static <E> Set<E> newConcurrentHashSet() {
        return Platform.newConcurrentHashSet();
    }
    
    public static <E> Set<E> newConcurrentHashSet(final Iterable<? extends E> elements) {
        final Set<E> set = newConcurrentHashSet();
        Iterables.addAll(set, elements);
        return set;
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSet(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedHashSet<E>((Collection)elements);
        }
        final LinkedHashSet<E> set = newLinkedHashSet();
        Iterables.addAll(set, elements);
        return set;
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(final int expectedSize) {
        return new LinkedHashSet<E>(Maps.capacity(expectedSize));
    }
    
    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet<E>();
    }
    
    public static <E extends Comparable> TreeSet<E> newTreeSet(final Iterable<? extends E> elements) {
        final TreeSet<E> set = newTreeSet();
        Iterables.addAll(set, elements);
        return set;
    }
    
    public static <E> TreeSet<E> newTreeSet(final Comparator<? super E> comparator) {
        return new TreeSet<E>(Preconditions.checkNotNull(comparator));
    }
    
    public static <E> Set<E> newIdentityHashSet() {
        return Collections.newSetFromMap((Map<E, Boolean>)Maps.newIdentityHashMap());
    }
    
    @GwtIncompatible
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet<E>();
    }
    
    @GwtIncompatible
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(final Iterable<? extends E> elements) {
        final Collection<? extends E> elementsCollection = (Collection<? extends E>)((elements instanceof Collection) ? ((Collection)elements) : Lists.newArrayList((Iterable<?>)elements));
        return new CopyOnWriteArraySet<E>(elementsCollection);
    }
    
    public static <E extends Enum<E>> EnumSet<E> complementOf(final Collection<E> collection) {
        if (collection instanceof EnumSet) {
            return EnumSet.complementOf((EnumSet<E>)(EnumSet)collection);
        }
        Preconditions.checkArgument(!collection.isEmpty(), (Object)"collection is empty; use the other version of this method");
        final Class<E> type = collection.iterator().next().getDeclaringClass();
        return makeComplementByHand(collection, type);
    }
    
    public static <E extends Enum<E>> EnumSet<E> complementOf(final Collection<E> collection, final Class<E> type) {
        Preconditions.checkNotNull(collection);
        return (EnumSet<E>)((collection instanceof EnumSet) ? EnumSet.complementOf((EnumSet<E>)(EnumSet)collection) : makeComplementByHand((Collection<Enum>)collection, (Class<Enum>)type));
    }
    
    private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(final Collection<E> collection, final Class<E> type) {
        final EnumSet<E> result = EnumSet.allOf(type);
        result.removeAll(collection);
        return result;
    }
    
    @Deprecated
    public static <E> Set<E> newSetFromMap(final Map<E, Boolean> map) {
        return Collections.newSetFromMap(map);
    }
    
    public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, (Object)"set1");
        Preconditions.checkNotNull(set2, (Object)"set2");
        return new SetView<E>() {
            @Override
            public int size() {
                int size = set1.size();
                for (final E e : set2) {
                    if (!set1.contains(e)) {
                        ++size;
                    }
                }
                return size;
            }
            
            @Override
            public boolean isEmpty() {
                return set1.isEmpty() && set2.isEmpty();
            }
            
            @Override
            public UnmodifiableIterator<E> iterator() {
                return new AbstractIterator<E>() {
                    final Iterator<? extends E> itr1 = set1.iterator();
                    final Iterator<? extends E> itr2 = set2.iterator();
                    
                    @CheckForNull
                    @Override
                    protected E computeNext() {
                        if (this.itr1.hasNext()) {
                            return (E)this.itr1.next();
                        }
                        while (this.itr2.hasNext()) {
                            final E e = (E)this.itr2.next();
                            if (!set1.contains(e)) {
                                return e;
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            public Stream<E> stream() {
                return Stream.concat((Stream<? extends E>)set1.stream(), (Stream<? extends E>)set2.stream().filter(e -> !set1.contains(e)));
            }
            
            @Override
            public Stream<E> parallelStream() {
                return this.stream().parallel();
            }
            
            @Override
            public boolean contains(@CheckForNull final Object object) {
                return set1.contains(object) || set2.contains(object);
            }
            
            @Override
            public <S extends Set<E>> S copyInto(final S set) {
                set.addAll(set1);
                set.addAll(set2);
                return set;
            }
            
            @Override
            public ImmutableSet<E> immutableCopy() {
                return new ImmutableSet.Builder<E>().addAll(set1).addAll(set2).build();
            }
        };
    }
    
    public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
        Preconditions.checkNotNull(set1, (Object)"set1");
        Preconditions.checkNotNull(set2, (Object)"set2");
        return new SetView<E>() {
            @Override
            public UnmodifiableIterator<E> iterator() {
                return new AbstractIterator<E>() {
                    final Iterator<E> itr = set1.iterator();
                    
                    @CheckForNull
                    @Override
                    protected E computeNext() {
                        while (this.itr.hasNext()) {
                            final E e = this.itr.next();
                            if (set2.contains(e)) {
                                return e;
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            public Stream<E> stream() {
                final Stream stream = set1.stream();
                final Set val$set2 = set2;
                Objects.requireNonNull(val$set2);
                return stream.filter(val$set2::contains);
            }
            
            @Override
            public Stream<E> parallelStream() {
                final Stream parallelStream = set1.parallelStream();
                final Set val$set2 = set2;
                Objects.requireNonNull(val$set2);
                return parallelStream.filter(val$set2::contains);
            }
            
            @Override
            public int size() {
                int size = 0;
                for (final E e : set1) {
                    if (set2.contains(e)) {
                        ++size;
                    }
                }
                return size;
            }
            
            @Override
            public boolean isEmpty() {
                return Collections.disjoint(set2, set1);
            }
            
            @Override
            public boolean contains(@CheckForNull final Object object) {
                return set1.contains(object) && set2.contains(object);
            }
            
            @Override
            public boolean containsAll(final Collection<?> collection) {
                return set1.containsAll(collection) && set2.containsAll(collection);
            }
        };
    }
    
    public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
        Preconditions.checkNotNull(set1, (Object)"set1");
        Preconditions.checkNotNull(set2, (Object)"set2");
        return new SetView<E>() {
            @Override
            public UnmodifiableIterator<E> iterator() {
                return new AbstractIterator<E>() {
                    final Iterator<E> itr = set1.iterator();
                    
                    @CheckForNull
                    @Override
                    protected E computeNext() {
                        while (this.itr.hasNext()) {
                            final E e = this.itr.next();
                            if (!set2.contains(e)) {
                                return e;
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            public Stream<E> stream() {
                return (Stream<E>)set1.stream().filter(e -> !set2.contains(e));
            }
            
            @Override
            public Stream<E> parallelStream() {
                return (Stream<E>)set1.parallelStream().filter(e -> !set2.contains(e));
            }
            
            @Override
            public int size() {
                int size = 0;
                for (final E e : set1) {
                    if (!set2.contains(e)) {
                        ++size;
                    }
                }
                return size;
            }
            
            @Override
            public boolean isEmpty() {
                return set2.containsAll(set1);
            }
            
            @Override
            public boolean contains(@CheckForNull final Object element) {
                return set1.contains(element) && !set2.contains(element);
            }
        };
    }
    
    public static <E> SetView<E> symmetricDifference(final Set<? extends E> set1, final Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, (Object)"set1");
        Preconditions.checkNotNull(set2, (Object)"set2");
        return new SetView<E>() {
            @Override
            public UnmodifiableIterator<E> iterator() {
                final Iterator<? extends E> itr1 = set1.iterator();
                final Iterator<? extends E> itr2 = set2.iterator();
                return new AbstractIterator<E>() {
                    @CheckForNull
                    public E computeNext() {
                        while (itr1.hasNext()) {
                            final E elem1 = itr1.next();
                            if (!set2.contains(elem1)) {
                                return elem1;
                            }
                        }
                        while (itr2.hasNext()) {
                            final E elem2 = itr2.next();
                            if (!set1.contains(elem2)) {
                                return elem2;
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            public int size() {
                int size = 0;
                for (final E e : set1) {
                    if (!set2.contains(e)) {
                        ++size;
                    }
                }
                for (final E e : set2) {
                    if (!set1.contains(e)) {
                        ++size;
                    }
                }
                return size;
            }
            
            @Override
            public boolean isEmpty() {
                return set1.equals(set2);
            }
            
            @Override
            public boolean contains(@CheckForNull final Object element) {
                return set1.contains(element) ^ set2.contains(element);
            }
        };
    }
    
    public static <E> Set<E> filter(final Set<E> unfiltered, final com.google.common.base.Predicate<? super E> predicate) {
        if (unfiltered instanceof SortedSet) {
            return (Set<E>)filter((SortedSet<Object>)(SortedSet)unfiltered, (com.google.common.base.Predicate<? super Object>)predicate);
        }
        if (unfiltered instanceof FilteredSet) {
            final FilteredSet<E> filtered = (FilteredSet<E>)(FilteredSet)unfiltered;
            final com.google.common.base.Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
            return new FilteredSet<E>((Set)filtered.unfiltered, combinedPredicate);
        }
        return new FilteredSet<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }
    
    public static <E> SortedSet<E> filter(final SortedSet<E> unfiltered, final com.google.common.base.Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredSet) {
            final FilteredSet<E> filtered = (FilteredSet<E>)(FilteredSet)unfiltered;
            final com.google.common.base.Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
            return new FilteredSortedSet<E>((SortedSet)filtered.unfiltered, combinedPredicate);
        }
        return new FilteredSortedSet<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }
    
    @GwtIncompatible
    public static <E> NavigableSet<E> filter(final NavigableSet<E> unfiltered, final com.google.common.base.Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredSet) {
            final FilteredSet<E> filtered = (FilteredSet<E>)(FilteredSet)unfiltered;
            final com.google.common.base.Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
            return new FilteredNavigableSet<E>((NavigableSet)filtered.unfiltered, combinedPredicate);
        }
        return new FilteredNavigableSet<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }
    
    public static <B> Set<List<B>> cartesianProduct(final List<? extends Set<? extends B>> sets) {
        return CartesianSet.create(sets);
    }
    
    @SafeVarargs
    public static <B> Set<List<B>> cartesianProduct(final Set<? extends B>... sets) {
        return cartesianProduct((List<? extends Set<? extends B>>)Arrays.asList((Set<? extends B>[])sets));
    }
    
    @GwtCompatible(serializable = false)
    public static <E> Set<Set<E>> powerSet(final Set<E> set) {
        return (Set<Set<E>>)new PowerSet((Set<Object>)set);
    }
    
    @Beta
    public static <E> Set<Set<E>> combinations(final Set<E> set, final int size) {
        final ImmutableMap<E, Integer> index = Maps.indexMap(set);
        CollectPreconditions.checkNonnegative(size, "size");
        Preconditions.checkArgument(size <= index.size(), "size (%s) must be <= set.size() (%s)", size, index.size());
        if (size == 0) {
            return (Set<Set<E>>)ImmutableSet.of(ImmutableSet.of());
        }
        if (size == index.size()) {
            return (Set<Set<E>>)ImmutableSet.of(index.keySet());
        }
        return new AbstractSet<Set<E>>() {
            @Override
            public boolean contains(@CheckForNull final Object o) {
                if (o instanceof Set) {
                    final Set<?> s = (Set<?>)o;
                    return s.size() == size && index.keySet().containsAll(s);
                }
                return false;
            }
            
            @Override
            public Iterator<Set<E>> iterator() {
                return new AbstractIterator<Set<E>>() {
                    final BitSet bits = new BitSet(index.size());
                    
                    @CheckForNull
                    @Override
                    protected Set<E> computeNext() {
                        if (this.bits.isEmpty()) {
                            this.bits.set(0, size);
                        }
                        else {
                            final int firstSetBit = this.bits.nextSetBit(0);
                            final int bitToFlip = this.bits.nextClearBit(firstSetBit);
                            if (bitToFlip == index.size()) {
                                return this.endOfData();
                            }
                            this.bits.set(0, bitToFlip - firstSetBit - 1);
                            this.bits.clear(bitToFlip - firstSetBit - 1, bitToFlip);
                            this.bits.set(bitToFlip);
                        }
                        final BitSet copy = (BitSet)this.bits.clone();
                        return new AbstractSet<E>() {
                            @Override
                            public boolean contains(@CheckForNull final Object o) {
                                final Integer i = index.get(o);
                                return i != null && copy.get(i);
                            }
                            
                            @Override
                            public Iterator<E> iterator() {
                                return new AbstractIterator<E>() {
                                    int i = -1;
                                    
                                    @CheckForNull
                                    @Override
                                    protected E computeNext() {
                                        this.i = copy.nextSetBit(this.i + 1);
                                        if (this.i == -1) {
                                            return this.endOfData();
                                        }
                                        return (E)index.keySet().asList().get(this.i);
                                    }
                                };
                            }
                            
                            @Override
                            public int size() {
                                return size;
                            }
                        };
                    }
                };
            }
            
            @Override
            public int size() {
                return IntMath.binomial(index.size(), size);
            }
            
            @Override
            public String toString() {
                final String value = String.valueOf(index.keySet());
                return new StringBuilder(32 + String.valueOf(value).length()).append("Sets.combinations(").append(value).append(", ").append(size).append(")").toString();
            }
        };
    }
    
    static int hashCodeImpl(final Set<?> s) {
        int hashCode = 0;
        for (final Object o : s) {
            hashCode += ((o != null) ? o.hashCode() : 0);
            hashCode = ~(~hashCode);
        }
        return hashCode;
    }
    
    static boolean equalsImpl(final Set<?> s, @CheckForNull final Object object) {
        if (s == object) {
            return true;
        }
        if (object instanceof Set) {
            final Set<?> o = (Set<?>)object;
            try {
                return s.size() == o.size() && s.containsAll(o);
            }
            catch (NullPointerException | ClassCastException ex2) {
                final RuntimeException ex;
                final RuntimeException ignored = ex;
                return false;
            }
        }
        return false;
    }
    
    public static <E> NavigableSet<E> unmodifiableNavigableSet(final NavigableSet<E> set) {
        if (set instanceof ImmutableCollection || set instanceof UnmodifiableNavigableSet) {
            return set;
        }
        return new UnmodifiableNavigableSet<E>(set);
    }
    
    @GwtIncompatible
    public static <E> NavigableSet<E> synchronizedNavigableSet(final NavigableSet<E> navigableSet) {
        return Synchronized.navigableSet(navigableSet);
    }
    
    static boolean removeAllImpl(final Set<?> set, final Iterator<?> iterator) {
        boolean changed = false;
        while (iterator.hasNext()) {
            changed |= set.remove(iterator.next());
        }
        return changed;
    }
    
    static boolean removeAllImpl(final Set<?> set, Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        if (collection instanceof Multiset) {
            collection = (Collection<?>)((Multiset)collection).elementSet();
        }
        if (collection instanceof Set && collection.size() > set.size()) {
            return Iterators.removeAll(set.iterator(), collection);
        }
        return removeAllImpl(set, collection.iterator());
    }
    
    @Beta
    @GwtIncompatible
    public static <K extends Comparable<? super K>> NavigableSet<K> subSet(final NavigableSet<K> set, final Range<K> range) {
        if (set.comparator() != null && set.comparator() != Ordering.natural() && range.hasLowerBound() && range.hasUpperBound()) {
            Preconditions.checkArgument(set.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0, (Object)"set is using a custom comparator which is inconsistent with the natural ordering.");
        }
        if (range.hasLowerBound() && range.hasUpperBound()) {
            return set.subSet(range.lowerEndpoint(), range.lowerBoundType() == BoundType.CLOSED, range.upperEndpoint(), range.upperBoundType() == BoundType.CLOSED);
        }
        if (range.hasLowerBound()) {
            return set.tailSet(range.lowerEndpoint(), range.lowerBoundType() == BoundType.CLOSED);
        }
        if (range.hasUpperBound()) {
            return set.headSet(range.upperEndpoint(), range.upperBoundType() == BoundType.CLOSED);
        }
        return Preconditions.checkNotNull(set);
    }
    
    abstract static class ImprovedAbstractSet<E> extends AbstractSet<E>
    {
        @Override
        public boolean removeAll(final Collection<?> c) {
            return Sets.removeAllImpl(this, c);
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            return super.retainAll(Preconditions.checkNotNull(c));
        }
    }
    
    public abstract static class SetView<E> extends AbstractSet<E>
    {
        private SetView() {
        }
        
        public ImmutableSet<E> immutableCopy() {
            return ImmutableSet.copyOf((Collection<? extends E>)this);
        }
        
        @CanIgnoreReturnValue
        public <S extends Set<E>> S copyInto(final S set) {
            set.addAll((Collection<? extends E>)this);
            return set;
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        @DoNotCall("Always throws UnsupportedOperationException")
        @Override
        public final boolean add(@ParametricNullness final E e) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        @DoNotCall("Always throws UnsupportedOperationException")
        @Override
        public final boolean remove(@CheckForNull final Object object) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        @DoNotCall("Always throws UnsupportedOperationException")
        @Override
        public final boolean addAll(final Collection<? extends E> newElements) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        @DoNotCall("Always throws UnsupportedOperationException")
        @Override
        public final boolean removeAll(final Collection<?> oldElements) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        @DoNotCall("Always throws UnsupportedOperationException")
        @Override
        public final boolean removeIf(final Predicate<? super E> filter) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        @DoNotCall("Always throws UnsupportedOperationException")
        @Override
        public final boolean retainAll(final Collection<?> elementsToKeep) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @DoNotCall("Always throws UnsupportedOperationException")
        @Override
        public final void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public abstract UnmodifiableIterator<E> iterator();
    }
    
    private static class FilteredSet<E> extends Collections2.FilteredCollection<E> implements Set<E>
    {
        FilteredSet(final Set<E> unfiltered, final com.google.common.base.Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
            return Sets.equalsImpl(this, object);
        }
        
        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }
    
    private static class FilteredSortedSet<E> extends FilteredSet<E> implements SortedSet<E>
    {
        FilteredSortedSet(final SortedSet<E> unfiltered, final com.google.common.base.Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }
        
        @CheckForNull
        @Override
        public Comparator<? super E> comparator() {
            return (Comparator<? super E>)((SortedSet)this.unfiltered).comparator();
        }
        
        @Override
        public SortedSet<E> subSet(@ParametricNullness final E fromElement, @ParametricNullness final E toElement) {
            return new FilteredSortedSet((SortedSet<Object>)((SortedSet)this.unfiltered).subSet(fromElement, toElement), (com.google.common.base.Predicate<? super Object>)this.predicate);
        }
        
        @Override
        public SortedSet<E> headSet(@ParametricNullness final E toElement) {
            return new FilteredSortedSet((SortedSet<Object>)((SortedSet)this.unfiltered).headSet(toElement), (com.google.common.base.Predicate<? super Object>)this.predicate);
        }
        
        @Override
        public SortedSet<E> tailSet(@ParametricNullness final E fromElement) {
            return new FilteredSortedSet((SortedSet<Object>)((SortedSet)this.unfiltered).tailSet(fromElement), (com.google.common.base.Predicate<? super Object>)this.predicate);
        }
        
        @ParametricNullness
        @Override
        public E first() {
            return Iterators.find(this.unfiltered.iterator(), this.predicate);
        }
        
        @ParametricNullness
        @Override
        public E last() {
            SortedSet<E> sortedUnfiltered = (SortedSet<E>)(SortedSet)this.unfiltered;
            E element;
            while (true) {
                element = sortedUnfiltered.last();
                if (this.predicate.apply((Object)element)) {
                    break;
                }
                sortedUnfiltered = sortedUnfiltered.headSet(element);
            }
            return element;
        }
    }
    
    @GwtIncompatible
    private static class FilteredNavigableSet<E> extends FilteredSortedSet<E> implements NavigableSet<E>
    {
        FilteredNavigableSet(final NavigableSet<E> unfiltered, final com.google.common.base.Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }
        
        NavigableSet<E> unfiltered() {
            return (NavigableSet<E>)(NavigableSet)this.unfiltered;
        }
        
        @CheckForNull
        @Override
        public E lower(@ParametricNullness final E e) {
            return Iterators.find((Iterator<? extends E>)this.unfiltered().headSet(e, false).descendingIterator(), this.predicate, (E)null);
        }
        
        @CheckForNull
        @Override
        public E floor(@ParametricNullness final E e) {
            return Iterators.find((Iterator<? extends E>)this.unfiltered().headSet(e, true).descendingIterator(), this.predicate, (E)null);
        }
        
        @CheckForNull
        @Override
        public E ceiling(@ParametricNullness final E e) {
            return Iterables.find((Iterable<? extends E>)this.unfiltered().tailSet(e, true), this.predicate, (E)null);
        }
        
        @CheckForNull
        @Override
        public E higher(@ParametricNullness final E e) {
            return Iterables.find((Iterable<? extends E>)this.unfiltered().tailSet(e, false), this.predicate, (E)null);
        }
        
        @CheckForNull
        @Override
        public E pollFirst() {
            return Iterables.removeFirstMatching(this.unfiltered(), this.predicate);
        }
        
        @CheckForNull
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
        
        @ParametricNullness
        @Override
        public E last() {
            return Iterators.find(this.unfiltered().descendingIterator(), this.predicate);
        }
        
        @Override
        public NavigableSet<E> subSet(@ParametricNullness final E fromElement, final boolean fromInclusive, @ParametricNullness final E toElement, final boolean toInclusive) {
            return Sets.filter(this.unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
        }
        
        @Override
        public NavigableSet<E> headSet(@ParametricNullness final E toElement, final boolean inclusive) {
            return Sets.filter(this.unfiltered().headSet(toElement, inclusive), this.predicate);
        }
        
        @Override
        public NavigableSet<E> tailSet(@ParametricNullness final E fromElement, final boolean inclusive) {
            return Sets.filter(this.unfiltered().tailSet(fromElement, inclusive), this.predicate);
        }
    }
    
    private static final class CartesianSet<E> extends ForwardingCollection<List<E>> implements Set<List<E>>
    {
        private final transient ImmutableList<ImmutableSet<E>> axes;
        private final transient CartesianList<E> delegate;
        
        static <E> Set<List<E>> create(final List<? extends Set<? extends E>> sets) {
            final ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder<ImmutableSet<E>>(sets.size());
            for (final Set<? extends E> set : sets) {
                final ImmutableSet<E> copy = ImmutableSet.copyOf((Collection<? extends E>)set);
                if (copy.isEmpty()) {
                    return (Set<List<E>>)ImmutableSet.of();
                }
                axesBuilder.add(copy);
            }
            final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
            final ImmutableList<List<E>> listAxes = new ImmutableList<List<E>>() {
                @Override
                public int size() {
                    return axes.size();
                }
                
                @Override
                public List<E> get(final int index) {
                    return (List<E>)((ImmutableSet)axes.get(index)).asList();
                }
                
                @Override
                boolean isPartialView() {
                    return true;
                }
            };
            return new CartesianSet<E>(axes, new CartesianList<E>(listAxes));
        }
        
        private CartesianSet(final ImmutableList<ImmutableSet<E>> axes, final CartesianList<E> delegate) {
            this.axes = axes;
            this.delegate = delegate;
        }
        
        @Override
        protected Collection<List<E>> delegate() {
            return (Collection<List<E>>)this.delegate;
        }
        
        @Override
        public boolean contains(@CheckForNull final Object object) {
            if (!(object instanceof List)) {
                return false;
            }
            final List<?> list = (List<?>)object;
            if (list.size() != this.axes.size()) {
                return false;
            }
            int i = 0;
            for (final Object o : list) {
                if (!this.axes.get(i).contains(o)) {
                    return false;
                }
                ++i;
            }
            return true;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
            if (object instanceof CartesianSet) {
                final CartesianSet<?> that = (CartesianSet<?>)object;
                return this.axes.equals(that.axes);
            }
            return super.equals(object);
        }
        
        @Override
        public int hashCode() {
            int adjust = this.size() - 1;
            for (int i = 0; i < this.axes.size(); ++i) {
                adjust *= 31;
                adjust = ~(~adjust);
            }
            int hash = 1;
            for (final Set<E> axis : this.axes) {
                hash = 31 * hash + this.size() / axis.size() * axis.hashCode();
                hash = ~(~hash);
            }
            hash += adjust;
            return ~(~hash);
        }
    }
    
    private static final class SubSet<E> extends AbstractSet<E>
    {
        private final ImmutableMap<E, Integer> inputSet;
        private final int mask;
        
        SubSet(final ImmutableMap<E, Integer> inputSet, final int mask) {
            this.inputSet = inputSet;
            this.mask = mask;
        }
        
        @Override
        public Iterator<E> iterator() {
            return new UnmodifiableIterator<E>() {
                final ImmutableList<E> elements = SubSet.this.inputSet.keySet().asList();
                int remainingSetBits = SubSet.this.mask;
                
                @Override
                public boolean hasNext() {
                    return this.remainingSetBits != 0;
                }
                
                @Override
                public E next() {
                    final int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
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
        public boolean contains(@CheckForNull final Object o) {
            final Integer index = this.inputSet.get(o);
            return index != null && (this.mask & 1 << index) != 0x0;
        }
    }
    
    private static final class PowerSet<E> extends AbstractSet<Set<E>>
    {
        final ImmutableMap<E, Integer> inputSet;
        
        PowerSet(final Set<E> input) {
            Preconditions.checkArgument(input.size() <= 30, "Too many elements to create power set: %s > 30", input.size());
            this.inputSet = Maps.indexMap(input);
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
            return new AbstractIndexedListIterator<Set<E>>(this.size()) {
                @Override
                protected Set<E> get(final int setBits) {
                    return new SubSet<E>(PowerSet.this.inputSet, setBits);
                }
            };
        }
        
        @Override
        public boolean contains(@CheckForNull final Object obj) {
            if (obj instanceof Set) {
                final Set<?> set = (Set<?>)obj;
                return this.inputSet.keySet().containsAll(set);
            }
            return false;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof PowerSet) {
                final PowerSet<?> that = (PowerSet<?>)obj;
                return this.inputSet.keySet().equals(that.inputSet.keySet());
            }
            return super.equals(obj);
        }
        
        @Override
        public int hashCode() {
            return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.inputSet);
            return new StringBuilder(10 + String.valueOf(value).length()).append("powerSet(").append(value).append(")").toString();
        }
    }
    
    static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable
    {
        private final NavigableSet<E> delegate;
        private final SortedSet<E> unmodifiableDelegate;
        @CheckForNull
        private transient UnmodifiableNavigableSet<E> descendingSet;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableNavigableSet(final NavigableSet<E> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.unmodifiableDelegate = Collections.unmodifiableSortedSet(delegate);
        }
        
        @Override
        protected SortedSet<E> delegate() {
            return this.unmodifiableDelegate;
        }
        
        @Override
        public boolean removeIf(final Predicate<? super E> filter) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Stream<E> stream() {
            return this.delegate.stream();
        }
        
        @Override
        public Stream<E> parallelStream() {
            return this.delegate.parallelStream();
        }
        
        @Override
        public void forEach(final Consumer<? super E> action) {
            this.delegate.forEach(action);
        }
        
        @CheckForNull
        @Override
        public E lower(@ParametricNullness final E e) {
            return this.delegate.lower(e);
        }
        
        @CheckForNull
        @Override
        public E floor(@ParametricNullness final E e) {
            return this.delegate.floor(e);
        }
        
        @CheckForNull
        @Override
        public E ceiling(@ParametricNullness final E e) {
            return this.delegate.ceiling(e);
        }
        
        @CheckForNull
        @Override
        public E higher(@ParametricNullness final E e) {
            return this.delegate.higher(e);
        }
        
        @CheckForNull
        @Override
        public E pollFirst() {
            throw new UnsupportedOperationException();
        }
        
        @CheckForNull
        @Override
        public E pollLast() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public NavigableSet<E> descendingSet() {
            UnmodifiableNavigableSet<E> result = this.descendingSet;
            if (result == null) {
                final UnmodifiableNavigableSet descendingSet = new UnmodifiableNavigableSet((NavigableSet<Object>)this.delegate.descendingSet());
                this.descendingSet = descendingSet;
                result = descendingSet;
                result.descendingSet = this;
            }
            return result;
        }
        
        @Override
        public Iterator<E> descendingIterator() {
            return (Iterator<E>)Iterators.unmodifiableIterator((Iterator<?>)this.delegate.descendingIterator());
        }
        
        @Override
        public NavigableSet<E> subSet(@ParametricNullness final E fromElement, final boolean fromInclusive, @ParametricNullness final E toElement, final boolean toInclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.subSet(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        @Override
        public NavigableSet<E> headSet(@ParametricNullness final E toElement, final boolean inclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
        }
        
        @Override
        public NavigableSet<E> tailSet(@ParametricNullness final E fromElement, final boolean inclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
        }
    }
    
    @GwtIncompatible
    static class DescendingSet<E> extends ForwardingNavigableSet<E>
    {
        private final NavigableSet<E> forward;
        
        DescendingSet(final NavigableSet<E> forward) {
            this.forward = forward;
        }
        
        @Override
        protected NavigableSet<E> delegate() {
            return this.forward;
        }
        
        @CheckForNull
        @Override
        public E lower(@ParametricNullness final E e) {
            return this.forward.higher(e);
        }
        
        @CheckForNull
        @Override
        public E floor(@ParametricNullness final E e) {
            return this.forward.ceiling(e);
        }
        
        @CheckForNull
        @Override
        public E ceiling(@ParametricNullness final E e) {
            return this.forward.floor(e);
        }
        
        @CheckForNull
        @Override
        public E higher(@ParametricNullness final E e) {
            return this.forward.lower(e);
        }
        
        @CheckForNull
        @Override
        public E pollFirst() {
            return this.forward.pollLast();
        }
        
        @CheckForNull
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
        public NavigableSet<E> subSet(@ParametricNullness final E fromElement, final boolean fromInclusive, @ParametricNullness final E toElement, final boolean toInclusive) {
            return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
        }
        
        @Override
        public SortedSet<E> subSet(@ParametricNullness final E fromElement, @ParametricNullness final E toElement) {
            return this.standardSubSet(fromElement, toElement);
        }
        
        @Override
        public NavigableSet<E> headSet(@ParametricNullness final E toElement, final boolean inclusive) {
            return this.forward.tailSet(toElement, inclusive).descendingSet();
        }
        
        @Override
        public SortedSet<E> headSet(@ParametricNullness final E toElement) {
            return this.standardHeadSet(toElement);
        }
        
        @Override
        public NavigableSet<E> tailSet(@ParametricNullness final E fromElement, final boolean inclusive) {
            return this.forward.headSet(fromElement, inclusive).descendingSet();
        }
        
        @Override
        public SortedSet<E> tailSet(@ParametricNullness final E fromElement) {
            return this.standardTailSet(fromElement);
        }
        
        @Override
        public Comparator<? super E> comparator() {
            final Comparator<? super E> forwardComparator = this.forward.comparator();
            if (forwardComparator == null) {
                return Ordering.natural().reverse();
            }
            return reverse(forwardComparator);
        }
        
        private static <T> Ordering<T> reverse(final Comparator<T> forward) {
            return Ordering.from(forward).reverse();
        }
        
        @ParametricNullness
        @Override
        public E first() {
            return this.forward.last();
        }
        
        @ParametricNullness
        @Override
        public E last() {
            return this.forward.first();
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
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public String toString() {
            return this.standardToString();
        }
    }
}
