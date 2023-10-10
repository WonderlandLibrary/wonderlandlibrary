// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

public interface DataPalette
{
    int index(final int p0, final int p1, final int p2);
    
    int idAt(final int p0);
    
    default int idAt(final int sectionX, final int sectionY, final int sectionZ) {
        return this.idAt(this.index(sectionX, sectionY, sectionZ));
    }
    
    void setIdAt(final int p0, final int p1);
    
    default void setIdAt(final int sectionX, final int sectionY, final int sectionZ, final int id) {
        this.setIdAt(this.index(sectionX, sectionY, sectionZ), id);
    }
    
    int idByIndex(final int p0);
    
    void setIdByIndex(final int p0, final int p1);
    
    int paletteIndexAt(final int p0);
    
    void setPaletteIndexAt(final int p0, final int p1);
    
    void addId(final int p0);
    
    void replaceId(final int p0, final int p1);
    
    int size();
    
    void clear();
}
