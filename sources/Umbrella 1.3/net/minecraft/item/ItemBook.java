/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBook
extends Item {
    private static final String __OBFID = "CL_00001775";

    @Override
    public boolean isItemTool(ItemStack stack) {
        return stack.stackSize == 1;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }
}

