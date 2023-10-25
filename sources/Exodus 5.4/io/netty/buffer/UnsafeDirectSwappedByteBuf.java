/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.SwappedByteBuf;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteOrder;

final class UnsafeDirectSwappedByteBuf
extends SwappedByteBuf {
    private final AbstractByteBuf wrapped;
    private final boolean nativeByteOrder;
    private static final boolean NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;

    @Override
    public long getUnsignedInt(int n) {
        return (long)this.getInt(n) & 0xFFFFFFFFL;
    }

    private void _setLong(int n, long l) {
        PlatformDependent.putLong(this.addr(n), this.nativeByteOrder ? l : Long.reverseBytes(l));
    }

    @Override
    public char getChar(int n) {
        return (char)this.getShort(n);
    }

    @Override
    public ByteBuf writeInt(int n) {
        this.wrapped.ensureAccessible();
        this.wrapped.ensureWritable(4);
        this._setInt(this.wrapped.writerIndex, n);
        this.wrapped.writerIndex += 4;
        return this;
    }

    @Override
    public double getDouble(int n) {
        return Double.longBitsToDouble(this.getLong(n));
    }

    @Override
    public int getInt(int n) {
        this.wrapped.checkIndex(n, 4);
        int n2 = PlatformDependent.getInt(this.addr(n));
        return this.nativeByteOrder ? n2 : Integer.reverseBytes(n2);
    }

    @Override
    public ByteBuf writeFloat(float f) {
        this.writeInt(Float.floatToRawIntBits(f));
        return this;
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        this.wrapped.checkIndex(n, 8);
        this._setLong(n, l);
        return this;
    }

    @Override
    public ByteBuf setDouble(int n, double d) {
        this.setLong(n, Double.doubleToRawLongBits(d));
        return this;
    }

    @Override
    public int getUnsignedShort(int n) {
        return this.getShort(n) & 0xFFFF;
    }

    @Override
    public ByteBuf setChar(int n, int n2) {
        this.setShort(n, n2);
        return this;
    }

    private void _setInt(int n, int n2) {
        PlatformDependent.putInt(this.addr(n), this.nativeByteOrder ? n2 : Integer.reverseBytes(n2));
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        this.wrapped.checkIndex(n, 2);
        this._setShort(n, n2);
        return this;
    }

    @Override
    public ByteBuf writeShort(int n) {
        this.wrapped.ensureAccessible();
        this.wrapped.ensureWritable(2);
        this._setShort(this.wrapped.writerIndex, n);
        this.wrapped.writerIndex += 2;
        return this;
    }

    @Override
    public long getLong(int n) {
        this.wrapped.checkIndex(n, 8);
        long l = PlatformDependent.getLong(this.addr(n));
        return this.nativeByteOrder ? l : Long.reverseBytes(l);
    }

    @Override
    public ByteBuf writeDouble(double d) {
        this.writeLong(Double.doubleToRawLongBits(d));
        return this;
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        this.wrapped.checkIndex(n, 4);
        this._setInt(n, n2);
        return this;
    }

    private long addr(int n) {
        return this.wrapped.memoryAddress() + (long)n;
    }

    @Override
    public ByteBuf setFloat(int n, float f) {
        this.setInt(n, Float.floatToRawIntBits(f));
        return this;
    }

    @Override
    public float getFloat(int n) {
        return Float.intBitsToFloat(this.getInt(n));
    }

    @Override
    public short getShort(int n) {
        this.wrapped.checkIndex(n, 2);
        short s = PlatformDependent.getShort(this.addr(n));
        return this.nativeByteOrder ? s : Short.reverseBytes(s);
    }

    UnsafeDirectSwappedByteBuf(AbstractByteBuf abstractByteBuf) {
        super(abstractByteBuf);
        this.wrapped = abstractByteBuf;
        this.nativeByteOrder = NATIVE_ORDER == (this.order() == ByteOrder.BIG_ENDIAN);
    }

    @Override
    public ByteBuf writeChar(int n) {
        this.writeShort(n);
        return this;
    }

    @Override
    public ByteBuf writeLong(long l) {
        this.wrapped.ensureAccessible();
        this.wrapped.ensureWritable(8);
        this._setLong(this.wrapped.writerIndex, l);
        this.wrapped.writerIndex += 8;
        return this;
    }

    private void _setShort(int n, int n2) {
        PlatformDependent.putShort(this.addr(n), this.nativeByteOrder ? (short)n2 : Short.reverseBytes((short)n2));
    }
}

