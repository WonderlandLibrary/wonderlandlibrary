package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.world.MoveUtil;

public class KarhuSpeed extends ModeSpeed {

    public KarhuSpeed() { super("Karhu"); }

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(mc.gameSettings.keyBindJump.pressed ||!MoveUtil.isMoving())
            return;

        mc.thePlayer.setSprinting(true);

        if (event.getState() == Event.State.PRE) {
            if(mc.thePlayer.onGround) {
                mc.timer.timerSpeed = 1;
                mc.thePlayer.jump();
                mc.thePlayer.motionY *= 0.55;
            } else {
                mc.timer.timerSpeed = (float) (1 + (Math.random() - 0.5) / 100);
            }
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        super.onDisable();
    }
}
