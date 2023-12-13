/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.clickgui.settings;

import java.util.ArrayList;
import me.wintware.client.module.Module;

public class Setting {
    private final String name;
    private final Module module;
    public String mode;
    private String current;
    private ArrayList<String> options;
    private boolean enabled;
    private double dval;
    private double min;
    private double max;
    private boolean onlyint = false;
    public boolean percentage;
    private boolean comboExpand;

    public Setting(String name, Module module, String current, ArrayList<String> options) {
        this.name = name;
        this.module = module;
        this.current = current;
        this.options = options;
        this.comboExpand = false;
        this.mode = "Combo";
    }

    public Setting(String name, Module module, boolean enabled) {
        this.name = name;
        this.module = module;
        this.enabled = enabled;
        this.mode = "Check";
    }

    public Setting(String name, Module module, double current, double min, double max, boolean onlyint) {
        this.name = name;
        this.module = module;
        this.dval = current;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
    }

    public Setting(String name, Module module, double current, double min, double max, boolean onlyint, boolean perc) {
        this.name = name;
        this.module = module;
        this.dval = current;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
        this.percentage = perc;
    }

    public String getName() {
        return this.name;
    }

    public Module getModule() {
        return this.module;
    }

    public String getValString() {
        return this.current;
    }

    public void setValString(String in) {
        this.current = in;
    }

    public ArrayList<String> getOptions() {
        return this.options;
    }

    public boolean getValue() {
        return this.enabled;
    }

    public void setValue(boolean in) {
        this.enabled = in;
    }

    public double getValDouble() {
        if (this.onlyint) {
            this.dval = (int)this.dval;
        }
        return this.dval;
    }

    public void setValDouble(double in) {
        this.dval = in;
    }

    public void setValFloat(float in) {
        this.dval = in;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public float getValFloat() {
        if (this.onlyint) {
            this.dval = (int)this.dval;
        }
        return (float)this.dval;
    }

    public long getValLong() {
        if (this.onlyint) {
            this.dval = (int)this.dval;
        }
        return (long)this.dval;
    }

    public int getValInt() {
        if (this.onlyint) {
            this.dval = (int)this.dval;
        }
        return (int)this.dval;
    }

    public boolean isCombo() {
        return this.mode.equalsIgnoreCase("Combo");
    }

    public boolean isCheck() {
        return this.mode.equalsIgnoreCase("Check");
    }

    public boolean isSlider() {
        return this.mode.equalsIgnoreCase("Slider");
    }

    public boolean onlyInt() {
        return this.onlyint;
    }

    public boolean isComboExpand() {
        return this.comboExpand;
    }

    public void setComboExpand(boolean comboExpand) {
        this.comboExpand = comboExpand;
    }

    public String getMode() {
        return this.current;
    }
}

