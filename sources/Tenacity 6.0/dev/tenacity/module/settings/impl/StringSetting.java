// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.settings.impl;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.module.settings.Setting;

public class StringSetting extends Setting
{
    private String string;
    
    public StringSetting(final String name) {
        this.string = "";
        this.name = name;
    }
    
    public StringSetting(final String name, final String defaultValue) {
        this.string = "";
        this.name = name;
        this.string = defaultValue;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public String getString() {
        return this.string;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setString(final String string) {
        this.string = string;
    }
    
    @Override
    public String getConfigValue() {
        return this.string;
    }
}
