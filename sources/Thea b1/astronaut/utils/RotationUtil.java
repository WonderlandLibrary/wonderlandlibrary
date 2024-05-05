package astronaut.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class RotationUtil {
    public static float yaw;
    public static float renderYawOffset;
    public static float pitch;
    public static boolean RotationInUse;
    public static float friction = 0;
    public static float strafe = 0;
    public static float forward = 0;
    public static float f1 = 0;
    public static float f2 = 0;

    public static float[] basicRotation(final EntityPlayerSP player, final EntityLivingBase target) {
        final double posX =  target.posX - player.posX ;
        final double posY =  target.posY + target.getEyeHeight() - (player.posY  + player.getEyeHeight() + 0.5);
        final double posZ =  target.posZ - player.posZ;
        final double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
        float yaw = (float) (Math.atan2(posZ, posX) * 180 / Math.PI) - 90;
        float pitch = (float) -(Math.atan2(posY, var14) * 180 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static int getRotation(double before, float after) {

        while (before > 360.0D) {
            before -= 360.0D;
        }
        while (before < 0.0D) {
            before += 360.0D;
        }
        while (after > 360.0F) {
            after -= 360.0F;
        }
        while (after < 0.0F) {
            after += 360.0F;
        }
        if (before > after) {
            if (before - after > 180.0D) {
                return 1;
            }
            return -1;
        }
        if (after - before > 180.0D) {
            return -1;
        }
        return 1;
    }

    public static boolean setYaw(float y, float speed) {

        setRotation(yaw, pitch);
        if (speed >= 360.0F) {
            yaw = y;
            return true;
        }

        if ((isInRange(yaw, y, speed)) || (speed >= 360.0F)) {
            yaw = y;
            return true;
        }
        if (getRotation(yaw, y) < 0) {
            yaw = yaw - speed;
        } else {
            yaw = yaw + speed;
        }
        return false;
    }

    public static boolean isInRange(double before, float after, float max) {
        while (before > 360.0D)
            before -= 360.0D;
        while (before < 0.0D)
            before += 360.0D;
        while (after > 360.0F)
            after -= 360.0F;
        while (after < 0.0F)
            after += 360.0F;
        if (before > after) {
            if ((before - after > 180.0D) && (360.0D - before - after <= max))
                return true;
            return before - after <= max;
        } else {
            if ((after - before > 180.0D) && (360.0F - after - before <= max))
                return true;
            return after - before <= max;
        }
    }

    public static boolean setPitch(float p, float speed) {

        if (p > 90.0F)
            p = 90.0F;
        else if (p < -90.0F)
            p = -90.0F;

        if ((Math.abs(pitch - p) <= speed) || (speed >= 360.0F)) {
            pitch = p;
            return false;
        }

        if (p < pitch)
            pitch = pitch - speed;
        else
            pitch = pitch + speed;

        return true;
    }

    public static void setRotation(float y, float p) {
        if (p > 90.0F)
            p = 90.0F;
        else if (p < -90.0F)
            p = -90.0F;

        yaw = y;
        pitch = p;
        RotationInUse = true;
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
}
