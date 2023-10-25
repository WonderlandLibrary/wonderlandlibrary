package org.dreamcore.client.settings.impl;

import org.dreamcore.client.settings.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ListSetting extends Setting {

    public final List<String> modes;
    public String currentMode;
    public int index;

    public ListSetting(String name, String currentMode, Supplier<Boolean> visible, String... options) {
        this.name = name;
        this.modes = Arrays.asList(options);
        this.index = modes.indexOf(currentMode);
        this.currentMode = modes.get(index);
        setVisible(visible);
        addSettings(this);
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setListMode(String selected) {
        this.currentMode = selected;
        this.index = this.modes.indexOf(selected);
    }

    public List<String> getModes() {
        return modes;
    }

    public String getOptions() {
        return this.modes.get(this.index);
    }
}
