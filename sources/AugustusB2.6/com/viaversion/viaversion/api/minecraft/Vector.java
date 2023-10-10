// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft;

public class Vector
{
    private int blockX;
    private int blockY;
    private int blockZ;
    
    public Vector(final int blockX, final int blockY, final int blockZ) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }
    
    public int blockX() {
        return this.blockX;
    }
    
    public int blockY() {
        return this.blockY;
    }
    
    public int blockZ() {
        return this.blockZ;
    }
    
    @Deprecated
    public int getBlockX() {
        return this.blockX;
    }
    
    @Deprecated
    public int getBlockY() {
        return this.blockY;
    }
    
    @Deprecated
    public int getBlockZ() {
        return this.blockZ;
    }
    
    @Deprecated
    public void setBlockX(final int blockX) {
        this.blockX = blockX;
    }
    
    @Deprecated
    public void setBlockY(final int blockY) {
        this.blockY = blockY;
    }
    
    @Deprecated
    public void setBlockZ(final int blockZ) {
        this.blockZ = blockZ;
    }
}
