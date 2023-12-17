// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerShulkerBox extends Container
{
    private final IInventory inventory;
    
    public ContainerShulkerBox(final InventoryPlayer playerInventoryIn, final IInventory inventoryIn, final EntityPlayer player) {
        (this.inventory = inventoryIn).openInventory(player);
        final int i = 3;
        final int j = 9;
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlotToContainer(new SlotShulkerBox(inventoryIn, l + k * 9, 8 + l * 18, 18 + k * 18));
            }
        }
        for (int i2 = 0; i2 < 3; ++i2) {
            for (int k2 = 0; k2 < 9; ++k2) {
                this.addSlotToContainer(new Slot(playerInventoryIn, k2 + i2 * 9 + 9, 8 + k2 * 18, 84 + i2 * 18));
            }
        }
        for (int j2 = 0; j2 < 9; ++j2) {
            this.addSlotToContainer(new Slot(playerInventoryIn, j2, 8 + j2 * 18, 142));
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.inventory.isUsableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index < this.inventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack2, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 0, this.inventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack2.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.inventory.closeInventory(playerIn);
    }
}
