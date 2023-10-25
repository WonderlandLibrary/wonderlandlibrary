/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.sctp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

public class SctpOutboundByteStreamHandler
extends MessageToMessageEncoder<ByteBuf> {
    private final int protocolIdentifier;
    private final int streamIdentifier;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        list.add(new SctpMessage(this.streamIdentifier, this.protocolIdentifier, (ByteBuf)byteBuf.retain()));
    }

    public SctpOutboundByteStreamHandler(int n, int n2) {
        this.streamIdentifier = n;
        this.protocolIdentifier = n2;
    }
}

