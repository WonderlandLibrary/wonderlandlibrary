package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@GwtCompatible
public abstract class ForwardingConcurrentMap<K, V> extends ForwardingMap<K, V> implements ConcurrentMap<K, V> {
  public V putIfAbsent(K key, V value) {
    return delegate().putIfAbsent(key, value);
  }
  
  public boolean remove(Object key, Object value) {
    return delegate().remove(key, value);
  }
  
  public V replace(K key, V value) {
    return delegate().replace(key, value);
  }
  
  public boolean replace(K key, V oldValue, V newValue) {
    return delegate().replace(key, oldValue, newValue);
  }
  
  protected abstract ConcurrentMap<K, V> delegate();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ForwardingConcurrentMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */