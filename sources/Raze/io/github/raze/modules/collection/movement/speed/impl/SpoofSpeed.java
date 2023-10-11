package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.world.MoveUtil;

public class SpoofSpeed extends ModeSpeed {

    public SpoofSpeed() { super("Spoof"); }

    @Listen
    public void onMotion(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            int yaw = 0;
            float moveSpeed = MoveUtil.getBaseMoveSpeed();

            if (mc.thePlayer.moveForward < 0) {
                yaw = -180;
            } else if (mc.thePlayer.moveStrafing > 0) {
                yaw = 90;
            } else if (mc.thePlayer.moveStrafing < 0) {
                yaw = -90;
            }

            event.setYaw(mc.thePlayer.rotationYaw - yaw);
            mc.thePlayer.rotationYawHead = event.getYaw();
            mc.thePlayer.renderYawOffset = event.getYaw();
            mc.gameSettings.keyBindJump.pressed = MoveUtil.isMoving();

            if (mc.thePlayer.onGround) {
                MoveUtil.strafe((float) (moveSpeed * 1.2) + MoveUtil.getSpeedBoost(1));
            } else {
                MoveUtil.strafe((float) (moveSpeed - 0.07 + Math.random() / 10));
            }
        }
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = false;
        super.onDisable();
    }
}
