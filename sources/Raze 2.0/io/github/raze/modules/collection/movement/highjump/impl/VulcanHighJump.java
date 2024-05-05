package io.github.raze.modules.collection.movement.highjump.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.highjump.ModeHighJump;

public class VulcanHighJump extends ModeHighJump {

    public VulcanHighJump() { super("Vulcan"); }

    private boolean jumped;

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                jumped = true;
            } else if(mc.thePlayer.fallDistance < 1.4 && jumped) {
                mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 9, mc.thePlayer.posZ);
                jumped = false;
            }
        }
    }

}
