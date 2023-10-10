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
    public SlotFurnaceFuel(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (TileEntityFurnace.isItemFuel(stack)) return true;
        if (SlotFurnaceFuel.isBucket(stack)) return true;
        return false;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        if (SlotFurnaceFuel.isBucket(stack)) {
            return 1;
        }
        int n = super.getItemStackLimit(stack);
        return n;
    }

    public static boolean isBucket(ItemStack stack) {
        if (stack == null) return false;
        if (stack.getItem() == null) return false;
        if (stack.getItem() != Items.bucket) return false;
        return true;
    }
}

