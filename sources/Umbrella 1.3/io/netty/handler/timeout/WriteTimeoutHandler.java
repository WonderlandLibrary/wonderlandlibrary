/*
 * Decompiled with CFR 0.150.
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
    private final long timeoutMillis;
    private boolean closed;

    public WriteTimeoutHandler(int timeoutSeconds) {
        this(timeoutSeconds, TimeUnit.SECONDS);
    }

    public WriteTimeoutHandler(long timeout, TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.timeoutMillis = timeout <= 0L ? 0L : Math.max(unit.toMillis(timeout), 1L);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        this.scheduleTimeout(ctx, promise);
        ctx.write(msg, promise);
    }

    private void scheduleTimeout(final ChannelHandlerContext ctx, final ChannelPromise future) {
        if (this.timeoutMillis > 0L) {
            final ScheduledFuture<?> sf = ctx.executor().schedule(new Runnable(){

                @Override
                public void run() {
                    if (future.tryFailure(WriteTimeoutException.INSTANCE)) {
                        try {
                            WriteTimeoutHandler.this.writeTimedOut(ctx);
                        }
                        catch (Throwable t) {
                            ctx.fireExceptionCaught(t);
                        }
                    }
                }
            }, this.timeoutMillis, TimeUnit.MILLISECONDS);
            future.addListener(new ChannelFutureListener(){

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    sf.cancel(false);
                }
            });
        }
    }

    protected void writeTimedOut(ChannelHandlerContext ctx) throws Exception {
        if (!this.closed) {
            ctx.fireExceptionCaught(WriteTimeoutException.INSTANCE);
            ctx.close();
            this.closed = true;
        }
    }
}

