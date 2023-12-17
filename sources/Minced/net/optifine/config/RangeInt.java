// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.config;

public class RangeInt
{
    private int min;
    private int max;
    
    public RangeInt(final int min, final int max) {
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
    }
    
    public boolean isInRange(final int val) {
        return val >= this.min && val <= this.max;
    }
    
    public int getMin() {
        return this.min;
    }
    
    public int getMax() {
        return this.max;
    }
    
    @Override
    public String toString() {
        return "min: " + this.min + ", max: " + this.max;
    }
}
