package tech.drainwalk.utility;


import java.util.Arrays;
import java.util.List;

public class MultipleBoolSetting extends Setting {

    private final List<BooleanSetting> boolSettings;

    public MultipleBoolSetting(String name, BooleanSetting... booleanSettings) {
        this.name = name;
        boolSettings = Arrays.asList(booleanSettings);
    }
    // ������)
    public BooleanSetting getSetting(String settingName) {
        return boolSettings.stream().filter(booleanSetting -> booleanSetting.getName().equals(settingName)).findFirst().orElse(null);
    }

    public List<BooleanSetting> getBoolSettings() {
        return boolSettings;
    }

}

