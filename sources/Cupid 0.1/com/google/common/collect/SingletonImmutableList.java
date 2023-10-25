package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableList<E> extends ImmutableList<E> {
  final transient E element;
  
  SingletonImmutableList(E element) {
    this.element = (E)Preconditions.checkNotNull(element);
  }
  
  public E get(int index) {
    Preconditions.checkElementIndex(index, 1);
    return this.element;
  }
  
  public int indexOf(@Nullable Object object) {
    return this.element.equals(object) ? 0 : -1;
  }
  
  public UnmodifiableIterator<E> iterator() {
    return Iterators.singletonIterator(this.element);
  }
  
  public int lastIndexOf(@Nullable Object object) {
    return indexOf(object);
  }
  
  public int size() {
    return 1;
  }
  
  public ImmutableList<E> subList(int fromIndex, int toIndex) {
    Preconditions.checkPositionIndexes(fromIndex, toIndex, 1);
    return (fromIndex == toIndex) ? ImmutableList.<E>of() : this;
  }
  
  public ImmutableList<E> reverse() {
    return this;
  }
  
  public boolean contains(@Nullable Object object) {
    return this.element.equals(object);
  }
  
  public boolean equals(@Nullable Object object) {
    if (object == this)
      return true; 
    if (object instanceof List) {
      List<?> that = (List)object;
      return (that.size() == 1 && this.element.equals(that.get(0)));
    } 
    return false;
  }
  
  public int hashCode() {
    return 31 + this.element.hashCode();
  }
  
  public String toString() {
    String elementToString = this.element.toString();
    return (new StringBuilder(elementToString.length() + 2)).append('[').append(elementToString).append(']').toString();
  }
  
  public boolean isEmpty() {
    return false;
  }
  
  boolean isPartialView() {
    return false;
  }
  
  int copyIntoArray(Object[] dst, int offset) {
    dst[offset] = this.element;
    return offset + 1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\SingletonImmutableList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */