// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.keybinding;

public interface Bindable
{
    void setKeybind(final Keybind p0);
    
    void onBindPress();
    
    void onBindRelease();
}
