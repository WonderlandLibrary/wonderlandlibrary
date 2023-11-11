package me.nyan.flush.module.settings;

import me.nyan.flush.module.Module;
import me.nyan.flush.utils.other.MathUtils;
import net.minecraft.util.MathHelper;

import java.util.function.BooleanSupplier;

public class NumberSetting extends Setting {
    private double value;
    private final double stepValue;
    private final double min;
    private final double max;

    public NumberSetting(String name, Module parent, double value, double min, double max, double stepValue, BooleanSupplier supplier) {
        super(name, parent, supplier);
        this.value = value;
        this.min = min;
        this.max = max;
        this.stepValue = stepValue;
        register();
    }

    public NumberSetting(String name, Module parent, double value, double min, double max, BooleanSupplier supplier) {
        this(name, parent, value, min, max, 1, supplier);
    }

    public NumberSetting(String name, Module parent, double value, double min, double max, double stepValue) {
        this(name, parent, value, min, max, stepValue, null);
    }

    public NumberSetting(String name, Module parent, double value, double min, double max) {
        this(name, parent, value, min, max, null);
    }

    public double setValue(double value) {
        return this.value = MathHelper.clamp_double(MathUtils.snapToStep(value, stepValue), min, max);
    }

    public double getValue() {
        return setValue(value);
    }

    public float getValueFloat() {
        return (float) value;
    }

    public int getValueInt() {
        return (int) value;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public String toString() {
        String s = String.valueOf(value);
        return value % 1D == 0 ? s.substring(0, s.length() - 2) : s;
    }
}