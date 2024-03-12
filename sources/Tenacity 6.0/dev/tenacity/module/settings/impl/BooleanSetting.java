// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.settings.impl;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import dev.tenacity.module.settings.Setting;

public class BooleanSetting extends Setting
{
    @Expose
    @SerializedName("name")
    private boolean state;
    
    public BooleanSetting(final String name, final boolean state) {
        this.name = name;
        this.state = state;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean isEnabled() {
        return this.state;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void toggle() {
        this.setState(!this.isEnabled());
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setState(final boolean state) {
        this.state = state;
    }
    
    @Override
    public Boolean getConfigValue() {
        return this.isEnabled();
    }
}
