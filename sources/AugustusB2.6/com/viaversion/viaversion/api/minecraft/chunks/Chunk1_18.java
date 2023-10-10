// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

import java.util.BitSet;
import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public class Chunk1_18 implements Chunk
{
    protected final int x;
    protected final int z;
    protected ChunkSection[] sections;
    protected CompoundTag heightMap;
    protected final List<BlockEntity> blockEntities;
    
    public Chunk1_18(final int x, final int z, final ChunkSection[] sections, final CompoundTag heightMap, final List<BlockEntity> blockEntities) {
        this.x = x;
        this.z = z;
        this.sections = sections;
        this.heightMap = heightMap;
        this.blockEntities = blockEntities;
    }
    
    @Override
    public boolean isBiomeData() {
        return false;
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    @Override
    public int getZ() {
        return this.z;
    }
    
    @Override
    public boolean isFullChunk() {
        return true;
    }
    
    @Override
    public boolean isIgnoreOldLightData() {
        return false;
    }
    
    @Override
    public void setIgnoreOldLightData(final boolean ignoreOldLightData) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int getBitmask() {
        return -1;
    }
    
    @Override
    public void setBitmask(final int bitmask) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public BitSet getChunkMask() {
        return null;
    }
    
    @Override
    public void setChunkMask(final BitSet chunkSectionMask) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ChunkSection[] getSections() {
        return this.sections;
    }
    
    @Override
    public void setSections(final ChunkSection[] sections) {
        this.sections = sections;
    }
    
    @Override
    public int[] getBiomeData() {
        return null;
    }
    
    @Override
    public void setBiomeData(final int[] biomeData) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public CompoundTag getHeightMap() {
        return this.heightMap;
    }
    
    @Override
    public void setHeightMap(final CompoundTag heightMap) {
        this.heightMap = heightMap;
    }
    
    @Override
    public List<CompoundTag> getBlockEntities() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<BlockEntity> blockEntities() {
        return this.blockEntities;
    }
}
