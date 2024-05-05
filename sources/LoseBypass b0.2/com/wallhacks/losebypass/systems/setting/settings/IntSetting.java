/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.systems.setting.settings.NumberSetting;
import java.util.function.Predicate;

public class IntSetting
extends Setting<Integer>
implements NumberSetting {
    private final int min;
    private final int max;

    public IntSetting(String name, SettingsHolder settingsHolder, int value, int min, int max) {
        super(value, name, settingsHolder);
        this.min = min;
        this.max = max;
    }

    public IntSetting visibility(Predicate<Integer> visible) {
        this.visible = visible;
        return this;
    }

    public IntSetting description(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public double getMin() {
        return this.min;
    }

    @Override
    public double getMax() {
        return this.max;
    }

    @Override
    public double getNumber() {
        return ((Integer)this.getValue()).intValue();
    }

    @Override
    public void setNumber(double value) {
        this.setValue((int)Math.round(value));
    }

    @Override
    public boolean setValueString(String value) {
        this.setValue(Integer.parseInt(value));
        return true;
    }
}

