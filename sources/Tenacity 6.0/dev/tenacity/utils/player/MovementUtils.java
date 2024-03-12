// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.player;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.potion.Potion;
import dev.tenacity.event.impl.player.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import dev.tenacity.event.impl.player.PlayerMoveUpdateEvent;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector2f;
import dev.tenacity.utils.Utils;

public class MovementUtils implements Utils
{
    public static boolean isMoving() {
        return MovementUtils.mc.thePlayer != null && (MovementUtils.mc.thePlayer.movementInput.moveForward != 0.0f || MovementUtils.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static float getMoveYaw(float yaw) {
        final Vector2f from = new Vector2f((float)MovementUtils.mc.thePlayer.lastTickPosX, (float)MovementUtils.mc.thePlayer.lastTickPosZ);
        final Vector2f to = new Vector2f((float)MovementUtils.mc.thePlayer.posX, (float)MovementUtils.mc.thePlayer.posZ);
        final Vector2f diff = new Vector2f(to.x - from.x, to.y - from.y);
        final double x = diff.x;
        final double z = diff.y;
        if (x != 0.0 && z != 0.0) {
            yaw = (float)Math.toDegrees((Math.atan2(-x, z) + MathHelper.PI2) % MathHelper.PI2);
        }
        return yaw;
    }
    
    public static void setSpeed(final double moveSpeed, float yaw, double strafe, double forward) {
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
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        MovementUtils.mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        MovementUtils.mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
    
    public static void setSpeedHypixel(final PlayerMoveUpdateEvent event, final float moveSpeed, final float strafeMotion) {
        final float remainder = 1.0f - strafeMotion;
        if (MovementUtils.mc.thePlayer.onGround) {
            setSpeed(moveSpeed);
        }
        else {
            final EntityPlayerSP thePlayer = MovementUtils.mc.thePlayer;
            thePlayer.motionX *= strafeMotion;
            final EntityPlayerSP thePlayer2 = MovementUtils.mc.thePlayer;
            thePlayer2.motionZ *= strafeMotion;
            event.setFriction(moveSpeed * remainder);
        }
    }
    
    public static void setSpeed(final double moveSpeed) {
        setSpeed(moveSpeed, MovementUtils.mc.thePlayer.rotationYaw, MovementUtils.mc.thePlayer.movementInput.moveStrafe, MovementUtils.mc.thePlayer.movementInput.moveForward);
    }
    
    public static void setSpeed(final MoveEvent moveEvent, final double moveSpeed, float yaw, double strafe, double forward) {
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
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }
    
    public static void setSpeed(final MoveEvent moveEvent, final double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, MovementUtils.mc.thePlayer.rotationYaw, MovementUtils.mc.thePlayer.movementInput.moveStrafe, MovementUtils.mc.thePlayer.movementInput.moveForward);
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = MovementUtils.mc.thePlayer.capabilities.getWalkSpeed() * 2.873;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
            baseSpeed /= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
        }
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static void sendFlyingCapabilities(final boolean isFlying, final boolean allowFlying) {
        final PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.isFlying = isFlying;
        playerCapabilities.allowFlying = allowFlying;
        PacketUtils.sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
    }
    
    public static double getBaseMoveSpeed2() {
        double baseSpeed = MovementUtils.mc.thePlayer.capabilities.getWalkSpeed() * (MovementUtils.mc.thePlayer.isSprinting() ? 2.873 : 2.215);
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
            baseSpeed /= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
        }
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static double getBaseMoveSpeedStupid() {
        double sped = 0.2873;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            sped *= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return sped;
    }
    
    public static boolean isOnGround(final double height) {
        return !MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static float getSpeed() {
        if (MovementUtils.mc.thePlayer == null || MovementUtils.mc.theWorld == null) {
            return 0.0f;
        }
        return (float)Math.sqrt(MovementUtils.mc.thePlayer.motionX * MovementUtils.mc.thePlayer.motionX + MovementUtils.mc.thePlayer.motionZ * MovementUtils.mc.thePlayer.motionZ);
    }
    
    public static float getMaxFallDist() {
        return (float)(MovementUtils.mc.thePlayer.getMaxFallHeight() + (MovementUtils.mc.thePlayer.isPotionActive(Potion.jump) ? (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) : 0));
    }
}
