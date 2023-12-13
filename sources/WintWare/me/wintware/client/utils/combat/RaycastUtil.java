/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package me.wintware.client.utils.combat;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RaycastUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Entity rayCastEntity(double range, float yaw, float pitch) {
        Entity renderViewEntity = mc.getRenderViewEntity();
        if (renderViewEntity != null && RaycastUtil.mc.world != null) {
            double blockReachDistance = range;
            Vec3d eyePosition = renderViewEntity.getPositionEyes(1.0f);
            float yawCos = MathHelper.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
            float yawSin = MathHelper.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
            float pitchCos = -MathHelper.cos(-pitch * ((float)Math.PI / 180));
            float pitchSin = MathHelper.sin(-pitch * ((float)Math.PI / 180));
            Vec3d entityLook = new Vec3d(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
            Vec3d vector = eyePosition.addVector(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance);
            List<Entity> entityList = RaycastUtil.mc.world.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            Entity pointedEntity = null;
            for (Entity entity : entityList) {
                double eyeDistance;
                float collisionBorderSize = entity.getCollisionBorderSize();
                AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                RayTraceResult movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);
                if (axisAlignedBB.isVecInside(eyePosition)) {
                    if (!(blockReachDistance >= 0.0)) continue;
                    pointedEntity = entity;
                    blockReachDistance = 0.0;
                    continue;
                }
                if (movingObjectPosition == null || !((eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec)) < blockReachDistance) && blockReachDistance != 0.0) continue;
                if (entity == renderViewEntity.ridingEntity) {
                    if (blockReachDistance != 0.0) continue;
                    pointedEntity = entity;
                    continue;
                }
                pointedEntity = entity;
                blockReachDistance = eyeDistance;
            }
            return pointedEntity;
        }
        return null;
    }

    public static Vec3d getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * ((float)Math.PI / 180));
        float f3 = MathHelper.sin(-pitch * ((float)Math.PI / 180));
        return new Vec3d(f1 * f2, f3, f * f2);
    }
}

