/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.SwappedByteBuf;
import io.netty.buffer.WrappedByteBuf;
import java.nio.ByteOrder;

final class UnreleasableByteBuf
extends WrappedByteBuf {
    private SwappedByteBuf swappedBuf;

    @Override
    public ByteBuf slice() {
        return new UnreleasableByteBuf(this.buf.slice());
    }

    @Override
    public ByteBuf retain(int n) {
        return this;
    }

    @Override
    public ByteBuf retain() {
        return this;
    }

    @Override
    public ByteBuf readSlice(int n) {
        return new UnreleasableByteBuf(this.buf.readSlice(n));
    }

    @Override
    public ByteBuf duplicate() {
        return new UnreleasableByteBuf(this.buf.duplicate());
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        return new UnreleasableByteBuf(this.buf.slice(n, n2));
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        if (byteOrder == null) {
            throw new NullPointerException("endianness");
        }
        if (byteOrder == this.order()) {
            return this;
        }
        SwappedByteBuf swappedByteBuf = this.swappedBuf;
        if (swappedByteBuf == null) {
            this.swappedBuf = swappedByteBuf = new SwappedByteBuf(this);
        }
        return swappedByteBuf;
    }

    @Override
    public boolean release() {
        return false;
    }

    @Override
    public boolean release(int n) {
        return false;
    }

    UnreleasableByteBuf(ByteBuf byteBuf) {
        super(byteBuf);
    }
}

