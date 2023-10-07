// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.azura;

import net.minecraft.util.AxisAlignedBB;
import java.util.Iterator;
import java.util.List;
import optifine.Reflector;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;

public class RaytraceUtil
{
    public static MovingObjectPosition rayTrace(final double reach, final float yaw, final float pitch) {
        final Vec3 vec3 = Minecraft.getMinecraft().thePlayer.getPositionEyes(1.0f);
        final Vec3 vec4 = getVectorForRotation(pitch, yaw);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * reach, vec4.yCoord * reach, vec4.zCoord * reach);
        return Minecraft.getMinecraft().theWorld.rayTraceBlocks(vec3, vec5, false, false, true);
    }
    
    public static Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3(f2 * f3, f4, f * f3);
    }
    
    public static Entity rayCast(final double reach, final float yaw, final float pitch) {
        final Minecraft mc = Minecraft.getMinecraft();
        final Entity entity = mc.getRenderViewEntity();
        mc.pointedEntity = null;
        final Vec3 vec3 = entity.getPositionEyes(1.0f);
        final Vec3 vec4 = getVectorForRotation(pitch, yaw);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * reach, vec4.yCoord * reach, vec4.zCoord * reach);
        Entity pointedEntity = null;
        final float f = 1.0f;
        final List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * reach, vec4.yCoord * reach, vec4.zCoord * reach).expand(f, f, f), Predicates.and((Predicate<? super Entity>)EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d2 = reach;
        for (final Entity value : list) {
            final float f2 = value.getCollisionBorderSize();
            final AxisAlignedBB axisalignedbb = value.getEntityBoundingBox().expand(f2, f2, f2);
            final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
            if (axisalignedbb.isVecInside(vec3)) {
                if (d2 < 0.0) {
                    continue;
                }
                pointedEntity = value;
                d2 = 0.0;
            }
            else {
                if (movingobjectposition == null) {
                    continue;
                }
                final double d3 = vec3.distanceTo(movingobjectposition.hitVec);
                if (d3 >= d2 && d2 != 0.0) {
                    continue;
                }
                boolean flag1 = false;
                if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                    flag1 = Reflector.callBoolean(value, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                }
                if (!flag1 && value == entity.ridingEntity) {
                    if (d2 != 0.0) {
                        continue;
                    }
                    pointedEntity = value;
                }
                else {
                    pointedEntity = value;
                    d2 = d3;
                }
            }
        }
        return pointedEntity;
    }
}
