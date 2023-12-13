// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module;

public interface Toggleable
{
    void toggle();
    
    void onEnable();
    
    void onDisable();
    
    boolean isEnabled();
}
