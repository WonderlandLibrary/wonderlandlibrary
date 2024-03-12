// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.player;

import net.minecraft.util.AxisAlignedBB;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.MovingObjectPosition;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import dev.tenacity.utils.misc.MathUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import dev.tenacity.event.impl.player.MotionEvent;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import net.minecraft.client.entity.EntityPlayerSP;
import dev.tenacity.utils.Utils;

public class RotationUtils implements Utils
{
    @Exclude({ Strategy.NAME_REMAPPING })
    public static void setVisualRotations(final float yaw, final float pitch) {
        final EntityPlayerSP thePlayer = RotationUtils.mc.thePlayer;
        RotationUtils.mc.thePlayer.renderYawOffset = yaw;
        thePlayer.rotationYawHead = yaw;
        RotationUtils.mc.thePlayer.rotationPitchHead = pitch;
    }
    
    public static void setVisualRotations(final float[] rotations) {
        setVisualRotations(rotations[0], rotations[1]);
    }
    
    public static void setVisualRotations(final MotionEvent e) {
        setVisualRotations(e.getYaw(), e.getPitch());
    }
    
    public static float clampRotation() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        float n = 1.0f;
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward < 0.0f) {
            rotationYaw += 180.0f;
            n = -0.5f;
        }
        else if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward > 0.0f) {
            n = 0.5f;
        }
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe > 0.0f) {
            rotationYaw -= 90.0f * n;
        }
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe < 0.0f) {
            rotationYaw += 90.0f * n;
        }
        return rotationYaw * 0.017453292f;
    }
    
    public static float getSensitivityMultiplier() {
        final float SENSITIVITY = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
        return SENSITIVITY * SENSITIVITY * SENSITIVITY * 8.0f * 0.15f;
    }
    
    public static float smoothRotation(final float from, final float to, final float speed) {
        float f = MathHelper.wrapAngleTo180_float(to - from);
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return from + f;
    }
    
    public static float[] getFacingRotations(final ScaffoldUtils.BlockCache blockCache) {
        final double d1 = blockCache.getPosition().getX() + 0.5 - RotationUtils.mc.thePlayer.posX + blockCache.getFacing().getFrontOffsetX() / 2.0;
        final double d2 = blockCache.getPosition().getZ() + 0.5 - RotationUtils.mc.thePlayer.posZ + blockCache.getFacing().getFrontOffsetZ() / 2.0;
        final double d3 = RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight() - blockCache.getPosition().getY();
        final double d4 = MathHelper.sqrt_double(d1 * d1 + d2 * d2);
        float f1 = (float)(Math.atan2(d2, d1) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(Math.atan2(d3, d4) * 180.0 / 3.141592653589793);
        if (f1 < 0.0f) {
            f1 += 360.0f;
        }
        return new float[] { f1, f2 };
    }
    
    public static float[] getRotations(final BlockPos blockPos, final EnumFacing enumFacing) {
        final double d = blockPos.getX() + 0.5 - RotationUtils.mc.thePlayer.posX + enumFacing.getFrontOffsetX() * 0.25;
        final double d2 = blockPos.getZ() + 0.5 - RotationUtils.mc.thePlayer.posZ + enumFacing.getFrontOffsetZ() * 0.25;
        final double d3 = RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight() - blockPos.getY() - enumFacing.getFrontOffsetY() * 0.25;
        final double d4 = MathHelper.sqrt_double(d * d + d2 * d2);
        final float f = (float)(Math.atan2(d2, d) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(Math.atan2(d3, d4) * 180.0 / 3.141592653589793);
        return new float[] { MathHelper.wrapAngleTo180_float(f), f2 };
    }
    
    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final Minecraft mc = Minecraft.getMinecraft();
        final double xSize = entity.posX - mc.thePlayer.posX;
        final double ySize = entity.posY + entity.getEyeHeight() / 2.0f - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double zSize = entity.posZ - mc.thePlayer.posZ;
        final double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        final float yaw = (float)(Math.atan2(zSize, xSize) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(ySize, theta) * 180.0 / 3.141592653589793));
        return new float[] { (mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360.0f, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f };
    }
    
    public static float[] getRotationsDiff(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final float[] rotations = getRotationsNeeded(entity);
        return new float[] { Math.abs(RotationUtils.mc.thePlayer.rotationYaw - rotations[0]), Math.abs(RotationUtils.mc.thePlayer.rotationPitch - rotations[1]) };
    }
    
    public static float[] getFacingRotations2(final int paramInt1, final double d, final int paramInt3) {
        final EntitySnowball localEntityPig = new EntitySnowball(Minecraft.getMinecraft().theWorld);
        localEntityPig.posX = paramInt1 + 0.5;
        localEntityPig.posY = d + 0.5;
        localEntityPig.posZ = paramInt3 + 0.5;
        return getRotationsNeeded(localEntityPig);
    }
    
    public static float getEnumRotations(final EnumFacing facing) {
        float yaw = 0.0f;
        if (facing == EnumFacing.NORTH) {
            yaw = 0.0f;
        }
        if (facing == EnumFacing.EAST) {
            yaw = 90.0f;
        }
        if (facing == EnumFacing.WEST) {
            yaw = -90.0f;
        }
        if (facing == EnumFacing.SOUTH) {
            yaw = 180.0f;
        }
        return yaw;
    }
    
    public static float getYaw(final Vec3 to) {
        final float x = (float)(to.xCoord - RotationUtils.mc.thePlayer.posX);
        final float z = (float)(to.zCoord - RotationUtils.mc.thePlayer.posZ);
        final float var1 = (float)(StrictMath.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float rotationYaw = RotationUtils.mc.thePlayer.rotationYaw;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }
    
    public static Vec3 getVecRotations(final float yaw, final float pitch) {
        final double d = Math.cos(Math.toRadians(-yaw) - 3.141592653589793);
        final double d2 = Math.sin(Math.toRadians(-yaw) - 3.141592653589793);
        final double d3 = -Math.cos(Math.toRadians(-pitch));
        final double d4 = Math.sin(Math.toRadians(-pitch));
        return new Vec3(d2 * d3, d4, d * d3);
    }
    
    public static float[] getRotations(final double posX, final double posY, final double posZ) {
        final double x = posX - RotationUtils.mc.thePlayer.posX;
        final double z = posZ - RotationUtils.mc.thePlayer.posZ;
        final double y = posY - (RotationUtils.mc.thePlayer.getEyeHeight() + RotationUtils.mc.thePlayer.posY);
        final double d3 = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float)(MathHelper.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(MathHelper.atan2(y, d3) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static float[] getSmoothRotations(final EntityLivingBase entity) {
        final float f1 = RotationUtils.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        final float fac = f1 * f1 * f1 * 256.0f;
        final double x = entity.posX - RotationUtils.mc.thePlayer.posX;
        final double z = entity.posZ - RotationUtils.mc.thePlayer.posZ;
        final double y = entity.posY + entity.getEyeHeight() - (RotationUtils.mc.thePlayer.getEntityBoundingBox().minY + (RotationUtils.mc.thePlayer.getEntityBoundingBox().maxY - RotationUtils.mc.thePlayer.getEntityBoundingBox().minY));
        final double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(MathHelper.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-(MathHelper.atan2(y, d3) * 180.0 / 3.141592653589793));
        yaw = smoothRotation(RotationUtils.mc.thePlayer.prevRotationYawHead, yaw, fac * MathUtils.getRandomFloat(0.9f, 1.0f));
        pitch = smoothRotation(RotationUtils.mc.thePlayer.prevRotationPitchHead, pitch, fac * MathUtils.getRandomFloat(0.7f, 1.0f));
        return new float[] { yaw, pitch };
    }
    
    public static boolean isMouseOver(final float yaw, final float pitch, final Entity target, final float range) {
        final float partialTicks = RotationUtils.mc.timer.renderPartialTicks;
        final Entity entity = RotationUtils.mc.getRenderViewEntity();
        Entity mcPointedEntity = null;
        if (entity != null && RotationUtils.mc.theWorld != null) {
            RotationUtils.mc.mcProfiler.startSection("pick");
            final double d0 = RotationUtils.mc.playerController.getBlockReachDistance();
            MovingObjectPosition objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d2 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > range;
            if (objectMouseOver != null) {
                d2 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            final Vec3 vec4 = RotationUtils.mc.thePlayer.getVectorForRotation(pitch, yaw);
            final Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec6 = null;
            final float f = 1.0f;
            final List<Entity> list = RotationUtils.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(1.0, 1.0, 1.0), (Predicate<? super Entity>)Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d3 = d2;
            for (final Entity entity2 : list) {
                final float f2 = entity2.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f2, f2, f2);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d3 < 0.0) {
                        continue;
                    }
                    pointedEntity = entity2;
                    vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                    d3 = 0.0;
                }
                else {
                    if (movingobjectposition == null) {
                        continue;
                    }
                    final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d4 >= d3 && d3 != 0.0) {
                        continue;
                    }
                    pointedEntity = entity2;
                    vec6 = movingobjectposition.hitVec;
                    d3 = d4;
                }
            }
            if (pointedEntity != null && flag && vec3.distanceTo(vec6) > range) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, null, new BlockPos(vec6));
            }
            if (pointedEntity != null && (d3 < d2 || objectMouseOver == null) && (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame)) {
                mcPointedEntity = pointedEntity;
            }
            RotationUtils.mc.mcProfiler.endSection();
            return mcPointedEntity == target;
        }
        return false;
    }
}
