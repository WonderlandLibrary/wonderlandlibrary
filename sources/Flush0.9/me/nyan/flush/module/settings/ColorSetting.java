package me.nyan.flush.module.settings;

import me.nyan.flush.module.Module;

import java.util.function.BooleanSupplier;

public class ColorSetting extends Setting {
    private int rgb;

    public ColorSetting(String name, Module parent, int rgb, BooleanSupplier supplier) {
        super(name, parent, supplier);
        this.rgb = rgb;
        register();
    }

    public ColorSetting(String name, Module parent, int rgb) {
        this(name, parent, rgb, null);
    }

    public void setRGB(int argb) {
        rgb = argb;
    }

    public int getRGB() {
        return rgb;
    }

    @Override
    public String toString() {
        return Integer.toHexString(rgb).toUpperCase();
    }
}
