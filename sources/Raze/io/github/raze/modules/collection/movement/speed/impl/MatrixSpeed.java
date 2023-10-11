package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.world.MoveUtil;

public class MatrixSpeed extends ModeSpeed {

    public MatrixSpeed() { super("Matrix"); }

    @Listen
    public void onMotion(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            switch(parent.matrixMode.get()) {
                case "Normal":
                    if (!MoveUtil.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        return;
                    }

                    mc.gameSettings.keyBindJump.pressed = true;

                    mc.thePlayer.speedInAir = 0.0203F;

                    if(mc.thePlayer.motionY > 0.4) {
                        mc.thePlayer.motionX *= 1.003F;
                        mc.thePlayer.motionZ *= 1.003F;
                    }

                    if(mc.thePlayer.onGround) {
                        mc.timer.timerSpeed = (float) (1.1 + Math.random() / 50 - Math.random() / 50);
                        mc.thePlayer.motionX *= 1.0045F;
                        mc.thePlayer.motionZ *= 1.0045F;
                    } else {
                        mc.timer.timerSpeed = (float) (1 - Math.random() / 500);
                    }
                    break;
                case "SkyCave":
                    if(MoveUtil.isMovingWithKeys()) {
                        MoveUtil.stop();
                    }

                    if (!MoveUtil.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        return;
                    }

                    mc.gameSettings.keyBindJump.pressed = true;

                    if(mc.thePlayer.motionY > 0) {
                        mc.thePlayer.motionX *= 1.005F;
                        mc.thePlayer.motionZ *= 1.005F;
                    }

                    mc.timer.timerSpeed = (float) (1.05 + mc.thePlayer.motionY / 3);

                    if(mc.thePlayer.onGround) {
                        MoveUtil.strafe((float) MoveUtil.getSpeed());
                    }
                    break;
            }
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.speedInAir = 0.02F;
        mc.gameSettings.keyBindJump.pressed = false;
        super.onDisable();
    }
}
