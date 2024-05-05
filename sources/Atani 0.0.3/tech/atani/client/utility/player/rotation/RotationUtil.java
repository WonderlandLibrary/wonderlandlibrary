package tech.atani.client.utility.player.rotation;

import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.random.RandomUtil;
import tech.atani.client.utility.player.PlayerHandler;
import tech.atani.client.utility.player.raytrace.RaytraceUtil;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RotationUtil implements Methods {

    public static Vec3 getBestVector(Vec3 look, AxisAlignedBB axisAlignedBB) {
        return new Vec3(MathHelper.clamp(look.xCoord, axisAlignedBB.minX, axisAlignedBB.maxX), MathHelper.clamp(look.yCoord, axisAlignedBB.minY, axisAlignedBB.maxY), MathHelper.clamp(look.zCoord, axisAlignedBB.minZ, axisAlignedBB.maxZ));
    }

    public static Vec3 toDirection(float yaw, float pitch) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    public static Vec3 getVectorForRotation(float yaw, float pitch) {
        float f = MathHelper.cos((float) (-yaw * 0.017163291F - Math.PI));
        float f2 = MathHelper.sin((float) (-yaw * 0.017163291F - Math.PI));
        float f3 = -MathHelper.cos(-pitch * 0.017163291F);
        float f4 = MathHelper.sin(-pitch * 0.017163291F);
        return new Vec3(f2 * f3, f4, f * f3);
    }

    public static float[] getRotation(Vec3 aimVector) {
        double x = aimVector.xCoord - mc.thePlayer.posX;
        double y = aimVector.yCoord - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double z = aimVector.zCoord - mc.thePlayer.posZ;

        double d3 = MathHelper.sqrt(x * x + z * z);
        float f = (float) (MathHelper.atan2(z, x) * (180 / Math.PI)) - 90.0F;
        float f1 = (float) (-(MathHelper.atan2(y, d3) * (180 / Math.PI)));
        f1 = MathHelper.clamp(f1, -90, 90);
        return new float[]{f, f1};
    }

    public static float[] getRotation(Entity entity, String vectorMode, float heightDivisor, boolean mouseFix, boolean heuristics, double minRandomYaw, double maxRandomYaw, double minRandomPitch, double maxRandomPitch, boolean prediction, float minYaw, float maxYaw, float minPitch, float maxPitch, boolean snapYaw, boolean snapPitch) {
        Vec3 aimVector = getBestVector(mc.thePlayer.getPositionEyes(1F), entity.getEntityBoundingBox());
        switch (vectorMode) {
            case "Bruteforce":
                for (double yPercent = 1; yPercent >= 0; yPercent -= 0.25) {
                    for (double xPercent = 1; xPercent >= -0.5; xPercent -= 0.5) {
                        for (double zPercent = 1; zPercent >= -0.5; zPercent -= 0.5) {
                            Vec3 tempVec = new Vec3(xPercent, yPercent, zPercent);
                            if (RaytraceUtil.rayCast(1F, getRotation(tempVec)).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                                aimVector = tempVec;
                            }
                        }
                    }
                }
                break;
            case "Head":
                aimVector = new Vec3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
                break;
            case "Torso":
                aimVector = new Vec3(entity.posX, entity.posY + entity.getEyeHeight() / 2d, entity.posZ);
                break;
            case "Feet":
                aimVector = new Vec3(entity.posX, entity.posY, entity.posZ);
                break;
            case "Custom":
                aimVector = new Vec3(entity.posX, entity.posY + entity.getEyeHeight() / heightDivisor, entity.posZ);
                break;
            case "Random":
                double x = RandomUtil.randomBetween(0, 0.4) - 0.2;
                double y = RandomUtil.randomBetween(0, 1) + 1;
                double z = RandomUtil.randomBetween(0, 0.4) - 0.2;
                aimVector = new Vec3(entity.posX + x, entity.posY + y, entity.posZ + z);
                break;
        }
        aimVector.xCoord += RandomUtil.randomBetween(minRandomYaw, maxRandomYaw);
        aimVector.yCoord += RandomUtil.randomBetween(minRandomPitch, maxRandomPitch);
        aimVector.zCoord += RandomUtil.randomBetween(minRandomYaw, maxRandomYaw);
        double x = aimVector.xCoord - mc.thePlayer.posX;
        double y = aimVector.yCoord - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double z = aimVector.zCoord - mc.thePlayer.posZ;

        if (prediction) {
            final boolean targetIsSprinting = entity.isSprinting();
            final boolean playerIsSprinting = mc.thePlayer.isSprinting();

            final float walkingSpeed = 0.10000000149011612f;
            final float targetSpeed = targetIsSprinting ? 1.25f : walkingSpeed;
            final float playerSpeed = playerIsSprinting ? 1.25f : walkingSpeed;

            final float targetPredictedX = (float) ((entity.posX - entity.prevPosX) * targetSpeed);
            final float targetPredictedZ = (float) ((entity.posZ - entity.prevPosZ) * targetSpeed);
            final float playerPredictedX = (float) ((mc.thePlayer.posX - mc.thePlayer.prevPosX) * playerSpeed);
            final float playerPredictedZ = (float) ((mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * playerSpeed);

            if (targetPredictedX != 0.0f && targetPredictedZ != 0.0f || playerPredictedX != 0.0f && playerPredictedZ != 0.0f) {
                x += targetPredictedX + playerPredictedX;
                z += targetPredictedZ + playerPredictedZ;
            }
        }

        if (heuristics) {
            try {
                x += SecureRandom.getInstanceStrong().nextDouble() * 0.1;
                y += SecureRandom.getInstanceStrong().nextDouble() * 0.1;
                z += SecureRandom.getInstanceStrong().nextDouble() * 0.1;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        double d3 = MathHelper.sqrt(x * x + z * z);
        float yawSpeed = (float) RandomUtil.randomBetween(minYaw, maxYaw);
        float pitchSpeed = (float) RandomUtil.randomBetween(minPitch, maxPitch);
        float f = (float) (MathHelper.atan2(z, x) * (180 / Math.PI)) - 90.0F;
        float f1 = (float) (-(MathHelper.atan2(y, d3) * (180 / Math.PI)));
        final float deltaYaw = (((f - PlayerHandler.yaw) + 540) % 360) - 180;
        final float deltaPitch = f1 - PlayerHandler.pitch;
        final float yawDistance = MathHelper.clamp_float(deltaYaw, -yawSpeed, yawSpeed);
        final float pitchDistance = MathHelper.clamp_float(deltaPitch, -pitchSpeed, pitchSpeed);
        float calcYaw = snapYaw ? f : PlayerHandler.yaw + yawDistance;
        float calcPitch = snapPitch ? f1 : PlayerHandler.pitch + pitchDistance;
        calcPitch = MathHelper.clamp(calcPitch, -90, 90);
        if (!mouseFix)
            return new float[]{calcYaw, calcPitch};
        return applyMouseFix(calcYaw, calcPitch);
    }

    public static float[] applyMouseFix(float newYaw, float newPitch) {
        final float sensitivity = Math.max(0.001F, mc.gameSettings.mouseSensitivity);
        final int deltaYaw = (int) ((newYaw - PlayerHandler.yaw) / ((sensitivity * (sensitivity >= 0.5 ? sensitivity : 1) / 2)));
        final int deltaPitch = (int) ((newPitch - PlayerHandler.pitch) / ((sensitivity * (sensitivity >= 0.5 ? sensitivity : 1) / 2))) * -1;
        final float f = sensitivity * 0.6F + 0.2F;
        final float f1 = f * f * f * 8.0F;
        final float f2 = (float) deltaYaw * f1;
        final float f3 = (float) deltaPitch * f1;

        final float endYaw = (float) ((double) PlayerHandler.yaw + (double) f2 * 0.15);
        float endPitch = (float) ((double) PlayerHandler.pitch - (double) f3 * 0.15);
        endPitch = MathHelper.clamp(endPitch, -90, 90);
        return new float[]{endYaw, endPitch};
    }

    public static float[] updateRotationSimple(float newYaw, float newPitch, float speed) {
        return RotationUtil.updateRotationSimple(PlayerHandler.yaw, newYaw, speed, PlayerHandler.pitch, newPitch, speed);
    }

    public static float[] updateRotationSimple(float oldYaw, float newYaw, float yawSpeed, float oldPitch, float newPitch, float pitchSpeed) {
        return new float[] {updateRotation(oldYaw, newYaw, yawSpeed), updateRotation(oldPitch, newPitch, pitchSpeed)};
    }

    public static float getDistanceToLastPitch(final float pitch) {
        return Math.abs(pitch - PlayerHandler.pitch);
    }

    public static void resetRotations(float yaw, float pitch, boolean silent) {
        if(silent) {
            mc.thePlayer.rotationYaw = yaw - yaw % 360 + mc.thePlayer.rotationYaw % 360;
        } else {
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
        }
    }

    public static float updateRotation(float oldRot, float newRot, float speed) {
        float f = MathHelper.wrapDegrees(newRot - oldRot);

        if (f > (float) speed) {
            f = (float) speed;
        }

        if (f < -(float) speed) {
            f = -(float) speed;
        }

        return oldRot + f;
    }

}
