package com.ohare.client.module.modules.movement;

import com.ohare.client.event.events.player.MotionEvent;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.value.impl.EnumValue;
import com.ohare.client.utils.value.impl.NumberValue;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.potion.Potion;

import java.awt.*;

public class LongJump extends Module {
    private double moveSpeed, lastDist;
    private int level;
    private NumberValue<Double> boostval = new NumberValue("Boost", 3.0D, 0.1D, 5.0D, 0.1D);
    private EnumValue<Mode> mode = new EnumValue("Mode", Mode.Hypixel);

    public LongJump() {
        super("LongJump", Category.MOVEMENT, new Color(0, 255, 150, 255).getRGB());
        setDescription("Jump and zoom");
        setRenderlabel("Long Jump");
        addValues(mode, boostval);
    }

    public enum Mode {
        Hypixel, Mineplex, Vanilla
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.isOnLiquid() || mc.thePlayer.isInLiquid()) return;
        switch (mode.getValue()) {
            case Vanilla:
                setMoveSpeed(event,boostval.getValue());
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        event.setY(mc.thePlayer.motionY = 0.41);
                    }
                } else {
                    mc.thePlayer.motionX = 0.0;
                    mc.thePlayer.motionZ = 0.0;
                }
                break;
            case Hypixel:
                double forward = mc.thePlayer.movementInput.moveForward;
                double strafe = mc.thePlayer.movementInput.moveStrafe;
                float yaw = mc.thePlayer.rotationYaw;
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
                        ++level;
                        double motionY = 0.40123128;
                        if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
                            if (mc.thePlayer.isPotionActive(Potion.jump)) motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            event.setY(mc.thePlayer.motionY = motionY);
                            moveSpeed *= 2.149;
                        }
                    } else if (level == 3) {
                        ++level;
                        double difference = 0.6963D * (lastDist - getBaseMoveSpeed());
                        moveSpeed = lastDist - difference;
                    } else {
                        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                            if (level == 4) {
                                toggle();
                            }
                            level = 1;
                        }
                        moveSpeed = lastDist - lastDist / 159.0D;
                    }
                } else {
                    level = 2;
                    double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? boostval.getValue() : boostval.getValue() + 1.1;
                    moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                final double mx = -Math.sin(Math.toRadians(yaw));
                final double mz = Math.cos(Math.toRadians(yaw));
                event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
                event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
                break;

            case Mineplex:
                if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
                    double speed = 0f;
                    level++;
                    if (mc.thePlayer.onGround) {
                        if (level > 7) {
                            toggle();
                            return;
                        }
                        event.setY(mc.thePlayer.motionY = 0.42);
                        mc.thePlayer.motionY += 0.098;
                        speed = 0;
                    }
                    else {
                        if (mc.thePlayer.motionY >= 0) {
                            mc.thePlayer.motionY += 0.04;
                        }

                        if (mc.thePlayer.motionY < 0) {
                            if (mc.thePlayer.motionY > -0.3) {
                                mc.thePlayer.motionY += 0.0331;
                            }
                            else {
                                if (mc.thePlayer.motionY > -0.38) {
                                    mc.thePlayer.motionY += 0.031;
                                }
                                else {
                                    mc.thePlayer.motionY += 0.01;
                                }
                            }
                        }
                        double slowdown = 0.007;
                        speed = (0.815-(level*slowdown));
                        if (speed < 0) speed = 0;
                    }
                    setMoveSpeed(event, speed);
                }
                break;

        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null) return;
        setSuffix(mode.getValue().name());
        if (mc.thePlayer.isOnLiquid() || mc.thePlayer.isInLiquid()) return;
        if (!event.isPre()) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
        }
    }
    private void setMoveSpeed(final MotionEvent event, final double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
            event.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
        }
    }

    private double getBaseMoveSpeed() {
        double n = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        if (mc.thePlayer != null) {
            moveSpeed = getBaseMoveSpeed();
        }
        lastDist = 0.0D;
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        level = 0;
        lastDist = 0.0D;
    }
}
