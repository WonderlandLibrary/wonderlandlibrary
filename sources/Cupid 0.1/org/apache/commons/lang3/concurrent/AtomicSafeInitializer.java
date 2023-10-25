package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicSafeInitializer<T> implements ConcurrentInitializer<T> {
  private final AtomicReference<AtomicSafeInitializer<T>> factory = new AtomicReference<AtomicSafeInitializer<T>>();
  
  private final AtomicReference<T> reference = new AtomicReference<T>();
  
  public final T get() throws ConcurrentException {
    T result;
    while ((result = this.reference.get()) == null) {
      if (this.factory.compareAndSet(null, this))
        this.reference.set(initialize()); 
    } 
    return result;
  }
  
  protected abstract T initialize() throws ConcurrentException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\concurrent\AtomicSafeInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */