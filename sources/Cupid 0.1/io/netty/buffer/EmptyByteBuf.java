package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

public final class EmptyByteBuf extends ByteBuf {
  private static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.allocateDirect(0);
  
  private static final long EMPTY_BYTE_BUFFER_ADDRESS;
  
  private final ByteBufAllocator alloc;
  
  private final ByteOrder order;
  
  private final String str;
  
  private EmptyByteBuf swapped;
  
  static {
    long emptyByteBufferAddress = 0L;
    try {
      if (PlatformDependent.hasUnsafe())
        emptyByteBufferAddress = PlatformDependent.directBufferAddress(EMPTY_BYTE_BUFFER); 
    } catch (Throwable t) {}
    EMPTY_BYTE_BUFFER_ADDRESS = emptyByteBufferAddress;
  }
  
  public EmptyByteBuf(ByteBufAllocator alloc) {
    this(alloc, ByteOrder.BIG_ENDIAN);
  }
  
  private EmptyByteBuf(ByteBufAllocator alloc, ByteOrder order) {
    if (alloc == null)
      throw new NullPointerException("alloc"); 
    this.alloc = alloc;
    this.order = order;
    this.str = StringUtil.simpleClassName(this) + ((order == ByteOrder.BIG_ENDIAN) ? "BE" : "LE");
  }
  
  public int capacity() {
    return 0;
  }
  
  public ByteBuf capacity(int newCapacity) {
    throw new ReadOnlyBufferException();
  }
  
  public ByteBufAllocator alloc() {
    return this.alloc;
  }
  
  public ByteOrder order() {
    return this.order;
  }
  
  public ByteBuf unwrap() {
    return null;
  }
  
  public boolean isDirect() {
    return true;
  }
  
  public int maxCapacity() {
    return 0;
  }
  
  public ByteBuf order(ByteOrder endianness) {
    if (endianness == null)
      throw new NullPointerException("endianness"); 
    if (endianness == order())
      return this; 
    EmptyByteBuf swapped = this.swapped;
    if (swapped != null)
      return swapped; 
    this.swapped = swapped = new EmptyByteBuf(alloc(), endianness);
    return swapped;
  }
  
  public int readerIndex() {
    return 0;
  }
  
  public ByteBuf readerIndex(int readerIndex) {
    return checkIndex(readerIndex);
  }
  
  public int writerIndex() {
    return 0;
  }
  
  public ByteBuf writerIndex(int writerIndex) {
    return checkIndex(writerIndex);
  }
  
  public ByteBuf setIndex(int readerIndex, int writerIndex) {
    checkIndex(readerIndex);
    checkIndex(writerIndex);
    return this;
  }
  
  public int readableBytes() {
    return 0;
  }
  
  public int writableBytes() {
    return 0;
  }
  
  public int maxWritableBytes() {
    return 0;
  }
  
  public boolean isReadable() {
    return false;
  }
  
  public boolean isWritable() {
    return false;
  }
  
  public ByteBuf clear() {
    return this;
  }
  
  public ByteBuf markReaderIndex() {
    return this;
  }
  
  public ByteBuf resetReaderIndex() {
    return this;
  }
  
  public ByteBuf markWriterIndex() {
    return this;
  }
  
  public ByteBuf resetWriterIndex() {
    return this;
  }
  
  public ByteBuf discardReadBytes() {
    return this;
  }
  
  public ByteBuf discardSomeReadBytes() {
    return this;
  }
  
  public ByteBuf ensureWritable(int minWritableBytes) {
    if (minWritableBytes < 0)
      throw new IllegalArgumentException("minWritableBytes: " + minWritableBytes + " (expected: >= 0)"); 
    if (minWritableBytes != 0)
      throw new IndexOutOfBoundsException(); 
    return this;
  }
  
  public int ensureWritable(int minWritableBytes, boolean force) {
    if (minWritableBytes < 0)
      throw new IllegalArgumentException("minWritableBytes: " + minWritableBytes + " (expected: >= 0)"); 
    if (minWritableBytes == 0)
      return 0; 
    return 1;
  }
  
  public boolean getBoolean(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public byte getByte(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public short getUnsignedByte(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public short getShort(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public int getUnsignedShort(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public int getMedium(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public int getUnsignedMedium(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public int getInt(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public long getUnsignedInt(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public long getLong(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public char getChar(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public float getFloat(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public double getDouble(int index) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf getBytes(int index, ByteBuf dst) {
    return checkIndex(index, dst.writableBytes());
  }
  
  public ByteBuf getBytes(int index, ByteBuf dst, int length) {
    return checkIndex(index, length);
  }
  
  public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
    return checkIndex(index, length);
  }
  
  public ByteBuf getBytes(int index, byte[] dst) {
    return checkIndex(index, dst.length);
  }
  
  public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
    return checkIndex(index, length);
  }
  
  public ByteBuf getBytes(int index, ByteBuffer dst) {
    return checkIndex(index, dst.remaining());
  }
  
  public ByteBuf getBytes(int index, OutputStream out, int length) {
    return checkIndex(index, length);
  }
  
  public int getBytes(int index, GatheringByteChannel out, int length) {
    checkIndex(index, length);
    return 0;
  }
  
  public ByteBuf setBoolean(int index, boolean value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setByte(int index, int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setShort(int index, int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setMedium(int index, int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setInt(int index, int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setLong(int index, long value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setChar(int index, int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setFloat(int index, float value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setDouble(int index, double value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setBytes(int index, ByteBuf src) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf setBytes(int index, ByteBuf src, int length) {
    return checkIndex(index, length);
  }
  
  public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
    return checkIndex(index, length);
  }
  
  public ByteBuf setBytes(int index, byte[] src) {
    return checkIndex(index, src.length);
  }
  
  public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
    return checkIndex(index, length);
  }
  
  public ByteBuf setBytes(int index, ByteBuffer src) {
    return checkIndex(index, src.remaining());
  }
  
  public int setBytes(int index, InputStream in, int length) {
    checkIndex(index, length);
    return 0;
  }
  
  public int setBytes(int index, ScatteringByteChannel in, int length) {
    checkIndex(index, length);
    return 0;
  }
  
  public ByteBuf setZero(int index, int length) {
    return checkIndex(index, length);
  }
  
  public boolean readBoolean() {
    throw new IndexOutOfBoundsException();
  }
  
  public byte readByte() {
    throw new IndexOutOfBoundsException();
  }
  
  public short readUnsignedByte() {
    throw new IndexOutOfBoundsException();
  }
  
  public short readShort() {
    throw new IndexOutOfBoundsException();
  }
  
  public int readUnsignedShort() {
    throw new IndexOutOfBoundsException();
  }
  
  public int readMedium() {
    throw new IndexOutOfBoundsException();
  }
  
  public int readUnsignedMedium() {
    throw new IndexOutOfBoundsException();
  }
  
  public int readInt() {
    throw new IndexOutOfBoundsException();
  }
  
  public long readUnsignedInt() {
    throw new IndexOutOfBoundsException();
  }
  
  public long readLong() {
    throw new IndexOutOfBoundsException();
  }
  
  public char readChar() {
    throw new IndexOutOfBoundsException();
  }
  
  public float readFloat() {
    throw new IndexOutOfBoundsException();
  }
  
  public double readDouble() {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf readBytes(int length) {
    return checkLength(length);
  }
  
  public ByteBuf readSlice(int length) {
    return checkLength(length);
  }
  
  public ByteBuf readBytes(ByteBuf dst) {
    return checkLength(dst.writableBytes());
  }
  
  public ByteBuf readBytes(ByteBuf dst, int length) {
    return checkLength(length);
  }
  
  public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
    return checkLength(length);
  }
  
  public ByteBuf readBytes(byte[] dst) {
    return checkLength(dst.length);
  }
  
  public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
    return checkLength(length);
  }
  
  public ByteBuf readBytes(ByteBuffer dst) {
    return checkLength(dst.remaining());
  }
  
  public ByteBuf readBytes(OutputStream out, int length) {
    return checkLength(length);
  }
  
  public int readBytes(GatheringByteChannel out, int length) {
    checkLength(length);
    return 0;
  }
  
  public ByteBuf skipBytes(int length) {
    return checkLength(length);
  }
  
  public ByteBuf writeBoolean(boolean value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeByte(int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeShort(int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeMedium(int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeInt(int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeLong(long value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeChar(int value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeFloat(float value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeDouble(double value) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeBytes(ByteBuf src) {
    throw new IndexOutOfBoundsException();
  }
  
  public ByteBuf writeBytes(ByteBuf src, int length) {
    return checkLength(length);
  }
  
  public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
    return checkLength(length);
  }
  
  public ByteBuf writeBytes(byte[] src) {
    return checkLength(src.length);
  }
  
  public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
    return checkLength(length);
  }
  
  public ByteBuf writeBytes(ByteBuffer src) {
    return checkLength(src.remaining());
  }
  
  public int writeBytes(InputStream in, int length) {
    checkLength(length);
    return 0;
  }
  
  public int writeBytes(ScatteringByteChannel in, int length) {
    checkLength(length);
    return 0;
  }
  
  public ByteBuf writeZero(int length) {
    return checkLength(length);
  }
  
  public int indexOf(int fromIndex, int toIndex, byte value) {
    checkIndex(fromIndex);
    checkIndex(toIndex);
    return -1;
  }
  
  public int bytesBefore(byte value) {
    return -1;
  }
  
  public int bytesBefore(int length, byte value) {
    checkLength(length);
    return -1;
  }
  
  public int bytesBefore(int index, int length, byte value) {
    checkIndex(index, length);
    return -1;
  }
  
  public int forEachByte(ByteBufProcessor processor) {
    return -1;
  }
  
  public int forEachByte(int index, int length, ByteBufProcessor processor) {
    checkIndex(index, length);
    return -1;
  }
  
  public int forEachByteDesc(ByteBufProcessor processor) {
    return -1;
  }
  
  public int forEachByteDesc(int index, int length, ByteBufProcessor processor) {
    checkIndex(index, length);
    return -1;
  }
  
  public ByteBuf copy() {
    return this;
  }
  
  public ByteBuf copy(int index, int length) {
    return checkIndex(index, length);
  }
  
  public ByteBuf slice() {
    return this;
  }
  
  public ByteBuf slice(int index, int length) {
    return checkIndex(index, length);
  }
  
  public ByteBuf duplicate() {
    return this;
  }
  
  public int nioBufferCount() {
    return 1;
  }
  
  public ByteBuffer nioBuffer() {
    return EMPTY_BYTE_BUFFER;
  }
  
  public ByteBuffer nioBuffer(int index, int length) {
    checkIndex(index, length);
    return nioBuffer();
  }
  
  public ByteBuffer[] nioBuffers() {
    return new ByteBuffer[] { EMPTY_BYTE_BUFFER };
  }
  
  public ByteBuffer[] nioBuffers(int index, int length) {
    checkIndex(index, length);
    return nioBuffers();
  }
  
  public ByteBuffer internalNioBuffer(int index, int length) {
    return EMPTY_BYTE_BUFFER;
  }
  
  public boolean hasArray() {
    return true;
  }
  
  public byte[] array() {
    return EmptyArrays.EMPTY_BYTES;
  }
  
  public int arrayOffset() {
    return 0;
  }
  
  public boolean hasMemoryAddress() {
    return (EMPTY_BYTE_BUFFER_ADDRESS != 0L);
  }
  
  public long memoryAddress() {
    if (hasMemoryAddress())
      return EMPTY_BYTE_BUFFER_ADDRESS; 
    throw new UnsupportedOperationException();
  }
  
  public String toString(Charset charset) {
    return "";
  }
  
  public String toString(int index, int length, Charset charset) {
    checkIndex(index, length);
    return toString(charset);
  }
  
  public int hashCode() {
    return 0;
  }
  
  public boolean equals(Object obj) {
    return (obj instanceof ByteBuf && !((ByteBuf)obj).isReadable());
  }
  
  public int compareTo(ByteBuf buffer) {
    return buffer.isReadable() ? -1 : 0;
  }
  
  public String toString() {
    return this.str;
  }
  
  public boolean isReadable(int size) {
    return false;
  }
  
  public boolean isWritable(int size) {
    return false;
  }
  
  public int refCnt() {
    return 1;
  }
  
  public ByteBuf retain() {
    return this;
  }
  
  public ByteBuf retain(int increment) {
    return this;
  }
  
  public boolean release() {
    return false;
  }
  
  public boolean release(int decrement) {
    return false;
  }
  
  private ByteBuf checkIndex(int index) {
    if (index != 0)
      throw new IndexOutOfBoundsException(); 
    return this;
  }
  
  private ByteBuf checkIndex(int index, int length) {
    if (length < 0)
      throw new IllegalArgumentException("length: " + length); 
    if (index != 0 || length != 0)
      throw new IndexOutOfBoundsException(); 
    return this;
  }
  
  private ByteBuf checkLength(int length) {
    if (length < 0)
      throw new IllegalArgumentException("length: " + length + " (expected: >= 0)"); 
    if (length != 0)
      throw new IndexOutOfBoundsException(); 
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\EmptyByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */