package com.ibm.icu.impl;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleCache<K, V> implements ICUCache<K, V> {
  private static final int DEFAULT_CAPACITY = 16;
  
  private Reference<Map<K, V>> cacheRef = null;
  
  private int type = 0;
  
  private int capacity = 16;
  
  public SimpleCache() {}
  
  public SimpleCache(int cacheType) {
    this(cacheType, 16);
  }
  
  public SimpleCache(int cacheType, int initialCapacity) {
    if (cacheType == 1)
      this.type = cacheType; 
    if (initialCapacity > 0)
      this.capacity = initialCapacity; 
  }
  
  public V get(Object key) {
    Reference<Map<K, V>> ref = this.cacheRef;
    if (ref != null) {
      Map<K, V> map = ref.get();
      if (map != null)
        return map.get(key); 
    } 
    return null;
  }
  
  public void put(K key, V value) {
    Reference<Map<K, V>> ref = this.cacheRef;
    Map<K, V> map = null;
    if (ref != null)
      map = ref.get(); 
    if (map == null) {
      map = Collections.synchronizedMap(new HashMap<K, V>(this.capacity));
      if (this.type == 1) {
        ref = new WeakReference<Map<K, V>>(map);
      } else {
        ref = new SoftReference<Map<K, V>>(map);
      } 
      this.cacheRef = ref;
    } 
    map.put(key, value);
  }
  
  public void clear() {
    this.cacheRef = null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\SimpleCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */