package de.Hero.settings;

import de.Hero.settings.Setting;
import intent.AquaDev.aqua.modules.Module;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class SettingsManager {
    private final ArrayList<Setting> settings = new ArrayList();

    public void register(Setting setting) {
        this.settings.add((Object)setting);
    }

    public Setting getSetting(String name) {
        Optional setting = this.getSettings().stream().filter(s -> s.getName().equalsIgnoreCase(name)).findFirst();
        return (Setting)setting.orElse(null);
    }

    public ArrayList<Setting> getSettingsFromModule(Module module) {
        return (ArrayList)this.getSettings().stream().filter(s -> s.getModule().equals((Object)module)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Setting> getSettings() {
        return this.settings;
    }
}
