package de.tired.base.guis.newclickgui.setting.impl;

import de.tired.base.guis.newclickgui.setting.Setting;
import de.tired.base.module.Module;

import java.util.function.Supplier;

public class DeviderSetting extends Setting {

    private boolean value;

    public DeviderSetting(String name, Module parent, boolean defaultValue, Supplier<Boolean> dependency) {
        super(name, parent, dependency);
        this.value = defaultValue;
    }

    public DeviderSetting(String name, Module parent, boolean defaultValue) {
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
