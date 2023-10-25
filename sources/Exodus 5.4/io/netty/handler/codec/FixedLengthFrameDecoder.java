/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class FixedLengthFrameDecoder
extends ByteToMessageDecoder {
    private final int frameLength;

    public FixedLengthFrameDecoder(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer: " + n);
        }
        this.frameLength = n;
    }

    protected Object decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        if (byteBuf.readableBytes() < this.frameLength) {
            return null;
        }
        return byteBuf.readSlice(this.frameLength).retain();
    }

    @Override
    protected final void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object object = this.decode(channelHandlerContext, byteBuf);
        if (object != null) {
            list.add(object);
        }
    }
}

