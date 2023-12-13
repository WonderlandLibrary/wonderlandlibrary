package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Beta
public abstract class Striped {
   private static final int LARGE_LAZY_CUTOFF = 1024;
   private static final Supplier READ_WRITE_LOCK_SUPPLIER = new Supplier() {
      public ReadWriteLock get() {
         return new ReentrantReadWriteLock();
      }

      public Object get() {
         return this.get();
      }
   };
   private static final int ALL_SET = -1;

   private Striped() {
   }

   public abstract Object get(Object var1);

   public abstract Object getAt(int var1);

   abstract int indexFor(Object var1);

   public abstract int size();

   public Iterable bulkGet(Iterable var1) {
      Object[] var2 = Iterables.toArray(var1, Object.class);
      if (var2.length == 0) {
         return ImmutableList.of();
      } else {
         int[] var3 = new int[var2.length];

         int var4;
         for(var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = this.indexFor(var2[var4]);
         }

         Arrays.sort(var3);
         var4 = var3[0];
         var2[0] = this.getAt(var4);

         for(int var5 = 1; var5 < var2.length; ++var5) {
            int var6 = var3[var5];
            if (var6 == var4) {
               var2[var5] = var2[var5 - 1];
            } else {
               var2[var5] = this.getAt(var6);
               var4 = var6;
            }
         }

         List var7 = Arrays.asList(var2);
         return Collections.unmodifiableList(var7);
      }
   }

   public static Striped lock(int var0) {
      return new Striped.CompactStriped(var0, new Supplier() {
         public Lock get() {
            return new Striped.PaddedLock();
         }

         public Object get() {
            return this.get();
         }
      });
   }

   public static Striped lazyWeakLock(int var0) {
      return lazy(var0, new Supplier() {
         public Lock get() {
            return new ReentrantLock(false);
         }

         public Object get() {
            return this.get();
         }
      });
   }

   private static Striped lazy(int var0, Supplier var1) {
      return (Striped)(var0 < 1024 ? new Striped.SmallLazyStriped(var0, var1) : new Striped.LargeLazyStriped(var0, var1));
   }

   public static Striped semaphore(int var0, int var1) {
      return new Striped.CompactStriped(var0, new Supplier(var1) {
         final int val$permits;

         {
            this.val$permits = var1;
         }

         public Semaphore get() {
            return new Striped.PaddedSemaphore(this.val$permits);
         }

         public Object get() {
            return this.get();
         }
      });
   }

   public static Striped lazyWeakSemaphore(int var0, int var1) {
      return lazy(var0, new Supplier(var1) {
         final int val$permits;

         {
            this.val$permits = var1;
         }

         public Semaphore get() {
            return new Semaphore(this.val$permits, false);
         }

         public Object get() {
            return this.get();
         }
      });
   }

   public static Striped readWriteLock(int var0) {
      return new Striped.CompactStriped(var0, READ_WRITE_LOCK_SUPPLIER);
   }

   public static Striped lazyWeakReadWriteLock(int var0) {
      return lazy(var0, READ_WRITE_LOCK_SUPPLIER);
   }

   private static int ceilToPowerOfTwo(int var0) {
      return 1 << IntMath.log2(var0, RoundingMode.CEILING);
   }

   private static int smear(int var0) {
      var0 ^= var0 >>> 20 ^ var0 >>> 12;
      return var0 ^ var0 >>> 7 ^ var0 >>> 4;
   }

   Striped(Object var1) {
      this();
   }

   static int access$200(int var0) {
      return ceilToPowerOfTwo(var0);
   }

   static int access$300(int var0) {
      return smear(var0);
   }

   private static class PaddedSemaphore extends Semaphore {
      long q1;
      long q2;
      long q3;

      PaddedSemaphore(int var1) {
         super(var1, false);
      }
   }

   private static class PaddedLock extends ReentrantLock {
      long q1;
      long q2;
      long q3;

      PaddedLock() {
         super(false);
      }
   }

   @VisibleForTesting
   static class LargeLazyStriped extends Striped.PowerOfTwoStriped {
      final ConcurrentMap locks;
      final Supplier supplier;
      final int size;

      LargeLazyStriped(int var1, Supplier var2) {
         super(var1);
         this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
         this.supplier = var2;
         this.locks = (new MapMaker()).weakValues().makeMap();
      }

      public Object getAt(int var1) {
         if (this.size != Integer.MAX_VALUE) {
            Preconditions.checkElementIndex(var1, this.size());
         }

         Object var2 = this.locks.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            Object var3 = this.supplier.get();
            var2 = this.locks.putIfAbsent(var1, var3);
            return Objects.firstNonNull(var2, var3);
         }
      }

      public int size() {
         return this.size;
      }
   }

   @VisibleForTesting
   static class SmallLazyStriped extends Striped.PowerOfTwoStriped {
      final AtomicReferenceArray locks;
      final Supplier supplier;
      final int size;
      final ReferenceQueue queue = new ReferenceQueue();

      SmallLazyStriped(int var1, Supplier var2) {
         super(var1);
         this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
         this.locks = new AtomicReferenceArray(this.size);
         this.supplier = var2;
      }

      public Object getAt(int var1) {
         if (this.size != Integer.MAX_VALUE) {
            Preconditions.checkElementIndex(var1, this.size());
         }

         Striped.SmallLazyStriped.ArrayReference var2 = (Striped.SmallLazyStriped.ArrayReference)this.locks.get(var1);
         Object var3 = var2 == null ? null : var2.get();
         if (var3 != null) {
            return var3;
         } else {
            Object var4 = this.supplier.get();
            Striped.SmallLazyStriped.ArrayReference var5 = new Striped.SmallLazyStriped.ArrayReference(var4, var1, this.queue);

            do {
               if (this.locks.compareAndSet(var1, var2, var5)) {
                  this.drainQueue();
                  return var4;
               }

               var2 = (Striped.SmallLazyStriped.ArrayReference)this.locks.get(var1);
               var3 = var2 == null ? null : var2.get();
            } while(var3 == null);

            return var3;
         }
      }

      private void drainQueue() {
         Reference var1;
         while((var1 = this.queue.poll()) != null) {
            Striped.SmallLazyStriped.ArrayReference var2 = (Striped.SmallLazyStriped.ArrayReference)var1;
            this.locks.compareAndSet(var2.index, var2, (Object)null);
         }

      }

      public int size() {
         return this.size;
      }

      private static final class ArrayReference extends WeakReference {
         final int index;

         ArrayReference(Object var1, int var2, ReferenceQueue var3) {
            super(var1, var3);
            this.index = var2;
         }
      }
   }

   private static class CompactStriped extends Striped.PowerOfTwoStriped {
      private final Object[] array;

      private CompactStriped(int var1, Supplier var2) {
         super(var1);
         Preconditions.checkArgument(var1 <= 1073741824, "Stripes must be <= 2^30)");
         this.array = new Object[this.mask + 1];

         for(int var3 = 0; var3 < this.array.length; ++var3) {
            this.array[var3] = var2.get();
         }

      }

      public Object getAt(int var1) {
         return this.array[var1];
      }

      public int size() {
         return this.array.length;
      }

      CompactStriped(int var1, Supplier var2, Object var3) {
         this(var1, var2);
      }
   }

   private abstract static class PowerOfTwoStriped extends Striped {
      final int mask;

      PowerOfTwoStriped(int var1) {
         super(null);
         Preconditions.checkArgument(var1 > 0, "Stripes must be positive");
         this.mask = var1 > 1073741824 ? -1 : Striped.access$200(var1) - 1;
      }

      final int indexFor(Object var1) {
         int var2 = Striped.access$300(var1.hashCode());
         return var2 & this.mask;
      }

      public final Object get(Object var1) {
         return this.getAt(this.indexFor(var1));
      }
   }
}
