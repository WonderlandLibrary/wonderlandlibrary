package xyz.northclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class RotationUtil {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static float[] getAuraRotations(final Entity entity) {
        return getAuraRotations(entity, entity.height * 0.7D);
    }
    public static double radToDeg(double rad) {
        return rad * 180.0D / Math.PI;
    }

    public static float[] getAuraRotations(final Entity entity, final double heightOffset) {
        double x = entity.posX - mc.thePlayer.posX;
        double z = entity.posZ - mc.thePlayer.posZ;
        double distance = MathHelper.sqrt_double(Math.pow(x, 2) + Math.pow(z, 2));
        double y = (entity.posY + heightOffset) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        float yaw = (float) radToDeg(Math.atan2(z, x)) - 90.0F;
        float pitch = (float) -radToDeg(Math.atan2(y, distance));
        return new float[]{
                mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw),
                mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)
        };
    }
}
