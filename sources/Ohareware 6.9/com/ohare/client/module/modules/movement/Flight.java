package com.ohare.client.module.modules.movement;

import com.ohare.client.event.events.player.MotionEvent;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.MathUtils;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.EnumValue;
import com.ohare.client.utils.value.impl.NumberValue;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.potion.Potion;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class Flight extends Module {
    private int level = 1;
    private TimerUtil timer = new TimerUtil();
    private boolean decreasing2, HYPIXELboost, canboost;
    private float lastDist, moveSpeed;
    private EnumValue<Modes> mode = new EnumValue<>("Mode", Modes.HYPIXEL);
    private EnumValue<BoostModes> BoostMode = new EnumValue<>("Boost Mode", BoostModes.MOTION);
    private BooleanValue viewbob = new BooleanValue("ViewBob", true);
    private BooleanValue Boost = new BooleanValue("Boost", true);
    private NumberValue<Double> flyspeed = new NumberValue<>("FlySpeed", 2.0, 0.8, 3.5, 0.1);
    private float timervalue;

    public Flight() {
        super("Flight", Category.MOVEMENT, new Color(33, 120, 255, 255).getRGB());
        setDescription("Be a little birdie");
        addValues(viewbob,Boost, flyspeed, mode, BoostMode);
    }
    public enum Modes {
        HYPIXEL, VANILLA
    }
    public enum BoostModes {
        MOTION, DAMAGE
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        setSpeed(0);
        mc.thePlayer.motionX = mc.thePlayer.motionY = mc.thePlayer.motionZ = 0;
    }

    @Override
    public void onEnable() {
        if (mc.theWorld != null && mc.thePlayer != null) {
            level = 1;
            moveSpeed = 0.1f;
            lastDist = 0.0f;
            float motionY = 0.4085f;
            if (Boost.isEnabled()) {
                if (BoostMode.getValue() == BoostModes.DAMAGE) {
                    if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.isCollidedVertically) {
                        mc.thePlayer.damagePlayer();
                        if (mc.thePlayer.isPotionActive(Potion.jump))
                            motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                        mc.thePlayer.motionY = motionY;
                    }
                }
                if (BoostMode.getValue() == BoostModes.MOTION) {
                    canboost = true;
                    timervalue = 1.0F;
                    if (mc.thePlayer.onGround) {
                        if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.isCollidedVertically) {
                            if (mc.thePlayer.isPotionActive(Potion.jump))
                                motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            mc.thePlayer.motionY = motionY;
                        }
                        level = 1;
                        moveSpeed = 0.1f;
                        HYPIXELboost = true;
                        lastDist = 0.0f;
                    }
                    timer.reset();
                }
            }
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        if (event.isPre()) {
            float xDist = (float) (mc.thePlayer.posX - mc.thePlayer.prevPosX);
            float zDist = (float) (mc.thePlayer.posZ - mc.thePlayer.prevPosZ);
            if (viewbob.getValue()) mc.thePlayer.cameraYaw = 0.15f;
            switch (mode.getValue()) {
                case HYPIXEL:
                    lastDist = (float) Math.sqrt((xDist * xDist) + (zDist * zDist));
                    if (canboost && HYPIXELboost) {
                        timervalue += decreasing2 ? -0.01 : 0.05;
                        if (timervalue >= 1.4) {
                            decreasing2 = true;
                        }
                        if (timervalue <= 0.9) {
                            decreasing2 = false;
                        }
                        if (timer.reach(2000)) {
                            canboost = false;
                        }
                    }
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.4, mc.thePlayer.posZ);
                        mc.thePlayer.motionY = 0.8;
                        mc.thePlayer.motionX *= 0.1;
                        mc.thePlayer.motionZ *= 0.1;
                    }
                    if ((mc.thePlayer.ticksExisted % 2) == 0) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtils.getRandomInRange(0.00000000000001235423532523523532521, 0.0000000000000123542353252352353252 * 10), mc.thePlayer.posZ);
                    }
                    mc.thePlayer.motionY = 0;
                    break;
                default:
                    break;
            }
        }
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        float yaw = mc.thePlayer.rotationYaw;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        double forward = mc.thePlayer.movementInput.moveForward;
        double mx = -Math.sin(Math.toRadians(yaw)), mz = Math.cos(Math.toRadians(yaw));
        switch (mode.getValue()) {
            case VANILLA:
                if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()) {
                    setMoveSpeed(event, flyspeed.getValue());
                }
                else {
                    setMoveSpeed(event, 0);
                }
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.motionY = 0.085;
                mc.thePlayer.jumpMovementFactor = 2;

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY += flyspeed.getValue()*0.75;
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY -= flyspeed.getValue()*0.75;
                }
                break;
            case HYPIXEL:
                setMoveSpeed(event, getBaseMoveSpeed());
                if (Boost.isEnabled()) {
                    switch (BoostMode.getValue()) {
                        case MOTION:
                            if (forward != 0 && strafe != 0) {
                                forward = forward * Math.sin(Math.PI / 4);
                                strafe = strafe * Math.cos(Math.PI / 4);
                            }
                            if (HYPIXELboost) {
                                if (level != 1 || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
                                    if (level == 2) {
                                        level = 3;
                                        moveSpeed *= 2.149F;
                                    } else if (level == 3) {
                                        level = 4;
                                        float difference = (float) (0.695f * (lastDist - getBaseMoveSpeed()));
                                        moveSpeed = lastDist - difference;
                                    } else {
                                        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                                            level = 1;
                                        }
                                        moveSpeed = lastDist - lastDist / 159.0f;
                                    }
                                } else {
                                    level = 2;
                                    double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.706 : 2.034;
                                    moveSpeed = (float) (boost * getBaseMoveSpeed() - 0.01f);
                                }
                                moveSpeed = (float) Math.max(moveSpeed, getBaseMoveSpeed());
                                event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
                                event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
                                if (forward == 0.0F && strafe == 0.0F) {
                                    event.setX(0.0);
                                    event.setZ(0.0);
                                }
                                if (timer.reach(2200) && HYPIXELboost) {
                                    HYPIXELboost = false;
                                }
                            }
                            else {
                                setMoveSpeed(event, getBaseMoveSpeed());
                            }
                            break;
                        case DAMAGE:
                            if (forward == 0.0F && strafe == 0.0F) {
                                event.setX(0);
                                event.setZ(0);
                            }
                            if (forward != 0 && strafe != 0) {
                                forward = forward * Math.sin(Math.PI / 4);
                                strafe = strafe * Math.cos(Math.PI / 4);
                            }
                            if (level != 1 || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
                                if (level == 2) {
                                    level = 3;
                                    moveSpeed *= 2.149f;
                                } else if (level == 3) {
                                    level = 4;
                                    double difference = (mc.thePlayer.ticksExisted % 2 == 0 ? 0.33f : 0.323f) * (lastDist - getBaseMoveSpeed());
                                    moveSpeed = (float) (lastDist - difference);
                                } else {
                                    if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                                        level = 1;
                                    }
                                    moveSpeed = lastDist - lastDist / 159f;
                                }
                            } else {
                                level = 2;
                                int amplifier = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
                                // double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56 : 2.034;
                                double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.8 : 2.2;
                                moveSpeed = (float) (boost * getBaseMoveSpeed() - 0.01f);
                            }
                            moveSpeed = (float) Math.max(moveSpeed, getBaseMoveSpeed());
                            event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
                            event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
                            if (forward == 0.0F && strafe == 0.0F) {
                                event.setX(0.0);
                                event.setZ(0.0);
                            }
                            break;
                    }
                }
                break;
        }
    }

    private double getBaseMoveSpeed() {
        double n = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    private void setSpeed(double speed) {
        mc.thePlayer.motionX = -(Math.sin(getDirection()) * speed);
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    private float getDirection() {
        float var1 = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        } else {
            forward = 1.0f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }

    private void setMoveSpeed(MotionEvent event, double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
            event.setX(0);
            event.setZ(0);
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += forward > 0.0D ? -45 : 45;
                } else if (strafe < 0.0D) {
                    yaw += forward > 0.0D ? 45 : -45;
                }

                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }
            }

            event.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
            event.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
        }
    }


    private float getSpeed() {
        return (float) Math.sqrt((mc.thePlayer.motionX * mc.thePlayer.motionX) + (mc.thePlayer.motionZ * mc.thePlayer.motionZ));
    }
}