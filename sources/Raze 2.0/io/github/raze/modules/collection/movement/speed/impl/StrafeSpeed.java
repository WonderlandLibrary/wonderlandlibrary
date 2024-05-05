package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.world.MoveUtil;

public class StrafeSpeed extends ModeSpeed {

    public StrafeSpeed() { super("Strafe"); }

    @Listen
    public void onMotion(EventMotion event) {
        if (event.getState().name().equalsIgnoreCase(parent.strafeState.get())) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                mc.thePlayer.jump();
            }

            switch (parent.strafeMode.get()) {
                case "Bypass":
                    MoveUtil.strafe((float) MoveUtil.getSpeed());
                    break;
                case "Normal":
                    MoveUtil.strafe(MoveUtil.getBaseMoveSpeed());
                    break;
            }
        }
    }

}
