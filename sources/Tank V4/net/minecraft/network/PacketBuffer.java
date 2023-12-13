package net.minecraft.network;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufProcessor;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ReferenceCounted;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.UUID;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class PacketBuffer extends ByteBuf {
   private final ByteBuf buf;

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      return this.buf.getBytes(var1, var2, var3, var4);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2) {
      return this.buf.setBytes(var1, var2);
   }

   public int maxCapacity() {
      return this.buf.maxCapacity();
   }

   public boolean isWritable() {
      return this.buf.isWritable();
   }

   public boolean isDirect() {
      return this.buf.isDirect();
   }

   public float getFloat(int var1) {
      return this.buf.getFloat(var1);
   }

   public ByteBuf copy(int var1, int var2) {
      return this.buf.copy(var1, var2);
   }

   public void writeNBTTagCompoundToBuffer(NBTTagCompound var1) {
      if (var1 == null) {
         this.writeByte(0);
      } else {
         try {
            CompressedStreamTools.write(var1, (DataOutput)(new ByteBufOutputStream(this)));
         } catch (IOException var3) {
            throw new EncoderException(var3);
         }
      }

   }

   public ByteBuf setChar(int var1, int var2) {
      return this.buf.setChar(var1, var2);
   }

   public void writeEnumValue(Enum var1) {
      this.writeVarIntToBuffer(var1.ordinal());
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      return this.buf.setBytes(var1, var2, var3, var4);
   }

   public ByteBuf writeLong(long var1) {
      return this.buf.writeLong(var1);
   }

   public PacketBuffer writeString(String var1) {
      byte[] var2 = var1.getBytes(Charsets.UTF_8);
      if (var2.length > 32767) {
         throw new EncoderException("String too big (was " + var1.length() + " bytes encoded, max " + 32767 + ")");
      } else {
         this.writeVarIntToBuffer(var2.length);
         this.writeBytes(var2);
         return this;
      }
   }

   public ByteBuf writeBoolean(boolean var1) {
      return this.buf.writeBoolean(var1);
   }

   public ByteBuf setMedium(int var1, int var2) {
      return this.buf.setMedium(var1, var2);
   }

   public byte getByte(int var1) {
      return this.buf.getByte(var1);
   }

   public ByteBuf readBytes(byte[] var1, int var2, int var3) {
      return this.buf.readBytes(var1, var2, var3);
   }

   public ByteBuf writeChar(int var1) {
      return this.buf.writeChar(var1);
   }

   public String toString(Charset var1) {
      return this.buf.toString(var1);
   }

   public long readVarLong() {
      long var1 = 0L;
      int var3 = 0;

      byte var4;
      do {
         var4 = this.readByte();
         var1 |= (long)(var4 & 127) << var3++ * 7;
         if (var3 > 10) {
            throw new RuntimeException("VarLong too big");
         }
      } while((var4 & 128) == 128);

      return var1;
   }

   public short readShort() {
      return this.buf.readShort();
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      return this.buf.nioBuffers(var1, var2);
   }

   public ByteBuf markWriterIndex() {
      return this.buf.markWriterIndex();
   }

   public int maxWritableBytes() {
      return this.buf.maxWritableBytes();
   }

   public int capacity() {
      return this.buf.capacity();
   }

   public int readBytes(GatheringByteChannel var1, int var2) throws IOException {
      return this.buf.readBytes(var1, var2);
   }

   public void writeVarLong(long var1) {
      while((var1 & -128L) != 0L) {
         this.writeByte((int)(var1 & 127L) | 128);
         var1 >>>= 7;
      }

      this.writeByte((int)var1);
   }

   public String toString() {
      return this.buf.toString();
   }

   public ByteBuf setZero(int var1, int var2) {
      return this.buf.setZero(var1, var2);
   }

   public boolean release() {
      return this.buf.release();
   }

   public float readFloat() {
      return this.buf.readFloat();
   }

   public Enum readEnumValue(Class var1) {
      return ((Enum[])var1.getEnumConstants())[this.readVarIntFromBuffer()];
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2, int var3) {
      return this.buf.writeBytes(var1, var2, var3);
   }

   public ByteBuf readBytes(ByteBuf var1, int var2) {
      return this.buf.readBytes(var1, var2);
   }

   public int hashCode() {
      return this.buf.hashCode();
   }

   public int readUnsignedShort() {
      return this.buf.readUnsignedShort();
   }

   public boolean isReadable(int var1) {
      return this.buf.isReadable(var1);
   }

   public ByteBuf readBytes(ByteBuf var1, int var2, int var3) {
      return this.buf.readBytes(var1, var2, var3);
   }

   public ByteBuf writeZero(int var1) {
      return this.buf.writeZero(var1);
   }

   public ByteBuf retain(int var1) {
      return this.buf.retain(var1);
   }

   public byte readByte() {
      return this.buf.readByte();
   }

   public long readLong() {
      return this.buf.readLong();
   }

   public ByteBuf setShort(int var1, int var2) {
      return this.buf.setShort(var1, var2);
   }

   public static int getVarIntSize(int var0) {
      for(int var1 = 1; var1 < 5; ++var1) {
         if ((var0 & -1 << var1 * 7) == 0) {
            return var1;
         }
      }

      return 5;
   }

   public ByteBuf setInt(int var1, int var2) {
      return this.buf.setInt(var1, var2);
   }

   public double readDouble() {
      return this.buf.readDouble();
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      return this.buf.nioBuffer(var1, var2);
   }

   public String toString(int var1, int var2, Charset var3) {
      return this.buf.toString(var1, var2, var3);
   }

   public ByteBuf ensureWritable(int var1) {
      return this.buf.ensureWritable(var1);
   }

   public int writeBytes(ScatteringByteChannel var1, int var2) throws IOException {
      return this.buf.writeBytes(var1, var2);
   }

   public boolean getBoolean(int var1) {
      return this.buf.getBoolean(var1);
   }

   public IChatComponent readChatComponent() throws IOException {
      return IChatComponent.Serializer.jsonToComponent(this.readStringFromBuffer(32767));
   }

   public ByteBuf setIndex(int var1, int var2) {
      return this.buf.setIndex(var1, var2);
   }

   public ByteBuf readerIndex(int var1) {
      return this.buf.readerIndex(var1);
   }

   public void writeByteArray(byte[] var1) {
      this.writeVarIntToBuffer(var1.length);
      this.writeBytes(var1);
   }

   public ByteBuf writeFloat(float var1) {
      return this.buf.writeFloat(var1);
   }

   public int bytesBefore(byte var1) {
      return this.buf.bytesBefore(var1);
   }

   public int readMedium() {
      return this.buf.readMedium();
   }

   public ByteBuf writeMedium(int var1) {
      return this.buf.writeMedium(var1);
   }

   public int forEachByteDesc(ByteBufProcessor var1) {
      return this.buf.forEachByteDesc(var1);
   }

   public boolean equals(Object var1) {
      return this.buf.equals(var1);
   }

   public ByteBuf slice() {
      return this.buf.slice();
   }

   public void writeItemStackToBuffer(ItemStack var1) {
      if (var1 == null) {
         this.writeShort(-1);
      } else {
         this.writeShort(Item.getIdFromItem(var1.getItem()));
         this.writeByte(var1.stackSize);
         this.writeShort(var1.getMetadata());
         NBTTagCompound var2 = null;
         if (var1.getItem().isDamageable() || var1.getItem().getShareTag()) {
            var2 = var1.getTagCompound();
         }

         this.writeNBTTagCompoundToBuffer(var2);
      }

   }

   public char readChar() {
      return this.buf.readChar();
   }

   public PacketBuffer(ByteBuf var1) {
      this.buf = var1;
   }

   public boolean release(int var1) {
      return this.buf.release(var1);
   }

   public int setBytes(int var1, InputStream var2, int var3) throws IOException {
      return this.buf.setBytes(var1, var2, var3);
   }

   public ByteBufAllocator alloc() {
      return this.buf.alloc();
   }

   public void writeBlockPos(BlockPos var1) {
      this.writeLong(var1.toLong());
   }

   public ByteBuf setByte(int var1, int var2) {
      return this.buf.setByte(var1, var2);
   }

   public ByteBuf readBytes(ByteBuffer var1) {
      return this.buf.readBytes(var1);
   }

   public int readableBytes() {
      return this.buf.readableBytes();
   }

   public double getDouble(int var1) {
      return this.buf.getDouble(var1);
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      return this.buf.getBytes(var1, var2);
   }

   public ItemStack readItemStackFromBuffer() throws IOException {
      ItemStack var1 = null;
      short var2 = this.readShort();
      if (var2 >= 0) {
         byte var3 = this.readByte();
         short var4 = this.readShort();
         var1 = new ItemStack(Item.getItemById(var2), var3, var4);
         var1.setTagCompound(this.readNBTTagCompoundFromBuffer());
      }

      return var1;
   }

   public long getUnsignedInt(int var1) {
      return this.buf.getUnsignedInt(var1);
   }

   public ByteBuf resetReaderIndex() {
      return this.buf.resetReaderIndex();
   }

   public boolean hasMemoryAddress() {
      return this.buf.hasMemoryAddress();
   }

   public char getChar(int var1) {
      return this.buf.getChar(var1);
   }

   public int readVarIntFromBuffer() {
      int var1 = 0;
      int var2 = 0;

      byte var3;
      do {
         var3 = this.readByte();
         var1 |= (var3 & 127) << var2++ * 7;
         if (var2 > 5) {
            throw new RuntimeException("VarInt too big");
         }
      } while((var3 & 128) == 128);

      return var1;
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2) {
      return this.buf.writeBytes(var1, var2);
   }

   public int compareTo(ByteBuf var1) {
      return this.buf.compareTo(var1);
   }

   public int getUnsignedShort(int var1) {
      return this.buf.getUnsignedShort(var1);
   }

   public ByteBuf retain() {
      return this.buf.retain();
   }

   public int writableBytes() {
      return this.buf.writableBytes();
   }

   public ByteBuf readBytes(int var1) {
      return this.buf.readBytes(var1);
   }

   public int getInt(int var1) {
      return this.buf.getInt(var1);
   }

   public ByteBuf markReaderIndex() {
      return this.buf.markReaderIndex();
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      return this.buf.setBytes(var1, var2);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3) {
      return this.buf.setBytes(var1, var2, var3);
   }

   public ByteBuffer[] nioBuffers() {
      return this.buf.nioBuffers();
   }

   public ByteBuf writeShort(int var1) {
      return this.buf.writeShort(var1);
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public int forEachByte(ByteBufProcessor var1) {
      return this.buf.forEachByte(var1);
   }

   public ByteBuf setBoolean(int var1, boolean var2) {
      return this.buf.setBoolean(var1, var2);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2) {
      return this.buf.getBytes(var1, var2);
   }

   public ByteBuf skipBytes(int var1) {
      return this.buf.skipBytes(var1);
   }

   public int readerIndex() {
      return this.buf.readerIndex();
   }

   public int readUnsignedMedium() {
      return this.buf.readUnsignedMedium();
   }

   public ByteBuf capacity(int var1) {
      return this.buf.capacity(var1);
   }

   public void writeVarIntToBuffer(int var1) {
      while((var1 & -128) != 0) {
         this.writeByte(var1 & 127 | 128);
         var1 >>>= 7;
      }

      this.writeByte(var1);
   }

   public ByteBuf writeBytes(ByteBuffer var1) {
      return this.buf.writeBytes(var1);
   }

   public ByteBuf unwrap() {
      return this.buf.unwrap();
   }

   public int getUnsignedMedium(int var1) {
      return this.buf.getUnsignedMedium(var1);
   }

   public UUID readUuid() {
      return new UUID(this.readLong(), this.readLong());
   }

   public ByteOrder order() {
      return this.buf.order();
   }

   public boolean readBoolean() {
      return this.buf.readBoolean();
   }

   public ByteBuf writeByte(int var1) {
      return this.buf.writeByte(var1);
   }

   public ByteBuf readBytes(ByteBuf var1) {
      return this.buf.readBytes(var1);
   }

   public void writeChatComponent(IChatComponent var1) throws IOException {
      this.writeString(IChatComponent.Serializer.componentToJson(var1));
   }

   public String readStringFromBuffer(int var1) {
      int var2 = this.readVarIntFromBuffer();
      if (var2 > var1 * 4) {
         throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + var2 + " > " + var1 * 4 + ")");
      } else if (var2 < 0) {
         throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
      } else {
         String var3 = new String(this.readBytes(var2).array(), Charsets.UTF_8);
         if (var3.length() > var1) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + var2 + " > " + var1 + ")");
         } else {
            return var3;
         }
      }
   }

   public int getMedium(int var1) {
      return this.buf.getMedium(var1);
   }

   public ByteBuf discardSomeReadBytes() {
      return this.buf.discardSomeReadBytes();
   }

   public ByteBuf readBytes(byte[] var1) {
      return this.buf.readBytes(var1);
   }

   public int refCnt() {
      return this.buf.refCnt();
   }

   public ByteBuf writeBytes(byte[] var1) {
      return this.buf.writeBytes(var1);
   }

   public ByteBuf readBytes(OutputStream var1, int var2) throws IOException {
      return this.buf.readBytes(var1, var2);
   }

   public int arrayOffset() {
      return this.buf.arrayOffset();
   }

   public ByteBuf setLong(int var1, long var2) {
      return this.buf.setLong(var1, var2);
   }

   public ByteBuf writeBytes(byte[] var1, int var2, int var3) {
      return this.buf.writeBytes(var1, var2, var3);
   }

   public byte[] readByteArray() {
      byte[] var1 = new byte[this.readVarIntFromBuffer()];
      this.readBytes(var1);
      return var1;
   }

   public ReferenceCounted retain() {
      return this.retain();
   }

   public int bytesBefore(int var1, int var2, byte var3) {
      return this.buf.bytesBefore(var1, var2, var3);
   }

   public short getShort(int var1) {
      return this.buf.getShort(var1);
   }

   public ByteBuf resetWriterIndex() {
      return this.buf.resetWriterIndex();
   }

   public ByteBuf writeInt(int var1) {
      return this.buf.writeInt(var1);
   }

   public ByteBuf discardReadBytes() {
      return this.buf.discardReadBytes();
   }

   public int forEachByte(int var1, int var2, ByteBufProcessor var3) {
      return this.buf.forEachByte(var1, var2, var3);
   }

   public int ensureWritable(int var1, boolean var2) {
      return this.buf.ensureWritable(var1, var2);
   }

   public ByteBuf order(ByteOrder var1) {
      return this.buf.order(var1);
   }

   public ByteBuf duplicate() {
      return this.buf.duplicate();
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      return this.buf.getBytes(var1, var2, var3);
   }

   public long readUnsignedInt() {
      return this.buf.readUnsignedInt();
   }

   public short getUnsignedByte(int var1) {
      return this.buf.getUnsignedByte(var1);
   }

   public ByteBuf clear() {
      return this.buf.clear();
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3) {
      return this.buf.getBytes(var1, var2, var3);
   }

   public int bytesBefore(int var1, byte var2) {
      return this.buf.bytesBefore(var1, var2);
   }

   public ByteBuffer nioBuffer() {
      return this.buf.nioBuffer();
   }

   public int nioBufferCount() {
      return this.buf.nioBufferCount();
   }

   public NBTTagCompound readNBTTagCompoundFromBuffer() throws IOException {
      int var1 = this.readerIndex();
      byte var2 = this.readByte();
      if (var2 == 0) {
         return null;
      } else {
         this.readerIndex(var1);
         return CompressedStreamTools.read(new ByteBufInputStream(this), new NBTSizeTracker(2097152L));
      }
   }

   public long memoryAddress() {
      return this.buf.memoryAddress();
   }

   public ByteBuf writeDouble(double var1) {
      return this.buf.writeDouble(var1);
   }

   public ByteBuf setDouble(int var1, double var2) {
      return this.buf.setDouble(var1, var2);
   }

   public ByteBuf slice(int var1, int var2) {
      return this.buf.slice(var1, var2);
   }

   public int indexOf(int var1, int var2, byte var3) {
      return this.buf.indexOf(var1, var2, var3);
   }

   public boolean isWritable(int var1) {
      return this.buf.isWritable(var1);
   }

   public ByteBuf getBytes(int var1, byte[] var2) {
      return this.buf.getBytes(var1, var2);
   }

   public boolean isReadable() {
      return this.buf.isReadable();
   }

   public ByteBuf writeBytes(ByteBuf var1) {
      return this.buf.writeBytes(var1);
   }

   public ByteBuf copy() {
      return this.buf.copy();
   }

   public BlockPos readBlockPos() {
      return BlockPos.fromLong(this.readLong());
   }

   public long getLong(int var1) {
      return this.buf.getLong(var1);
   }

   public ByteBuf writerIndex(int var1) {
      return this.buf.writerIndex(var1);
   }

   public void writeUuid(UUID var1) {
      this.writeLong(var1.getMostSignificantBits());
      this.writeLong(var1.getLeastSignificantBits());
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      return this.buf.setBytes(var1, var2, var3, var4);
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) throws IOException {
      return this.buf.setBytes(var1, var2, var3);
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      return this.buf.getBytes(var1, var2, var3);
   }

   public int readInt() {
      return this.buf.readInt();
   }

   public ByteBuf readSlice(int var1) {
      return this.buf.readSlice(var1);
   }

   public byte[] array() {
      return this.buf.array();
   }

   public ByteBuf setBytes(int var1, byte[] var2) {
      return this.buf.setBytes(var1, var2);
   }

   public int writerIndex() {
      return this.buf.writerIndex();
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      return this.buf.internalNioBuffer(var1, var2);
   }

   public short readUnsignedByte() {
      return this.buf.readUnsignedByte();
   }

   public int writeBytes(InputStream var1, int var2) throws IOException {
      return this.buf.writeBytes(var1, var2);
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      return this.buf.getBytes(var1, var2, var3, var4);
   }

   public boolean hasArray() {
      return this.buf.hasArray();
   }

   public ByteBuf setFloat(int var1, float var2) {
      return this.buf.setFloat(var1, var2);
   }

   public int forEachByteDesc(int var1, int var2, ByteBufProcessor var3) {
      return this.buf.forEachByteDesc(var1, var2, var3);
   }
}
