package wtf.evolution.helpers;

import net.minecraft.client.Minecraft;

public class MovementUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void setSpeed(float speed) {
        if (mc.player.moveForward > 0) {
            mc.player.motionX = -Math.sin(Math.toRadians(mc.player.rotationYaw)) * speed;
            mc.player.motionZ = Math.cos(Math.toRadians(mc.player.rotationYaw)) * speed;
        }
        if (mc.player.moveForward < 0) {
            mc.player.motionX = Math.sin(Math.toRadians(mc.player.rotationYaw)) * speed;
            mc.player.motionZ = -Math.cos(Math.toRadians(mc.player.rotationYaw)) * speed;
        }
        if (mc.player.moveStrafing > 0) {
            mc.player.motionX = Math.cos(Math.toRadians(mc.player.rotationYaw)) * speed;
            mc.player.motionZ = Math.sin(Math.toRadians(mc.player.rotationYaw)) * speed;
        }
        if (mc.player.moveStrafing < 0) {
            mc.player.motionX = -Math.cos(Math.toRadians(mc.player.rotationYaw)) * speed;
            mc.player.motionZ = -Math.sin(Math.toRadians(mc.player.rotationYaw)) * speed;
        }
        if (mc.player.moveStrafing > 0 && mc.player.moveForward > 0) {
            mc.player.motionX = Math.cos(Math.toRadians(mc.player.rotationYaw + 45)) * speed;
            mc.player.motionZ = Math.sin(Math.toRadians(mc.player.rotationYaw + 45)) * speed;
        }
        if (mc.player.moveStrafing < 0 && mc.player.moveForward > 0) {
            mc.player.motionX = -Math.cos(Math.toRadians(mc.player.rotationYaw - 45)) * speed;
            mc.player.motionZ = -Math.sin(Math.toRadians(mc.player.rotationYaw - 45)) * speed;
        }
        if (mc.player.moveStrafing > 0 && mc.player.moveForward < 0) {
            mc.player.motionX = -Math.cos(Math.toRadians(mc.player.rotationYaw + 135)) * speed;
            mc.player.motionZ = -Math.sin(Math.toRadians(mc.player.rotationYaw + 135)) * speed;
        }
        if (mc.player.moveStrafing < 0 && mc.player.moveForward < 0) {
            mc.player.motionX = Math.cos(Math.toRadians(mc.player.rotationYaw - 135)) * speed;
            mc.player.motionZ = Math.sin(Math.toRadians(mc.player.rotationYaw - 135)) * speed;
        }
    }

    public static double getPlayerMotion() {
        return Math.hypot(mc.player.motionX, mc.player.motionZ);
    }

    public static boolean isMoving() {
        return mc.player.moveForward != 0 || mc.player.moveStrafing != 0;
    }

}
