package intent.AquaDev.aqua.utils;

import intent.AquaDev.aqua.Aqua;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class RotationUtil {
    public static float yaw;
    public static float renderYawOffset;
    public static float pitch;
    public static boolean RotationInUse;
    static Minecraft mc;

    public static float[] Intavee(EntityPlayerSP player, EntityLivingBase target) {
        float yawRandom = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)-0.1, (double)0.1);
        float pitchRandom = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)-0.01, (double)0.01);
        double posX = Aqua.setmgr.getSetting("KillauraRandom").isState() ? target.posX - player.posX + (double)yawRandom : target.posX - player.posX;
        double posY = Aqua.setmgr.getSetting("KillauraRandom").isState() ? target.posY + (double)target.getEyeHeight() - (player.posY + (double)player.getEyeHeight() - (double)pitchRandom) : target.posY + (double)target.getEyeHeight() - (player.posY + (double)player.getEyeHeight());
        double posZ = Aqua.setmgr.getSetting("KillauraRandom").isState() ? target.posZ - player.posZ - (double)yawRandom : target.posZ - player.posZ;
        double var14 = MathHelper.sqrt_double((double)(posX * posX + posZ * posZ));
        float yaw = (float)(Math.atan2((double)posZ, (double)posX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2((double)posY, (double)var14) * 180.0 / Math.PI));
        if (Aqua.setmgr.getSetting("KillauraMouseSensiFix").isState()) {
            float f2 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f3 = f2 * f2 * f2 * 1.2f;
            yaw -= yaw % f3;
            pitch -= pitch % (f3 * f2);
        }
        return new float[]{yaw, pitch};
    }

    public static void setRotation(float y, float p) {
        if (p > 90.0f) {
            p = 90.0f;
        } else if (p < -90.0f) {
            p = -90.0f;
        }
        yaw = y;
        pitch = p;
        RotationInUse = true;
    }

    public static boolean setYaw(float y, float speed) {
        RotationUtil.setRotation(yaw, pitch);
        if (speed >= 360.0f) {
            yaw = y;
            return true;
        }
        if (RotationUtil.isInRange(yaw, y, speed) || speed >= 360.0f) {
            yaw = y;
            return true;
        }
        yaw = RotationUtil.getRotation(yaw, y) < 0 ? (yaw -= speed) : (yaw += speed);
        return false;
    }

    public static int getRotation(double before, float after) {
        while (before > 360.0) {
            before -= 360.0;
        }
        while (before < 0.0) {
            before += 360.0;
        }
        while (after > 360.0f) {
            after -= 360.0f;
        }
        while (after < 0.0f) {
            after += 360.0f;
        }
        if (before > (double)after) {
            if (before - (double)after > 180.0) {
                return 1;
            }
            return -1;
        }
        if ((double)after - before > 180.0) {
            return -1;
        }
        return 1;
    }

    public static boolean isInRange(double before, float after, float max) {
        while (before > 360.0) {
            before -= 360.0;
        }
        while (before < 0.0) {
            before += 360.0;
        }
        while (after > 360.0f) {
            after -= 360.0f;
        }
        while (after < 0.0f) {
            after += 360.0f;
        }
        if (before > (double)after) {
            if (before - (double)after > 180.0 && 360.0 - before - (double)after <= (double)max) {
                return true;
            }
            return before - (double)after <= (double)max;
        }
        if ((double)after - before > 180.0 && (double)(360.0f - after) - before <= (double)max) {
            return true;
        }
        return (double)after - before <= (double)max;
    }

    public static boolean setPitch(float p, float speed) {
        if (p > 90.0f) {
            p = 90.0f;
        } else if (p < -90.0f) {
            p = -90.0f;
        }
        if (Math.abs((float)(pitch - p)) <= speed || speed >= 360.0f) {
            pitch = p;
            return false;
        }
        pitch = p < pitch ? (pitch -= speed) : (pitch += speed);
        return true;
    }

    public static float calculateCorrectYawOffset(float yaw) {
        float offsetDiff;
        float yawOffsetDiff;
        float renderYawOffset;
        double xDiff = RotationUtil.mc.thePlayer.posX - RotationUtil.mc.thePlayer.prevPosX;
        double zDiff = RotationUtil.mc.thePlayer.posZ - RotationUtil.mc.thePlayer.prevPosZ;
        float dist = (float)(xDiff * xDiff + zDiff * zDiff);
        float offset = renderYawOffset = RotationUtil.mc.thePlayer.renderYawOffset;
        if (dist > 0.0025000002f) {
            offset = (float)MathHelper.func_181159_b((double)zDiff, (double)xDiff) * 180.0f / (float)Math.PI - 90.0f;
        }
        if (RotationUtil.mc.thePlayer != null && RotationUtil.mc.thePlayer.swingProgress > 0.0f) {
            offset = yaw;
        }
        if ((yawOffsetDiff = MathHelper.wrapAngleTo180_float((float)(yaw - (renderYawOffset += (offsetDiff = MathHelper.wrapAngleTo180_float((float)(offset - renderYawOffset))) * 0.3f)))) < -75.0f) {
            yawOffsetDiff = -75.0f;
        }
        if (yawOffsetDiff >= 75.0f) {
            yawOffsetDiff = 75.0f;
        }
        renderYawOffset = yaw - yawOffsetDiff;
        if (yawOffsetDiff * yawOffsetDiff > 2500.0f) {
            renderYawOffset += yawOffsetDiff * 0.2f;
        }
        return renderYawOffset;
    }

    static {
        mc = Minecraft.getMinecraft();
    }
}
