package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

@GwtCompatible(
   emulated = true
)
class LocalCache extends AbstractMap implements ConcurrentMap {
   static final int MAXIMUM_CAPACITY = 1073741824;
   static final int MAX_SEGMENTS = 65536;
   static final int CONTAINS_VALUE_RETRIES = 3;
   static final int DRAIN_THRESHOLD = 63;
   static final int DRAIN_MAX = 16;
   static final Logger logger = Logger.getLogger(LocalCache.class.getName());
   static final ListeningExecutorService sameThreadExecutor = MoreExecutors.sameThreadExecutor();
   final int segmentMask;
   final int segmentShift;
   final LocalCache.Segment[] segments;
   final int concurrencyLevel;
   final Equivalence keyEquivalence;
   final Equivalence valueEquivalence;
   final LocalCache.Strength keyStrength;
   final LocalCache.Strength valueStrength;
   final long maxWeight;
   final Weigher weigher;
   final long expireAfterAccessNanos;
   final long expireAfterWriteNanos;
   final long refreshNanos;
   final Queue removalNotificationQueue;
   final RemovalListener removalListener;
   final Ticker ticker;
   final LocalCache.EntryFactory entryFactory;
   final AbstractCache.StatsCounter globalStatsCounter;
   @Nullable
   final CacheLoader defaultLoader;
   static final LocalCache.ValueReference UNSET = new LocalCache.ValueReference() {
      public Object get() {
         return null;
      }

      public int getWeight() {
         return 0;
      }

      public LocalCache.ReferenceEntry getEntry() {
         return null;
      }

      public LocalCache.ValueReference copyFor(ReferenceQueue var1, @Nullable Object var2, LocalCache.ReferenceEntry var3) {
         return this;
      }

      public boolean isLoading() {
         return false;
      }

      public boolean isActive() {
         return false;
      }

      public Object waitForValue() {
         return null;
      }

      public void notifyNewValue(Object var1) {
      }
   };
   static final Queue DISCARDING_QUEUE = new AbstractQueue() {
      public boolean offer(Object var1) {
         return true;
      }

      public Object peek() {
         return null;
      }

      public Object poll() {
         return null;
      }

      public int size() {
         return 0;
      }

      public Iterator iterator() {
         return Iterators.emptyIterator();
      }
   };
   Set keySet;
   Collection values;
   Set entrySet;

   LocalCache(CacheBuilder param1, @Nullable CacheLoader param2) {
      // $FF: Couldn't be decompiled
   }

   boolean expires() {
      return !(this > 0) || this > 0;
   }

   boolean usesWriteQueue() {
      return this.expiresAfterWrite();
   }

   boolean recordsAccess() {
      return this.expiresAfterAccess();
   }

   boolean recordsTime() {
      return !(this > 0) || this.recordsAccess();
   }

   boolean usesWriteEntries() {
      return this.usesWriteQueue() || this > 0;
   }

   boolean usesAccessEntries() {
      return !(this > 0) || this.recordsAccess();
   }

   boolean usesKeyReferences() {
      return this.keyStrength != LocalCache.Strength.STRONG;
   }

   boolean usesValueReferences() {
      return this.valueStrength != LocalCache.Strength.STRONG;
   }

   static LocalCache.ValueReference unset() {
      return UNSET;
   }

   static LocalCache.ReferenceEntry nullEntry() {
      return LocalCache.NullEntry.INSTANCE;
   }

   static Queue discardingQueue() {
      return DISCARDING_QUEUE;
   }

   static int rehash(int var0) {
      var0 += var0 << 15 ^ -12931;
      var0 ^= var0 >>> 10;
      var0 += var0 << 3;
      var0 ^= var0 >>> 6;
      var0 += (var0 << 2) + (var0 << 14);
      return var0 ^ var0 >>> 16;
   }

   @GuardedBy("Segment.this")
   @VisibleForTesting
   LocalCache.ReferenceEntry newEntry(Object var1, int var2, @Nullable LocalCache.ReferenceEntry var3) {
      return this.segmentFor(var2).newEntry(var1, var2, var3);
   }

   @GuardedBy("Segment.this")
   @VisibleForTesting
   LocalCache.ReferenceEntry copyEntry(LocalCache.ReferenceEntry var1, LocalCache.ReferenceEntry var2) {
      int var3 = var1.getHash();
      return this.segmentFor(var3).copyEntry(var1, var2);
   }

   @GuardedBy("Segment.this")
   @VisibleForTesting
   LocalCache.ValueReference newValueReference(LocalCache.ReferenceEntry var1, Object var2, int var3) {
      int var4 = var1.getHash();
      return this.valueStrength.referenceValue(this.segmentFor(var4), var1, Preconditions.checkNotNull(var2), var3);
   }

   int hash(@Nullable Object var1) {
      int var2 = this.keyEquivalence.hash(var1);
      return rehash(var2);
   }

   void reclaimValue(LocalCache.ValueReference var1) {
      LocalCache.ReferenceEntry var2 = var1.getEntry();
      int var3 = var2.getHash();
      this.segmentFor(var3).reclaimValue(var2.getKey(), var3, var1);
   }

   void reclaimKey(LocalCache.ReferenceEntry var1) {
      int var2 = var1.getHash();
      this.segmentFor(var2).reclaimKey(var1, var2);
   }

   @VisibleForTesting
   boolean isLive(LocalCache.ReferenceEntry var1, long var2) {
      return this.segmentFor(var1.getHash()).getLiveValue(var1, var2) != null;
   }

   LocalCache.Segment segmentFor(int var1) {
      return this.segments[var1 >>> this.segmentShift & this.segmentMask];
   }

   LocalCache.Segment createSegment(int var1, long var2, AbstractCache.StatsCounter var4) {
      return new LocalCache.Segment(this, var1, var2, var4);
   }

   @Nullable
   Object getLiveValue(LocalCache.ReferenceEntry var1, long var2) {
      if (var1.getKey() == null) {
         return null;
      } else {
         Object var4 = var1.getValueReference().get();
         if (var4 == null) {
            return null;
         } else {
            return var2 != false ? null : var4;
         }
      }
   }

   @GuardedBy("Segment.this")
   static void connectAccessOrder(LocalCache.ReferenceEntry var0, LocalCache.ReferenceEntry var1) {
      var0.setNextInAccessQueue(var1);
      var1.setPreviousInAccessQueue(var0);
   }

   @GuardedBy("Segment.this")
   static void nullifyAccessOrder(LocalCache.ReferenceEntry var0) {
      LocalCache.ReferenceEntry var1 = nullEntry();
      var0.setNextInAccessQueue(var1);
      var0.setPreviousInAccessQueue(var1);
   }

   @GuardedBy("Segment.this")
   static void connectWriteOrder(LocalCache.ReferenceEntry var0, LocalCache.ReferenceEntry var1) {
      var0.setNextInWriteQueue(var1);
      var1.setPreviousInWriteQueue(var0);
   }

   @GuardedBy("Segment.this")
   static void nullifyWriteOrder(LocalCache.ReferenceEntry var0) {
      LocalCache.ReferenceEntry var1 = nullEntry();
      var0.setNextInWriteQueue(var1);
      var0.setPreviousInWriteQueue(var1);
   }

   void processPendingNotifications() {
      RemovalNotification var1;
      while((var1 = (RemovalNotification)this.removalNotificationQueue.poll()) != null) {
         try {
            this.removalListener.onRemoval(var1);
         } catch (Throwable var3) {
            logger.log(Level.WARNING, "Exception thrown by removal listener", var3);
         }
      }

   }

   final LocalCache.Segment[] newSegmentArray(int var1) {
      return new LocalCache.Segment[var1];
   }

   public void cleanUp() {
      LocalCache.Segment[] var1 = this.segments;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         LocalCache.Segment var4 = var1[var3];
         var4.cleanUp();
      }

   }

   public boolean isEmpty() {
      long var1 = 0L;
      LocalCache.Segment[] var3 = this.segments;

      int var4;
      for(var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].count != 0) {
            return false;
         }

         var1 += (long)var3[var4].modCount;
      }

      if (var1 != 0L) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].count != 0) {
               return false;
            }

            var1 -= (long)var3[var4].modCount;
         }

         if (var1 != 0L) {
            return false;
         }
      }

      return true;
   }

   long longSize() {
      LocalCache.Segment[] var1 = this.segments;
      long var2 = 0L;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var2 += (long)var1[var4].count;
      }

      return var2;
   }

   public int size() {
      return Ints.saturatedCast(this.longSize());
   }

   @Nullable
   public Object get(@Nullable Object var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).get(var1, var2);
      }
   }

   @Nullable
   public Object getIfPresent(Object var1) {
      int var2 = this.hash(Preconditions.checkNotNull(var1));
      Object var3 = this.segmentFor(var2).get(var1, var2);
      if (var3 == null) {
         this.globalStatsCounter.recordMisses(1);
      } else {
         this.globalStatsCounter.recordHits(1);
      }

      return var3;
   }

   Object get(Object var1, CacheLoader var2) throws ExecutionException {
      int var3 = this.hash(Preconditions.checkNotNull(var1));
      return this.segmentFor(var3).get(var1, var3, var2);
   }

   Object getOrLoad(Object var1) throws ExecutionException {
      return this.get(var1, this.defaultLoader);
   }

   ImmutableMap getAllPresent(Iterable var1) {
      int var2 = 0;
      int var3 = 0;
      LinkedHashMap var4 = Maps.newLinkedHashMap();
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         Object var7 = this.get(var6);
         if (var7 == null) {
            ++var3;
         } else {
            var4.put(var6, var7);
            ++var2;
         }
      }

      this.globalStatsCounter.recordHits(var2);
      this.globalStatsCounter.recordMisses(var3);
      return ImmutableMap.copyOf(var4);
   }

   ImmutableMap getAll(Iterable var1) throws ExecutionException {
      int var2 = 0;
      int var3 = 0;
      LinkedHashMap var4 = Maps.newLinkedHashMap();
      LinkedHashSet var5 = Sets.newLinkedHashSet();
      Iterator var6 = var1.iterator();

      Object var8;
      while(var6.hasNext()) {
         Object var7 = var6.next();
         var8 = this.get(var7);
         if (!var4.containsKey(var7)) {
            var4.put(var7, var8);
            if (var8 == null) {
               ++var3;
               var5.add(var7);
            } else {
               ++var2;
            }
         }
      }

      if (!var5.isEmpty()) {
         Iterator var14;
         try {
            Map var12 = this.loadAll(var5, this.defaultLoader);
            var14 = var5.iterator();

            while(var14.hasNext()) {
               var8 = var14.next();
               Object var9 = var12.get(var8);
               if (var9 == null) {
                  throw new CacheLoader.InvalidCacheLoadException("loadAll failed to return a value for " + var8);
               }

               var4.put(var8, var9);
            }
         } catch (CacheLoader.UnsupportedLoadingOperationException var11) {
            var14 = var5.iterator();

            while(var14.hasNext()) {
               var8 = var14.next();
               --var3;
               var4.put(var8, this.get(var8, this.defaultLoader));
            }
         }
      }

      ImmutableMap var13 = ImmutableMap.copyOf(var4);
      this.globalStatsCounter.recordHits(var2);
      this.globalStatsCounter.recordMisses(var3);
      return var13;
   }

   @Nullable
   Map loadAll(Set var1, CacheLoader var2) throws ExecutionException {
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var1);
      Stopwatch var3 = Stopwatch.createStarted();
      boolean var5 = false;

      Map var4;
      try {
         Map var6 = var2.loadAll(var1);
         var4 = var6;
         var5 = true;
      } catch (CacheLoader.UnsupportedLoadingOperationException var11) {
         var5 = true;
         throw var11;
      } catch (InterruptedException var12) {
         Thread.currentThread().interrupt();
         throw new ExecutionException(var12);
      } catch (RuntimeException var13) {
         throw new UncheckedExecutionException(var13);
      } catch (Exception var14) {
         throw new ExecutionException(var14);
      } catch (Error var15) {
         throw new ExecutionError(var15);
      }

      if (!var5) {
         this.globalStatsCounter.recordLoadException(var3.elapsed(TimeUnit.NANOSECONDS));
      }

      if (var4 == null) {
         this.globalStatsCounter.recordLoadException(var3.elapsed(TimeUnit.NANOSECONDS));
         throw new CacheLoader.InvalidCacheLoadException(var2 + " returned null map from loadAll");
      } else {
         var3.stop();
         boolean var16 = false;
         Iterator var7 = var4.entrySet().iterator();

         while(true) {
            while(var7.hasNext()) {
               Entry var8 = (Entry)var7.next();
               Object var9 = var8.getKey();
               Object var10 = var8.getValue();
               if (var9 != null && var10 != null) {
                  this.put(var9, var10);
               } else {
                  var16 = true;
               }
            }

            if (var16) {
               this.globalStatsCounter.recordLoadException(var3.elapsed(TimeUnit.NANOSECONDS));
               throw new CacheLoader.InvalidCacheLoadException(var2 + " returned null keys or values from loadAll");
            }

            this.globalStatsCounter.recordLoadSuccess(var3.elapsed(TimeUnit.NANOSECONDS));
            return var4;
         }
      }
   }

   LocalCache.ReferenceEntry getEntry(@Nullable Object var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).getEntry(var1, var2);
      }
   }

   void refresh(Object var1) {
      int var2 = this.hash(Preconditions.checkNotNull(var1));
      this.segmentFor(var2).refresh(var1, var2, this.defaultLoader, false);
   }

   public boolean containsKey(@Nullable Object var1) {
      if (var1 == null) {
         return false;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).containsKey(var1, var2);
      }
   }

   public boolean containsValue(@Nullable Object var1) {
      if (var1 == null) {
         return false;
      } else {
         long var2 = this.ticker.read();
         LocalCache.Segment[] var4 = this.segments;
         long var5 = -1L;

         for(int var7 = 0; var7 < 3; ++var7) {
            long var8 = 0L;
            LocalCache.Segment[] var10 = var4;
            int var11 = var4.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               LocalCache.Segment var13 = var10[var12];
               int var14 = var13.count;
               AtomicReferenceArray var15 = var13.table;

               for(int var16 = 0; var16 < var15.length(); ++var16) {
                  for(LocalCache.ReferenceEntry var17 = (LocalCache.ReferenceEntry)var15.get(var16); var17 != null; var17 = var17.getNext()) {
                     Object var18 = var13.getLiveValue(var17, var2);
                     if (var18 != null && this.valueEquivalence.equivalent(var1, var18)) {
                        return true;
                     }
                  }
               }

               var8 += (long)var13.modCount;
            }

            if (var8 == var5) {
               break;
            }

            var5 = var8;
         }

         return false;
      }
   }

   public Object put(Object var1, Object var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      int var3 = this.hash(var1);
      return this.segmentFor(var3).put(var1, var3, var2, false);
   }

   public Object putIfAbsent(Object var1, Object var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      int var3 = this.hash(var1);
      return this.segmentFor(var3).put(var1, var3, var2, true);
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public Object remove(@Nullable Object var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).remove(var1, var2);
      }
   }

   public boolean remove(@Nullable Object var1, @Nullable Object var2) {
      if (var1 != null && var2 != null) {
         int var3 = this.hash(var1);
         return this.segmentFor(var3).remove(var1, var3, var2);
      } else {
         return false;
      }
   }

   public boolean replace(Object var1, @Nullable Object var2, Object var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var3);
      if (var2 == null) {
         return false;
      } else {
         int var4 = this.hash(var1);
         return this.segmentFor(var4).replace(var1, var4, var2, var3);
      }
   }

   public Object replace(Object var1, Object var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      int var3 = this.hash(var1);
      return this.segmentFor(var3).replace(var1, var3, var2);
   }

   public void clear() {
      LocalCache.Segment[] var1 = this.segments;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         LocalCache.Segment var4 = var1[var3];
         var4.clear();
      }

   }

   void invalidateAll(Iterable var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         this.remove(var3);
      }

   }

   public Set keySet() {
      Set var1 = this.keySet;
      return var1 != null ? var1 : (this.keySet = new LocalCache.KeySet(this, this));
   }

   public Collection values() {
      Collection var1 = this.values;
      return var1 != null ? var1 : (this.values = new LocalCache.Values(this, this));
   }

   @GwtIncompatible("Not supported.")
   public Set entrySet() {
      Set var1 = this.entrySet;
      return var1 != null ? var1 : (this.entrySet = new LocalCache.EntrySet(this, this));
   }

   static class LocalLoadingCache extends LocalCache.LocalManualCache implements LoadingCache {
      private static final long serialVersionUID = 1L;

      LocalLoadingCache(CacheBuilder var1, CacheLoader var2) {
         super(new LocalCache(var1, (CacheLoader)Preconditions.checkNotNull(var2)), null);
      }

      public Object get(Object var1) throws ExecutionException {
         return this.localCache.getOrLoad(var1);
      }

      public Object getUnchecked(Object var1) {
         try {
            return this.get(var1);
         } catch (ExecutionException var3) {
            throw new UncheckedExecutionException(var3.getCause());
         }
      }

      public ImmutableMap getAll(Iterable var1) throws ExecutionException {
         return this.localCache.getAll(var1);
      }

      public void refresh(Object var1) {
         this.localCache.refresh(var1);
      }

      public final Object apply(Object var1) {
         return this.getUnchecked(var1);
      }

      Object writeReplace() {
         return new LocalCache.LoadingSerializationProxy(this.localCache);
      }
   }

   static class LocalManualCache implements Cache, Serializable {
      final LocalCache localCache;
      private static final long serialVersionUID = 1L;

      LocalManualCache(CacheBuilder var1) {
         this(new LocalCache(var1, (CacheLoader)null));
      }

      private LocalManualCache(LocalCache var1) {
         this.localCache = var1;
      }

      @Nullable
      public Object getIfPresent(Object var1) {
         return this.localCache.getIfPresent(var1);
      }

      public Object get(Object var1, Callable var2) throws ExecutionException {
         Preconditions.checkNotNull(var2);
         return this.localCache.get(var1, new CacheLoader(this, var2) {
            final Callable val$valueLoader;
            final LocalCache.LocalManualCache this$0;

            {
               this.this$0 = var1;
               this.val$valueLoader = var2;
            }

            public Object load(Object var1) throws Exception {
               return this.val$valueLoader.call();
            }
         });
      }

      public ImmutableMap getAllPresent(Iterable var1) {
         return this.localCache.getAllPresent(var1);
      }

      public void put(Object var1, Object var2) {
         this.localCache.put(var1, var2);
      }

      public void putAll(Map var1) {
         this.localCache.putAll(var1);
      }

      public void invalidate(Object var1) {
         Preconditions.checkNotNull(var1);
         this.localCache.remove(var1);
      }

      public void invalidateAll(Iterable var1) {
         this.localCache.invalidateAll(var1);
      }

      public void invalidateAll() {
         this.localCache.clear();
      }

      public long size() {
         return this.localCache.longSize();
      }

      public ConcurrentMap asMap() {
         return this.localCache;
      }

      public CacheStats stats() {
         AbstractCache.SimpleStatsCounter var1 = new AbstractCache.SimpleStatsCounter();
         var1.incrementBy(this.localCache.globalStatsCounter);
         LocalCache.Segment[] var2 = this.localCache.segments;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            LocalCache.Segment var5 = var2[var4];
            var1.incrementBy(var5.statsCounter);
         }

         return var1.snapshot();
      }

      public void cleanUp() {
         this.localCache.cleanUp();
      }

      Object writeReplace() {
         return new LocalCache.ManualSerializationProxy(this.localCache);
      }

      LocalManualCache(LocalCache var1, Object var2) {
         this(var1);
      }
   }

   static final class LoadingSerializationProxy extends LocalCache.ManualSerializationProxy implements LoadingCache, Serializable {
      private static final long serialVersionUID = 1L;
      transient LoadingCache autoDelegate;

      LoadingSerializationProxy(LocalCache var1) {
         super(var1);
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         CacheBuilder var2 = this.recreateCacheBuilder();
         this.autoDelegate = var2.build(this.loader);
      }

      public Object get(Object var1) throws ExecutionException {
         return this.autoDelegate.get(var1);
      }

      public Object getUnchecked(Object var1) {
         return this.autoDelegate.getUnchecked(var1);
      }

      public ImmutableMap getAll(Iterable var1) throws ExecutionException {
         return this.autoDelegate.getAll(var1);
      }

      public final Object apply(Object var1) {
         return this.autoDelegate.apply(var1);
      }

      public void refresh(Object var1) {
         this.autoDelegate.refresh(var1);
      }

      private Object readResolve() {
         return this.autoDelegate;
      }
   }

   static class ManualSerializationProxy extends ForwardingCache implements Serializable {
      private static final long serialVersionUID = 1L;
      final LocalCache.Strength keyStrength;
      final LocalCache.Strength valueStrength;
      final Equivalence keyEquivalence;
      final Equivalence valueEquivalence;
      final long expireAfterWriteNanos;
      final long expireAfterAccessNanos;
      final long maxWeight;
      final Weigher weigher;
      final int concurrencyLevel;
      final RemovalListener removalListener;
      final Ticker ticker;
      final CacheLoader loader;
      transient Cache delegate;

      ManualSerializationProxy(LocalCache var1) {
         this(var1.keyStrength, var1.valueStrength, var1.keyEquivalence, var1.valueEquivalence, var1.expireAfterWriteNanos, var1.expireAfterAccessNanos, var1.maxWeight, var1.weigher, var1.concurrencyLevel, var1.removalListener, var1.ticker, var1.defaultLoader);
      }

      private ManualSerializationProxy(LocalCache.Strength var1, LocalCache.Strength var2, Equivalence var3, Equivalence var4, long var5, long var7, long var9, Weigher var11, int var12, RemovalListener var13, Ticker var14, CacheLoader var15) {
         this.keyStrength = var1;
         this.valueStrength = var2;
         this.keyEquivalence = var3;
         this.valueEquivalence = var4;
         this.expireAfterWriteNanos = var5;
         this.expireAfterAccessNanos = var7;
         this.maxWeight = var9;
         this.weigher = var11;
         this.concurrencyLevel = var12;
         this.removalListener = var13;
         this.ticker = var14 != Ticker.systemTicker() && var14 != CacheBuilder.NULL_TICKER ? var14 : null;
         this.loader = var15;
      }

      CacheBuilder recreateCacheBuilder() {
         CacheBuilder var1 = CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener(this.removalListener);
         var1.strictParsing = false;
         if (this.expireAfterWriteNanos > 0L) {
            var1.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
         }

         if (this.expireAfterAccessNanos > 0L) {
            var1.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
         }

         if (this.weigher != CacheBuilder.OneWeigher.INSTANCE) {
            var1.weigher(this.weigher);
            if (this.maxWeight != -1L) {
               var1.maximumWeight(this.maxWeight);
            }
         } else if (this.maxWeight != -1L) {
            var1.maximumSize(this.maxWeight);
         }

         if (this.ticker != null) {
            var1.ticker(this.ticker);
         }

         return var1;
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         CacheBuilder var2 = this.recreateCacheBuilder();
         this.delegate = var2.build();
      }

      private Object readResolve() {
         return this.delegate;
      }

      protected Cache delegate() {
         return this.delegate;
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   final class EntrySet extends LocalCache.AbstractCacheSet {
      final LocalCache this$0;

      EntrySet(LocalCache var1, ConcurrentMap var2) {
         super(var1, var2);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return this.this$0.new EntryIterator(this.this$0);
      }

      public boolean contains(Object var1) {
         if (!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            Object var3 = var2.getKey();
            if (var3 == null) {
               return false;
            } else {
               Object var4 = this.this$0.get(var3);
               return var4 != null && this.this$0.valueEquivalence.equivalent(var2.getValue(), var4);
            }
         }
      }

      public boolean remove(Object var1) {
         if (!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            Object var3 = var2.getKey();
            return var3 != null && this.this$0.remove(var3, var2.getValue());
         }
      }
   }

   final class Values extends AbstractCollection {
      private final ConcurrentMap map;
      final LocalCache this$0;

      Values(LocalCache var1, ConcurrentMap var2) {
         this.this$0 = var1;
         this.map = var2;
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

      public Iterator iterator() {
         return this.this$0.new ValueIterator(this.this$0);
      }

      public boolean contains(Object var1) {
         return this.map.containsValue(var1);
      }
   }

   final class KeySet extends LocalCache.AbstractCacheSet {
      final LocalCache this$0;

      KeySet(LocalCache var1, ConcurrentMap var2) {
         super(var1, var2);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return this.this$0.new KeyIterator(this.this$0);
      }

      public boolean contains(Object var1) {
         return this.map.containsKey(var1);
      }

      public boolean remove(Object var1) {
         return this.map.remove(var1) != null;
      }
   }

   abstract class AbstractCacheSet extends AbstractSet {
      final ConcurrentMap map;
      final LocalCache this$0;

      AbstractCacheSet(LocalCache var1, ConcurrentMap var2) {
         this.this$0 = var1;
         this.map = var2;
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
   }

   final class EntryIterator extends LocalCache.HashIterator {
      final LocalCache this$0;

      EntryIterator(LocalCache var1) {
         super(var1);
         this.this$0 = var1;
      }

      public Entry next() {
         return this.nextEntry();
      }

      public Object next() {
         return this.next();
      }
   }

   final class WriteThroughEntry implements Entry {
      final Object key;
      Object value;
      final LocalCache this$0;

      WriteThroughEntry(LocalCache var1, Object var2, Object var3) {
         this.this$0 = var1;
         this.key = var2;
         this.value = var3;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            return this.key.equals(var2.getKey()) && this.value.equals(var2.getValue());
         }
      }

      public int hashCode() {
         return this.key.hashCode() ^ this.value.hashCode();
      }

      public Object setValue(Object var1) {
         throw new UnsupportedOperationException();
      }

      public String toString() {
         return this.getKey() + "=" + this.getValue();
      }
   }

   final class ValueIterator extends LocalCache.HashIterator {
      final LocalCache this$0;

      ValueIterator(LocalCache var1) {
         super(var1);
         this.this$0 = var1;
      }

      public Object next() {
         return this.nextEntry().getValue();
      }
   }

   final class KeyIterator extends LocalCache.HashIterator {
      final LocalCache this$0;

      KeyIterator(LocalCache var1) {
         super(var1);
         this.this$0 = var1;
      }

      public Object next() {
         return this.nextEntry().getKey();
      }
   }

   abstract class HashIterator implements Iterator {
      int nextSegmentIndex;
      int nextTableIndex;
      LocalCache.Segment currentSegment;
      AtomicReferenceArray currentTable;
      LocalCache.ReferenceEntry nextEntry;
      LocalCache.WriteThroughEntry nextExternal;
      LocalCache.WriteThroughEntry lastReturned;
      final LocalCache this$0;

      HashIterator(LocalCache var1) {
         this.this$0 = var1;
         this.nextSegmentIndex = var1.segments.length - 1;
         this.nextTableIndex = -1;
         this.advance();
      }

      public abstract Object next();

      final void advance() {
         this.nextExternal = null;
         if (this == null) {
            if (!(this >= 0)) {
               while(this.nextSegmentIndex >= 0) {
                  this.currentSegment = this.this$0.segments[this.nextSegmentIndex--];
                  if (this.currentSegment.count != 0) {
                     this.currentTable = this.currentSegment.table;
                     this.nextTableIndex = this.currentTable.length() - 1;
                     if (this >= 0) {
                        return;
                     }
                  }
               }

            }
         }
      }

      public boolean hasNext() {
         return this.nextExternal != null;
      }

      LocalCache.WriteThroughEntry nextEntry() {
         if (this.nextExternal == null) {
            throw new NoSuchElementException();
         } else {
            this.lastReturned = this.nextExternal;
            this.advance();
            return this.lastReturned;
         }
      }

      public void remove() {
         Preconditions.checkState(this.lastReturned != null);
         this.this$0.remove(this.lastReturned.getKey());
         this.lastReturned = null;
      }
   }

   static final class AccessQueue extends AbstractQueue {
      final LocalCache.ReferenceEntry head = new LocalCache.AbstractReferenceEntry(this) {
         LocalCache.ReferenceEntry nextAccess;
         LocalCache.ReferenceEntry previousAccess;
         final LocalCache.AccessQueue this$0;

         {
            this.this$0 = var1;
            this.nextAccess = this;
            this.previousAccess = this;
         }

         public long getAccessTime() {
            return Long.MAX_VALUE;
         }

         public void setAccessTime(long var1) {
         }

         public LocalCache.ReferenceEntry getNextInAccessQueue() {
            return this.nextAccess;
         }

         public void setNextInAccessQueue(LocalCache.ReferenceEntry var1) {
            this.nextAccess = var1;
         }

         public LocalCache.ReferenceEntry getPreviousInAccessQueue() {
            return this.previousAccess;
         }

         public void setPreviousInAccessQueue(LocalCache.ReferenceEntry var1) {
            this.previousAccess = var1;
         }
      };

      public boolean offer(LocalCache.ReferenceEntry var1) {
         LocalCache.connectAccessOrder(var1.getPreviousInAccessQueue(), var1.getNextInAccessQueue());
         LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), var1);
         LocalCache.connectAccessOrder(var1, this.head);
         return true;
      }

      public LocalCache.ReferenceEntry peek() {
         LocalCache.ReferenceEntry var1 = this.head.getNextInAccessQueue();
         return var1 == this.head ? null : var1;
      }

      public LocalCache.ReferenceEntry poll() {
         LocalCache.ReferenceEntry var1 = this.head.getNextInAccessQueue();
         if (var1 == this.head) {
            return null;
         } else {
            this.remove(var1);
            return var1;
         }
      }

      public boolean remove(Object var1) {
         LocalCache.ReferenceEntry var2 = (LocalCache.ReferenceEntry)var1;
         LocalCache.ReferenceEntry var3 = var2.getPreviousInAccessQueue();
         LocalCache.ReferenceEntry var4 = var2.getNextInAccessQueue();
         LocalCache.connectAccessOrder(var3, var4);
         LocalCache.nullifyAccessOrder(var2);
         return var4 != LocalCache.NullEntry.INSTANCE;
      }

      public boolean contains(Object var1) {
         LocalCache.ReferenceEntry var2 = (LocalCache.ReferenceEntry)var1;
         return var2.getNextInAccessQueue() != LocalCache.NullEntry.INSTANCE;
      }

      public boolean isEmpty() {
         return this.head.getNextInAccessQueue() == this.head;
      }

      public int size() {
         int var1 = 0;

         for(LocalCache.ReferenceEntry var2 = this.head.getNextInAccessQueue(); var2 != this.head; var2 = var2.getNextInAccessQueue()) {
            ++var1;
         }

         return var1;
      }

      public void clear() {
         LocalCache.ReferenceEntry var2;
         for(LocalCache.ReferenceEntry var1 = this.head.getNextInAccessQueue(); var1 != this.head; var1 = var2) {
            var2 = var1.getNextInAccessQueue();
            LocalCache.nullifyAccessOrder(var1);
         }

         this.head.setNextInAccessQueue(this.head);
         this.head.setPreviousInAccessQueue(this.head);
      }

      public Iterator iterator() {
         return new AbstractSequentialIterator(this, this.peek()) {
            final LocalCache.AccessQueue this$0;

            {
               this.this$0 = var1;
            }

            protected LocalCache.ReferenceEntry computeNext(LocalCache.ReferenceEntry var1) {
               LocalCache.ReferenceEntry var2 = var1.getNextInAccessQueue();
               return var2 == this.this$0.head ? null : var2;
            }

            protected Object computeNext(Object var1) {
               return this.computeNext((LocalCache.ReferenceEntry)var1);
            }
         };
      }

      public Object peek() {
         return this.peek();
      }

      public Object poll() {
         return this.poll();
      }

      public boolean offer(Object var1) {
         return this.offer((LocalCache.ReferenceEntry)var1);
      }
   }

   static final class WriteQueue extends AbstractQueue {
      final LocalCache.ReferenceEntry head = new LocalCache.AbstractReferenceEntry(this) {
         LocalCache.ReferenceEntry nextWrite;
         LocalCache.ReferenceEntry previousWrite;
         final LocalCache.WriteQueue this$0;

         {
            this.this$0 = var1;
            this.nextWrite = this;
            this.previousWrite = this;
         }

         public long getWriteTime() {
            return Long.MAX_VALUE;
         }

         public void setWriteTime(long var1) {
         }

         public LocalCache.ReferenceEntry getNextInWriteQueue() {
            return this.nextWrite;
         }

         public void setNextInWriteQueue(LocalCache.ReferenceEntry var1) {
            this.nextWrite = var1;
         }

         public LocalCache.ReferenceEntry getPreviousInWriteQueue() {
            return this.previousWrite;
         }

         public void setPreviousInWriteQueue(LocalCache.ReferenceEntry var1) {
            this.previousWrite = var1;
         }
      };

      public boolean offer(LocalCache.ReferenceEntry var1) {
         LocalCache.connectWriteOrder(var1.getPreviousInWriteQueue(), var1.getNextInWriteQueue());
         LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), var1);
         LocalCache.connectWriteOrder(var1, this.head);
         return true;
      }

      public LocalCache.ReferenceEntry peek() {
         LocalCache.ReferenceEntry var1 = this.head.getNextInWriteQueue();
         return var1 == this.head ? null : var1;
      }

      public LocalCache.ReferenceEntry poll() {
         LocalCache.ReferenceEntry var1 = this.head.getNextInWriteQueue();
         if (var1 == this.head) {
            return null;
         } else {
            this.remove(var1);
            return var1;
         }
      }

      public boolean remove(Object var1) {
         LocalCache.ReferenceEntry var2 = (LocalCache.ReferenceEntry)var1;
         LocalCache.ReferenceEntry var3 = var2.getPreviousInWriteQueue();
         LocalCache.ReferenceEntry var4 = var2.getNextInWriteQueue();
         LocalCache.connectWriteOrder(var3, var4);
         LocalCache.nullifyWriteOrder(var2);
         return var4 != LocalCache.NullEntry.INSTANCE;
      }

      public boolean contains(Object var1) {
         LocalCache.ReferenceEntry var2 = (LocalCache.ReferenceEntry)var1;
         return var2.getNextInWriteQueue() != LocalCache.NullEntry.INSTANCE;
      }

      public boolean isEmpty() {
         return this.head.getNextInWriteQueue() == this.head;
      }

      public int size() {
         int var1 = 0;

         for(LocalCache.ReferenceEntry var2 = this.head.getNextInWriteQueue(); var2 != this.head; var2 = var2.getNextInWriteQueue()) {
            ++var1;
         }

         return var1;
      }

      public void clear() {
         LocalCache.ReferenceEntry var2;
         for(LocalCache.ReferenceEntry var1 = this.head.getNextInWriteQueue(); var1 != this.head; var1 = var2) {
            var2 = var1.getNextInWriteQueue();
            LocalCache.nullifyWriteOrder(var1);
         }

         this.head.setNextInWriteQueue(this.head);
         this.head.setPreviousInWriteQueue(this.head);
      }

      public Iterator iterator() {
         return new AbstractSequentialIterator(this, this.peek()) {
            final LocalCache.WriteQueue this$0;

            {
               this.this$0 = var1;
            }

            protected LocalCache.ReferenceEntry computeNext(LocalCache.ReferenceEntry var1) {
               LocalCache.ReferenceEntry var2 = var1.getNextInWriteQueue();
               return var2 == this.this$0.head ? null : var2;
            }

            protected Object computeNext(Object var1) {
               return this.computeNext((LocalCache.ReferenceEntry)var1);
            }
         };
      }

      public Object peek() {
         return this.peek();
      }

      public Object poll() {
         return this.poll();
      }

      public boolean offer(Object var1) {
         return this.offer((LocalCache.ReferenceEntry)var1);
      }
   }

   static class LoadingValueReference implements LocalCache.ValueReference {
      volatile LocalCache.ValueReference oldValue;
      final SettableFuture futureValue;
      final Stopwatch stopwatch;

      public LoadingValueReference() {
         this(LocalCache.unset());
      }

      public LoadingValueReference(LocalCache.ValueReference var1) {
         this.futureValue = SettableFuture.create();
         this.stopwatch = Stopwatch.createUnstarted();
         this.oldValue = var1;
      }

      public boolean isLoading() {
         return true;
      }

      public boolean isActive() {
         return this.oldValue.isActive();
      }

      public int getWeight() {
         return this.oldValue.getWeight();
      }

      public boolean set(@Nullable Object var1) {
         return this.futureValue.set(var1);
      }

      public boolean setException(Throwable var1) {
         return this.futureValue.setException(var1);
      }

      private ListenableFuture fullyFailedFuture(Throwable var1) {
         return Futures.immediateFailedFuture(var1);
      }

      public void notifyNewValue(@Nullable Object var1) {
         if (var1 != null) {
            this.set(var1);
         } else {
            this.oldValue = LocalCache.unset();
         }

      }

      public ListenableFuture loadFuture(Object var1, CacheLoader var2) {
         this.stopwatch.start();
         Object var3 = this.oldValue.get();

         try {
            if (var3 == null) {
               Object var6 = var2.load(var1);
               return (ListenableFuture)(this.set(var6) ? this.futureValue : Futures.immediateFuture(var6));
            } else {
               ListenableFuture var4 = var2.reload(var1, var3);
               return var4 == null ? Futures.immediateFuture((Object)null) : Futures.transform(var4, new Function(this) {
                  final LocalCache.LoadingValueReference this$0;

                  {
                     this.this$0 = var1;
                  }

                  public Object apply(Object var1) {
                     this.this$0.set(var1);
                     return var1;
                  }
               });
            }
         } catch (Throwable var5) {
            if (var5 instanceof InterruptedException) {
               Thread.currentThread().interrupt();
            }

            return (ListenableFuture)(this.setException(var5) ? this.futureValue : this.fullyFailedFuture(var5));
         }
      }

      public long elapsedNanos() {
         return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
      }

      public Object waitForValue() throws ExecutionException {
         return Uninterruptibles.getUninterruptibly(this.futureValue);
      }

      public Object get() {
         return this.oldValue.get();
      }

      public LocalCache.ValueReference getOldValue() {
         return this.oldValue;
      }

      public LocalCache.ReferenceEntry getEntry() {
         return null;
      }

      public LocalCache.ValueReference copyFor(ReferenceQueue var1, @Nullable Object var2, LocalCache.ReferenceEntry var3) {
         return this;
      }
   }

   static class Segment extends ReentrantLock {
      final LocalCache map;
      volatile int count;
      @GuardedBy("Segment.this")
      int totalWeight;
      int modCount;
      int threshold;
      volatile AtomicReferenceArray table;
      final long maxSegmentWeight;
      final ReferenceQueue keyReferenceQueue;
      final ReferenceQueue valueReferenceQueue;
      final Queue recencyQueue;
      final AtomicInteger readCount = new AtomicInteger();
      @GuardedBy("Segment.this")
      final Queue writeQueue;
      @GuardedBy("Segment.this")
      final Queue accessQueue;
      final AbstractCache.StatsCounter statsCounter;

      Segment(LocalCache var1, int var2, long var3, AbstractCache.StatsCounter var5) {
         this.map = var1;
         this.maxSegmentWeight = var3;
         this.statsCounter = (AbstractCache.StatsCounter)Preconditions.checkNotNull(var5);
         this.initTable(this.newEntryArray(var2));
         this.keyReferenceQueue = var1.usesKeyReferences() ? new ReferenceQueue() : null;
         this.valueReferenceQueue = var1.usesValueReferences() ? new ReferenceQueue() : null;
         this.recencyQueue = (Queue)(var1.usesAccessQueue() ? new ConcurrentLinkedQueue() : LocalCache.discardingQueue());
         this.writeQueue = (Queue)(var1.usesWriteQueue() ? new LocalCache.WriteQueue() : LocalCache.discardingQueue());
         this.accessQueue = (Queue)(var1.usesAccessQueue() ? new LocalCache.AccessQueue() : LocalCache.discardingQueue());
      }

      AtomicReferenceArray newEntryArray(int var1) {
         return new AtomicReferenceArray(var1);
      }

      void initTable(AtomicReferenceArray var1) {
         this.threshold = var1.length() * 3 / 4;
         if (!this.map.customWeigher() && (long)this.threshold == this.maxSegmentWeight) {
            ++this.threshold;
         }

         this.table = var1;
      }

      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry newEntry(Object var1, int var2, @Nullable LocalCache.ReferenceEntry var3) {
         return this.map.entryFactory.newEntry(this, Preconditions.checkNotNull(var1), var2, var3);
      }

      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry copyEntry(LocalCache.ReferenceEntry var1, LocalCache.ReferenceEntry var2) {
         if (var1.getKey() == null) {
            return null;
         } else {
            LocalCache.ValueReference var3 = var1.getValueReference();
            Object var4 = var3.get();
            if (var4 == null && var3.isActive()) {
               return null;
            } else {
               LocalCache.ReferenceEntry var5 = this.map.entryFactory.copyEntry(this, var1, var2);
               var5.setValueReference(var3.copyFor(this.valueReferenceQueue, var4, var5));
               return var5;
            }
         }
      }

      @GuardedBy("Segment.this")
      void setValue(LocalCache.ReferenceEntry var1, Object var2, Object var3, long var4) {
         LocalCache.ValueReference var6 = var1.getValueReference();
         int var7 = this.map.weigher.weigh(var2, var3);
         Preconditions.checkState(var7 >= 0, "Weights must be non-negative");
         LocalCache.ValueReference var8 = this.map.valueStrength.referenceValue(this, var1, var3, var7);
         var1.setValueReference(var8);
         this.recordWrite(var1, var7, var4);
         var6.notifyNewValue(var3);
      }

      Object get(Object var1, int var2, CacheLoader var3) throws ExecutionException {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var3);

         Object var14;
         label43: {
            Object var9;
            label42: {
               Object var12;
               try {
                  if (this.count != 0) {
                     LocalCache.ReferenceEntry var4 = this.getEntry(var1, var2);
                     if (var4 != null) {
                        long var13 = this.map.ticker.read();
                        Object var7 = this.getLiveValue(var4, var13);
                        if (var7 != null) {
                           this.recordRead(var4, var13);
                           this.statsCounter.recordHits(1);
                           var14 = this.scheduleRefresh(var4, var1, var2, var7, var13, var3);
                           break label43;
                        }

                        LocalCache.ValueReference var8 = var4.getValueReference();
                        if (var8.isLoading()) {
                           var9 = this.waitForLoadingValue(var4, var1, var8);
                           break label42;
                        }
                     }
                  }

                  var12 = this.lockedGetOrLoad(var1, var2, var3);
               } catch (ExecutionException var11) {
                  Throwable var5 = var11.getCause();
                  if (var5 instanceof Error) {
                     throw new ExecutionError((Error)var5);
                  }

                  if (var5 instanceof RuntimeException) {
                     throw new UncheckedExecutionException(var5);
                  }

                  throw var11;
               }

               this.postReadCleanup();
               return var12;
            }

            this.postReadCleanup();
            return var9;
         }

         this.postReadCleanup();
         return var14;
      }

      Object lockedGetOrLoad(Object var1, int var2, CacheLoader var3) throws ExecutionException {
         LocalCache.ValueReference var5 = null;
         LocalCache.LoadingValueReference var6 = null;
         boolean var7 = true;
         this.lock();
         long var8 = this.map.ticker.read();
         this.preWriteCleanup(var8);
         int var10 = this.count - 1;
         AtomicReferenceArray var11 = this.table;
         int var12 = var2 & var11.length() - 1;
         LocalCache.ReferenceEntry var13 = (LocalCache.ReferenceEntry)var11.get(var12);

         LocalCache.ReferenceEntry var4;
         for(var4 = var13; var4 != null; var4 = var4.getNext()) {
            Object var14 = var4.getKey();
            if (var4.getHash() == var2 && var14 != null && this.map.keyEquivalence.equivalent(var1, var14)) {
               var5 = var4.getValueReference();
               if (var5.isLoading()) {
                  var7 = false;
               } else {
                  Object var15 = var5.get();
                  if (var15 == null) {
                     this.enqueueNotification(var14, var2, var5, RemovalCause.COLLECTED);
                  } else {
                     if (!this.map.isExpired(var4, var8)) {
                        this.recordLockedRead(var4, var8);
                        this.statsCounter.recordHits(1);
                        this.unlock();
                        this.postWriteCleanup();
                        return var15;
                     }

                     this.enqueueNotification(var14, var2, var5, RemovalCause.EXPIRED);
                  }

                  this.writeQueue.remove(var4);
                  this.accessQueue.remove(var4);
                  this.count = var10;
               }
               break;
            }
         }

         if (var7) {
            var6 = new LocalCache.LoadingValueReference();
            if (var4 == null) {
               var4 = this.newEntry(var1, var2, var13);
               var4.setValueReference(var6);
               var11.set(var12, var4);
            } else {
               var4.setValueReference(var6);
            }
         }

         this.unlock();
         this.postWriteCleanup();
         if (var7) {
            synchronized(var4){}
            Object var9 = this.loadSync(var1, var2, var6, var3);
            this.statsCounter.recordMisses(1);
            return var9;
         } else {
            return this.waitForLoadingValue(var4, var1, var5);
         }
      }

      Object waitForLoadingValue(LocalCache.ReferenceEntry var1, Object var2, LocalCache.ValueReference var3) throws ExecutionException {
         if (!var3.isLoading()) {
            throw new AssertionError();
         } else {
            Preconditions.checkState(!Thread.holdsLock(var1), "Recursive load of: %s", var2);
            Object var4 = var3.waitForValue();
            if (var4 == null) {
               throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + var2 + ".");
            } else {
               long var5 = this.map.ticker.read();
               this.recordRead(var1, var5);
               this.statsCounter.recordMisses(1);
               return var4;
            }
         }
      }

      Object loadSync(Object var1, int var2, LocalCache.LoadingValueReference var3, CacheLoader var4) throws ExecutionException {
         ListenableFuture var5 = var3.loadFuture(var1, var4);
         return this.getAndRecordStats(var1, var2, var3, var5);
      }

      ListenableFuture loadAsync(Object var1, int var2, LocalCache.LoadingValueReference var3, CacheLoader var4) {
         ListenableFuture var5 = var3.loadFuture(var1, var4);
         var5.addListener(new Runnable(this, var1, var2, var3, var5) {
            final Object val$key;
            final int val$hash;
            final LocalCache.LoadingValueReference val$loadingValueReference;
            final ListenableFuture val$loadingFuture;
            final LocalCache.Segment this$0;

            {
               this.this$0 = var1;
               this.val$key = var2;
               this.val$hash = var3;
               this.val$loadingValueReference = var4;
               this.val$loadingFuture = var5;
            }

            public void run() {
               try {
                  Object var1 = this.this$0.getAndRecordStats(this.val$key, this.val$hash, this.val$loadingValueReference, this.val$loadingFuture);
               } catch (Throwable var2) {
                  LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", var2);
                  this.val$loadingValueReference.setException(var2);
               }

            }
         }, LocalCache.sameThreadExecutor);
         return var5;
      }

      Object getAndRecordStats(Object var1, int var2, LocalCache.LoadingValueReference var3, ListenableFuture var4) throws ExecutionException {
         Object var5 = null;
         var5 = Uninterruptibles.getUninterruptibly(var4);
         if (var5 == null) {
            throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + var1 + ".");
         } else {
            this.statsCounter.recordLoadSuccess(var3.elapsedNanos());
            this.storeLoadedValue(var1, var2, var3, var5);
            if (var5 == null) {
               this.statsCounter.recordLoadException(var3.elapsedNanos());
               this.removeLoadingValue(var1, var2, var3);
            }

            return var5;
         }
      }

      Object scheduleRefresh(LocalCache.ReferenceEntry var1, Object var2, int var3, Object var4, long var5, CacheLoader var7) {
         if (this.map.refreshes() && var5 - var1.getWriteTime() > this.map.refreshNanos && !var1.getValueReference().isLoading()) {
            Object var8 = this.refresh(var2, var3, var7, true);
            if (var8 != null) {
               return var8;
            }
         }

         return var4;
      }

      @Nullable
      Object refresh(Object var1, int var2, CacheLoader var3, boolean var4) {
         LocalCache.LoadingValueReference var5 = this.insertLoadingValueReference(var1, var2, var4);
         if (var5 == null) {
            return null;
         } else {
            ListenableFuture var6 = this.loadAsync(var1, var2, var5, var3);
            if (var6.isDone()) {
               try {
                  return Uninterruptibles.getUninterruptibly(var6);
               } catch (Throwable var8) {
               }
            }

            return null;
         }
      }

      @Nullable
      LocalCache.LoadingValueReference insertLoadingValueReference(Object var1, int var2, boolean var3) {
         LocalCache.ReferenceEntry var4 = null;
         this.lock();
         long var5 = this.map.ticker.read();
         this.preWriteCleanup(var5);
         AtomicReferenceArray var7 = this.table;
         int var8 = var2 & var7.length() - 1;
         LocalCache.ReferenceEntry var9 = (LocalCache.ReferenceEntry)var7.get(var8);

         for(var4 = var9; var4 != null; var4 = var4.getNext()) {
            Object var10 = var4.getKey();
            if (var4.getHash() == var2 && var10 != null && this.map.keyEquivalence.equivalent(var1, var10)) {
               LocalCache.ValueReference var11 = var4.getValueReference();
               LocalCache.LoadingValueReference var12;
               if (!var11.isLoading() && (!var3 || var5 - var4.getWriteTime() >= this.map.refreshNanos)) {
                  ++this.modCount;
                  var12 = new LocalCache.LoadingValueReference(var11);
                  var4.setValueReference(var12);
                  this.unlock();
                  this.postWriteCleanup();
                  return var12;
               }

               var12 = null;
               this.unlock();
               this.postWriteCleanup();
               return var12;
            }
         }

         ++this.modCount;
         LocalCache.LoadingValueReference var15 = new LocalCache.LoadingValueReference();
         var4 = this.newEntry(var1, var2, var9);
         var4.setValueReference(var15);
         var7.set(var8, var4);
         this.unlock();
         this.postWriteCleanup();
         return var15;
      }

      void tryDrainReferenceQueues() {
         if (this.tryLock()) {
            this.drainReferenceQueues();
            this.unlock();
         }

      }

      @GuardedBy("Segment.this")
      void drainReferenceQueues() {
         if (this.map.usesKeyReferences()) {
            this.drainKeyReferenceQueue();
         }

         if (this.map.usesValueReferences()) {
            this.drainValueReferenceQueue();
         }

      }

      @GuardedBy("Segment.this")
      void drainKeyReferenceQueue() {
         int var2 = 0;

         Reference var1;
         while((var1 = this.keyReferenceQueue.poll()) != null) {
            LocalCache.ReferenceEntry var3 = (LocalCache.ReferenceEntry)var1;
            this.map.reclaimKey(var3);
            ++var2;
            if (var2 == 16) {
               break;
            }
         }

      }

      @GuardedBy("Segment.this")
      void drainValueReferenceQueue() {
         int var2 = 0;

         Reference var1;
         while((var1 = this.valueReferenceQueue.poll()) != null) {
            LocalCache.ValueReference var3 = (LocalCache.ValueReference)var1;
            this.map.reclaimValue(var3);
            ++var2;
            if (var2 == 16) {
               break;
            }
         }

      }

      void clearReferenceQueues() {
         if (this.map.usesKeyReferences()) {
            this.clearKeyReferenceQueue();
         }

         if (this.map.usesValueReferences()) {
            this.clearValueReferenceQueue();
         }

      }

      void clearKeyReferenceQueue() {
         while(this.keyReferenceQueue.poll() != null) {
         }

      }

      void clearValueReferenceQueue() {
         while(this.valueReferenceQueue.poll() != null) {
         }

      }

      void recordRead(LocalCache.ReferenceEntry var1, long var2) {
         if (this.map.recordsAccess()) {
            var1.setAccessTime(var2);
         }

         this.recencyQueue.add(var1);
      }

      @GuardedBy("Segment.this")
      void recordLockedRead(LocalCache.ReferenceEntry var1, long var2) {
         if (this.map.recordsAccess()) {
            var1.setAccessTime(var2);
         }

         this.accessQueue.add(var1);
      }

      @GuardedBy("Segment.this")
      void recordWrite(LocalCache.ReferenceEntry var1, int var2, long var3) {
         this.drainRecencyQueue();
         this.totalWeight += var2;
         if (this.map.recordsAccess()) {
            var1.setAccessTime(var3);
         }

         if (this.map.recordsWrite()) {
            var1.setWriteTime(var3);
         }

         this.accessQueue.add(var1);
         this.writeQueue.add(var1);
      }

      @GuardedBy("Segment.this")
      void drainRecencyQueue() {
         LocalCache.ReferenceEntry var1;
         while((var1 = (LocalCache.ReferenceEntry)this.recencyQueue.poll()) != null) {
            if (this.accessQueue.contains(var1)) {
               this.accessQueue.add(var1);
            }
         }

      }

      void tryExpireEntries(long var1) {
         if (this.tryLock()) {
            this.expireEntries(var1);
            this.unlock();
         }

      }

      @GuardedBy("Segment.this")
      void expireEntries(long var1) {
         this.drainRecencyQueue();

         LocalCache.ReferenceEntry var3;
         while((var3 = (LocalCache.ReferenceEntry)this.writeQueue.peek()) != null && this.map.isExpired(var3, var1)) {
            var3.getHash();
            if (RemovalCause.EXPIRED != null) {
               throw new AssertionError();
            }
         }

         while((var3 = (LocalCache.ReferenceEntry)this.accessQueue.peek()) != null && this.map.isExpired(var3, var1)) {
            var3.getHash();
            if (RemovalCause.EXPIRED != null) {
               throw new AssertionError();
            }
         }

      }

      @GuardedBy("Segment.this")
      void enqueueNotification(LocalCache.ReferenceEntry var1, RemovalCause var2) {
         this.enqueueNotification(var1.getKey(), var1.getHash(), var1.getValueReference(), var2);
      }

      @GuardedBy("Segment.this")
      void enqueueNotification(@Nullable Object var1, int var2, LocalCache.ValueReference var3, RemovalCause var4) {
         this.totalWeight -= var3.getWeight();
         if (var4.wasEvicted()) {
            this.statsCounter.recordEviction();
         }

         if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
            Object var5 = var3.get();
            RemovalNotification var6 = new RemovalNotification(var1, var5, var4);
            this.map.removalNotificationQueue.offer(var6);
         }

      }

      @GuardedBy("Segment.this")
      void evictEntries() {
         if (this.map.evictsBySize()) {
            this.drainRecencyQueue();

            do {
               if ((long)this.totalWeight <= this.maxSegmentWeight) {
                  return;
               }

               LocalCache.ReferenceEntry var1 = this.getNextEvictable();
               var1.getHash();
            } while(RemovalCause.SIZE == null);

            throw new AssertionError();
         }
      }

      LocalCache.ReferenceEntry getNextEvictable() {
         Iterator var1 = this.accessQueue.iterator();

         LocalCache.ReferenceEntry var2;
         int var3;
         do {
            if (!var1.hasNext()) {
               throw new AssertionError();
            }

            var2 = (LocalCache.ReferenceEntry)var1.next();
            var3 = var2.getValueReference().getWeight();
         } while(var3 <= 0);

         return var2;
      }

      LocalCache.ReferenceEntry getFirst(int var1) {
         AtomicReferenceArray var2 = this.table;
         return (LocalCache.ReferenceEntry)var2.get(var1 & var2.length() - 1);
      }

      @Nullable
      LocalCache.ReferenceEntry getEntry(Object var1, int var2) {
         for(LocalCache.ReferenceEntry var3 = this.getFirst(var2); var3 != null; var3 = var3.getNext()) {
            if (var3.getHash() == var2) {
               Object var4 = var3.getKey();
               if (var4 == null) {
                  this.tryDrainReferenceQueues();
               } else if (this.map.keyEquivalence.equivalent(var1, var4)) {
                  return var3;
               }
            }
         }

         return null;
      }

      @Nullable
      LocalCache.ReferenceEntry getLiveEntry(Object var1, int var2, long var3) {
         LocalCache.ReferenceEntry var5 = this.getEntry(var1, var2);
         if (var5 == null) {
            return null;
         } else if (this.map.isExpired(var5, var3)) {
            this.tryExpireEntries(var3);
            return null;
         } else {
            return var5;
         }
      }

      Object getLiveValue(LocalCache.ReferenceEntry var1, long var2) {
         if (var1.getKey() == null) {
            this.tryDrainReferenceQueues();
            return null;
         } else {
            Object var4 = var1.getValueReference().get();
            if (var4 == null) {
               this.tryDrainReferenceQueues();
               return null;
            } else if (this.map.isExpired(var1, var2)) {
               this.tryExpireEntries(var2);
               return null;
            } else {
               return var4;
            }
         }
      }

      @Nullable
      Object get(Object var1, int var2) {
         if (this.count != 0) {
            long var3 = this.map.ticker.read();
            LocalCache.ReferenceEntry var5 = this.getLiveEntry(var1, var2, var3);
            Object var6;
            if (var5 == null) {
               var6 = null;
               this.postReadCleanup();
               return var6;
            }

            var6 = var5.getValueReference().get();
            if (var6 != null) {
               this.recordRead(var5, var3);
               Object var7 = this.scheduleRefresh(var5, var5.getKey(), var2, var6, var3, this.map.defaultLoader);
               this.postReadCleanup();
               return var7;
            }

            this.tryDrainReferenceQueues();
         }

         Object var9 = null;
         this.postReadCleanup();
         return var9;
      }

      boolean containsKey(Object var1, int var2) {
         if (this.count != 0) {
            long var8 = this.map.ticker.read();
            LocalCache.ReferenceEntry var5 = this.getLiveEntry(var1, var2, var8);
            boolean var6;
            if (var5 == null) {
               var6 = false;
               this.postReadCleanup();
               return var6;
            } else {
               var6 = var5.getValueReference().get() != null;
               this.postReadCleanup();
               return var6;
            }
         } else {
            boolean var3 = false;
            this.postReadCleanup();
            return var3;
         }
      }

      @VisibleForTesting
      boolean containsValue(Object var1) {
         if (this.count != 0) {
            long var2 = this.map.ticker.read();
            AtomicReferenceArray var4 = this.table;
            int var5 = var4.length();

            for(int var6 = 0; var6 < var5; ++var6) {
               for(LocalCache.ReferenceEntry var7 = (LocalCache.ReferenceEntry)var4.get(var6); var7 != null; var7 = var7.getNext()) {
                  Object var8 = this.getLiveValue(var7, var2);
                  if (var8 != null && this.map.valueEquivalence.equivalent(var1, var8)) {
                     boolean var9 = true;
                     this.postReadCleanup();
                     return var9;
                  }
               }
            }
         }

         boolean var11 = false;
         this.postReadCleanup();
         return var11;
      }

      @Nullable
      Object put(Object var1, int var2, Object var3, boolean var4) {
         this.lock();
         long var5 = this.map.ticker.read();
         this.preWriteCleanup(var5);
         int var7 = this.count + 1;
         if (var7 > this.threshold) {
            this.expand();
            var7 = this.count + 1;
         }

         AtomicReferenceArray var8 = this.table;
         int var9 = var2 & var8.length() - 1;
         LocalCache.ReferenceEntry var10 = (LocalCache.ReferenceEntry)var8.get(var9);

         LocalCache.ReferenceEntry var11;
         Object var12;
         for(var11 = var10; var11 != null; var11 = var11.getNext()) {
            var12 = var11.getKey();
            if (var11.getHash() == var2 && var12 != null && this.map.keyEquivalence.equivalent(var1, var12)) {
               LocalCache.ValueReference var13 = var11.getValueReference();
               Object var14 = var13.get();
               if (var14 == null) {
                  ++this.modCount;
                  if (var13.isActive()) {
                     this.enqueueNotification(var1, var2, var13, RemovalCause.COLLECTED);
                     this.setValue(var11, var1, var3, var5);
                     var7 = this.count;
                  } else {
                     this.setValue(var11, var1, var3, var5);
                     var7 = this.count + 1;
                  }

                  this.count = var7;
                  this.evictEntries();
                  Object var15 = null;
                  this.unlock();
                  this.postWriteCleanup();
                  return var15;
               }

               if (var4) {
                  this.recordLockedRead(var11, var5);
                  this.unlock();
                  this.postWriteCleanup();
                  return var14;
               }

               ++this.modCount;
               this.enqueueNotification(var1, var2, var13, RemovalCause.REPLACED);
               this.setValue(var11, var1, var3, var5);
               this.evictEntries();
               this.unlock();
               this.postWriteCleanup();
               return var14;
            }
         }

         ++this.modCount;
         var11 = this.newEntry(var1, var2, var10);
         this.setValue(var11, var1, var3, var5);
         var8.set(var9, var11);
         var7 = this.count + 1;
         this.count = var7;
         this.evictEntries();
         var12 = null;
         this.unlock();
         this.postWriteCleanup();
         return var12;
      }

      @GuardedBy("Segment.this")
      void expand() {
         AtomicReferenceArray var1 = this.table;
         int var2 = var1.length();
         if (var2 < 1073741824) {
            int var3 = this.count;
            AtomicReferenceArray var4 = this.newEntryArray(var2 << 1);
            this.threshold = var4.length() * 3 / 4;
            int var5 = var4.length() - 1;

            for(int var6 = 0; var6 < var2; ++var6) {
               LocalCache.ReferenceEntry var7 = (LocalCache.ReferenceEntry)var1.get(var6);
               if (var7 != null) {
                  LocalCache.ReferenceEntry var8 = var7.getNext();
                  int var9 = var7.getHash() & var5;
                  if (var8 == null) {
                     var4.set(var9, var7);
                  } else {
                     LocalCache.ReferenceEntry var10 = var7;
                     int var11 = var9;

                     LocalCache.ReferenceEntry var12;
                     int var13;
                     for(var12 = var8; var12 != null; var12 = var12.getNext()) {
                        var13 = var12.getHash() & var5;
                        if (var13 != var11) {
                           var11 = var13;
                           var10 = var12;
                        }
                     }

                     var4.set(var11, var10);

                     for(var12 = var7; var12 != var10; var12 = var12.getNext()) {
                        var13 = var12.getHash() & var5;
                        LocalCache.ReferenceEntry var14 = (LocalCache.ReferenceEntry)var4.get(var13);
                        LocalCache.ReferenceEntry var15 = this.copyEntry(var12, var14);
                        if (var15 != null) {
                           var4.set(var13, var15);
                        } else {
                           this.removeCollectedEntry(var12);
                           --var3;
                        }
                     }
                  }
               }
            }

            this.table = var4;
            this.count = var3;
         }
      }

      boolean replace(Object var1, int var2, Object var3, Object var4) {
         this.lock();
         long var5 = this.map.ticker.read();
         this.preWriteCleanup(var5);
         AtomicReferenceArray var7 = this.table;
         int var8 = var2 & var7.length() - 1;
         LocalCache.ReferenceEntry var9 = (LocalCache.ReferenceEntry)var7.get(var8);

         for(LocalCache.ReferenceEntry var10 = var9; var10 != null; var10 = var10.getNext()) {
            Object var11 = var10.getKey();
            if (var10.getHash() == var2 && var11 != null && this.map.keyEquivalence.equivalent(var1, var11)) {
               LocalCache.ValueReference var12 = var10.getValueReference();
               Object var13 = var12.get();
               boolean var14;
               if (var13 == null) {
                  if (var12.isActive()) {
                     int var18 = this.count - 1;
                     ++this.modCount;
                     LocalCache.ReferenceEntry var15 = this.removeValueFromChain(var9, var10, var11, var2, var12, RemovalCause.COLLECTED);
                     var18 = this.count - 1;
                     var7.set(var8, var15);
                     this.count = var18;
                  }

                  var14 = false;
                  this.unlock();
                  this.postWriteCleanup();
                  return var14;
               }

               if (this.map.valueEquivalence.equivalent(var3, var13)) {
                  ++this.modCount;
                  this.enqueueNotification(var1, var2, var12, RemovalCause.REPLACED);
                  this.setValue(var10, var1, var4, var5);
                  this.evictEntries();
                  var14 = true;
                  this.unlock();
                  this.postWriteCleanup();
                  return var14;
               }

               this.recordLockedRead(var10, var5);
               var14 = false;
               this.unlock();
               this.postWriteCleanup();
               return var14;
            }
         }

         boolean var17 = false;
         this.unlock();
         this.postWriteCleanup();
         return var17;
      }

      @Nullable
      Object replace(Object var1, int var2, Object var3) {
         this.lock();
         long var4 = this.map.ticker.read();
         this.preWriteCleanup(var4);
         AtomicReferenceArray var6 = this.table;
         int var7 = var2 & var6.length() - 1;
         LocalCache.ReferenceEntry var8 = (LocalCache.ReferenceEntry)var6.get(var7);

         LocalCache.ReferenceEntry var9;
         for(var9 = var8; var9 != null; var9 = var9.getNext()) {
            Object var10 = var9.getKey();
            if (var9.getHash() == var2 && var10 != null && this.map.keyEquivalence.equivalent(var1, var10)) {
               LocalCache.ValueReference var11 = var9.getValueReference();
               Object var12 = var11.get();
               if (var12 == null) {
                  if (var11.isActive()) {
                     int var13 = this.count - 1;
                     ++this.modCount;
                     LocalCache.ReferenceEntry var14 = this.removeValueFromChain(var8, var9, var10, var2, var11, RemovalCause.COLLECTED);
                     var13 = this.count - 1;
                     var6.set(var7, var14);
                     this.count = var13;
                  }

                  Object var16 = null;
                  this.unlock();
                  this.postWriteCleanup();
                  return var16;
               }

               ++this.modCount;
               this.enqueueNotification(var1, var2, var11, RemovalCause.REPLACED);
               this.setValue(var9, var1, var3, var4);
               this.evictEntries();
               this.unlock();
               this.postWriteCleanup();
               return var12;
            }
         }

         var9 = null;
         this.unlock();
         this.postWriteCleanup();
         return var9;
      }

      @Nullable
      Object remove(Object var1, int var2) {
         this.lock();
         long var3 = this.map.ticker.read();
         this.preWriteCleanup(var3);
         int var5 = this.count - 1;
         AtomicReferenceArray var6 = this.table;
         int var7 = var2 & var6.length() - 1;
         LocalCache.ReferenceEntry var8 = (LocalCache.ReferenceEntry)var6.get(var7);

         LocalCache.ReferenceEntry var9;
         for(var9 = var8; var9 != null; var9 = var9.getNext()) {
            Object var10 = var9.getKey();
            if (var9.getHash() == var2 && var10 != null && this.map.keyEquivalence.equivalent(var1, var10)) {
               LocalCache.ValueReference var11 = var9.getValueReference();
               Object var12 = var11.get();
               RemovalCause var13;
               LocalCache.ReferenceEntry var14;
               if (var12 != null) {
                  var13 = RemovalCause.EXPLICIT;
               } else {
                  if (!var11.isActive()) {
                     var14 = null;
                     this.unlock();
                     this.postWriteCleanup();
                     return var14;
                  }

                  var13 = RemovalCause.COLLECTED;
               }

               ++this.modCount;
               var14 = this.removeValueFromChain(var8, var9, var10, var2, var11, var13);
               var5 = this.count - 1;
               var6.set(var7, var14);
               this.count = var5;
               this.unlock();
               this.postWriteCleanup();
               return var12;
            }
         }

         var9 = null;
         this.unlock();
         this.postWriteCleanup();
         return var9;
      }

      boolean storeLoadedValue(Object var1, int var2, LocalCache.LoadingValueReference var3, Object var4) {
         this.lock();
         long var5 = this.map.ticker.read();
         this.preWriteCleanup(var5);
         int var7 = this.count + 1;
         if (var7 > this.threshold) {
            this.expand();
            var7 = this.count + 1;
         }

         AtomicReferenceArray var8 = this.table;
         int var9 = var2 & var8.length() - 1;
         LocalCache.ReferenceEntry var10 = (LocalCache.ReferenceEntry)var8.get(var9);

         LocalCache.ReferenceEntry var11;
         for(var11 = var10; var11 != null; var11 = var11.getNext()) {
            Object var12 = var11.getKey();
            if (var11.getHash() == var2 && var12 != null && this.map.keyEquivalence.equivalent(var1, var12)) {
               LocalCache.ValueReference var13 = var11.getValueReference();
               Object var14 = var13.get();
               boolean var15;
               if (var3 == var13 || var14 == null && var13 != LocalCache.UNSET) {
                  ++this.modCount;
                  if (var3.isActive()) {
                     RemovalCause var19 = var14 == null ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
                     this.enqueueNotification(var1, var2, var3, var19);
                     --var7;
                  }

                  this.setValue(var11, var1, var4, var5);
                  this.count = var7;
                  this.evictEntries();
                  var15 = true;
                  this.unlock();
                  this.postWriteCleanup();
                  return var15;
               }

               LocalCache.WeightedStrongValueReference var18 = new LocalCache.WeightedStrongValueReference(var4, 0);
               this.enqueueNotification(var1, var2, var18, RemovalCause.REPLACED);
               var15 = false;
               this.unlock();
               this.postWriteCleanup();
               return var15;
            }
         }

         ++this.modCount;
         var11 = this.newEntry(var1, var2, var10);
         this.setValue(var11, var1, var4, var5);
         var8.set(var9, var11);
         this.count = var7;
         this.evictEntries();
         boolean var17 = true;
         this.unlock();
         this.postWriteCleanup();
         return var17;
      }

      boolean remove(Object var1, int var2, Object var3) {
         this.lock();
         long var4 = this.map.ticker.read();
         this.preWriteCleanup(var4);
         int var6 = this.count - 1;
         AtomicReferenceArray var7 = this.table;
         int var8 = var2 & var7.length() - 1;
         LocalCache.ReferenceEntry var9 = (LocalCache.ReferenceEntry)var7.get(var8);

         for(LocalCache.ReferenceEntry var10 = var9; var10 != null; var10 = var10.getNext()) {
            Object var11 = var10.getKey();
            if (var10.getHash() == var2 && var11 != null && this.map.keyEquivalence.equivalent(var1, var11)) {
               LocalCache.ValueReference var12 = var10.getValueReference();
               Object var13 = var12.get();
               RemovalCause var14;
               if (this.map.valueEquivalence.equivalent(var3, var13)) {
                  var14 = RemovalCause.EXPLICIT;
               } else {
                  if (var13 != null || !var12.isActive()) {
                     boolean var15 = false;
                     this.unlock();
                     this.postWriteCleanup();
                     return var15;
                  }

                  var14 = RemovalCause.COLLECTED;
               }

               ++this.modCount;
               LocalCache.ReferenceEntry var19 = this.removeValueFromChain(var9, var10, var11, var2, var12, var14);
               var6 = this.count - 1;
               var7.set(var8, var19);
               this.count = var6;
               boolean var16 = var14 == RemovalCause.EXPLICIT;
               this.unlock();
               this.postWriteCleanup();
               return var16;
            }
         }

         boolean var18 = false;
         this.unlock();
         this.postWriteCleanup();
         return var18;
      }

      void clear() {
         if (this.count != 0) {
            this.lock();
            AtomicReferenceArray var1 = this.table;

            int var2;
            for(var2 = 0; var2 < var1.length(); ++var2) {
               for(LocalCache.ReferenceEntry var3 = (LocalCache.ReferenceEntry)var1.get(var2); var3 != null; var3 = var3.getNext()) {
                  if (var3.getValueReference().isActive()) {
                     this.enqueueNotification(var3, RemovalCause.EXPLICIT);
                  }
               }
            }

            for(var2 = 0; var2 < var1.length(); ++var2) {
               var1.set(var2, (Object)null);
            }

            this.clearReferenceQueues();
            this.writeQueue.clear();
            this.accessQueue.clear();
            this.readCount.set(0);
            ++this.modCount;
            this.count = 0;
            this.unlock();
            this.postWriteCleanup();
         }

      }

      @Nullable
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry removeValueFromChain(LocalCache.ReferenceEntry var1, LocalCache.ReferenceEntry var2, @Nullable Object var3, int var4, LocalCache.ValueReference var5, RemovalCause var6) {
         this.enqueueNotification(var3, var4, var5, var6);
         this.writeQueue.remove(var2);
         this.accessQueue.remove(var2);
         if (var5.isLoading()) {
            var5.notifyNewValue((Object)null);
            return var1;
         } else {
            return this.removeEntryFromChain(var1, var2);
         }
      }

      @Nullable
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry removeEntryFromChain(LocalCache.ReferenceEntry var1, LocalCache.ReferenceEntry var2) {
         int var3 = this.count;
         LocalCache.ReferenceEntry var4 = var2.getNext();

         for(LocalCache.ReferenceEntry var5 = var1; var5 != var2; var5 = var5.getNext()) {
            LocalCache.ReferenceEntry var6 = this.copyEntry(var5, var4);
            if (var6 != null) {
               var4 = var6;
            } else {
               this.removeCollectedEntry(var5);
               --var3;
            }
         }

         this.count = var3;
         return var4;
      }

      @GuardedBy("Segment.this")
      void removeCollectedEntry(LocalCache.ReferenceEntry var1) {
         this.enqueueNotification(var1, RemovalCause.COLLECTED);
         this.writeQueue.remove(var1);
         this.accessQueue.remove(var1);
      }

      boolean reclaimKey(LocalCache.ReferenceEntry var1, int var2) {
         this.lock();
         int var3 = this.count - 1;
         AtomicReferenceArray var4 = this.table;
         int var5 = var2 & var4.length() - 1;
         LocalCache.ReferenceEntry var6 = (LocalCache.ReferenceEntry)var4.get(var5);

         for(LocalCache.ReferenceEntry var7 = var6; var7 != null; var7 = var7.getNext()) {
            if (var7 == var1) {
               ++this.modCount;
               LocalCache.ReferenceEntry var8 = this.removeValueFromChain(var6, var7, var7.getKey(), var2, var7.getValueReference(), RemovalCause.COLLECTED);
               var3 = this.count - 1;
               var4.set(var5, var8);
               this.count = var3;
               boolean var9 = true;
               this.unlock();
               this.postWriteCleanup();
               return var9;
            }
         }

         boolean var11 = false;
         this.unlock();
         this.postWriteCleanup();
         return var11;
      }

      boolean reclaimValue(Object var1, int var2, LocalCache.ValueReference var3) {
         this.lock();
         int var4 = this.count - 1;
         AtomicReferenceArray var5 = this.table;
         int var6 = var2 & var5.length() - 1;
         LocalCache.ReferenceEntry var7 = (LocalCache.ReferenceEntry)var5.get(var6);

         for(LocalCache.ReferenceEntry var8 = var7; var8 != null; var8 = var8.getNext()) {
            Object var9 = var8.getKey();
            if (var8.getHash() == var2 && var9 != null && this.map.keyEquivalence.equivalent(var1, var9)) {
               LocalCache.ValueReference var10 = var8.getValueReference();
               if (var10 == var3) {
                  ++this.modCount;
                  LocalCache.ReferenceEntry var15 = this.removeValueFromChain(var7, var8, var9, var2, var3, RemovalCause.COLLECTED);
                  var4 = this.count - 1;
                  var5.set(var6, var15);
                  this.count = var4;
                  boolean var12 = true;
                  this.unlock();
                  if (!this.isHeldByCurrentThread()) {
                     this.postWriteCleanup();
                  }

                  return var12;
               }

               boolean var11 = false;
               this.unlock();
               if (!this.isHeldByCurrentThread()) {
                  this.postWriteCleanup();
               }

               return var11;
            }
         }

         boolean var14 = false;
         this.unlock();
         if (!this.isHeldByCurrentThread()) {
            this.postWriteCleanup();
         }

         return var14;
      }

      boolean removeLoadingValue(Object var1, int var2, LocalCache.LoadingValueReference var3) {
         this.lock();
         AtomicReferenceArray var4 = this.table;
         int var5 = var2 & var4.length() - 1;
         LocalCache.ReferenceEntry var6 = (LocalCache.ReferenceEntry)var4.get(var5);

         for(LocalCache.ReferenceEntry var7 = var6; var7 != null; var7 = var7.getNext()) {
            Object var8 = var7.getKey();
            if (var7.getHash() == var2 && var8 != null && this.map.keyEquivalence.equivalent(var1, var8)) {
               LocalCache.ValueReference var9 = var7.getValueReference();
               boolean var10;
               if (var9 == var3) {
                  if (var3.isActive()) {
                     var7.setValueReference(var3.getOldValue());
                  } else {
                     LocalCache.ReferenceEntry var12 = this.removeEntryFromChain(var6, var7);
                     var4.set(var5, var12);
                  }

                  var10 = true;
                  this.unlock();
                  this.postWriteCleanup();
                  return var10;
               }

               var10 = false;
               this.unlock();
               this.postWriteCleanup();
               return var10;
            }
         }

         boolean var13 = false;
         this.unlock();
         this.postWriteCleanup();
         return var13;
      }

      void postReadCleanup() {
         if ((this.readCount.incrementAndGet() & 63) == 0) {
            this.cleanUp();
         }

      }

      @GuardedBy("Segment.this")
      void preWriteCleanup(long var1) {
         this.runLockedCleanup(var1);
      }

      void postWriteCleanup() {
         this.runUnlockedCleanup();
      }

      void cleanUp() {
         long var1 = this.map.ticker.read();
         this.runLockedCleanup(var1);
         this.runUnlockedCleanup();
      }

      void runLockedCleanup(long var1) {
         if (this.tryLock()) {
            this.drainReferenceQueues();
            this.expireEntries(var1);
            this.readCount.set(0);
            this.unlock();
         }

      }

      void runUnlockedCleanup() {
         if (!this.isHeldByCurrentThread()) {
            this.map.processPendingNotifications();
         }

      }
   }

   static final class WeightedStrongValueReference extends LocalCache.StrongValueReference {
      final int weight;

      WeightedStrongValueReference(Object var1, int var2) {
         super(var1);
         this.weight = var2;
      }

      public int getWeight() {
         return this.weight;
      }
   }

   static final class WeightedSoftValueReference extends LocalCache.SoftValueReference {
      final int weight;

      WeightedSoftValueReference(ReferenceQueue var1, Object var2, LocalCache.ReferenceEntry var3, int var4) {
         super(var1, var2, var3);
         this.weight = var4;
      }

      public int getWeight() {
         return this.weight;
      }

      public LocalCache.ValueReference copyFor(ReferenceQueue var1, Object var2, LocalCache.ReferenceEntry var3) {
         return new LocalCache.WeightedSoftValueReference(var1, var2, var3, this.weight);
      }
   }

   static final class WeightedWeakValueReference extends LocalCache.WeakValueReference {
      final int weight;

      WeightedWeakValueReference(ReferenceQueue var1, Object var2, LocalCache.ReferenceEntry var3, int var4) {
         super(var1, var2, var3);
         this.weight = var4;
      }

      public int getWeight() {
         return this.weight;
      }

      public LocalCache.ValueReference copyFor(ReferenceQueue var1, Object var2, LocalCache.ReferenceEntry var3) {
         return new LocalCache.WeightedWeakValueReference(var1, var2, var3, this.weight);
      }
   }

   static class StrongValueReference implements LocalCache.ValueReference {
      final Object referent;

      StrongValueReference(Object var1) {
         this.referent = var1;
      }

      public Object get() {
         return this.referent;
      }

      public int getWeight() {
         return 1;
      }

      public LocalCache.ReferenceEntry getEntry() {
         return null;
      }

      public LocalCache.ValueReference copyFor(ReferenceQueue var1, Object var2, LocalCache.ReferenceEntry var3) {
         return this;
      }

      public boolean isLoading() {
         return false;
      }

      public boolean isActive() {
         return true;
      }

      public Object waitForValue() {
         return this.get();
      }

      public void notifyNewValue(Object var1) {
      }
   }

   static class SoftValueReference extends SoftReference implements LocalCache.ValueReference {
      final LocalCache.ReferenceEntry entry;

      SoftValueReference(ReferenceQueue var1, Object var2, LocalCache.ReferenceEntry var3) {
         super(var2, var1);
         this.entry = var3;
      }

      public int getWeight() {
         return 1;
      }

      public LocalCache.ReferenceEntry getEntry() {
         return this.entry;
      }

      public void notifyNewValue(Object var1) {
      }

      public LocalCache.ValueReference copyFor(ReferenceQueue var1, Object var2, LocalCache.ReferenceEntry var3) {
         return new LocalCache.SoftValueReference(var1, var2, var3);
      }

      public boolean isLoading() {
         return false;
      }

      public boolean isActive() {
         return true;
      }

      public Object waitForValue() {
         return this.get();
      }
   }

   static class WeakValueReference extends WeakReference implements LocalCache.ValueReference {
      final LocalCache.ReferenceEntry entry;

      WeakValueReference(ReferenceQueue var1, Object var2, LocalCache.ReferenceEntry var3) {
         super(var2, var1);
         this.entry = var3;
      }

      public int getWeight() {
         return 1;
      }

      public LocalCache.ReferenceEntry getEntry() {
         return this.entry;
      }

      public void notifyNewValue(Object var1) {
      }

      public LocalCache.ValueReference copyFor(ReferenceQueue var1, Object var2, LocalCache.ReferenceEntry var3) {
         return new LocalCache.WeakValueReference(var1, var2, var3);
      }

      public boolean isLoading() {
         return false;
      }

      public boolean isActive() {
         return true;
      }

      public Object waitForValue() {
         return this.get();
      }
   }

   static final class WeakAccessWriteEntry extends LocalCache.WeakEntry {
      volatile long accessTime = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry nextAccess = LocalCache.nullEntry();
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry previousAccess = LocalCache.nullEntry();
      volatile long writeTime = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry nextWrite = LocalCache.nullEntry();
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry previousWrite = LocalCache.nullEntry();

      WeakAccessWriteEntry(ReferenceQueue var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
         super(var1, var2, var3, var4);
      }

      public long getAccessTime() {
         return this.accessTime;
      }

      public void setAccessTime(long var1) {
         this.accessTime = var1;
      }

      public LocalCache.ReferenceEntry getNextInAccessQueue() {
         return this.nextAccess;
      }

      public void setNextInAccessQueue(LocalCache.ReferenceEntry var1) {
         this.nextAccess = var1;
      }

      public LocalCache.ReferenceEntry getPreviousInAccessQueue() {
         return this.previousAccess;
      }

      public void setPreviousInAccessQueue(LocalCache.ReferenceEntry var1) {
         this.previousAccess = var1;
      }

      public long getWriteTime() {
         return this.writeTime;
      }

      public void setWriteTime(long var1) {
         this.writeTime = var1;
      }

      public LocalCache.ReferenceEntry getNextInWriteQueue() {
         return this.nextWrite;
      }

      public void setNextInWriteQueue(LocalCache.ReferenceEntry var1) {
         this.nextWrite = var1;
      }

      public LocalCache.ReferenceEntry getPreviousInWriteQueue() {
         return this.previousWrite;
      }

      public void setPreviousInWriteQueue(LocalCache.ReferenceEntry var1) {
         this.previousWrite = var1;
      }
   }

   static final class WeakWriteEntry extends LocalCache.WeakEntry {
      volatile long writeTime = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry nextWrite = LocalCache.nullEntry();
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry previousWrite = LocalCache.nullEntry();

      WeakWriteEntry(ReferenceQueue var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
         super(var1, var2, var3, var4);
      }

      public long getWriteTime() {
         return this.writeTime;
      }

      public void setWriteTime(long var1) {
         this.writeTime = var1;
      }

      public LocalCache.ReferenceEntry getNextInWriteQueue() {
         return this.nextWrite;
      }

      public void setNextInWriteQueue(LocalCache.ReferenceEntry var1) {
         this.nextWrite = var1;
      }

      public LocalCache.ReferenceEntry getPreviousInWriteQueue() {
         return this.previousWrite;
      }

      public void setPreviousInWriteQueue(LocalCache.ReferenceEntry var1) {
         this.previousWrite = var1;
      }
   }

   static final class WeakAccessEntry extends LocalCache.WeakEntry {
      volatile long accessTime = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry nextAccess = LocalCache.nullEntry();
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry previousAccess = LocalCache.nullEntry();

      WeakAccessEntry(ReferenceQueue var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
         super(var1, var2, var3, var4);
      }

      public long getAccessTime() {
         return this.accessTime;
      }

      public void setAccessTime(long var1) {
         this.accessTime = var1;
      }

      public LocalCache.ReferenceEntry getNextInAccessQueue() {
         return this.nextAccess;
      }

      public void setNextInAccessQueue(LocalCache.ReferenceEntry var1) {
         this.nextAccess = var1;
      }

      public LocalCache.ReferenceEntry getPreviousInAccessQueue() {
         return this.previousAccess;
      }

      public void setPreviousInAccessQueue(LocalCache.ReferenceEntry var1) {
         this.previousAccess = var1;
      }
   }

   static class WeakEntry extends WeakReference implements LocalCache.ReferenceEntry {
      final int hash;
      final LocalCache.ReferenceEntry next;
      volatile LocalCache.ValueReference valueReference = LocalCache.unset();

      WeakEntry(ReferenceQueue var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
         super(var2, var1);
         this.hash = var3;
         this.next = var4;
      }

      public Object getKey() {
         return this.get();
      }

      public long getAccessTime() {
         throw new UnsupportedOperationException();
      }

      public void setAccessTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ReferenceEntry getNextInAccessQueue() {
         throw new UnsupportedOperationException();
      }

      public void setNextInAccessQueue(LocalCache.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ReferenceEntry getPreviousInAccessQueue() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousInAccessQueue(LocalCache.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public long getWriteTime() {
         throw new UnsupportedOperationException();
      }

      public void setWriteTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ReferenceEntry getNextInWriteQueue() {
         throw new UnsupportedOperationException();
      }

      public void setNextInWriteQueue(LocalCache.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ReferenceEntry getPreviousInWriteQueue() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousInWriteQueue(LocalCache.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ValueReference getValueReference() {
         return this.valueReference;
      }

      public void setValueReference(LocalCache.ValueReference var1) {
         this.valueReference = var1;
      }

      public int getHash() {
         return this.hash;
      }

      public LocalCache.ReferenceEntry getNext() {
         return this.next;
      }
   }

   static final class StrongAccessWriteEntry extends LocalCache.StrongEntry {
      volatile long accessTime = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry nextAccess = LocalCache.nullEntry();
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry previousAccess = LocalCache.nullEntry();
      volatile long writeTime = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry nextWrite = LocalCache.nullEntry();
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry previousWrite = LocalCache.nullEntry();

      StrongAccessWriteEntry(Object var1, int var2, @Nullable LocalCache.ReferenceEntry var3) {
         super(var1, var2, var3);
      }

      public long getAccessTime() {
         return this.accessTime;
      }

      public void setAccessTime(long var1) {
         this.accessTime = var1;
      }

      public LocalCache.ReferenceEntry getNextInAccessQueue() {
         return this.nextAccess;
      }

      public void setNextInAccessQueue(LocalCache.ReferenceEntry var1) {
         this.nextAccess = var1;
      }

      public LocalCache.ReferenceEntry getPreviousInAccessQueue() {
         return this.previousAccess;
      }

      public void setPreviousInAccessQueue(LocalCache.ReferenceEntry var1) {
         this.previousAccess = var1;
      }

      public long getWriteTime() {
         return this.writeTime;
      }

      public void setWriteTime(long var1) {
         this.writeTime = var1;
      }

      public LocalCache.ReferenceEntry getNextInWriteQueue() {
         return this.nextWrite;
      }

      public void setNextInWriteQueue(LocalCache.ReferenceEntry var1) {
         this.nextWrite = var1;
      }

      public LocalCache.ReferenceEntry getPreviousInWriteQueue() {
         return this.previousWrite;
      }

      public void setPreviousInWriteQueue(LocalCache.ReferenceEntry var1) {
         this.previousWrite = var1;
      }
   }

   static final class StrongWriteEntry extends LocalCache.StrongEntry {
      volatile long writeTime = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry nextWrite = LocalCache.nullEntry();
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry previousWrite = LocalCache.nullEntry();

      StrongWriteEntry(Object var1, int var2, @Nullable LocalCache.ReferenceEntry var3) {
         super(var1, var2, var3);
      }

      public long getWriteTime() {
         return this.writeTime;
      }

      public void setWriteTime(long var1) {
         this.writeTime = var1;
      }

      public LocalCache.ReferenceEntry getNextInWriteQueue() {
         return this.nextWrite;
      }

      public void setNextInWriteQueue(LocalCache.ReferenceEntry var1) {
         this.nextWrite = var1;
      }

      public LocalCache.ReferenceEntry getPreviousInWriteQueue() {
         return this.previousWrite;
      }

      public void setPreviousInWriteQueue(LocalCache.ReferenceEntry var1) {
         this.previousWrite = var1;
      }
   }

   static final class StrongAccessEntry extends LocalCache.StrongEntry {
      volatile long accessTime = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry nextAccess = LocalCache.nullEntry();
      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry previousAccess = LocalCache.nullEntry();

      StrongAccessEntry(Object var1, int var2, @Nullable LocalCache.ReferenceEntry var3) {
         super(var1, var2, var3);
      }

      public long getAccessTime() {
         return this.accessTime;
      }

      public void setAccessTime(long var1) {
         this.accessTime = var1;
      }

      public LocalCache.ReferenceEntry getNextInAccessQueue() {
         return this.nextAccess;
      }

      public void setNextInAccessQueue(LocalCache.ReferenceEntry var1) {
         this.nextAccess = var1;
      }

      public LocalCache.ReferenceEntry getPreviousInAccessQueue() {
         return this.previousAccess;
      }

      public void setPreviousInAccessQueue(LocalCache.ReferenceEntry var1) {
         this.previousAccess = var1;
      }
   }

   static class StrongEntry extends LocalCache.AbstractReferenceEntry {
      final Object key;
      final int hash;
      final LocalCache.ReferenceEntry next;
      volatile LocalCache.ValueReference valueReference = LocalCache.unset();

      StrongEntry(Object var1, int var2, @Nullable LocalCache.ReferenceEntry var3) {
         this.key = var1;
         this.hash = var2;
         this.next = var3;
      }

      public Object getKey() {
         return this.key;
      }

      public LocalCache.ValueReference getValueReference() {
         return this.valueReference;
      }

      public void setValueReference(LocalCache.ValueReference var1) {
         this.valueReference = var1;
      }

      public int getHash() {
         return this.hash;
      }

      public LocalCache.ReferenceEntry getNext() {
         return this.next;
      }
   }

   abstract static class AbstractReferenceEntry implements LocalCache.ReferenceEntry {
      public LocalCache.ValueReference getValueReference() {
         throw new UnsupportedOperationException();
      }

      public void setValueReference(LocalCache.ValueReference var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ReferenceEntry getNext() {
         throw new UnsupportedOperationException();
      }

      public int getHash() {
         throw new UnsupportedOperationException();
      }

      public Object getKey() {
         throw new UnsupportedOperationException();
      }

      public long getAccessTime() {
         throw new UnsupportedOperationException();
      }

      public void setAccessTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ReferenceEntry getNextInAccessQueue() {
         throw new UnsupportedOperationException();
      }

      public void setNextInAccessQueue(LocalCache.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ReferenceEntry getPreviousInAccessQueue() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousInAccessQueue(LocalCache.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public long getWriteTime() {
         throw new UnsupportedOperationException();
      }

      public void setWriteTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ReferenceEntry getNextInWriteQueue() {
         throw new UnsupportedOperationException();
      }

      public void setNextInWriteQueue(LocalCache.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public LocalCache.ReferenceEntry getPreviousInWriteQueue() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousInWriteQueue(LocalCache.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }
   }

   private static enum NullEntry implements LocalCache.ReferenceEntry {
      INSTANCE;

      private static final LocalCache.NullEntry[] $VALUES = new LocalCache.NullEntry[]{INSTANCE};

      public LocalCache.ValueReference getValueReference() {
         return null;
      }

      public void setValueReference(LocalCache.ValueReference var1) {
      }

      public LocalCache.ReferenceEntry getNext() {
         return null;
      }

      public int getHash() {
         return 0;
      }

      public Object getKey() {
         return null;
      }

      public long getAccessTime() {
         return 0L;
      }

      public void setAccessTime(long var1) {
      }

      public LocalCache.ReferenceEntry getNextInAccessQueue() {
         return this;
      }

      public void setNextInAccessQueue(LocalCache.ReferenceEntry var1) {
      }

      public LocalCache.ReferenceEntry getPreviousInAccessQueue() {
         return this;
      }

      public void setPreviousInAccessQueue(LocalCache.ReferenceEntry var1) {
      }

      public long getWriteTime() {
         return 0L;
      }

      public void setWriteTime(long var1) {
      }

      public LocalCache.ReferenceEntry getNextInWriteQueue() {
         return this;
      }

      public void setNextInWriteQueue(LocalCache.ReferenceEntry var1) {
      }

      public LocalCache.ReferenceEntry getPreviousInWriteQueue() {
         return this;
      }

      public void setPreviousInWriteQueue(LocalCache.ReferenceEntry var1) {
      }
   }

   interface ReferenceEntry {
      LocalCache.ValueReference getValueReference();

      void setValueReference(LocalCache.ValueReference var1);

      @Nullable
      LocalCache.ReferenceEntry getNext();

      int getHash();

      @Nullable
      Object getKey();

      long getAccessTime();

      void setAccessTime(long var1);

      LocalCache.ReferenceEntry getNextInAccessQueue();

      void setNextInAccessQueue(LocalCache.ReferenceEntry var1);

      LocalCache.ReferenceEntry getPreviousInAccessQueue();

      void setPreviousInAccessQueue(LocalCache.ReferenceEntry var1);

      long getWriteTime();

      void setWriteTime(long var1);

      LocalCache.ReferenceEntry getNextInWriteQueue();

      void setNextInWriteQueue(LocalCache.ReferenceEntry var1);

      LocalCache.ReferenceEntry getPreviousInWriteQueue();

      void setPreviousInWriteQueue(LocalCache.ReferenceEntry var1);
   }

   interface ValueReference {
      @Nullable
      Object get();

      Object waitForValue() throws ExecutionException;

      int getWeight();

      @Nullable
      LocalCache.ReferenceEntry getEntry();

      LocalCache.ValueReference copyFor(ReferenceQueue var1, @Nullable Object var2, LocalCache.ReferenceEntry var3);

      void notifyNewValue(@Nullable Object var1);

      boolean isLoading();

      boolean isActive();
   }

   static enum EntryFactory {
      STRONG {
         LocalCache.ReferenceEntry newEntry(LocalCache.Segment var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
            return new LocalCache.StrongEntry(var2, var3, var4);
         }
      },
      STRONG_ACCESS {
         LocalCache.ReferenceEntry newEntry(LocalCache.Segment var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
            return new LocalCache.StrongAccessEntry(var2, var3, var4);
         }

         LocalCache.ReferenceEntry copyEntry(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, LocalCache.ReferenceEntry var3) {
            LocalCache.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyAccessEntry(var2, var4);
            return var4;
         }
      },
      STRONG_WRITE {
         LocalCache.ReferenceEntry newEntry(LocalCache.Segment var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
            return new LocalCache.StrongWriteEntry(var2, var3, var4);
         }

         LocalCache.ReferenceEntry copyEntry(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, LocalCache.ReferenceEntry var3) {
            LocalCache.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyWriteEntry(var2, var4);
            return var4;
         }
      },
      STRONG_ACCESS_WRITE {
         LocalCache.ReferenceEntry newEntry(LocalCache.Segment var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
            return new LocalCache.StrongAccessWriteEntry(var2, var3, var4);
         }

         LocalCache.ReferenceEntry copyEntry(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, LocalCache.ReferenceEntry var3) {
            LocalCache.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyAccessEntry(var2, var4);
            this.copyWriteEntry(var2, var4);
            return var4;
         }
      },
      WEAK {
         LocalCache.ReferenceEntry newEntry(LocalCache.Segment var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
            return new LocalCache.WeakEntry(var1.keyReferenceQueue, var2, var3, var4);
         }
      },
      WEAK_ACCESS {
         LocalCache.ReferenceEntry newEntry(LocalCache.Segment var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
            return new LocalCache.WeakAccessEntry(var1.keyReferenceQueue, var2, var3, var4);
         }

         LocalCache.ReferenceEntry copyEntry(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, LocalCache.ReferenceEntry var3) {
            LocalCache.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyAccessEntry(var2, var4);
            return var4;
         }
      },
      WEAK_WRITE {
         LocalCache.ReferenceEntry newEntry(LocalCache.Segment var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
            return new LocalCache.WeakWriteEntry(var1.keyReferenceQueue, var2, var3, var4);
         }

         LocalCache.ReferenceEntry copyEntry(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, LocalCache.ReferenceEntry var3) {
            LocalCache.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyWriteEntry(var2, var4);
            return var4;
         }
      },
      WEAK_ACCESS_WRITE {
         LocalCache.ReferenceEntry newEntry(LocalCache.Segment var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4) {
            return new LocalCache.WeakAccessWriteEntry(var1.keyReferenceQueue, var2, var3, var4);
         }

         LocalCache.ReferenceEntry copyEntry(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, LocalCache.ReferenceEntry var3) {
            LocalCache.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyAccessEntry(var2, var4);
            this.copyWriteEntry(var2, var4);
            return var4;
         }
      };

      static final int ACCESS_MASK = 1;
      static final int WRITE_MASK = 2;
      static final int WEAK_MASK = 4;
      static final LocalCache.EntryFactory[] factories = new LocalCache.EntryFactory[]{STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE};
      private static final LocalCache.EntryFactory[] $VALUES = new LocalCache.EntryFactory[]{STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE};

      private EntryFactory() {
      }

      static LocalCache.EntryFactory getFactory(LocalCache.Strength var0, boolean var1, boolean var2) {
         int var3 = (var0 == LocalCache.Strength.WEAK ? 4 : 0) | (var1 ? 1 : 0) | (var2 ? 2 : 0);
         return factories[var3];
      }

      abstract LocalCache.ReferenceEntry newEntry(LocalCache.Segment var1, Object var2, int var3, @Nullable LocalCache.ReferenceEntry var4);

      @GuardedBy("Segment.this")
      LocalCache.ReferenceEntry copyEntry(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, LocalCache.ReferenceEntry var3) {
         return this.newEntry(var1, var2.getKey(), var2.getHash(), var3);
      }

      @GuardedBy("Segment.this")
      void copyAccessEntry(LocalCache.ReferenceEntry var1, LocalCache.ReferenceEntry var2) {
         var2.setAccessTime(var1.getAccessTime());
         LocalCache.connectAccessOrder(var1.getPreviousInAccessQueue(), var2);
         LocalCache.connectAccessOrder(var2, var1.getNextInAccessQueue());
         LocalCache.nullifyAccessOrder(var1);
      }

      @GuardedBy("Segment.this")
      void copyWriteEntry(LocalCache.ReferenceEntry var1, LocalCache.ReferenceEntry var2) {
         var2.setWriteTime(var1.getWriteTime());
         LocalCache.connectWriteOrder(var1.getPreviousInWriteQueue(), var2);
         LocalCache.connectWriteOrder(var2, var1.getNextInWriteQueue());
         LocalCache.nullifyWriteOrder(var1);
      }

      EntryFactory(Object var3) {
         this();
      }
   }

   static enum Strength {
      STRONG {
         LocalCache.ValueReference referenceValue(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, Object var3, int var4) {
            return (LocalCache.ValueReference)(var4 == 1 ? new LocalCache.StrongValueReference(var3) : new LocalCache.WeightedStrongValueReference(var3, var4));
         }

         Equivalence defaultEquivalence() {
            return Equivalence.equals();
         }
      },
      SOFT {
         LocalCache.ValueReference referenceValue(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, Object var3, int var4) {
            return (LocalCache.ValueReference)(var4 == 1 ? new LocalCache.SoftValueReference(var1.valueReferenceQueue, var3, var2) : new LocalCache.WeightedSoftValueReference(var1.valueReferenceQueue, var3, var2, var4));
         }

         Equivalence defaultEquivalence() {
            return Equivalence.identity();
         }
      },
      WEAK {
         LocalCache.ValueReference referenceValue(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, Object var3, int var4) {
            return (LocalCache.ValueReference)(var4 == 1 ? new LocalCache.WeakValueReference(var1.valueReferenceQueue, var3, var2) : new LocalCache.WeightedWeakValueReference(var1.valueReferenceQueue, var3, var2, var4));
         }

         Equivalence defaultEquivalence() {
            return Equivalence.identity();
         }
      };

      private static final LocalCache.Strength[] $VALUES = new LocalCache.Strength[]{STRONG, SOFT, WEAK};

      private Strength() {
      }

      abstract LocalCache.ValueReference referenceValue(LocalCache.Segment var1, LocalCache.ReferenceEntry var2, Object var3, int var4);

      abstract Equivalence defaultEquivalence();

      Strength(Object var3) {
         this();
      }
   }
}
