/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.clickgui.settings;

import java.util.ArrayList;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.module.Module;

public class SettingsManager {
    private static ArrayList<Setting> settings;

    public SettingsManager() {
        settings = new ArrayList();
    }

    public void rSetting(Setting in) {
        settings.add(in);
    }

    public static ArrayList<Setting> getSettings() {
        return settings;
    }

    public ArrayList<Setting> getSettingsByMod(Module mod) {
        ArrayList<Setting> out = new ArrayList<Setting>();
        for (Setting s : SettingsManager.getSettings()) {
            if (!s.getModule().equals(mod)) continue;
            out.add(s);
        }
        if (out.isEmpty()) {
            return null;
        }
        return out;
    }

    public Setting getSettingByName(String name) {
        for (Setting set : SettingsManager.getSettings()) {
            if (!set.getName().equalsIgnoreCase(name)) continue;
            return set;
        }
        return null;
    }
}

