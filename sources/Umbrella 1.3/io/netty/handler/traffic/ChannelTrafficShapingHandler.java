/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.traffic;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

public class ChannelTrafficShapingHandler
extends AbstractTrafficShapingHandler {
    public ChannelTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval) {
        super(writeLimit, readLimit, checkInterval);
    }

    public ChannelTrafficShapingHandler(long writeLimit, long readLimit) {
        super(writeLimit, readLimit);
    }

    public ChannelTrafficShapingHandler(long checkInterval) {
        super(checkInterval);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        TrafficCounter trafficCounter = new TrafficCounter(this, ctx.executor(), "ChannelTC" + ctx.channel().hashCode(), this.checkInterval);
        this.setTrafficCounter(trafficCounter);
        trafficCounter.start();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (this.trafficCounter != null) {
            this.trafficCounter.stop();
        }
    }
}

