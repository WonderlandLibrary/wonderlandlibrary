/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.SwappedByteBuf;
import io.netty.buffer.UnsafeDirectSwappedByteBuf;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class UnpooledUnsafeDirectByteBuf
extends AbstractReferenceCountedByteBuf {
    private int capacity;
    private static final boolean NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    private long memoryAddress;
    private final ByteBufAllocator alloc;
    private boolean doNotFree;
    private ByteBuffer buffer;
    private ByteBuffer tmpNioBuf;

    protected UnpooledUnsafeDirectByteBuf(ByteBufAllocator byteBufAllocator, int n, int n2) {
        super(n2);
        if (byteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        if (n < 0) {
            throw new IllegalArgumentException("initialCapacity: " + n);
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("maxCapacity: " + n2);
        }
        if (n > n2) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", n, n2));
        }
        this.alloc = byteBufAllocator;
        this.setByteBuffer(this.allocateDirect(n));
    }

    @Override
    public ByteBuf unwrap() {
        return null;
    }

    @Override
    public boolean hasMemoryAddress() {
        return true;
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        return ((ByteBuffer)this.buffer.duplicate().position(n).limit(n + n2)).slice();
    }

    @Override
    protected byte _getByte(int n) {
        return PlatformDependent.getByte(this.addr(n));
    }

    private void setByteBuffer(ByteBuffer byteBuffer) {
        ByteBuffer byteBuffer2 = this.buffer;
        if (byteBuffer2 != null) {
            if (this.doNotFree) {
                this.doNotFree = false;
            } else {
                this.freeDirect(byteBuffer2);
            }
        }
        this.buffer = byteBuffer;
        this.memoryAddress = PlatformDependent.directBufferAddress(byteBuffer);
        this.tmpNioBuf = null;
        this.capacity = byteBuffer.remaining();
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return new ByteBuffer[]{this.nioBuffer(n, n2)};
    }

    @Override
    protected void deallocate() {
        ByteBuffer byteBuffer = this.buffer;
        if (byteBuffer == null) {
            return;
        }
        this.buffer = null;
        if (!this.doNotFree) {
            this.freeDirect(byteBuffer);
        }
    }

    @Override
    public int nioBufferCount() {
        return 1;
    }

    @Override
    protected void _setMedium(int n, int n2) {
        long l = this.addr(n);
        PlatformDependent.putByte(l, (byte)(n2 >>> 16));
        PlatformDependent.putByte(l + 1L, (byte)(n2 >>> 8));
        PlatformDependent.putByte(l + 2L, (byte)n2);
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        long l = this.addr(n);
        return (PlatformDependent.getByte(l) & 0xFF) << 16 | (PlatformDependent.getByte(l + 1L) & 0xFF) << 8 | PlatformDependent.getByte(l + 2L) & 0xFF;
    }

    @Override
    public boolean hasArray() {
        return false;
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.getBytes(n, byteBuffer, false);
        return this;
    }

    private ByteBuffer internalNioBuffer() {
        ByteBuffer byteBuffer = this.tmpNioBuf;
        if (byteBuffer == null) {
            this.tmpNioBuf = byteBuffer = this.buffer.duplicate();
        }
        return byteBuffer;
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
    protected long _getLong(int n) {
        long l = PlatformDependent.getLong(this.addr(n));
        return NATIVE_ORDER ? l : Long.reverseBytes(l);
    }

    private void getBytes(int n, ByteBuffer byteBuffer, boolean bl) {
        this.checkIndex(n);
        if (byteBuffer == null) {
            throw new NullPointerException("dst");
        }
        int n2 = Math.min(this.capacity() - n, byteBuffer.remaining());
        ByteBuffer byteBuffer2 = bl ? this.internalNioBuffer() : this.buffer.duplicate();
        byteBuffer2.clear().position(n).limit(n + n2);
        byteBuffer.put(byteBuffer2);
    }

    long addr(int n) {
        return this.memoryAddress + (long)n;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        this.ensureAccessible();
        ByteBuffer byteBuffer2 = this.internalNioBuffer();
        if (byteBuffer == byteBuffer2) {
            byteBuffer = byteBuffer.duplicate();
        }
        byteBuffer2.clear().position(n).limit(n + byteBuffer.remaining());
        byteBuffer2.put(byteBuffer);
        return this;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.alloc;
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        return (ByteBuffer)this.internalNioBuffer().clear().position(n).limit(n + n2);
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
    protected void _setLong(int n, long l) {
        PlatformDependent.putLong(this.addr(n), NATIVE_ORDER ? l : Long.reverseBytes(l));
    }

    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException("direct buffer");
    }

    @Override
    protected void _setInt(int n, int n2) {
        PlatformDependent.putInt(this.addr(n), NATIVE_ORDER ? n2 : Integer.reverseBytes(n2));
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        this.checkReadableBytes(n);
        int n2 = this.getBytes(this.readerIndex, gatheringByteChannel, n, true);
        this.readerIndex += n2;
        return n2;
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.ensureAccessible();
        if (n2 != 0) {
            byte[] byArray = new byte[n2];
            PlatformDependent.copyMemory(this.addr(n), byArray, 0, (long)n2);
            outputStream.write(byArray);
        }
        return this;
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

    protected void freeDirect(ByteBuffer byteBuffer) {
        PlatformDependent.freeDirectBuffer(byteBuffer);
    }

    @Override
    public boolean isDirect() {
        return true;
    }

    @Override
    public long memoryAddress() {
        return this.memoryAddress;
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

    private int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2, boolean bl) throws IOException {
        this.ensureAccessible();
        if (n2 == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = bl ? this.internalNioBuffer() : this.buffer.duplicate();
        byteBuffer.clear().position(n).limit(n + n2);
        return gatheringByteChannel.write(byteBuffer);
    }

    @Override
    protected void _setByte(int n, int n2) {
        PlatformDependent.putByte(this.addr(n), (byte)n2);
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

    protected UnpooledUnsafeDirectByteBuf(ByteBufAllocator byteBufAllocator, ByteBuffer byteBuffer, int n) {
        super(n);
        if (byteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        if (byteBuffer == null) {
            throw new NullPointerException("initialBuffer");
        }
        if (!byteBuffer.isDirect()) {
            throw new IllegalArgumentException("initialBuffer is not a direct buffer.");
        }
        if (byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("initialBuffer is a read-only buffer.");
        }
        int n2 = byteBuffer.remaining();
        if (n2 > n) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", n2, n));
        }
        this.alloc = byteBufAllocator;
        this.doNotFree = true;
        this.setByteBuffer(byteBuffer.slice().order(ByteOrder.BIG_ENDIAN));
        this.writerIndex(n2);
    }

    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("direct buffer");
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        this.ensureAccessible();
        ByteBuffer byteBuffer = this.internalNioBuffer();
        byteBuffer.clear().position(n).limit(n + n2);
        try {
            return scatteringByteChannel.read(byteBuffer);
        }
        catch (ClosedChannelException closedChannelException) {
            return -1;
        }
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        return this.getBytes(n, gatheringByteChannel, n2, false);
    }

    @Override
    protected short _getShort(int n) {
        short s = PlatformDependent.getShort(this.addr(n));
        return NATIVE_ORDER ? s : Short.reverseBytes(s);
    }

    @Override
    protected SwappedByteBuf newSwappedByteBuf() {
        return new UnsafeDirectSwappedByteBuf(this);
    }

    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    @Override
    protected int _getInt(int n) {
        int n2 = PlatformDependent.getInt(this.addr(n));
        return NATIVE_ORDER ? n2 : Integer.reverseBytes(n2);
    }

    @Override
    protected void _setShort(int n, int n2) {
        PlatformDependent.putShort(this.addr(n), NATIVE_ORDER ? (short)n2 : Short.reverseBytes((short)n2));
    }

    protected ByteBuffer allocateDirect(int n) {
        return ByteBuffer.allocateDirect(n);
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
    public ByteBuf capacity(int n) {
        this.ensureAccessible();
        if (n < 0 || n > this.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + n);
        }
        int n2 = this.readerIndex();
        int n3 = this.writerIndex();
        int n4 = this.capacity;
        if (n > n4) {
            ByteBuffer byteBuffer = this.buffer;
            ByteBuffer byteBuffer2 = this.allocateDirect(n);
            byteBuffer.position(0).limit(byteBuffer.capacity());
            byteBuffer2.position(0).limit(byteBuffer.capacity());
            byteBuffer2.put(byteBuffer);
            byteBuffer2.clear();
            this.setByteBuffer(byteBuffer2);
        } else if (n < n4) {
            ByteBuffer byteBuffer = this.buffer;
            ByteBuffer byteBuffer3 = this.allocateDirect(n);
            if (n2 < n) {
                if (n3 > n) {
                    n3 = n;
                    this.writerIndex(n3);
                }
                byteBuffer.position(n2).limit(n3);
                byteBuffer3.position(n2).limit(n3);
                byteBuffer3.put(byteBuffer);
                byteBuffer3.clear();
            } else {
                this.setIndex(n, n);
            }
            this.setByteBuffer(byteBuffer3);
        }
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
}

