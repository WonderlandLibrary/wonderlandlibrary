// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.math;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.optifine.reflect.Reflector;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.util.vector.Vector2f;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.Utility;

public class RayCastUtility implements Utility
{
    private static Minecraft mc;
    
    public static RayTraceResult getPointed(final Vector2f rot, final double dst, final float scale, final boolean walls) {
        final Minecraft mc = RayCastUtility.mc;
        final Entity entity = Minecraft.player;
        final double d0 = dst;
        RayTraceResult objectMouseOver = rayTrace(d0, rot.x, rot.y, walls);
        final Vec3d vec3d = entity.getPositionEyes(1.0f);
        final boolean flag = false;
        double d2 = d0;
        if (objectMouseOver != null) {
            d2 = objectMouseOver.hitVec.distanceTo(vec3d);
        }
        final Vec3d vec3d2 = getLook(rot.x, rot.y);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * d0, vec3d2.y * d0, vec3d2.z * d0);
        Entity pointedEntity = null;
        Vec3d vec3d4 = null;
        final List<Entity> list = RayCastUtility.mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().contract(vec3d2.x * d0, vec3d2.y * d0, vec3d2.z * d0).expand(1.0, 1.0, 1.0), (Predicate<? super Entity>)Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_ != null && p_apply_1_.canBeCollidedWith()));
        double d3 = d2;
        for (final Entity entity3 : list) {
            final Entity entity2 = entity3;
            final Minecraft mc2 = RayCastUtility.mc;
            if (entity3 != Minecraft.player) {
                final float widthPrev = entity2.width;
                final float heightPrev = entity2.height;
                entity2.setSizeAdvanced(widthPrev / scale, heightPrev);
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().grow(entity2.getCollisionBorderSize());
                final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d3);
                if (axisalignedbb.contains(vec3d)) {
                    if (d3 >= 0.0) {
                        pointedEntity = entity2;
                        vec3d4 = ((raytraceresult == null) ? vec3d : raytraceresult.hitVec);
                        d3 = 0.0;
                    }
                }
                else if (raytraceresult != null) {
                    final double d4 = vec3d.distanceTo(raytraceresult.hitVec);
                    if (d4 < d3 || d3 == 0.0) {
                        boolean flag2 = false;
                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            flag2 = Reflector.callBoolean(entity2, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }
                        if (!flag2 && entity2.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                            if (d3 == 0.0) {
                                pointedEntity = entity2;
                                vec3d4 = raytraceresult.hitVec;
                            }
                        }
                        else {
                            pointedEntity = entity2;
                            vec3d4 = raytraceresult.hitVec;
                            d3 = d4;
                        }
                    }
                }
                entity2.setSizeAdvanced(widthPrev, heightPrev);
            }
        }
        if (pointedEntity != null && flag && vec3d.distanceTo(vec3d4) > dst) {
            pointedEntity = null;
            objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d4, null, new BlockPos(vec3d4));
        }
        if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
            objectMouseOver = new RayTraceResult(pointedEntity, vec3d4);
        }
        return objectMouseOver;
    }
    
    public static Entity getPointedEntity(final Vector2f rot, final double dst, final float scale, final boolean walls, final Entity target) {
        final Minecraft mc = RayCastUtility.mc;
        final Entity entity = Minecraft.player;
        final double d0 = dst;
        RayTraceResult objectMouseOver = rayTrace(d0, rot.x, rot.y, walls);
        final Vec3d vec3d = entity.getPositionEyes(1.0f);
        final boolean flag = false;
        double d2 = d0;
        if (objectMouseOver != null) {
            d2 = objectMouseOver.hitVec.distanceTo(vec3d);
        }
        final Vec3d vec3d2 = getLook(rot.x, rot.y);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * d0, vec3d2.y * d0, vec3d2.z * d0);
        Entity pointedEntity = null;
        Vec3d vec3d4 = null;
        double d3 = d2;
        final Entity entity2 = target;
        final float widthPrev = entity2.width;
        final float heightPrev = entity2.height;
        final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().grow(entity2.getCollisionBorderSize());
        final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d3);
        if (axisalignedbb.contains(vec3d)) {
            if (d3 >= 0.0) {
                pointedEntity = entity2;
                vec3d4 = ((raytraceresult == null) ? vec3d : raytraceresult.hitVec);
                d3 = 0.0;
            }
        }
        else if (raytraceresult != null) {
            final double d4 = vec3d.distanceTo(raytraceresult.hitVec);
            if (d4 < d3 || d3 == 0.0) {
                final boolean flag2 = false;
                if (!flag2 && entity2.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                    if (d3 == 0.0) {
                        pointedEntity = entity2;
                        vec3d4 = raytraceresult.hitVec;
                    }
                }
                else {
                    pointedEntity = entity2;
                    vec3d4 = raytraceresult.hitVec;
                    d3 = d4;
                }
            }
        }
        if (pointedEntity != null && flag && vec3d.distanceTo(vec3d4) > dst) {
            pointedEntity = null;
            objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d4, null, new BlockPos(vec3d4));
        }
        if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
            objectMouseOver = new RayTraceResult(pointedEntity, vec3d4);
        }
        return (objectMouseOver != null) ? ((objectMouseOver.entityHit instanceof Entity) ? objectMouseOver.entityHit : null) : null;
    }
    
    public static EntityLivingBase getPointedEntity(final Vector2f rot, final double dst, final float scale, final boolean walls) {
        final Minecraft mc = RayCastUtility.mc;
        final Entity entity = Minecraft.player;
        final double d0 = dst;
        RayTraceResult objectMouseOver = rayTrace(d0, rot.x, rot.y, walls);
        final Vec3d vec3d = entity.getPositionEyes(1.0f);
        final boolean flag = false;
        double d2 = d0;
        if (objectMouseOver != null) {
            d2 = objectMouseOver.hitVec.distanceTo(vec3d);
        }
        final Vec3d vec3d2 = getLook(rot.x, rot.y);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * d0, vec3d2.y * d0, vec3d2.z * d0);
        Entity pointedEntity = null;
        Vec3d vec3d4 = null;
        final List<Entity> list = RayCastUtility.mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().contract(vec3d2.x * d0, vec3d2.y * d0, vec3d2.z * d0).expand(1.0, 1.0, 1.0), (Predicate<? super Entity>)Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_ != null && p_apply_1_.canBeCollidedWith()));
        double d3 = d2;
        for (final Entity entity3 : list) {
            final Entity entity2 = entity3;
            final Minecraft mc2 = RayCastUtility.mc;
            if (entity3 != Minecraft.player) {
                final float widthPrev = entity2.width;
                final float heightPrev = entity2.height;
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().grow(entity2.getCollisionBorderSize());
                final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d3);
                if (axisalignedbb.contains(vec3d)) {
                    if (d3 < 0.0) {
                        continue;
                    }
                    pointedEntity = entity2;
                    vec3d4 = ((raytraceresult == null) ? vec3d : raytraceresult.hitVec);
                    d3 = 0.0;
                }
                else {
                    if (raytraceresult == null) {
                        continue;
                    }
                    final double d4 = vec3d.distanceTo(raytraceresult.hitVec);
                    if (d4 >= d3 && d3 != 0.0) {
                        continue;
                    }
                    boolean flag2 = false;
                    if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                        flag2 = Reflector.callBoolean(entity2, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                    }
                    if (!flag2 && entity2.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                        if (d3 != 0.0) {
                            continue;
                        }
                        pointedEntity = entity2;
                        vec3d4 = raytraceresult.hitVec;
                    }
                    else {
                        pointedEntity = entity2;
                        vec3d4 = raytraceresult.hitVec;
                        d3 = d4;
                    }
                }
            }
        }
        if (pointedEntity != null && flag && vec3d.distanceTo(vec3d4) > dst) {
            pointedEntity = null;
            objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d4, null, new BlockPos(vec3d4));
        }
        if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
            objectMouseOver = new RayTraceResult(pointedEntity, vec3d4);
        }
        return (objectMouseOver != null) ? ((objectMouseOver.entityHit instanceof EntityLivingBase) ? ((EntityLivingBase)objectMouseOver.entityHit) : null) : null;
    }
    
    public static RayTraceResult rayTrace(final double blockReachDistance, final float yaw, final float pitch, final boolean walls) {
        if (!walls) {
            return null;
        }
        final Minecraft mc = RayCastUtility.mc;
        final Vec3d vec3d = Minecraft.player.getPositionEyes(1.0f);
        final Vec3d vec3d2 = getLook(yaw, pitch);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * blockReachDistance, vec3d2.y * blockReachDistance, vec3d2.z * blockReachDistance);
        return RayCastUtility.mc.world.rayTraceBlocks(vec3d, vec3d3, true, true, true);
    }
    
    static Vec3d getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3d(f2 * f3, f4, f * f3);
    }
    
    static Vec3d getLook(final float yaw, final float pitch) {
        return getVectorForRotation(pitch, yaw);
    }
    
    static {
        RayCastUtility.mc = Minecraft.getMinecraft();
    }
}
