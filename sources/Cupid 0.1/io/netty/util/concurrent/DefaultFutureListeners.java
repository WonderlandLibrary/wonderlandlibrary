package io.netty.util.concurrent;

import java.util.Arrays;

final class DefaultFutureListeners {
  private GenericFutureListener<? extends Future<?>>[] listeners;
  
  private int size;
  
  private int progressiveSize;
  
  DefaultFutureListeners(GenericFutureListener<? extends Future<?>> first, GenericFutureListener<? extends Future<?>> second) {
    this.listeners = (GenericFutureListener<? extends Future<?>>[])new GenericFutureListener[2];
    this.listeners[0] = first;
    this.listeners[1] = second;
    this.size = 2;
    if (first instanceof GenericProgressiveFutureListener)
      this.progressiveSize++; 
    if (second instanceof GenericProgressiveFutureListener)
      this.progressiveSize++; 
  }
  
  public void add(GenericFutureListener<? extends Future<?>> l) {
    GenericFutureListener[] arrayOfGenericFutureListener;
    GenericFutureListener<? extends Future<?>>[] listeners = this.listeners;
    int size = this.size;
    if (size == listeners.length)
      this.listeners = (GenericFutureListener<? extends Future<?>>[])(arrayOfGenericFutureListener = Arrays.copyOf((GenericFutureListener[])listeners, size << 1)); 
    arrayOfGenericFutureListener[size] = l;
    this.size = size + 1;
    if (l instanceof GenericProgressiveFutureListener)
      this.progressiveSize++; 
  }
  
  public void remove(GenericFutureListener<? extends Future<?>> l) {
    GenericFutureListener<? extends Future<?>>[] listeners = this.listeners;
    int size = this.size;
    for (int i = 0; i < size; i++) {
      if (listeners[i] == l) {
        int listenersToMove = size - i - 1;
        if (listenersToMove > 0)
          System.arraycopy(listeners, i + 1, listeners, i, listenersToMove); 
        listeners[--size] = null;
        this.size = size;
        if (l instanceof GenericProgressiveFutureListener)
          this.progressiveSize--; 
        return;
      } 
    } 
  }
  
  public GenericFutureListener<? extends Future<?>>[] listeners() {
    return this.listeners;
  }
  
  public int size() {
    return this.size;
  }
  
  public int progressiveSize() {
    return this.progressiveSize;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\DefaultFutureListeners.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */