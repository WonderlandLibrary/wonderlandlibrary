package club.async.module.setting.impl;

import club.async.module.Module;
import club.async.module.setting.Setting;
import club.async.module.setting.SettingType;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {

    protected boolean toggled;

    public BooleanSetting(String name, Module parent, boolean toggled, Supplier<Boolean> visible) {
        super(name, parent, visible);
        this.toggled = toggled;
    }

    public BooleanSetting(String name, Module parent, boolean toggled) {
        this(name, parent, toggled, () -> true);
    }

    public final boolean get() {
        return toggled;
    }
    public final void setToggled(boolean toggled) {
        this.toggled = toggled;
    }
    public final void toggle() {
        toggled = !toggled;
    }

}
