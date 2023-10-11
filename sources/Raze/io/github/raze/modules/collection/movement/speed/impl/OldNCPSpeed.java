package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.world.MoveUtil;

public class OldNCPSpeed extends ModeSpeed {

    public OldNCPSpeed() { super("Old NCP"); }

    @Listen
    public void onMotion(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            if (parent.oldNcpMode.compare("Y-Port")) {
                mc.thePlayer.setSprinting(true);
                if (MoveUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionX *= 0.75;
                        mc.thePlayer.motionZ *= 0.75;
                    } else {
                        if (mc.thePlayer.motionY < 0.4) {
                            mc.thePlayer.motionY = -1337.0;
                            MoveUtil.setSpeed(0.26);
                        }
                    }
                }
            }
        }
    }
}
