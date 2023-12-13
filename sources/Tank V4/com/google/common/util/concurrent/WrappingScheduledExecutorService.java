package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

abstract class WrappingScheduledExecutorService extends WrappingExecutorService implements ScheduledExecutorService {
   final ScheduledExecutorService delegate;

   protected WrappingScheduledExecutorService(ScheduledExecutorService var1) {
      super(var1);
      this.delegate = var1;
   }

   public final ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
      return this.delegate.schedule(this.wrapTask(var1), var2, var4);
   }

   public final ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
      return this.delegate.schedule(this.wrapTask(var1), var2, var4);
   }

   public final ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.delegate.scheduleAtFixedRate(this.wrapTask(var1), var2, var4, var6);
   }

   public final ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.delegate.scheduleWithFixedDelay(this.wrapTask(var1), var2, var4, var6);
   }
}
