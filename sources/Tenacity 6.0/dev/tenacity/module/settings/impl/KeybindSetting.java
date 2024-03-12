// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.settings.impl;

import dev.tenacity.module.settings.Setting;

public class KeybindSetting extends Setting
{
    private int code;
    
    public KeybindSetting(final int code) {
        this.name = "Keybind";
        this.code = code;
    }
    
    public int getCode() {
        return (this.code == -1) ? 0 : this.code;
    }
    
    public void setCode(final int code) {
        this.code = code;
    }
    
    @Override
    public Integer getConfigValue() {
        return this.getCode();
    }
}
