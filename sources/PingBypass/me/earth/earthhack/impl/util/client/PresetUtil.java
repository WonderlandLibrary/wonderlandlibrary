package me.earth.earthhack.impl.util.client;

import me.earth.earthhack.api.config.Preset;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.data.ModuleData;
import me.earth.earthhack.api.util.TextUtil;

public class PresetUtil
{

    public static Preset getPresetByName(Module module, String name)
    {
        ModuleData data = module.getData();
        if (data != null)
        {
            for (Preset preset : data.getPresets())
            {
                if (preset.getName().equalsIgnoreCase(name))
                {
                    return preset;
                }
            }
        }

        return null;
    }

    public static Preset getPresetStartingWith(Module module, String name)
    {
        ModuleData data = module.getData();
        if (data != null)
        {
            for (Preset preset : data.getPresets())
            {
                if (TextUtil.startsWithIgnoreCase(preset.getName(), name))
                {
                    return preset;
                }
            }
        }

        return null;
    }

}
