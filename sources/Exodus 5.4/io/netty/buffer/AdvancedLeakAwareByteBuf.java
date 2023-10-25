/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.WrappedByteBuf;
import io.netty.util.ResourceLeak;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

final class AdvancedLeakAwareByteBuf
extends WrappedByteBuf {
    private final ResourceLeak leak;

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n, int n2) {
        this.leak.record();
        return super.writeBytes(byteBuf, n, n2);
    }

    @Override
    public int getInt(int n) {
        this.leak.record();
        return super.getInt(n);
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray) {
        this.leak.record();
        return super.writeBytes(byArray);
    }

    @Override
    public ByteBuf writeInt(int n) {
        this.leak.record();
        return super.writeInt(n);
    }

    @Override
    public ByteBuf setMedium(int n, int n2) {
        this.leak.record();
        return super.setMedium(n, n2);
    }

    @Override
    public ByteBuf duplicate() {
        this.leak.record();
        return new AdvancedLeakAwareByteBuf(super.duplicate(), this.leak);
    }

    @Override
    public short getShort(int n) {
        this.leak.record();
        return super.getShort(n);
    }

    @Override
    public ByteBuf writeChar(int n) {
        this.leak.record();
        return super.writeChar(n);
    }

    @Override
    public ByteBuf writeShort(int n) {
        this.leak.record();
        return super.writeShort(n);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.leak.record();
        return super.setBytes(n, byteBuf, n2, n3);
    }

    @Override
    public long getUnsignedInt(int n) {
        this.leak.record();
        return super.getUnsignedInt(n);
    }

    @Override
    public ByteBuf setDouble(int n, double d) {
        this.leak.record();
        return super.setDouble(n, d);
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        this.leak.record();
        return super.internalNioBuffer(n, n2);
    }

    @Override
    public ByteBuf writeMedium(int n) {
        this.leak.record();
        return super.writeMedium(n);
    }

    @Override
    public int forEachByte(int n, int n2, ByteBufProcessor byteBufProcessor) {
        this.leak.record();
        return super.forEachByte(n, n2, byteBufProcessor);
    }

    @Override
    public ByteBuf writeBoolean(boolean bl) {
        this.leak.record();
        return super.writeBoolean(bl);
    }

    @Override
    public ByteBuf setBoolean(int n, boolean bl) {
        this.leak.record();
        return super.setBoolean(n, bl);
    }

    @Override
    public ByteBuffer nioBuffer() {
        this.leak.record();
        return super.nioBuffer();
    }

    @Override
    public ByteBuf setByte(int n, int n2) {
        this.leak.record();
        return super.setByte(n, n2);
    }

    @Override
    public boolean readBoolean() {
        this.leak.record();
        return super.readBoolean();
    }

    @Override
    public ByteBuf readBytes(byte[] byArray) {
        this.leak.record();
        return super.readBytes(byArray);
    }

    @Override
    public char readChar() {
        this.leak.record();
        return super.readChar();
    }

    @Override
    public int readInt() {
        this.leak.record();
        return super.readInt();
    }

    @Override
    public ByteBuf writeLong(long l) {
        this.leak.record();
        return super.writeLong(l);
    }

    @Override
    public float readFloat() {
        this.leak.record();
        return super.readFloat();
    }

    @Override
    public ByteBuf readBytes(byte[] byArray, int n, int n2) {
        this.leak.record();
        return super.readBytes(byArray, n, n2);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        this.leak.record();
        return super.writeBytes(byteBuf);
    }

    @Override
    public boolean release() {
        boolean bl = super.release();
        if (bl) {
            this.leak.close();
        } else {
            this.leak.record();
        }
        return bl;
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor byteBufProcessor) {
        this.leak.record();
        return super.forEachByteDesc(byteBufProcessor);
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        this.leak.record();
        return super.setLong(n, l);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int n) throws IOException {
        this.leak.record();
        return super.writeBytes(scatteringByteChannel, n);
    }

    @Override
    public ByteBuf copy() {
        this.leak.record();
        return super.copy();
    }

    @Override
    public long getLong(int n) {
        this.leak.record();
        return super.getLong(n);
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        this.leak.record();
        return super.setBytes(n, inputStream, n2);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n) {
        this.leak.record();
        return super.writeBytes(byteBuf, n);
    }

    @Override
    public ByteBuf setChar(int n, int n2) {
        this.leak.record();
        return super.setChar(n, n2);
    }

    @Override
    public int getMedium(int n) {
        this.leak.record();
        return super.getMedium(n);
    }

    @Override
    public short readUnsignedByte() {
        this.leak.record();
        return super.readUnsignedByte();
    }

    @Override
    public double getDouble(int n) {
        this.leak.record();
        return super.getDouble(n);
    }

    @Override
    public ByteBuf setFloat(int n, float f) {
        this.leak.record();
        return super.setFloat(n, f);
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        this.leak.record();
        return new AdvancedLeakAwareByteBuf(super.slice(n, n2), this.leak);
    }

    @Override
    public int bytesBefore(byte by) {
        this.leak.record();
        return super.bytesBefore(by);
    }

    @Override
    public byte readByte() {
        this.leak.record();
        return super.readByte();
    }

    @Override
    public int bytesBefore(int n, byte by) {
        this.leak.record();
        return super.bytesBefore(n, by);
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int n) throws IOException {
        this.leak.record();
        return super.readBytes(outputStream, n);
    }

    @Override
    public ByteBuf skipBytes(int n) {
        this.leak.record();
        return super.skipBytes(n);
    }

    @Override
    public int getUnsignedShort(int n) {
        this.leak.record();
        return super.getUnsignedShort(n);
    }

    @Override
    public short readShort() {
        this.leak.record();
        return super.readShort();
    }

    @Override
    public ByteBuf setZero(int n, int n2) {
        this.leak.record();
        return super.setZero(n, n2);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        this.leak.record();
        return super.setBytes(n, byteBuffer);
    }

    @Override
    public ByteBuf writeZero(int n) {
        this.leak.record();
        return super.writeZero(n);
    }

    @Override
    public int ensureWritable(int n, boolean bl) {
        this.leak.record();
        return super.ensureWritable(n, bl);
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        this.leak.record();
        if (this.order() == byteOrder) {
            return this;
        }
        return new AdvancedLeakAwareByteBuf(super.order(byteOrder), this.leak);
    }

    @Override
    public int readUnsignedMedium() {
        this.leak.record();
        return super.readUnsignedMedium();
    }

    @Override
    public int indexOf(int n, int n2, byte by) {
        this.leak.record();
        return super.indexOf(n, n2, by);
    }

    @Override
    public boolean getBoolean(int n) {
        this.leak.record();
        return super.getBoolean(n);
    }

    @Override
    public int writeBytes(InputStream inputStream, int n) throws IOException {
        this.leak.record();
        return super.writeBytes(inputStream, n);
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        this.leak.record();
        return super.nioBuffers(n, n2);
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        this.leak.record();
        return super.setShort(n, n2);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        this.leak.record();
        return super.readBytes(byteBuf);
    }

    AdvancedLeakAwareByteBuf(ByteBuf byteBuf, ResourceLeak resourceLeak) {
        super(byteBuf);
        this.leak = resourceLeak;
    }

    @Override
    public ByteBuf writeByte(int n) {
        this.leak.record();
        return super.writeByte(n);
    }

    @Override
    public double readDouble() {
        this.leak.record();
        return super.readDouble();
    }

    @Override
    public int nioBufferCount() {
        this.leak.record();
        return super.nioBufferCount();
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        this.leak.record();
        return super.copy(n, n2);
    }

    @Override
    public char getChar(int n) {
        this.leak.record();
        return super.getChar(n);
    }

    @Override
    public ByteBuf ensureWritable(int n) {
        this.leak.record();
        return super.ensureWritable(n);
    }

    @Override
    public int forEachByteDesc(int n, int n2, ByteBufProcessor byteBufProcessor) {
        this.leak.record();
        return super.forEachByteDesc(n, n2, byteBufProcessor);
    }

    @Override
    public ByteBuf slice() {
        this.leak.record();
        return new AdvancedLeakAwareByteBuf(super.slice(), this.leak);
    }

    @Override
    public ByteBuf readBytes(int n) {
        this.leak.record();
        return super.readBytes(n);
    }

    @Override
    public ByteBuf writeFloat(float f) {
        this.leak.record();
        return super.writeFloat(f);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf) {
        this.leak.record();
        return super.setBytes(n, byteBuf);
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray, int n, int n2) {
        this.leak.record();
        return super.writeBytes(byArray, n, n2);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        this.leak.record();
        return super.writeBytes(byteBuffer);
    }

    @Override
    public ByteBuf readSlice(int n) {
        this.leak.record();
        return new AdvancedLeakAwareByteBuf(super.readSlice(n), this.leak);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.leak.record();
        return super.getBytes(n, byteBuffer);
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray) {
        this.leak.record();
        return super.setBytes(n, byArray);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        this.leak.record();
        return super.readBytes(gatheringByteChannel, n);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf) {
        this.leak.record();
        return super.getBytes(n, byteBuf);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n) {
        this.leak.record();
        return super.readBytes(byteBuf, n);
    }

    @Override
    public ByteBuf writeDouble(double d) {
        this.leak.record();
        return super.writeDouble(d);
    }

    @Override
    public ByteBuf capacity(int n) {
        this.leak.record();
        return super.capacity(n);
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.leak.record();
        return super.getBytes(n, byArray, n2, n3);
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        this.leak.record();
        return super.discardSomeReadBytes();
    }

    @Override
    public long readUnsignedInt() {
        this.leak.record();
        return super.readUnsignedInt();
    }

    @Override
    public int forEachByte(ByteBufProcessor byteBufProcessor) {
        this.leak.record();
        return super.forEachByte(byteBufProcessor);
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.leak.record();
        return super.getBytes(n, outputStream, n2);
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        this.leak.record();
        return super.setBytes(n, scatteringByteChannel, n2);
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray) {
        this.leak.record();
        return super.getBytes(n, byArray);
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.leak.record();
        return super.setBytes(n, byArray, n2, n3);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2) {
        this.leak.record();
        return super.setBytes(n, byteBuf, n2);
    }

    @Override
    public String toString(Charset charset) {
        this.leak.record();
        return super.toString(charset);
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        this.leak.record();
        return super.nioBuffer(n, n2);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.leak.record();
        return super.getBytes(n, byteBuf, n2, n3);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        this.leak.record();
        return super.nioBuffers();
    }

    @Override
    public ByteBuf retain(int n) {
        this.leak.record();
        return super.retain(n);
    }

    @Override
    public ByteBuf retain() {
        this.leak.record();
        return super.retain();
    }

    @Override
    public long readLong() {
        this.leak.record();
        return super.readLong();
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        this.leak.record();
        return super.getBytes(n, gatheringByteChannel, n2);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2) {
        this.leak.record();
        return super.getBytes(n, byteBuf, n2);
    }

    @Override
    public int readUnsignedShort() {
        this.leak.record();
        return super.readUnsignedShort();
    }

    @Override
    public int bytesBefore(int n, int n2, byte by) {
        this.leak.record();
        return super.bytesBefore(n, n2, by);
    }

    @Override
    public boolean release(int n) {
        boolean bl = super.release(n);
        if (bl) {
            this.leak.close();
        } else {
            this.leak.record();
        }
        return bl;
    }

    @Override
    public ByteBuf discardReadBytes() {
        this.leak.record();
        return super.discardReadBytes();
    }

    @Override
    public String toString(int n, int n2, Charset charset) {
        this.leak.record();
        return super.toString(n, n2, charset);
    }

    @Override
    public float getFloat(int n) {
        this.leak.record();
        return super.getFloat(n);
    }

    @Override
    public int readMedium() {
        this.leak.record();
        return super.readMedium();
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n, int n2) {
        this.leak.record();
        return super.readBytes(byteBuf, n, n2);
    }

    @Override
    public byte getByte(int n) {
        this.leak.record();
        return super.getByte(n);
    }

    @Override
    public short getUnsignedByte(int n) {
        this.leak.record();
        return super.getUnsignedByte(n);
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        this.leak.record();
        return super.setInt(n, n2);
    }

    @Override
    public int getUnsignedMedium(int n) {
        this.leak.record();
        return super.getUnsignedMedium(n);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        this.leak.record();
        return super.readBytes(byteBuffer);
    }
}

