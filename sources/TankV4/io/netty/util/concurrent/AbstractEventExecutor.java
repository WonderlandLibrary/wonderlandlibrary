package io.netty.util.concurrent;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractEventExecutor extends AbstractExecutorService implements EventExecutor {
   public EventExecutor next() {
      return this;
   }

   public boolean inEventLoop() {
      return this.inEventLoop(Thread.currentThread());
   }

   public Iterator iterator() {
      return new AbstractEventExecutor.EventExecutorIterator(this);
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

   public Promise newPromise() {
      return new DefaultPromise(this);
   }

   public ProgressivePromise newProgressivePromise() {
      return new DefaultProgressivePromise(this);
   }

   public Future newSucceededFuture(Object var1) {
      return new SucceededFuture(this, var1);
   }

   public Future newFailedFuture(Throwable var1) {
      return new FailedFuture(this, var1);
   }

   public Future submit(Runnable var1) {
      return (Future)super.submit(var1);
   }

   public Future submit(Runnable var1, Object var2) {
      return (Future)super.submit(var1, var2);
   }

   public Future submit(Callable var1) {
      return (Future)super.submit(var1);
   }

   protected final RunnableFuture newTaskFor(Runnable var1, Object var2) {
      return new PromiseTask(this, var1, var2);
   }

   protected final RunnableFuture newTaskFor(Callable var1) {
      return new PromiseTask(this, var1);
   }

   public ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
      throw new UnsupportedOperationException();
   }

   public ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
      throw new UnsupportedOperationException();
   }

   public ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      throw new UnsupportedOperationException();
   }

   public ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      throw new UnsupportedOperationException();
   }

   public java.util.concurrent.Future submit(Callable var1) {
      return this.submit(var1);
   }

   public java.util.concurrent.Future submit(Runnable var1, Object var2) {
      return this.submit(var1, var2);
   }

   public java.util.concurrent.Future submit(Runnable var1) {
      return this.submit(var1);
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

   private final class EventExecutorIterator implements Iterator {
      private boolean nextCalled;
      final AbstractEventExecutor this$0;

      private EventExecutorIterator(AbstractEventExecutor var1) {
         this.this$0 = var1;
      }

      public EventExecutor next() {
         if (this == false) {
            throw new NoSuchElementException();
         } else {
            this.nextCalled = true;
            return this.this$0;
         }
      }

      public void remove() {
         throw new UnsupportedOperationException("read-only");
      }

      public Object next() {
         return this.next();
      }

      EventExecutorIterator(AbstractEventExecutor var1, Object var2) {
         this(var1);
      }
   }
}
