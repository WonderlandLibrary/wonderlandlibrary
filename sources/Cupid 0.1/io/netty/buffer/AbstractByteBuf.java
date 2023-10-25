package io.netty.buffer;

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

public abstract class AbstractByteBuf extends ByteBuf {
  static final ResourceLeakDetector<ByteBuf> leakDetector = new ResourceLeakDetector(ByteBuf.class);
  
  int readerIndex;
  
  int writerIndex;
  
  private int markedReaderIndex;
  
  private int markedWriterIndex;
  
  private int maxCapacity;
  
  private SwappedByteBuf swappedBuf;
  
  protected AbstractByteBuf(int maxCapacity) {
    if (maxCapacity < 0)
      throw new IllegalArgumentException("maxCapacity: " + maxCapacity + " (expected: >= 0)"); 
    this.maxCapacity = maxCapacity;
  }
  
  public int maxCapacity() {
    return this.maxCapacity;
  }
  
  protected final void maxCapacity(int maxCapacity) {
    this.maxCapacity = maxCapacity;
  }
  
  public int readerIndex() {
    return this.readerIndex;
  }
  
  public ByteBuf readerIndex(int readerIndex) {
    if (readerIndex < 0 || readerIndex > this.writerIndex)
      throw new IndexOutOfBoundsException(String.format("readerIndex: %d (expected: 0 <= readerIndex <= writerIndex(%d))", new Object[] { Integer.valueOf(readerIndex), Integer.valueOf(this.writerIndex) })); 
    this.readerIndex = readerIndex;
    return this;
  }
  
  public int writerIndex() {
    return this.writerIndex;
  }
  
  public ByteBuf writerIndex(int writerIndex) {
    if (writerIndex < this.readerIndex || writerIndex > capacity())
      throw new IndexOutOfBoundsException(String.format("writerIndex: %d (expected: readerIndex(%d) <= writerIndex <= capacity(%d))", new Object[] { Integer.valueOf(writerIndex), Integer.valueOf(this.readerIndex), Integer.valueOf(capacity()) })); 
    this.writerIndex = writerIndex;
    return this;
  }
  
  public ByteBuf setIndex(int readerIndex, int writerIndex) {
    if (readerIndex < 0 || readerIndex > writerIndex || writerIndex > capacity())
      throw new IndexOutOfBoundsException(String.format("readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))", new Object[] { Integer.valueOf(readerIndex), Integer.valueOf(writerIndex), Integer.valueOf(capacity()) })); 
    this.readerIndex = readerIndex;
    this.writerIndex = writerIndex;
    return this;
  }
  
  public ByteBuf clear() {
    this.readerIndex = this.writerIndex = 0;
    return this;
  }
  
  public boolean isReadable() {
    return (this.writerIndex > this.readerIndex);
  }
  
  public boolean isReadable(int numBytes) {
    return (this.writerIndex - this.readerIndex >= numBytes);
  }
  
  public boolean isWritable() {
    return (capacity() > this.writerIndex);
  }
  
  public boolean isWritable(int numBytes) {
    return (capacity() - this.writerIndex >= numBytes);
  }
  
  public int readableBytes() {
    return this.writerIndex - this.readerIndex;
  }
  
  public int writableBytes() {
    return capacity() - this.writerIndex;
  }
  
  public int maxWritableBytes() {
    return maxCapacity() - this.writerIndex;
  }
  
  public ByteBuf markReaderIndex() {
    this.markedReaderIndex = this.readerIndex;
    return this;
  }
  
  public ByteBuf resetReaderIndex() {
    readerIndex(this.markedReaderIndex);
    return this;
  }
  
  public ByteBuf markWriterIndex() {
    this.markedWriterIndex = this.writerIndex;
    return this;
  }
  
  public ByteBuf resetWriterIndex() {
    this.writerIndex = this.markedWriterIndex;
    return this;
  }
  
  public ByteBuf discardReadBytes() {
    ensureAccessible();
    if (this.readerIndex == 0)
      return this; 
    if (this.readerIndex != this.writerIndex) {
      setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
      this.writerIndex -= this.readerIndex;
      adjustMarkers(this.readerIndex);
      this.readerIndex = 0;
    } else {
      adjustMarkers(this.readerIndex);
      this.writerIndex = this.readerIndex = 0;
    } 
    return this;
  }
  
  public ByteBuf discardSomeReadBytes() {
    ensureAccessible();
    if (this.readerIndex == 0)
      return this; 
    if (this.readerIndex == this.writerIndex) {
      adjustMarkers(this.readerIndex);
      this.writerIndex = this.readerIndex = 0;
      return this;
    } 
    if (this.readerIndex >= capacity() >>> 1) {
      setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
      this.writerIndex -= this.readerIndex;
      adjustMarkers(this.readerIndex);
      this.readerIndex = 0;
    } 
    return this;
  }
  
  protected final void adjustMarkers(int decrement) {
    int markedReaderIndex = this.markedReaderIndex;
    if (markedReaderIndex <= decrement) {
      this.markedReaderIndex = 0;
      int markedWriterIndex = this.markedWriterIndex;
      if (markedWriterIndex <= decrement) {
        this.markedWriterIndex = 0;
      } else {
        this.markedWriterIndex = markedWriterIndex - decrement;
      } 
    } else {
      this.markedReaderIndex = markedReaderIndex - decrement;
      this.markedWriterIndex -= decrement;
    } 
  }
  
  public ByteBuf ensureWritable(int minWritableBytes) {
    if (minWritableBytes < 0)
      throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[] { Integer.valueOf(minWritableBytes) })); 
    if (minWritableBytes <= writableBytes())
      return this; 
    if (minWritableBytes > this.maxCapacity - this.writerIndex)
      throw new IndexOutOfBoundsException(String.format("writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s", new Object[] { Integer.valueOf(this.writerIndex), Integer.valueOf(minWritableBytes), Integer.valueOf(this.maxCapacity), this })); 
    int newCapacity = calculateNewCapacity(this.writerIndex + minWritableBytes);
    capacity(newCapacity);
    return this;
  }
  
  public int ensureWritable(int minWritableBytes, boolean force) {
    if (minWritableBytes < 0)
      throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[] { Integer.valueOf(minWritableBytes) })); 
    if (minWritableBytes <= writableBytes())
      return 0; 
    if (minWritableBytes > this.maxCapacity - this.writerIndex && 
      force) {
      if (capacity() == maxCapacity())
        return 1; 
      capacity(maxCapacity());
      return 3;
    } 
    int newCapacity = calculateNewCapacity(this.writerIndex + minWritableBytes);
    capacity(newCapacity);
    return 2;
  }
  
  private int calculateNewCapacity(int minNewCapacity) {
    int maxCapacity = this.maxCapacity;
    int threshold = 4194304;
    if (minNewCapacity == 4194304)
      return 4194304; 
    if (minNewCapacity > 4194304) {
      int i = minNewCapacity / 4194304 * 4194304;
      if (i > maxCapacity - 4194304) {
        i = maxCapacity;
      } else {
        i += 4194304;
      } 
      return i;
    } 
    int newCapacity = 64;
    while (newCapacity < minNewCapacity)
      newCapacity <<= 1; 
    return Math.min(newCapacity, maxCapacity);
  }
  
  public ByteBuf order(ByteOrder endianness) {
    if (endianness == null)
      throw new NullPointerException("endianness"); 
    if (endianness == order())
      return this; 
    SwappedByteBuf swappedBuf = this.swappedBuf;
    if (swappedBuf == null)
      this.swappedBuf = swappedBuf = newSwappedByteBuf(); 
    return swappedBuf;
  }
  
  protected SwappedByteBuf newSwappedByteBuf() {
    return new SwappedByteBuf(this);
  }
  
  public byte getByte(int index) {
    checkIndex(index);
    return _getByte(index);
  }
  
  public boolean getBoolean(int index) {
    return (getByte(index) != 0);
  }
  
  public short getUnsignedByte(int index) {
    return (short)(getByte(index) & 0xFF);
  }
  
  public short getShort(int index) {
    checkIndex(index, 2);
    return _getShort(index);
  }
  
  public int getUnsignedShort(int index) {
    return getShort(index) & 0xFFFF;
  }
  
  public int getUnsignedMedium(int index) {
    checkIndex(index, 3);
    return _getUnsignedMedium(index);
  }
  
  public int getMedium(int index) {
    int value = getUnsignedMedium(index);
    if ((value & 0x800000) != 0)
      value |= 0xFF000000; 
    return value;
  }
  
  public int getInt(int index) {
    checkIndex(index, 4);
    return _getInt(index);
  }
  
  public long getUnsignedInt(int index) {
    return getInt(index) & 0xFFFFFFFFL;
  }
  
  public long getLong(int index) {
    checkIndex(index, 8);
    return _getLong(index);
  }
  
  public char getChar(int index) {
    return (char)getShort(index);
  }
  
  public float getFloat(int index) {
    return Float.intBitsToFloat(getInt(index));
  }
  
  public double getDouble(int index) {
    return Double.longBitsToDouble(getLong(index));
  }
  
  public ByteBuf getBytes(int index, byte[] dst) {
    getBytes(index, dst, 0, dst.length);
    return this;
  }
  
  public ByteBuf getBytes(int index, ByteBuf dst) {
    getBytes(index, dst, dst.writableBytes());
    return this;
  }
  
  public ByteBuf getBytes(int index, ByteBuf dst, int length) {
    getBytes(index, dst, dst.writerIndex(), length);
    dst.writerIndex(dst.writerIndex() + length);
    return this;
  }
  
  public ByteBuf setByte(int index, int value) {
    checkIndex(index);
    _setByte(index, value);
    return this;
  }
  
  public ByteBuf setBoolean(int index, boolean value) {
    setByte(index, value ? 1 : 0);
    return this;
  }
  
  public ByteBuf setShort(int index, int value) {
    checkIndex(index, 2);
    _setShort(index, value);
    return this;
  }
  
  public ByteBuf setChar(int index, int value) {
    setShort(index, value);
    return this;
  }
  
  public ByteBuf setMedium(int index, int value) {
    checkIndex(index, 3);
    _setMedium(index, value);
    return this;
  }
  
  public ByteBuf setInt(int index, int value) {
    checkIndex(index, 4);
    _setInt(index, value);
    return this;
  }
  
  public ByteBuf setFloat(int index, float value) {
    setInt(index, Float.floatToRawIntBits(value));
    return this;
  }
  
  public ByteBuf setLong(int index, long value) {
    checkIndex(index, 8);
    _setLong(index, value);
    return this;
  }
  
  public ByteBuf setDouble(int index, double value) {
    setLong(index, Double.doubleToRawLongBits(value));
    return this;
  }
  
  public ByteBuf setBytes(int index, byte[] src) {
    setBytes(index, src, 0, src.length);
    return this;
  }
  
  public ByteBuf setBytes(int index, ByteBuf src) {
    setBytes(index, src, src.readableBytes());
    return this;
  }
  
  public ByteBuf setBytes(int index, ByteBuf src, int length) {
    checkIndex(index, length);
    if (src == null)
      throw new NullPointerException("src"); 
    if (length > src.readableBytes())
      throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", new Object[] { Integer.valueOf(length), Integer.valueOf(src.readableBytes()), src })); 
    setBytes(index, src, src.readerIndex(), length);
    src.readerIndex(src.readerIndex() + length);
    return this;
  }
  
  public ByteBuf setZero(int index, int length) {
    if (length == 0)
      return this; 
    checkIndex(index, length);
    int nLong = length >>> 3;
    int nBytes = length & 0x7;
    int i;
    for (i = nLong; i > 0; i--) {
      setLong(index, 0L);
      index += 8;
    } 
    if (nBytes == 4) {
      setInt(index, 0);
    } else if (nBytes < 4) {
      for (i = nBytes; i > 0; i--) {
        setByte(index, 0);
        index++;
      } 
    } else {
      setInt(index, 0);
      index += 4;
      for (i = nBytes - 4; i > 0; i--) {
        setByte(index, 0);
        index++;
      } 
    } 
    return this;
  }
  
  public byte readByte() {
    checkReadableBytes(1);
    int i = this.readerIndex;
    byte b = getByte(i);
    this.readerIndex = i + 1;
    return b;
  }
  
  public boolean readBoolean() {
    return (readByte() != 0);
  }
  
  public short readUnsignedByte() {
    return (short)(readByte() & 0xFF);
  }
  
  public short readShort() {
    checkReadableBytes(2);
    short v = _getShort(this.readerIndex);
    this.readerIndex += 2;
    return v;
  }
  
  public int readUnsignedShort() {
    return readShort() & 0xFFFF;
  }
  
  public int readMedium() {
    int value = readUnsignedMedium();
    if ((value & 0x800000) != 0)
      value |= 0xFF000000; 
    return value;
  }
  
  public int readUnsignedMedium() {
    checkReadableBytes(3);
    int v = _getUnsignedMedium(this.readerIndex);
    this.readerIndex += 3;
    return v;
  }
  
  public int readInt() {
    checkReadableBytes(4);
    int v = _getInt(this.readerIndex);
    this.readerIndex += 4;
    return v;
  }
  
  public long readUnsignedInt() {
    return readInt() & 0xFFFFFFFFL;
  }
  
  public long readLong() {
    checkReadableBytes(8);
    long v = _getLong(this.readerIndex);
    this.readerIndex += 8;
    return v;
  }
  
  public char readChar() {
    return (char)readShort();
  }
  
  public float readFloat() {
    return Float.intBitsToFloat(readInt());
  }
  
  public double readDouble() {
    return Double.longBitsToDouble(readLong());
  }
  
  public ByteBuf readBytes(int length) {
    checkReadableBytes(length);
    if (length == 0)
      return Unpooled.EMPTY_BUFFER; 
    ByteBuf buf = Unpooled.buffer(length, this.maxCapacity);
    buf.writeBytes(this, this.readerIndex, length);
    this.readerIndex += length;
    return buf;
  }
  
  public ByteBuf readSlice(int length) {
    ByteBuf slice = slice(this.readerIndex, length);
    this.readerIndex += length;
    return slice;
  }
  
  public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
    checkReadableBytes(length);
    getBytes(this.readerIndex, dst, dstIndex, length);
    this.readerIndex += length;
    return this;
  }
  
  public ByteBuf readBytes(byte[] dst) {
    readBytes(dst, 0, dst.length);
    return this;
  }
  
  public ByteBuf readBytes(ByteBuf dst) {
    readBytes(dst, dst.writableBytes());
    return this;
  }
  
  public ByteBuf readBytes(ByteBuf dst, int length) {
    if (length > dst.writableBytes())
      throw new IndexOutOfBoundsException(String.format("length(%d) exceeds dst.writableBytes(%d) where dst is: %s", new Object[] { Integer.valueOf(length), Integer.valueOf(dst.writableBytes()), dst })); 
    readBytes(dst, dst.writerIndex(), length);
    dst.writerIndex(dst.writerIndex() + length);
    return this;
  }
  
  public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
    checkReadableBytes(length);
    getBytes(this.readerIndex, dst, dstIndex, length);
    this.readerIndex += length;
    return this;
  }
  
  public ByteBuf readBytes(ByteBuffer dst) {
    int length = dst.remaining();
    checkReadableBytes(length);
    getBytes(this.readerIndex, dst);
    this.readerIndex += length;
    return this;
  }
  
  public int readBytes(GatheringByteChannel out, int length) throws IOException {
    checkReadableBytes(length);
    int readBytes = getBytes(this.readerIndex, out, length);
    this.readerIndex += readBytes;
    return readBytes;
  }
  
  public ByteBuf readBytes(OutputStream out, int length) throws IOException {
    checkReadableBytes(length);
    getBytes(this.readerIndex, out, length);
    this.readerIndex += length;
    return this;
  }
  
  public ByteBuf skipBytes(int length) {
    checkReadableBytes(length);
    this.readerIndex += length;
    return this;
  }
  
  public ByteBuf writeBoolean(boolean value) {
    writeByte(value ? 1 : 0);
    return this;
  }
  
  public ByteBuf writeByte(int value) {
    ensureAccessible();
    ensureWritable(1);
    _setByte(this.writerIndex++, value);
    return this;
  }
  
  public ByteBuf writeShort(int value) {
    ensureAccessible();
    ensureWritable(2);
    _setShort(this.writerIndex, value);
    this.writerIndex += 2;
    return this;
  }
  
  public ByteBuf writeMedium(int value) {
    ensureAccessible();
    ensureWritable(3);
    _setMedium(this.writerIndex, value);
    this.writerIndex += 3;
    return this;
  }
  
  public ByteBuf writeInt(int value) {
    ensureAccessible();
    ensureWritable(4);
    _setInt(this.writerIndex, value);
    this.writerIndex += 4;
    return this;
  }
  
  public ByteBuf writeLong(long value) {
    ensureAccessible();
    ensureWritable(8);
    _setLong(this.writerIndex, value);
    this.writerIndex += 8;
    return this;
  }
  
  public ByteBuf writeChar(int value) {
    writeShort(value);
    return this;
  }
  
  public ByteBuf writeFloat(float value) {
    writeInt(Float.floatToRawIntBits(value));
    return this;
  }
  
  public ByteBuf writeDouble(double value) {
    writeLong(Double.doubleToRawLongBits(value));
    return this;
  }
  
  public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
    ensureAccessible();
    ensureWritable(length);
    setBytes(this.writerIndex, src, srcIndex, length);
    this.writerIndex += length;
    return this;
  }
  
  public ByteBuf writeBytes(byte[] src) {
    writeBytes(src, 0, src.length);
    return this;
  }
  
  public ByteBuf writeBytes(ByteBuf src) {
    writeBytes(src, src.readableBytes());
    return this;
  }
  
  public ByteBuf writeBytes(ByteBuf src, int length) {
    if (length > src.readableBytes())
      throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", new Object[] { Integer.valueOf(length), Integer.valueOf(src.readableBytes()), src })); 
    writeBytes(src, src.readerIndex(), length);
    src.readerIndex(src.readerIndex() + length);
    return this;
  }
  
  public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
    ensureAccessible();
    ensureWritable(length);
    setBytes(this.writerIndex, src, srcIndex, length);
    this.writerIndex += length;
    return this;
  }
  
  public ByteBuf writeBytes(ByteBuffer src) {
    ensureAccessible();
    int length = src.remaining();
    ensureWritable(length);
    setBytes(this.writerIndex, src);
    this.writerIndex += length;
    return this;
  }
  
  public int writeBytes(InputStream in, int length) throws IOException {
    ensureAccessible();
    ensureWritable(length);
    int writtenBytes = setBytes(this.writerIndex, in, length);
    if (writtenBytes > 0)
      this.writerIndex += writtenBytes; 
    return writtenBytes;
  }
  
  public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
    ensureAccessible();
    ensureWritable(length);
    int writtenBytes = setBytes(this.writerIndex, in, length);
    if (writtenBytes > 0)
      this.writerIndex += writtenBytes; 
    return writtenBytes;
  }
  
  public ByteBuf writeZero(int length) {
    if (length == 0)
      return this; 
    ensureWritable(length);
    checkIndex(this.writerIndex, length);
    int nLong = length >>> 3;
    int nBytes = length & 0x7;
    int i;
    for (i = nLong; i > 0; i--)
      writeLong(0L); 
    if (nBytes == 4) {
      writeInt(0);
    } else if (nBytes < 4) {
      for (i = nBytes; i > 0; i--)
        writeByte(0); 
    } else {
      writeInt(0);
      for (i = nBytes - 4; i > 0; i--)
        writeByte(0); 
    } 
    return this;
  }
  
  public ByteBuf copy() {
    return copy(this.readerIndex, readableBytes());
  }
  
  public ByteBuf duplicate() {
    return new DuplicatedByteBuf(this);
  }
  
  public ByteBuf slice() {
    return slice(this.readerIndex, readableBytes());
  }
  
  public ByteBuf slice(int index, int length) {
    if (length == 0)
      return Unpooled.EMPTY_BUFFER; 
    return new SlicedByteBuf(this, index, length);
  }
  
  public ByteBuffer nioBuffer() {
    return nioBuffer(this.readerIndex, readableBytes());
  }
  
  public ByteBuffer[] nioBuffers() {
    return nioBuffers(this.readerIndex, readableBytes());
  }
  
  public String toString(Charset charset) {
    return toString(this.readerIndex, readableBytes(), charset);
  }
  
  public String toString(int index, int length, Charset charset) {
    ByteBuffer nioBuffer;
    if (length == 0)
      return ""; 
    if (nioBufferCount() == 1) {
      nioBuffer = nioBuffer(index, length);
    } else {
      nioBuffer = ByteBuffer.allocate(length);
      getBytes(index, nioBuffer);
      nioBuffer.flip();
    } 
    return ByteBufUtil.decodeString(nioBuffer, charset);
  }
  
  public int indexOf(int fromIndex, int toIndex, byte value) {
    return ByteBufUtil.indexOf(this, fromIndex, toIndex, value);
  }
  
  public int bytesBefore(byte value) {
    return bytesBefore(readerIndex(), readableBytes(), value);
  }
  
  public int bytesBefore(int length, byte value) {
    checkReadableBytes(length);
    return bytesBefore(readerIndex(), length, value);
  }
  
  public int bytesBefore(int index, int length, byte value) {
    int endIndex = indexOf(index, index + length, value);
    if (endIndex < 0)
      return -1; 
    return endIndex - index;
  }
  
  public int forEachByte(ByteBufProcessor processor) {
    int index = this.readerIndex;
    int length = this.writerIndex - index;
    ensureAccessible();
    return forEachByteAsc0(index, length, processor);
  }
  
  public int forEachByte(int index, int length, ByteBufProcessor processor) {
    checkIndex(index, length);
    return forEachByteAsc0(index, length, processor);
  }
  
  private int forEachByteAsc0(int index, int length, ByteBufProcessor processor) {
    if (processor == null)
      throw new NullPointerException("processor"); 
    if (length == 0)
      return -1; 
    int endIndex = index + length;
    int i = index;
    try {
      do {
        if (processor.process(_getByte(i))) {
          i++;
        } else {
          return i;
        } 
      } while (i < endIndex);
    } catch (Exception e) {
      PlatformDependent.throwException(e);
    } 
    return -1;
  }
  
  public int forEachByteDesc(ByteBufProcessor processor) {
    int index = this.readerIndex;
    int length = this.writerIndex - index;
    ensureAccessible();
    return forEachByteDesc0(index, length, processor);
  }
  
  public int forEachByteDesc(int index, int length, ByteBufProcessor processor) {
    checkIndex(index, length);
    return forEachByteDesc0(index, length, processor);
  }
  
  private int forEachByteDesc0(int index, int length, ByteBufProcessor processor) {
    if (processor == null)
      throw new NullPointerException("processor"); 
    if (length == 0)
      return -1; 
    int i = index + length - 1;
    try {
      do {
        if (processor.process(_getByte(i))) {
          i--;
        } else {
          return i;
        } 
      } while (i >= index);
    } catch (Exception e) {
      PlatformDependent.throwException(e);
    } 
    return -1;
  }
  
  public int hashCode() {
    return ByteBufUtil.hashCode(this);
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o instanceof ByteBuf)
      return ByteBufUtil.equals(this, (ByteBuf)o); 
    return false;
  }
  
  public int compareTo(ByteBuf that) {
    return ByteBufUtil.compare(this, that);
  }
  
  public String toString() {
    if (refCnt() == 0)
      return StringUtil.simpleClassName(this) + "(freed)"; 
    StringBuilder buf = new StringBuilder();
    buf.append(StringUtil.simpleClassName(this));
    buf.append("(ridx: ");
    buf.append(this.readerIndex);
    buf.append(", widx: ");
    buf.append(this.writerIndex);
    buf.append(", cap: ");
    buf.append(capacity());
    if (this.maxCapacity != Integer.MAX_VALUE) {
      buf.append('/');
      buf.append(this.maxCapacity);
    } 
    ByteBuf unwrapped = unwrap();
    if (unwrapped != null) {
      buf.append(", unwrapped: ");
      buf.append(unwrapped);
    } 
    buf.append(')');
    return buf.toString();
  }
  
  protected final void checkIndex(int index) {
    ensureAccessible();
    if (index < 0 || index >= capacity())
      throw new IndexOutOfBoundsException(String.format("index: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(index), Integer.valueOf(capacity()) })); 
  }
  
  protected final void checkIndex(int index, int fieldLength) {
    ensureAccessible();
    if (fieldLength < 0)
      throw new IllegalArgumentException("length: " + fieldLength + " (expected: >= 0)"); 
    if (index < 0 || index > capacity() - fieldLength)
      throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(index), Integer.valueOf(fieldLength), Integer.valueOf(capacity()) })); 
  }
  
  protected final void checkSrcIndex(int index, int length, int srcIndex, int srcCapacity) {
    checkIndex(index, length);
    if (srcIndex < 0 || srcIndex > srcCapacity - length)
      throw new IndexOutOfBoundsException(String.format("srcIndex: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(srcIndex), Integer.valueOf(length), Integer.valueOf(srcCapacity) })); 
  }
  
  protected final void checkDstIndex(int index, int length, int dstIndex, int dstCapacity) {
    checkIndex(index, length);
    if (dstIndex < 0 || dstIndex > dstCapacity - length)
      throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dstCapacity) })); 
  }
  
  protected final void checkReadableBytes(int minimumReadableBytes) {
    ensureAccessible();
    if (minimumReadableBytes < 0)
      throw new IllegalArgumentException("minimumReadableBytes: " + minimumReadableBytes + " (expected: >= 0)"); 
    if (this.readerIndex > this.writerIndex - minimumReadableBytes)
      throw new IndexOutOfBoundsException(String.format("readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s", new Object[] { Integer.valueOf(this.readerIndex), Integer.valueOf(minimumReadableBytes), Integer.valueOf(this.writerIndex), this })); 
  }
  
  protected final void ensureAccessible() {
    if (refCnt() == 0)
      throw new IllegalReferenceCountException(0); 
  }
  
  protected abstract byte _getByte(int paramInt);
  
  protected abstract short _getShort(int paramInt);
  
  protected abstract int _getUnsignedMedium(int paramInt);
  
  protected abstract int _getInt(int paramInt);
  
  protected abstract long _getLong(int paramInt);
  
  protected abstract void _setByte(int paramInt1, int paramInt2);
  
  protected abstract void _setShort(int paramInt1, int paramInt2);
  
  protected abstract void _setMedium(int paramInt1, int paramInt2);
  
  protected abstract void _setInt(int paramInt1, int paramInt2);
  
  protected abstract void _setLong(int paramInt, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\AbstractByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */