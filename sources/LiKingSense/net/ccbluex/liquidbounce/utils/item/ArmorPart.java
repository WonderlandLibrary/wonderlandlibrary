/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils.item;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0019\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/utils/item/ArmorPart;", "", "itemStack", "Lnet/minecraft/item/ItemStack;", "slot", "", "(Lnet/minecraft/item/ItemStack;I)V", "armorType", "Lnet/minecraft/inventory/EntityEquipmentSlot;", "kotlin.jvm.PlatformType", "getArmorType", "()Lnet/minecraft/inventory/EntityEquipmentSlot;", "getItemStack", "()Lnet/minecraft/item/ItemStack;", "getSlot", "()I", "LiKingSense"})
public final class ArmorPart {
    private final EntityEquipmentSlot armorType;
    @NotNull
    private final ItemStack itemStack;
    private final int slot;

    public final EntityEquipmentSlot getArmorType() {
        return this.armorType;
    }

    @NotNull
    public final ItemStack getItemStack() {
        return this.itemStack;
    }

    public final int getSlot() {
        return this.slot;
    }

    public ArmorPart(@NotNull ItemStack itemStack, int slot) {
        Intrinsics.checkParameterIsNotNull((Object)itemStack, (String)"itemStack");
        this.itemStack = itemStack;
        this.slot = slot;
        Item item = this.itemStack.func_77973_b();
        if (item == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemArmor");
        }
        this.armorType = ((ItemArmor)item).field_77881_a;
    }
}

