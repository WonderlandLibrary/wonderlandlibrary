// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.rotation;

import Lavish.utils.math.MathUtils;
import Lavish.utils.movement.MovementUtil;
import java.util.Iterator;
import net.minecraft.util.MovingObjectPosition;
import java.util.List;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class RotationUtils
{
    protected static Minecraft mc;
    
    static {
        RotationUtils.mc = Minecraft.getMinecraft();
    }
    
    public static float[] faceTarget(final Entity target, final float p_706252, final float p_706253, final boolean miss) {
        final double var4 = target.posX - RotationUtils.mc.thePlayer.posX;
        final double var5 = target.posZ - RotationUtils.mc.thePlayer.posZ;
        double var7;
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase)target;
            var7 = var6.posY + var6.getEyeHeight() - RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight();
        }
        else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight();
        }
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float)(-(Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.25 : 0.0), var8) * 180.0 / 3.141592653589793));
        final float pitch = changeRotation(RotationUtils.mc.thePlayer.rotationPitch, var10);
        final float yaw = changeRotation(RotationUtils.mc.thePlayer.rotationYaw, var9);
        return new float[] { yaw, pitch };
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
    
    public static float roundTo360(final float angle) {
        float angle2;
        for (angle2 = angle; angle2 > 360.0f; angle2 -= 360.0f) {}
        return angle2;
    }
    
    public static float getYawChange(final float yaw, final double posX, final double posZ) {
        final double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = 0.0;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        }
        else if (deltaZ != 0.0) {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
    }
    
    public static float[] getRotations(final Entity target) {
        final double var4 = target.posX - RotationUtils.mc.thePlayer.posX;
        final double var5 = target.posZ - RotationUtils.mc.thePlayer.posZ;
        double var7;
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase)target;
            var7 = var6.posY + var6.getEyeHeight() - RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight();
        }
        else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight();
        }
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float)(-(Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.25 : 0.0), var8) * 180.0 / 3.141592653589793));
        final float pitch = changeRotation(RotationUtils.mc.thePlayer.rotationPitch, var10);
        final float yaw = changeRotation(RotationUtils.mc.thePlayer.rotationYaw, var9);
        return new float[] { yaw, pitch };
    }
    
    public static float changeRotation(final float p_706631, final float p_706632) {
        float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
        if (var4 > 1000.0f) {
            var4 = 1000.0f;
        }
        if (var4 < -1000.0f) {
            var4 = -1000.0f;
        }
        return p_706631 + var4;
    }
    
    public static float getYawToEntity(final Entity entity, final boolean useOldPos) {
        final EntityPlayerSP player = RotationUtils.mc.thePlayer;
        final double xDist = (useOldPos ? entity.prevPosX : entity.posX) - (useOldPos ? player.prevPosX : player.posX);
        final double zDist = (useOldPos ? entity.prevPosZ : entity.posZ) - (useOldPos ? player.prevPosZ : player.posZ);
        final float rotationYaw = useOldPos ? RotationUtils.mc.thePlayer.prevRotationYaw : RotationUtils.mc.thePlayer.rotationYaw;
        final float var1 = (float)(Math.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }
    
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotations(final EntityLivingBase ent, final String mode) {
        if (mode == "Head") {
            final double x = ent.posX;
            final double z = ent.posZ;
            final double y = ent.posY + ent.getEyeHeight() / 2.0f;
            return getRotationFromPosition(x, z, y);
        }
        if (mode == "Chest") {
            final double x = ent.posX;
            final double z = ent.posZ;
            final double y = ent.posY + ent.getEyeHeight() / 2.0f - 0.75;
            return getRotationFromPosition(x, z, y);
        }
        if (mode == "Dick") {
            final double x = ent.posX;
            final double z = ent.posZ;
            final double y = ent.posY + ent.getEyeHeight() / 2.0f - 1.2;
            return getRotationFromPosition(x, z, y);
        }
        if (mode == "Legs") {
            final double x = ent.posX;
            final double z = ent.posZ;
            final double y = ent.posY + ent.getEyeHeight() / 2.0f - 1.5;
            return getRotationFromPosition(x, z, y);
        }
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f - 0.5;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float getTrajAngleSolutionLow(final float d3, final float d1, final float velocity) {
        final float g = 0.006f;
        final float sqrt = velocity * velocity * velocity * velocity - 0.006f * (0.006f * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (0.006f * d3)));
    }
    
    public static float getNewAngle(float angle) {
        angle %= 360.0f;
        if (angle >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
    
    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 360.0f - angle3;
        }
        return angle3;
    }
    
    public static float[] aimAtLocation(final BlockPos paramBlockPos, final EnumFacing paramEnumFacing) {
        final double d1 = paramBlockPos.getX() + 0.5 - RotationUtils.mc.thePlayer.posX + paramEnumFacing.getFrontOffsetX() / 2.0;
        final double d2 = paramBlockPos.getZ() + 0.5 - RotationUtils.mc.thePlayer.posZ + paramEnumFacing.getFrontOffsetZ() / 2.0;
        final double d3 = RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight() - (paramBlockPos.getY() + 0.5);
        final double d4 = MathHelper.sqrt_double(d1 * d1 + d2 * d2);
        float f1 = (float)(Math.atan2(d2, d1) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(Math.atan2(d3, d4) * 180.0 / 3.141592653589793);
        if (f1 < 0.0f) {
            f1 += 360.0f;
        }
        return new float[] { f1, f2 };
    }
    
    public static EntityLivingBase rayCast(final float yaw, final float pitch, final double range) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld != null && mc.thePlayer != null) {
            final Vec3 position = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
            final Vec3 lookVector = mc.thePlayer.getVectorForRotation(pitch, yaw);
            double reachDistance = range;
            Entity pointedEntity = null;
            final List<Entity> var5 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(lookVector.xCoord * mc.playerController.getBlockReachDistance(), lookVector.yCoord * mc.playerController.getBlockReachDistance(), lookVector.zCoord * mc.playerController.getBlockReachDistance()).expand(reachDistance, reachDistance, reachDistance));
            for (int var6 = 0; var6 < var5.size(); ++var6) {
                final Entity currentEntity = var5.get(var6);
                final MovingObjectPosition objPosition;
                final double distance;
                if (currentEntity.canBeCollidedWith() && (objPosition = currentEntity.getEntityBoundingBox().expand(currentEntity.getCollisionBorderSize(), currentEntity.getCollisionBorderSize(), currentEntity.getCollisionBorderSize()).contract(0.1, 0.1, 0.1).calculateIntercept(position, position.addVector(lookVector.xCoord * reachDistance, lookVector.yCoord * reachDistance, lookVector.zCoord * reachDistance))) != null && (distance = position.distanceTo(objPosition.hitVec)) < reachDistance) {
                    if (currentEntity == mc.thePlayer.ridingEntity && reachDistance == 0.0) {
                        pointedEntity = currentEntity;
                    }
                    else {
                        pointedEntity = currentEntity;
                        reachDistance = distance;
                    }
                }
            }
            if (pointedEntity != null && pointedEntity instanceof EntityLivingBase) {
                return (EntityLivingBase)pointedEntity;
            }
        }
        return null;
    }
    
    public static float constrainAngle(float angle) {
        while ((angle %= 360.0f) <= -180.0f) {
            angle += 360.0f;
        }
        while (angle > 180.0f) {
            angle -= 360.0f;
        }
        return angle;
    }
    
    public static float[] getRotations(final EntityLivingBase ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float[] getRotations(final EntityLivingBase ent, final int mode) {
        if (mode == 0) {
            final double x = ent.posX;
            final double z = ent.posZ;
            final double y = ent.posY + ent.getEyeHeight() / 2.0f;
            return getRotationFromPosition(x, z, y);
        }
        if (mode == 1) {
            final double x = ent.posX;
            final double z = ent.posZ;
            final double y = ent.posY + ent.getEyeHeight() / 2.0f - 0.75;
            return getRotationFromPosition(x, z, y);
        }
        if (mode == 2) {
            final double x = ent.posX;
            final double z = ent.posZ;
            final double y = ent.posY + ent.getEyeHeight() / 2.0f - 1.2;
            return getRotationFromPosition(x, z, y);
        }
        if (mode == 3) {
            final double x = ent.posX;
            final double z = ent.posZ;
            final double y = ent.posY + ent.getEyeHeight() / 2.0f - 1.5;
            return getRotationFromPosition(x, z, y);
        }
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f - 0.5;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float[] getAverageRotations(final List<EntityLivingBase> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (final Entity entity : targetList) {
            posX += entity.posX;
            posY += entity.getEntityBoundingBox().maxY - 2.0;
            posZ += entity.posZ;
        }
        final float[] array = new float[2];
        final int n = 0;
        posX /= targetList.size();
        posZ /= targetList.size();
        array[n] = getRotationFromPosition(posX, posZ, posY /= targetList.size())[0];
        array[1] = getRotationFromPosition(posX, posZ, posY)[1];
        return array;
    }
    
    public static float[] getRotations(final double posX, final double posY, final double posZ) {
        final EntityPlayerSP player = RotationUtils.mc.thePlayer;
        final double x = posX - player.posX;
        final double y = posY - (player.posY + player.getEyeHeight());
        final double z = posZ - player.posZ;
        final double dist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotationsEntity(final EntityLivingBase entity) {
        if (MovementUtil.isMoving()) {
            return getRotations(entity.posX + MathUtils.getRandomInRange(0.03, -0.03), entity.posY + entity.getEyeHeight() - 0.4 + MathUtils.getRandomInRange(0.07, -0.07), entity.posZ + MathUtils.getRandomInRange(0.03, -0.03));
        }
        return getRotations(entity.posX, entity.posY + entity.getEyeHeight() - 0.4, entity.posZ);
    }
}
