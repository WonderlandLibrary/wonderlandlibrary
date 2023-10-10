package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HashedWheelTimer implements Timer {
   static final InternalLogger logger = InternalLoggerFactory.getInstance(HashedWheelTimer.class);
   private static final ResourceLeakDetector leakDetector = new ResourceLeakDetector(HashedWheelTimer.class, 1, (long)(Runtime.getRuntime().availableProcessors() * 4));
   private final ResourceLeak leak;
   private final HashedWheelTimer.Worker worker;
   final Thread workerThread;
   public static final int WORKER_STATE_INIT = 0;
   public static final int WORKER_STATE_STARTED = 1;
   public static final int WORKER_STATE_SHUTDOWN = 2;
   final AtomicInteger workerState;
   final long tickDuration;
   final Set[] wheel;
   final int mask;
   final ReadWriteLock lock;
   final CountDownLatch startTimeInitialized;
   volatile long startTime;
   volatile long tick;

   public HashedWheelTimer() {
      this(Executors.defaultThreadFactory());
   }

   public HashedWheelTimer(long var1, TimeUnit var3) {
      this(Executors.defaultThreadFactory(), var1, var3);
   }

   public HashedWheelTimer(long var1, TimeUnit var3, int var4) {
      this(Executors.defaultThreadFactory(), var1, var3, var4);
   }

   public HashedWheelTimer(ThreadFactory var1) {
      this(var1, 100L, TimeUnit.MILLISECONDS);
   }

   public HashedWheelTimer(ThreadFactory var1, long var2, TimeUnit var4) {
      this(var1, var2, var4, 512);
   }

   public HashedWheelTimer(ThreadFactory var1, long var2, TimeUnit var4, int var5) {
      this.worker = new HashedWheelTimer.Worker(this);
      this.workerState = new AtomicInteger();
      this.lock = new ReentrantReadWriteLock();
      this.startTimeInitialized = new CountDownLatch(1);
      if (var1 == null) {
         throw new NullPointerException("threadFactory");
      } else if (var4 == null) {
         throw new NullPointerException("unit");
      } else if (var2 <= 0L) {
         throw new IllegalArgumentException("tickDuration must be greater than 0: " + var2);
      } else if (var5 <= 0) {
         throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + var5);
      } else {
         this.wheel = createWheel(var5);
         this.mask = this.wheel.length - 1;
         this.tickDuration = var4.toNanos(var2);
         if (this.tickDuration >= Long.MAX_VALUE / (long)this.wheel.length) {
            throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", var2, Long.MAX_VALUE / (long)this.wheel.length));
         } else {
            this.workerThread = var1.newThread(this.worker);
            this.leak = leakDetector.open(this);
         }
      }
   }

   private static Set[] createWheel(int var0) {
      if (var0 <= 0) {
         throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + var0);
      } else if (var0 > 1073741824) {
         throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + var0);
      } else {
         var0 = normalizeTicksPerWheel(var0);
         Set[] var1 = new Set[var0];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = Collections.newSetFromMap(PlatformDependent.newConcurrentHashMap());
         }

         return var1;
      }
   }

   private static int normalizeTicksPerWheel(int var0) {
      int var1;
      for(var1 = 1; var1 < var0; var1 <<= 1) {
      }

      return var1;
   }

   public void start() {
      switch(this.workerState.get()) {
      case 0:
         if (this.workerState.compareAndSet(0, 1)) {
            this.workerThread.start();
         }
      case 1:
         break;
      case 2:
         throw new IllegalStateException("cannot be started once stopped");
      default:
         throw new Error("Invalid WorkerState");
      }

      while(this.startTime == 0L) {
         try {
            this.startTimeInitialized.await();
         } catch (InterruptedException var2) {
         }
      }

   }

   public Set stop() {
      if (Thread.currentThread() == this.workerThread) {
         throw new IllegalStateException(HashedWheelTimer.class.getSimpleName() + ".stop() cannot be called from " + TimerTask.class.getSimpleName());
      } else if (!this.workerState.compareAndSet(1, 2)) {
         this.workerState.set(2);
         if (this.leak != null) {
            this.leak.close();
         }

         return Collections.emptySet();
      } else {
         boolean var1 = false;

         while(this.workerThread.isAlive()) {
            this.workerThread.interrupt();

            try {
               this.workerThread.join(100L);
            } catch (InterruptedException var7) {
               var1 = true;
            }
         }

         if (var1) {
            Thread.currentThread().interrupt();
         }

         if (this.leak != null) {
            this.leak.close();
         }

         HashSet var2 = new HashSet();
         Set[] var3 = this.wheel;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Set var6 = var3[var5];
            var2.addAll(var6);
            var6.clear();
         }

         return Collections.unmodifiableSet(var2);
      }
   }

   public Timeout newTimeout(TimerTask var1, long var2, TimeUnit var4) {
      this.start();
      if (var1 == null) {
         throw new NullPointerException("task");
      } else if (var4 == null) {
         throw new NullPointerException("unit");
      } else {
         long var5 = System.nanoTime() + var4.toNanos(var2) - this.startTime;
         this.lock.readLock().lock();
         HashedWheelTimer.HashedWheelTimeout var7 = new HashedWheelTimer.HashedWheelTimeout(this, var1, var5);
         if (this.workerState.get() == 2) {
            throw new IllegalStateException("Cannot enqueue after shutdown");
         } else {
            this.wheel[var7.stopIndex].add(var7);
            this.lock.readLock().unlock();
            return var7;
         }
      }
   }

   private final class HashedWheelTimeout implements Timeout {
      private static final int ST_INIT = 0;
      private static final int ST_CANCELLED = 1;
      private static final int ST_EXPIRED = 2;
      private final TimerTask task;
      final long deadline;
      final int stopIndex;
      volatile long remainingRounds;
      private final AtomicInteger state;
      final HashedWheelTimer this$0;

      HashedWheelTimeout(HashedWheelTimer var1, TimerTask var2, long var3) {
         this.this$0 = var1;
         this.state = new AtomicInteger(0);
         this.task = var2;
         this.deadline = var3;
         long var5 = var3 / var1.tickDuration;
         long var7 = Math.max(var5, var1.tick);
         this.stopIndex = (int)(var7 & (long)var1.mask);
         this.remainingRounds = (var5 - var1.tick) / (long)var1.wheel.length;
      }

      public Timer timer() {
         return this.this$0;
      }

      public TimerTask task() {
         return this.task;
      }

      public boolean cancel() {
         if (!this.state.compareAndSet(0, 1)) {
            return false;
         } else {
            this.this$0.wheel[this.stopIndex].remove(this);
            return true;
         }
      }

      public boolean isExpired() {
         return this.state.get() != 0;
      }

      public void expire() {
         if (this.state.compareAndSet(0, 2)) {
            try {
               this.task.run(this);
            } catch (Throwable var2) {
               if (HashedWheelTimer.logger.isWarnEnabled()) {
                  HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', var2);
               }
            }

         }
      }

      public String toString() {
         // $FF: Couldn't be decompiled
      }
   }

   private final class Worker implements Runnable {
      final HashedWheelTimer this$0;

      Worker(HashedWheelTimer var1) {
         this.this$0 = var1;
      }

      public void run() {
         this.this$0.startTime = System.nanoTime();
         if (this.this$0.startTime == 0L) {
            this.this$0.startTime = 1L;
         }

         this.this$0.startTimeInitialized.countDown();
         ArrayList var1 = new ArrayList();

         do {
            long var2 = this.waitForNextTick();
            if (var2 > 0L) {
               this.fetchExpiredTimeouts(var1, var2);
               this.notifyExpiredTimeouts(var1);
            }
         } while(this.this$0.workerState.get() == 1);

      }

      private void fetchExpiredTimeouts(List var1, long var2) {
         this.this$0.lock.writeLock().lock();
         this.fetchExpiredTimeouts(var1, this.this$0.wheel[(int)(this.this$0.tick & (long)this.this$0.mask)].iterator(), var2);
         ++this.this$0.tick;
         this.this$0.lock.writeLock().unlock();
      }

      private void fetchExpiredTimeouts(List var1, Iterator var2, long var3) {
         while(var2.hasNext()) {
            HashedWheelTimer.HashedWheelTimeout var5 = (HashedWheelTimer.HashedWheelTimeout)var2.next();
            if (var5.remainingRounds <= 0L) {
               var2.remove();
               if (var5.deadline > var3) {
                  throw new Error(String.format("timeout.deadline (%d) > deadline (%d)", var5.deadline, var3));
               }

               var1.add(var5);
            } else {
               --var5.remainingRounds;
            }
         }

      }

      private void notifyExpiredTimeouts(List var1) {
         for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
            ((HashedWheelTimer.HashedWheelTimeout)var1.get(var2)).expire();
         }

         var1.clear();
      }

      private long waitForNextTick() {
         long var1 = this.this$0.tickDuration * (this.this$0.tick + 1L);

         while(true) {
            long var3 = System.nanoTime() - this.this$0.startTime;
            long var5 = (var1 - var3 + 999999L) / 1000000L;
            if (var5 <= 0L) {
               if (var3 == Long.MIN_VALUE) {
                  return -9223372036854775807L;
               }

               return var3;
            }

            if (PlatformDependent.isWindows()) {
               var5 = var5 / 10L * 10L;
            }

            try {
               Thread.sleep(var5);
            } catch (InterruptedException var8) {
               if (this.this$0.workerState.get() == 2) {
                  return Long.MIN_VALUE;
               }
            }
         }
      }
   }
}
