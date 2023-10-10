// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_17to1_16_4.types;

import com.viaversion.viaversion.api.type.types.minecraft.BaseChunkType;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.chunks.BaseChunk;
import com.viaversion.viaversion.api.Via;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import java.util.BitSet;
import io.netty.buffer.ByteBuf;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.Type;

public final class Chunk1_17Type extends Type<Chunk>
{
    private static final CompoundTag[] EMPTY_COMPOUNDS;
    private final int ySectionCount;
    
    public Chunk1_17Type(final int ySectionCount) {
        super(Chunk.class);
        Preconditions.checkArgument(ySectionCount > 0);
        this.ySectionCount = ySectionCount;
    }
    
    @Override
    public Chunk read(final ByteBuf input) throws Exception {
        final int chunkX = input.readInt();
        final int chunkZ = input.readInt();
        final BitSet sectionsMask = BitSet.valueOf(Type.LONG_ARRAY_PRIMITIVE.read(input));
        final CompoundTag heightMap = Type.NBT.read(input);
        final int[] biomeData = Type.VAR_INT_ARRAY_PRIMITIVE.read(input);
        Type.VAR_INT.readPrimitive(input);
        final ChunkSection[] sections = new ChunkSection[this.ySectionCount];
        for (int i = 0; i < this.ySectionCount; ++i) {
            if (sectionsMask.get(i)) {
                final short nonAirBlocksCount = input.readShort();
                final ChunkSection section = Types1_16.CHUNK_SECTION.read(input);
                section.setNonAirBlocksCount(nonAirBlocksCount);
                sections[i] = section;
            }
        }
        final List<CompoundTag> nbtData = new ArrayList<CompoundTag>(Arrays.asList((CompoundTag[])Type.NBT_ARRAY.read(input)));
        if (input.readableBytes() > 0) {
            final byte[] array = Type.REMAINING_BYTES.read(input);
            if (Via.getManager().isDebug()) {
                Via.getPlatform().getLogger().warning("Found " + array.length + " more bytes than expected while reading the chunk: " + chunkX + "/" + chunkZ);
            }
        }
        return new BaseChunk(chunkX, chunkZ, true, false, sectionsMask, sections, biomeData, heightMap, nbtData);
    }
    
    @Override
    public void write(final ByteBuf output, final Chunk chunk) throws Exception {
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        Type.LONG_ARRAY_PRIMITIVE.write(output, chunk.getChunkMask().toLongArray());
        Type.NBT.write(output, chunk.getHeightMap());
        Type.VAR_INT_ARRAY_PRIMITIVE.write(output, chunk.getBiomeData());
        final ByteBuf buf = output.alloc().buffer();
        try {
            final ChunkSection[] sections2;
            final ChunkSection[] sections = sections2 = chunk.getSections();
            for (final ChunkSection section : sections2) {
                if (section != null) {
                    buf.writeShort(section.getNonAirBlocksCount());
                    Types1_16.CHUNK_SECTION.write(buf, section);
                }
            }
            buf.readerIndex(0);
            Type.VAR_INT.writePrimitive(output, buf.readableBytes());
            output.writeBytes(buf);
        }
        finally {
            buf.release();
        }
        Type.NBT_ARRAY.write(output, chunk.getBlockEntities().toArray(Chunk1_17Type.EMPTY_COMPOUNDS));
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }
    
    static {
        EMPTY_COMPOUNDS = new CompoundTag[0];
    }
}
