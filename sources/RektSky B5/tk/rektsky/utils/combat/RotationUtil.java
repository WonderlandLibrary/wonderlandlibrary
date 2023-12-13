/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.combat;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vector3d;
import org.lwjgl.util.vector.Vector2f;
import tk.rektsky.Client;

public class RotationUtil {
    private static float yaw1 = 0.0f;
    private static float pitch1 = 0.0f;
    private static Minecraft mc = Client.mc;
    private static List<String> rotating = new ArrayList<String>();

    public static Vector2f getCirclePosition(double angle) {
        double a2 = Math.toRadians(angle);
        return new Vector2f((float)(-Math.sin(a2)), (float)Math.cos(a2));
    }

    public static Vector3d get360Position(double yaw, double pitch, double distance, float circlePosition) {
        double rYaw = Math.toRadians(yaw);
        double rPitch = Math.toRadians(pitch);
        double centerZ = Math.cos(rYaw) * Math.cos(rPitch) * distance;
        double centerY = -Math.sin(rPitch) * distance;
        double centerX = -Math.sin(rYaw) * Math.cos(rPitch) * distance;
        return new Vector3d(centerX, centerY, centerZ);
    }

    public static Vector2f tryFaceBlock(BlockPos position, EnumFacing face) {
        double actualFacingX = (double)position.getX() + 0.5;
        double actualFacingY = (double)position.getY() + 0.5;
        double actualFacingZ = (double)position.getZ() + 0.5;
        actualFacingX += (double)face.getDirectionVec().getX() / 2.0;
        actualFacingY += (double)face.getDirectionVec().getY() / 2.0;
        actualFacingZ += (double)face.getDirectionVec().getZ() / 2.0;
        actualFacingX = RotationUtil.mc.thePlayer.posX - actualFacingX;
        actualFacingY = RotationUtil.mc.thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight() - actualFacingY;
        actualFacingZ = RotationUtil.mc.thePlayer.posZ - actualFacingZ;
        float yaw = (float)(Math.toDegrees(Math.atan2(actualFacingZ, actualFacingX)) + 90.0);
        float pitch = (float)Math.toDegrees(Math.atan(actualFacingY / RotationUtil.distanceTo(actualFacingX, 0.0, actualFacingZ)));
        return new Vector2f(yaw, pitch);
    }

    public static Vector2f tryFace(double x2, double y2, double z2) {
        double actualFacingX = x2;
        double actualFacingY = y2;
        double actualFacingZ = z2;
        actualFacingX = RotationUtil.mc.thePlayer.posX - actualFacingX;
        actualFacingY = RotationUtil.mc.thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight() - actualFacingY;
        actualFacingZ = RotationUtil.mc.thePlayer.posZ - actualFacingZ;
        float yaw = (float)(Math.toDegrees(Math.atan2(actualFacingZ, actualFacingX)) + 90.0);
        float pitch = (float)Math.toDegrees(Math.atan(actualFacingY / RotationUtil.distanceTo(actualFacingX, 0.0, actualFacingZ)));
        return new Vector2f(yaw, pitch);
    }

    @Deprecated
    public static boolean setReverseRotating(String id, boolean value) {
        boolean bl = value = !value;
        if (value && !rotating.contains(id)) {
            rotating.add(id);
        }
        if (!value) {
            rotating.remove(id);
        }
        return value;
    }

    public static boolean setRotating(String id, boolean value) {
        if (value && !rotating.contains(id)) {
            rotating.add(id);
        }
        if (!value) {
            rotating.remove(id);
        }
        return value;
    }

    public static boolean doReset() {
        return !RotationUtil.isRotating();
    }

    public static boolean isRotating() {
        return rotating.size() != 0;
    }

    public static boolean isRotating(String id) {
        return rotating.contains(id);
    }

    public static float getPitch() {
        return pitch1;
    }

    public static float getYaw() {
        return yaw1;
    }

    public static void reset() {
        RotationUtil.setYaw(RotationUtil.mc.thePlayer.rotationYaw);
        RotationUtil.setPitch(RotationUtil.mc.thePlayer.rotationPitch);
    }

    public static void setPitch(float pitch) {
        pitch1 = pitch;
    }

    public static void setYaw(float yaw) {
        yaw1 = yaw;
        RotationUtil.mc.thePlayer.rotationYawHead = yaw;
    }

    public static Vector2f faceBlock(double x2, double y2, double z2) {
        double actualFacingX = x2;
        double actualFacingY = y2;
        double actualFacingZ = z2;
        actualFacingX = RotationUtil.mc.thePlayer.posX - actualFacingX;
        actualFacingY = RotationUtil.mc.thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight() - actualFacingY;
        actualFacingZ = RotationUtil.mc.thePlayer.posZ - actualFacingZ;
        float yaw = (float)(Math.toDegrees(Math.atan2(actualFacingZ, actualFacingX)) + 90.0);
        float pitch = (float)Math.toDegrees(Math.atan(actualFacingY / RotationUtil.distanceTo(actualFacingX, 0.0, actualFacingZ)));
        return new Vector2f(yaw, pitch);
    }

    private static double distanceTo(double x2, double y2, double z2) {
        double distance = 0.0;
        distance = Math.sqrt(x2 * x2 + z2 * z2);
        distance = Math.sqrt(distance * distance + y2 * y2);
        return distance;
    }

    public static float[] getRotations(double x2, double y2, double z2) {
        x2 -= 1.0;
        y2 -= 1.0;
        z2 -= 1.0;
        float[] ret = new float[]{0.0f, 0.0f};
        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    Vec3 eyesPos = new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.getEntityBoundingBox().minY + (double)RotationUtil.mc.thePlayer.getEyeHeight(), RotationUtil.mc.thePlayer.posZ);
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

    public static void oldFaceBlock(double x2, double y2, double z2) {
        x2 -= 1.0;
        y2 -= 1.0;
        z2 -= 1.0;
        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    Vec3 eyesPos = new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.getEntityBoundingBox().minY + (double)RotationUtil.mc.thePlayer.getEyeHeight(), RotationUtil.mc.thePlayer.posZ);
                    Vec3 posVec = new Vec3(x2, y2, z2).addVector(xSearch, ySearch, zSearch);
                    double diffX = posVec.xCoord - eyesPos.xCoord;
                    double diffY = posVec.yCoord - eyesPos.yCoord;
                    double diffZ = posVec.zCoord - eyesPos.zCoord;
                    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    RotationUtil.setPitch(MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)))));
                    RotationUtil.setYaw(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f));
                }
            }
        }
    }

    public static float[] oldGetRotations(double x2, double y2, double z2) {
        x2 -= 1.0;
        y2 -= 1.0;
        z2 -= 1.0;
        float[] ret = new float[]{0.0f, 0.0f};
        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    Vec3 eyesPos = new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.getEntityBoundingBox().minY + (double)RotationUtil.mc.thePlayer.getEyeHeight(), RotationUtil.mc.thePlayer.posZ);
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

