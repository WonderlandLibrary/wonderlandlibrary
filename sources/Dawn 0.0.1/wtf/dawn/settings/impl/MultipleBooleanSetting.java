package wtf.dawn.settings.impl;

import wtf.dawn.settings.Setting;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MultipleBooleanSetting extends Setting {
    private final List<BooleanSetting> boolSettings;

    public MultipleBooleanSetting(String name, BooleanSetting... booleanSettings) {
        this.name = name;
        this.boolSettings = Arrays.asList(booleanSettings);
    }

    public BooleanSetting getSetting(String settingName) {
        return (BooleanSetting)this.boolSettings.stream().filter((booleanSetting) -> {
            return booleanSetting.name.equalsIgnoreCase(settingName);
        }).findFirst().orElse((BooleanSetting) null);
    }

    public List<BooleanSetting> getBoolSettings() {
        return this.boolSettings;
    }

    public HashMap<String, Boolean> getConfigValue() {
        HashMap<String, Boolean> booleans = new HashMap();
        Iterator var2 = this.getBoolSettings().iterator();

        while(var2.hasNext()) {
            BooleanSetting booleanSetting = (BooleanSetting)var2.next();
            booleans.put(booleanSetting.name, booleanSetting.isEnabled());
        }

        return booleans;
    }
}
