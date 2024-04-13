package dev.tenacity.setting.impl;

import dev.tenacity.setting.Setting;

import java.awt.*;

public final class ColorSetting extends Setting<Object> {

    private float hue = 0,
            saturation = 1,
            brightness = 1;

    public ColorSetting(final String name, final Color defaultColor) {
        this.name = name;
        setColor(defaultColor);
    }

    public void setColor(final Color color) {
        float[] hsb = new float[3];
        hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);

        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
    }

    public Color getColor() {
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public void setHue(final float hue) {
        this.hue = hue;
    }

    public void setSaturation(final float saturation) {
        this.saturation = saturation;
    }

    public void setBrightness(final float brightness) {
        this.brightness = brightness;
    }

    public float getHue() {
        return hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    @Override
    public Object getConfigValue() {
        return getColor().getRGB();
    }
}
