package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.concurrent.GuardedBy;

@Beta
public final class Monitor {
   private final boolean fair;
   private final ReentrantLock lock;
   @GuardedBy("lock")
   private Monitor.Guard activeGuards;

   public Monitor() {
      this(false);
   }

   public Monitor(boolean var1) {
      this.activeGuards = null;
      this.fair = var1;
      this.lock = new ReentrantLock(var1);
   }

   public void enter() {
      this.lock.lock();
   }

   public void enterInterruptibly() throws InterruptedException {
      this.lock.lockInterruptibly();
   }

   public boolean enterInterruptibly(long var1, TimeUnit var3) throws InterruptedException {
      return this.lock.tryLock(var1, var3);
   }

   public boolean tryEnter() {
      return this.lock.tryLock();
   }

   public void enterWhen(Monitor.Guard var1) throws InterruptedException {
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         boolean var3 = var2.isHeldByCurrentThread();
         var2.lockInterruptibly();
         boolean var4 = false;
         if (!var1.isSatisfied()) {
            this.await(var1, var3);
         }

         var4 = true;
         if (!var4) {
            this.leave();
         }

      }
   }

   public void enterWhenUninterruptibly(Monitor.Guard var1) {
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         boolean var3 = var2.isHeldByCurrentThread();
         var2.lock();
         boolean var4 = false;
         if (!var1.isSatisfied()) {
            this.awaitUninterruptibly(var1, var3);
         }

         var4 = true;
         if (!var4) {
            this.leave();
         }

      }
   }

   public boolean enterWhen(Monitor.Guard var1, long var2, TimeUnit var4) throws InterruptedException {
      long var5 = var4.toNanos(var2);
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var7 = this.lock;
         boolean var8 = var7.isHeldByCurrentThread();
         if (this.fair || !var7.tryLock()) {
            long var9 = System.nanoTime() + var5;
            if (!var7.tryLock(var2, var4)) {
               return false;
            }

            var5 = var9 - System.nanoTime();
         }

         boolean var10;
         Monitor var10000;
         label33: {
            boolean var15 = false;
            var10 = true;
            if (!var1.isSatisfied()) {
               var10000 = this;
               if (!var8) {
                  boolean var10003 = false;
                  break label33;
               }
            }

            var10000 = true;
         }

         Monitor var16 = var10000;
         var10 = false;
         if (var16 == false) {
            if (var10 && !var8) {
               this.signalNextWaiter();
            }

            var7.unlock();
         }

         return (boolean)var16;
      }
   }

   public boolean enterWhenUninterruptibly(Monitor.Guard var1, long var2, TimeUnit var4) {
      long var5 = var4.toNanos(var2);
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var7 = this.lock;
         long var8 = System.nanoTime() + var5;
         boolean var10 = var7.isHeldByCurrentThread();
         boolean var11 = Thread.interrupted();
         boolean var12;
         if (this.fair || !var7.tryLock()) {
            label73: {
               var12 = false;

               boolean var18;
               while(true) {
                  try {
                     var12 = var7.tryLock(var5, TimeUnit.NANOSECONDS);
                     if (!var12) {
                        var18 = false;
                        break;
                     }
                  } catch (InterruptedException var17) {
                     var11 = true;
                  }

                  var5 = var8 - System.nanoTime();
                  if (var12) {
                     break label73;
                  }
               }

               if (var11) {
                  Thread.currentThread().interrupt();
               }

               return var18;
            }
         }

         var12 = false;

         Monitor var13;
         Monitor var19;
         while(true) {
            try {
               Monitor var10000;
               label48: {
                  if (!var1.isSatisfied()) {
                     var10000 = this;
                     if (!var10) {
                        boolean var10003 = false;
                        break label48;
                     }
                  }

                  var10000 = true;
               }

               var19 = var10000;
               var13 = var10000;
               break;
            } catch (InterruptedException var16) {
               var11 = true;
               var10 = false;
               var5 = var8 - System.nanoTime();
            }
         }

         if (var19 == false) {
            var7.unlock();
         }

         if (var11) {
            Thread.currentThread().interrupt();
         }

         return (boolean)var13;
      }
   }

   public boolean enterIf(Monitor.Guard var1) {
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         var2.lock();
         boolean var3 = false;
         boolean var4 = var3 = var1.isSatisfied();
         if (!var3) {
            var2.unlock();
         }

         return var4;
      }
   }

   public boolean enterIfInterruptibly(Monitor.Guard var1) throws InterruptedException {
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         var2.lockInterruptibly();
         boolean var3 = false;
         boolean var4 = var3 = var1.isSatisfied();
         if (!var3) {
            var2.unlock();
         }

         return var4;
      }
   }

   public boolean enterIf(Monitor.Guard var1, long var2, TimeUnit var4) {
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else if (var4 == false) {
         return false;
      } else {
         boolean var5 = false;
         boolean var6 = var5 = var1.isSatisfied();
         if (!var5) {
            this.lock.unlock();
         }

         return var6;
      }
   }

   public boolean enterIfInterruptibly(Monitor.Guard var1, long var2, TimeUnit var4) throws InterruptedException {
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var5 = this.lock;
         if (!var5.tryLock(var2, var4)) {
            return false;
         } else {
            boolean var6 = false;
            boolean var7 = var6 = var1.isSatisfied();
            if (!var6) {
               var5.unlock();
            }

            return var7;
         }
      }
   }

   public boolean tryEnterIf(Monitor.Guard var1) {
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         if (!var2.tryLock()) {
            return false;
         } else {
            boolean var3 = false;
            boolean var4 = var3 = var1.isSatisfied();
            if (!var3) {
               var2.unlock();
            }

            return var4;
         }
      }
   }

   public void waitFor(Monitor.Guard var1) throws InterruptedException {
      if (!(var1.monitor == this & this.lock.isHeldByCurrentThread())) {
         throw new IllegalMonitorStateException();
      } else {
         if (!var1.isSatisfied()) {
            this.await(var1, true);
         }

      }
   }

   public void waitForUninterruptibly(Monitor.Guard var1) {
      if (!(var1.monitor == this & this.lock.isHeldByCurrentThread())) {
         throw new IllegalMonitorStateException();
      } else {
         if (!var1.isSatisfied()) {
            this.awaitUninterruptibly(var1, true);
         }

      }
   }

   public boolean waitFor(Monitor.Guard var1, long var2, TimeUnit var4) throws InterruptedException {
      long var5 = var4.toNanos(var2);
      if (!(var1.monitor == this & this.lock.isHeldByCurrentThread())) {
         throw new IllegalMonitorStateException();
      } else {
         if (!var1.isSatisfied()) {
            ;
         }

         return true;
      }
   }

   public boolean waitForUninterruptibly(Monitor.Guard var1, long var2, TimeUnit var4) {
      long var5 = var4.toNanos(var2);
      if (!(var1.monitor == this & this.lock.isHeldByCurrentThread())) {
         throw new IllegalMonitorStateException();
      } else if (var1.isSatisfied()) {
         return true;
      } else {
         boolean var7 = true;
         long var8 = System.nanoTime() + var5;
         boolean var10 = Thread.interrupted();

         boolean var11;
         while(true) {
            try {
               var11 = this.awaitNanos(var1, var5, var7);
               break;
            } catch (InterruptedException var14) {
               var10 = true;
               if (var1.isSatisfied()) {
                  boolean var12 = true;
                  if (var10) {
                     Thread.currentThread().interrupt();
                  }

                  return var12;
               }

               var7 = false;
               var5 = var8 - System.nanoTime();
            }
         }

         if (var10) {
            Thread.currentThread().interrupt();
         }

         return var11;
      }
   }

   public void leave() {
      ReentrantLock var1 = this.lock;
      if (var1.getHoldCount() == 1) {
         this.signalNextWaiter();
      }

      var1.unlock();
   }

   public boolean isFair() {
      return this.fair;
   }

   public boolean isOccupied() {
      return this.lock.isLocked();
   }

   public boolean isOccupiedByCurrentThread() {
      return this.lock.isHeldByCurrentThread();
   }

   public int getOccupiedDepth() {
      return this.lock.getHoldCount();
   }

   public int getQueueLength() {
      return this.lock.getQueueLength();
   }

   public boolean hasQueuedThreads() {
      return this.lock.hasQueuedThreads();
   }

   public boolean hasQueuedThread(Thread var1) {
      return this.lock.hasQueuedThread(var1);
   }

   public boolean hasWaiters(Monitor.Guard var1) {
      return this.getWaitQueueLength(var1) > 0;
   }

   public int getWaitQueueLength(Monitor.Guard var1) {
      if (var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         this.lock.lock();
         int var2 = var1.waiterCount;
         this.lock.unlock();
         return var2;
      }
   }

   @GuardedBy("lock")
   private void signalNextWaiter() {
      for(Monitor.Guard var1 = this.activeGuards; var1 != null; var1 = var1.next) {
         if (this.isSatisfied(var1)) {
            var1.condition.signal();
            break;
         }
      }

   }

   @GuardedBy("lock")
   private boolean isSatisfied(Monitor.Guard var1) {
      try {
         return var1.isSatisfied();
      } catch (Throwable var3) {
         this.signalAllWaiters();
         throw Throwables.propagate(var3);
      }
   }

   @GuardedBy("lock")
   private void signalAllWaiters() {
      for(Monitor.Guard var1 = this.activeGuards; var1 != null; var1 = var1.next) {
         var1.condition.signalAll();
      }

   }

   @GuardedBy("lock")
   private void beginWaitingFor(Monitor.Guard var1) {
      int var2 = var1.waiterCount++;
      if (var2 == 0) {
         var1.next = this.activeGuards;
         this.activeGuards = var1;
      }

   }

   @GuardedBy("lock")
   private void endWaitingFor(Monitor.Guard var1) {
      int var2 = --var1.waiterCount;
      if (var2 == 0) {
         Monitor.Guard var3 = this.activeGuards;

         Monitor.Guard var4;
         for(var4 = null; var3 != var1; var3 = var3.next) {
            var4 = var3;
         }

         if (var4 == null) {
            this.activeGuards = var3.next;
         } else {
            var4.next = var3.next;
         }

         var3.next = null;
      }

   }

   @GuardedBy("lock")
   private void await(Monitor.Guard var1, boolean var2) throws InterruptedException {
      if (var2) {
         this.signalNextWaiter();
      }

      this.beginWaitingFor(var1);

      do {
         var1.condition.await();
      } while(!var1.isSatisfied());

      this.endWaitingFor(var1);
   }

   @GuardedBy("lock")
   private void awaitUninterruptibly(Monitor.Guard var1, boolean var2) {
      if (var2) {
         this.signalNextWaiter();
      }

      this.beginWaitingFor(var1);

      do {
         var1.condition.awaitUninterruptibly();
      } while(!var1.isSatisfied());

      this.endWaitingFor(var1);
   }

   static ReentrantLock access$000(Monitor var0) {
      return var0.lock;
   }

   @Beta
   public abstract static class Guard {
      final Monitor monitor;
      final Condition condition;
      @GuardedBy("monitor.lock")
      int waiterCount = 0;
      @GuardedBy("monitor.lock")
      Monitor.Guard next;

      protected Guard(Monitor var1) {
         this.monitor = (Monitor)Preconditions.checkNotNull(var1, "monitor");
         this.condition = Monitor.access$000(var1).newCondition();
      }

      public abstract boolean isSatisfied();
   }
}
