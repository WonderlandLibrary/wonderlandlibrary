/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.base64;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.base64.Base64Dialect;
import java.util.List;

@ChannelHandler.Sharable
public class Base64Encoder
extends MessageToMessageEncoder<ByteBuf> {
    private final Base64Dialect dialect;
    private final boolean breakLines;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        list.add(Base64.encode(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes(), this.breakLines, this.dialect));
    }

    public Base64Encoder() {
        this(true);
    }

    public Base64Encoder(boolean bl) {
        this(bl, Base64Dialect.STANDARD);
    }

    public Base64Encoder(boolean bl, Base64Dialect base64Dialect) {
        if (base64Dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.breakLines = bl;
        this.dialect = base64Dialect;
    }
}

