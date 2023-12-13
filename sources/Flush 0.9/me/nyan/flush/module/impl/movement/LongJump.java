package me.nyan.flush.module.impl.movement;

import me.nyan.flush.event.Event;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import net.minecraft.util.MathHelper;

public class LongJump extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Cubecraft", "Funcraft", "Redesky");
    private final NumberSetting speedValue = new NumberSetting("Speed", this, 3, 1, 10, 0.1,
            () -> mode.is("vanilla")),
            height = new NumberSetting("Height", this, 0.45, 0.4, 1, 0.1,
                    () -> mode.is("vanilla"));

    private int state, stage, autoDisable;
    private double lastDist, speed;

    public LongJump() {
        super("LongJump", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (mode.is("cubecraft")) {
        }
        stage = 0;
        autoDisable = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1;

        if (!mode.is("redesky"))
            MovementUtils.stopMotion();
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (isEnabled(Fly.class)) {
            return;
        }

        switch (mode.getValue().toUpperCase()) {
            case "VANILLA":
                if (MovementUtils.isMoving()) {
                    MovementUtils.setSpeed(speedValue.getValue() / 2);
                } else {
                    MovementUtils.setSpeed(0);
                }

                if (mc.thePlayer.onGround && MovementUtils.isMoving()) {
                    mc.thePlayer.motionY = height.getValue();
                }
                break;


            case "CUBECRAFT":
                if (mc.thePlayer.onGround && mc.thePlayer.hurtTime > 8 && stage == 0)
                    stage = 1;

                MovementUtils.stopMotion();

                if (stage > 0) {
                    if (stage == 1) {
                        speed = lastDist / 159 + 1.4;
                        stage++;
                    }

                    if (stage == 2) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                            stage++;
                        }
                    }

                    if (stage == 3) {
                        mc.gameSettings.keyBindRight.pressed = false;
                        mc.gameSettings.keyBindLeft.pressed = false;
                        mc.gameSettings.keyBindBack.pressed = false;

                        if (speed > 0.25)
                            speed -= 0.01;

                        state++;

                        switch (state) {
                            case 1:
                                MovementUtils.vClip(1E-12);
                                break;
                            case 2:
                                MovementUtils.vClip(-1E-12);
                                state = 0;
                                break;
                        }
                    }

                    if (!mc.thePlayer.onGround && MovementUtils.isMoving())
                        MovementUtils.setSpeed(speed);
                }
                break;

            case "FUNCRAFT":
                if (mc.thePlayer.onGround) {
                    if (stage == 0) stage = 1;
                    else disable();
                }

                if (stage > 0) {
                    if (stage == 1) {
                        speed = lastDist / 159 + 1.3;
                        stage++;
                    }

                    if (stage == 2) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                            stage++;
                        }
                    }

                    if (stage == 3) {
                        mc.gameSettings.keyBindRight.pressed = false;
                        mc.gameSettings.keyBindLeft.pressed = false;
                        mc.gameSettings.keyBindBack.pressed = false;

                        if (speed > 0.25)
                            speed -= 0.01;

                        state++;

                        switch (state) {
                            case 1:
                                MovementUtils.vClip(1E-12);
                                break;
                            case 2:
                                MovementUtils.vClip(-1E-12);
                                state = 0;
                                break;
                        }
                    }

                    if (!mc.thePlayer.onGround && MovementUtils.isMoving())
                        MovementUtils.setSpeed(speed);
                }
                break;

            case "JETPACK":
                if (MovementUtils.isMoving()) {
                    mc.thePlayer.motionY = 0.30;
                    mc.thePlayer.motionX *= 1.1;
                    mc.thePlayer.motionZ *= 1.1;
                }
                break;

            case "REDESKY":
                /*
                *MovementUtils.setSpeed(0.114332);

                if (!MovementUtils.isMoving()) {
                    MovementUtils.setSpeed(0);
                } else if (!mc.thePlayer.isCollidedHorizontally) {
                    mc.timer.timerSpeed = 0.4237832223f;

                    if (mc.thePlayer.ticksExisted % 3 == 0) {
                        MovementUtils.hClip(1.38823);
                    }

                    if (mc.thePlayer.onGround)
                        mc.thePlayer.jump();
                }

                 */

                /*
                mc.timer.timerSpeed = 0.37153457F;

                if (MovementUtils.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0;
                        MovementUtils.vClip(2.80673223);
                        MovementUtils.packetvClip(-2.3973223, false);
                    } else {
                        MovementUtils.setSpeed(8.7232);
                        MovementUtils.packethClip(8.7232);

                        mc.thePlayer.motionY = 0.12823;
                    }
                }

                 */

                if (mc.thePlayer.onGround || mc.thePlayer.isCollidedHorizontally) {
                    autoDisable++;

                    if (autoDisable == 2) {
                        toggle();
                        return;
                    }
                }

                if (MovementUtils.isMoving()) {
                    mc.thePlayer.moveStrafing = 0;

                    if (mc.thePlayer.onGround)
                        mc.thePlayer.motionY = 0.55;

                    float multiplier = mc.thePlayer.motionY > 0 ? 0.222F : 0.018f;

                    float f = mc.thePlayer.rotationYaw * 0.017453292F;
                    mc.thePlayer.motionX -= MathHelper.sin(f) * multiplier;
                    mc.thePlayer.motionZ += MathHelper.cos(f) * multiplier;

                    if (mc.thePlayer.motionY < 0 && mc.thePlayer.motionY > -0.1)
                        mc.thePlayer.motionY += 0.072;
                }
                break;
        }
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (e.getState() == Event.State.PRE) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}
