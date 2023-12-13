/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTrafficShapingHandler
extends ChannelDuplexHandler {
    public static final long DEFAULT_CHECK_INTERVAL = 1000L;
    private static final long MINIMAL_WAIT = 10L;
    protected TrafficCounter trafficCounter;
    private long writeLimit;
    private long readLimit;
    protected long checkInterval = 1000L;
    private static final AttributeKey<Boolean> READ_SUSPENDED = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
    private static final AttributeKey<Runnable> REOPEN_TASK = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".REOPEN_TASK");

    void setTrafficCounter(TrafficCounter newTrafficCounter) {
        this.trafficCounter = newTrafficCounter;
    }

    protected AbstractTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval) {
        this.writeLimit = writeLimit;
        this.readLimit = readLimit;
        this.checkInterval = checkInterval;
    }

    protected AbstractTrafficShapingHandler(long writeLimit, long readLimit) {
        this(writeLimit, readLimit, 1000L);
    }

    protected AbstractTrafficShapingHandler() {
        this(0L, 0L, 1000L);
    }

    protected AbstractTrafficShapingHandler(long checkInterval) {
        this(0L, 0L, checkInterval);
    }

    public void configure(long newWriteLimit, long newReadLimit, long newCheckInterval) {
        this.configure(newWriteLimit, newReadLimit);
        this.configure(newCheckInterval);
    }

    public void configure(long newWriteLimit, long newReadLimit) {
        this.writeLimit = newWriteLimit;
        this.readLimit = newReadLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
        }
    }

    public void configure(long newCheckInterval) {
        this.checkInterval = newCheckInterval;
        if (this.trafficCounter != null) {
            this.trafficCounter.configure(this.checkInterval);
        }
    }

    protected void doAccounting(TrafficCounter counter) {
    }

    private static long getTimeToWait(long limit, long bytes, long lastTime, long curtime) {
        long interval = curtime - lastTime;
        if (interval <= 0L) {
            return 0L;
        }
        return (bytes * 1000L / limit - interval) / 10L * 10L;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        long size = this.calculateSize(msg);
        long curtime = System.currentTimeMillis();
        if (this.trafficCounter != null) {
            this.trafficCounter.bytesRecvFlowControl(size);
            if (this.readLimit == 0L) {
                ctx.fireChannelRead(msg);
                return;
            }
            long wait = AbstractTrafficShapingHandler.getTimeToWait(this.readLimit, this.trafficCounter.currentReadBytes(), this.trafficCounter.lastTime(), curtime);
            if (wait >= 10L) {
                if (!AbstractTrafficShapingHandler.isSuspended(ctx)) {
                    ctx.attr(READ_SUSPENDED).set(true);
                    Attribute<Runnable> attr = ctx.attr(REOPEN_TASK);
                    Runnable reopenTask = attr.get();
                    if (reopenTask == null) {
                        reopenTask = new ReopenReadTimerTask(ctx);
                        attr.set(reopenTask);
                    }
                    ctx.executor().schedule(reopenTask, wait, TimeUnit.MILLISECONDS);
                } else {
                    Runnable bufferUpdateTask = new Runnable(){

                        @Override
                        public void run() {
                            ctx.fireChannelRead(msg);
                        }
                    };
                    ctx.executor().schedule(bufferUpdateTask, wait, TimeUnit.MILLISECONDS);
                    return;
                }
            }
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void read(ChannelHandlerContext ctx) {
        if (!AbstractTrafficShapingHandler.isSuspended(ctx)) {
            ctx.read();
        }
    }

    private static boolean isSuspended(ChannelHandlerContext ctx) {
        Boolean suspended = ctx.attr(READ_SUSPENDED).get();
        return suspended != null && !Boolean.FALSE.equals(suspended);
    }

    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        long curtime = System.currentTimeMillis();
        long size = this.calculateSize(msg);
        if (size > -1L && this.trafficCounter != null) {
            this.trafficCounter.bytesWriteFlowControl(size);
            if (this.writeLimit == 0L) {
                ctx.write(msg, promise);
                return;
            }
            long wait = AbstractTrafficShapingHandler.getTimeToWait(this.writeLimit, this.trafficCounter.currentWrittenBytes(), this.trafficCounter.lastTime(), curtime);
            if (wait >= 10L) {
                ctx.executor().schedule(new Runnable(){

                    @Override
                    public void run() {
                        ctx.write(msg, promise);
                    }
                }, wait, TimeUnit.MILLISECONDS);
                return;
            }
        }
        ctx.write(msg, promise);
    }

    public TrafficCounter trafficCounter() {
        return this.trafficCounter;
    }

    public String toString() {
        return "TrafficShaping with Write Limit: " + this.writeLimit + " Read Limit: " + this.readLimit + " and Counter: " + (this.trafficCounter != null ? this.trafficCounter.toString() : "none");
    }

    protected long calculateSize(Object msg) {
        if (msg instanceof ByteBuf) {
            return ((ByteBuf)msg).readableBytes();
        }
        if (msg instanceof ByteBufHolder) {
            return ((ByteBufHolder)msg).content().readableBytes();
        }
        return -1L;
    }

    private static final class ReopenReadTimerTask
    implements Runnable {
        final ChannelHandlerContext ctx;

        ReopenReadTimerTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            this.ctx.attr(READ_SUSPENDED).set(false);
            this.ctx.read();
        }
    }
}

