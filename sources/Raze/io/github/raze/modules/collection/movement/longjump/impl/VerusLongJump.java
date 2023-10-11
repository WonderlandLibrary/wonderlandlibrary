package io.github.raze.modules.collection.movement.longjump.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.longjump.ModeLongJump;
import io.github.raze.utilities.collection.world.MoveUtil;
import net.minecraft.potion.Potion;

public class VerusLongJump extends ModeLongJump {

    public VerusLongJump() { super("Verus"); }

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {

            if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                mc.gameSettings.keyBindJump.pressed = true;
            }

            if (MoveUtil.isMoving()) {
                float dir = mc.thePlayer.rotationYaw + ((mc.thePlayer.moveForward < 0) ? 180 : 0) + ((mc.thePlayer.moveStrafing > 0) ? (-90F * ((mc.thePlayer.moveForward < 0) ? -.5F : ((mc.thePlayer.moveForward > 0) ? .4F : 1F))) : 0);

                float xDir = (float) Math.cos((dir + 90) * Math.PI / 180);
                float zDir = (float) Math.sin((dir + 90) * Math.PI / 180);

                if (mc.thePlayer.isCollidedVertically) {
                    mc.thePlayer.motionX = xDir * .29F;
                    mc.thePlayer.motionZ = zDir * .29F;
                }

                if (mc.thePlayer.motionY == .33319999363422365) {
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        mc.thePlayer.motionX = xDir * 1.34;
                        mc.thePlayer.motionZ = zDir * 1.34;
                    } else {
                        mc.thePlayer.motionX = xDir * 0.50;
                        mc.thePlayer.motionZ = zDir * 0.50;
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = false;
        super.onDisable();
    }

}
