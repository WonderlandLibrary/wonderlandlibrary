package dev.tenacity.setting.impl;

import dev.tenacity.setting.Setting;
import dev.tenacity.setting.impl.BooleanSetting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MultipleBoolSetting extends Setting {

    private final List<BooleanSetting> boolSettings;

    public MultipleBoolSetting(String name, BooleanSetting... booleanSettings) {
        this.name = name;
        boolSettings = Arrays.asList(booleanSettings);
    }

    public BooleanSetting getSetting(String settingName) {
        return boolSettings.stream().filter(booleanSetting -> booleanSetting.name.equalsIgnoreCase(settingName)).findFirst().orElse(null);
    }

    public List<BooleanSetting> getBoolSettings() {
        return boolSettings;
    }

    @Override
    public HashMap<String, Boolean> getConfigValue() {
        HashMap<String, Boolean> booleans = new HashMap<>();
        for (BooleanSetting booleanSetting : getBoolSettings()) {
            booleans.put(booleanSetting.name, booleanSetting.isEnabled());
        }
        return booleans;
    }

}

