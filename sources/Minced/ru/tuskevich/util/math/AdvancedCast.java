// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.math;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import ru.tuskevich.util.Utility;

public class AdvancedCast implements Utility
{
    public static AdvancedCast instance;
    
    public Entity getMouseOver(final Entity target, final float yaw, final float pitch, final double distance, final boolean ignoreWalls) {
        final Minecraft mc = Minecraft.getMinecraft();
        final Entity entity = mc.getRenderViewEntity();
        if (entity == null || mc.world == null) {
            return null;
        }
        RayTraceResult objectMouseOver = ignoreWalls ? null : this.rayTrace(distance, yaw, pitch);
        final Vec3d vec3d = entity.getPositionEyes(1.0f);
        boolean flag = false;
        double d1 = distance;
        if (distance > 3.0) {
            flag = true;
        }
        if (objectMouseOver != null) {
            d1 = objectMouseOver.hitVec.distanceTo(vec3d);
        }
        final Vec3d vec3d2 = this.getVectorForRotation(pitch, yaw);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);
        Entity pointedEntity = null;
        Vec3d vec3d4 = null;
        double d2 = d1;
        final AxisAlignedBB axisalignedbb = target.getEntityBoundingBox().grow(target.getCollisionBorderSize());
        final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d3);
        if (axisalignedbb.contains(vec3d)) {
            if (d2 >= 0.0) {
                pointedEntity = target;
                vec3d4 = ((raytraceresult == null) ? vec3d : raytraceresult.hitVec);
                d2 = 0.0;
            }
        }
        else if (raytraceresult != null) {
            final double d3 = vec3d.distanceTo(raytraceresult.hitVec);
            if (d3 < d2 || d2 == 0.0) {
                final boolean flag2 = false;
                if (!flag2 && target.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                    if (d2 == 0.0) {
                        pointedEntity = target;
                        vec3d4 = raytraceresult.hitVec;
                    }
                }
                else {
                    pointedEntity = target;
                    vec3d4 = raytraceresult.hitVec;
                    d2 = d3;
                }
            }
        }
        if (pointedEntity != null && flag && vec3d.distanceTo(vec3d4) > distance) {
            pointedEntity = null;
            objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d4, null, new BlockPos(vec3d4));
        }
        if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
            objectMouseOver = new RayTraceResult(pointedEntity, vec3d4);
        }
        if (objectMouseOver == null) {
            return null;
        }
        return objectMouseOver.entityHit;
    }
    
    public RayTraceResult rayTrace(final double blockReachDistance, final float yaw, final float pitch) {
        Minecraft.getMinecraft();
        final Vec3d vec3d = Minecraft.player.getPositionEyes(1.0f);
        final Vec3d vec3d2 = this.getVectorForRotation(pitch, yaw);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * blockReachDistance, vec3d2.y * blockReachDistance, vec3d2.z * blockReachDistance);
        return Minecraft.getMinecraft().world.rayTraceBlocks(vec3d, vec3d3, true, true, true);
    }
    
    protected final Vec3d getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3d(f2 * f3, f4, f * f3);
    }
    
    static {
        AdvancedCast.instance = new AdvancedCast();
    }
}
