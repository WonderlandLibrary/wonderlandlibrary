package ru.smertnix.celestial.ui.settings.impl;


import java.awt.Color;
import java.util.function.Supplier;

import ru.smertnix.celestial.ui.settings.Setting;

public class ColorSetting extends Setting {

    private int color;

    public ColorSetting(String name, int color, Supplier<Boolean> visible) {
        this.name = name;
        this.color = color;
        setVisible(visible);
    }

    public int getColorValue() {
        return color;
    }
    
    public Color getColorValue2() {
        return new Color(color);
    }


    public void setColorValue(int color) {
        this.color = color;
    }
}
