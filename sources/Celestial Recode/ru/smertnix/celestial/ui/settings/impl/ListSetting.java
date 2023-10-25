package ru.smertnix.celestial.ui.settings.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import ru.smertnix.celestial.ui.settings.Setting;

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
