// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.utils;

public class ModuleProperty
{
    private String propertyKey;
    private String defaultValue;
    
    public ModuleProperty(final String propertyKey, final String defaultValue) {
        this.propertyKey = propertyKey;
        this.defaultValue = defaultValue;
    }
    
    public String getValue() {
        return "";
    }
    
    public String getPropertyKey() {
        return this.propertyKey;
    }
    
    public String getDefaultValue() {
        return this.defaultValue;
    }
}
