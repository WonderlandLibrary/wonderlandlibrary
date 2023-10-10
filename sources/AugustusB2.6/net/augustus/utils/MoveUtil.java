// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import net.augustus.Augustus;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.augustus.utils.interfaces.MM;
import net.augustus.utils.interfaces.MC;

public class MoveUtil implements MC, MM
{
    public static void setMotion(final double speed, final double strafeDegree, final float yaw) {
        setMotion(speed, strafeDegree, yaw, false);
    }
    
    public static void setMotion(final double speed, final double strafeDegree, float yaw, final boolean ignoreSprint) {
        float strafe = MoveUtil.mc.thePlayer.moveStrafing;
        final float forward = MoveUtil.mc.thePlayer.moveForward;
        final float friction = (float)speed;
        if (strafe != 0.0f && forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw = (float)((forward > 0.0f) ? (yaw - strafeDegree) : (yaw + strafeDegree));
            }
            else {
                yaw = (float)((forward > 0.0f) ? (yaw + strafeDegree) : (yaw - strafeDegree));
            }
            strafe = 0.0f;
        }
        final float f1 = MathHelper.sin(yaw * 3.1415927f / 180.0f);
        final float f2 = MathHelper.cos(yaw * 3.1415927f / 180.0f);
        MoveUtil.mc.thePlayer.motionX = strafe * friction * f2 - forward * friction * f1;
        MoveUtil.mc.thePlayer.motionZ = forward * friction * f2 + strafe * friction * f1;
        if (MoveUtil.mc.thePlayer.isSprinting() && !ignoreSprint) {
            final float f3 = yaw * 0.017453292f;
            final EntityPlayerSP thePlayer = MoveUtil.mc.thePlayer;
            thePlayer.motionX -= MathHelper.sin(f3) * 0.2f;
            final EntityPlayerSP thePlayer2 = MoveUtil.mc.thePlayer;
            thePlayer2.motionZ += MathHelper.cos(f3) * 0.2f;
        }
    }
    
    public static double[] getMotion(final double speed, final float strafe, final float forward, final float yaw) {
        final float friction = (float)speed;
        final float f1 = MathHelper.sin(yaw * 3.1415927f / 180.0f);
        final float f2 = MathHelper.cos(yaw * 3.1415927f / 180.0f);
        final double motionX = strafe * friction * f2 - forward * friction * f1;
        final double motionZ = forward * friction * f2 + strafe * friction * f1;
        return new double[] { motionX, motionZ };
    }
    
    public static void setMotion2(final double speed, final double strafeDegree, float yaw) {
        float strafe = MoveUtil.mc.thePlayer.moveStrafing;
        final float forward = MoveUtil.mc.thePlayer.moveForward;
        final float friction = (float)speed;
        if (strafe != 0.0f && forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw = (float)((forward > 0.0f) ? (yaw - strafeDegree) : (yaw + strafeDegree));
            }
            else {
                yaw = (float)((forward > 0.0f) ? (yaw + strafeDegree) : (yaw - strafeDegree));
            }
            strafe = 0.0f;
        }
        final float f1 = MathHelper.sin(yaw * 3.1415927f / 180.0f);
        final float f2 = MathHelper.cos(yaw * 3.1415927f / 180.0f);
        MoveUtil.mc.thePlayer.motionX = strafe * friction * f2 - forward * friction * f1;
        MoveUtil.mc.thePlayer.motionZ = forward * friction * f2 + strafe * friction * f1;
    }
    
    public static double[] addMotion(final double speed, final float yaw) {
        float strafe = 0.0f;
        float forward = MoveUtil.mc.thePlayer.moveForward;
        final float friction = (float)speed;
        float f = strafe * strafe + forward * forward;
        if (f >= 1.0E-4f) {
            f = MathHelper.sqrt_float(f);
            if (f < 1.0f) {
                f = 1.0f;
            }
            f = friction / f;
            strafe *= f;
            forward *= f;
            final float f2 = MathHelper.sin(yaw * 3.1415927f / 180.0f);
            final float f3 = MathHelper.cos(yaw * 3.1415927f / 180.0f);
            final double[] d = { strafe * f3 - forward * f2, forward * f3 + strafe * f2 };
            return d;
        }
        return new double[] { 0.0, 0.0 };
    }
    
    public static boolean isMoving() {
        return MoveUtil.mc.thePlayer.moveForward != 0.0f || (MoveUtil.mc.thePlayer.moveStrafing != 0.0f && !MoveUtil.mc.thePlayer.isCollidedHorizontally);
    }
    
    public static void setSpeed(final float f2) {
        MoveUtil.mc.thePlayer.motionX = -(Math.sin(direction()) * f2);
        MoveUtil.mc.thePlayer.motionZ = Math.cos(direction()) * f2;
    }
    
    public static void setSpeed(final float f2, final boolean strafe) {
        final double d = Math.toRadians(getYaw(strafe));
        MoveUtil.mc.thePlayer.motionX = -(Math.sin(d) * f2);
        MoveUtil.mc.thePlayer.motionZ = Math.cos(d) * f2;
    }
    
    private static void strafe(final double d) {
        if (isMoving()) {
            final double direction = direction();
            MoveUtil.mc.thePlayer.motionX = -Math.sin(direction) * d;
            MoveUtil.mc.thePlayer.motionZ = Math.cos(direction) * d;
        }
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static void strafeMatrix() {
        final double speed = getSpeed();
        if (isMoving()) {
            float f = getYaw();
            if (MoveUtil.mc.thePlayer.moveForward < 0.0f) {
                f += 180.0f;
            }
            else {
                float f2 = 1.0f;
                if (MoveUtil.mc.thePlayer.moveForward < 0.0f) {
                    f2 = -0.5f;
                }
                else if (MoveUtil.mc.thePlayer.moveForward > 0.0f) {
                    f2 = 0.5f;
                }
                if (MoveUtil.mc.thePlayer.moveStrafing > 0.0f) {
                    f -= 90.0f * f2;
                }
                if (MoveUtil.mc.thePlayer.moveStrafing < 0.0f) {
                    f += 90.0f * f2;
                }
            }
            final double direction = Math.toRadians(f);
            MoveUtil.mc.thePlayer.motionX = -Math.sin(direction) * speed;
            MoveUtil.mc.thePlayer.motionZ = Math.cos(direction) * speed;
        }
    }
    
    public static double getSpeed() {
        return Math.sqrt(MoveUtil.mc.thePlayer.motionX * MoveUtil.mc.thePlayer.motionX + MoveUtil.mc.thePlayer.motionZ * MoveUtil.mc.thePlayer.motionZ);
    }
    
    public static double direction() {
        float f = getYaw();
        if (MoveUtil.mc.thePlayer.moveForward < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (MoveUtil.mc.thePlayer.moveForward < 0.0f) {
            f2 = -0.5f;
        }
        else if (MoveUtil.mc.thePlayer.moveForward > 0.0f) {
            f2 = 0.5f;
        }
        if (MoveUtil.mc.thePlayer.moveStrafing > 0.0f) {
            f -= 90.0f * f2;
        }
        if (MoveUtil.mc.thePlayer.moveStrafing < 0.0f) {
            f += 90.0f * f2;
        }
        return Math.toRadians(f);
    }
    
    public static void addSpeed(final double speed, final boolean strafe) {
        final float f = getYaw(strafe) * 0.017453292f;
        final EntityPlayerSP thePlayer = MoveUtil.mc.thePlayer;
        thePlayer.motionX -= MathHelper.sin(f) * speed;
        final EntityPlayerSP thePlayer2 = MoveUtil.mc.thePlayer;
        thePlayer2.motionZ += MathHelper.cos(f) * speed;
    }
    
    public static float getYaw() {
        return (MoveUtil.mm.targetStrafe.target != null && MoveUtil.mm.targetStrafe.isToggled()) ? MoveUtil.mm.targetStrafe.moveYaw : ((MoveUtil.mm.killAura.isToggled() && MoveUtil.mm.killAura.target != null && MoveUtil.mm.killAura.moveFix.getBoolean()) ? MoveUtil.mc.thePlayer.rotationYaw : Augustus.getInstance().getYawPitchHelper().realYaw);
    }
    
    public static float getYaw(final boolean strafe) {
        if (strafe) {
            return (float)Math.toDegrees(direction());
        }
        return getYaw();
    }
    
    public static void setSpeed2(final double speed) {
        final EntityPlayerSP player = MoveUtil.mc.thePlayer;
        double yaw = getYaw();
        final boolean isMoving = player.moveForward != 0.0f || player.moveStrafing != 0.0f;
        final boolean isMovingForward = player.moveForward > 0.0f;
        final boolean isMovingBackward = player.moveForward < 0.0f;
        final boolean isMovingRight = player.moveStrafing > 0.0f;
        final boolean isMovingLeft = player.moveStrafing < 0.0f;
        final boolean isMovingSideways = isMovingLeft || isMovingRight;
        final boolean isMovingStraight = isMovingForward || isMovingBackward;
        if (isMoving) {
            if (isMovingForward && !isMovingSideways) {
                yaw += 0.0;
            }
            else if (isMovingBackward && !isMovingSideways) {
                yaw += 180.0;
            }
            else if (isMovingForward && isMovingLeft) {
                yaw += 45.0;
            }
            else if (isMovingForward) {
                yaw -= 45.0;
            }
            else if (!isMovingStraight && isMovingLeft) {
                yaw += 90.0;
            }
            else if (!isMovingStraight && isMovingRight) {
                yaw -= 90.0;
            }
            else if (isMovingBackward && isMovingLeft) {
                yaw += 135.0;
            }
            else if (isMovingBackward) {
                yaw -= 135.0;
            }
            yaw = Math.toRadians(yaw);
            player.motionX = -Math.sin(yaw) * speed;
            player.motionZ = Math.cos(yaw) * speed;
        }
    }
}
