package io.github.raze.modules.collection.movement.highjump.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.highjump.ModeHighJump;

public class VulcanDamageHighJump extends ModeHighJump {

    public VulcanDamageHighJump() { super("Vulcan Damage"); }

    private int jumpTimes;

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            if(mc.thePlayer.onGround)
                jumpTimes = 0;

            if(mc.thePlayer.hurtTime != 0) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                } else {
                    mc.thePlayer.motionY += 0.02;

                    if (mc.thePlayer.hurtTime < 7) {
                        mc.thePlayer.motionX *= .066F;
                        mc.thePlayer.motionZ *= .066F;
                        mc.thePlayer.jump();
                    }
                }

                if(mc.thePlayer.ticksExisted % 2 == 0 && 3 >= jumpTimes && !mc.thePlayer.onGround && mc.thePlayer.hurtTime != 0) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 9, mc.thePlayer.posZ);
                    jumpTimes++;
                }
            }
        }
    }

}
