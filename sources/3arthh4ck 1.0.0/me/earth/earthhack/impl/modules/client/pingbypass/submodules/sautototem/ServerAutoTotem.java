package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautototem;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.module.SubModule;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import net.minecraft.init.Items;

@SuppressWarnings("unused")
public class ServerAutoTotem extends SubModule
{
    private static final ServerAutoTotem INSTANCE = new ServerAutoTotem();

    final Setting<Float> health    = register(new NumberSetting<>("Health", 14.5f, 0.0f, 36.0f));
    final Setting<Float> sHealth   = register(new NumberSetting<>("SafeHealth", 3.5f, 0.0f, 36.0f));
    final Setting<Boolean> xCarry  = register(new BooleanSetting("XCarry", false));

    int count = 0;

    private ServerAutoTotem()
    {
        super("S-AutoTotem", Category.Client, PingBypass.getInstance());
        this.listeners.add(new TickListener(this));
        this.listeners.add(new SetSlotListener(this));
    }

    public static ServerAutoTotem getInstance()
    {
        return INSTANCE;
    }

    @Override
    public String getDisplayInfo()
    {
        return Integer.toString(count);
    }

    protected void onTick()
    {
        if (mc.player != null)
        {
            count = InventoryUtil.getCount(Items.TOTEM_OF_UNDYING);
        }
    }

}
