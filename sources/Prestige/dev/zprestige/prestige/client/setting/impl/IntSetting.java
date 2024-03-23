package dev.zprestige.prestige.client.setting.impl;

import dev.zprestige.prestige.client.setting.Setting;

import java.util.function.Predicate;

public class IntSetting extends Setting<Integer> {
    public int min, max;

    public IntSetting(final String name, final Integer value, int min, int max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }

    @Override
    public IntSetting invokeVisibility(Predicate<Integer> visible) {
        super.invokeVisibility(visible);
        return this;
    }

    @Override
    public IntSetting description(String description) {
        super.description(description);
        return this;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}