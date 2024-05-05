package tech.dort.dortware.impl.utils.combat.rotation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import skidmonke.Minecraft;

import java.util.List;

public class RotationUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();


    public static float[] getRotations(EntityLivingBase entityIn, float speed) {
        float yaw = updateRotation(mc.thePlayer.rotationYaw,
                getNeededRotations(entityIn)[0],
                speed);
        float pitch = updateRotation(mc.thePlayer.rotationPitch,
                getNeededRotations(entityIn)[1],
                speed);
        return new float[]{yaw, pitch};
    }

    private static float updateRotation(float currentRotation, float intendedRotation, float increment) {
        float f = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);

        if (f > increment)
            f = increment;

        if (f < -increment)
            f = -increment;

        return currentRotation + f;
    }

    public static float getAngleChange(EntityLivingBase entityIn) {
        float yaw = getNeededRotations(entityIn)[0];
        float pitch = getNeededRotations(entityIn)[1];
        float playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        float playerPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
        if (playerYaw < 0)
            playerYaw += 360;
        if (playerPitch < 0)
            playerPitch += 360;
        if (yaw < 0)
            yaw += 360;
        if (pitch < 0)
            pitch += 360;
        float yawChange = Math.max(playerYaw, yaw) - Math.min(playerYaw, yaw);
        float pitchChange = Math.max(playerPitch, pitch) - Math.min(playerPitch, pitch);
        return yawChange + pitchChange;
    }

//    public static EntityLivingBase rayCast(float yaw, float pitch, double range) {
//        Minecraft mc = Minecraft.getMinecraft();
//        if (mc.theWorld != null && mc.thePlayer != null) {
//            Vec3 position = mc.thePlayer.func_174824_e(mc.timer.renderPartialTicks);
//            Vec3 lookVector = mc.thePlayer.func_174806_f(pitch, yaw);
//            double reachDistance = range;
//            Entity pointedEntity = null;
//            List var5 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(lookVector.xCoord * mc.playerController.getBlockReachDistance(), lookVector.yCoord * mc.playerController.getBlockReachDistance(), lookVector.zCoord * mc.playerController.getBlockReachDistance()).expand(reachDistance, reachDistance, reachDistance));
//            for (Object o : var5) {
//                Entity currentEntity = (Entity) o;
//                if (currentEntity.canBeCollidedWith()) {
//                    MovingObjectPosition objPosition = currentEntity.getEntityBoundingBox().expand((double) currentEntity.getCollisionBorderSize(), (double) currentEntity.getCollisionBorderSize(), (double) currentEntity.getCollisionBorderSize()).contract(0.1, 0.1, 0.1).calculateIntercept(position, position.addVector(lookVector.xCoord * reachDistance, lookVector.yCoord * reachDistance, lookVector.zCoord * reachDistance));
//                    if (objPosition != null) {
//                        double distance = position.distanceTo(objPosition.hitVec);
//                        if (distance < reachDistance) {
//                            if (currentEntity == mc.thePlayer.ridingEntity && reachDistance == 0.0D) {
//                                pointedEntity = currentEntity;
//                            } else {
//                                pointedEntity = currentEntity;
//                                reachDistance = distance;
//                            }
//                        }
//                    }
//                }
//            }
//            if ((pointedEntity instanceof EntityLivingBase))
//                return (EntityLivingBase) pointedEntity;
//        }
//        return null;
//    }

    public static float constrainAngle(float angle) {
        angle = angle % 360F;

        while (angle <= -180) {
            angle = angle + 360;
        }

        while (angle > 180) {
            angle = angle - 360;
        }
        return angle;
    }

    public static float[] getRotations(final EntityLivingBase ent, int mode) {
        switch (mode) {
            case 0: {
                final double x = ent.posX;
                final double z = ent.posZ;
                final double y = ent.posY + ent.getEyeHeight() / 2.0f;
                return getRotationFromPosition(x, z, y);
            }

            case 1: {
                final double x = ent.posX;
                final double z = ent.posZ;
                final double y = ent.posY + ent.getEyeHeight() / 2.0f - 0.75;
                return getRotationFromPosition(x, z, y);
            }

            case 2: {
                final double x = ent.posX;
                final double z = ent.posZ;
                final double y = ent.posY + ent.getEyeHeight() / 2.0f - 1.2;
                return getRotationFromPosition(x, z, y);
            }

            case 3: {
                final double x = ent.posX;
                final double z = ent.posZ;
                final double y = ent.posY + ent.getEyeHeight() / 2.0f - 1.5;
                return getRotationFromPosition(x, z, y);
            }
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
        for (Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.getEntityBoundingBox().maxY - 2.0;
            posZ += ent.posZ;
        }
        posX /= targetList.size();
        posY /= targetList.size();
        posZ /= targetList.size();
        return new float[]{getRotationFromPosition(posX, posZ, posY)[0], getRotationFromPosition(posX, posZ, posY)[1]};
    }

    public static float[] getScaffoldRotations(final BlockPos position) { // Credits: Hideri
        double direction = direction();
        double posX = -Math.sin(direction) * 0.5F;
        double posZ = Math.cos(direction) * 0.5F;

        double x = position.getX() - mc.thePlayer.posX - posX;
        double y = position.getY() - mc.thePlayer.prevPosY - mc.thePlayer.getEyeHeight();
        double z = position.getZ() - mc.thePlayer.posZ - posZ;

        double distance = Math.hypot(x, z);

        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI - 90.0F);
        float pitch = (float) -(Math.atan2(y, distance) * 180.0D / Math.PI);

        return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)};
    }

    private static double direction() { // Credits: Hideri
        float rotationYaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.movementInput.moveForward < 0.0F)
            rotationYaw += 180.0F;
        float forward = 1.0F;
        if (mc.thePlayer.movementInput.moveForward < 0.0F)
            forward = -0.5F;
        else if (mc.thePlayer.movementInput.moveForward > 0.0F)
            forward = 0.5F;
        if (mc.thePlayer.movementInput.moveStrafe > 0.0F)
            rotationYaw -= 90.0F * forward;
        if (mc.thePlayer.movementInput.moveStrafe < 0.0F)
            rotationYaw += 90.0F * forward;
        return Math.toRadians(rotationYaw);
    }

    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 0.6;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float) (Math.atan2(zDiff, xDiff) * 180 / Math.PI) - 90.0f;
        final float pitch = (float) (-(Math.atan2(yDiff, dist) * 180 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getNeededRotations(EntityLivingBase entityIn) {
        double d0 = entityIn.posX - mc.thePlayer.posX;
        double d1 = entityIn.posZ - mc.thePlayer.posZ;
        double d2 = entityIn.posY + entityIn.getEyeHeight() - (mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight());

        double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float) (-(Math.atan2(d2, d3) * 180.0D / Math.PI));
        return new float[]{f, f1};
    }
}
