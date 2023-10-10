// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft;

public interface BlockChangeRecord
{
    byte getSectionX();
    
    byte getSectionY();
    
    byte getSectionZ();
    
    short getY(final int p0);
    
    @Deprecated
    default short getY() {
        return this.getY(-1);
    }
    
    int getBlockId();
    
    void setBlockId(final int p0);
}
