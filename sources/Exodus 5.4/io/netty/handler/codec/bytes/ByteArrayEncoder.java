/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.bytes;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

@ChannelHandler.Sharable
public class ByteArrayEncoder
extends MessageToMessageEncoder<byte[]> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, byte[] byArray, List<Object> list) throws Exception {
        list.add(Unpooled.wrappedBuffer(byArray));
    }
}

