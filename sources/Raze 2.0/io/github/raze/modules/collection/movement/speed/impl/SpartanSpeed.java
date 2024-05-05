package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.math.TimeUtil;
import io.github.raze.utilities.collection.world.MoveUtil;

public class SpartanSpeed extends ModeSpeed {

    public SpartanSpeed() { super("Spartan"); }

    private final TimeUtil timer = new TimeUtil();
    private boolean boost = true;

    @Listen
    public void onMotion(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            switch (parent.spartanMode.get()) {
                case "Normal":
                    mc.gameSettings.keyBindJump.pressed = MoveUtil.isMoving();

                    MoveUtil.strafe((float) MoveUtil.getSpeed());

                    mc.timer.timerSpeed = (float) (1.07 + Math.random() / 25);

                    if (0 > mc.thePlayer.moveForward && mc.thePlayer.moveStrafing == 0) {
                        mc.thePlayer.speedInAir = (float) (0.04 - Math.random() / 100);
                    } else if (mc.thePlayer.moveStrafing != 0) {
                        mc.thePlayer.speedInAir = (float) (0.036 - Math.random() / 100);
                    } else {
                        mc.thePlayer.speedInAir = (float) (0.0215F - Math.random() / 1000);
                    }

                    if (mc.thePlayer.onGround) {
                        MoveUtil.strafe((float) (MoveUtil.getSpeed() + 0.002));
                    }
                    break;

                case "LowHop":
                    if (mc.gameSettings.keyBindJump.pressed)
                        return;

                    MoveUtil.strafe((float) MoveUtil.getSpeed());

                    mc.timer.timerSpeed = (float) (1.07 + Math.random() / 25);

                    if (0 > mc.thePlayer.moveForward && mc.thePlayer.moveStrafing == 0) {
                        mc.thePlayer.speedInAir = (float) (0.04 - Math.random() / 100);
                    } else if (mc.thePlayer.moveStrafing != 0) {
                        mc.thePlayer.speedInAir = (float) (0.036 - Math.random() / 100);
                    } else {
                        mc.thePlayer.speedInAir = (float) (0.0215F - Math.random() / 1000);
                    }

                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.0002F;
                        MoveUtil.strafe((float) (MoveUtil.getSpeed() + 0.008));
                    }
                    break;

                case "Timer":
                    if(mc.thePlayer.onGround && MoveUtil.isMoving()) {
                        mc.thePlayer.jump();
                    }

                    if(timer.elapsed(3000)) {
                        boost = !boost;
                        timer.reset();
                    }

                    if(boost) {
                        mc.timer.timerSpeed = 1.6f;
                    } else {
                        mc.timer.timerSpeed = 1.0f;
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
