/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.SwappedByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.Signal;
import io.netty.util.internal.StringUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

final class ReplayingDecoderBuffer
extends ByteBuf {
    private SwappedByteBuf swapped;
    private static final Signal REPLAY = ReplayingDecoder.REPLAY;
    private ByteBuf buffer;
    static final ReplayingDecoderBuffer EMPTY_BUFFER = new ReplayingDecoderBuffer(Unpooled.EMPTY_BUFFER);
    private boolean terminated;

    @Override
    public short readShort() {
        this.checkReadableBytes(2);
        return this.buffer.readShort();
    }

    @Override
    public int indexOf(int n, int n2, byte by) {
        if (n == n2) {
            return -1;
        }
        if (Math.max(n, n2) > this.buffer.writerIndex()) {
            throw REPLAY;
        }
        return this.buffer.indexOf(n, n2, by);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int n) {
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public char getChar(int n) {
        this.checkIndex(n, 2);
        return this.buffer.getChar(n);
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkIndex(n, n3);
        this.buffer.getBytes(n, byArray, n2, n3);
        return this;
    }

    @Override
    public ByteBuf retain(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf duplicate() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public boolean release() {
        ReplayingDecoderBuffer.reject();
        return false;
    }

    @Override
    public ByteBuf markReaderIndex() {
        this.buffer.markReaderIndex();
        return this;
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        this.checkIndex(n, n2);
        return this.buffer.copy(n, n2);
    }

    @Override
    public int writableBytes() {
        return 0;
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        return this.buffer.internalNioBuffer(n, n2);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf clear() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public byte[] array() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteOrder order() {
        return this.buffer.order();
    }

    @Override
    public short getUnsignedByte(int n) {
        this.checkIndex(n, 1);
        return this.buffer.getUnsignedByte(n);
    }

    @Override
    public ByteBuf writeInt(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public char readChar() {
        this.checkReadableBytes(2);
        return this.buffer.readChar();
    }

    @Override
    public ByteBuf retain() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) {
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public int writerIndex() {
        return this.buffer.writerIndex();
    }

    @Override
    public int readMedium() {
        this.checkReadableBytes(3);
        return this.buffer.readMedium();
    }

    @Override
    public int refCnt() {
        return this.buffer.refCnt();
    }

    @Override
    public ByteBuf setBoolean(int n, boolean bl) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    @Override
    public boolean readBoolean() {
        this.checkReadableBytes(1);
        return this.buffer.readBoolean();
    }

    @Override
    public ByteBuf unwrap() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf setMedium(int n, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf setIndex(int n, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int capacity() {
        if (this.terminated) {
            return this.buffer.capacity();
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public ByteBuf setDouble(int n, double d) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf setZero(int n, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf readerIndex(int n) {
        this.buffer.readerIndex(n);
        return this;
    }

    @Override
    public ByteBuf resetReaderIndex() {
        this.buffer.resetReaderIndex();
        return this;
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray, int n, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf writeDouble(double d) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) {
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public ByteBuf writeFloat(float f) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkIndex(n, n3);
        this.buffer.getBytes(n, byteBuf, n2, n3);
        return this;
    }

    @Override
    public ByteBuf copy() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int forEachByteDesc(int n, int n2, ByteBufProcessor byteBufProcessor) {
        if (n + n2 > this.buffer.writerIndex()) {
            throw REPLAY;
        }
        return this.buffer.forEachByteDesc(n, n2, byteBufProcessor);
    }

    ReplayingDecoderBuffer(ByteBuf byteBuf) {
        this.setCumulation(byteBuf);
    }

    @Override
    public int readUnsignedMedium() {
        this.checkReadableBytes(3);
        return this.buffer.readUnsignedMedium();
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf writeZero(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        this.checkIndex(n, n2);
        return this.buffer.nioBuffers(n, n2);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf capacity(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }

    @Override
    public ByteBuf markWriterIndex() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        this.checkReadableBytes(byteBuf.writableBytes());
        this.buffer.readBytes(byteBuf);
        return this;
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) {
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public byte getByte(int n) {
        this.checkIndex(n, 1);
        return this.buffer.getByte(n);
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
    }

    @Override
    public String toString(Charset charset) {
        ReplayingDecoderBuffer.reject();
        return null;
    }

    @Override
    public int getUnsignedShort(int n) {
        this.checkIndex(n, 2);
        return this.buffer.getUnsignedShort(n);
    }

    @Override
    public int bytesBefore(int n, byte by) {
        int n2 = this.buffer.readerIndex();
        return this.bytesBefore(n2, this.buffer.writerIndex() - n2, by);
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        if (byteOrder == null) {
            throw new NullPointerException("endianness");
        }
        if (byteOrder == this.order()) {
            return this;
        }
        SwappedByteBuf swappedByteBuf = this.swapped;
        if (swappedByteBuf == null) {
            this.swapped = swappedByteBuf = new SwappedByteBuf(this);
        }
        return swappedByteBuf;
    }

    @Override
    public ByteBuf resetWriterIndex() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    static {
        EMPTY_BUFFER.terminate();
    }

    @Override
    public int forEachByte(ByteBufProcessor byteBufProcessor) {
        int n = this.buffer.forEachByte(byteBufProcessor);
        if (n < 0) {
            throw REPLAY;
        }
        return n;
    }

    @Override
    public ByteBuf skipBytes(int n) {
        this.checkReadableBytes(n);
        this.buffer.skipBytes(n);
        return this;
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int getUnsignedMedium(int n) {
        this.checkIndex(n, 3);
        return this.buffer.getUnsignedMedium(n);
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray) {
        this.checkIndex(n, byArray.length);
        this.buffer.getBytes(n, byArray);
        return this;
    }

    private void checkReadableBytes(int n) {
        if (this.buffer.readableBytes() < n) {
            throw REPLAY;
        }
    }

    @Override
    public int readUnsignedShort() {
        this.checkReadableBytes(2);
        return this.buffer.readUnsignedShort();
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        return this.buffer.nioBuffer(n, n2);
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor byteBufProcessor) {
        if (this.terminated) {
            return this.buffer.forEachByteDesc(byteBufProcessor);
        }
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public short getShort(int n) {
        this.checkIndex(n, 2);
        return this.buffer.getShort(n);
    }

    @Override
    public int hashCode() {
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public byte readByte() {
        this.checkReadableBytes(1);
        return this.buffer.readByte();
    }

    @Override
    public long getLong(int n) {
        this.checkIndex(n, 8);
        return this.buffer.getLong(n);
    }

    @Override
    public ByteBuf setByte(int n, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) {
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public String toString(int n, int n2, Charset charset) {
        this.checkIndex(n, n2);
        return this.buffer.toString(n, n2, charset);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        ReplayingDecoderBuffer.reject();
        return null;
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        this.checkIndex(n, n2);
        return this.buffer.slice(n, n2);
    }

    @Override
    public int forEachByte(int n, int n2, ByteBufProcessor byteBufProcessor) {
        int n3 = this.buffer.writerIndex();
        if (n >= n3) {
            throw REPLAY;
        }
        if (n <= n3 - n2) {
            return this.buffer.forEachByte(n, n2, byteBufProcessor);
        }
        int n4 = this.buffer.forEachByte(n, n3 - n, byteBufProcessor);
        if (n4 < 0) {
            throw REPLAY;
        }
        return n4;
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public float readFloat() {
        this.checkReadableBytes(4);
        return this.buffer.readFloat();
    }

    @Override
    public int bytesBefore(byte by) {
        int n = this.buffer.bytesBefore(by);
        if (n < 0) {
            throw REPLAY;
        }
        return n;
    }

    @Override
    public ByteBuf writeLong(long l) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public boolean hasArray() {
        return false;
    }

    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getUnsignedInt(int n) {
        this.checkIndex(n, 4);
        return this.buffer.getUnsignedInt(n);
    }

    @Override
    public ByteBuf ensureWritable(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public boolean isReadable(int n) {
        return this.terminated ? this.buffer.isReadable(n) : true;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    void setCumulation(ByteBuf byteBuf) {
        this.buffer = byteBuf;
    }

    @Override
    public float getFloat(int n) {
        this.checkIndex(n, 4);
        return this.buffer.getFloat(n);
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int readInt() {
        this.checkReadableBytes(4);
        return this.buffer.readInt();
    }

    @Override
    public boolean getBoolean(int n) {
        this.checkIndex(n, 1);
        return this.buffer.getBoolean(n);
    }

    @Override
    public int readableBytes() {
        if (this.terminated) {
            return this.buffer.readableBytes();
        }
        return Integer.MAX_VALUE - this.buffer.readerIndex();
    }

    @Override
    public ByteBuf writeBoolean(boolean bl) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int maxCapacity() {
        return this.capacity();
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    private static void reject() {
        throw new UnsupportedOperationException("not a replayable operation");
    }

    @Override
    public short readUnsignedByte() {
        this.checkReadableBytes(1);
        return this.buffer.readUnsignedByte();
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    ReplayingDecoderBuffer() {
    }

    @Override
    public ByteBuf readBytes(byte[] byArray, int n, int n2) {
        this.checkReadableBytes(n2);
        this.buffer.readBytes(byArray, n, n2);
        return this;
    }

    @Override
    public int ensureWritable(int n, boolean bl) {
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public ByteBuf writeChar(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int readerIndex() {
        return this.buffer.readerIndex();
    }

    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteBuf discardReadBytes() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int writeBytes(InputStream inputStream, int n) {
        ReplayingDecoderBuffer.reject();
        return 0;
    }

    @Override
    public ByteBuf writerIndex(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    private void checkIndex(int n, int n2) {
        if (n + n2 > this.buffer.writerIndex()) {
            throw REPLAY;
        }
    }

    @Override
    public ByteBuf writeByte(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public ByteBuf slice() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf setFloat(int n, float f) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuffer nioBuffer() {
        ReplayingDecoderBuffer.reject();
        return null;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf setChar(int n, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf writeShort(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public double getDouble(int n) {
        this.checkIndex(n, 8);
        return this.buffer.getDouble(n);
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public boolean isReadable() {
        return this.terminated ? this.buffer.isReadable() : true;
    }

    @Override
    public int maxWritableBytes() {
        return 0;
    }

    @Override
    public ByteBuf readSlice(int n) {
        this.checkReadableBytes(n);
        return this.buffer.readSlice(n);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    void terminate() {
        this.terminated = true;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }

    @Override
    public boolean release(int n) {
        ReplayingDecoderBuffer.reject();
        return false;
    }

    @Override
    public ByteBuf readBytes(byte[] byArray) {
        this.checkReadableBytes(byArray.length);
        this.buffer.readBytes(byArray);
        return this;
    }

    @Override
    public int getMedium(int n) {
        this.checkIndex(n, 3);
        return this.buffer.getMedium(n);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public ByteBuf writeMedium(int n) {
        ReplayingDecoderBuffer.reject();
        return this;
    }

    @Override
    public int bytesBefore(int n, int n2, byte by) {
        int n3 = this.buffer.writerIndex();
        if (n >= n3) {
            throw REPLAY;
        }
        if (n <= n3 - n2) {
            return this.buffer.bytesBefore(n, n2, by);
        }
        int n4 = this.buffer.bytesBefore(n, n3 - n, by);
        if (n4 < 0) {
            throw REPLAY;
        }
        return n4;
    }

    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + "ridx=" + this.readerIndex() + ", " + "widx=" + this.writerIndex() + ')';
    }

    @Override
    public boolean hasMemoryAddress() {
        return false;
    }

    @Override
    public ByteBuf readBytes(int n) {
        this.checkReadableBytes(n);
        return this.buffer.readBytes(n);
    }

    @Override
    public boolean isWritable(int n) {
        return false;
    }

    @Override
    public double readDouble() {
        this.checkReadableBytes(8);
        return this.buffer.readDouble();
    }

    @Override
    public int getInt(int n) {
        this.checkIndex(n, 4);
        return this.buffer.getInt(n);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n, int n2) {
        this.checkReadableBytes(n2);
        this.buffer.readBytes(byteBuf, n, n2);
        return this;
    }

    @Override
    public long readUnsignedInt() {
        this.checkReadableBytes(4);
        return this.buffer.readUnsignedInt();
    }

    @Override
    public long readLong() {
        this.checkReadableBytes(8);
        return this.buffer.readLong();
    }
}

