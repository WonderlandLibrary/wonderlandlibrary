package dev.zprestige.prestige.client.setting.impl;

import dev.zprestige.prestige.client.setting.Setting;

import java.util.function.Predicate;

public class ModeSetting extends Setting<String> {
    public final String[] values;

    public ModeSetting(String name, String value, String[] values) {
        super(name, value);
        this.values = values;
    }

    @Override
    public ModeSetting invokeVisibility(Predicate<String> visible) {
        super.invokeVisibility(visible);
        return this;
    }

    @Override
    public ModeSetting description(String description) {
        super.description(description);
        return this;
    }

    public String[] getValues() {
        return values;
    }
}