package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public interface ChannelFutureListener extends GenericFutureListener<ChannelFuture> {
  public static final ChannelFutureListener CLOSE = new ChannelFutureListener() {
      public void operationComplete(ChannelFuture future) {
        future.channel().close();
      }
    };
  
  public static final ChannelFutureListener CLOSE_ON_FAILURE = new ChannelFutureListener() {
      public void operationComplete(ChannelFuture future) {
        if (!future.isSuccess())
          future.channel().close(); 
      }
    };
  
  public static final ChannelFutureListener FIRE_EXCEPTION_ON_FAILURE = new ChannelFutureListener() {
      public void operationComplete(ChannelFuture future) {
        if (!future.isSuccess())
          future.channel().pipeline().fireExceptionCaught(future.cause()); 
      }
    };
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelFutureListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */