package rip.helium.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.List;

public class RotUtils {
    static Minecraft mc;

    static {
        RotUtils.mc = Minecraft.getMinecraft();
    }

    public static float[] getRotations(final EntityLivingBase ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }

    public static float[] getPredictedRotations(final EntityLivingBase ent) {
        final double x = ent.posX + (ent.posX - ent.lastTickPosX);
        final double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }

    public static float[] getAverageRotations(final List<EntityLivingBase> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (final Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.boundingBox.maxY - 2.0;
            posZ += ent.posZ;
        }
        posX /= targetList.size();
        posY /= targetList.size();
        posZ /= targetList.size();
        return new float[]{getRotationFromPosition(posX, posZ, posY)[0], getRotationFromPosition(posX, posZ, posY)[1]};
    }

    public static float getStraitYaw() {
        float YAW = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
        if (YAW < 45.0f && YAW > -45.0f) {
            YAW = 0.0f;
        } else if (YAW > 45.0f && YAW < 135.0f) {
            YAW = 90.0f;
        } else if (YAW > 135.0f || YAW < -135.0f) {
            YAW = 180.0f;
        } else {
            YAW = -90.0f;
        }
        return YAW;
    }

    public static float[] getBowAngles(final Entity entity) {
        final double xDelta = (entity.posX - entity.lastTickPosX) * 0.4;
        final double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4;
        double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        final boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        final double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
        final double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
        final double y = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        final float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        final float pitch = (float) (-(Math.atan2(y, d2) * 180.0 / 3.141592653589793)) + (float) dist * 0.11f;
        return new float[]{yaw, -pitch};
    }

    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }

    public static float getTrajAngleSolutionLow(final float d3, final float d1, final float velocity) {
        final float g = 0.006f;
        final float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float) Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
    }

    public static float getYawChange(final float yaw, final double posX, final double posZ) {
        final double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = 0.0;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        } else if (deltaZ < 0.0 && deltaX > 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        } else if (deltaZ != 0.0) {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(yaw - (float) yawToEntity));
    }

    public static float getPitchChange(final float pitch, final Entity entity, final double posY) {
        final double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double deltaY = posY - 2.2 + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5f;
    }

    public static float getNewAngle(float angle) {
        angle %= 360.0f;
        if (angle >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static boolean canEntityBeSeen(final Entity e) {
        final Vec3 vec1 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        final AxisAlignedBB box = e.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(e.posX, e.posY + e.getEyeHeight() / 1.32f, e.posZ);
        final double minx = e.posX - 0.25;
        final double maxx = e.posX + 0.25;
        final double miny = e.posY;
        final double maxy = e.posY + Math.abs(e.posY - box.maxY);
        final double minz = e.posZ - 0.25;
        final double maxz = e.posZ + 0.25;
        boolean see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, minz);
        see = (mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, minz);
        see = (mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, maxz);
        see = (mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, maxz);
        see = (mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, minz);
        see = (mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, minz);
        see = (mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, maxz - 0.1);
        see = (mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, maxz);
        see = (mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        return see;
    }

    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 360.0f - angle3;
        }
        return angle3;
    }
}
