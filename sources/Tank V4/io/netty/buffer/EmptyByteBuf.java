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

   public EmptyByteBuf(ByteBufAllocator var1) {
      this(var1, ByteOrder.BIG_ENDIAN);
   }

   private EmptyByteBuf(ByteBufAllocator var1, ByteOrder var2) {
      if (var1 == null) {
         throw new NullPointerException("alloc");
      } else {
         this.alloc = var1;
         this.order = var2;
         this.str = StringUtil.simpleClassName((Object)this) + (var2 == ByteOrder.BIG_ENDIAN ? "BE" : "LE");
      }
   }

   public int capacity() {
      return 0;
   }

   public ByteBuf capacity(int var1) {
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

   public ByteBuf order(ByteOrder var1) {
      if (var1 == null) {
         throw new NullPointerException("endianness");
      } else if (var1 == this.order()) {
         return this;
      } else {
         EmptyByteBuf var2 = this.swapped;
         if (var2 != null) {
            return var2;
         } else {
            this.swapped = var2 = new EmptyByteBuf(this.alloc(), var1);
            return var2;
         }
      }
   }

   public int readerIndex() {
      return 0;
   }

   public ByteBuf readerIndex(int var1) {
      return this.checkIndex(var1);
   }

   public int writerIndex() {
      return 0;
   }

   public ByteBuf writerIndex(int var1) {
      return this.checkIndex(var1);
   }

   public ByteBuf setIndex(int var1, int var2) {
      this.checkIndex(var1);
      this.checkIndex(var2);
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

   public ByteBuf ensureWritable(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("minWritableBytes: " + var1 + " (expected: >= 0)");
      } else if (var1 != 0) {
         throw new IndexOutOfBoundsException();
      } else {
         return this;
      }
   }

   public int ensureWritable(int var1, boolean var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException("minWritableBytes: " + var1 + " (expected: >= 0)");
      } else {
         return var1 == 0 ? 0 : 1;
      }
   }

   public boolean getBoolean(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public byte getByte(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public short getUnsignedByte(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public short getShort(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public int getUnsignedShort(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public int getMedium(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public int getUnsignedMedium(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public int getInt(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public long getUnsignedInt(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public long getLong(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public char getChar(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public float getFloat(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public double getDouble(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf getBytes(int var1, ByteBuf var2) {
      return this.checkIndex(var1, var2.writableBytes());
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3) {
      return this.checkIndex(var1, var3);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      return this.checkIndex(var1, var4);
   }

   public ByteBuf getBytes(int var1, byte[] var2) {
      return this.checkIndex(var1, var2.length);
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      return this.checkIndex(var1, var4);
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      return this.checkIndex(var1, var2.remaining());
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) {
      return this.checkIndex(var1, var3);
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) {
      this.checkIndex(var1, var3);
      return 0;
   }

   public ByteBuf setBoolean(int var1, boolean var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setByte(int var1, int var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setShort(int var1, int var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setMedium(int var1, int var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setInt(int var1, int var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setLong(int var1, long var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setChar(int var1, int var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setFloat(int var1, float var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setDouble(int var1, double var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setBytes(int var1, ByteBuf var2) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3) {
      return this.checkIndex(var1, var3);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      return this.checkIndex(var1, var4);
   }

   public ByteBuf setBytes(int var1, byte[] var2) {
      return this.checkIndex(var1, var2.length);
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      return this.checkIndex(var1, var4);
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      return this.checkIndex(var1, var2.remaining());
   }

   public int setBytes(int var1, InputStream var2, int var3) {
      this.checkIndex(var1, var3);
      return 0;
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) {
      this.checkIndex(var1, var3);
      return 0;
   }

   public ByteBuf setZero(int var1, int var2) {
      return this.checkIndex(var1, var2);
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

   public ByteBuf readBytes(int var1) {
      return this.checkLength(var1);
   }

   public ByteBuf readSlice(int var1) {
      return this.checkLength(var1);
   }

   public ByteBuf readBytes(ByteBuf var1) {
      return this.checkLength(var1.writableBytes());
   }

   public ByteBuf readBytes(ByteBuf var1, int var2) {
      return this.checkLength(var2);
   }

   public ByteBuf readBytes(ByteBuf var1, int var2, int var3) {
      return this.checkLength(var3);
   }

   public ByteBuf readBytes(byte[] var1) {
      return this.checkLength(var1.length);
   }

   public ByteBuf readBytes(byte[] var1, int var2, int var3) {
      return this.checkLength(var3);
   }

   public ByteBuf readBytes(ByteBuffer var1) {
      return this.checkLength(var1.remaining());
   }

   public ByteBuf readBytes(OutputStream var1, int var2) {
      return this.checkLength(var2);
   }

   public int readBytes(GatheringByteChannel var1, int var2) {
      this.checkLength(var2);
      return 0;
   }

   public ByteBuf skipBytes(int var1) {
      return this.checkLength(var1);
   }

   public ByteBuf writeBoolean(boolean var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeByte(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeShort(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeMedium(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeInt(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeLong(long var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeChar(int var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeFloat(float var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeDouble(double var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeBytes(ByteBuf var1) {
      throw new IndexOutOfBoundsException();
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2) {
      return this.checkLength(var2);
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2, int var3) {
      return this.checkLength(var3);
   }

   public ByteBuf writeBytes(byte[] var1) {
      return this.checkLength(var1.length);
   }

   public ByteBuf writeBytes(byte[] var1, int var2, int var3) {
      return this.checkLength(var3);
   }

   public ByteBuf writeBytes(ByteBuffer var1) {
      return this.checkLength(var1.remaining());
   }

   public int writeBytes(InputStream var1, int var2) {
      this.checkLength(var2);
      return 0;
   }

   public int writeBytes(ScatteringByteChannel var1, int var2) {
      this.checkLength(var2);
      return 0;
   }

   public ByteBuf writeZero(int var1) {
      return this.checkLength(var1);
   }

   public int indexOf(int var1, int var2, byte var3) {
      this.checkIndex(var1);
      this.checkIndex(var2);
      return -1;
   }

   public int bytesBefore(byte var1) {
      return -1;
   }

   public int bytesBefore(int var1, byte var2) {
      this.checkLength(var1);
      return -1;
   }

   public int bytesBefore(int var1, int var2, byte var3) {
      this.checkIndex(var1, var2);
      return -1;
   }

   public int forEachByte(ByteBufProcessor var1) {
      return -1;
   }

   public int forEachByte(int var1, int var2, ByteBufProcessor var3) {
      this.checkIndex(var1, var2);
      return -1;
   }

   public int forEachByteDesc(ByteBufProcessor var1) {
      return -1;
   }

   public int forEachByteDesc(int var1, int var2, ByteBufProcessor var3) {
      this.checkIndex(var1, var2);
      return -1;
   }

   public ByteBuf copy() {
      return this;
   }

   public ByteBuf copy(int var1, int var2) {
      return this.checkIndex(var1, var2);
   }

   public ByteBuf slice() {
      return this;
   }

   public ByteBuf slice(int var1, int var2) {
      return this.checkIndex(var1, var2);
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

   public ByteBuffer nioBuffer(int var1, int var2) {
      this.checkIndex(var1, var2);
      return this.nioBuffer();
   }

   public ByteBuffer[] nioBuffers() {
      return new ByteBuffer[]{EMPTY_BYTE_BUFFER};
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      this.checkIndex(var1, var2);
      return this.nioBuffers();
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
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
      return false;
   }

   public long memoryAddress() {
      if (this.hasMemoryAddress()) {
         return EMPTY_BYTE_BUFFER_ADDRESS;
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public String toString(Charset var1) {
      return "";
   }

   public String toString(int var1, int var2, Charset var3) {
      this.checkIndex(var1, var2);
      return this.toString(var3);
   }

   public int hashCode() {
      return 0;
   }

   public boolean equals(Object var1) {
      return var1 instanceof ByteBuf && !((ByteBuf)var1).isReadable();
   }

   public int compareTo(ByteBuf var1) {
      return var1.isReadable() ? -1 : 0;
   }

   public String toString() {
      return this.str;
   }

   public boolean isReadable(int var1) {
      return false;
   }

   public boolean isWritable(int var1) {
      return false;
   }

   public int refCnt() {
      return 1;
   }

   public ByteBuf retain() {
      return this;
   }

   public ByteBuf retain(int var1) {
      return this;
   }

   public boolean release() {
      return false;
   }

   public boolean release(int var1) {
      return false;
   }

   private ByteBuf checkIndex(int var1) {
      if (var1 != 0) {
         throw new IndexOutOfBoundsException();
      } else {
         return this;
      }
   }

   private ByteBuf checkIndex(int var1, int var2) {
      if (var2 < 0) {
         throw new IllegalArgumentException("length: " + var2);
      } else if (var1 == 0 && var2 == 0) {
         return this;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private ByteBuf checkLength(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("length: " + var1 + " (expected: >= 0)");
      } else if (var1 != 0) {
         throw new IndexOutOfBoundsException();
      } else {
         return this;
      }
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
      long var0 = 0L;

      try {
         if (PlatformDependent.hasUnsafe()) {
            var0 = PlatformDependent.directBufferAddress(EMPTY_BYTE_BUFFER);
         }
      } catch (Throwable var3) {
      }

      EMPTY_BYTE_BUFFER_ADDRESS = var0;
   }
}
