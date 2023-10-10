package io.netty.util.concurrent;

public interface EventExecutor extends EventExecutorGroup {
   EventExecutor next();

   EventExecutorGroup parent();

   boolean inEventLoop();

   boolean inEventLoop(Thread var1);

   Promise newPromise();

   ProgressivePromise newProgressivePromise();

   Future newSucceededFuture(Object var1);

   Future newFailedFuture(Throwable var1);
}
