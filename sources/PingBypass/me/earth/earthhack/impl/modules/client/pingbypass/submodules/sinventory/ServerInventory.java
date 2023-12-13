package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sinventory;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.NumberSetting;

public class ServerInventory extends Module
{
    private static final ServerInventory INSTANCE = new ServerInventory();

    @SuppressWarnings("unused")
    final Setting<Integer> delay = register(new NumberSetting<>("Delay", 5, 1, 60));

    private ServerInventory()
    {
        super("S-Inventory", Category.Client);
    }

    public static ServerInventory getInstance()
    {
        return INSTANCE;
    }

}
