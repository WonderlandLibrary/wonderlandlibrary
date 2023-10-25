package club.async.module.setting.impl;

import club.async.module.Module;
import club.async.module.setting.Setting;
import club.async.module.setting.SettingType;

import java.util.function.Supplier;

public class NumberSetting extends Setting {

    protected double min;
    protected double max;
    protected double current;
    protected double increment;

    public NumberSetting(String name, Module parent, double min, double max, double current, double increment, Supplier<Boolean> visible) {
        super(name, parent, visible);
        this.min = min;
        this.max = max;
        this.current = current;
        this.increment = increment;
    }

    public NumberSetting(String name, Module parent, double min, double max, double current, double increment) {
        this(name, parent, min, max, current, increment, () -> true);
    }


    public final double getMin() {
        return min;
    }
    public final double getMax() {
        return max;
    }
    public final int getInt() {
        return (int)current;
    }
    public final double getDouble() {
        return current;
    }
    public final long getLong() {
        return (long) current;
    }
    public final float getFloat() {
        return (float) current;
    }
    public final void setCurrent(double current) {
        this.current = current;
    }
    public final double getIncrement() {
        return increment;
    }


}
