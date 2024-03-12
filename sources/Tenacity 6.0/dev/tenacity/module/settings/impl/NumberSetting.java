// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.settings.impl;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import dev.tenacity.module.settings.Setting;

public class NumberSetting extends Setting
{
    private final double maxValue;
    private final double minValue;
    private final double increment;
    private final double defaultValue;
    @Expose
    @SerializedName("value")
    private Double value;
    
    public NumberSetting(final String name, final double defaultValue, final double maxValue, final double minValue, final double increment) {
        this.name = name;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.increment = increment;
    }
    
    private static double clamp(double value, final double min, final double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public double getMaxValue() {
        return this.maxValue;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public double getMinValue() {
        return this.minValue;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public double getDefaultValue() {
        return this.defaultValue;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public Double getValue() {
        return this.value;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setValue(double value) {
        value = clamp(value, this.minValue, this.maxValue);
        value = Math.round(value * (1.0 / this.increment)) / (1.0 / this.increment);
        this.value = value;
    }
    
    public double getIncrement() {
        return this.increment;
    }
    
    @Override
    public Double getConfigValue() {
        return this.getValue();
    }
}
