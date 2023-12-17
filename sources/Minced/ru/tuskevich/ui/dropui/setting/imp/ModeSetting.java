// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.dropui.setting.imp;

import java.util.function.Supplier;
import java.util.Arrays;
import java.util.List;
import ru.tuskevich.ui.dropui.setting.Setting;

public class ModeSetting extends Setting
{
    public final List<String> modes;
    public String currentMode;
    public int index;
    
    public ModeSetting(final String name, final String currentMode, final String... options) {
        super(name, currentMode);
        this.modes = Arrays.asList(options);
        this.index = this.modes.indexOf(currentMode);
        this.currentMode = this.modes.get(this.index);
        this.setVisible(() -> true);
    }
    
    public ModeSetting(final String name, final String currentMode, final Supplier<Boolean> visible, final String... options) {
        super(name, currentMode);
        this.modes = Arrays.asList(options);
        this.index = this.modes.indexOf(currentMode);
        this.currentMode = this.modes.get(this.index);
        this.setVisible(visible);
    }
    
    public boolean is(final String mode) {
        return this.currentMode.equals(mode);
    }
    
    public String getCurrentMode() {
        return this.currentMode;
    }
    
    public void setListMode(final String selected) {
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
