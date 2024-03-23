package dev.zprestige.prestige.client.setting.impl;

import dev.zprestige.prestige.client.setting.Setting;

import java.util.Arrays;
import java.util.function.Predicate;

public class MultipleSetting extends Setting<String> {
    public final String[] options;
    public final Boolean[] values;

    public MultipleSetting(String name, String[] options, Boolean[] values) {
        super(name, "");
        this.options = options;
        this.values = values;
    }

    @Override
    public MultipleSetting invokeVisibility(Predicate<String> visible) {
        super.invokeVisibility(visible);
        return this;
    }

    @Override
    public MultipleSetting description(String description) {
        super.description(description);
        return this;
    }

    public String[] getOptions() {
        return options;
    }

    public Boolean[] getValues() {
        return values;
    }

    public boolean getValue(String option) {
        return values[Arrays.asList(options).indexOf(option)];
    }

    public void invokeValue(String option, boolean value) {
        values[Arrays.asList(options).indexOf(option)] = value;
    }
}