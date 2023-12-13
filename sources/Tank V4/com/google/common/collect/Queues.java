package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public final class Queues {
   private Queues() {
   }

   public static ArrayBlockingQueue newArrayBlockingQueue(int var0) {
      return new ArrayBlockingQueue(var0);
   }

   public static ArrayDeque newArrayDeque() {
      return new ArrayDeque();
   }

   public static ArrayDeque newArrayDeque(Iterable var0) {
      if (var0 instanceof Collection) {
         return new ArrayDeque(Collections2.cast(var0));
      } else {
         ArrayDeque var1 = new ArrayDeque();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static ConcurrentLinkedQueue newConcurrentLinkedQueue() {
      return new ConcurrentLinkedQueue();
   }

   public static ConcurrentLinkedQueue newConcurrentLinkedQueue(Iterable var0) {
      if (var0 instanceof Collection) {
         return new ConcurrentLinkedQueue(Collections2.cast(var0));
      } else {
         ConcurrentLinkedQueue var1 = new ConcurrentLinkedQueue();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static LinkedBlockingDeque newLinkedBlockingDeque() {
      return new LinkedBlockingDeque();
   }

   public static LinkedBlockingDeque newLinkedBlockingDeque(int var0) {
      return new LinkedBlockingDeque(var0);
   }

   public static LinkedBlockingDeque newLinkedBlockingDeque(Iterable var0) {
      if (var0 instanceof Collection) {
         return new LinkedBlockingDeque(Collections2.cast(var0));
      } else {
         LinkedBlockingDeque var1 = new LinkedBlockingDeque();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static LinkedBlockingQueue newLinkedBlockingQueue() {
      return new LinkedBlockingQueue();
   }

   public static LinkedBlockingQueue newLinkedBlockingQueue(int var0) {
      return new LinkedBlockingQueue(var0);
   }

   public static LinkedBlockingQueue newLinkedBlockingQueue(Iterable var0) {
      if (var0 instanceof Collection) {
         return new LinkedBlockingQueue(Collections2.cast(var0));
      } else {
         LinkedBlockingQueue var1 = new LinkedBlockingQueue();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static PriorityBlockingQueue newPriorityBlockingQueue() {
      return new PriorityBlockingQueue();
   }

   public static PriorityBlockingQueue newPriorityBlockingQueue(Iterable var0) {
      if (var0 instanceof Collection) {
         return new PriorityBlockingQueue(Collections2.cast(var0));
      } else {
         PriorityBlockingQueue var1 = new PriorityBlockingQueue();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static PriorityQueue newPriorityQueue() {
      return new PriorityQueue();
   }

   public static PriorityQueue newPriorityQueue(Iterable var0) {
      if (var0 instanceof Collection) {
         return new PriorityQueue(Collections2.cast(var0));
      } else {
         PriorityQueue var1 = new PriorityQueue();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static SynchronousQueue newSynchronousQueue() {
      return new SynchronousQueue();
   }

   @Beta
   public static int drain(BlockingQueue var0, Collection var1, int var2, long var3, TimeUnit var5) throws InterruptedException {
      Preconditions.checkNotNull(var1);
      long var6 = System.nanoTime() + var5.toNanos(var3);
      int var8 = 0;

      while(var8 < var2) {
         var8 += var0.drainTo(var1, var2 - var8);
         if (var8 < var2) {
            Object var9 = var0.poll(var6 - System.nanoTime(), TimeUnit.NANOSECONDS);
            if (var9 == null) {
               break;
            }

            var1.add(var9);
            ++var8;
         }
      }

      return var8;
   }

   @Beta
   public static int drainUninterruptibly(BlockingQueue var0, Collection var1, int var2, long var3, TimeUnit var5) {
      Preconditions.checkNotNull(var1);
      long var6 = System.nanoTime() + var5.toNanos(var3);
      int var8 = 0;
      boolean var9 = false;

      while(var8 < var2) {
         var8 += var0.drainTo(var1, var2 - var8);
         if (var8 < var2) {
            Object var10;
            while(true) {
               try {
                  var10 = var0.poll(var6 - System.nanoTime(), TimeUnit.NANOSECONDS);
                  break;
               } catch (InterruptedException var13) {
                  var9 = true;
               }
            }

            if (var10 == null) {
               break;
            }

            var1.add(var10);
            ++var8;
         }
      }

      if (var9) {
         Thread.currentThread().interrupt();
      }

      return var8;
   }

   @Beta
   public static Queue synchronizedQueue(Queue var0) {
      return Synchronized.queue(var0, (Object)null);
   }

   @Beta
   public static Deque synchronizedDeque(Deque var0) {
      return Synchronized.deque(var0, (Object)null);
   }
}
