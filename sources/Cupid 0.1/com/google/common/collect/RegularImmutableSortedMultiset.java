package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

final class RegularImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
  private final transient RegularImmutableSortedSet<E> elementSet;
  
  private final transient int[] counts;
  
  private final transient long[] cumulativeCounts;
  
  private final transient int offset;
  
  private final transient int length;
  
  RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> elementSet, int[] counts, long[] cumulativeCounts, int offset, int length) {
    this.elementSet = elementSet;
    this.counts = counts;
    this.cumulativeCounts = cumulativeCounts;
    this.offset = offset;
    this.length = length;
  }
  
  Multiset.Entry<E> getEntry(int index) {
    return Multisets.immutableEntry(this.elementSet.asList().get(index), this.counts[this.offset + index]);
  }
  
  public Multiset.Entry<E> firstEntry() {
    return getEntry(0);
  }
  
  public Multiset.Entry<E> lastEntry() {
    return getEntry(this.length - 1);
  }
  
  public int count(@Nullable Object element) {
    int index = this.elementSet.indexOf(element);
    return (index == -1) ? 0 : this.counts[index + this.offset];
  }
  
  public int size() {
    long size = this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset];
    return Ints.saturatedCast(size);
  }
  
  public ImmutableSortedSet<E> elementSet() {
    return this.elementSet;
  }
  
  public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
    return getSubMultiset(0, this.elementSet.headIndex(upperBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED)));
  }
  
  public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
    return getSubMultiset(this.elementSet.tailIndex(lowerBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED)), this.length);
  }
  
  ImmutableSortedMultiset<E> getSubMultiset(int from, int to) {
    Preconditions.checkPositionIndexes(from, to, this.length);
    if (from == to)
      return emptyMultiset(comparator()); 
    if (from == 0 && to == this.length)
      return this; 
    RegularImmutableSortedSet<E> subElementSet = (RegularImmutableSortedSet<E>)this.elementSet.getSubSet(from, to);
    return new RegularImmutableSortedMultiset(subElementSet, this.counts, this.cumulativeCounts, this.offset + from, to - from);
  }
  
  boolean isPartialView() {
    return (this.offset > 0 || this.length < this.counts.length);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\RegularImmutableSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */