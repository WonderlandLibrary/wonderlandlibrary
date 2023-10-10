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

final class PooledUnsafeDirectByteBuf extends PooledByteBuf {
   private static final boolean NATIVE_ORDER;
   private static final Recycler RECYCLER;
   private long memoryAddress;

   static PooledUnsafeDirectByteBuf newInstance(int var0) {
      PooledUnsafeDirectByteBuf var1 = (PooledUnsafeDirectByteBuf)RECYCLER.get();
      var1.setRefCnt(1);
      var1.maxCapacity(var0);
      return var1;
   }

   private PooledUnsafeDirectByteBuf(Recycler.Handle var1, int var2) {
      super(var1, var2);
   }

   void init(PoolChunk var1, long var2, int var4, int var5, int var6) {
      super.init(var1, var2, var4, var5, var6);
      this.initMemoryAddress();
   }

   void initUnpooled(PoolChunk var1, int var2) {
      super.initUnpooled(var1, var2);
      this.initMemoryAddress();
   }

   private void initMemoryAddress() {
      this.memoryAddress = PlatformDependent.directBufferAddress((ByteBuffer)this.memory) + (long)this.offset;
   }

   protected ByteBuffer newInternalNioBuffer(ByteBuffer var1) {
      return var1.duplicate();
   }

   public boolean isDirect() {
      return true;
   }

   protected byte _getByte(int var1) {
      return PlatformDependent.getByte(this.addr(var1));
   }

   protected short _getShort(int var1) {
      short var2 = PlatformDependent.getShort(this.addr(var1));
      return NATIVE_ORDER ? var2 : Short.reverseBytes(var2);
   }

   protected int _getUnsignedMedium(int var1) {
      long var2 = this.addr(var1);
      return (PlatformDependent.getByte(var2) & 255) << 16 | (PlatformDependent.getByte(var2 + 1L) & 255) << 8 | PlatformDependent.getByte(var2 + 2L) & 255;
   }

   protected int _getInt(int var1) {
      int var2 = PlatformDependent.getInt(this.addr(var1));
      return NATIVE_ORDER ? var2 : Integer.reverseBytes(var2);
   }

   protected long _getLong(int var1) {
      long var2 = PlatformDependent.getLong(this.addr(var1));
      return NATIVE_ORDER ? var2 : Long.reverseBytes(var2);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.checkIndex(var1, var4);
      if (var2 == null) {
         throw new NullPointerException("dst");
      } else if (var3 >= 0 && var3 <= var2.capacity() - var4) {
         if (var4 != 0) {
            if (var2.hasMemoryAddress()) {
               PlatformDependent.copyMemory(this.addr(var1), var2.memoryAddress() + (long)var3, (long)var4);
            } else if (var2.hasArray()) {
               PlatformDependent.copyMemory(this.addr(var1), var2.array(), var2.arrayOffset() + var3, (long)var4);
            } else {
               var2.setBytes(var3, (ByteBuf)this, var1, var4);
            }
         }

         return this;
      } else {
         throw new IndexOutOfBoundsException("dstIndex: " + var3);
      }
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      this.checkIndex(var1, var4);
      if (var2 == null) {
         throw new NullPointerException("dst");
      } else if (var3 >= 0 && var3 <= var2.length - var4) {
         if (var4 != 0) {
            PlatformDependent.copyMemory(this.addr(var1), var2, var3, (long)var4);
         }

         return this;
      } else {
         throw new IndexOutOfBoundsException("dstIndex: " + var3);
      }
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      this.getBytes(var1, var2, false);
      return this;
   }

   private void getBytes(int var1, ByteBuffer var2, boolean var3) {
      this.checkIndex(var1);
      int var4 = Math.min(this.capacity() - var1, var2.remaining());
      ByteBuffer var5;
      if (var3) {
         var5 = this.internalNioBuffer();
      } else {
         var5 = ((ByteBuffer)this.memory).duplicate();
      }

      var1 = this.idx(var1);
      var5.clear().position(var1).limit(var1 + var4);
      var2.put(var5);
   }

   public ByteBuf readBytes(ByteBuffer var1) {
      int var2 = var1.remaining();
      this.checkReadableBytes(var2);
      this.getBytes(this.readerIndex, var1, true);
      this.readerIndex += var2;
      return this;
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      this.checkIndex(var1, var3);
      if (var3 != 0) {
         byte[] var4 = new byte[var3];
         PlatformDependent.copyMemory(this.addr(var1), var4, 0, (long)var3);
         var2.write(var4);
      }

      return this;
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      return this.getBytes(var1, var2, var3, false);
   }

   private int getBytes(int var1, GatheringByteChannel var2, int var3, boolean var4) throws IOException {
      this.checkIndex(var1, var3);
      if (var3 == 0) {
         return 0;
      } else {
         ByteBuffer var5;
         if (var4) {
            var5 = this.internalNioBuffer();
         } else {
            var5 = ((ByteBuffer)this.memory).duplicate();
         }

         var1 = this.idx(var1);
         var5.clear().position(var1).limit(var1 + var3);
         return var2.write(var5);
      }
   }

   public int readBytes(GatheringByteChannel var1, int var2) throws IOException {
      this.checkReadableBytes(var2);
      int var3 = this.getBytes(this.readerIndex, var1, var2, true);
      this.readerIndex += var3;
      return var3;
   }

   protected void _setByte(int var1, int var2) {
      PlatformDependent.putByte(this.addr(var1), (byte)var2);
   }

   protected void _setShort(int var1, int var2) {
      PlatformDependent.putShort(this.addr(var1), NATIVE_ORDER ? (short)var2 : Short.reverseBytes((short)var2));
   }

   protected void _setMedium(int var1, int var2) {
      long var3 = this.addr(var1);
      PlatformDependent.putByte(var3, (byte)(var2 >>> 16));
      PlatformDependent.putByte(var3 + 1L, (byte)(var2 >>> 8));
      PlatformDependent.putByte(var3 + 2L, (byte)var2);
   }

   protected void _setInt(int var1, int var2) {
      PlatformDependent.putInt(this.addr(var1), NATIVE_ORDER ? var2 : Integer.reverseBytes(var2));
   }

   protected void _setLong(int var1, long var2) {
      PlatformDependent.putLong(this.addr(var1), NATIVE_ORDER ? var2 : Long.reverseBytes(var2));
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.checkIndex(var1, var4);
      if (var2 == null) {
         throw new NullPointerException("src");
      } else if (var3 >= 0 && var3 <= var2.capacity() - var4) {
         if (var4 != 0) {
            if (var2.hasMemoryAddress()) {
               PlatformDependent.copyMemory(var2.memoryAddress() + (long)var3, this.addr(var1), (long)var4);
            } else if (var2.hasArray()) {
               PlatformDependent.copyMemory(var2.array(), var2.arrayOffset() + var3, this.addr(var1), (long)var4);
            } else {
               var2.getBytes(var3, (ByteBuf)this, var1, var4);
            }
         }

         return this;
      } else {
         throw new IndexOutOfBoundsException("srcIndex: " + var3);
      }
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      this.checkIndex(var1, var4);
      if (var4 != 0) {
         PlatformDependent.copyMemory(var2, var3, this.addr(var1), (long)var4);
      }

      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      this.checkIndex(var1);
      ByteBuffer var3 = this.internalNioBuffer();
      if (var2 == var3) {
         var2 = var2.duplicate();
      }

      var1 = this.idx(var1);
      var3.clear().position(var1).limit(var1 + var2.remaining());
      var3.put(var2);
      return this;
   }

   public int setBytes(int var1, InputStream var2, int var3) throws IOException {
      this.checkIndex(var1, var3);
      byte[] var4 = new byte[var3];
      int var5 = var2.read(var4);
      if (var5 > 0) {
         PlatformDependent.copyMemory(var4, 0, this.addr(var1), (long)var5);
      }

      return var5;
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) throws IOException {
      this.checkIndex(var1, var3);
      ByteBuffer var4 = this.internalNioBuffer();
      var1 = this.idx(var1);
      var4.clear().position(var1).limit(var1 + var3);

      try {
         return var2.read(var4);
      } catch (ClosedChannelException var6) {
         return -1;
      }
   }

   public ByteBuf copy(int var1, int var2) {
      this.checkIndex(var1, var2);
      ByteBuf var3 = this.alloc().directBuffer(var2, this.maxCapacity());
      if (var2 != 0) {
         if (var3.hasMemoryAddress()) {
            PlatformDependent.copyMemory(this.addr(var1), var3.memoryAddress(), (long)var2);
            var3.setIndex(0, var2);
         } else {
            var3.writeBytes((ByteBuf)this, var1, var2);
         }
      }

      return var3;
   }

   public int nioBufferCount() {
      return 1;
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      return new ByteBuffer[]{this.nioBuffer(var1, var2)};
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      this.checkIndex(var1, var2);
      var1 = this.idx(var1);
      return ((ByteBuffer)((ByteBuffer)this.memory).duplicate().position(var1).limit(var1 + var2)).slice();
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      this.checkIndex(var1, var2);
      var1 = this.idx(var1);
      return (ByteBuffer)this.internalNioBuffer().clear().position(var1).limit(var1 + var2);
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

   private long addr(int var1) {
      return this.memoryAddress + (long)var1;
   }

   protected Recycler recycler() {
      return RECYCLER;
   }

   protected ByteBuffer newInternalNioBuffer(Object var1) {
      return this.newInternalNioBuffer((ByteBuffer)var1);
   }

   PooledUnsafeDirectByteBuf(Recycler.Handle var1, int var2, Object var3) {
      this(var1, var2);
   }

   static {
      NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
      RECYCLER = new Recycler() {
         protected PooledUnsafeDirectByteBuf newObject(Recycler.Handle var1) {
            return new PooledUnsafeDirectByteBuf(var1, 0);
         }

         protected Object newObject(Recycler.Handle var1) {
            return this.newObject(var1);
         }
      };
   }
}
