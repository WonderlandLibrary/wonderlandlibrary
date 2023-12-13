// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

public class LockedResolution
{
    public static final int SCALE_FACTOR = 2;
    private final int width;
    private final int height;
    
    public LockedResolution(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
}
