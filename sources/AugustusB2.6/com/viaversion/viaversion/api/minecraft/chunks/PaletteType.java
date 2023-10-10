// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

public enum PaletteType
{
    BLOCKS(4096, 8), 
    BIOMES(64, 3);
    
    private final int size;
    private final int highestBitsPerValue;
    
    private PaletteType(final int size, final int highestBitsPerValue) {
        this.size = size;
        this.highestBitsPerValue = highestBitsPerValue;
    }
    
    public int size() {
        return this.size;
    }
    
    public int highestBitsPerValue() {
        return this.highestBitsPerValue;
    }
}
