package io.netty.buffer;

import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

class ReadOnlyByteBufferBuf extends AbstractReferenceCountedByteBuf {
   protected final ByteBuffer buffer;
   private final ByteBufAllocator allocator;
   private ByteBuffer tmpNioBuf;

   public ReadOnlyByteBufferBuf(ByteBufAllocator var1, ByteBuffer var2) {
      super(var2.remaining());
      if (!var2.isReadOnly()) {
         throw new IllegalArgumentException("must be a readonly buffer: " + StringUtil.simpleClassName((Object)var2));
      } else {
         this.allocator = var1;
         this.buffer = var2.slice().order(ByteOrder.BIG_ENDIAN);
         this.writerIndex(this.buffer.limit());
      }
   }

   protected void deallocate() {
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
      this.checkDstIndex(var1, var4, var3, var2.length);
      if (var3 >= 0 && var3 <= var2.length - var4) {
         ByteBuffer var5 = this.internalNioBuffer();
         var5.clear().position(var1).limit(var1 + var4);
         var5.get(var2, var3, var4);
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

   protected void _setByte(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   protected void _setShort(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   protected void _setMedium(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   protected void _setInt(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   protected void _setLong(int var1, long var2) {
      throw new ReadOnlyBufferException();
   }

   public int capacity() {
      return this.maxCapacity();
   }

   public ByteBuf capacity(int var1) {
      throw new ReadOnlyBufferException();
   }

   public ByteBufAllocator alloc() {
      return this.allocator;
   }

   public ByteOrder order() {
      return ByteOrder.BIG_ENDIAN;
   }

   public ByteBuf unwrap() {
      return null;
   }

   public boolean isDirect() {
      return this.buffer.isDirect();
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      this.ensureAccessible();
      if (var3 == 0) {
         return this;
      } else {
         if (this.buffer.hasArray()) {
            var2.write(this.buffer.array(), var1 + this.buffer.arrayOffset(), var3);
         } else {
            byte[] var4 = new byte[var3];
            ByteBuffer var5 = this.internalNioBuffer();
            var5.clear().position(var1);
            var5.get(var4);
            var2.write(var4);
         }

         return this;
      }
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      this.ensureAccessible();
      if (var3 == 0) {
         return 0;
      } else {
         ByteBuffer var4 = this.internalNioBuffer();
         var4.clear().position(var1).limit(var1 + var3);
         return var2.write(var4);
      }
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      throw new ReadOnlyBufferException();
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      throw new ReadOnlyBufferException();
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      throw new ReadOnlyBufferException();
   }

   public int setBytes(int var1, InputStream var2, int var3) throws IOException {
      throw new ReadOnlyBufferException();
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) throws IOException {
      throw new ReadOnlyBufferException();
   }

   protected final ByteBuffer internalNioBuffer() {
      ByteBuffer var1 = this.tmpNioBuf;
      if (var1 == null) {
         this.tmpNioBuf = var1 = this.buffer.duplicate();
      }

      return var1;
   }

   public ByteBuf copy(int var1, int var2) {
      this.ensureAccessible();

      ByteBuffer var3;
      try {
         var3 = (ByteBuffer)this.internalNioBuffer().clear().position(var1).limit(var1 + var2);
      } catch (IllegalArgumentException var5) {
         throw new IndexOutOfBoundsException("Too many bytes to read - Need " + (var1 + var2));
      }

      ByteBuffer var4 = ByteBuffer.allocateDirect(var2);
      var4.put(var3);
      var4.order(this.order());
      var4.clear();
      return new UnpooledDirectByteBuf(this.alloc(), var4, this.maxCapacity());
   }

   public int nioBufferCount() {
      return 1;
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      return new ByteBuffer[]{this.nioBuffer(var1, var2)};
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      return (ByteBuffer)this.buffer.duplicate().position(var1).limit(var2);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      this.ensureAccessible();
      return (ByteBuffer)this.internalNioBuffer().clear().position(var1).limit(var1 + var2);
   }

   public boolean hasArray() {
      return this.buffer.hasArray();
   }

   public byte[] array() {
      return this.buffer.array();
   }

   public int arrayOffset() {
      return this.buffer.arrayOffset();
   }

   public boolean hasMemoryAddress() {
      return false;
   }

   public long memoryAddress() {
      throw new UnsupportedOperationException();
   }
}
