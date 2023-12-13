package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import io.netty.util.ResourceLeak;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

final class AdvancedLeakAwareByteBuf extends WrappedByteBuf {
   private final ResourceLeak leak;

   AdvancedLeakAwareByteBuf(ByteBuf var1, ResourceLeak var2) {
      super(var1);
      this.leak = var2;
   }

   public boolean release() {
      boolean var1 = super.release();
      if (var1) {
         this.leak.close();
      } else {
         this.leak.record();
      }

      return var1;
   }

   public boolean release(int var1) {
      boolean var2 = super.release(var1);
      if (var2) {
         this.leak.close();
      } else {
         this.leak.record();
      }

      return var2;
   }

   public ByteBuf order(ByteOrder var1) {
      this.leak.record();
      return this.order() == var1 ? this : new AdvancedLeakAwareByteBuf(super.order(var1), this.leak);
   }

   public ByteBuf slice() {
      this.leak.record();
      return new AdvancedLeakAwareByteBuf(super.slice(), this.leak);
   }

   public ByteBuf slice(int var1, int var2) {
      this.leak.record();
      return new AdvancedLeakAwareByteBuf(super.slice(var1, var2), this.leak);
   }

   public ByteBuf duplicate() {
      this.leak.record();
      return new AdvancedLeakAwareByteBuf(super.duplicate(), this.leak);
   }

   public ByteBuf readSlice(int var1) {
      this.leak.record();
      return new AdvancedLeakAwareByteBuf(super.readSlice(var1), this.leak);
   }

   public ByteBuf discardReadBytes() {
      this.leak.record();
      return super.discardReadBytes();
   }

   public ByteBuf discardSomeReadBytes() {
      this.leak.record();
      return super.discardSomeReadBytes();
   }

   public ByteBuf ensureWritable(int var1) {
      this.leak.record();
      return super.ensureWritable(var1);
   }

   public int ensureWritable(int var1, boolean var2) {
      this.leak.record();
      return super.ensureWritable(var1, var2);
   }

   public boolean getBoolean(int var1) {
      this.leak.record();
      return super.getBoolean(var1);
   }

   public byte getByte(int var1) {
      this.leak.record();
      return super.getByte(var1);
   }

   public short getUnsignedByte(int var1) {
      this.leak.record();
      return super.getUnsignedByte(var1);
   }

   public short getShort(int var1) {
      this.leak.record();
      return super.getShort(var1);
   }

   public int getUnsignedShort(int var1) {
      this.leak.record();
      return super.getUnsignedShort(var1);
   }

   public int getMedium(int var1) {
      this.leak.record();
      return super.getMedium(var1);
   }

   public int getUnsignedMedium(int var1) {
      this.leak.record();
      return super.getUnsignedMedium(var1);
   }

   public int getInt(int var1) {
      this.leak.record();
      return super.getInt(var1);
   }

   public long getUnsignedInt(int var1) {
      this.leak.record();
      return super.getUnsignedInt(var1);
   }

   public long getLong(int var1) {
      this.leak.record();
      return super.getLong(var1);
   }

   public char getChar(int var1) {
      this.leak.record();
      return super.getChar(var1);
   }

   public float getFloat(int var1) {
      this.leak.record();
      return super.getFloat(var1);
   }

   public double getDouble(int var1) {
      this.leak.record();
      return super.getDouble(var1);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2) {
      this.leak.record();
      return super.getBytes(var1, var2);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3) {
      this.leak.record();
      return super.getBytes(var1, var2, var3);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.leak.record();
      return super.getBytes(var1, var2, var3, var4);
   }

   public ByteBuf getBytes(int var1, byte[] var2) {
      this.leak.record();
      return super.getBytes(var1, var2);
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      this.leak.record();
      return super.getBytes(var1, var2, var3, var4);
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      this.leak.record();
      return super.getBytes(var1, var2);
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      this.leak.record();
      return super.getBytes(var1, var2, var3);
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      this.leak.record();
      return super.getBytes(var1, var2, var3);
   }

   public ByteBuf setBoolean(int var1, boolean var2) {
      this.leak.record();
      return super.setBoolean(var1, var2);
   }

   public ByteBuf setByte(int var1, int var2) {
      this.leak.record();
      return super.setByte(var1, var2);
   }

   public ByteBuf setShort(int var1, int var2) {
      this.leak.record();
      return super.setShort(var1, var2);
   }

   public ByteBuf setMedium(int var1, int var2) {
      this.leak.record();
      return super.setMedium(var1, var2);
   }

   public ByteBuf setInt(int var1, int var2) {
      this.leak.record();
      return super.setInt(var1, var2);
   }

   public ByteBuf setLong(int var1, long var2) {
      this.leak.record();
      return super.setLong(var1, var2);
   }

   public ByteBuf setChar(int var1, int var2) {
      this.leak.record();
      return super.setChar(var1, var2);
   }

   public ByteBuf setFloat(int var1, float var2) {
      this.leak.record();
      return super.setFloat(var1, var2);
   }

   public ByteBuf setDouble(int var1, double var2) {
      this.leak.record();
      return super.setDouble(var1, var2);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2) {
      this.leak.record();
      return super.setBytes(var1, var2);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3) {
      this.leak.record();
      return super.setBytes(var1, var2, var3);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      this.leak.record();
      return super.setBytes(var1, var2, var3, var4);
   }

   public ByteBuf setBytes(int var1, byte[] var2) {
      this.leak.record();
      return super.setBytes(var1, var2);
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      this.leak.record();
      return super.setBytes(var1, var2, var3, var4);
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      this.leak.record();
      return super.setBytes(var1, var2);
   }

   public int setBytes(int var1, InputStream var2, int var3) throws IOException {
      this.leak.record();
      return super.setBytes(var1, var2, var3);
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) throws IOException {
      this.leak.record();
      return super.setBytes(var1, var2, var3);
   }

   public ByteBuf setZero(int var1, int var2) {
      this.leak.record();
      return super.setZero(var1, var2);
   }

   public boolean readBoolean() {
      this.leak.record();
      return super.readBoolean();
   }

   public byte readByte() {
      this.leak.record();
      return super.readByte();
   }

   public short readUnsignedByte() {
      this.leak.record();
      return super.readUnsignedByte();
   }

   public short readShort() {
      this.leak.record();
      return super.readShort();
   }

   public int readUnsignedShort() {
      this.leak.record();
      return super.readUnsignedShort();
   }

   public int readMedium() {
      this.leak.record();
      return super.readMedium();
   }

   public int readUnsignedMedium() {
      this.leak.record();
      return super.readUnsignedMedium();
   }

   public int readInt() {
      this.leak.record();
      return super.readInt();
   }

   public long readUnsignedInt() {
      this.leak.record();
      return super.readUnsignedInt();
   }

   public long readLong() {
      this.leak.record();
      return super.readLong();
   }

   public char readChar() {
      this.leak.record();
      return super.readChar();
   }

   public float readFloat() {
      this.leak.record();
      return super.readFloat();
   }

   public double readDouble() {
      this.leak.record();
      return super.readDouble();
   }

   public ByteBuf readBytes(int var1) {
      this.leak.record();
      return super.readBytes(var1);
   }

   public ByteBuf readBytes(ByteBuf var1) {
      this.leak.record();
      return super.readBytes(var1);
   }

   public ByteBuf readBytes(ByteBuf var1, int var2) {
      this.leak.record();
      return super.readBytes(var1, var2);
   }

   public ByteBuf readBytes(ByteBuf var1, int var2, int var3) {
      this.leak.record();
      return super.readBytes(var1, var2, var3);
   }

   public ByteBuf readBytes(byte[] var1) {
      this.leak.record();
      return super.readBytes(var1);
   }

   public ByteBuf readBytes(byte[] var1, int var2, int var3) {
      this.leak.record();
      return super.readBytes(var1, var2, var3);
   }

   public ByteBuf readBytes(ByteBuffer var1) {
      this.leak.record();
      return super.readBytes(var1);
   }

   public ByteBuf readBytes(OutputStream var1, int var2) throws IOException {
      this.leak.record();
      return super.readBytes(var1, var2);
   }

   public int readBytes(GatheringByteChannel var1, int var2) throws IOException {
      this.leak.record();
      return super.readBytes(var1, var2);
   }

   public ByteBuf skipBytes(int var1) {
      this.leak.record();
      return super.skipBytes(var1);
   }

   public ByteBuf writeBoolean(boolean var1) {
      this.leak.record();
      return super.writeBoolean(var1);
   }

   public ByteBuf writeByte(int var1) {
      this.leak.record();
      return super.writeByte(var1);
   }

   public ByteBuf writeShort(int var1) {
      this.leak.record();
      return super.writeShort(var1);
   }

   public ByteBuf writeMedium(int var1) {
      this.leak.record();
      return super.writeMedium(var1);
   }

   public ByteBuf writeInt(int var1) {
      this.leak.record();
      return super.writeInt(var1);
   }

   public ByteBuf writeLong(long var1) {
      this.leak.record();
      return super.writeLong(var1);
   }

   public ByteBuf writeChar(int var1) {
      this.leak.record();
      return super.writeChar(var1);
   }

   public ByteBuf writeFloat(float var1) {
      this.leak.record();
      return super.writeFloat(var1);
   }

   public ByteBuf writeDouble(double var1) {
      this.leak.record();
      return super.writeDouble(var1);
   }

   public ByteBuf writeBytes(ByteBuf var1) {
      this.leak.record();
      return super.writeBytes(var1);
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2) {
      this.leak.record();
      return super.writeBytes(var1, var2);
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2, int var3) {
      this.leak.record();
      return super.writeBytes(var1, var2, var3);
   }

   public ByteBuf writeBytes(byte[] var1) {
      this.leak.record();
      return super.writeBytes(var1);
   }

   public ByteBuf writeBytes(byte[] var1, int var2, int var3) {
      this.leak.record();
      return super.writeBytes(var1, var2, var3);
   }

   public ByteBuf writeBytes(ByteBuffer var1) {
      this.leak.record();
      return super.writeBytes(var1);
   }

   public int writeBytes(InputStream var1, int var2) throws IOException {
      this.leak.record();
      return super.writeBytes(var1, var2);
   }

   public int writeBytes(ScatteringByteChannel var1, int var2) throws IOException {
      this.leak.record();
      return super.writeBytes(var1, var2);
   }

   public ByteBuf writeZero(int var1) {
      this.leak.record();
      return super.writeZero(var1);
   }

   public int indexOf(int var1, int var2, byte var3) {
      this.leak.record();
      return super.indexOf(var1, var2, var3);
   }

   public int bytesBefore(byte var1) {
      this.leak.record();
      return super.bytesBefore(var1);
   }

   public int bytesBefore(int var1, byte var2) {
      this.leak.record();
      return super.bytesBefore(var1, var2);
   }

   public int bytesBefore(int var1, int var2, byte var3) {
      this.leak.record();
      return super.bytesBefore(var1, var2, var3);
   }

   public int forEachByte(ByteBufProcessor var1) {
      this.leak.record();
      return super.forEachByte(var1);
   }

   public int forEachByte(int var1, int var2, ByteBufProcessor var3) {
      this.leak.record();
      return super.forEachByte(var1, var2, var3);
   }

   public int forEachByteDesc(ByteBufProcessor var1) {
      this.leak.record();
      return super.forEachByteDesc(var1);
   }

   public int forEachByteDesc(int var1, int var2, ByteBufProcessor var3) {
      this.leak.record();
      return super.forEachByteDesc(var1, var2, var3);
   }

   public ByteBuf copy() {
      this.leak.record();
      return super.copy();
   }

   public ByteBuf copy(int var1, int var2) {
      this.leak.record();
      return super.copy(var1, var2);
   }

   public int nioBufferCount() {
      this.leak.record();
      return super.nioBufferCount();
   }

   public ByteBuffer nioBuffer() {
      this.leak.record();
      return super.nioBuffer();
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      this.leak.record();
      return super.nioBuffer(var1, var2);
   }

   public ByteBuffer[] nioBuffers() {
      this.leak.record();
      return super.nioBuffers();
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      this.leak.record();
      return super.nioBuffers(var1, var2);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      this.leak.record();
      return super.internalNioBuffer(var1, var2);
   }

   public String toString(Charset var1) {
      this.leak.record();
      return super.toString(var1);
   }

   public String toString(int var1, int var2, Charset var3) {
      this.leak.record();
      return super.toString(var1, var2, var3);
   }

   public ByteBuf retain() {
      this.leak.record();
      return super.retain();
   }

   public ByteBuf retain(int var1) {
      this.leak.record();
      return super.retain(var1);
   }

   public ByteBuf capacity(int var1) {
      this.leak.record();
      return super.capacity(var1);
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }
}
