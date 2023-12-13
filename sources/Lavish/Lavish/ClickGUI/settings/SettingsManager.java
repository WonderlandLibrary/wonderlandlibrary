// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.ClickGUI.settings;

import java.util.Iterator;
import Lavish.modules.Module;
import java.util.ArrayList;

public class SettingsManager
{
    private ArrayList<Setting> settings;
    
    public SettingsManager() {
        this.settings = new ArrayList<Setting>();
    }
    
    public void rSetting(final Setting in) {
        this.settings.add(in);
    }
    
    public ArrayList<Setting> getSettings() {
        return this.settings;
    }
    
    public ArrayList<Setting> getSettingsByMod(final Module mod) {
        final ArrayList<Setting> out = new ArrayList<Setting>();
        for (final Setting s : this.getSettings()) {
            if (s.getParentMod().equals(mod)) {
                out.add(s);
            }
        }
        if (out.isEmpty()) {
            return null;
        }
        return out;
    }
    
    public Setting getSettingByName(final String name) {
        for (final Setting set : this.getSettings()) {
            if (set.getName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        System.err.println("[Lavish] Error Setting NOT found: '" + name + "'!");
        return null;
    }
}
