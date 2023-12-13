/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.AllEqualOrdering;
import com.google.common.collect.ByFunctionOrdering;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ComparatorOrdering;
import com.google.common.collect.CompoundOrdering;
import com.google.common.collect.ExplicitOrdering;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.LexicographicalOrdering;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.NaturalOrdering;
import com.google.common.collect.NullsFirstOrdering;
import com.google.common.collect.NullsLastOrdering;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Platform;
import com.google.common.collect.ReverseOrdering;
import com.google.common.collect.UsingToStringOrdering;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class Ordering<T>
implements Comparator<T> {
    static final int LEFT_IS_GREATER = 1;
    static final int RIGHT_IS_GREATER = -1;

    @GwtCompatible(serializable=true)
    public static <C extends Comparable> Ordering<C> natural() {
        return NaturalOrdering.INSTANCE;
    }

    @GwtCompatible(serializable=true)
    public static <T> Ordering<T> from(Comparator<T> comparator) {
        return comparator instanceof Ordering ? (Ordering<T>)comparator : new ComparatorOrdering<T>(comparator);
    }

    @Deprecated
    @GwtCompatible(serializable=true)
    public static <T> Ordering<T> from(Ordering<T> ordering) {
        return Preconditions.checkNotNull(ordering);
    }

    @GwtCompatible(serializable=true)
    public static <T> Ordering<T> explicit(List<T> valuesInOrder) {
        return new ExplicitOrdering<T>(valuesInOrder);
    }

    @GwtCompatible(serializable=true)
    public static <T> Ordering<T> explicit(T leastValue, T ... remainingValuesInOrder) {
        return Ordering.explicit(Lists.asList(leastValue, remainingValuesInOrder));
    }

    @GwtCompatible(serializable=true)
    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }

    @GwtCompatible(serializable=true)
    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }

    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }

    protected Ordering() {
    }

    @GwtCompatible(serializable=true)
    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering(this);
    }

    @GwtCompatible(serializable=true)
    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering(this);
    }

    @GwtCompatible(serializable=true)
    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering(this);
    }

    @GwtCompatible(serializable=true)
    public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
        return new ByFunctionOrdering<F, T>(function, this);
    }

    <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
        return this.onResultOf(Maps.keyFunction());
    }

    @GwtCompatible(serializable=true)
    public <U extends T> Ordering<U> compound(Comparator<? super U> secondaryComparator) {
        return new CompoundOrdering<U>(this, Preconditions.checkNotNull(secondaryComparator));
    }

    @GwtCompatible(serializable=true)
    public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> comparators) {
        return new CompoundOrdering(comparators);
    }

    @GwtCompatible(serializable=true)
    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return new LexicographicalOrdering(this);
    }

    @Override
    public abstract int compare(@Nullable T var1, @Nullable T var2);

    public <E extends T> E min(Iterator<E> iterator) {
        E minSoFar = iterator.next();
        while (iterator.hasNext()) {
            minSoFar = this.min(minSoFar, iterator.next());
        }
        return minSoFar;
    }

    public <E extends T> E min(Iterable<E> iterable) {
        return this.min(iterable.iterator());
    }

    public <E extends T> E min(@Nullable E a2, @Nullable E b2) {
        return this.compare(a2, b2) <= 0 ? a2 : b2;
    }

    public <E extends T> E min(@Nullable E a2, @Nullable E b2, @Nullable E c2, E ... rest) {
        E minSoFar = this.min(this.min(a2, b2), c2);
        for (E r2 : rest) {
            minSoFar = this.min(minSoFar, r2);
        }
        return minSoFar;
    }

    public <E extends T> E max(Iterator<E> iterator) {
        E maxSoFar = iterator.next();
        while (iterator.hasNext()) {
            maxSoFar = this.max(maxSoFar, iterator.next());
        }
        return maxSoFar;
    }

    public <E extends T> E max(Iterable<E> iterable) {
        return this.max(iterable.iterator());
    }

    public <E extends T> E max(@Nullable E a2, @Nullable E b2) {
        return this.compare(a2, b2) >= 0 ? a2 : b2;
    }

    public <E extends T> E max(@Nullable E a2, @Nullable E b2, @Nullable E c2, E ... rest) {
        E maxSoFar = this.max(this.max(a2, b2), c2);
        for (E r2 : rest) {
            maxSoFar = this.max(maxSoFar, r2);
        }
        return maxSoFar;
    }

    public <E extends T> List<E> leastOf(Iterable<E> iterable, int k2) {
        Collection collection;
        if (iterable instanceof Collection && (long)(collection = (Collection)iterable).size() <= 2L * (long)k2) {
            Object[] array = collection.toArray();
            Arrays.sort(array, this);
            if (array.length > k2) {
                array = ObjectArrays.arraysCopyOf(array, k2);
            }
            return Collections.unmodifiableList(Arrays.asList(array));
        }
        return this.leastOf(iterable.iterator(), k2);
    }

    public <E extends T> List<E> leastOf(Iterator<E> elements, int k2) {
        E e2;
        Preconditions.checkNotNull(elements);
        CollectPreconditions.checkNonnegative(k2, "k");
        if (k2 == 0 || !elements.hasNext()) {
            return ImmutableList.of();
        }
        if (k2 >= 0x3FFFFFFF) {
            ArrayList<E> list = Lists.newArrayList(elements);
            Collections.sort(list, this);
            if (list.size() > k2) {
                list.subList(k2, list.size()).clear();
            }
            list.trimToSize();
            return Collections.unmodifiableList(list);
        }
        int bufferCap = k2 * 2;
        Object[] buffer = new Object[bufferCap];
        Object threshold = elements.next();
        buffer[0] = threshold;
        int bufferSize = 1;
        while (bufferSize < k2 && elements.hasNext()) {
            e2 = elements.next();
            buffer[bufferSize++] = e2;
            threshold = this.max(threshold, e2);
        }
        while (elements.hasNext()) {
            e2 = elements.next();
            if (this.compare(e2, threshold) >= 0) continue;
            buffer[bufferSize++] = e2;
            if (bufferSize != bufferCap) continue;
            int left = 0;
            int right = bufferCap - 1;
            int minThresholdPosition = 0;
            while (left < right) {
                int pivotIndex = left + right + 1 >>> 1;
                int pivotNewIndex = this.partition(buffer, left, right, pivotIndex);
                if (pivotNewIndex > k2) {
                    right = pivotNewIndex - 1;
                    continue;
                }
                if (pivotNewIndex >= k2) break;
                left = Math.max(pivotNewIndex, left + 1);
                minThresholdPosition = pivotNewIndex;
            }
            bufferSize = k2;
            threshold = buffer[minThresholdPosition];
            for (int i2 = minThresholdPosition + 1; i2 < bufferSize; ++i2) {
                threshold = this.max(threshold, buffer[i2]);
            }
        }
        Arrays.sort(buffer, 0, bufferSize, this);
        bufferSize = Math.min(bufferSize, k2);
        return Collections.unmodifiableList(Arrays.asList(ObjectArrays.arraysCopyOf(buffer, bufferSize)));
    }

    private <E extends T> int partition(E[] values, int left, int right, int pivotIndex) {
        E pivotValue = values[pivotIndex];
        values[pivotIndex] = values[right];
        values[right] = pivotValue;
        int storeIndex = left;
        for (int i2 = left; i2 < right; ++i2) {
            if (this.compare(values[i2], pivotValue) >= 0) continue;
            ObjectArrays.swap(values, storeIndex, i2);
            ++storeIndex;
        }
        ObjectArrays.swap(values, right, storeIndex);
        return storeIndex;
    }

    public <E extends T> List<E> greatestOf(Iterable<E> iterable, int k2) {
        return this.reverse().leastOf(iterable, k2);
    }

    public <E extends T> List<E> greatestOf(Iterator<E> iterator, int k2) {
        return this.reverse().leastOf(iterator, k2);
    }

    public <E extends T> List<E> sortedCopy(Iterable<E> elements) {
        Object[] array = Iterables.toArray(elements);
        Arrays.sort(array, this);
        return Lists.newArrayList(Arrays.asList(array));
    }

    public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> elements) {
        Object[] array;
        for (Object e2 : array = Iterables.toArray(elements)) {
            Preconditions.checkNotNull(e2);
        }
        Arrays.sort(array, this);
        return ImmutableList.asImmutableList(array);
    }

    public boolean isOrdered(Iterable<? extends T> iterable) {
        Iterator<T> it = iterable.iterator();
        if (it.hasNext()) {
            T prev = it.next();
            while (it.hasNext()) {
                T next = it.next();
                if (this.compare(prev, next) > 0) {
                    return false;
                }
                prev = next;
            }
        }
        return true;
    }

    public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
        Iterator<T> it = iterable.iterator();
        if (it.hasNext()) {
            T prev = it.next();
            while (it.hasNext()) {
                T next = it.next();
                if (this.compare(prev, next) >= 0) {
                    return false;
                }
                prev = next;
            }
        }
        return true;
    }

    public int binarySearch(List<? extends T> sortedList, @Nullable T key) {
        return Collections.binarySearch(sortedList, key, this);
    }

    @VisibleForTesting
    static class IncomparableValueException
    extends ClassCastException {
        final Object value;
        private static final long serialVersionUID = 0L;

        IncomparableValueException(Object value) {
            super("Cannot compare value: " + value);
            this.value = value;
        }
    }

    @VisibleForTesting
    static class ArbitraryOrdering
    extends Ordering<Object> {
        private Map<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeComputingMap(new Function<Object, Integer>(){
            final AtomicInteger counter = new AtomicInteger(0);

            @Override
            public Integer apply(Object from) {
                return this.counter.getAndIncrement();
            }
        });

        ArbitraryOrdering() {
        }

        @Override
        public int compare(Object left, Object right) {
            int rightCode;
            if (left == right) {
                return 0;
            }
            if (left == null) {
                return -1;
            }
            if (right == null) {
                return 1;
            }
            int leftCode = this.identityHashCode(left);
            if (leftCode != (rightCode = this.identityHashCode(right))) {
                return leftCode < rightCode ? -1 : 1;
            }
            int result = this.uids.get(left).compareTo(this.uids.get(right));
            if (result == 0) {
                throw new AssertionError();
            }
            return result;
        }

        public String toString() {
            return "Ordering.arbitrary()";
        }

        int identityHashCode(Object object) {
            return System.identityHashCode(object);
        }
    }

    private static class ArbitraryOrderingHolder {
        static final Ordering<Object> ARBITRARY_ORDERING = new ArbitraryOrdering();

        private ArbitraryOrderingHolder() {
        }
    }
}

