package com.masterof13fps.manager.settingsmanager;

import com.masterof13fps.features.modules.Module;

/**
 * Made by HeroCode
 * it's free to use
 * but you have to credit me
 *
 * @author HeroCode
 */
public class Setting {

    private final String name;
    private final Module parent;
    private final String mode;

    private String sval;
    private String[] options;

    private boolean bval;

    private double dval;
    private double min;
    private double max;
    private boolean onlyint = false;

    private SettingType settingType;


    public Setting(String name, Module parent, String modeValue, String[] options) {
        this.name = (parent == null ? "global" : parent.name()) + "_" + name;
        this.parent = parent;
        this.sval = modeValue;
        this.options = options;
        this.mode = "Combo";
        settingType = SettingType.COMBO;
        parent.s.addSetting(this);
    }

    public Setting(String name, Module parent, boolean booleanValue) {
        this.name = (parent == null ? "global" : parent.name()) + "_" + name;
        this.parent = parent;
        this.bval = booleanValue;
        this.mode = "Check";
        settingType = SettingType.BOOLEAN;
        parent.s.addSetting(this);
    }

    public Setting(String name, Module parent, double dval, double min, double max, boolean onlyInt) {
        this.name = (parent == null ? "global" : parent.name()) + "_" + name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyInt;
        this.mode = "Slider";
        settingType = SettingType.VALUE;
        parent.s.addSetting(this);
    }

    public String getName() {
        return name.split("_")[1];
    }

    public String getFullName() {
        return name;
    }

    public Module getParentMod() {
        return parent;
    }

    public String getCurrentMode() {
        return this.sval;
    }

    public void setMode(String in) {
        this.sval = in;
    }

    public String[] getOptions() {
        return this.options;
    }

    public boolean isToggled() {
        return this.bval;
    }

    public void setBool(boolean in) {
        this.bval = in;
    }

    public double getCurrentValue() {
        if (this.onlyint) {
            this.dval = (int) dval;
        }
        return this.dval;
    }

    public void setNum(double in) {
        this.dval = in;
    }

    public double min() {
        return this.min;
    }

    public double max() {
        return this.max;
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

    public SettingType getSettingType() {
        return settingType;
    }

    public void setSettingType(SettingType settingType) {
        this.settingType = settingType;
    }
}
