package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class ReadOnlyUnsafeDirectByteBuf extends ReadOnlyByteBufferBuf {
  private static final boolean NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
  
  private final long memoryAddress;
  
  ReadOnlyUnsafeDirectByteBuf(ByteBufAllocator allocator, ByteBuffer buffer) {
    super(allocator, buffer);
    this.memoryAddress = PlatformDependent.directBufferAddress(buffer);
  }
  
  protected byte _getByte(int index) {
    return PlatformDependent.getByte(addr(index));
  }
  
  protected short _getShort(int index) {
    short v = PlatformDependent.getShort(addr(index));
    return NATIVE_ORDER ? v : Short.reverseBytes(v);
  }
  
  protected int _getUnsignedMedium(int index) {
    long addr = addr(index);
    return (PlatformDependent.getByte(addr) & 0xFF) << 16 | (PlatformDependent.getByte(addr + 1L) & 0xFF) << 8 | PlatformDependent.getByte(addr + 2L) & 0xFF;
  }
  
  protected int _getInt(int index) {
    int v = PlatformDependent.getInt(addr(index));
    return NATIVE_ORDER ? v : Integer.reverseBytes(v);
  }
  
  protected long _getLong(int index) {
    long v = PlatformDependent.getLong(addr(index));
    return NATIVE_ORDER ? v : Long.reverseBytes(v);
  }
  
  public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
    checkIndex(index, length);
    if (dst == null)
      throw new NullPointerException("dst"); 
    if (dstIndex < 0 || dstIndex > dst.capacity() - length)
      throw new IndexOutOfBoundsException("dstIndex: " + dstIndex); 
    if (dst.hasMemoryAddress()) {
      PlatformDependent.copyMemory(addr(index), dst.memoryAddress() + dstIndex, length);
    } else if (dst.hasArray()) {
      PlatformDependent.copyMemory(addr(index), dst.array(), dst.arrayOffset() + dstIndex, length);
    } else {
      dst.setBytes(dstIndex, this, index, length);
    } 
    return this;
  }
  
  public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
    checkIndex(index, length);
    if (dst == null)
      throw new NullPointerException("dst"); 
    if (dstIndex < 0 || dstIndex > dst.length - length)
      throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dst.length) })); 
    if (length != 0)
      PlatformDependent.copyMemory(addr(index), dst, dstIndex, length); 
    return this;
  }
  
  public ByteBuf getBytes(int index, ByteBuffer dst) {
    checkIndex(index);
    if (dst == null)
      throw new NullPointerException("dst"); 
    int bytesToCopy = Math.min(capacity() - index, dst.remaining());
    ByteBuffer tmpBuf = internalNioBuffer();
    tmpBuf.clear().position(index).limit(index + bytesToCopy);
    dst.put(tmpBuf);
    return this;
  }
  
  public ByteBuf copy(int index, int length) {
    checkIndex(index, length);
    ByteBuf copy = alloc().directBuffer(length, maxCapacity());
    if (length != 0)
      if (copy.hasMemoryAddress()) {
        PlatformDependent.copyMemory(addr(index), copy.memoryAddress(), length);
        copy.setIndex(0, length);
      } else {
        copy.writeBytes(this, index, length);
      }  
    return copy;
  }
  
  private long addr(int index) {
    return this.memoryAddress + index;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\ReadOnlyUnsafeDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */