package io.netty.buffer;

import io.netty.util.Recycler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

final class PooledDirectByteBuf extends PooledByteBuf<ByteBuffer> {
  private static final Recycler<PooledDirectByteBuf> RECYCLER = new Recycler<PooledDirectByteBuf>() {
      protected PooledDirectByteBuf newObject(Recycler.Handle handle) {
        return new PooledDirectByteBuf(handle, 0);
      }
    };
  
  static PooledDirectByteBuf newInstance(int maxCapacity) {
    PooledDirectByteBuf buf = (PooledDirectByteBuf)RECYCLER.get();
    buf.setRefCnt(1);
    buf.maxCapacity(maxCapacity);
    return buf;
  }
  
  private PooledDirectByteBuf(Recycler.Handle recyclerHandle, int maxCapacity) {
    super(recyclerHandle, maxCapacity);
  }
  
  protected ByteBuffer newInternalNioBuffer(ByteBuffer memory) {
    return memory.duplicate();
  }
  
  public boolean isDirect() {
    return true;
  }
  
  protected byte _getByte(int index) {
    return this.memory.get(idx(index));
  }
  
  protected short _getShort(int index) {
    return this.memory.getShort(idx(index));
  }
  
  protected int _getUnsignedMedium(int index) {
    index = idx(index);
    return (this.memory.get(index) & 0xFF) << 16 | (this.memory.get(index + 1) & 0xFF) << 8 | this.memory.get(index + 2) & 0xFF;
  }
  
  protected int _getInt(int index) {
    return this.memory.getInt(idx(index));
  }
  
  protected long _getLong(int index) {
    return this.memory.getLong(idx(index));
  }
  
  public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
    checkDstIndex(index, length, dstIndex, dst.capacity());
    if (dst.hasArray()) {
      getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
    } else if (dst.nioBufferCount() > 0) {
      for (ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
        int bbLen = bb.remaining();
        getBytes(index, bb);
        index += bbLen;
      } 
    } else {
      dst.setBytes(dstIndex, this, index, length);
    } 
    return this;
  }
  
  public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
    getBytes(index, dst, dstIndex, length, false);
    return this;
  }
  
  private void getBytes(int index, byte[] dst, int dstIndex, int length, boolean internal) {
    ByteBuffer tmpBuf;
    checkDstIndex(index, length, dstIndex, dst.length);
    if (internal) {
      tmpBuf = internalNioBuffer();
    } else {
      tmpBuf = this.memory.duplicate();
    } 
    index = idx(index);
    tmpBuf.clear().position(index).limit(index + length);
    tmpBuf.get(dst, dstIndex, length);
  }
  
  public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
    checkReadableBytes(length);
    getBytes(this.readerIndex, dst, dstIndex, length, true);
    this.readerIndex += length;
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
    getBytes(index, out, length, false);
    return this;
  }
  
  private void getBytes(int index, OutputStream out, int length, boolean internal) throws IOException {
    ByteBuffer tmpBuf;
    checkIndex(index, length);
    if (length == 0)
      return; 
    byte[] tmp = new byte[length];
    if (internal) {
      tmpBuf = internalNioBuffer();
    } else {
      tmpBuf = this.memory.duplicate();
    } 
    tmpBuf.clear().position(idx(index));
    tmpBuf.get(tmp);
    out.write(tmp);
  }
  
  public ByteBuf readBytes(OutputStream out, int length) throws IOException {
    checkReadableBytes(length);
    getBytes(this.readerIndex, out, length, true);
    this.readerIndex += length;
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
    this.memory.put(idx(index), (byte)value);
  }
  
  protected void _setShort(int index, int value) {
    this.memory.putShort(idx(index), (short)value);
  }
  
  protected void _setMedium(int index, int value) {
    index = idx(index);
    this.memory.put(index, (byte)(value >>> 16));
    this.memory.put(index + 1, (byte)(value >>> 8));
    this.memory.put(index + 2, (byte)value);
  }
  
  protected void _setInt(int index, int value) {
    this.memory.putInt(idx(index), value);
  }
  
  protected void _setLong(int index, long value) {
    this.memory.putLong(idx(index), value);
  }
  
  public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
    checkSrcIndex(index, length, srcIndex, src.capacity());
    if (src.hasArray()) {
      setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
    } else if (src.nioBufferCount() > 0) {
      for (ByteBuffer bb : src.nioBuffers(srcIndex, length)) {
        int bbLen = bb.remaining();
        setBytes(index, bb);
        index += bbLen;
      } 
    } else {
      src.getBytes(srcIndex, this, index, length);
    } 
    return this;
  }
  
  public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
    checkSrcIndex(index, length, srcIndex, src.length);
    ByteBuffer tmpBuf = internalNioBuffer();
    index = idx(index);
    tmpBuf.clear().position(index).limit(index + length);
    tmpBuf.put(src, srcIndex, length);
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
    if (readBytes <= 0)
      return readBytes; 
    ByteBuffer tmpBuf = internalNioBuffer();
    tmpBuf.clear().position(idx(index));
    tmpBuf.put(tmp, 0, readBytes);
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
    copy.writeBytes(this, index, length);
    return copy;
  }
  
  public int nioBufferCount() {
    return 1;
  }
  
  public ByteBuffer nioBuffer(int index, int length) {
    checkIndex(index, length);
    index = idx(index);
    return ((ByteBuffer)this.memory.duplicate().position(index).limit(index + length)).slice();
  }
  
  public ByteBuffer[] nioBuffers(int index, int length) {
    return new ByteBuffer[] { nioBuffer(index, length) };
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
    return false;
  }
  
  public long memoryAddress() {
    throw new UnsupportedOperationException();
  }
  
  protected Recycler<?> recycler() {
    return RECYCLER;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\PooledDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */