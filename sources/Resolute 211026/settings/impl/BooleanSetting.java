// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.settings.impl;

import java.util.function.Supplier;
import vip.Resolute.settings.Setting;

public class BooleanSetting extends Setting<Boolean>
{
    public boolean enabled;
    
    public BooleanSetting(final String name, final boolean enabled, final Supplier<Boolean> dependancy) {
        super(name, enabled, dependancy);
        this.name = name;
        this.enabled = enabled;
    }
    
    public BooleanSetting(final String name, final boolean enabled) {
        this(name, enabled, () -> true);
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public void toggle() {
        this.enabled = !this.enabled;
    }
}
