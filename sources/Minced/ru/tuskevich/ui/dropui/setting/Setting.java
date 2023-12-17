// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.dropui.setting;

import java.util.function.Supplier;

public class Setting
{
    private String name;
    private Object value;
    protected Supplier<Boolean> visible;
    
    public Setting(final String name, final Object value) {
        this.name = name;
        this.value = value;
    }
    
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
    
    public Object getValue() {
        return this.value;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
}
