package info.sigmaclient.sigma.utils.player;

import info.sigmaclient.sigma.utils.RandomUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static info.sigmaclient.sigma.modules.Module.mc;

public class RotationUtils {
    public static int SMOOTH_BACK_TICK = 0;
    public static int NEXT_SLOT = -1;
    public static float NO_ROTATION = -999;
    public static Random random = new Random();

    /**
     * Translate vec to rotation
     *
     * @param vec     target vec
     * @return rotation
     */
    public static Rotation toRotation(final Vector3d vec) {
        final double x2 = mc.player.lastTickPosX + (mc.player.getPosX() - mc.player.lastTickPosX) * mc.timer.renderPartialTicks;
        final double z2 = mc.player.lastTickPosZ + (mc.player.getPosZ() - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks;
        final Vector3d eyesPos = new Vector3d(x2, mc.player.getPosY() + mc.player.getEyeHeight(), z2);

        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;

        return new Rotation(MathHelper.wrapAngleTo180_float(
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F
        ), MathHelper.wrapAngleTo180_float(
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        ));
    }
    /**
     * Translate vec to rotation
     *
     * @param vec     target vec
     * @return rotation
     */
    public static Rotation toRotationTimer(final Vector3d vec) {
        final double x2 = mc.player.lastTickPosX + (mc.player.getPosX() - mc.player.lastTickPosX) * mc.timer.renderPartialTicks;
        final double z2 = mc.player.lastTickPosZ + (mc.player.getPosZ() - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks;
        final Vector3d eyesPos = new Vector3d(x2, mc.player.getPosY() + mc.player.getEyeHeight(), z2);

        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;

        return new Rotation(MathHelper.wrapAngleTo180_float(
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F
        ), MathHelper.wrapAngleTo180_float(
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        ));
    }
    /**
     * Translate vec to rotation
     *
     * @param vec     target vec
     * @param predict predict new location of your body
     * @return rotation
     */
    public static Rotation toRotation(final Vector3d vec, final boolean predict) {
        final Vector3d eyesPos = new Vector3d(mc.player.getPosX(), mc.player.getBoundingBox().minY +
                mc.player.getEyeHeight(), mc.player.getPosZ());
        
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;

        return new Rotation(MathHelper.wrapAngleTo180_float(
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F
        ), MathHelper.wrapAngleTo180_float(
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        ));
    }
    /**
     * Calculate difference between two rotations
     *
     * @param a rotation
     * @param b rotation
     * @return difference between rotation
     */
    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    /**
     * Limit your rotation using a turn speed
     *
     * @param currentRotation your current rotation
     * @param targetRotation your goal rotation
     * @param turnSpeed your turn speed
     * @return limited rotation
     */
    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(
                currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed))
        );
    }
    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float horizontalSpeed, final float verticalSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(
                currentRotation.getYaw() + (yawDifference > horizontalSpeed ? horizontalSpeed : Math.max(yawDifference, -horizontalSpeed)),
                currentRotation.getPitch() + (pitchDifference > verticalSpeed ? verticalSpeed : Math.max(pitchDifference, -verticalSpeed))
        );
    }

    /**
     * Calculate difference between two angle points
     *
     * @param a angle point
     * @param b angle point
     * @return difference between angle points
     */
    public static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }
    public static final float[] lastRandomDeltaRotation = {0f, 0f};
    public static Rotation nearestRotationFinder(final AxisAlignedBB bb, final boolean random,
                                                 final boolean predict, final boolean throughWalls, final float distance) {
        final Vector3d eyes = mc.player.getEyePosition(1F);

        Vector3d vecRotation3d = null;

        for(double xSearch = 0.05D; xSearch <= 0.95D; xSearch += 0.05D) {
            for (double ySearch = 0.05D; ySearch < 0.95D; ySearch += 0.05D) {
                for (double zSearch = 0.05D; zSearch <= 0.95D; zSearch += 0.05D) {
                    final Vector3d vec3 = new Vector3d(
                            bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + (bb.maxY - bb.minY) * ySearch,
                            bb.minZ + (bb.maxZ - bb.minZ) * zSearch
                    );
                    final double vecDist = eyes.squareDistanceTo(vec3);

//                    if (vecDist > distance * distance)
//                        continue;

//                    if (throughWalls) {
                        if (vecRotation3d == null || eyes.squareDistanceTo(vecRotation3d) > vecDist) {
                            vecRotation3d = vec3;
//                        }
                    }
                }
            }
        }
        return toRotation(vecRotation3d, false);
    }
    public static Rotation NCPRotation(final Entity e) {
        final Vector3d eyes = mc.player.getEyePosition(1F);
        AxisAlignedBB bb = e.getBoundingBox();
        Vector3d vecRotation3d = null;
        double w = mc.player.getWidth() * 0.5;
        for(double xSearch = 0D; xSearch <= 1D; xSearch += 0.1D) {
            for (double ySearch = 0D; ySearch < 1.0D; ySearch += 0.1D) {
                for (double zSearch = 0D; zSearch <= 1D; zSearch += 0.1D) {
                    final Vector3d vec3 = new Vector3d(
                            bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + e.getEyeHeight() * ySearch,
                            bb.minZ + (bb.maxZ - bb.minZ) * zSearch
                    );
                    final double vecDist = eyes.squareDistanceTo(vec3);

//                    if (vecDist > distance * distance)
//                        continue;

//                    if (throughWalls) {
                    if (vecRotation3d == null || eyes.squareDistanceTo(vecRotation3d) > vecDist) {
                        vecRotation3d = vec3;
//                        }
                    }
                }
            }
        }

        final double diffX = vecRotation3d.x - eyes.x;
        final double diffY = vecRotation3d.y - eyes.y;
        final double diffZ = vecRotation3d.z - eyes.z;
        Rotation r = new Rotation(MathHelper.wrapAngleTo180_float(
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F
        ), MathHelper.wrapAngleTo180_float(
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        ));
        if(e.getBoundingBox().contains(eyes)){
            r.setYaw(0);
        }
        return limitAngleChange(new Rotation(mc.player.lastReportedYaw, mc.player.lastReportedPitch),r, 30, 20);
    }
    public static double nearestRotation(final AxisAlignedBB bb) {
        final Vector3d eyes = mc.player.getEyePosition(1F);

        Vector3d vecRotation3d = null;

        for(double xSearch = 0D; xSearch <= 1D; xSearch += 0.05D) {
            for (double ySearch = 0D; ySearch < 1D; ySearch += 0.05D) {
                for (double zSearch = 0D; zSearch <= 1D; zSearch += 0.05D) {
                    final Vector3d vec3 = new Vector3d(
                            bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + (bb.maxY - bb.minY) * ySearch,
                            bb.minZ + (bb.maxZ - bb.minZ) * zSearch
                    );
                    final double vecDist = eyes.squareDistanceTo(vec3);

                    if (vecRotation3d == null || eyes.squareDistanceTo(vecRotation3d) > vecDist) {
                        vecRotation3d = vec3;
                    }
                }
            }
        }
        return vecRotation3d.distanceTo(eyes);
    }
    public static void reset(){
        RotationUtils.movementFixYaw = NO_ROTATION;
        RotationUtils.movementFixPitch = NO_ROTATION;
        RotationUtils.slient = false;
        fixing = false;
    }
    public static boolean isMovefixing(){
        return RotationUtils.movementFixYaw != NO_ROTATION && RotationUtils.movementFixPitch != NO_ROTATION;
    }
    public static boolean isMovefixingMove(){
        return RotationUtils.movementFixYaw != NO_ROTATION && RotationUtils.movementFixPitch != NO_ROTATION && fixing;
    }
    public static int getDiff(){
        return (int) ((MathHelper.wrapAngleTo180_float(mc.player.rotationYaw - movementFixYaw - 22.5f - 135.0f) + 180.0) / 45.0);
    }
    public static float getYaw() {
        int angleDiff = getDiff();
        if (slient) {
            return movementFixYaw + 45.0f * angleDiff;
        }
        return movementFixYaw;
    }
    public static float movementFixYaw = NO_ROTATION;
    public static float movementFixPitch = NO_ROTATION;
    public static boolean slient = false;
    public static boolean fixing = false;
    public static boolean isMouseOver(final float yaw, final float pitch, final Entity target, final float range, float expand, boolean onlytarget) {
        final float partialTicks = mc.timer.renderPartialTicks;
        final Entity entity = mc.player;
        Entity mcPointedEntity = null;

        if (entity != null && mc.world != null) {
            Vector3d vector3d = entity.getEyePosition(partialTicks);

            mc.getProfiler().startSection("pick");
            Vector3d vector3d1 = entity.getLookCustom(1.0f, yaw, pitch);
            Vector3d vector3d2 = vector3d.add(vector3d1.x * (double) 4.5, vector3d1.y * (double) 4.5, vector3d1.z * (double) 4.5);
            AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(expand).expand(vector3d1.scale(4.5)).grow(1.0D, 1.0D, 1.0D);
            EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(entity, vector3d, vector3d2,
                    axisalignedbb, (p_lambda$getMouseOver$0_0_) ->
                    !p_lambda$getMouseOver$0_0_.isSpectator() && p_lambda$getMouseOver$0_0_.canBeCollidedWith() && (!onlytarget || p_lambda$getMouseOver$0_0_.equals(target)), 4.5 * 4.5);

            if (entityraytraceresult != null) {
                Vector3d vector3d3 = entityraytraceresult.getHitVec();
                double d2 = vector3d.squareDistanceTo(vector3d3);
                if(d2 <= range * range)
                    mcPointedEntity = entityraytraceresult.getEntity();
            }
            mc.getProfiler().endSection();

            return target.equals(mcPointedEntity);
        }

        return false;
    }

    public static float updateRotation(float current, float calc, float maxDelta) {
        float f = MathHelper.wrapAngleTo180_float(calc - current);
        if (f > maxDelta) {
            f = maxDelta;
        }
        if (f < -maxDelta) {
            f = -maxDelta;
        }
        return current + f;
    }
    public static float[] scaffoldRots(double bx, double by, double bz, float lastYaw, float lastPitch, float yawSpeed, float pitchSpeed, boolean random) {
        double x = bx - mc.player.getPosX();
        double y = by - (mc.player.getPosY() + (double)mc.player.getEyeHeight());
        double z = bz - mc.player.getPosZ();
        float calcYaw = (float)(Math.toDegrees(MathHelper.atan2(z, x)) - 90.0);
        float calcPitch = (float)(-(MathHelper.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180.0 / Math.PI));
        float pitch = updateRotation(lastPitch, calcPitch, pitchSpeed + RandomUtil.nextFloat(0.0f, 15.0f));
        float yaw = updateRotation(lastYaw, calcYaw, yawSpeed + RandomUtil.nextFloat(0.0f, 15.0f));
        if (random) {
            yaw = (float)((double)yaw + ThreadLocalRandom.current().nextDouble(-2.0, 2.0));
            pitch = (float)((double)pitch + ThreadLocalRandom.current().nextDouble(-0.2, 0.2));
        }
        return new float[]{yaw, pitch};
    }


    public static float[] mouseSens(float yaw, float pitch, float lastYaw, float lastPitch) {
        if (yaw == lastYaw && pitch == lastPitch) {
            return new float[]{yaw, pitch};
        }
        float f1 = (float) (mc.gameSettings.mouseSensitivity * 0.6f + 0.2f);
        float f2 = f1 * f1 * f1 * 8.0f;
        int deltaX = (int)((6.66666666666666666666 * (double)yaw - 6.66666666666666666666 * (double)lastYaw) / (double)f2);
        int deltaY = (int)((6.66666666666666666666 * (double)pitch - 6.66666666666666666666 * (double)lastPitch) / (double)f2) * -1;
        float f5 = (float)deltaX * f2;
        float f3 = (float)deltaY * f2;
        yaw = (float)((double)lastYaw + (double)f5 * 0.15);
        float f4 = (float)((double)lastPitch - (double)f3 * 0.15);
        pitch = MathHelper.clamp(f4, -90.0f, 90.0f);
        return new float[]{yaw, pitch};
    }
    public static float rotateToYaw(float yawSpeed, float currentYaw, float calcYaw) {
        float yaw = updateRotation(currentYaw, calcYaw, yawSpeed + RandomUtil.nextFloat(0.0f, 15.0f));
        double diffYaw = MathHelper.wrapAngleTo180_float(calcYaw - currentYaw);
        if (!((double)(-yawSpeed) <= diffYaw) || !(diffYaw <= (double)yawSpeed)) {
            yaw = (float)((double)yaw + (double)RandomUtil.nextFloat(1.0f, 2.0f) * Math.sin((double)mc.player.rotationPitch * Math.PI));
        }
        if (yaw == currentYaw) {
            return currentYaw;
        }
        if ((double)mc.gameSettings.mouseSensitivity == 0.5) {
            mc.gameSettings.mouseSensitivity = 0.47887325f;
        }
        float f1 = (float) (mc.gameSettings.mouseSensitivity * 0.6f + 0.2f);
        float f2 = f1 * f1 * f1 * 8.0f;
        int deltaX = (int)((6.667 * (double)yaw - 6.666666666666667 * (double)currentYaw) / (double)f2);
        float f5 = (float)deltaX * f2;
        yaw = (float)((double)currentYaw + (double)f5 * 0.15);
        return yaw;
    }

    public static float rotateToPitch(float pitchSpeed, float currentPitch, float calcPitch) {
        float pitch = updateRotation(currentPitch, calcPitch, pitchSpeed + RandomUtil.nextFloat(0.0f, 15.0f));
        if (pitch != calcPitch) {
            pitch = (float)((double)pitch + (double)RandomUtil.nextFloat(1.0f, 2.0f) * Math.sin((double)mc.player.rotationYaw * Math.PI));
        }
        if ((double)mc.gameSettings.mouseSensitivity == 0.5) {
            mc.gameSettings.mouseSensitivity = 0.47887325f;
        }
        float f1 = (float) (mc.gameSettings.mouseSensitivity * 0.6f + 0.2f);
        float f2 = f1 * f1 * f1 * 8.0f;
        int deltaY = (int)((6.667 * (double)pitch - 6.666667 * (double)currentPitch) / (double)f2) * -1;
        float f3 = (float)deltaY * f2;
        float f4 = (float)((double)currentPitch - (double)f3 * 0.15);
        pitch = MathHelper.clamp(f4, -90.0f, 90.0f);
        return pitch;
    }

    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        Minecraft mc = Minecraft.getInstance();
        final double xSize = entity.getPosX() - mc.player.getPosX();
        final double ySize = entity.getPosY() + entity.getEyeHeight() / 2 - (mc.player.getPosY() + mc.player.getEyeHeight());
        final double zSize = entity.getPosZ() - mc.player.getPosZ();
        final double theta = MathHelper.sqrt(xSize * xSize + zSize * zSize);
        final float yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90;
        final float pitch = (float) (-(Math.atan2(ySize, theta) * 180 / Math.PI));
        return new float[]{(mc.player.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.player.rotationYaw)) % 360, (mc.player.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.player.rotationPitch)) % 360.0f};
    }

    public static float[] getFacingRotations2(final int paramInt1, final double d, final int paramInt3) {
        final SnowballEntity localEntityPig = new SnowballEntity(Minecraft.getInstance().world, paramInt1 + 0.5, d + 0.5, paramInt3 + 0.5);
        return getRotationsNeeded(localEntityPig);
    }

}
