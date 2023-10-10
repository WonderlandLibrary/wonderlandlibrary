// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.BitSet;

public class BaseChunk implements Chunk
{
    protected final int x;
    protected final int z;
    protected final boolean fullChunk;
    protected boolean ignoreOldLightData;
    protected BitSet chunkSectionBitSet;
    protected int bitmask;
    protected ChunkSection[] sections;
    protected int[] biomeData;
    protected CompoundTag heightMap;
    protected final List<CompoundTag> blockEntities;
    
    public BaseChunk(final int x, final int z, final boolean fullChunk, final boolean ignoreOldLightData, final BitSet chunkSectionBitSet, final ChunkSection[] sections, final int[] biomeData, final CompoundTag heightMap, final List<CompoundTag> blockEntities) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.ignoreOldLightData = ignoreOldLightData;
        this.chunkSectionBitSet = chunkSectionBitSet;
        this.sections = sections;
        this.biomeData = biomeData;
        this.heightMap = heightMap;
        this.blockEntities = blockEntities;
    }
    
    public BaseChunk(final int x, final int z, final boolean fullChunk, final boolean ignoreOldLightData, final int bitmask, final ChunkSection[] sections, final int[] biomeData, final CompoundTag heightMap, final List<CompoundTag> blockEntities) {
        this(x, z, fullChunk, ignoreOldLightData, null, sections, biomeData, heightMap, blockEntities);
        this.bitmask = bitmask;
    }
    
    public BaseChunk(final int x, final int z, final boolean fullChunk, final boolean ignoreOldLightData, final int bitmask, final ChunkSection[] sections, final int[] biomeData, final List<CompoundTag> blockEntities) {
        this(x, z, fullChunk, ignoreOldLightData, bitmask, sections, biomeData, null, blockEntities);
    }
    
    @Override
    public boolean isBiomeData() {
        return this.biomeData != null;
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
        return this.fullChunk;
    }
    
    @Override
    public boolean isIgnoreOldLightData() {
        return this.ignoreOldLightData;
    }
    
    @Override
    public void setIgnoreOldLightData(final boolean ignoreOldLightData) {
        this.ignoreOldLightData = ignoreOldLightData;
    }
    
    @Override
    public int getBitmask() {
        return this.bitmask;
    }
    
    @Override
    public void setBitmask(final int bitmask) {
        this.bitmask = bitmask;
    }
    
    @Override
    public BitSet getChunkMask() {
        return this.chunkSectionBitSet;
    }
    
    @Override
    public void setChunkMask(final BitSet chunkSectionMask) {
        this.chunkSectionBitSet = chunkSectionMask;
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
        return this.biomeData;
    }
    
    @Override
    public void setBiomeData(final int[] biomeData) {
        this.biomeData = biomeData;
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
        return this.blockEntities;
    }
    
    @Override
    public List<BlockEntity> blockEntities() {
        throw new UnsupportedOperationException();
    }
}
