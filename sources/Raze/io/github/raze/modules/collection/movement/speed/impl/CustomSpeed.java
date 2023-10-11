package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.world.MoveUtil;
import net.minecraft.client.settings.KeyBinding;

public class CustomSpeed extends ModeSpeed {

    public CustomSpeed() { super("Custom"); }

    @Listen
    public void onMotion(EventMotion event) {
        if (event.getState() == Event.State.PRE) {

            mc.timer.timerSpeed = parent.timerSpeed.get().floatValue();

            if (mc.thePlayer.onGround) {
                MoveUtil.setSpeed(parent.groundSpeed.get().doubleValue());

                if (mc.thePlayer.moveForward != 0.0) {
                    mc.thePlayer.motionY = parent.motionY.get().doubleValue();
                }

            } else if (mc.thePlayer.isAirBorne) {
                MoveUtil.setSpeed(parent.airSpeed.get().doubleValue());

                if (mc.thePlayer.motionY <= 0.0) {
                    mc.thePlayer.motionY *= parent.downMultiplier.get().doubleValue();
                }

                if (parent.strafe.get()) {
                    MoveUtil.setSpeed(Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
                }

                if (parent.yPort.get()) {
                    mc.thePlayer.motionY = -parent.minusMotionY.get().doubleValue();
                }

            } else {
                MoveUtil.setSpeed(Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
                if (parent.yPort.get()) {
                    mc.thePlayer.motionY = -parent.minusMotionY.get().doubleValue();
                }
            }

            if (parent.sprint.get() && !mc.thePlayer.isSprinting()) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            }

            if (!MoveUtil.isMoving()) {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
            }

        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        super.onDisable();
    }
}