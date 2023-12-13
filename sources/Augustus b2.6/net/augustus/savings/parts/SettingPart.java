// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.savings.parts;

import net.augustus.modules.Module;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import net.augustus.settings.Setting;

public class SettingPart extends Setting
{
    @Expose
    @SerializedName("Boolean")
    private boolean aBoolean;
    @Expose
    @SerializedName("Red")
    private int red;
    @Expose
    @SerializedName("Green")
    private int green;
    @Expose
    @SerializedName("Blue")
    private int blue;
    @Expose
    @SerializedName("Alpha")
    private int alpha;
    @Expose
    @SerializedName("Value")
    private double value;
    @Expose
    @SerializedName("MinValue")
    private double minValue;
    @Expose
    @SerializedName("MaxValue")
    private double maxValue;
    @Expose
    @SerializedName("DecimalPlaces")
    private int decimalPlaces;
    @Expose
    @SerializedName("SelectedOption")
    private String string;
    @Expose
    @SerializedName("Options")
    private String[] options;
    
    public SettingPart(final int id, final String name, final Module parent, final boolean aBoolean, final int red, final int green, final int blue, final int alpha, final double value, final double minValue, final double maxValue, final int decimalPlaces, final String string, final String[] options) {
        super(id, name, parent);
        this.aBoolean = aBoolean;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.decimalPlaces = decimalPlaces;
        this.string = string;
        this.options = options;
    }
    
    public SettingPart(final int id, final String name, final Module parent, final String string, final String[] options) {
        super(id, name, parent);
        this.string = string;
        this.options = options;
    }
    
    public SettingPart(final int id, final String name, final Module parent, final double value, final double minValue, final double maxValue, final int decimalPlaces) {
        super(id, name, parent);
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.decimalPlaces = decimalPlaces;
    }
    
    public SettingPart(final int id, final String name, final Module parent, final int red, final int green, final int blue, final int alpha) {
        super(id, name, parent);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    
    public SettingPart(final int id, final String name, final Module parent, final boolean aBoolean) {
        super(id, name, parent);
        this.aBoolean = aBoolean;
    }
    
    public boolean isaBoolean() {
        return this.aBoolean;
    }
    
    public void setaBoolean(final boolean aBoolean) {
        this.aBoolean = aBoolean;
    }
    
    public int getRed() {
        return this.red;
    }
    
    public void setRed(final int red) {
        this.red = red;
    }
    
    public int getGreen() {
        return this.green;
    }
    
    public void setGreen(final int green) {
        this.green = green;
    }
    
    public int getBlue() {
        return this.blue;
    }
    
    public void setBlue(final int blue) {
        this.blue = blue;
    }
    
    public int getAlpha() {
        return this.alpha;
    }
    
    public void setAlpha(final int alpha) {
        this.alpha = alpha;
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
    
    public String getString() {
        return this.string;
    }
    
    public void setString(final String string) {
        this.string = string;
    }
    
    public String[] getOptions() {
        return this.options;
    }
    
    public void setOptions(final String[] options) {
        this.options = options;
    }
}
