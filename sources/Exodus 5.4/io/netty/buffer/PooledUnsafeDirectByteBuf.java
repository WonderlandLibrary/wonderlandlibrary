/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PoolChunk;
import io.netty.buffer.PooledByteBuf;
import io.netty.buffer.SwappedByteBuf;
import io.netty.buffer.UnsafeDirectSwappedByteBuf;
import io.netty.util.Recycler;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

final class PooledUnsafeDirectByteBuf
extends PooledByteBuf<ByteBuffer> {
    private static final Recycler<PooledUnsafeDirectByteBuf> RECYCLER;
    private long memoryAddress;
    private static final boolean NATIVE_ORDER;

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkIndex(n, n3);
        if (byArray == null) {
            throw new NullPointerException("dst");
        }
        if (n2 < 0 || n2 > byArray.length - n3) {
            throw new IndexOutOfBoundsException("dstIndex: " + n2);
        }
        if (n3 != 0) {
            PlatformDependent.copyMemory(this.addr(n), byArray, n2, (long)n3);
        }
        return this;
    }

    @Override
    public boolean hasArray() {
        return false;
    }

    private void getBytes(int n, ByteBuffer byteBuffer, boolean bl) {
        this.checkIndex(n);
        int n2 = Math.min(this.capacity() - n, byteBuffer.remaining());
        ByteBuffer byteBuffer2 = bl ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate();
        n = this.idx(n);
        byteBuffer2.clear().position(n).limit(n + n2);
        byteBuffer.put(byteBuffer2);
    }

    private long addr(int n) {
        return this.memoryAddress + (long)n;
    }

    @Override
    public boolean isDirect() {
        return true;
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        this.checkIndex(n, n2);
        ByteBuf byteBuf = this.alloc().directBuffer(n2, this.maxCapacity());
        if (n2 != 0) {
            if (byteBuf.hasMemoryAddress()) {
                PlatformDependent.copyMemory(this.addr(n), byteBuf.memoryAddress(), n2);
                byteBuf.setIndex(0, n2);
            } else {
                byteBuf.writeBytes(this, n, n2);
            }
        }
        return byteBuf;
    }

    @Override
    void init(PoolChunk<ByteBuffer> poolChunk, long l, int n, int n2, int n3) {
        super.init(poolChunk, l, n, n2, n3);
        this.initMemoryAddress();
    }

    @Override
    protected short _getShort(int n) {
        short s = PlatformDependent.getShort(this.addr(n));
        return NATIVE_ORDER ? s : Short.reverseBytes(s);
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        n = this.idx(n);
        return ((ByteBuffer)((ByteBuffer)this.memory).duplicate().position(n).limit(n + n2)).slice();
    }

    private void initMemoryAddress() {
        this.memoryAddress = PlatformDependent.directBufferAddress((ByteBuffer)this.memory) + (long)this.offset;
    }

    @Override
    protected Recycler<?> recycler() {
        return RECYCLER;
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return new ByteBuffer[]{this.nioBuffer(n, n2)};
    }

    @Override
    protected void _setInt(int n, int n2) {
        PlatformDependent.putInt(this.addr(n), NATIVE_ORDER ? n2 : Integer.reverseBytes(n2));
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        long l = this.addr(n);
        return (PlatformDependent.getByte(l) & 0xFF) << 16 | (PlatformDependent.getByte(l + 1L) & 0xFF) << 8 | PlatformDependent.getByte(l + 2L) & 0xFF;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkIndex(n, n3);
        if (byteBuf == null) {
            throw new NullPointerException("dst");
        }
        if (n2 < 0 || n2 > byteBuf.capacity() - n3) {
            throw new IndexOutOfBoundsException("dstIndex: " + n2);
        }
        if (n3 != 0) {
            if (byteBuf.hasMemoryAddress()) {
                PlatformDependent.copyMemory(this.addr(n), byteBuf.memoryAddress() + (long)n2, n3);
            } else if (byteBuf.hasArray()) {
                PlatformDependent.copyMemory(this.addr(n), byteBuf.array(), byteBuf.arrayOffset() + n2, (long)n3);
            } else {
                byteBuf.setBytes(n2, this, n, n3);
            }
        }
        return this;
    }

    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException("direct buffer");
    }

    @Override
    protected SwappedByteBuf newSwappedByteBuf() {
        return new UnsafeDirectSwappedByteBuf(this);
    }

    static PooledUnsafeDirectByteBuf newInstance(int n) {
        PooledUnsafeDirectByteBuf pooledUnsafeDirectByteBuf = RECYCLER.get();
        pooledUnsafeDirectByteBuf.setRefCnt(1);
        pooledUnsafeDirectByteBuf.maxCapacity(n);
        return pooledUnsafeDirectByteBuf;
    }

    private PooledUnsafeDirectByteBuf(Recycler.Handle handle, int n) {
        super(handle, n);
    }

    @Override
    public boolean hasMemoryAddress() {
        return true;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.getBytes(n, byteBuffer, false);
        return this;
    }

    @Override
    public int nioBufferCount() {
        return 1;
    }

    @Override
    protected void _setShort(int n, int n2) {
        PlatformDependent.putShort(this.addr(n), NATIVE_ORDER ? (short)n2 : Short.reverseBytes((short)n2));
    }

    @Override
    protected void _setLong(int n, long l) {
        PlatformDependent.putLong(this.addr(n), NATIVE_ORDER ? l : Long.reverseBytes(l));
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        this.checkReadableBytes(n);
        int n2 = this.getBytes(this.readerIndex, gatheringByteChannel, n, true);
        this.readerIndex += n2;
        return n2;
    }

    @Override
    protected void _setByte(int n, int n2) {
        PlatformDependent.putByte(this.addr(n), (byte)n2);
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        return this.getBytes(n, gatheringByteChannel, n2, false);
    }

    static {
        NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
        RECYCLER = new Recycler<PooledUnsafeDirectByteBuf>(){

            @Override
            protected PooledUnsafeDirectByteBuf newObject(Recycler.Handle handle) {
                return new PooledUnsafeDirectByteBuf(handle, 0);
            }
        };
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        this.checkIndex(n, n2);
        ByteBuffer byteBuffer = this.internalNioBuffer();
        n = this.idx(n);
        byteBuffer.clear().position(n).limit(n + n2);
        try {
            return scatteringByteChannel.read(byteBuffer);
        }
        catch (ClosedChannelException closedChannelException) {
            return -1;
        }
    }

    @Override
    protected int _getInt(int n) {
        int n2 = PlatformDependent.getInt(this.addr(n));
        return NATIVE_ORDER ? n2 : Integer.reverseBytes(n2);
    }

    @Override
    void initUnpooled(PoolChunk<ByteBuffer> poolChunk, int n) {
        super.initUnpooled(poolChunk, n);
        this.initMemoryAddress();
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.checkIndex(n, n2);
        if (n2 != 0) {
            byte[] byArray = new byte[n2];
            PlatformDependent.copyMemory(this.addr(n), byArray, 0, (long)n2);
            outputStream.write(byArray);
        }
        return this;
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        n = this.idx(n);
        return (ByteBuffer)this.internalNioBuffer().clear().position(n).limit(n + n2);
    }

    private int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2, boolean bl) throws IOException {
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = bl ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate();
        n = this.idx(n);
        byteBuffer.clear().position(n).limit(n + n2);
        return gatheringByteChannel.write(byteBuffer);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        int n = byteBuffer.remaining();
        this.checkReadableBytes(n);
        this.getBytes(this.readerIndex, byteBuffer, true);
        this.readerIndex += n;
        return this;
    }

    @Override
    protected byte _getByte(int n) {
        return PlatformDependent.getByte(this.addr(n));
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        this.checkIndex(n, n2);
        byte[] byArray = new byte[n2];
        int n3 = inputStream.read(byArray);
        if (n3 > 0) {
            PlatformDependent.copyMemory(byArray, 0, this.addr(n), (long)n3);
        }
        return n3;
    }

    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("direct buffer");
    }

    @Override
    protected long _getLong(int n) {
        long l = PlatformDependent.getLong(this.addr(n));
        return NATIVE_ORDER ? l : Long.reverseBytes(l);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        this.checkIndex(n, byteBuffer.remaining());
        ByteBuffer byteBuffer2 = this.internalNioBuffer();
        if (byteBuffer == byteBuffer2) {
            byteBuffer = byteBuffer.duplicate();
        }
        n = this.idx(n);
        byteBuffer2.clear().position(n).limit(n + byteBuffer.remaining());
        byteBuffer2.put(byteBuffer);
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkIndex(n, n3);
        if (n3 != 0) {
            PlatformDependent.copyMemory(byArray, n2, this.addr(n), (long)n3);
        }
        return this;
    }

    @Override
    public long memoryAddress() {
        return this.memoryAddress;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkIndex(n, n3);
        if (byteBuf == null) {
            throw new NullPointerException("src");
        }
        if (n2 < 0 || n2 > byteBuf.capacity() - n3) {
            throw new IndexOutOfBoundsException("srcIndex: " + n2);
        }
        if (n3 != 0) {
            if (byteBuf.hasMemoryAddress()) {
                PlatformDependent.copyMemory(byteBuf.memoryAddress() + (long)n2, this.addr(n), n3);
            } else if (byteBuf.hasArray()) {
                PlatformDependent.copyMemory(byteBuf.array(), byteBuf.arrayOffset() + n2, this.addr(n), (long)n3);
            } else {
                byteBuf.getBytes(n2, this, n, n3);
            }
        }
        return this;
    }

    @Override
    protected void _setMedium(int n, int n2) {
        long l = this.addr(n);
        PlatformDependent.putByte(l, (byte)(n2 >>> 16));
        PlatformDependent.putByte(l + 1L, (byte)(n2 >>> 8));
        PlatformDependent.putByte(l + 2L, (byte)n2);
    }

    @Override
    protected ByteBuffer newInternalNioBuffer(ByteBuffer byteBuffer) {
        return byteBuffer.duplicate();
    }
}

