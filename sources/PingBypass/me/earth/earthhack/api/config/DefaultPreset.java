package me.earth.earthhack.api.config;

import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.SettingContainer;

public class DefaultPreset extends Preset
{
    public DefaultPreset(SettingContainer container)
    {
        super("Default", container);
    }

    @Override
    public void apply()
    {
        for (Setting<?> setting : this.getContainer().getSettings())
        {
            setting.reset();
        }
    }

}
