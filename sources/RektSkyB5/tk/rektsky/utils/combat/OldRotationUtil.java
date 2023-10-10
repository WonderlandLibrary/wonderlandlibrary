/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import tk.rektsky.Client;

public class OldRotationUtil {
    private static float yaw1 = 0.0f;
    private static float pitch1 = 0.0f;
    private static final Minecraft mc = Client.mc;
    private static final EntityPlayerSP thePlayer = Client.mc.thePlayer;
    public static boolean doReset = true;

    public static float getPitch() {
        return pitch1;
    }

    public static float getYaw() {
        return yaw1;
    }

    public static void reset() {
        if (thePlayer == null) {
            return;
        }
        OldRotationUtil.setYaw(OldRotationUtil.thePlayer.rotationYaw);
        OldRotationUtil.setPitch(OldRotationUtil.thePlayer.rotationPitch);
    }

    public static void setPitch(float pitch) {
        pitch1 = pitch;
    }

    public static void setYaw(float yaw) {
        yaw1 = yaw;
        OldRotationUtil.thePlayer.rotationYawHead = yaw;
    }

    public static void faceBlock(double x2, double y2, double z2) {
        x2 -= 1.0;
        y2 -= 1.0;
        z2 -= 1.0;
        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    Vec3 eyesPos = new Vec3(OldRotationUtil.mc.thePlayer.posX, OldRotationUtil.mc.thePlayer.getEntityBoundingBox().minY + (double)OldRotationUtil.mc.thePlayer.getEyeHeight(), OldRotationUtil.mc.thePlayer.posZ);
                    Vec3 posVec = new Vec3(x2, y2, z2).addVector(xSearch, ySearch, zSearch);
                    double diffX = posVec.xCoord - eyesPos.xCoord;
                    double diffY = posVec.yCoord - eyesPos.yCoord;
                    double diffZ = posVec.zCoord - eyesPos.zCoord;
                    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    OldRotationUtil.setPitch(MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)))));
                    OldRotationUtil.setYaw(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f));
                }
            }
        }
    }

    public static float[] getRotations(double x2, double y2, double z2) {
        x2 -= 1.0;
        y2 -= 1.0;
        z2 -= 1.0;
        float[] ret = new float[]{0.0f, 0.0f};
        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    Vec3 eyesPos = new Vec3(OldRotationUtil.mc.thePlayer.posX, OldRotationUtil.mc.thePlayer.getEntityBoundingBox().minY + (double)OldRotationUtil.mc.thePlayer.getEyeHeight(), OldRotationUtil.mc.thePlayer.posZ);
                    Vec3 posVec = new Vec3(x2, y2, z2).addVector(xSearch, ySearch, zSearch);
                    double diffX = posVec.xCoord - eyesPos.xCoord;
                    double diffY = posVec.yCoord - eyesPos.yCoord;
                    double diffZ = posVec.zCoord - eyesPos.zCoord;
                    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    ret[0] = MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f);
                    ret[1] = MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))));
                }
            }
        }
        return ret;
    }
}

