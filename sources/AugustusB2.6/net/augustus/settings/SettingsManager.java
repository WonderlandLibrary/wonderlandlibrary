// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.settings;

import java.util.Iterator;
import net.augustus.Augustus;
import java.util.ArrayList;

public class SettingsManager
{
    private final ArrayList<Setting> settings;
    
    public SettingsManager() {
        this.settings = new ArrayList<Setting>();
    }
    
    public void newSetting(final Setting setting) {
        this.settings.add(setting);
    }
    
    public ArrayList<Setting> getStgs() {
        return this.settings;
    }
    
    public Setting getFromID(final int id) {
        for (final Setting setting : this.getStgs()) {
            if (setting.getId() == id) {
                return setting;
            }
        }
        System.err.println("[" + Augustus.getInstance().getName() + "] ERROR Setting not found: '" + id + "'!");
        return null;
    }
}
