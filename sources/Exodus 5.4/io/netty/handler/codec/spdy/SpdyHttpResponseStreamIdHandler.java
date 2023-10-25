/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.spdy.SpdyHttpHeaders;
import io.netty.handler.codec.spdy.SpdyRstStreamFrame;
import io.netty.util.ReferenceCountUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SpdyHttpResponseStreamIdHandler
extends MessageToMessageCodec<Object, HttpMessage> {
    private static final Integer NO_ID = -1;
    private final Queue<Integer> ids = new LinkedList<Integer>();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, HttpMessage httpMessage, List<Object> list) throws Exception {
        Integer n = this.ids.poll();
        if (n != null && n.intValue() != NO_ID.intValue() && !httpMessage.headers().contains("X-SPDY-Stream-ID")) {
            SpdyHttpHeaders.setStreamId(httpMessage, n);
        }
        list.add(ReferenceCountUtil.retain(httpMessage));
    }

    @Override
    public boolean acceptInboundMessage(Object object) throws Exception {
        return object instanceof HttpMessage || object instanceof SpdyRstStreamFrame;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object object, List<Object> list) throws Exception {
        if (object instanceof HttpMessage) {
            boolean bl = ((HttpMessage)object).headers().contains("X-SPDY-Stream-ID");
            if (!bl) {
                this.ids.add(NO_ID);
            } else {
                this.ids.add(SpdyHttpHeaders.getStreamId((HttpMessage)object));
            }
        } else if (object instanceof SpdyRstStreamFrame) {
            this.ids.remove(((SpdyRstStreamFrame)object).streamId());
        }
        list.add(ReferenceCountUtil.retain(object));
    }
}

