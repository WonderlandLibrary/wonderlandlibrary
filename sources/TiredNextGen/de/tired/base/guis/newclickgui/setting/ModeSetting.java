package de.tired.base.guis.newclickgui.setting;


import de.tired.base.guis.newclickgui.setting.impl.DeviderSetting;
import de.tired.base.module.Module;

import java.util.function.Supplier;

public class ModeSetting extends Setting {

    private String value;
    private final String[] options;
    private int modeIndex;

    public ModeSetting(String name, Module parent, String[] options, Supplier<Boolean> dependency, DeviderSetting deviderSetting) {
        super(name, parent, dependency, deviderSetting);
        this.options = options;
        if (options.length > 0)
            this.value = options[0];
        this.modeIndex = 0;
    }

    public ModeSetting(String name, Module parent, String[] options, Supplier<Boolean> dependency) {
        this(name, parent, options, dependency, null);
    }

    public ModeSetting(String name, Module parent, String[] options) {
        this(name, parent, options, () -> true);
    }

    public String[] getOptions() {
        return this.options;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getModeIndex() {
        return modeIndex;
    }

    public void setModeIndex(int modeIndex) {
        this.modeIndex = modeIndex;
    }
}