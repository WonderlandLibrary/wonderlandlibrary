/*
 * Decompiled with CFR 0.152.
 */
package de.Hero.settings;

import java.awt.Color;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Module;

public class Setting {
    private boolean bval;
    private float brightness;
    private double max;
    private float hue;
    private Module parent;
    private boolean onlyint = false;
    private ArrayList<Boolean> optionsB;
    private String name;
    private String mode;
    private ArrayList<String> options;
    private float saturation;
    private String sval;
    private double min;
    private double dval;

    public void setValString(String string) {
        this.sval = string;
        if (Exodus.INSTANCE.configManager != null) {
            Exodus.INSTANCE.configManager.save();
        }
    }

    public boolean onlyInt() {
        return this.onlyint;
    }

    public boolean isSlider() {
        return this.mode.equalsIgnoreCase("Slider");
    }

    public void setValDouble(double d) {
        this.dval = d;
        if (Exodus.INSTANCE.configManager != null) {
            Exodus.INSTANCE.configManager.save();
        }
    }

    public void setValBoolean(boolean bl) {
        this.bval = bl;
        if (Exodus.INSTANCE.configManager != null) {
            Exodus.INSTANCE.configManager.save();
        }
    }

    public void setBrightness(float f) {
        this.brightness = f;
    }

    public Setting(String string, Module module, boolean bl) {
        this.name = string;
        this.parent = module;
        this.bval = bl;
        this.mode = "Check";
        ++Module.settings;
    }

    public String getName() {
        return this.name;
    }

    public void setColor(Color color) {
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = fArray[0];
        this.saturation = fArray[1];
        this.brightness = fArray[2];
    }

    public boolean isCombo() {
        return this.mode.equalsIgnoreCase("Combo");
    }

    public Setting(String string, Color color) {
        this.setColor(color);
    }

    public Module getParentMod() {
        return this.parent;
    }

    public double getMin() {
        return this.min;
    }

    public boolean isCheck() {
        return this.mode.equalsIgnoreCase("Check");
    }

    public float getBrightness() {
        return this.brightness;
    }

    public void setSaturation(float f) {
        this.saturation = f;
    }

    public int getColor() {
        return this.getColorValue().getRGB();
    }

    public String getValString() {
        return this.sval;
    }

    public Setting(String string, Module module, Setting setting, boolean bl) {
        this.name = string;
        this.parent = module;
        this.bval = bl;
        this.mode = "Check";
        ++Module.settings;
    }

    public float getHue() {
        return this.hue;
    }

    public ArrayList<String> getOptions() {
        return this.options;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public Setting(String string, Module module, double d, double d2, double d3, boolean bl) {
        this.name = string;
        this.parent = module;
        this.dval = d;
        this.min = d2;
        this.max = d3;
        this.onlyint = bl;
        this.mode = "Slider";
        ++Module.settings;
    }

    public Color getColorValue() {
        return Color.getHSBColor(this.hue, this.saturation, this.brightness);
    }

    public void setHue(float f) {
        this.hue = f;
    }

    public Setting(String string, Module module, Setting setting, double d, double d2, double d3, boolean bl) {
        this.name = string;
        this.parent = module;
        this.dval = d;
        this.min = d2;
        this.max = d3;
        this.onlyint = bl;
        this.mode = "Slider";
        ++Module.settings;
    }

    public double getMax() {
        return this.max;
    }

    public boolean getValBoolean() {
        return this.bval;
    }

    public Setting(String string, Module module) {
        this.name = string;
        this.parent = module;
        ++Module.settings;
    }

    public Setting(String string, Module module, String string2, ArrayList<String> arrayList) {
        this.name = string;
        this.parent = module;
        this.sval = string2;
        this.options = arrayList;
        this.mode = "Combo";
        ++Module.settings;
    }

    public double getValDouble() {
        return this.dval;
    }

    public Class getParentClass() {
        return this.parent.getClass();
    }
}

