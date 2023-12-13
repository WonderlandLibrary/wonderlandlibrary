package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.SwappedByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;
import io.netty.util.Signal;
import io.netty.util.internal.StringUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

final class ReplayingDecoderBuffer extends ByteBuf {
   private static final Signal REPLAY;
   private ByteBuf buffer;
   private boolean terminated;
   private SwappedByteBuf swapped;
   static final ReplayingDecoderBuffer EMPTY_BUFFER;

   ReplayingDecoderBuffer() {
   }

   ReplayingDecoderBuffer(ByteBuf var1) {
      this.setCumulation(var1);
   }

   void setCumulation(ByteBuf var1) {
      this.buffer = var1;
   }

   void terminate() {
      this.terminated = true;
   }

   public int capacity() {
      return this.terminated ? this.buffer.capacity() : Integer.MAX_VALUE;
   }

   public ByteBuf capacity(int var1) {
      reject();
      return this;
   }

   public int maxCapacity() {
      return this.capacity();
   }

   public ByteBufAllocator alloc() {
      return this.buffer.alloc();
   }

   public boolean isDirect() {
      return this.buffer.isDirect();
   }

   public boolean hasArray() {
      return false;
   }

   public byte[] array() {
      throw new UnsupportedOperationException();
   }

   public int arrayOffset() {
      throw new UnsupportedOperationException();
   }

   public boolean hasMemoryAddress() {
      return false;
   }

   public long memoryAddress() {
      throw new UnsupportedOperationException();
   }

   public ByteBuf clear() {
      reject();
      return this;
   }

   public boolean equals(Object var1) {
      return this == var1;
   }

   public int compareTo(ByteBuf var1) {
      reject();
      return 0;
   }

   public ByteBuf copy() {
      reject();
      return this;
   }

   public ByteBuf copy(int var1, int var2) {
      this.checkIndex(var1, var2);
      return this.buffer.copy(var1, var2);
   }

   public ByteBuf discardReadBytes() {
      reject();
      return this;
   }

   public ByteBuf ensureWritable(int var1) {
      reject();
      return this;
   }

   public int ensureWritable(int var1, boolean var2) {
      reject();
      return 0;
   }

   public ByteBuf duplicate() {
      reject();
      return this;
   }

   public boolean getBoolean(int var1) {
      this.checkIndex(var1, 1);
      return this.buffer.getBoolean(var1);
   }

   public byte getByte(int var1) {
      this.checkIndex(var1, 1);
      return this.buffer.getByte(var1);
   }

   public short getUnsignedByte(int var1) {
      this.checkIndex(var1, 1);
      return this.buffer.getUnsignedByte(var1);
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      this.checkIndex(var1, var4);
      this.buffer.getBytes(var1, var2, var3, var4);
      return this;
   }

   public ByteBuf getBytes(int var1, byte[] var2) {
      this.checkIndex(var1, var2.length);
      this.buffer.getBytes(var1, var2);
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      reject();
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.checkIndex(var1, var4);
      this.buffer.getBytes(var1, var2, var3, var4);
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3) {
      reject();
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuf var2) {
      reject();
      return this;
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) {
      reject();
      return 0;
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) {
      reject();
      return this;
   }

   public int getInt(int var1) {
      this.checkIndex(var1, 4);
      return this.buffer.getInt(var1);
   }

   public long getUnsignedInt(int var1) {
      this.checkIndex(var1, 4);
      return this.buffer.getUnsignedInt(var1);
   }

   public long getLong(int var1) {
      this.checkIndex(var1, 8);
      return this.buffer.getLong(var1);
   }

   public int getMedium(int var1) {
      this.checkIndex(var1, 3);
      return this.buffer.getMedium(var1);
   }

   public int getUnsignedMedium(int var1) {
      this.checkIndex(var1, 3);
      return this.buffer.getUnsignedMedium(var1);
   }

   public short getShort(int var1) {
      this.checkIndex(var1, 2);
      return this.buffer.getShort(var1);
   }

   public int getUnsignedShort(int var1) {
      this.checkIndex(var1, 2);
      return this.buffer.getUnsignedShort(var1);
   }

   public char getChar(int var1) {
      this.checkIndex(var1, 2);
      return this.buffer.getChar(var1);
   }

   public float getFloat(int var1) {
      this.checkIndex(var1, 4);
      return this.buffer.getFloat(var1);
   }

   public double getDouble(int var1) {
      this.checkIndex(var1, 8);
      return this.buffer.getDouble(var1);
   }

   public int hashCode() {
      reject();
      return 0;
   }

   public int indexOf(int var1, int var2, byte var3) {
      int var4 = this.buffer.indexOf(var1, var2, var3);
      if (var4 < 0) {
         throw REPLAY;
      } else {
         return var4;
      }
   }

   public int bytesBefore(byte var1) {
      int var2 = this.buffer.bytesBefore(var1);
      if (var2 < 0) {
         throw REPLAY;
      } else {
         return var2;
      }
   }

   public int bytesBefore(int var1, byte var2) {
      this.checkReadableBytes(var1);
      int var3 = this.buffer.bytesBefore(var1, var2);
      if (var3 < 0) {
         throw REPLAY;
      } else {
         return var3;
      }
   }

   public int bytesBefore(int var1, int var2, byte var3) {
      int var4 = this.buffer.bytesBefore(var1, var2, var3);
      if (var4 < 0) {
         throw REPLAY;
      } else {
         return var4;
      }
   }

   public int forEachByte(ByteBufProcessor var1) {
      int var2 = this.buffer.forEachByte(var1);
      if (var2 < 0) {
         throw REPLAY;
      } else {
         return var2;
      }
   }

   public int forEachByte(int var1, int var2, ByteBufProcessor var3) {
      int var4 = this.buffer.writerIndex();
      if (var1 >= var4) {
         throw REPLAY;
      } else if (var1 + var2 <= var4) {
         return this.buffer.forEachByte(var1, var2, var3);
      } else {
         int var5 = this.buffer.forEachByte(var1, var4 - var1, var3);
         if (var5 < 0) {
            throw REPLAY;
         } else {
            return var5;
         }
      }
   }

   public int forEachByteDesc(ByteBufProcessor var1) {
      if (this.terminated) {
         return this.buffer.forEachByteDesc(var1);
      } else {
         reject();
         return 0;
      }
   }

   public int forEachByteDesc(int var1, int var2, ByteBufProcessor var3) {
      if (var1 + var2 > this.buffer.writerIndex()) {
         throw REPLAY;
      } else {
         return this.buffer.forEachByteDesc(var1, var2, var3);
      }
   }

   public ByteBuf markReaderIndex() {
      this.buffer.markReaderIndex();
      return this;
   }

   public ByteBuf markWriterIndex() {
      reject();
      return this;
   }

   public ByteOrder order() {
      return this.buffer.order();
   }

   public ByteBuf order(ByteOrder var1) {
      if (var1 == null) {
         throw new NullPointerException("endianness");
      } else if (var1 == this.order()) {
         return this;
      } else {
         SwappedByteBuf var2 = this.swapped;
         if (var2 == null) {
            this.swapped = var2 = new SwappedByteBuf(this);
         }

         return var2;
      }
   }

   public boolean isReadable() {
      return this.terminated ? this.buffer.isReadable() : true;
   }

   public boolean isReadable(int var1) {
      return this.terminated ? this.buffer.isReadable(var1) : true;
   }

   public int readableBytes() {
      return this.terminated ? this.buffer.readableBytes() : Integer.MAX_VALUE - this.buffer.readerIndex();
   }

   public boolean readBoolean() {
      this.checkReadableBytes(1);
      return this.buffer.readBoolean();
   }

   public byte readByte() {
      this.checkReadableBytes(1);
      return this.buffer.readByte();
   }

   public short readUnsignedByte() {
      this.checkReadableBytes(1);
      return this.buffer.readUnsignedByte();
   }

   public ByteBuf readBytes(byte[] var1, int var2, int var3) {
      this.checkReadableBytes(var3);
      this.buffer.readBytes(var1, var2, var3);
      return this;
   }

   public ByteBuf readBytes(byte[] var1) {
      this.checkReadableBytes(var1.length);
      this.buffer.readBytes(var1);
      return this;
   }

   public ByteBuf readBytes(ByteBuffer var1) {
      reject();
      return this;
   }

   public ByteBuf readBytes(ByteBuf var1, int var2, int var3) {
      this.checkReadableBytes(var3);
      this.buffer.readBytes(var1, var2, var3);
      return this;
   }

   public ByteBuf readBytes(ByteBuf var1, int var2) {
      reject();
      return this;
   }

   public ByteBuf readBytes(ByteBuf var1) {
      this.checkReadableBytes(var1.writableBytes());
      this.buffer.readBytes(var1);
      return this;
   }

   public int readBytes(GatheringByteChannel var1, int var2) {
      reject();
      return 0;
   }

   public ByteBuf readBytes(int var1) {
      this.checkReadableBytes(var1);
      return this.buffer.readBytes(var1);
   }

   public ByteBuf readSlice(int var1) {
      this.checkReadableBytes(var1);
      return this.buffer.readSlice(var1);
   }

   public ByteBuf readBytes(OutputStream var1, int var2) {
      reject();
      return this;
   }

   public int readerIndex() {
      return this.buffer.readerIndex();
   }

   public ByteBuf readerIndex(int var1) {
      this.buffer.readerIndex(var1);
      return this;
   }

   public int readInt() {
      this.checkReadableBytes(4);
      return this.buffer.readInt();
   }

   public long readUnsignedInt() {
      this.checkReadableBytes(4);
      return this.buffer.readUnsignedInt();
   }

   public long readLong() {
      this.checkReadableBytes(8);
      return this.buffer.readLong();
   }

   public int readMedium() {
      this.checkReadableBytes(3);
      return this.buffer.readMedium();
   }

   public int readUnsignedMedium() {
      this.checkReadableBytes(3);
      return this.buffer.readUnsignedMedium();
   }

   public short readShort() {
      this.checkReadableBytes(2);
      return this.buffer.readShort();
   }

   public int readUnsignedShort() {
      this.checkReadableBytes(2);
      return this.buffer.readUnsignedShort();
   }

   public char readChar() {
      this.checkReadableBytes(2);
      return this.buffer.readChar();
   }

   public float readFloat() {
      this.checkReadableBytes(4);
      return this.buffer.readFloat();
   }

   public double readDouble() {
      this.checkReadableBytes(8);
      return this.buffer.readDouble();
   }

   public ByteBuf resetReaderIndex() {
      this.buffer.resetReaderIndex();
      return this;
   }

   public ByteBuf resetWriterIndex() {
      reject();
      return this;
   }

   public ByteBuf setBoolean(int var1, boolean var2) {
      reject();
      return this;
   }

   public ByteBuf setByte(int var1, int var2) {
      reject();
      return this;
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      reject();
      return this;
   }

   public ByteBuf setBytes(int var1, byte[] var2) {
      reject();
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      reject();
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      reject();
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3) {
      reject();
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuf var2) {
      reject();
      return this;
   }

   public int setBytes(int var1, InputStream var2, int var3) {
      reject();
      return 0;
   }

   public ByteBuf setZero(int var1, int var2) {
      reject();
      return this;
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) {
      reject();
      return 0;
   }

   public ByteBuf setIndex(int var1, int var2) {
      reject();
      return this;
   }

   public ByteBuf setInt(int var1, int var2) {
      reject();
      return this;
   }

   public ByteBuf setLong(int var1, long var2) {
      reject();
      return this;
   }

   public ByteBuf setMedium(int var1, int var2) {
      reject();
      return this;
   }

   public ByteBuf setShort(int var1, int var2) {
      reject();
      return this;
   }

   public ByteBuf setChar(int var1, int var2) {
      reject();
      return this;
   }

   public ByteBuf setFloat(int var1, float var2) {
      reject();
      return this;
   }

   public ByteBuf setDouble(int var1, double var2) {
      reject();
      return this;
   }

   public ByteBuf skipBytes(int var1) {
      this.checkReadableBytes(var1);
      this.buffer.skipBytes(var1);
      return this;
   }

   public ByteBuf slice() {
      reject();
      return this;
   }

   public ByteBuf slice(int var1, int var2) {
      this.checkIndex(var1, var2);
      return this.buffer.slice(var1, var2);
   }

   public int nioBufferCount() {
      return this.buffer.nioBufferCount();
   }

   public ByteBuffer nioBuffer() {
      reject();
      return null;
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      this.checkIndex(var1, var2);
      return this.buffer.nioBuffer(var1, var2);
   }

   public ByteBuffer[] nioBuffers() {
      reject();
      return null;
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      this.checkIndex(var1, var2);
      return this.buffer.nioBuffers(var1, var2);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      this.checkIndex(var1, var2);
      return this.buffer.internalNioBuffer(var1, var2);
   }

   public String toString(int var1, int var2, Charset var3) {
      this.checkIndex(var1, var2);
      return this.buffer.toString(var1, var2, var3);
   }

   public String toString(Charset var1) {
      reject();
      return null;
   }

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + '(' + "ridx=" + this.readerIndex() + ", " + "widx=" + this.writerIndex() + ')';
   }

   public boolean isWritable() {
      return false;
   }

   public boolean isWritable(int var1) {
      return false;
   }

   public int writableBytes() {
      return 0;
   }

   public int maxWritableBytes() {
      return 0;
   }

   public ByteBuf writeBoolean(boolean var1) {
      reject();
      return this;
   }

   public ByteBuf writeByte(int var1) {
      reject();
      return this;
   }

   public ByteBuf writeBytes(byte[] var1, int var2, int var3) {
      reject();
      return this;
   }

   public ByteBuf writeBytes(byte[] var1) {
      reject();
      return this;
   }

   public ByteBuf writeBytes(ByteBuffer var1) {
      reject();
      return this;
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2, int var3) {
      reject();
      return this;
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2) {
      reject();
      return this;
   }

   public ByteBuf writeBytes(ByteBuf var1) {
      reject();
      return this;
   }

   public int writeBytes(InputStream var1, int var2) {
      reject();
      return 0;
   }

   public int writeBytes(ScatteringByteChannel var1, int var2) {
      reject();
      return 0;
   }

   public ByteBuf writeInt(int var1) {
      reject();
      return this;
   }

   public ByteBuf writeLong(long var1) {
      reject();
      return this;
   }

   public ByteBuf writeMedium(int var1) {
      reject();
      return this;
   }

   public ByteBuf writeZero(int var1) {
      reject();
      return this;
   }

   public int writerIndex() {
      return this.buffer.writerIndex();
   }

   public ByteBuf writerIndex(int var1) {
      reject();
      return this;
   }

   public ByteBuf writeShort(int var1) {
      reject();
      return this;
   }

   public ByteBuf writeChar(int var1) {
      reject();
      return this;
   }

   public ByteBuf writeFloat(float var1) {
      reject();
      return this;
   }

   public ByteBuf writeDouble(double var1) {
      reject();
      return this;
   }

   private void checkIndex(int var1, int var2) {
      if (var1 + var2 > this.buffer.writerIndex()) {
         throw REPLAY;
      }
   }

   private void checkReadableBytes(int var1) {
      if (this.buffer.readableBytes() < var1) {
         throw REPLAY;
      }
   }

   public ByteBuf discardSomeReadBytes() {
      reject();
      return this;
   }

   public int refCnt() {
      return this.buffer.refCnt();
   }

   public ByteBuf retain() {
      reject();
      return this;
   }

   public ByteBuf retain(int var1) {
      reject();
      return this;
   }

   public boolean release() {
      reject();
      return false;
   }

   public boolean release(int var1) {
      reject();
      return false;
   }

   public ByteBuf unwrap() {
      reject();
      return this;
   }

   private static void reject() {
      throw new UnsupportedOperationException("not a replayable operation");
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

   static {
      REPLAY = ReplayingDecoder.REPLAY;
      EMPTY_BUFFER = new ReplayingDecoderBuffer(Unpooled.EMPTY_BUFFER);
      EMPTY_BUFFER.terminate();
   }
}
