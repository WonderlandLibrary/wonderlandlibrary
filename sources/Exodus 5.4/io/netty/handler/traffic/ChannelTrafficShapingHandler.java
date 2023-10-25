/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChannelTrafficShapingHandler
extends AbstractTrafficShapingHandler {
    private List<ToSend> messagesQueue = new LinkedList<ToSend>();

    private synchronized void sendAllValid(ChannelHandlerContext channelHandlerContext) {
        while (!this.messagesQueue.isEmpty()) {
            ToSend toSend = this.messagesQueue.remove(0);
            if (toSend.date <= System.currentTimeMillis()) {
                channelHandlerContext.write(toSend.toSend, toSend.promise);
                continue;
            }
            this.messagesQueue.add(0, toSend);
            break;
        }
        channelHandlerContext.flush();
    }

    @Override
    public synchronized void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.trafficCounter != null) {
            this.trafficCounter.stop();
        }
        for (ToSend toSend : this.messagesQueue) {
            if (!(toSend.toSend instanceof ByteBuf)) continue;
            ((ByteBuf)toSend.toSend).release();
        }
        this.messagesQueue.clear();
    }

    @Override
    protected synchronized void submitWrite(final ChannelHandlerContext channelHandlerContext, Object object, long l, ChannelPromise channelPromise) {
        if (l == 0L && this.messagesQueue.isEmpty()) {
            channelHandlerContext.write(object, channelPromise);
            return;
        }
        ToSend toSend = new ToSend(l, object, channelPromise);
        this.messagesQueue.add(toSend);
        channelHandlerContext.executor().schedule(new Runnable(){

            @Override
            public void run() {
                ChannelTrafficShapingHandler.this.sendAllValid(channelHandlerContext);
            }
        }, l, TimeUnit.MILLISECONDS);
    }

    public ChannelTrafficShapingHandler(long l) {
        super(l);
    }

    public ChannelTrafficShapingHandler(long l, long l2, long l3, long l4) {
        super(l, l2, l3, l4);
    }

    public ChannelTrafficShapingHandler(long l, long l2, long l3) {
        super(l, l2, l3);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        TrafficCounter trafficCounter = new TrafficCounter(this, channelHandlerContext.executor(), "ChannelTC" + channelHandlerContext.channel().hashCode(), this.checkInterval);
        this.setTrafficCounter(trafficCounter);
        trafficCounter.start();
    }

    public ChannelTrafficShapingHandler(long l, long l2) {
        super(l, l2);
    }

    private static final class ToSend {
        final ChannelPromise promise;
        final long date;
        final Object toSend;

        private ToSend(long l, Object object, ChannelPromise channelPromise) {
            this.date = System.currentTimeMillis() + l;
            this.toSend = object;
            this.promise = channelPromise;
        }
    }
}

