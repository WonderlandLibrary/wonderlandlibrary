package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.util.concurrent.ThreadFactory;

public abstract class SingleThreadEventLoop extends SingleThreadEventExecutor implements EventLoop {
  protected SingleThreadEventLoop(EventLoopGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
    super(parent, threadFactory, addTaskWakesUp);
  }
  
  public EventLoopGroup parent() {
    return (EventLoopGroup)super.parent();
  }
  
  public EventLoop next() {
    return (EventLoop)super.next();
  }
  
  public ChannelFuture register(Channel channel) {
    return register(channel, new DefaultChannelPromise(channel, this));
  }
  
  public ChannelFuture register(Channel channel, ChannelPromise promise) {
    if (channel == null)
      throw new NullPointerException("channel"); 
    if (promise == null)
      throw new NullPointerException("promise"); 
    channel.unsafe().register(this, promise);
    return promise;
  }
  
  protected boolean wakesUpForTask(Runnable task) {
    return !(task instanceof NonWakeupRunnable);
  }
  
  static interface NonWakeupRunnable extends Runnable {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\SingleThreadEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */