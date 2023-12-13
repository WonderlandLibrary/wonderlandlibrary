package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.module.SubModule;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal.modes.Rotate;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal.modes.Target;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class ServerAutoCrystal extends SubModule
{
    /** The Singleton instance of this module */
    private static final ServerAutoCrystal INSTANCE = new ServerAutoCrystal();

    final Setting<Boolean> place = register(new BooleanSetting("Place", true));
    final Setting<Target> target = register(new EnumSetting<>("Target", Target.Closest));
    final Setting<Float> placeRange = register(new NumberSetting<>("PlaceRange", 6.0f, 0.0f, 6.0f));
    final Setting<Float> placeTrace = register(new NumberSetting<>("PlaceTrace", 6.0f, 0.0f, 6.0f));
    final Setting<Float> minDamage = register(new NumberSetting<>("MinDamage", 6.0f, 0.0f, 20.0f));
    final Setting<Integer> placeDelay = register(new NumberSetting<>("PlaceDelay", 0, 0, 500));
    final Setting<Float> maxSelfP = register(new NumberSetting<>("MaxSelfPlace", 9.0f, 0.0f, 20.0f));
    final Setting<Float> facePlace = register(new NumberSetting<>("FacePlace", 10.0f, 0.0f, 36.0f));
    final Setting<Integer> multiPlace = register(new NumberSetting<>("MultiPlace", 1, 1, 5));
    final Setting<Boolean> countMin = register(new BooleanSetting("CountMin", true));
    final Setting<Boolean> antiSurr = register(new BooleanSetting("AntiSurround", true));
    final Setting<Boolean> newerVer = register(new BooleanSetting("1.13+", false));

    //Break settings
    final Setting<Boolean> explode = register(new BooleanSetting("Break", true));
    final Setting<Float> breakRange = register(new NumberSetting<>("BreakRange", 6.0f, 0.0f, 6.0f));
    final Setting<Float> breakTrace = register(new NumberSetting<>("BreakTrace", 4.5f, 0.0f, 6.0f));
    final Setting<Integer> breakDelay = register(new NumberSetting<>("BreakDelay", 0, 0, 500));
    final Setting<Float> maxSelfB = register(new NumberSetting<>("MaxSelfBreak", 10.0f, 0.0f, 20.0f));
    final Setting<Boolean> instant = register(new BooleanSetting("Instant", false));

    //Important other settings
    final Setting<Rotate> rotate = register(new EnumSetting<>("Rotate", Rotate.None));
    final Setting<Boolean> multiThread = register(new BooleanSetting("MultiThread", false));
    final Setting<Boolean> suicide = register(new BooleanSetting("Suicide", false));

    //Misc settings
    final Setting<Float> range = register(new NumberSetting<>("Range", 12.0f, 6.0f, 12.0f));
    final Setting<Boolean> override = register(new BooleanSetting("Override", false));
    final Setting<Float> minFP = register(new NumberSetting<>("MinFace", 2.0f, 0.1f, 4.0f));
    final Setting<Boolean> noFriendP = register(new BooleanSetting("AntiFriendPop", true));

    //DEV
    final Setting<Integer> cooldown = register(new NumberSetting<>("Cooldown", 500, 0, 500));
    final Setting<Boolean> multiTask     = register(new BooleanSetting("MultiTask", true));
    final Setting<Float> pbTrace = register(new NumberSetting<>("CombinedTrace", 4.5f, 0.0f, 6.0f));
    final Setting<Boolean> fallBack = register(new BooleanSetting("FallBack", true));
    final Setting<Float> fallbackDmg = register(new NumberSetting<>("FB-Dmg", 2.0f, 0.0f, 6.0f));
    final Setting<Boolean> soundR = register(new BooleanSetting("SoundRemove", false));
    final Setting<Boolean> tick = register(new BooleanSetting("Tick", true));

    final Setting<Integer> threadDelay = register(new NumberSetting<>("ThreadDelay", 30, 0, 100));

    BlockPos renderPos;
    final StopWatch timer = new StopWatch();

    private ServerAutoCrystal()
    {
        super("S-AutoCrystal", Category.Client, PingBypass.getInstance());
        this.listeners.add(new Rotator(this));
        this.listeners.add(new RenderListener(this));
        this.listeners.add(new SPacketRenderPosListener(this));
        this.listeners.add(new TickListener(this));
    }

    public static ServerAutoCrystal getInstance()
    {
        return INSTANCE;
    }

    protected void onTick()
    {
        if (timer.passed(1000) || mc.player == null || !InventoryUtil.isHolding(Items.END_CRYSTAL))
        {
            renderPos = null;
            timer.reset();
        }

        if (mc.player != null && InventoryUtil.isHolding(Items.END_CRYSTAL) && !InventoryUtil.isHoldingServer(Items.END_CRYSTAL))
        {
            InventoryUtil.syncItem();
        }
    }

}
