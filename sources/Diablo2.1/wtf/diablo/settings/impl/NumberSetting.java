package wtf.diablo.settings.impl;

import wtf.diablo.settings.Setting;

public class NumberSetting extends Setting {
    private double value;
    private double increment;
    private double min;
    private double max;

    public NumberSetting(String name, double value, double increment, double min, double max) {
        this.name = name;
        this.value = value;
        this.increment = increment;
        this.min = min;
        this.max = max;
    }

    public void setValue(double value) {
        this.value = (double) Math.round(value / increment) * increment;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    public double getValue() {
        return this.value;
    }

    public float getFloatValue() {
        return (float) this.value;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }
    public double getIncrement() { return this.increment; }
    public Object getObjectValue() {
        return value;
    }
}
