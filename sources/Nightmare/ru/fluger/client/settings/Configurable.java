// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.settings;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class Configurable
{
    private final ArrayList<Setting> settingList;
    
    public Configurable() {
        this.settingList = new ArrayList<Setting>();
    }
    
    public final ArrayList<Setting> getOptions() {
        return this.settingList;
    }
    
    public final void addSettings(final Setting... options) {
        this.settingList.addAll(Arrays.asList(options));
    }
    
    public Setting getSettingByName(final String name) {
        for (final Setting set : this.getOptions()) {
            if (set != null) {
                if (!set.getName().equalsIgnoreCase(name)) {
                    continue;
                }
                return set;
            }
        }
        return null;
    }
}
