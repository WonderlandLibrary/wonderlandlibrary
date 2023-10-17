package de.tired.base.module.implementation.movement.speed.list;

import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.implementation.movement.speed.SpeedExtension;
import de.tired.base.module.implementation.movement.speed.SpeedModeAnnotation;
import de.tired.util.hook.PlayerHook;
import de.tired.util.log.IngameChatLog;
import de.tired.util.math.MathUtil;

@SpeedModeAnnotation(name = "Vulcan")
public class Vulcan extends SpeedExtension {

    private boolean speedUp = false;

    @Override
    public void onPre(EventPreMotion eventPreMotion) {

    }

    @Override
    public void onPacket(PacketEvent eventPacket) {

    }

    @Override
    public void onUpdate(UpdateEvent eventUpdate) {

        final double speedMultiply = 1.02F;

        if (MC.thePlayer.onGround && PlayerHook.isMoving()) {
            MC.gameSettings.keyBindSneak.pressed = false;
            MC.thePlayer.jump();
            this.speedUp = !speedUp;
            MC.thePlayer.ticksExisted = 0;
        }
        if (speedUp) {
            MC.thePlayer.speedInAir = .023F;
        } else {
            MC.thePlayer.motionX *= .97F;
            MC.thePlayer.motionZ *= .97F;
            MC.timer.timerSpeed = 1F;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        MC.thePlayer.speedInAir = .02F;
    }

}
