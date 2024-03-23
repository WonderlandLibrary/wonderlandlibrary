package dev.zprestige.prestige.client.setting.impl;

import dev.zprestige.prestige.client.setting.Setting;

import java.util.function.Predicate;

public class FloatSetting extends Setting<Float> {
    public float min, max;

    public FloatSetting(String name, Float value, float min, float max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }

    @Override
    public FloatSetting invokeVisibility(Predicate<Float> visible) {
        super.invokeVisibility(visible);
        return this;
    }

    @Override
    public FloatSetting description(String description) {
        super.description(description);
        return this;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }
}