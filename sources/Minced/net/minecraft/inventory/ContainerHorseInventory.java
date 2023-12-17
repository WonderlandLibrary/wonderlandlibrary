// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.AbstractHorse;

public class ContainerHorseInventory extends Container
{
    private final IInventory horseInventory;
    private final AbstractHorse horse;
    
    public ContainerHorseInventory(final IInventory playerInventory, final IInventory horseInventoryIn, final AbstractHorse horse, final EntityPlayer player) {
        this.horseInventory = horseInventoryIn;
        this.horse = horse;
        final int i = 3;
        horseInventoryIn.openInventory(player);
        final int j = -18;
        this.addSlotToContainer(new Slot(horseInventoryIn, 0, 8, 18) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.getHasStack() && horse.canBeSaddled();
            }
            
            @Override
            public boolean isEnabled() {
                return horse.canBeSaddled();
            }
        });
        this.addSlotToContainer(new Slot(horseInventoryIn, 1, 8, 36) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return horse.isArmor(stack);
            }
            
            @Override
            public boolean isEnabled() {
                return horse.wearsArmor();
            }
            
            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        if (horse instanceof AbstractChestHorse && ((AbstractChestHorse)horse).hasChest()) {
            for (int k = 0; k < 3; ++k) {
                for (int l = 0; l < ((AbstractChestHorse)horse).getInventoryColumns(); ++l) {
                    this.addSlotToContainer(new Slot(horseInventoryIn, 2 + l + k * ((AbstractChestHorse)horse).getInventoryColumns(), 80 + l * 18, 18 + k * 18));
                }
            }
        }
        for (int i2 = 0; i2 < 3; ++i2) {
            for (int k2 = 0; k2 < 9; ++k2) {
                this.addSlotToContainer(new Slot(playerInventory, k2 + i2 * 9 + 9, 8 + k2 * 18, 102 + i2 * 18 - 18));
            }
        }
        for (int j2 = 0; j2 < 9; ++j2) {
            this.addSlotToContainer(new Slot(playerInventory, j2, 8 + j2 * 18, 142));
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.horseInventory.isUsableByPlayer(playerIn) && this.horse.isEntityAlive() && this.horse.getDistance(playerIn) < 8.0f;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index < this.horseInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack2, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(1).isItemValid(itemstack2) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(itemstack2, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(0).isItemValid(itemstack2)) {
                if (!this.mergeItemStack(itemstack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.horseInventory.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack2, 2, this.horseInventory.getSizeInventory(), false)) {
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
        this.horseInventory.closeInventory(playerIn);
    }
}
