/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting;

import com.wallhacks.losebypass.event.events.SettingChangeEvent;
import com.wallhacks.losebypass.systems.SettingsHolder;
import java.util.function.Predicate;

public class Setting<T> {
    private T value;
    private final T defaultValue;
    private final String name;
    private final SettingsHolder settingsHolder;
    private String description;
    public Predicate<T> visible = null;

    public boolean isVisible() {
        if (this.visible != null) return this.visible.test(this.getValue());
        return true;
    }

    public Setting(T value, String name, SettingsHolder settingsHolder) {
        this.value = value;
        this.defaultValue = value;
        this.name = name;
        this.settingsHolder = settingsHolder;
        settingsHolder.addSetting(this);
    }

    public T getValue() {
        return this.value;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public void setValue(T value) {
        this.value = value;
        new SettingChangeEvent(this);
    }

    public void setValueSilent(T value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public SettingsHolder getSettingsHolder() {
        return this.settingsHolder;
    }

    public boolean setValueString(String value) {
        return false;
    }

    protected String getStringOfValue(T value) {
        return value.toString();
    }

    public String getDefaultValueString() {
        return this.getStringOfValue(this.defaultValue);
    }

    public String getValueString() {
        return this.getStringOfValue(this.value);
    }

    public SettingsHolder getsettingsHolder() {
        return this.settingsHolder;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}

