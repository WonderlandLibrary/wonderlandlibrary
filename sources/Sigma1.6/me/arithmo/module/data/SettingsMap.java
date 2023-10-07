/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.data;

import java.util.HashMap;
import java.util.Set;
import me.arithmo.module.data.Setting;

public class SettingsMap
extends HashMap<String, Setting> {
    public void update(HashMap<String, Setting> newMap) {
        for (String key : newMap.keySet()) {
            if (!this.containsKey(key)) continue;
            ((Setting)this.get(key)).update(newMap.get(key));
        }
    }
}

