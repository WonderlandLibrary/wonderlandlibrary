package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

final class SucceededChannelFuture extends CompleteChannelFuture {
  SucceededChannelFuture(Channel channel, EventExecutor executor) {
    super(channel, executor);
  }
  
  public Throwable cause() {
    return null;
  }
  
  public boolean isSuccess() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\SucceededChannelFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */