package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible(emulated = true)
abstract class DescendingMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {
  private transient Comparator<? super E> comparator;
  
  private transient NavigableSet<E> elementSet;
  
  private transient Set<Multiset.Entry<E>> entrySet;
  
  public Comparator<? super E> comparator() {
    Comparator<? super E> result = this.comparator;
    if (result == null)
      return this.comparator = Ordering.from(forwardMultiset().comparator()).<Object>reverse(); 
    return result;
  }
  
  public NavigableSet<E> elementSet() {
    NavigableSet<E> result = this.elementSet;
    if (result == null)
      return this.elementSet = new SortedMultisets.NavigableElementSet<E>(this); 
    return result;
  }
  
  public Multiset.Entry<E> pollFirstEntry() {
    return forwardMultiset().pollLastEntry();
  }
  
  public Multiset.Entry<E> pollLastEntry() {
    return forwardMultiset().pollFirstEntry();
  }
  
  public SortedMultiset<E> headMultiset(E toElement, BoundType boundType) {
    return forwardMultiset().tailMultiset(toElement, boundType).descendingMultiset();
  }
  
  public SortedMultiset<E> subMultiset(E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType) {
    return forwardMultiset().subMultiset(toElement, toBoundType, fromElement, fromBoundType).descendingMultiset();
  }
  
  public SortedMultiset<E> tailMultiset(E fromElement, BoundType boundType) {
    return forwardMultiset().headMultiset(fromElement, boundType).descendingMultiset();
  }
  
  protected Multiset<E> delegate() {
    return forwardMultiset();
  }
  
  public SortedMultiset<E> descendingMultiset() {
    return forwardMultiset();
  }
  
  public Multiset.Entry<E> firstEntry() {
    return forwardMultiset().lastEntry();
  }
  
  public Multiset.Entry<E> lastEntry() {
    return forwardMultiset().firstEntry();
  }
  
  public Set<Multiset.Entry<E>> entrySet() {
    Set<Multiset.Entry<E>> result = this.entrySet;
    return (result == null) ? (this.entrySet = createEntrySet()) : result;
  }
  
  Set<Multiset.Entry<E>> createEntrySet() {
    return new Multisets.EntrySet<E>() {
        Multiset<E> multiset() {
          return DescendingMultiset.this;
        }
        
        public Iterator<Multiset.Entry<E>> iterator() {
          return DescendingMultiset.this.entryIterator();
        }
        
        public int size() {
          return DescendingMultiset.this.forwardMultiset().entrySet().size();
        }
      };
  }
  
  public Iterator<E> iterator() {
    return Multisets.iteratorImpl(this);
  }
  
  public Object[] toArray() {
    return standardToArray();
  }
  
  public <T> T[] toArray(T[] array) {
    return (T[])standardToArray((Object[])array);
  }
  
  public String toString() {
    return entrySet().toString();
  }
  
  abstract SortedMultiset<E> forwardMultiset();
  
  abstract Iterator<Multiset.Entry<E>> entryIterator();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\DescendingMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */