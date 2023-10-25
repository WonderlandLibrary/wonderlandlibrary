/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.ByteBufUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

public class SwappedByteBuf
extends ByteBuf {
    private final ByteBuf buf;
    private final ByteOrder order;

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n) {
        this.buf.readBytes(byteBuf, n);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        this.buf.readBytes(byteBuf);
        return this;
    }

    @Override
    public ByteBuf setByte(int n, int n2) {
        this.buf.setByte(n, n2);
        return this;
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray) {
        this.buf.writeBytes(byArray);
        return this;
    }

    @Override
    public long readLong() {
        return ByteBufUtil.swapLong(this.buf.readLong());
    }

    @Override
    public int bytesBefore(byte by) {
        return this.buf.bytesBefore(by);
    }

    @Override
    public long getUnsignedInt(int n) {
        return (long)this.getInt(n) & 0xFFFFFFFFL;
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        this.buf.writeBytes(byteBuffer);
        return this;
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        this.buf.discardSomeReadBytes();
        return this;
    }

    @Override
    public boolean isReadable() {
        return this.buf.isReadable();
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        this.buf.writeBytes(byteBuf);
        return this;
    }

    @Override
    public boolean release() {
        return this.buf.release();
    }

    @Override
    public int refCnt() {
        return this.buf.refCnt();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ByteBuf) {
            return ByteBufUtil.equals(this, (ByteBuf)object);
        }
        return false;
    }

    @Override
    public int ensureWritable(int n, boolean bl) {
        return this.buf.ensureWritable(n, bl);
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        return this.buf.slice(n, n2).order(this.order);
    }

    @Override
    public int readerIndex() {
        return this.buf.readerIndex();
    }

    @Override
    public ByteBuf readBytes(byte[] byArray, int n, int n2) {
        this.buf.readBytes(byArray, n, n2);
        return this;
    }

    @Override
    public boolean getBoolean(int n) {
        return this.buf.getBoolean(n);
    }

    @Override
    public float getFloat(int n) {
        return Float.intBitsToFloat(this.getInt(n));
    }

    @Override
    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n, int n2) {
        this.buf.writeBytes(byteBuf, n, n2);
        return this;
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        ByteBuffer[] byteBufferArray = this.buf.nioBuffers(n, n2);
        for (int i = 0; i < byteBufferArray.length; ++i) {
            byteBufferArray[i] = byteBufferArray[i].order(this.order);
        }
        return byteBufferArray;
    }

    @Override
    public ByteBuf readBytes(byte[] byArray) {
        this.buf.readBytes(byArray);
        return this;
    }

    @Override
    public int readMedium() {
        return ByteBufUtil.swapMedium(this.buf.readMedium());
    }

    @Override
    public char getChar(int n) {
        return (char)this.getShort(n);
    }

    @Override
    public String toString(Charset charset) {
        return this.buf.toString(charset);
    }

    @Override
    public int writeBytes(InputStream inputStream, int n) throws IOException {
        return this.buf.writeBytes(inputStream, n);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2) {
        this.buf.setBytes(n, byteBuf, n2);
        return this;
    }

    @Override
    public double getDouble(int n) {
        return Double.longBitsToDouble(this.getLong(n));
    }

    @Override
    public ByteBuf skipBytes(int n) {
        this.buf.skipBytes(n);
        return this;
    }

    @Override
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    @Override
    public ByteBuf writeFloat(float f) {
        this.writeInt(Float.floatToRawIntBits(f));
        return this;
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int n) throws IOException {
        this.buf.readBytes(outputStream, n);
        return this;
    }

    @Override
    public short getShort(int n) {
        return ByteBufUtil.swapShort(this.buf.getShort(n));
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        return this.buf.setBytes(n, scatteringByteChannel, n2);
    }

    @Override
    public String toString(int n, int n2, Charset charset) {
        return this.buf.toString(n, n2, charset);
    }

    @Override
    public ByteBuf writeShort(int n) {
        this.buf.writeShort(ByteBufUtil.swapShort((short)n));
        return this;
    }

    @Override
    public int readInt() {
        return ByteBufUtil.swapInt(this.buf.readInt());
    }

    @Override
    public ByteBuf setBoolean(int n, boolean bl) {
        this.buf.setBoolean(n, bl);
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray) {
        this.buf.getBytes(n, byArray);
        return this;
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int n) throws IOException {
        return this.buf.writeBytes(scatteringByteChannel, n);
    }

    @Override
    public ByteBuf retain(int n) {
        this.buf.retain(n);
        return this;
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        this.buf.setShort(n, ByteBufUtil.swapShort((short)n2));
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray) {
        this.buf.setBytes(n, byArray);
        return this;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray, int n, int n2) {
        this.buf.writeBytes(byArray, n, n2);
        return this;
    }

    @Override
    public int bytesBefore(int n, byte by) {
        return this.buf.bytesBefore(n, by);
    }

    @Override
    public ByteBuf setFloat(int n, float f) {
        this.setInt(n, Float.floatToRawIntBits(f));
        return this;
    }

    @Override
    public int writableBytes() {
        return this.buf.writableBytes();
    }

    @Override
    public boolean release(int n) {
        return this.buf.release(n);
    }

    @Override
    public int readableBytes() {
        return this.buf.readableBytes();
    }

    @Override
    public byte readByte() {
        return this.buf.readByte();
    }

    @Override
    public byte[] array() {
        return this.buf.array();
    }

    @Override
    public ByteOrder order() {
        return this.order;
    }

    @Override
    public int readUnsignedMedium() {
        return this.readMedium() & 0xFFFFFF;
    }

    @Override
    public ByteBuf resetWriterIndex() {
        this.buf.resetWriterIndex();
        return this;
    }

    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByteDesc(byteBufProcessor);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        return this.buf.readBytes(gatheringByteChannel, n);
    }

    @Override
    public int getInt(int n) {
        return ByteBufUtil.swapInt(this.buf.getInt(n));
    }

    @Override
    public int forEachByte(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByte(n, n2, byteBufProcessor);
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return ByteBufUtil.compare(this, byteBuf);
    }

    @Override
    public short getUnsignedByte(int n) {
        return this.buf.getUnsignedByte(n);
    }

    @Override
    public ByteBuf ensureWritable(int n) {
        this.buf.ensureWritable(n);
        return this;
    }

    @Override
    public ByteBuf setDouble(int n, double d) {
        this.setLong(n, Double.doubleToRawLongBits(d));
        return this;
    }

    @Override
    public ByteBuf writerIndex(int n) {
        this.buf.writerIndex(n);
        return this;
    }

    @Override
    public char readChar() {
        return (char)this.readShort();
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        ByteBuffer[] byteBufferArray = this.buf.nioBuffers();
        for (int i = 0; i < byteBufferArray.length; ++i) {
            byteBufferArray[i] = byteBufferArray[i].order(this.order);
        }
        return byteBufferArray;
    }

    @Override
    public int writerIndex() {
        return this.buf.writerIndex();
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2) {
        this.buf.getBytes(n, byteBuf, n2);
        return this;
    }

    @Override
    public ByteBuf readerIndex(int n) {
        this.buf.readerIndex(n);
        return this;
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        this.buf.setInt(n, ByteBufUtil.swapInt(n2));
        return this;
    }

    @Override
    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }

    @Override
    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    @Override
    public boolean isDirect() {
        return this.buf.isDirect();
    }

    @Override
    public int indexOf(int n, int n2, byte by) {
        return this.buf.indexOf(n, n2, by);
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        return this.buf.getBytes(n, gatheringByteChannel, n2);
    }

    @Override
    public ByteBuf discardReadBytes() {
        this.buf.discardReadBytes();
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.buf.getBytes(n, outputStream, n2);
        return this;
    }

    @Override
    public ByteBuf clear() {
        this.buf.clear();
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf) {
        this.buf.getBytes(n, byteBuf);
        return this;
    }

    @Override
    public ByteBuf writeBoolean(boolean bl) {
        this.buf.writeBoolean(bl);
        return this;
    }

    @Override
    public double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }

    @Override
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    @Override
    public boolean isReadable(int n) {
        return this.buf.isReadable(n);
    }

    @Override
    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    @Override
    public ByteBuf writeLong(long l) {
        this.buf.writeLong(ByteBufUtil.swapLong(l));
        return this;
    }

    @Override
    public ByteBuf setChar(int n, int n2) {
        this.setShort(n, n2);
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.buf.setBytes(n, byArray, n2, n3);
        return this;
    }

    @Override
    public short readShort() {
        return ByteBufUtil.swapShort(this.buf.readShort());
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.buf.getBytes(n, byArray, n2, n3);
        return this;
    }

    @Override
    public ByteBuf readBytes(int n) {
        return this.buf.readBytes(n).order(this.order());
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.buf.setBytes(n, byteBuf, n2, n3);
        return this;
    }

    @Override
    public int bytesBefore(int n, int n2, byte by) {
        return this.buf.bytesBefore(n, n2, by);
    }

    @Override
    public ByteBuf readSlice(int n) {
        return this.buf.readSlice(n).order(this.order);
    }

    @Override
    public long getLong(int n) {
        return ByteBufUtil.swapLong(this.buf.getLong(n));
    }

    @Override
    public int hashCode() {
        return this.buf.hashCode();
    }

    @Override
    public ByteBuf slice() {
        return this.buf.slice().order(this.order);
    }

    @Override
    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        if (byteOrder == null) {
            throw new NullPointerException("endianness");
        }
        if (byteOrder == this.order) {
            return this;
        }
        return this.buf;
    }

    @Override
    public ByteBuf writeMedium(int n) {
        this.buf.writeMedium(ByteBufUtil.swapMedium(n));
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf) {
        this.buf.setBytes(n, byteBuf);
        return this;
    }

    @Override
    public int getMedium(int n) {
        return ByteBufUtil.swapMedium(this.buf.getMedium(n));
    }

    @Override
    public int capacity() {
        return this.buf.capacity();
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.buf.getBytes(n, byteBuffer);
        return this;
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    @Override
    public boolean isWritable() {
        return this.buf.isWritable();
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        return this.buf.nioBuffer(n, n2).order(this.order);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n, int n2) {
        this.buf.readBytes(byteBuf, n, n2);
        return this;
    }

    @Override
    public ByteBuf setZero(int n, int n2) {
        this.buf.setZero(n, n2);
        return this;
    }

    @Override
    public ByteBuf writeByte(int n) {
        this.buf.writeByte(n);
        return this;
    }

    @Override
    public int forEachByte(ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByte(byteBufProcessor);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        this.buf.setBytes(n, byteBuffer);
        return this;
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        return this.buf.setBytes(n, inputStream, n2);
    }

    @Override
    public int getUnsignedMedium(int n) {
        return this.getMedium(n) & 0xFFFFFF;
    }

    @Override
    public ByteBuf retain() {
        this.buf.retain();
        return this;
    }

    @Override
    public ByteBuf writeInt(int n) {
        this.buf.writeInt(ByteBufUtil.swapInt(n));
        return this;
    }

    @Override
    public boolean hasArray() {
        return this.buf.hasArray();
    }

    @Override
    public ByteBuf setMedium(int n, int n2) {
        this.buf.setMedium(n, ByteBufUtil.swapMedium(n2));
        return this;
    }

    @Override
    public byte getByte(int n) {
        return this.buf.getByte(n);
    }

    @Override
    public ByteBuf duplicate() {
        return this.buf.duplicate().order(this.order);
    }

    @Override
    public ByteBuf capacity(int n) {
        this.buf.capacity(n);
        return this;
    }

    @Override
    public ByteBuf writeDouble(double d) {
        this.writeLong(Double.doubleToRawLongBits(d));
        return this;
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer().order(this.order);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.buf.getBytes(n, byteBuf, n2, n3);
        return this;
    }

    @Override
    public ByteBuf writeChar(int n) {
        this.writeShort(n);
        return this;
    }

    @Override
    public ByteBuf markWriterIndex() {
        this.buf.markWriterIndex();
        return this;
    }

    public SwappedByteBuf(ByteBuf byteBuf) {
        if (byteBuf == null) {
            throw new NullPointerException("buf");
        }
        this.buf = byteBuf;
        this.order = byteBuf.order() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }

    @Override
    public long readUnsignedInt() {
        return (long)this.readInt() & 0xFFFFFFFFL;
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        return this.buf.copy(n, n2).order(this.order);
    }

    @Override
    public int forEachByteDesc(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByteDesc(n, n2, byteBufProcessor);
    }

    @Override
    public String toString() {
        return "Swapped(" + this.buf.toString() + ')';
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        this.buf.setLong(n, ByteBufUtil.swapLong(l));
        return this;
    }

    @Override
    public int readUnsignedShort() {
        return this.readShort() & 0xFFFF;
    }

    @Override
    public boolean isWritable(int n) {
        return this.buf.isWritable(n);
    }

    @Override
    public ByteBuf copy() {
        return this.buf.copy().order(this.order);
    }

    @Override
    public int getUnsignedShort(int n) {
        return this.getShort(n) & 0xFFFF;
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        this.buf.readBytes(byteBuffer);
        return this;
    }

    @Override
    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        this.buf.resetReaderIndex();
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n) {
        this.buf.writeBytes(byteBuf, n);
        return this;
    }

    @Override
    public ByteBuf writeZero(int n) {
        this.buf.writeZero(n);
        return this;
    }

    @Override
    public ByteBuf markReaderIndex() {
        this.buf.markReaderIndex();
        return this;
    }

    @Override
    public ByteBuf setIndex(int n, int n2) {
        this.buf.setIndex(n, n2);
        return this;
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        return this.nioBuffer(n, n2);
    }
}

