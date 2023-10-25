package io.netty.channel.embedded;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

final class EmbeddedEventLoop extends AbstractEventExecutor implements EventLoop {
  private final Queue<Runnable> tasks = new ArrayDeque<Runnable>(2);
  
  public void execute(Runnable command) {
    if (command == null)
      throw new NullPointerException("command"); 
    this.tasks.add(command);
  }
  
  void runTasks() {
    while (true) {
      Runnable task = this.tasks.poll();
      if (task == null)
        break; 
      task.run();
    } 
  }
  
  public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
    throw new UnsupportedOperationException();
  }
  
  public Future<?> terminationFuture() {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public void shutdown() {
    throw new UnsupportedOperationException();
  }
  
  public boolean isShuttingDown() {
    return false;
  }
  
  public boolean isShutdown() {
    return false;
  }
  
  public boolean isTerminated() {
    return false;
  }
  
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    Thread.sleep(unit.toMillis(timeout));
    return false;
  }
  
  public ChannelFuture register(Channel channel) {
    return register(channel, (ChannelPromise)new DefaultChannelPromise(channel, (EventExecutor)this));
  }
  
  public ChannelFuture register(Channel channel, ChannelPromise promise) {
    channel.unsafe().register(this, promise);
    return (ChannelFuture)promise;
  }
  
  public boolean inEventLoop() {
    return true;
  }
  
  public boolean inEventLoop(Thread thread) {
    return true;
  }
  
  public EventLoop next() {
    return this;
  }
  
  public EventLoopGroup parent() {
    return (EventLoopGroup)this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\embedded\EmbeddedEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */