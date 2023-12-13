/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ReadTimeoutHandler
extends ChannelInboundHandlerAdapter {
    private final long timeoutMillis;
    private volatile ScheduledFuture<?> timeout;
    private volatile long lastReadTime;
    private volatile int state;
    private boolean closed;

    public ReadTimeoutHandler(int timeoutSeconds) {
        this(timeoutSeconds, TimeUnit.SECONDS);
    }

    public ReadTimeoutHandler(long timeout, TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.timeoutMillis = timeout <= 0L ? 0L : Math.max(unit.toMillis(timeout), 1L);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive() && ctx.channel().isRegistered()) {
            this.initialize(ctx);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.destroy();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive()) {
            this.initialize(ctx);
        }
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.initialize(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.destroy();
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.lastReadTime = System.currentTimeMillis();
        ctx.fireChannelRead(msg);
    }

    private void initialize(ChannelHandlerContext ctx) {
        switch (this.state) {
            case 1: 
            case 2: {
                return;
            }
        }
        this.state = 1;
        this.lastReadTime = System.currentTimeMillis();
        if (this.timeoutMillis > 0L) {
            this.timeout = ctx.executor().schedule(new ReadTimeoutTask(ctx), this.timeoutMillis, TimeUnit.MILLISECONDS);
        }
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
            ctx.fireExceptionCaught(ReadTimeoutException.INSTANCE);
            ctx.close();
            this.closed = true;
        }
    }

    private final class ReadTimeoutTask
    implements Runnable {
        private final ChannelHandlerContext ctx;

        ReadTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            long nextDelay = ReadTimeoutHandler.this.timeoutMillis - (currentTime - ReadTimeoutHandler.this.lastReadTime);
            if (nextDelay <= 0L) {
                ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, ReadTimeoutHandler.this.timeoutMillis, TimeUnit.MILLISECONDS);
                try {
                    ReadTimeoutHandler.this.readTimedOut(this.ctx);
                }
                catch (Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            } else {
                ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, nextDelay, TimeUnit.MILLISECONDS);
            }
        }
    }
}

