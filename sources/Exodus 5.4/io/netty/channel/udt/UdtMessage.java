/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.udt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

public final class UdtMessage
extends DefaultByteBufHolder {
    @Override
    public UdtMessage duplicate() {
        return new UdtMessage(this.content().duplicate());
    }

    @Override
    public UdtMessage retain(int n) {
        super.retain(n);
        return this;
    }

    @Override
    public UdtMessage copy() {
        return new UdtMessage(this.content().copy());
    }

    public UdtMessage(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    public UdtMessage retain() {
        super.retain();
        return this;
    }
}

