/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBook
extends Item {
    @Override
    public boolean isItemTool(ItemStack stack) {
        if (stack.stackSize != 1) return false;
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }
}

