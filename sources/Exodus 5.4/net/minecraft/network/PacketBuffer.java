/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 */
package net.minecraft.network;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufProcessor;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
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

public class PacketBuffer
extends ByteBuf {
    private final ByteBuf buf;

    @Override
    public ByteBuf capacity(int n) {
        return this.buf.capacity(n);
    }

    public void writeItemStackToBuffer(ItemStack itemStack) {
        if (itemStack == null) {
            this.writeShort(-1);
        } else {
            this.writeShort(Item.getIdFromItem(itemStack.getItem()));
            this.writeByte(itemStack.stackSize);
            this.writeShort(itemStack.getMetadata());
            NBTTagCompound nBTTagCompound = null;
            if (itemStack.getItem().isDamageable() || itemStack.getItem().getShareTag()) {
                nBTTagCompound = itemStack.getTagCompound();
            }
            this.writeNBTTagCompoundToBuffer(nBTTagCompound);
        }
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        return this.buf.getBytes(n, gatheringByteChannel, n2);
    }

    @Override
    public boolean release(int n) {
        return this.buf.release(n);
    }

    @Override
    public ByteBuf writeDouble(double d) {
        return this.buf.writeDouble(d);
    }

    @Override
    public ByteBuf writeShort(int n) {
        return this.buf.writeShort(n);
    }

    @Override
    public ByteBuf writeZero(int n) {
        return this.buf.writeZero(n);
    }

    @Override
    public int bytesBefore(int n, int n2, byte by) {
        return this.buf.bytesBefore(n, n2, by);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf) {
        return this.buf.getBytes(n, byteBuf);
    }

    public void writeNBTTagCompoundToBuffer(NBTTagCompound nBTTagCompound) {
        if (nBTTagCompound == null) {
            this.writeByte(0);
        } else {
            try {
                CompressedStreamTools.write(nBTTagCompound, new ByteBufOutputStream(this));
            }
            catch (IOException iOException) {
                throw new EncoderException(iOException);
            }
        }
    }

    @Override
    public ByteBuf setInt(int n, int n2) {
        return this.buf.setInt(n, n2);
    }

    @Override
    public int readMedium() {
        return this.buf.readMedium();
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        return this.buf.nioBuffer(n, n2);
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        return this.buf.copy(n, n2);
    }

    @Override
    public int forEachByteDesc(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByteDesc(n, n2, byteBufProcessor);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n, int n2) {
        return this.buf.readBytes(byteBuf, n, n2);
    }

    @Override
    public ByteBuf setDouble(int n, double d) {
        return this.buf.setDouble(n, d);
    }

    @Override
    public int writeBytes(InputStream inputStream, int n) throws IOException {
        return this.buf.writeBytes(inputStream, n);
    }

    @Override
    public int getMedium(int n) {
        return this.buf.getMedium(n);
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return this.buf.nioBuffers(n, n2);
    }

    @Override
    public ByteBuf discardReadBytes() {
        return this.buf.discardReadBytes();
    }

    @Override
    public int readableBytes() {
        return this.buf.readableBytes();
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        return this.buf.getBytes(n, outputStream, n2);
    }

    @Override
    public ByteBuf readBytes(int n) {
        return this.buf.readBytes(n);
    }

    @Override
    public ByteBuf writeChar(int n) {
        return this.buf.writeChar(n);
    }

    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }

    @Override
    public ByteBuf setZero(int n, int n2) {
        return this.buf.setZero(n, n2);
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        return this.buf.setBytes(n, inputStream, n2);
    }

    @Override
    public byte[] array() {
        return this.buf.array();
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2) {
        return this.buf.getBytes(n, byteBuf, n2);
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        return this.buf.setBytes(n, scatteringByteChannel, n2);
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        return this.buf.setBytes(n, byArray, n2, n3);
    }

    public BlockPos readBlockPos() {
        return BlockPos.fromLong(this.readLong());
    }

    @Override
    public String toString(Charset charset) {
        return this.buf.toString(charset);
    }

    @Override
    public ByteBuf setFloat(int n, float f) {
        return this.buf.setFloat(n, f);
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return this.buf.discardSomeReadBytes();
    }

    @Override
    public ByteBuf setChar(int n, int n2) {
        return this.buf.setChar(n, n2);
    }

    @Override
    public ByteBuf setByte(int n, int n2) {
        return this.buf.setByte(n, n2);
    }

    @Override
    public ByteBuf clear() {
        return this.buf.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return this.buf.markReaderIndex();
    }

    @Override
    public int refCnt() {
        return this.buf.refCnt();
    }

    public int readVarIntFromBuffer() {
        byte by;
        int n = 0;
        int n2 = 0;
        do {
            by = this.readByte();
            n |= (by & 0x7F) << n2++ * 7;
            if (n2 <= 5) continue;
            throw new RuntimeException("VarInt too big");
        } while ((by & 0x80) == 128);
        return n;
    }

    @Override
    public byte getByte(int n) {
        return this.buf.getByte(n);
    }

    public void writeByteArray(byte[] byArray) {
        this.writeVarIntToBuffer(byArray.length);
        this.writeBytes(byArray);
    }

    public PacketBuffer writeString(String string) {
        byte[] byArray = string.getBytes(Charsets.UTF_8);
        if (byArray.length > Short.MAX_VALUE) {
            throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + Short.MAX_VALUE + ")");
        }
        this.writeVarIntToBuffer(byArray.length);
        this.writeBytes(byArray);
        return this;
    }

    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    @Override
    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        return this.buf.order(byteOrder);
    }

    @Override
    public ByteBuf ensureWritable(int n) {
        return this.buf.ensureWritable(n);
    }

    @Override
    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }

    @Override
    public int getInt(int n) {
        return this.buf.getInt(n);
    }

    @Override
    public ByteBuf retain(int n) {
        return this.buf.retain(n);
    }

    @Override
    public int bytesBefore(int n, byte by) {
        return this.buf.bytesBefore(n, by);
    }

    @Override
    public int readInt() {
        return this.buf.readInt();
    }

    @Override
    public ByteBuf readBytes(byte[] byArray, int n, int n2) {
        return this.buf.readBytes(byArray, n, n2);
    }

    @Override
    public ByteBuf retain() {
        return this.buf.retain();
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        return this.buf.internalNioBuffer(n, n2);
    }

    @Override
    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    @Override
    public ByteBuf readBytes(byte[] byArray) {
        return this.buf.readBytes(byArray);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n) {
        return this.buf.writeBytes(byteBuf, n);
    }

    @Override
    public ByteBuf writeBoolean(boolean bl) {
        return this.buf.writeBoolean(bl);
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray) {
        return this.buf.getBytes(n, byArray);
    }

    @Override
    public int indexOf(int n, int n2, byte by) {
        return this.buf.indexOf(n, n2, by);
    }

    @Override
    public ByteBuf skipBytes(int n) {
        return this.buf.skipBytes(n);
    }

    @Override
    public String toString() {
        return this.buf.toString();
    }

    public static int getVarIntSize(int n) {
        int n2 = 1;
        while (n2 < 5) {
            if ((n & -1 << n2 * 7) == 0) {
                return n2;
            }
            ++n2;
        }
        return 5;
    }

    @Override
    public float getFloat(int n) {
        return this.buf.getFloat(n);
    }

    @Override
    public ByteBuf setMedium(int n, int n2) {
        return this.buf.setMedium(n, n2);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        return this.buf.writeBytes(byteBuffer);
    }

    @Override
    public ByteBuf writeInt(int n) {
        return this.buf.writeInt(n);
    }

    @Override
    public int hashCode() {
        return this.buf.hashCode();
    }

    @Override
    public boolean hasArray() {
        return this.buf.hasArray();
    }

    public IChatComponent readChatComponent() throws IOException {
        return IChatComponent.Serializer.jsonToComponent(this.readStringFromBuffer(Short.MAX_VALUE));
    }

    public void writeUuid(UUID uUID) {
        this.writeLong(uUID.getMostSignificantBits());
        this.writeLong(uUID.getLeastSignificantBits());
    }

    @Override
    public int forEachByte(ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByte(byteBufProcessor);
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        return this.buf.getBytes(n, byArray, n2, n3);
    }

    @Override
    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    @Override
    public ByteBuf writeByte(int n) {
        return this.buf.writeByte(n);
    }

    @Override
    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return this.buf.resetReaderIndex();
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return this.buf.compareTo(byteBuf);
    }

    @Override
    public int forEachByte(int n, int n2, ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByte(n, n2, byteBufProcessor);
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    @Override
    public ByteBuf writeLong(long l) {
        return this.buf.writeLong(l);
    }

    @Override
    public byte readByte() {
        return this.buf.readByte();
    }

    public long readVarLong() {
        byte by;
        long l = 0L;
        int n = 0;
        do {
            by = this.readByte();
            l |= (long)(by & 0x7F) << n++ * 7;
            if (n <= 10) continue;
            throw new RuntimeException("VarLong too big");
        } while ((by & 0x80) == 128);
        return l;
    }

    @Override
    public ByteBuf setBoolean(int n, boolean bl) {
        return this.buf.setBoolean(n, bl);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int n) throws IOException {
        return this.buf.writeBytes(scatteringByteChannel, n);
    }

    public void writeVarIntToBuffer(int n) {
        while ((n & 0xFFFFFF80) != 0) {
            this.writeByte(n & 0x7F | 0x80);
            n >>>= 7;
        }
        this.writeByte(n);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int n) {
        return this.buf.readBytes(byteBuf, n);
    }

    @Override
    public boolean isReadable() {
        return this.buf.isReadable();
    }

    @Override
    public double getDouble(int n) {
        return this.buf.getDouble(n);
    }

    @Override
    public boolean isWritable() {
        return this.buf.isWritable();
    }

    public PacketBuffer(ByteBuf byteBuf) {
        this.buf = byteBuf;
    }

    @Override
    public long readLong() {
        return this.buf.readLong();
    }

    @Override
    public int readerIndex() {
        return this.buf.readerIndex();
    }

    @Override
    public short readShort() {
        return this.buf.readShort();
    }

    @Override
    public int capacity() {
        return this.buf.capacity();
    }

    @Override
    public int getUnsignedShort(int n) {
        return this.buf.getUnsignedShort(n);
    }

    public UUID readUuid() {
        return new UUID(this.readLong(), this.readLong());
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor byteBufProcessor) {
        return this.buf.forEachByteDesc(byteBufProcessor);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        return this.buf.readBytes(byteBuffer);
    }

    @Override
    public int writerIndex() {
        return this.buf.writerIndex();
    }

    @Override
    public boolean equals(Object object) {
        return this.buf.equals(object);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        return this.buf.setBytes(n, byteBuf, n2, n3);
    }

    @Override
    public ByteBuf markWriterIndex() {
        return this.buf.markWriterIndex();
    }

    @Override
    public boolean getBoolean(int n) {
        return this.buf.getBoolean(n);
    }

    @Override
    public ByteBuf setLong(int n, long l) {
        return this.buf.setLong(n, l);
    }

    public NBTTagCompound readNBTTagCompoundFromBuffer() throws IOException {
        int n = this.readerIndex();
        byte by = this.readByte();
        if (by == 0) {
            return null;
        }
        this.readerIndex(n);
        return CompressedStreamTools.read(new ByteBufInputStream(this), new NBTSizeTracker(0x200000L));
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray) {
        return this.buf.setBytes(n, byArray);
    }

    @Override
    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }

    @Override
    public ByteBuf writeMedium(int n) {
        return this.buf.writeMedium(n);
    }

    @Override
    public long getUnsignedInt(int n) {
        return this.buf.getUnsignedInt(n);
    }

    @Override
    public short getShort(int n) {
        return this.buf.getShort(n);
    }

    public void writeVarLong(long l) {
        while ((l & 0xFFFFFFFFFFFFFF80L) != 0L) {
            this.writeByte((int)(l & 0x7FL) | 0x80);
            l >>>= 7;
        }
        this.writeByte((int)l);
    }

    @Override
    public ByteBuf readerIndex(int n) {
        return this.buf.readerIndex(n);
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return this.buf.resetWriterIndex();
    }

    @Override
    public char readChar() {
        return this.buf.readChar();
    }

    public <T extends Enum<T>> T readEnumValue(Class<T> clazz) {
        return (T)((Enum[])clazz.getEnumConstants())[this.readVarIntFromBuffer()];
    }

    @Override
    public boolean isDirect() {
        return this.buf.isDirect();
    }

    @Override
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    @Override
    public ByteBuf slice() {
        return this.buf.slice();
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        return this.buf.writeBytes(byteBuf);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        return this.buf.setBytes(n, byteBuffer);
    }

    @Override
    public boolean isWritable(int n) {
        return this.buf.isWritable(n);
    }

    @Override
    public ByteBuf setShort(int n, int n2) {
        return this.buf.setShort(n, n2);
    }

    public ItemStack readItemStackFromBuffer() throws IOException {
        ItemStack itemStack = null;
        short s = this.readShort();
        if (s >= 0) {
            byte by = this.readByte();
            short s2 = this.readShort();
            itemStack = new ItemStack(Item.getItemById(s), (int)by, (int)s2);
            itemStack.setTagCompound(this.readNBTTagCompoundFromBuffer());
        }
        return itemStack;
    }

    @Override
    public short getUnsignedByte(int n) {
        return this.buf.getUnsignedByte(n);
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray) {
        return this.buf.writeBytes(byArray);
    }

    @Override
    public int getUnsignedMedium(int n) {
        return this.buf.getUnsignedMedium(n);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2) {
        return this.buf.setBytes(n, byteBuf, n2);
    }

    @Override
    public int writableBytes() {
        return this.buf.writableBytes();
    }

    @Override
    public ByteOrder order() {
        return this.buf.order();
    }

    @Override
    public ByteBuf writeBytes(byte[] byArray, int n, int n2) {
        return this.buf.writeBytes(byArray, n, n2);
    }

    @Override
    public int ensureWritable(int n, boolean bl) {
        return this.buf.ensureWritable(n, bl);
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }

    @Override
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int n, int n2) {
        return this.buf.writeBytes(byteBuf, n, n2);
    }

    @Override
    public ByteBuf readSlice(int n) {
        return this.buf.readSlice(n);
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf) {
        return this.buf.setBytes(n, byteBuf);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        return this.buf.getBytes(n, byteBuffer);
    }

    public byte[] readByteArray() {
        byte[] byArray = new byte[this.readVarIntFromBuffer()];
        this.readBytes(byArray);
        return byArray;
    }

    @Override
    public long getLong(int n) {
        return this.buf.getLong(n);
    }

    @Override
    public ByteBuf setIndex(int n, int n2) {
        return this.buf.setIndex(n, n2);
    }

    @Override
    public ByteBuf copy() {
        return this.buf.copy();
    }

    @Override
    public String toString(int n, int n2, Charset charset) {
        return this.buf.toString(n, n2, charset);
    }

    @Override
    public ByteBuf slice(int n, int n2) {
        return this.buf.slice(n, n2);
    }

    public void writeBlockPos(BlockPos blockPos) {
        this.writeLong(blockPos.toLong());
    }

    @Override
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        return this.buf.readBytes(byteBuf);
    }

    public void writeChatComponent(IChatComponent iChatComponent) throws IOException {
        this.writeString(IChatComponent.Serializer.componentToJson(iChatComponent));
    }

    @Override
    public int bytesBefore(byte by) {
        return this.buf.bytesBefore(by);
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    @Override
    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int n) throws IOException {
        return this.buf.readBytes(outputStream, n);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        return this.buf.readBytes(gatheringByteChannel, n);
    }

    @Override
    public ByteBuf writerIndex(int n) {
        return this.buf.writerIndex(n);
    }

    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }

    @Override
    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }

    public String readStringFromBuffer(int n) {
        int n2 = this.readVarIntFromBuffer();
        if (n2 > n * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + n2 + " > " + n * 4 + ")");
        }
        if (n2 < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }
        String string = new String(this.readBytes(n2).array(), Charsets.UTF_8);
        if (string.length() > n) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + n2 + " > " + n + ")");
        }
        return string;
    }

    @Override
    public boolean release() {
        return this.buf.release();
    }

    @Override
    public ByteBuf writeFloat(float f) {
        return this.buf.writeFloat(f);
    }

    @Override
    public char getChar(int n) {
        return this.buf.getChar(n);
    }

    @Override
    public boolean isReadable(int n) {
        return this.buf.isReadable(n);
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        return this.buf.getBytes(n, byteBuf, n2, n3);
    }

    public void writeEnumValue(Enum<?> enum_) {
        this.writeVarIntToBuffer(enum_.ordinal());
    }
}

