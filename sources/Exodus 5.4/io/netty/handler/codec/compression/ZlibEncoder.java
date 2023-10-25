/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

public abstract class ZlibEncoder
extends MessageToByteEncoder<ByteBuf> {
    public abstract ChannelFuture close();

    public abstract boolean isClosed();

    public abstract ChannelFuture close(ChannelPromise var1);

    protected ZlibEncoder() {
        super(false);
    }
}

