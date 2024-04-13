package wtf.dawn.settings.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import wtf.dawn.settings.Setting;

public class SliderSetting extends Setting {
    private final double maxValue;
    private final double minValue;
    private final double increment;
    private final double defaultValue;
    @Expose
    @SerializedName("value")
    private Double value;

    public SliderSetting(String name, double defaultValue, double maxValue, double minValue, double increment) {
        this.name = name;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.increment = increment;
    }

    private static double clamp(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    public double getMinValue() {
        return this.minValue;
    }

    public double getDefaultValue() {
        return this.defaultValue;
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        value = clamp(value, this.minValue, this.maxValue);
        value = (double)Math.round(value * (1.0 / this.increment)) / (1.0 / this.increment);
        this.value = value;
    }

    public double getIncrement() {
        return this.increment;
    }

    public Double getConfigValue() {
        return this.getValue();
    }
}
