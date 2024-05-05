package slavikcodd3r.rainbow.utils;

import net.minecraft.client.Minecraft;

public class MoveUtils3
{
    public static double getPosForSetPosX(final double value) {
        final double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
        final double x = -Math.sin(yaw) * value;
        return x;
    }
    
    public static double getPosForSetPosZ(final double value) {
        final double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
        final double z = Math.cos(yaw) * value;
        return z;
    }
}
