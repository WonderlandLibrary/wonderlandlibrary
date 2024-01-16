// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.settings;

import java.util.function.Supplier;

public class Setting
{
    protected String name;
    protected Supplier<Boolean> visible;
    
    public boolean isVisible() {
        return this.visible.get();
    }
    
    public void setVisible(final Supplier<Boolean> visible) {
        this.visible = visible;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
