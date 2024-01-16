// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.settings.impl;

import java.util.function.Supplier;
import ru.fluger.client.settings.Setting;

public class ColorSetting extends Setting
{
    public int color;
    
    public ColorSetting(final String name, final int color, final Supplier<Boolean> visible) {
        this.name = name;
        this.color = color;
        this.setVisible(visible);
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
}
