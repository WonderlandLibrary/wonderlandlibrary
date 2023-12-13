package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

abstract class WrappingExecutorService implements ExecutorService {
   private final ExecutorService delegate;

   protected WrappingExecutorService(ExecutorService var1) {
      this.delegate = (ExecutorService)Preconditions.checkNotNull(var1);
   }

   protected abstract Callable wrapTask(Callable var1);

   protected Runnable wrapTask(Runnable var1) {
      Callable var2 = this.wrapTask(Executors.callable(var1, (Object)null));
      return new Runnable(this, var2) {
         final Callable val$wrapped;
         final WrappingExecutorService this$0;

         {
            this.this$0 = var1;
            this.val$wrapped = var2;
         }

         public void run() {
            try {
               this.val$wrapped.call();
            } catch (Exception var2) {
               Throwables.propagate(var2);
            }

         }
      };
   }

   private final ImmutableList wrapTasks(Collection var1) {
      ImmutableList.Builder var2 = ImmutableList.builder();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Callable var4 = (Callable)var3.next();
         var2.add((Object)this.wrapTask(var4));
      }

      return var2.build();
   }

   public final void execute(Runnable var1) {
      this.delegate.execute(this.wrapTask(var1));
   }

   public final Future submit(Callable var1) {
      return this.delegate.submit(this.wrapTask((Callable)Preconditions.checkNotNull(var1)));
   }

   public final Future submit(Runnable var1) {
      return this.delegate.submit(this.wrapTask(var1));
   }

   public final Future submit(Runnable var1, Object var2) {
      return this.delegate.submit(this.wrapTask(var1), var2);
   }

   public final List invokeAll(Collection var1) throws InterruptedException {
      return this.delegate.invokeAll(this.wrapTasks(var1));
   }

   public final List invokeAll(Collection var1, long var2, TimeUnit var4) throws InterruptedException {
      return this.delegate.invokeAll(this.wrapTasks(var1), var2, var4);
   }

   public final Object invokeAny(Collection var1) throws InterruptedException, ExecutionException {
      return this.delegate.invokeAny(this.wrapTasks(var1));
   }

   public final Object invokeAny(Collection var1, long var2, TimeUnit var4) throws InterruptedException, ExecutionException, TimeoutException {
      return this.delegate.invokeAny(this.wrapTasks(var1), var2, var4);
   }

   public final void shutdown() {
      this.delegate.shutdown();
   }

   public final List shutdownNow() {
      return this.delegate.shutdownNow();
   }

   public final boolean isShutdown() {
      return this.delegate.isShutdown();
   }

   public final boolean isTerminated() {
      return this.delegate.isTerminated();
   }

   public final boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      return this.delegate.awaitTermination(var1, var3);
   }
}
