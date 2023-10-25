package com.google.common.collect;

import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

final class DescendingImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
  private final transient ImmutableSortedMultiset<E> forward;
  
  DescendingImmutableSortedMultiset(ImmutableSortedMultiset<E> forward) {
    this.forward = forward;
  }
  
  public int count(@Nullable Object element) {
    return this.forward.count(element);
  }
  
  public Multiset.Entry<E> firstEntry() {
    return this.forward.lastEntry();
  }
  
  public Multiset.Entry<E> lastEntry() {
    return this.forward.firstEntry();
  }
  
  public int size() {
    return this.forward.size();
  }
  
  public ImmutableSortedSet<E> elementSet() {
    return this.forward.elementSet().descendingSet();
  }
  
  Multiset.Entry<E> getEntry(int index) {
    return this.forward.entrySet().asList().reverse().get(index);
  }
  
  public ImmutableSortedMultiset<E> descendingMultiset() {
    return this.forward;
  }
  
  public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
    return this.forward.tailMultiset(upperBound, boundType).descendingMultiset();
  }
  
  public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
    return this.forward.headMultiset(lowerBound, boundType).descendingMultiset();
  }
  
  boolean isPartialView() {
    return this.forward.isPartialView();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\DescendingImmutableSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */