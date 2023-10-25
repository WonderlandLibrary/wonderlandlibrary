package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@Beta
@GwtCompatible
public abstract class AbstractCache<K, V> implements Cache<K, V> {
  public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
    throw new UnsupportedOperationException();
  }
  
  public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
    Map<K, V> result = Maps.newLinkedHashMap();
    for (Object key : keys) {
      if (!result.containsKey(key)) {
        K castKey = (K)key;
        result.put(castKey, getIfPresent(key));
      } 
    } 
    return ImmutableMap.copyOf(result);
  }
  
  public void put(K key, V value) {
    throw new UnsupportedOperationException();
  }
  
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Map.Entry<? extends K, ? extends V> entry : m.entrySet())
      put(entry.getKey(), entry.getValue()); 
  }
  
  public void cleanUp() {}
  
  public long size() {
    throw new UnsupportedOperationException();
  }
  
  public void invalidate(Object key) {
    throw new UnsupportedOperationException();
  }
  
  public void invalidateAll(Iterable<?> keys) {
    for (Object key : keys)
      invalidate(key); 
  }
  
  public void invalidateAll() {
    throw new UnsupportedOperationException();
  }
  
  public CacheStats stats() {
    throw new UnsupportedOperationException();
  }
  
  public ConcurrentMap<K, V> asMap() {
    throw new UnsupportedOperationException();
  }
  
  @Beta
  public static final class SimpleStatsCounter implements StatsCounter {
    private final LongAddable hitCount = LongAddables.create();
    
    private final LongAddable missCount = LongAddables.create();
    
    private final LongAddable loadSuccessCount = LongAddables.create();
    
    private final LongAddable loadExceptionCount = LongAddables.create();
    
    private final LongAddable totalLoadTime = LongAddables.create();
    
    private final LongAddable evictionCount = LongAddables.create();
    
    public void recordHits(int count) {
      this.hitCount.add(count);
    }
    
    public void recordMisses(int count) {
      this.missCount.add(count);
    }
    
    public void recordLoadSuccess(long loadTime) {
      this.loadSuccessCount.increment();
      this.totalLoadTime.add(loadTime);
    }
    
    public void recordLoadException(long loadTime) {
      this.loadExceptionCount.increment();
      this.totalLoadTime.add(loadTime);
    }
    
    public void recordEviction() {
      this.evictionCount.increment();
    }
    
    public CacheStats snapshot() {
      return new CacheStats(this.hitCount.sum(), this.missCount.sum(), this.loadSuccessCount.sum(), this.loadExceptionCount.sum(), this.totalLoadTime.sum(), this.evictionCount.sum());
    }
    
    public void incrementBy(AbstractCache.StatsCounter other) {
      CacheStats otherStats = other.snapshot();
      this.hitCount.add(otherStats.hitCount());
      this.missCount.add(otherStats.missCount());
      this.loadSuccessCount.add(otherStats.loadSuccessCount());
      this.loadExceptionCount.add(otherStats.loadExceptionCount());
      this.totalLoadTime.add(otherStats.totalLoadTime());
      this.evictionCount.add(otherStats.evictionCount());
    }
  }
  
  @Beta
  public static interface StatsCounter {
    void recordHits(int param1Int);
    
    void recordMisses(int param1Int);
    
    void recordLoadSuccess(long param1Long);
    
    void recordLoadException(long param1Long);
    
    void recordEviction();
    
    CacheStats snapshot();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\cache\AbstractCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */