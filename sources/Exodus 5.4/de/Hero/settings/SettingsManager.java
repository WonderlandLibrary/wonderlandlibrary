/*
 * Decompiled with CFR 0.152.
 */
package de.Hero.settings;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Module;

public class SettingsManager {
    private ArrayList<Setting> settings = new ArrayList();

    public Setting getSettingByModule(String string, Module module) {
        for (Setting setting : this.getSettings()) {
            if (!setting.getName().equalsIgnoreCase(string) || !setting.getParentMod().equals(module)) continue;
            return setting;
        }
        System.err.println("[" + Exodus.INSTANCE.name + "] Error Setting NOT found: '" + string + "'!");
        return null;
    }

    public void addSetting(Setting setting) {
        this.settings.add(setting);
    }

    public Setting getSettingByClass(String string, Class clazz) {
        for (Setting setting : this.getSettings()) {
            if (!setting.getName().equalsIgnoreCase(string) || !setting.getParentClass().equals(clazz)) continue;
            return setting;
        }
        System.err.println("[" + Exodus.INSTANCE.name + "] Error Setting NOT found: '" + string + "'!");
        return null;
    }

    public ArrayList<Setting> getSettingsByMod(Module module) {
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        for (Setting setting : this.getSettings()) {
            if (!setting.getParentMod().equals(module)) continue;
            arrayList.add(setting);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList;
    }

    public ArrayList<Setting> getSettings() {
        return this.settings;
    }

    public Setting getSettingByName(String string) {
        for (Setting setting : this.getSettings()) {
            if (!setting.getName().equalsIgnoreCase(string)) continue;
            return setting;
        }
        System.err.println("[" + Exodus.INSTANCE.name + "] Error Setting NOT found: '" + string + "'!");
        return null;
    }
}

