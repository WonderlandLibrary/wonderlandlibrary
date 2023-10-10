package io.netty.util.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractEventExecutorGroup implements EventExecutorGroup {
   public Future submit(Runnable var1) {
      return this.next().submit(var1);
   }

   public Future submit(Runnable var1, Object var2) {
      return this.next().submit(var1, var2);
   }

   public Future submit(Callable var1) {
      return this.next().submit(var1);
   }

   public ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
      return this.next().schedule(var1, var2, var4);
   }

   public ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
      return this.next().schedule(var1, var2, var4);
   }

   public ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.next().scheduleAtFixedRate(var1, var2, var4, var6);
   }

   public ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.next().scheduleWithFixedDelay(var1, var2, var4, var6);
   }

   public Future shutdownGracefully() {
      return this.shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
   }

   /** @deprecated */
   @Deprecated
   public abstract void shutdown();

   /** @deprecated */
   @Deprecated
   public List shutdownNow() {
      this.shutdown();
      return Collections.emptyList();
   }

   public List invokeAll(Collection var1) throws InterruptedException {
      return this.next().invokeAll(var1);
   }

   public List invokeAll(Collection var1, long var2, TimeUnit var4) throws InterruptedException {
      return this.next().invokeAll(var1, var2, var4);
   }

   public Object invokeAny(Collection var1) throws InterruptedException, ExecutionException {
      return this.next().invokeAny(var1);
   }

   public Object invokeAny(Collection var1, long var2, TimeUnit var4) throws InterruptedException, ExecutionException, TimeoutException {
      return this.next().invokeAny(var1, var2, var4);
   }

   public void execute(Runnable var1) {
      this.next().execute(var1);
   }

   public java.util.concurrent.ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.scheduleWithFixedDelay(var1, var2, var4, var6);
   }

   public java.util.concurrent.ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.scheduleAtFixedRate(var1, var2, var4, var6);
   }

   public java.util.concurrent.ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
      return this.schedule(var1, var2, var4);
   }

   public java.util.concurrent.ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
      return this.schedule(var1, var2, var4);
   }

   public java.util.concurrent.Future submit(Runnable var1) {
      return this.submit(var1);
   }

   public java.util.concurrent.Future submit(Runnable var1, Object var2) {
      return this.submit(var1, var2);
   }

   public java.util.concurrent.Future submit(Callable var1) {
      return this.submit(var1);
   }
}
