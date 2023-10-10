// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.version;

import java.util.function.IntToLongFunction;
import com.viaversion.viaversion.util.BiIntConsumer;
import com.viaversion.viaversion.util.CompactArrayUtil;
import java.util.Objects;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionImpl;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.Type;

public class ChunkSectionType1_16 extends Type<ChunkSection>
{
    private static final int GLOBAL_PALETTE = 15;
    
    public ChunkSectionType1_16() {
        super("Chunk Section Type", ChunkSection.class);
    }
    
    @Override
    public ChunkSection read(final ByteBuf buffer) throws Exception {
        final int originalBitsPerBlock;
        int bitsPerBlock = originalBitsPerBlock = buffer.readUnsignedByte();
        if (bitsPerBlock == 0 || bitsPerBlock > 8) {
            bitsPerBlock = 15;
        }
        ChunkSection chunkSection;
        if (bitsPerBlock != 15) {
            final int paletteLength = Type.VAR_INT.readPrimitive(buffer);
            chunkSection = new ChunkSectionImpl(false, paletteLength);
            for (int i = 0; i < paletteLength; ++i) {
                chunkSection.addPaletteEntry(Type.VAR_INT.readPrimitive(buffer));
            }
        }
        else {
            chunkSection = new ChunkSectionImpl(false);
        }
        final long[] blockData = new long[Type.VAR_INT.readPrimitive(buffer)];
        if (blockData.length > 0) {
            final char valuesPerLong = (char)(64 / bitsPerBlock);
            final int expectedLength = ('\u1000' + valuesPerLong - 1) / valuesPerLong;
            if (blockData.length != expectedLength) {
                throw new IllegalStateException("Block data length (" + blockData.length + ") does not match expected length (" + expectedLength + ")! bitsPerBlock=" + bitsPerBlock + ", originalBitsPerBlock=" + originalBitsPerBlock);
            }
            for (int j = 0; j < blockData.length; ++j) {
                blockData[j] = buffer.readLong();
            }
            final int bitsPerEntry = bitsPerBlock;
            final int entries = 4096;
            final long[] data = blockData;
            BiIntConsumer consumer;
            if (bitsPerBlock == 15) {
                final ChunkSection obj = chunkSection;
                Objects.requireNonNull(obj);
                consumer = obj::setFlatBlock;
            }
            else {
                final ChunkSection obj2 = chunkSection;
                Objects.requireNonNull(obj2);
                consumer = obj2::setPaletteIndex;
            }
            CompactArrayUtil.iterateCompactArrayWithPadding(bitsPerEntry, entries, data, consumer);
        }
        return chunkSection;
    }
    
    @Override
    public void write(final ByteBuf buffer, final ChunkSection chunkSection) throws Exception {
        int bitsPerBlock;
        for (bitsPerBlock = 4; chunkSection.getPaletteSize() > 1 << bitsPerBlock; ++bitsPerBlock) {}
        if (bitsPerBlock > 8) {
            bitsPerBlock = 15;
        }
        buffer.writeByte(bitsPerBlock);
        if (bitsPerBlock != 15) {
            Type.VAR_INT.writePrimitive(buffer, chunkSection.getPaletteSize());
            for (int i = 0; i < chunkSection.getPaletteSize(); ++i) {
                Type.VAR_INT.writePrimitive(buffer, chunkSection.getPaletteEntry(i));
            }
        }
        final int bitsPerEntry = bitsPerBlock;
        final int entries = 4096;
        IntToLongFunction valueGetter;
        if (bitsPerBlock == 15) {
            Objects.requireNonNull(chunkSection);
            valueGetter = chunkSection::getFlatBlock;
        }
        else {
            Objects.requireNonNull(chunkSection);
            valueGetter = chunkSection::getPaletteIndex;
        }
        final long[] data = CompactArrayUtil.createCompactArrayWithPadding(bitsPerEntry, entries, valueGetter);
        Type.VAR_INT.writePrimitive(buffer, data.length);
        for (final long l : data) {
            buffer.writeLong(l);
        }
    }
}
