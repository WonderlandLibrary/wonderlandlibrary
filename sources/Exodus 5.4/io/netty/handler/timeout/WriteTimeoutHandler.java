/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.timeout;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WriteTimeoutHandler
extends ChannelOutboundHandlerAdapter {
    private final long timeoutNanos;
    private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
    private boolean closed;

    public WriteTimeoutHandler(int n) {
        this(n, TimeUnit.SECONDS);
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        this.scheduleTimeout(channelHandlerContext, channelPromise);
        channelHandlerContext.write(object, channelPromise);
    }

    protected void writeTimedOut(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (!this.closed) {
            channelHandlerContext.fireExceptionCaught(WriteTimeoutException.INSTANCE);
            channelHandlerContext.close();
            this.closed = true;
        }
    }

    public WriteTimeoutHandler(long l, TimeUnit timeUnit) {
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        this.timeoutNanos = l <= 0L ? 0L : Math.max(timeUnit.toNanos(l), MIN_TIMEOUT_NANOS);
    }

    private void scheduleTimeout(final ChannelHandlerContext channelHandlerContext, final ChannelPromise channelPromise) {
        if (this.timeoutNanos > 0L) {
            final ScheduledFuture<?> scheduledFuture = channelHandlerContext.executor().schedule(new Runnable(){

                @Override
                public void run() {
                    if (!channelPromise.isDone()) {
                        try {
                            WriteTimeoutHandler.this.writeTimedOut(channelHandlerContext);
                        }
                        catch (Throwable throwable) {
                            channelHandlerContext.fireExceptionCaught(throwable);
                        }
                    }
                }
            }, this.timeoutNanos, TimeUnit.NANOSECONDS);
            channelPromise.addListener(new ChannelFutureListener(){

                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    scheduledFuture.cancel(false);
                }
            });
        }
    }
}

