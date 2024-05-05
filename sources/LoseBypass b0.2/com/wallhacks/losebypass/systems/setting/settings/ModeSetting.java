/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.systems.setting.settings.EnumSetting;
import java.util.List;
import java.util.function.Predicate;

public class ModeSetting
extends Setting<String>
implements EnumSetting {
    private final List<String> modeNames;

    public ModeSetting(String name, SettingsHolder settingsHolder, String value, List<String> modeNames) {
        super(value, name, settingsHolder);
        this.modeNames = modeNames;
    }

    public ModeSetting description(String description) {
        super.setDescription(description);
        return this;
    }

    public ModeSetting visibility(Predicate<String> visible) {
        this.visible = visible;
        return this;
    }

    public List<String> getModes() {
        return this.modeNames;
    }

    @Override
    public void increment() {
        int modeIndex = (this.getValueIndex() + 1) % this.getModes().size();
        this.setValue(this.getModes().get(modeIndex));
    }

    public boolean is(String value) {
        return ((String)this.getValue()).equals(value);
    }

    @Override
    public String getValueName() {
        return (String)this.getValue();
    }

    public boolean isValueName(String value) {
        return ((String)this.getValue()).equals(value);
    }

    @Override
    public int getValueIndex() {
        return this.modeNames.indexOf(this.getValueName());
    }

    @Override
    public boolean setValueString(String value) {
        this.setValue(value);
        return true;
    }

    public boolean setValueWithIndex(int value) {
        if (value < 0) return false;
        if (value < this.modeNames.size()) return this.setValueString(this.modeNames.get(value));
        return false;
    }
}

