package de.tired.base.module.implementation.movement.speed.list;

import de.tired.base.module.implementation.movement.speed.SpeedExtension;
import de.tired.base.module.implementation.movement.speed.SpeedModeAnnotation;
import de.tired.util.hook.PlayerHook;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;

@SpeedModeAnnotation(name = "KauriLowHop")
public class KauriLowHop extends SpeedExtension {

    @Override
    public void onPre(EventPreMotion eventPreMotion) {

    }
    public KauriLowHop() {
        super();
    }

    @Override
    public void onEnable() {
        MC.timer.timerSpeed = 1F;
    }

    @Override
    public void onDisable() {
        MC.timer.timerSpeed = 1F;
    }

    @Override
    public void onPacket(PacketEvent eventPacket) {

    }


    @Override
    public void onUpdate(UpdateEvent eventUpdate) {
        if (PlayerHook.isMoving()) {
            if (MC.thePlayer.onGround && !MC.thePlayer.isInWater() && !MC.thePlayer.isInLava()) {
                MC.thePlayer.jump();
                MC.timer.timerSpeed = 1F;
                MC.thePlayer.motionY = PlayerHook.getPredictedMotionY(MC.thePlayer.motionY) * .99;
            } else {
                if (MC.thePlayer.motionY > .27) {
                    PlayerHook.stop();
                    MC.timer.timerSpeed = 1F;
                    MC.thePlayer.motionY -= PlayerHook.getPredictedMotionY(MC.thePlayer.motionY) * .87;
                }
            }
        }
    }


}