package ru.smertnix.celestial.ui.settings.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ru.smertnix.celestial.ui.settings.Setting;

public class MultipleBoolSetting extends Setting {

    private final List<BooleanSetting> boolSettings;

    public MultipleBoolSetting(String name, BooleanSetting... booleanSettings) {
        this.name = name;
        boolSettings = Arrays.asList(booleanSettings);
    }
    public BooleanSetting getSetting(String settingName) {
        return boolSettings.stream().filter(booleanSetting -> booleanSetting.getName().equals(settingName)).findFirst().orElse(null);
    }

    public List<BooleanSetting> getBoolSettings() {
        return boolSettings;
    }

    
}
