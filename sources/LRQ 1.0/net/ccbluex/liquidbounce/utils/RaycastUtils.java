/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import org.jetbrains.annotations.Nullable;

public final class RaycastUtils
extends MinecraftInstance {
    public static final RaycastUtils INSTANCE;

    @JvmStatic
    public static final IEntity raycastEntity(double range, EntityFilter entityFilter) {
        return INSTANCE.raycastEntity(range, RotationUtils.serverRotation.getYaw(), RotationUtils.serverRotation.getPitch(), entityFilter);
    }

    /*
     * WARNING - void declaration
     */
    private final IEntity raycastEntity(double range, float yaw, float pitch, EntityFilter entityFilter) {
        IEntity renderViewEntity = MinecraftInstance.mc.getRenderViewEntity();
        if (renderViewEntity != null && MinecraftInstance.mc.getTheWorld() != null) {
            void y$iv;
            void x$iv;
            void this_$iv;
            double blockReachDistance = range;
            WVec3 eyePosition = renderViewEntity.getPositionEyes(1.0f);
            float f = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl = false;
            float yawCos = (float)Math.cos(f);
            float f2 = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl2 = false;
            float yawSin = (float)Math.sin(f2);
            double d = (double)(-pitch) * (double)((float)Math.PI / 180);
            boolean bl3 = false;
            float pitchCos = (float)(-Math.cos(d));
            double d2 = (double)(-pitch) * (double)((float)Math.PI / 180);
            boolean bl4 = false;
            float pitchSin = (float)Math.sin(d2);
            WVec3 entityLook = new WVec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
            WVec3 wVec3 = eyePosition;
            double d3 = entityLook.getXCoord() * blockReachDistance;
            double d4 = entityLook.getYCoord() * blockReachDistance;
            double z$iv = entityLook.getZCoord() * blockReachDistance;
            boolean $i$f$addVector = false;
            WVec3 vector = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            Collection<IEntity> entityList2 = iWorldClient.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.getXCoord() * blockReachDistance, entityLook.getYCoord() * blockReachDistance, entityLook.getZCoord() * blockReachDistance).expand(1.0, 1.0, 1.0), (Function1<? super IEntity, Boolean>)((Function1)raycastEntity.entityList.1.INSTANCE));
            IEntity pointedEntity = null;
            for (IEntity entity : entityList2) {
                double eyeDistance;
                if (!entityFilter.canRaycast(entity)) continue;
                double collisionBorderSize = entity.getCollisionBorderSize();
                IAxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                IMovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);
                if (axisAlignedBB.isVecInside(eyePosition)) {
                    if (!(blockReachDistance >= 0.0)) continue;
                    pointedEntity = entity;
                    blockReachDistance = 0.0;
                    continue;
                }
                if (movingObjectPosition == null || !((eyeDistance = eyePosition.distanceTo(movingObjectPosition.getHitVec())) < blockReachDistance) && blockReachDistance != 0.0) continue;
                if (entity.equals(renderViewEntity.getRidingEntity()) && !renderViewEntity.canRiderInteract()) {
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

    private RaycastUtils() {
    }

    static {
        RaycastUtils raycastUtils;
        INSTANCE = raycastUtils = new RaycastUtils();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&\u00a8\u0006\u0006"}, d2={"Lnet/ccbluex/liquidbounce/utils/RaycastUtils$EntityFilter;", "", "canRaycast", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "LiquidSense"})
    public static interface EntityFilter {
        public boolean canRaycast(@Nullable IEntity var1);
    }
}

