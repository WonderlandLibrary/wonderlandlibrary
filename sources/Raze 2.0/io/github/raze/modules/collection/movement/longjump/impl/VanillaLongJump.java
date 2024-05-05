package io.github.raze.modules.collection.movement.longjump.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.longjump.ModeLongJump;
import io.github.raze.utilities.collection.world.MoveUtil;

public class VanillaLongJump extends ModeLongJump {

    public VanillaLongJump() { super("Vanilla"); }

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                mc.thePlayer.motionY = 0.493F;
            } else {
                final double rotation = Math.toRadians(mc.thePlayer.rotationYaw), x = Math.sin(rotation), z = Math.cos(rotation);

                if(MoveUtil.isMoving()) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX - x * 0.1673, mc.thePlayer.posY, mc.thePlayer.posZ + z * 0.1673);
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01557f, mc.thePlayer.posZ);
                    mc.thePlayer.motionY = mc.thePlayer.motionY + 0.0155f;
                    mc.thePlayer.speedInAir = 0.027f;
                    mc.thePlayer.jumpMovementFactor = mc.thePlayer.jumpMovementFactor + 0.00155f;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        super.onDisable();
    }

}
