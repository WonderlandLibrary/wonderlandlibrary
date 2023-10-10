/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.systems.setting.settings.NumberSetting;
import java.util.function.Predicate;

public class DoubleSetting
extends Setting<Double>
implements NumberSetting {
    private final double min;
    private final double max;
    private final double sliderStep;

    public DoubleSetting(String name, SettingsHolder settingsHolder, double value, double min, double max, double sliderStep) {
        super(value, name, settingsHolder);
        this.sliderStep = sliderStep;
        this.min = min;
        this.max = max;
    }

    public DoubleSetting(String name, SettingsHolder settingsHolder, double value, double min, double max) {
        this(name, settingsHolder, value, min, max, 0.1);
    }

    public DoubleSetting visibility(Predicate<Double> visible) {
        this.visible = visible;
        return this;
    }

    public DoubleSetting description(String description) {
        super.setDescription(description);
        return this;
    }

    public double getSliderStep() {
        return this.sliderStep;
    }

    @Override
    public double getMin() {
        return this.min;
    }

    @Override
    public double getMax() {
        return this.max;
    }

    @Override
    public double getNumber() {
        return (Double)this.getValue();
    }

    @Override
    public void setNumber(double value) {
        this.setValue(value);
    }

    @Override
    public boolean setValueString(String value) {
        this.setValue(Double.parseDouble(value));
        return true;
    }

    public float getFloatValue() {
        return ((Double)this.getValue()).floatValue();
    }

    @Override
    public String getValueString() {
        return String.valueOf(this.getFloatValue());
    }
}

