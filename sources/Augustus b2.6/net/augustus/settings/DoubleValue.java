// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.settings;

import net.augustus.savings.parts.SettingPart;
import net.augustus.modules.Module;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class DoubleValue extends Setting
{
    @Expose
    @SerializedName("Value")
    private double value;
    private double minValue;
    private double maxValue;
    private int decimalPlaces;
    
    public DoubleValue(final int id, final String name, final Module parent, final double value, final double minValue, final double maxValue, final int decimalPlaces) {
        super(id, name, parent);
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.decimalPlaces = decimalPlaces;
        DoubleValue.sm.newSetting(this);
    }
    
    public double getValue() {
        return this.value;
    }
    
    public void setValue(final double value) {
        this.value = value;
    }
    
    public double getMinValue() {
        return this.minValue;
    }
    
    public void setMinValue(final double minValue) {
        this.minValue = minValue;
    }
    
    public double getMaxValue() {
        return this.maxValue;
    }
    
    public void setMaxValue(final double maxValue) {
        this.maxValue = maxValue;
    }
    
    public int getDecimalPlaces() {
        return this.decimalPlaces;
    }
    
    public void setDecimalPlaces(final int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }
    
    @Override
    public void readSetting(final SettingPart setting) {
        super.readSetting(setting);
        this.setValue(setting.getValue());
    }
    
    @Override
    public void readConfigSetting(final SettingPart setting) {
        super.readConfigSetting(setting);
        this.setValue(setting.getValue());
    }
}
