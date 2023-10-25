package com.ibm.icu.impl;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SoftCache<K, V, D> extends CacheBase<K, V, D> {
  public final V getInstance(K key, D data) {
    SettableSoftReference<V> valueRef = this.map.get(key);
    if (valueRef != null)
      synchronized (valueRef) {
        v = (V)valueRef.ref.get();
        if (v != null)
          return v; 
        v = createInstance(key, data);
        if (v != null)
          valueRef.ref = new SoftReference<V>(v); 
        return v;
      }  
    V v = createInstance(key, data);
    if (v == null)
      return null; 
    valueRef = this.map.putIfAbsent(key, new SettableSoftReference<V>(v));
    if (valueRef == null)
      return v; 
    return valueRef.setIfAbsent(v);
  }
  
  private static final class SettableSoftReference<V> {
    private SoftReference<V> ref;
    
    private SettableSoftReference(V value) {
      this.ref = new SoftReference<V>(value);
    }
    
    private synchronized V setIfAbsent(V value) {
      V oldValue = this.ref.get();
      if (oldValue == null) {
        this.ref = new SoftReference<V>(value);
        return value;
      } 
      return oldValue;
    }
  }
  
  private ConcurrentHashMap<K, SettableSoftReference<V>> map = new ConcurrentHashMap<K, SettableSoftReference<V>>();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\SoftCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */