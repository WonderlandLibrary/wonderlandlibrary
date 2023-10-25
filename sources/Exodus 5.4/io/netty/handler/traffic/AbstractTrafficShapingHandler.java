/*
 * Decompiled with CFR 0.152.
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
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTrafficShapingHandler
extends ChannelDuplexHandler {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractTrafficShapingHandler.class);
    protected TrafficCounter trafficCounter;
    private static final AttributeKey<Runnable> REOPEN_TASK;
    private static final AttributeKey<Boolean> READ_SUSPENDED;
    public static final long DEFAULT_MAX_TIME = 15000L;
    static final long MINIMAL_WAIT = 10L;
    private long readLimit;
    protected long maxTime = 15000L;
    protected long checkInterval = 1000L;
    private long writeLimit;
    public static final long DEFAULT_CHECK_INTERVAL = 1000L;

    public long getCheckInterval() {
        return this.checkInterval;
    }

    protected static boolean isHandlerActive(ChannelHandlerContext channelHandlerContext) {
        Boolean bl = channelHandlerContext.attr(READ_SUSPENDED).get();
        return bl == null || Boolean.FALSE.equals(bl);
    }

    public long getWriteLimit() {
        return this.writeLimit;
    }

    public void configure(long l) {
        this.checkInterval = l;
        if (this.trafficCounter != null) {
            this.trafficCounter.configure(this.checkInterval);
        }
    }

    protected void doAccounting(TrafficCounter trafficCounter) {
    }

    protected AbstractTrafficShapingHandler(long l, long l2, long l3, long l4) {
        this.writeLimit = l;
        this.readLimit = l2;
        this.checkInterval = l3;
        this.maxTime = l4;
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        long l;
        long l2 = this.calculateSize(object);
        if (l2 > 0L && this.trafficCounter != null && (l = this.trafficCounter.writeTimeToWait(l2, this.writeLimit, this.maxTime)) >= 10L) {
            if (logger.isDebugEnabled()) {
                logger.debug("Channel:" + channelHandlerContext.channel().hashCode() + " Write suspend: " + l + ":" + channelHandlerContext.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(channelHandlerContext));
            }
            this.submitWrite(channelHandlerContext, object, l, channelPromise);
            return;
        }
        this.submitWrite(channelHandlerContext, object, 0L, channelPromise);
    }

    public void setWriteLimit(long l) {
        this.writeLimit = l;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
        }
    }

    public long getReadLimit() {
        return this.readLimit;
    }

    public TrafficCounter trafficCounter() {
        return this.trafficCounter;
    }

    protected AbstractTrafficShapingHandler() {
        this(0L, 0L, 1000L, 15000L);
    }

    public void setCheckInterval(long l) {
        this.checkInterval = l;
        if (this.trafficCounter != null) {
            this.trafficCounter.configure(l);
        }
    }

    public long getMaxTimeWait() {
        return this.maxTime;
    }

    @Override
    public void read(ChannelHandlerContext channelHandlerContext) {
        if (AbstractTrafficShapingHandler.isHandlerActive(channelHandlerContext)) {
            channelHandlerContext.read();
        }
    }

    protected long calculateSize(Object object) {
        if (object instanceof ByteBuf) {
            return ((ByteBuf)object).readableBytes();
        }
        if (object instanceof ByteBufHolder) {
            return ((ByteBufHolder)object).content().readableBytes();
        }
        return -1L;
    }

    protected abstract void submitWrite(ChannelHandlerContext var1, Object var2, long var3, ChannelPromise var5);

    public String toString() {
        return "TrafficShaping with Write Limit: " + this.writeLimit + " Read Limit: " + this.readLimit + " and Counter: " + (this.trafficCounter != null ? this.trafficCounter.toString() : "none");
    }

    protected AbstractTrafficShapingHandler(long l, long l2, long l3) {
        this(l, l2, l3, 15000L);
    }

    public void configure(long l, long l2) {
        this.writeLimit = l;
        this.readLimit = l2;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
        }
    }

    public void setMaxTimeWait(long l) {
        this.maxTime = l;
    }

    public void setReadLimit(long l) {
        this.readLimit = l;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
        }
    }

    protected AbstractTrafficShapingHandler(long l) {
        this(0L, 0L, l, 15000L);
    }

    static {
        READ_SUSPENDED = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
        REOPEN_TASK = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".REOPEN_TASK");
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        long l;
        long l2 = this.calculateSize(object);
        if (l2 > 0L && this.trafficCounter != null && (l = this.trafficCounter.readTimeToWait(l2, this.readLimit, this.maxTime)) >= 10L) {
            if (logger.isDebugEnabled()) {
                logger.debug("Channel:" + channelHandlerContext.channel().hashCode() + " Read Suspend: " + l + ":" + channelHandlerContext.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(channelHandlerContext));
            }
            if (channelHandlerContext.channel().config().isAutoRead() && AbstractTrafficShapingHandler.isHandlerActive(channelHandlerContext)) {
                channelHandlerContext.channel().config().setAutoRead(false);
                channelHandlerContext.attr(READ_SUSPENDED).set(true);
                Attribute<Runnable> attribute = channelHandlerContext.attr(REOPEN_TASK);
                Runnable runnable = attribute.get();
                if (runnable == null) {
                    runnable = new ReopenReadTimerTask(channelHandlerContext);
                    attribute.set(runnable);
                }
                channelHandlerContext.executor().schedule(runnable, l, TimeUnit.MILLISECONDS);
                if (logger.isDebugEnabled()) {
                    logger.debug("Channel:" + channelHandlerContext.channel().hashCode() + " Suspend final status => " + channelHandlerContext.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(channelHandlerContext) + " will reopened at: " + l);
                }
            }
        }
        channelHandlerContext.fireChannelRead(object);
    }

    public void configure(long l, long l2, long l3) {
        this.configure(l, l2);
        this.configure(l3);
    }

    protected AbstractTrafficShapingHandler(long l, long l2) {
        this(l, l2, 1000L, 15000L);
    }

    void setTrafficCounter(TrafficCounter trafficCounter) {
        this.trafficCounter = trafficCounter;
    }

    private static final class ReopenReadTimerTask
    implements Runnable {
        final ChannelHandlerContext ctx;

        ReopenReadTimerTask(ChannelHandlerContext channelHandlerContext) {
            this.ctx = channelHandlerContext;
        }

        @Override
        public void run() {
            if (!this.ctx.channel().config().isAutoRead() && AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Channel:" + this.ctx.channel().hashCode() + " Not Unsuspend: " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
                }
                this.ctx.attr(READ_SUSPENDED).set(false);
            } else {
                if (logger.isDebugEnabled()) {
                    if (this.ctx.channel().config().isAutoRead() && !AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
                        logger.debug("Channel:" + this.ctx.channel().hashCode() + " Unsuspend: " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
                    } else {
                        logger.debug("Channel:" + this.ctx.channel().hashCode() + " Normal Unsuspend: " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
                    }
                }
                this.ctx.attr(READ_SUSPENDED).set(false);
                this.ctx.channel().config().setAutoRead(true);
                this.ctx.channel().read();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Channel:" + this.ctx.channel().hashCode() + " Unsupsend final status => " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
            }
        }
    }
}

