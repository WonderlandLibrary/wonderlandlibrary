package me.earth.earthhack.impl.modules.client.commands;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.StringSetting;

public class Commands extends Module
{
    private static final Commands INSTANCE = new Commands();

    final Setting<String> prefix = register(new StringSetting("Prefix", "+"));

    private Commands()
    {
        super("Commands", Category.Client);
    }

    public static Commands getInstance()
    {
        return INSTANCE;
    }

    public String getPrefix()
    {
        return prefix.getValue();
    }

    public void setPrefix(String prefix)
    {
        this.prefix.setValue(prefix);
    }

}
