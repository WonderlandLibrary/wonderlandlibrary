package tech.drainwalk.client.module.modules.movement;

import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.events.EventTarget;
import tech.drainwalk.events.Player.EventPreMotion;
import tech.drainwalk.utility.Helper;

import tech.drainwalk.utility.movement.MovementUtils;

public class SpeedModule extends Module {
    public SpeedModule() {
        super("SpeedModule", Category.MOVEMENT);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (MovementUtils.isMoving()) {
            if (Helper.mc.player.onGround) {
                Helper.mc.player.jump();
            }
            if (Helper.mc.player.fallDistance <= 0.2) {
                Helper.mc.timer.timerSpeed = 2.88f;
                Helper.mc.player.jumpMovementFactor = 0.02652f;
            } else if ((double) Helper.mc.player.fallDistance < 1.24) {
                Helper.mc.timer.timerSpeed = 0.47f;
            }
            if (Helper.mc.player.motionY == -0.4478299643949201D) ;
        }
    }
}
