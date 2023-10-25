package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
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
  
  protected WrappingExecutorService(ExecutorService delegate) {
    this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
  }
  
  protected abstract <T> Callable<T> wrapTask(Callable<T> paramCallable);
  
  protected Runnable wrapTask(Runnable command) {
    final Callable<Object> wrapped = wrapTask(Executors.callable(command, null));
    return new Runnable() {
        public void run() {
          try {
            wrapped.call();
          } catch (Exception e) {
            Throwables.propagate(e);
          } 
        }
      };
  }
  
  private final <T> ImmutableList<Callable<T>> wrapTasks(Collection<? extends Callable<T>> tasks) {
    ImmutableList.Builder<Callable<T>> builder = ImmutableList.builder();
    for (Callable<T> task : tasks)
      builder.add(wrapTask(task)); 
    return builder.build();
  }
  
  public final void execute(Runnable command) {
    this.delegate.execute(wrapTask(command));
  }
  
  public final <T> Future<T> submit(Callable<T> task) {
    return this.delegate.submit(wrapTask((Callable<T>)Preconditions.checkNotNull(task)));
  }
  
  public final Future<?> submit(Runnable task) {
    return this.delegate.submit(wrapTask(task));
  }
  
  public final <T> Future<T> submit(Runnable task, T result) {
    return this.delegate.submit(wrapTask(task), result);
  }
  
  public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
    return this.delegate.invokeAll((Collection<? extends Callable<T>>)wrapTasks(tasks));
  }
  
  public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
    return this.delegate.invokeAll((Collection<? extends Callable<T>>)wrapTasks(tasks), timeout, unit);
  }
  
  public final <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    return this.delegate.invokeAny((Collection<? extends Callable<T>>)wrapTasks(tasks));
  }
  
  public final <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return this.delegate.invokeAny((Collection<? extends Callable<T>>)wrapTasks(tasks), timeout, unit);
  }
  
  public final void shutdown() {
    this.delegate.shutdown();
  }
  
  public final List<Runnable> shutdownNow() {
    return this.delegate.shutdownNow();
  }
  
  public final boolean isShutdown() {
    return this.delegate.isShutdown();
  }
  
  public final boolean isTerminated() {
    return this.delegate.isTerminated();
  }
  
  public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return this.delegate.awaitTermination(timeout, unit);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\WrappingExecutorService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */