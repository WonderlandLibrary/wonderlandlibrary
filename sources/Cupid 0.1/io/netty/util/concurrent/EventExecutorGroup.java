package io.netty.util.concurrent;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface EventExecutorGroup extends ScheduledExecutorService, Iterable<EventExecutor> {
  boolean isShuttingDown();
  
  Future<?> shutdownGracefully();
  
  Future<?> shutdownGracefully(long paramLong1, long paramLong2, TimeUnit paramTimeUnit);
  
  Future<?> terminationFuture();
  
  @Deprecated
  void shutdown();
  
  @Deprecated
  List<Runnable> shutdownNow();
  
  EventExecutor next();
  
  Iterator<EventExecutor> iterator();
  
  Future<?> submit(Runnable paramRunnable);
  
  <T> Future<T> submit(Runnable paramRunnable, T paramT);
  
  <T> Future<T> submit(Callable<T> paramCallable);
  
  ScheduledFuture<?> schedule(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit);
  
  <V> ScheduledFuture<V> schedule(Callable<V> paramCallable, long paramLong, TimeUnit paramTimeUnit);
  
  ScheduledFuture<?> scheduleAtFixedRate(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit);
  
  ScheduledFuture<?> scheduleWithFixedDelay(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\EventExecutorGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */