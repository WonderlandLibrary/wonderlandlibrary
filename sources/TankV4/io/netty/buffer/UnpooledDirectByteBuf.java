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

public class UnpooledDirectByteBuf extends AbstractReferenceCountedByteBuf {
   private final ByteBufAllocator alloc;
   private ByteBuffer buffer;
   private ByteBuffer tmpNioBuf;
   private int capacity;
   private boolean doNotFree;

   protected UnpooledDirectByteBuf(ByteBufAllocator var1, int var2, int var3) {
      super(var3);
      if (var1 == null) {
         throw new NullPointerException("alloc");
      } else if (var2 < 0) {
         throw new IllegalArgumentException("initialCapacity: " + var2);
      } else if (var3 < 0) {
         throw new IllegalArgumentException("maxCapacity: " + var3);
      } else if (var2 > var3) {
         throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", var2, var3));
      } else {
         this.alloc = var1;
         this.setByteBuffer(ByteBuffer.allocateDirect(var2));
      }
   }

   protected UnpooledDirectByteBuf(ByteBufAllocator var1, ByteBuffer var2, int var3) {
      super(var3);
      if (var1 == null) {
         throw new NullPointerException("alloc");
      } else if (var2 == null) {
         throw new NullPointerException("initialBuffer");
      } else if (!var2.isDirect()) {
         throw new IllegalArgumentException("initialBuffer is not a direct buffer.");
      } else if (var2.isReadOnly()) {
         throw new IllegalArgumentException("initialBuffer is a read-only buffer.");
      } else {
         int var4 = var2.remaining();
         if (var4 > var3) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", var4, var3));
         } else {
            this.alloc = var1;
            this.doNotFree = true;
            this.setByteBuffer(var2.slice().order(ByteOrder.BIG_ENDIAN));
            this.writerIndex(var4);
         }
      }
   }

   protected ByteBuffer allocateDirect(int var1) {
      return ByteBuffer.allocateDirect(var1);
   }

   protected void freeDirect(ByteBuffer var1) {
      PlatformDependent.freeDirectBuffer(var1);
   }

   private void setByteBuffer(ByteBuffer var1) {
      ByteBuffer var2 = this.buffer;
      if (var2 != null) {
         if (this.doNotFree) {
            this.doNotFree = false;
         } else {
            this.freeDirect(var2);
         }
      }

      this.buffer = var1;
      this.tmpNioBuf = null;
      this.capacity = var1.remaining();
   }

   public boolean isDirect() {
      return true;
   }

   public int capacity() {
      return this.capacity;
   }

   public ByteBuf capacity(int var1) {
      this.ensureAccessible();
      if (var1 >= 0 && var1 <= this.maxCapacity()) {
         int var2 = this.readerIndex();
         int var3 = this.writerIndex();
         int var4 = this.capacity;
         ByteBuffer var5;
         ByteBuffer var6;
         if (var1 > var4) {
            var5 = this.buffer;
            var6 = this.allocateDirect(var1);
            var5.position(0).limit(var5.capacity());
            var6.position(0).limit(var5.capacity());
            var6.put(var5);
            var6.clear();
            this.setByteBuffer(var6);
         } else if (var1 < var4) {
            var5 = this.buffer;
            var6 = this.allocateDirect(var1);
            if (var2 < var1) {
               if (var3 > var1) {
                  var3 = var1;
                  this.writerIndex(var1);
               }

               var5.position(var2).limit(var3);
               var6.position(var2).limit(var3);
               var6.put(var5);
               var6.clear();
            } else {
               this.setIndex(var1, var1);
            }

            this.setByteBuffer(var6);
         }

         return this;
      } else {
         throw new IllegalArgumentException("newCapacity: " + var1);
      }
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
      return false;
   }

   public long memoryAddress() {
      throw new UnsupportedOperationException();
   }

   public byte getByte(int var1) {
      this.ensureAccessible();
      return this._getByte(var1);
   }

   protected byte _getByte(int var1) {
      return this.buffer.get(var1);
   }

   public short getShort(int var1) {
      this.ensureAccessible();
      return this._getShort(var1);
   }

   protected short _getShort(int var1) {
      return this.buffer.getShort(var1);
   }

   public int getUnsignedMedium(int var1) {
      this.ensureAccessible();
      return this._getUnsignedMedium(var1);
   }

   protected int _getUnsignedMedium(int var1) {
      return (this.getByte(var1) & 255) << 16 | (this.getByte(var1 + 1) & 255) << 8 | this.getByte(var1 + 2) & 255;
   }

   public int getInt(int var1) {
      this.ensureAccessible();
      return this._getInt(var1);
   }

   protected int _getInt(int var1) {
      return this.buffer.getInt(var1);
   }

   public long getLong(int var1) {
      this.ensureAccessible();
      return this._getLong(var1);
   }

   protected long _getLong(int var1) {
      return this.buffer.getLong(var1);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.checkDstIndex(var1, var4, var3, var2.capacity());
      if (var2.hasArray()) {
         this.getBytes(var1, var2.array(), var2.arrayOffset() + var3, var4);
      } else if (var2.nioBufferCount() > 0) {
         ByteBuffer[] var5 = var2.nioBuffers(var3, var4);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            ByteBuffer var8 = var5[var7];
            int var9 = var8.remaining();
            this.getBytes(var1, var8);
            var1 += var9;
         }
      } else {
         var2.setBytes(var3, (ByteBuf)this, var1, var4);
      }

      return this;
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      this.getBytes(var1, var2, var3, var4, false);
      return this;
   }

   private void getBytes(int var1, byte[] var2, int var3, int var4, boolean var5) {
      this.checkDstIndex(var1, var4, var3, var2.length);
      if (var3 >= 0 && var3 <= var2.length - var4) {
         ByteBuffer var6;
         if (var5) {
            var6 = this.internalNioBuffer();
         } else {
            var6 = this.buffer.duplicate();
         }

         var6.clear().position(var1).limit(var1 + var4);
         var6.get(var2, var3, var4);
      } else {
         throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", var3, var4, var2.length));
      }
   }

   public ByteBuf readBytes(byte[] var1, int var2, int var3) {
      this.checkReadableBytes(var3);
      this.getBytes(this.readerIndex, var1, var2, var3, true);
      this.readerIndex += var3;
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      this.getBytes(var1, var2, false);
      return this;
   }

   private void getBytes(int var1, ByteBuffer var2, boolean var3) {
      this.checkIndex(var1);
      if (var2 == null) {
         throw new NullPointerException("dst");
      } else {
         int var4 = Math.min(this.capacity() - var1, var2.remaining());
         ByteBuffer var5;
         if (var3) {
            var5 = this.internalNioBuffer();
         } else {
            var5 = this.buffer.duplicate();
         }

         var5.clear().position(var1).limit(var1 + var4);
         var2.put(var5);
      }
   }

   public ByteBuf readBytes(ByteBuffer var1) {
      int var2 = var1.remaining();
      this.checkReadableBytes(var2);
      this.getBytes(this.readerIndex, var1, true);
      this.readerIndex += var2;
      return this;
   }

   public ByteBuf setByte(int var1, int var2) {
      this.ensureAccessible();
      this._setByte(var1, var2);
      return this;
   }

   protected void _setByte(int var1, int var2) {
      this.buffer.put(var1, (byte)var2);
   }

   public ByteBuf setShort(int var1, int var2) {
      this.ensureAccessible();
      this._setShort(var1, var2);
      return this;
   }

   protected void _setShort(int var1, int var2) {
      this.buffer.putShort(var1, (short)var2);
   }

   public ByteBuf setMedium(int var1, int var2) {
      this.ensureAccessible();
      this._setMedium(var1, var2);
      return this;
   }

   protected void _setMedium(int var1, int var2) {
      this.setByte(var1, (byte)(var2 >>> 16));
      this.setByte(var1 + 1, (byte)(var2 >>> 8));
      this.setByte(var1 + 2, (byte)var2);
   }

   public ByteBuf setInt(int var1, int var2) {
      this.ensureAccessible();
      this._setInt(var1, var2);
      return this;
   }

   protected void _setInt(int var1, int var2) {
      this.buffer.putInt(var1, var2);
   }

   public ByteBuf setLong(int var1, long var2) {
      this.ensureAccessible();
      this._setLong(var1, var2);
      return this;
   }

   protected void _setLong(int var1, long var2) {
      this.buffer.putLong(var1, var2);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.checkSrcIndex(var1, var4, var3, var2.capacity());
      if (this.buffer.hasArray()) {
         var2.getBytes(var3, this.buffer.array(), var1 + this.buffer.arrayOffset(), var4);
      } else if (var2.nioBufferCount() > 0) {
         ByteBuffer[] var5 = var2.nioBuffers(var3, var4);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            ByteBuffer var8 = var5[var7];
            int var9 = var8.remaining();
            this.setBytes(var1, var8);
            var1 += var9;
         }
      } else {
         var2.getBytes(var3, (ByteBuf)this, var1, var4);
      }

      return this;
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      this.checkSrcIndex(var1, var4, var3, var2.length);
      ByteBuffer var5 = this.internalNioBuffer();
      var5.clear().position(var1).limit(var1 + var4);
      var5.put(var2, var3, var4);
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      this.ensureAccessible();
      ByteBuffer var3 = this.internalNioBuffer();
      if (var2 == var3) {
         var2 = var2.duplicate();
      }

      var3.clear().position(var1).limit(var1 + var2.remaining());
      var3.put(var2);
      return this;
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      this.getBytes(var1, var2, var3, false);
      return this;
   }

   private void getBytes(int var1, OutputStream var2, int var3, boolean var4) throws IOException {
      this.ensureAccessible();
      if (var3 != 0) {
         if (this.buffer.hasArray()) {
            var2.write(this.buffer.array(), var1 + this.buffer.arrayOffset(), var3);
         } else {
            byte[] var5 = new byte[var3];
            ByteBuffer var6;
            if (var4) {
               var6 = this.internalNioBuffer();
            } else {
               var6 = this.buffer.duplicate();
            }

            var6.clear().position(var1);
            var6.get(var5);
            var2.write(var5);
         }

      }
   }

   public ByteBuf readBytes(OutputStream var1, int var2) throws IOException {
      this.checkReadableBytes(var2);
      this.getBytes(this.readerIndex, var1, var2, true);
      this.readerIndex += var2;
      return this;
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      return this.getBytes(var1, var2, var3, false);
   }

   private int getBytes(int var1, GatheringByteChannel var2, int var3, boolean var4) throws IOException {
      this.ensureAccessible();
      if (var3 == 0) {
         return 0;
      } else {
         ByteBuffer var5;
         if (var4) {
            var5 = this.internalNioBuffer();
         } else {
            var5 = this.buffer.duplicate();
         }

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

   public int setBytes(int var1, InputStream var2, int var3) throws IOException {
      this.ensureAccessible();
      if (this.buffer.hasArray()) {
         return var2.read(this.buffer.array(), this.buffer.arrayOffset() + var1, var3);
      } else {
         byte[] var4 = new byte[var3];
         int var5 = var2.read(var4);
         if (var5 <= 0) {
            return var5;
         } else {
            ByteBuffer var6 = this.internalNioBuffer();
            var6.clear().position(var1);
            var6.put(var4, 0, var5);
            return var5;
         }
      }
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) throws IOException {
      this.ensureAccessible();
      ByteBuffer var4 = this.internalNioBuffer();
      var4.clear().position(var1).limit(var1 + var3);

      try {
         return var2.read(this.tmpNioBuf);
      } catch (ClosedChannelException var6) {
         return -1;
      }
   }

   public int nioBufferCount() {
      return 1;
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      return new ByteBuffer[]{this.nioBuffer(var1, var2)};
   }

   public ByteBuf copy(int var1, int var2) {
      this.ensureAccessible();

      ByteBuffer var3;
      try {
         var3 = (ByteBuffer)this.buffer.duplicate().clear().position(var1).limit(var1 + var2);
      } catch (IllegalArgumentException var5) {
         throw new IndexOutOfBoundsException("Too many bytes to read - Need " + (var1 + var2));
      }

      return this.alloc().directBuffer(var2, this.maxCapacity()).writeBytes(var3);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      return (ByteBuffer)this.internalNioBuffer().clear().position(var1).limit(var1 + var2);
   }

   private ByteBuffer internalNioBuffer() {
      ByteBuffer var1 = this.tmpNioBuf;
      if (var1 == null) {
         this.tmpNioBuf = var1 = this.buffer.duplicate();
      }

      return var1;
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      return ((ByteBuffer)this.buffer.duplicate().position(var1).limit(var1 + var2)).slice();
   }

   protected void deallocate() {
      ByteBuffer var1 = this.buffer;
      if (var1 != null) {
         this.buffer = null;
         if (!this.doNotFree) {
            this.freeDirect(var1);
         }

      }
   }

   public ByteBuf unwrap() {
      return null;
   }
}
