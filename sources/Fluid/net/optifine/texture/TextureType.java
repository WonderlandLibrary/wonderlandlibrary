// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.texture;

public enum TextureType
{
    TEXTURE_1D("TEXTURE_1D", 0, 3552), 
    TEXTURE_2D("TEXTURE_2D", 1, 3553), 
    TEXTURE_3D("TEXTURE_3D", 2, 32879), 
    TEXTURE_RECTANGLE("TEXTURE_RECTANGLE", 3, 34037);
    
    private int id;
    
    private TextureType(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
