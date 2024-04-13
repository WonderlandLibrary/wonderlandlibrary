package wtf.dawn.settings.impl;

import wtf.dawn.settings.Setting;
import java.awt.Color;

public class ColorSetting extends Setting {
    float hue = 0.0F;
    float saturation = 1.0F;
    float brightness = 1.0F;

    public ColorSetting(String name, Color defaultColor) {
        this.name = name;
        this.setColor(defaultColor);
    }

    public Color getColor() {
        return Color.getHSBColor(this.hue, this.saturation, this.brightness);
    }

    public void setColor(Color color) {
        float[] hsb = new float[3];
        hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    public void setColor(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public double getHue() {
        return (double)this.hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public double getSaturation() {
        return (double)this.saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public double getBrightness() {
        return (double)this.brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public Integer getConfigValue() {
        return this.getColor().getRGB();
    }
}

