// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.dropui.setting.imp;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.List;
import ru.tuskevich.ui.dropui.setting.Setting;

public class MultiBoxSetting extends Setting
{
    public List<Boolean> selectedValues;
    public String[] values;
    
    public MultiBoxSetting(final String name, final String[] values) {
        this(name, values, null);
        this.setVisible(() -> true);
    }
    
    public MultiBoxSetting(final String name, final String[] values, final Supplier<Boolean> condition) {
        super(name, condition);
        this.selectedValues = new ArrayList<Boolean>();
        this.values = values;
        for (int i = 0; i < values.length; ++i) {
            this.selectedValues.add(false);
        }
        this.setVisible(condition);
    }
    
    public boolean get(final int id) {
        return this.selectedValues.get(id);
    }
    
    public boolean isEnabled() {
        return this.isVisible();
    }
}
