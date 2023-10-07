package net.minecraft.network;

import com.google.common.base.Charsets;
import io.netty.buffer.*;
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
import org.jetbrains.annotations.Nullable;

public class PacketBuffer extends ByteBuf {

    private final ByteBuf buf;

    public PacketBuffer(ByteBuf wrapped) {
        this.buf = wrapped;
    }

    /**
     * Calculates the number of bytes required to fit the supplied int (0-5) if it were to be read/written using
     * readVarIntFromBuffer or writeVarIntToBuffer
     */
    public static int getVarIntSize(int input) {
        for (int i = 1; i < 5; ++i) {
            if ((input & -1 << i * 7) == 0) {
                return i;
            }
        }

        return 5;
    }

    public void writeByteArray(byte[] array) {
        this.writeVarIntToBuffer(array.length);
        this.writeBytes(array);
    }

    public byte[] readByteArray() {
        byte[] bytes = new byte[readVarIntFromBuffer()];
        readBytes(bytes);
        return bytes;
    }

    public BlockPos readBlockPos() {
        return BlockPos.fromLong(this.readLong());
    }

    public PacketBuffer writeBlockPos(BlockPos pos) {
        writeLong(pos.toLong());
        return this;
    }

    public IChatComponent readChatComponent() throws IOException {
        return IChatComponent.Serializer.jsonToComponent(this.readStringFromBuffer(32_767));
    }

    public PacketBuffer writeChatComponent(IChatComponent component) throws IOException {
        writeString(IChatComponent.Serializer.componentToJson(component));
        return this;
    }

    public <T extends Enum<T>> T readEnumValue(Class<T> enumClass) {
        return enumClass.getEnumConstants()[this.readVarIntFromBuffer()];
    }

    public PacketBuffer writeEnumValue(Enum<?> value) {
        writeVarIntToBuffer(value.ordinal());
        return this;
    }

    /**
     * Reads a compressed int from the buffer. To do so it maximally reads 5 byte-sized chunks whose most significant
     * bit dictates whether another byte should be read.
     */
    public int readVarIntFromBuffer() {
        int i = 0;
        int j = 0;

        while (true) {
            byte b0 = this.readByte();
            i |= (b0 & 127) << j++ * 7;

            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }

            if ((b0 & 128) != 128) {
                break;
            }
        }

        return i;
    }

    public long readVarLong() {
        long i = 0L;
        int j = 0;

        while (true) {
            byte b0 = this.readByte();
            i |= (long) (b0 & 127) << j++ * 7;

            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }

            if ((b0 & 128) != 128) {
                break;
            }
        }

        return i;
    }

    public PacketBuffer writeUuid(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public UUID readUuid() {
        return new UUID(this.readLong(), this.readLong());
    }

    /**
     * Writes a compressed int to the buffer. The smallest number of bytes to fit the passed int will be written. Of
     * each such byte only 7 bits will be used to describe the actual value since its most significant bit dictates
     * whether the next byte is part of that same int. Micro-optimization for int values that are expected to have
     * values below 128.
     */
    public PacketBuffer writeVarIntToBuffer(int input) {
        while ((input & -128) != 0) {
            writeByte(input & 127 | 128);
            input >>>= 7;
        }

        writeByte(input);
        return this;
    }

    public PacketBuffer writeVarLong(long value) {
        while ((value & -128L) != 0L) {
            writeByte((int) (value & 127L) | 128);
            value >>>= 7;
        }

        writeByte((int) value);
        return this;
    }

    /**
     * Writes a compressed NBTTagCompound to this buffer
     */
    public PacketBuffer writeNBTTagCompoundToBuffer(NBTTagCompound nbt) {
        if (nbt == null) {
            this.writeByte(0);
        } else {
            try {
                CompressedStreamTools.write(nbt, new ByteBufOutputStream(this));
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }

        return this;
    }

    /**
     * Reads a compressed NBTTagCompound from this buffer
     */
    public @Nullable NBTTagCompound readNBTTagCompoundFromBuffer() throws IOException {
        int i = this.readerIndex();
        byte b0 = this.readByte();

        if (b0 == 0) {
            return null;
        } else {
            readerIndex(i);

            try {
                return CompressedStreamTools.read(new ByteBufInputStream(this), new NBTSizeTracker(2097152L));
            } catch (IOException e) {
                throw new EncoderException(e);
            }
        }
    }

    /**
     * Writes the ItemStack's ID (short), then size (byte), then damage. (short)
     */
    public PacketBuffer writeItemStackToBuffer(ItemStack stack) {
        if (stack == null) {
            writeShort(-1);
        } else {
            writeShort(Item.getIdFromItem(stack.getItem()));
            writeByte(stack.stackSize);
            writeShort(stack.getMetadata());
            NBTTagCompound tagCompound = null;

            if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
                tagCompound = stack.getTagCompound();
            }

            writeNBTTagCompoundToBuffer(tagCompound);
        }

        return this;
    }

    /**
     * Reads an ItemStack from this buffer
     */
    public ItemStack readItemStackFromBuffer() throws IOException {
        ItemStack itemstack = null;
        int i = this.readShort();

        if (i >= 0) {
            int j = this.readByte();
            int k = this.readShort();
            itemstack = new ItemStack(Item.getItemById(i), j, k);
            itemstack.setTagCompound(this.readNBTTagCompoundFromBuffer());
        }

        return itemstack;
    }

    /**
     * Reads a string from this buffer. Expected parameter is maximum allowed string length. Will throw IOException if
     * string length exceeds this value!
     */
    public String readStringFromBuffer(int maxLength) {
        int i = this.readVarIntFromBuffer();

        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        } else if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = new String(this.readBytes(i).array(), Charsets.UTF_8);

            if (s.length() > maxLength) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
            } else {
                return s;
            }
        }
    }

    public PacketBuffer writeString(String string) {
        byte[] bytes = string.getBytes(Charsets.UTF_8);

        if (bytes.length > 32767) {
            throw new EncoderException("String too big (was " + bytes.length + " bytes encoded, max " + 32767 + ")");
        } else {
            this.writeVarIntToBuffer(bytes.length);
            this.writeBytes(bytes);
            return this;
        }
    }

    public int capacity() {
        return this.buf.capacity();
    }

    public ByteBuf capacity(int i) {
        return this.buf.capacity(i);
    }

    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    public ByteOrder order() {
        return this.buf.order();
    }

    public ByteBuf order(ByteOrder order) {
        return this.buf.order(order);
    }

    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }

    public boolean isDirect() {
        return this.buf.isDirect();
    }

    public int readerIndex() {
        return this.buf.readerIndex();
    }

    public ByteBuf readerIndex(int i) {
        return this.buf.readerIndex(i);
    }

    public int writerIndex() {
        return this.buf.writerIndex();
    }

    public ByteBuf writerIndex(int i) {
        return this.buf.writerIndex(i);
    }

    public ByteBuf setIndex(int i, int v) {
        return this.buf.setIndex(i, v);
    }

    public int readableBytes() {
        return this.buf.readableBytes();
    }

    public int writableBytes() {
        return this.buf.writableBytes();
    }

    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    public boolean isReadable() {
        return this.buf.isReadable();
    }

    public boolean isReadable(int i) {
        return this.buf.isReadable(i);
    }

    public boolean isWritable() {
        return this.buf.isWritable();
    }

    public boolean isWritable(int i) {
        return this.buf.isWritable(i);
    }

    public ByteBuf clear() {
        return this.buf.clear();
    }

    public ByteBuf markReaderIndex() {
        return this.buf.markReaderIndex();
    }

    public ByteBuf resetReaderIndex() {
        return this.buf.resetReaderIndex();
    }

    public ByteBuf markWriterIndex() {
        return this.buf.markWriterIndex();
    }

    public ByteBuf resetWriterIndex() {
        return this.buf.resetWriterIndex();
    }

    public ByteBuf discardReadBytes() {
        return this.buf.discardReadBytes();
    }

    public ByteBuf discardSomeReadBytes() {
        return this.buf.discardSomeReadBytes();
    }

    public ByteBuf ensureWritable(int i) {
        return this.buf.ensureWritable(i);
    }

    public int ensureWritable(int i, boolean b) {
        return this.buf.ensureWritable(i, b);
    }

    public boolean getBoolean(int i) {
        return this.buf.getBoolean(i);
    }

    public byte getByte(int i) {
        return this.buf.getByte(i);
    }

    public short getUnsignedByte(int i) {
        return this.buf.getUnsignedByte(i);
    }

    public short getShort(int i) {
        return this.buf.getShort(i);
    }

    public int getUnsignedShort(int i) {
        return this.buf.getUnsignedShort(i);
    }

    public int getMedium(int i) {
        return this.buf.getMedium(i);
    }

    public int getUnsignedMedium(int i) {
        return this.buf.getUnsignedMedium(i);
    }

    public int getInt(int i) {
        return this.buf.getInt(i);
    }

    public long getUnsignedInt(int i) {
        return this.buf.getUnsignedInt(i);
    }

    public long getLong(int i) {
        return this.buf.getLong(i);
    }

    public char getChar(int i) {
        return this.buf.getChar(i);
    }

    public float getFloat(int i) {
        return this.buf.getFloat(i);
    }

    public double getDouble(int i) {
        return this.buf.getDouble(i);
    }

    public ByteBuf getBytes(int i, ByteBuf dest) {
        return this.buf.getBytes(i, dest);
    }

    public ByteBuf getBytes(int i, ByteBuf dest, int length) {
        return this.buf.getBytes(i, dest, length);
    }

    public ByteBuf getBytes(int i, ByteBuf dest, int dstIndex, int length) {
        return this.buf.getBytes(i, dest, dstIndex, length);
    }

    public ByteBuf getBytes(int i, byte[] dst) {
        return this.buf.getBytes(i, dst);
    }

    public ByteBuf getBytes(int i, byte[] dst, int dstIndex, int length) {
        return this.buf.getBytes(i, dst, dstIndex, length);
    }

    public ByteBuf getBytes(int i, ByteBuffer dst) {
        return this.buf.getBytes(i, dst);
    }

    public ByteBuf getBytes(int i, OutputStream out, int length) throws IOException {
        return this.buf.getBytes(i, out, length);
    }

    public int getBytes(int i, GatheringByteChannel out, int length) throws IOException {
        return this.buf.getBytes(i, out, length);
    }

    public ByteBuf setBoolean(int i, boolean b) {
        return this.buf.setBoolean(i, b);
    }

    public ByteBuf setByte(int i, int value) {
        return this.buf.setByte(i, value);
    }

    public ByteBuf setShort(int i, int v) {
        return this.buf.setShort(i, v);
    }

    public ByteBuf setMedium(int i, int v) {
        return this.buf.setMedium(i, v);
    }

    public ByteBuf setInt(int i, int v) {
        return this.buf.setInt(i, v);
    }

    public ByteBuf setLong(int i, long v) {
        return this.buf.setLong(i, v);
    }

    public ByteBuf setChar(int i, int v) {
        return this.buf.setChar(i, v);
    }

    public ByteBuf setFloat(int i, float v) {
        return this.buf.setFloat(i, v);
    }

    public ByteBuf setDouble(int i, double v) {
        return this.buf.setDouble(i, v);
    }

    public ByteBuf setBytes(int i, ByteBuf src) {
        return this.buf.setBytes(i, src);
    }

    public ByteBuf setBytes(int i, ByteBuf src, int length) {
        return this.buf.setBytes(i, src, length);
    }

    public ByteBuf setBytes(int i, ByteBuf src, int srcIndex, int length) {
        return this.buf.setBytes(i, src, srcIndex, length);
    }

    public ByteBuf setBytes(int i, byte[] src) {
        return this.buf.setBytes(i, src);
    }

    public ByteBuf setBytes(int i, byte[] src, int srcIndex, int length) {
        return this.buf.setBytes(i, src, srcIndex, length);
    }

    public ByteBuf setBytes(int i, ByteBuffer src) {
        return this.buf.setBytes(i, src);
    }

    public int setBytes(int i, InputStream in, int length) throws IOException {
        return this.buf.setBytes(i, in, length);
    }

    public int setBytes(int i, ScatteringByteChannel in, int length) throws IOException {
        return this.buf.setBytes(i, in, length);
    }

    public ByteBuf setZero(int i, int length) {
        return this.buf.setZero(i, length);
    }

    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    public byte readByte() {
        return this.buf.readByte();
    }

    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    public short readShort() {
        return this.buf.readShort();
    }

    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    public int readMedium() {
        return this.buf.readMedium();
    }

    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }

    public int readInt() {
        return this.buf.readInt();
    }

    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }

    public long readLong() {
        return this.buf.readLong();
    }

    public char readChar() {
        return this.buf.readChar();
    }

    public float readFloat() {
        return this.buf.readFloat();
    }

    public double readDouble() {
        return this.buf.readDouble();
    }

    public ByteBuf readBytes(int length) {
        return this.buf.readBytes(length);
    }

    public ByteBuf readSlice(int length) {
        return this.buf.readSlice(length);
    }

    public ByteBuf readBytes(ByteBuf dst) {
        return this.buf.readBytes(dst);
    }

    public ByteBuf readBytes(ByteBuf dst, int length) {
        return this.buf.readBytes(dst, length);
    }

    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return this.buf.readBytes(dst, dstIndex, length);
    }

    public ByteBuf readBytes(byte[] dst) {
        return this.buf.readBytes(dst);
    }

    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return this.buf.readBytes(dst, dstIndex, length);
    }

    public ByteBuf readBytes(ByteBuffer dst) {
        return this.buf.readBytes(dst);
    }

    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        return this.buf.readBytes(out, length);
    }

    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return this.buf.readBytes(out, length);
    }

    public ByteBuf skipBytes(int length) {
        return this.buf.skipBytes(length);
    }

    public ByteBuf writeBoolean(boolean b) {
        return this.buf.writeBoolean(b);
    }

    public ByteBuf writeByte(int i) {
        return this.buf.writeByte(i);
    }

    public ByteBuf writeShort(int i) {
        return this.buf.writeShort(i);
    }

    public ByteBuf writeMedium(int i) {
        return this.buf.writeMedium(i);
    }

    public ByteBuf writeInt(int i) {
        return this.buf.writeInt(i);
    }

    public ByteBuf writeLong(long l) {
        return this.buf.writeLong(l);
    }

    public ByteBuf writeChar(int ch) {
        return this.buf.writeChar(ch);
    }

    public ByteBuf writeFloat(float f) {
        return this.buf.writeFloat(f);
    }

    public ByteBuf writeDouble(double d) {
        return this.buf.writeDouble(d);
    }

    public ByteBuf writeBytes(ByteBuf src) {
        return this.buf.writeBytes(src);
    }

    public ByteBuf writeBytes(ByteBuf src, int length) {
        return this.buf.writeBytes(src, length);
    }

    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return this.buf.writeBytes(src, srcIndex, length);
    }

    public ByteBuf writeBytes(byte[] src) {
        return this.buf.writeBytes(src);
    }

    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return this.buf.writeBytes(src, srcIndex, length);
    }

    public ByteBuf writeBytes(ByteBuffer src) {
        return this.buf.writeBytes(src);
    }

    public int writeBytes(InputStream in, int length) throws IOException {
        return this.buf.writeBytes(in, length);
    }

    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return this.buf.writeBytes(in, length);
    }

    public ByteBuf writeZero(int i) {
        return this.buf.writeZero(i);
    }

    public int indexOf(int fromIndex, int toIndex, byte b) {
        return this.buf.indexOf(fromIndex, toIndex, b);
    }

    public int bytesBefore(byte b) {
        return this.buf.bytesBefore(b);
    }

    public int bytesBefore(int length, byte b) {
        return this.buf.bytesBefore(length, b);
    }

    public int bytesBefore(int i, int length, byte b) {
        return this.buf.bytesBefore(i, length, b);
    }

    public int forEachByte(ByteBufProcessor processor) {
        return this.buf.forEachByte(processor);
    }

    public int forEachByte(int i, int length, ByteBufProcessor processor) {
        return this.buf.forEachByte(i, length, processor);
    }

    public int forEachByteDesc(ByteBufProcessor processor) {
        return this.buf.forEachByteDesc(processor);
    }

    public int forEachByteDesc(int i, int length, ByteBufProcessor processor) {
        return this.buf.forEachByteDesc(i, length, processor);
    }

    public ByteBuf copy() {
        return this.buf.copy();
    }

    public ByteBuf copy(int from, int length) {
        return this.buf.copy(from, length);
    }

    public ByteBuf slice() {
        return this.buf.slice();
    }

    public ByteBuf slice(int from, int slice) {
        return this.buf.slice(from, slice);
    }

    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }

    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }

    public ByteBuffer nioBuffer(int i, int length) {
        return this.buf.nioBuffer(i, length);
    }

    public ByteBuffer internalNioBuffer(int i, int length) {
        return this.buf.internalNioBuffer(i, length);
    }

    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int i, int length) {
        return this.buf.nioBuffers(i, length);
    }

    public boolean hasArray() {
        return this.buf.hasArray();
    }

    public byte[] array() {
        return this.buf.array();
    }

    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    public String toString(Charset charset) {
        return this.buf.toString(charset);
    }

    public String toString(int i, int length, Charset charset) {
        return this.buf.toString(i, length, charset);
    }

    @Override
    public int hashCode() {
        return this.buf.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this.buf.equals(o);
    }

    @Override
    public int compareTo(ByteBuf buf) {
        return this.buf.compareTo(buf);
    }

    @Override
    public String toString() {
        return this.buf.toString();
    }

    public ByteBuf retain(int retain) {
        return this.buf.retain(retain);
    }

    public ByteBuf retain() {
        return this.buf.retain();
    }

    public int refCnt() {
        return this.buf.refCnt();
    }

    public boolean release() {
        return this.buf.release();
    }

    public boolean release(int decrement) {
        return this.buf.release(decrement);
    }

}
