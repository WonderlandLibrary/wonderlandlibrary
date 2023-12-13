/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Cut;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.RangeSet;
import com.google.common.collect.RegularImmutableSortedMap;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.SortedLists;
import com.google.common.collect.TreeRangeMap;
import com.google.common.collect.TreeRangeSet;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible(value="NavigableMap")
public class ImmutableRangeMap<K extends Comparable<?>, V>
implements RangeMap<K, V> {
    private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(ImmutableList.of(), ImmutableList.of());
    private final ImmutableList<Range<K>> ranges;
    private final ImmutableList<V> values;

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
        return EMPTY;
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value) {
        return new ImmutableRangeMap<K, V>(ImmutableList.of(range), ImmutableList.of(value));
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
        if (rangeMap instanceof ImmutableRangeMap) {
            return (ImmutableRangeMap)rangeMap;
        }
        Map<Range<K>, V> map = rangeMap.asMapOfRanges();
        ImmutableList.Builder rangesBuilder = new ImmutableList.Builder(map.size());
        ImmutableList.Builder valuesBuilder = new ImmutableList.Builder(map.size());
        for (Map.Entry<Range<K>, V> entry : map.entrySet()) {
            rangesBuilder.add(entry.getKey());
            valuesBuilder.add(entry.getValue());
        }
        return new ImmutableRangeMap<K, V>(rangesBuilder.build(), valuesBuilder.build());
    }

    public static <K extends Comparable<?>, V> Builder<K, V> builder() {
        return new Builder();
    }

    ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values) {
        this.ranges = ranges;
        this.values = values;
    }

    @Override
    @Nullable
    public V get(K key) {
        int index = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (index == -1) {
            return null;
        }
        Range range = (Range)this.ranges.get(index);
        return range.contains(key) ? (V)this.values.get(index) : null;
    }

    @Override
    @Nullable
    public Map.Entry<Range<K>, V> getEntry(K key) {
        int index = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (index == -1) {
            return null;
        }
        Range range = (Range)this.ranges.get(index);
        return range.contains(key) ? Maps.immutableEntry(range, this.values.get(index)) : null;
    }

    @Override
    public Range<K> span() {
        if (this.ranges.isEmpty()) {
            throw new NoSuchElementException();
        }
        Range firstRange = (Range)this.ranges.get(0);
        Range lastRange = (Range)this.ranges.get(this.ranges.size() - 1);
        return Range.create(firstRange.lowerBound, lastRange.upperBound);
    }

    @Override
    public void put(Range<K> range, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(RangeMap<K, V> rangeMap) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Range<K> range) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableMap<Range<K>, V> asMapOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableMap.of();
        }
        RegularImmutableSortedSet rangeSet = new RegularImmutableSortedSet(this.ranges, Range.RANGE_LEX_ORDERING);
        return new RegularImmutableSortedMap(rangeSet, this.values);
    }

    @Override
    public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
        int upperIndex;
        if (Preconditions.checkNotNull(range).isEmpty()) {
            return ImmutableRangeMap.of();
        }
        if (this.ranges.isEmpty() || range.encloses(this.span())) {
            return this;
        }
        int lowerIndex = SortedLists.binarySearch(this.ranges, Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        if (lowerIndex >= (upperIndex = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER))) {
            return ImmutableRangeMap.of();
        }
        final int off = lowerIndex;
        final int len = upperIndex - lowerIndex;
        ImmutableList subRanges = new ImmutableList<Range<K>>(){

            @Override
            public int size() {
                return len;
            }

            @Override
            public Range<K> get(int index) {
                Preconditions.checkElementIndex(index, len);
                if (index == 0 || index == len - 1) {
                    return ((Range)ImmutableRangeMap.this.ranges.get(index + off)).intersection(range);
                }
                return (Range)ImmutableRangeMap.this.ranges.get(index + off);
            }

            @Override
            boolean isPartialView() {
                return true;
            }
        };
        final ImmutableRangeMap outer = this;
        return new ImmutableRangeMap<K, V>(subRanges, (ImmutableList)this.values.subList(lowerIndex, upperIndex)){

            @Override
            public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
                if (range.isConnected(subRange)) {
                    return outer.subRangeMap(subRange.intersection(range));
                }
                return ImmutableRangeMap.of();
            }
        };
    }

    @Override
    public int hashCode() {
        return ((ImmutableMap)this.asMapOfRanges()).hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o2) {
        if (o2 instanceof RangeMap) {
            RangeMap rangeMap = (RangeMap)o2;
            return ((ImmutableMap)this.asMapOfRanges()).equals(rangeMap.asMapOfRanges());
        }
        return false;
    }

    @Override
    public String toString() {
        return ((ImmutableMap)this.asMapOfRanges()).toString();
    }

    public static final class Builder<K extends Comparable<?>, V> {
        private final RangeSet<K> keyRanges = TreeRangeSet.create();
        private final RangeMap<K, V> rangeMap = TreeRangeMap.create();

        public Builder<K, V> put(Range<K> range, V value) {
            Preconditions.checkNotNull(range);
            Preconditions.checkNotNull(value);
            Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
            if (!this.keyRanges.complement().encloses(range)) {
                for (Map.Entry<Range<K>, V> entry : this.rangeMap.asMapOfRanges().entrySet()) {
                    Range<K> key = entry.getKey();
                    if (!key.isConnected(range) || key.intersection(range).isEmpty()) continue;
                    throw new IllegalArgumentException("Overlapping ranges: range " + range + " overlaps with entry " + entry);
                }
            }
            this.keyRanges.add(range);
            this.rangeMap.put(range, value);
            return this;
        }

        public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap) {
            for (Map.Entry<Range<K>, V> entry : rangeMap.asMapOfRanges().entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableRangeMap<K, V> build() {
            Map<Range<K>, V> map = this.rangeMap.asMapOfRanges();
            ImmutableList.Builder rangesBuilder = new ImmutableList.Builder(map.size());
            ImmutableList.Builder valuesBuilder = new ImmutableList.Builder(map.size());
            for (Map.Entry<Range<K>, V> entry : map.entrySet()) {
                rangesBuilder.add(entry.getKey());
                valuesBuilder.add(entry.getValue());
            }
            return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
        }
    }
}

