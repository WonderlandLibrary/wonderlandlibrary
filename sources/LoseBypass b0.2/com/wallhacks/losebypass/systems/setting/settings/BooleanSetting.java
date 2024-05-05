/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.systems.setting.settings.Toggleable;
import java.util.function.Predicate;

public class BooleanSetting
extends Setting<Boolean>
implements Toggleable {
    public BooleanSetting(String name, SettingsHolder settingsHolder, boolean value) {
        super(value, name, settingsHolder);
    }

    public BooleanSetting visibility(Predicate<Boolean> visible) {
        this.visible = visible;
        return this;
    }

    public BooleanSetting description(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public void toggle() {
        this.setValue((Boolean)this.getValue() == false);
    }

    @Override
    public boolean isOn() {
        return (Boolean)this.getValue();
    }

    @Override
    public boolean setValueString(String value) {
        this.setValue(Boolean.parseBoolean(value));
        return true;
    }
}

