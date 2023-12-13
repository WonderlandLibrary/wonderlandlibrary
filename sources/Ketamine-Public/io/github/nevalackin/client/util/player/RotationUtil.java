package io.github.nevalackin.client.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.List;
import java.util.function.BiFunction;

public final class RotationUtil {

    private static final double RAD_TO_DEG = 180.0 / Math.PI;
    private static final double DEG_TO_RAD = Math.PI / 180.0;

    private RotationUtil() {
    }

    /**
     * Calculate where ray intercepts with boundingBox'
     *
     * @param boundingBox Bounding box to check if ray will intercept with
     * @param src         Where ray originates from
     * @param yaw         Yaw of ray cast
     * @param pitch       Pitch of ray cast
     * @param reach       Max distance ray can travel
     * @return hitVec where bounding box and ray intercept
     */
    public static MovingObjectPosition calculateIntercept(final AxisAlignedBB boundingBox,
                                                          final Vec3 src,
                                                          final float yaw,
                                                          final float pitch,
                                                          final double reach) {
        return boundingBox.calculateIntercept(src, getDstVec(src, yaw, pitch, reach));
    }

    /**
     * Get optimal attack hit vec
     *
     * @param mc           Minecraft instance
     * @param src          Ray trace origin {@link RotationUtil#getHitOrigin}
     * @param boundingBox  boundingBox of entity being attacked
     * @param ignoreBlocks If can attack through blocks
     * @param maxRayTraces Max number of ray traces that can be performed (use -1 for max)
     * @return Hit vec
     */
    public static Vec3 getAttackHitVec(final Minecraft mc,
                                       final Vec3 src,
                                       final AxisAlignedBB boundingBox,
                                       final Vec3 desiredHitVec,
                                       final boolean ignoreBlocks,
                                       final int maxRayTraces) {
        // Validate that closest hit vec is legit
        if (validateHitVec(mc, src, desiredHitVec, ignoreBlocks))
            return desiredHitVec;

        // If not find a better hit vec
        double closestDist = Double.MAX_VALUE;
        Vec3 bone = null;

        final double xWidth = boundingBox.maxX - boundingBox.minX;
        final double zWidth = boundingBox.maxZ - boundingBox.minZ;
        final double height = boundingBox.maxY - boundingBox.minY;

        int passes = 0;

        for (double x = 0.0; x < 1.0; x += 0.2) {
            for (double y = 0.0; y < 1.0; y += 0.2) {
                for (double z = 0.0; z < 1.0; z += 0.2) {
                    if (maxRayTraces != -1 && passes > maxRayTraces) return null;

                    final Vec3 hitVec = new Vec3(boundingBox.minX + xWidth * x,
                                                 boundingBox.minY + height * y,
                                                 boundingBox.minZ + zWidth * z);

                    final double dist;
                    if (validateHitVec(mc, src, hitVec, ignoreBlocks) &&
                        (dist = src.distanceTo(hitVec)) < closestDist) {

                        closestDist = dist;
                        bone = hitVec;
                    }

                    passes++;
                }
            }
        }

        return bone;
    }

    public static float[] calculateAverageRotations(final List<float[]> rotations) {
        final int n = rotations.size();

        final float[] tRots = new float[2];

        for (final float[] rotation : rotations) {
            tRots[0] += rotation[0];
            tRots[1] += rotation[1];
        }

        tRots[0] /= n;
        tRots[1] /= n;

        return tRots;
    }

    public static boolean validateHitVec(final Minecraft mc,
                                         final Vec3 src,
                                         final Vec3 dst,
                                         final boolean ignoreBlocks,
                                         final double penetrationDist) {
        final Vec3 blockHitVec = rayTraceHitVec(mc, src, dst);
        // If not return closest
        if (blockHitVec == null) return true;
        // Get the distance of the hit (a.k.a the reach)
        final double distance = src.distanceTo(dst);
        // If ignoreBlocks & distance passed < penetrationDist use the closest
        return ignoreBlocks && distance < penetrationDist;
    }

    public static boolean validateHitVec(final Minecraft mc,
                                         final Vec3 src,
                                         final Vec3 dst,
                                         final boolean ignoreBlocks) {
        // Max vanilla penetration range
        return validateHitVec(mc, src, dst, ignoreBlocks, 2.8);
    }

    /**
     * @param entity Entity to get hit origin from
     * @return hit origin of entity (eye pos)
     */
    public static Vec3 getHitOrigin(final Entity entity) {
        return new Vec3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
    }

    /**
     * Get the closest point on a boundingBox from start
     *
     * @param start       Src
     * @param boundingBox boundingBox to calculate closest point from start
     * @return The closest point on boundingBox as a hit vec
     */
    public static Vec3 getClosestPoint(final Vec3 start,
                                       final AxisAlignedBB boundingBox) {
        final double closestX = start.xCoord >= boundingBox.maxX ? boundingBox.maxX :
            start.xCoord <= boundingBox.minX ? boundingBox.minX :
                boundingBox.minX + (start.xCoord - boundingBox.minX);

        final double closestY = start.yCoord >= boundingBox.maxY ? boundingBox.maxY :
            start.yCoord <= boundingBox.minY ? boundingBox.minY :
                boundingBox.minY + (start.yCoord - boundingBox.minY);

        final double closestZ = start.zCoord >= boundingBox.maxZ ? boundingBox.maxZ :
            start.zCoord <= boundingBox.minZ ? boundingBox.minZ :
                boundingBox.minZ + (start.zCoord - boundingBox.minZ);

        return new Vec3(closestX, closestY, closestZ);
    }

    public static Vec3 getCenterPointOnBB(final AxisAlignedBB hitBox,
                                          final double progressToTop) {
        final double xWidth = hitBox.maxX - hitBox.minX;
        final double zWidth = hitBox.maxZ - hitBox.minZ;
        final double height = hitBox.maxY - hitBox.minY;
        return new Vec3(hitBox.minX + xWidth / 2.0, hitBox.minY + height * progressToTop, hitBox.minZ + zWidth / 2.0);
    }

    public static AxisAlignedBB getHittableBoundingBox(final Entity entity,
                                                       final double boundingBoxScale) {
        return entity.getEntityBoundingBox().expand(boundingBoxScale, boundingBoxScale, boundingBoxScale);
    }

    public static AxisAlignedBB getHittableBoundingBox(final Entity entity) {
        return getHittableBoundingBox(entity, entity.getCollisionBorderSize());
    }

    public static Vec3 getAverageChange(final List<Vec3> positions) {
        final int nPositions = positions.size();

        if (nPositions <= 1)
            return new Vec3(0, 0, 0); // No change since there are either 0 or 1 elements

        Vec3 changeAccumulator = new Vec3(0, 0, 0);
        Vec3 previous = positions.get(0);

        for (int i = 1; i < nPositions; i++) {
            Vec3 position = positions.get(i);
            changeAccumulator = changeAccumulator.addVector(previous.xCoord - position.xCoord,
                                                            previous.yCoord - position.yCoord,
                                                            previous.zCoord - position.zCoord);
            previous = position;
        }

        return new Vec3(changeAccumulator.xCoord / nPositions,
                        changeAccumulator.yCoord / nPositions,
                        changeAccumulator.zCoord / nPositions);
    }

    /**
     * Adapted from {@link Entity#getVectorForRotation}
     */
    public static Vec3 getPointedVec(final float yaw,
                                     final float pitch) {
        final double theta = -Math.cos(-pitch * DEG_TO_RAD);

        return new Vec3(Math.sin(-yaw * DEG_TO_RAD - Math.PI) * theta,
                        Math.sin(-pitch * DEG_TO_RAD),
                        Math.cos(-yaw * DEG_TO_RAD - Math.PI) * theta);
    }

    public static Vec3 getDstVec(final Vec3 src,
                                 final float yaw,
                                 final float pitch,
                                 final double reach) {
        final Vec3 rotationVec = getPointedVec(yaw, pitch);
        return src.addVector(rotationVec.xCoord * reach,
                             rotationVec.yCoord * reach,
                             rotationVec.zCoord * reach);
    }

    public static Vec3 rayTraceHitVec(final Minecraft mc,
                                      final Vec3 src,
                                      final Vec3 dst) {
        final MovingObjectPosition rayTraceResult = mc.theWorld.rayTraceBlocks(src, dst,
                                                                               false,
                                                                               false,
                                                                               false);

        return rayTraceResult != null ? rayTraceResult.hitVec : null;
    }

    public static MovingObjectPosition rayTraceBlocks(final Minecraft mc,
                                                      final Vec3 src,
                                                      final double reach,
                                                      final float yaw,
                                                      final float pitch) {
        return mc.theWorld.rayTraceBlocks(src,
                                          getDstVec(src, yaw, pitch, reach),
                                          false,
                                          false,
                                          true);
    }

    public static MovingObjectPosition rayTraceBlocks(final Minecraft mc,
                                                      final float yaw,
                                                      final float pitch) {
        return rayTraceBlocks(mc, getHitOrigin(mc.thePlayer), mc.playerController.getBlockReachDistance(), yaw, pitch);
    }

    public static float[] getRotations(final Vec3 start,
                                       final Vec3 dst) {
        final double xDif = dst.xCoord - start.xCoord;
        final double yDif = dst.yCoord - start.yCoord;
        final double zDif = dst.zCoord - start.zCoord;

        final double distXZ = Math.sqrt(xDif * xDif + zDif * zDif);

        return new float[]{
            (float) (Math.atan2(zDif, xDif) * RAD_TO_DEG) - 90.0F,
            (float) (-(Math.atan2(yDif, distXZ) * RAD_TO_DEG))
        };
    }

    public static float[] getRotations(final float[] lastRotations,
                                       final float smoothing,
                                       final Vec3 start,
                                       final Vec3 dst) {
        // Get rotations from start - dst
        final float[] rotations = getRotations(start, dst);
        // Apply smoothing to them
        applySmoothing(lastRotations, smoothing, rotations);
        return rotations;
    }

    public static void applySmoothing(final float[] lastRotations,
                                      final float smoothing,
                                      final float[] dstRotation) {
        if (smoothing > 0.0F) {
            final float yawChange = MathHelper.wrapAngleTo180_float(dstRotation[0] - lastRotations[0]);
            final float pitchChange = MathHelper.wrapAngleTo180_float(dstRotation[1] - lastRotations[1]);

            final float smoothingFactor = Math.max(1.0F, smoothing / 10.0F);

            dstRotation[0] = lastRotations[0] + yawChange / smoothingFactor;
            dstRotation[1] = Math.max(Math.min(90.0F, lastRotations[1] + pitchChange / smoothingFactor), -90.0F);
        }
    }

    public static double getMouseGCD() {
        final float sens = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float pow = sens * sens * sens * 8.0F;
        return (double) pow * 0.15;
    }

    public static void applyGCD(final float[] rotations,
                                final float[] prevRots) {
        final float yawDif = rotations[0] - prevRots[0];
        final float pitchDif = rotations[1] - prevRots[1];
        final double gcd = getMouseGCD();

        rotations[0] -= yawDif % gcd;
        rotations[1] -= pitchDif % gcd;
    }

    public static float calculateYawFromSrcToDst(final float yaw,
                                                 final double srcX,
                                                 final double srcZ,
                                                 final double dstX,
                                                 final double dstZ) {
        final double xDist = dstX - srcX;
        final double zDist = dstZ - srcZ;
        final float var1 = (float) (StrictMath.atan2(zDist, xDist) * 180.0 / Math.PI) - 90.0F;
        return yaw + MathHelper.wrapAngleTo180_float(var1 - yaw);
    }

    public enum RotationsPoint {
        CLOSEST("Closest", RotationUtil::getClosestPoint),
        HEAD("Head", (start, hitBox) -> {
            return RotationUtil.getCenterPointOnBB(hitBox, 0.9);
        }),
        CHEST("Chest", (start, hitBox) -> {
            return RotationUtil.getCenterPointOnBB(hitBox, 0.7);
        }),
        PELVIS("Pelvis", (start, hitBox) -> {
            return RotationUtil.getCenterPointOnBB(hitBox, 0.5);
        }),
        LEGS("Legs", (start, hitBox) -> {
            return RotationUtil.getCenterPointOnBB(hitBox, 0.3);
        }),
        FEET("Feet", (start, hitBox) -> {
            return RotationUtil.getCenterPointOnBB(hitBox, 0.1);
        });

        private final String name;
        private final BiFunction<Vec3, AxisAlignedBB, Vec3> getHitVecFunc;

        RotationsPoint(final String name, BiFunction<Vec3, AxisAlignedBB, Vec3> getHitVecFunc) {
            this.name = name;
            this.getHitVecFunc = getHitVecFunc;
        }

        public Vec3 getHitVec(final Vec3 start, final AxisAlignedBB hitBox) {
            return this.getHitVecFunc.apply(start, hitBox);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
