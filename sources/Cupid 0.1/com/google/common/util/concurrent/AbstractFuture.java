package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import javax.annotation.Nullable;

public abstract class AbstractFuture<V> implements ListenableFuture<V> {
  private final Sync<V> sync = new Sync<V>();
  
  private final ExecutionList executionList = new ExecutionList();
  
  public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
    return this.sync.get(unit.toNanos(timeout));
  }
  
  public V get() throws InterruptedException, ExecutionException {
    return this.sync.get();
  }
  
  public boolean isDone() {
    return this.sync.isDone();
  }
  
  public boolean isCancelled() {
    return this.sync.isCancelled();
  }
  
  public boolean cancel(boolean mayInterruptIfRunning) {
    if (!this.sync.cancel(mayInterruptIfRunning))
      return false; 
    this.executionList.execute();
    if (mayInterruptIfRunning)
      interruptTask(); 
    return true;
  }
  
  protected void interruptTask() {}
  
  protected final boolean wasInterrupted() {
    return this.sync.wasInterrupted();
  }
  
  public void addListener(Runnable listener, Executor exec) {
    this.executionList.add(listener, exec);
  }
  
  protected boolean set(@Nullable V value) {
    boolean result = this.sync.set(value);
    if (result)
      this.executionList.execute(); 
    return result;
  }
  
  protected boolean setException(Throwable throwable) {
    boolean result = this.sync.setException((Throwable)Preconditions.checkNotNull(throwable));
    if (result)
      this.executionList.execute(); 
    return result;
  }
  
  static final class Sync<V> extends AbstractQueuedSynchronizer {
    private static final long serialVersionUID = 0L;
    
    static final int RUNNING = 0;
    
    static final int COMPLETING = 1;
    
    static final int COMPLETED = 2;
    
    static final int CANCELLED = 4;
    
    static final int INTERRUPTED = 8;
    
    private V value;
    
    private Throwable exception;
    
    protected int tryAcquireShared(int ignored) {
      if (isDone())
        return 1; 
      return -1;
    }
    
    protected boolean tryReleaseShared(int finalState) {
      setState(finalState);
      return true;
    }
    
    V get(long nanos) throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
      if (!tryAcquireSharedNanos(-1, nanos))
        throw new TimeoutException("Timeout waiting for task."); 
      return getValue();
    }
    
    V get() throws CancellationException, ExecutionException, InterruptedException {
      acquireSharedInterruptibly(-1);
      return getValue();
    }
    
    private V getValue() throws CancellationException, ExecutionException {
      int state = getState();
      switch (state) {
        case 2:
          if (this.exception != null)
            throw new ExecutionException(this.exception); 
          return this.value;
        case 4:
        case 8:
          throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.exception);
      } 
      throw new IllegalStateException("Error, synchronizer in invalid state: " + state);
    }
    
    boolean isDone() {
      return ((getState() & 0xE) != 0);
    }
    
    boolean isCancelled() {
      return ((getState() & 0xC) != 0);
    }
    
    boolean wasInterrupted() {
      return (getState() == 8);
    }
    
    boolean set(@Nullable V v) {
      return complete(v, (Throwable)null, 2);
    }
    
    boolean setException(Throwable t) {
      return complete((V)null, t, 2);
    }
    
    boolean cancel(boolean interrupt) {
      return complete((V)null, (Throwable)null, interrupt ? 8 : 4);
    }
    
    private boolean complete(@Nullable V v, @Nullable Throwable t, int finalState) {
      boolean doCompletion = compareAndSetState(0, 1);
      if (doCompletion) {
        this.value = v;
        this.exception = ((finalState & 0xC) != 0) ? new CancellationException("Future.cancel() was called.") : t;
        releaseShared(finalState);
      } else if (getState() == 1) {
        acquireShared(-1);
      } 
      return doCompletion;
    }
  }
  
  static final CancellationException cancellationExceptionWithCause(@Nullable String message, @Nullable Throwable cause) {
    CancellationException exception = new CancellationException(message);
    exception.initCause(cause);
    return exception;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\AbstractFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */