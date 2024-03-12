package dev.tenacity.module.impl.movement;

import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastLadder extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Motion", "Motion", "Timer", "Position");
    private final NumberSetting speed = new NumberSetting("Speed", 1.5, 5, 0.1, 0.01);

    public FastLadder() {
        super("FastLadder", Category.MOVEMENT, "Climbs up ladders faster than normal");
        this.addSettings(mode, speed);
    }

    @Override
    public void onMotionEvent(MotionEvent event) {
        this.setSuffix(mode.getMode());
        if (mc.thePlayer.isOnLadder()) {
            switch (mode.getMode()) {
                case "Timer":
                    mc.timer.timerSpeed = speed.getValue().floatValue();
                    break;
                case "Motion":
                    mc.thePlayer.motionY = speed.getValue();
                    break;
                case "Position":
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
                            mc.thePlayer.posX, mc.thePlayer.posY + speed.getValue(), mc.thePlayer.posZ,
                            mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + speed.getValue(), mc.thePlayer.posZ);
                    break;
            }
        } else {
            mc.timer.timerSpeed = 1;
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }

}
