package dev.tenacity.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public final class RotationUtil {

    private static final Minecraft MC = Minecraft.getMinecraft();

    private RotationUtil() {
    }

    public static float getSensitivityMultiplier() {
        final float sensitivity = MC.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return (sensitivity * sensitivity * sensitivity * 8.0F) * 0.15F;
    }

    public static float[] getRotationFromTarget(final Entity entity) {
        if (entity == null) {
            return null;
        }
        Minecraft mc = Minecraft.getMinecraft();
        final double xSize = entity.posX - mc.thePlayer.posX;
        final double ySize = entity.posY + entity.getEyeHeight() / 2 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()/2);
        final double zSize = entity.posZ - mc.thePlayer.posZ;
        final double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        final float yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90;
        final float pitch = (float) (-(Math.atan2(ySize, theta) * 180 / Math.PI));
        return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f};
//        final double xSize = entity.posX - MC.thePlayer.posX;
//        final double ySize = entity.posY + entity.getEyeHeight() / 2 - (MC.thePlayer.posY + MC.thePlayer.getEyeHeight());
//        final double zSize = entity.posZ - MC.thePlayer.posZ;
//        final double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
//
//        final float yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90;
//        final float pitch = (float) (-(Math.atan2(ySize, theta) * 180 / Math.PI));
//        return new float[]{(MC.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - MC.thePlayer.rotationYaw)) % 360, (MC.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - MC.thePlayer.rotationPitch)) % 360.0f};
    }

}
