// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.version;

import java.nio.ByteOrder;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionImpl;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.Type;

public class ChunkSectionType1_8 extends Type<ChunkSection>
{
    public ChunkSectionType1_8() {
        super("Chunk Section Type", ChunkSection.class);
    }
    
    @Override
    public ChunkSection read(final ByteBuf buffer) throws Exception {
        final ChunkSection chunkSection = new ChunkSectionImpl(true);
        chunkSection.addPaletteEntry(0);
        final ByteBuf littleEndianView = buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 4096; ++i) {
            final int mask = littleEndianView.readShort();
            final int type = mask >> 4;
            final int data = mask & 0xF;
            chunkSection.setBlockWithData(i, type, data);
        }
        return chunkSection;
    }
    
    @Override
    public void write(final ByteBuf buffer, final ChunkSection chunkSection) throws Exception {
        for (int y = 0; y < 16; ++y) {
            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    final int block = chunkSection.getFlatBlock(x, y, z);
                    buffer.writeByte(block);
                    buffer.writeByte(block >> 8);
                }
            }
        }
    }
}
