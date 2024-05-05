package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sSafety;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.module.SubModule;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sSafety.modes.UpdateMode;

@SuppressWarnings("unused")
public class ServerSafety extends SubModule
{
    private static final ServerSafety INSTANCE = new ServerSafety();

    final Setting<Float> maxDamage      = register(new NumberSetting<>("MaxDamage", 4.0f, 0.0f, 36.0f));
    final Setting<Boolean> bedCheck     = register(new BooleanSetting("BedCheck", false));
    final Setting<Boolean> newerVersion = register(new BooleanSetting("1.13+", false));
    final Setting<Boolean> safetyPlayer = register(new BooleanSetting("SafetyPlayer", false));
    final Setting<UpdateMode> updates   = register(new EnumSetting<>("Updates", UpdateMode.Tick));
    final Setting<Integer> delay        = register(new NumberSetting<>("Delay", 25, 0, 100).withVisibility(v -> updates.getValue() == UpdateMode.Fast));

    private ServerSafety()
    {
        super("S-Safety", Category.Client, PingBypass.getInstance());
    }

    public static ServerSafety getInstance()
    {
        return INSTANCE;
    }
}
