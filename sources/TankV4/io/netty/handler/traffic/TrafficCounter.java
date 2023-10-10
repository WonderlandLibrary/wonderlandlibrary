package io.netty.handler.traffic;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TrafficCounter {
   private final AtomicLong currentWrittenBytes = new AtomicLong();
   private final AtomicLong currentReadBytes = new AtomicLong();
   private final AtomicLong cumulativeWrittenBytes = new AtomicLong();
   private final AtomicLong cumulativeReadBytes = new AtomicLong();
   private long lastCumulativeTime;
   private long lastWriteThroughput;
   private long lastReadThroughput;
   private final AtomicLong lastTime = new AtomicLong();
   private long lastWrittenBytes;
   private long lastReadBytes;
   final AtomicLong checkInterval = new AtomicLong(1000L);
   final String name;
   private final AbstractTrafficShapingHandler trafficShapingHandler;
   private final ScheduledExecutorService executor;
   private Runnable monitor;
   private volatile ScheduledFuture scheduledFuture;
   final AtomicBoolean monitorActive = new AtomicBoolean();

   public void start() {
      AtomicLong var1;
      synchronized(var1 = this.lastTime){}
      if (!this.monitorActive.get()) {
         this.lastTime.set(System.currentTimeMillis());
         if (this.checkInterval.get() > 0L) {
            this.monitorActive.set(true);
            this.monitor = new TrafficCounter.TrafficMonitoringTask(this.trafficShapingHandler, this);
            this.scheduledFuture = this.executor.schedule(this.monitor, this.checkInterval.get(), TimeUnit.MILLISECONDS);
         }

      }
   }

   public void stop() {
      AtomicLong var1;
      synchronized(var1 = this.lastTime){}
      if (this.monitorActive.get()) {
         this.monitorActive.set(false);
         this.resetAccounting(System.currentTimeMillis());
         if (this.trafficShapingHandler != null) {
            this.trafficShapingHandler.doAccounting(this);
         }

         if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
         }

      }
   }

   void resetAccounting(long var1) {
      AtomicLong var3;
      synchronized(var3 = this.lastTime){}
      long var4 = var1 - this.lastTime.getAndSet(var1);
      if (var4 != 0L) {
         this.lastReadBytes = this.currentReadBytes.getAndSet(0L);
         this.lastWrittenBytes = this.currentWrittenBytes.getAndSet(0L);
         this.lastReadThroughput = this.lastReadBytes / var4 * 1000L;
         this.lastWriteThroughput = this.lastWrittenBytes / var4 * 1000L;
      }
   }

   public TrafficCounter(AbstractTrafficShapingHandler var1, ScheduledExecutorService var2, String var3, long var4) {
      this.trafficShapingHandler = var1;
      this.executor = var2;
      this.name = var3;
      this.lastCumulativeTime = System.currentTimeMillis();
      this.configure(var4);
   }

   public void configure(long var1) {
      long var3 = var1 / 10L * 10L;
      if (this.checkInterval.get() != var3) {
         this.checkInterval.set(var3);
         if (var3 <= 0L) {
            this.stop();
            this.lastTime.set(System.currentTimeMillis());
         } else {
            this.start();
         }
      }

   }

   void bytesRecvFlowControl(long var1) {
      this.currentReadBytes.addAndGet(var1);
      this.cumulativeReadBytes.addAndGet(var1);
   }

   void bytesWriteFlowControl(long var1) {
      this.currentWrittenBytes.addAndGet(var1);
      this.cumulativeWrittenBytes.addAndGet(var1);
   }

   public long checkInterval() {
      return this.checkInterval.get();
   }

   public long lastReadThroughput() {
      return this.lastReadThroughput;
   }

   public long lastWriteThroughput() {
      return this.lastWriteThroughput;
   }

   public long lastReadBytes() {
      return this.lastReadBytes;
   }

   public long lastWrittenBytes() {
      return this.lastWrittenBytes;
   }

   public long currentReadBytes() {
      return this.currentReadBytes.get();
   }

   public long currentWrittenBytes() {
      return this.currentWrittenBytes.get();
   }

   public long lastTime() {
      return this.lastTime.get();
   }

   public long cumulativeWrittenBytes() {
      return this.cumulativeWrittenBytes.get();
   }

   public long cumulativeReadBytes() {
      return this.cumulativeReadBytes.get();
   }

   public long lastCumulativeTime() {
      return this.lastCumulativeTime;
   }

   public void resetCumulativeTime() {
      this.lastCumulativeTime = System.currentTimeMillis();
      this.cumulativeReadBytes.set(0L);
      this.cumulativeWrittenBytes.set(0L);
   }

   public String name() {
      return this.name;
   }

   public String toString() {
      return "Monitor " + this.name + " Current Speed Read: " + (this.lastReadThroughput >> 10) + " KB/s, Write: " + (this.lastWriteThroughput >> 10) + " KB/s Current Read: " + (this.currentReadBytes.get() >> 10) + " KB Current Write: " + (this.currentWrittenBytes.get() >> 10) + " KB";
   }

   static ScheduledFuture access$002(TrafficCounter var0, ScheduledFuture var1) {
      return var0.scheduledFuture = var1;
   }

   static ScheduledExecutorService access$100(TrafficCounter var0) {
      return var0.executor;
   }

   private static class TrafficMonitoringTask implements Runnable {
      private final AbstractTrafficShapingHandler trafficShapingHandler1;
      private final TrafficCounter counter;

      protected TrafficMonitoringTask(AbstractTrafficShapingHandler var1, TrafficCounter var2) {
         this.trafficShapingHandler1 = var1;
         this.counter = var2;
      }

      public void run() {
         if (this.counter.monitorActive.get()) {
            long var1 = System.currentTimeMillis();
            this.counter.resetAccounting(var1);
            if (this.trafficShapingHandler1 != null) {
               this.trafficShapingHandler1.doAccounting(this.counter);
            }

            TrafficCounter.access$002(this.counter, TrafficCounter.access$100(this.counter).schedule(this, this.counter.checkInterval.get(), TimeUnit.MILLISECONDS));
         }
      }
   }
}
