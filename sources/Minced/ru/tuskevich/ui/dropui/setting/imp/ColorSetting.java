// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.dropui.setting.imp;

import java.awt.Color;
import java.util.function.Supplier;
import ru.tuskevich.ui.dropui.setting.Setting;

public class ColorSetting extends Setting
{
    private int color;
    
    public ColorSetting(final String name, final int color, final Supplier<Boolean> visible) {
        super(name, color);
        this.color = color;
        this.setVisible(visible);
    }
    
    public ColorSetting(final String name, final int color) {
        super(name, color);
        this.color = color;
        this.setVisible(() -> true);
    }
    
    public int getColorValue() {
        return this.color;
    }
    
    public Color getColorValueColor() {
        return new Color(this.color);
    }
    
    public void setColorValue(final int color) {
        this.color = color;
    }
}
