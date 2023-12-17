// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.config;

public class ShaderMacro
{
    private String name;
    private String value;
    
    public ShaderMacro(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public String getSourceLine() {
        return "#define " + this.name + " " + this.value;
    }
    
    @Override
    public String toString() {
        return this.getSourceLine();
    }
}
