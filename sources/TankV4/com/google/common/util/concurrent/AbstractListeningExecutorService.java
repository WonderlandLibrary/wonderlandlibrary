package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractListeningExecutorService extends AbstractExecutorService implements ListeningExecutorService {
   protected final ListenableFutureTask newTaskFor(Runnable var1, Object var2) {
      return ListenableFutureTask.create(var1, var2);
   }

   protected final ListenableFutureTask newTaskFor(Callable var1) {
      return ListenableFutureTask.create(var1);
   }

   public ListenableFuture submit(Runnable var1) {
      return (ListenableFuture)super.submit(var1);
   }

   public ListenableFuture submit(Runnable var1, @Nullable Object var2) {
      return (ListenableFuture)super.submit(var1, var2);
   }

   public ListenableFuture submit(Callable var1) {
      return (ListenableFuture)super.submit(var1);
   }

   public Future submit(Callable var1) {
      return this.submit(var1);
   }

   public Future submit(Runnable var1, Object var2) {
      return this.submit(var1, var2);
   }

   public Future submit(Runnable var1) {
      return this.submit(var1);
   }

   protected RunnableFuture newTaskFor(Callable var1) {
      return this.newTaskFor(var1);
   }

   protected RunnableFuture newTaskFor(Runnable var1, Object var2) {
      return this.newTaskFor(var1, var2);
   }
}
