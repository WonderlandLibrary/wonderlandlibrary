// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.movement;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import ru.tuskevich.event.events.impl.EventPreMove;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.Utility;

public class MoveUtility implements Utility
{
    public static float getPlayerDirection() {
        final Minecraft mc = MoveUtility.mc;
        float rotationYaw = Minecraft.player.rotationYaw;
        final Minecraft mc2 = MoveUtility.mc;
        if (Minecraft.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        final Minecraft mc3 = MoveUtility.mc;
        if (Minecraft.player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else {
            final Minecraft mc4 = MoveUtility.mc;
            if (Minecraft.player.moveForward > 0.0f) {
                forward = 0.5f;
            }
        }
        final Minecraft mc5 = MoveUtility.mc;
        if (Minecraft.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        final Minecraft mc6 = MoveUtility.mc;
        if (Minecraft.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return (float)Math.toRadians(rotationYaw);
    }
    
    public static float getEntityDirection(final EntityLivingBase entity) {
        float rotationYaw = entity.rotationYaw;
        if (entity.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (entity.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (entity.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (entity.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (entity.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return (float)Math.toRadians(rotationYaw);
    }
    
    public static void setMotion(final double motion) {
        final Minecraft mc = MoveUtility.mc;
        double forward = Minecraft.player.movementInput.moveForward;
        final Minecraft mc2 = MoveUtility.mc;
        double strafe = Minecraft.player.movementInput.moveStrafe;
        final Minecraft mc3 = MoveUtility.mc;
        float yaw = Minecraft.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            final Minecraft mc4 = MoveUtility.mc;
            Minecraft.player.motionX = 0.0;
            final Minecraft mc5 = MoveUtility.mc;
            Minecraft.player.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double sin = MathHelper.sin((float)Math.toRadians(yaw + 90.0f));
            final double cos = MathHelper.cos((float)Math.toRadians(yaw + 90.0f));
            final Minecraft mc6 = MoveUtility.mc;
            Minecraft.player.motionX = forward * motion * cos + strafe * motion * sin;
            final Minecraft mc7 = MoveUtility.mc;
            Minecraft.player.motionZ = forward * motion * sin - strafe * motion * cos;
        }
    }
    
    public static double getPlayerMotion() {
        final Minecraft mc = MoveUtility.mc;
        final double motionX = Minecraft.player.motionX;
        final Minecraft mc2 = MoveUtility.mc;
        return Math.hypot(motionX, Minecraft.player.motionZ);
    }
    
    public static double getBaseSpeed() {
        double baseSpeed = 0.2873;
        final Minecraft mc = MoveUtility.mc;
        if (Minecraft.player.isPotionActive(Potion.getPotionById(1))) {
            final Minecraft mc2 = MoveUtility.mc;
            final int amplifier = Minecraft.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static void setEventSpeed(final EventPreMove move, final float speed) {
        final Minecraft mc = MoveUtility.mc;
        float yaw = Minecraft.player.rotationYaw;
        final Minecraft mc2 = MoveUtility.mc;
        float forward = Minecraft.player.movementInput.moveForward;
        final Minecraft mc3 = MoveUtility.mc;
        float strafe = Minecraft.player.movementInput.moveStrafe;
        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (strafe < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            strafe = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        move.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
        move.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
    }
    
    public static int getSpeedEffect() {
        final Minecraft mc = MoveUtility.mc;
        if (Minecraft.player.isPotionActive(MobEffects.SPEED)) {
            final Minecraft mc2 = MoveUtility.mc;
            return Objects.requireNonNull(Minecraft.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static void setStrafe(final double motion) {
        if (!isMoving()) {
            return;
        }
        final double radians = getDirection();
        final Minecraft mc = MoveUtility.mc;
        Minecraft.player.motionX = -Math.sin(radians) * motion;
        final Minecraft mc2 = MoveUtility.mc;
        Minecraft.player.motionZ = Math.cos(radians) * motion;
    }
    
    public static boolean isMoving() {
        final Minecraft mc = MoveUtility.mc;
        if (Minecraft.player.movementInput.moveStrafe == 0.0) {
            final Minecraft mc2 = MoveUtility.mc;
            if (Minecraft.player.movementInput.moveForward == 0.0) {
                return false;
            }
        }
        return true;
    }
    
    public static float getDirection() {
        final Minecraft mc = MoveUtility.mc;
        float rotationYaw = Minecraft.player.rotationYaw;
        float strafeFactor = 0.0f;
        final Minecraft mc2 = MoveUtility.mc;
        if (Minecraft.player.movementInput.moveForward > 0.0f) {
            strafeFactor = 1.0f;
        }
        final Minecraft mc3 = MoveUtility.mc;
        if (Minecraft.player.movementInput.moveForward < 0.0f) {
            strafeFactor = -1.0f;
        }
        if (strafeFactor == 0.0f) {
            final Minecraft mc4 = MoveUtility.mc;
            if (Minecraft.player.movementInput.moveStrafe > 0.0f) {
                rotationYaw -= 90.0f;
            }
            final Minecraft mc5 = MoveUtility.mc;
            if (Minecraft.player.movementInput.moveStrafe < 0.0f) {
                rotationYaw += 90.0f;
            }
        }
        else {
            final Minecraft mc6 = MoveUtility.mc;
            if (Minecraft.player.movementInput.moveStrafe > 0.0f) {
                rotationYaw -= 45.0f * strafeFactor;
            }
            final Minecraft mc7 = MoveUtility.mc;
            if (Minecraft.player.movementInput.moveStrafe < 0.0f) {
                rotationYaw += 45.0f * strafeFactor;
            }
        }
        if (strafeFactor < 0.0f) {
            rotationYaw -= 180.0f;
        }
        return (float)Math.toRadians(rotationYaw);
    }
    
    public static boolean isBlockAboveHead() {
        final Minecraft mc = MoveUtility.mc;
        final double x1 = Minecraft.player.posX - 0.3;
        final Minecraft mc2 = MoveUtility.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc3 = MoveUtility.mc;
        final double y1 = posY + Minecraft.player.getEyeHeight();
        final Minecraft mc4 = MoveUtility.mc;
        final double z1 = Minecraft.player.posZ + 0.3;
        final Minecraft mc5 = MoveUtility.mc;
        final double x2 = Minecraft.player.posX + 0.3;
        final Minecraft mc6 = MoveUtility.mc;
        final double posY2 = Minecraft.player.posY;
        final Minecraft mc7 = MoveUtility.mc;
        final double y2 = posY2 + (Minecraft.player.onGround ? 2.5 : 1.5);
        final Minecraft mc8 = MoveUtility.mc;
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x1, y1, z1, x2, y2, Minecraft.player.posZ - 0.3);
        final WorldClient world = MoveUtility.mc.world;
        final Minecraft mc9 = MoveUtility.mc;
        return !world.getCollisionBoxes(Minecraft.player, axisAlignedBB).isEmpty();
    }
    
    public static float getSpeed() {
        final Minecraft mc = MoveUtility.mc;
        final double motionX = Minecraft.player.motionX;
        final Minecraft mc2 = MoveUtility.mc;
        final double n = motionX * Minecraft.player.motionX;
        final Minecraft mc3 = MoveUtility.mc;
        final double motionZ = Minecraft.player.motionZ;
        final Minecraft mc4 = MoveUtility.mc;
        return (float)Math.sqrt(n + motionZ * Minecraft.player.motionZ);
    }
    
    public static boolean isInGround() {
        final Minecraft mc = MoveUtility.mc;
        return Minecraft.player.onGround && !MoveUtility.mc.gameSettings.keyBindJump.pressed;
    }
    
    public static boolean isInLiquid() {
        final Minecraft mc = MoveUtility.mc;
        if (!Minecraft.player.isInWater()) {
            final Minecraft mc2 = MoveUtility.mc;
            if (!Minecraft.player.isInLava()) {
                return false;
            }
        }
        return true;
    }
}
