/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.timeout;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class IdleStateHandler
extends ChannelDuplexHandler {
    private final long readerIdleTimeMillis;
    private final long writerIdleTimeMillis;
    private final long allIdleTimeMillis;
    volatile ScheduledFuture<?> readerIdleTimeout;
    volatile long lastReadTime;
    private boolean firstReaderIdleEvent = true;
    volatile ScheduledFuture<?> writerIdleTimeout;
    volatile long lastWriteTime;
    private boolean firstWriterIdleEvent = true;
    volatile ScheduledFuture<?> allIdleTimeout;
    private boolean firstAllIdleEvent = true;
    private volatile int state;

    public IdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        this(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, TimeUnit.SECONDS);
    }

    public IdleStateHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.readerIdleTimeMillis = readerIdleTime <= 0L ? 0L : Math.max(unit.toMillis(readerIdleTime), 1L);
        this.writerIdleTimeMillis = writerIdleTime <= 0L ? 0L : Math.max(unit.toMillis(writerIdleTime), 1L);
        this.allIdleTimeMillis = allIdleTime <= 0L ? 0L : Math.max(unit.toMillis(allIdleTime), 1L);
    }

    public long getReaderIdleTimeInMillis() {
        return this.readerIdleTimeMillis;
    }

    public long getWriterIdleTimeInMillis() {
        return this.writerIdleTimeMillis;
    }

    public long getAllIdleTimeInMillis() {
        return this.allIdleTimeMillis;
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
        this.firstAllIdleEvent = true;
        this.firstReaderIdleEvent = true;
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        promise.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                IdleStateHandler.this.lastWriteTime = System.currentTimeMillis();
                IdleStateHandler.this.firstWriterIdleEvent = (IdleStateHandler.this.firstAllIdleEvent = true);
            }
        });
        ctx.write(msg, promise);
    }

    private void initialize(ChannelHandlerContext ctx) {
        switch (this.state) {
            case 1: 
            case 2: {
                return;
            }
        }
        this.state = 1;
        EventExecutor loop = ctx.executor();
        this.lastReadTime = this.lastWriteTime = System.currentTimeMillis();
        if (this.readerIdleTimeMillis > 0L) {
            this.readerIdleTimeout = loop.schedule(new ReaderIdleTimeoutTask(ctx), this.readerIdleTimeMillis, TimeUnit.MILLISECONDS);
        }
        if (this.writerIdleTimeMillis > 0L) {
            this.writerIdleTimeout = loop.schedule(new WriterIdleTimeoutTask(ctx), this.writerIdleTimeMillis, TimeUnit.MILLISECONDS);
        }
        if (this.allIdleTimeMillis > 0L) {
            this.allIdleTimeout = loop.schedule(new AllIdleTimeoutTask(ctx), this.allIdleTimeMillis, TimeUnit.MILLISECONDS);
        }
    }

    private void destroy() {
        this.state = 2;
        if (this.readerIdleTimeout != null) {
            this.readerIdleTimeout.cancel(false);
            this.readerIdleTimeout = null;
        }
        if (this.writerIdleTimeout != null) {
            this.writerIdleTimeout.cancel(false);
            this.writerIdleTimeout = null;
        }
        if (this.allIdleTimeout != null) {
            this.allIdleTimeout.cancel(false);
            this.allIdleTimeout = null;
        }
    }

    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    private final class AllIdleTimeoutTask
    implements Runnable {
        private final ChannelHandlerContext ctx;

        AllIdleTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            long lastIoTime = Math.max(IdleStateHandler.this.lastReadTime, IdleStateHandler.this.lastWriteTime);
            long nextDelay = IdleStateHandler.this.allIdleTimeMillis - (currentTime - lastIoTime);
            if (nextDelay <= 0L) {
                IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.allIdleTimeMillis, TimeUnit.MILLISECONDS);
                try {
                    IdleStateEvent event;
                    if (IdleStateHandler.this.firstAllIdleEvent) {
                        IdleStateHandler.this.firstAllIdleEvent = false;
                        event = IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT;
                    } else {
                        event = IdleStateEvent.ALL_IDLE_STATE_EVENT;
                    }
                    IdleStateHandler.this.channelIdle(this.ctx, event);
                }
                catch (Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            } else {
                IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule(this, nextDelay, TimeUnit.MILLISECONDS);
            }
        }
    }

    private final class WriterIdleTimeoutTask
    implements Runnable {
        private final ChannelHandlerContext ctx;

        WriterIdleTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            long lastWriteTime = IdleStateHandler.this.lastWriteTime;
            long nextDelay = IdleStateHandler.this.writerIdleTimeMillis - (currentTime - lastWriteTime);
            if (nextDelay <= 0L) {
                IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.writerIdleTimeMillis, TimeUnit.MILLISECONDS);
                try {
                    IdleStateEvent event;
                    if (IdleStateHandler.this.firstWriterIdleEvent) {
                        IdleStateHandler.this.firstWriterIdleEvent = false;
                        event = IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT;
                    } else {
                        event = IdleStateEvent.WRITER_IDLE_STATE_EVENT;
                    }
                    IdleStateHandler.this.channelIdle(this.ctx, event);
                }
                catch (Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            } else {
                IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule(this, nextDelay, TimeUnit.MILLISECONDS);
            }
        }
    }

    private final class ReaderIdleTimeoutTask
    implements Runnable {
        private final ChannelHandlerContext ctx;

        ReaderIdleTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            long lastReadTime = IdleStateHandler.this.lastReadTime;
            long nextDelay = IdleStateHandler.this.readerIdleTimeMillis - (currentTime - lastReadTime);
            if (nextDelay <= 0L) {
                IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.readerIdleTimeMillis, TimeUnit.MILLISECONDS);
                try {
                    IdleStateEvent event;
                    if (IdleStateHandler.this.firstReaderIdleEvent) {
                        IdleStateHandler.this.firstReaderIdleEvent = false;
                        event = IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT;
                    } else {
                        event = IdleStateEvent.READER_IDLE_STATE_EVENT;
                    }
                    IdleStateHandler.this.channelIdle(this.ctx, event);
                }
                catch (Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            } else {
                IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule(this, nextDelay, TimeUnit.MILLISECONDS);
            }
        }
    }
}

