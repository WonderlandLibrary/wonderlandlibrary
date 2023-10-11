package io.github.raze.modules.collection.movement.step.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.step.ModeStep;
import io.github.raze.utilities.collection.world.MoveUtil;

public class NCPStep extends ModeStep {

    public NCPStep() { super("NCP"); }

    private boolean hasStepped;

    @Listen
    public void onMotionEvent(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.stepHeight = 0.6F;
            if(mc.thePlayer.onGround) {
                hasStepped = false;
            }

            if(!hasStepped && MoveUtil.isMoving() &&  mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally) {
                mc.thePlayer.jump();
                hasStepped = true;
            } else {
                if (!mc.thePlayer.isCollidedHorizontally && hasStepped && MoveUtil.isMoving()) {
                    hasStepped = false;
                    mc.thePlayer.motionY = 0;
                    if (mc.thePlayer.moveForward > 0) {
                        MoveUtil.setSpeed(MoveUtil.getBaseMoveSpeed() * 0.47);
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
        super.onDisable();
    }

}
