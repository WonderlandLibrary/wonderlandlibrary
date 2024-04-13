package dev.tenacity.module.impl.player;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.util.misc.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

import static net.minecraft.command.CommandBase.getPlayer;

public final class NoFallModule extends Module {

    private final TimerUtil timerUtil = new TimerUtil();
    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Verus");

    public NoFallModule() {
        super("NoFall", "Prevents fall damage", ModuleCategory.PLAYER);
        initializeSettings(mode);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
            switch (mode.getCurrentMode()) {
                case "Vanilla": {
                        if (mc.thePlayer.fallDistance >=3.1) {
                            mc.thePlayer.onGround = false;
                            mc.thePlayer.fall(3f, 0);
                        } else {
                            mc.thePlayer.onGround = true;
                            mc.thePlayer.fallDistance = 3;
                        }
                }
                case "Verus": {
                    if(mc.thePlayer.fallDistance > 3.5) {
                        mc.thePlayer.motionY = 0;
                        event.setOnGround(true);
                        mc.thePlayer.fallDistance = 0;
                    }
                }
            }
    };
}
