package org.apache.commons.lang3.concurrent;

public abstract class LazyInitializer<T> implements ConcurrentInitializer<T> {
  private volatile T object;
  
  public T get() throws ConcurrentException {
    T result = this.object;
    if (result == null)
      synchronized (this) {
        result = this.object;
        if (result == null)
          this.object = result = initialize(); 
      }  
    return result;
  }
  
  protected abstract T initialize() throws ConcurrentException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\concurrent\LazyInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */