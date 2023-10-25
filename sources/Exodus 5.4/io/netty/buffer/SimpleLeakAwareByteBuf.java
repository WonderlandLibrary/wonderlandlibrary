/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.WrappedByteBuf;
import io.netty.util.ResourceLeak;
import java.nio.ByteOrder;

final class SimpleLeakAwareByteBuf
extends WrappedByteBuf {
    private final ResourceLeak leak;

    @Override
    public ByteBuf slice() {
        return new SimpleLeakAwareByteBuf(super.slice(), this.leak);
    }

    SimpleLeakAwareByteBuf(ByteBuf byteBuf, ResourceLeak resourceLeak) {
        super(byteBuf);
        this.leak = resourceLeak;
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        this.leak.record();
        if (this.order() == byteOrder) {
            return this;
        }
        return new SimpleLeakAwareByteBuf(super.order(byteOrder), this.leak);
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        return new SimpleLeakAwareByteBuf(super.slice(n, n2), this.leak);
    }

    @Override
    public ByteBuf duplicate() {
        return new SimpleLeakAwareByteBuf(super.duplicate(), this.leak);
    }

    @Override
    public boolean release(int n) {
        boolean bl = super.release(n);
        if (bl) {
            this.leak.close();
        }
        return bl;
    }

    @Override
    public ByteBuf readSlice(int n) {
        return new SimpleLeakAwareByteBuf(super.readSlice(n), this.leak);
    }

    @Override
    public boolean release() {
        boolean bl = super.release();
        if (bl) {
            this.leak.close();
        }
        return bl;
    }
}

