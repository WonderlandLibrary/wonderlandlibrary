package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableSet<E> extends ImmutableSet<E> {
  final transient E element;
  
  private transient int cachedHashCode;
  
  SingletonImmutableSet(E element) {
    this.element = (E)Preconditions.checkNotNull(element);
  }
  
  SingletonImmutableSet(E element, int hashCode) {
    this.element = element;
    this.cachedHashCode = hashCode;
  }
  
  public int size() {
    return 1;
  }
  
  public boolean isEmpty() {
    return false;
  }
  
  public boolean contains(Object target) {
    return this.element.equals(target);
  }
  
  public UnmodifiableIterator<E> iterator() {
    return Iterators.singletonIterator(this.element);
  }
  
  boolean isPartialView() {
    return false;
  }
  
  int copyIntoArray(Object[] dst, int offset) {
    dst[offset] = this.element;
    return offset + 1;
  }
  
  public boolean equals(@Nullable Object object) {
    if (object == this)
      return true; 
    if (object instanceof Set) {
      Set<?> that = (Set)object;
      return (that.size() == 1 && this.element.equals(that.iterator().next()));
    } 
    return false;
  }
  
  public final int hashCode() {
    int code = this.cachedHashCode;
    if (code == 0)
      this.cachedHashCode = code = this.element.hashCode(); 
    return code;
  }
  
  boolean isHashCodeFast() {
    return (this.cachedHashCode != 0);
  }
  
  public String toString() {
    String elementToString = this.element.toString();
    return (new StringBuilder(elementToString.length() + 2)).append('[').append(elementToString).append(']').toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\SingletonImmutableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */