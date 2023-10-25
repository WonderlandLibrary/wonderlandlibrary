package io.netty.channel.group;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class CombinedIterator<E> implements Iterator<E> {
  private final Iterator<E> i1;
  
  private final Iterator<E> i2;
  
  private Iterator<E> currentIterator;
  
  CombinedIterator(Iterator<E> i1, Iterator<E> i2) {
    if (i1 == null)
      throw new NullPointerException("i1"); 
    if (i2 == null)
      throw new NullPointerException("i2"); 
    this.i1 = i1;
    this.i2 = i2;
    this.currentIterator = i1;
  }
  
  public boolean hasNext() {
    while (true) {
      if (this.currentIterator.hasNext())
        return true; 
      if (this.currentIterator == this.i1) {
        this.currentIterator = this.i2;
        continue;
      } 
      break;
    } 
    return false;
  }
  
  public E next() {
    while (true) {
      try {
        return this.currentIterator.next();
      } catch (NoSuchElementException e) {
        if (this.currentIterator == this.i1) {
          this.currentIterator = this.i2;
          continue;
        } 
        break;
      } 
    } 
    throw e;
  }
  
  public void remove() {
    this.currentIterator.remove();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\group\CombinedIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */