package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.world.MoveUtil;

public class LegitSpeed extends ModeSpeed {

    public LegitSpeed() { super("Legit"); }

    @Listen
    public void onMotionEvent(EventMotion event) {
        if (event.getState() != Event.State.PRE) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                mc.thePlayer.jump();
            }
        }
    }
}
