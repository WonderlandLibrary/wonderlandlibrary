package com.ibm.icu.impl.locale;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LocaleObjectCache<K, V> {
  private ConcurrentHashMap<K, CacheEntry<K, V>> _map;
  
  private ReferenceQueue<V> _queue = new ReferenceQueue<V>();
  
  public LocaleObjectCache() {
    this(16, 0.75F, 16);
  }
  
  public LocaleObjectCache(int initialCapacity, float loadFactor, int concurrencyLevel) {
    this._map = new ConcurrentHashMap<K, CacheEntry<K, V>>(initialCapacity, loadFactor, concurrencyLevel);
  }
  
  public V get(K key) {
    V value = null;
    cleanStaleEntries();
    CacheEntry<K, V> entry = this._map.get(key);
    if (entry != null)
      value = entry.get(); 
    if (value == null) {
      key = normalizeKey(key);
      V newVal = createObject(key);
      if (key == null || newVal == null)
        return null; 
      CacheEntry<K, V> newEntry = new CacheEntry<K, V>(key, newVal, this._queue);
      while (value == null) {
        cleanStaleEntries();
        entry = this._map.putIfAbsent(key, newEntry);
        if (entry == null) {
          value = newVal;
          break;
        } 
        value = entry.get();
      } 
    } 
    return value;
  }
  
  private void cleanStaleEntries() {
    CacheEntry<K, V> entry;
    while ((entry = (CacheEntry)this._queue.poll()) != null)
      this._map.remove(entry.getKey()); 
  }
  
  protected abstract V createObject(K paramK);
  
  protected K normalizeKey(K key) {
    return key;
  }
  
  private static class CacheEntry<K, V> extends SoftReference<V> {
    private K _key;
    
    CacheEntry(K key, V value, ReferenceQueue<V> queue) {
      super(value, queue);
      this._key = key;
    }
    
    K getKey() {
      return this._key;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\locale\LocaleObjectCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */