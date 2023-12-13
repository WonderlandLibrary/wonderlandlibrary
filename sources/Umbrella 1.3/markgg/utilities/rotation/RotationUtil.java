/*
 * Decompiled with CFR 0.150.
 */
package markgg.utilities.rotation;

import java.util.ArrayList;
import java.util.HashSet;
import markgg.events.listeners.EventMotion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RotationUtil {
    static Minecraft mc = Minecraft.getMinecraft();
    public static float Setpitch = 0.0f;
    public static float Setyaw = 0.0f;

    public static float[] getRotations(Entity e) {
        double deltaX = e.posX + (e.posX - e.lastTickPosX) - RotationUtil.mc.thePlayer.posX;
        double deltaY = e.posY - 3.5 + (double)e.getEyeHeight() - RotationUtil.mc.thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight();
        double deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - RotationUtil.mc.thePlayer.posZ;
        double distance = Math.sqrt(Math.pow(deltaX, 2.0) + Math.pow(deltaZ, 2.0));
        float yaw = (float)Math.toDegrees(-Math.atan(deltaX - deltaZ));
        float pitch = (float)(-Math.toDegrees(Math.atan(deltaY / distance)));
        if (deltaX < 0.0 && deltaZ < 0.0) {
            yaw = (float)(90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        } else if (deltaX > 0.0 && deltaZ < 0.0) {
            yaw = (float)(-90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getPredictedRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX) + (double)RotationUtil.randomNumber(0.03, -0.03);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ) + (double)RotationUtil.randomNumber(0.03, -0.03);
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtil.getRotationFromPosition(x, z, y);
    }

    public static float[] getRotationss(double posX, double posY, double posZ) {
        Minecraft mc = Minecraft.getMinecraft();
        double x = posX - mc.thePlayer.posX + (double)RotationUtil.randomNumber(0.03, -0.03);
        double y = posY - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
        double z = posZ - mc.thePlayer.posZ + (double)RotationUtil.randomNumber(0.03, -0.03);
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static void RotatePlayer(EventMotion event, double pitch, double yaw, double speed) {
        if (Setpitch == 0.0f) {
            Setpitch = RotationUtil.mc.thePlayer.rotationPitch;
        }
        if (Setyaw == 0.0f) {
            Setpitch = RotationUtil.mc.thePlayer.rotationYaw;
        }
        if ((double)Setpitch > pitch + speed || (double)Setpitch < pitch - speed) {
            if ((double)Setpitch > pitch) {
                Setpitch = (float)((double)Setpitch - speed);
            }
            if ((double)Setpitch < pitch) {
                Setpitch = (float)((double)Setpitch + speed);
            }
        }
        if ((double)Setyaw > yaw - speed || (double)Setyaw < yaw + speed) {
            if ((double)Setyaw > yaw) {
                Setyaw = (float)((double)Setyaw - speed);
            }
            if ((double)Setyaw < yaw) {
                Setyaw = (float)((double)Setyaw + speed);
            }
        }
    }

    public static float[] getRotationsEntity(EntityLivingBase entity) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer.isSprinting()) {
            return RotationUtil.getRotationss(entity.posX + (double)RotationUtil.randomNumber(0.03, -0.03), entity.posY + (double)entity.getEyeHeight() - 0.4 + (double)RotationUtil.randomNumber(0.07, -0.07), entity.posZ + (double)RotationUtil.randomNumber(0.03, -0.03));
        }
        return RotationUtil.getRotationss(entity.posX, entity.posY + (double)entity.getEyeHeight() - 0.4, entity.posZ);
    }

    private static MovingObjectPosition tracePath(World world, float x, float y, float z, float tx, float ty, float tz, float borderSize, HashSet<Entity> excluded) {
        Vec3 startVec = new Vec3(x, y, z);
        Vec3 endVec = new Vec3(tx, ty, tz);
        float minX = x < tx ? x : tx;
        float minY = y < ty ? y : ty;
        float minZ = z < tz ? z : tz;
        float maxX = x > tx ? x : tx;
        float maxY = y > ty ? y : ty;
        float maxZ = z > tz ? z : tz;
        AxisAlignedBB bb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ).expand(borderSize, borderSize, borderSize);
        ArrayList allEntities = (ArrayList)world.getEntitiesWithinAABBExcludingEntity(null, bb);
        MovingObjectPosition blockHit = world.rayTraceBlocks(startVec, endVec);
        startVec = new Vec3(x, y, z);
        endVec = new Vec3(tx, ty, tz);
        Entity closestHitEntity = null;
        float closestHit = Float.POSITIVE_INFINITY;
        for (Entity ent : allEntities) {
            float currentHit;
            MovingObjectPosition intercept;
            if (!ent.canBeCollidedWith() || excluded.contains(ent)) continue;
            float entBorder = ent.getCollisionBorderSize();
            AxisAlignedBB entityBb = ent.getEntityBoundingBox();
            if (entityBb == null || (intercept = (entityBb = entityBb.expand(entBorder, entBorder, entBorder)).calculateIntercept(startVec, endVec)) == null || (currentHit = (float)intercept.hitVec.distanceTo(startVec)) >= closestHit && currentHit != 0.0f) continue;
            closestHit = currentHit;
            closestHitEntity = ent;
        }
        if (closestHitEntity != null) {
            blockHit = new MovingObjectPosition(closestHitEntity);
        }
        return blockHit;
    }

    private static MovingObjectPosition tracePathD(World w, double posX, double posY, double posZ, double v, double v1, double v2, float borderSize, HashSet<Entity> exclude) {
        return RotationUtil.tracePath(w, (float)posX, (float)posY, (float)posZ, (float)v, (float)v1, (float)v2, borderSize, exclude);
    }

    public static MovingObjectPosition rayCast(EntityPlayerSP player, double x, double y, double z) {
        HashSet<Entity> excluded = new HashSet<Entity>();
        excluded.add(player);
        return RotationUtil.tracePathD(player.worldObj, player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, x, y, z, 1.0f, excluded);
    }

    public static int randomNumber(double d, double e) {
        int ii = (int)(-e + (double)((int)(Math.random() * (d - -e + 1.0))));
        return ii;
    }
}

