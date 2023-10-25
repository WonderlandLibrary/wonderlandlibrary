/*
 * Decompiled with CFR 0.152.
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
    private boolean firstAllIdleEvent = true;
    volatile ScheduledFuture<?> readerIdleTimeout;
    volatile ScheduledFuture<?> writerIdleTimeout;
    private boolean firstReaderIdleEvent = true;
    private final long readerIdleTimeNanos;
    private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
    private final long writerIdleTimeNanos;
    volatile ScheduledFuture<?> allIdleTimeout;
    volatile long lastReadTime;
    volatile long lastWriteTime;
    private boolean firstWriterIdleEvent = true;
    private volatile int state;
    private final long allIdleTimeNanos;

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.initialize(channelHandlerContext);
        super.channelActive(channelHandlerContext);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (channelHandlerContext.channel().isActive() && channelHandlerContext.channel().isRegistered()) {
            this.initialize(channelHandlerContext);
        }
    }

    private void initialize(ChannelHandlerContext channelHandlerContext) {
        switch (this.state) {
            case 1: 
            case 2: {
                return;
            }
        }
        this.state = 1;
        EventExecutor eventExecutor = channelHandlerContext.executor();
        this.lastReadTime = this.lastWriteTime = System.nanoTime();
        if (this.readerIdleTimeNanos > 0L) {
            this.readerIdleTimeout = eventExecutor.schedule(new ReaderIdleTimeoutTask(channelHandlerContext), this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
        }
        if (this.writerIdleTimeNanos > 0L) {
            this.writerIdleTimeout = eventExecutor.schedule(new WriterIdleTimeoutTask(channelHandlerContext), this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);
        }
        if (this.allIdleTimeNanos > 0L) {
            this.allIdleTimeout = eventExecutor.schedule(new AllIdleTimeoutTask(channelHandlerContext), this.allIdleTimeNanos, TimeUnit.NANOSECONDS);
        }
    }

    public long getAllIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.allIdleTimeNanos);
    }

    public IdleStateHandler(int n, int n2, int n3) {
        this(n, n2, n3, TimeUnit.SECONDS);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (channelHandlerContext.channel().isActive()) {
            this.initialize(channelHandlerContext);
        }
        super.channelRegistered(channelHandlerContext);
    }

    public IdleStateHandler(long l, long l2, long l3, TimeUnit timeUnit) {
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        this.readerIdleTimeNanos = l <= 0L ? 0L : Math.max(timeUnit.toNanos(l), MIN_TIMEOUT_NANOS);
        this.writerIdleTimeNanos = l2 <= 0L ? 0L : Math.max(timeUnit.toNanos(l2), MIN_TIMEOUT_NANOS);
        this.allIdleTimeNanos = l3 <= 0L ? 0L : Math.max(timeUnit.toNanos(l3), MIN_TIMEOUT_NANOS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.destroy();
        super.channelInactive(channelHandlerContext);
    }

    public long getReaderIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.readerIdleTimeNanos);
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

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        channelPromise.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                IdleStateHandler.this.lastWriteTime = System.nanoTime();
                IdleStateHandler.this.firstWriterIdleEvent = (IdleStateHandler.this.firstAllIdleEvent = true);
            }
        });
        channelHandlerContext.write(object, channelPromise);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        this.lastReadTime = System.nanoTime();
        this.firstAllIdleEvent = true;
        this.firstReaderIdleEvent = true;
        channelHandlerContext.fireChannelRead(object);
    }

    public long getWriterIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.writerIdleTimeNanos);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.destroy();
    }

    protected void channelIdle(ChannelHandlerContext channelHandlerContext, IdleStateEvent idleStateEvent) throws Exception {
        channelHandlerContext.fireUserEventTriggered(idleStateEvent);
    }

    private final class AllIdleTimeoutTask
    implements Runnable {
        private final ChannelHandlerContext ctx;

        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long l = System.nanoTime();
            long l2 = Math.max(IdleStateHandler.this.lastReadTime, IdleStateHandler.this.lastWriteTime);
            long l3 = IdleStateHandler.this.allIdleTimeNanos - (l - l2);
            if (l3 <= 0L) {
                IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.allIdleTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    IdleStateEvent idleStateEvent;
                    if (IdleStateHandler.this.firstAllIdleEvent) {
                        IdleStateHandler.this.firstAllIdleEvent = false;
                        idleStateEvent = IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT;
                    } else {
                        idleStateEvent = IdleStateEvent.ALL_IDLE_STATE_EVENT;
                    }
                    IdleStateHandler.this.channelIdle(this.ctx, idleStateEvent);
                }
                catch (Throwable throwable) {
                    this.ctx.fireExceptionCaught(throwable);
                }
            } else {
                IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule(this, l3, TimeUnit.NANOSECONDS);
            }
        }

        AllIdleTimeoutTask(ChannelHandlerContext channelHandlerContext) {
            this.ctx = channelHandlerContext;
        }
    }

    private final class ReaderIdleTimeoutTask
    implements Runnable {
        private final ChannelHandlerContext ctx;

        ReaderIdleTimeoutTask(ChannelHandlerContext channelHandlerContext) {
            this.ctx = channelHandlerContext;
        }

        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long l = System.nanoTime();
            long l2 = IdleStateHandler.this.lastReadTime;
            long l3 = IdleStateHandler.this.readerIdleTimeNanos - (l - l2);
            if (l3 <= 0L) {
                IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    IdleStateEvent idleStateEvent;
                    if (IdleStateHandler.this.firstReaderIdleEvent) {
                        IdleStateHandler.this.firstReaderIdleEvent = false;
                        idleStateEvent = IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT;
                    } else {
                        idleStateEvent = IdleStateEvent.READER_IDLE_STATE_EVENT;
                    }
                    IdleStateHandler.this.channelIdle(this.ctx, idleStateEvent);
                }
                catch (Throwable throwable) {
                    this.ctx.fireExceptionCaught(throwable);
                }
            } else {
                IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule(this, l3, TimeUnit.NANOSECONDS);
            }
        }
    }

    private final class WriterIdleTimeoutTask
    implements Runnable {
        private final ChannelHandlerContext ctx;

        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long l = System.nanoTime();
            long l2 = IdleStateHandler.this.lastWriteTime;
            long l3 = IdleStateHandler.this.writerIdleTimeNanos - (l - l2);
            if (l3 <= 0L) {
                IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    IdleStateEvent idleStateEvent;
                    if (IdleStateHandler.this.firstWriterIdleEvent) {
                        IdleStateHandler.this.firstWriterIdleEvent = false;
                        idleStateEvent = IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT;
                    } else {
                        idleStateEvent = IdleStateEvent.WRITER_IDLE_STATE_EVENT;
                    }
                    IdleStateHandler.this.channelIdle(this.ctx, idleStateEvent);
                }
                catch (Throwable throwable) {
                    this.ctx.fireExceptionCaught(throwable);
                }
            } else {
                IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule(this, l3, TimeUnit.NANOSECONDS);
            }
        }

        WriterIdleTimeoutTask(ChannelHandlerContext channelHandlerContext) {
            this.ctx = channelHandlerContext;
        }
    }
}

