/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;

public abstract class AbstractDerivedByteBuf
extends AbstractByteBuf {
    @Override
    public final int refCnt() {
        return this.unwrap().refCnt();
    }

    @Override
    public final ByteBuf retain(int n) {
        this.unwrap().retain(n);
        return this;
    }

    @Override
    public final ByteBuf retain() {
        this.unwrap().retain();
        return this;
    }

    @Override
    public final boolean release() {
        return this.unwrap().release();
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        return this.unwrap().nioBuffer(n, n2);
    }

    protected AbstractDerivedByteBuf(int n) {
        super(n);
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        return this.nioBuffer(n, n2);
    }

    @Override
    public final boolean release(int n) {
        return this.unwrap().release(n);
    }
}

