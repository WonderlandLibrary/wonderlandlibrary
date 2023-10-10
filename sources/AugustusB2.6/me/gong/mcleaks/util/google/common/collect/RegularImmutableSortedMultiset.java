// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Set;
import java.util.SortedSet;
import java.util.NavigableSet;
import me.gong.mcleaks.util.google.common.primitives.Ints;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.function.ObjIntConsumer;
import java.util.Comparator;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

@GwtIncompatible
final class RegularImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E>
{
    private static final long[] ZERO_CUMULATIVE_COUNTS;
    static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET;
    private final transient RegularImmutableSortedSet<E> elementSet;
    private final transient long[] cumulativeCounts;
    private final transient int offset;
    private final transient int length;
    
    RegularImmutableSortedMultiset(final Comparator<? super E> comparator) {
        this.elementSet = ImmutableSortedSet.emptySet(comparator);
        this.cumulativeCounts = RegularImmutableSortedMultiset.ZERO_CUMULATIVE_COUNTS;
        this.offset = 0;
        this.length = 0;
    }
    
    RegularImmutableSortedMultiset(final RegularImmutableSortedSet<E> elementSet, final long[] cumulativeCounts, final int offset, final int length) {
        this.elementSet = elementSet;
        this.cumulativeCounts = cumulativeCounts;
        this.offset = offset;
        this.length = length;
    }
    
    private int getCount(final int index) {
        return (int)(this.cumulativeCounts[this.offset + index + 1] - this.cumulativeCounts[this.offset + index]);
    }
    
    @Override
    Multiset.Entry<E> getEntry(final int index) {
        return Multisets.immutableEntry(this.elementSet.asList().get(index), this.getCount(index));
    }
    
    @Override
    public void forEachEntry(final ObjIntConsumer<? super E> action) {
        Preconditions.checkNotNull(action);
        for (int i = 0; i < this.size(); ++i) {
            action.accept((Object)this.elementSet.asList().get(i), this.getCount(i));
        }
    }
    
    @Override
    public Multiset.Entry<E> firstEntry() {
        return this.isEmpty() ? null : this.getEntry(0);
    }
    
    @Override
    public Multiset.Entry<E> lastEntry() {
        return this.isEmpty() ? null : this.getEntry(this.length - 1);
    }
    
    @Override
    public int count(@Nullable final Object element) {
        final int index = this.elementSet.indexOf(element);
        return (index >= 0) ? this.getCount(index) : 0;
    }
    
    @Override
    public int size() {
        final long size = this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset];
        return Ints.saturatedCast(size);
    }
    
    @Override
    public ImmutableSortedSet<E> elementSet() {
        return this.elementSet;
    }
    
    @Override
    public ImmutableSortedMultiset<E> headMultiset(final E upperBound, final BoundType boundType) {
        return this.getSubMultiset(0, this.elementSet.headIndex(upperBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED));
    }
    
    @Override
    public ImmutableSortedMultiset<E> tailMultiset(final E lowerBound, final BoundType boundType) {
        return this.getSubMultiset(this.elementSet.tailIndex(lowerBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED), this.length);
    }
    
    ImmutableSortedMultiset<E> getSubMultiset(final int from, final int to) {
        Preconditions.checkPositionIndexes(from, to, this.length);
        if (from == to) {
            return ImmutableSortedMultiset.emptyMultiset(this.comparator());
        }
        if (from == 0 && to == this.length) {
            return this;
        }
        final RegularImmutableSortedSet<E> subElementSet = this.elementSet.getSubSet(from, to);
        return new RegularImmutableSortedMultiset((RegularImmutableSortedSet<Object>)subElementSet, this.cumulativeCounts, this.offset + from, to - from);
    }
    
    @Override
    boolean isPartialView() {
        return this.offset > 0 || this.length < this.cumulativeCounts.length - 1;
    }
    
    static {
        ZERO_CUMULATIVE_COUNTS = new long[] { 0L };
        NATURAL_EMPTY_MULTISET = new RegularImmutableSortedMultiset<Comparable>(Ordering.natural());
    }
}
