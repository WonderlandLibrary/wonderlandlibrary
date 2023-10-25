package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConcurrentUtils {
  public static ConcurrentException extractCause(ExecutionException ex) {
    if (ex == null || ex.getCause() == null)
      return null; 
    throwCause(ex);
    return new ConcurrentException(ex.getMessage(), ex.getCause());
  }
  
  public static ConcurrentRuntimeException extractCauseUnchecked(ExecutionException ex) {
    if (ex == null || ex.getCause() == null)
      return null; 
    throwCause(ex);
    return new ConcurrentRuntimeException(ex.getMessage(), ex.getCause());
  }
  
  public static void handleCause(ExecutionException ex) throws ConcurrentException {
    ConcurrentException cex = extractCause(ex);
    if (cex != null)
      throw cex; 
  }
  
  public static void handleCauseUnchecked(ExecutionException ex) {
    ConcurrentRuntimeException crex = extractCauseUnchecked(ex);
    if (crex != null)
      throw crex; 
  }
  
  static Throwable checkedException(Throwable ex) {
    if (ex != null && !(ex instanceof RuntimeException) && !(ex instanceof Error))
      return ex; 
    throw new IllegalArgumentException("Not a checked exception: " + ex);
  }
  
  private static void throwCause(ExecutionException ex) {
    if (ex.getCause() instanceof RuntimeException)
      throw (RuntimeException)ex.getCause(); 
    if (ex.getCause() instanceof Error)
      throw (Error)ex.getCause(); 
  }
  
  public static <T> T initialize(ConcurrentInitializer<T> initializer) throws ConcurrentException {
    return (initializer != null) ? initializer.get() : null;
  }
  
  public static <T> T initializeUnchecked(ConcurrentInitializer<T> initializer) {
    try {
      return initialize(initializer);
    } catch (ConcurrentException cex) {
      throw new ConcurrentRuntimeException(cex.getCause());
    } 
  }
  
  public static <K, V> V putIfAbsent(ConcurrentMap<K, V> map, K key, V value) {
    if (map == null)
      return null; 
    V result = map.putIfAbsent(key, value);
    return (result != null) ? result : value;
  }
  
  public static <K, V> V createIfAbsent(ConcurrentMap<K, V> map, K key, ConcurrentInitializer<V> init) throws ConcurrentException {
    if (map == null || init == null)
      return null; 
    V value = map.get(key);
    if (value == null)
      return putIfAbsent(map, key, init.get()); 
    return value;
  }
  
  public static <K, V> V createIfAbsentUnchecked(ConcurrentMap<K, V> map, K key, ConcurrentInitializer<V> init) {
    try {
      return createIfAbsent(map, key, init);
    } catch (ConcurrentException cex) {
      throw new ConcurrentRuntimeException(cex.getCause());
    } 
  }
  
  public static <T> Future<T> constantFuture(T value) {
    return new ConstantFuture<T>(value);
  }
  
  static final class ConstantFuture<T> implements Future<T> {
    private final T value;
    
    ConstantFuture(T value) {
      this.value = value;
    }
    
    public boolean isDone() {
      return true;
    }
    
    public T get() {
      return this.value;
    }
    
    public T get(long timeout, TimeUnit unit) {
      return this.value;
    }
    
    public boolean isCancelled() {
      return false;
    }
    
    public boolean cancel(boolean mayInterruptIfRunning) {
      return false;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\concurrent\ConcurrentUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */