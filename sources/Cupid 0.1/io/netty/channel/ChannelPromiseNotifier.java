package io.netty.channel;

import io.netty.util.concurrent.Future;

public final class ChannelPromiseNotifier implements ChannelFutureListener {
  private final ChannelPromise[] promises;
  
  public ChannelPromiseNotifier(ChannelPromise... promises) {
    if (promises == null)
      throw new NullPointerException("promises"); 
    for (ChannelPromise promise : promises) {
      if (promise == null)
        throw new IllegalArgumentException("promises contains null ChannelPromise"); 
    } 
    this.promises = (ChannelPromise[])promises.clone();
  }
  
  public void operationComplete(ChannelFuture cf) throws Exception {
    if (cf.isSuccess()) {
      for (ChannelPromise p : this.promises)
        p.setSuccess(); 
      return;
    } 
    Throwable cause = cf.cause();
    for (ChannelPromise p : this.promises)
      p.setFailure(cause); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelPromiseNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */