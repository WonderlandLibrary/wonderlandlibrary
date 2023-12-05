package koks.api.settings;

import koks.Koks;
import koks.manager.event.impl.EventSettingUpdate;
import koks.manager.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:25
 */
public class Setting {

    private String name, currentMode, currentTyped;
    private String[] modes;

    private float currentValue, minValue, maxValue;
    private boolean toggled, onlyInt, visible = true;

    private int key;

    private Module module;

    private Type type;

    // TYPED
    public Setting(String name, String typed, Module module) {
        this.name = name;
        this.currentTyped = typed;
        this.module = module;

        type = Type.TYPE;
        Koks.getKoks().settingsManager.registerSetting(this);
    }

    // KEY
    public Setting(String name, int key, Module module) {
        this.name = name;
        this.key = key;
        this.module = module;

        type = Type.KEY;
        Koks.getKoks().settingsManager.registerSetting(this);
    }

    // CheckBox
    public Setting(String name, boolean toggled, Module module) {
        this.name = name;
        this.toggled = toggled;
        this.module = module;

        type = Type.CHECKBOX;
        Koks.getKoks().settingsManager.registerSetting(this);
    }

    // ComboBox
    public Setting(String name, String[] modes, String currentMode, Module module) {
        Arrays.sort(modes);
        this.name = name;
        this.modes = modes;
        this.currentMode = currentMode;
        this.module = module;

        type = Type.COMBOBOX;
        Koks.getKoks().settingsManager.registerSetting(this);
    }

    // Slider
    public Setting(String name, float currentValue, float minValue, float maxValue, boolean onlyInt, Module module) {
        this.name = name;
        this.currentValue = currentValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.onlyInt = onlyInt;
        this.module = module;

        type = Type.SLIDER;
        Koks.getKoks().settingsManager.registerSetting(this);
    }

    public enum Type {
        CHECKBOX, COMBOBOX, SLIDER, KEY, TYPE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    public String[] getModes() {
        return modes;
    }

    public void setModes(String[] modes) {
        this.modes = modes;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        new EventSettingUpdate(this).onFire();
        this.currentValue = currentValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isOnlyInt() {
        return onlyInt;
    }

    public void setOnlyInt(boolean onlyInt) {
        this.onlyInt = onlyInt;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Type getType() {
        return type;
    }

    public String getTyped() {
        return currentTyped;
    }

    public void setTyped(String currentTyped) {
        this.currentTyped = currentTyped;
    }
}