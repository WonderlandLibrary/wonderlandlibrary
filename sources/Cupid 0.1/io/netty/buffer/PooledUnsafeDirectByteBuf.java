package io.netty.buffer;

import io.netty.util.Recycler;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

final class PooledUnsafeDirectByteBuf extends PooledByteBuf<ByteBuffer> {
  private static final boolean NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
  
  private static final Recycler<PooledUnsafeDirectByteBuf> RECYCLER = new Recycler<PooledUnsafeDirectByteBuf>() {
      protected PooledUnsafeDirectByteBuf newObject(Recycler.Handle handle) {
        return new PooledUnsafeDirectByteBuf(handle, 0);
      }
    };
  
  private long memoryAddress;
  
  static PooledUnsafeDirectByteBuf newInstance(int maxCapacity) {
    PooledUnsafeDirectByteBuf buf = (PooledUnsafeDirectByteBuf)RECYCLER.get();
    buf.setRefCnt(1);
    buf.maxCapacity(maxCapacity);
    return buf;
  }
  
  private PooledUnsafeDirectByteBuf(Recycler.Handle recyclerHandle, int maxCapacity) {
    super(recyclerHandle, maxCapacity);
  }
  
  void init(PoolChunk<ByteBuffer> chunk, long handle, int offset, int length, int maxLength) {
    super.init(chunk, handle, offset, length, maxLength);
    initMemoryAddress();
  }
  
  void initUnpooled(PoolChunk<ByteBuffer> chunk, int length) {
    super.initUnpooled(chunk, length);
    initMemoryAddress();
  }
  
  private void initMemoryAddress() {
    this.memoryAddress = PlatformDependent.directBufferAddress(this.memory) + this.offset;
  }
  
  protected ByteBuffer newInternalNioBuffer(ByteBuffer memory) {
    return memory.duplicate();
  }
  
  public boolean isDirect() {
    return true;
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
    if (length != 0)
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
      throw new IndexOutOfBoundsException("dstIndex: " + dstIndex); 
    if (length != 0)
      PlatformDependent.copyMemory(addr(index), dst, dstIndex, length); 
    return this;
  }
  
  public ByteBuf getBytes(int index, ByteBuffer dst) {
    getBytes(index, dst, false);
    return this;
  }
  
  private void getBytes(int index, ByteBuffer dst, boolean internal) {
    ByteBuffer tmpBuf;
    checkIndex(index);
    int bytesToCopy = Math.min(capacity() - index, dst.remaining());
    if (internal) {
      tmpBuf = internalNioBuffer();
    } else {
      tmpBuf = this.memory.duplicate();
    } 
    index = idx(index);
    tmpBuf.clear().position(index).limit(index + bytesToCopy);
    dst.put(tmpBuf);
  }
  
  public ByteBuf readBytes(ByteBuffer dst) {
    int length = dst.remaining();
    checkReadableBytes(length);
    getBytes(this.readerIndex, dst, true);
    this.readerIndex += length;
    return this;
  }
  
  public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
    checkIndex(index, length);
    if (length != 0) {
      byte[] tmp = new byte[length];
      PlatformDependent.copyMemory(addr(index), tmp, 0, length);
      out.write(tmp);
    } 
    return this;
  }
  
  public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
    return getBytes(index, out, length, false);
  }
  
  private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
    ByteBuffer tmpBuf;
    checkIndex(index, length);
    if (length == 0)
      return 0; 
    if (internal) {
      tmpBuf = internalNioBuffer();
    } else {
      tmpBuf = this.memory.duplicate();
    } 
    index = idx(index);
    tmpBuf.clear().position(index).limit(index + length);
    return out.write(tmpBuf);
  }
  
  public int readBytes(GatheringByteChannel out, int length) throws IOException {
    checkReadableBytes(length);
    int readBytes = getBytes(this.readerIndex, out, length, true);
    this.readerIndex += readBytes;
    return readBytes;
  }
  
  protected void _setByte(int index, int value) {
    PlatformDependent.putByte(addr(index), (byte)value);
  }
  
  protected void _setShort(int index, int value) {
    PlatformDependent.putShort(addr(index), NATIVE_ORDER ? (short)value : Short.reverseBytes((short)value));
  }
  
  protected void _setMedium(int index, int value) {
    long addr = addr(index);
    PlatformDependent.putByte(addr, (byte)(value >>> 16));
    PlatformDependent.putByte(addr + 1L, (byte)(value >>> 8));
    PlatformDependent.putByte(addr + 2L, (byte)value);
  }
  
  protected void _setInt(int index, int value) {
    PlatformDependent.putInt(addr(index), NATIVE_ORDER ? value : Integer.reverseBytes(value));
  }
  
  protected void _setLong(int index, long value) {
    PlatformDependent.putLong(addr(index), NATIVE_ORDER ? value : Long.reverseBytes(value));
  }
  
  public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
    checkIndex(index, length);
    if (src == null)
      throw new NullPointerException("src"); 
    if (srcIndex < 0 || srcIndex > src.capacity() - length)
      throw new IndexOutOfBoundsException("srcIndex: " + srcIndex); 
    if (length != 0)
      if (src.hasMemoryAddress()) {
        PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, addr(index), length);
      } else if (src.hasArray()) {
        PlatformDependent.copyMemory(src.array(), src.arrayOffset() + srcIndex, addr(index), length);
      } else {
        src.getBytes(srcIndex, this, index, length);
      }  
    return this;
  }
  
  public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
    checkIndex(index, length);
    if (length != 0)
      PlatformDependent.copyMemory(src, srcIndex, addr(index), length); 
    return this;
  }
  
  public ByteBuf setBytes(int index, ByteBuffer src) {
    checkIndex(index, src.remaining());
    ByteBuffer tmpBuf = internalNioBuffer();
    if (src == tmpBuf)
      src = src.duplicate(); 
    index = idx(index);
    tmpBuf.clear().position(index).limit(index + src.remaining());
    tmpBuf.put(src);
    return this;
  }
  
  public int setBytes(int index, InputStream in, int length) throws IOException {
    checkIndex(index, length);
    byte[] tmp = new byte[length];
    int readBytes = in.read(tmp);
    if (readBytes > 0)
      PlatformDependent.copyMemory(tmp, 0, addr(index), readBytes); 
    return readBytes;
  }
  
  public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
    checkIndex(index, length);
    ByteBuffer tmpBuf = internalNioBuffer();
    index = idx(index);
    tmpBuf.clear().position(index).limit(index + length);
    try {
      return in.read(tmpBuf);
    } catch (ClosedChannelException ignored) {
      return -1;
    } 
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
  
  public int nioBufferCount() {
    return 1;
  }
  
  public ByteBuffer[] nioBuffers(int index, int length) {
    return new ByteBuffer[] { nioBuffer(index, length) };
  }
  
  public ByteBuffer nioBuffer(int index, int length) {
    checkIndex(index, length);
    index = idx(index);
    return ((ByteBuffer)this.memory.duplicate().position(index).limit(index + length)).slice();
  }
  
  public ByteBuffer internalNioBuffer(int index, int length) {
    checkIndex(index, length);
    index = idx(index);
    return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
  }
  
  public boolean hasArray() {
    return false;
  }
  
  public byte[] array() {
    throw new UnsupportedOperationException("direct buffer");
  }
  
  public int arrayOffset() {
    throw new UnsupportedOperationException("direct buffer");
  }
  
  public boolean hasMemoryAddress() {
    return true;
  }
  
  public long memoryAddress() {
    return this.memoryAddress;
  }
  
  private long addr(int index) {
    return this.memoryAddress + index;
  }
  
  protected Recycler<?> recycler() {
    return RECYCLER;
  }
  
  protected SwappedByteBuf newSwappedByteBuf() {
    return new UnsafeDirectSwappedByteBuf(this);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\PooledUnsafeDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */