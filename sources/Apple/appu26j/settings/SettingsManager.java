package appu26j.settings;

import java.util.stream.Collector;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import appu26j.mods.Mod;
import java.util.ArrayList;

public class SettingsManager
{
    private ArrayList<Setting> settings;
    
    public SettingsManager initialize()
    {
        this.settings = new ArrayList<>();
        return this;
    }
    
    public void addSetting(Setting setting)
    {
        this.settings.add(setting);
    }
    
    public Setting getSetting(String name, Mod parentMod)
    {
        return this.settings.stream().filter(setting -> setting.getName().equalsIgnoreCase(name) && setting.getParentMod().equals(parentMod)).findFirst().orElse(null);
    }
    
    public ArrayList<Setting> getSettings(Mod parentMod)
    {
        return this.settings.stream().filter(setting -> setting.getParentMod().equals(parentMod)).collect(Collectors.toCollection(ArrayList::new));
    }
}

