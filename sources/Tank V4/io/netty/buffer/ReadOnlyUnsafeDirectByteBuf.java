package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class ReadOnlyUnsafeDirectByteBuf extends ReadOnlyByteBufferBuf {
   private static final boolean NATIVE_ORDER;
   private final long memoryAddress;

   public ReadOnlyUnsafeDirectByteBuf(ByteBufAllocator var1, ByteBuffer var2) {
      super(var1, var2);
      this.memoryAddress = PlatformDependent.directBufferAddress(var2);
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
         if (var2.hasMemoryAddress()) {
            PlatformDependent.copyMemory(this.addr(var1), var2.memoryAddress() + (long)var3, (long)var4);
         } else if (var2.hasArray()) {
            PlatformDependent.copyMemory(this.addr(var1), var2.array(), var2.arrayOffset() + var3, (long)var4);
         } else {
            var2.setBytes(var3, (ByteBuf)this, var1, var4);
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
         throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", var3, var4, var2.length));
      }
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      this.checkIndex(var1);
      if (var2 == null) {
         throw new NullPointerException("dst");
      } else {
         int var3 = Math.min(this.capacity() - var1, var2.remaining());
         ByteBuffer var4 = this.internalNioBuffer();
         var4.clear().position(var1).limit(var1 + var3);
         var2.put(var4);
         return this;
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

   private long addr(int var1) {
      return this.memoryAddress + (long)var1;
   }

   static {
      NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
   }
}
