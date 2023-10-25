package io.netty.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractFuture<V> implements Future<V> {
  public V get() throws InterruptedException, ExecutionException {
    await();
    Throwable cause = cause();
    if (cause == null)
      return getNow(); 
    throw new ExecutionException(cause);
  }
  
  public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    if (await(timeout, unit)) {
      Throwable cause = cause();
      if (cause == null)
        return getNow(); 
      throw new ExecutionException(cause);
    } 
    throw new TimeoutException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\AbstractFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */