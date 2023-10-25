package net.minecraft.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;

class ContainerEnchantment.1
extends InventoryBasic {
    ContainerEnchantment.1(String title, boolean customName, int slotCount) {
        super(title, customName, slotCount);
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void markDirty() {
        super.markDirty();
        ContainerEnchantment.this.onCraftMatrixChanged((IInventory)this);
    }
}
