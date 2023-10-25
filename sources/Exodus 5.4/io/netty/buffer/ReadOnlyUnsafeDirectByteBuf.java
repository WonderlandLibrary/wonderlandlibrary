/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ReadOnlyByteBufferBuf;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class ReadOnlyUnsafeDirectByteBuf
extends ReadOnlyByteBufferBuf {
    private static final boolean NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    private final long memoryAddress;

    ReadOnlyUnsafeDirectByteBuf(ByteBufAllocator byteBufAllocator, ByteBuffer byteBuffer) {
        super(byteBufAllocator, byteBuffer);
        this.memoryAddress = PlatformDependent.directBufferAddress(byteBuffer);
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

    private long addr(int n) {
        return this.memoryAddress + (long)n;
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkIndex(n, n3);
        if (byArray == null) {
            throw new NullPointerException("dst");
        }
        if (n2 < 0 || n2 > byArray.length - n3) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", n2, n3, byArray.length));
        }
        if (n3 != 0) {
            PlatformDependent.copyMemory(this.addr(n), byArray, n2, (long)n3);
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
    protected long _getLong(int n) {
        long l = PlatformDependent.getLong(this.addr(n));
        return NATIVE_ORDER ? l : Long.reverseBytes(l);
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        long l = this.addr(n);
        return (PlatformDependent.getByte(l) & 0xFF) << 16 | (PlatformDependent.getByte(l + 1L) & 0xFF) << 8 | PlatformDependent.getByte(l + 2L) & 0xFF;
    }

    @Override
    protected int _getInt(int n) {
        int n2 = PlatformDependent.getInt(this.addr(n));
        return NATIVE_ORDER ? n2 : Integer.reverseBytes(n2);
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
        if (byteBuf.hasMemoryAddress()) {
            PlatformDependent.copyMemory(this.addr(n), byteBuf.memoryAddress() + (long)n2, n3);
        } else if (byteBuf.hasArray()) {
            PlatformDependent.copyMemory(this.addr(n), byteBuf.array(), byteBuf.arrayOffset() + n2, (long)n3);
        } else {
            byteBuf.setBytes(n2, this, n, n3);
        }
        return this;
    }

    @Override
    protected short _getShort(int n) {
        short s = PlatformDependent.getShort(this.addr(n));
        return NATIVE_ORDER ? s : Short.reverseBytes(s);
    }

    @Override
    protected byte _getByte(int n) {
        return PlatformDependent.getByte(this.addr(n));
    }
}

