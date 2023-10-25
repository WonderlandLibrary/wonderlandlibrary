package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
class EmptyImmutableSortedSet<E> extends ImmutableSortedSet<E> {
  EmptyImmutableSortedSet(Comparator<? super E> comparator) {
    super(comparator);
  }
  
  public int size() {
    return 0;
  }
  
  public boolean isEmpty() {
    return true;
  }
  
  public boolean contains(@Nullable Object target) {
    return false;
  }
  
  public boolean containsAll(Collection<?> targets) {
    return targets.isEmpty();
  }
  
  public UnmodifiableIterator<E> iterator() {
    return Iterators.emptyIterator();
  }
  
  @GwtIncompatible("NavigableSet")
  public UnmodifiableIterator<E> descendingIterator() {
    return Iterators.emptyIterator();
  }
  
  boolean isPartialView() {
    return false;
  }
  
  public ImmutableList<E> asList() {
    return ImmutableList.of();
  }
  
  int copyIntoArray(Object[] dst, int offset) {
    return offset;
  }
  
  public boolean equals(@Nullable Object object) {
    if (object instanceof Set) {
      Set<?> that = (Set)object;
      return that.isEmpty();
    } 
    return false;
  }
  
  public int hashCode() {
    return 0;
  }
  
  public String toString() {
    return "[]";
  }
  
  public E first() {
    throw new NoSuchElementException();
  }
  
  public E last() {
    throw new NoSuchElementException();
  }
  
  ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
    return this;
  }
  
  ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
    return this;
  }
  
  ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
    return this;
  }
  
  int indexOf(@Nullable Object target) {
    return -1;
  }
  
  ImmutableSortedSet<E> createDescendingSet() {
    return new EmptyImmutableSortedSet(Ordering.<E>from(this.comparator).reverse());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\EmptyImmutableSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */