/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.traffic;

import io.netty.channel.ChannelHandler;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.ScheduledExecutorService;

@ChannelHandler.Sharable
public class GlobalTrafficShapingHandler
extends AbstractTrafficShapingHandler {
    void createGlobalTrafficCounter(ScheduledExecutorService executor) {
        if (executor == null) {
            throw new NullPointerException("executor");
        }
        TrafficCounter tc = new TrafficCounter(this, executor, "GlobalTC", this.checkInterval);
        this.setTrafficCounter(tc);
        tc.start();
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit, long checkInterval) {
        super(writeLimit, readLimit, checkInterval);
        this.createGlobalTrafficCounter(executor);
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit) {
        super(writeLimit, readLimit);
        this.createGlobalTrafficCounter(executor);
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long checkInterval) {
        super(checkInterval);
        this.createGlobalTrafficCounter(executor);
    }

    public GlobalTrafficShapingHandler(EventExecutor executor) {
        this.createGlobalTrafficCounter(executor);
    }

    public final void release() {
        if (this.trafficCounter != null) {
            this.trafficCounter.stop();
        }
    }
}

