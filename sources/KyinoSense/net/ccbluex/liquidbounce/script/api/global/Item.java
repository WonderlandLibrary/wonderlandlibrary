/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.script.api.global;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/script/api/global/Item;", "", "()V", "create", "Lnet/minecraft/item/ItemStack;", "itemArguments", "", "KyinoClient"})
public final class Item {
    public static final Item INSTANCE;

    @JvmStatic
    @NotNull
    public static final ItemStack create(@NotNull String itemArguments) {
        Intrinsics.checkParameterIsNotNull(itemArguments, "itemArguments");
        ItemStack itemStack = ItemUtils.createItem(itemArguments);
        Intrinsics.checkExpressionValueIsNotNull(itemStack, "ItemUtils.createItem(itemArguments)");
        return itemStack;
    }

    private Item() {
    }

    static {
        Item item;
        INSTANCE = item = new Item();
    }
}

