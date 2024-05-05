/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;

public class EntityUtil {
    public static boolean lookChanged;
    public static float yaw;
    public static float pitch;
    public static Minecraft mc;

    static {
        mc = Minecraft.getMinecraft();
    }

    public static synchronized float getDistanceToEntity(EntityLivingBase entity) {
        return Minecraft.thePlayer.getDistanceToEntity(entity);
    }

    public static void faceEntity(EntityLivingBase entity) {
        float[] rotations = EntityUtil.getRotations(entity);
        if (rotations != null) {
            Minecraft.getMinecraft();
            Minecraft.thePlayer.rotationYaw = rotations[0];
            Minecraft.getMinecraft();
            Minecraft.thePlayer.rotationPitch = rotations[1] - 8.0f;
        }
    }

    public static float[] getRotations(Entity ent) {
        double x2 = ent.posX;
        double z2 = ent.posZ;
        double y2 = ent.boundingBox.maxY - 4.5;
        return EntityUtil.getRotationFromPosition(x2, z2, y2);
    }

    public static float[] getRotationsNeeded(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Minecraft.thePlayer.posX;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() * 0.9 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        } else {
            diffY = entity.boundingBox.minY + entity.boundingBox.maxY / 2.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        }
        double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(diffY, dist)) * 180.0 / 3.141592653589793);
        return new float[]{Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw), Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch)};
    }

    public static synchronized void faceEntityPacket(EntityLivingBase entity) {
        float[] rotations = EntityUtil.getRotationsNeeded(entity);
        if (rotations != null) {
            yaw = EntityUtil.limitAngleChange(Minecraft.thePlayer.prevRotationYaw, rotations[0], 55.0f);
            pitch = rotations[1];
            lookChanged = true;
        }
    }

    private static final float limitAngleChange(float current, float intended, float maxChange) {
        float change = intended - current;
        if (change > maxChange) {
            change = maxChange;
        } else if (change < - maxChange) {
            change = - maxChange;
        }
        return current + change;
    }

    public static float[] getRotationFromPosition(double x2, double z2, double y2) {
        double xDiff = x2 - Minecraft.thePlayer.posX;
        double zDiff = z2 - Minecraft.thePlayer.posZ;
        double yDiff = y2 - Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)((double)((float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f) + Math.random() + Math.random() + Math.random() + Math.random() + Math.random() + Math.random());
        float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793 + Math.random() + Math.random());
        return new float[]{yaw, pitch};
    }

    public static float[] faceTarget(Entity target, float p_70625_2_, float p_70625_3_, boolean miss) {
        double var6;
        double var4 = target.posX - Minecraft.thePlayer.posX;
        double var8 = target.posZ - Minecraft.thePlayer.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var10 = (EntityLivingBase)target;
            var6 = var10.posY + (double)var10.getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        } else {
            var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        }
        Random rnd = new Random();
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float var13 = (float)(- Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0.0), var14) * 180.0 / 3.141592653589793);
        float pitch = EntityUtil.changeRotation(Minecraft.thePlayer.rotationPitch, var13, p_70625_3_);
        float yaw = EntityUtil.changeRotation(Minecraft.thePlayer.rotationYaw, var12, p_70625_2_);
        return new float[]{yaw, pitch};
    }

    public static float changeRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < - p_70663_3_) {
            var4 = - p_70663_3_;
        }
        return p_70663_1_ + var4;
    }

    public static Entity findClosestToCross(double range) {
        Entity e2 = null;
        double best = 360.0;
        for (Object o2 : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            Entity ent = (Entity)o2;
            if (!(ent instanceof EntityPlayer)) continue;
            Minecraft.getMinecraft();
            double diffX = ent.posX - Minecraft.thePlayer.posX;
            Minecraft.getMinecraft();
            double diffZ = ent.posZ - Minecraft.thePlayer.posZ;
            float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
            Minecraft.getMinecraft();
            double difference = Math.abs(EntityUtil.angleDifference(newYaw, Minecraft.thePlayer.rotationYaw));
            Minecraft.getMinecraft();
            if (ent == Minecraft.thePlayer) continue;
            Minecraft.getMinecraft();
            if ((double)Minecraft.thePlayer.getDistanceToEntity(ent) > range || !(ent instanceof EntityPlayer) || difference >= best) continue;
            best = difference;
            e2 = ent;
        }
        return e2;
    }

    public static double angleDifference(double a2, double b2) {
        return ((a2 - b2) % 360.0 + 360.0) % 360.0 - 180.0;
    }

    public static boolean isWithingFOV(Entity en2, float angle) {
        angle = (float)((double)angle * 0.5);
        double angleDifference = EntityUtil.angleDifference(Minecraft.thePlayer.rotationYaw, EntityUtil.getRotationToEntity(en2)[0]);
        if (!(angleDifference > 0.0 && angleDifference < (double)angle || (double)(- angle) < angleDifference && angleDifference < 0.0)) {
            return false;
        }
        return true;
    }

    public static double[] getRotationToEntity(Entity entity) {
        double pX = Minecraft.thePlayer.posX;
        double pY = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
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
        return new double[]{yaw, 90.0 - pitch};
    }

    public static boolean isValid(boolean animals, boolean players, boolean invisibles, Entity e2) {
        boolean state = false;
        if (!(!EntityUtil.isMob(e2) || EntityUtil.isPlayer(e2) || EntityUtil.isAnimal(e2) || EntityUtil.isItem(e2) || EntityUtil.isProjectile(e2) || EntityUtil.isFalling(e2) || EntityUtil.isInvisible(e2))) {
            state = false;
        }
        if (!(!animals || !EntityUtil.isAnimal(e2) || EntityUtil.isMob(e2) || EntityUtil.isPlayer(e2) || EntityUtil.isItem(e2) || EntityUtil.isProjectile(e2) || EntityUtil.isFalling(e2) || EntityUtil.isInvisible(e2))) {
            state = true;
        }
        if (!(!players || !EntityUtil.isPlayer(e2) || EntityUtil.isMob(e2) || EntityUtil.isAnimal(e2) || EntityUtil.isItem(e2) || EntityUtil.isProjectile(e2) || EntityUtil.isFalling(e2) || EntityUtil.isInvisible(e2))) {
            state = true;
        }
        if (!(!EntityUtil.isProjectile(e2) || EntityUtil.isMob(e2) || EntityUtil.isAnimal(e2) || EntityUtil.isPlayer(e2) || EntityUtil.isItem(e2) || EntityUtil.isFalling(e2) || EntityUtil.isInvisible(e2))) {
            state = false;
        }
        if (!(!EntityUtil.isItem(e2) || EntityUtil.isMob(e2) || EntityUtil.isPlayer(e2) || EntityUtil.isProjectile(e2) || EntityUtil.isAnimal(e2) || EntityUtil.isFalling(e2))) {
            state = false;
        }
        if (invisibles && EntityUtil.isInvisible(e2) && animals && EntityUtil.isAnimal(e2) && !EntityUtil.isProjectile(e2) && !EntityUtil.isPlayer(e2) && !EntityUtil.isMob(e2)) {
            state = true;
        }
        if (invisibles && EntityUtil.isInvisible(e2) && players && EntityUtil.isPlayer(e2) && !EntityUtil.isProjectile(e2) && !EntityUtil.isAnimal(e2) && !EntityUtil.isMob(e2)) {
            state = true;
        }
        if (invisibles && EntityUtil.isInvisible(e2) && EntityUtil.isMob(e2) && !EntityUtil.isProjectile(e2) && !EntityUtil.isPlayer(e2) && !EntityUtil.isAnimal(e2)) {
            state = false;
        }
        return state;
    }

    public static boolean isPlayer(Entity e2) {
        return e2 instanceof EntityPlayer;
    }

    public static boolean isInvisible(Entity e2) {
        return e2.isInvisible();
    }

    public static boolean isAnimal(Entity e2) {
        if (!(e2 instanceof EntityAnimal) && !(e2 instanceof EntitySquid)) {
            return false;
        }
        return true;
    }

    public static boolean isMob(Entity e2) {
        return e2 instanceof IMob;
    }

    public static boolean isProjectile(Entity e2) {
        if (!(e2 instanceof EntityThrowable) && !(e2 instanceof EntityFireball)) {
            return false;
        }
        return true;
    }

    public static boolean isArrow(Entity e2) {
        return e2 instanceof EntityArrow;
    }

    public static boolean isFalling(Entity e2) {
        return e2 instanceof EntityFallingBlock;
    }

    public static double[] interpolate(Entity entity) {
        double partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;
        double[] pos = new double[]{entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks};
        return pos;
    }

    public static boolean isItem(Entity e2) {
        return e2 instanceof EntityItem;
    }

    public static boolean isNotPlayer(Entity e2) {
        if (!(EntityUtil.isAnimal(e2) || EntityUtil.isMob(e2) || EntityUtil.isProjectile(e2) || EntityUtil.isFalling(e2) || EntityUtil.isItem(e2))) {
            return false;
        }
        return true;
    }
}

