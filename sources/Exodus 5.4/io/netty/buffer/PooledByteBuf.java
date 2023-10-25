/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PoolChunk;
import io.netty.util.Recycler;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

abstract class PooledByteBuf<T>
extends AbstractReferenceCountedByteBuf {
    int maxLength;
    protected int length;
    protected long handle;
    private ByteBuffer tmpNioBuf;
    private final Recycler.Handle recyclerHandle;
    protected PoolChunk<T> chunk;
    protected T memory;
    protected int offset;

    protected final ByteBuffer internalNioBuffer() {
        ByteBuffer byteBuffer = this.tmpNioBuf;
        if (byteBuffer == null) {
            this.tmpNioBuf = byteBuffer = this.newInternalNioBuffer(this.memory);
        }
        return byteBuffer;
    }

    protected final int idx(int n) {
        return this.offset + n;
    }

    void initUnpooled(PoolChunk<T> poolChunk, int n) {
        assert (poolChunk != null);
        this.chunk = poolChunk;
        this.handle = 0L;
        this.memory = poolChunk.memory;
        this.offset = 0;
        this.length = this.maxLength = n;
        this.setIndex(0, 0);
        this.tmpNioBuf = null;
    }

    @Override
    public final ByteBufAllocator alloc() {
        return this.chunk.arena.parent;
    }

    @Override
    protected final void deallocate() {
        if (this.handle >= 0L) {
            long l = this.handle;
            this.handle = -1L;
            this.memory = null;
            this.chunk.arena.free(this.chunk, l, this.maxLength);
            this.recycle();
        }
    }

    @Override
    public final ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    private void recycle() {
        Recycler.Handle handle = this.recyclerHandle;
        if (handle != null) {
            this.recycler().recycle(this, handle);
        }
    }

    @Override
    public final int capacity() {
        return this.length;
    }

    protected abstract Recycler<?> recycler();

    protected abstract ByteBuffer newInternalNioBuffer(T var1);

    void init(PoolChunk<T> poolChunk, long l, int n, int n2, int n3) {
        assert (l >= 0L);
        assert (poolChunk != null);
        this.chunk = poolChunk;
        this.handle = l;
        this.memory = poolChunk.memory;
        this.offset = n;
        this.length = n2;
        this.maxLength = n3;
        this.setIndex(0, 0);
        this.tmpNioBuf = null;
    }

    @Override
    public final ByteBuf unwrap() {
        return null;
    }

    protected PooledByteBuf(Recycler.Handle handle, int n) {
        super(n);
        this.recyclerHandle = handle;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public final ByteBuf capacity(int n) {
        this.ensureAccessible();
        if (this.chunk.unpooled) {
            if (n == this.length) {
                return this;
            }
        } else if (n > this.length) {
            if (n <= this.maxLength) {
                this.length = n;
                return this;
            }
        } else {
            if (n >= this.length) return this;
            if (n > this.maxLength >>> 1) {
                if (this.maxLength <= 512) {
                    if (n > this.maxLength - 16) {
                        this.length = n;
                        this.setIndex(Math.min(this.readerIndex(), n), Math.min(this.writerIndex(), n));
                        return this;
                    }
                } else {
                    this.length = n;
                    this.setIndex(Math.min(this.readerIndex(), n), Math.min(this.writerIndex(), n));
                    return this;
                }
            }
        }
        this.chunk.arena.reallocate(this, n, true);
        return this;
    }
}

