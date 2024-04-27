/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.item;

import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;

public class ArmorPiece {
    private final int slot;
    private final IItemStack itemStack;

    public IItemStack getItemStack() {
        return this.itemStack;
    }

    public int getSlot() {
        return this.slot;
    }

    public ArmorPiece(IItemStack iItemStack, int n) {
        this.itemStack = iItemStack;
        this.slot = n;
    }

    public int getArmorType() {
        return this.itemStack.getItem().asItemArmor().getArmorType();
    }
}

