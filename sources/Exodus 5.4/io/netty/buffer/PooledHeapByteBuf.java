/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBuf;
import io.netty.util.Recycler;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

final class PooledHeapByteBuf
extends PooledByteBuf<byte[]> {
    private static final Recycler<PooledHeapByteBuf> RECYCLER = new Recycler<PooledHeapByteBuf>(){

        @Override
        protected PooledHeapByteBuf newObject(Recycler.Handle handle) {
            return new PooledHeapByteBuf(handle, 0);
        }
    };

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        this.checkIndex(n, n2);
        return inputStream.read((byte[])this.memory, this.idx(n), n2);
    }

    @Override
    protected ByteBuffer newInternalNioBuffer(byte[] byArray) {
        return ByteBuffer.wrap(byArray);
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        this.checkIndex(n, n2);
        ByteBuf byteBuf = this.alloc().heapBuffer(n2, this.maxCapacity());
        byteBuf.writeBytes((byte[])this.memory, this.idx(n), n2);
        return byteBuf;
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return new ByteBuffer[]{this.nioBuffer(n, n2)};
    }

    @Override
    public boolean hasArray() {
        return true;
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        return this.getBytes(n, gatheringByteChannel, n2, false);
    }

    @Override
    protected void _setLong(int n, long l) {
        n = this.idx(n);
        ((byte[])this.memory)[n] = (byte)(l >>> 56);
        ((byte[])this.memory)[n + 1] = (byte)(l >>> 48);
        ((byte[])this.memory)[n + 2] = (byte)(l >>> 40);
        ((byte[])this.memory)[n + 3] = (byte)(l >>> 32);
        ((byte[])this.memory)[n + 4] = (byte)(l >>> 24);
        ((byte[])this.memory)[n + 5] = (byte)(l >>> 16);
        ((byte[])this.memory)[n + 6] = (byte)(l >>> 8);
        ((byte[])this.memory)[n + 7] = (byte)l;
    }

    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void _setByte(int n, int n2) {
        ((byte[])this.memory)[this.idx((int)n)] = (byte)n2;
    }

    @Override
    protected void _setInt(int n, int n2) {
        n = this.idx(n);
        ((byte[])this.memory)[n] = (byte)(n2 >>> 24);
        ((byte[])this.memory)[n + 1] = (byte)(n2 >>> 16);
        ((byte[])this.memory)[n + 2] = (byte)(n2 >>> 8);
        ((byte[])this.memory)[n + 3] = (byte)n2;
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        n = this.idx(n);
        ByteBuffer byteBuffer = ByteBuffer.wrap((byte[])this.memory, n, n2);
        return byteBuffer.slice();
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.checkIndex(n);
        byteBuffer.put((byte[])this.memory, this.idx(n), Math.min(this.capacity() - n, byteBuffer.remaining()));
        return this;
    }

    @Override
    protected short _getShort(int n) {
        n = this.idx(n);
        return (short)(((byte[])this.memory)[n] << 8 | ((byte[])this.memory)[n + 1] & 0xFF);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        this.checkReadableBytes(n);
        int n2 = this.getBytes(this.readerIndex, gatheringByteChannel, n, true);
        this.readerIndex += n2;
        return n2;
    }

    @Override
    public boolean isDirect() {
        return false;
    }

    @Override
    protected int _getInt(int n) {
        n = this.idx(n);
        return (((byte[])this.memory)[n] & 0xFF) << 24 | (((byte[])this.memory)[n + 1] & 0xFF) << 16 | (((byte[])this.memory)[n + 2] & 0xFF) << 8 | ((byte[])this.memory)[n + 3] & 0xFF;
    }

    @Override
    public byte[] array() {
        return (byte[])this.memory;
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkDstIndex(n, n3, n2, byArray.length);
        System.arraycopy(this.memory, this.idx(n), byArray, n2, n3);
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.checkIndex(n, n2);
        outputStream.write((byte[])this.memory, this.idx(n), n2);
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkSrcIndex(n, n3, n2, byteBuf.capacity());
        if (byteBuf.hasMemoryAddress()) {
            PlatformDependent.copyMemory(byteBuf.memoryAddress() + (long)n2, (byte[])this.memory, this.idx(n), (long)n3);
        } else if (byteBuf.hasArray()) {
            this.setBytes(n, byteBuf.array(), byteBuf.arrayOffset() + n2, n3);
        } else {
            byteBuf.getBytes(n2, (byte[])this.memory, this.idx(n), n3);
        }
        return this;
    }

    private PooledHeapByteBuf(Recycler.Handle handle, int n) {
        super(handle, n);
    }

    @Override
    protected Recycler<?> recycler() {
        return RECYCLER;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkDstIndex(n, n3, n2, byteBuf.capacity());
        if (byteBuf.hasMemoryAddress()) {
            PlatformDependent.copyMemory((byte[])this.memory, this.idx(n), byteBuf.memoryAddress() + (long)n2, (long)n3);
        } else if (byteBuf.hasArray()) {
            this.getBytes(n, byteBuf.array(), byteBuf.arrayOffset() + n2, n3);
        } else {
            byteBuf.setBytes(n2, (byte[])this.memory, this.idx(n), n3);
        }
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkSrcIndex(n, n3, n2, byArray.length);
        System.arraycopy(byArray, n2, this.memory, this.idx(n), n3);
        return this;
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        this.checkIndex(n, n2);
        n = this.idx(n);
        try {
            return scatteringByteChannel.read((ByteBuffer)this.internalNioBuffer().clear().position(n).limit(n + n2));
        }
        catch (ClosedChannelException closedChannelException) {
            return -1;
        }
    }

    private int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2, boolean bl) throws IOException {
        this.checkIndex(n, n2);
        n = this.idx(n);
        ByteBuffer byteBuffer = bl ? this.internalNioBuffer() : ByteBuffer.wrap((byte[])this.memory);
        return gatheringByteChannel.write((ByteBuffer)byteBuffer.clear().position(n).limit(n + n2));
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        n = this.idx(n);
        return (((byte[])this.memory)[n] & 0xFF) << 16 | (((byte[])this.memory)[n + 1] & 0xFF) << 8 | ((byte[])this.memory)[n + 2] & 0xFF;
    }

    static PooledHeapByteBuf newInstance(int n) {
        PooledHeapByteBuf pooledHeapByteBuf = RECYCLER.get();
        pooledHeapByteBuf.setRefCnt(1);
        pooledHeapByteBuf.maxCapacity(n);
        return pooledHeapByteBuf;
    }

    @Override
    protected void _setShort(int n, int n2) {
        n = this.idx(n);
        ((byte[])this.memory)[n] = (byte)(n2 >>> 8);
        ((byte[])this.memory)[n + 1] = (byte)n2;
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        n = this.idx(n);
        return (ByteBuffer)this.internalNioBuffer().clear().position(n).limit(n + n2);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        int n2 = byteBuffer.remaining();
        this.checkIndex(n, n2);
        byteBuffer.get((byte[])this.memory, this.idx(n), n2);
        return this;
    }

    @Override
    public int nioBufferCount() {
        return 1;
    }

    @Override
    protected byte _getByte(int n) {
        return ((byte[])this.memory)[this.idx(n)];
    }

    @Override
    protected void _setMedium(int n, int n2) {
        n = this.idx(n);
        ((byte[])this.memory)[n] = (byte)(n2 >>> 16);
        ((byte[])this.memory)[n + 1] = (byte)(n2 >>> 8);
        ((byte[])this.memory)[n + 2] = (byte)n2;
    }

    @Override
    public boolean hasMemoryAddress() {
        return false;
    }

    @Override
    public int arrayOffset() {
        return this.offset;
    }

    @Override
    protected long _getLong(int n) {
        n = this.idx(n);
        return ((long)((byte[])this.memory)[n] & 0xFFL) << 56 | ((long)((byte[])this.memory)[n + 1] & 0xFFL) << 48 | ((long)((byte[])this.memory)[n + 2] & 0xFFL) << 40 | ((long)((byte[])this.memory)[n + 3] & 0xFFL) << 32 | ((long)((byte[])this.memory)[n + 4] & 0xFFL) << 24 | ((long)((byte[])this.memory)[n + 5] & 0xFFL) << 16 | ((long)((byte[])this.memory)[n + 6] & 0xFFL) << 8 | (long)((byte[])this.memory)[n + 7] & 0xFFL;
    }
}

