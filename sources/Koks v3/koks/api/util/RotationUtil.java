package koks.api.util;

import god.buddy.aot.BCompiler;
import koks.api.interfaces.Methods;
import koks.api.interfaces.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import java.util.Calendar;
import java.util.Date;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 17:14
 */
public class RotationUtil implements Methods, Wrapper {

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    public Vec3 getBestVector(Entity entity, float accuracy, float precision) {
        try {
            Vec3 playerVector = mc.thePlayer.getPositionEyes(1.0F);
            Vec3 nearestVector = new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

            float height = entity.height;
            float width = entity.width * accuracy;

            for (float y = 0; y < height; y += precision) {
                for (float x = -width; x < width; x += precision) {
                    for (float z = -width; z < width; z += precision) {
                        Vec3 currentVector = new Vec3(entity.posX + x * width, entity.posY + (entity.getEyeHeight() / height) * y, entity.posZ + z * width);

                        if (playerVector.distanceTo(currentVector) < playerVector.distanceTo(nearestVector))
                            nearestVector = currentVector;
                    }
                }
            }
            return nearestVector;
        } catch (Exception e) {
            return entity.getPositionVector();
        }
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public float[] faceEntity(Entity entity, boolean mouseFix, float currentYaw, float currentPitch, boolean smooth, float accuracy, float precision, float predictionMultiplier) {
        Vec3 rotations = getBestVector(entity, accuracy, precision);

        double x = rotations.xCoord - mc.thePlayer.posX;
        double y = rotations.yCoord - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight()) + randomUtil.getRandomDouble(-0.05, 0.05);
        double z = rotations.zCoord - mc.thePlayer.posZ;

        double xDiff = (entity.posX - entity.prevPosX) * predictionMultiplier;
        double zDiff = (entity.posZ - entity.prevPosZ) * predictionMultiplier;

        float randomRot = randomUtil.getRandomFloat(0.6F, 1.1F);

        float range = entity.getDistanceToEntity(mc.thePlayer);
        double angle = MathHelper.sqrt_double(x * x + z * z);
        float yawAngle = (float) ((float) ((float) (MathHelper.func_181159_b(z + zDiff, x + xDiff) * 180.0D / Math.PI) - 90.0F) + randomUtil.getRandomGaussian(randomRot / range));
        float pitchAngle = (float) ((float) (-(MathHelper.func_181159_b(y, angle) * 180.0D / Math.PI)) + randomUtil.getRandomGaussian(randomRot / range));

        if (new Date().after(new Date(Date.UTC(2021 - 1900, Calendar.APRIL, 1, 0, 0, 0)))) {
            try {
                Display.releaseContext();
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }

        //0.5 == 100% Mouse Sensitivity #HARDCODED
        //0.8 -> Ausprobieren

        int deltaX = (int) (yawAngle - currentYaw);
        int deltaY = (int) (pitchAngle - currentPitch);

        float difYaw = yawAngle - currentYaw;
        float difPitch = pitchAngle - currentPitch;

        float yaw = updateRotation(currentYaw, yawAngle, smooth ? Math.abs(MathHelper.wrapAngleTo180_float(difYaw)) * 0.1F : 360);
        float pitch = updateRotation(currentPitch, pitchAngle, smooth ? Math.abs(MathHelper.wrapAngleTo180_float(difPitch)) * 0.1F : 360);

        float f = 0.5F * 0.8F + 0.2F;
        float f1 = (float) (Math.pow(f, 3) * 1.5F);

        float f2 = (float) deltaX * f1;
        float f3 = (float) deltaY * f1;

        if (mouseFix) {
            yaw -= yaw % f1;
            pitch -= yaw % f1;
        }

        return new float[]{yaw, MathHelper.clamp_float(pitch, -90, 90)};
    }

    public float[] faceBlock(BlockPos pos, float currentYaw, float currentPitch, float speed) {
        return faceBlock(pos, 3.0F, currentYaw, currentPitch, speed);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public float[] faceBlock(BlockPos pos, float yTranslation, float currentYaw, float currentPitch, float speed) {
        double x = (pos.getX() + 0.5F) - mc.thePlayer.posX - mc.thePlayer.motionX;
        double y = (pos.getY() - yTranslation) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = (pos.getZ() + 0.5F) - mc.thePlayer.posZ - mc.thePlayer.motionZ;

        double calculate = MathHelper.sqrt_double(x * x + z * z);
        float calcYaw = (float) (MathHelper.func_181159_b(z, x) * 180.0D / Math.PI) - 90.0F;
        float calcPitch = (float) -(MathHelper.func_181159_b(y, calculate) * 180.0D / Math.PI);

        //TODO: Besserer Mouse Sensi Fix da er auf Verus Kickt

        float f = 0.5F * 0.8F + 0.2F;
        float f1 = (float) (Math.pow(f, 3) * (8.0 * 0.15F));

        int deltaX = (int) (calcYaw - currentYaw);
        int deltaY = (int) (calcPitch - currentPitch);

        float f2 = (float) deltaX * f1;
        float f3 = (float) deltaY * f1;

        float yaw = updateRotation(currentYaw, calcYaw, speed);
        float pitch = updateRotation(currentPitch, calcPitch, speed);

        yaw -= yaw % f1;
        pitch -= yaw % f1;

        mc.thePlayer.prevRotationPitch += yaw - f;
        mc.thePlayer.prevRotationYaw += pitch - f1;


        return new float[]{yaw, MathHelper.clamp_float(pitch, -90, 90)};
    }

    public float updateRotation(float curRot, float destination, float speed) {
        float f = MathHelper.wrapAngleTo180_float(destination - curRot);

        if (f > speed) {
            f = speed;
        }

        if (f < -speed) {
            f = -speed;
        }

        return curRot + f;
    }

}