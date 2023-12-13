package com.ohare.client.module.modules.movement;

import com.ohare.client.event.events.player.MotionEvent;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.value.impl.EnumValue;
import com.ohare.client.utils.value.impl.NumberValue;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.potion.Potion;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;


/**
 * made by oHare for Client
 *
 * @since 5/29/2019
 **/
public class Speed extends Module {
    private int stage = 1, stageOG = 1;
    private double moveSpeed, lastDist, moveSpeedOG, lastDistOG;
    private EnumValue<Mode> mode = new EnumValue<>("Mode", Mode.HYPIXEL);
    private NumberValue<Double> boost = new NumberValue<>("Boost", 1.2, 0.1, 5.0, 0.1);
    private NumberValue<Double> jumpheight = new NumberValue<>("JumpHeight",0.41, 0.01, 1.0, 0.01);
    
    public Speed() {
        super("Speed", Category.MOVEMENT, new Color(0, 255, 0, 255).getRGB());
        setDescription("Zoomie zoom");
        addValues(mode, boost, jumpheight);
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null) return;
        lastDist = 0;
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null) return;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
        mc.timer.timerSpeed = 1f;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        final boolean tick = mc.thePlayer.ticksExisted % 2 == 0;
        if (mode.getValue() == Mode.ONGROUND) {
            if (event.isPre()) {
                if (mc.thePlayer.moveForward > 0 || mc.thePlayer.moveStrafing > 0) {
                    if (mc.thePlayer.onGround && !tick) {
                        event.setY(event.getY() + 0.41248);
                    }
                    if (tick) {
                        if (!mc.thePlayer.onGround) mc.thePlayer.motionY = -1.02345234623;
                    }
                    moveSpeedOG *= tick ? 2.12542 : 0.905;
                }
            }
        } else if (mode.getValue() == Mode.HYPIXEL || mode.getValue() == Mode.HypixelWtf || mode.getValue() == Mode.NCP) {
            lastDist = Math.sqrt(((mc.thePlayer.posX - mc.thePlayer.prevPosX) * (mc.thePlayer.posX - mc.thePlayer.prevPosX)) + ((mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (mc.thePlayer.posZ - mc.thePlayer.prevPosZ)));
            if (lastDist > 5) lastDist = 0.0D;
        } else if (mode.getValue() == Mode.AGC) {
            double speed = 0.2D;
            double x = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
            double z = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
            double n = mc.thePlayer.movementInput.moveForward * speed * x;
            double xOff = n + mc.thePlayer.movementInput.moveStrafe * speed * z;
            double n2 = mc.thePlayer.movementInput.moveForward * speed * z;
            double zOff = n2 - mc.thePlayer.movementInput.moveStrafe * 0.5F * x;

            if (mc.thePlayer.onGround) {
                if (mc.thePlayer.isMoving()) {
                    mc.thePlayer.motionY = 0.2D;
                }
            } else if (mc.thePlayer.motionY <= -0.10000000149011612D) {
                mc.thePlayer.setPosition(mc.thePlayer.posX + xOff * 1.0, mc.thePlayer.posY, mc.thePlayer.posZ + zOff * 1.0);
                mc.thePlayer.motionY -= 0.0010000000474974513D;
            }
        }
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        double forward = mc.thePlayer.movementInput.moveForward, strafe = mc.thePlayer.movementInput.moveStrafe, yaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.isOnLiquid() || mc.thePlayer.isInLiquid()) return;
        switch (mode.getValue()) {
            case BHOP:
                setMoveSpeed(event, boost.getValue());
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        event.setY(mc.thePlayer.motionY = jumpheight.getValue());
                    }
                } else {
                    mc.thePlayer.motionX = 0.0;
                    mc.thePlayer.motionZ = 0.0;
                }
                break;
            case ONGROUND:
                switch (stageOG) {
                    case 0:
                        ++stageOG;
                        lastDistOG = 0.0D;
                        break;
                    case 2:
                        break;
                    case 3:
                        moveSpeedOG = lastDistOG - (0.720236434 * (lastDistOG - getBaseMoveSpeed()));
                        break;
                    default:
                        if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                            stageOG = mc.thePlayer.moveForward == 0.0D && mc.thePlayer.moveStrafing == 0.0D ? 0 : 1;
                        }
                        moveSpeedOG = lastDistOG - lastDistOG / 159.0213245D;
                        break;
                }
                moveSpeedOG = mc.thePlayer.isInWater() ? 0 : Math.max(moveSpeedOG, getBaseMoveSpeed());
                double forward3 = mc.thePlayer.movementInput.moveForward;
                double strafe3 = mc.thePlayer.movementInput.moveStrafe;
                float yaw3 = mc.thePlayer.rotationYaw;
                if (forward3 == 0.0D && strafe3 == 0.0D) {
                } else if (forward3 != 0.0D) {
                    if (strafe3 >= 1.0D) {
                        yaw3 += forward3 > 0.0D ? -45.0F : 45.0F;
                        strafe3 = 0.0D;
                    } else if (strafe3 <= -1.0D) {
                        yaw3 += forward3 > 0.0D ? 45.0F : -45.0F;
                        strafe3 = 0.0D;
                    }
                    if (forward3 > 0.0D) {
                        forward3 = 1.0D;
                    } else if (forward3 < 0.0D) {
                        forward3 = -1.0D;
                    }
                }
                event.setX((forward3 * moveSpeedOG * -Math.sin(Math.toRadians(yaw3)) + strafe3 * moveSpeedOG * Math.cos(Math.toRadians(yaw3))) * 0.99479567D);
                event.setZ((forward3 * moveSpeedOG * Math.cos(Math.toRadians(yaw3)) - strafe3 * moveSpeedOG * -Math.sin(Math.toRadians(yaw3))) * 0.9946797684D);
                ++stageOG;
                break;
            case HYPIXEL:
                switch (stage) {
                    case 0:
                        ++stage;
                        lastDist = 0.0D;
                        break;
                    case 2:
                        lastDist = 0.0D;
                        double motionY = 0.4025;
                        if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
                            if (mc.thePlayer.isPotionActive(Potion.jump))
                                motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            event.setY(mc.thePlayer.motionY = motionY);
                            moveSpeed *= mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 2.1499D : 2.1499D;
                        }
                        break;
                    case 3:
                        // moveSpeed = lastDist - (0.709496) * (lastDist - getBaseMoveSpeed());
                        moveSpeed = lastDist - (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (mc.thePlayer.isPotionActive(Potion.jump) ? 0.54 : 0.655) : 0.7025) * (lastDist - getBaseMoveSpeed());
                        break;
                    default:
                        if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                            stage = mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F ? 0 : 1;
                        }
                        moveSpeed = lastDist - lastDist / (mc.thePlayer.isPotionActive(Potion.jump) ? 159D : 159D);
                        break;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0);
                    event.setZ(0);
                }
                if (forward != 0 && strafe != 0) {
                    forward = forward * Math.sin(Math.PI / 4);
                    strafe = strafe * Math.cos(Math.PI / 4);
                }
                event.setX((forward * moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99D);
                event.setZ((forward * moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99D);
                ++stage;
                break;
            case HypixelWtf:
                mc.timer.timerSpeed = 1f;
                switch (stage) {
                    case 0:
                        ++stage;
                        lastDist = 0.0D;
                        break;
                    case 2:
                        //mc.timer.timerSpeed = 2f;
                        double motionY = 0.4025;
                        if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
                            if (mc.thePlayer.isPotionActive(Potion.jump))
                                motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            event.setY(mc.thePlayer.motionY = motionY);
                            moveSpeed *= mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 2.1499D : 2.1499D;
                        }
                        break;
                    case 3:
                        moveSpeed = lastDist - (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.654 : 0.695) * (lastDist - getBaseMoveSpeed());
                        break;
                    default:
                        if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                            stage = mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F ? 0 : 1;
                        }
                        moveSpeed = lastDist - lastDist / 159D;
                        break;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0);
                    event.setZ(0);
                }
                if (forward != 0 && strafe != 0) {
                    forward = forward * Math.sin(Math.PI / 4);
                    strafe = strafe * Math.cos(Math.PI / 4);
                }
                event.setX((forward * moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99D);
                event.setZ((forward * moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99D);
                ++stage;
                break;
            case NCP:
                switch (stage) {
                    case 0:
                        ++stage;
                        lastDist = 0.0D;
                        break;
                    case 2:
                        double motionY = 0.4025;
                        if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
                            if (mc.thePlayer.isPotionActive(Potion.jump))
                                motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            event.setY(mc.thePlayer.motionY = motionY);
                            moveSpeed *= 2.149;
                        }
                        break;
                    case 3:
                        moveSpeed = lastDist - (0.747* (lastDist - getBaseMoveSpeed()));
                        break;
                    default:
                        if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                            stage = mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F ? 0 : 1;
                        }
                        moveSpeed = lastDist - lastDist / 159.0D;
                        break;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0);
                    event.setZ(0);
                }
                if (forward != 0 && strafe != 0) {
                    forward = forward * Math.sin(Math.PI / 4);
                    strafe = strafe * Math.cos(Math.PI / 4);
                }
                event.setX((forward * moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99D);
                event.setZ((forward * moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99D);
                ++stage;
                break;

            case Mineplex:
                double speed = 0;
                mc.timer.timerSpeed = 1f;
                stage++;
                if (mc.thePlayer.isCollidedHorizontally) {
                    stage = 50;
                }
                if (mc.thePlayer.onGround && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                    mc.timer.timerSpeed = 3f;
                    mc.thePlayer.jump();
                    event.setY(mc.thePlayer.motionY = 0.42);
                    stage = 0;
                    speed = 0;
                }
                if (!mc.thePlayer.onGround) {
                    if (mc.thePlayer.motionY > -0.38) {
                        mc.thePlayer.motionY += 0.023;
                    }
                    else {
                        mc.thePlayer.motionY += 0.01;
                    }
                    double slowdown = 0.006;
                    speed = (0.8-(stage*slowdown));
                    if (speed < 0) speed = 0;
                }
                setMoveSpeed(event, speed);
                break;

            case VANILLA:
                setMoveSpeed(event, boost.getValue());
                break;
            default:
                break;
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
        double baseSpeed = 0.272;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + (0.2 * amplifier);
        }
        return baseSpeed;
    }

    public enum Mode {
        HYPIXEL, HypixelWtf, Mineplex, NCP, ONGROUND, VANILLA, BHOP, AGC
    }
}