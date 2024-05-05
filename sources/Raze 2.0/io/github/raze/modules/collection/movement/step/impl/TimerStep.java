package io.github.raze.modules.collection.movement.step.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.step.ModeStep;

public class TimerStep extends ModeStep {

    public TimerStep() { super("Timer"); }

    private boolean hasStepped;

    @Listen
    public void onMotionEvent(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.stepHeight = 0.6F;
            if(mc.thePlayer.onGround) {
                hasStepped = false;
            }

            if(mc.thePlayer.isCollidedHorizontally && mc.thePlayer.fallDistance < 1.4 && !hasStepped) {
                mc.timer.timerSpeed = parent.timerSetting.get().intValue();
                mc.thePlayer.jump();
                hasStepped = true;
            } else {
                mc.timer.timerSpeed = 1;
            }
        }
    }


    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
        super.onDisable();
    }

}
