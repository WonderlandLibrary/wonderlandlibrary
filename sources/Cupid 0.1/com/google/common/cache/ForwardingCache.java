package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingObject;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;

@Beta
public abstract class ForwardingCache<K, V> extends ForwardingObject implements Cache<K, V> {
  @Nullable
  public V getIfPresent(Object key) {
    return delegate().getIfPresent(key);
  }
  
  public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
    return delegate().get(key, valueLoader);
  }
  
  public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
    return delegate().getAllPresent(keys);
  }
  
  public void put(K key, V value) {
    delegate().put(key, value);
  }
  
  public void putAll(Map<? extends K, ? extends V> m) {
    delegate().putAll(m);
  }
  
  public void invalidate(Object key) {
    delegate().invalidate(key);
  }
  
  public void invalidateAll(Iterable<?> keys) {
    delegate().invalidateAll(keys);
  }
  
  public void invalidateAll() {
    delegate().invalidateAll();
  }
  
  public long size() {
    return delegate().size();
  }
  
  public CacheStats stats() {
    return delegate().stats();
  }
  
  public ConcurrentMap<K, V> asMap() {
    return delegate().asMap();
  }
  
  public void cleanUp() {
    delegate().cleanUp();
  }
  
  protected abstract Cache<K, V> delegate();
  
  @Beta
  public static abstract class SimpleForwardingCache<K, V> extends ForwardingCache<K, V> {
    private final Cache<K, V> delegate;
    
    protected SimpleForwardingCache(Cache<K, V> delegate) {
      this.delegate = (Cache<K, V>)Preconditions.checkNotNull(delegate);
    }
    
    protected final Cache<K, V> delegate() {
      return this.delegate;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\cache\ForwardingCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */