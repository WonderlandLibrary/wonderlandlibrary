// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.settings.impl;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.List;
import ru.fluger.client.settings.Setting;

public class ListSetting extends Setting
{
    public final List<String> modes;
    public String currentMode;
    public int index;
    
    public ListSetting(final String name, final String currentMode, final Supplier<Boolean> visible, final String... options) {
        this.name = name;
        this.modes = Arrays.asList(options);
        this.index = this.modes.indexOf(currentMode);
        this.currentMode = this.modes.get(this.index);
        this.setVisible(visible);
    }
    
    public ListSetting(final String name, final String currentMode, final String... options) {
        this.name = name;
        this.modes = Arrays.asList(options);
        this.index = this.modes.indexOf(currentMode);
        this.currentMode = this.modes.get(this.index);
        this.setVisible(() -> true);
    }
    
    public void changeMode() {
        if (this.index < this.modes.size() - 1) {
            ++this.index;
            this.currentMode = this.modes.get(this.index);
        }
        else if (this.index >= this.modes.size() - 1) {
            this.index = 0;
            this.currentMode = this.modes.get(0);
        }
    }
    
    public String getCurrentMode() {
        return this.currentMode;
    }
    
    public void setCurrentMode(final String selected) {
        this.currentMode = selected;
        this.index = this.modes.indexOf(selected);
    }
    
    public List<String> getModes() {
        return this.modes;
    }
    
    public String getOptions() {
        return this.modes.get(this.index);
    }
}
