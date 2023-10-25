package io.netty.handler.timeout;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WriteTimeoutHandler extends ChannelOutboundHandlerAdapter {
  private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
  
  private final long timeoutNanos;
  
  private boolean closed;
  
  public WriteTimeoutHandler(int timeoutSeconds) {
    this(timeoutSeconds, TimeUnit.SECONDS);
  }
  
  public WriteTimeoutHandler(long timeout, TimeUnit unit) {
    if (unit == null)
      throw new NullPointerException("unit"); 
    if (timeout <= 0L) {
      this.timeoutNanos = 0L;
    } else {
      this.timeoutNanos = Math.max(unit.toNanos(timeout), MIN_TIMEOUT_NANOS);
    } 
  }
  
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    scheduleTimeout(ctx, promise);
    ctx.write(msg, promise);
  }
  
  private void scheduleTimeout(final ChannelHandlerContext ctx, final ChannelPromise future) {
    if (this.timeoutNanos > 0L) {
      final ScheduledFuture sf = ctx.executor().schedule(new Runnable() {
            public void run() {
              if (!future.isDone())
                try {
                  WriteTimeoutHandler.this.writeTimedOut(ctx);
                } catch (Throwable t) {
                  ctx.fireExceptionCaught(t);
                }  
            }
          },  this.timeoutNanos, TimeUnit.NANOSECONDS);
      future.addListener((GenericFutureListener)new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
              sf.cancel(false);
            }
          });
    } 
  }
  
  protected void writeTimedOut(ChannelHandlerContext ctx) throws Exception {
    if (!this.closed) {
      ctx.fireExceptionCaught((Throwable)WriteTimeoutException.INSTANCE);
      ctx.close();
      this.closed = true;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\timeout\WriteTimeoutHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */