package de.tired.base.guis.newclickgui.setting.impl;

import de.tired.base.guis.newclickgui.setting.Setting;
import de.tired.base.module.Module;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {

    private boolean value;

    public BooleanSetting(String name, Module parent, boolean defaultValue, Supplier<Boolean> dependency, DeviderSetting deviderSetting) {
        super(name, parent, dependency, deviderSetting);
        this.value = defaultValue;
    }

    public BooleanSetting(String name, Module parent, boolean defaultValue, Supplier<Boolean> dependency) {
        this(name, parent, defaultValue, dependency, null);
    }

    public BooleanSetting(String name, Module parent, boolean defaultValue) {
        this(name, parent, defaultValue, () -> true);
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }
}
