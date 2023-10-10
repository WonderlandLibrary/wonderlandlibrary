package io.netty.buffer;

import io.netty.util.Recycler;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

final class PooledHeapByteBuf extends PooledByteBuf {
   private static final Recycler RECYCLER = new Recycler() {
      protected PooledHeapByteBuf newObject(Recycler.Handle var1) {
         return new PooledHeapByteBuf(var1, 0);
      }

      protected Object newObject(Recycler.Handle var1) {
         return this.newObject(var1);
      }
   };

   static PooledHeapByteBuf newInstance(int var0) {
      PooledHeapByteBuf var1 = (PooledHeapByteBuf)RECYCLER.get();
      var1.setRefCnt(1);
      var1.maxCapacity(var0);
      return var1;
   }

   private PooledHeapByteBuf(Recycler.Handle var1, int var2) {
      super(var1, var2);
   }

   public boolean isDirect() {
      return false;
   }

   protected byte _getByte(int var1) {
      return ((byte[])this.memory)[this.idx(var1)];
   }

   protected short _getShort(int var1) {
      var1 = this.idx(var1);
      return (short)(((byte[])this.memory)[var1] << 8 | ((byte[])this.memory)[var1 + 1] & 255);
   }

   protected int _getUnsignedMedium(int var1) {
      var1 = this.idx(var1);
      return (((byte[])this.memory)[var1] & 255) << 16 | (((byte[])this.memory)[var1 + 1] & 255) << 8 | ((byte[])this.memory)[var1 + 2] & 255;
   }

   protected int _getInt(int var1) {
      var1 = this.idx(var1);
      return (((byte[])this.memory)[var1] & 255) << 24 | (((byte[])this.memory)[var1 + 1] & 255) << 16 | (((byte[])this.memory)[var1 + 2] & 255) << 8 | ((byte[])this.memory)[var1 + 3] & 255;
   }

   protected long _getLong(int var1) {
      var1 = this.idx(var1);
      return ((long)((byte[])this.memory)[var1] & 255L) << 56 | ((long)((byte[])this.memory)[var1 + 1] & 255L) << 48 | ((long)((byte[])this.memory)[var1 + 2] & 255L) << 40 | ((long)((byte[])this.memory)[var1 + 3] & 255L) << 32 | ((long)((byte[])this.memory)[var1 + 4] & 255L) << 24 | ((long)((byte[])this.memory)[var1 + 5] & 255L) << 16 | ((long)((byte[])this.memory)[var1 + 6] & 255L) << 8 | (long)((byte[])this.memory)[var1 + 7] & 255L;
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.checkDstIndex(var1, var4, var3, var2.capacity());
      if (var2.hasMemoryAddress()) {
         PlatformDependent.copyMemory((byte[])this.memory, this.idx(var1), var2.memoryAddress() + (long)var3, (long)var4);
      } else if (var2.hasArray()) {
         this.getBytes(var1, var2.array(), var2.arrayOffset() + var3, var4);
      } else {
         var2.setBytes(var3, (byte[])this.memory, this.idx(var1), var4);
      }

      return this;
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      this.checkDstIndex(var1, var4, var3, var2.length);
      System.arraycopy(this.memory, this.idx(var1), var2, var3, var4);
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      this.checkIndex(var1);
      var2.put((byte[])this.memory, this.idx(var1), Math.min(this.capacity() - var1, var2.remaining()));
      return this;
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      this.checkIndex(var1, var3);
      var2.write((byte[])this.memory, this.idx(var1), var3);
      return this;
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      return this.getBytes(var1, var2, var3, false);
   }

   private int getBytes(int var1, GatheringByteChannel var2, int var3, boolean var4) throws IOException {
      this.checkIndex(var1, var3);
      var1 = this.idx(var1);
      ByteBuffer var5;
      if (var4) {
         var5 = this.internalNioBuffer();
      } else {
         var5 = ByteBuffer.wrap((byte[])this.memory);
      }

      return var2.write((ByteBuffer)var5.clear().position(var1).limit(var1 + var3));
   }

   public int readBytes(GatheringByteChannel var1, int var2) throws IOException {
      this.checkReadableBytes(var2);
      int var3 = this.getBytes(this.readerIndex, var1, var2, true);
      this.readerIndex += var3;
      return var3;
   }

   protected void _setByte(int var1, int var2) {
      ((byte[])this.memory)[this.idx(var1)] = (byte)var2;
   }

   protected void _setShort(int var1, int var2) {
      var1 = this.idx(var1);
      ((byte[])this.memory)[var1] = (byte)(var2 >>> 8);
      ((byte[])this.memory)[var1 + 1] = (byte)var2;
   }

   protected void _setMedium(int var1, int var2) {
      var1 = this.idx(var1);
      ((byte[])this.memory)[var1] = (byte)(var2 >>> 16);
      ((byte[])this.memory)[var1 + 1] = (byte)(var2 >>> 8);
      ((byte[])this.memory)[var1 + 2] = (byte)var2;
   }

   protected void _setInt(int var1, int var2) {
      var1 = this.idx(var1);
      ((byte[])this.memory)[var1] = (byte)(var2 >>> 24);
      ((byte[])this.memory)[var1 + 1] = (byte)(var2 >>> 16);
      ((byte[])this.memory)[var1 + 2] = (byte)(var2 >>> 8);
      ((byte[])this.memory)[var1 + 3] = (byte)var2;
   }

   protected void _setLong(int var1, long var2) {
      var1 = this.idx(var1);
      ((byte[])this.memory)[var1] = (byte)((int)(var2 >>> 56));
      ((byte[])this.memory)[var1 + 1] = (byte)((int)(var2 >>> 48));
      ((byte[])this.memory)[var1 + 2] = (byte)((int)(var2 >>> 40));
      ((byte[])this.memory)[var1 + 3] = (byte)((int)(var2 >>> 32));
      ((byte[])this.memory)[var1 + 4] = (byte)((int)(var2 >>> 24));
      ((byte[])this.memory)[var1 + 5] = (byte)((int)(var2 >>> 16));
      ((byte[])this.memory)[var1 + 6] = (byte)((int)(var2 >>> 8));
      ((byte[])this.memory)[var1 + 7] = (byte)((int)var2);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.checkSrcIndex(var1, var4, var3, var2.capacity());
      if (var2.hasMemoryAddress()) {
         PlatformDependent.copyMemory(var2.memoryAddress() + (long)var3, (byte[])this.memory, this.idx(var1), (long)var4);
      } else if (var2.hasArray()) {
         this.setBytes(var1, var2.array(), var2.arrayOffset() + var3, var4);
      } else {
         var2.getBytes(var3, (byte[])this.memory, this.idx(var1), var4);
      }

      return this;
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      this.checkSrcIndex(var1, var4, var3, var2.length);
      System.arraycopy(var2, var3, this.memory, this.idx(var1), var4);
      return this;
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      int var3 = var2.remaining();
      this.checkIndex(var1, var3);
      var2.get((byte[])this.memory, this.idx(var1), var3);
      return this;
   }

   public int setBytes(int var1, InputStream var2, int var3) throws IOException {
      this.checkIndex(var1, var3);
      return var2.read((byte[])this.memory, this.idx(var1), var3);
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) throws IOException {
      this.checkIndex(var1, var3);
      var1 = this.idx(var1);

      try {
         return var2.read((ByteBuffer)this.internalNioBuffer().clear().position(var1).limit(var1 + var3));
      } catch (ClosedChannelException var5) {
         return -1;
      }
   }

   public ByteBuf copy(int var1, int var2) {
      this.checkIndex(var1, var2);
      ByteBuf var3 = this.alloc().heapBuffer(var2, this.maxCapacity());
      var3.writeBytes((byte[])this.memory, this.idx(var1), var2);
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
      ByteBuffer var3 = ByteBuffer.wrap((byte[])this.memory, var1, var2);
      return var3.slice();
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      this.checkIndex(var1, var2);
      var1 = this.idx(var1);
      return (ByteBuffer)this.internalNioBuffer().clear().position(var1).limit(var1 + var2);
   }

   public boolean hasArray() {
      return true;
   }

   public byte[] array() {
      return (byte[])this.memory;
   }

   public int arrayOffset() {
      return this.offset;
   }

   public boolean hasMemoryAddress() {
      return false;
   }

   public long memoryAddress() {
      throw new UnsupportedOperationException();
   }

   protected ByteBuffer newInternalNioBuffer(byte[] var1) {
      return ByteBuffer.wrap(var1);
   }

   protected Recycler recycler() {
      return RECYCLER;
   }

   protected ByteBuffer newInternalNioBuffer(Object var1) {
      return this.newInternalNioBuffer((byte[])var1);
   }

   PooledHeapByteBuf(Recycler.Handle var1, int var2, Object var3) {
      this(var1, var2);
   }
}
