/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package com.viaversion.viaversion.protocols.protocol1_9to1_8.types;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk1_8;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.PartialType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.BaseChunkType;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.protocols.protocol1_10to1_9_3.Protocol1_10To1_9_3_4;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.logging.Level;

public class Chunk1_9to1_8Type
extends PartialType<Chunk, ClientChunks> {
    public static final int SECTION_COUNT = 16;
    private static final int SECTION_SIZE = 16;
    private static final int BIOME_DATA_LENGTH = 256;

    public Chunk1_9to1_8Type(ClientChunks chunks) {
        super(chunks, Chunk.class);
    }

    private static long toLong(int msw, int lsw) {
        return ((long)msw << 32) + (long)lsw - Integer.MIN_VALUE;
    }

    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }

    @Override
    public Chunk read(ByteBuf input, ClientChunks param) throws Exception {
        int i;
        boolean replacePistons = param.getUser().getProtocolInfo().getPipeline().contains(Protocol1_10To1_9_3_4.class) && Via.getConfig().isReplacePistons();
        int replacementId = Via.getConfig().getPistonReplacementId();
        int chunkX = input.readInt();
        int chunkZ = input.readInt();
        long chunkHash = Chunk1_9to1_8Type.toLong(chunkX, chunkZ);
        boolean fullChunk = input.readByte() != 0;
        int bitmask = input.readUnsignedShort();
        int dataLength = Type.VAR_INT.readPrimitive(input);
        BitSet usedSections = new BitSet(16);
        ChunkSection[] sections = new ChunkSection[16];
        int[] biomeData = null;
        for (int i2 = 0; i2 < 16; ++i2) {
            if ((bitmask & 1 << i2) == 0) continue;
            usedSections.set(i2);
        }
        int sectionCount = usedSections.cardinality();
        boolean isBulkPacket = param.getBulkChunks().remove(chunkHash);
        if (sectionCount == 0 && fullChunk && !isBulkPacket && param.getLoadedChunks().contains(chunkHash)) {
            param.getLoadedChunks().remove(chunkHash);
            return new Chunk1_8(chunkX, chunkZ);
        }
        int startIndex = input.readerIndex();
        param.getLoadedChunks().add(chunkHash);
        for (i = 0; i < 16; ++i) {
            ChunkSection section;
            if (!usedSections.get(i)) continue;
            sections[i] = section = (ChunkSection)Types1_8.CHUNK_SECTION.read(input);
            if (!replacePistons) continue;
            section.replacePaletteEntry(36, replacementId);
        }
        for (i = 0; i < 16; ++i) {
            if (!usedSections.get(i)) continue;
            sections[i].getLight().readBlockLight(input);
        }
        int bytesLeft = dataLength - (input.readerIndex() - startIndex);
        if (bytesLeft >= 2048) {
            for (int i3 = 0; i3 < 16; ++i3) {
                if (!usedSections.get(i3)) continue;
                sections[i3].getLight().readSkyLight(input);
                bytesLeft -= 2048;
            }
        }
        if (bytesLeft >= 256) {
            biomeData = new int[256];
            for (int i4 = 0; i4 < 256; ++i4) {
                biomeData[i4] = input.readByte() & 0xFF;
            }
            bytesLeft -= 256;
        }
        if (bytesLeft > 0) {
            Via.getPlatform().getLogger().log(Level.WARNING, bytesLeft + " Bytes left after reading chunks! (" + fullChunk + ")");
        }
        return new Chunk1_8(chunkX, chunkZ, fullChunk, bitmask, sections, biomeData, new ArrayList<CompoundTag>());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void write(ByteBuf output, ClientChunks param, Chunk input) throws Exception {
        if (!(input instanceof Chunk1_8)) {
            throw new Exception("Incompatible chunk, " + input.getClass());
        }
        Chunk1_8 chunk = (Chunk1_8)input;
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        if (chunk.isUnloadPacket()) {
            return;
        }
        output.writeByte(chunk.isFullChunk() ? 1 : 0);
        Type.VAR_INT.writePrimitive(output, chunk.getBitmask());
        ByteBuf buf = output.alloc().buffer();
        try {
            for (int i = 0; i < 16; ++i) {
                ChunkSection section = chunk.getSections()[i];
                if (section == null) continue;
                Types1_9.CHUNK_SECTION.write(buf, section);
                section.getLight().writeBlockLight(buf);
                if (!section.getLight().hasSkyLight()) continue;
                section.getLight().writeSkyLight(buf);
            }
            buf.readerIndex(0);
            Type.VAR_INT.writePrimitive(output, buf.readableBytes() + (chunk.hasBiomeData() ? 256 : 0));
            output.writeBytes(buf);
        }
        finally {
            buf.release();
        }
        if (chunk.hasBiomeData()) {
            for (int biome : chunk.getBiomeData()) {
                output.writeByte((int)((byte)biome));
            }
        }
    }
}

