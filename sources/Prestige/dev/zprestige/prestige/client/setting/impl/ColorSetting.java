package dev.zprestige.prestige.client.setting.impl;

import dev.zprestige.prestige.client.setting.Setting;

import java.awt.Color;
import java.util.function.Predicate;

public class ColorSetting extends Setting<Color> {

    public ColorSetting(String name, Color value) {
        super(name, value);
    }

    @Override
    public ColorSetting invokeVisibility(Predicate<Color> visible) {
        super.invokeVisibility(visible);
        return this;
    }

    @Override
    public ColorSetting description(String description) {
        super.description(description);
        return this;
    }
}