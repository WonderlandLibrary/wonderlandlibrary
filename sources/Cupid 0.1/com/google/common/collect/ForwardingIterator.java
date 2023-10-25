package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
public abstract class ForwardingIterator<T> extends ForwardingObject implements Iterator<T> {
  public boolean hasNext() {
    return delegate().hasNext();
  }
  
  public T next() {
    return delegate().next();
  }
  
  public void remove() {
    delegate().remove();
  }
  
  protected abstract Iterator<T> delegate();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ForwardingIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */