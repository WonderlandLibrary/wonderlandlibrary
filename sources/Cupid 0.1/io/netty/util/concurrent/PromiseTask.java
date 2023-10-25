package io.netty.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

class PromiseTask<V> extends DefaultPromise<V> implements RunnableFuture<V> {
  protected final Callable<V> task;
  
  static <T> Callable<T> toCallable(Runnable runnable, T result) {
    return new RunnableAdapter<T>(runnable, result);
  }
  
  private static final class RunnableAdapter<T> implements Callable<T> {
    final Runnable task;
    
    final T result;
    
    RunnableAdapter(Runnable task, T result) {
      this.task = task;
      this.result = result;
    }
    
    public T call() {
      this.task.run();
      return this.result;
    }
    
    public String toString() {
      return "Callable(task: " + this.task + ", result: " + this.result + ')';
    }
  }
  
  PromiseTask(EventExecutor executor, Runnable runnable, V result) {
    this(executor, toCallable(runnable, result));
  }
  
  PromiseTask(EventExecutor executor, Callable<V> callable) {
    super(executor);
    this.task = callable;
  }
  
  public final int hashCode() {
    return System.identityHashCode(this);
  }
  
  public final boolean equals(Object obj) {
    return (this == obj);
  }
  
  public void run() {
    try {
      if (setUncancellableInternal()) {
        V result = this.task.call();
        setSuccessInternal(result);
      } 
    } catch (Throwable e) {
      setFailureInternal(e);
    } 
  }
  
  public final Promise<V> setFailure(Throwable cause) {
    throw new IllegalStateException();
  }
  
  protected final Promise<V> setFailureInternal(Throwable cause) {
    super.setFailure(cause);
    return this;
  }
  
  public final boolean tryFailure(Throwable cause) {
    return false;
  }
  
  protected final boolean tryFailureInternal(Throwable cause) {
    return super.tryFailure(cause);
  }
  
  public final Promise<V> setSuccess(V result) {
    throw new IllegalStateException();
  }
  
  protected final Promise<V> setSuccessInternal(V result) {
    super.setSuccess(result);
    return this;
  }
  
  public final boolean trySuccess(V result) {
    return false;
  }
  
  protected final boolean trySuccessInternal(V result) {
    return super.trySuccess(result);
  }
  
  public final boolean setUncancellable() {
    throw new IllegalStateException();
  }
  
  protected final boolean setUncancellableInternal() {
    return super.setUncancellable();
  }
  
  protected StringBuilder toStringBuilder() {
    StringBuilder buf = super.toStringBuilder();
    buf.setCharAt(buf.length() - 1, ',');
    buf.append(" task: ");
    buf.append(this.task);
    buf.append(')');
    return buf;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\PromiseTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */