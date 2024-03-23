package dev.zprestige.prestige.client.setting.impl;

import dev.zprestige.prestige.client.setting.Setting;

import java.util.function.Predicate;

public class StringSetting extends Setting<String> {

    public String value;

    public StringSetting(String name, String description, String value) {
        super(name, description);
        this.value = value;
    }

    @Override
    public StringSetting invokeVisibility(Predicate<String> visible) {
        super.invokeVisibility(visible);
        return this;
    }

    @Override
    public StringSetting description(String description) {
        super.description(description);
        return this;
    }

    public String getValue() {
        return value;
    }
}