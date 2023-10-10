/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory
extends Container {
    private IInventory horseInventory;
    private EntityHorse theHorse;

    public ContainerHorseInventory(IInventory playerInventory, IInventory horseInventoryIn, final EntityHorse horse, EntityPlayer player) {
        this.horseInventory = horseInventoryIn;
        this.theHorse = horse;
        int i = 3;
        horseInventoryIn.openInventory(player);
        int j = (i - 4) * 18;
        this.addSlotToContainer(new Slot(horseInventoryIn, 0, 8, 18){

            @Override
            public boolean isItemValid(ItemStack stack) {
                if (!super.isItemValid(stack)) return false;
                if (stack.getItem() != Items.saddle) return false;
                if (this.getHasStack()) return false;
                return true;
            }
        });
        this.addSlotToContainer(new Slot(horseInventoryIn, 1, 8, 36){

            @Override
            public boolean isItemValid(ItemStack stack) {
                if (!super.isItemValid(stack)) return false;
                if (!horse.canWearArmor()) return false;
                if (!EntityHorse.isArmorItem(stack.getItem())) return false;
                return true;
            }

            @Override
            public boolean canBeHovered() {
                return horse.canWearArmor();
            }
        });
        if (horse.isChested()) {
            for (int k = 0; k < i; ++k) {
                for (int l = 0; l < 5; ++l) {
                    this.addSlotToContainer(new Slot(horseInventoryIn, 2 + l + k * 5, 80 + l * 18, 18 + k * 18));
                }
            }
        }
        for (int i1 = 0; i1 < 3; ++i1) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlotToContainer(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + j));
            }
        }
        int j1 = 0;
        while (j1 < 9) {
            this.addSlotToContainer(new Slot(playerInventory, j1, 8 + j1 * 18, 160 + j));
            ++j1;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (!this.horseInventory.isUseableByPlayer(playerIn)) return false;
        if (!this.theHorse.isEntityAlive()) return false;
        if (!(this.theHorse.getDistanceToEntity(playerIn) < 8.0f)) return false;
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot == null) return itemstack;
        if (!slot.getHasStack()) return itemstack;
        ItemStack itemstack1 = slot.getStack();
        itemstack = itemstack1.copy();
        if (index < this.horseInventory.getSizeInventory()) {
            if (!this.mergeItemStack(itemstack1, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                return null;
            }
        } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
            if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                return null;
            }
        } else if (this.getSlot(0).isItemValid(itemstack1)) {
            if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                return null;
            }
        } else {
            if (this.horseInventory.getSizeInventory() <= 2) return null;
            if (!this.mergeItemStack(itemstack1, 2, this.horseInventory.getSizeInventory(), false)) {
                return null;
            }
        }
        if (itemstack1.stackSize == 0) {
            slot.putStack(null);
            return itemstack;
        }
        slot.onSlotChanged();
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.horseInventory.closeInventory(playerIn);
    }
}

