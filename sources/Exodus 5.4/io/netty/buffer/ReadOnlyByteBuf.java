/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractDerivedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.DuplicatedByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class ReadOnlyByteBuf
extends AbstractDerivedByteBuf {
    private final ByteBuf buffer;

    @Override
    public ByteBuf capacity(int n) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.buffer.getBytes(n, byteBuffer);
        return this;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuf setByte(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    protected void _setShort(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        return this.buffer.getUnsignedMedium(n);
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        return this.buffer.nioBuffer(n, n2).asReadOnlyBuffer();
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.buffer.getBytes(n, byArray, n2, n3);
        return this;
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public boolean isWritable(int n) {
        return false;
    }

    @Override
    public ByteBuf unwrap() {
        return this.buffer;
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        return this.buffer.getBytes(n, gatheringByteChannel, n2);
    }

    @Override
    public ByteBuf discardReadBytes() {
        throw new ReadOnlyBufferException();
    }

    @Override
    public short getShort(int n) {
        return this._getShort(n);
    }

    @Override
    public long getLong(int n) {
        return this._getLong(n);
    }

    @Override
    public ByteBuf setMedium(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    protected long _getLong(int n) {
        return this.buffer.getLong(n);
    }

    @Override
    protected void _setMedium(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    @Override
    public int getInt(int n) {
        return this._getInt(n);
    }

    @Override
    public int forEachByte(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buffer.forEachByte(n, n2, byteBufProcessor);
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return this.buffer.nioBuffers(n, n2);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public boolean hasMemoryAddress() {
        return false;
    }

    @Override
    protected void _setLong(int n, long l) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        return Unpooled.unmodifiableBuffer(this.buffer.slice(n, n2));
    }

    @Override
    public int arrayOffset() {
        throw new ReadOnlyBufferException();
    }

    @Override
    public int getUnsignedMedium(int n) {
        return this._getUnsignedMedium(n);
    }

    @Override
    public int forEachByteDesc(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buffer.forEachByteDesc(n, n2, byteBufProcessor);
    }

    @Override
    protected short _getShort(int n) {
        return this.buffer.getShort(n);
    }

    @Override
    public long memoryAddress() {
        throw new ReadOnlyBufferException();
    }

    @Override
    protected byte _getByte(int n) {
        return this.buffer.getByte(n);
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public int capacity() {
        return this.buffer.capacity();
    }

    public ReadOnlyByteBuf(ByteBuf byteBuf) {
        super(byteBuf.maxCapacity());
        this.buffer = byteBuf instanceof ReadOnlyByteBuf || byteBuf instanceof DuplicatedByteBuf ? byteBuf.unwrap() : byteBuf;
        this.setIndex(byteBuf.readerIndex(), byteBuf.writerIndex());
    }

    @Override
    protected int _getInt(int n) {
        return this.buffer.getInt(n);
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        return this.buffer.copy(n, n2);
    }

    @Override
    public ByteBuf duplicate() {
        return new ReadOnlyByteBuf(this);
    }

    @Override
    public ByteOrder order() {
        return this.buffer.order();
    }

    @Override
    protected void _setByte(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.buffer.getBytes(n, byteBuf, n2, n3);
        return this;
    }

    @Override
    public byte[] array() {
        throw new ReadOnlyBufferException();
    }

    @Override
    public boolean hasArray() {
        return false;
    }

    @Override
    public byte getByte(int n) {
        return this._getByte(n);
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.buffer.getBytes(n, outputStream, n2);
        return this;
    }

    @Override
    protected void _setInt(int n, int n2) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        return this.nioBuffer(n, n2);
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) {
        throw new ReadOnlyBufferException();
    }
}

