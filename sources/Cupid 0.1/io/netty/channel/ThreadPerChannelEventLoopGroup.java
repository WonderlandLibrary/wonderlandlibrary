package io.netty.channel;

import io.netty.util.concurrent.AbstractEventExecutorGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ReadOnlyIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadPerChannelEventLoopGroup extends AbstractEventExecutorGroup implements EventLoopGroup {
  private final Object[] childArgs;
  
  private final int maxChannels;
  
  final ThreadFactory threadFactory;
  
  final Set<ThreadPerChannelEventLoop> activeChildren = Collections.newSetFromMap(PlatformDependent.newConcurrentHashMap());
  
  final Queue<ThreadPerChannelEventLoop> idleChildren = new ConcurrentLinkedQueue<ThreadPerChannelEventLoop>();
  
  private final ChannelException tooManyChannels;
  
  private volatile boolean shuttingDown;
  
  private final Promise<?> terminationFuture = (Promise<?>)new DefaultPromise((EventExecutor)GlobalEventExecutor.INSTANCE);
  
  private final FutureListener<Object> childTerminationListener = new FutureListener<Object>() {
      public void operationComplete(Future<Object> future) throws Exception {
        if (ThreadPerChannelEventLoopGroup.this.isTerminated())
          ThreadPerChannelEventLoopGroup.this.terminationFuture.trySuccess(null); 
      }
    };
  
  protected ThreadPerChannelEventLoopGroup() {
    this(0);
  }
  
  protected ThreadPerChannelEventLoopGroup(int maxChannels) {
    this(maxChannels, Executors.defaultThreadFactory(), new Object[0]);
  }
  
  protected ThreadPerChannelEventLoopGroup(int maxChannels, ThreadFactory threadFactory, Object... args) {
    if (maxChannels < 0)
      throw new IllegalArgumentException(String.format("maxChannels: %d (expected: >= 0)", new Object[] { Integer.valueOf(maxChannels) })); 
    if (threadFactory == null)
      throw new NullPointerException("threadFactory"); 
    if (args == null) {
      this.childArgs = EmptyArrays.EMPTY_OBJECTS;
    } else {
      this.childArgs = (Object[])args.clone();
    } 
    this.maxChannels = maxChannels;
    this.threadFactory = threadFactory;
    this.tooManyChannels = new ChannelException("too many channels (max: " + maxChannels + ')');
    this.tooManyChannels.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
  }
  
  protected ThreadPerChannelEventLoop newChild(Object... args) throws Exception {
    return new ThreadPerChannelEventLoop(this);
  }
  
  public Iterator<EventExecutor> iterator() {
    return (Iterator<EventExecutor>)new ReadOnlyIterator(this.activeChildren.iterator());
  }
  
  public EventLoop next() {
    throw new UnsupportedOperationException();
  }
  
  public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
    this.shuttingDown = true;
    for (EventLoop l : this.activeChildren)
      l.shutdownGracefully(quietPeriod, timeout, unit); 
    for (EventLoop l : this.idleChildren)
      l.shutdownGracefully(quietPeriod, timeout, unit); 
    if (isTerminated())
      this.terminationFuture.trySuccess(null); 
    return terminationFuture();
  }
  
  public Future<?> terminationFuture() {
    return (Future<?>)this.terminationFuture;
  }
  
  @Deprecated
  public void shutdown() {
    this.shuttingDown = true;
    for (EventLoop l : this.activeChildren)
      l.shutdown(); 
    for (EventLoop l : this.idleChildren)
      l.shutdown(); 
    if (isTerminated())
      this.terminationFuture.trySuccess(null); 
  }
  
  public boolean isShuttingDown() {
    for (EventLoop l : this.activeChildren) {
      if (!l.isShuttingDown())
        return false; 
    } 
    for (EventLoop l : this.idleChildren) {
      if (!l.isShuttingDown())
        return false; 
    } 
    return true;
  }
  
  public boolean isShutdown() {
    for (EventLoop l : this.activeChildren) {
      if (!l.isShutdown())
        return false; 
    } 
    for (EventLoop l : this.idleChildren) {
      if (!l.isShutdown())
        return false; 
    } 
    return true;
  }
  
  public boolean isTerminated() {
    for (EventLoop l : this.activeChildren) {
      if (!l.isTerminated())
        return false; 
    } 
    for (EventLoop l : this.idleChildren) {
      if (!l.isTerminated())
        return false; 
    } 
    return true;
  }
  
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    long deadline = System.nanoTime() + unit.toNanos(timeout);
    label26: for (EventLoop l : this.activeChildren) {
      while (true) {
        long timeLeft = deadline - System.nanoTime();
        if (timeLeft <= 0L)
          return isTerminated(); 
        if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS))
          continue label26; 
      } 
    } 
    label27: for (EventLoop l : this.idleChildren) {
      while (true) {
        long timeLeft = deadline - System.nanoTime();
        if (timeLeft <= 0L)
          return isTerminated(); 
        if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS))
          continue label27; 
      } 
    } 
    return isTerminated();
  }
  
  public ChannelFuture register(Channel channel) {
    if (channel == null)
      throw new NullPointerException("channel"); 
    try {
      EventLoop l = nextChild();
      return l.register(channel, new DefaultChannelPromise(channel, l));
    } catch (Throwable t) {
      return new FailedChannelFuture(channel, (EventExecutor)GlobalEventExecutor.INSTANCE, t);
    } 
  }
  
  public ChannelFuture register(Channel channel, ChannelPromise promise) {
    if (channel == null)
      throw new NullPointerException("channel"); 
    try {
      return nextChild().register(channel, promise);
    } catch (Throwable t) {
      promise.setFailure(t);
      return promise;
    } 
  }
  
  private EventLoop nextChild() throws Exception {
    if (this.shuttingDown)
      throw new RejectedExecutionException("shutting down"); 
    ThreadPerChannelEventLoop loop = this.idleChildren.poll();
    if (loop == null) {
      if (this.maxChannels > 0 && this.activeChildren.size() >= this.maxChannels)
        throw this.tooManyChannels; 
      loop = newChild(this.childArgs);
      loop.terminationFuture().addListener((GenericFutureListener)this.childTerminationListener);
    } 
    this.activeChildren.add(loop);
    return loop;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ThreadPerChannelEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */