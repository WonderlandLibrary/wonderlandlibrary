/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

class WrappedByteBuf
extends ByteBuf {
    protected final ByteBuf buf;

    @Override
    public ByteBuf setIndex(int n, int n2) {
        this.buf.setIndex(n, n2);
        return this;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        this.buf.setInt(n, n2);
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.buf.setBytes(n, byteBuf, n2, n3);
        return this;
    }

    @Override
    public float getFloat(int n) {
        return this.buf.getFloat(n);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n) {
        this.buf.writeBytes(byteBuf, n);
        return this;
    }

    protected WrappedByteBuf(ByteBuf byteBuf) {
        if (byteBuf == null) {
            throw new NullPointerException("buf");
        }
        this.buf = byteBuf;
    }

    @Override
    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    @Override
    public int readableBytes() {
        return this.buf.readableBytes();
    }

    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        this.buf.writeBytes(byteBuf);
        return this;
    }

    @Override
    public double getDouble(int n) {
        return this.buf.getDouble(n);
    }

    @Override
    public ByteBuf writeMedium(int n) {
        this.buf.writeMedium(n);
        return this;
    }

    @Override
    public ByteBuf writeByte(int n) {
        this.buf.writeByte(n);
        return this;
    }

    @Override
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    @Override
    public ByteOrder order() {
        return this.buf.order();
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        this.buf.writeBytes(byteBuffer);
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2) {
        this.buf.getBytes(n, byteBuf, n2);
        return this;
    }

    @Override
    public int getUnsignedShort(int n) {
        return this.buf.getUnsignedShort(n);
    }

    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }

    @Override
    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }

    @Override
    public ByteBuf markReaderIndex() {
        this.buf.markReaderIndex();
        return this;
    }

    @Override
    public ByteBuf writeShort(int n) {
        this.buf.writeShort(n);
        return this;
    }

    @Override
    public boolean equals(Object object) {
        return this.buf.equals(object);
    }

    @Override
    public ByteBuf setFloat(int n, float f) {
        this.buf.setFloat(n, f);
        return this;
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        return this.buf.copy(n, n2);
    }

    @Override
    public String toString(Charset charset) {
        return this.buf.toString(charset);
    }

    @Override
    public ByteBuf writeDouble(double d) {
        this.buf.writeDouble(d);
        return this;
    }

    @Override
    public ByteBuf writeZero(int n) {
        this.buf.writeZero(n);
        return this;
    }

    @Override
    public int bytesBefore(byte by) {
        return this.buf.bytesBefore(by);
    }

    @Override
    public String toString(int n, int n2, Charset charset) {
        return this.buf.toString(n, n2, charset);
    }

    @Override
    public boolean release() {
        return this.buf.release();
    }

    @Override
    public int forEachByteDesc(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByteDesc(n, n2, byteBufProcessor);
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray, int n, int n2) {
        this.buf.writeBytes(byArray, n, n2);
        return this;
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        this.buf.discardSomeReadBytes();
        return this;
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        this.buf.setShort(n, n2);
        return this;
    }

    @Override
    public short getShort(int n) {
        return this.buf.getShort(n);
    }

    @Override
    public ByteBuf retain(int n) {
        this.buf.retain(n);
        return this;
    }

    @Override
    public short readShort() {
        return this.buf.readShort();
    }

    @Override
    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        return this.buf.readBytes(gatheringByteChannel, n);
    }

    @Override
    public long getUnsignedInt(int n) {
        return this.buf.getUnsignedInt(n);
    }

    @Override
    public int indexOf(int n, int n2, byte by) {
        return this.buf.indexOf(n, n2, by);
    }

    @Override
    public int hashCode() {
        return this.buf.hashCode();
    }

    @Override
    public byte readByte() {
        return this.buf.readByte();
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.buf.getBytes(n, byteBuffer);
        return this;
    }

    @Override
    public long readLong() {
        return this.buf.readLong();
    }

    @Override
    public boolean hasArray() {
        return this.buf.hasArray();
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf) {
        this.buf.getBytes(n, byteBuf);
        return this;
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        return this.buf.getBytes(n, gatheringByteChannel, n2);
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray) {
        this.buf.writeBytes(byArray);
        return this;
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        return this.buf.setBytes(n, inputStream, n2);
    }

    @Override
    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray) {
        this.buf.getBytes(n, byArray);
        return this;
    }

    @Override
    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        return this.buf.nioBuffer(n, n2);
    }

    @Override
    public ByteBuf setZero(int n, int n2) {
        this.buf.setZero(n, n2);
        return this;
    }

    @Override
    public ByteBuf readSlice(int n) {
        return this.buf.readSlice(n);
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        return this.buf.internalNioBuffer(n, n2);
    }

    @Override
    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }

    @Override
    public ByteBuf ensureWritable(int n) {
        this.buf.ensureWritable(n);
        return this;
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return this.buf.compareTo(byteBuf);
    }

    @Override
    public ByteBuf clear() {
        this.buf.clear();
        return this;
    }

    @Override
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    @Override
    public byte getByte(int n) {
        return this.buf.getByte(n);
    }

    @Override
    public ByteBuf writeBoolean(boolean bl) {
        this.buf.writeBoolean(bl);
        return this;
    }

    @Override
    public int forEachByte(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByte(n, n2, byteBufProcessor);
    }

    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.buf.toString() + ')';
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByteDesc(byteBufProcessor);
    }

    @Override
    public ByteBuf markWriterIndex() {
        this.buf.markWriterIndex();
        return this;
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int n) throws IOException {
        return this.buf.writeBytes(scatteringByteChannel, n);
    }

    @Override
    public ByteBuf setMedium(int n, int n2) {
        this.buf.setMedium(n, n2);
        return this;
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return this.buf.nioBuffers(n, n2);
    }

    @Override
    public int writeBytes(InputStream inputStream, int n) throws IOException {
        return this.buf.writeBytes(inputStream, n);
    }

    @Override
    public ByteBuf readerIndex(int n) {
        this.buf.readerIndex(n);
        return this;
    }

    @Override
    public int refCnt() {
        return this.buf.refCnt();
    }

    @Override
    public int bytesBefore(int n, int n2, byte by) {
        return this.buf.bytesBefore(n, n2, by);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.buf.getBytes(n, byteBuf, n2, n3);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n, int n2) {
        this.buf.readBytes(byteBuf, n, n2);
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.buf.getBytes(n, outputStream, n2);
        return this;
    }

    @Override
    public int readInt() {
        return this.buf.readInt();
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        return this.buf.setBytes(n, scatteringByteChannel, n2);
    }

    @Override
    public ByteBuf retain() {
        this.buf.retain();
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.buf.getBytes(n, byArray, n2, n3);
        return this;
    }

    @Override
    public ByteBuf readBytes(int n) {
        return this.buf.readBytes(n);
    }

    @Override
    public ByteBuf setChar(int n, int n2) {
        this.buf.setChar(n, n2);
        return this;
    }

    @Override
    public long getLong(int n) {
        return this.buf.getLong(n);
    }

    @Override
    public int capacity() {
        return this.buf.capacity();
    }

    @Override
    public ByteBuf readBytes(byte[] byArray, int n, int n2) {
        this.buf.readBytes(byArray, n, n2);
        return this;
    }

    @Override
    public ByteBuf copy() {
        return this.buf.copy();
    }

    @Override
    public ByteBuf capacity(int n) {
        this.buf.capacity(n);
        return this;
    }

    @Override
    public ByteBuf skipBytes(int n) {
        this.buf.skipBytes(n);
        return this;
    }

    @Override
    public ByteBuf discardReadBytes() {
        this.buf.discardReadBytes();
        return this;
    }

    @Override
    public ByteBuf slice() {
        return this.buf.slice();
    }

    @Override
    public ByteBuf unwrap() {
        return this.buf;
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        return this.buf.order(byteOrder);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        this.buf.readBytes(byteBuffer);
        return this;
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int n) throws IOException {
        this.buf.readBytes(outputStream, n);
        return this;
    }

    @Override
    public int getInt(int n) {
        return this.buf.getInt(n);
    }

    @Override
    public int ensureWritable(int n, boolean bl) {
        return this.buf.ensureWritable(n, bl);
    }

    @Override
    public int getUnsignedMedium(int n) {
        return this.buf.getUnsignedMedium(n);
    }

    @Override
    public int getMedium(int n) {
        return this.buf.getMedium(n);
    }

    @Override
    public boolean release(int n) {
        return this.buf.release(n);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        this.buf.setBytes(n, byteBuffer);
        return this;
    }

    @Override
    public boolean isReadable(int n) {
        return this.buf.isReadable(n);
    }

    @Override
    public byte[] array() {
        return this.buf.array();
    }

    @Override
    public ByteBuf setByte(int n, int n2) {
        this.buf.setByte(n, n2);
        return this;
    }

    @Override
    public boolean isReadable() {
        return this.buf.isReadable();
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        return this.buf.slice(n, n2);
    }

    @Override
    public ByteBuf writeInt(int n) {
        this.buf.writeInt(n);
        return this;
    }

    @Override
    public char readChar() {
        return this.buf.readChar();
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        this.buf.readBytes(byteBuf);
        return this;
    }

    @Override
    public int writableBytes() {
        return this.buf.writableBytes();
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n) {
        this.buf.readBytes(byteBuf, n);
        return this;
    }

    @Override
    public int writerIndex() {
        return this.buf.writerIndex();
    }

    @Override
    public int readerIndex() {
        return this.buf.readerIndex();
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray) {
        this.buf.setBytes(n, byArray);
        return this;
    }

    @Override
    public boolean isDirect() {
        return this.buf.isDirect();
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.buf.setBytes(n, byArray, n2, n3);
        return this;
    }

    @Override
    public int readMedium() {
        return this.buf.readMedium();
    }

    @Override
    public short getUnsignedByte(int n) {
        return this.buf.getUnsignedByte(n);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n, int n2) {
        this.buf.writeBytes(byteBuf, n, n2);
        return this;
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        this.buf.setLong(n, l);
        return this;
    }

    @Override
    public char getChar(int n) {
        return this.buf.getChar(n);
    }

    @Override
    public ByteBuf readBytes(byte[] byArray) {
        this.buf.readBytes(byArray);
        return this;
    }

    @Override
    public ByteBuf writeLong(long l) {
        this.buf.writeLong(l);
        return this;
    }

    @Override
    public ByteBuf resetReaderIndex() {
        this.buf.resetReaderIndex();
        return this;
    }

    @Override
    public boolean isWritable(int n) {
        return this.buf.isWritable(n);
    }

    @Override
    public ByteBuf setBoolean(int n, boolean bl) {
        this.buf.setBoolean(n, bl);
        return this;
    }

    @Override
    public ByteBuf writerIndex(int n) {
        this.buf.writerIndex(n);
        return this;
    }

    @Override
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    @Override
    public ByteBuf writeFloat(float f) {
        this.buf.writeFloat(f);
        return this;
    }

    @Override
    public int bytesBefore(int n, byte by) {
        return this.buf.bytesBefore(n, by);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2) {
        this.buf.setBytes(n, byteBuf, n2);
        return this;
    }

    @Override
    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    @Override
    public boolean isWritable() {
        return this.buf.isWritable();
    }

    @Override
    public boolean getBoolean(int n) {
        return this.buf.getBoolean(n);
    }

    @Override
    public ByteBuf setDouble(int n, double d) {
        this.buf.setDouble(n, d);
        return this;
    }

    @Override
    public ByteBuf writeChar(int n) {
        this.buf.writeChar(n);
        return this;
    }

    @Override
    public int forEachByte(ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByte(byteBufProcessor);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf) {
        this.buf.setBytes(n, byteBuf);
        return this;
    }

    @Override
    public ByteBuf resetWriterIndex() {
        this.buf.resetWriterIndex();
        return this;
    }
}

