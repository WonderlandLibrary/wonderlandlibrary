/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 */
package net.ccbluex.liquidbounce.utils.item;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ArmorPiece {
    private ItemStack itemStack;
    private int slot;

    public ArmorPiece(ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

    public int getArmorType() {
        return ((ItemArmor)this.itemStack.func_77973_b()).field_77881_a;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}

