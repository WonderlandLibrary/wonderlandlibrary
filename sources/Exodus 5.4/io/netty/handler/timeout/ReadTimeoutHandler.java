/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ReadTimeoutHandler
extends ChannelInboundHandlerAdapter {
    private volatile ScheduledFuture<?> timeout;
    private volatile long lastReadTime;
    private volatile int state;
    private final long timeoutNanos;
    private boolean closed;
    private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);

    private void destroy() {
        this.state = 2;
        if (this.timeout != null) {
            this.timeout.cancel(false);
            this.timeout = null;
        }
    }

    public ReadTimeoutHandler(long l, TimeUnit timeUnit) {
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        this.timeoutNanos = l <= 0L ? 0L : Math.max(timeUnit.toNanos(l), MIN_TIMEOUT_NANOS);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (channelHandlerContext.channel().isActive() && channelHandlerContext.channel().isRegistered()) {
            this.initialize(channelHandlerContext);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.initialize(channelHandlerContext);
        super.channelActive(channelHandlerContext);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        this.lastReadTime = System.nanoTime();
        channelHandlerContext.fireChannelRead(object);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.destroy();
        super.channelInactive(channelHandlerContext);
    }

    private void initialize(ChannelHandlerContext channelHandlerContext) {
        switch (this.state) {
            case 1: 
            case 2: {
                return;
            }
        }
        this.state = 1;
        this.lastReadTime = System.nanoTime();
        if (this.timeoutNanos > 0L) {
            this.timeout = channelHandlerContext.executor().schedule(new ReadTimeoutTask(channelHandlerContext), this.timeoutNanos, TimeUnit.NANOSECONDS);
        }
    }

    public ReadTimeoutHandler(int n) {
        this(n, TimeUnit.SECONDS);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.destroy();
    }

    protected void readTimedOut(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (!this.closed) {
            channelHandlerContext.fireExceptionCaught(ReadTimeoutException.INSTANCE);
            channelHandlerContext.close();
            this.closed = true;
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (channelHandlerContext.channel().isActive()) {
            this.initialize(channelHandlerContext);
        }
        super.channelRegistered(channelHandlerContext);
    }

    private final class ReadTimeoutTask
    implements Runnable {
        private final ChannelHandlerContext ctx;

        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long l = System.nanoTime();
            long l2 = ReadTimeoutHandler.this.timeoutNanos - (l - ReadTimeoutHandler.this.lastReadTime);
            if (l2 <= 0L) {
                ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, ReadTimeoutHandler.this.timeoutNanos, TimeUnit.NANOSECONDS);
                try {
                    ReadTimeoutHandler.this.readTimedOut(this.ctx);
                }
                catch (Throwable throwable) {
                    this.ctx.fireExceptionCaught(throwable);
                }
            } else {
                ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, l2, TimeUnit.NANOSECONDS);
            }
        }

        ReadTimeoutTask(ChannelHandlerContext channelHandlerContext) {
            this.ctx = channelHandlerContext;
        }
    }
}

