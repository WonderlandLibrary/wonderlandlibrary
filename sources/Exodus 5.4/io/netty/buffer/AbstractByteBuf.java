/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.DuplicatedByteBuf;
import io.netty.buffer.SlicedByteBuf;
import io.netty.buffer.SwappedByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

public abstract class AbstractByteBuf
extends ByteBuf {
    static final ResourceLeakDetector<ByteBuf> leakDetector = new ResourceLeakDetector(ByteBuf.class);
    private SwappedByteBuf swappedBuf;
    private int markedWriterIndex;
    int readerIndex;
    private int maxCapacity;
    private int markedReaderIndex;
    int writerIndex;

    @Override
    public ByteBuf writerIndex(int n) {
        if (n < this.readerIndex || n > this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("writerIndex: %d (expected: readerIndex(%d) <= writerIndex <= capacity(%d))", n, this.readerIndex, this.capacity()));
        }
        this.writerIndex = n;
        return this;
    }

    protected abstract long _getLong(int var1);

    @Override
    public char getChar(int n) {
        return (char)this.getShort(n);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int n) throws IOException {
        this.ensureAccessible();
        this.ensureWritable(n);
        int n2 = this.setBytes(this.writerIndex, scatteringByteChannel, n);
        if (n2 > 0) {
            this.writerIndex += n2;
        }
        return n2;
    }

    @Override
    public long readLong() {
        this.checkReadableBytes(8);
        long l = this._getLong(this.readerIndex);
        this.readerIndex += 8;
        return l;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2) {
        this.getBytes(n, byteBuf, byteBuf.writerIndex(), n2);
        byteBuf.writerIndex(byteBuf.writerIndex() + n2);
        return this;
    }

    protected AbstractByteBuf(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("maxCapacity: " + n + " (expected: >= 0)");
        }
        this.maxCapacity = n;
    }

    @Override
    public ByteBuf writeLong(long l) {
        this.ensureAccessible();
        this.ensureWritable(8);
        this._setLong(this.writerIndex, l);
        this.writerIndex += 8;
        return this;
    }

    protected abstract int _getUnsignedMedium(int var1);

    @Override
    public boolean getBoolean(int n) {
        return this.getByte(n) != 0;
    }

    @Override
    public ByteBuf writeBoolean(boolean bl) {
        this.writeByte(bl ? 1 : 0);
        return this;
    }

    protected final void checkIndex(int n) {
        this.ensureAccessible();
        if (n < 0 || n >= this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("index: %d (expected: range(0, %d))", n, this.capacity()));
        }
    }

    protected abstract void _setLong(int var1, long var2);

    @Override
    public ByteBuf writeShort(int n) {
        this.ensureAccessible();
        this.ensureWritable(2);
        this._setShort(this.writerIndex, n);
        this.writerIndex += 2;
        return this;
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        this.checkReadableBytes(n);
        int n2 = this.getBytes(this.readerIndex, gatheringByteChannel, n);
        this.readerIndex += n2;
        return n2;
    }

    @Override
    public ByteBuf setFloat(int n, float f) {
        this.setInt(n, Float.floatToRawIntBits(f));
        return this;
    }

    @Override
    public ByteBuf duplicate() {
        return new DuplicatedByteBuf(this);
    }

    @Override
    public byte readByte() {
        this.checkReadableBytes(1);
        int n = this.readerIndex;
        byte by = this.getByte(n);
        this.readerIndex = n + 1;
        return by;
    }

    @Override
    public ByteBuf readBytes(int n) {
        this.checkReadableBytes(n);
        if (n == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBuf byteBuf = Unpooled.buffer(n, this.maxCapacity);
        byteBuf.writeBytes(this, this.readerIndex, n);
        this.readerIndex += n;
        return byteBuf;
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return ByteBufUtil.compare(this, byteBuf);
    }

    @Override
    public int bytesBefore(int n, byte by) {
        this.checkReadableBytes(n);
        return this.bytesBefore(this.readerIndex(), n, by);
    }

    @Override
    public ByteBuf writeZero(int n) {
        int n2;
        if (n == 0) {
            return this;
        }
        this.ensureWritable(n);
        this.checkIndex(this.writerIndex, n);
        int n3 = n >>> 3;
        int n4 = n & 7;
        for (n2 = n3; n2 > 0; --n2) {
            this.writeLong(0L);
        }
        if (n4 == 4) {
            this.writeInt(0);
        } else if (n4 < 4) {
            for (n2 = n4; n2 > 0; --n2) {
                this.writeByte(0);
            }
        } else {
            this.writeInt(0);
            for (n2 = n4 - 4; n2 > 0; --n2) {
                this.writeByte(0);
            }
        }
        return this;
    }

    @Override
    public int readerIndex() {
        return this.readerIndex;
    }

    protected abstract void _setInt(int var1, int var2);

    @Override
    public ByteBuf slice(int n, int n2) {
        if (n2 == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        return new SlicedByteBuf(this, n, n2);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n, int n2) {
        this.ensureAccessible();
        this.ensureWritable(n2);
        this.setBytes(this.writerIndex, byteBuf, n, n2);
        this.writerIndex += n2;
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2) {
        this.checkIndex(n, n2);
        if (byteBuf == null) {
            throw new NullPointerException("src");
        }
        if (n2 > byteBuf.readableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", n2, byteBuf.readableBytes(), byteBuf));
        }
        this.setBytes(n, byteBuf, byteBuf.readerIndex(), n2);
        byteBuf.readerIndex(byteBuf.readerIndex() + n2);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n) {
        if (n > byteBuf.writableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds dst.writableBytes(%d) where dst is: %s", n, byteBuf.writableBytes(), byteBuf));
        }
        this.readBytes(byteBuf, byteBuf.writerIndex(), n);
        byteBuf.writerIndex(byteBuf.writerIndex() + n);
        return this;
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        this.checkIndex(n, 4);
        this._setInt(n, n2);
        return this;
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        if (byteOrder == null) {
            throw new NullPointerException("endianness");
        }
        if (byteOrder == this.order()) {
            return this;
        }
        SwappedByteBuf swappedByteBuf = this.swappedBuf;
        if (swappedByteBuf == null) {
            this.swappedBuf = swappedByteBuf = this.newSwappedByteBuf();
        }
        return swappedByteBuf;
    }

    @Override
    public byte getByte(int n) {
        this.checkIndex(n);
        return this._getByte(n);
    }

    protected final void ensureAccessible() {
        if (this.refCnt() == 0) {
            throw new IllegalReferenceCountException(0);
        }
    }

    protected abstract byte _getByte(int var1);

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        int n = byteBuffer.remaining();
        this.checkReadableBytes(n);
        this.getBytes(this.readerIndex, byteBuffer);
        this.readerIndex += n;
        return this;
    }

    protected abstract short _getShort(int var1);

    @Override
    public short readShort() {
        this.checkReadableBytes(2);
        short s = this._getShort(this.readerIndex);
        this.readerIndex += 2;
        return s;
    }

    @Override
    public int getUnsignedShort(int n) {
        return this.getShort(n) & 0xFFFF;
    }

    @Override
    public ByteBuf discardReadBytes() {
        this.ensureAccessible();
        if (this.readerIndex == 0) {
            return this;
        }
        if (this.readerIndex != this.writerIndex) {
            this.setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
            this.writerIndex -= this.readerIndex;
            this.adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
        } else {
            this.adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
            this.writerIndex = 0;
        }
        return this;
    }

    protected abstract void _setShort(int var1, int var2);

    @Override
    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf) {
        this.setBytes(n, byteBuf, byteBuf.readableBytes());
        return this;
    }

    @Override
    public ByteBuf setBoolean(int n, boolean bl) {
        this.setByte(n, bl ? 1 : 0);
        return this;
    }

    @Override
    public boolean readBoolean() {
        return this.readByte() != 0;
    }

    @Override
    public double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }

    protected SwappedByteBuf newSwappedByteBuf() {
        return new SwappedByteBuf(this);
    }

    @Override
    public ByteBuf writeByte(int n) {
        this.ensureAccessible();
        this.ensureWritable(1);
        this._setByte(this.writerIndex++, n);
        return this;
    }

    @Override
    public int ensureWritable(int n, boolean bl) {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", n));
        }
        if (n <= this.writableBytes()) {
            return 0;
        }
        if (n > this.maxCapacity - this.writerIndex && bl) {
            if (this.capacity() == this.maxCapacity()) {
                return 1;
            }
            this.capacity(this.maxCapacity());
            return 3;
        }
        int n2 = this.calculateNewCapacity(this.writerIndex + n);
        this.capacity(n2);
        return 2;
    }

    @Override
    public ByteBuf clear() {
        this.writerIndex = 0;
        this.readerIndex = 0;
        return this;
    }

    @Override
    public String toString() {
        ByteBuf byteBuf;
        if (this.refCnt() == 0) {
            return StringUtil.simpleClassName(this) + "(freed)";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append("(ridx: ");
        stringBuilder.append(this.readerIndex);
        stringBuilder.append(", widx: ");
        stringBuilder.append(this.writerIndex);
        stringBuilder.append(", cap: ");
        stringBuilder.append(this.capacity());
        if (this.maxCapacity != Integer.MAX_VALUE) {
            stringBuilder.append('/');
            stringBuilder.append(this.maxCapacity);
        }
        if ((byteBuf = this.unwrap()) != null) {
            stringBuilder.append(", unwrapped: ");
            stringBuilder.append(byteBuf);
        }
        stringBuilder.append(')');
        return stringBuilder.toString();
    }

    @Override
    public ByteBuf readBytes(byte[] byArray) {
        this.readBytes(byArray, 0, byArray.length);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        this.writeBytes(byteBuf, byteBuf.readableBytes());
        return this;
    }

    protected final void checkIndex(int n, int n2) {
        this.ensureAccessible();
        if (n2 < 0) {
            throw new IllegalArgumentException("length: " + n2 + " (expected: >= 0)");
        }
        if (n < 0 || n > this.capacity() - n2) {
            throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", n, n2, this.capacity()));
        }
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray) {
        this.getBytes(n, byArray, 0, byArray.length);
        return this;
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.nioBuffer(this.readerIndex, this.readableBytes());
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
    public ByteBuf setIndex(int n, int n2) {
        if (n < 0 || n > n2 || n2 > this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))", n, n2, this.capacity()));
        }
        this.readerIndex = n;
        this.writerIndex = n2;
        return this;
    }

    @Override
    public int forEachByte(int n, int n2, ByteBufProcessor byteBufProcessor) {
        this.checkIndex(n, n2);
        return this.forEachByteAsc0(n, n2, byteBufProcessor);
    }

    protected final void checkDstIndex(int n, int n2, int n3, int n4) {
        this.checkIndex(n, n2);
        if (n3 < 0 || n3 > n4 - n2) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", n3, n2, n4));
        }
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n, int n2) {
        this.checkReadableBytes(n2);
        this.getBytes(this.readerIndex, byteBuf, n, n2);
        this.readerIndex += n2;
        return this;
    }

    @Override
    public char readChar() {
        return (char)this.readShort();
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int n) throws IOException {
        this.checkReadableBytes(n);
        this.getBytes(this.readerIndex, outputStream, n);
        this.readerIndex += n;
        return this;
    }

    @Override
    public ByteBuf skipBytes(int n) {
        this.checkReadableBytes(n);
        this.readerIndex += n;
        return this;
    }

    @Override
    public int maxWritableBytes() {
        return this.maxCapacity() - this.writerIndex;
    }

    @Override
    public int writerIndex() {
        return this.writerIndex;
    }

    @Override
    public int forEachByteDesc(int n, int n2, ByteBufProcessor byteBufProcessor) {
        this.checkIndex(n, n2);
        return this.forEachByteDesc0(n, n2, byteBufProcessor);
    }

    @Override
    public int writeBytes(InputStream inputStream, int n) throws IOException {
        this.ensureAccessible();
        this.ensureWritable(n);
        int n2 = this.setBytes(this.writerIndex, inputStream, n);
        if (n2 > 0) {
            this.writerIndex += n2;
        }
        return n2;
    }

    @Override
    public boolean isReadable(int n) {
        return this.writerIndex - this.readerIndex >= n;
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        this.checkIndex(n, 8);
        this._setLong(n, l);
        return this;
    }

    @Override
    public ByteBuf writeInt(int n) {
        this.ensureAccessible();
        this.ensureWritable(4);
        this._setInt(this.writerIndex, n);
        this.writerIndex += 4;
        return this;
    }

    @Override
    public int readUnsignedMedium() {
        this.checkReadableBytes(3);
        int n = this._getUnsignedMedium(this.readerIndex);
        this.readerIndex += 3;
        return n;
    }

    protected final void checkSrcIndex(int n, int n2, int n3, int n4) {
        this.checkIndex(n, n2);
        if (n3 < 0 || n3 > n4 - n2) {
            throw new IndexOutOfBoundsException(String.format("srcIndex: %d, length: %d (expected: range(0, %d))", n3, n2, n4));
        }
    }

    @Override
    public int getUnsignedMedium(int n) {
        this.checkIndex(n, 3);
        return this._getUnsignedMedium(n);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.nioBuffers(this.readerIndex, this.readableBytes());
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray) {
        this.writeBytes(byArray, 0, byArray.length);
        return this;
    }

    @Override
    public ByteBuf setMedium(int n, int n2) {
        this.checkIndex(n, 3);
        this._setMedium(n, n2);
        return this;
    }

    @Override
    public ByteBuf writeDouble(double d) {
        this.writeLong(Double.doubleToRawLongBits(d));
        return this;
    }

    @Override
    public boolean isWritable() {
        return this.capacity() > this.writerIndex;
    }

    @Override
    public int writableBytes() {
        return this.capacity() - this.writerIndex;
    }

    @Override
    public short getUnsignedByte(int n) {
        return (short)(this.getByte(n) & 0xFF);
    }

    @Override
    public ByteBuf setByte(int n, int n2) {
        this.checkIndex(n);
        this._setByte(n, n2);
        return this;
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        this.checkIndex(n, 2);
        this._setShort(n, n2);
        return this;
    }

    private int forEachByteDesc0(int n, int n2, ByteBufProcessor byteBufProcessor) {
        if (byteBufProcessor == null) {
            throw new NullPointerException("processor");
        }
        if (n2 == 0) {
            return -1;
        }
        int n3 = n + n2 - 1;
        try {
            do {
                if (byteBufProcessor.process(this._getByte(n3))) continue;
                return n3;
            } while (--n3 >= n);
        }
        catch (Exception exception) {
            PlatformDependent.throwException(exception);
        }
        return -1;
    }

    @Override
    public ByteBuf ensureWritable(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", n));
        }
        if (n <= this.writableBytes()) {
            return this;
        }
        if (n > this.maxCapacity - this.writerIndex) {
            throw new IndexOutOfBoundsException(String.format("writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s", this.writerIndex, n, this.maxCapacity, this));
        }
        int n2 = this.calculateNewCapacity(this.writerIndex + n);
        this.capacity(n2);
        return this;
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor byteBufProcessor) {
        int n = this.readerIndex;
        int n2 = this.writerIndex - n;
        this.ensureAccessible();
        return this.forEachByteDesc0(n, n2, byteBufProcessor);
    }

    @Override
    public int maxCapacity() {
        return this.maxCapacity;
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        this.readBytes(byteBuf, byteBuf.writableBytes());
        return this;
    }

    @Override
    public ByteBuf writeChar(int n) {
        this.writeShort(n);
        return this;
    }

    @Override
    public double getDouble(int n) {
        return Double.longBitsToDouble(this.getLong(n));
    }

    @Override
    public ByteBuf setDouble(int n, double d) {
        this.setLong(n, Double.doubleToRawLongBits(d));
        return this;
    }

    @Override
    public ByteBuf copy() {
        return this.copy(this.readerIndex, this.readableBytes());
    }

    @Override
    public int forEachByte(ByteBufProcessor byteBufProcessor) {
        int n = this.readerIndex;
        int n2 = this.writerIndex - n;
        this.ensureAccessible();
        return this.forEachByteAsc0(n, n2, byteBufProcessor);
    }

    @Override
    public String toString(Charset charset) {
        return this.toString(this.readerIndex, this.readableBytes(), charset);
    }

    @Override
    public ByteBuf setZero(int n, int n2) {
        int n3;
        if (n2 == 0) {
            return this;
        }
        this.checkIndex(n, n2);
        int n4 = n2 >>> 3;
        int n5 = n2 & 7;
        for (n3 = n4; n3 > 0; --n3) {
            this.setLong(n, 0L);
            n += 8;
        }
        if (n5 == 4) {
            this.setInt(n, 0);
        } else if (n5 < 4) {
            for (n3 = n5; n3 > 0; --n3) {
                this.setByte(n, 0);
                ++n;
            }
        } else {
            this.setInt(n, 0);
            n += 4;
            for (n3 = n5 - 4; n3 > 0; --n3) {
                this.setByte(n, 0);
                ++n;
            }
        }
        return this;
    }

    @Override
    public ByteBuf readerIndex(int n) {
        if (n < 0 || n > this.writerIndex) {
            throw new IndexOutOfBoundsException(String.format("readerIndex: %d (expected: 0 <= readerIndex <= writerIndex(%d))", n, this.writerIndex));
        }
        this.readerIndex = n;
        return this;
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        this.ensureAccessible();
        if (this.readerIndex == 0) {
            return this;
        }
        if (this.readerIndex == this.writerIndex) {
            this.adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
            this.writerIndex = 0;
            return this;
        }
        if (this.readerIndex >= this.capacity() >>> 1) {
            this.setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
            this.writerIndex -= this.readerIndex;
            this.adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
        }
        return this;
    }

    @Override
    public int hashCode() {
        return ByteBufUtil.hashCode(this);
    }

    @Override
    public int readMedium() {
        int n = this.readUnsignedMedium();
        if ((n & 0x800000) != 0) {
            n |= 0xFF000000;
        }
        return n;
    }

    @Override
    public int indexOf(int n, int n2, byte by) {
        return ByteBufUtil.indexOf(this, n, n2, by);
    }

    @Override
    public int getMedium(int n) {
        int n2 = this.getUnsignedMedium(n);
        if ((n2 & 0x800000) != 0) {
            n2 |= 0xFF000000;
        }
        return n2;
    }

    @Override
    public ByteBuf markReaderIndex() {
        this.markedReaderIndex = this.readerIndex;
        return this;
    }

    @Override
    public boolean isReadable() {
        return this.writerIndex > this.readerIndex;
    }

    @Override
    public int bytesBefore(int n, int n2, byte by) {
        int n3 = this.indexOf(n, n + n2, by);
        if (n3 < 0) {
            return -1;
        }
        return n3 - n;
    }

    @Override
    public ByteBuf writeFloat(float f) {
        this.writeInt(Float.floatToRawIntBits(f));
        return this;
    }

    @Override
    public ByteBuf resetWriterIndex() {
        this.writerIndex = this.markedWriterIndex;
        return this;
    }

    @Override
    public long getUnsignedInt(int n) {
        return (long)this.getInt(n) & 0xFFFFFFFFL;
    }

    @Override
    public short readUnsignedByte() {
        return (short)(this.readByte() & 0xFF);
    }

    @Override
    public boolean isWritable(int n) {
        return this.capacity() - this.writerIndex >= n;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n) {
        if (n > byteBuf.readableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", n, byteBuf.readableBytes(), byteBuf));
        }
        this.writeBytes(byteBuf, byteBuf.readerIndex(), n);
        byteBuf.readerIndex(byteBuf.readerIndex() + n);
        return this;
    }

    protected abstract int _getInt(int var1);

    @Override
    public ByteBuf resetReaderIndex() {
        this.readerIndex(this.markedReaderIndex);
        return this;
    }

    protected final void maxCapacity(int n) {
        this.maxCapacity = n;
    }

    protected abstract void _setByte(int var1, int var2);

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf) {
        this.getBytes(n, byteBuf, byteBuf.writableBytes());
        return this;
    }

    @Override
    public int readInt() {
        this.checkReadableBytes(4);
        int n = this._getInt(this.readerIndex);
        this.readerIndex += 4;
        return n;
    }

    @Override
    public short getShort(int n) {
        this.checkIndex(n, 2);
        return this._getShort(n);
    }

    @Override
    public ByteBuf markWriterIndex() {
        this.markedWriterIndex = this.writerIndex;
        return this;
    }

    protected final void adjustMarkers(int n) {
        int n2 = this.markedReaderIndex;
        if (n2 <= n) {
            this.markedReaderIndex = 0;
            int n3 = this.markedWriterIndex;
            this.markedWriterIndex = n3 <= n ? 0 : n3 - n;
        } else {
            this.markedReaderIndex = n2 - n;
            this.markedWriterIndex -= n;
        }
    }

    private int calculateNewCapacity(int n) {
        int n2;
        int n3 = this.maxCapacity;
        int n4 = 0x400000;
        if (n == 0x400000) {
            return 0x400000;
        }
        if (n > 0x400000) {
            int n5 = n / 0x400000 * 0x400000;
            n5 = n5 > n3 - 0x400000 ? n3 : (n5 += 0x400000);
            return n5;
        }
        for (n2 = 64; n2 < n; n2 <<= 1) {
        }
        return Math.min(n2, n3);
    }

    protected final void checkReadableBytes(int n) {
        this.ensureAccessible();
        if (n < 0) {
            throw new IllegalArgumentException("minimumReadableBytes: " + n + " (expected: >= 0)");
        }
        if (this.readerIndex > this.writerIndex - n) {
            throw new IndexOutOfBoundsException(String.format("readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s", this.readerIndex, n, this.writerIndex, this));
        }
    }

    @Override
    public ByteBuf readSlice(int n) {
        ByteBuf byteBuf = this.slice(this.readerIndex, n);
        this.readerIndex += n;
        return byteBuf;
    }

    @Override
    public String toString(int n, int n2, Charset charset) {
        ByteBuffer byteBuffer;
        if (n2 == 0) {
            return "";
        }
        if (this.nioBufferCount() == 1) {
            byteBuffer = this.nioBuffer(n, n2);
        } else {
            byteBuffer = ByteBuffer.allocate(n2);
            this.getBytes(n, byteBuffer);
            byteBuffer.flip();
        }
        return ByteBufUtil.decodeString(byteBuffer, charset);
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray, int n, int n2) {
        this.ensureAccessible();
        this.ensureWritable(n2);
        this.setBytes(this.writerIndex, byArray, n, n2);
        this.writerIndex += n2;
        return this;
    }

    @Override
    public int bytesBefore(byte by) {
        return this.bytesBefore(this.readerIndex(), this.readableBytes(), by);
    }

    @Override
    public long readUnsignedInt() {
        return (long)this.readInt() & 0xFFFFFFFFL;
    }

    @Override
    public ByteBuf readBytes(byte[] byArray, int n, int n2) {
        this.checkReadableBytes(n2);
        this.getBytes(this.readerIndex, byArray, n, n2);
        this.readerIndex += n2;
        return this;
    }

    @Override
    public long getLong(int n) {
        this.checkIndex(n, 8);
        return this._getLong(n);
    }

    @Override
    public int readableBytes() {
        return this.writerIndex - this.readerIndex;
    }

    private int forEachByteAsc0(int n, int n2, ByteBufProcessor byteBufProcessor) {
        if (byteBufProcessor == null) {
            throw new NullPointerException("processor");
        }
        if (n2 == 0) {
            return -1;
        }
        int n3 = n + n2;
        int n4 = n;
        try {
            do {
                if (byteBufProcessor.process(this._getByte(n4))) continue;
                return n4;
            } while (++n4 < n3);
        }
        catch (Exception exception) {
            PlatformDependent.throwException(exception);
        }
        return -1;
    }

    @Override
    public ByteBuf slice() {
        return this.slice(this.readerIndex, this.readableBytes());
    }

    @Override
    public ByteBuf setChar(int n, int n2) {
        this.setShort(n, n2);
        return this;
    }

    @Override
    public int readUnsignedShort() {
        return this.readShort() & 0xFFFF;
    }

    protected abstract void _setMedium(int var1, int var2);

    @Override
    public float getFloat(int n) {
        return Float.intBitsToFloat(this.getInt(n));
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray) {
        this.setBytes(n, byArray, 0, byArray.length);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        this.ensureAccessible();
        int n = byteBuffer.remaining();
        this.ensureWritable(n);
        this.setBytes(this.writerIndex, byteBuffer);
        this.writerIndex += n;
        return this;
    }

    @Override
    public int getInt(int n) {
        this.checkIndex(n, 4);
        return this._getInt(n);
    }

    @Override
    public ByteBuf writeMedium(int n) {
        this.ensureAccessible();
        this.ensureWritable(3);
        this._setMedium(this.writerIndex, n);
        this.writerIndex += 3;
        return this;
    }
}

