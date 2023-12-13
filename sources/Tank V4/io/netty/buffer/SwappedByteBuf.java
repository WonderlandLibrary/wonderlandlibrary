package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

public final class SwappedByteBuf extends ByteBuf {
   private final ByteBuf buf;
   private final ByteOrder order;

   public SwappedByteBuf(ByteBuf var1) {
      if (var1 == null) {
         throw new NullPointerException("buf");
      } else {
         this.buf = var1;
         if (var1.order() == ByteOrder.BIG_ENDIAN) {
            this.order = ByteOrder.LITTLE_ENDIAN;
         } else {
            this.order = ByteOrder.BIG_ENDIAN;
         }

      }
   }

   public ByteOrder order() {
      return this.order;
   }

   public ByteBuf order(ByteOrder var1) {
      if (var1 == null) {
         throw new NullPointerException("endianness");
      } else {
         return (ByteBuf)(var1 == this.order ? this : this.buf);
      }
   }

   public ByteBuf unwrap() {
      return this.buf.unwrap();
   }

   public ByteBufAllocator alloc() {
      return this.buf.alloc();
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

   public boolean isReadable(int var1) {
      return this.buf.isReadable(var1);
   }

   public boolean isWritable() {
      return this.buf.isWritable();
   }

   public boolean isWritable(int var1) {
      return this.buf.isWritable(var1);
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
      return ByteBufUtil.swapShort(this.buf.getShort(var1));
   }

   public int getUnsignedShort(int var1) {
      return this.getShort(var1) & '\uffff';
   }

   public int getMedium(int var1) {
      return ByteBufUtil.swapMedium(this.buf.getMedium(var1));
   }

   public int getUnsignedMedium(int var1) {
      return this.getMedium(var1) & 16777215;
   }

   public int getInt(int var1) {
      return ByteBufUtil.swapInt(this.buf.getInt(var1));
   }

   public long getUnsignedInt(int var1) {
      return (long)this.getInt(var1) & 4294967295L;
   }

   public long getLong(int var1) {
      return ByteBufUtil.swapLong(this.buf.getLong(var1));
   }

   public char getChar(int var1) {
      return (char)this.getShort(var1);
   }

   public float getFloat(int var1) {
      return Float.intBitsToFloat(this.getInt(var1));
   }

   public double getDouble(int var1) {
      return Double.longBitsToDouble(this.getLong(var1));
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
      this.buf.setShort(var1, ByteBufUtil.swapShort((short)var2));
      return this;
   }

   public ByteBuf setMedium(int var1, int var2) {
      this.buf.setMedium(var1, ByteBufUtil.swapMedium(var2));
      return this;
   }

   public ByteBuf setInt(int var1, int var2) {
      this.buf.setInt(var1, ByteBufUtil.swapInt(var2));
      return this;
   }

   public ByteBuf setLong(int var1, long var2) {
      this.buf.setLong(var1, ByteBufUtil.swapLong(var2));
      return this;
   }

   public ByteBuf setChar(int var1, int var2) {
      this.setShort(var1, var2);
      return this;
   }

   public ByteBuf setFloat(int var1, float var2) {
      this.setInt(var1, Float.floatToRawIntBits(var2));
      return this;
   }

   public ByteBuf setDouble(int var1, double var2) {
      this.setLong(var1, Double.doubleToRawLongBits(var2));
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
      return ByteBufUtil.swapShort(this.buf.readShort());
   }

   public int readUnsignedShort() {
      return this.readShort() & '\uffff';
   }

   public int readMedium() {
      return ByteBufUtil.swapMedium(this.buf.readMedium());
   }

   public int readUnsignedMedium() {
      return this.readMedium() & 16777215;
   }

   public int readInt() {
      return ByteBufUtil.swapInt(this.buf.readInt());
   }

   public long readUnsignedInt() {
      return (long)this.readInt() & 4294967295L;
   }

   public long readLong() {
      return ByteBufUtil.swapLong(this.buf.readLong());
   }

   public char readChar() {
      return (char)this.readShort();
   }

   public float readFloat() {
      return Float.intBitsToFloat(this.readInt());
   }

   public double readDouble() {
      return Double.longBitsToDouble(this.readLong());
   }

   public ByteBuf readBytes(int var1) {
      return this.buf.readBytes(var1).order(this.order());
   }

   public ByteBuf readSlice(int var1) {
      return this.buf.readSlice(var1).order(this.order);
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
      this.buf.writeShort(ByteBufUtil.swapShort((short)var1));
      return this;
   }

   public ByteBuf writeMedium(int var1) {
      this.buf.writeMedium(ByteBufUtil.swapMedium(var1));
      return this;
   }

   public ByteBuf writeInt(int var1) {
      this.buf.writeInt(ByteBufUtil.swapInt(var1));
      return this;
   }

   public ByteBuf writeLong(long var1) {
      this.buf.writeLong(ByteBufUtil.swapLong(var1));
      return this;
   }

   public ByteBuf writeChar(int var1) {
      this.writeShort(var1);
      return this;
   }

   public ByteBuf writeFloat(float var1) {
      this.writeInt(Float.floatToRawIntBits(var1));
      return this;
   }

   public ByteBuf writeDouble(double var1) {
      this.writeLong(Double.doubleToRawLongBits(var1));
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
      return this.buf.copy().order(this.order);
   }

   public ByteBuf copy(int var1, int var2) {
      return this.buf.copy(var1, var2).order(this.order);
   }

   public ByteBuf slice() {
      return this.buf.slice().order(this.order);
   }

   public ByteBuf slice(int var1, int var2) {
      return this.buf.slice(var1, var2).order(this.order);
   }

   public ByteBuf duplicate() {
      return this.buf.duplicate().order(this.order);
   }

   public int nioBufferCount() {
      return this.buf.nioBufferCount();
   }

   public ByteBuffer nioBuffer() {
      return this.buf.nioBuffer().order(this.order);
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      return this.buf.nioBuffer(var1, var2).order(this.order);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      return this.nioBuffer(var1, var2);
   }

   public ByteBuffer[] nioBuffers() {
      ByteBuffer[] var1 = this.buf.nioBuffers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = var1[var2].order(this.order);
      }

      return var1;
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      ByteBuffer[] var3 = this.buf.nioBuffers(var1, var2);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = var3[var4].order(this.order);
      }

      return var3;
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

   public boolean hasMemoryAddress() {
      return this.buf.hasMemoryAddress();
   }

   public long memoryAddress() {
      return this.buf.memoryAddress();
   }

   public String toString(Charset var1) {
      return this.buf.toString(var1);
   }

   public String toString(int var1, int var2, Charset var3) {
      return this.buf.toString(var1, var2, var3);
   }

   public int refCnt() {
      return this.buf.refCnt();
   }

   public ByteBuf retain() {
      this.buf.retain();
      return this;
   }

   public ByteBuf retain(int var1) {
      this.buf.retain(var1);
      return this;
   }

   public boolean release() {
      return this.buf.release();
   }

   public boolean release(int var1) {
      return this.buf.release(var1);
   }

   public int hashCode() {
      return this.buf.hashCode();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 instanceof ByteBuf ? ByteBufUtil.equals(this, (ByteBuf)var1) : false;
      }
   }

   public int compareTo(ByteBuf var1) {
      return ByteBufUtil.compare(this, var1);
   }

   public String toString() {
      return "Swapped(" + this.buf.toString() + ')';
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
