package best.azura.client.util.player;

import best.azura.client.impl.events.EventStrafe;
import best.azura.client.impl.module.impl.movement.Sprint;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.concurrent.ThreadLocalRandom;

public class RotationUtil {

    private final static Minecraft mc = Minecraft.getMinecraft();

    public static float[] faceVector(Vec3 vec) {
        double d0 = vec.xCoord - mc.thePlayer.posX;
        double d1 = vec.zCoord - mc.thePlayer.posZ;
        double d2 = vec.yCoord - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f = (float) (Math.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
        float f1 = (float) (-(Math.atan2(d2, d3) * (double) (180F / (float) Math.PI)));
        return new float[]{MathHelper.wrapAngleTo180_float(f), MathHelper.wrapAngleTo180_float(f1)};
    }

    public static Vec3 getClosestPoint(final Vec3 start, final AxisAlignedBB boundingBox) {
        final double closestX = start.xCoord >= boundingBox.maxX ? boundingBox.maxX : boundingBox.minX,
                closestY = start.yCoord >= boundingBox.maxY ? boundingBox.maxY :
                        start.yCoord <= boundingBox.minY ? boundingBox.minY : boundingBox.minY + (start.yCoord - boundingBox.minY),
                closestZ = start.zCoord >= boundingBox.maxZ ? boundingBox.maxZ : boundingBox.minZ;
        return new Vec3(closestX, closestY, closestZ);
    }

    public static void silentMoveFix(EventStrafe e, float yaw) {
        final float[] movementValues = getSilentMovementValues(yaw);
        float forward = movementValues[0], strafe = movementValues[1];
        Sprint.disable = false;
        if (forward < 0) {
            mc.thePlayer.setSprinting(false);
            mc.gameSettings.keyBindSprint.pressed = false;
            Sprint.disable = true;
        }
        e.forward = forward;
        e.strafe = strafe;
        {
           float f = strafe * strafe + forward * forward;

            if (f >= 1.0E-4F) {
                f = MathHelper.sqrt_float(f);

                if (f < 1.0F) {
                    f = 1.0F;
                }

                f = e.friction / f;
                strafe = strafe * f;
                forward = forward * f;
                float f1 = MathHelper.sin(yaw * (float) Math.PI / 180.0F);
                float f2 = MathHelper.cos(yaw * (float) Math.PI / 180.0F);
                mc.thePlayer.motionX +=  (strafe * f2 - forward * f1);
                mc.thePlayer.motionZ +=  (forward * f2 + strafe * f1);
            }
        }
        e.setCancelled(true);
    }

    public static float[] getSilentMovementValues(float yaw) {
        float diff = (mc.thePlayer.rotationYaw - yaw);
        float f = MathHelper.sin(diff * ((float) Math.PI / 180F));
        float f1 = MathHelper.cos(diff * ((float) Math.PI / 180F));
        float multiplier = 1f;
        if (mc.thePlayer.isSneaking() || mc.thePlayer.isUsingItem()) multiplier = 10;
        float forward = (float) (Math.round((mc.thePlayer.moveForward * (double) f1 + mc.thePlayer.moveStrafing * (double) f) * multiplier)) / multiplier;
        float strafe = (float) (Math.round((mc.thePlayer.moveStrafing * (double) f1 - mc.thePlayer.moveForward * (double) f) * multiplier)) / multiplier;
        forward *= 0.98;
        strafe *= 0.98;
        return new float[] { forward, strafe };
    }

    public static Vec3 getCenter(AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }
    public static Vec3 getFeet(AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * Double.MIN_VALUE, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static Vec3 getRandomCenter(AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * ThreadLocalRandom.current().nextDouble(0.2, 0.8),
                bb.minY + (bb.maxY - bb.minY) * ThreadLocalRandom.current().nextDouble(0.2, 0.8),
                bb.minZ + (bb.maxZ - bb.minZ) * ThreadLocalRandom.current().nextDouble(0.2, 0.8));
    }

    public static Vec3 getHead(AxisAlignedBB bb, double height) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * height, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static Vec3 getBlockVecCenter(BlockPos pos) {
        return (new Vec3(pos)).addVector(0.5D, 0.5D, 0.5D);
    }


    public static float[] mouseFix(float lastYaw, float lastPitch, float yaw, float pitch) {
        float f = 0.5f * 0.6F + 0.2F;
        float f1 = f * f * f * 8.0F;
        float f2 = MathHelper.wrapAngleTo180_float(yaw - lastYaw % 360f);
        float f3 = MathHelper.wrapAngleTo180_float(pitch - lastPitch % 360f);
        float f4 = (int) f2 * 8 * f1;
        float f5 = (int) f3 * 8 * f1;
        yaw = (float) ((double) lastYaw + (double) f4 * 0.15D);
        pitch = (float) ((double) lastPitch + (double) f5 * 0.15D);
        return new float[]{yaw, pitch};
    }

    public static float[] mouseFix(float yaw, float pitch) {
        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f1 = f * f * f * 8.0F;
        yaw -= yaw % f1;
        pitch -= pitch % f1;
        return new float[]{yaw, pitch};
    }

    public static float[] getNeededRotations(Vec3 vec) {
        Vec3 playerVector = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        double y = vec.yCoord - playerVector.yCoord;
        double x = vec.xCoord - playerVector.xCoord;
        double z = vec.zCoord - playerVector.zCoord;
        double dff = Math.sqrt(x * x + z * z);
        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90;
        float pitch = (float) -Math.toDegrees(Math.atan2(y, dff));
        return new float[]{updateRotation(yaw, yaw, 180), updateRotation(pitch, pitch, 90)};
    }

    public static float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_) {
        float f = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);

        if (f > p_75652_3_)
        {
            f = p_75652_3_;
        }

        if (f < -p_75652_3_)
        {
            f = -p_75652_3_;
        }

        return p_75652_1_ + f;
    }

}
