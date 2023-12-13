package me.earth.phobos.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MathUtil
        implements Util {
    private static final Random random = new Random();

    public static int getRandom(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static double getRandom(double min, double max) {
        return MathHelper.clamp(min + random.nextDouble() * max, min, max);
    }

    public static float getRandom(float min, float max) {
        return MathHelper.clamp(min + random.nextFloat() * max, min, max);
    }

    public static int clamp(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double clamp(double num, double min, double max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float sin(float value) {
        return MathHelper.sin(value);
    }

    public static float cos(float value) {
        return MathHelper.cos(value);
    }

    public static float wrapDegrees(float value) {
        return MathHelper.wrapDegrees(value);
    }

    public static double wrapDegrees(double value) {
        return MathHelper.wrapDegrees(value);
    }

    public static Vec3d roundVec(Vec3d vec3d, int places) {
        return new Vec3d(MathUtil.round(vec3d.x, places), MathUtil.round(vec3d.y, places), MathUtil.round(vec3d.z, places));
    }

    public static double angleBetweenVecs(Vec3d vec3d, Vec3d other) {
        double angle = Math.atan2(vec3d.x - other.x, vec3d.z - other.z);
        angle = -(angle / Math.PI) * 360.0 / 2.0 + 180.0;
        return angle;
    }

    public static double lengthSQ(Vec3d vec3d) {
        return MathUtil.square(vec3d.x) + MathUtil.square(vec3d.y) + MathUtil.square(vec3d.z);
    }

    public static double length(Vec3d vec3d) {
        return Math.sqrt(MathUtil.lengthSQ(vec3d));
    }

    public static double dot(Vec3d vec3d, Vec3d other) {
        return vec3d.x * other.x + vec3d.y * other.y + vec3d.z * other.z;
    }

    public static double square(double input) {
        return input * input;
    }

    public static double square(float input) {
        return input * input;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }

    public static float wrap(float valI) {
        float val = valI % 360.0f;
        if (val >= 180.0f) {
            val -= 360.0f;
        }
        if (val < -180.0f) {
            val += 360.0f;
        }
        return val;
    }

    public static Vec3d direction(float yaw) {
        return new Vec3d(Math.cos(MathUtil.degToRad(yaw + 90.0f)), 0.0, Math.sin(MathUtil.degToRad(yaw + 90.0f)));
    }

    public static float round(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.floatValue();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean descending) {
        LinkedList<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        if (descending) {
            list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        } else {
            list.sort(Map.Entry.comparingByValue());
        }
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String getTimeOfDay() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(11);
        if (timeOfDay < 12) {
            return "Good Morning ";
        }
        if (timeOfDay < 16) {
            return "Good Afternoon ";
        }
        if (timeOfDay < 21) {
            return "Good Evening ";
        }
        return "Good Night ";
    }

    public static double radToDeg(double rad) {
        return rad * (double) 57.29578f;
    }

    public static double degToRad(double deg) {
        return deg * 0.01745329238474369;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double) Math.round(val * one) / one;
    }

    public static double[] directionSpeed(double speed) {
        float forward = MathUtil.mc.player.movementInput.moveForward;
        float side = MathUtil.mc.player.movementInput.moveStrafe;
        float yaw = MathUtil.mc.player.prevRotationYaw + (MathUtil.mc.player.rotationYaw - MathUtil.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float) (forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double) forward * speed * cos + (double) side * speed * sin;
        double posZ = (double) forward * speed * sin - (double) side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static List<Vec3d> getBlockBlocks(Entity entity) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        double y = entity.posY;
        double minX = MathUtil.round(bb.minX, 0);
        double minZ = MathUtil.round(bb.minZ, 0);
        double maxX = MathUtil.round(bb.maxX, 0);
        double maxZ = MathUtil.round(bb.maxZ, 0);
        if (minX != maxX) {
            Vec3d vec3d1 = new Vec3d(minX, y, minZ);
            Vec3d vec3d2 = new Vec3d(maxX, y, minZ);
            BlockPos pos1 = new BlockPos(vec3d1);
            BlockPos pos2 = new BlockPos(vec3d2);
            if (BlockUtil.isBlockUnSolid(pos1) && BlockUtil.isBlockUnSolid(pos2)) {
                vec3ds.add(vec3d1);
                vec3ds.add(vec3d2);
            }
            if (minZ != maxZ) {
                Vec3d vec3d3 = new Vec3d(minX, y, maxZ);
                Vec3d vec3d4 = new Vec3d(maxX, y, maxZ);
                BlockPos pos3 = new BlockPos(vec3d1);
                BlockPos pos4 = new BlockPos(vec3d2);
                if (BlockUtil.isBlockUnSolid(pos3) && BlockUtil.isBlockUnSolid(pos4)) {
                    vec3ds.add(vec3d3);
                    vec3ds.add(vec3d4);
                    return vec3ds;
                }
            }
            if (vec3ds.isEmpty()) {
                vec3ds.add(entity.getPositionVector());
            }
            return vec3ds;
        }
        if (minZ != maxZ) {
            Vec3d vec3d1 = new Vec3d(minX, y, minZ);
            Vec3d vec3d2 = new Vec3d(minX, y, maxZ);
            BlockPos pos1 = new BlockPos(vec3d1);
            BlockPos pos2 = new BlockPos(vec3d2);
            if (BlockUtil.isBlockUnSolid(pos1) && BlockUtil.isBlockUnSolid(pos2)) {
                vec3ds.add(vec3d1);
                vec3ds.add(vec3d2);
            }
            if (vec3ds.isEmpty()) {
                vec3ds.add(entity.getPositionVector());
            }
            return vec3ds;
        }
        vec3ds.add(entity.getPositionVector());
        return vec3ds;
    }

    public static boolean areVec3dsAligned(Vec3d vec3d1, Vec3d vec3d2) {
        return MathUtil.areVec3dsAlignedRetarded(vec3d1, vec3d2);
    }

    public static boolean areVec3dsAlignedRetarded(Vec3d vec3d1, Vec3d vec3d2) {
        BlockPos pos1 = new BlockPos(vec3d1);
        BlockPos pos2 = new BlockPos(vec3d2.x, vec3d1.y, vec3d2.z);
        return pos1.equals(pos2);
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static float[] calcAngleNoY(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difZ = to.z - from.z;
        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)};
    }

    public static Vec3d calculateLine(Vec3d x1, Vec3d x2, double distance) {
        double length = Math.sqrt(MathUtil.multiply(x2.x - x1.x) + MathUtil.multiply(x2.y - x1.y) + MathUtil.multiply(x2.z - x1.z));
        double unitSlopeX = (x2.x - x1.x) / length;
        double unitSlopeY = (x2.y - x1.y) / length;
        double unitSlopeZ = (x2.z - x1.z) / length;
        double x = x1.x + unitSlopeX * distance;
        double y = x1.y + unitSlopeY * distance;
        double z = x1.z + unitSlopeZ * distance;
        return new Vec3d(x, y, z);
    }

    public static double multiply(double one) {
        return one * one;
    }

    public static Vec3d extrapolatePlayerPosition(EntityPlayer player, int ticks) {
        Vec3d lastPos = new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
        Vec3d currentPos = new Vec3d(player.posX, player.posY, player.posZ);
        double distance = MathUtil.multiply(player.motionX) + MathUtil.multiply(player.motionY) + MathUtil.multiply(player.motionZ);
        Vec3d tempVec = MathUtil.calculateLine(lastPos, currentPos, distance * (double) ticks);
        return new Vec3d(tempVec.x, player.posY, tempVec.z);
    }

    public static double[] differentDirectionSpeed(double speed) {
        Minecraft mc = Minecraft.getMinecraft();
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float) (forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double) forward * speed * cos + (double) side * speed * sin;
        double posZ = (double) forward * speed * sin - (double) side * speed * cos;
        return new double[]{posX, posZ};
    }
}

