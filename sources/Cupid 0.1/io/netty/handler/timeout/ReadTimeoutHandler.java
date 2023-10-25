package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ReadTimeoutHandler extends ChannelInboundHandlerAdapter {
  private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
  
  private final long timeoutNanos;
  
  private volatile ScheduledFuture<?> timeout;
  
  private volatile long lastReadTime;
  
  private volatile int state;
  
  private boolean closed;
  
  public ReadTimeoutHandler(int timeoutSeconds) {
    this(timeoutSeconds, TimeUnit.SECONDS);
  }
  
  public ReadTimeoutHandler(long timeout, TimeUnit unit) {
    if (unit == null)
      throw new NullPointerException("unit"); 
    if (timeout <= 0L) {
      this.timeoutNanos = 0L;
    } else {
      this.timeoutNanos = Math.max(unit.toNanos(timeout), MIN_TIMEOUT_NANOS);
    } 
  }
  
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    if (ctx.channel().isActive() && ctx.channel().isRegistered())
      initialize(ctx); 
  }
  
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    destroy();
  }
  
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    if (ctx.channel().isActive())
      initialize(ctx); 
    super.channelRegistered(ctx);
  }
  
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    initialize(ctx);
    super.channelActive(ctx);
  }
  
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    destroy();
    super.channelInactive(ctx);
  }
  
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    this.lastReadTime = System.nanoTime();
    ctx.fireChannelRead(msg);
  }
  
  private void initialize(ChannelHandlerContext ctx) {
    switch (this.state) {
      case 1:
      case 2:
        return;
    } 
    this.state = 1;
    this.lastReadTime = System.nanoTime();
    if (this.timeoutNanos > 0L)
      this.timeout = (ScheduledFuture<?>)ctx.executor().schedule(new ReadTimeoutTask(ctx), this.timeoutNanos, TimeUnit.NANOSECONDS); 
  }
  
  private void destroy() {
    this.state = 2;
    if (this.timeout != null) {
      this.timeout.cancel(false);
      this.timeout = null;
    } 
  }
  
  protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
    if (!this.closed) {
      ctx.fireExceptionCaught((Throwable)ReadTimeoutException.INSTANCE);
      ctx.close();
      this.closed = true;
    } 
  }
  
  private final class ReadTimeoutTask implements Runnable {
    private final ChannelHandlerContext ctx;
    
    ReadTimeoutTask(ChannelHandlerContext ctx) {
      this.ctx = ctx;
    }
    
    public void run() {
      if (!this.ctx.channel().isOpen())
        return; 
      long currentTime = System.nanoTime();
      long nextDelay = ReadTimeoutHandler.this.timeoutNanos - currentTime - ReadTimeoutHandler.this.lastReadTime;
      if (nextDelay <= 0L) {
        ReadTimeoutHandler.this.timeout = (ScheduledFuture<?>)this.ctx.executor().schedule(this, ReadTimeoutHandler.this.timeoutNanos, TimeUnit.NANOSECONDS);
        try {
          ReadTimeoutHandler.this.readTimedOut(this.ctx);
        } catch (Throwable t) {
          this.ctx.fireExceptionCaught(t);
        } 
      } else {
        ReadTimeoutHandler.this.timeout = (ScheduledFuture<?>)this.ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\timeout\ReadTimeoutHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */