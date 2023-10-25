package io.netty.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class SlicedByteBuf extends AbstractDerivedByteBuf {
  private final ByteBuf buffer;
  
  private final int adjustment;
  
  private final int length;
  
  public SlicedByteBuf(ByteBuf buffer, int index, int length) {
    super(length);
    if (index < 0 || index > buffer.capacity() - length)
      throw new IndexOutOfBoundsException(buffer + ".slice(" + index + ", " + length + ')'); 
    if (buffer instanceof SlicedByteBuf) {
      this.buffer = ((SlicedByteBuf)buffer).buffer;
      ((SlicedByteBuf)buffer).adjustment += index;
    } else if (buffer instanceof DuplicatedByteBuf) {
      this.buffer = buffer.unwrap();
      this.adjustment = index;
    } else {
      this.buffer = buffer;
      this.adjustment = index;
    } 
    this.length = length;
    writerIndex(length);
  }
  
  public ByteBuf unwrap() {
    return this.buffer;
  }
  
  public ByteBufAllocator alloc() {
    return this.buffer.alloc();
  }
  
  public ByteOrder order() {
    return this.buffer.order();
  }
  
  public boolean isDirect() {
    return this.buffer.isDirect();
  }
  
  public int capacity() {
    return this.length;
  }
  
  public ByteBuf capacity(int newCapacity) {
    throw new UnsupportedOperationException("sliced buffer");
  }
  
  public boolean hasArray() {
    return this.buffer.hasArray();
  }
  
  public byte[] array() {
    return this.buffer.array();
  }
  
  public int arrayOffset() {
    return this.buffer.arrayOffset() + this.adjustment;
  }
  
  public boolean hasMemoryAddress() {
    return this.buffer.hasMemoryAddress();
  }
  
  public long memoryAddress() {
    return this.buffer.memoryAddress() + this.adjustment;
  }
  
  protected byte _getByte(int index) {
    return this.buffer.getByte(index + this.adjustment);
  }
  
  protected short _getShort(int index) {
    return this.buffer.getShort(index + this.adjustment);
  }
  
  protected int _getUnsignedMedium(int index) {
    return this.buffer.getUnsignedMedium(index + this.adjustment);
  }
  
  protected int _getInt(int index) {
    return this.buffer.getInt(index + this.adjustment);
  }
  
  protected long _getLong(int index) {
    return this.buffer.getLong(index + this.adjustment);
  }
  
  public ByteBuf duplicate() {
    ByteBuf duplicate = this.buffer.slice(this.adjustment, this.length);
    duplicate.setIndex(readerIndex(), writerIndex());
    return duplicate;
  }
  
  public ByteBuf copy(int index, int length) {
    checkIndex(index, length);
    return this.buffer.copy(index + this.adjustment, length);
  }
  
  public ByteBuf slice(int index, int length) {
    checkIndex(index, length);
    if (length == 0)
      return Unpooled.EMPTY_BUFFER; 
    return this.buffer.slice(index + this.adjustment, length);
  }
  
  public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
    checkIndex(index, length);
    this.buffer.getBytes(index + this.adjustment, dst, dstIndex, length);
    return this;
  }
  
  public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
    checkIndex(index, length);
    this.buffer.getBytes(index + this.adjustment, dst, dstIndex, length);
    return this;
  }
  
  public ByteBuf getBytes(int index, ByteBuffer dst) {
    checkIndex(index, dst.remaining());
    this.buffer.getBytes(index + this.adjustment, dst);
    return this;
  }
  
  protected void _setByte(int index, int value) {
    this.buffer.setByte(index + this.adjustment, value);
  }
  
  protected void _setShort(int index, int value) {
    this.buffer.setShort(index + this.adjustment, value);
  }
  
  protected void _setMedium(int index, int value) {
    this.buffer.setMedium(index + this.adjustment, value);
  }
  
  protected void _setInt(int index, int value) {
    this.buffer.setInt(index + this.adjustment, value);
  }
  
  protected void _setLong(int index, long value) {
    this.buffer.setLong(index + this.adjustment, value);
  }
  
  public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
    checkIndex(index, length);
    this.buffer.setBytes(index + this.adjustment, src, srcIndex, length);
    return this;
  }
  
  public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
    checkIndex(index, length);
    this.buffer.setBytes(index + this.adjustment, src, srcIndex, length);
    return this;
  }
  
  public ByteBuf setBytes(int index, ByteBuffer src) {
    checkIndex(index, src.remaining());
    this.buffer.setBytes(index + this.adjustment, src);
    return this;
  }
  
  public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
    checkIndex(index, length);
    this.buffer.getBytes(index + this.adjustment, out, length);
    return this;
  }
  
  public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
    checkIndex(index, length);
    return this.buffer.getBytes(index + this.adjustment, out, length);
  }
  
  public int setBytes(int index, InputStream in, int length) throws IOException {
    checkIndex(index, length);
    return this.buffer.setBytes(index + this.adjustment, in, length);
  }
  
  public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
    checkIndex(index, length);
    return this.buffer.setBytes(index + this.adjustment, in, length);
  }
  
  public int nioBufferCount() {
    return this.buffer.nioBufferCount();
  }
  
  public ByteBuffer nioBuffer(int index, int length) {
    checkIndex(index, length);
    return this.buffer.nioBuffer(index + this.adjustment, length);
  }
  
  public ByteBuffer[] nioBuffers(int index, int length) {
    checkIndex(index, length);
    return this.buffer.nioBuffers(index + this.adjustment, length);
  }
  
  public ByteBuffer internalNioBuffer(int index, int length) {
    checkIndex(index, length);
    return nioBuffer(index, length);
  }
  
  public int forEachByte(int index, int length, ByteBufProcessor processor) {
    int ret = this.buffer.forEachByte(index + this.adjustment, length, processor);
    if (ret >= this.adjustment)
      return ret - this.adjustment; 
    return -1;
  }
  
  public int forEachByteDesc(int index, int length, ByteBufProcessor processor) {
    int ret = this.buffer.forEachByteDesc(index + this.adjustment, length, processor);
    if (ret >= this.adjustment)
      return ret - this.adjustment; 
    return -1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\SlicedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */