package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicInitializer<T> implements ConcurrentInitializer<T> {
  private final AtomicReference<T> reference = new AtomicReference<T>();
  
  public T get() throws ConcurrentException {
    T result = this.reference.get();
    if (result == null) {
      result = initialize();
      if (!this.reference.compareAndSet(null, result))
        result = this.reference.get(); 
    } 
    return result;
  }
  
  protected abstract T initialize() throws ConcurrentException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\concurrent\AtomicInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */