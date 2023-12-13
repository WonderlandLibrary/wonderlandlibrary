package io.netty.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class ReadOnlyByteBuf extends AbstractDerivedByteBuf {
   private final ByteBuf buffer;

   public ReadOnlyByteBuf(ByteBuf var1) {
      super(var1.maxCapacity());
      if (!(var1 instanceof ReadOnlyByteBuf) && !(var1 instanceof DuplicatedByteBuf)) {
         this.buffer = var1;
      } else {
         this.buffer = var1.unwrap();
      }

      this.setIndex(var1.readerIndex(), var1.writerIndex());
   }

   public boolean isWritable() {
      return false;
   }

   public boolean isWritable(int var1) {
      return false;
   }

   public ByteBuf unwrap() {
      return this.buffer;
   }

   public ByteBufAllocator alloc() {
      return this.buffer.alloc();
   }

   public ByteOrder order() {
      return this.buffer.order();
   }

   public boolean isDirect() {
      return this.buffer.isDirect();
   }

   public boolean hasArray() {
      return false;
   }

   public byte[] array() {
      throw new ReadOnlyBufferException();
   }

   public int arrayOffset() {
      throw new ReadOnlyBufferException();
   }

   public boolean hasMemoryAddress() {
      return false;
   }

   public long memoryAddress() {
      throw new ReadOnlyBufferException();
   }

   public ByteBuf discardReadBytes() {
      throw new ReadOnlyBufferException();
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

   public ByteBuf setByte(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   protected void _setByte(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   public ByteBuf setShort(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   protected void _setShort(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   public ByteBuf setMedium(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   protected void _setMedium(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   public ByteBuf setInt(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   protected void _setInt(int var1, int var2) {
      throw new ReadOnlyBufferException();
   }

   public ByteBuf setLong(int var1, long var2) {
      throw new ReadOnlyBufferException();
   }

   protected void _setLong(int var1, long var2) {
      throw new ReadOnlyBufferException();
   }

   public int setBytes(int var1, InputStream var2, int var3) {
      throw new ReadOnlyBufferException();
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) {
      throw new ReadOnlyBufferException();
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      return this.buffer.getBytes(var1, var2, var3);
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      this.buffer.getBytes(var1, var2, var3);
      return this;
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      this.buffer.getBytes(var1, var2, var3, var4);
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.buffer.getBytes(var1, var2, var3, var4);
      return this;
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      this.buffer.getBytes(var1, var2);
      return this;
   }

   public ByteBuf duplicate() {
      return new ReadOnlyByteBuf(this);
   }

   public ByteBuf copy(int var1, int var2) {
      return this.buffer.copy(var1, var2);
   }

   public ByteBuf slice(int var1, int var2) {
      return Unpooled.unmodifiableBuffer(this.buffer.slice(var1, var2));
   }

   public byte getByte(int var1) {
      return this._getByte(var1);
   }

   protected byte _getByte(int var1) {
      return this.buffer.getByte(var1);
   }

   public short getShort(int var1) {
      return this._getShort(var1);
   }

   protected short _getShort(int var1) {
      return this.buffer.getShort(var1);
   }

   public int getUnsignedMedium(int var1) {
      return this._getUnsignedMedium(var1);
   }

   protected int _getUnsignedMedium(int var1) {
      return this.buffer.getUnsignedMedium(var1);
   }

   public int getInt(int var1) {
      return this._getInt(var1);
   }

   protected int _getInt(int var1) {
      return this.buffer.getInt(var1);
   }

   public long getLong(int var1) {
      return this._getLong(var1);
   }

   protected long _getLong(int var1) {
      return this.buffer.getLong(var1);
   }

   public int nioBufferCount() {
      return this.buffer.nioBufferCount();
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      return this.buffer.nioBuffer(var1, var2).asReadOnlyBuffer();
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      return this.buffer.nioBuffers(var1, var2);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      return this.nioBuffer(var1, var2);
   }

   public int forEachByte(int var1, int var2, ByteBufProcessor var3) {
      return this.buffer.forEachByte(var1, var2, var3);
   }

   public int forEachByteDesc(int var1, int var2, ByteBufProcessor var3) {
      return this.buffer.forEachByteDesc(var1, var2, var3);
   }

   public int capacity() {
      return this.buffer.capacity();
   }

   public ByteBuf capacity(int var1) {
      throw new ReadOnlyBufferException();
   }
}
