package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

class WrappedByteBuf extends ByteBuf {
   protected final ByteBuf buf;

   protected WrappedByteBuf(ByteBuf var1) {
      if (var1 == null) {
         throw new NullPointerException("buf");
      } else {
         this.buf = var1;
      }
   }

   public boolean hasMemoryAddress() {
      return this.buf.hasMemoryAddress();
   }

   public long memoryAddress() {
      return this.buf.memoryAddress();
   }

   public int capacity() {
      return this.buf.capacity();
   }

   public ByteBuf capacity(int var1) {
      this.buf.capacity(var1);
      return this;
   }

   public int maxCapacity() {
      return this.buf.maxCapacity();
   }

   public ByteBufAllocator alloc() {
      return this.buf.alloc();
   }

   public ByteOrder order() {
      return this.buf.order();
   }

   public ByteBuf order(ByteOrder var1) {
      return this.buf.order(var1);
   }

   public ByteBuf unwrap() {
      return this.buf;
   }

   public boolean isDirect() {
      return this.buf.isDirect();
   }

   public int readerIndex() {
      return this.buf.readerIndex();
   }

   public ByteBuf readerIndex(int var1) {
      this.buf.readerIndex(var1);
      return this;
   }

   public int writerIndex() {
      return this.buf.writerIndex();
   }

   public ByteBuf writerIndex(int var1) {
      this.buf.writerIndex(var1);
      return this;
   }

   public ByteBuf setIndex(int var1, int var2) {
      this.buf.setIndex(var1, var2);
      return this;
   }

   public int readableBytes() {
      return this.buf.readableBytes();
   }

   public int writableBytes() {
      return this.buf.writableBytes();
   }

   public int maxWritableBytes() {
      return this.buf.maxWritableBytes();
   }

   public boolean isReadable() {
      return this.buf.isReadable();
   }

   public boolean isWritable() {
      return this.buf.isWritable();
   }

   public ByteBuf clear() {
      this.buf.clear();
      return this;
   }

   public ByteBuf markReaderIndex() {
      this.buf.markReaderIndex();
      return this;
   }

   public ByteBuf resetReaderIndex() {
      this.buf.resetReaderIndex();
      return this;
   }

   public ByteBuf markWriterIndex() {
      this.buf.markWriterIndex();
      return this;
   }

   public ByteBuf resetWriterIndex() {
      this.buf.resetWriterIndex();
      return this;
   }

   public ByteBuf discardReadBytes() {
      this.buf.discardReadBytes();
      return this;
   }

   public ByteBuf discardSomeReadBytes() {
      this.buf.discardSomeReadBytes();
      return this;
   }

   public ByteBuf ensureWritable(int var1) {
      this.buf.ensureWritable(var1);
      return this;
   }

   public int ensureWritable(int var1, boolean var2) {
      return this.buf.ensureWritable(var1, var2);
   }

   public boolean getBoolean(int var1) {
      return this.buf.getBoolean(var1);
   }

   public byte getByte(int var1) {
      return this.buf.getByte(var1);
   }

   public short getUnsignedByte(int var1) {
      return this.buf.getUnsignedByte(var1);
   }

   public short getShort(int var1) {
      return this.buf.getShort(var1);
   }

   public int getUnsignedShort(int var1) {
      return this.buf.getUnsignedShort(var1);
   }

   public int getMedium(int var1) {
      return this.buf.getMedium(var1);
   }

   public int getUnsignedMedium(int var1) {
      return this.buf.getUnsignedMedium(var1);
   }

   public int getInt(int var1) {
      return this.buf.getInt(var1);
   }

   public long getUnsignedInt(int var1) {
      return this.buf.getUnsignedInt(var1);
   }

   public long getLong(int var1) {
      return this.buf.getLong(var1);
   }

   public char getChar(int var1) {
      return this.buf.getChar(var1);
   }

   public float getFloat(int var1) {
      return this.buf.getFloat(var1);
   }

   public double getDouble(int var1) {
      return this.buf.getDouble(var1);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2) {
      this.buf.getBytes(var1, var2);
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3) {
      this.buf.getBytes(var1, var2, var3);
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.buf.getBytes(var1, var2, var3, var4);
      return this;
   }

   public ByteBuf getBytes(int var1, byte[] var2) {
      this.buf.getBytes(var1, var2);
      return this;
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      this.buf.getBytes(var1, var2, var3, var4);
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      this.buf.getBytes(var1, var2);
      return this;
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      this.buf.getBytes(var1, var2, var3);
      return this;
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      return this.buf.getBytes(var1, var2, var3);
   }

   public ByteBuf setBoolean(int var1, boolean var2) {
      this.buf.setBoolean(var1, var2);
      return this;
   }

   public ByteBuf setByte(int var1, int var2) {
      this.buf.setByte(var1, var2);
      return this;
   }

   public ByteBuf setShort(int var1, int var2) {
      this.buf.setShort(var1, var2);
      return this;
   }

   public ByteBuf setMedium(int var1, int var2) {
      this.buf.setMedium(var1, var2);
      return this;
   }

   public ByteBuf setInt(int var1, int var2) {
      this.buf.setInt(var1, var2);
      return this;
   }

   public ByteBuf setLong(int var1, long var2) {
      this.buf.setLong(var1, var2);
      return this;
   }

   public ByteBuf setChar(int var1, int var2) {
      this.buf.setChar(var1, var2);
      return this;
   }

   public ByteBuf setFloat(int var1, float var2) {
      this.buf.setFloat(var1, var2);
      return this;
   }

   public ByteBuf setDouble(int var1, double var2) {
      this.buf.setDouble(var1, var2);
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuf var2) {
      this.buf.setBytes(var1, var2);
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3) {
      this.buf.setBytes(var1, var2, var3);
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.buf.setBytes(var1, var2, var3, var4);
      return this;
   }

   public ByteBuf setBytes(int var1, byte[] var2) {
      this.buf.setBytes(var1, var2);
      return this;
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      this.buf.setBytes(var1, var2, var3, var4);
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      this.buf.setBytes(var1, var2);
      return this;
   }

   public int setBytes(int var1, InputStream var2, int var3) throws IOException {
      return this.buf.setBytes(var1, var2, var3);
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) throws IOException {
      return this.buf.setBytes(var1, var2, var3);
   }

   public ByteBuf setZero(int var1, int var2) {
      this.buf.setZero(var1, var2);
      return this;
   }

   public boolean readBoolean() {
      return this.buf.readBoolean();
   }

   public byte readByte() {
      return this.buf.readByte();
   }

   public short readUnsignedByte() {
      return this.buf.readUnsignedByte();
   }

   public short readShort() {
      return this.buf.readShort();
   }

   public int readUnsignedShort() {
      return this.buf.readUnsignedShort();
   }

   public int readMedium() {
      return this.buf.readMedium();
   }

   public int readUnsignedMedium() {
      return this.buf.readUnsignedMedium();
   }

   public int readInt() {
      return this.buf.readInt();
   }

   public long readUnsignedInt() {
      return this.buf.readUnsignedInt();
   }

   public long readLong() {
      return this.buf.readLong();
   }

   public char readChar() {
      return this.buf.readChar();
   }

   public float readFloat() {
      return this.buf.readFloat();
   }

   public double readDouble() {
      return this.buf.readDouble();
   }

   public ByteBuf readBytes(int var1) {
      return this.buf.readBytes(var1);
   }

   public ByteBuf readSlice(int var1) {
      return this.buf.readSlice(var1);
   }

   public ByteBuf readBytes(ByteBuf var1) {
      this.buf.readBytes(var1);
      return this;
   }

   public ByteBuf readBytes(ByteBuf var1, int var2) {
      this.buf.readBytes(var1, var2);
      return this;
   }

   public ByteBuf readBytes(ByteBuf var1, int var2, int var3) {
      this.buf.readBytes(var1, var2, var3);
      return this;
   }

   public ByteBuf readBytes(byte[] var1) {
      this.buf.readBytes(var1);
      return this;
   }

   public ByteBuf readBytes(byte[] var1, int var2, int var3) {
      this.buf.readBytes(var1, var2, var3);
      return this;
   }

   public ByteBuf readBytes(ByteBuffer var1) {
      this.buf.readBytes(var1);
      return this;
   }

   public ByteBuf readBytes(OutputStream var1, int var2) throws IOException {
      this.buf.readBytes(var1, var2);
      return this;
   }

   public int readBytes(GatheringByteChannel var1, int var2) throws IOException {
      return this.buf.readBytes(var1, var2);
   }

   public ByteBuf skipBytes(int var1) {
      this.buf.skipBytes(var1);
      return this;
   }

   public ByteBuf writeBoolean(boolean var1) {
      this.buf.writeBoolean(var1);
      return this;
   }

   public ByteBuf writeByte(int var1) {
      this.buf.writeByte(var1);
      return this;
   }

   public ByteBuf writeShort(int var1) {
      this.buf.writeShort(var1);
      return this;
   }

   public ByteBuf writeMedium(int var1) {
      this.buf.writeMedium(var1);
      return this;
   }

   public ByteBuf writeInt(int var1) {
      this.buf.writeInt(var1);
      return this;
   }

   public ByteBuf writeLong(long var1) {
      this.buf.writeLong(var1);
      return this;
   }

   public ByteBuf writeChar(int var1) {
      this.buf.writeChar(var1);
      return this;
   }

   public ByteBuf writeFloat(float var1) {
      this.buf.writeFloat(var1);
      return this;
   }

   public ByteBuf writeDouble(double var1) {
      this.buf.writeDouble(var1);
      return this;
   }

   public ByteBuf writeBytes(ByteBuf var1) {
      this.buf.writeBytes(var1);
      return this;
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2) {
      this.buf.writeBytes(var1, var2);
      return this;
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2, int var3) {
      this.buf.writeBytes(var1, var2, var3);
      return this;
   }

   public ByteBuf writeBytes(byte[] var1) {
      this.buf.writeBytes(var1);
      return this;
   }

   public ByteBuf writeBytes(byte[] var1, int var2, int var3) {
      this.buf.writeBytes(var1, var2, var3);
      return this;
   }

   public ByteBuf writeBytes(ByteBuffer var1) {
      this.buf.writeBytes(var1);
      return this;
   }

   public int writeBytes(InputStream var1, int var2) throws IOException {
      return this.buf.writeBytes(var1, var2);
   }

   public int writeBytes(ScatteringByteChannel var1, int var2) throws IOException {
      return this.buf.writeBytes(var1, var2);
   }

   public ByteBuf writeZero(int var1) {
      this.buf.writeZero(var1);
      return this;
   }

   public int indexOf(int var1, int var2, byte var3) {
      return this.buf.indexOf(var1, var2, var3);
   }

   public int bytesBefore(byte var1) {
      return this.buf.bytesBefore(var1);
   }

   public int bytesBefore(int var1, byte var2) {
      return this.buf.bytesBefore(var1, var2);
   }

   public int bytesBefore(int var1, int var2, byte var3) {
      return this.buf.bytesBefore(var1, var2, var3);
   }

   public int forEachByte(ByteBufProcessor var1) {
      return this.buf.forEachByte(var1);
   }

   public int forEachByte(int var1, int var2, ByteBufProcessor var3) {
      return this.buf.forEachByte(var1, var2, var3);
   }

   public int forEachByteDesc(ByteBufProcessor var1) {
      return this.buf.forEachByteDesc(var1);
   }

   public int forEachByteDesc(int var1, int var2, ByteBufProcessor var3) {
      return this.buf.forEachByteDesc(var1, var2, var3);
   }

   public ByteBuf copy() {
      return this.buf.copy();
   }

   public ByteBuf copy(int var1, int var2) {
      return this.buf.copy(var1, var2);
   }

   public ByteBuf slice() {
      return this.buf.slice();
   }

   public ByteBuf slice(int var1, int var2) {
      return this.buf.slice(var1, var2);
   }

   public ByteBuf duplicate() {
      return this.buf.duplicate();
   }

   public int nioBufferCount() {
      return this.buf.nioBufferCount();
   }

   public ByteBuffer nioBuffer() {
      return this.buf.nioBuffer();
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      return this.buf.nioBuffer(var1, var2);
   }

   public ByteBuffer[] nioBuffers() {
      return this.buf.nioBuffers();
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      return this.buf.nioBuffers(var1, var2);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      return this.buf.internalNioBuffer(var1, var2);
   }

   public boolean hasArray() {
      return this.buf.hasArray();
   }

   public byte[] array() {
      return this.buf.array();
   }

   public int arrayOffset() {
      return this.buf.arrayOffset();
   }

   public String toString(Charset var1) {
      return this.buf.toString(var1);
   }

   public String toString(int var1, int var2, Charset var3) {
      return this.buf.toString(var1, var2, var3);
   }

   public int hashCode() {
      return this.buf.hashCode();
   }

   public boolean equals(Object var1) {
      return this.buf.equals(var1);
   }

   public int compareTo(ByteBuf var1) {
      return this.buf.compareTo(var1);
   }

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + '(' + this.buf.toString() + ')';
   }

   public ByteBuf retain(int var1) {
      this.buf.retain(var1);
      return this;
   }

   public ByteBuf retain() {
      this.buf.retain();
      return this;
   }

   public boolean isReadable(int var1) {
      return this.buf.isReadable(var1);
   }

   public boolean isWritable(int var1) {
      return this.buf.isWritable(var1);
   }

   public int refCnt() {
      return this.buf.refCnt();
   }

   public boolean release() {
      return this.buf.release();
   }

   public boolean release(int var1) {
      return this.buf.release(var1);
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }

   public int compareTo(Object var1) {
      return this.compareTo((ByteBuf)var1);
   }
}
