// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft;

import com.google.common.base.Preconditions;

public class BlockChangeRecord1_16_2 implements BlockChangeRecord
{
    private final byte sectionX;
    private final byte sectionY;
    private final byte sectionZ;
    private int blockId;
    
    public BlockChangeRecord1_16_2(final byte sectionX, final byte sectionY, final byte sectionZ, final int blockId) {
        this.sectionX = sectionX;
        this.sectionY = sectionY;
        this.sectionZ = sectionZ;
        this.blockId = blockId;
    }
    
    public BlockChangeRecord1_16_2(final int sectionX, final int sectionY, final int sectionZ, final int blockId) {
        this((byte)sectionX, (byte)sectionY, (byte)sectionZ, blockId);
    }
    
    @Override
    public byte getSectionX() {
        return this.sectionX;
    }
    
    @Override
    public byte getSectionY() {
        return this.sectionY;
    }
    
    @Override
    public byte getSectionZ() {
        return this.sectionZ;
    }
    
    @Override
    public short getY(final int chunkSectionY) {
        Preconditions.checkArgument(chunkSectionY >= 0, (Object)("Invalid chunkSectionY: " + chunkSectionY));
        return (short)((chunkSectionY << 4) + this.sectionY);
    }
    
    @Override
    public int getBlockId() {
        return this.blockId;
    }
    
    @Override
    public void setBlockId(final int blockId) {
        this.blockId = blockId;
    }
}
