package dev.tenacity.module.impl.world;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.packet.PacketReceiveEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.misc.TimerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.Timer;

public final class TimerModule extends Module {


    private final TimerUtil timerUtil = new TimerUtil();

    public final NumberSetting timer = new NumberSetting("Speed:", 1, 0.1, 10, 0.1);

    public TimerModule() {
        super("Timer", "Speeds up or slows down time", ModuleCategory.WORLD);
         initializeSettings(timer);
       // initializeSettings(mode);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if (event.isPre()) {
            Timer.timerSpeed = (float) (int) timer.getCurrentValue();
        }
    };

    @Override
    public void onEnable() {
        if (mc.thePlayer == null) {
            return;
        }
        Timer.timerSpeed = (float) timer.getCurrentValue();
        super.onEnable();
    }
    @Override
    public void onDisable() {
        if (mc.thePlayer == null) {
            return;
        }
        Timer.timerSpeed = 1f;
        super.onDisable();
    }
}

