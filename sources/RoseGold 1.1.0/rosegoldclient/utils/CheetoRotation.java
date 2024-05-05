package rosegoldclient.utils;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import org.lwjgl.util.vector.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import rosegoldclient.Main;

import java.util.ArrayList;

public class CheetoRotation {
    private static final Minecraft mc;
    public static Rotation startRot;
    public static Rotation neededChange;
    public static Rotation endRot;
    public static long startTime;
    public static long endTime;
    public static boolean done;
    private static final float[][] BLOCK_SIDES;

    public static Rotation getRotation(final Vec3d vec) {
        final Vec3d eyes = Main.mc.player.getPositionEyes(1.0f);
        return getRotation(eyes, vec);
    }

    public static Rotation getRotation(final Vec3d from, final Vec3d to) {
        final double diffX = to.x - from.x;
        final double diffY = to.y - from.y;
        final double diffZ = to.z - from.z;
        return new Rotation(MathHelper.wrapDegrees((float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0)), (float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)))));
    }

    public static Rotation getRotation(final BlockPos bp) {
        final Vec3d vec = new Vec3d(bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5);
        return getRotation(vec);
    }

    public static void setup(final Rotation rot, final Long aimTime) {
        CheetoRotation.done = false;
        CheetoRotation.startRot = new Rotation(CheetoRotation.mc.player.rotationYaw, CheetoRotation.mc.player.rotationPitch);
        CheetoRotation.neededChange = getNeededChange(CheetoRotation.startRot, rot);
        CheetoRotation.endRot = new Rotation(CheetoRotation.startRot.getYaw() + CheetoRotation.neededChange.getYaw(), CheetoRotation.startRot.getPitch() + CheetoRotation.neededChange.getPitch());
        CheetoRotation.startTime = System.currentTimeMillis();
        CheetoRotation.endTime = System.currentTimeMillis() + aimTime;
    }

    public static void reset() {
        CheetoRotation.done = true;
        CheetoRotation.startRot = null;
        CheetoRotation.neededChange = null;
        CheetoRotation.endRot = null;
        CheetoRotation.startTime = 0L;
        CheetoRotation.endTime = 0L;
    }

    public static void update() {
        if (System.currentTimeMillis() <= CheetoRotation.endTime) {
            CheetoRotation.mc.player.rotationYaw = interpolate(CheetoRotation.startRot.getYaw(), CheetoRotation.endRot.getYaw());
            CheetoRotation.mc.player.rotationPitch = interpolate(CheetoRotation.startRot.getPitch(), CheetoRotation.endRot.getPitch());
        }
        else if (!CheetoRotation.done) {
            CheetoRotation.mc.player.rotationYaw = CheetoRotation.endRot.getYaw();
            CheetoRotation.mc.player.rotationPitch = CheetoRotation.endRot.getPitch();
            reset();
        }
    }

    public static void snapAngles(final Rotation rot) {
        CheetoRotation.mc.player.rotationYaw = rot.getYaw();
        CheetoRotation.mc.player.rotationPitch = rot.getPitch();
    }

    private static float interpolate(final float start, final float end) {
        final float spentMillis = (float)(System.currentTimeMillis() - CheetoRotation.startTime);
        final float relativeProgress = spentMillis / (CheetoRotation.endTime - CheetoRotation.startTime);
        return (end - start) * easeOutCubic(relativeProgress) + start;
    }

    public static float easeOutCubic(final double number) {
        return (float)(1.0 - Math.pow(1.0 - number, 3.0));
    }

    public static Rotation getNeededChange(final Rotation startRot, final Rotation endRot) {
        float yawChng = MathHelper.wrapDegrees(endRot.getYaw()) - MathHelper.wrapDegrees(startRot.getYaw());
        if (yawChng <= -180.0f) {
            yawChng += 360.0f;
        }
        else if (yawChng > 180.0f) {
            yawChng -= 360.0f;
        }
        return new Rotation(yawChng, endRot.getPitch() - startRot.getPitch());
    }

    public static double fovFromEntity(final Entity en) {
        return ((CheetoRotation.mc.player.rotationYaw - fovToEntity(en)) % 360.0 + 540.0) % 360.0 - 180.0;
    }

    public static float fovToEntity(final Entity ent) {
        final double x = ent.posX - CheetoRotation.mc.player.posX;
        final double z = ent.posZ - CheetoRotation.mc.player.posZ;
        final double yaw = Math.atan2(x, z) * 57.2957795;
        return (float)(yaw * -1.0);
    }

    public static Rotation getNeededChange(final Rotation endRot) {
        final Rotation startRot = new Rotation(CheetoRotation.mc.player.rotationYaw, CheetoRotation.mc.player.rotationPitch);
        return getNeededChange(startRot, endRot);
    }

    public static ArrayList<Vec3d> getBlockSides(final BlockPos bp) {
        final ArrayList<Vec3d> ret = new ArrayList<>();
        for (final float[] side : CheetoRotation.BLOCK_SIDES) {
            ret.add(new Vec3d(bp).addVector(side[0], side[1], side[2]));
        }
        return ret;
    }

    public static boolean lookingAt(final BlockPos blockPos, final float range) {
        final float stepSize = 0.15f;
        Vec3d position = new Vec3d(CheetoRotation.mc.player.posX, CheetoRotation.mc.player.posY + CheetoRotation.mc.player.getEyeHeight(), CheetoRotation.mc.player.posZ);
        final Vec3d look = CheetoRotation.mc.player.getLook(0.0f);
        final Vector3f step = new Vector3f((float)look.x, (float)look.y, (float)look.z);
        step.scale(stepSize / step.length());
        for (int i = 0; i < Math.floor(range / stepSize) - 2.0; ++i) {
            final BlockPos blockAtPos = new BlockPos(position.x, position.y, position.z);
            if (blockAtPos.equals(blockPos)) {
                return true;
            }
            position = position.add(new Vec3d(step.x, step.y, step.z));
        }
        return false;
    }

    public static Vec3d getVectorForRotation(final float pitch, final float yaw) {
        final float f2 = -MathHelper.cos(-pitch * 0.017453292f);
        return new Vec3d(MathHelper.sin(-yaw * 0.017453292f - 3.1415927f) * f2, MathHelper.sin(-pitch * 0.017453292f), MathHelper.cos(-yaw * 0.017453292f - 3.1415927f) * f2);
    }

    public static Vec3d getLook(final Vec3d vec) {
        final double diffX = vec.x - CheetoRotation.mc.player.posX;
        final double diffY = vec.y - (CheetoRotation.mc.player.posY + CheetoRotation.mc.player.getEyeHeight());
        final double diffZ = vec.z - CheetoRotation.mc.player.posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        return getVectorForRotation((float)(-(MathHelper.atan2(diffY, dist) * 180.0 / 3.141592653589793)), (float)(MathHelper.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0));
    }

    public static EnumFacing calculateEnumfacing(final Vec3d pos) {
        final int x = MathHelper.floor(pos.x);
        final int y = MathHelper.floor(pos.y);
        final int z = MathHelper.floor(pos.z);
        final RayTraceResult position = calculateIntercept(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1), pos, 50.0f);
        return (position != null) ? position.sideHit : null;
    }

    public static RayTraceResult calculateIntercept(final AxisAlignedBB aabb, final Vec3d block, final float range) {
        final Vec3d vec3 = CheetoRotation.mc.player.getPositionEyes(1.0f);
        final Vec3d vec4 = getLook(block);
        return aabb.calculateIntercept(vec3, vec3.addVector(vec4.x * range, vec4.y * range, vec4.z * range));
    }

    public static ArrayList<Vec3d> getPointsOnBlock(final BlockPos bp) {
        final ArrayList<Vec3d> ret = new ArrayList<>();
        for (final float[] side : CheetoRotation.BLOCK_SIDES) {
            for (int i = 0; i < 20; ++i) {
                float x = side[0];
                float y = side[1];
                float z = side[2];
                if (x == 0.5) {
                    x = RandomUtil.randBetween(0.1f, 0.9f);
                }
                if (y == 0.5) {
                    y = RandomUtil.randBetween(0.1f, 0.9f);
                }
                if (z == 0.5) {
                    z = RandomUtil.randBetween(0.1f, 0.9f);
                }
                ret.add(new Vec3d(bp).addVector((double)x, (double)y, (double)z));
            }
        }
        return ret;
    }

    static {
        mc = Minecraft.getMinecraft();
        CheetoRotation.done = true;
        BLOCK_SIDES = new float[][] { { 0.5f, 0.01f, 0.5f }, { 0.5f, 0.99f, 0.5f }, { 0.01f, 0.5f, 0.5f }, { 0.99f, 0.5f, 0.5f }, { 0.5f, 0.5f, 0.01f }, { 0.5f, 0.5f, 0.99f } };
    }
}
