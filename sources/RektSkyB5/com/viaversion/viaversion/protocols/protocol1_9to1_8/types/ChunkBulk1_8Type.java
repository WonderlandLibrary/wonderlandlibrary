/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocols.protocol1_9to1_8.types;

import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.PartialType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.BaseChunkBulkType;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.types.Chunk1_8Type;
import io.netty.buffer.ByteBuf;

public class ChunkBulk1_8Type
extends PartialType<Chunk[], ClientWorld> {
    private static final int BLOCKS_PER_SECTION = 4096;
    private static final int BLOCKS_BYTES = 8192;
    private static final int LIGHT_BYTES = 2048;
    private static final int BIOME_BYTES = 256;

    public ChunkBulk1_8Type(ClientWorld clientWorld) {
        super(clientWorld, Chunk[].class);
    }

    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkBulkType.class;
    }

    @Override
    public Chunk[] read(ByteBuf input, ClientWorld world) throws Exception {
        int i2;
        boolean skyLight = input.readBoolean();
        int count = Type.VAR_INT.readPrimitive(input);
        Chunk[] chunks = new Chunk[count];
        ChunkBulkSection[] chunkInfo = new ChunkBulkSection[count];
        for (i2 = 0; i2 < chunkInfo.length; ++i2) {
            chunkInfo[i2] = new ChunkBulkSection(input, skyLight);
        }
        for (i2 = 0; i2 < chunks.length; ++i2) {
            ChunkBulkSection chunkBulkSection = chunkInfo[i2];
            chunkBulkSection.readData(input);
            chunks[i2] = Chunk1_8Type.deserialize(chunkBulkSection.chunkX, chunkBulkSection.chunkZ, true, skyLight, chunkBulkSection.bitmask, chunkBulkSection.getData());
        }
        return chunks;
    }

    @Override
    public void write(ByteBuf output, ClientWorld world, Chunk[] chunks) throws Exception {
        boolean skyLight = false;
        block0: for (Chunk c2 : chunks) {
            for (ChunkSection section : c2.getSections()) {
                if (section == null || !section.getLight().hasSkyLight()) continue;
                skyLight = true;
                break block0;
            }
        }
        output.writeBoolean(skyLight);
        Type.VAR_INT.writePrimitive(output, chunks.length);
        for (Chunk c2 : chunks) {
            output.writeInt(c2.getX());
            output.writeInt(c2.getZ());
            output.writeShort(c2.getBitmask());
        }
        for (Chunk c2 : chunks) {
            output.writeBytes(Chunk1_8Type.serialize(c2));
        }
    }

    public static final class ChunkBulkSection {
        private final int chunkX;
        private final int chunkZ;
        private final int bitmask;
        private final byte[] data;

        public ChunkBulkSection(ByteBuf input, boolean skyLight) {
            this.chunkX = input.readInt();
            this.chunkZ = input.readInt();
            this.bitmask = input.readUnsignedShort();
            int setSections = Integer.bitCount(this.bitmask);
            this.data = new byte[setSections * (8192 + (skyLight ? 4096 : 2048)) + 256];
        }

        public void readData(ByteBuf input) {
            input.readBytes(this.data);
        }

        public int getChunkX() {
            return this.chunkX;
        }

        public int getChunkZ() {
            return this.chunkZ;
        }

        public int getBitmask() {
            return this.bitmask;
        }

        public byte[] getData() {
            return this.data;
        }
    }
}

