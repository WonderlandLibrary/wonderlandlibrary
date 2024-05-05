package me.earth.earthhack.impl.util.client;

import me.earth.earthhack.api.config.Preset;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.SettingContainer;

import java.util.HashMap;
import java.util.Map;

public class ValuePreset extends Preset
{
    private final Map<Setting<?>, Object> values = new HashMap<>();

    public ValuePreset(String name, SettingContainer settingContainer, Map<Setting<?>, Object> map)
    {
        super(name, settingContainer);
        values.putAll(map);
    }

    @Override
    public void apply()
    {
        for (Map.Entry<Setting<?>, Object> entry : values.entrySet())
        {
            setValue(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void setValue(Setting<T> setting, Object object)
    {
        setting.setValue((T) object);
    }

}
