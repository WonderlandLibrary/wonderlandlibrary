package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class SortedMultisets {
  static class ElementSet<E> extends Multisets.ElementSet<E> implements SortedSet<E> {
    private final SortedMultiset<E> multiset;
    
    ElementSet(SortedMultiset<E> multiset) {
      this.multiset = multiset;
    }
    
    final SortedMultiset<E> multiset() {
      return this.multiset;
    }
    
    public Comparator<? super E> comparator() {
      return multiset().comparator();
    }
    
    public SortedSet<E> subSet(E fromElement, E toElement) {
      return multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet();
    }
    
    public SortedSet<E> headSet(E toElement) {
      return multiset().headMultiset(toElement, BoundType.OPEN).elementSet();
    }
    
    public SortedSet<E> tailSet(E fromElement) {
      return multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet();
    }
    
    public E first() {
      return SortedMultisets.getElementOrThrow(multiset().firstEntry());
    }
    
    public E last() {
      return SortedMultisets.getElementOrThrow(multiset().lastEntry());
    }
  }
  
  @GwtIncompatible("Navigable")
  static class NavigableElementSet<E> extends ElementSet<E> implements NavigableSet<E> {
    NavigableElementSet(SortedMultiset<E> multiset) {
      super(multiset);
    }
    
    public E lower(E e) {
      return SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.OPEN).lastEntry());
    }
    
    public E floor(E e) {
      return SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.CLOSED).lastEntry());
    }
    
    public E ceiling(E e) {
      return SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.CLOSED).firstEntry());
    }
    
    public E higher(E e) {
      return SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.OPEN).firstEntry());
    }
    
    public NavigableSet<E> descendingSet() {
      return new NavigableElementSet(multiset().descendingMultiset());
    }
    
    public Iterator<E> descendingIterator() {
      return descendingSet().iterator();
    }
    
    public E pollFirst() {
      return SortedMultisets.getElementOrNull(multiset().pollFirstEntry());
    }
    
    public E pollLast() {
      return SortedMultisets.getElementOrNull(multiset().pollLastEntry());
    }
    
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
      return new NavigableElementSet(multiset().subMultiset(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive)));
    }
    
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
      return new NavigableElementSet(multiset().headMultiset(toElement, BoundType.forBoolean(inclusive)));
    }
    
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
      return new NavigableElementSet(multiset().tailMultiset(fromElement, BoundType.forBoolean(inclusive)));
    }
  }
  
  private static <E> E getElementOrThrow(Multiset.Entry<E> entry) {
    if (entry == null)
      throw new NoSuchElementException(); 
    return entry.getElement();
  }
  
  private static <E> E getElementOrNull(@Nullable Multiset.Entry<E> entry) {
    return (entry == null) ? null : entry.getElement();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\SortedMultisets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */