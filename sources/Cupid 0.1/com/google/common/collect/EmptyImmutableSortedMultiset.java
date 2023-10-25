package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

final class EmptyImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
  private final ImmutableSortedSet<E> elementSet;
  
  EmptyImmutableSortedMultiset(Comparator<? super E> comparator) {
    this.elementSet = ImmutableSortedSet.emptySet(comparator);
  }
  
  public Multiset.Entry<E> firstEntry() {
    return null;
  }
  
  public Multiset.Entry<E> lastEntry() {
    return null;
  }
  
  public int count(@Nullable Object element) {
    return 0;
  }
  
  public boolean containsAll(Collection<?> targets) {
    return targets.isEmpty();
  }
  
  public int size() {
    return 0;
  }
  
  public ImmutableSortedSet<E> elementSet() {
    return this.elementSet;
  }
  
  Multiset.Entry<E> getEntry(int index) {
    throw new AssertionError("should never be called");
  }
  
  public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
    Preconditions.checkNotNull(upperBound);
    Preconditions.checkNotNull(boundType);
    return this;
  }
  
  public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
    Preconditions.checkNotNull(lowerBound);
    Preconditions.checkNotNull(boundType);
    return this;
  }
  
  public UnmodifiableIterator<E> iterator() {
    return Iterators.emptyIterator();
  }
  
  public boolean equals(@Nullable Object object) {
    if (object instanceof Multiset) {
      Multiset<?> other = (Multiset)object;
      return other.isEmpty();
    } 
    return false;
  }
  
  boolean isPartialView() {
    return false;
  }
  
  int copyIntoArray(Object[] dst, int offset) {
    return offset;
  }
  
  public ImmutableList<E> asList() {
    return ImmutableList.of();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\EmptyImmutableSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */