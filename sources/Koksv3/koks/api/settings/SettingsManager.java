package koks.api.settings;

import koks.manager.module.Module;

import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:25
 */
public class SettingsManager {

    public ArrayList<Setting> settings = new ArrayList<>();

    public void registerSetting(Setting setting) {
        settings.add(setting);
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public Setting getSetting(Module module, String name) {
        for(Setting setting : getSettings()) {
            if(setting.getModule().equals(module) && setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }

}