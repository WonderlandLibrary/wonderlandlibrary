package de.tired.base.module.implementation.movement.speed.list;

import de.tired.util.hook.PlayerHook;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.implementation.movement.speed.SpeedExtension;
import de.tired.base.module.implementation.movement.speed.SpeedModeAnnotation;

@SpeedModeAnnotation(name = "NoCheatPlusPort")
public class NoCheatPlusPort extends SpeedExtension {

    @Override
    public void onPre(EventPreMotion eventPreMotion) {

    }

    @Override
    public void onEnable() {
        MC.timer.timerSpeed = 1F;
        count = 0;
    }

    @Override
    public void onDisable() {
        count = 0;
        MC.timer.timerSpeed = 1F;
    }

    @Override
    public void onPacket(PacketEvent eventPacket) {

    }

    @Override
    public void onUpdate(UpdateEvent eventUpdate) {
        if (!PlayerHook.isMoving()) return;
        if (MC.thePlayer.motionY < .4)
            PlayerHook.setupStrafe();
        if (MC.thePlayer.onGround) {
            MC.thePlayer.jump();
        }
        else {
            if (MC.thePlayer.motionY > .4) {
                PlayerHook.increaseSpeedWithStrafe(PlayerHook.getSpeed() * 1.02);
            } else {
                if (MC.thePlayer.motionY < .2)
                    MC.timer.timerSpeed = 1.04f;
                else
                    MC.timer.timerSpeed = 1F;
            }
        }
    }
}
