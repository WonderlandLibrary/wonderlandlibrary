package me.earth.earthhack.impl.util.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.earth.earthhack.api.config.Preset;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.SettingContainer;
import me.earth.earthhack.impl.Earthhack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigPreset extends Preset
{
    private final Map<Setting<?>, JsonElement> settings = new HashMap<>();

    public ConfigPreset(String name, SettingContainer container, Map<Setting<?>, JsonElement> map)
    {
        super(name, container);
        settings.putAll(map);
    }

    @Override
    public void apply()
    {
        for (Map.Entry<Setting<?>, JsonElement> entry : settings.entrySet())
        {
            try
            {
                entry.getKey().fromJson(entry.getValue());
            }
            catch (Exception e)
            {
                Earthhack.logger.info("Error: could not set " + entry.getKey().getName() + " to " + entry.getValue().toString());
                e.printStackTrace();
            }
        }
    }

    public Map<Setting<?>, JsonElement> getSettings()
    {
        return settings;
    }

    public static ConfigPreset fromObject(String configName, SettingContainer container, JsonObject object)
    {
        Map<Setting<?>, JsonElement> elements = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet())
        {
            Setting<?> setting = container.getSetting(entry.getKey());
            if (setting == null)
            {
                Earthhack.logger.info("Config: could not find " + entry.getKey() + " in " + container.getName());
            }
            else
            {
                elements.put(setting, entry.getValue());
            }
        }

        return new ConfigPreset(configName, container, elements);
    }

    public static ConfigPreset empty(String configName, SettingContainer container)
    {
        return new ConfigPreset(configName, container, Collections.emptyMap());
    }

    public static ConfigPreset snapShot(Module module)
    {
        JsonParser parser = new JsonParser();
        Map<Setting<?>, JsonElement> elements = new HashMap<>();
        for (Setting<?> setting : module.getSettings())
        {
            System.out.println(setting.getName());
            elements.put(setting, parser.parse(setting.getValue().toString()));
        }

        return new ConfigPreset(module.getName(), module, elements);
    }

}
