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
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class SlicedByteBuf
extends AbstractDerivedByteBuf {
    private final int length;
    private final ByteBuf buffer;
    private final int adjustment;

    @Override
    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }

    @Override
    public long memoryAddress() {
        return this.buffer.memoryAddress() + (long)this.adjustment;
    }

    @Override
    public ByteBuf duplicate() {
        ByteBuf byteBuf = this.buffer.slice(this.adjustment, this.length);
        byteBuf.setIndex(this.readerIndex(), this.writerIndex());
        return byteBuf;
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        return this.buffer.getUnsignedMedium(n + this.adjustment);
    }

    @Override
    protected int _getInt(int n) {
        return this.buffer.getInt(n + this.adjustment);
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        this.checkIndex(n, n2);
        return this.buffer.getBytes(n + this.adjustment, gatheringByteChannel, n2);
    }

    @Override
    protected void _setInt(int n, int n2) {
        this.buffer.setInt(n + this.adjustment, n2);
    }

    public SlicedByteBuf(ByteBuf byteBuf, int n, int n2) {
        super(n2);
        if (n < 0 || n > byteBuf.capacity() - n2) {
            throw new IndexOutOfBoundsException(byteBuf + ".slice(" + n + ", " + n2 + ')');
        }
        if (byteBuf instanceof SlicedByteBuf) {
            this.buffer = ((SlicedByteBuf)byteBuf).buffer;
            this.adjustment = ((SlicedByteBuf)byteBuf).adjustment + n;
        } else if (byteBuf instanceof DuplicatedByteBuf) {
            this.buffer = byteBuf.unwrap();
            this.adjustment = n;
        } else {
            this.buffer = byteBuf;
            this.adjustment = n;
        }
        this.length = n2;
        this.writerIndex(n2);
    }

    @Override
    public boolean hasArray() {
        return this.buffer.hasArray();
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        this.checkIndex(n, byteBuffer.remaining());
        this.buffer.setBytes(n + this.adjustment, byteBuffer);
        return this;
    }

    @Override
    public ByteBuf unwrap() {
        return this.buffer;
    }

    @Override
    protected byte _getByte(int n) {
        return this.buffer.getByte(n + this.adjustment);
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        return this.nioBuffer(n, n2);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.checkIndex(n, byteBuffer.remaining());
        this.buffer.getBytes(n + this.adjustment, byteBuffer);
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkIndex(n, n3);
        this.buffer.setBytes(n + this.adjustment, byArray, n2, n3);
        return this;
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        return this.buffer.nioBuffer(n + this.adjustment, n2);
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        return this.buffer.slice(n + this.adjustment, n2);
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.checkIndex(n, n2);
        this.buffer.getBytes(n + this.adjustment, outputStream, n2);
        return this;
    }

    @Override
    public byte[] array() {
        return this.buffer.array();
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        this.checkIndex(n, n2);
        return this.buffer.setBytes(n + this.adjustment, scatteringByteChannel, n2);
    }

    @Override
    protected void _setByte(int n, int n2) {
        this.buffer.setByte(n + this.adjustment, n2);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkIndex(n, n3);
        this.buffer.setBytes(n + this.adjustment, byteBuf, n2, n3);
        return this;
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        this.checkIndex(n, n2);
        return this.buffer.nioBuffers(n + this.adjustment, n2);
    }

    @Override
    protected void _setLong(int n, long l) {
        this.buffer.setLong(n + this.adjustment, l);
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        this.checkIndex(n, n2);
        return this.buffer.copy(n + this.adjustment, n2);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkIndex(n, n3);
        this.buffer.getBytes(n + this.adjustment, byteBuf, n2, n3);
        return this;
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        this.checkIndex(n, n2);
        return this.buffer.setBytes(n + this.adjustment, inputStream, n2);
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkIndex(n, n3);
        this.buffer.getBytes(n + this.adjustment, byArray, n2, n3);
        return this;
    }

    @Override
    protected void _setShort(int n, int n2) {
        this.buffer.setShort(n + this.adjustment, n2);
    }

    @Override
    public int capacity() {
        return this.length;
    }

    @Override
    public int forEachByte(int n, int n2, ByteBufProcessor byteBufProcessor) {
        int n3 = this.buffer.forEachByte(n + this.adjustment, n2, byteBufProcessor);
        if (n3 >= this.adjustment) {
            return n3 - this.adjustment;
        }
        return -1;
    }

    @Override
    protected void _setMedium(int n, int n2) {
        this.buffer.setMedium(n + this.adjustment, n2);
    }

    @Override
    protected long _getLong(int n) {
        return this.buffer.getLong(n + this.adjustment);
    }

    @Override
    public int forEachByteDesc(int n, int n2, ByteBufProcessor byteBufProcessor) {
        int n3 = this.buffer.forEachByteDesc(n + this.adjustment, n2, byteBufProcessor);
        if (n3 >= this.adjustment) {
            return n3 - this.adjustment;
        }
        return -1;
    }

    @Override
    public ByteOrder order() {
        return this.buffer.order();
    }

    @Override
    public ByteBuf capacity(int n) {
        throw new UnsupportedOperationException("sliced buffer");
    }

    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.buffer.hasMemoryAddress();
    }

    @Override
    protected short _getShort(int n) {
        return this.buffer.getShort(n + this.adjustment);
    }

    @Override
    public int arrayOffset() {
        return this.buffer.arrayOffset() + this.adjustment;
    }

    @Override
    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }
}

