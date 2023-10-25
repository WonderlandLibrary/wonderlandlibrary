package intent.AquaDev.aqua.utils;

import net.minecraft.client.Minecraft;

public class PlayerUtil {
    public static double getSpeed() {
        return Math.sqrt((double)(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX + Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ));
    }

    public static void setSpeed(double speed) {
        boolean isMovingStraight;
        Minecraft mc = Minecraft.getMinecraft();
        double yaw = mc.thePlayer.rotationYaw;
        boolean isMoving = mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
        boolean isMovingForward = mc.thePlayer.moveForward > 0.0f;
        boolean isMovingBackward = mc.thePlayer.moveForward < 0.0f;
        boolean isMovingRight = mc.thePlayer.moveStrafing > 0.0f;
        boolean isMovingLeft = mc.thePlayer.moveStrafing < 0.0f;
        boolean isMovingSideways = isMovingLeft || isMovingRight;
        boolean bl = isMovingStraight = isMovingForward || isMovingBackward;
        if (isMoving) {
            if (isMovingForward && !isMovingSideways) {
                yaw += 0.0;
            } else if (isMovingBackward && !isMovingSideways) {
                yaw += 180.0;
            } else if (isMovingForward && isMovingLeft) {
                yaw += 45.0;
            } else if (isMovingForward) {
                yaw -= 45.0;
            } else if (!isMovingStraight && isMovingLeft) {
                yaw += 90.0;
            } else if (!isMovingStraight && isMovingRight) {
                yaw -= 90.0;
            } else if (isMovingBackward && isMovingLeft) {
                yaw += 135.0;
            } else if (isMovingBackward) {
                yaw -= 135.0;
            }
            yaw = Math.toRadians((double)yaw);
            mc.thePlayer.motionX = -Math.sin((double)yaw) * speed;
            mc.thePlayer.motionZ = Math.cos((double)yaw) * speed;
        }
    }
}
