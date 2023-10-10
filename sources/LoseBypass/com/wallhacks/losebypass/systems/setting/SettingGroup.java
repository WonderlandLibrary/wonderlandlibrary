/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.Setting;
import java.util.function.Predicate;

public class SettingGroup
extends SettingsHolder {
    private String name;
    SettingsHolder holder;
    private Predicate<Boolean> visible = null;

    public SettingGroup(String name, SettingsHolder holder) {
        holder.addGroup(this);
        this.name = name;
        this.holder = holder;
    }

    public boolean isVisible() {
        if (this.visible != null) return this.visible.test(true);
        return true;
    }

    public SettingGroup setVisible(Predicate<Boolean> visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public void addSetting(Setting<?> setting) {
        super.addSetting(setting);
        if (this.holder.getSettings().contains(setting)) return;
        this.holder.addSetting(setting);
    }

    @Override
    public String getName() {
        return this.name;
    }
}

