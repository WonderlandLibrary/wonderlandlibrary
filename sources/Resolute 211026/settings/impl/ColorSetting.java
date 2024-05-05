// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.settings.impl;

import java.util.function.Supplier;
import java.awt.Color;
import vip.Resolute.settings.Setting;

public class ColorSetting extends Setting<Color>
{
    private float hue;
    private float saturation;
    private float brightness;
    
    public ColorSetting(final String name, final Color defaultColor, final Supplier<Boolean> dependency) {
        super(name, defaultColor, dependency);
        this.name = name;
        this.setColor(defaultColor);
    }
    
    public ColorSetting(final String name, final Color defaultColor) {
        super(name, defaultColor, () -> true);
        this.name = name;
        this.setColor(defaultColor);
    }
    
    public Color getValue() {
        return Color.getHSBColor(this.hue, this.saturation, this.brightness);
    }
    
    public int getColor() {
        return this.getValue().getRGB();
    }
    
    public float getSaturation() {
        return this.saturation;
    }
    
    public float getBrightness() {
        return this.brightness;
    }
    
    public void setHue(final float hue) {
        this.hue = hue;
    }
    
    public float getHue() {
        return this.hue;
    }
    
    public void setColor(final Color color) {
        final float[] colors = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = colors[0];
        this.saturation = colors[1];
        this.brightness = colors[2];
    }
    
    public void setValue(final int hex) {
        final float[] hsb = this.getHSBFromColor(hex);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }
    
    private float[] getHSBFromColor(final int hex) {
        final int r = hex >> 16 & 0xFF;
        final int g = hex >> 8 & 0xFF;
        final int b = hex & 0xFF;
        return Color.RGBtoHSB(r, g, b, null);
    }
    
    public void setSaturation(final float saturation) {
        this.saturation = saturation;
    }
    
    public void setBrightness(final float brightness) {
        this.brightness = brightness;
    }
}
