// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import com.google.common.base.Function;
import java.util.List;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class Ordering<T> implements Comparator<T>
{
    static final int LEFT_IS_GREATER = 1;
    static final int RIGHT_IS_GREATER = -1;
    
    @GwtCompatible(serializable = true)
    public static <C extends Comparable> Ordering<C> natural() {
        return (Ordering<C>)NaturalOrdering.INSTANCE;
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> from(final Comparator<T> comparator) {
        return (comparator instanceof Ordering) ? ((Ordering)comparator) : new ComparatorOrdering<T>(comparator);
    }
    
    @Deprecated
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> from(final Ordering<T> ordering) {
        return Preconditions.checkNotNull(ordering);
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(final List<T> valuesInOrder) {
        return new ExplicitOrdering<T>(valuesInOrder);
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(final T leastValue, final T... remainingValuesInOrder) {
        return explicit((List<T>)Lists.asList(leastValue, (T[])remainingValuesInOrder));
    }
    
    @GwtCompatible(serializable = true)
    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }
    
    @GwtCompatible(serializable = true)
    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }
    
    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }
    
    protected Ordering() {
    }
    
    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering<S>(this);
    }
    
    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering<S>(this);
    }
    
    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering<S>(this);
    }
    
    @GwtCompatible(serializable = true)
    public <F> Ordering<F> onResultOf(final Function<F, ? extends T> function) {
        return (Ordering<F>)new ByFunctionOrdering((Function<Object, ?>)function, (Ordering<Object>)this);
    }
    
     <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
        return this.onResultOf((Function<Map.Entry<T2, ?>, ? extends T>)Maps.keyFunction());
    }
    
    @GwtCompatible(serializable = true)
    public <U extends T> Ordering<U> compound(final Comparator<? super U> secondaryComparator) {
        return new CompoundOrdering<U>(this, Preconditions.checkNotNull(secondaryComparator));
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> compound(final Iterable<? extends Comparator<? super T>> comparators) {
        return new CompoundOrdering<T>(comparators);
    }
    
    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return (Ordering<Iterable<S>>)new LexicographicalOrdering((Comparator<? super Object>)this);
    }
    
    @CanIgnoreReturnValue
    @Override
    public abstract int compare(@ParametricNullness final T p0, @ParametricNullness final T p1);
    
    @ParametricNullness
    public <E extends T> E min(final Iterator<E> iterator) {
        E minSoFar = iterator.next();
        while (iterator.hasNext()) {
            minSoFar = this.min(minSoFar, iterator.next());
        }
        return minSoFar;
    }
    
    @ParametricNullness
    public <E extends T> E min(final Iterable<E> iterable) {
        return this.min(iterable.iterator());
    }
    
    @ParametricNullness
    public <E extends T> E min(@ParametricNullness final E a, @ParametricNullness final E b) {
        return (this.compare(a, b) <= 0) ? a : b;
    }
    
    @ParametricNullness
    public <E extends T> E min(@ParametricNullness final E a, @ParametricNullness final E b, @ParametricNullness final E c, final E... rest) {
        E minSoFar = this.min(this.min(a, b), c);
        for (final E r : rest) {
            minSoFar = this.min(minSoFar, r);
        }
        return minSoFar;
    }
    
    @ParametricNullness
    public <E extends T> E max(final Iterator<E> iterator) {
        E maxSoFar = iterator.next();
        while (iterator.hasNext()) {
            maxSoFar = this.max(maxSoFar, iterator.next());
        }
        return maxSoFar;
    }
    
    @ParametricNullness
    public <E extends T> E max(final Iterable<E> iterable) {
        return this.max(iterable.iterator());
    }
    
    @ParametricNullness
    public <E extends T> E max(@ParametricNullness final E a, @ParametricNullness final E b) {
        return (this.compare(a, b) >= 0) ? a : b;
    }
    
    @ParametricNullness
    public <E extends T> E max(@ParametricNullness final E a, @ParametricNullness final E b, @ParametricNullness final E c, final E... rest) {
        E maxSoFar = this.max(this.max(a, b), c);
        for (final E r : rest) {
            maxSoFar = this.max(maxSoFar, r);
        }
        return maxSoFar;
    }
    
    public <E extends T> List<E> leastOf(final Iterable<E> iterable, final int k) {
        if (iterable instanceof Collection) {
            final Collection<E> collection = (Collection<E>)(Collection)iterable;
            if (collection.size() <= 2L * k) {
                E[] array = (E[])collection.toArray();
                Arrays.sort(array, this);
                if (array.length > k) {
                    array = Arrays.copyOf(array, k);
                }
                return Collections.unmodifiableList((List<? extends E>)Arrays.asList((T[])array));
            }
        }
        return this.leastOf(iterable.iterator(), k);
    }
    
    public <E extends T> List<E> leastOf(final Iterator<E> iterator, final int k) {
        Preconditions.checkNotNull(iterator);
        CollectPreconditions.checkNonnegative(k, "k");
        if (k == 0 || !iterator.hasNext()) {
            return Collections.emptyList();
        }
        if (k >= 1073741823) {
            final ArrayList<E> list = Lists.newArrayList((Iterator<? extends E>)iterator);
            Collections.sort(list, this);
            if (list.size() > k) {
                list.subList(k, list.size()).clear();
            }
            list.trimToSize();
            return Collections.unmodifiableList((List<? extends E>)list);
        }
        final TopKSelector<E> selector = TopKSelector.least(k, (Comparator<? super E>)this);
        selector.offerAll((Iterator<? extends E>)iterator);
        return selector.topK();
    }
    
    public <E extends T> List<E> greatestOf(final Iterable<E> iterable, final int k) {
        return (List<E>)this.reverse().leastOf((Iterable<Object>)iterable, k);
    }
    
    public <E extends T> List<E> greatestOf(final Iterator<E> iterator, final int k) {
        return (List<E>)this.reverse().leastOf((Iterator<Object>)iterator, k);
    }
    
    public <E extends T> List<E> sortedCopy(final Iterable<E> elements) {
        final E[] array = (E[])Iterables.toArray(elements);
        Arrays.sort(array, this);
        return (List<E>)Lists.newArrayList((Iterable<?>)Arrays.asList(array));
    }
    
    public <E extends T> ImmutableList<E> immutableSortedCopy(final Iterable<E> elements) {
        return ImmutableList.sortedCopyOf((Comparator<? super E>)this, (Iterable<? extends E>)elements);
    }
    
    public boolean isOrdered(final Iterable<? extends T> iterable) {
        final Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T prev = (T)it.next();
            while (it.hasNext()) {
                final T next = (T)it.next();
                if (this.compare(prev, next) > 0) {
                    return false;
                }
                prev = next;
            }
        }
        return true;
    }
    
    public boolean isStrictlyOrdered(final Iterable<? extends T> iterable) {
        final Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T prev = (T)it.next();
            while (it.hasNext()) {
                final T next = (T)it.next();
                if (this.compare(prev, next) >= 0) {
                    return false;
                }
                prev = next;
            }
        }
        return true;
    }
    
    @Deprecated
    public int binarySearch(final List<? extends T> sortedList, @ParametricNullness final T key) {
        return Collections.binarySearch(sortedList, key, this);
    }
    
    private static class ArbitraryOrderingHolder
    {
        static final Ordering<Object> ARBITRARY_ORDERING;
        
        static {
            ARBITRARY_ORDERING = new ArbitraryOrdering();
        }
    }
    
    @VisibleForTesting
    static class ArbitraryOrdering extends Ordering<Object>
    {
        private final AtomicInteger counter;
        private final ConcurrentMap<Object, Integer> uids;
        
        ArbitraryOrdering() {
            this.counter = new AtomicInteger(0);
            this.uids = Platform.tryWeakKeys(new MapMaker()).makeMap();
        }
        
        private Integer getUid(final Object obj) {
            Integer uid = this.uids.get(obj);
            if (uid == null) {
                uid = this.counter.getAndIncrement();
                final Integer alreadySet = this.uids.putIfAbsent(obj, uid);
                if (alreadySet != null) {
                    uid = alreadySet;
                }
            }
            return uid;
        }
        
        @Override
        public int compare(@CheckForNull final Object left, @CheckForNull final Object right) {
            if (left == right) {
                return 0;
            }
            if (left == null) {
                return -1;
            }
            if (right == null) {
                return 1;
            }
            final int leftCode = this.identityHashCode(left);
            final int rightCode = this.identityHashCode(right);
            if (leftCode != rightCode) {
                return (leftCode < rightCode) ? -1 : 1;
            }
            final int result = this.getUid(left).compareTo(this.getUid(right));
            if (result == 0) {
                throw new AssertionError();
            }
            return result;
        }
        
        @Override
        public String toString() {
            return "Ordering.arbitrary()";
        }
        
        int identityHashCode(final Object object) {
            return System.identityHashCode(object);
        }
    }
    
    @VisibleForTesting
    static class IncomparableValueException extends ClassCastException
    {
        final Object value;
        private static final long serialVersionUID = 0L;
        
        IncomparableValueException(final Object value) {
            final String value2 = String.valueOf(value);
            super(new StringBuilder(22 + String.valueOf(value2).length()).append("Cannot compare value: ").append(value2).toString());
            this.value = value;
        }
    }
}
