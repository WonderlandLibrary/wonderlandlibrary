// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

public class Plane
{
    private final double x;
    private final double y;
    private final boolean visible;
    
    public Plane(final double x, final double y, final boolean visible) {
        this.x = x;
        this.y = y;
        this.visible = visible;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
}
