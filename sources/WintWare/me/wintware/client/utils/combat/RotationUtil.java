/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.combat;

import me.wintware.client.Main;
import me.wintware.client.utils.combat.GCDUtil;
import me.wintware.client.utils.other.RandomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {
    public static RotationUtil instance = new RotationUtil();
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        Minecraft.getMinecraft();
        double diffX = entityLiving.posX - Minecraft.player.posX;
        Minecraft.getMinecraft();
        double diffZ = entityLiving.posZ - Minecraft.player.posZ;
        float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double d = newYaw;
        Minecraft.getMinecraft();
        double difference = RotationUtil.angleDifference(d, Minecraft.player.rotationYaw);
        return difference <= (double)scope;
    }

    public static boolean isLookingAtEntity(Entity e) {
        return RotationUtil.isLookingAt(e.getPositionEyes(RotationUtil.mc.timer.elapsedPartialTicks));
    }

    public static float[] getFacePosRemote(Vec3d src, Vec3d dest) {
        double diffX = dest.xCoord - src.xCoord;
        double diffY = dest.yCoord - src.yCoord;
        double diffZ = dest.zCoord - src.zCoord;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        float[] arrf = new float[2];
        arrf[0] = RotationUtil.fixRotation(Minecraft.player.rotationYaw, yaw);
        arrf[1] = RotationUtil.fixRotation(Minecraft.player.rotationPitch, pitch);
        return arrf;
    }

    public static float[] getFacePosEntityRemote(EntityLivingBase facing, Entity en) {
        if (en == null) {
            return new float[]{facing.rotationYaw, facing.rotationPitch};
        }
        Vec3d vec = new Vec3d(facing.posX, facing.posY, facing.posZ);
        Vec3d vec1 = new Vec3d(en.posX, en.posY, en.posZ);
        return RotationUtil.getFacePosRemote(new Vec3d(facing.posX, facing.posY, facing.posZ), new Vec3d(en.posX, en.posY, en.posZ));
    }

    public static EntityLivingBase getClosestEntityToEntity(float range, Entity ent) {
        EntityLivingBase closestEntity = null;
        float mindistance = range;
        for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
            EntityLivingBase en;
            if (!RotationUtil.isNotItem(o) || ent.isEntityEqual((EntityLivingBase)o) || !(ent.getDistanceToEntity(en = (EntityLivingBase)o) < mindistance)) continue;
            mindistance = ent.getDistanceToEntity(en);
            closestEntity = en;
        }
        return closestEntity;
    }

    public static boolean isNotItem(Object o) {
        return o instanceof EntityLivingBase;
    }

    public static float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360.0f;
        return f > 180.0f ? 360.0f - f : f;
    }

    public static float[] getRotations(double x, double y, double z) {
        double diffX = x + 0.5 - Minecraft.player.posX;
        double diffY = (y + 0.5) / 2.0 - (Minecraft.player.posY + (double)Minecraft.player.getEyeHeight());
        double diffZ = z + 0.5 - Minecraft.player.posZ;
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static Float[] getLookAngles(Vec3d vec) {
        Float[] angles = new Float[2];
        Minecraft mc = Minecraft.getMinecraft();
        angles[0] = Float.valueOf((float)(Math.atan2(Minecraft.player.posZ - vec.zCoord, Minecraft.player.posX - vec.xCoord) / Math.PI * 180.0) + 90.0f);
        float heightdiff = (float)(Minecraft.player.posY + (double)Minecraft.player.getEyeHeight() - vec.yCoord);
        float distance = (float)Math.sqrt((Minecraft.player.posZ - vec.zCoord) * (Minecraft.player.posZ - vec.zCoord) + (Minecraft.player.posX - vec.xCoord) * (Minecraft.player.posX - vec.xCoord));
        angles[1] = Float.valueOf((float)(Math.atan2(heightdiff, distance) / Math.PI * 180.0));
        return angles;
    }

    public static boolean isLookingAt(Vec3d vec) {
        Float[] targetangles = RotationUtil.getLookAngles(vec);
        targetangles = RotationUtil.getLookAngles(vec);
        float change = Math.abs(MathHelper.wrapAngleTo180_float(targetangles[0].floatValue() - Main.getFakeYaw())) / Main.instance.setmgr.getSettingByName("RayTrace Box").getValFloat();
        return change < 20.0f;
    }

    public static float[] getMatrixRotations(Entity e, boolean oldPositionUse) {
        double diffY;
        double diffX = (oldPositionUse ? e.prevPosX : e.posX) - (oldPositionUse ? Minecraft.player.prevPosX : Minecraft.player.posX);
        double diffZ = (oldPositionUse ? e.prevPosZ : e.posZ) - (oldPositionUse ? Minecraft.player.prevPosZ : Minecraft.player.posZ);
        if (e instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)e;
            float randomed = RandomUtils.nextFloat((float)(entitylivingbase.posY + (double)(entitylivingbase.getEyeHeight() / 1.5f)), (float)(entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (double)(entitylivingbase.getEyeHeight() / 3.0f)));
            diffY = (double)randomed - (Minecraft.player.posY + (double)Minecraft.player.getEyeHeight());
        } else {
            diffY = (double)RandomUtils.nextFloat((float)e.getEntityBoundingBox().minY, (float)e.getEntityBoundingBox().maxY) - (Minecraft.player.posY + (double)Minecraft.player.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0) + RandomUtils.nextFloat(-2.0f, 2.0f);
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI)) + RandomUtils.nextFloat(-2.0f, 2.0f);
        yaw = Minecraft.player.rotationYaw + GCDUtil.getFixedRotation(MathHelper.wrapAngleTo180_float(yaw - Minecraft.player.rotationYaw));
        pitch = Minecraft.player.rotationPitch + GCDUtil.getFixedRotation(MathHelper.wrapAngleTo180_float(pitch - Minecraft.player.rotationPitch));
        pitch = MathHelper.clamp_float(pitch, -90.0f, 90.0f);
        return new float[]{yaw, pitch};
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float)(Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }

    public static float getYawToEntity(Entity entity) {
        double pX = Minecraft.player.posX;
        double pZ = Minecraft.player.posZ;
        double eX = entity.posX;
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dZ = pZ - eZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float)yaw;
    }

    public static float getYawToEntity(Entity mainEntity, Entity targetEntity) {
        double pX = mainEntity.posX;
        double pZ = mainEntity.posZ;
        double eX = targetEntity.posX;
        double eZ = targetEntity.posZ;
        double dX = pX - eX;
        double dZ = pZ - eZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float)yaw;
    }

    public static float getNormalizedYaw(float yaw) {
        float yawStageFirst = yaw % 360.0f;
        if (yawStageFirst > 180.0f) {
            return yawStageFirst -= 360.0f;
        }
        if (yawStageFirst < -180.0f) {
            return yawStageFirst += 360.0f;
        }
        return yawStageFirst;
    }

    public static boolean isAimAtMe(Entity entity) {
        float entityYaw = RotationUtil.getNormalizedYaw(entity.rotationYaw);
        float entityPitch = entity.rotationPitch;
        double pMinX = Minecraft.player.getEntityBoundingBox().minX;
        double pMaxX = Minecraft.player.getEntityBoundingBox().maxX;
        double pMaxY = Minecraft.player.posY + (double)Minecraft.player.height;
        double pMinY = Minecraft.player.getEntityBoundingBox().minY;
        double pMaxZ = Minecraft.player.getEntityBoundingBox().maxZ;
        double pMinZ = Minecraft.player.getEntityBoundingBox().minZ;
        double eX = entity.posX;
        double eY = entity.posY + (double)(entity.height / 2.0f);
        double eZ = entity.posZ;
        double dMaxX = pMaxX - eX;
        double dMaxY = pMaxY - eY;
        double dMaxZ = pMaxZ - eZ;
        double dMinX = pMinX - eX;
        double dMinY = pMinY - eY;
        double dMinZ = pMinZ - eZ;
        double dMinH = Math.sqrt(Math.pow(dMinX, 2.0) + Math.pow(dMinZ, 2.0));
        double dMaxH = Math.sqrt(Math.pow(dMaxX, 2.0) + Math.pow(dMaxZ, 2.0));
        double maxPitch = 90.0 - Math.toDegrees(Math.atan2(dMaxH, dMaxY));
        double minPitch = 90.0 - Math.toDegrees(Math.atan2(dMinH, dMinY));
        boolean yawAt = Math.abs(RotationUtil.getNormalizedYaw(RotationUtil.getYawToEntity(entity, Minecraft.player)) - entityYaw) <= 16.0f - Minecraft.player.getDistanceToEntity(entity) / 2.0f;
        boolean pitchAt = maxPitch >= (double)entityPitch && (double)entityPitch >= minPitch || minPitch >= (double)entityPitch && (double)entityPitch >= maxPitch;
        return yawAt && pitchAt;
    }

    public static float getSensitivityMultiplier() {
        float f = RotationUtil.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        return f * f * f * 8.0f * 0.15f;
    }

    public static float getYawToPos(BlockPos blockPos) {
        int pX = Minecraft.player.getPosition().getX();
        int pZ = Minecraft.player.getPosition().getZ();
        double dX = pX - blockPos.getX();
        double dZ = pZ - blockPos.getZ();
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float)yaw;
    }

    public static float[] getRotations(Entity entity) {
        double d = entity.posX + (entity.posX - entity.lastTickPosX) - Minecraft.player.posX;
        double d2 = entity.posY + (double)entity.getEyeHeight() - Minecraft.player.posY + (double)Minecraft.player.getEyeHeight() - 3.5;
        double d3 = entity.posZ + (entity.posZ - entity.lastTickPosZ) - Minecraft.player.posZ;
        double d4 = Math.sqrt(Math.pow(d, 2.0) + Math.pow(d3, 2.0));
        float f = (float)Math.toDegrees(-Math.atan(d / d3));
        float f2 = (float)(-Math.toDegrees(Math.atan(d2 / d4)));
        if (d < 0.0 && d3 < 0.0) {
            f = (float)(90.0 + Math.toDegrees(Math.atan(d3 / d)));
        } else if (d > 0.0 && d3 < 0.0) {
            double v = Math.toDegrees(Math.atan(d3 / d));
            f = (float)(-90.0 + v);
            float f5 = RotationUtil.mc.gameSettings.mouseSensitivity * 0.8f;
            float gcd = f * f * f * 1.2f;
            d -= d % (double)gcd;
            d2 -= d2 % (double)gcd;
        }
        return new float[]{f, f2};
    }

    public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
        double posY;
        double posX = target.posX - Minecraft.player.posX;
        double posZ = target.posZ - Minecraft.player.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var6 = (EntityLivingBase)target;
            posY = var6.posY + (double)var6.getEyeHeight() - (Minecraft.player.posY + (double)Minecraft.player.getEyeHeight());
        } else {
            posY = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.player.posY + (double)Minecraft.player.getEyeHeight());
        }
        float range = Minecraft.player.getDistanceToEntity(target);
        float calculate = MathHelper.sqrt(posX * posX + posZ * posZ);
        float var9 = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f + (float)RandomUtils.randomNumber((int)(4.0f / range + calculate), (int)(-5.0f / range + calculate));
        float var10 = (float)(-(Math.atan2(posY, calculate) * 180.0 / Math.PI) + (double)RandomUtils.randomNumber((int)(5.0f / range + calculate), (int)(-4.0f / range + calculate)));
        float f = RotationUtil.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        float gcd = f * f * f * 1.2f + (float)RandomUtils.randomNumber((int)f, (int)(-f));
        float pitch = RotationUtil.updateRotation(Minecraft.player.rotationPitch, var10, p_706253);
        float yaw = RotationUtil.updateRotation(Minecraft.player.rotationYaw, var9, p_706252) + (float)RandomUtils.randomNumber(Main.instance.setmgr.getSettingByName("Randomize").getValInt(), -Main.instance.setmgr.getSettingByName("Randomize").getValInt());
        yaw -= yaw % gcd;
        pitch -= pitch % gcd;
        return new float[]{yaw, pitch};
    }

    public static float updateRotation(float current, float intended, float speed) {
        float f = MathHelper.wrapDegrees(intended - current);
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return current + f;
    }

    public static float fixRotation(float p_70663_1_, float p_70663_2_) {
        float var4 = MathHelper.wrapDegrees(p_70663_2_ - p_70663_1_);
        if (var4 > 360.0f) {
            var4 = 360.0f;
        }
        if (var4 < -360.0f) {
            var4 = -360.0f;
        }
        return p_70663_1_ + var4;
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.player.posX;
        double zDiff = z - Minecraft.player.posZ;
        double yDiff = y - Minecraft.player.posY - 1.2;
        double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(Minecraft.player.posX, Minecraft.player.posY + (double)Minecraft.player.getEyeHeight(), Minecraft.player.posZ);
    }

    public static float[] getNeededFacing(Vec3d vec) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - (Minecraft.player.posY + (double)Minecraft.player.getEyeHeight() + 0.2);
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        pitch = MathHelper.clamp_float(pitch, -90.0f, 90.0f);
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }
}

