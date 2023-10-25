package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@GwtCompatible
public final class AtomicLongMap<K> {
  private final ConcurrentHashMap<K, AtomicLong> map;
  
  private transient Map<K, Long> asMap;
  
  private AtomicLongMap(ConcurrentHashMap<K, AtomicLong> map) {
    this.map = (ConcurrentHashMap<K, AtomicLong>)Preconditions.checkNotNull(map);
  }
  
  public static <K> AtomicLongMap<K> create() {
    return new AtomicLongMap<K>(new ConcurrentHashMap<K, AtomicLong>());
  }
  
  public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> m) {
    AtomicLongMap<K> result = create();
    result.putAll(m);
    return result;
  }
  
  public long get(K key) {
    AtomicLong atomic = this.map.get(key);
    return (atomic == null) ? 0L : atomic.get();
  }
  
  public long incrementAndGet(K key) {
    return addAndGet(key, 1L);
  }
  
  public long decrementAndGet(K key) {
    return addAndGet(key, -1L);
  }
  
  public long addAndGet(K key, long delta) {
    label17: while (true) {
      AtomicLong atomic = this.map.get(key);
      if (atomic == null) {
        atomic = this.map.putIfAbsent(key, new AtomicLong(delta));
        if (atomic == null)
          return delta; 
      } 
      while (true) {
        long oldValue = atomic.get();
        if (oldValue == 0L) {
          if (this.map.replace(key, atomic, new AtomicLong(delta)))
            return delta; 
          continue label17;
        } 
        long newValue = oldValue + delta;
        if (atomic.compareAndSet(oldValue, newValue))
          return newValue; 
      } 
      break;
    } 
  }
  
  public long getAndIncrement(K key) {
    return getAndAdd(key, 1L);
  }
  
  public long getAndDecrement(K key) {
    return getAndAdd(key, -1L);
  }
  
  public long getAndAdd(K key, long delta) {
    label17: while (true) {
      AtomicLong atomic = this.map.get(key);
      if (atomic == null) {
        atomic = this.map.putIfAbsent(key, new AtomicLong(delta));
        if (atomic == null)
          return 0L; 
      } 
      while (true) {
        long oldValue = atomic.get();
        if (oldValue == 0L) {
          if (this.map.replace(key, atomic, new AtomicLong(delta)))
            return 0L; 
          continue label17;
        } 
        long newValue = oldValue + delta;
        if (atomic.compareAndSet(oldValue, newValue))
          return oldValue; 
      } 
      break;
    } 
  }
  
  public long put(K key, long newValue) {
    label16: while (true) {
      AtomicLong atomic = this.map.get(key);
      if (atomic == null) {
        atomic = this.map.putIfAbsent(key, new AtomicLong(newValue));
        if (atomic == null)
          return 0L; 
      } 
      while (true) {
        long oldValue = atomic.get();
        if (oldValue == 0L) {
          if (this.map.replace(key, atomic, new AtomicLong(newValue)))
            return 0L; 
          continue label16;
        } 
        if (atomic.compareAndSet(oldValue, newValue))
          return oldValue; 
      } 
      break;
    } 
  }
  
  public void putAll(Map<? extends K, ? extends Long> m) {
    for (Map.Entry<? extends K, ? extends Long> entry : m.entrySet())
      put(entry.getKey(), ((Long)entry.getValue()).longValue()); 
  }
  
  public long remove(K key) {
    long oldValue;
    AtomicLong atomic = this.map.get(key);
    if (atomic == null)
      return 0L; 
    do {
      oldValue = atomic.get();
    } while (oldValue != 0L && !atomic.compareAndSet(oldValue, 0L));
    this.map.remove(key, atomic);
    return oldValue;
  }
  
  public void removeAllZeros() {
    for (K key : this.map.keySet()) {
      AtomicLong atomic = this.map.get(key);
      if (atomic != null && atomic.get() == 0L)
        this.map.remove(key, atomic); 
    } 
  }
  
  public long sum() {
    long sum = 0L;
    for (AtomicLong value : this.map.values())
      sum += value.get(); 
    return sum;
  }
  
  public Map<K, Long> asMap() {
    Map<K, Long> result = this.asMap;
    return (result == null) ? (this.asMap = createAsMap()) : result;
  }
  
  private Map<K, Long> createAsMap() {
    return Collections.unmodifiableMap(Maps.transformValues(this.map, new Function<AtomicLong, Long>() {
            public Long apply(AtomicLong atomic) {
              return Long.valueOf(atomic.get());
            }
          }));
  }
  
  public boolean containsKey(Object key) {
    return this.map.containsKey(key);
  }
  
  public int size() {
    return this.map.size();
  }
  
  public boolean isEmpty() {
    return this.map.isEmpty();
  }
  
  public void clear() {
    this.map.clear();
  }
  
  public String toString() {
    return this.map.toString();
  }
  
  long putIfAbsent(K key, long newValue) {
    long oldValue;
    while (true) {
      AtomicLong atomic = this.map.get(key);
      if (atomic == null) {
        atomic = this.map.putIfAbsent(key, new AtomicLong(newValue));
        if (atomic == null)
          return 0L; 
      } 
      oldValue = atomic.get();
      if (oldValue == 0L) {
        if (this.map.replace(key, atomic, new AtomicLong(newValue)))
          return 0L; 
        continue;
      } 
      break;
    } 
    return oldValue;
  }
  
  boolean replace(K key, long expectedOldValue, long newValue) {
    if (expectedOldValue == 0L)
      return (putIfAbsent(key, newValue) == 0L); 
    AtomicLong atomic = this.map.get(key);
    return (atomic == null) ? false : atomic.compareAndSet(expectedOldValue, newValue);
  }
  
  boolean remove(K key, long value) {
    AtomicLong atomic = this.map.get(key);
    if (atomic == null)
      return false; 
    long oldValue = atomic.get();
    if (oldValue != value)
      return false; 
    if (oldValue == 0L || atomic.compareAndSet(oldValue, 0L)) {
      this.map.remove(key, atomic);
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\AtomicLongMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */