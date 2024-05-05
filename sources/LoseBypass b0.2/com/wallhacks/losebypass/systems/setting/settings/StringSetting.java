/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.Setting;
import java.util.function.Predicate;

public class StringSetting
extends Setting<String> {
    public StringSetting(String name, SettingsHolder settingsHolder, String value) {
        super(value, name, settingsHolder);
    }

    public StringSetting description(String description) {
        super.setDescription(description);
        return this;
    }

    public StringSetting visibility(Predicate<String> visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean setValueString(String value) {
        this.setValue(value);
        return true;
    }
}

