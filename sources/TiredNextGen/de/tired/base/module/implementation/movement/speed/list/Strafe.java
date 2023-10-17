package de.tired.base.module.implementation.movement.speed.list;

import de.tired.util.hook.PlayerHook;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.implementation.movement.speed.SpeedExtension;
import de.tired.base.module.implementation.movement.speed.SpeedModeAnnotation;

@SpeedModeAnnotation(name = "Strafe")
public class Strafe extends SpeedExtension {

    @Override
    public void onPre(EventPreMotion eventPreMotion) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        MC.thePlayer.ticksExisted = 0;
        MC.timer.timerSpeed = 1;
    }

    @Override
    public void onPacket(PacketEvent eventPacket) {

    }

    @Override
    public void onUpdate(UpdateEvent eventUpdate) {
        if (!PlayerHook.isMoving()) return;
        PlayerHook.setupStrafe();
        if (MC.thePlayer.onGround)
            MC.thePlayer.jump();
    }
}
