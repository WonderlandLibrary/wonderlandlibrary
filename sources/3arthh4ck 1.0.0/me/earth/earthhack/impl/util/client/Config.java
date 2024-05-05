package me.earth.earthhack.impl.util.client;

import me.earth.earthhack.api.config.Preset;
import me.earth.earthhack.api.setting.SettingContainer;

import java.util.HashMap;
import java.util.Map;

public class Config
{
    private final Map<SettingContainer, Preset> presets = new HashMap<>();
    private final String name;

    public Config(String name)
    {
        this.name = name;
    }

    public void addPreset(SettingContainer container, Preset preset)
    {
        presets.put(container, preset);
    }

    public void apply()
    {
        presets.forEach((s, p) -> p.apply());
    }

    public String getName()
    {
        return name;
    }

}
