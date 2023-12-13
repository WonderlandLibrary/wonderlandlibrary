package koks.api.util;

import net.minecraft.client.Minecraft;

/**
 * @author deleteboys | lmao | kroko
 * @created on 15.09.2020 : 22:12
 */
public class MovementUtil {

    private Minecraft mc = Minecraft.getMinecraft();

    public float getDirection(float rotationYaw) {
        float left = Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed ? mc.gameSettings.keyBindBack.pressed ? 45 : mc.gameSettings.keyBindForward.pressed ? -45 : -90 : 0;
        float right = Minecraft.getMinecraft().gameSettings.keyBindRight.pressed ? mc.gameSettings.keyBindBack.pressed ? -45 : mc.gameSettings.keyBindForward.pressed ? 45 : 90 : 0;
        float back = Minecraft.getMinecraft().gameSettings.keyBindBack.pressed ? + 180 : 0;
        float yaw = left + right + back;
        return rotationYaw + yaw;
    }

    public boolean isMoving() {
        return mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed;
    }

    public void setSpeed(double speed, boolean movingCheck) {
        if (isMoving() || !movingCheck) {
            Minecraft.getMinecraft().thePlayer.motionX = -Math.sin(Math.toRadians(getDirection(mc.thePlayer.rotationYaw))) * speed;
            Minecraft.getMinecraft().thePlayer.motionZ = Math.cos(Math.toRadians(getDirection(mc.thePlayer.rotationYaw))) * speed;
        }
    }
}