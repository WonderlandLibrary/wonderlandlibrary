package de.tired.util.combat.rotation;


import de.tired.util.math.MathUtil;
import de.tired.base.interfaces.IHook;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;

public class NewRotationUtil implements IHook {


    private static double heuristicsX;
    private static double heuristicsY;
    private static double heuristicsZ;


    public static Rotation smoothRotation(final Rotation currentRotation, final Rotation targetRotation, float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw()) * MC.timer.renderPartialTicks;
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch()) * MC.timer.renderPartialTicks;

        return new Rotation(currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)));
    }

    public static float[] getRotations(BlockPos pos) {
        final Vec3 playerPos = new Vec3(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ);

        final double diffX = pos.getX() + 0.5 - playerPos.xCoord;
        final double diffY = pos.getY() + 0.5 - (playerPos.yCoord + MC.thePlayer.getEyeHeight());
        final double diffZ = pos.getZ() + 0.5 - playerPos.zCoord;

        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f);
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, dist));
        yaw = (float) (MC.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_double(yaw - MC.thePlayer.rotationYaw));
        pitch = (float) (MC.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_double(pitch - MC.thePlayer.rotationPitch));

        return new float[]{yaw, pitch};
    }

    private static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

    public static Rotation makeRotationScaffold(final BlockPos rotationVector, boolean randomAim, double yTranslation, boolean prediction) {
        double x = (rotationVector.getX() + (randomAim ? MathUtil.getRandom(0.45D, 0.5D) : 0.5D)) - MC.thePlayer.posX - (prediction ? MC.thePlayer.motionX : 0);
        double y = (rotationVector.getY() - yTranslation) - (MC.thePlayer.posY + MC.thePlayer.getEyeHeight());
        double z = (rotationVector.getZ() + (randomAim ? MathUtil.getRandom(0.45D, 0.5D) : 0.5D)) - MC.thePlayer.posZ - (prediction ? MC.thePlayer.motionZ : 0);


        return new Rotation((float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90f), (float) MathHelper.wrapDegrees((-Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z))))));
    }


    public static Rotation makeRotation(final Vec3 rotationVector, final Vec3 eyeVector) {
        final double diffX = rotationVector.xCoord - eyeVector.xCoord;
        final double diffY = rotationVector.yCoord - eyeVector.yCoord;
        final double diffZ = rotationVector.zCoord - eyeVector.zCoord;


        return new Rotation((float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90f), (float) MathHelper.wrapDegrees((-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }


    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }


    public static VectorRotation blockRotationNew(BlockPos blockPos, boolean checks, float speed) {

        final Vec3 eyesPos = new Vec3(MC.thePlayer.posX, MC.thePlayer.getEntityBoundingBox().minY + MC.thePlayer.getEyeHeight(), MC.thePlayer.posZ);

        Rotation rotationData = null;

        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = blockPos.offset(side);

            if (!canBeClicked(neighbor))
                continue;

            final Vec3 dirVec = new Vec3(side.getDirectionVec());

            for (double xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
                for (double ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
                    for (double zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
                        final Vec3 posVec = new Vec3(blockPos).addVector(xSearch, ySearch, zSearch);
                        final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                        final Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.5, dirVec.yCoord * 0.5, dirVec.zCoord * 0.5));
                        if (checks && (eyesPos.squareDistanceTo(hitVec) > 18D || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) || MC.theWorld.rayTraceBlocks(eyesPos, hitVec, false, true, false) != null))
                            continue;

                        for (int i = 0; i < (1); i++) {
                            final double diffX = hitVec.xCoord - eyesPos.xCoord;
                            final double diffY = hitVec.yCoord - eyesPos.yCoord;
                            final double diffZ = hitVec.zCoord - eyesPos.zCoord;
                            final double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);

                            Rotation rotation = new Rotation(
                                    MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F),
                                    MathHelper.wrapAngleTo180_float((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)))
                            );

                            rotationData = new Rotation(rotation.getYaw(), rotation.getPitch());

                            final Vec3 rotationVector = getVectorForRotation(rotation);
                            final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * 4, rotationVector.yCoord * 4, rotationVector.zCoord * 4);
                            final MovingObjectPosition obj = MC.theWorld.rayTraceBlocks(eyesPos, vector, false, false, true);
                            if (!(obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && obj.getBlockPos().equals(neighbor)))
                                continue;


                            return new VectorRotation(rotationData, null);

                        }

                    }
                }
            }

        }

        return null;
    }

    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(
                currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)
                ));
    }

    public static Vec3 getVectorForRotation(final Rotation rotation) {
        float yawCos = MathHelper.cos(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
        float yawSin = MathHelper.sin(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
        float pitchCos = -MathHelper.cos(-rotation.getPitch() * 0.017453292F);
        float pitchSin = MathHelper.sin(-rotation.getPitch() * 0.017453292F);
        return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }

    private static Block getBlock(BlockPos pos) {
        return MC.theWorld.getBlockState(pos).getBlock();
    }

    public static boolean canBeClicked(BlockPos blockPos) {
        return getBlock(blockPos).canCollideCheck(MC.theWorld.getBlockState(blockPos), false) && MC.theWorld.worldBorder.contains(blockPos);
    }

    public static VectorRotation rotation(final Entity target, final boolean throughWalls, final double range, final boolean blockRaycast, final boolean outrange, final boolean predict, String mode, boolean heuristics) {
        final Vec3 eyePos = MC.thePlayer.getPositionEyes(MC.timer.renderPartialTicks);
        final AxisAlignedBB box = target.getEntityBoundingBox();

        final Vec3 targetPos = getBestVector(MC.thePlayer.getPositionEyes(MC.timer.renderPartialTicks), box);
        final Rotation preferredRotation = makeRotation(targetPos, eyePos);

        VectorRotation vecRotation = null;


        double xMin = 0.15D;
        double yMin = 0.15D;
        double zMin = 0.15D;
        double xMax = 0.85D;
        double yMax = 1.00D;
        double zMax = 0.85D;
        double xDist = 0.1D;
        double yDist = 0.1D;
        double zDist = 0.1D;


        switch (mode) {
            case "CenterLine": {
                xMin = 0.45D;
                xMax = 0.55D;
                xDist = 0.0125D;
                yMin = 0.10D;
                yMax = 0.90D;
                zMin = 0.45D;
                zMax = 0.55D;
                zDist = 0.0125D;
                break;

            }

            case "CenterSimple": {
                xMin = 0.45D;
                xMax = 0.55D;
                xDist = 0.0125D;
                yMin = 0.65D;
                yMax = 0.75D;
                yDist = 0.0125D;
                zMin = 0.45D;
                zMax = 0.55D;
                zDist = 0.0125D;
                break;
            }
        }

        double minX = target.boundingBox.minX - target.posX;
        double maxX = target.boundingBox.maxX - target.posX;
        double minY = target.boundingBox.minY - target.posY;
        double maxY = target.boundingBox.maxY - target.posY;
        double minZ = target.boundingBox.minZ - target.posZ;
        double maxZ = target.boundingBox.maxZ - target.posZ;
        if (heuristics) {
            heuristicsX = MathUtil.getRandomSin(minX, maxX, 300) + Math.random() / 750.0D;
            heuristicsY = MathUtil.getRandomSin(minY - 1, maxY - 1, 500) + Math.random() / 450.0D;
            heuristicsZ = MathUtil.getRandomSin(minZ, maxZ, 200) + Math.random() / 250.0D;
        } else {
            heuristicsX = 0.0D;
            heuristicsY = 0.0D;
            heuristicsZ = 0.0D;
        }

        for (double xSearch = xMin; xSearch < xMax; xSearch += xDist) {
            for (double ySearch = yMin; ySearch < yMax; ySearch += yDist) {
                for (double zSearch = zMin; zSearch < zMax; zSearch += zDist) {
                    final Vec3 vec3d = new Vec3((box.minX + (box.maxX - box.minX) * xSearch) + heuristicsX, (box.minY + (box.maxY - box.minY) * ySearch) + heuristicsY, (box.minZ + (box.maxZ - box.minZ) * zSearch) + heuristicsZ);

                    if (eyePos.distanceTo(vec3d) > range)
                        continue;

                    if (predict) { //Todo improve this
                        eyePos.addVector(MC.thePlayer.motionX, MC.thePlayer.motionY > 0.01 ? MC.thePlayer.motionY : 0.0, MC.thePlayer.motionZ);

                        targetPos.addVector(target.motionX, target.motionY > 0.01 ? target.motionY : 0.0, target.motionZ);
                    }

                    final boolean visible = isVisible(eyePos, vec3d, blockRaycast);
                    final Rotation rotation = makeRotation(vec3d, eyePos);

                    if (throughWalls || visible) {
                        final VectorRotation currentVec = new VectorRotation(rotation, vec3d);

                        if (vecRotation == null || getRotationDifference(currentVec, preferredRotation) < getRotationDifference(vecRotation, preferredRotation)) {
                            if (vecRotation != null && outrange) {
                                if (eyePos.distanceTo(vec3d) >= eyePos.distanceTo(vecRotation.getVector())) {
                                    vecRotation = currentVec;
                                }
                            } else vecRotation = currentVec;
                        }
                    }
                }
            }
        }

        return vecRotation;
    }

    public static boolean isVisible(final Vec3 eyePos, final Vec3 vec3d, final boolean blockRaycast) {
        final MovingObjectPosition rayTraceResult = MC.theWorld.rayTraceBlocks(eyePos, vec3d);
        return rayTraceResult == null || (blockRaycast && rayTraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY);
    }

    public static boolean canHit(final Vec3 eyePos, final AxisAlignedBB box, final boolean blockRaycast) {
        for (double x = 0.1; x < 0.9; x += 0.1) {
            for (double y = 0.1; y < 0.9; y += 0.1) {
                for (double z = 0.1; z < 0.9; z += 0.1) {
                    if (isVisible(eyePos, new Vec3(box.minX + (box.maxX - box.minX) * x,
                            box.minY + (box.maxY - box.minY) * y,
                            box.minZ + (box.maxZ - box.minZ) * z), blockRaycast)) return true;
                }
            }
        }
        return false;
    }

    public static Vec3 getBestVector(final Vec3 look, final AxisAlignedBB axisAlignedBB) {
        return new Vec3(MathHelper.clamp_double(look.xCoord, axisAlignedBB.minX, axisAlignedBB.maxX), MathHelper.clamp_double(look.yCoord, axisAlignedBB.minY, axisAlignedBB.maxY), MathHelper.clamp_double(look.zCoord, axisAlignedBB.minZ, axisAlignedBB.maxZ));
    }

}