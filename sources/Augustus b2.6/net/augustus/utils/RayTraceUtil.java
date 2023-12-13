// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import net.minecraft.util.MathHelper;
import java.util.List;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import optifine.Reflector;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.util.Vec3;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.BlockPos;
import net.augustus.utils.interfaces.MC;

public class RayTraceUtil implements MC
{
    public static MovingObjectPosition getHitVec(final BlockPos blockPos, final float yaw, final float pitch, final double range) {
        final Vec3 vec31 = RayTraceUtil.mc.thePlayer.getVectorForRotation(pitch, yaw);
        final Vec3 vec32 = RayTraceUtil.mc.thePlayer.getPositionEyes(1.0f).addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
        final Block block = RayTraceUtil.mc.theWorld.getBlockState(blockPos).getBlock();
        final AxisAlignedBB axisalignedbb = new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + block.getBlockBoundsMaxX(), blockPos.getY() + block.getBlockBoundsMaxY(), blockPos.getZ() + block.getBlockBoundsMaxZ());
        return axisalignedbb.calculateIntercept(RayTraceUtil.mc.thePlayer.getPositionEyes(1.0f), vec32);
    }
    
    public static MovingObjectPosition rayCast(final float partialTicks) {
        MovingObjectPosition objectMouseOver = null;
        final Entity entity = RayTraceUtil.mc.getRenderViewEntity();
        if (entity != null && RayTraceUtil.mc.theWorld != null) {
            RayTraceUtil.mc.mcProfiler.startSection("pick");
            RayTraceUtil.mc.pointedEntity = null;
            double d0 = RayTraceUtil.mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d2 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            final boolean flag2 = true;
            if (RayTraceUtil.mc.playerController.extendedReach()) {
                d0 = 6.0;
                d2 = 6.0;
            }
            else {
                if (d0 > 3.0) {
                    flag = true;
                }
                d0 = d0;
            }
            if (objectMouseOver != null) {
                d2 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            final Vec3 vec4 = entity.getLook(partialTicks);
            final Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec6 = null;
            final float f = 1.0f;
            final List list = RayTraceUtil.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING));
            double d3 = d2;
            final AxisAlignedBB realBB = null;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                final float f2 = entity2.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f2, f2, f2);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d3 >= 0.0) {
                        pointedEntity = entity2;
                        vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                        d3 = 0.0;
                    }
                }
                else if (movingobjectposition != null) {
                    final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d4 < d3 || d3 == 0.0) {
                        boolean flag3 = false;
                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            flag3 = Reflector.callBoolean(entity2, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }
                        if (entity2 == entity.ridingEntity && !flag3) {
                            if (d3 == 0.0) {
                                pointedEntity = entity2;
                                vec6 = movingobjectposition.hitVec;
                            }
                        }
                        else {
                            pointedEntity = entity2;
                            vec6 = movingobjectposition.hitVec;
                            d3 = d4;
                        }
                    }
                }
            }
            if (pointedEntity != null && flag && vec3.distanceTo(vec6) > 3.0) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, null, new BlockPos(vec6));
            }
            if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    pointedEntity = pointedEntity;
                }
            }
        }
        return objectMouseOver;
    }
    
    public static MovingObjectPosition rayCast(final float partialTicks, final float[] rots) {
        MovingObjectPosition objectMouseOver = null;
        final Entity entity = RayTraceUtil.mc.getRenderViewEntity();
        if (entity != null && RayTraceUtil.mc.theWorld != null) {
            RayTraceUtil.mc.mcProfiler.startSection("pick");
            RayTraceUtil.mc.pointedEntity = null;
            double d0 = RayTraceUtil.mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.customRayTrace(d0, partialTicks, rots[0], rots[1]);
            double d2 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            final boolean flag2 = true;
            if (RayTraceUtil.mc.playerController.extendedReach()) {
                d0 = 6.0;
                d2 = 6.0;
            }
            else {
                if (d0 > 3.0) {
                    flag = true;
                }
                d0 = d0;
            }
            if (objectMouseOver != null) {
                d2 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            final Vec3 vec4 = entity.getCustomLook(partialTicks, rots[0], rots[1]);
            final Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec6 = null;
            final float f = 1.0f;
            final List list = RayTraceUtil.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING));
            double d3 = d2;
            final AxisAlignedBB realBB = null;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                final float f2 = entity2.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f2, f2, f2);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d3 >= 0.0) {
                        pointedEntity = entity2;
                        vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                        d3 = 0.0;
                    }
                }
                else if (movingobjectposition != null) {
                    final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d4 < d3 || d3 == 0.0) {
                        boolean flag3 = false;
                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            flag3 = Reflector.callBoolean(entity2, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }
                        if (entity2 == entity.ridingEntity && !flag3) {
                            if (d3 == 0.0) {
                                pointedEntity = entity2;
                                vec6 = movingobjectposition.hitVec;
                            }
                        }
                        else {
                            pointedEntity = entity2;
                            vec6 = movingobjectposition.hitVec;
                            d3 = d4;
                        }
                    }
                }
            }
            if (pointedEntity != null && flag && vec3.distanceTo(vec6) > 3.0) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, null, new BlockPos(vec6));
            }
            if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    pointedEntity = pointedEntity;
                }
            }
        }
        return objectMouseOver;
    }
    
    public static MovingObjectPosition rayCast(final float partialTicks, final float[] rots, final double range, final double hitBoxExpand) {
        MovingObjectPosition objectMouseOver = null;
        final Entity entity = RayTraceUtil.mc.getRenderViewEntity();
        if (entity != null && RayTraceUtil.mc.theWorld != null) {
            RayTraceUtil.mc.mcProfiler.startSection("pick");
            RayTraceUtil.mc.pointedEntity = null;
            double d0 = range;
            objectMouseOver = entity.customRayTrace(d0, partialTicks, rots[0], rots[1]);
            double d2 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            final boolean flag2 = true;
            if (RayTraceUtil.mc.playerController.extendedReach()) {
                d0 = 6.0;
                d2 = 6.0;
            }
            else {
                if (d0 > 3.0) {
                    flag = true;
                }
                d0 = d0;
            }
            if (objectMouseOver != null) {
                d2 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            final Vec3 vec4 = entity.getCustomLook(partialTicks, rots[0], rots[1]);
            final Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec6 = null;
            final float f = 1.0f;
            final List list = RayTraceUtil.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING));
            double d3 = d2;
            final AxisAlignedBB realBB = null;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                final float f2 = (float)(entity2.getCollisionBorderSize() + hitBoxExpand);
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f2, f2, f2);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d3 >= 0.0) {
                        pointedEntity = entity2;
                        vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                        d3 = 0.0;
                    }
                }
                else if (movingobjectposition != null) {
                    final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d4 < d3 || d3 == 0.0) {
                        boolean flag3 = false;
                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            flag3 = Reflector.callBoolean(entity2, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }
                        if (entity2 == entity.ridingEntity && !flag3) {
                            if (d3 == 0.0) {
                                pointedEntity = entity2;
                                vec6 = movingobjectposition.hitVec;
                            }
                        }
                        else {
                            pointedEntity = entity2;
                            vec6 = movingobjectposition.hitVec;
                            d3 = d4;
                        }
                    }
                }
            }
            if (pointedEntity != null && flag && vec3.distanceTo(vec6) > range) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, null, new BlockPos(vec6));
            }
            if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    pointedEntity = pointedEntity;
                }
            }
        }
        return objectMouseOver;
    }
    
    public static boolean couldHit(final Entity hitEntity, final float partialTicks, final float currentYaw, final float currentPitch, final float yawSpeed, final float pitchSpeed) {
        final RotationUtil rotationUtil = new RotationUtil();
        final Vec3 positionEyes = RayTraceUtil.mc.thePlayer.getPositionEyes(partialTicks);
        final float f11 = hitEntity.getCollisionBorderSize();
        final double ex = MathHelper.clamp_double(positionEyes.xCoord, hitEntity.getEntityBoundingBox().minX - f11, hitEntity.getEntityBoundingBox().maxX + f11);
        final double ey = MathHelper.clamp_double(positionEyes.yCoord, hitEntity.getEntityBoundingBox().minY - f11, hitEntity.getEntityBoundingBox().maxY + f11);
        final double ez = MathHelper.clamp_double(positionEyes.zCoord, hitEntity.getEntityBoundingBox().minZ - f11, hitEntity.getEntityBoundingBox().maxZ + f11);
        final double x = ex - RayTraceUtil.mc.thePlayer.posX;
        final double y = ey - (RayTraceUtil.mc.thePlayer.posY + RayTraceUtil.mc.thePlayer.getEyeHeight());
        final double z = ez - RayTraceUtil.mc.thePlayer.posZ;
        final float calcYaw = (float)(MathHelper.func_181159_b(z, x) * 180.0 / 3.141592653589793 - 90.0);
        final float calcPitch = (float)(-(MathHelper.func_181159_b(y, MathHelper.sqrt_double(x * x + z * z)) * 180.0 / 3.141592653589793));
        final float yaw = RotationUtil.updateRotation(currentYaw, calcYaw, 180.0f);
        final float pitch = RotationUtil.updateRotation(currentPitch, calcPitch, 180.0f);
        MovingObjectPosition objectMouseOver = null;
        final Entity entity = RayTraceUtil.mc.getRenderViewEntity();
        if (entity != null && RayTraceUtil.mc.theWorld != null) {
            RayTraceUtil.mc.mcProfiler.startSection("pick");
            RayTraceUtil.mc.pointedEntity = null;
            double d0 = RayTraceUtil.mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.customRayTrace(d0, partialTicks, yaw, pitch);
            double d2 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            final boolean flag2 = true;
            if (RayTraceUtil.mc.playerController.extendedReach()) {
                d0 = 6.0;
                d2 = 6.0;
            }
            else {
                if (d0 > 3.0) {
                    flag = true;
                }
                d0 = d0;
            }
            if (objectMouseOver != null) {
                d2 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            final Vec3 vec4 = entity.getCustomLook(partialTicks, yaw, pitch);
            final Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec6 = null;
            final float f12 = 1.0f;
            final List list = RayTraceUtil.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(f12, f12, f12), Predicates.and(EntitySelectors.NOT_SPECTATING));
            double d3 = d2;
            final AxisAlignedBB realBB = null;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                final float f13 = entity2.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f13, f13, f13);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d3 >= 0.0) {
                        pointedEntity = entity2;
                        vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                        d3 = 0.0;
                    }
                }
                else if (movingobjectposition != null) {
                    final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d4 < d3 || d3 == 0.0) {
                        boolean flag3 = false;
                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            flag3 = Reflector.callBoolean(entity2, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }
                        if (entity2 == entity.ridingEntity && !flag3) {
                            if (d3 == 0.0) {
                                pointedEntity = entity2;
                                vec6 = movingobjectposition.hitVec;
                            }
                        }
                        else {
                            pointedEntity = entity2;
                            vec6 = movingobjectposition.hitVec;
                            d3 = d4;
                        }
                    }
                }
            }
            if (pointedEntity != null && flag && vec3.distanceTo(vec6) > 3.0) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, null, new BlockPos(vec6));
            }
            if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    pointedEntity = pointedEntity;
                }
            }
        }
        return objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && objectMouseOver.entityHit.getEntityId() == hitEntity.getEntityId();
    }
}
