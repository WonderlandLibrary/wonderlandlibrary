// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import vip.Resolute.settings.impl.ModeSetting;

public class Setting<T>
{
    public String name;
    public String parentValue;
    public ModeSetting parentMode;
    public boolean hidden;
    protected final Supplier<Boolean> dependency;
    private final List<ValueChangeListener<T>> valueChangeListeners;
    protected T value;
    
    public Setting(final String label, final T value, final Supplier<Boolean> dependency) {
        this.valueChangeListeners = new ArrayList<ValueChangeListener<T>>();
        this.name = label;
        this.value = value;
        this.dependency = dependency;
    }
    
    public boolean isAvailable() {
        return this.dependency.get();
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public ModeSetting getParentMode() {
        return this.parentMode;
    }
}
