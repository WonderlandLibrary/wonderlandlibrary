// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.settings.impl;

import java.util.function.Supplier;
import ru.fluger.client.settings.Setting;

public class NumberSetting extends Setting
{
    private double current;
    private double minimum;
    private double maximum;
    private double increment;
    private String desc;
    
    public NumberSetting(final String name, final float current, final float minimum, final float maximum, final float increment) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.setVisible(() -> true);
    }
    
    public NumberSetting(final String name, final double current, final double minimum, final double maximum, final double increment) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.setVisible(() -> true);
    }
    
    public NumberSetting(final String name, final float current, final float minimum, final float maximum, final float increment, final Supplier<Boolean> visible) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.setVisible(visible);
    }
    
    public NumberSetting(final String name, final String desc, final float current, final float minimum, final float maximum, final float increment, final Supplier<Boolean> visible) {
        this.name = name;
        this.desc = desc;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.setVisible(visible);
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public float getMinimum() {
        return (float)this.minimum;
    }
    
    public void setMinimum(final float minimum) {
        this.minimum = minimum;
    }
    
    public float getMaximum() {
        return (float)this.maximum;
    }
    
    public void setMaximum(final float maximum) {
        this.maximum = maximum;
    }
    
    public float getCurrentValue() {
        return (float)this.current;
    }
    
    public int getCurrentValueInt() {
        return (int)this.current;
    }
    
    public void setCurrentValue(final float current) {
        this.current = current;
    }
    
    public float getIncrement() {
        return (float)this.increment;
    }
    
    public void setIncrement(final float increment) {
        this.increment = increment;
    }
}
