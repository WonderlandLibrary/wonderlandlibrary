package io.github.raze.modules.collection.movement.longjump.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.longjump.ModeLongJump;
import io.github.raze.utilities.collection.world.MoveUtil;

public class VulcanLongJump extends ModeLongJump {

    public VulcanLongJump() { super("Vulcan"); }

    private int jumpTimes = 0;

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            if(MoveUtil.isMoving() && mc.thePlayer.onGround)
                mc.gameSettings.keyBindJump.pressed = true;

            if(mc.thePlayer.ticksExisted % 2 == 0 && 3 >= jumpTimes && !mc.thePlayer.onGround) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 9, mc.thePlayer.posZ);
                jumpTimes++;
            }

            if(mc.thePlayer.onGround) {
                jumpTimes = 0;
            }

            if (mc.thePlayer.ticksExisted % 2 == 0) {
                mc.thePlayer.motionY = -0.155F;
            } else {
                mc.thePlayer.motionY = -0.1F;
            }
        }
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = false;
        super.onDisable();
    }

}
