// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.types;

import com.viaversion.viaversion.api.type.types.minecraft.BaseChunkType;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.chunks.BaseChunk;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.logging.Level;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.Environment;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.PartialType;

public class Chunk1_13Type extends PartialType<Chunk, ClientWorld>
{
    public Chunk1_13Type(final ClientWorld param) {
        super(param, Chunk.class);
    }
    
    @Override
    public Chunk read(final ByteBuf input, final ClientWorld world) throws Exception {
        final int chunkX = input.readInt();
        final int chunkZ = input.readInt();
        final boolean fullChunk = input.readBoolean();
        final int primaryBitmask = Type.VAR_INT.readPrimitive(input);
        final ByteBuf data = input.readSlice(Type.VAR_INT.readPrimitive(input));
        final ChunkSection[] sections = new ChunkSection[16];
        for (int i = 0; i < 16; ++i) {
            if ((primaryBitmask & 1 << i) != 0x0) {
                final ChunkSection section = Types1_13.CHUNK_SECTION.read(data);
                sections[i] = section;
                section.getLight().readBlockLight(data);
                if (world.getEnvironment() == Environment.NORMAL) {
                    section.getLight().readSkyLight(data);
                }
            }
        }
        final int[] biomeData = (int[])(fullChunk ? new int[256] : null);
        if (fullChunk) {
            if (data.readableBytes() >= 1024) {
                for (int j = 0; j < 256; ++j) {
                    biomeData[j] = data.readInt();
                }
            }
            else {
                Via.getPlatform().getLogger().log(Level.WARNING, "Chunk x=" + chunkX + " z=" + chunkZ + " doesn't have biome data!");
            }
        }
        final List<CompoundTag> nbtData = new ArrayList<CompoundTag>(Arrays.asList((CompoundTag[])Type.NBT_ARRAY.read(input)));
        if (input.readableBytes() > 0) {
            final byte[] array = Type.REMAINING_BYTES.read(input);
            if (Via.getManager().isDebug()) {
                Via.getPlatform().getLogger().warning("Found " + array.length + " more bytes than expected while reading the chunk: " + chunkX + "/" + chunkZ);
            }
        }
        return new BaseChunk(chunkX, chunkZ, fullChunk, false, primaryBitmask, sections, biomeData, nbtData);
    }
    
    @Override
    public void write(final ByteBuf output, final ClientWorld world, final Chunk chunk) throws Exception {
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        output.writeBoolean(chunk.isFullChunk());
        Type.VAR_INT.writePrimitive(output, chunk.getBitmask());
        final ByteBuf buf = output.alloc().buffer();
        try {
            for (int i = 0; i < 16; ++i) {
                final ChunkSection section = chunk.getSections()[i];
                if (section != null) {
                    Types1_13.CHUNK_SECTION.write(buf, section);
                    section.getLight().writeBlockLight(buf);
                    if (section.getLight().hasSkyLight()) {
                        section.getLight().writeSkyLight(buf);
                    }
                }
            }
            buf.readerIndex(0);
            Type.VAR_INT.writePrimitive(output, buf.readableBytes() + (chunk.isBiomeData() ? 1024 : 0));
            output.writeBytes(buf);
        }
        finally {
            buf.release();
        }
        if (chunk.isBiomeData()) {
            for (final int value : chunk.getBiomeData()) {
                output.writeInt(value);
            }
        }
        Type.NBT_ARRAY.write(output, chunk.getBlockEntities().toArray(new CompoundTag[0]));
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }
}
