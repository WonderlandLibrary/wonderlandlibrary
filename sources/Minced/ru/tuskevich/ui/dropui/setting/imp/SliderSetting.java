// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.dropui.setting.imp;

import java.util.function.Supplier;
import ru.tuskevich.ui.dropui.setting.Setting;

public class SliderSetting extends Setting
{
    public float current;
    public float min;
    public float max;
    public float increment;
    
    public SliderSetting(final String name, final float value, final float min, final float max, final float increment) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.current = value;
        this.increment = increment;
        this.setVisible(() -> true);
    }
    
    public SliderSetting(final String name, final float value, final float min, final float max, final float increment, final Supplier<Boolean> visible) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.current = value;
        this.increment = increment;
        this.setVisible(visible);
    }
    
    public double getMinValue() {
        return this.min;
    }
    
    public void setMinValue(final float minimum) {
        this.min = minimum;
    }
    
    public double getMaxValue() {
        return this.max;
    }
    
    public void setMaxValue(final float maximum) {
        this.max = maximum;
    }
    
    public float getFloatValue() {
        return this.current;
    }
    
    public double getDoubleValue() {
        return this.current;
    }
    
    public int getIntValue() {
        return (int)this.current;
    }
    
    public void setValueNumber(final float current) {
        this.current = current;
    }
    
    public double getIncrement() {
        return this.increment;
    }
}
