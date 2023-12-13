package me.earth.phobos.util;

import me.earth.phobos.features.modules.client.ClickGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class RotationUtil
        implements Util {
    public static Vec3d getEyesPos() {
        return new Vec3d(RotationUtil.mc.player.posX, RotationUtil.mc.player.posY + (double) RotationUtil.mc.player.getEyeHeight(), RotationUtil.mc.player.posZ);
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        double yaw = Math.atan2(dirz /= len, dirx /= len);
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;
        return new double[]{yaw += 90.0, pitch};
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{RotationUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - RotationUtil.mc.player.rotationYaw), RotationUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - RotationUtil.mc.player.rotationPitch)};
    }

    public static float[] simpleFacing(EnumFacing facing) {
        switch (facing) {
            case DOWN: {
                return new float[]{RotationUtil.mc.player.rotationYaw, 90.0f};
            }
            case UP: {
                return new float[]{RotationUtil.mc.player.rotationYaw, -90.0f};
            }
            case NORTH: {
                return new float[]{180.0f, 0.0f};
            }
            case SOUTH: {
                return new float[]{0.0f, 0.0f};
            }
            case WEST: {
                return new float[]{90.0f, 0.0f};
            }
        }
        return new float[]{270.0f, 0.0f};
    }

    public static void faceYawAndPitch(float yaw, float pitch) {
        RotationUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, RotationUtil.mc.player.onGround));
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = RotationUtil.getLegitRotations(vec);
        RotationUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float) MathHelper.normalizeAngle((int) rotations[1], 360) : rotations[1], RotationUtil.mc.player.onGround));
    }

    public static void faceEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(RotationUtil.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionEyes(mc.getRenderPartialTicks()));
        RotationUtil.faceYawAndPitch(angle[0], angle[1]);
    }

    public static float[] getAngle(Entity entity) {
        return MathUtil.calcAngle(RotationUtil.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionEyes(mc.getRenderPartialTicks()));
    }

    public static float transformYaw() {
        float yaw = RotationUtil.mc.player.rotationYaw % 360.0f;
        if (RotationUtil.mc.player.rotationYaw > 0.0f) {
            if (yaw > 180.0f) {
                yaw = -180.0f + (yaw - 180.0f);
            }
        } else if (yaw < -180.0f) {
            yaw = 180.0f + (yaw + 180.0f);
        }
        if (yaw < 0.0f) {
            return 180.0f + yaw;
        }
        return -180.0f + yaw;
    }

    public static boolean isInFov(BlockPos pos) {
        return pos != null && (RotationUtil.mc.player.getDistanceSq(pos) < 4.0 || RotationUtil.yawDist(pos) < (double) (RotationUtil.getHalvedfov() + 2.0f));
    }

    public static boolean isInFov(Entity entity) {
        return entity != null && (RotationUtil.mc.player.getDistanceSq(entity) < 4.0 || RotationUtil.yawDist(entity) < (double) (RotationUtil.getHalvedfov() + 2.0f));
    }

    public static double yawDist(BlockPos pos) {
        if (pos != null) {
            Vec3d difference = new Vec3d(pos).subtract(RotationUtil.mc.player.getPositionEyes(mc.getRenderPartialTicks()));
            double d = Math.abs((double) RotationUtil.mc.player.rotationYaw - (Math.toDegrees(Math.atan2(difference.z, difference.x)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static double yawDist(Entity e) {
        if (e != null) {
            Vec3d difference = e.getPositionVector().add(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(RotationUtil.mc.player.getPositionEyes(mc.getRenderPartialTicks()));
            double d = Math.abs((double) RotationUtil.mc.player.rotationYaw - (Math.toDegrees(Math.atan2(difference.z, difference.x)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static boolean isInFov(Vec3d vec3d, Vec3d other) {
        if (RotationUtil.mc.player.rotationPitch > 30.0f ? other.y > RotationUtil.mc.player.posY : RotationUtil.mc.player.rotationPitch < -30.0f && other.y < RotationUtil.mc.player.posY) {
            return true;
        }
        float angle = MathUtil.calcAngleNoY(vec3d, other)[0] - RotationUtil.transformYaw();
        if (angle < -270.0f) {
            return true;
        }
        float fov = (ClickGui.getInstance().customFov.getValue() != false ? ClickGui.getInstance().fov.getValue().floatValue() : RotationUtil.mc.gameSettings.fovSetting) / 2.0f;
        return angle < fov + 10.0f && angle > -fov - 10.0f;
    }

    public static float getFov() {
        return ClickGui.getInstance().customFov.getValue() != false ? ClickGui.getInstance().fov.getValue().floatValue() : RotationUtil.mc.gameSettings.fovSetting;
    }

    public static float getHalvedfov() {
        return RotationUtil.getFov() / 2.0f;
    }

    public static int getDirection4D() {
        return MathHelper.floor((double) (RotationUtil.mc.player.rotationYaw * 4.0f / 360.0f) + 0.5) & 3;
    }

    public static String getDirection4D(boolean northRed) {
        int dirnumber = RotationUtil.getDirection4D();
        if (dirnumber == 0) {
            return "South (+Z)";
        }
        if (dirnumber == 1) {
            return "West (-X)";
        }
        if (dirnumber == 2) {
            return (northRed ? "\u00a7c" : "") + "North (-Z)";
        }
        if (dirnumber == 3) {
            return "East (+X)";
        }
        return "Loading...";
    }
}

