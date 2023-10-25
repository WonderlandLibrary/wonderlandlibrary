/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

class ReadOnlyByteBufferBuf
extends AbstractReferenceCountedByteBuf {
    private final ByteBufAllocator allocator;
    private ByteBuffer tmpNioBuf;
    protected final ByteBuffer buffer;

    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    @Override
    public ByteBuf capacity(int n) {
        throw new ReadOnlyBufferException();
    }

    @Override
    protected void _setShort(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    protected final ByteBuffer internalNioBuffer() {
        ByteBuffer byteBuffer = this.tmpNioBuf;
        if (byteBuffer == null) {
            this.tmpNioBuf = byteBuffer = this.buffer.duplicate();
        }
        return byteBuffer;
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        ByteBuffer byteBuffer;
        this.ensureAccessible();
        try {
            byteBuffer = (ByteBuffer)this.internalNioBuffer().clear().position(n).limit(n + n2);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IndexOutOfBoundsException("Too many bytes to read - Need " + (n + n2));
        }
        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(n2);
        byteBuffer2.put(byteBuffer);
        byteBuffer2.order(this.order());
        byteBuffer2.clear();
        return new UnpooledDirectByteBuf(this.alloc(), byteBuffer2, this.maxCapacity());
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public int nioBufferCount() {
        return 1;
    }

    @Override
    public short getShort(int n) {
        this.ensureAccessible();
        return this._getShort(n);
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        this.ensureAccessible();
        if (n2 == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = this.internalNioBuffer();
        byteBuffer.clear().position(n).limit(n + n2);
        return gatheringByteChannel.write(byteBuffer);
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkDstIndex(n, n3, n2, byArray.length);
        if (n2 < 0 || n2 > byArray.length - n3) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", n2, n3, byArray.length));
        }
        ByteBuffer byteBuffer = this.internalNioBuffer();
        byteBuffer.clear().position(n).limit(n + n3);
        byteBuffer.get(byArray, n2, n3);
        return this;
    }

    @Override
    protected void _setByte(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return new ByteBuffer[]{this.nioBuffer(n, n2)};
    }

    @Override
    protected void deallocate() {
    }

    @Override
    public int getInt(int n) {
        this.ensureAccessible();
        return this._getInt(n);
    }

    @Override
    public int getUnsignedMedium(int n) {
        this.ensureAccessible();
        return this._getUnsignedMedium(n);
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        return (this.getByte(n) & 0xFF) << 16 | (this.getByte(n + 1) & 0xFF) << 8 | this.getByte(n + 2) & 0xFF;
    }

    @Override
    public ByteBuf unwrap() {
        return null;
    }

    @Override
    protected short _getShort(int n) {
        return this.buffer.getShort(n);
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.allocator;
    }

    @Override
    protected void _setLong(int n, long l) {
        throw new ReadOnlyBufferException();
    }

    @Override
    protected byte _getByte(int n) {
        return this.buffer.get(n);
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        throw new ReadOnlyBufferException();
    }

    @Override
    protected int _getInt(int n) {
        return this.buffer.getInt(n);
    }

    @Override
    protected long _getLong(int n) {
        return this.buffer.getLong(n);
    }

    @Override
    public int capacity() {
        return this.maxCapacity();
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        this.ensureAccessible();
        return (ByteBuffer)this.internalNioBuffer().clear().position(n).limit(n + n2);
    }

    @Override
    public boolean hasMemoryAddress() {
        return false;
    }

    @Override
    public int arrayOffset() {
        return this.buffer.arrayOffset();
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public long getLong(int n) {
        this.ensureAccessible();
        return this._getLong(n);
    }

    @Override
    protected void _setInt(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] array() {
        return this.buffer.array();
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.ensureAccessible();
        if (n2 == 0) {
            return this;
        }
        if (this.buffer.hasArray()) {
            outputStream.write(this.buffer.array(), n + this.buffer.arrayOffset(), n2);
        } else {
            byte[] byArray = new byte[n2];
            ByteBuffer byteBuffer = this.internalNioBuffer();
            byteBuffer.clear().position(n);
            byteBuffer.get(byArray);
            outputStream.write(byArray);
        }
        return this;
    }

    @Override
    public byte getByte(int n) {
        this.ensureAccessible();
        return this._getByte(n);
    }

    ReadOnlyByteBufferBuf(ByteBufAllocator byteBufAllocator, ByteBuffer byteBuffer) {
        super(byteBuffer.remaining());
        if (!byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("must be a readonly buffer: " + StringUtil.simpleClassName(byteBuffer));
        }
        this.allocator = byteBufAllocator;
        this.buffer = byteBuffer.slice().order(ByteOrder.BIG_ENDIAN);
        this.writerIndex(this.buffer.limit());
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkDstIndex(n, n3, n2, byteBuf.capacity());
        if (byteBuf.hasArray()) {
            this.getBytes(n, byteBuf.array(), byteBuf.arrayOffset() + n2, n3);
        } else if (byteBuf.nioBufferCount() > 0) {
            for (ByteBuffer byteBuffer : byteBuf.nioBuffers(n2, n3)) {
                int n4 = byteBuffer.remaining();
                this.getBytes(n, byteBuffer);
                n += n4;
            }
        } else {
            byteBuf.setBytes(n2, this, n, n3);
        }
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.checkIndex(n);
        if (byteBuffer == null) {
            throw new NullPointerException("dst");
        }
        int n2 = Math.min(this.capacity() - n, byteBuffer.remaining());
        ByteBuffer byteBuffer2 = this.internalNioBuffer();
        byteBuffer2.clear().position(n).limit(n + n2);
        byteBuffer.put(byteBuffer2);
        return this;
    }

    @Override
    protected void _setMedium(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        return (ByteBuffer)this.buffer.duplicate().position(n).limit(n + n2);
    }

    @Override
    public boolean hasArray() {
        return this.buffer.hasArray();
    }
}

