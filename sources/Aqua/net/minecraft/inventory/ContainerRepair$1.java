package net.minecraft.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;

class ContainerRepair.1
extends InventoryBasic {
    ContainerRepair.1(String title, boolean customName, int slotCount) {
        super(title, customName, slotCount);
    }

    public void markDirty() {
        super.markDirty();
        ContainerRepair.this.onCraftMatrixChanged((IInventory)this);
    }
}
