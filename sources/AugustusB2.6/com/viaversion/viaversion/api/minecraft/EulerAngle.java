// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft;

public class EulerAngle
{
    private final float x;
    private final float y;
    private final float z;
    
    public EulerAngle(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public float x() {
        return this.x;
    }
    
    public float y() {
        return this.y;
    }
    
    public float z() {
        return this.z;
    }
    
    @Deprecated
    public float getX() {
        return this.x;
    }
    
    @Deprecated
    public float getY() {
        return this.y;
    }
    
    @Deprecated
    public float getZ() {
        return this.z;
    }
}
