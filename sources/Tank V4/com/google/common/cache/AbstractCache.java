package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@Beta
@GwtCompatible
public abstract class AbstractCache implements Cache {
   protected AbstractCache() {
   }

   public Object get(Object var1, Callable var2) throws ExecutionException {
      throw new UnsupportedOperationException();
   }

   public ImmutableMap getAllPresent(Iterable var1) {
      LinkedHashMap var2 = Maps.newLinkedHashMap();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (!var2.containsKey(var4)) {
            var2.put(var4, this.getIfPresent(var4));
         }
      }

      return ImmutableMap.copyOf(var2);
   }

   public void put(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public void cleanUp() {
   }

   public long size() {
      throw new UnsupportedOperationException();
   }

   public void invalidate(Object var1) {
      throw new UnsupportedOperationException();
   }

   public void invalidateAll(Iterable var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         this.invalidate(var3);
      }

   }

   public void invalidateAll() {
      throw new UnsupportedOperationException();
   }

   public CacheStats stats() {
      throw new UnsupportedOperationException();
   }

   public ConcurrentMap asMap() {
      throw new UnsupportedOperationException();
   }

   @Beta
   public static final class SimpleStatsCounter implements AbstractCache.StatsCounter {
      private final LongAddable hitCount = LongAddables.create();
      private final LongAddable missCount = LongAddables.create();
      private final LongAddable loadSuccessCount = LongAddables.create();
      private final LongAddable loadExceptionCount = LongAddables.create();
      private final LongAddable totalLoadTime = LongAddables.create();
      private final LongAddable evictionCount = LongAddables.create();

      public void recordHits(int var1) {
         this.hitCount.add((long)var1);
      }

      public void recordMisses(int var1) {
         this.missCount.add((long)var1);
      }

      public void recordLoadSuccess(long var1) {
         this.loadSuccessCount.increment();
         this.totalLoadTime.add(var1);
      }

      public void recordLoadException(long var1) {
         this.loadExceptionCount.increment();
         this.totalLoadTime.add(var1);
      }

      public void recordEviction() {
         this.evictionCount.increment();
      }

      public CacheStats snapshot() {
         return new CacheStats(this.hitCount.sum(), this.missCount.sum(), this.loadSuccessCount.sum(), this.loadExceptionCount.sum(), this.totalLoadTime.sum(), this.evictionCount.sum());
      }

      public void incrementBy(AbstractCache.StatsCounter var1) {
         CacheStats var2 = var1.snapshot();
         this.hitCount.add(var2.hitCount());
         this.missCount.add(var2.missCount());
         this.loadSuccessCount.add(var2.loadSuccessCount());
         this.loadExceptionCount.add(var2.loadExceptionCount());
         this.totalLoadTime.add(var2.totalLoadTime());
         this.evictionCount.add(var2.evictionCount());
      }
   }

   @Beta
   public interface StatsCounter {
      void recordHits(int var1);

      void recordMisses(int var1);

      void recordLoadSuccess(long var1);

      void recordLoadException(long var1);

      void recordEviction();

      CacheStats snapshot();
   }
}
