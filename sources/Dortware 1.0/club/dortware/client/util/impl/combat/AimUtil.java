package club.dortware.client.util.impl.combat;

import club.dortware.client.util.Util;
import club.dortware.client.util.impl.combat.extras.Rotation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dort
 */
public final class AimUtil implements Util {

    private static Minecraft mc = Minecraft.getMinecraft();
    /**
     * Attempts to look at an {@code EntityLivingBase}'s head
     *
     * @param entityLivingBase - The entity to look at.
     */
    public static void turnToEntityClient(EntityLivingBase entityLivingBase) {
        final Rotation destinationRotations = getRotationsRandom(entityLivingBase);
        mc.thePlayer.rotationYaw = destinationRotations.getRotationYaw();
        mc.thePlayer.rotationPitch = destinationRotations.getRotationPitch();
    }

    /**
     * Tries to get a {@code Rotation} for an {@code EntityLivingBase}
     *
     * @param entity - The entity to {@code EntityLivingBase} at.
     * @return The {@code Rotation} for this {@code Entity}
     */
    public static Rotation getRotationsRandom(EntityLivingBase entity) {
        ThreadLocalRandom threadLocalRandom =  ThreadLocalRandom.current();
        double randomXZ = threadLocalRandom.nextDouble(-0.08, 0.08);
        double randomY = threadLocalRandom.nextDouble(-0.125, 0.125);
        double x = entity.posX + randomXZ;
        double y = entity.posY + (entity.getEyeHeight() / 2.05) + randomY;
        double z = entity.posZ + randomXZ;
        return attemptFacePosition(x, y, z);
    }

    /**
     * Tries to obtain a {@code Rotation} for an XZ coordinate
     *
     * @param x - The X coordinate.
     * @param z - The Z coordinate.
     * @return The {@code Rotation} for this block position
     */
    public static Rotation attemptFaceBlockSide(double x, double z) {
        double xDifference = x - mc.thePlayer.posX;
        double zDifference = z - mc.thePlayer.posZ;
        float yaw = (float) (Math.toDegrees(Math.atan2(zDifference, xDifference)) - RandomUtils.nextFloat(0, .01F));
        return new Rotation(wrapAngle(yaw), 82.1F);
    }

    /**
     * Wraps the specified angle between -180 and 180
     *
     * @param angle - Input angle
     * @return The wrapped angle
     * @author Mojang
     */
    public static float wrapAngle(float angle) {

        angle %= 360.0F;

        if (angle >= 180.0F) {
            angle -= 360.0F;
        }

        if (angle < -180.0F) {
            angle += 360.0F;
        }

        return angle;
    }


    /**
     * Attempts to get rotations to aim a perfect bow shot for this {@code Entity}
     *
     * @param entity - The {@code Entity} to get rotations
     * @return The predicted rotations for this entity
     */
    public static Rotation getBowAngles(Entity entity) {
        double xDelta = entity.posX - entity.lastTickPosX;
        double zDelta = entity.posZ - entity.lastTickPosZ;
        double distance = mc.thePlayer.getDistanceToEntity(entity) % .8;
        boolean sprint = entity.isSprinting();
        double xMulti = distance / .8 * xDelta * (sprint ? 1.25 : 1);
        double zMulti = distance / .8 * zDelta * (sprint ? 1.25 : 1);
        double x = entity.posX + xMulti - mc.thePlayer.posX;
        double y = mc.thePlayer.posY + mc.thePlayer.getEyeHeight()
                - (entity.posY + entity.getEyeHeight());
        double z = entity.posZ + zMulti - mc.thePlayer.posZ;
        double distanceToEntity = mc.thePlayer.getDistanceToEntity(entity);
        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90;
        float pitch = (float) Math.toDegrees(Math.atan2(y, distanceToEntity));
        return new Rotation(yaw, pitch);
    }

    /**
     * Tries to get a {@code Rotation} for the specified coordinates
     *
     * @param x - The X coordinate
     * @param y - The Y coordinate
     * @param z - The Z coordinate
     * @return The rotations for the specified coordinates
     */
    public static Rotation attemptFacePosition(double x, double y, double z) {
        double xDiff = x - mc.thePlayer.posX;
        double yDiff = y - mc.thePlayer.posY - 1.2;
        double zDiff = z - mc.thePlayer.posZ;

        double dist = Math.hypot(xDiff, zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180 / Math.PI) - 90;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180 / Math.PI);
        return new Rotation(yaw, pitch);
    }


}
