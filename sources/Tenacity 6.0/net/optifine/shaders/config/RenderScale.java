// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.config;

public class RenderScale
{
    private float scale;
    private float offsetX;
    private float offsetY;
    
    public RenderScale(final float scale, final float offsetX, final float offsetY) {
        this.scale = 1.0f;
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public float getOffsetX() {
        return this.offsetX;
    }
    
    public float getOffsetY() {
        return this.offsetY;
    }
    
    @Override
    public String toString() {
        return "" + this.scale + ", " + this.offsetX + ", " + this.offsetY;
    }
}
