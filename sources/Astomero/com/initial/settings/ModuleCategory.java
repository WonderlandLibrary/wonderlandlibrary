package com.initial.settings;

import java.util.*;

public class ModuleCategory extends Setting
{
    public boolean expanded;
    public ArrayList<Setting> settingsOnCat;
    
    public ModuleCategory(final String displayName) {
        this.settingsOnCat = new ArrayList<Setting>();
        this.name = displayName;
    }
    
    public void addCatSettings(final Setting... settings) {
        this.settingsOnCat.addAll(Arrays.asList(settings));
    }
    
    public boolean isExpanded() {
        return this.expanded;
    }
    
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
}
