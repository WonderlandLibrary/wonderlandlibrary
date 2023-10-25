package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

@Beta
@GwtCompatible(emulated = true)
public abstract class ForwardingSortedMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {
  public NavigableSet<E> elementSet() {
    return (NavigableSet<E>)super.elementSet();
  }
  
  protected class StandardElementSet extends SortedMultisets.NavigableElementSet<E> {
    public StandardElementSet() {
      super(ForwardingSortedMultiset.this);
    }
  }
  
  public Comparator<? super E> comparator() {
    return delegate().comparator();
  }
  
  public SortedMultiset<E> descendingMultiset() {
    return delegate().descendingMultiset();
  }
  
  protected abstract class StandardDescendingMultiset extends DescendingMultiset<E> {
    SortedMultiset<E> forwardMultiset() {
      return ForwardingSortedMultiset.this;
    }
  }
  
  public Multiset.Entry<E> firstEntry() {
    return delegate().firstEntry();
  }
  
  protected Multiset.Entry<E> standardFirstEntry() {
    Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
    if (!entryIterator.hasNext())
      return null; 
    Multiset.Entry<E> entry = entryIterator.next();
    return Multisets.immutableEntry(entry.getElement(), entry.getCount());
  }
  
  public Multiset.Entry<E> lastEntry() {
    return delegate().lastEntry();
  }
  
  protected Multiset.Entry<E> standardLastEntry() {
    Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
    if (!entryIterator.hasNext())
      return null; 
    Multiset.Entry<E> entry = entryIterator.next();
    return Multisets.immutableEntry(entry.getElement(), entry.getCount());
  }
  
  public Multiset.Entry<E> pollFirstEntry() {
    return delegate().pollFirstEntry();
  }
  
  protected Multiset.Entry<E> standardPollFirstEntry() {
    Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
    if (!entryIterator.hasNext())
      return null; 
    Multiset.Entry<E> entry = entryIterator.next();
    entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
    entryIterator.remove();
    return entry;
  }
  
  public Multiset.Entry<E> pollLastEntry() {
    return delegate().pollLastEntry();
  }
  
  protected Multiset.Entry<E> standardPollLastEntry() {
    Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
    if (!entryIterator.hasNext())
      return null; 
    Multiset.Entry<E> entry = entryIterator.next();
    entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
    entryIterator.remove();
    return entry;
  }
  
  public SortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
    return delegate().headMultiset(upperBound, boundType);
  }
  
  public SortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
    return delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType);
  }
  
  protected SortedMultiset<E> standardSubMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
    return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
  }
  
  public SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
    return delegate().tailMultiset(lowerBound, boundType);
  }
  
  protected abstract SortedMultiset<E> delegate();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ForwardingSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */