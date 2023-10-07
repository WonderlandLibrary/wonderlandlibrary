package cc.novoline.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.concurrent.ThreadLocalRandom;

public final class RotationUtil {

    private static float[] serverAngles = new float[2];
    private static Minecraft mc = Minecraft.getInstance();

    private RotationUtil() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(Math.toRadians(-yaw) - (float) Math.PI);
        float f1 = MathHelper.sin(Math.toRadians(-yaw) - (float) Math.PI);
        float f2 = -MathHelper.cos(Math.toRadians(-pitch));
        float f3 = MathHelper.sin(Math.toRadians(-pitch));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    public static Vec3 getPositionEyes(float partialTicks) {
        return new Vec3(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ);
    }

    public static float getYawChange(double posX, double posZ) {
        final EntityPlayerSP player = mc.player;
        final double deltaX = posX - player.posX, // @off
                deltaZ = posZ - player.posZ; // @on

        final double yawToEntity;
        final double v1 = Math.toDegrees(Math.atan(deltaZ / deltaX));

        if (deltaZ < 0.0D && deltaX < 0.0D) {
            yawToEntity = 90.0D + v1;
        } else if (deltaZ < 0.0D && deltaX > 0.0D) {
            yawToEntity = -90.0D + v1;
        } else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }

        return MathHelper.wrapAngleTo180_float(-(player.rotationYaw - (float) yawToEntity));
    }

    public static float[] getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = mc.player;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double) player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(y, dist) * 180.0D / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsEntity(EntityLivingBase entity) {
        return mc.player.isMoving() ? getRotations(entity.posX + ThreadLocalRandom.current().nextDouble(0.03D, -0.03D), entity.posY + (double) entity.getEyeHeight() - 0.4D + ThreadLocalRandom.current().nextDouble(0.07D, -0.07D), entity.posZ + ThreadLocalRandom.current().nextDouble(0.03D, -0.03D)) : getRotations(entity.posX, entity.posY + (double) entity.getEyeHeight() - 0.4D, entity.posZ);
    }

    public static float getYawToPoint(double posX, double posZ) {
        Minecraft instance = mc;
        double xDiff = posX - (instance.player.lastTickPosX + (instance.player.posX - instance.player.lastTickPosX) * instance.timer.elapsedPartialTicks), zDiff = posZ - (instance.player.lastTickPosZ + (instance.player.posZ - instance.player.lastTickPosZ) * instance.timer.elapsedPartialTicks), dist = MathHelper.sqrt_double(
                xDiff * xDiff + zDiff * zDiff);
        return (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
    }

    public static float getPitchChange(Entity entity, double posY) {
        final double deltaX = entity.posX - mc.player.posX;

        final double deltaZ = entity.posZ - mc.player.posZ;
        final double deltaY = posY - 2.2D + (double) entity.getEyeHeight() - mc.player.posY;
        final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));

        return -MathHelper.wrapAngleTo180_float(mc.player.rotationPitch - (float) pitchToEntity) - 2.5F;
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        final double xDiff = x - mc.player.posX, zDiff = z - mc.player.posZ, yDiff = y - mc.player.posY - 1.2D, dist = MathHelper.sqrt_double(
                xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F, pitch = (float) -(Math.atan2(yDiff,
                dist) * 180.0D / Math.PI);

        return new float[]{yaw, pitch};
    }

    public static float[] aimAtLocation(double positionX, double positionY, double positionZ) {
        final double x = positionX - mc.player.posX, // @off
                y = positionY - mc.player.posY,
                z = positionZ - mc.player.posZ,
                distance = MathHelper.sqrt_double(x * x + z * z); // @on

        return new float[]{(float) (Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f, (float) -(Math.atan2(y, distance) * 180.0 / Math.PI)};
    }

    public static float[] getAngles(EntityLivingBase entity) {
        if (entity == null) return null;
        final EntityPlayerSP player = mc.player;

        final double diffX = entity.posX - player.posX,
                diffY = entity.posY + entity.getEyeHeight() * 0.9 - (player.posY + player.getEyeHeight()),
                diffZ = entity.posZ - player.posZ, dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ); // @on

        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F,
                pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

        return new float[]{player.rotationYaw + MathHelper.wrapAngleTo180_float(
                yaw - player.rotationYaw), player.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch)};
    }

    public static float[] getAngles(BlockPos pos) {
        double posX = pos.getX(), posY = pos.getY(), posZ = pos.getZ();
        final EntityPlayerSP player = mc.player;

        final double diffX = posX - player.posX,
                diffY = posY - (player.posY + player.getEyeHeight()),
                diffZ = posZ - player.posZ, dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ); // @on

        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F,
                pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

        return new float[]{player.rotationYaw + MathHelper.wrapAngleTo180_float(
                yaw - player.rotationYaw), player.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch)};
    }

    public static float[] getAngles(Vec3 vector) {
        double posX = vector.getX(), posY = vector.getY(), posZ = vector.getZ();
        final EntityPlayerSP player = mc.player;

        final double diffX = posX - player.posX,
                diffY = posY - (player.posY + player.getEyeHeight()),
                diffZ = posZ - player.posZ, dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ); // @on

        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F,
                pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

        return new float[]{player.rotationYaw + MathHelper.wrapAngleTo180_float(
                yaw - player.rotationYaw), player.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch)};
    }

    public static float getNewAngle(float angle) {
        angle %= 360.0F;

        if (angle >= 180.0F) {
            angle -= 360.0F;
        }

        if (angle < -180.0F) {
            angle += 360.0F;
        }

        return angle;
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        final float angle = Math.abs(angle1 - angle2) % 360.0F;
        return angle > 180.0F ? 360.0F - angle : angle;
    }

    public static double[] getDistance(double x, double z, double y) {
        final double distance = MathHelper.sqrt_double(x * x + z * z), // @off
                yaw = Math.atan2(z, x) * 180.0D / Math.PI - 90.0F,
                pitch = -(Math.atan2(y, distance) * 180.0D / Math.PI); // @on

        return new double[]{mc.player.rotationYaw + MathHelper.wrapAngleTo180_float(
                (float) (yaw - mc.player.rotationYaw)), mc.player.rotationPitch + MathHelper.wrapAngleTo180_float(
                (float) (pitch - mc.player.rotationPitch))};
    }
}