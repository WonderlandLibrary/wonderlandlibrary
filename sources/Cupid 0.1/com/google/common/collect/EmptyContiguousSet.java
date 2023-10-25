package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class EmptyContiguousSet<C extends Comparable> extends ContiguousSet<C> {
  EmptyContiguousSet(DiscreteDomain<C> domain) {
    super(domain);
  }
  
  public C first() {
    throw new NoSuchElementException();
  }
  
  public C last() {
    throw new NoSuchElementException();
  }
  
  public int size() {
    return 0;
  }
  
  public ContiguousSet<C> intersection(ContiguousSet<C> other) {
    return this;
  }
  
  public Range<C> range() {
    throw new NoSuchElementException();
  }
  
  public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) {
    throw new NoSuchElementException();
  }
  
  ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
    return this;
  }
  
  ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
    return this;
  }
  
  ContiguousSet<C> tailSetImpl(C fromElement, boolean fromInclusive) {
    return this;
  }
  
  @GwtIncompatible("not used by GWT emulation")
  int indexOf(Object target) {
    return -1;
  }
  
  public UnmodifiableIterator<C> iterator() {
    return Iterators.emptyIterator();
  }
  
  @GwtIncompatible("NavigableSet")
  public UnmodifiableIterator<C> descendingIterator() {
    return Iterators.emptyIterator();
  }
  
  boolean isPartialView() {
    return false;
  }
  
  public boolean isEmpty() {
    return true;
  }
  
  public ImmutableList<C> asList() {
    return ImmutableList.of();
  }
  
  public String toString() {
    return "[]";
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
  
  @GwtIncompatible("serialization")
  private static final class SerializedForm<C extends Comparable> implements Serializable {
    private final DiscreteDomain<C> domain;
    
    private static final long serialVersionUID = 0L;
    
    private SerializedForm(DiscreteDomain<C> domain) {
      this.domain = domain;
    }
    
    private Object readResolve() {
      return new EmptyContiguousSet<C>(this.domain);
    }
  }
  
  @GwtIncompatible("serialization")
  Object writeReplace() {
    return new SerializedForm<Comparable>(this.domain);
  }
  
  @GwtIncompatible("NavigableSet")
  ImmutableSortedSet<C> createDescendingSet() {
    return new EmptyImmutableSortedSet<C>(Ordering.<Comparable>natural().reverse());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\EmptyContiguousSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */