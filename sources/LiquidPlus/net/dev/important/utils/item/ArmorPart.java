/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.utils.item;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\r"}, d2={"Lnet/dev/important/utils/item/ArmorPart;", "", "itemStack", "Lnet/minecraft/item/ItemStack;", "slot", "", "(Lnet/minecraft/item/ItemStack;I)V", "armorType", "getArmorType", "()I", "getItemStack", "()Lnet/minecraft/item/ItemStack;", "getSlot", "LiquidBounce"})
public final class ArmorPart {
    @NotNull
    private final ItemStack itemStack;
    private final int slot;
    private final int armorType;

    public ArmorPart(@NotNull ItemStack itemStack, int slot) {
        Intrinsics.checkNotNullParameter(itemStack, "itemStack");
        this.itemStack = itemStack;
        this.slot = slot;
        Item item = this.itemStack.func_77973_b();
        if (item == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.minecraft.item.ItemArmor");
        }
        this.armorType = ((ItemArmor)item).field_77881_a;
    }

    @NotNull
    public final ItemStack getItemStack() {
        return this.itemStack;
    }

    public final int getSlot() {
        return this.slot;
    }

    public final int getArmorType() {
        return this.armorType;
    }
}

