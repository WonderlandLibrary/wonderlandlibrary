// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.settings.impl;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import java.util.Arrays;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dev.tenacity.module.settings.Setting;

public class ModeSetting extends Setting
{
    public final List<String> modes;
    private final HashMap<String, ArrayList<Setting>> childrenMap;
    private String defaultMode;
    private int modeIndex;
    @Expose
    @SerializedName("value")
    private String currentMode;
    
    public ModeSetting(final String name, final String defaultMode, final String... modes) {
        this.childrenMap = new HashMap<String, ArrayList<Setting>>();
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.modeIndex = this.modes.indexOf(defaultMode);
        if (this.currentMode == null) {
            this.currentMode = defaultMode;
        }
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public String getMode() {
        return this.currentMode;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean is(final String mode) {
        return this.currentMode.equalsIgnoreCase(mode);
    }
    
    public void cycleForwards() {
        ++this.modeIndex;
        if (this.modeIndex > this.modes.size() - 1) {
            this.modeIndex = 0;
        }
        this.currentMode = this.modes.get(this.modeIndex);
    }
    
    public void cycleBackwards() {
        --this.modeIndex;
        if (this.modeIndex < 0) {
            this.modeIndex = this.modes.size() - 1;
        }
        this.currentMode = this.modes.get(this.modeIndex);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setCurrentMode(final String currentMode) {
        this.currentMode = currentMode;
    }
    
    @Override
    public String getConfigValue() {
        return this.currentMode;
    }
}
