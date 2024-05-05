// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.player;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import java.util.HashSet;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.Resolute.events.impl.EventStrafe;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;

public class RotationUtils
{
    public static float[] getRotationFromPosition(final double var0, final double var2, final double var4) {
        final double var5 = var0 - Minecraft.getMinecraft().thePlayer.posX;
        final double var6 = var4 - Minecraft.getMinecraft().thePlayer.posZ;
        final double var7 = var2 - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        final double var8 = MathHelper.sqrt_double(var5 * var5 + var6 * var6);
        final float var9 = (float)(Math.atan2(var6, var5) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float)(-(Math.atan2(var7, var8) * 180.0 / 3.141592653589793));
        return new float[] { var9, var10 };
    }
    
    public static float[] faceBlock(final BlockPos pos, final boolean scaffoldFix, final float currentYaw, final float currentPitch, final float speed) {
        final double x = pos.getX() + (scaffoldFix ? 0.5 : 0.0) - Minecraft.getMinecraft().thePlayer.posX;
        final double y = pos.getY() - (scaffoldFix ? 1.75 : 0.0) - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        final double z = pos.getZ() + (scaffoldFix ? 0.5 : 0.0) - Minecraft.getMinecraft().thePlayer.posZ;
        final double calculate = MathHelper.sqrt_double(x * x + z * z);
        final float calcYaw = (float)(MathHelper.func_181159_b(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float calcPitch = (float)(-(MathHelper.func_181159_b(y, calculate) * 180.0 / 3.141592653589793));
        final float finalPitch = (calcPitch >= 90.0f) ? 90.0f : calcPitch;
        float yaw = updateRotation(currentYaw, calcYaw, speed);
        float pitch = updateRotation(currentPitch, finalPitch, speed);
        final float sense = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.8f + 0.2f;
        final float fix = (float)(Math.pow(sense, 3.0) * 1.5);
        yaw -= yaw % fix;
        pitch -= pitch % fix;
        return new float[] { yaw, pitch };
    }
    
    public static void applyStrafeToPlayer(final EventStrafe event) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final float dif = (MathHelper.wrapAngleTo180_float(player.rotationYaw - event.yaw - 23.5f - 135.0f) + 180.0f) / 45.0f;
        final float yaw = event.yaw;
        final float strafe = event.strafe;
        final float forward = event.forward;
        final float friction = event.friction;
        float calcForward = 0.0f;
        float calcStrafe = 0.0f;
        switch ((int)dif) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
            }
            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
            }
            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
            }
            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
            }
            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
            }
            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
            }
            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
            }
            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
            }
        }
        if (calcForward > 1.0f || (calcForward < 0.9f && calcForward > 0.3f) || calcForward < -1.0f || (calcForward > -0.9f && calcForward < -0.3f)) {
            calcForward *= 0.5f;
        }
        if (calcStrafe > 1.0f || (calcStrafe < 0.9f && calcStrafe > 0.3f) || calcStrafe < -1.0f || (calcStrafe > -0.9f && calcStrafe < -0.3f)) {
            calcStrafe *= 0.5f;
        }
        float d = calcStrafe * calcStrafe + calcForward * calcForward;
        if (d >= 1.0E-4f) {
            d = MathHelper.sqrt_float(d);
            if (d < 1.0f) {
                d = 1.0f;
            }
            d = friction / d;
            calcStrafe *= d;
            calcForward *= d;
            final float yawSin = MathHelper.sin((float)(yaw * 3.141592653589793 / 180.0));
            final float yawCos = MathHelper.cos((float)(yaw * 3.141592653589793 / 180.0));
            final EntityPlayerSP entityPlayerSP = player;
            entityPlayerSP.motionX += calcStrafe * yawCos - calcForward * yawSin;
            final EntityPlayerSP entityPlayerSP2 = player;
            entityPlayerSP2.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }
    
    public static float getYawToEntity(final Entity entity, final boolean useOldPos) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final double xDist = (useOldPos ? entity.prevPosX : entity.posX) - (useOldPos ? player.prevPosX : player.posX);
        final double zDist = (useOldPos ? entity.prevPosZ : entity.posZ) - (useOldPos ? player.prevPosZ : player.posZ);
        final float rotationYaw = useOldPos ? Minecraft.getMinecraft().thePlayer.prevRotationYaw : Minecraft.getMinecraft().thePlayer.rotationYaw;
        final float var1 = (float)(Math.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }
    
    public static float getOldYaw(final Entity entity) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        return getYawBetween(player.prevRotationYaw, player.prevPosX, player.prevPosZ, entity.prevPosX, entity.prevPosZ);
    }
    
    public static float getYawToEntity(final Entity entity) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        return getYawBetween(player.rotationYaw, player.posX, player.posZ, entity.posX, entity.posZ);
    }
    
    public static float getYawBetween(final float yaw, final double srcX, final double srcZ, final double destX, final double destZ) {
        final double xDist = destX - srcX;
        final double zDist = destZ - srcZ;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return yaw + MathHelper.wrapAngleTo180_float(var1 - yaw);
    }
    
    public static MovingObjectPosition rayCast(final EntityPlayerSP player, final double x, final double y, final double z) {
        final HashSet<Entity> excluded = new HashSet<Entity>();
        excluded.add(player);
        return tracePathD(player.worldObj, player.posX, player.posY + player.getEyeHeight(), player.posZ, x, y, z, 1.0f, excluded);
    }
    
    private static MovingObjectPosition tracePathD(final World w, final double posX, final double posY, final double posZ, final double v, final double v1, final double v2, final float borderSize, final HashSet<Entity> exclude) {
        return tracePath(w, (float)posX, (float)posY, (float)posZ, (float)v, (float)v1, (float)v2, borderSize, exclude);
    }
    
    private static MovingObjectPosition tracePath(final World world, final float x, final float y, final float z, final float tx, final float ty, final float tz, final float borderSize, final HashSet<Entity> excluded) {
        Vec3 startVec = new Vec3(x, y, z);
        Vec3 endVec = new Vec3(tx, ty, tz);
        final float minX = (x < tx) ? x : tx;
        final float minY = (y < ty) ? y : ty;
        final float minZ = (z < tz) ? z : tz;
        final float maxX = (x > tx) ? x : tx;
        final float maxY = (y > ty) ? y : ty;
        final float maxZ = (z > tz) ? z : tz;
        final AxisAlignedBB bb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ).expand(borderSize, borderSize, borderSize);
        final ArrayList<Entity> allEntities = (ArrayList<Entity>)(ArrayList)world.getEntitiesWithinAABBExcludingEntity(null, bb);
        MovingObjectPosition blockHit = world.rayTraceBlocks(startVec, endVec);
        startVec = new Vec3(x, y, z);
        endVec = new Vec3(tx, ty, tz);
        Entity closestHitEntity = null;
        float closestHit = Float.POSITIVE_INFINITY;
        for (final Entity ent : allEntities) {
            if (ent.canBeCollidedWith() && !excluded.contains(ent)) {
                final float entBorder = ent.getCollisionBorderSize();
                AxisAlignedBB entityBb = ent.getEntityBoundingBox();
                if (entityBb == null) {
                    continue;
                }
                entityBb = entityBb.expand(entBorder, entBorder, entBorder);
                final MovingObjectPosition intercept = entityBb.calculateIntercept(startVec, endVec);
                if (intercept == null) {
                    continue;
                }
                final float currentHit = (float)intercept.hitVec.distanceTo(startVec);
                if (currentHit >= closestHit && currentHit != 0.0f) {
                    continue;
                }
                closestHit = currentHit;
                closestHitEntity = ent;
            }
        }
        if (closestHitEntity != null) {
            blockHit = new MovingObjectPosition(closestHitEntity);
        }
        return blockHit;
    }
    
    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 360.0f - angle3;
        }
        return angle3;
    }
    
    public static boolean isValid(final EntityLivingBase entity, final boolean players, final boolean monsters, final boolean animals, final boolean teams, final boolean invisibles, final boolean passives, final double range) {
        return (!(entity instanceof EntityPlayer) || players) && (!(entity instanceof EntityMob) || monsters) && !(entity instanceof EntityVillager) && (!(entity instanceof EntityGolem) || passives) && (!(entity instanceof EntityAnimal) || animals) && entity != Minecraft.getMinecraft().thePlayer && !entity.isDead && entity.getHealth() != 0.0f && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= range && (!entity.isInvisible() || invisibles) && (!isOnSameTeam(entity) || !teams) && !(entity instanceof EntityBat) && !(entity instanceof EntityArmorStand);
    }
    
    public static boolean isOnSameTeam(final EntityLivingBase entity) {
        if (entity.getTeam() != null && Minecraft.getMinecraft().thePlayer.getTeam() != null) {
            final char c1 = entity.getDisplayName().getFormattedText().charAt(1);
            final char c2 = Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }
        return false;
    }
    
    public static float[] getRotations(final EntityLivingBase entityIn, final float speed) {
        final float yaw = updateRotation(Minecraft.getMinecraft().thePlayer.rotationYaw, getNeededRotations(entityIn)[0], speed);
        final float pitch = updateRotation(Minecraft.getMinecraft().thePlayer.rotationPitch, getNeededRotations(entityIn)[1], speed);
        return new float[] { yaw, pitch };
    }
    
    public static float getDistanceToEntity(final EntityLivingBase entityLivingBase) {
        return Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entityLivingBase);
    }
    
    public static float getAngleChange(final EntityLivingBase entityIn) {
        float yaw = getNeededRotations(entityIn)[0];
        float pitch = getNeededRotations(entityIn)[1];
        float playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        float playerPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
        if (playerYaw < 0.0f) {
            playerYaw += 360.0f;
        }
        if (playerPitch < 0.0f) {
            playerPitch += 360.0f;
        }
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        if (pitch < 0.0f) {
            pitch += 360.0f;
        }
        final float yawChange = Math.max(playerYaw, yaw) - Math.min(playerYaw, yaw);
        final float pitchChange = Math.max(playerPitch, pitch) - Math.min(playerPitch, pitch);
        return yawChange + pitchChange;
    }
    
    public static float[] getNeededRotations(final EntityLivingBase entityIn) {
        final double d0 = entityIn.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double d2 = entityIn.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double d3 = entityIn.posY + entityIn.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        final double d4 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        final float f = (float)(MathHelper.func_181159_b(d2, d0) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-(MathHelper.func_181159_b(d3, d4) * 180.0 / 3.141592653589793));
        return new float[] { f, f2 };
    }
    
    public static Vec3 getVectorForRotation(final float[] rotation) {
        final float yawCos = MathHelper.cos(-rotation[0] * 0.017453292f - 3.1415927f);
        final float yawSin = MathHelper.sin(-rotation[0] * 0.017453292f - 3.1415927f);
        final float pitchCos = -MathHelper.cos(-rotation[1] * 0.017453292f);
        final float pitchSin = MathHelper.sin(-rotation[1] * 0.017453292f);
        return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }
    
    public static Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3(f2 * f3, f4, f * f3);
    }
    
    public static float[] getFaceDirectionToBlockPos(final BlockPos pos, final float currentYaw, final float currentPitch) {
        final double x = pos.getX() + 0.5f - Minecraft.getMinecraft().thePlayer.posX;
        final double y = pos.getY() - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        final double z = pos.getZ() + 0.5f - Minecraft.getMinecraft().thePlayer.posZ;
        final double calculate = MathHelper.sqrt_double(x * x + z * z);
        final float calcYaw = (float)(MathHelper.func_181159_b(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float calcPitch = (float)(-(MathHelper.func_181159_b(y, calculate) * 180.0 / 3.141592653589793));
        final float finalPitch = (calcPitch >= 90.0f) ? 90.0f : calcPitch;
        final float yaw = updateRotation(currentYaw, calcYaw, 360.0f);
        final float pitch = updateRotation(currentPitch, finalPitch, 360.0f);
        return new float[] { yaw, pitch };
    }
    
    public static float updateRotation(final float current, final float intended, final float speed) {
        float f = MathHelper.wrapAngleTo180_float(intended - current);
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return current + f;
    }
    
    public static float[] getRotationsToEntity(final Entity entity) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final double xDist = entity.posX - player.posX;
        final double zDist = entity.posZ - player.posZ;
        final double entEyeHeight = entity.getEyeHeight();
        final double yDist = entity.posY + entEyeHeight - Math.min(Math.max(entity.posY - player.posY, 0.0), entEyeHeight) - (player.posY + player.getEyeHeight());
        final double fDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        final float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        final float var1 = (float)(Math.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        final float yaw = rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
        final float rotationPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
        final float var2 = (float)(-(Math.atan2(yDist, fDist) * 180.0 / 3.141592653589793));
        final float pitch = rotationPitch + MathHelper.wrapAngleTo180_float(var2 - rotationPitch);
        return new float[] { yaw, MathHelper.clamp_float(pitch, -90.0f, 90.0f) };
    }
    
    public static float[] getRotations(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + (elb.getEyeHeight() - 0.4) - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        else {
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotations2(final EntityLivingBase ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition2(x, z, y);
    }
    
    public static float[] getRotationFromPosition2(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
}
