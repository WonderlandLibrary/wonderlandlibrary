/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockSlime
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.AxisAlignedBB
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\r\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\u0006\u0010\u0006\u001a\u00020\u0007J\u0006\u0010\b\u001a\u00020\u0007J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/utils/PlayerUtils;", "", "()V", "findSlimeBlock", "", "()Ljava/lang/Integer;", "isBlockUnder", "", "isUsingFood", "randomUnicode", "", "str", "KyinoClient"})
public final class PlayerUtils {
    public static final PlayerUtils INSTANCE;

    @NotNull
    public final String randomUnicode(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "str");
        StringBuilder stringBuilder = new StringBuilder();
        String string = str;
        boolean bl = false;
        char[] cArray = string.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull(cArray, "(this as java.lang.String).toCharArray()");
        for (char c : cArray) {
        }
        String string2 = stringBuilder.toString();
        Intrinsics.checkExpressionValueIsNotNull(string2, "stringBuilder.toString()");
        return string2;
    }

    public final boolean isUsingFood() {
        boolean bl;
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        ItemStack itemStack = entityPlayerSP.func_71011_bu();
        Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.itemInUse");
        Item usingItem = itemStack.func_77973_b();
        EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        if (entityPlayerSP2.func_71011_bu() != null) {
            EntityPlayerSP entityPlayerSP3 = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
            bl = entityPlayerSP3.func_71039_bw() && (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion);
        } else {
            bl = false;
        }
        return bl;
    }

    public final boolean isBlockUnder() {
        if (MinecraftInstance.mc.field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)MinecraftInstance.mc.field_71439_g.field_70163_u + 2; off += 2) {
            AxisAlignedBB bb;
            Intrinsics.checkExpressionValueIsNotNull(MinecraftInstance.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -((double)off), 0.0), "mc.thePlayer.getEntityBo\u2026.0, -off.toDouble(), 0.0)");
            List list = MinecraftInstance.mc.field_71441_e.func_72945_a((Entity)MinecraftInstance.mc.field_71439_g, bb);
            Intrinsics.checkExpressionValueIsNotNull(list, "mc.theWorld.getColliding\u2026     bb\n                )");
            Collection collection = list;
            boolean bl = false;
            if (!(!collection.isEmpty())) continue;
            return true;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public final Integer findSlimeBlock() {
        int n = 0;
        int n2 = 8;
        while (n <= n2) {
            void i;
            ItemStack itemStack = MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a((int)i);
            if (itemStack != null && itemStack.func_77973_b() != null && itemStack.func_77973_b() instanceof ItemBlock) {
                Item item = itemStack.func_77973_b();
                if (item == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
                }
                ItemBlock block = (ItemBlock)item;
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

