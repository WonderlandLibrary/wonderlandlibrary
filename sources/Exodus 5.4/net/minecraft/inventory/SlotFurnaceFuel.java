/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class SlotFurnaceFuel
extends Slot {
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return TileEntityFurnace.isItemFuel(itemStack) || SlotFurnaceFuel.isBucket(itemStack);
    }

    @Override
    public int getItemStackLimit(ItemStack itemStack) {
        return SlotFurnaceFuel.isBucket(itemStack) ? 1 : super.getItemStackLimit(itemStack);
    }

    public SlotFurnaceFuel(IInventory iInventory, int n, int n2, int n3) {
        super(iInventory, n, n2, n3);
    }

    public static boolean isBucket(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() != null && itemStack.getItem() == Items.bucket;
    }
}

