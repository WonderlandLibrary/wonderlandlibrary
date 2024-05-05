/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.systems.setting.settings.LoseBypassColor;
import com.wallhacks.losebypass.utils.ColorUtil;
import java.awt.Color;
import java.util.function.Predicate;

public class ColorSetting
extends Setting<LoseBypassColor> {
    final boolean allowChangeAlpha;
    final int offset = (int)(Math.random() * 11520.0);

    public ColorSetting(String name, SettingsHolder settingsHolder, Color color, boolean allowChangeAlpha) {
        super(new LoseBypassColor(color), name, settingsHolder);
        this.allowChangeAlpha = allowChangeAlpha;
    }

    public ColorSetting(String name, SettingsHolder settingsHolder, Color color) {
        this(name, settingsHolder, color, true);
    }

    public ColorSetting visibility(Predicate<LoseBypassColor> visible) {
        this.visible = visible;
        return this;
    }

    public ColorSetting description(String description) {
        super.setDescription(description);
        return this;
    }

    public boolean isAllowChangeAlpha() {
        return this.allowChangeAlpha;
    }

    public Color getColor(double alphaMultiply) {
        Color c = this.getColor();
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)((double)c.getAlpha() * alphaMultiply));
    }

    public Color getColor() {
        if (((LoseBypassColor)this.getValue()).rainbow == LoseBypassColor.Rainbow.OFF) return ((LoseBypassColor)this.getValue()).color;
        int speed = 0;
        speed = ((LoseBypassColor)this.getValue()).rainbow == LoseBypassColor.Rainbow.SLOW ? 1 : (((LoseBypassColor)this.getValue()).rainbow == LoseBypassColor.Rainbow.MEDIUM ? 2 : (((LoseBypassColor)this.getValue()).rainbow == LoseBypassColor.Rainbow.FAST ? 4 : 16));
        Color c = ColorUtil.fromHSB((float)((System.currentTimeMillis() * (long)speed / 2L + (long)this.offset) % 11520L) / 11520.0f, ColorUtil.getSaturation(((LoseBypassColor)this.getValue()).color), ColorUtil.getBrightness(((LoseBypassColor)this.getValue()).color));
        ((LoseBypassColor)this.getValue()).color = new Color(c.getRed(), c.getGreen(), c.getBlue(), ((LoseBypassColor)this.getValue()).color.getAlpha());
        return ((LoseBypassColor)this.getValue()).color;
    }

    public float getBrightness() {
        return ColorUtil.getBrightness(this.getColor());
    }

    public float getSaturation() {
        return ColorUtil.getSaturation(this.getColor());
    }

    public float getHue() {
        return ColorUtil.getHue(this.getColor());
    }

    public int getRGB() {
        return this.getColor().getRGB();
    }

    public void setColor(Color color) {
        ((LoseBypassColor)this.getValue()).color = color;
    }

    public void setRainbow(LoseBypassColor.Rainbow value) {
        ((LoseBypassColor)this.getValue()).rainbow = value;
    }

    public LoseBypassColor.Rainbow getRainbow() {
        return ((LoseBypassColor)this.getValue()).rainbow;
    }

    @Override
    public boolean setValueString(String value) {
        String[] list = value.split(",");
        this.setColor(new Color(Integer.parseInt(list[0]), Integer.parseInt(list[1]), Integer.parseInt(list[2]), Integer.parseInt(list[3])));
        this.setRainbow(LoseBypassColor.Rainbow.values()[Integer.parseInt(list[4])]);
        return true;
    }

    @Override
    protected String getStringOfValue(LoseBypassColor value) {
        return value.color.getRed() + "," + value.color.getGreen() + "," + value.color.getBlue() + "," + value.color.getAlpha() + "," + value.rainbow.ordinal();
    }
}

