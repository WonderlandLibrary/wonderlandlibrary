package org.dreamcore.client.settings.impl;

import org.dreamcore.client.settings.Setting;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {

    private boolean state;
    private String desc;

    public BooleanSetting(String name, String desc, boolean state, Supplier<Boolean> visible) {
        this.name = name;
        this.desc = desc;
        this.state = state;
        setVisible(visible);
    }

    public BooleanSetting(String name, boolean state, Supplier<Boolean> visible) {
        this.name = name;
        this.state = state;
        setVisible(visible);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean getBoolValue() {
        return state;
    }

    public void setBoolValue(boolean state) {
        this.state = state;
    }
}
