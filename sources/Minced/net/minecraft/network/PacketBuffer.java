// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import io.netty.util.ReferenceCounted;
import io.netty.util.ByteProcessor;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import io.netty.buffer.ByteBufAllocator;
import java.util.Date;
import net.minecraft.util.ResourceLocation;
import java.nio.charset.StandardCharsets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.io.DataInput;
import net.minecraft.nbt.NBTSizeTracker;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.EncoderException;
import java.io.DataOutput;
import net.minecraft.nbt.CompressedStreamTools;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.nbt.NBTTagCompound;
import java.util.UUID;
import java.io.IOException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import io.netty.handler.codec.DecoderException;
import io.netty.buffer.ByteBuf;

public class PacketBuffer extends ByteBuf
{
    private final ByteBuf buf;
    
    public PacketBuffer(final ByteBuf wrapped) {
        this.buf = wrapped;
    }
    
    public static int getVarIntSize(final int input) {
        for (int i = 1; i < 5; ++i) {
            if ((input & -1 << i * 7) == 0x0) {
                return i;
            }
        }
        return 5;
    }
    
    public PacketBuffer writeByteArray(final byte[] array) {
        this.writeVarInt(array.length);
        this.writeBytes(array);
        return this;
    }
    
    public byte[] readByteArray() {
        return this.readByteArray(this.readableBytes());
    }
    
    public byte[] readByteArray(final int maxLength) {
        final int i = this.readVarInt();
        if (i > maxLength) {
            throw new DecoderException("ByteArray with size " + i + " is bigger than allowed " + maxLength);
        }
        final byte[] abyte = new byte[i];
        this.readBytes(abyte);
        return abyte;
    }
    
    public PacketBuffer writeVarIntArray(final int[] array) {
        this.writeVarInt(array.length);
        for (final int i : array) {
            this.writeVarInt(i);
        }
        return this;
    }
    
    public int[] readVarIntArray() {
        return this.readVarIntArray(this.readableBytes());
    }
    
    public int[] readVarIntArray(final int maxLength) {
        final int i = this.readVarInt();
        if (i > maxLength) {
            throw new DecoderException("VarIntArray with size " + i + " is bigger than allowed " + maxLength);
        }
        final int[] aint = new int[i];
        for (int j = 0; j < aint.length; ++j) {
            aint[j] = this.readVarInt();
        }
        return aint;
    }
    
    public PacketBuffer writeLongArray(final long[] array) {
        this.writeVarInt(array.length);
        for (final long i : array) {
            this.writeLong(i);
        }
        return this;
    }
    
    public long[] readLongArray(@Nullable final long[] array) {
        return this.readLongArray(array, this.readableBytes() / 8);
    }
    
    public long[] readLongArray(@Nullable long[] array, final int maxLength) {
        final int i = this.readVarInt();
        if (array == null || array.length != i) {
            if (i > maxLength) {
                throw new DecoderException("LongArray with size " + i + " is bigger than allowed " + maxLength);
            }
            array = new long[i];
        }
        for (int j = 0; j < array.length; ++j) {
            array[j] = this.readLong();
        }
        return array;
    }
    
    public BlockPos readBlockPos() {
        return BlockPos.fromLong(this.readLong());
    }
    
    public PacketBuffer writeBlockPos(final BlockPos pos) {
        this.writeLong(pos.toLong());
        return this;
    }
    
    public ITextComponent readTextComponent() throws IOException {
        return ITextComponent.Serializer.jsonToComponent(this.readString(32767));
    }
    
    public PacketBuffer writeTextComponent(final ITextComponent component) {
        return this.writeString(ITextComponent.Serializer.componentToJson(component));
    }
    
    public <T extends Enum<T>> T readEnumValue(final Class<T> enumClass) {
        return enumClass.getEnumConstants()[this.readVarInt()];
    }
    
    public PacketBuffer writeEnumValue(final Enum<?> value) {
        return this.writeVarInt(value.ordinal());
    }
    
    public int readVarInt() {
        int i = 0;
        int j = 0;
        while (true) {
            final byte b0 = this.readByte();
            i |= (b0 & 0x7F) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
            if ((b0 & 0x80) != 0x80) {
                return i;
            }
        }
    }
    
    public long readVarLong() {
        long i = 0L;
        int j = 0;
        while (true) {
            final byte b0 = this.readByte();
            i |= (long)(b0 & 0x7F) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
            if ((b0 & 0x80) != 0x80) {
                return i;
            }
        }
    }
    
    public PacketBuffer writeUniqueId(final UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
        return this;
    }
    
    public UUID readUniqueId() {
        return new UUID(this.readLong(), this.readLong());
    }
    
    public PacketBuffer writeVarInt(int input) {
        while ((input & 0xFFFFFF80) != 0x0) {
            this.writeByte((input & 0x7F) | 0x80);
            input >>>= 7;
        }
        this.writeByte(input);
        return this;
    }
    
    public PacketBuffer writeVarLong(long value) {
        while ((value & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
            this.writeByte((int)(value & 0x7FL) | 0x80);
            value >>>= 7;
        }
        this.writeByte((int)value);
        return this;
    }
    
    public PacketBuffer writeCompoundTag(@Nullable final NBTTagCompound nbt) {
        if (nbt == null) {
            this.writeByte(0);
        }
        else {
            try {
                CompressedStreamTools.write(nbt, (DataOutput)new ByteBufOutputStream((ByteBuf)this));
            }
            catch (IOException ioexception) {
                throw new EncoderException((Throwable)ioexception);
            }
        }
        return this;
    }
    
    @Nullable
    public NBTTagCompound readCompoundTag() throws IOException {
        final int i = this.readerIndex();
        final byte b0 = this.readByte();
        if (b0 == 0) {
            return null;
        }
        this.readerIndex(i);
        try {
            return CompressedStreamTools.read((DataInput)new ByteBufInputStream((ByteBuf)this), new NBTSizeTracker(2097152L));
        }
        catch (IOException ioexception) {
            throw new EncoderException((Throwable)ioexception);
        }
    }
    
    public PacketBuffer writeItemStack(final ItemStack stack) {
        if (stack.isEmpty()) {
            this.writeShort(-1);
        }
        else {
            this.writeShort(Item.getIdFromItem(stack.getItem()));
            this.writeByte(stack.getCount());
            this.writeShort(stack.getMetadata());
            NBTTagCompound nbttagcompound = null;
            if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
                nbttagcompound = stack.getTagCompound();
            }
            this.writeCompoundTag(nbttagcompound);
        }
        return this;
    }
    
    public ItemStack readItemStack() throws IOException {
        final int i = this.readShort();
        if (i < 0) {
            return ItemStack.EMPTY;
        }
        final int j = this.readByte();
        final int k = this.readShort();
        final ItemStack itemstack = new ItemStack(Item.getItemById(i), j, k);
        itemstack.setTagCompound(this.readCompoundTag());
        return itemstack;
    }
    
    public String readString(final int maxLength) {
        final int i = this.readVarInt();
        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        }
        if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }
        final String s = this.toString(this.readerIndex(), i, StandardCharsets.UTF_8);
        this.readerIndex(this.readerIndex() + i);
        if (s.length() > maxLength) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
        }
        return s;
    }
    
    public PacketBuffer writeString(final String string) {
        final byte[] abyte = string.getBytes(StandardCharsets.UTF_8);
        if (abyte.length > 32767) {
            throw new EncoderException("String too big (was " + abyte.length + " bytes encoded, max " + 32767 + ")");
        }
        this.writeVarInt(abyte.length);
        this.writeBytes(abyte);
        return this;
    }
    
    public ResourceLocation readResourceLocation() {
        return new ResourceLocation(this.readString(32767));
    }
    
    public PacketBuffer writeResourceLocation(final ResourceLocation resourceLocationIn) {
        this.writeString(resourceLocationIn.toString());
        return this;
    }
    
    public Date readTime() {
        return new Date(this.readLong());
    }
    
    public PacketBuffer writeTime(final Date time) {
        this.writeLong(time.getTime());
        return this;
    }
    
    public int capacity() {
        return this.buf.capacity();
    }
    
    public ByteBuf capacity(final int p_capacity_1_) {
        return this.buf.capacity(p_capacity_1_);
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
    
    public ByteBuf order(final ByteOrder p_order_1_) {
        return this.buf.order(p_order_1_);
    }
    
    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }
    
    public boolean isDirect() {
        return this.buf.isDirect();
    }
    
    public boolean isReadOnly() {
        return this.buf.isReadOnly();
    }
    
    public ByteBuf asReadOnly() {
        return this.buf.asReadOnly();
    }
    
    public int readerIndex() {
        return this.buf.readerIndex();
    }
    
    public ByteBuf readerIndex(final int p_readerIndex_1_) {
        return this.buf.readerIndex(p_readerIndex_1_);
    }
    
    public int writerIndex() {
        return this.buf.writerIndex();
    }
    
    public ByteBuf writerIndex(final int p_writerIndex_1_) {
        return this.buf.writerIndex(p_writerIndex_1_);
    }
    
    public ByteBuf setIndex(final int p_setIndex_1_, final int p_setIndex_2_) {
        return this.buf.setIndex(p_setIndex_1_, p_setIndex_2_);
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
    
    public boolean isReadable(final int p_isReadable_1_) {
        return this.buf.isReadable(p_isReadable_1_);
    }
    
    public boolean isWritable() {
        return this.buf.isWritable();
    }
    
    public boolean isWritable(final int p_isWritable_1_) {
        return this.buf.isWritable(p_isWritable_1_);
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
    
    public ByteBuf ensureWritable(final int p_ensureWritable_1_) {
        return this.buf.ensureWritable(p_ensureWritable_1_);
    }
    
    public int ensureWritable(final int p_ensureWritable_1_, final boolean p_ensureWritable_2_) {
        return this.buf.ensureWritable(p_ensureWritable_1_, p_ensureWritable_2_);
    }
    
    public boolean getBoolean(final int p_getBoolean_1_) {
        return this.buf.getBoolean(p_getBoolean_1_);
    }
    
    public byte getByte(final int p_getByte_1_) {
        return this.buf.getByte(p_getByte_1_);
    }
    
    public short getUnsignedByte(final int p_getUnsignedByte_1_) {
        return this.buf.getUnsignedByte(p_getUnsignedByte_1_);
    }
    
    public short getShort(final int p_getShort_1_) {
        return this.buf.getShort(p_getShort_1_);
    }
    
    public short getShortLE(final int p_getShortLE_1_) {
        return this.buf.getShortLE(p_getShortLE_1_);
    }
    
    public int getUnsignedShort(final int p_getUnsignedShort_1_) {
        return this.buf.getUnsignedShort(p_getUnsignedShort_1_);
    }
    
    public int getUnsignedShortLE(final int p_getUnsignedShortLE_1_) {
        return this.buf.getUnsignedShortLE(p_getUnsignedShortLE_1_);
    }
    
    public int getMedium(final int p_getMedium_1_) {
        return this.buf.getMedium(p_getMedium_1_);
    }
    
    public int getMediumLE(final int p_getMediumLE_1_) {
        return this.buf.getMediumLE(p_getMediumLE_1_);
    }
    
    public int getUnsignedMedium(final int p_getUnsignedMedium_1_) {
        return this.buf.getUnsignedMedium(p_getUnsignedMedium_1_);
    }
    
    public int getUnsignedMediumLE(final int p_getUnsignedMediumLE_1_) {
        return this.buf.getUnsignedMediumLE(p_getUnsignedMediumLE_1_);
    }
    
    public int getInt(final int p_getInt_1_) {
        return this.buf.getInt(p_getInt_1_);
    }
    
    public int getIntLE(final int p_getIntLE_1_) {
        return this.buf.getIntLE(p_getIntLE_1_);
    }
    
    public long getUnsignedInt(final int p_getUnsignedInt_1_) {
        return this.buf.getUnsignedInt(p_getUnsignedInt_1_);
    }
    
    public long getUnsignedIntLE(final int p_getUnsignedIntLE_1_) {
        return this.buf.getUnsignedIntLE(p_getUnsignedIntLE_1_);
    }
    
    public long getLong(final int p_getLong_1_) {
        return this.buf.getLong(p_getLong_1_);
    }
    
    public long getLongLE(final int p_getLongLE_1_) {
        return this.buf.getLongLE(p_getLongLE_1_);
    }
    
    public char getChar(final int p_getChar_1_) {
        return this.buf.getChar(p_getChar_1_);
    }
    
    public float getFloat(final int p_getFloat_1_) {
        return this.buf.getFloat(p_getFloat_1_);
    }
    
    public double getDouble(final int p_getDouble_1_) {
        return this.buf.getDouble(p_getDouble_1_);
    }
    
    public ByteBuf getBytes(final int p_getBytes_1_, final ByteBuf p_getBytes_2_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }
    
    public ByteBuf getBytes(final int p_getBytes_1_, final ByteBuf p_getBytes_2_, final int p_getBytes_3_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }
    
    public ByteBuf getBytes(final int p_getBytes_1_, final ByteBuf p_getBytes_2_, final int p_getBytes_3_, final int p_getBytes_4_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }
    
    public ByteBuf getBytes(final int p_getBytes_1_, final byte[] p_getBytes_2_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }
    
    public ByteBuf getBytes(final int p_getBytes_1_, final byte[] p_getBytes_2_, final int p_getBytes_3_, final int p_getBytes_4_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }
    
    public ByteBuf getBytes(final int p_getBytes_1_, final ByteBuffer p_getBytes_2_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }
    
    public ByteBuf getBytes(final int p_getBytes_1_, final OutputStream p_getBytes_2_, final int p_getBytes_3_) throws IOException {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }
    
    public int getBytes(final int p_getBytes_1_, final GatheringByteChannel p_getBytes_2_, final int p_getBytes_3_) throws IOException {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }
    
    public int getBytes(final int p_getBytes_1_, final FileChannel p_getBytes_2_, final long p_getBytes_3_, final int p_getBytes_5_) throws IOException {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_5_);
    }
    
    public CharSequence getCharSequence(final int p_getCharSequence_1_, final int p_getCharSequence_2_, final Charset p_getCharSequence_3_) {
        return this.buf.getCharSequence(p_getCharSequence_1_, p_getCharSequence_2_, p_getCharSequence_3_);
    }
    
    public ByteBuf setBoolean(final int p_setBoolean_1_, final boolean p_setBoolean_2_) {
        return this.buf.setBoolean(p_setBoolean_1_, p_setBoolean_2_);
    }
    
    public ByteBuf setByte(final int p_setByte_1_, final int p_setByte_2_) {
        return this.buf.setByte(p_setByte_1_, p_setByte_2_);
    }
    
    public ByteBuf setShort(final int p_setShort_1_, final int p_setShort_2_) {
        return this.buf.setShort(p_setShort_1_, p_setShort_2_);
    }
    
    public ByteBuf setShortLE(final int p_setShortLE_1_, final int p_setShortLE_2_) {
        return this.buf.setShortLE(p_setShortLE_1_, p_setShortLE_2_);
    }
    
    public ByteBuf setMedium(final int p_setMedium_1_, final int p_setMedium_2_) {
        return this.buf.setMedium(p_setMedium_1_, p_setMedium_2_);
    }
    
    public ByteBuf setMediumLE(final int p_setMediumLE_1_, final int p_setMediumLE_2_) {
        return this.buf.setMediumLE(p_setMediumLE_1_, p_setMediumLE_2_);
    }
    
    public ByteBuf setInt(final int p_setInt_1_, final int p_setInt_2_) {
        return this.buf.setInt(p_setInt_1_, p_setInt_2_);
    }
    
    public ByteBuf setIntLE(final int p_setIntLE_1_, final int p_setIntLE_2_) {
        return this.buf.setIntLE(p_setIntLE_1_, p_setIntLE_2_);
    }
    
    public ByteBuf setLong(final int p_setLong_1_, final long p_setLong_2_) {
        return this.buf.setLong(p_setLong_1_, p_setLong_2_);
    }
    
    public ByteBuf setLongLE(final int p_setLongLE_1_, final long p_setLongLE_2_) {
        return this.buf.setLongLE(p_setLongLE_1_, p_setLongLE_2_);
    }
    
    public ByteBuf setChar(final int p_setChar_1_, final int p_setChar_2_) {
        return this.buf.setChar(p_setChar_1_, p_setChar_2_);
    }
    
    public ByteBuf setFloat(final int p_setFloat_1_, final float p_setFloat_2_) {
        return this.buf.setFloat(p_setFloat_1_, p_setFloat_2_);
    }
    
    public ByteBuf setDouble(final int p_setDouble_1_, final double p_setDouble_2_) {
        return this.buf.setDouble(p_setDouble_1_, p_setDouble_2_);
    }
    
    public ByteBuf setBytes(final int p_setBytes_1_, final ByteBuf p_setBytes_2_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }
    
    public ByteBuf setBytes(final int p_setBytes_1_, final ByteBuf p_setBytes_2_, final int p_setBytes_3_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }
    
    public ByteBuf setBytes(final int p_setBytes_1_, final ByteBuf p_setBytes_2_, final int p_setBytes_3_, final int p_setBytes_4_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }
    
    public ByteBuf setBytes(final int p_setBytes_1_, final byte[] p_setBytes_2_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }
    
    public ByteBuf setBytes(final int p_setBytes_1_, final byte[] p_setBytes_2_, final int p_setBytes_3_, final int p_setBytes_4_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }
    
    public ByteBuf setBytes(final int p_setBytes_1_, final ByteBuffer p_setBytes_2_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }
    
    public int setBytes(final int p_setBytes_1_, final InputStream p_setBytes_2_, final int p_setBytes_3_) throws IOException {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }
    
    public int setBytes(final int p_setBytes_1_, final ScatteringByteChannel p_setBytes_2_, final int p_setBytes_3_) throws IOException {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }
    
    public int setBytes(final int p_setBytes_1_, final FileChannel p_setBytes_2_, final long p_setBytes_3_, final int p_setBytes_5_) throws IOException {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_5_);
    }
    
    public ByteBuf setZero(final int p_setZero_1_, final int p_setZero_2_) {
        return this.buf.setZero(p_setZero_1_, p_setZero_2_);
    }
    
    public int setCharSequence(final int p_setCharSequence_1_, final CharSequence p_setCharSequence_2_, final Charset p_setCharSequence_3_) {
        return this.buf.setCharSequence(p_setCharSequence_1_, p_setCharSequence_2_, p_setCharSequence_3_);
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
    
    public short readShortLE() {
        return this.buf.readShortLE();
    }
    
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }
    
    public int readUnsignedShortLE() {
        return this.buf.readUnsignedShortLE();
    }
    
    public int readMedium() {
        return this.buf.readMedium();
    }
    
    public int readMediumLE() {
        return this.buf.readMediumLE();
    }
    
    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }
    
    public int readUnsignedMediumLE() {
        return this.buf.readUnsignedMediumLE();
    }
    
    public int readInt() {
        return this.buf.readInt();
    }
    
    public int readIntLE() {
        return this.buf.readIntLE();
    }
    
    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }
    
    public long readUnsignedIntLE() {
        return this.buf.readUnsignedIntLE();
    }
    
    public long readLong() {
        return this.buf.readLong();
    }
    
    public long readLongLE() {
        return this.buf.readLongLE();
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
    
    public ByteBuf readBytes(final int p_readBytes_1_) {
        return this.buf.readBytes(p_readBytes_1_);
    }
    
    public ByteBuf readSlice(final int p_readSlice_1_) {
        return this.buf.readSlice(p_readSlice_1_);
    }
    
    public ByteBuf readRetainedSlice(final int p_readRetainedSlice_1_) {
        return this.buf.readRetainedSlice(p_readRetainedSlice_1_);
    }
    
    public ByteBuf readBytes(final ByteBuf p_readBytes_1_) {
        return this.buf.readBytes(p_readBytes_1_);
    }
    
    public ByteBuf readBytes(final ByteBuf p_readBytes_1_, final int p_readBytes_2_) {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }
    
    public ByteBuf readBytes(final ByteBuf p_readBytes_1_, final int p_readBytes_2_, final int p_readBytes_3_) {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }
    
    public ByteBuf readBytes(final byte[] p_readBytes_1_) {
        return this.buf.readBytes(p_readBytes_1_);
    }
    
    public ByteBuf readBytes(final byte[] p_readBytes_1_, final int p_readBytes_2_, final int p_readBytes_3_) {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }
    
    public ByteBuf readBytes(final ByteBuffer p_readBytes_1_) {
        return this.buf.readBytes(p_readBytes_1_);
    }
    
    public ByteBuf readBytes(final OutputStream p_readBytes_1_, final int p_readBytes_2_) throws IOException {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }
    
    public int readBytes(final GatheringByteChannel p_readBytes_1_, final int p_readBytes_2_) throws IOException {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }
    
    public CharSequence readCharSequence(final int p_readCharSequence_1_, final Charset p_readCharSequence_2_) {
        return this.buf.readCharSequence(p_readCharSequence_1_, p_readCharSequence_2_);
    }
    
    public int readBytes(final FileChannel p_readBytes_1_, final long p_readBytes_2_, final int p_readBytes_4_) throws IOException {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_4_);
    }
    
    public ByteBuf skipBytes(final int p_skipBytes_1_) {
        return this.buf.skipBytes(p_skipBytes_1_);
    }
    
    public ByteBuf writeBoolean(final boolean p_writeBoolean_1_) {
        return this.buf.writeBoolean(p_writeBoolean_1_);
    }
    
    public ByteBuf writeByte(final int p_writeByte_1_) {
        return this.buf.writeByte(p_writeByte_1_);
    }
    
    public ByteBuf writeShort(final int p_writeShort_1_) {
        return this.buf.writeShort(p_writeShort_1_);
    }
    
    public ByteBuf writeShortLE(final int p_writeShortLE_1_) {
        return this.buf.writeShortLE(p_writeShortLE_1_);
    }
    
    public ByteBuf writeMedium(final int p_writeMedium_1_) {
        return this.buf.writeMedium(p_writeMedium_1_);
    }
    
    public ByteBuf writeMediumLE(final int p_writeMediumLE_1_) {
        return this.buf.writeMediumLE(p_writeMediumLE_1_);
    }
    
    public ByteBuf writeInt(final int p_writeInt_1_) {
        return this.buf.writeInt(p_writeInt_1_);
    }
    
    public ByteBuf writeIntLE(final int p_writeIntLE_1_) {
        return this.buf.writeIntLE(p_writeIntLE_1_);
    }
    
    public ByteBuf writeLong(final long p_writeLong_1_) {
        return this.buf.writeLong(p_writeLong_1_);
    }
    
    public ByteBuf writeLongLE(final long p_writeLongLE_1_) {
        return this.buf.writeLongLE(p_writeLongLE_1_);
    }
    
    public ByteBuf writeChar(final int p_writeChar_1_) {
        return this.buf.writeChar(p_writeChar_1_);
    }
    
    public ByteBuf writeFloat(final float p_writeFloat_1_) {
        return this.buf.writeFloat(p_writeFloat_1_);
    }
    
    public ByteBuf writeDouble(final double p_writeDouble_1_) {
        return this.buf.writeDouble(p_writeDouble_1_);
    }
    
    public ByteBuf writeBytes(final ByteBuf p_writeBytes_1_) {
        return this.buf.writeBytes(p_writeBytes_1_);
    }
    
    public ByteBuf writeBytes(final ByteBuf p_writeBytes_1_, final int p_writeBytes_2_) {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }
    
    public ByteBuf writeBytes(final ByteBuf p_writeBytes_1_, final int p_writeBytes_2_, final int p_writeBytes_3_) {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }
    
    public ByteBuf writeBytes(final byte[] p_writeBytes_1_) {
        return this.buf.writeBytes(p_writeBytes_1_);
    }
    
    public ByteBuf writeBytes(final byte[] p_writeBytes_1_, final int p_writeBytes_2_, final int p_writeBytes_3_) {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }
    
    public ByteBuf writeBytes(final ByteBuffer p_writeBytes_1_) {
        return this.buf.writeBytes(p_writeBytes_1_);
    }
    
    public int writeBytes(final InputStream p_writeBytes_1_, final int p_writeBytes_2_) throws IOException {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }
    
    public int writeBytes(final ScatteringByteChannel p_writeBytes_1_, final int p_writeBytes_2_) throws IOException {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }
    
    public int writeBytes(final FileChannel p_writeBytes_1_, final long p_writeBytes_2_, final int p_writeBytes_4_) throws IOException {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_4_);
    }
    
    public ByteBuf writeZero(final int p_writeZero_1_) {
        return this.buf.writeZero(p_writeZero_1_);
    }
    
    public int writeCharSequence(final CharSequence p_writeCharSequence_1_, final Charset p_writeCharSequence_2_) {
        return this.buf.writeCharSequence(p_writeCharSequence_1_, p_writeCharSequence_2_);
    }
    
    public int indexOf(final int p_indexOf_1_, final int p_indexOf_2_, final byte p_indexOf_3_) {
        return this.buf.indexOf(p_indexOf_1_, p_indexOf_2_, p_indexOf_3_);
    }
    
    public int bytesBefore(final byte p_bytesBefore_1_) {
        return this.buf.bytesBefore(p_bytesBefore_1_);
    }
    
    public int bytesBefore(final int p_bytesBefore_1_, final byte p_bytesBefore_2_) {
        return this.buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_);
    }
    
    public int bytesBefore(final int p_bytesBefore_1_, final int p_bytesBefore_2_, final byte p_bytesBefore_3_) {
        return this.buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_, p_bytesBefore_3_);
    }
    
    public int forEachByte(final ByteProcessor p_forEachByte_1_) {
        return this.buf.forEachByte(p_forEachByte_1_);
    }
    
    public int forEachByte(final int p_forEachByte_1_, final int p_forEachByte_2_, final ByteProcessor p_forEachByte_3_) {
        return this.buf.forEachByte(p_forEachByte_1_, p_forEachByte_2_, p_forEachByte_3_);
    }
    
    public int forEachByteDesc(final ByteProcessor p_forEachByteDesc_1_) {
        return this.buf.forEachByteDesc(p_forEachByteDesc_1_);
    }
    
    public int forEachByteDesc(final int p_forEachByteDesc_1_, final int p_forEachByteDesc_2_, final ByteProcessor p_forEachByteDesc_3_) {
        return this.buf.forEachByteDesc(p_forEachByteDesc_1_, p_forEachByteDesc_2_, p_forEachByteDesc_3_);
    }
    
    public ByteBuf copy() {
        return this.buf.copy();
    }
    
    public ByteBuf copy(final int p_copy_1_, final int p_copy_2_) {
        return this.buf.copy(p_copy_1_, p_copy_2_);
    }
    
    public ByteBuf slice() {
        return this.buf.slice();
    }
    
    public ByteBuf retainedSlice() {
        return this.buf.retainedSlice();
    }
    
    public ByteBuf slice(final int p_slice_1_, final int p_slice_2_) {
        return this.buf.slice(p_slice_1_, p_slice_2_);
    }
    
    public ByteBuf retainedSlice(final int p_retainedSlice_1_, final int p_retainedSlice_2_) {
        return this.buf.retainedSlice(p_retainedSlice_1_, p_retainedSlice_2_);
    }
    
    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }
    
    public ByteBuf retainedDuplicate() {
        return this.buf.retainedDuplicate();
    }
    
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }
    
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }
    
    public ByteBuffer nioBuffer(final int p_nioBuffer_1_, final int p_nioBuffer_2_) {
        return this.buf.nioBuffer(p_nioBuffer_1_, p_nioBuffer_2_);
    }
    
    public ByteBuffer internalNioBuffer(final int p_internalNioBuffer_1_, final int p_internalNioBuffer_2_) {
        return this.buf.internalNioBuffer(p_internalNioBuffer_1_, p_internalNioBuffer_2_);
    }
    
    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }
    
    public ByteBuffer[] nioBuffers(final int p_nioBuffers_1_, final int p_nioBuffers_2_) {
        return this.buf.nioBuffers(p_nioBuffers_1_, p_nioBuffers_2_);
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
    
    public String toString(final Charset p_toString_1_) {
        return this.buf.toString(p_toString_1_);
    }
    
    public String toString(final int p_toString_1_, final int p_toString_2_, final Charset p_toString_3_) {
        return this.buf.toString(p_toString_1_, p_toString_2_, p_toString_3_);
    }
    
    public int hashCode() {
        return this.buf.hashCode();
    }
    
    public boolean equals(final Object p_equals_1_) {
        return this.buf.equals(p_equals_1_);
    }
    
    public int compareTo(final ByteBuf p_compareTo_1_) {
        return this.buf.compareTo(p_compareTo_1_);
    }
    
    public String toString() {
        return this.buf.toString();
    }
    
    public ByteBuf retain(final int p_retain_1_) {
        return this.buf.retain(p_retain_1_);
    }
    
    public ByteBuf retain() {
        return this.buf.retain();
    }
    
    public ByteBuf touch() {
        return this.buf.touch();
    }
    
    public ByteBuf touch(final Object p_touch_1_) {
        return this.buf.touch(p_touch_1_);
    }
    
    public int refCnt() {
        return this.buf.refCnt();
    }
    
    public boolean release() {
        return this.buf.release();
    }
    
    public boolean release(final int p_release_1_) {
        return this.buf.release(p_release_1_);
    }
}
