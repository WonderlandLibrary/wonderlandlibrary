package summer.base.utilities;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class RotationUtils {

    protected static Minecraft mc = Minecraft.getMinecraft();

    public static float[] faceTarget(final Entity target, final float p_706252, final float p_706253,
                                     final boolean miss) {
        final double var4 = target.posX - mc.thePlayer.posX;
        final double var5 = target.posZ - mc.thePlayer.posZ;
        double var7;
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase) target;
            var7 = var6.posY + var6.getEyeHeight() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        } else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0
                    - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        final Random rnd = new Random();
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float) (Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float) (-(Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.25 : 0.0), var8) * 180.0
                / 3.141592653589793));
        final float pitch = changeRotation(mc.thePlayer.rotationPitch, var10, p_706253);
        final float yaw = changeRotation(mc.thePlayer.rotationYaw, var9, p_706252);
        return new float[]{yaw, pitch};
    }

    public static float changeRotation(final float p_706631, final float p_706632, final float p_706633) {
        float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
        if (var4 > p_706633) {
            var4 = p_706633;
        }
        if (var4 < -p_706633) {
            var4 = -p_706633;
        }
        return p_706631 + var4;
    }

    public static float roundTo360(float angle) {
        float angle2 = angle;
        while (angle2 > 360) {
            angle2 -= 360;
        }
        return angle2;
    }

    public static float getYawChange(float yaw, double posX, double posZ) {
        double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = 0;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
            if (deltaX != 0)
                yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
            if (deltaX != 0)
                yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            if (deltaZ != 0)
                yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }

        return MathHelper.wrapAngleTo180_float(-(yaw - (float) yawToEntity));
    }

    public static float[] getRotations(Entity target) {
        double var7, var4 = target.posX - mc.thePlayer.posX;
        double var5 = target.posZ - mc.thePlayer.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var6 = (EntityLivingBase) target;
            var7 = var6.posY + var6.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        } else {
            var7 = ((target.getEntityBoundingBox()).minY + (target.getEntityBoundingBox()).maxY) / 2.0D - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        }
        double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        float var9 = (float) (Math.atan2(var5, var4) * 180.0D / Math.PI) - 90.0F;
        float var10 = (float) -(Math.atan2(var7 - ((target instanceof net.minecraft.entity.player.EntityPlayer) ? 0.25D : 0.0D), var8) * 180.0D / Math.PI);
        float pitch = changeRotation(mc.thePlayer.rotationPitch, var10);
        float yaw = changeRotation(mc.thePlayer.rotationYaw, var9);
        return new float[]{yaw, pitch};
    }

    public static float changeRotation(float p_706631, float p_706632) {
        float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
        if (var4 > 1000.0F)
            var4 = 1000.0F;
        if (var4 < -1000.0F)
            var4 = -1000.0F;
        return p_706631 + var4;
    }

    public static float getYawToEntity(Entity entity, boolean useOldPos) {
        final EntityPlayerSP player = Minecraft.thePlayer;
        double xDist = (useOldPos ? entity.prevPosX : entity.posX) -
                (useOldPos ? player.prevPosX : player.posX);
        double zDist = (useOldPos ? entity.prevPosZ : entity.posZ) -
                (useOldPos ? player.prevPosZ : player.posZ);
        float rotationYaw = useOldPos ? Minecraft.thePlayer.prevRotationYaw : Minecraft.thePlayer.rotationYaw;
        float var1 = (float) (Math.atan2(zDist, xDist) * 180.0D / Math.PI) - 90.0F;
        return rotationYaw + MathHelper.wrapAngleTo180_float( var1 - rotationYaw);
    }

}