// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.settings;

import net.augustus.Augustus;
import java.util.Iterator;
import net.augustus.modules.Module;
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
    
    public ArrayList<Setting> getSettingsByMod(final Module mod) {
        final ArrayList<Setting> sets = new ArrayList<Setting>();
        for (final Setting setting : this.settings) {
            if (setting.getParent().equals(mod)) {
                sets.add(setting);
            }
        }
        return sets;
    }
    
    public ArrayList<Setting> getSettingByName(final String name) {
        for (final Setting setting : this.settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return this.settings;
            }
        }
        return null;
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
