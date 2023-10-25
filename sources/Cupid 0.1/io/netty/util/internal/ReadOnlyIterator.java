package io.netty.util.internal;

import java.util.Iterator;

public final class ReadOnlyIterator<T> implements Iterator<T> {
  private final Iterator<? extends T> iterator;
  
  public ReadOnlyIterator(Iterator<? extends T> iterator) {
    if (iterator == null)
      throw new NullPointerException("iterator"); 
    this.iterator = iterator;
  }
  
  public boolean hasNext() {
    return this.iterator.hasNext();
  }
  
  public T next() {
    return this.iterator.next();
  }
  
  public void remove() {
    throw new UnsupportedOperationException("read-only");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\ReadOnlyIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */