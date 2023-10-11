package io.github.raze.modules.collection.movement.step.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.step.ModeStep;

public class MotionStep extends ModeStep {

    public MotionStep() { super("Motion"); }

    @Listen
    public void onMotionEvent(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.stepHeight = 0.6F;

            if(mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
                mc.thePlayer.motionY = .39;
            }
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
        super.onDisable();
    }

}
