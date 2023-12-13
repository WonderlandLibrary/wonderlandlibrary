package vip.radium.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import vip.radium.event.impl.player.UpdatePositionEvent;

public final class RotationUtils {

    public static final double TO_RADS = 180.0D / StrictMath.PI;

    private RotationUtils() {
    }

    public static float getOldYaw(Entity entity) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        return getYawBetween(player.prevRotationYaw,
                player.prevPosX, player.prevPosZ,
                entity.prevPosX, entity.prevPosZ);
    }

    public static float getYawToEntity(Entity entity) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        return getYawBetween(player.rotationYaw,
                player.posX, player.posZ,
                entity.posX, entity.posZ);
    }

    public static void rotate(final UpdatePositionEvent event, final float[] rotations, final float aimSpeed, boolean lockview) {
        final float[] prevRotations = {event.getPrevYaw(), event.getPrevPitch()};

        final float[] cappedRotations = {
                maxAngleChange(prevRotations[0], rotations[0], aimSpeed),
                maxAngleChange(prevRotations[1], rotations[1], aimSpeed)
        };

        final float[] appliedRotations = RotationUtils.applyGCD(cappedRotations, prevRotations);

        event.setYaw(appliedRotations[0]);
        event.setPitch(appliedRotations[1]);

        if (lockview) {
            Wrapper.getPlayer().rotationYaw = appliedRotations[0];
            Wrapper.getPlayer().rotationPitch = appliedRotations[1];
        }
    }

    private static float maxAngleChange(final float prev, final float now, final float maxTurn) {
        float dif = MathHelper.wrapAngleTo180_float(now - prev);
        if (dif > maxTurn) dif = maxTurn;
        if (dif < -maxTurn) dif = -maxTurn;
        return prev + dif;
    }

    public static float[] getRotationsToEntity(Entity entity) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        final double xDif = entity.posX - player.posX;
        final double zDif = entity.posZ - player.posZ;

        final AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.1F, 0.1F, 0.1F);
        final double playerEyePos = (player.posY + player.getEyeHeight());
        final double yDif = playerEyePos > entityBB.maxY ? entityBB.maxY - playerEyePos : // Higher than max, aim at max
                playerEyePos < entityBB.minY ? entityBB.minY - playerEyePos : // Lower than min, aim at min
                        0; // Else aim straight

        final double fDist = MathHelper.sqrt_double(xDif * xDif + zDif * zDif);

        return new float[]{
                (float) (StrictMath.atan2(zDif, xDif) * RotationUtils.TO_RADS) - 90.0F,
                (float) (-(StrictMath.atan2(yDif, fDist) * RotationUtils.TO_RADS))
        };
    }

    public static double getMouseGCD() {
        final float sens = Wrapper.getGameSettings().mouseSensitivity * 0.6F + 0.2F;
        final float pow = sens * sens * sens * 8.0F;
        return pow * 0.15D;
    }

    public static float[] applyGCD(final float[] rotations, final float[] prevRots) {
        final float yawDif = rotations[0] - prevRots[0];
        final float pitchDif = rotations[1] - prevRots[1];
        final double gcd = getMouseGCD();

        rotations[0] -= yawDif % gcd;
        rotations[1] -= pitchDif % gcd;
        return rotations;
    }

    public static float getYawBetween(final float yaw,
                                      final double srcX,
                                      final double srcZ,
                                      final double destX,
                                      final double destZ) {
        final double xDist = destX - srcX;
        final double zDist = destZ - srcZ;
        final float var1 = (float) (StrictMath.atan2(zDist, xDist) * 180.0D / Math.PI) - 90.0F;
        return yaw + MathHelper.wrapAngleTo180_float(var1 - yaw);
    }
}
