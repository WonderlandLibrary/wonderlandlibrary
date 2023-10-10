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

public class UnpooledHeapByteBuf extends AbstractReferenceCountedByteBuf {
   private final ByteBufAllocator alloc;
   private byte[] array;
   private ByteBuffer tmpNioBuf;

   protected UnpooledHeapByteBuf(ByteBufAllocator var1, int var2, int var3) {
      this(var1, new byte[var2], 0, 0, var3);
   }

   protected UnpooledHeapByteBuf(ByteBufAllocator var1, byte[] var2, int var3) {
      this(var1, var2, 0, var2.length, var3);
   }

   private UnpooledHeapByteBuf(ByteBufAllocator var1, byte[] var2, int var3, int var4, int var5) {
      super(var5);
      if (var1 == null) {
         throw new NullPointerException("alloc");
      } else if (var2 == null) {
         throw new NullPointerException("initialArray");
      } else if (var2.length > var5) {
         throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", var2.length, var5));
      } else {
         this.alloc = var1;
         this.setArray(var2);
         this.setIndex(var3, var4);
      }
   }

   private void setArray(byte[] var1) {
      this.array = var1;
      this.tmpNioBuf = null;
   }

   public ByteBufAllocator alloc() {
      return this.alloc;
   }

   public ByteOrder order() {
      return ByteOrder.BIG_ENDIAN;
   }

   public boolean isDirect() {
      return false;
   }

   public int capacity() {
      this.ensureAccessible();
      return this.array.length;
   }

   public ByteBuf capacity(int var1) {
      this.ensureAccessible();
      if (var1 >= 0 && var1 <= this.maxCapacity()) {
         int var2 = this.array.length;
         byte[] var3;
         if (var1 > var2) {
            var3 = new byte[var1];
            System.arraycopy(this.array, 0, var3, 0, this.array.length);
            this.setArray(var3);
         } else if (var1 < var2) {
            var3 = new byte[var1];
            int var4 = this.readerIndex();
            if (var4 < var1) {
               int var5 = this.writerIndex();
               if (var5 > var1) {
                  var5 = var1;
                  this.writerIndex(var1);
               }

               System.arraycopy(this.array, var4, var3, var4, var5 - var4);
            } else {
               this.setIndex(var1, var1);
            }

            this.setArray(var3);
         }

         return this;
      } else {
         throw new IllegalArgumentException("newCapacity: " + var1);
      }
   }

   public boolean hasArray() {
      return true;
   }

   public byte[] array() {
      this.ensureAccessible();
      return this.array;
   }

   public int arrayOffset() {
      return 0;
   }

   public boolean hasMemoryAddress() {
      return false;
   }

   public long memoryAddress() {
      throw new UnsupportedOperationException();
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.checkDstIndex(var1, var4, var3, var2.capacity());
      if (var2.hasMemoryAddress()) {
         PlatformDependent.copyMemory(this.array, var1, var2.memoryAddress() + (long)var3, (long)var4);
      } else if (var2.hasArray()) {
         this.getBytes(var1, var2.array(), var2.arrayOffset() + var3, var4);
      } else {
         var2.setBytes(var3, this.array, var1, var4);
      }

      return this;
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      this.checkDstIndex(var1, var4, var3, var2.length);
      System.arraycopy(this.array, var1, var2, var3, var4);
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      this.ensureAccessible();
      var2.put(this.array, var1, Math.min(this.capacity() - var1, var2.remaining()));
      return this;
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      this.ensureAccessible();
      var2.write(this.array, var1, var3);
      return this;
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      this.ensureAccessible();
      return this.getBytes(var1, var2, var3, false);
   }

   private int getBytes(int var1, GatheringByteChannel var2, int var3, boolean var4) throws IOException {
      this.ensureAccessible();
      ByteBuffer var5;
      if (var4) {
         var5 = this.internalNioBuffer();
      } else {
         var5 = ByteBuffer.wrap(this.array);
      }

      return var2.write((ByteBuffer)var5.clear().position(var1).limit(var1 + var3));
   }

   public int readBytes(GatheringByteChannel var1, int var2) throws IOException {
      this.checkReadableBytes(var2);
      int var3 = this.getBytes(this.readerIndex, var1, var2, true);
      this.readerIndex += var3;
      return var3;
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.checkSrcIndex(var1, var4, var3, var2.capacity());
      if (var2.hasMemoryAddress()) {
         PlatformDependent.copyMemory(var2.memoryAddress() + (long)var3, this.array, var1, (long)var4);
      } else if (var2.hasArray()) {
         this.setBytes(var1, var2.array(), var2.arrayOffset() + var3, var4);
      } else {
         var2.getBytes(var3, this.array, var1, var4);
      }

      return this;
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      this.checkSrcIndex(var1, var4, var3, var2.length);
      System.arraycopy(var2, var3, this.array, var1, var4);
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      this.ensureAccessible();
      var2.get(this.array, var1, var2.remaining());
      return this;
   }

   public int setBytes(int var1, InputStream var2, int var3) throws IOException {
      this.ensureAccessible();
      return var2.read(this.array, var1, var3);
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) throws IOException {
      this.ensureAccessible();

      try {
         return var2.read((ByteBuffer)this.internalNioBuffer().clear().position(var1).limit(var1 + var3));
      } catch (ClosedChannelException var5) {
         return -1;
      }
   }

   public int nioBufferCount() {
      return 1;
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      this.ensureAccessible();
      return ByteBuffer.wrap(this.array, var1, var2).slice();
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      return new ByteBuffer[]{this.nioBuffer(var1, var2)};
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      return (ByteBuffer)this.internalNioBuffer().clear().position(var1).limit(var1 + var2);
   }

   public byte getByte(int var1) {
      this.ensureAccessible();
      return this._getByte(var1);
   }

   protected byte _getByte(int var1) {
      return this.array[var1];
   }

   public short getShort(int var1) {
      this.ensureAccessible();
      return this._getShort(var1);
   }

   protected short _getShort(int var1) {
      return (short)(this.array[var1] << 8 | this.array[var1 + 1] & 255);
   }

   public int getUnsignedMedium(int var1) {
      this.ensureAccessible();
      return this._getUnsignedMedium(var1);
   }

   protected int _getUnsignedMedium(int var1) {
      return (this.array[var1] & 255) << 16 | (this.array[var1 + 1] & 255) << 8 | this.array[var1 + 2] & 255;
   }

   public int getInt(int var1) {
      this.ensureAccessible();
      return this._getInt(var1);
   }

   protected int _getInt(int var1) {
      return (this.array[var1] & 255) << 24 | (this.array[var1 + 1] & 255) << 16 | (this.array[var1 + 2] & 255) << 8 | this.array[var1 + 3] & 255;
   }

   public long getLong(int var1) {
      this.ensureAccessible();
      return this._getLong(var1);
   }

   protected long _getLong(int var1) {
      return ((long)this.array[var1] & 255L) << 56 | ((long)this.array[var1 + 1] & 255L) << 48 | ((long)this.array[var1 + 2] & 255L) << 40 | ((long)this.array[var1 + 3] & 255L) << 32 | ((long)this.array[var1 + 4] & 255L) << 24 | ((long)this.array[var1 + 5] & 255L) << 16 | ((long)this.array[var1 + 6] & 255L) << 8 | (long)this.array[var1 + 7] & 255L;
   }

   public ByteBuf setByte(int var1, int var2) {
      this.ensureAccessible();
      this._setByte(var1, var2);
      return this;
   }

   protected void _setByte(int var1, int var2) {
      this.array[var1] = (byte)var2;
   }

   public ByteBuf setShort(int var1, int var2) {
      this.ensureAccessible();
      this._setShort(var1, var2);
      return this;
   }

   protected void _setShort(int var1, int var2) {
      this.array[var1] = (byte)(var2 >>> 8);
      this.array[var1 + 1] = (byte)var2;
   }

   public ByteBuf setMedium(int var1, int var2) {
      this.ensureAccessible();
      this._setMedium(var1, var2);
      return this;
   }

   protected void _setMedium(int var1, int var2) {
      this.array[var1] = (byte)(var2 >>> 16);
      this.array[var1 + 1] = (byte)(var2 >>> 8);
      this.array[var1 + 2] = (byte)var2;
   }

   public ByteBuf setInt(int var1, int var2) {
      this.ensureAccessible();
      this._setInt(var1, var2);
      return this;
   }

   protected void _setInt(int var1, int var2) {
      this.array[var1] = (byte)(var2 >>> 24);
      this.array[var1 + 1] = (byte)(var2 >>> 16);
      this.array[var1 + 2] = (byte)(var2 >>> 8);
      this.array[var1 + 3] = (byte)var2;
   }

   public ByteBuf setLong(int var1, long var2) {
      this.ensureAccessible();
      this._setLong(var1, var2);
      return this;
   }

   protected void _setLong(int var1, long var2) {
      this.array[var1] = (byte)((int)(var2 >>> 56));
      this.array[var1 + 1] = (byte)((int)(var2 >>> 48));
      this.array[var1 + 2] = (byte)((int)(var2 >>> 40));
      this.array[var1 + 3] = (byte)((int)(var2 >>> 32));
      this.array[var1 + 4] = (byte)((int)(var2 >>> 24));
      this.array[var1 + 5] = (byte)((int)(var2 >>> 16));
      this.array[var1 + 6] = (byte)((int)(var2 >>> 8));
      this.array[var1 + 7] = (byte)((int)var2);
   }

   public ByteBuf copy(int var1, int var2) {
      this.checkIndex(var1, var2);
      byte[] var3 = new byte[var2];
      System.arraycopy(this.array, var1, var3, 0, var2);
      return new UnpooledHeapByteBuf(this.alloc(), var3, this.maxCapacity());
   }

   private ByteBuffer internalNioBuffer() {
      ByteBuffer var1 = this.tmpNioBuf;
      if (var1 == null) {
         this.tmpNioBuf = var1 = ByteBuffer.wrap(this.array);
      }

      return var1;
   }

   protected void deallocate() {
      this.array = null;
   }

   public ByteBuf unwrap() {
      return null;
   }
}
