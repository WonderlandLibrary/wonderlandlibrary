/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.StringUtil;

public class DefaultByteBufHolder
implements ByteBufHolder {
    private final ByteBuf data;

    @Override
    public boolean release(int n) {
        return this.data.release(n);
    }

    @Override
    public int refCnt() {
        return this.data.refCnt();
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.content().toString() + ')';
    }

    @Override
    public ByteBufHolder duplicate() {
        return new DefaultByteBufHolder(this.data.duplicate());
    }

    @Override
    public ByteBufHolder retain() {
        this.data.retain();
        return this;
    }

    public DefaultByteBufHolder(ByteBuf byteBuf) {
        if (byteBuf == null) {
            throw new NullPointerException("data");
        }
        this.data = byteBuf;
    }

    @Override
    public boolean release() {
        return this.data.release();
    }

    @Override
    public ByteBuf content() {
        if (this.data.refCnt() <= 0) {
            throw new IllegalReferenceCountException(this.data.refCnt());
        }
        return this.data;
    }

    @Override
    public ByteBufHolder copy() {
        return new DefaultByteBufHolder(this.data.copy());
    }

    @Override
    public ByteBufHolder retain(int n) {
        this.data.retain(n);
        return this;
    }
}

