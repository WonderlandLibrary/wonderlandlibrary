package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface Multiset<E> extends Collection<E> {
  int count(@Nullable Object paramObject);
  
  int add(@Nullable E paramE, int paramInt);
  
  int remove(@Nullable Object paramObject, int paramInt);
  
  int setCount(E paramE, int paramInt);
  
  boolean setCount(E paramE, int paramInt1, int paramInt2);
  
  Set<E> elementSet();
  
  Set<Entry<E>> entrySet();
  
  boolean equals(@Nullable Object paramObject);
  
  int hashCode();
  
  String toString();
  
  Iterator<E> iterator();
  
  boolean contains(@Nullable Object paramObject);
  
  boolean containsAll(Collection<?> paramCollection);
  
  boolean add(E paramE);
  
  boolean remove(@Nullable Object paramObject);
  
  boolean removeAll(Collection<?> paramCollection);
  
  boolean retainAll(Collection<?> paramCollection);
  
  public static interface Entry<E> {
    E getElement();
    
    int getCount();
    
    boolean equals(Object param1Object);
    
    int hashCode();
    
    String toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Multiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */