package de.tired.base.guis.newclickgui.setting;

import de.tired.base.guis.newclickgui.setting.impl.DeviderSetting;
import de.tired.base.module.Module;

import java.util.function.Supplier;

public class NumberSetting extends Setting {

    private double value;
    private double min;
    private double max;
    private double inc;
    public boolean dragged = false;

    public NumberSetting(String name, Module parent, double defaultValue, double min, double max, double inc, Supplier<Boolean> dependency, DeviderSetting deviderSetting) {
        super(name, parent, dependency, deviderSetting);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.inc = inc;
    }

    public NumberSetting(String name, Module parent, double defaultValue, double min, double max, double inc, Supplier<Boolean> dependency) {
        this(name, parent, defaultValue, min, max, inc, dependency, null);
    }

    public NumberSetting(String name, Module parent, double defaultValue, double min, double max, double inc) {
        this(name, parent, defaultValue, min, max, inc, () -> true);
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    public float getValueFloat() {
        return (float) this.value;
    }

    public long getValueLong() {
        return (long)this.value;
    }

    public int getValueInt() {
        return (int)this.value;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public void setValue(double value) {

        this.value = value;
    }

    public double getInc() {
        return inc;
    }
}
