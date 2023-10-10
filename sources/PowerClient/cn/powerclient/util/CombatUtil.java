/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class CombatUtil {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static float[] getRotations(Entity entity) {
        Minecraft.getMinecraft();
        double pX = Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double pY = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
        Minecraft.getMinecraft();
        double pZ = Minecraft.thePlayer.posZ;
        double eX = entity.posX;
        double eY = entity.posY + (double)(entity.height / 2.0f);
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new float[]{(float)yaw, (float)(90.0 - pitch)};
    }

    private static float[] getDirectionToEntity(Entity var0) {
        return new float[]{CombatUtil.getYaw(var0) + Minecraft.thePlayer.rotationYaw, CombatUtil.getPitch(var0) + Minecraft.thePlayer.rotationPitch};
    }

    public static float[] getDirectionToBlock(double var0, double var1, double var2, EnumFacing var3) {
        EntityEgg var4 = new EntityEgg(CombatUtil.mc.theWorld);
        var4.posX = var0 + 0.5;
        var4.posY = var1 + 0.5;
        var4.posZ = var2 + 0.5;
        var4.posX += (double)var3.getDirectionVec().getX() * 0.25;
        var4.posY += (double)var3.getDirectionVec().getY() * 0.25;
        var4.posZ += (double)var3.getDirectionVec().getZ() * 0.25;
        return CombatUtil.getDirectionToEntity(var4);
    }

    public static float[] getRotationNeededForBlock(EntityPlayer paramEntityPlayer, BlockPos pos) {
        double d1 = (double)pos.getX() - paramEntityPlayer.posX;
        double d2 = (double)pos.getY() + 0.5 - (paramEntityPlayer.posY + (double)paramEntityPlayer.getEyeHeight());
        double d3 = (double)pos.getZ() - paramEntityPlayer.posZ;
        double d4 = Math.sqrt(d1 * d1 + d3 * d3);
        float f1 = (float)(Math.atan2(d3, d1) * 180.0 / 3.141592653589793) - 90.0f;
        float f2 = (float)((- Math.atan2(d2, d4)) * 180.0 / 3.141592653589793);
        return new float[]{f1, f2};
    }

    public static float getYaw(Entity var0) {
        double var1 = var0.posX - Minecraft.thePlayer.posX;
        double var3 = var0.posZ - Minecraft.thePlayer.posZ;
        double var5 = var3 < 0.0 && var1 < 0.0 ? 90.0 + Math.toDegrees(Math.atan(var3 / var1)) : (var3 < 0.0 && var1 > 0.0 ? -90.0 + Math.toDegrees(Math.atan(var3 / var1)) : Math.toDegrees(- Math.atan(var1 / var3)));
        return MathHelper.wrapAngleTo180_float(- Minecraft.thePlayer.rotationYaw - (float)var5);
    }

    public static float getPitch(Entity var0) {
        double var1 = var0.posX - Minecraft.thePlayer.posX;
        double var3 = var0.posZ - Minecraft.thePlayer.posZ;
        double var5 = var0.posY - 1.6 + (double)var0.getEyeHeight() - Minecraft.thePlayer.posY;
        double var7 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
        double var9 = - Math.toDegrees(Math.atan(var5 / var7));
        return - MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)var9);
    }

    public static float[] getRotationFromPosition(double x2, double y2, double z2) {
        Minecraft.getMinecraft();
        double xDiff = x2 - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double zDiff = z2 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double yDiff = y2 - Minecraft.thePlayer.posY - (double)Minecraft.thePlayer.getEyeHeight();
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(yDiff, dist)) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsNeededBlock(double x2, double y2, double z2) {
        Minecraft.getMinecraft();
        double diffX = x2 + 0.5 - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double diffZ = z2 + 0.5 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double diffY = y2 + 0.5 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(diffY, dist)) * 180.0 / 3.141592653589793);
        float[] arrf = new float[2];
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        arrf[0] = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        arrf[1] = Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
        return arrf;
    }

    public static float[] getHypixelRotationsNeededBlock(double x2, double y2, double z2) {
        Minecraft.getMinecraft();
        double diffX = x2 + 0.5 - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double diffZ = z2 + 0.5 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double diffY = y2 + 0.5 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(diffY, dist)) * 180.0 / 3.141592653589793);
        float[] arrf = new float[2];
        Minecraft.getMinecraft();
        arrf[0] = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - (float)(120 + new Random().nextInt(2)));
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        arrf[1] = Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
        return arrf;
    }

    public static float[] getRotationsNeededBlock(double x2, double y2, double z2, double x1, double y1, double z1) {
        double diffX = x1 + 0.5 - x2;
        double diffZ = z1 + 0.5 - z2;
        Minecraft.getMinecraft();
        double diffY = y1 + 0.5 - (y2 + (double)Minecraft.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(diffY, dist)) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
        float g2 = 0.006f;
        float sqrt = velocity * velocity * velocity * velocity - g2 * (g2 * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt(sqrt)) / (double)(g2 * d3)));
    }

    public static float getNewAngle(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }

    public static Vec3[] getCorners(AxisAlignedBB box2) {
        return new Vec3[]{new Vec3(box2.minX, box2.minY, box2.minZ), new Vec3(box2.maxX, box2.minY, box2.minZ), new Vec3(box2.minX, box2.maxY, box2.minZ), new Vec3(box2.minX, box2.minY, box2.maxZ), new Vec3(box2.maxX, box2.maxY, box2.minZ), new Vec3(box2.minX, box2.maxY, box2.maxZ), new Vec3(box2.maxX, box2.minY, box2.maxZ), new Vec3(box2.maxX, box2.maxY, box2.maxZ)};
    }

    public static AxisAlignedBB getCloserBox(AxisAlignedBB b1, AxisAlignedBB b2) {
        Vec3[] arrvec3 = CombatUtil.getCorners(b2);
        int n2 = arrvec3.length;
        int n22 = 0;
        while (n22 < n2) {
            Vec3 pos = arrvec3[n22];
            if (CombatUtil.isRotationIn(CombatUtil.getRotationFromPosition(pos.xCoord, pos.yCoord, pos.zCoord), b1)) {
                return CombatUtil.getDistanceToBox(b2) < CombatUtil.getDistanceToBox(b1) ? b2 : b1;
            }
            ++n22;
        }
        return b2;
    }

    public static double getDistanceToBox(AxisAlignedBB box2) {
        Minecraft.getMinecraft();
        return Minecraft.thePlayer.getDistance((box2.minX + box2.maxX) / 2.0, (box2.minY + box2.maxY) / 2.0, (box2.minZ + box2.maxZ) / 2.0);
    }

    public static boolean isRotationIn(float[] rotation, AxisAlignedBB box2) {
        float[] maxRotations = CombatUtil.getMaxRotations(box2);
        if (maxRotations[0] < rotation[0] && maxRotations[2] < rotation[1] && maxRotations[1] > rotation[0] && maxRotations[3] > rotation[1]) {
            return true;
        }
        return false;
    }

    public static float[] getRandomRotationsInBox(AxisAlignedBB box2) {
        float[] maxRotations = CombatUtil.getMaxRotations(box2);
        float yaw = (float)MathHelper.getRandomDoubleInRange(new Random(), maxRotations[0], maxRotations[1]);
        float pitch = (float)MathHelper.getRandomDoubleInRange(new Random(), maxRotations[2], maxRotations[3]);
        return new float[]{yaw, pitch};
    }

    public static float[] getMaxRotations(AxisAlignedBB box2) {
        float minYaw = 2.14748365E9f;
        float maxYaw = -2.14748365E9f;
        float minPitch = 2.14748365E9f;
        float maxPitch = -2.14748365E9f;
        Vec3[] arrvec3 = CombatUtil.getCorners(box2);
        int n2 = arrvec3.length;
        int n22 = 0;
        while (n22 < n2) {
            Vec3 pos = arrvec3[n22];
            float[] rot = CombatUtil.getRotationFromPosition(pos.xCoord, pos.yCoord, pos.zCoord);
            if (rot[0] < minYaw) {
                minYaw = rot[0];
            }
            if (rot[0] > maxYaw) {
                maxYaw = rot[0];
            }
            if (rot[1] < minPitch) {
                minPitch = rot[1];
            }
            if (rot[1] > maxPitch) {
                maxPitch = rot[1];
            }
            ++n22;
        }
        return new float[]{minYaw, maxYaw, minPitch, maxPitch};
    }

    public static AxisAlignedBB expandBox(AxisAlignedBB box2, double multiplier) {
        multiplier = 1.0 - multiplier / 100.0;
        return box2.expand((box2.maxX - box2.minX) * multiplier, 0.12, (box2.maxZ - box2.minZ) * multiplier);
    }

    public static AxisAlignedBB contractBox(AxisAlignedBB box2, double multiplier) {
        multiplier = 1.0 - multiplier / 100.0;
        return box2.contract((box2.maxX - box2.minX) * multiplier, 0.12, (box2.maxZ - box2.minZ) * multiplier);
    }

    public static float getYawDifference(float current, float target) {
        float rot = 0;
        return rot + ((rot = (target + 180.0f - current) % 360.0f) > 0.0f ? -180.0f : 180.0f);
    }

    public static float getPitchDifference(float current, float target) {
        float rot = 0;
        return rot + ((rot = (target + 90.0f - current) % 180.0f) > 0.0f ? -90.0f : 90.0f);
    }

    public static float[] getRotations(Object entity) {
        Entity eny = (Entity)entity;
        Minecraft.getMinecraft();
        double pX = Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double pY = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
        Minecraft.getMinecraft();
        double pZ = Minecraft.thePlayer.posZ;
        double eX = eny.posX;
        double eY = eny.posY + (double)(eny.height / 2.0f);
        double eZ = eny.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new float[]{(float)yaw, (float)(90.0 - pitch)};
    }
}

