/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.extensions;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;

public final class PlayerExtensionKt {
    public static final boolean isAnimal(IEntity $this$isAnimal) {
        return MinecraftInstance.classProvider.isEntityAnimal($this$isAnimal) || MinecraftInstance.classProvider.isEntitySquid($this$isAnimal) || MinecraftInstance.classProvider.isEntityGolem($this$isAnimal) || MinecraftInstance.classProvider.isEntityBat($this$isAnimal);
    }

    public static final boolean isMob(IEntity $this$isMob) {
        return MinecraftInstance.classProvider.isEntityMob($this$isMob) || MinecraftInstance.classProvider.isEntityVillager($this$isMob) || MinecraftInstance.classProvider.isEntitySlime($this$isMob) || MinecraftInstance.classProvider.isEntityGhast($this$isMob) || MinecraftInstance.classProvider.isEntityDragon($this$isMob) || MinecraftInstance.classProvider.isEntityShulker($this$isMob);
    }

    public static final boolean isClientFriend(IEntityPlayer $this$isClientFriend) {
        String string = $this$isClientFriend.getName();
        if (string == null) {
            return false;
        }
        String entityName = string;
        return LiquidBounce.INSTANCE.getFileManager().friendsConfig.isFriend(ColorUtils.stripColor(entityName));
    }

    public static final double getDistanceToEntityBox(IEntity $this$getDistanceToEntityBox, IEntity entity) {
        WVec3 eyes = $this$getDistanceToEntityBox.getPositionEyes(1.0f);
        WVec3 pos = PlayerExtensionKt.getNearestPointBB(eyes, entity.getEntityBoundingBox());
        double d = pos.getXCoord() - eyes.getXCoord();
        boolean bl = false;
        double xDist = Math.abs(d);
        double d2 = pos.getYCoord() - eyes.getYCoord();
        boolean bl2 = false;
        double yDist = Math.abs(d2);
        double d3 = pos.getZCoord() - eyes.getZCoord();
        int n = 0;
        double zDist = Math.abs(d3);
        d3 = xDist;
        n = 2;
        boolean bl3 = false;
        double d4 = Math.pow(d3, n);
        d3 = yDist;
        n = 2;
        double d5 = d4;
        bl3 = false;
        double d6 = Math.pow(d3, n);
        d3 = zDist;
        n = 2;
        d5 += d6;
        bl3 = false;
        d6 = Math.pow(d3, n);
        d3 = d5 + d6;
        n = 0;
        return Math.sqrt(d3);
    }

    /*
     * WARNING - void declaration
     */
    public static final WVec3 getNearestPointBB(WVec3 eye, IAxisAlignedBB box) {
        double[] origin = new double[]{eye.getXCoord(), eye.getYCoord(), eye.getZCoord()};
        double[] destMins = new double[]{box.getMinX(), box.getMinY(), box.getMinZ()};
        double[] destMaxs = new double[]{box.getMaxX(), box.getMaxY(), box.getMaxZ()};
        int n = 0;
        int n2 = 2;
        while (n <= n2) {
            void i;
            if (origin[i] > destMaxs[i]) {
                origin[i] = destMaxs[i];
            } else if (origin[i] < destMins[i]) {
                origin[i] = destMins[i];
            }
            ++i;
        }
        return new WVec3(origin[0], origin[1], origin[2]);
    }

    public static final int getPing(IEntityPlayer $this$getPing) {
        INetworkPlayerInfo playerInfo;
        INetworkPlayerInfo iNetworkPlayerInfo = playerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo($this$getPing.getUniqueID());
        return iNetworkPlayerInfo != null ? iNetworkPlayerInfo.getResponseTime() : 0;
    }
}

