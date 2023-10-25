package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible
final class Constraints {
  public static <E> Collection<E> constrainedCollection(Collection<E> collection, Constraint<? super E> constraint) {
    return new ConstrainedCollection<E>(collection, constraint);
  }
  
  static class ConstrainedCollection<E> extends ForwardingCollection<E> {
    private final Collection<E> delegate;
    
    private final Constraint<? super E> constraint;
    
    public ConstrainedCollection(Collection<E> delegate, Constraint<? super E> constraint) {
      this.delegate = (Collection<E>)Preconditions.checkNotNull(delegate);
      this.constraint = (Constraint<? super E>)Preconditions.checkNotNull(constraint);
    }
    
    protected Collection<E> delegate() {
      return this.delegate;
    }
    
    public boolean add(E element) {
      this.constraint.checkElement(element);
      return this.delegate.add(element);
    }
    
    public boolean addAll(Collection<? extends E> elements) {
      return this.delegate.addAll(Constraints.checkElements((Collection)elements, this.constraint));
    }
  }
  
  public static <E> Set<E> constrainedSet(Set<E> set, Constraint<? super E> constraint) {
    return new ConstrainedSet<E>(set, constraint);
  }
  
  static class ConstrainedSet<E> extends ForwardingSet<E> {
    private final Set<E> delegate;
    
    private final Constraint<? super E> constraint;
    
    public ConstrainedSet(Set<E> delegate, Constraint<? super E> constraint) {
      this.delegate = (Set<E>)Preconditions.checkNotNull(delegate);
      this.constraint = (Constraint<? super E>)Preconditions.checkNotNull(constraint);
    }
    
    protected Set<E> delegate() {
      return this.delegate;
    }
    
    public boolean add(E element) {
      this.constraint.checkElement(element);
      return this.delegate.add(element);
    }
    
    public boolean addAll(Collection<? extends E> elements) {
      return this.delegate.addAll(Constraints.checkElements((Collection)elements, this.constraint));
    }
  }
  
  public static <E> SortedSet<E> constrainedSortedSet(SortedSet<E> sortedSet, Constraint<? super E> constraint) {
    return new ConstrainedSortedSet<E>(sortedSet, constraint);
  }
  
  private static class ConstrainedSortedSet<E> extends ForwardingSortedSet<E> {
    final SortedSet<E> delegate;
    
    final Constraint<? super E> constraint;
    
    ConstrainedSortedSet(SortedSet<E> delegate, Constraint<? super E> constraint) {
      this.delegate = (SortedSet<E>)Preconditions.checkNotNull(delegate);
      this.constraint = (Constraint<? super E>)Preconditions.checkNotNull(constraint);
    }
    
    protected SortedSet<E> delegate() {
      return this.delegate;
    }
    
    public SortedSet<E> headSet(E toElement) {
      return Constraints.constrainedSortedSet(this.delegate.headSet(toElement), this.constraint);
    }
    
    public SortedSet<E> subSet(E fromElement, E toElement) {
      return Constraints.constrainedSortedSet(this.delegate.subSet(fromElement, toElement), this.constraint);
    }
    
    public SortedSet<E> tailSet(E fromElement) {
      return Constraints.constrainedSortedSet(this.delegate.tailSet(fromElement), this.constraint);
    }
    
    public boolean add(E element) {
      this.constraint.checkElement(element);
      return this.delegate.add(element);
    }
    
    public boolean addAll(Collection<? extends E> elements) {
      return this.delegate.addAll(Constraints.checkElements((Collection)elements, this.constraint));
    }
  }
  
  public static <E> List<E> constrainedList(List<E> list, Constraint<? super E> constraint) {
    return (list instanceof RandomAccess) ? new ConstrainedRandomAccessList<E>(list, constraint) : new ConstrainedList<E>(list, constraint);
  }
  
  @GwtCompatible
  private static class ConstrainedList<E> extends ForwardingList<E> {
    final List<E> delegate;
    
    final Constraint<? super E> constraint;
    
    ConstrainedList(List<E> delegate, Constraint<? super E> constraint) {
      this.delegate = (List<E>)Preconditions.checkNotNull(delegate);
      this.constraint = (Constraint<? super E>)Preconditions.checkNotNull(constraint);
    }
    
    protected List<E> delegate() {
      return this.delegate;
    }
    
    public boolean add(E element) {
      this.constraint.checkElement(element);
      return this.delegate.add(element);
    }
    
    public void add(int index, E element) {
      this.constraint.checkElement(element);
      this.delegate.add(index, element);
    }
    
    public boolean addAll(Collection<? extends E> elements) {
      return this.delegate.addAll(Constraints.checkElements((Collection)elements, this.constraint));
    }
    
    public boolean addAll(int index, Collection<? extends E> elements) {
      return this.delegate.addAll(index, Constraints.checkElements((Collection)elements, this.constraint));
    }
    
    public ListIterator<E> listIterator() {
      return Constraints.constrainedListIterator(this.delegate.listIterator(), this.constraint);
    }
    
    public ListIterator<E> listIterator(int index) {
      return Constraints.constrainedListIterator(this.delegate.listIterator(index), this.constraint);
    }
    
    public E set(int index, E element) {
      this.constraint.checkElement(element);
      return this.delegate.set(index, element);
    }
    
    public List<E> subList(int fromIndex, int toIndex) {
      return Constraints.constrainedList(this.delegate.subList(fromIndex, toIndex), this.constraint);
    }
  }
  
  static class ConstrainedRandomAccessList<E> extends ConstrainedList<E> implements RandomAccess {
    ConstrainedRandomAccessList(List<E> delegate, Constraint<? super E> constraint) {
      super(delegate, constraint);
    }
  }
  
  private static <E> ListIterator<E> constrainedListIterator(ListIterator<E> listIterator, Constraint<? super E> constraint) {
    return new ConstrainedListIterator<E>(listIterator, constraint);
  }
  
  static class ConstrainedListIterator<E> extends ForwardingListIterator<E> {
    private final ListIterator<E> delegate;
    
    private final Constraint<? super E> constraint;
    
    public ConstrainedListIterator(ListIterator<E> delegate, Constraint<? super E> constraint) {
      this.delegate = delegate;
      this.constraint = constraint;
    }
    
    protected ListIterator<E> delegate() {
      return this.delegate;
    }
    
    public void add(E element) {
      this.constraint.checkElement(element);
      this.delegate.add(element);
    }
    
    public void set(E element) {
      this.constraint.checkElement(element);
      this.delegate.set(element);
    }
  }
  
  static <E> Collection<E> constrainedTypePreservingCollection(Collection<E> collection, Constraint<E> constraint) {
    if (collection instanceof SortedSet)
      return constrainedSortedSet((SortedSet<E>)collection, constraint); 
    if (collection instanceof Set)
      return constrainedSet((Set<E>)collection, constraint); 
    if (collection instanceof List)
      return constrainedList((List<E>)collection, constraint); 
    return constrainedCollection(collection, constraint);
  }
  
  private static <E> Collection<E> checkElements(Collection<E> elements, Constraint<? super E> constraint) {
    Collection<E> copy = Lists.newArrayList(elements);
    for (E element : copy)
      constraint.checkElement(element); 
    return copy;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Constraints.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */