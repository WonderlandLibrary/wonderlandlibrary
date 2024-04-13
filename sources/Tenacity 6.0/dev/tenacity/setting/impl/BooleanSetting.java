package dev.tenacity.setting.impl;

import dev.tenacity.setting.Setting;

public final class BooleanSetting extends Setting<Boolean> {

    private boolean state;

    public BooleanSetting(final String name, final boolean defaultState) {
        this.name = name;
        this.state = defaultState;
    }

    public boolean isEnabled() {
        return state;
    }

    public void toggle() {
        state = !state;
    }

    public void setState(final boolean state) {
        this.state = state;
    }

    @Override
    public Boolean getConfigValue() {
        return state;
    }
}
