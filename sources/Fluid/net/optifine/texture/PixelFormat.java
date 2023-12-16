// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.texture;

public enum PixelFormat
{
    RED("RED", 0, 6403), 
    RG("RG", 1, 33319), 
    RGB("RGB", 2, 6407), 
    BGR("BGR", 3, 32992), 
    RGBA("RGBA", 4, 6408), 
    BGRA("BGRA", 5, 32993), 
    RED_INTEGER("RED_INTEGER", 6, 36244), 
    RG_INTEGER("RG_INTEGER", 7, 33320), 
    RGB_INTEGER("RGB_INTEGER", 8, 36248), 
    BGR_INTEGER("BGR_INTEGER", 9, 36250), 
    RGBA_INTEGER("RGBA_INTEGER", 10, 36249), 
    BGRA_INTEGER("BGRA_INTEGER", 11, 36251);
    
    private int id;
    
    private PixelFormat(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
