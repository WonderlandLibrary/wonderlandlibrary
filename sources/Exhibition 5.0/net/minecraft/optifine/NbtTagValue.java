// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

public class NbtTagValue
{
    private String tag;
    private String value;
    
    public NbtTagValue(final String tag, final String value) {
        this.tag = null;
        this.value = null;
        this.tag = tag;
        this.value = value;
    }
    
    public boolean matches(final String key, final String value) {
        return false;
    }
}
