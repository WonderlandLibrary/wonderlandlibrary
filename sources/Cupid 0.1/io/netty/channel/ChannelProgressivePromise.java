package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ProgressivePromise;

public interface ChannelProgressivePromise extends ProgressivePromise<Void>, ChannelProgressiveFuture, ChannelPromise {
  ChannelProgressivePromise addListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);
  
  ChannelProgressivePromise addListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);
  
  ChannelProgressivePromise removeListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);
  
  ChannelProgressivePromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);
  
  ChannelProgressivePromise sync() throws InterruptedException;
  
  ChannelProgressivePromise syncUninterruptibly();
  
  ChannelProgressivePromise await() throws InterruptedException;
  
  ChannelProgressivePromise awaitUninterruptibly();
  
  ChannelProgressivePromise setSuccess(Void paramVoid);
  
  ChannelProgressivePromise setSuccess();
  
  ChannelProgressivePromise setFailure(Throwable paramThrowable);
  
  ChannelProgressivePromise setProgress(long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelProgressivePromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */