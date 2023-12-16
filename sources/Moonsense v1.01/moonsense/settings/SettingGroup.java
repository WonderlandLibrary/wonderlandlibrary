// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.settings;

import com.google.common.collect.Lists;
import java.util.ArrayList;

public class SettingGroup
{
    private String name;
    private ArrayList<Setting> settings;
    
    public SettingGroup(final String name, final Setting[] settings) {
        this.name = name;
        this.settings = (ArrayList<Setting>)Lists.newArrayList();
        for (final Setting s : settings) {
            this.settings.add(s);
        }
    }
    
    public void add(final Setting s) {
        this.settings.add(s);
    }
    
    public String getName() {
        return this.name;
    }
    
    public ArrayList<Setting> getSettings() {
        return this.settings;
    }
    
    public int size() {
        return this.settings.size();
    }
}
