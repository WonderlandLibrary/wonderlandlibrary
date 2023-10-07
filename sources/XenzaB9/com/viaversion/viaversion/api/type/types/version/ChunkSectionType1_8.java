// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import java.nio.ByteOrder;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
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
        final DataPalette blocks = chunkSection.palette(PaletteType.BLOCKS);
        blocks.addId(0);
        final ByteBuf littleEndianView = buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int idx = 0; idx < 4096; ++idx) {
            blocks.setIdAt(idx, littleEndianView.readShort());
        }
        return chunkSection;
    }
    
    @Override
    public void write(final ByteBuf buffer, final ChunkSection chunkSection) throws Exception {
        final DataPalette blocks = chunkSection.palette(PaletteType.BLOCKS);
        final ByteBuf littleEndianView = buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int idx = 0; idx < 4096; ++idx) {
            littleEndianView.writeShort(blocks.idAt(idx));
        }
    }
}
