/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.settings.impl;

import java.util.function.Supplier;
import org.celestial.client.settings.Setting;

public class ColorSetting
extends Setting {
    public int color;

    public ColorSetting(String name, int color, Supplier<Boolean> visible) {
        this.name = name;
        this.color = color;
        this.setVisible(visible);
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

