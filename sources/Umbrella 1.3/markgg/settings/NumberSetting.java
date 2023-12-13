/*
 * Decompiled with CFR 0.150.
 */
package markgg.settings;

import markgg.settings.Setting;

public class NumberSetting
extends Setting {
    public double value;
    public double minimum;
    public double maximum;
    public double increment;

    public NumberSetting(String name, double value, double minimum, double maximum, double increment) {
        this.name = name;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        double precision = 1.0 / this.increment;
        this.value = (double)Math.round(Math.max(this.minimum, Math.min(this.maximum, value)) * precision) / precision;
    }

    public void increment(boolean positive) {
        this.setValue(this.getValue() + (double)(positive ? 1 : -1) * this.increment);
    }

    public double getMinimum() {
        return this.minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public double getMaximum() {
        return this.maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public double getIncrement() {
        return this.increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }
}

