package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;

final class FailedChannelFuture extends CompleteChannelFuture {
  private final Throwable cause;
  
  FailedChannelFuture(Channel channel, EventExecutor executor, Throwable cause) {
    super(channel, executor);
    if (cause == null)
      throw new NullPointerException("cause"); 
    this.cause = cause;
  }
  
  public Throwable cause() {
    return this.cause;
  }
  
  public boolean isSuccess() {
    return false;
  }
  
  public ChannelFuture sync() {
    PlatformDependent.throwException(this.cause);
    return this;
  }
  
  public ChannelFuture syncUninterruptibly() {
    PlatformDependent.throwException(this.cause);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\FailedChannelFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */