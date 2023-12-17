// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.dropui.setting;

import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class Configurable
{
    public final ArrayList<Setting> settingList;
    
    public Configurable() {
        this.settingList = new ArrayList<Setting>();
    }
    
    public final void add(final Setting... options) {
        this.settingList.addAll(Arrays.asList(options));
    }
    
    public List<Setting> get() {
        return this.settingList;
    }
}
