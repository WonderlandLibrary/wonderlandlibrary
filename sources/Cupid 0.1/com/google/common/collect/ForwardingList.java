package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingList<E> extends ForwardingCollection<E> implements List<E> {
  public void add(int index, E element) {
    delegate().add(index, element);
  }
  
  public boolean addAll(int index, Collection<? extends E> elements) {
    return delegate().addAll(index, elements);
  }
  
  public E get(int index) {
    return delegate().get(index);
  }
  
  public int indexOf(Object element) {
    return delegate().indexOf(element);
  }
  
  public int lastIndexOf(Object element) {
    return delegate().lastIndexOf(element);
  }
  
  public ListIterator<E> listIterator() {
    return delegate().listIterator();
  }
  
  public ListIterator<E> listIterator(int index) {
    return delegate().listIterator(index);
  }
  
  public E remove(int index) {
    return delegate().remove(index);
  }
  
  public E set(int index, E element) {
    return delegate().set(index, element);
  }
  
  public List<E> subList(int fromIndex, int toIndex) {
    return delegate().subList(fromIndex, toIndex);
  }
  
  public boolean equals(@Nullable Object object) {
    return (object == this || delegate().equals(object));
  }
  
  public int hashCode() {
    return delegate().hashCode();
  }
  
  protected boolean standardAdd(E element) {
    add(size(), element);
    return true;
  }
  
  protected boolean standardAddAll(int index, Iterable<? extends E> elements) {
    return Lists.addAllImpl(this, index, elements);
  }
  
  protected int standardIndexOf(@Nullable Object element) {
    return Lists.indexOfImpl(this, element);
  }
  
  protected int standardLastIndexOf(@Nullable Object element) {
    return Lists.lastIndexOfImpl(this, element);
  }
  
  protected Iterator<E> standardIterator() {
    return listIterator();
  }
  
  protected ListIterator<E> standardListIterator() {
    return listIterator(0);
  }
  
  @Beta
  protected ListIterator<E> standardListIterator(int start) {
    return Lists.listIteratorImpl(this, start);
  }
  
  @Beta
  protected List<E> standardSubList(int fromIndex, int toIndex) {
    return Lists.subListImpl(this, fromIndex, toIndex);
  }
  
  @Beta
  protected boolean standardEquals(@Nullable Object object) {
    return Lists.equalsImpl(this, object);
  }
  
  @Beta
  protected int standardHashCode() {
    return Lists.hashCodeImpl(this);
  }
  
  protected abstract List<E> delegate();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ForwardingList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */