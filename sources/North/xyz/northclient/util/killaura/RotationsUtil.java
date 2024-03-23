package xyz.northclient.util.killaura;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import xyz.northclient.InstanceAccess;

public class RotationsUtil implements InstanceAccess {
    private static final Minecraft mc = Minecraft.getMinecraft();

    @Getter
    private float yaw, pitch;
    @Getter
    private float lastYaw, lastPitch;

    public RotationsUtil(float rotationYaw, float rotationPitch) {
        lastYaw = yaw = rotationYaw;
        lastPitch = pitch = rotationPitch;
    }

    public void updateRotations(float rotationYaw, float rotationPitch) {
        lastYaw = yaw;
        lastPitch = pitch;

        float yawDiff = (rotationYaw - yaw);
        float pitchDiff = (rotationPitch - pitch);
        float gcd = (float) (Math.pow(mc.gameSettings.mouseSensitivity * 0.6 + 0.2, 3) * 1.2);

        float fixedYawDiff = yawDiff - (yawDiff % gcd);
        float fixedPitchDiff = pitchDiff - (pitchDiff % gcd);

        yaw += fixedYawDiff;
        pitch += fixedPitchDiff;

        pitch = Math.max(-90, Math.min(90, pitch));
    }

    public static float[] getRotationsFromPosition(double x, double y, double z) {
        double deltaX = x - mc.thePlayer.posX;
        double deltaY = y - mc.thePlayer.posY - mc.thePlayer.getEyeHeight();
        double deltaZ = z - mc.thePlayer.posZ;

        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        float yaw = (float) Math.toDegrees(-Math.atan2(deltaX, deltaZ));
        float pitch = (float) Math.toDegrees(-Math.atan2(deltaY, horizontalDistance));

        return new float[]{yaw, pitch};
    }

    public static float[] getRotationToBlock(BlockPos blockPos) {
        double deltaX = blockPos.getX() + 0.5 - mc.thePlayer.posX;
        double deltaY = blockPos.getY() + 0.5 - 3.5 - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double deltaZ = blockPos.getZ() + 0.5 - mc.thePlayer.posZ;
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) (Math.toDegrees(-Math.atan(deltaX / deltaZ)));
        float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        if (deltaX < 0 && deltaZ < 0) {
            yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        } else {
            if (deltaX > 0 && deltaZ < 0) {
                yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
            }
        }
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsToEntity(EntityLivingBase entity) {
        double x = entity.posX;
        double y = entity.posY;
        double z = entity.posZ;

        double finalY = mc.thePlayer.posY - y >= 0 ? y + entity.getEyeHeight() :
                -mc.thePlayer.posY - y < mc.thePlayer.getEyeHeight() ?
                        mc.thePlayer.posY + mc.thePlayer.getEyeHeight() : y;

        return getRotationsFromPosition(x, finalY, z);
    }
}
