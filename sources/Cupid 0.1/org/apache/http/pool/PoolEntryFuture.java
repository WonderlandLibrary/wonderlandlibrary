package org.apache.http.pool;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;

@ThreadSafe
abstract class PoolEntryFuture<T> implements Future<T> {
  private final Lock lock;
  
  private final FutureCallback<T> callback;
  
  private final Condition condition;
  
  private volatile boolean cancelled;
  
  private volatile boolean completed;
  
  private T result;
  
  PoolEntryFuture(Lock lock, FutureCallback<T> callback) {
    this.lock = lock;
    this.condition = lock.newCondition();
    this.callback = callback;
  }
  
  public boolean cancel(boolean mayInterruptIfRunning) {
    this.lock.lock();
    try {
      if (this.completed)
        return false; 
      this.completed = true;
      this.cancelled = true;
      if (this.callback != null)
        this.callback.cancelled(); 
      this.condition.signalAll();
      return true;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public boolean isCancelled() {
    return this.cancelled;
  }
  
  public boolean isDone() {
    return this.completed;
  }
  
  public T get() throws InterruptedException, ExecutionException {
    try {
      return get(0L, TimeUnit.MILLISECONDS);
    } catch (TimeoutException ex) {
      throw new ExecutionException(ex);
    } 
  }
  
  public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    Args.notNull(unit, "Time unit");
    this.lock.lock();
    try {
      if (this.completed)
        return this.result; 
      this.result = getPoolEntry(timeout, unit);
      this.completed = true;
      if (this.callback != null)
        this.callback.completed(this.result); 
      return this.result;
    } catch (IOException ex) {
      this.completed = true;
      this.result = null;
      if (this.callback != null)
        this.callback.failed(ex); 
      throw new ExecutionException(ex);
    } finally {
      this.lock.unlock();
    } 
  }
  
  protected abstract T getPoolEntry(long paramLong, TimeUnit paramTimeUnit) throws IOException, InterruptedException, TimeoutException;
  
  public boolean await(Date deadline) throws InterruptedException {
    this.lock.lock();
    try {
      boolean success;
      if (this.cancelled)
        throw new InterruptedException("Operation interrupted"); 
      if (deadline != null) {
        success = this.condition.awaitUntil(deadline);
      } else {
        this.condition.await();
        success = true;
      } 
      if (this.cancelled)
        throw new InterruptedException("Operation interrupted"); 
      return success;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void wakeup() {
    this.lock.lock();
    try {
      this.condition.signalAll();
    } finally {
      this.lock.unlock();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\pool\PoolEntryFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */