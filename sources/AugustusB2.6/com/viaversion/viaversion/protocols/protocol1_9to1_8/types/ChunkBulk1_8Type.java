// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.types;

import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.types.minecraft.BaseChunkBulkType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.PartialType;

public class ChunkBulk1_8Type extends PartialType<Chunk[], ClientWorld>
{
    private static final int BLOCKS_PER_SECTION = 4096;
    private static final int BLOCKS_BYTES = 8192;
    private static final int LIGHT_BYTES = 2048;
    private static final int BIOME_BYTES = 256;
    
    public ChunkBulk1_8Type(final ClientWorld clientWorld) {
        super(clientWorld, Chunk[].class);
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkBulkType.class;
    }
    
    @Override
    public Chunk[] read(final ByteBuf input, final ClientWorld world) throws Exception {
        final boolean skyLight = input.readBoolean();
        final int count = Type.VAR_INT.readPrimitive(input);
        final Chunk[] chunks = new Chunk[count];
        final ChunkBulkSection[] chunkInfo = new ChunkBulkSection[count];
        for (int i = 0; i < chunkInfo.length; ++i) {
            chunkInfo[i] = new ChunkBulkSection(input, skyLight);
        }
        for (int i = 0; i < chunks.length; ++i) {
            final ChunkBulkSection chunkBulkSection = chunkInfo[i];
            chunkBulkSection.readData(input);
            chunks[i] = Chunk1_8Type.deserialize(chunkBulkSection.chunkX, chunkBulkSection.chunkZ, true, skyLight, chunkBulkSection.bitmask, chunkBulkSection.getData());
        }
        return chunks;
    }
    
    @Override
    public void write(final ByteBuf output, final ClientWorld world, final Chunk[] chunks) throws Exception {
        boolean skyLight = false;
    Label_0097:
        for (final Chunk c : chunks) {
            for (final ChunkSection section : c.getSections()) {
                if (section != null && section.getLight().hasSkyLight()) {
                    skyLight = true;
                    break Label_0097;
                }
            }
        }
        output.writeBoolean(skyLight);
        Type.VAR_INT.writePrimitive(output, chunks.length);
        for (final Chunk c : chunks) {
            output.writeInt(c.getX());
            output.writeInt(c.getZ());
            output.writeShort(c.getBitmask());
        }
        for (final Chunk c : chunks) {
            output.writeBytes(Chunk1_8Type.serialize(c));
        }
    }
    
    public static final class ChunkBulkSection
    {
        private final int chunkX;
        private final int chunkZ;
        private final int bitmask;
        private final byte[] data;
        
        public ChunkBulkSection(final ByteBuf input, final boolean skyLight) {
            this.chunkX = input.readInt();
            this.chunkZ = input.readInt();
            this.bitmask = input.readUnsignedShort();
            final int setSections = Integer.bitCount(this.bitmask);
            this.data = new byte[setSections * (8192 + (skyLight ? 4096 : 2048)) + 256];
        }
        
        public void readData(final ByteBuf input) {
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
