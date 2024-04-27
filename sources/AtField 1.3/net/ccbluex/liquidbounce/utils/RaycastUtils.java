/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
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
    public static final IEntity raycastEntity(double d, EntityFilter entityFilter) {
        return INSTANCE.raycastEntity(d, RotationUtils.serverRotation.getYaw(), RotationUtils.serverRotation.getPitch(), entityFilter);
    }

    static {
        RaycastUtils raycastUtils;
        INSTANCE = raycastUtils = new RaycastUtils();
    }

    private final IEntity raycastEntity(double d, float f, float f2, EntityFilter entityFilter) {
        IEntity iEntity = MinecraftInstance.mc.getRenderViewEntity();
        if (iEntity != null && MinecraftInstance.mc.getTheWorld() != null) {
            double d2 = d;
            WVec3 wVec3 = iEntity.getPositionEyes(1.0f);
            float f3 = -f * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl = false;
            float f4 = (float)Math.cos(f3);
            float f5 = -f * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl2 = false;
            f3 = (float)Math.sin(f5);
            double d3 = (double)(-f2) * (double)((float)Math.PI / 180);
            boolean bl3 = false;
            f5 = (float)(-Math.cos(d3));
            double d4 = (double)(-f2) * (double)((float)Math.PI / 180);
            boolean bl4 = false;
            float f6 = (float)Math.sin(d4);
            WVec3 wVec32 = new WVec3(f3 * f5, f6, f4 * f5);
            Object object = wVec3;
            double d5 = wVec32.getXCoord() * d2;
            double d6 = wVec32.getYCoord() * d2;
            double d7 = wVec32.getZCoord() * d2;
            boolean bl5 = false;
            WVec3 wVec33 = new WVec3(((WVec3)object).getXCoord() + d5, ((WVec3)object).getYCoord() + d6, ((WVec3)object).getZCoord() + d7);
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            object = iWorldClient.getEntitiesInAABBexcluding(iEntity, iEntity.getEntityBoundingBox().addCoord(wVec32.getXCoord() * d2, wVec32.getYCoord() * d2, wVec32.getZCoord() * d2).expand(1.0, 1.0, 1.0), raycastEntity.entityList.1.INSTANCE);
            IEntity iEntity2 = null;
            Iterator iterator2 = object.iterator();
            while (iterator2.hasNext()) {
                double d8;
                IEntity iEntity3 = (IEntity)iterator2.next();
                if (!entityFilter.canRaycast(iEntity3)) continue;
                double d9 = iEntity3.getCollisionBorderSize();
                IAxisAlignedBB iAxisAlignedBB = iEntity3.getEntityBoundingBox().expand(d9, d9, d9);
                IMovingObjectPosition iMovingObjectPosition = iAxisAlignedBB.calculateIntercept(wVec3, wVec33);
                if (iAxisAlignedBB.isVecInside(wVec3)) {
                    if (!(d2 >= 0.0)) continue;
                    iEntity2 = iEntity3;
                    d2 = 0.0;
                    continue;
                }
                if (iMovingObjectPosition == null || !((d8 = wVec3.distanceTo(iMovingObjectPosition.getHitVec())) < d2) && d2 != 0.0) continue;
                if (iEntity3.equals(iEntity.getRidingEntity()) && !iEntity.canRiderInteract()) {
                    if (d2 != 0.0) continue;
                    iEntity2 = iEntity3;
                    continue;
                }
                iEntity2 = iEntity3;
                d2 = d8;
            }
            return iEntity2;
        }
        return null;
    }

    private RaycastUtils() {
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&\u00a8\u0006\u0006"}, d2={"Lnet/ccbluex/liquidbounce/utils/RaycastUtils$EntityFilter;", "", "canRaycast", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "AtField"})
    public static interface EntityFilter {
        public boolean canRaycast(@Nullable IEntity var1);
    }
}

