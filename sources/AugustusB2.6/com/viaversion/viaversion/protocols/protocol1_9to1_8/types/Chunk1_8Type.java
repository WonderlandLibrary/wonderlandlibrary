// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.types;

import com.viaversion.viaversion.api.type.types.version.Types1_8;
import io.netty.buffer.Unpooled;
import com.viaversion.viaversion.api.minecraft.Environment;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.chunks.BaseChunk;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.types.minecraft.BaseChunkType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.PartialType;

public class Chunk1_8Type extends PartialType<Chunk, ClientWorld>
{
    public Chunk1_8Type(final ClientWorld param) {
        super(param, Chunk.class);
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }
    
    @Override
    public Chunk read(final ByteBuf input, final ClientWorld world) throws Exception {
        final int chunkX = input.readInt();
        final int chunkZ = input.readInt();
        final boolean fullChunk = input.readBoolean();
        final int bitmask = input.readUnsignedShort();
        final int dataLength = Type.VAR_INT.readPrimitive(input);
        final byte[] data = new byte[dataLength];
        input.readBytes(data);
        if (fullChunk && bitmask == 0) {
            return new BaseChunk(chunkX, chunkZ, true, false, 0, new ChunkSection[16], null, new ArrayList<CompoundTag>());
        }
        return deserialize(chunkX, chunkZ, fullChunk, world.getEnvironment() == Environment.NORMAL, bitmask, data);
    }
    
    @Override
    public void write(final ByteBuf output, final ClientWorld world, final Chunk chunk) throws Exception {
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        output.writeBoolean(chunk.isFullChunk());
        output.writeShort(chunk.getBitmask());
        final byte[] data = serialize(chunk);
        Type.VAR_INT.writePrimitive(output, data.length);
        output.writeBytes(data);
    }
    
    public static Chunk deserialize(final int chunkX, final int chunkZ, final boolean fullChunk, final boolean skyLight, final int bitmask, final byte[] data) throws Exception {
        final ByteBuf input = Unpooled.wrappedBuffer(data);
        final ChunkSection[] sections = new ChunkSection[16];
        int[] biomeData = null;
        for (int i = 0; i < sections.length; ++i) {
            if ((bitmask & 1 << i) != 0x0) {
                sections[i] = Types1_8.CHUNK_SECTION.read(input);
            }
        }
        for (int i = 0; i < sections.length; ++i) {
            if ((bitmask & 1 << i) != 0x0) {
                sections[i].getLight().readBlockLight(input);
            }
        }
        if (skyLight) {
            for (int i = 0; i < sections.length; ++i) {
                if ((bitmask & 1 << i) != 0x0) {
                    sections[i].getLight().readSkyLight(input);
                }
            }
        }
        if (fullChunk) {
            biomeData = new int[256];
            for (int i = 0; i < 256; ++i) {
                biomeData[i] = input.readUnsignedByte();
            }
        }
        input.release();
        return new BaseChunk(chunkX, chunkZ, fullChunk, false, bitmask, sections, biomeData, new ArrayList<CompoundTag>());
    }
    
    public static byte[] serialize(final Chunk chunk) throws Exception {
        final ByteBuf output = Unpooled.buffer();
        for (int i = 0; i < chunk.getSections().length; ++i) {
            if ((chunk.getBitmask() & 1 << i) != 0x0) {
                Types1_8.CHUNK_SECTION.write(output, chunk.getSections()[i]);
            }
        }
        for (int i = 0; i < chunk.getSections().length; ++i) {
            if ((chunk.getBitmask() & 1 << i) != 0x0) {
                chunk.getSections()[i].getLight().writeBlockLight(output);
            }
        }
        for (int i = 0; i < chunk.getSections().length; ++i) {
            if ((chunk.getBitmask() & 1 << i) != 0x0) {
                if (chunk.getSections()[i].getLight().hasSkyLight()) {
                    chunk.getSections()[i].getLight().writeSkyLight(output);
                }
            }
        }
        if (chunk.isFullChunk() && chunk.getBiomeData() != null) {
            for (final int biome : chunk.getBiomeData()) {
                output.writeByte((byte)biome);
            }
        }
        final byte[] data = new byte[output.readableBytes()];
        output.readBytes(data);
        output.release();
        return data;
    }
}
