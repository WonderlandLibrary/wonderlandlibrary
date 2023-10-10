package Reality.Realii.utils.cheats.RenderUtills;

import Reality.Realii.mods.modules.combat.CustomRotations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RotationUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float[] EveryRotation(EntityLivingBase target) {
        float yawRandom = (float) MathHelper.getRandomDoubleInRange(new Random(), -0.2, 0.2);
        float pitchRandom = (float)MathHelper.getRandomDoubleInRange(new Random(), -0.02, 0.02);
        double posX = (boolean)CustomRotations.Randomisze.getValue() ? target.posX - mc.thePlayer.posX + (double)yawRandom : target.posX - mc.thePlayer.posX;
        double posY = (boolean)CustomRotations.ClamPitch.getValue()
                ? MathHelper.clamp_double(mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), target.getEntityBoundingBox().minY, target.getEntityBoundingBox().maxY)
                - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight())
                : (
                (boolean)CustomRotations.Randomisze.getValue()
                        ? target.posY + (double)target.getEyeHeight() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - (double)pitchRandom)
                        : target.posY + (double)target.getEyeHeight() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight())
        );
        double posZ = (boolean)CustomRotations.Randomisze.getValue() ? target.posZ - mc.thePlayer.posZ - (double)yawRandom : target.posZ - mc.thePlayer.posZ;
        double var14 = (double)MathHelper.sqrt_double(posX * posX + posZ * posZ);
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float)(-(Math.atan2(posY, var14) * 180.0 / Math.PI));
      if( (boolean)CustomRotations.MouseSens.getValue()) {
            float f2 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f3 = f2 * f2 * f2 * 1.2F;
            yaw -= yaw % f3;
            pitch -= pitch % (f3 * f2);
        }

        return new float[]{yaw, pitch};
    }

    public static Vec3 getBestHitVec(Entity entity) {
        Vec3 positionEyes = RotationUtil.mc.thePlayer.getPositionEyes(1.0f);
        float f11 = entity.getCollisionBorderSize();
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox().expand((double)f11, (double)f11, (double)f11);
        double ex = MathHelper.clamp_double((double)positionEyes.xCoord, (double)entityBoundingBox.minX, (double)entityBoundingBox.maxX);
        double ey = MathHelper.clamp_double((double)positionEyes.yCoord, (double)entityBoundingBox.minY, (double)entityBoundingBox.maxY);
        double ez = MathHelper.clamp_double((double)positionEyes.zCoord, (double)entityBoundingBox.minZ, (double)entityBoundingBox.maxZ);
        return new Vec3(ex, ey, ez);
    }

    public static float updateRotation(float current, float calc, float maxDelta) {
        float f = MathHelper.wrapAngleTo180_float((float)(calc - current));
        if (f > maxDelta) {
            f = maxDelta;
        }
        if (f < -maxDelta) {
            f = -maxDelta;
        }
        return current + f;
    }

    public static float[] mouseSens(float yaw, float pitch, float lastYaw, float lastPitch) {
        if ((double)RotationUtil.mc.gameSettings.mouseSensitivity == 0.5) {
            RotationUtil.mc.gameSettings.mouseSensitivity = 0.47887325f;
        }
        if (yaw == lastYaw && pitch == lastPitch) {
            return new float[]{yaw, pitch};
        }
        float f1 = RotationUtil.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        float f2 = f1 * f1 * f1 * 8.0f;
        int deltaX = (int)((6.667 * (double)yaw - 6.667 * (double)lastYaw) / (double)f2);
        int deltaY = (int)((6.667 * (double)pitch - 6.667 * (double)lastPitch) / (double)f2) * -1;
        float f5 = (float)deltaX * f2;
        float f3 = (float)deltaY * f2;
        yaw = (float)((double)lastYaw + (double)f5 * 0.15);
        float f4 = (float)((double)lastPitch - (double)f3 * 0.15);
        pitch = MathHelper.clamp_float((float)f4, (float)-90.0f, (float)90.0f);
        return new float[]{yaw, pitch};
    }


    public static float[] basicRotation(Entity entity, float currentYaw, float currentPitch, boolean random) {
        Vec3 ePos = RotationUtil.getBestHitVec(entity);
        double x = ePos.xCoord - mc.thePlayer.posX;
        double y = ePos.yCoord - (mc.thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight());
        double z = ePos.zCoord - RotationUtil.mc.thePlayer.posZ;
        float calcYaw = (float)(MathHelper.func_181159_b((double)z, (double)x) * 180.0 / Math.PI - 90.0);
        float calcPitch = (float)(-(MathHelper.func_181159_b((double)y, (double)MathHelper.sqrt_double((double)(x * x + z * z))) * 180.0 / Math.PI));
        float yaw = RotationUtil.updateRotation(currentYaw, calcYaw, 180.0f);
        float pitch = RotationUtil.updateRotation(currentPitch, calcPitch, 180.0f);
        if (random) {
            yaw = (float)((double)yaw + ThreadLocalRandom.current().nextGaussian());
            pitch = (float)((double)pitch + ThreadLocalRandom.current().nextGaussian());
        }
        return RotationUtil.mouseSens(yaw, pitch, currentYaw, currentPitch);
    }
}
