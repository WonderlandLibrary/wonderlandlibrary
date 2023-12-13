package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

class ComputingConcurrentHashMap extends MapMakerInternalMap {
   final Function computingFunction;
   private static final long serialVersionUID = 4L;

   ComputingConcurrentHashMap(MapMaker var1, Function var2) {
      super(var1);
      this.computingFunction = (Function)Preconditions.checkNotNull(var2);
   }

   MapMakerInternalMap.Segment createSegment(int var1, int var2) {
      return new ComputingConcurrentHashMap.ComputingSegment(this, var1, var2);
   }

   ComputingConcurrentHashMap.ComputingSegment segmentFor(int var1) {
      return (ComputingConcurrentHashMap.ComputingSegment)super.segmentFor(var1);
   }

   Object getOrCompute(Object var1) throws ExecutionException {
      int var2 = this.hash(Preconditions.checkNotNull(var1));
      return this.segmentFor(var2).getOrCompute(var1, var2, this.computingFunction);
   }

   Object writeReplace() {
      return new ComputingConcurrentHashMap.ComputingSerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this, this.computingFunction);
   }

   MapMakerInternalMap.Segment segmentFor(int var1) {
      return this.segmentFor(var1);
   }

   static final class ComputingSerializationProxy extends MapMakerInternalMap.AbstractSerializationProxy {
      final Function computingFunction;
      private static final long serialVersionUID = 4L;

      ComputingSerializationProxy(MapMakerInternalMap.Strength var1, MapMakerInternalMap.Strength var2, Equivalence var3, Equivalence var4, long var5, long var7, int var9, int var10, MapMaker.RemovalListener var11, ConcurrentMap var12, Function var13) {
         super(var1, var2, var3, var4, var5, var7, var9, var10, var11, var12);
         this.computingFunction = var13;
      }

      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         this.writeMapTo(var1);
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         MapMaker var2 = this.readMapMaker(var1);
         this.delegate = var2.makeComputingMap(this.computingFunction);
         this.readEntries(var1);
      }

      Object readResolve() {
         return this.delegate;
      }
   }

   private static final class ComputingValueReference implements MapMakerInternalMap.ValueReference {
      final Function computingFunction;
      @GuardedBy("ComputingValueReference.this")
      volatile MapMakerInternalMap.ValueReference computedReference = MapMakerInternalMap.unset();

      public ComputingValueReference(Function var1) {
         this.computingFunction = var1;
      }

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
         return true;
      }

      public Object waitForValue() throws ExecutionException {
         if (this.computedReference == MapMakerInternalMap.UNSET) {
            boolean var1 = false;
            synchronized(this){}

            while(this.computedReference == MapMakerInternalMap.UNSET) {
               try {
                  this.wait();
               } catch (InterruptedException var6) {
                  var1 = true;
               }
            }

            if (var1) {
               Thread.currentThread().interrupt();
            }
         }

         return this.computedReference.waitForValue();
      }

      public void clear(MapMakerInternalMap.ValueReference var1) {
         this.setValueReference(var1);
      }

      Object compute(Object var1, int var2) throws ExecutionException {
         Object var3;
         try {
            var3 = this.computingFunction.apply(var1);
         } catch (Throwable var5) {
            this.setValueReference(new ComputingConcurrentHashMap.ComputationExceptionReference(var5));
            throw new ExecutionException(var5);
         }

         this.setValueReference(new ComputingConcurrentHashMap.ComputedReference(var3));
         return var3;
      }

      void setValueReference(MapMakerInternalMap.ValueReference var1) {
         synchronized(this){}
         if (this.computedReference == MapMakerInternalMap.UNSET) {
            this.computedReference = var1;
            this.notifyAll();
         }

      }
   }

   private static final class ComputedReference implements MapMakerInternalMap.ValueReference {
      final Object value;

      ComputedReference(@Nullable Object var1) {
         this.value = var1;
      }

      public Object get() {
         return this.value;
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

   private static final class ComputationExceptionReference implements MapMakerInternalMap.ValueReference {
      final Throwable t;

      ComputationExceptionReference(Throwable var1) {
         this.t = var1;
      }

      public Object get() {
         return null;
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

      public Object waitForValue() throws ExecutionException {
         throw new ExecutionException(this.t);
      }

      public void clear(MapMakerInternalMap.ValueReference var1) {
      }
   }

   static final class ComputingSegment extends MapMakerInternalMap.Segment {
      ComputingSegment(MapMakerInternalMap var1, int var2, int var3) {
         super(var1, var2, var3);
      }

      Object getOrCompute(Object var1, int var2, Function var3) throws ExecutionException {
         MapMakerInternalMap.ReferenceEntry var4;
         Object var5;
         do {
            var4 = this.getEntry(var1, var2);
            if (var4 != null) {
               var5 = this.getLiveValue(var4);
               if (var5 != null) {
                  this.recordRead(var4);
                  this.postReadCleanup();
                  return var5;
               }
            }

            if (var4 == null || !var4.getValueReference().isComputingReference()) {
               boolean var17 = true;
               ComputingConcurrentHashMap.ComputingValueReference var6 = null;
               this.lock();
               this.preWriteCleanup();
               int var7 = this.count - 1;
               AtomicReferenceArray var8 = this.table;
               int var9 = var2 & var8.length() - 1;
               MapMakerInternalMap.ReferenceEntry var10 = (MapMakerInternalMap.ReferenceEntry)var8.get(var9);
               var4 = var10;

               while(true) {
                  if (var4 != null) {
                     Object var11 = var4.getKey();
                     if (var4.getHash() != var2 || var11 == null || !this.map.keyEquivalence.equivalent(var1, var11)) {
                        var4 = var4.getNext();
                        continue;
                     }

                     MapMakerInternalMap.ValueReference var12 = var4.getValueReference();
                     if (var12.isComputingReference()) {
                        var17 = false;
                     } else {
                        Object var13 = var4.getValueReference().get();
                        if (var13 == null) {
                           this.enqueueNotification(var11, var2, var13, MapMaker.RemovalCause.COLLECTED);
                        } else {
                           if (!this.map.expires() || !this.map.isExpired(var4)) {
                              this.recordLockedRead(var4);
                              this.unlock();
                              this.postWriteCleanup();
                              this.postReadCleanup();
                              return var13;
                           }

                           this.enqueueNotification(var11, var2, var13, MapMaker.RemovalCause.EXPIRED);
                        }

                        this.evictionQueue.remove(var4);
                        this.expirationQueue.remove(var4);
                        this.count = var7;
                     }
                  }

                  if (var17) {
                     var6 = new ComputingConcurrentHashMap.ComputingValueReference(var3);
                     if (var4 == null) {
                        var4 = this.newEntry(var1, var2, var10);
                        var4.setValueReference(var6);
                        var8.set(var9, var4);
                     } else {
                        var4.setValueReference(var6);
                     }
                  }

                  this.unlock();
                  this.postWriteCleanup();
                  if (var17) {
                     Object var18 = this.compute(var1, var2, var4, var6);
                     this.postReadCleanup();
                     return var18;
                  }
                  break;
               }
            }

            Preconditions.checkState(!Thread.holdsLock(var4), "Recursive computation");
            var5 = var4.getValueReference().waitForValue();
         } while(var5 == null);

         this.recordRead(var4);
         this.postReadCleanup();
         return var5;
      }

      Object compute(Object var1, int var2, MapMakerInternalMap.ReferenceEntry var3, ComputingConcurrentHashMap.ComputingValueReference var4) throws ExecutionException {
         Object var5 = null;
         long var6 = System.nanoTime();
         long var8 = 0L;
         synchronized(var3){}
         var5 = var4.compute(var1, var2);
         var8 = System.nanoTime();
         if (var5 != null) {
            Object var10 = this.put(var1, var2, var5, true);
            if (var10 != null) {
               this.enqueueNotification(var1, var2, var5, MapMaker.RemovalCause.REPLACED);
            }
         }

         if (var8 == 0L) {
            var8 = System.nanoTime();
         }

         if (var5 == null) {
            this.clearValue(var1, var2, var4);
         }

         return var5;
      }
   }
}
