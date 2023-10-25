/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.settings;

import java.util.ArrayList;
import java.util.Arrays;
import org.celestial.client.settings.Setting;

public class Configurable {
    private final ArrayList<Setting> settingList = new ArrayList();

    public final ArrayList<Setting> getOptions() {
        return this.settingList;
    }

    public final void addSettings(Setting ... options) {
        this.settingList.addAll(Arrays.asList(options));
    }

    public Setting getSettingByName(String name) {
        for (Setting set : this.getOptions()) {
            if (set == null || !set.getName().equalsIgnoreCase(name)) continue;
            return set;
        }
        return null;
    }
}

