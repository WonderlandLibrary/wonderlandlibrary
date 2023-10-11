package com.initial.settings.impl;

import com.initial.settings.*;

public class BooleanSet extends Setting
{
    public boolean enabled;
    
    public BooleanSet(final String name, final boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public void toggle() {
        this.setEnabled(!this.enabled);
    }
}
