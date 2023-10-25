package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBeacon
extends Container {
    private IInventory tileBeacon;
    private final BeaconSlot beaconSlot;

    public ContainerBeacon(IInventory playerInventory, IInventory tileBeaconIn) {
        this.tileBeacon = tileBeaconIn;
        this.beaconSlot = new BeaconSlot(this, tileBeaconIn, 0, 136, 110);
        this.addSlotToContainer((Slot)this.beaconSlot);
        int i = 36;
        int j = 137;
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlotToContainer(new Slot(playerInventory, l + k * 9 + 9, i + l * 18, j + k * 18));
            }
        }
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1, i + i1 * 18, 58 + j));
        }
    }

    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.sendAllWindowProperties((Container)this, this.tileBeacon);
    }

    public void updateProgressBar(int id, int data) {
        this.tileBeacon.setField(id, data);
    }

    public IInventory func_180611_e() {
        return this.tileBeacon;
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        ItemStack itemstack;
        super.onContainerClosed(playerIn);
        if (playerIn != null && !playerIn.worldObj.isRemote && (itemstack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit())) != null) {
            playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileBeacon.isUseableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(itemstack1) && itemstack1.stackSize == 1 ? !this.mergeItemStack(itemstack1, 0, 1, false) : (index >= 1 && index < 28 ? !this.mergeItemStack(itemstack1, 28, 37, false) : (index >= 28 && index < 37 ? !this.mergeItemStack(itemstack1, 1, 28, false) : !this.mergeItemStack(itemstack1, 1, 37, false)))) {
                return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(playerIn, itemstack1);
        }
        return itemstack;
    }
}
