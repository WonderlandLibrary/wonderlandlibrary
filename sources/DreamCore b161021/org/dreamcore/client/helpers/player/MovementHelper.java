package org.dreamcore.client.helpers.player;

import net.minecraft.init.MobEffects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import org.dreamcore.client.event.events.impl.motion.EventMove;
import org.dreamcore.client.event.events.impl.motion.EventStrafe;
import org.dreamcore.client.helpers.Helper;

public class MovementHelper implements Helper {

    public static boolean isMoving() {
        return mc.player.movementInput.moveForward != 0 || mc.player.movementInput.moveStrafe != 0;
    }

    public static float getBaseMoveSpeed() {
        float baseSpeed = 0.2873F;
        if (mc.player != null && mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static int getSpeedEffect() {
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            return mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier() + 1;
        } else {
            return 0;
        }
    }

    public static float getMoveDirection() {
        double motionX = mc.player.motionX;
        double motionZ = mc.player.motionZ;
        float direction = (float) (Math.atan2(motionX, motionZ) / Math.PI * 180);
        return -direction;
    }

    public static boolean isBlockAboveHead() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + (!mc.player.onGround ? 1.5 : 2.5), mc.player.posZ - 0.3);
        return mc.world.getCollisionBoxes(mc.player, axisAlignedBB).isEmpty();
    }

    public static void calculateSilentMove(EventStrafe event, float yaw) {
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        int difference = (int) ((MathHelper.wrapDegrees(mc.player.rotationYaw - yaw - 23.5F - 135) + 180) / 45);
        float calcForward = 0F;
        float calcStrafe = 0F;
        switch (difference) {
            case 0:
                calcForward = forward;
                calcStrafe = strafe;
                break;
            case 1:
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            case 2:
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            case 3:
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            case 4:
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            case 5:
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            case 6:
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            case 7:
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
        }
        if (calcForward > 1F || calcForward < 0.9F && calcForward > 0.3F || calcForward < -1F || calcForward > -0.9F && calcForward < -0.3F) {
            calcForward *= 0.5F;
        }
        if (calcStrafe > 1F || calcStrafe < 0.9F && calcStrafe > 0.3F || calcStrafe < -1F || calcStrafe > -0.9F && calcStrafe < -0.3F) {
            calcStrafe *= 0.5F;
        }
        float dist = calcStrafe * calcStrafe + calcForward * calcForward;
        if (dist >= 1E-4F) {
            dist = (float) (friction / Math.max(1, Math.sqrt(dist)));
            calcStrafe *= dist;
            calcForward *= dist;
            float yawSin = MathHelper.sin((float) (yaw * Math.PI / 180F));
            float yawCos = MathHelper.cos((float) (yaw * Math.PI / 180F));
            mc.player.motionX += calcStrafe * yawCos - calcForward * yawSin;
            mc.player.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }

    public static void setEventSpeed(EventMove event, double speed) {
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0 && strafe == 0) {
            event.setX(0);
            event.setZ(0);
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }

    public static float getSpeed() {
        return (float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }

    public static void setSpeed(float speed) {
        float yaw = mc.player.rotationYaw;
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        if (forward != 0) {
            if (strafe > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (strafe < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            strafe = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }
        mc.player.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90)));
        mc.player.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90)));
    }

    public static double getDirectionAll() {
        float rotationYaw = mc.player.rotationYaw;
        float forward = 1f;
        if (mc.player.moveForward < 0f)
            rotationYaw += 180f;
        if (mc.player.moveForward < 0f)
            forward = -0.5f;
        else if (mc.player.moveForward > 0f)
            forward = 0.5f;
        if (mc.player.moveStrafing > 0f)
            rotationYaw -= 90f * forward;
        if (mc.player.moveStrafing < 0f)
            rotationYaw += 90f * forward;
        return Math.toRadians(rotationYaw);
    }

    public static void strafePlayer(float speed) {
        double yaw = getDirectionAll();
        float getSpeed = speed == 0 ? MovementHelper.getSpeed() : speed;
        mc.player.motionX = -Math.sin(yaw) * (getSpeed);
        mc.player.motionZ = Math.cos(yaw) * (getSpeed);
    }

}
