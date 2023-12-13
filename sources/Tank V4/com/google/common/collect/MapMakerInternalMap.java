package com.google.common.collect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
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

class MapMakerInternalMap extends AbstractMap implements ConcurrentMap, Serializable {
   static final int MAXIMUM_CAPACITY = 1073741824;
   static final int MAX_SEGMENTS = 65536;
   static final int CONTAINS_VALUE_RETRIES = 3;
   static final int DRAIN_THRESHOLD = 63;
   static final int DRAIN_MAX = 16;
   static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
   private static final Logger logger = Logger.getLogger(MapMakerInternalMap.class.getName());
   final transient int segmentMask;
   final transient int segmentShift;
   final transient MapMakerInternalMap.Segment[] segments;
   final int concurrencyLevel;
   final Equivalence keyEquivalence;
   final Equivalence valueEquivalence;
   final MapMakerInternalMap.Strength keyStrength;
   final MapMakerInternalMap.Strength valueStrength;
   final int maximumSize;
   final long expireAfterAccessNanos;
   final long expireAfterWriteNanos;
   final Queue removalNotificationQueue;
   final MapMaker.RemovalListener removalListener;
   final transient MapMakerInternalMap.EntryFactory entryFactory;
   final Ticker ticker;
   static final MapMakerInternalMap.ValueReference UNSET = new MapMakerInternalMap.ValueReference() {
      public Object get() {
         return null;
      }

      public MapMakerInternalMap.ReferenceEntry getEntry() {
         return null;
      }

      public MapMakerInternalMap.ValueReference copyFor(ReferenceQueue var1, @Nullable Object var2, MapMakerInternalMap.ReferenceEntry var3) {
         return this;
      }

      public boolean isComputingReference() {
         return false;
      }

      public Object waitForValue() {
         return null;
      }

      public void clear(MapMakerInternalMap.ValueReference var1) {
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
   transient Set keySet;
   transient Collection values;
   transient Set entrySet;
   private static final long serialVersionUID = 5L;

   MapMakerInternalMap(MapMaker param1) {
      // $FF: Couldn't be decompiled
   }

   boolean usesKeyReferences() {
      return this.keyStrength != MapMakerInternalMap.Strength.STRONG;
   }

   boolean usesValueReferences() {
      return this.valueStrength != MapMakerInternalMap.Strength.STRONG;
   }

   static MapMakerInternalMap.ValueReference unset() {
      return UNSET;
   }

   static MapMakerInternalMap.ReferenceEntry nullEntry() {
      return MapMakerInternalMap.NullEntry.INSTANCE;
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
   MapMakerInternalMap.ReferenceEntry newEntry(Object var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry var3) {
      return this.segmentFor(var2).newEntry(var1, var2, var3);
   }

   @GuardedBy("Segment.this")
   @VisibleForTesting
   MapMakerInternalMap.ReferenceEntry copyEntry(MapMakerInternalMap.ReferenceEntry var1, MapMakerInternalMap.ReferenceEntry var2) {
      int var3 = var1.getHash();
      return this.segmentFor(var3).copyEntry(var1, var2);
   }

   @GuardedBy("Segment.this")
   @VisibleForTesting
   MapMakerInternalMap.ValueReference newValueReference(MapMakerInternalMap.ReferenceEntry var1, Object var2) {
      int var3 = var1.getHash();
      return this.valueStrength.referenceValue(this.segmentFor(var3), var1, var2);
   }

   int hash(Object var1) {
      int var2 = this.keyEquivalence.hash(var1);
      return rehash(var2);
   }

   void reclaimValue(MapMakerInternalMap.ValueReference var1) {
      MapMakerInternalMap.ReferenceEntry var2 = var1.getEntry();
      int var3 = var2.getHash();
      this.segmentFor(var3).reclaimValue(var2.getKey(), var3, var1);
   }

   void reclaimKey(MapMakerInternalMap.ReferenceEntry var1) {
      int var2 = var1.getHash();
      this.segmentFor(var2).reclaimKey(var1, var2);
   }

   @VisibleForTesting
   boolean isLive(MapMakerInternalMap.ReferenceEntry var1) {
      return this.segmentFor(var1.getHash()).getLiveValue(var1) != null;
   }

   MapMakerInternalMap.Segment segmentFor(int var1) {
      return this.segments[var1 >>> this.segmentShift & this.segmentMask];
   }

   MapMakerInternalMap.Segment createSegment(int var1, int var2) {
      return new MapMakerInternalMap.Segment(this, var1, var2);
   }

   Object getLiveValue(MapMakerInternalMap.ReferenceEntry var1) {
      if (var1.getKey() == null) {
         return null;
      } else {
         Object var2 = var1.getValueReference().get();
         if (var2 == null) {
            return null;
         } else {
            return this > 0 && this.isExpired(var1) ? null : var2;
         }
      }
   }

   boolean isExpired(MapMakerInternalMap.ReferenceEntry var1) {
      return this.isExpired(var1, this.ticker.read());
   }

   boolean isExpired(MapMakerInternalMap.ReferenceEntry var1, long var2) {
      return var2 - var1.getExpirationTime() > 0L;
   }

   @GuardedBy("Segment.this")
   static void connectExpirables(MapMakerInternalMap.ReferenceEntry var0, MapMakerInternalMap.ReferenceEntry var1) {
      var0.setNextExpirable(var1);
      var1.setPreviousExpirable(var0);
   }

   @GuardedBy("Segment.this")
   static void nullifyExpirable(MapMakerInternalMap.ReferenceEntry var0) {
      MapMakerInternalMap.ReferenceEntry var1 = nullEntry();
      var0.setNextExpirable(var1);
      var0.setPreviousExpirable(var1);
   }

   void processPendingNotifications() {
      MapMaker.RemovalNotification var1;
      while((var1 = (MapMaker.RemovalNotification)this.removalNotificationQueue.poll()) != null) {
         try {
            this.removalListener.onRemoval(var1);
         } catch (Exception var3) {
            logger.log(Level.WARNING, "Exception thrown by removal listener", var3);
         }
      }

   }

   @GuardedBy("Segment.this")
   static void connectEvictables(MapMakerInternalMap.ReferenceEntry var0, MapMakerInternalMap.ReferenceEntry var1) {
      var0.setNextEvictable(var1);
      var1.setPreviousEvictable(var0);
   }

   @GuardedBy("Segment.this")
   static void nullifyEvictable(MapMakerInternalMap.ReferenceEntry var0) {
      MapMakerInternalMap.ReferenceEntry var1 = nullEntry();
      var0.setNextEvictable(var1);
      var0.setPreviousEvictable(var1);
   }

   final MapMakerInternalMap.Segment[] newSegmentArray(int var1) {
      return new MapMakerInternalMap.Segment[var1];
   }

   public boolean isEmpty() {
      long var1 = 0L;
      MapMakerInternalMap.Segment[] var3 = this.segments;

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

   public int size() {
      MapMakerInternalMap.Segment[] var1 = this.segments;
      long var2 = 0L;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var2 += (long)var1[var4].count;
      }

      return Ints.saturatedCast(var2);
   }

   public Object get(@Nullable Object var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).get(var1, var2);
      }
   }

   MapMakerInternalMap.ReferenceEntry getEntry(@Nullable Object var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).getEntry(var1, var2);
      }
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
         MapMakerInternalMap.Segment[] var2 = this.segments;
         long var3 = -1L;

         for(int var5 = 0; var5 < 3; ++var5) {
            long var6 = 0L;
            MapMakerInternalMap.Segment[] var8 = var2;
            int var9 = var2.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               MapMakerInternalMap.Segment var11 = var8[var10];
               int var12 = var11.count;
               AtomicReferenceArray var13 = var11.table;

               for(int var14 = 0; var14 < var13.length(); ++var14) {
                  for(MapMakerInternalMap.ReferenceEntry var15 = (MapMakerInternalMap.ReferenceEntry)var13.get(var14); var15 != null; var15 = var15.getNext()) {
                     Object var16 = var11.getLiveValue(var15);
                     if (var16 != null && this.valueEquivalence.equivalent(var1, var16)) {
                        return true;
                     }
                  }
               }

               var6 += (long)var11.modCount;
            }

            if (var6 == var3) {
               break;
            }

            var3 = var6;
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
      MapMakerInternalMap.Segment[] var1 = this.segments;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MapMakerInternalMap.Segment var4 = var1[var3];
         var4.clear();
      }

   }

   public Set keySet() {
      Set var1 = this.keySet;
      return var1 != null ? var1 : (this.keySet = new MapMakerInternalMap.KeySet(this));
   }

   public Collection values() {
      Collection var1 = this.values;
      return var1 != null ? var1 : (this.values = new MapMakerInternalMap.Values(this));
   }

   public Set entrySet() {
      Set var1 = this.entrySet;
      return var1 != null ? var1 : (this.entrySet = new MapMakerInternalMap.EntrySet(this));
   }

   Object writeReplace() {
      return new MapMakerInternalMap.SerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this);
   }

   private static final class SerializationProxy extends MapMakerInternalMap.AbstractSerializationProxy {
      private static final long serialVersionUID = 3L;

      SerializationProxy(MapMakerInternalMap.Strength var1, MapMakerInternalMap.Strength var2, Equivalence var3, Equivalence var4, long var5, long var7, int var9, int var10, MapMaker.RemovalListener var11, ConcurrentMap var12) {
         super(var1, var2, var3, var4, var5, var7, var9, var10, var11, var12);
      }

      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         this.writeMapTo(var1);
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         MapMaker var2 = this.readMapMaker(var1);
         this.delegate = var2.makeMap();
         this.readEntries(var1);
      }

      private Object readResolve() {
         return this.delegate;
      }
   }

   abstract static class AbstractSerializationProxy extends ForwardingConcurrentMap implements Serializable {
      private static final long serialVersionUID = 3L;
      final MapMakerInternalMap.Strength keyStrength;
      final MapMakerInternalMap.Strength valueStrength;
      final Equivalence keyEquivalence;
      final Equivalence valueEquivalence;
      final long expireAfterWriteNanos;
      final long expireAfterAccessNanos;
      final int maximumSize;
      final int concurrencyLevel;
      final MapMaker.RemovalListener removalListener;
      transient ConcurrentMap delegate;

      AbstractSerializationProxy(MapMakerInternalMap.Strength var1, MapMakerInternalMap.Strength var2, Equivalence var3, Equivalence var4, long var5, long var7, int var9, int var10, MapMaker.RemovalListener var11, ConcurrentMap var12) {
         this.keyStrength = var1;
         this.valueStrength = var2;
         this.keyEquivalence = var3;
         this.valueEquivalence = var4;
         this.expireAfterWriteNanos = var5;
         this.expireAfterAccessNanos = var7;
         this.maximumSize = var9;
         this.concurrencyLevel = var10;
         this.removalListener = var11;
         this.delegate = var12;
      }

      protected ConcurrentMap delegate() {
         return this.delegate;
      }

      void writeMapTo(ObjectOutputStream var1) throws IOException {
         var1.writeInt(this.delegate.size());
         Iterator var2 = this.delegate.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            var1.writeObject(var3.getKey());
            var1.writeObject(var3.getValue());
         }

         var1.writeObject((Object)null);
      }

      MapMaker readMapMaker(ObjectInputStream var1) throws IOException {
         int var2 = var1.readInt();
         MapMaker var3 = (new MapMaker()).initialCapacity(var2).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
         var3.removalListener(this.removalListener);
         if (this.expireAfterWriteNanos > 0L) {
            var3.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
         }

         if (this.expireAfterAccessNanos > 0L) {
            var3.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
         }

         if (this.maximumSize != -1) {
            var3.maximumSize(this.maximumSize);
         }

         return var3;
      }

      void readEntries(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         while(true) {
            Object var2 = var1.readObject();
            if (var2 == null) {
               return;
            }

            Object var3 = var1.readObject();
            this.delegate.put(var2, var3);
         }
      }

      protected Map delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   final class EntrySet extends AbstractSet {
      final MapMakerInternalMap this$0;

      EntrySet(MapMakerInternalMap var1) {
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

      public int size() {
         return this.this$0.size();
      }

      public boolean isEmpty() {
         return this.this$0.isEmpty();
      }

      public void clear() {
         this.this$0.clear();
      }
   }

   final class Values extends AbstractCollection {
      final MapMakerInternalMap this$0;

      Values(MapMakerInternalMap var1) {
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return this.this$0.new ValueIterator(this.this$0);
      }

      public int size() {
         return this.this$0.size();
      }

      public boolean isEmpty() {
         return this.this$0.isEmpty();
      }

      public boolean contains(Object var1) {
         return this.this$0.containsValue(var1);
      }

      public void clear() {
         this.this$0.clear();
      }
   }

   final class KeySet extends AbstractSet {
      final MapMakerInternalMap this$0;

      KeySet(MapMakerInternalMap var1) {
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return this.this$0.new KeyIterator(this.this$0);
      }

      public int size() {
         return this.this$0.size();
      }

      public boolean isEmpty() {
         return this.this$0.isEmpty();
      }

      public boolean contains(Object var1) {
         return this.this$0.containsKey(var1);
      }

      public boolean remove(Object var1) {
         return this.this$0.remove(var1) != null;
      }

      public void clear() {
         this.this$0.clear();
      }
   }

   final class EntryIterator extends MapMakerInternalMap.HashIterator {
      final MapMakerInternalMap this$0;

      EntryIterator(MapMakerInternalMap var1) {
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

   final class WriteThroughEntry extends AbstractMapEntry {
      final Object key;
      Object value;
      final MapMakerInternalMap this$0;

      WriteThroughEntry(MapMakerInternalMap var1, Object var2, Object var3) {
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
         Object var2 = this.this$0.put(this.key, var1);
         this.value = var1;
         return var2;
      }
   }

   final class ValueIterator extends MapMakerInternalMap.HashIterator {
      final MapMakerInternalMap this$0;

      ValueIterator(MapMakerInternalMap var1) {
         super(var1);
         this.this$0 = var1;
      }

      public Object next() {
         return this.nextEntry().getValue();
      }
   }

   final class KeyIterator extends MapMakerInternalMap.HashIterator {
      final MapMakerInternalMap this$0;

      KeyIterator(MapMakerInternalMap var1) {
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
      MapMakerInternalMap.Segment currentSegment;
      AtomicReferenceArray currentTable;
      MapMakerInternalMap.ReferenceEntry nextEntry;
      MapMakerInternalMap.WriteThroughEntry nextExternal;
      MapMakerInternalMap.WriteThroughEntry lastReturned;
      final MapMakerInternalMap this$0;

      HashIterator(MapMakerInternalMap var1) {
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

      MapMakerInternalMap.WriteThroughEntry nextEntry() {
         if (this.nextExternal == null) {
            throw new NoSuchElementException();
         } else {
            this.lastReturned = this.nextExternal;
            this.advance();
            return this.lastReturned;
         }
      }

      public void remove() {
         CollectPreconditions.checkRemove(this.lastReturned != null);
         this.this$0.remove(this.lastReturned.getKey());
         this.lastReturned = null;
      }
   }

   static final class CleanupMapTask implements Runnable {
      final WeakReference mapReference;

      public CleanupMapTask(MapMakerInternalMap var1) {
         this.mapReference = new WeakReference(var1);
      }

      public void run() {
         MapMakerInternalMap var1 = (MapMakerInternalMap)this.mapReference.get();
         if (var1 == null) {
            throw new CancellationException();
         } else {
            MapMakerInternalMap.Segment[] var2 = var1.segments;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               MapMakerInternalMap.Segment var5 = var2[var4];
               var5.runCleanup();
            }

         }
      }
   }

   static final class ExpirationQueue extends AbstractQueue {
      final MapMakerInternalMap.ReferenceEntry head = new MapMakerInternalMap.AbstractReferenceEntry(this) {
         MapMakerInternalMap.ReferenceEntry nextExpirable;
         MapMakerInternalMap.ReferenceEntry previousExpirable;
         final MapMakerInternalMap.ExpirationQueue this$0;

         {
            this.this$0 = var1;
            this.nextExpirable = this;
            this.previousExpirable = this;
         }

         public long getExpirationTime() {
            return Long.MAX_VALUE;
         }

         public void setExpirationTime(long var1) {
         }

         public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
            return this.nextExpirable;
         }

         public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
            this.nextExpirable = var1;
         }

         public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
            return this.previousExpirable;
         }

         public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
            this.previousExpirable = var1;
         }
      };

      public boolean offer(MapMakerInternalMap.ReferenceEntry var1) {
         MapMakerInternalMap.connectExpirables(var1.getPreviousExpirable(), var1.getNextExpirable());
         MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), var1);
         MapMakerInternalMap.connectExpirables(var1, this.head);
         return true;
      }

      public MapMakerInternalMap.ReferenceEntry peek() {
         MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextExpirable();
         return var1 == this.head ? null : var1;
      }

      public MapMakerInternalMap.ReferenceEntry poll() {
         MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextExpirable();
         if (var1 == this.head) {
            return null;
         } else {
            this.remove(var1);
            return var1;
         }
      }

      public boolean remove(Object var1) {
         MapMakerInternalMap.ReferenceEntry var2 = (MapMakerInternalMap.ReferenceEntry)var1;
         MapMakerInternalMap.ReferenceEntry var3 = var2.getPreviousExpirable();
         MapMakerInternalMap.ReferenceEntry var4 = var2.getNextExpirable();
         MapMakerInternalMap.connectExpirables(var3, var4);
         MapMakerInternalMap.nullifyExpirable(var2);
         return var4 != MapMakerInternalMap.NullEntry.INSTANCE;
      }

      public boolean contains(Object var1) {
         MapMakerInternalMap.ReferenceEntry var2 = (MapMakerInternalMap.ReferenceEntry)var1;
         return var2.getNextExpirable() != MapMakerInternalMap.NullEntry.INSTANCE;
      }

      public boolean isEmpty() {
         return this.head.getNextExpirable() == this.head;
      }

      public int size() {
         int var1 = 0;

         for(MapMakerInternalMap.ReferenceEntry var2 = this.head.getNextExpirable(); var2 != this.head; var2 = var2.getNextExpirable()) {
            ++var1;
         }

         return var1;
      }

      public void clear() {
         MapMakerInternalMap.ReferenceEntry var2;
         for(MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextExpirable(); var1 != this.head; var1 = var2) {
            var2 = var1.getNextExpirable();
            MapMakerInternalMap.nullifyExpirable(var1);
         }

         this.head.setNextExpirable(this.head);
         this.head.setPreviousExpirable(this.head);
      }

      public Iterator iterator() {
         return new AbstractSequentialIterator(this, this.peek()) {
            final MapMakerInternalMap.ExpirationQueue this$0;

            {
               this.this$0 = var1;
            }

            protected MapMakerInternalMap.ReferenceEntry computeNext(MapMakerInternalMap.ReferenceEntry var1) {
               MapMakerInternalMap.ReferenceEntry var2 = var1.getNextExpirable();
               return var2 == this.this$0.head ? null : var2;
            }

            protected Object computeNext(Object var1) {
               return this.computeNext((MapMakerInternalMap.ReferenceEntry)var1);
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
         return this.offer((MapMakerInternalMap.ReferenceEntry)var1);
      }
   }

   static final class EvictionQueue extends AbstractQueue {
      final MapMakerInternalMap.ReferenceEntry head = new MapMakerInternalMap.AbstractReferenceEntry(this) {
         MapMakerInternalMap.ReferenceEntry nextEvictable;
         MapMakerInternalMap.ReferenceEntry previousEvictable;
         final MapMakerInternalMap.EvictionQueue this$0;

         {
            this.this$0 = var1;
            this.nextEvictable = this;
            this.previousEvictable = this;
         }

         public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
            return this.nextEvictable;
         }

         public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
            this.nextEvictable = var1;
         }

         public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
            return this.previousEvictable;
         }

         public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
            this.previousEvictable = var1;
         }
      };

      public boolean offer(MapMakerInternalMap.ReferenceEntry var1) {
         MapMakerInternalMap.connectEvictables(var1.getPreviousEvictable(), var1.getNextEvictable());
         MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), var1);
         MapMakerInternalMap.connectEvictables(var1, this.head);
         return true;
      }

      public MapMakerInternalMap.ReferenceEntry peek() {
         MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextEvictable();
         return var1 == this.head ? null : var1;
      }

      public MapMakerInternalMap.ReferenceEntry poll() {
         MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextEvictable();
         if (var1 == this.head) {
            return null;
         } else {
            this.remove(var1);
            return var1;
         }
      }

      public boolean remove(Object var1) {
         MapMakerInternalMap.ReferenceEntry var2 = (MapMakerInternalMap.ReferenceEntry)var1;
         MapMakerInternalMap.ReferenceEntry var3 = var2.getPreviousEvictable();
         MapMakerInternalMap.ReferenceEntry var4 = var2.getNextEvictable();
         MapMakerInternalMap.connectEvictables(var3, var4);
         MapMakerInternalMap.nullifyEvictable(var2);
         return var4 != MapMakerInternalMap.NullEntry.INSTANCE;
      }

      public boolean contains(Object var1) {
         MapMakerInternalMap.ReferenceEntry var2 = (MapMakerInternalMap.ReferenceEntry)var1;
         return var2.getNextEvictable() != MapMakerInternalMap.NullEntry.INSTANCE;
      }

      public boolean isEmpty() {
         return this.head.getNextEvictable() == this.head;
      }

      public int size() {
         int var1 = 0;

         for(MapMakerInternalMap.ReferenceEntry var2 = this.head.getNextEvictable(); var2 != this.head; var2 = var2.getNextEvictable()) {
            ++var1;
         }

         return var1;
      }

      public void clear() {
         MapMakerInternalMap.ReferenceEntry var2;
         for(MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextEvictable(); var1 != this.head; var1 = var2) {
            var2 = var1.getNextEvictable();
            MapMakerInternalMap.nullifyEvictable(var1);
         }

         this.head.setNextEvictable(this.head);
         this.head.setPreviousEvictable(this.head);
      }

      public Iterator iterator() {
         return new AbstractSequentialIterator(this, this.peek()) {
            final MapMakerInternalMap.EvictionQueue this$0;

            {
               this.this$0 = var1;
            }

            protected MapMakerInternalMap.ReferenceEntry computeNext(MapMakerInternalMap.ReferenceEntry var1) {
               MapMakerInternalMap.ReferenceEntry var2 = var1.getNextEvictable();
               return var2 == this.this$0.head ? null : var2;
            }

            protected Object computeNext(Object var1) {
               return this.computeNext((MapMakerInternalMap.ReferenceEntry)var1);
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
         return this.offer((MapMakerInternalMap.ReferenceEntry)var1);
      }
   }

   static class Segment extends ReentrantLock {
      final MapMakerInternalMap map;
      volatile int count;
      int modCount;
      int threshold;
      volatile AtomicReferenceArray table;
      final int maxSegmentSize;
      final ReferenceQueue keyReferenceQueue;
      final ReferenceQueue valueReferenceQueue;
      final Queue recencyQueue;
      final AtomicInteger readCount = new AtomicInteger();
      @GuardedBy("Segment.this")
      final Queue evictionQueue;
      @GuardedBy("Segment.this")
      final Queue expirationQueue;

      Segment(MapMakerInternalMap var1, int var2, int var3) {
         this.map = var1;
         this.maxSegmentSize = var3;
         this.initTable(this.newEntryArray(var2));
         this.keyReferenceQueue = var1.usesKeyReferences() ? new ReferenceQueue() : null;
         this.valueReferenceQueue = var1.usesValueReferences() ? new ReferenceQueue() : null;
         this.recencyQueue = (Queue)(!var1.evictsBySize() && !var1.expiresAfterAccess() ? MapMakerInternalMap.discardingQueue() : new ConcurrentLinkedQueue());
         this.evictionQueue = (Queue)(var1.evictsBySize() ? new MapMakerInternalMap.EvictionQueue() : MapMakerInternalMap.discardingQueue());
         this.expirationQueue = (Queue)(var1.expires() ? new MapMakerInternalMap.ExpirationQueue() : MapMakerInternalMap.discardingQueue());
      }

      AtomicReferenceArray newEntryArray(int var1) {
         return new AtomicReferenceArray(var1);
      }

      void initTable(AtomicReferenceArray var1) {
         this.threshold = var1.length() * 3 / 4;
         if (this.threshold == this.maxSegmentSize) {
            ++this.threshold;
         }

         this.table = var1;
      }

      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry newEntry(Object var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry var3) {
         return this.map.entryFactory.newEntry(this, var1, var2, var3);
      }

      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry copyEntry(MapMakerInternalMap.ReferenceEntry var1, MapMakerInternalMap.ReferenceEntry var2) {
         if (var1.getKey() == null) {
            return null;
         } else {
            MapMakerInternalMap.ValueReference var3 = var1.getValueReference();
            Object var4 = var3.get();
            if (var4 == null && !var3.isComputingReference()) {
               return null;
            } else {
               MapMakerInternalMap.ReferenceEntry var5 = this.map.entryFactory.copyEntry(this, var1, var2);
               var5.setValueReference(var3.copyFor(this.valueReferenceQueue, var4, var5));
               return var5;
            }
         }
      }

      @GuardedBy("Segment.this")
      void setValue(MapMakerInternalMap.ReferenceEntry var1, Object var2) {
         MapMakerInternalMap.ValueReference var3 = this.map.valueStrength.referenceValue(this, var1, var2);
         var1.setValueReference(var3);
         this.recordWrite(var1);
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
            MapMakerInternalMap.ReferenceEntry var3 = (MapMakerInternalMap.ReferenceEntry)var1;
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
            MapMakerInternalMap.ValueReference var3 = (MapMakerInternalMap.ValueReference)var1;
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

      void recordRead(MapMakerInternalMap.ReferenceEntry var1) {
         if (this.map.expiresAfterAccess()) {
            this.recordExpirationTime(var1, this.map.expireAfterAccessNanos);
         }

         this.recencyQueue.add(var1);
      }

      @GuardedBy("Segment.this")
      void recordLockedRead(MapMakerInternalMap.ReferenceEntry var1) {
         this.evictionQueue.add(var1);
         if (this.map.expiresAfterAccess()) {
            this.recordExpirationTime(var1, this.map.expireAfterAccessNanos);
            this.expirationQueue.add(var1);
         }

      }

      @GuardedBy("Segment.this")
      void recordWrite(MapMakerInternalMap.ReferenceEntry var1) {
         this.drainRecencyQueue();
         this.evictionQueue.add(var1);
         if (this.map.expires()) {
            long var2 = this.map.expiresAfterAccess() ? this.map.expireAfterAccessNanos : this.map.expireAfterWriteNanos;
            this.recordExpirationTime(var1, var2);
            this.expirationQueue.add(var1);
         }

      }

      @GuardedBy("Segment.this")
      void drainRecencyQueue() {
         MapMakerInternalMap.ReferenceEntry var1;
         while((var1 = (MapMakerInternalMap.ReferenceEntry)this.recencyQueue.poll()) != null) {
            if (this.evictionQueue.contains(var1)) {
               this.evictionQueue.add(var1);
            }

            if (this.map.expiresAfterAccess() && this.expirationQueue.contains(var1)) {
               this.expirationQueue.add(var1);
            }
         }

      }

      void recordExpirationTime(MapMakerInternalMap.ReferenceEntry var1, long var2) {
         var1.setExpirationTime(this.map.ticker.read() + var2);
      }

      void tryExpireEntries() {
         if (this.tryLock()) {
            this.expireEntries();
            this.unlock();
         }

      }

      @GuardedBy("Segment.this")
      void expireEntries() {
         this.drainRecencyQueue();
         if (!this.expirationQueue.isEmpty()) {
            long var1 = this.map.ticker.read();

            MapMakerInternalMap.ReferenceEntry var3;
            while((var3 = (MapMakerInternalMap.ReferenceEntry)this.expirationQueue.peek()) != null && this.map.isExpired(var3, var1)) {
               var3.getHash();
               if (MapMaker.RemovalCause.EXPIRED != null) {
                  throw new AssertionError();
               }
            }

         }
      }

      void enqueueNotification(MapMakerInternalMap.ReferenceEntry var1, MapMaker.RemovalCause var2) {
         this.enqueueNotification(var1.getKey(), var1.getHash(), var1.getValueReference().get(), var2);
      }

      void enqueueNotification(@Nullable Object var1, int var2, @Nullable Object var3, MapMaker.RemovalCause var4) {
         if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
            MapMaker.RemovalNotification var5 = new MapMaker.RemovalNotification(var1, var3, var4);
            this.map.removalNotificationQueue.offer(var5);
         }

      }

      MapMakerInternalMap.ReferenceEntry getFirst(int var1) {
         AtomicReferenceArray var2 = this.table;
         return (MapMakerInternalMap.ReferenceEntry)var2.get(var1 & var2.length() - 1);
      }

      MapMakerInternalMap.ReferenceEntry getEntry(Object var1, int var2) {
         if (this.count != 0) {
            for(MapMakerInternalMap.ReferenceEntry var3 = this.getFirst(var2); var3 != null; var3 = var3.getNext()) {
               if (var3.getHash() == var2) {
                  Object var4 = var3.getKey();
                  if (var4 == null) {
                     this.tryDrainReferenceQueues();
                  } else if (this.map.keyEquivalence.equivalent(var1, var4)) {
                     return var3;
                  }
               }
            }
         }

         return null;
      }

      MapMakerInternalMap.ReferenceEntry getLiveEntry(Object var1, int var2) {
         MapMakerInternalMap.ReferenceEntry var3 = this.getEntry(var1, var2);
         if (var3 == null) {
            return null;
         } else if (this.map.expires() && this.map.isExpired(var3)) {
            this.tryExpireEntries();
            return null;
         } else {
            return var3;
         }
      }

      Object get(Object var1, int var2) {
         MapMakerInternalMap.ReferenceEntry var3 = this.getLiveEntry(var1, var2);
         Object var4;
         if (var3 == null) {
            var4 = null;
            this.postReadCleanup();
            return var4;
         } else {
            var4 = var3.getValueReference().get();
            if (var4 != null) {
               this.recordRead(var3);
            } else {
               this.tryDrainReferenceQueues();
            }

            this.postReadCleanup();
            return var4;
         }
      }

      boolean containsKey(Object var1, int var2) {
         if (this.count != 0) {
            MapMakerInternalMap.ReferenceEntry var6 = this.getLiveEntry(var1, var2);
            boolean var4;
            if (var6 == null) {
               var4 = false;
               this.postReadCleanup();
               return var4;
            } else {
               var4 = var6.getValueReference().get() != null;
               this.postReadCleanup();
               return var4;
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
            AtomicReferenceArray var2 = this.table;
            int var3 = var2.length();

            for(int var4 = 0; var4 < var3; ++var4) {
               for(MapMakerInternalMap.ReferenceEntry var5 = (MapMakerInternalMap.ReferenceEntry)var2.get(var4); var5 != null; var5 = var5.getNext()) {
                  Object var6 = this.getLiveValue(var5);
                  if (var6 != null && this.map.valueEquivalence.equivalent(var1, var6)) {
                     boolean var7 = true;
                     this.postReadCleanup();
                     return var7;
                  }
               }
            }
         }

         boolean var9 = false;
         this.postReadCleanup();
         return var9;
      }

      Object put(Object var1, int var2, Object var3, boolean var4) {
         this.lock();
         this.preWriteCleanup();
         int var5 = this.count + 1;
         if (var5 > this.threshold) {
            this.expand();
            var5 = this.count + 1;
         }

         AtomicReferenceArray var6 = this.table;
         int var7 = var2 & var6.length() - 1;
         MapMakerInternalMap.ReferenceEntry var8 = (MapMakerInternalMap.ReferenceEntry)var6.get(var7);

         MapMakerInternalMap.ReferenceEntry var9;
         Object var10;
         for(var9 = var8; var9 != null; var9 = var9.getNext()) {
            var10 = var9.getKey();
            if (var9.getHash() == var2 && var10 != null && this.map.keyEquivalence.equivalent(var1, var10)) {
               MapMakerInternalMap.ValueReference var11 = var9.getValueReference();
               Object var12 = var11.get();
               if (var12 == null) {
                  ++this.modCount;
                  this.setValue(var9, var3);
                  if (!var11.isComputingReference()) {
                     this.enqueueNotification(var1, var2, var12, MapMaker.RemovalCause.COLLECTED);
                     var5 = this.count;
                  } else if (this != false) {
                     var5 = this.count + 1;
                  }

                  this.count = var5;
                  Object var13 = null;
                  this.unlock();
                  this.postWriteCleanup();
                  return var13;
               }

               if (var4) {
                  this.recordLockedRead(var9);
                  this.unlock();
                  this.postWriteCleanup();
                  return var12;
               }

               ++this.modCount;
               this.enqueueNotification(var1, var2, var12, MapMaker.RemovalCause.REPLACED);
               this.setValue(var9, var3);
               this.unlock();
               this.postWriteCleanup();
               return var12;
            }
         }

         ++this.modCount;
         var9 = this.newEntry(var1, var2, var8);
         this.setValue(var9, var3);
         var6.set(var7, var9);
         if (this != false) {
            var5 = this.count + 1;
         }

         this.count = var5;
         var10 = null;
         this.unlock();
         this.postWriteCleanup();
         return var10;
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
               MapMakerInternalMap.ReferenceEntry var7 = (MapMakerInternalMap.ReferenceEntry)var1.get(var6);
               if (var7 != null) {
                  MapMakerInternalMap.ReferenceEntry var8 = var7.getNext();
                  int var9 = var7.getHash() & var5;
                  if (var8 == null) {
                     var4.set(var9, var7);
                  } else {
                     MapMakerInternalMap.ReferenceEntry var10 = var7;
                     int var11 = var9;

                     MapMakerInternalMap.ReferenceEntry var12;
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
                        MapMakerInternalMap.ReferenceEntry var14 = (MapMakerInternalMap.ReferenceEntry)var4.get(var13);
                        MapMakerInternalMap.ReferenceEntry var15 = this.copyEntry(var12, var14);
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
         this.preWriteCleanup();
         AtomicReferenceArray var5 = this.table;
         int var6 = var2 & var5.length() - 1;
         MapMakerInternalMap.ReferenceEntry var7 = (MapMakerInternalMap.ReferenceEntry)var5.get(var6);

         for(MapMakerInternalMap.ReferenceEntry var8 = var7; var8 != null; var8 = var8.getNext()) {
            Object var9 = var8.getKey();
            if (var8.getHash() == var2 && var9 != null && this.map.keyEquivalence.equivalent(var1, var9)) {
               MapMakerInternalMap.ValueReference var10 = var8.getValueReference();
               Object var11 = var10.get();
               boolean var12;
               if (var11 == null) {
                  if (var10 != false) {
                     int var16 = this.count - 1;
                     ++this.modCount;
                     this.enqueueNotification(var9, var2, var11, MapMaker.RemovalCause.COLLECTED);
                     MapMakerInternalMap.ReferenceEntry var13 = this.removeFromChain(var7, var8);
                     var16 = this.count - 1;
                     var5.set(var6, var13);
                     this.count = var16;
                  }

                  var12 = false;
                  this.unlock();
                  this.postWriteCleanup();
                  return var12;
               }

               if (this.map.valueEquivalence.equivalent(var3, var11)) {
                  ++this.modCount;
                  this.enqueueNotification(var1, var2, var11, MapMaker.RemovalCause.REPLACED);
                  this.setValue(var8, var4);
                  var12 = true;
                  this.unlock();
                  this.postWriteCleanup();
                  return var12;
               }

               this.recordLockedRead(var8);
               var12 = false;
               this.unlock();
               this.postWriteCleanup();
               return var12;
            }
         }

         boolean var15 = false;
         this.unlock();
         this.postWriteCleanup();
         return var15;
      }

      Object replace(Object var1, int var2, Object var3) {
         this.lock();
         this.preWriteCleanup();
         AtomicReferenceArray var4 = this.table;
         int var5 = var2 & var4.length() - 1;
         MapMakerInternalMap.ReferenceEntry var6 = (MapMakerInternalMap.ReferenceEntry)var4.get(var5);

         MapMakerInternalMap.ReferenceEntry var7;
         for(var7 = var6; var7 != null; var7 = var7.getNext()) {
            Object var8 = var7.getKey();
            if (var7.getHash() == var2 && var8 != null && this.map.keyEquivalence.equivalent(var1, var8)) {
               MapMakerInternalMap.ValueReference var9 = var7.getValueReference();
               Object var10 = var9.get();
               if (var10 == null) {
                  if (var9 != false) {
                     int var11 = this.count - 1;
                     ++this.modCount;
                     this.enqueueNotification(var8, var2, var10, MapMaker.RemovalCause.COLLECTED);
                     MapMakerInternalMap.ReferenceEntry var12 = this.removeFromChain(var6, var7);
                     var11 = this.count - 1;
                     var4.set(var5, var12);
                     this.count = var11;
                  }

                  Object var14 = null;
                  this.unlock();
                  this.postWriteCleanup();
                  return var14;
               }

               ++this.modCount;
               this.enqueueNotification(var1, var2, var10, MapMaker.RemovalCause.REPLACED);
               this.setValue(var7, var3);
               this.unlock();
               this.postWriteCleanup();
               return var10;
            }
         }

         var7 = null;
         this.unlock();
         this.postWriteCleanup();
         return var7;
      }

      Object remove(Object var1, int var2) {
         this.lock();
         this.preWriteCleanup();
         int var3 = this.count - 1;
         AtomicReferenceArray var4 = this.table;
         int var5 = var2 & var4.length() - 1;
         MapMakerInternalMap.ReferenceEntry var6 = (MapMakerInternalMap.ReferenceEntry)var4.get(var5);

         MapMakerInternalMap.ReferenceEntry var7;
         for(var7 = var6; var7 != null; var7 = var7.getNext()) {
            Object var8 = var7.getKey();
            if (var7.getHash() == var2 && var8 != null && this.map.keyEquivalence.equivalent(var1, var8)) {
               MapMakerInternalMap.ValueReference var9 = var7.getValueReference();
               Object var10 = var9.get();
               MapMaker.RemovalCause var11;
               MapMakerInternalMap.ReferenceEntry var12;
               if (var10 != null) {
                  var11 = MapMaker.RemovalCause.EXPLICIT;
               } else {
                  if (var9 == false) {
                     var12 = null;
                     this.unlock();
                     this.postWriteCleanup();
                     return var12;
                  }

                  var11 = MapMaker.RemovalCause.COLLECTED;
               }

               ++this.modCount;
               this.enqueueNotification(var8, var2, var10, var11);
               var12 = this.removeFromChain(var6, var7);
               var3 = this.count - 1;
               var4.set(var5, var12);
               this.count = var3;
               this.unlock();
               this.postWriteCleanup();
               return var10;
            }
         }

         var7 = null;
         this.unlock();
         this.postWriteCleanup();
         return var7;
      }

      boolean remove(Object var1, int var2, Object var3) {
         this.lock();
         this.preWriteCleanup();
         int var4 = this.count - 1;
         AtomicReferenceArray var5 = this.table;
         int var6 = var2 & var5.length() - 1;
         MapMakerInternalMap.ReferenceEntry var7 = (MapMakerInternalMap.ReferenceEntry)var5.get(var6);

         for(MapMakerInternalMap.ReferenceEntry var8 = var7; var8 != null; var8 = var8.getNext()) {
            Object var9 = var8.getKey();
            if (var8.getHash() == var2 && var9 != null && this.map.keyEquivalence.equivalent(var1, var9)) {
               MapMakerInternalMap.ValueReference var10 = var8.getValueReference();
               Object var11 = var10.get();
               MapMaker.RemovalCause var12;
               if (this.map.valueEquivalence.equivalent(var3, var11)) {
                  var12 = MapMaker.RemovalCause.EXPLICIT;
               } else {
                  if (var10 == false) {
                     boolean var17 = false;
                     this.unlock();
                     this.postWriteCleanup();
                     return var17;
                  }

                  var12 = MapMaker.RemovalCause.COLLECTED;
               }

               ++this.modCount;
               this.enqueueNotification(var9, var2, var11, var12);
               MapMakerInternalMap.ReferenceEntry var13 = this.removeFromChain(var7, var8);
               var4 = this.count - 1;
               var5.set(var6, var13);
               this.count = var4;
               boolean var14 = var12 == MapMaker.RemovalCause.EXPLICIT;
               this.unlock();
               this.postWriteCleanup();
               return var14;
            }
         }

         boolean var16 = false;
         this.unlock();
         this.postWriteCleanup();
         return var16;
      }

      void clear() {
         if (this.count != 0) {
            this.lock();
            AtomicReferenceArray var1 = this.table;
            int var2;
            if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
               for(var2 = 0; var2 < var1.length(); ++var2) {
                  for(MapMakerInternalMap.ReferenceEntry var3 = (MapMakerInternalMap.ReferenceEntry)var1.get(var2); var3 != null; var3 = var3.getNext()) {
                     if (!var3.getValueReference().isComputingReference()) {
                        this.enqueueNotification(var3, MapMaker.RemovalCause.EXPLICIT);
                     }
                  }
               }
            }

            for(var2 = 0; var2 < var1.length(); ++var2) {
               var1.set(var2, (Object)null);
            }

            this.clearReferenceQueues();
            this.evictionQueue.clear();
            this.expirationQueue.clear();
            this.readCount.set(0);
            ++this.modCount;
            this.count = 0;
            this.unlock();
            this.postWriteCleanup();
         }

      }

      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry removeFromChain(MapMakerInternalMap.ReferenceEntry var1, MapMakerInternalMap.ReferenceEntry var2) {
         this.evictionQueue.remove(var2);
         this.expirationQueue.remove(var2);
         int var3 = this.count;
         MapMakerInternalMap.ReferenceEntry var4 = var2.getNext();

         for(MapMakerInternalMap.ReferenceEntry var5 = var1; var5 != var2; var5 = var5.getNext()) {
            MapMakerInternalMap.ReferenceEntry var6 = this.copyEntry(var5, var4);
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

      void removeCollectedEntry(MapMakerInternalMap.ReferenceEntry var1) {
         this.enqueueNotification(var1, MapMaker.RemovalCause.COLLECTED);
         this.evictionQueue.remove(var1);
         this.expirationQueue.remove(var1);
      }

      boolean reclaimKey(MapMakerInternalMap.ReferenceEntry var1, int var2) {
         this.lock();
         int var3 = this.count - 1;
         AtomicReferenceArray var4 = this.table;
         int var5 = var2 & var4.length() - 1;
         MapMakerInternalMap.ReferenceEntry var6 = (MapMakerInternalMap.ReferenceEntry)var4.get(var5);

         for(MapMakerInternalMap.ReferenceEntry var7 = var6; var7 != null; var7 = var7.getNext()) {
            if (var7 == var1) {
               ++this.modCount;
               this.enqueueNotification(var7.getKey(), var2, var7.getValueReference().get(), MapMaker.RemovalCause.COLLECTED);
               MapMakerInternalMap.ReferenceEntry var8 = this.removeFromChain(var6, var7);
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

      boolean reclaimValue(Object var1, int var2, MapMakerInternalMap.ValueReference var3) {
         this.lock();
         int var4 = this.count - 1;
         AtomicReferenceArray var5 = this.table;
         int var6 = var2 & var5.length() - 1;
         MapMakerInternalMap.ReferenceEntry var7 = (MapMakerInternalMap.ReferenceEntry)var5.get(var6);

         for(MapMakerInternalMap.ReferenceEntry var8 = var7; var8 != null; var8 = var8.getNext()) {
            Object var9 = var8.getKey();
            if (var8.getHash() == var2 && var9 != null && this.map.keyEquivalence.equivalent(var1, var9)) {
               MapMakerInternalMap.ValueReference var10 = var8.getValueReference();
               if (var10 == var3) {
                  ++this.modCount;
                  this.enqueueNotification(var1, var2, var3.get(), MapMaker.RemovalCause.COLLECTED);
                  MapMakerInternalMap.ReferenceEntry var15 = this.removeFromChain(var7, var8);
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

      boolean clearValue(Object var1, int var2, MapMakerInternalMap.ValueReference var3) {
         this.lock();
         AtomicReferenceArray var4 = this.table;
         int var5 = var2 & var4.length() - 1;
         MapMakerInternalMap.ReferenceEntry var6 = (MapMakerInternalMap.ReferenceEntry)var4.get(var5);

         for(MapMakerInternalMap.ReferenceEntry var7 = var6; var7 != null; var7 = var7.getNext()) {
            Object var8 = var7.getKey();
            if (var7.getHash() == var2 && var8 != null && this.map.keyEquivalence.equivalent(var1, var8)) {
               MapMakerInternalMap.ValueReference var9 = var7.getValueReference();
               if (var9 == var3) {
                  MapMakerInternalMap.ReferenceEntry var13 = this.removeFromChain(var6, var7);
                  var4.set(var5, var13);
                  boolean var11 = true;
                  this.unlock();
                  this.postWriteCleanup();
                  return var11;
               }

               boolean var10 = false;
               this.unlock();
               this.postWriteCleanup();
               return var10;
            }
         }

         boolean var14 = false;
         this.unlock();
         this.postWriteCleanup();
         return var14;
      }

      Object getLiveValue(MapMakerInternalMap.ReferenceEntry var1) {
         if (var1.getKey() == null) {
            this.tryDrainReferenceQueues();
            return null;
         } else {
            Object var2 = var1.getValueReference().get();
            if (var2 == null) {
               this.tryDrainReferenceQueues();
               return null;
            } else if (this.map.expires() && this.map.isExpired(var1)) {
               this.tryExpireEntries();
               return null;
            } else {
               return var2;
            }
         }
      }

      void postReadCleanup() {
         if ((this.readCount.incrementAndGet() & 63) == 0) {
            this.runCleanup();
         }

      }

      @GuardedBy("Segment.this")
      void preWriteCleanup() {
         this.runLockedCleanup();
      }

      void postWriteCleanup() {
         this.runUnlockedCleanup();
      }

      void runCleanup() {
         this.runLockedCleanup();
         this.runUnlockedCleanup();
      }

      void runLockedCleanup() {
         if (this.tryLock()) {
            this.drainReferenceQueues();
            this.expireEntries();
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

   static final class StrongValueReference implements MapMakerInternalMap.ValueReference {
      final Object referent;

      StrongValueReference(Object var1) {
         this.referent = var1;
      }

      public Object get() {
         return this.referent;
      }

      public MapMakerInternalMap.ReferenceEntry getEntry() {
         return null;
      }

      public MapMakerInternalMap.ValueReference copyFor(ReferenceQueue var1, Object var2, MapMakerInternalMap.ReferenceEntry var3) {
         return this;
      }

      public boolean isComputingReference() {
         return false;
      }

      public Object waitForValue() {
         return this.get();
      }

      public void clear(MapMakerInternalMap.ValueReference var1) {
      }
   }

   static final class SoftValueReference extends SoftReference implements MapMakerInternalMap.ValueReference {
      final MapMakerInternalMap.ReferenceEntry entry;

      SoftValueReference(ReferenceQueue var1, Object var2, MapMakerInternalMap.ReferenceEntry var3) {
         super(var2, var1);
         this.entry = var3;
      }

      public MapMakerInternalMap.ReferenceEntry getEntry() {
         return this.entry;
      }

      public void clear(MapMakerInternalMap.ValueReference var1) {
         this.clear();
      }

      public MapMakerInternalMap.ValueReference copyFor(ReferenceQueue var1, Object var2, MapMakerInternalMap.ReferenceEntry var3) {
         return new MapMakerInternalMap.SoftValueReference(var1, var2, var3);
      }

      public boolean isComputingReference() {
         return false;
      }

      public Object waitForValue() {
         return this.get();
      }
   }

   static final class WeakValueReference extends WeakReference implements MapMakerInternalMap.ValueReference {
      final MapMakerInternalMap.ReferenceEntry entry;

      WeakValueReference(ReferenceQueue var1, Object var2, MapMakerInternalMap.ReferenceEntry var3) {
         super(var2, var1);
         this.entry = var3;
      }

      public MapMakerInternalMap.ReferenceEntry getEntry() {
         return this.entry;
      }

      public void clear(MapMakerInternalMap.ValueReference var1) {
         this.clear();
      }

      public MapMakerInternalMap.ValueReference copyFor(ReferenceQueue var1, Object var2, MapMakerInternalMap.ReferenceEntry var3) {
         return new MapMakerInternalMap.WeakValueReference(var1, var2, var3);
      }

      public boolean isComputingReference() {
         return false;
      }

      public Object waitForValue() {
         return this.get();
      }
   }

   static final class WeakExpirableEvictableEntry extends MapMakerInternalMap.WeakEntry implements MapMakerInternalMap.ReferenceEntry {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousEvictable = MapMakerInternalMap.nullEntry();

      WeakExpirableEvictableEntry(ReferenceQueue var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
         super(var1, var2, var3, var4);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousEvictable = var1;
      }
   }

   static final class WeakEvictableEntry extends MapMakerInternalMap.WeakEntry implements MapMakerInternalMap.ReferenceEntry {
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousEvictable = MapMakerInternalMap.nullEntry();

      WeakEvictableEntry(ReferenceQueue var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
         super(var1, var2, var3, var4);
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousEvictable = var1;
      }
   }

   static final class WeakExpirableEntry extends MapMakerInternalMap.WeakEntry implements MapMakerInternalMap.ReferenceEntry {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousExpirable = MapMakerInternalMap.nullEntry();

      WeakExpirableEntry(ReferenceQueue var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
         super(var1, var2, var3, var4);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousExpirable = var1;
      }
   }

   static class WeakEntry extends WeakReference implements MapMakerInternalMap.ReferenceEntry {
      final int hash;
      final MapMakerInternalMap.ReferenceEntry next;
      volatile MapMakerInternalMap.ValueReference valueReference = MapMakerInternalMap.unset();

      WeakEntry(ReferenceQueue var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
         super(var2, var1);
         this.hash = var3;
         this.next = var4;
      }

      public Object getKey() {
         return this.get();
      }

      public long getExpirationTime() {
         throw new UnsupportedOperationException();
      }

      public void setExpirationTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ValueReference getValueReference() {
         return this.valueReference;
      }

      public void setValueReference(MapMakerInternalMap.ValueReference var1) {
         MapMakerInternalMap.ValueReference var2 = this.valueReference;
         this.valueReference = var1;
         var2.clear(var1);
      }

      public int getHash() {
         return this.hash;
      }

      public MapMakerInternalMap.ReferenceEntry getNext() {
         return this.next;
      }
   }

   static final class SoftExpirableEvictableEntry extends MapMakerInternalMap.SoftEntry implements MapMakerInternalMap.ReferenceEntry {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousEvictable = MapMakerInternalMap.nullEntry();

      SoftExpirableEvictableEntry(ReferenceQueue var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
         super(var1, var2, var3, var4);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousEvictable = var1;
      }
   }

   static final class SoftEvictableEntry extends MapMakerInternalMap.SoftEntry implements MapMakerInternalMap.ReferenceEntry {
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousEvictable = MapMakerInternalMap.nullEntry();

      SoftEvictableEntry(ReferenceQueue var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
         super(var1, var2, var3, var4);
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousEvictable = var1;
      }
   }

   static final class SoftExpirableEntry extends MapMakerInternalMap.SoftEntry implements MapMakerInternalMap.ReferenceEntry {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousExpirable = MapMakerInternalMap.nullEntry();

      SoftExpirableEntry(ReferenceQueue var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
         super(var1, var2, var3, var4);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousExpirable = var1;
      }
   }

   static class SoftEntry extends SoftReference implements MapMakerInternalMap.ReferenceEntry {
      final int hash;
      final MapMakerInternalMap.ReferenceEntry next;
      volatile MapMakerInternalMap.ValueReference valueReference = MapMakerInternalMap.unset();

      SoftEntry(ReferenceQueue var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
         super(var2, var1);
         this.hash = var3;
         this.next = var4;
      }

      public Object getKey() {
         return this.get();
      }

      public long getExpirationTime() {
         throw new UnsupportedOperationException();
      }

      public void setExpirationTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ValueReference getValueReference() {
         return this.valueReference;
      }

      public void setValueReference(MapMakerInternalMap.ValueReference var1) {
         MapMakerInternalMap.ValueReference var2 = this.valueReference;
         this.valueReference = var1;
         var2.clear(var1);
      }

      public int getHash() {
         return this.hash;
      }

      public MapMakerInternalMap.ReferenceEntry getNext() {
         return this.next;
      }
   }

   static final class StrongExpirableEvictableEntry extends MapMakerInternalMap.StrongEntry implements MapMakerInternalMap.ReferenceEntry {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousEvictable = MapMakerInternalMap.nullEntry();

      StrongExpirableEvictableEntry(Object var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry var3) {
         super(var1, var2, var3);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousEvictable = var1;
      }
   }

   static final class StrongEvictableEntry extends MapMakerInternalMap.StrongEntry implements MapMakerInternalMap.ReferenceEntry {
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousEvictable = MapMakerInternalMap.nullEntry();

      StrongEvictableEntry(Object var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry var3) {
         super(var1, var2, var3);
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousEvictable = var1;
      }
   }

   static final class StrongExpirableEntry extends MapMakerInternalMap.StrongEntry implements MapMakerInternalMap.ReferenceEntry {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry previousExpirable = MapMakerInternalMap.nullEntry();

      StrongExpirableEntry(Object var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry var3) {
         super(var1, var2, var3);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         this.previousExpirable = var1;
      }
   }

   static class StrongEntry implements MapMakerInternalMap.ReferenceEntry {
      final Object key;
      final int hash;
      final MapMakerInternalMap.ReferenceEntry next;
      volatile MapMakerInternalMap.ValueReference valueReference = MapMakerInternalMap.unset();

      StrongEntry(Object var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry var3) {
         this.key = var1;
         this.hash = var2;
         this.next = var3;
      }

      public Object getKey() {
         return this.key;
      }

      public long getExpirationTime() {
         throw new UnsupportedOperationException();
      }

      public void setExpirationTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ValueReference getValueReference() {
         return this.valueReference;
      }

      public void setValueReference(MapMakerInternalMap.ValueReference var1) {
         MapMakerInternalMap.ValueReference var2 = this.valueReference;
         this.valueReference = var1;
         var2.clear(var1);
      }

      public int getHash() {
         return this.hash;
      }

      public MapMakerInternalMap.ReferenceEntry getNext() {
         return this.next;
      }
   }

   abstract static class AbstractReferenceEntry implements MapMakerInternalMap.ReferenceEntry {
      public MapMakerInternalMap.ValueReference getValueReference() {
         throw new UnsupportedOperationException();
      }

      public void setValueReference(MapMakerInternalMap.ValueReference var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getNext() {
         throw new UnsupportedOperationException();
      }

      public int getHash() {
         throw new UnsupportedOperationException();
      }

      public Object getKey() {
         throw new UnsupportedOperationException();
      }

      public long getExpirationTime() {
         throw new UnsupportedOperationException();
      }

      public void setExpirationTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
         throw new UnsupportedOperationException();
      }
   }

   private static enum NullEntry implements MapMakerInternalMap.ReferenceEntry {
      INSTANCE;

      private static final MapMakerInternalMap.NullEntry[] $VALUES = new MapMakerInternalMap.NullEntry[]{INSTANCE};

      public MapMakerInternalMap.ValueReference getValueReference() {
         return null;
      }

      public void setValueReference(MapMakerInternalMap.ValueReference var1) {
      }

      public MapMakerInternalMap.ReferenceEntry getNext() {
         return null;
      }

      public int getHash() {
         return 0;
      }

      public Object getKey() {
         return null;
      }

      public long getExpirationTime() {
         return 0L;
      }

      public void setExpirationTime(long var1) {
      }

      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         return this;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1) {
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         return this;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1) {
      }

      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         return this;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1) {
      }

      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         return this;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1) {
      }
   }

   interface ReferenceEntry {
      MapMakerInternalMap.ValueReference getValueReference();

      void setValueReference(MapMakerInternalMap.ValueReference var1);

      MapMakerInternalMap.ReferenceEntry getNext();

      int getHash();

      Object getKey();

      long getExpirationTime();

      void setExpirationTime(long var1);

      MapMakerInternalMap.ReferenceEntry getNextExpirable();

      void setNextExpirable(MapMakerInternalMap.ReferenceEntry var1);

      MapMakerInternalMap.ReferenceEntry getPreviousExpirable();

      void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry var1);

      MapMakerInternalMap.ReferenceEntry getNextEvictable();

      void setNextEvictable(MapMakerInternalMap.ReferenceEntry var1);

      MapMakerInternalMap.ReferenceEntry getPreviousEvictable();

      void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry var1);
   }

   interface ValueReference {
      Object get();

      Object waitForValue() throws ExecutionException;

      MapMakerInternalMap.ReferenceEntry getEntry();

      MapMakerInternalMap.ValueReference copyFor(ReferenceQueue var1, @Nullable Object var2, MapMakerInternalMap.ReferenceEntry var3);

      void clear(@Nullable MapMakerInternalMap.ValueReference var1);

      boolean isComputingReference();
   }

   static enum EntryFactory {
      STRONG {
         MapMakerInternalMap.ReferenceEntry newEntry(MapMakerInternalMap.Segment var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
            return new MapMakerInternalMap.StrongEntry(var2, var3, var4);
         }
      },
      STRONG_EXPIRABLE {
         MapMakerInternalMap.ReferenceEntry newEntry(MapMakerInternalMap.Segment var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
            return new MapMakerInternalMap.StrongExpirableEntry(var2, var3, var4);
         }

         MapMakerInternalMap.ReferenceEntry copyEntry(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, MapMakerInternalMap.ReferenceEntry var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyExpirableEntry(var2, var4);
            return var4;
         }
      },
      STRONG_EVICTABLE {
         MapMakerInternalMap.ReferenceEntry newEntry(MapMakerInternalMap.Segment var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
            return new MapMakerInternalMap.StrongEvictableEntry(var2, var3, var4);
         }

         MapMakerInternalMap.ReferenceEntry copyEntry(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, MapMakerInternalMap.ReferenceEntry var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyEvictableEntry(var2, var4);
            return var4;
         }
      },
      STRONG_EXPIRABLE_EVICTABLE {
         MapMakerInternalMap.ReferenceEntry newEntry(MapMakerInternalMap.Segment var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
            return new MapMakerInternalMap.StrongExpirableEvictableEntry(var2, var3, var4);
         }

         MapMakerInternalMap.ReferenceEntry copyEntry(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, MapMakerInternalMap.ReferenceEntry var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyExpirableEntry(var2, var4);
            this.copyEvictableEntry(var2, var4);
            return var4;
         }
      },
      WEAK {
         MapMakerInternalMap.ReferenceEntry newEntry(MapMakerInternalMap.Segment var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
            return new MapMakerInternalMap.WeakEntry(var1.keyReferenceQueue, var2, var3, var4);
         }
      },
      WEAK_EXPIRABLE {
         MapMakerInternalMap.ReferenceEntry newEntry(MapMakerInternalMap.Segment var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
            return new MapMakerInternalMap.WeakExpirableEntry(var1.keyReferenceQueue, var2, var3, var4);
         }

         MapMakerInternalMap.ReferenceEntry copyEntry(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, MapMakerInternalMap.ReferenceEntry var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyExpirableEntry(var2, var4);
            return var4;
         }
      },
      WEAK_EVICTABLE {
         MapMakerInternalMap.ReferenceEntry newEntry(MapMakerInternalMap.Segment var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
            return new MapMakerInternalMap.WeakEvictableEntry(var1.keyReferenceQueue, var2, var3, var4);
         }

         MapMakerInternalMap.ReferenceEntry copyEntry(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, MapMakerInternalMap.ReferenceEntry var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyEvictableEntry(var2, var4);
            return var4;
         }
      },
      WEAK_EXPIRABLE_EVICTABLE {
         MapMakerInternalMap.ReferenceEntry newEntry(MapMakerInternalMap.Segment var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4) {
            return new MapMakerInternalMap.WeakExpirableEvictableEntry(var1.keyReferenceQueue, var2, var3, var4);
         }

         MapMakerInternalMap.ReferenceEntry copyEntry(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, MapMakerInternalMap.ReferenceEntry var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyExpirableEntry(var2, var4);
            this.copyEvictableEntry(var2, var4);
            return var4;
         }
      };

      static final int EXPIRABLE_MASK = 1;
      static final int EVICTABLE_MASK = 2;
      static final MapMakerInternalMap.EntryFactory[][] factories = new MapMakerInternalMap.EntryFactory[][]{{STRONG, STRONG_EXPIRABLE, STRONG_EVICTABLE, STRONG_EXPIRABLE_EVICTABLE}, new MapMakerInternalMap.EntryFactory[0], {WEAK, WEAK_EXPIRABLE, WEAK_EVICTABLE, WEAK_EXPIRABLE_EVICTABLE}};
      private static final MapMakerInternalMap.EntryFactory[] $VALUES = new MapMakerInternalMap.EntryFactory[]{STRONG, STRONG_EXPIRABLE, STRONG_EVICTABLE, STRONG_EXPIRABLE_EVICTABLE, WEAK, WEAK_EXPIRABLE, WEAK_EVICTABLE, WEAK_EXPIRABLE_EVICTABLE};

      private EntryFactory() {
      }

      static MapMakerInternalMap.EntryFactory getFactory(MapMakerInternalMap.Strength var0, boolean var1, boolean var2) {
         int var3 = (var1 ? 1 : 0) | (var2 ? 2 : 0);
         return factories[var0.ordinal()][var3];
      }

      abstract MapMakerInternalMap.ReferenceEntry newEntry(MapMakerInternalMap.Segment var1, Object var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry var4);

      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry copyEntry(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, MapMakerInternalMap.ReferenceEntry var3) {
         return this.newEntry(var1, var2.getKey(), var2.getHash(), var3);
      }

      @GuardedBy("Segment.this")
      void copyExpirableEntry(MapMakerInternalMap.ReferenceEntry var1, MapMakerInternalMap.ReferenceEntry var2) {
         var2.setExpirationTime(var1.getExpirationTime());
         MapMakerInternalMap.connectExpirables(var1.getPreviousExpirable(), var2);
         MapMakerInternalMap.connectExpirables(var2, var1.getNextExpirable());
         MapMakerInternalMap.nullifyExpirable(var1);
      }

      @GuardedBy("Segment.this")
      void copyEvictableEntry(MapMakerInternalMap.ReferenceEntry var1, MapMakerInternalMap.ReferenceEntry var2) {
         MapMakerInternalMap.connectEvictables(var1.getPreviousEvictable(), var2);
         MapMakerInternalMap.connectEvictables(var2, var1.getNextEvictable());
         MapMakerInternalMap.nullifyEvictable(var1);
      }

      EntryFactory(Object var3) {
         this();
      }
   }

   static enum Strength {
      STRONG {
         MapMakerInternalMap.ValueReference referenceValue(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, Object var3) {
            return new MapMakerInternalMap.StrongValueReference(var3);
         }

         Equivalence defaultEquivalence() {
            return Equivalence.equals();
         }
      },
      SOFT {
         MapMakerInternalMap.ValueReference referenceValue(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, Object var3) {
            return new MapMakerInternalMap.SoftValueReference(var1.valueReferenceQueue, var3, var2);
         }

         Equivalence defaultEquivalence() {
            return Equivalence.identity();
         }
      },
      WEAK {
         MapMakerInternalMap.ValueReference referenceValue(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, Object var3) {
            return new MapMakerInternalMap.WeakValueReference(var1.valueReferenceQueue, var3, var2);
         }

         Equivalence defaultEquivalence() {
            return Equivalence.identity();
         }
      };

      private static final MapMakerInternalMap.Strength[] $VALUES = new MapMakerInternalMap.Strength[]{STRONG, SOFT, WEAK};

      private Strength() {
      }

      abstract MapMakerInternalMap.ValueReference referenceValue(MapMakerInternalMap.Segment var1, MapMakerInternalMap.ReferenceEntry var2, Object var3);

      abstract Equivalence defaultEquivalence();

      Strength(Object var3) {
         this();
      }
   }
}
