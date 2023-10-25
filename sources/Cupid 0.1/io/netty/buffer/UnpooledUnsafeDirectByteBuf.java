package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class UnpooledUnsafeDirectByteBuf extends AbstractReferenceCountedByteBuf {
  private static final boolean NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
  
  private final ByteBufAllocator alloc;
  
  private long memoryAddress;
  
  private ByteBuffer buffer;
  
  private ByteBuffer tmpNioBuf;
  
  private int capacity;
  
  private boolean doNotFree;
  
  protected UnpooledUnsafeDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
    super(maxCapacity);
    if (alloc == null)
      throw new NullPointerException("alloc"); 
    if (initialCapacity < 0)
      throw new IllegalArgumentException("initialCapacity: " + initialCapacity); 
    if (maxCapacity < 0)
      throw new IllegalArgumentException("maxCapacity: " + maxCapacity); 
    if (initialCapacity > maxCapacity)
      throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] { Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity) })); 
    this.alloc = alloc;
    setByteBuffer(allocateDirect(initialCapacity));
  }
  
  protected UnpooledUnsafeDirectByteBuf(ByteBufAllocator alloc, ByteBuffer initialBuffer, int maxCapacity) {
    super(maxCapacity);
    if (alloc == null)
      throw new NullPointerException("alloc"); 
    if (initialBuffer == null)
      throw new NullPointerException("initialBuffer"); 
    if (!initialBuffer.isDirect())
      throw new IllegalArgumentException("initialBuffer is not a direct buffer."); 
    if (initialBuffer.isReadOnly())
      throw new IllegalArgumentException("initialBuffer is a read-only buffer."); 
    int initialCapacity = initialBuffer.remaining();
    if (initialCapacity > maxCapacity)
      throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] { Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity) })); 
    this.alloc = alloc;
    this.doNotFree = true;
    setByteBuffer(initialBuffer.slice().order(ByteOrder.BIG_ENDIAN));
    writerIndex(initialCapacity);
  }
  
  protected ByteBuffer allocateDirect(int initialCapacity) {
    return ByteBuffer.allocateDirect(initialCapacity);
  }
  
  protected void freeDirect(ByteBuffer buffer) {
    PlatformDependent.freeDirectBuffer(buffer);
  }
  
  private void setByteBuffer(ByteBuffer buffer) {
    ByteBuffer oldBuffer = this.buffer;
    if (oldBuffer != null)
      if (this.doNotFree) {
        this.doNotFree = false;
      } else {
        freeDirect(oldBuffer);
      }  
    this.buffer = buffer;
    this.memoryAddress = PlatformDependent.directBufferAddress(buffer);
    this.tmpNioBuf = null;
    this.capacity = buffer.remaining();
  }
  
  public boolean isDirect() {
    return true;
  }
  
  public int capacity() {
    return this.capacity;
  }
  
  public ByteBuf capacity(int newCapacity) {
    ensureAccessible();
    if (newCapacity < 0 || newCapacity > maxCapacity())
      throw new IllegalArgumentException("newCapacity: " + newCapacity); 
    int readerIndex = readerIndex();
    int writerIndex = writerIndex();
    int oldCapacity = this.capacity;
    if (newCapacity > oldCapacity) {
      ByteBuffer oldBuffer = this.buffer;
      ByteBuffer newBuffer = allocateDirect(newCapacity);
      oldBuffer.position(0).limit(oldBuffer.capacity());
      newBuffer.position(0).limit(oldBuffer.capacity());
      newBuffer.put(oldBuffer);
      newBuffer.clear();
      setByteBuffer(newBuffer);
    } else if (newCapacity < oldCapacity) {
      ByteBuffer oldBuffer = this.buffer;
      ByteBuffer newBuffer = allocateDirect(newCapacity);
      if (readerIndex < newCapacity) {
        if (writerIndex > newCapacity)
          writerIndex(writerIndex = newCapacity); 
        oldBuffer.position(readerIndex).limit(writerIndex);
        newBuffer.position(readerIndex).limit(writerIndex);
        newBuffer.put(oldBuffer);
        newBuffer.clear();
      } else {
        setIndex(newCapacity, newCapacity);
      } 
      setByteBuffer(newBuffer);
    } 
    return this;
  }
  
  public ByteBufAllocator alloc() {
    return this.alloc;
  }
  
  public ByteOrder order() {
    return ByteOrder.BIG_ENDIAN;
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
    getBytes(index, dst, false);
    return this;
  }
  
  private void getBytes(int index, ByteBuffer dst, boolean internal) {
    ByteBuffer tmpBuf;
    checkIndex(index);
    if (dst == null)
      throw new NullPointerException("dst"); 
    int bytesToCopy = Math.min(capacity() - index, dst.remaining());
    if (internal) {
      tmpBuf = internalNioBuffer();
    } else {
      tmpBuf = this.buffer.duplicate();
    } 
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
    ensureAccessible();
    ByteBuffer tmpBuf = internalNioBuffer();
    if (src == tmpBuf)
      src = src.duplicate(); 
    tmpBuf.clear().position(index).limit(index + src.remaining());
    tmpBuf.put(src);
    return this;
  }
  
  public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
    ensureAccessible();
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
    ensureAccessible();
    if (length == 0)
      return 0; 
    if (internal) {
      tmpBuf = internalNioBuffer();
    } else {
      tmpBuf = this.buffer.duplicate();
    } 
    tmpBuf.clear().position(index).limit(index + length);
    return out.write(tmpBuf);
  }
  
  public int readBytes(GatheringByteChannel out, int length) throws IOException {
    checkReadableBytes(length);
    int readBytes = getBytes(this.readerIndex, out, length, true);
    this.readerIndex += readBytes;
    return readBytes;
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
    ensureAccessible();
    ByteBuffer tmpBuf = internalNioBuffer();
    tmpBuf.clear().position(index).limit(index + length);
    try {
      return in.read(tmpBuf);
    } catch (ClosedChannelException ignored) {
      return -1;
    } 
  }
  
  public int nioBufferCount() {
    return 1;
  }
  
  public ByteBuffer[] nioBuffers(int index, int length) {
    return new ByteBuffer[] { nioBuffer(index, length) };
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
  
  public ByteBuffer internalNioBuffer(int index, int length) {
    checkIndex(index, length);
    return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
  }
  
  private ByteBuffer internalNioBuffer() {
    ByteBuffer tmpNioBuf = this.tmpNioBuf;
    if (tmpNioBuf == null)
      this.tmpNioBuf = tmpNioBuf = this.buffer.duplicate(); 
    return tmpNioBuf;
  }
  
  public ByteBuffer nioBuffer(int index, int length) {
    checkIndex(index, length);
    return ((ByteBuffer)this.buffer.duplicate().position(index).limit(index + length)).slice();
  }
  
  protected void deallocate() {
    ByteBuffer buffer = this.buffer;
    if (buffer == null)
      return; 
    this.buffer = null;
    if (!this.doNotFree)
      freeDirect(buffer); 
  }
  
  public ByteBuf unwrap() {
    return null;
  }
  
  long addr(int index) {
    return this.memoryAddress + index;
  }
  
  protected SwappedByteBuf newSwappedByteBuf() {
    return new UnsafeDirectSwappedByteBuf(this);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\UnpooledUnsafeDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */