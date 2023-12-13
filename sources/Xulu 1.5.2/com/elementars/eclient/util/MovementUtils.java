// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public final class MovementUtils implements Helper
{
    public static float getSpeed() {
        return (float)Math.sqrt(Wrapper.getMinecraft().player.motionX * Wrapper.getMinecraft().player.motionX + Wrapper.getMinecraft().player.motionZ * Wrapper.getMinecraft().player.motionZ);
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static boolean isMoving() {
        return Wrapper.getMinecraft().player != null && (Wrapper.getMinecraft().player.movementInput.moveForward != 0.0f || Wrapper.getMinecraft().player.movementInput.moveStrafe != 0.0f);
    }
    
    public static boolean hasMotion() {
        return Wrapper.getMinecraft().player.motionX != 0.0 && Wrapper.getMinecraft().player.motionZ != 0.0 && Wrapper.getMinecraft().player.motionY != 0.0;
    }
    
    public static void strafe(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        Wrapper.getMinecraft().player.motionX = -Math.sin(yaw) * speed;
        Wrapper.getMinecraft().player.motionZ = Math.cos(yaw) * speed;
    }
    
    public static void forward(final double length) {
        final double yaw = Math.toRadians(Wrapper.getMinecraft().player.rotationYaw);
        Wrapper.getMinecraft().player.setPosition(Wrapper.getMinecraft().player.posX + -Math.sin(yaw) * length, Wrapper.getMinecraft().player.posY, Wrapper.getMinecraft().player.posZ + Math.cos(yaw) * length);
    }
    
    public static double[] forward2(final double speed) {
        float forward = MovementUtils.mc.player.movementInput.moveForward;
        float side = MovementUtils.mc.player.movementInput.moveStrafe;
        float yaw = MovementUtils.mc.player.prevRotationYaw + (MovementUtils.mc.player.rotationYaw - MovementUtils.mc.player.prevRotationYaw) * MovementUtils.mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public static double getDirection() {
        float rotationYaw = Wrapper.getMinecraft().player.rotationYaw;
        if (Wrapper.getMinecraft().player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (Wrapper.getMinecraft().player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Wrapper.getMinecraft().player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Wrapper.getMinecraft().player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (Wrapper.getMinecraft().player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (MovementUtils.mc.player != null && MovementUtils.mc.player.isPotionActive(Potion.getPotionById(1))) {
            final int amplifier = MovementUtils.mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static double getDirectionStrafe() {
        return Math.toRadians(Minecraft.getMinecraft().player.rotationYaw);
    }
    
    public static double getSpeedStrafe() {
        return Math.sqrt(Math.pow(Minecraft.getMinecraft().player.motionX, 2.0) + Math.pow(Minecraft.getMinecraft().player.motionX, 2.0));
    }
    
    public static void setSpeedStrafe(final double speed) {
        Minecraft.getMinecraft().player.motionX = -Math.sin(getDirection()) * speed;
        Minecraft.getMinecraft().player.motionZ = Math.cos(getDirection()) * speed;
    }
}
