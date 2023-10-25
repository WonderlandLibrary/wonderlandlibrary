package io.netty.util.concurrent;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractEventExecutor extends AbstractExecutorService implements EventExecutor {
  public EventExecutor next() {
    return this;
  }
  
  public boolean inEventLoop() {
    return inEventLoop(Thread.currentThread());
  }
  
  public Iterator<EventExecutor> iterator() {
    return new EventExecutorIterator();
  }
  
  public Future<?> shutdownGracefully() {
    return shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
  }
  
  @Deprecated
  public List<Runnable> shutdownNow() {
    shutdown();
    return Collections.emptyList();
  }
  
  public <V> Promise<V> newPromise() {
    return new DefaultPromise<V>(this);
  }
  
  public <V> ProgressivePromise<V> newProgressivePromise() {
    return new DefaultProgressivePromise<V>(this);
  }
  
  public <V> Future<V> newSucceededFuture(V result) {
    return new SucceededFuture<V>(this, result);
  }
  
  public <V> Future<V> newFailedFuture(Throwable cause) {
    return new FailedFuture<V>(this, cause);
  }
  
  public Future<?> submit(Runnable task) {
    return (Future)super.submit(task);
  }
  
  public <T> Future<T> submit(Runnable task, T result) {
    return (Future<T>)super.<T>submit(task, result);
  }
  
  public <T> Future<T> submit(Callable<T> task) {
    return (Future<T>)super.<T>submit(task);
  }
  
  protected final <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
    return new PromiseTask<T>(this, runnable, value);
  }
  
  protected final <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
    return new PromiseTask<T>(this, callable);
  }
  
  public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
    throw new UnsupportedOperationException();
  }
  
  public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
    throw new UnsupportedOperationException();
  }
  
  public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
    throw new UnsupportedOperationException();
  }
  
  public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public abstract void shutdown();
  
  private final class EventExecutorIterator implements Iterator<EventExecutor> {
    private boolean nextCalled;
    
    private EventExecutorIterator() {}
    
    public boolean hasNext() {
      return !this.nextCalled;
    }
    
    public EventExecutor next() {
      if (!hasNext())
        throw new NoSuchElementException(); 
      this.nextCalled = true;
      return AbstractEventExecutor.this;
    }
    
    public void remove() {
      throw new UnsupportedOperationException("read-only");
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\AbstractEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */