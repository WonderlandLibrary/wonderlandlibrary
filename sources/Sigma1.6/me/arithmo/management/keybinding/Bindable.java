package me.arithmo.management.keybinding;

public interface Bindable
{
    void setKeybind(final Keybind p0);
    
    void onBindPress();
    
    void onBindRelease();
}
