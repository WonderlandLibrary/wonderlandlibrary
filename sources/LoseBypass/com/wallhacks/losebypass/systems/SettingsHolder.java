/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems;

import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.systems.setting.SettingGroup;
import java.util.ArrayList;

public abstract class SettingsHolder {
    protected static int lastId = 0;
    public final int modId;
    private final ArrayList<Setting<?>> settings = new ArrayList();
    private final ArrayList<SettingGroup> groups = new ArrayList();

    public SettingsHolder() {
        this.modId = lastId++;
    }

    public void addSetting(Setting<?> setting) {
        if (this.settings.contains(setting)) return;
        this.settings.add(setting);
    }

    public void addGroup(SettingGroup group) {
        this.groups.add(group);
    }

    public ArrayList<Setting<?>> getSettings() {
        return this.settings;
    }

    public void onConfigLoad() {
    }

    public void onConfigSave() {
    }

    public String getName() {
        return "";
    }
}

