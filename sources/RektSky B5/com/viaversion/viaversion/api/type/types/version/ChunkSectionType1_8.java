/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionImpl;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import java.nio.ByteOrder;

public class ChunkSectionType1_8
extends Type<ChunkSection> {
    public ChunkSectionType1_8() {
        super("Chunk Section Type", ChunkSection.class);
    }

    @Override
    public ChunkSection read(ByteBuf buffer) throws Exception {
        ChunkSectionImpl chunkSection = new ChunkSectionImpl(true);
        chunkSection.addPaletteEntry(0);
        ByteBuf littleEndianView = buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int i2 = 0; i2 < 4096; ++i2) {
            short mask = littleEndianView.readShort();
            int type = mask >> 4;
            int data = mask & 0xF;
            chunkSection.setBlockWithData(i2, type, data);
        }
        return chunkSection;
    }

    @Override
    public void write(ByteBuf buffer, ChunkSection chunkSection) throws Exception {
        for (int y2 = 0; y2 < 16; ++y2) {
            for (int z2 = 0; z2 < 16; ++z2) {
                for (int x2 = 0; x2 < 16; ++x2) {
                    int block = chunkSection.getFlatBlock(x2, y2, z2);
                    buffer.writeByte(block);
                    buffer.writeByte(block >> 8);
                }
            }
        }
    }
}

