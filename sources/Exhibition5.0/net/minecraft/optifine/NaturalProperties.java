// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

public class NaturalProperties
{
    public int rotation;
    public boolean flip;
    
    public NaturalProperties(final String type) {
        this.rotation = 1;
        this.flip = false;
        if (type.equals("4")) {
            this.rotation = 4;
        }
        else if (type.equals("2")) {
            this.rotation = 2;
        }
        else if (type.equals("F")) {
            this.flip = true;
        }
        else if (type.equals("4F")) {
            this.rotation = 4;
            this.flip = true;
        }
        else if (type.equals("2F")) {
            this.rotation = 2;
            this.flip = true;
        }
        else {
            Config.warn("NaturalTextures: Unknown type: " + type);
        }
    }
    
    public boolean isValid() {
        return this.rotation == 2 || this.rotation == 4 || this.flip;
    }
}
