// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

public class RangeInt
{
    private int min;
    private int max;
    
    public RangeInt(final int min, final int max) {
        this.min = -1;
        this.max = -1;
        this.min = min;
        this.max = max;
    }
    
    public boolean isInRange(final int val) {
        return (this.min < 0 || val >= this.min) && (this.max < 0 || val <= this.max);
    }
    
    @Override
    public String toString() {
        return "min: " + this.min + ", max: " + this.max;
    }
}
