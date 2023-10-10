// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

public class Kerning
{
    private final int x;
    private final int y;
    
    public Kerning(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getHorizontalKerning() {
        return this.x;
    }
    
    public int getVerticalKerning() {
        return this.y;
    }
    
    @Override
    public String toString() {
        return "Kerning(" + this.x + ", " + this.y + ")";
    }
}
