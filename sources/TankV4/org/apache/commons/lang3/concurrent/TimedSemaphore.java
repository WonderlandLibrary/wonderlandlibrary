package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimedSemaphore {
   public static final int NO_LIMIT = 0;
   private static final int THREAD_POOL_SIZE = 1;
   private final ScheduledExecutorService executorService;
   private final long period;
   private final TimeUnit unit;
   private final boolean ownExecutor;
   private ScheduledFuture task;
   private long totalAcquireCount;
   private long periodCount;
   private int limit;
   private int acquireCount;
   private int lastCallsPerPeriod;
   private boolean shutdown;

   public TimedSemaphore(long var1, TimeUnit var3, int var4) {
      this((ScheduledExecutorService)null, var1, var3, var4);
   }

   public TimedSemaphore(ScheduledExecutorService var1, long var2, TimeUnit var4, int var5) {
      if (var2 <= 0L) {
         throw new IllegalArgumentException("Time period must be greater 0!");
      } else {
         this.period = var2;
         this.unit = var4;
         if (var1 != null) {
            this.executorService = var1;
            this.ownExecutor = false;
         } else {
            ScheduledThreadPoolExecutor var6 = new ScheduledThreadPoolExecutor(1);
            var6.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
            var6.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            this.executorService = var6;
            this.ownExecutor = true;
         }

         this.setLimit(var5);
      }
   }

   public final synchronized int getLimit() {
      return this.limit;
   }

   public final synchronized void setLimit(int var1) {
      this.limit = var1;
   }

   public synchronized void shutdown() {
      if (!this.shutdown) {
         if (this.ownExecutor) {
            this.getExecutorService().shutdownNow();
         }

         if (this.task != null) {
            this.task.cancel(false);
         }

         this.shutdown = true;
      }

   }

   public synchronized boolean isShutdown() {
      return this.shutdown;
   }

   public synchronized void acquire() throws InterruptedException {
      if (this.isShutdown()) {
         throw new IllegalStateException("TimedSemaphore is shut down!");
      } else {
         if (this.task == null) {
            this.task = this.startTimer();
         }

         boolean var1 = false;

         do {
            var1 = this.getLimit() <= 0 || this.acquireCount < this.getLimit();
            if (!var1) {
               this.wait();
            } else {
               ++this.acquireCount;
            }
         } while(!var1);

      }
   }

   public synchronized int getLastAcquiresPerPeriod() {
      return this.lastCallsPerPeriod;
   }

   public synchronized int getAcquireCount() {
      return this.acquireCount;
   }

   public synchronized int getAvailablePermits() {
      return this.getLimit() - this.getAcquireCount();
   }

   public synchronized double getAverageCallsPerPeriod() {
      return this.periodCount == 0L ? 0.0D : (double)this.totalAcquireCount / (double)this.periodCount;
   }

   public long getPeriod() {
      return this.period;
   }

   public TimeUnit getUnit() {
      return this.unit;
   }

   protected ScheduledExecutorService getExecutorService() {
      return this.executorService;
   }

   protected ScheduledFuture startTimer() {
      return this.getExecutorService().scheduleAtFixedRate(new Runnable(this) {
         final TimedSemaphore this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            this.this$0.endOfPeriod();
         }
      }, this.getPeriod(), this.getPeriod(), this.getUnit());
   }

   synchronized void endOfPeriod() {
      this.lastCallsPerPeriod = this.acquireCount;
      this.totalAcquireCount += (long)this.acquireCount;
      ++this.periodCount;
      this.acquireCount = 0;
      this.notifyAll();
   }
}
