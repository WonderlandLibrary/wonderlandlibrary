/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class UnpooledHeapByteBuf
extends AbstractReferenceCountedByteBuf {
    private ByteBuffer tmpNioBuf;
    private byte[] array;
    private final ByteBufAllocator alloc;

    @Override
    protected void _setByte(int n, int n2) {
        this.array[n] = (byte)n2;
    }

    @Override
    public ByteBuf setMedium(int n, int n2) {
        this.ensureAccessible();
        this._setMedium(n, n2);
        return this;
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        this.ensureAccessible();
        this._setShort(n, n2);
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        this.ensureAccessible();
        byteBuffer.get(this.array, n, byteBuffer.remaining());
        return this;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.alloc;
    }

    @Override
    protected short _getShort(int n) {
        return (short)(this.array[n] << 8 | this.array[n + 1] & 0xFF);
    }

    protected UnpooledHeapByteBuf(ByteBufAllocator byteBufAllocator, byte[] byArray, int n) {
        this(byteBufAllocator, byArray, 0, byArray.length, n);
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        this.ensureAccessible();
        return this.getBytes(n, gatheringByteChannel, n2, false);
    }

    @Override
    public int capacity() {
        this.ensureAccessible();
        return this.array.length;
    }

    @Override
    protected void _setInt(int n, int n2) {
        this.array[n] = (byte)(n2 >>> 24);
        this.array[n + 1] = (byte)(n2 >>> 16);
        this.array[n + 2] = (byte)(n2 >>> 8);
        this.array[n + 3] = (byte)n2;
    }

    @Override
    public int arrayOffset() {
        return 0;
    }

    @Override
    public boolean hasMemoryAddress() {
        return false;
    }

    @Override
    protected void _setLong(int n, long l) {
        this.array[n] = (byte)(l >>> 56);
        this.array[n + 1] = (byte)(l >>> 48);
        this.array[n + 2] = (byte)(l >>> 40);
        this.array[n + 3] = (byte)(l >>> 32);
        this.array[n + 4] = (byte)(l >>> 24);
        this.array[n + 5] = (byte)(l >>> 16);
        this.array[n + 6] = (byte)(l >>> 8);
        this.array[n + 7] = (byte)l;
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.ensureAccessible();
        outputStream.write(this.array, n, n2);
        return this;
    }

    private UnpooledHeapByteBuf(ByteBufAllocator byteBufAllocator, byte[] byArray, int n, int n2, int n3) {
        super(n3);
        if (byteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        if (byArray == null) {
            throw new NullPointerException("initialArray");
        }
        if (byArray.length > n3) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", byArray.length, n3));
        }
        this.alloc = byteBufAllocator;
        this.setArray(byArray);
        this.setIndex(n, n2);
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        return (this.array[n] & 0xFF) << 16 | (this.array[n + 1] & 0xFF) << 8 | this.array[n + 2] & 0xFF;
    }

    @Override
    protected long _getLong(int n) {
        return ((long)this.array[n] & 0xFFL) << 56 | ((long)this.array[n + 1] & 0xFFL) << 48 | ((long)this.array[n + 2] & 0xFFL) << 40 | ((long)this.array[n + 3] & 0xFFL) << 32 | ((long)this.array[n + 4] & 0xFFL) << 24 | ((long)this.array[n + 5] & 0xFFL) << 16 | ((long)this.array[n + 6] & 0xFFL) << 8 | (long)this.array[n + 7] & 0xFFL;
    }

    @Override
    public int getUnsignedMedium(int n) {
        this.ensureAccessible();
        return this._getUnsignedMedium(n);
    }

    @Override
    protected void deallocate() {
        this.array = null;
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        this.ensureAccessible();
        try {
            return scatteringByteChannel.read((ByteBuffer)this.internalNioBuffer().clear().position(n).limit(n + n2));
        }
        catch (ClosedChannelException closedChannelException) {
            return -1;
        }
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        return (ByteBuffer)this.internalNioBuffer().clear().position(n).limit(n + n2);
    }

    @Override
    public long getLong(int n) {
        this.ensureAccessible();
        return this._getLong(n);
    }

    private void setArray(byte[] byArray) {
        this.array = byArray;
        this.tmpNioBuf = null;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkSrcIndex(n, n3, n2, byteBuf.capacity());
        if (byteBuf.hasMemoryAddress()) {
            PlatformDependent.copyMemory(byteBuf.memoryAddress() + (long)n2, this.array, n, (long)n3);
        } else if (byteBuf.hasArray()) {
            this.setBytes(n, byteBuf.array(), byteBuf.arrayOffset() + n2, n3);
        } else {
            byteBuf.getBytes(n2, this.array, n, n3);
        }
        return this;
    }

    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] array() {
        this.ensureAccessible();
        return this.array;
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkDstIndex(n, n3, n2, byArray.length);
        System.arraycopy(this.array, n, byArray, n2, n3);
        return this;
    }

    @Override
    public boolean isDirect() {
        return false;
    }

    @Override
    public int getInt(int n) {
        this.ensureAccessible();
        return this._getInt(n);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkDstIndex(n, n3, n2, byteBuf.capacity());
        if (byteBuf.hasMemoryAddress()) {
            PlatformDependent.copyMemory(this.array, n, byteBuf.memoryAddress() + (long)n2, (long)n3);
        } else if (byteBuf.hasArray()) {
            this.getBytes(n, byteBuf.array(), byteBuf.arrayOffset() + n2, n3);
        } else {
            byteBuf.setBytes(n2, this.array, n, n3);
        }
        return this;
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        this.checkReadableBytes(n);
        int n2 = this.getBytes(this.readerIndex, gatheringByteChannel, n, true);
        this.readerIndex += n2;
        return n2;
    }

    @Override
    public short getShort(int n) {
        this.ensureAccessible();
        return this._getShort(n);
    }

    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        this.ensureAccessible();
        this._setLong(n, l);
        return this;
    }

    private int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2, boolean bl) throws IOException {
        this.ensureAccessible();
        ByteBuffer byteBuffer = bl ? this.internalNioBuffer() : ByteBuffer.wrap(this.array);
        return gatheringByteChannel.write((ByteBuffer)byteBuffer.clear().position(n).limit(n + n2));
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return new ByteBuffer[]{this.nioBuffer(n, n2)};
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        this.checkIndex(n, n2);
        byte[] byArray = new byte[n2];
        System.arraycopy(this.array, n, byArray, 0, n2);
        return new UnpooledHeapByteBuf(this.alloc(), byArray, this.maxCapacity());
    }

    @Override
    public int nioBufferCount() {
        return 1;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.ensureAccessible();
        byteBuffer.put(this.array, n, Math.min(this.capacity() - n, byteBuffer.remaining()));
        return this;
    }

    @Override
    public boolean hasArray() {
        return true;
    }

    @Override
    public byte getByte(int n) {
        this.ensureAccessible();
        return this._getByte(n);
    }

    private ByteBuffer internalNioBuffer() {
        ByteBuffer byteBuffer = this.tmpNioBuf;
        if (byteBuffer == null) {
            this.tmpNioBuf = byteBuffer = ByteBuffer.wrap(this.array);
        }
        return byteBuffer;
    }

    @Override
    protected int _getInt(int n) {
        return (this.array[n] & 0xFF) << 24 | (this.array[n + 1] & 0xFF) << 16 | (this.array[n + 2] & 0xFF) << 8 | this.array[n + 3] & 0xFF;
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        this.ensureAccessible();
        return ByteBuffer.wrap(this.array, n, n2).slice();
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkSrcIndex(n, n3, n2, byArray.length);
        System.arraycopy(byArray, n2, this.array, n, n3);
        return this;
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        this.ensureAccessible();
        this._setInt(n, n2);
        return this;
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        this.ensureAccessible();
        return inputStream.read(this.array, n, n2);
    }

    @Override
    protected byte _getByte(int n) {
        return this.array[n];
    }

    protected UnpooledHeapByteBuf(ByteBufAllocator byteBufAllocator, int n, int n2) {
        this(byteBufAllocator, new byte[n], 0, 0, n2);
    }

    @Override
    public ByteBuf capacity(int n) {
        this.ensureAccessible();
        if (n < 0 || n > this.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + n);
        }
        int n2 = this.array.length;
        if (n > n2) {
            byte[] byArray = new byte[n];
            System.arraycopy(this.array, 0, byArray, 0, this.array.length);
            this.setArray(byArray);
        } else if (n < n2) {
            byte[] byArray = new byte[n];
            int n3 = this.readerIndex();
            if (n3 < n) {
                int n4 = this.writerIndex();
                if (n4 > n) {
                    n4 = n;
                    this.writerIndex(n4);
                }
                System.arraycopy(this.array, n3, byArray, n3, n4 - n3);
            } else {
                this.setIndex(n, n);
            }
            this.setArray(byArray);
        }
        return this;
    }

    @Override
    public ByteBuf unwrap() {
        return null;
    }

    @Override
    protected void _setMedium(int n, int n2) {
        this.array[n] = (byte)(n2 >>> 16);
        this.array[n + 1] = (byte)(n2 >>> 8);
        this.array[n + 2] = (byte)n2;
    }

    @Override
    public ByteBuf setByte(int n, int n2) {
        this.ensureAccessible();
        this._setByte(n, n2);
        return this;
    }

    @Override
    protected void _setShort(int n, int n2) {
        this.array[n] = (byte)(n2 >>> 8);
        this.array[n + 1] = (byte)n2;
    }
}

