package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.world.MoveUtil;

public class VerusSpeed extends ModeSpeed {

    private int damageTicks;
    private int ticks;

    public VerusSpeed() { super("Verus"); }

    @Listen
    public void onMotion(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            if(MoveUtil.isMovingWithKeys()) {
                MoveUtil.stop();
            }

            if(mc.thePlayer.onGround) {
                ticks = 0;
            } else {
                ticks++;
            }

            if(mc.thePlayer.hurtTime > 1 && !mc.thePlayer.isBurning() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && parent.damageBoost.get() && damageTicks > 30) {
                    MoveUtil.strafe(5);
                    mc.thePlayer.motionX *= 1.2F;
                    mc.thePlayer.motionZ *= 1.2F;
                    mc.thePlayer.motionY = 0.1F;
                damageTicks = 0;
            } else {
                damageTicks++;
            }

            switch(parent.verusMode.get()) {
                case "Hop 1":
                    if (!MoveUtil.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        return;
                    }

                    mc.gameSettings.keyBindJump.pressed = true;
                    mc.thePlayer.speedInAir = (float) (0.02 + Math.random() / 100);

                    MoveUtil.strafe((float) MoveUtil.getSpeed());
                    break;
                case "Hop 2":
                    if(!MoveUtil.isMoving())
                        return;

                    if (mc.thePlayer.onGround) {
                        MoveUtil.strafe(0.53F);
                        mc.thePlayer.jump();
                    } else {
                        MoveUtil.strafe(0.33F);
                    }

                    if (mc.thePlayer.moveForward < 0) {
                        MoveUtil.strafe(0.3F);
                    }
                    break;
                case "Funny":
                    switch(ticks) {
                        case 0:
                            mc.thePlayer.jump();
                            MoveUtil.strafe(0.4F);
                            break;
                        case 1:
                            MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() + 0.05F);
                            break;
                        case 5:
                        case 10:
                        case 15:
                        case 20:
                            if (mc.thePlayer.moveForward > 0 && mc.thePlayer.moveStrafing == 0) {
                                MoveUtil.strafe(0.375F);
                            } else if (mc.thePlayer.moveStrafing != 0 && mc.thePlayer.moveForward > 0) {
                                MoveUtil.strafe(0.38F);
                            } else if (mc.thePlayer.moveStrafing == 0 && mc.thePlayer.moveForward < 0) {
                                MoveUtil.strafe(0.34F);
                            } else if (mc.thePlayer.moveForward == 0) {
                                MoveUtil.strafe(0.34F);
                            }
                            break;
                        case 8:
                            event.setOnGround(true);
                            mc.thePlayer.onGround = true;
                            mc.thePlayer.jump();
                            break;
                    }
                    MoveUtil.strafe((float) MoveUtil.getSpeed());
            }
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        mc.gameSettings.keyBindJump.pressed = false;
        super.onDisable();
    }
}
