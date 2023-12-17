// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;

public final class ProjectileHelper
{
    public static RayTraceResult forwardsRaycast(final Entity projectile, final boolean includeEntities, final boolean ignoreExcludedEntity, final Entity excludedEntity) {
        final double d0 = projectile.posX;
        final double d2 = projectile.posY;
        final double d3 = projectile.posZ;
        final double d4 = projectile.motionX;
        final double d5 = projectile.motionY;
        final double d6 = projectile.motionZ;
        final World world = projectile.world;
        final Vec3d vec3d = new Vec3d(d0, d2, d3);
        Vec3d vec3d2 = new Vec3d(d0 + d4, d2 + d5, d3 + d6);
        RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d2, false, true, false);
        if (includeEntities) {
            if (raytraceresult != null) {
                vec3d2 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
            }
            Entity entity = null;
            final List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(projectile, projectile.getEntityBoundingBox().expand(d4, d5, d6).grow(1.0));
            double d7 = 0.0;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                if (entity2.canBeCollidedWith() && (ignoreExcludedEntity || !entity2.isEntityEqual(excludedEntity)) && !entity2.noClip) {
                    final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().grow(0.30000001192092896);
                    final RayTraceResult raytraceresult2 = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                    if (raytraceresult2 != null) {
                        final double d8 = vec3d.squareDistanceTo(raytraceresult2.hitVec);
                        if (d8 < d7 || d7 == 0.0) {
                            entity = entity2;
                            d7 = d8;
                        }
                    }
                }
            }
            if (entity != null) {
                raytraceresult = new RayTraceResult(entity);
            }
        }
        return raytraceresult;
    }
    
    public static final void rotateTowardsMovement(final Entity projectile, final float rotationSpeed) {
        final double d0 = projectile.motionX;
        final double d2 = projectile.motionY;
        final double d3 = projectile.motionZ;
        final float f = MathHelper.sqrt(d0 * d0 + d3 * d3);
        projectile.rotationYaw = (float)(MathHelper.atan2(d3, d0) * 57.29577951308232) + 90.0f;
        projectile.rotationPitch = (float)(MathHelper.atan2(f, d2) * 57.29577951308232) - 90.0f;
        while (projectile.rotationPitch - projectile.prevRotationPitch < -180.0f) {
            projectile.prevRotationPitch -= 360.0f;
        }
        while (projectile.rotationPitch - projectile.prevRotationPitch >= 180.0f) {
            projectile.prevRotationPitch += 360.0f;
        }
        while (projectile.rotationYaw - projectile.prevRotationYaw < -180.0f) {
            projectile.prevRotationYaw -= 360.0f;
        }
        while (projectile.rotationYaw - projectile.prevRotationYaw >= 180.0f) {
            projectile.prevRotationYaw += 360.0f;
        }
        projectile.rotationPitch = projectile.prevRotationPitch + (projectile.rotationPitch - projectile.prevRotationPitch) * rotationSpeed;
        projectile.rotationYaw = projectile.prevRotationYaw + (projectile.rotationYaw - projectile.prevRotationYaw) * rotationSpeed;
    }
}
