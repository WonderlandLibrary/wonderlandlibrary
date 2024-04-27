/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.utils.extensions;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;

public final class PlayerExtensionKt {
    public static final double getDistanceToEntityBox(IEntity iEntity, IEntity iEntity2) {
        WVec3 wVec3 = iEntity.getPositionEyes(1.0f);
        WVec3 wVec32 = PlayerExtensionKt.getNearestPointBB(wVec3, iEntity2.getEntityBoundingBox());
        double d = wVec32.getXCoord() - wVec3.getXCoord();
        boolean bl = false;
        double d2 = Math.abs(d);
        double d3 = wVec32.getYCoord() - wVec3.getYCoord();
        boolean bl2 = false;
        d = Math.abs(d3);
        double d4 = wVec32.getZCoord() - wVec3.getZCoord();
        int n = 0;
        d3 = Math.abs(d4);
        d4 = d2;
        n = 2;
        boolean bl3 = false;
        double d5 = Math.pow(d4, n);
        d4 = d;
        n = 2;
        double d6 = d5;
        bl3 = false;
        double d7 = Math.pow(d4, n);
        d4 = d3;
        n = 2;
        d6 += d7;
        bl3 = false;
        d7 = Math.pow(d4, n);
        d4 = d6 + d7;
        n = 0;
        return Math.sqrt(d4);
    }

    public static final float getRenderHurtTime(IEntityLivingBase iEntityLivingBase) {
        return (float)iEntityLivingBase.getHurtTime() - (iEntityLivingBase.getHurtTime() != 0 ? Minecraft.func_71410_x().field_71428_T.field_194147_b : 0.0f);
    }

    public static final boolean isClientFriend(IEntityPlayer iEntityPlayer) {
        String string = iEntityPlayer.getName();
        if (string == null) {
            return false;
        }
        String string2 = string;
        return LiquidBounce.INSTANCE.getFileManager().friendsConfig.isFriend(ColorUtils.stripColor(string2));
    }

    public static final boolean isAnimal(IEntity iEntity) {
        return MinecraftInstance.classProvider.isEntityAnimal(iEntity) || MinecraftInstance.classProvider.isEntitySquid(iEntity) || MinecraftInstance.classProvider.isEntityGolem(iEntity) || MinecraftInstance.classProvider.isEntityBat(iEntity);
    }

    public static final IMovingObjectPosition rayTraceWithServerSideRotation(IEntity iEntity, double d) {
        return PlayerExtensionKt.rayTraceWithCustomRotation(iEntity, d, RotationUtils.serverRotation);
    }

    public static final WVec3 getNearestPointBB(WVec3 wVec3, IAxisAlignedBB iAxisAlignedBB) {
        double[] dArray = new double[]{wVec3.getXCoord(), wVec3.getYCoord(), wVec3.getZCoord()};
        double[] dArray2 = new double[]{iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()};
        double[] dArray3 = new double[]{iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()};
        int n = 2;
        for (int i = 0; i <= n; ++i) {
            if (dArray[i] > dArray3[i]) {
                dArray[i] = dArray3[i];
                continue;
            }
            if (!(dArray[i] < dArray2[i])) continue;
            dArray[i] = dArray2[i];
        }
        return new WVec3(dArray[0], dArray[1], dArray[2]);
    }

    public static final IMovingObjectPosition rayTraceWithCustomRotation(IEntity iEntity, double d, Rotation rotation) {
        return PlayerExtensionKt.rayTraceWithCustomRotation(iEntity, d, rotation.getYaw(), rotation.getPitch());
    }

    public static final float getHurtPercent(IEntityLivingBase iEntityLivingBase) {
        return PlayerExtensionKt.getRenderHurtTime(iEntityLivingBase) / (float)10;
    }

    public static final boolean isMob(IEntity iEntity) {
        return MinecraftInstance.classProvider.isEntityMob(iEntity) || MinecraftInstance.classProvider.isEntityVillager(iEntity) || MinecraftInstance.classProvider.isEntitySlime(iEntity) || MinecraftInstance.classProvider.isEntityGhast(iEntity) || MinecraftInstance.classProvider.isEntityDragon(iEntity) || MinecraftInstance.classProvider.isEntityShulker(iEntity);
    }

    public static final IMovingObjectPosition rayTraceWithCustomRotation(IEntity iEntity, double d, float f, float f2) {
        WVec3 wVec3 = iEntity.getPositionEyes(1.0f);
        WVec3 wVec32 = ClientUtils.getVectorForRotation(f2, f);
        WVec3 wVec33 = wVec3;
        double d2 = wVec32.getXCoord() * d;
        double d3 = wVec32.getYCoord() * d;
        double d4 = wVec32.getZCoord() * d;
        boolean bl = false;
        WVec3 wVec34 = new WVec3(wVec33.getXCoord() + d2, wVec33.getYCoord() + d3, wVec33.getZCoord() + d4);
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        return iWorldClient.rayTraceBlocks(wVec3, wVec34, false, false, true);
    }
}

