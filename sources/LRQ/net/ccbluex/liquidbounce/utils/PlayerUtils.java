/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.BlockSlime
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.util.math.MathHelper
 */
package net.ccbluex.liquidbounce.utils;

import java.util.Collection;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.BlockSlime;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.util.math.MathHelper;

public final class PlayerUtils {
    public static final PlayerUtils INSTANCE;

    public final String randomUnicode(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        String string = str;
        int n = 0;
        for (char c : string.toCharArray()) {
            if (Math.random() > 0.5 && 33 <= (n = Character.hashCode(c)) && 128 >= n) {
                stringBuilder.append(Character.toChars(Character.hashCode(c) + 65248));
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public final double getAr(IEntityLivingBase player) {
        double arPercentage = (float)player.getTotalArmorValue() / player.getMaxHealth();
        arPercentage = MathHelper.func_151237_a((double)arPercentage, (double)0.0, (double)1.0);
        return (double)100 * arPercentage;
    }

    public final double getHp(IEntityLivingBase player) {
        float heal = (int)player.getHealth();
        double hpPercentage = heal / player.getMaxHealth();
        hpPercentage = MathHelper.func_151237_a((double)hpPercentage, (double)0.0, (double)1.0);
        return (double)100 * hpPercentage;
    }

    public final boolean isUsingFood() {
        boolean bl;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = iEntityPlayerSP.getItemInUse();
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        IItem usingItem = iItemStack.getItem();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP2.getItemInUse() != null) {
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            bl = iEntityPlayerSP3.isUsingItem() && (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion);
        } else {
            bl = false;
        }
        return bl;
    }

    public final boolean isBlockUnder() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getPosY() < 0.0) {
            return false;
        }
        int off = 0;
        while (true) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (off >= (int)iEntityPlayerSP2.getPosY() + 2) break;
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            IAxisAlignedBB bb = iEntityPlayerSP3.getEntityBoundingBox().offset(0.0, -((double)off), 0.0);
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            Collection<IAxisAlignedBB> collection = iWorldClient.getCollidingBoundingBoxes(iEntityPlayerSP4, bb);
            boolean bl = false;
            if (!collection.isEmpty()) {
                return true;
            }
            off += 2;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    public final Integer findSlimeBlock() {
        int n = 0;
        int n2 = 8;
        while (n <= n2) {
            void i;
            IItemStack itemStack;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if ((itemStack = iEntityPlayerSP.getInventory().getStackInSlot((int)i)) != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemBlock) {
                IItem iItem = itemStack.getItem();
                if (iItem == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
                }
                ItemBlock block = (ItemBlock)iItem;
                if (block.func_179223_d() instanceof BlockSlime) {
                    return (int)i;
                }
            }
            ++i;
        }
        return -1;
    }

    private PlayerUtils() {
    }

    static {
        PlayerUtils playerUtils;
        INSTANCE = playerUtils = new PlayerUtils();
    }
}

