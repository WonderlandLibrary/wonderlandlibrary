package dev.tenacity.setting.impl;

import dev.tenacity.setting.Setting;
import dev.tenacity.util.misc.MathUtil;

public final class NumberSetting extends Setting<Double> {

    private final double defaultValue, minValue, maxValue, increment;
    private double currentValue;

    public NumberSetting(final String name, final double defaultValue, final double minValue, final double maxValue, final double increment) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.currentValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getIncrement() {
        return increment;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        currentValue = MathUtil.clamp(currentValue, minValue, maxValue);
        currentValue = Math.round(currentValue * (1 / increment)) / (1 / increment);
        this.currentValue = currentValue;
    }

    @Override
    public Double getConfigValue() {
        return currentValue;
    }
}
