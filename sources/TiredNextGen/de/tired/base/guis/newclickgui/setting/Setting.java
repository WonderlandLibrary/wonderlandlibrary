package de.tired.base.guis.newclickgui.setting;
import de.tired.base.guis.newclickgui.setting.impl.DeviderSetting;
import de.tired.base.module.Module;
import de.tired.Tired;

import java.awt.*;
import java.util.function.Supplier;

public class Setting {

    private final String name;
    private final Module parent;
    private final Supplier<Boolean> dependency;
    private final DeviderSetting deviderParent;
    private boolean extraSetting;

    public Setting(String name, Module parent, Supplier<Boolean> dependency) {
        this.name = name;
        this.parent = parent;
        this.dependency = dependency;
        this.deviderParent = null;
        Tired.INSTANCE.settingsManager.rSetting(this);
    }


    public Setting(String name, Module parent, Supplier<Boolean> dependency, boolean colorPicker, Color color, int HUE) {
        this.name = name;
        this.parent = parent;
        this.dependency = dependency;
        this.deviderParent = null;
        Tired.INSTANCE.settingsManager.rSetting(this);
    }

    public Setting(String name, Module parent, Supplier<Boolean> dependency, DeviderSetting deviderParent) {
        this.extraSetting = true;
        this.name = name;
        this.parent = parent;
        this.dependency = dependency;
        this.deviderParent = deviderParent;

        Tired.INSTANCE.settingsManager.rSetting(this);
    }

    public Object getValue() {
        return null;
    }

    public String getName() {
        return this.name;
    }

    public Module getModule() {
        return this.parent;
    }

    public DeviderSetting getDeviderParent() {
        return deviderParent;
    }

    public boolean isVisible() {
        if(dependency != null) {
            return dependency.get();
        }
        return true;
    }
}
