package io.netty.util.concurrent;

public interface ProgressiveFuture extends Future {
   ProgressiveFuture addListener(GenericFutureListener var1);

   ProgressiveFuture addListeners(GenericFutureListener... var1);

   ProgressiveFuture removeListener(GenericFutureListener var1);

   ProgressiveFuture removeListeners(GenericFutureListener... var1);

   ProgressiveFuture sync() throws InterruptedException;

   ProgressiveFuture syncUninterruptibly();

   ProgressiveFuture await() throws InterruptedException;

   ProgressiveFuture awaitUninterruptibly();
}
