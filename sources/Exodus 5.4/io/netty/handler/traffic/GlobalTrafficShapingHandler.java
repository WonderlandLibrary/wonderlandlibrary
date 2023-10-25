/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.concurrent.EventExecutor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class GlobalTrafficShapingHandler
extends AbstractTrafficShapingHandler {
    private Map<Integer, List<ToSend>> messagesQueues = new HashMap<Integer, List<ToSend>>();

    public GlobalTrafficShapingHandler(ScheduledExecutorService scheduledExecutorService, long l) {
        super(l);
        this.createGlobalTrafficCounter(scheduledExecutorService);
    }

    private synchronized void sendAllValid(ChannelHandlerContext channelHandlerContext, List<ToSend> list) {
        while (!list.isEmpty()) {
            ToSend toSend = list.remove(0);
            if (toSend.date <= System.currentTimeMillis()) {
                channelHandlerContext.write(toSend.toSend, toSend.promise);
                continue;
            }
            list.add(0, toSend);
            break;
        }
        channelHandlerContext.flush();
    }

    void createGlobalTrafficCounter(ScheduledExecutorService scheduledExecutorService) {
        if (scheduledExecutorService == null) {
            throw new NullPointerException("executor");
        }
        TrafficCounter trafficCounter = new TrafficCounter(this, scheduledExecutorService, "GlobalTC", this.checkInterval);
        this.setTrafficCounter(trafficCounter);
        trafficCounter.start();
    }

    @Override
    public synchronized void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        Integer n = channelHandlerContext.channel().hashCode();
        List<ToSend> list = this.messagesQueues.remove(n);
        if (list != null) {
            for (ToSend toSend : list) {
                if (!(toSend.toSend instanceof ByteBuf)) continue;
                ((ByteBuf)toSend.toSend).release();
            }
            list.clear();
        }
    }

    @Override
    protected synchronized void submitWrite(final ChannelHandlerContext channelHandlerContext, Object object, long l, ChannelPromise channelPromise) {
        Integer n = channelHandlerContext.channel().hashCode();
        List<ToSend> list = this.messagesQueues.get(n);
        if (l == 0L && (list == null || list.isEmpty())) {
            channelHandlerContext.write(object, channelPromise);
            return;
        }
        ToSend toSend = new ToSend(l, object, channelPromise);
        if (list == null) {
            list = new LinkedList<ToSend>();
            this.messagesQueues.put(n, list);
        }
        list.add(toSend);
        final List<ToSend> list2 = list;
        channelHandlerContext.executor().schedule(new Runnable(){

            @Override
            public void run() {
                GlobalTrafficShapingHandler.this.sendAllValid(channelHandlerContext, list2);
            }
        }, l, TimeUnit.MILLISECONDS);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        Integer n = channelHandlerContext.channel().hashCode();
        LinkedList linkedList = new LinkedList();
        this.messagesQueues.put(n, linkedList);
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService scheduledExecutorService, long l, long l2) {
        super(l, l2);
        this.createGlobalTrafficCounter(scheduledExecutorService);
    }

    public final void release() {
        if (this.trafficCounter != null) {
            this.trafficCounter.stop();
        }
    }

    public GlobalTrafficShapingHandler(EventExecutor eventExecutor) {
        this.createGlobalTrafficCounter(eventExecutor);
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService scheduledExecutorService, long l, long l2, long l3) {
        super(l, l2, l3);
        this.createGlobalTrafficCounter(scheduledExecutorService);
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService scheduledExecutorService, long l, long l2, long l3, long l4) {
        super(l, l2, l3, l4);
        this.createGlobalTrafficCounter(scheduledExecutorService);
    }

    private static final class ToSend {
        final Object toSend;
        final ChannelPromise promise;
        final long date;

        private ToSend(long l, Object object, ChannelPromise channelPromise) {
            this.date = System.currentTimeMillis() + l;
            this.toSend = object;
            this.promise = channelPromise;
        }
    }
}

