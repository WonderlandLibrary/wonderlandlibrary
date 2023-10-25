package io.netty.channel;

import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

abstract class CompleteChannelFuture extends CompleteFuture<Void> implements ChannelFuture {
  private final Channel channel;
  
  protected CompleteChannelFuture(Channel channel, EventExecutor executor) {
    super(executor);
    if (channel == null)
      throw new NullPointerException("channel"); 
    this.channel = channel;
  }
  
  protected EventExecutor executor() {
    EventExecutor e = super.executor();
    if (e == null)
      return channel().eventLoop(); 
    return e;
  }
  
  public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
    super.addListener(listener);
    return this;
  }
  
  public ChannelFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
    super.addListeners((GenericFutureListener[])listeners);
    return this;
  }
  
  public ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
    super.removeListener(listener);
    return this;
  }
  
  public ChannelFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
    super.removeListeners((GenericFutureListener[])listeners);
    return this;
  }
  
  public ChannelFuture syncUninterruptibly() {
    return this;
  }
  
  public ChannelFuture sync() throws InterruptedException {
    return this;
  }
  
  public ChannelFuture await() throws InterruptedException {
    return this;
  }
  
  public ChannelFuture awaitUninterruptibly() {
    return this;
  }
  
  public Channel channel() {
    return this.channel;
  }
  
  public Void getNow() {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\CompleteChannelFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */