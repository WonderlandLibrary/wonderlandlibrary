package io.netty.util.concurrent;

public interface EventExecutor extends EventExecutorGroup {
  EventExecutor next();
  
  EventExecutorGroup parent();
  
  boolean inEventLoop();
  
  boolean inEventLoop(Thread paramThread);
  
  <V> Promise<V> newPromise();
  
  <V> ProgressivePromise<V> newProgressivePromise();
  
  <V> Future<V> newSucceededFuture(V paramV);
  
  <V> Future<V> newFailedFuture(Throwable paramThrowable);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\EventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */