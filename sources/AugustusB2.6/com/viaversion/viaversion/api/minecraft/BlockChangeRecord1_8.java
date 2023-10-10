// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft;

public class BlockChangeRecord1_8 implements BlockChangeRecord
{
    private final byte sectionX;
    private final short y;
    private final byte sectionZ;
    private int blockId;
    
    public BlockChangeRecord1_8(final byte sectionX, final short y, final byte sectionZ, final int blockId) {
        this.sectionX = sectionX;
        this.y = y;
        this.sectionZ = sectionZ;
        this.blockId = blockId;
    }
    
    public BlockChangeRecord1_8(final int sectionX, final int y, final int sectionZ, final int blockId) {
        this((byte)sectionX, (short)y, (byte)sectionZ, blockId);
    }
    
    @Override
    public byte getSectionX() {
        return this.sectionX;
    }
    
    @Override
    public byte getSectionY() {
        return (byte)(this.y & 0xF);
    }
    
    @Override
    public short getY(final int chunkSectionY) {
        return this.y;
    }
    
    @Override
    public byte getSectionZ() {
        return this.sectionZ;
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
