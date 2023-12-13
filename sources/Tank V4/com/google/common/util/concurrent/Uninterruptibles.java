package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public final class Uninterruptibles {
   public static void awaitUninterruptibly(CountDownLatch var0) {
      boolean var1 = false;

      while(true) {
         try {
            var0.await();
            break;
         } catch (InterruptedException var4) {
            var1 = true;
         }
      }

      if (var1) {
         Thread.currentThread().interrupt();
      }

   }

   public static boolean awaitUninterruptibly(CountDownLatch var0, long var1, TimeUnit var3) {
      boolean var4 = false;
      long var5 = var3.toNanos(var1);
      long var7 = System.nanoTime() + var5;

      boolean var9;
      while(true) {
         try {
            var9 = var0.await(var5, TimeUnit.NANOSECONDS);
            break;
         } catch (InterruptedException var11) {
            var4 = true;
            var5 = var7 - System.nanoTime();
         }
      }

      if (var4) {
         Thread.currentThread().interrupt();
      }

      return var9;
   }

   public static void joinUninterruptibly(Thread var0) {
      boolean var1 = false;

      while(true) {
         try {
            var0.join();
            break;
         } catch (InterruptedException var4) {
            var1 = true;
         }
      }

      if (var1) {
         Thread.currentThread().interrupt();
      }

   }

   public static Object getUninterruptibly(Future var0) throws ExecutionException {
      boolean var1 = false;

      Object var2;
      while(true) {
         try {
            var2 = var0.get();
            break;
         } catch (InterruptedException var4) {
            var1 = true;
         }
      }

      if (var1) {
         Thread.currentThread().interrupt();
      }

      return var2;
   }

   public static Object getUninterruptibly(Future var0, long var1, TimeUnit var3) throws ExecutionException, TimeoutException {
      boolean var4 = false;
      long var5 = var3.toNanos(var1);
      long var7 = System.nanoTime() + var5;

      Object var9;
      while(true) {
         try {
            var9 = var0.get(var5, TimeUnit.NANOSECONDS);
            break;
         } catch (InterruptedException var11) {
            var4 = true;
            var5 = var7 - System.nanoTime();
         }
      }

      if (var4) {
         Thread.currentThread().interrupt();
      }

      return var9;
   }

   public static void joinUninterruptibly(Thread var0, long var1, TimeUnit var3) {
      Preconditions.checkNotNull(var0);
      boolean var4 = false;
      long var5 = var3.toNanos(var1);
      long var7 = System.nanoTime() + var5;

      while(true) {
         try {
            TimeUnit.NANOSECONDS.timedJoin(var0, var5);
            break;
         } catch (InterruptedException var11) {
            var4 = true;
            var5 = var7 - System.nanoTime();
         }
      }

      if (var4) {
         Thread.currentThread().interrupt();
      }

   }

   public static Object takeUninterruptibly(BlockingQueue var0) {
      boolean var1 = false;

      Object var2;
      while(true) {
         try {
            var2 = var0.take();
            break;
         } catch (InterruptedException var4) {
            var1 = true;
         }
      }

      if (var1) {
         Thread.currentThread().interrupt();
      }

      return var2;
   }

   public static void putUninterruptibly(BlockingQueue var0, Object var1) {
      boolean var2 = false;

      while(true) {
         try {
            var0.put(var1);
            break;
         } catch (InterruptedException var5) {
            var2 = true;
         }
      }

      if (var2) {
         Thread.currentThread().interrupt();
      }

   }

   public static void sleepUninterruptibly(long var0, TimeUnit var2) {
      boolean var3 = false;
      long var4 = var2.toNanos(var0);
      long var6 = System.nanoTime() + var4;

      while(true) {
         try {
            TimeUnit.NANOSECONDS.sleep(var4);
            break;
         } catch (InterruptedException var10) {
            var3 = true;
            var4 = var6 - System.nanoTime();
         }
      }

      if (var3) {
         Thread.currentThread().interrupt();
      }

   }

   private Uninterruptibles() {
   }
}
