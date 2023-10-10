// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.graphics;

import java.util.HashMap;

public final class Texture extends GLTexture
{
    @Override
    public final String toString() {
        return super.toString();
    }
    
    static {
        new HashMap();
    }
    
    public enum TextureFilter
    {
        Nearest(9728), 
        Linear(9729), 
        MipMap(9987), 
        MipMapNearestNearest(9984), 
        MipMapLinearNearest(9985), 
        MipMapNearestLinear(9986), 
        MipMapLinearLinear(9987);
        
        final int glEnum;
        
        private TextureFilter(final int glEnum) {
            this.glEnum = glEnum;
        }
        
        public final boolean isMipMap() {
            return this.glEnum != 9728 && this.glEnum != 9729;
        }
    }
    
    public enum TextureWrap
    {
        MirroredRepeat(33648), 
        ClampToEdge(33071), 
        Repeat(10497);
        
        final int glEnum;
        
        private TextureWrap(final int glEnum) {
            this.glEnum = glEnum;
        }
    }
}
