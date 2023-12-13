/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class MovementUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void teleportUp(float d2) {
        MovementUtil.mc.thePlayer.setPosition(MovementUtil.mc.thePlayer.posX, MovementUtil.mc.thePlayer.posY + (double)d2, MovementUtil.mc.thePlayer.posZ);
    }

    public static void teleportForward(float d2) {
        double playerYaw = Math.toRadians(MovementUtil.mc.thePlayer.rotationYaw);
        MovementUtil.mc.thePlayer.setPosition(MovementUtil.mc.thePlayer.posX + (double)d2 * -Math.sin(playerYaw), MovementUtil.mc.thePlayer.posY, MovementUtil.mc.thePlayer.posZ + (double)d2 * Math.cos(playerYaw));
    }

    public static void teleportForwardPacket(float d2) {
        double playerYaw = Math.toRadians(MovementUtil.mc.thePlayer.rotationYaw);
        MovementUtil.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtil.mc.thePlayer.posX + (double)d2 * -Math.sin(playerYaw), MovementUtil.mc.thePlayer.posY, MovementUtil.mc.thePlayer.posZ + (double)d2 * Math.cos(playerYaw), true));
    }

    public static void teleportUpPacket(float d2) {
        MovementUtil.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtil.mc.thePlayer.posX, MovementUtil.mc.thePlayer.posY + (double)d2, MovementUtil.mc.thePlayer.posZ, true));
    }

    public static float getSpeed() {
        return (float)Math.sqrt(MovementUtil.mc.thePlayer.motionX * MovementUtil.mc.thePlayer.motionX + MovementUtil.mc.thePlayer.motionZ * MovementUtil.mc.thePlayer.motionZ);
    }

    public static boolean isMoving() {
        if (MovementUtil.mc.thePlayer == null || MovementUtil.mc.thePlayer.movementInput == null) {
            return false;
        }
        return MovementUtil.mc.thePlayer != null && (MovementUtil.mc.thePlayer.movementInput.moveForward != 0.0f || MovementUtil.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }

    public static void strafe(Double speed) {
        if (!MovementUtil.isMoving()) {
            return;
        }
        float yaw = (float)MovementUtil.getDirection();
        EntityPlayerSP thePlayer = MovementUtil.mc.thePlayer;
        thePlayer.motionX = -Math.sin(yaw) * speed;
        thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static double getDirection() {
        EntityPlayerSP thePlayer = MovementUtil.mc.thePlayer;
        Float rotationYaw = Float.valueOf(thePlayer.rotationYaw);
        if (thePlayer.moveForward < 0.0f) {
            rotationYaw = Float.valueOf(rotationYaw.floatValue() + 180.0f);
        }
        Float forward = Float.valueOf(1.0f);
        if (thePlayer.moveForward < 0.0f) {
            forward = Float.valueOf(-0.5f);
        } else if (thePlayer.moveForward > 0.0f) {
            forward = Float.valueOf(0.5f);
        }
        if (thePlayer.moveStrafing > 0.0f) {
            rotationYaw = Float.valueOf(rotationYaw.floatValue() - 90.0f * forward.floatValue());
        }
        if (thePlayer.moveStrafing < 0.0f) {
            rotationYaw = Float.valueOf(rotationYaw.floatValue() + 90.0f * forward.floatValue());
        }
        return Math.toRadians(rotationYaw.floatValue());
    }
}

