package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import java.nio.ByteOrder;

final class UnsafeDirectSwappedByteBuf extends SwappedByteBuf {
   private static final boolean NATIVE_ORDER;
   private final boolean nativeByteOrder;
   private final AbstractByteBuf wrapped;

   UnsafeDirectSwappedByteBuf(AbstractByteBuf var1) {
      super(var1);
      this.wrapped = var1;
      this.nativeByteOrder = NATIVE_ORDER == (this.order() == ByteOrder.BIG_ENDIAN);
   }

   private long addr(int var1) {
      return this.wrapped.memoryAddress() + (long)var1;
   }

   public long getLong(int var1) {
      this.wrapped.checkIndex(var1, 8);
      long var2 = PlatformDependent.getLong(this.addr(var1));
      return this.nativeByteOrder ? var2 : Long.reverseBytes(var2);
   }

   public float getFloat(int var1) {
      return Float.intBitsToFloat(this.getInt(var1));
   }

   public double getDouble(int var1) {
      return Double.longBitsToDouble(this.getLong(var1));
   }

   public char getChar(int var1) {
      return (char)this.getShort(var1);
   }

   public long getUnsignedInt(int var1) {
      return (long)this.getInt(var1) & 4294967295L;
   }

   public int getInt(int var1) {
      this.wrapped.checkIndex(var1, 4);
      int var2 = PlatformDependent.getInt(this.addr(var1));
      return this.nativeByteOrder ? var2 : Integer.reverseBytes(var2);
   }

   public int getUnsignedShort(int var1) {
      return this.getShort(var1) & '\uffff';
   }

   public short getShort(int var1) {
      this.wrapped.checkIndex(var1, 2);
      short var2 = PlatformDependent.getShort(this.addr(var1));
      return this.nativeByteOrder ? var2 : Short.reverseBytes(var2);
   }

   public ByteBuf setShort(int var1, int var2) {
      this.wrapped.checkIndex(var1, 2);
      this._setShort(var1, var2);
      return this;
   }

   public ByteBuf setInt(int var1, int var2) {
      this.wrapped.checkIndex(var1, 4);
      this._setInt(var1, var2);
      return this;
   }

   public ByteBuf setLong(int var1, long var2) {
      this.wrapped.checkIndex(var1, 8);
      this._setLong(var1, var2);
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

   public ByteBuf writeShort(int var1) {
      this.wrapped.ensureAccessible();
      this.wrapped.ensureWritable(2);
      this._setShort(this.wrapped.writerIndex, var1);
      AbstractByteBuf var10000 = this.wrapped;
      var10000.writerIndex += 2;
      return this;
   }

   public ByteBuf writeInt(int var1) {
      this.wrapped.ensureAccessible();
      this.wrapped.ensureWritable(4);
      this._setInt(this.wrapped.writerIndex, var1);
      AbstractByteBuf var10000 = this.wrapped;
      var10000.writerIndex += 4;
      return this;
   }

   public ByteBuf writeLong(long var1) {
      this.wrapped.ensureAccessible();
      this.wrapped.ensureWritable(8);
      this._setLong(this.wrapped.writerIndex, var1);
      AbstractByteBuf var10000 = this.wrapped;
      var10000.writerIndex += 8;
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

   private void _setShort(int var1, int var2) {
      PlatformDependent.putShort(this.addr(var1), this.nativeByteOrder ? (short)var2 : Short.reverseBytes((short)var2));
   }

   private void _setInt(int var1, int var2) {
      PlatformDependent.putInt(this.addr(var1), this.nativeByteOrder ? var2 : Integer.reverseBytes(var2));
   }

   private void _setLong(int var1, long var2) {
      PlatformDependent.putLong(this.addr(var1), this.nativeByteOrder ? var2 : Long.reverseBytes(var2));
   }

   static {
      NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
   }
}
