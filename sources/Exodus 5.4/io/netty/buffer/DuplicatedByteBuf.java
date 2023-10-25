/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractDerivedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class DuplicatedByteBuf
extends AbstractDerivedByteBuf {
    private final ByteBuf buffer;

    @Override
    protected void _setInt(int n, int n2) {
        this.buffer.setInt(n, n2);
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        this._setInt(n, n2);
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.buffer.getBytes(n, byArray, n2, n3);
        return this;
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        return this.buffer.slice(n, n2);
    }

    @Override
    public int getUnsignedMedium(int n) {
        return this._getUnsignedMedium(n);
    }

    public DuplicatedByteBuf(ByteBuf byteBuf) {
        super(byteBuf.maxCapacity());
        this.buffer = byteBuf instanceof DuplicatedByteBuf ? ((DuplicatedByteBuf)byteBuf).buffer : byteBuf;
        this.setIndex(byteBuf.readerIndex(), byteBuf.writerIndex());
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.buffer.setBytes(n, byArray, n2, n3);
        return this;
    }

    @Override
    protected void _setMedium(int n, int n2) {
        this.buffer.setMedium(n, n2);
    }

    @Override
    public ByteBuf setByte(int n, int n2) {
        this._setByte(n, n2);
        return this;
    }

    @Override
    protected void _setByte(int n, int n2) {
        this.buffer.setByte(n, n2);
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
    public byte[] array() {
        return this.buffer.array();
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return this.buffer.nioBuffers(n, n2);
    }

    @Override
    public int capacity() {
        return this.buffer.capacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }

    @Override
    public int forEachByte(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buffer.forEachByte(n, n2, byteBufProcessor);
    }

    @Override
    public int getInt(int n) {
        return this._getInt(n);
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        return this.buffer.setBytes(n, scatteringByteChannel, n2);
    }

    @Override
    public ByteBuf setMedium(int n, int n2) {
        this._setMedium(n, n2);
        return this;
    }

    @Override
    protected void _setShort(int n, int n2) {
        this.buffer.setShort(n, n2);
    }

    @Override
    public short getShort(int n) {
        return this._getShort(n);
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        return this.buffer.getBytes(n, gatheringByteChannel, n2);
    }

    @Override
    public byte getByte(int n) {
        return this._getByte(n);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.buffer.getBytes(n, byteBuf, n2, n3);
        return this;
    }

    @Override
    protected void _setLong(int n, long l) {
        this.buffer.setLong(n, l);
    }

    @Override
    protected short _getShort(int n) {
        return this.buffer.getShort(n);
    }

    @Override
    public ByteBuf capacity(int n) {
        this.buffer.capacity(n);
        return this;
    }

    @Override
    protected long _getLong(int n) {
        return this.buffer.getLong(n);
    }

    @Override
    public ByteBuf unwrap() {
        return this.buffer;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.buffer.setBytes(n, byteBuf, n2, n3);
        return this;
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        this._setLong(n, l);
        return this;
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        this._setShort(n, n2);
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.buffer.getBytes(n, byteBuffer);
        return this;
    }

    @Override
    public boolean hasArray() {
        return this.buffer.hasArray();
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.buffer.hasMemoryAddress();
    }

    @Override
    public int arrayOffset() {
        return this.buffer.arrayOffset();
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        return this.buffer.setBytes(n, inputStream, n2);
    }

    @Override
    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        this.buffer.setBytes(n, byteBuffer);
        return this;
    }

    @Override
    protected byte _getByte(int n) {
        return this.buffer.getByte(n);
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.buffer.getBytes(n, outputStream, n2);
        return this;
    }

    @Override
    public long memoryAddress() {
        return this.buffer.memoryAddress();
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        return this.nioBuffer(n, n2);
    }

    @Override
    public ByteOrder order() {
        return this.buffer.order();
    }

    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    @Override
    public int forEachByteDesc(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buffer.forEachByteDesc(n, n2, byteBufProcessor);
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        return this.buffer.getUnsignedMedium(n);
    }

    @Override
    public long getLong(int n) {
        return this._getLong(n);
    }
}

