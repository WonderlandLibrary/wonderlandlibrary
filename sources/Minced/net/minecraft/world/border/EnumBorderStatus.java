// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.border;

public enum EnumBorderStatus
{
    GROWING(4259712), 
    SHRINKING(16724016), 
    STATIONARY(2138367);
    
    private final int color;
    
    private EnumBorderStatus(final int color) {
        this.color = color;
    }
    
    public int getColor() {
        return this.color;
    }
}
