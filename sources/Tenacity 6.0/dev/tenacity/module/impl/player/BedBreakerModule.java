package dev.tenacity.module.impl.player;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import net.minecraft.network.Packet;
import net.minecraft.util.Timer;
import dev.tenacity.util.misc.TimerUtil;

import java.util.ArrayList;
import java.util.List;

public final class BedBreakerModule extends Module {

    private final TimerUtil timerUtil = new TimerUtil();

    private final List<Packet<?>> packetList = new ArrayList<>();

    private float lastMovementYaw;

    private final ModeSetting mode = new ModeSetting("Mode", "Through Walls", "Around Walls");
    private final NumberSetting distance = new NumberSetting("Distance", 1, 1, 7, 1);

    public BedBreakerModule() {
        super("BedBreaker", "Break beds through walls", ModuleCategory.PLAYER);
        initializeSettings(mode, distance);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
