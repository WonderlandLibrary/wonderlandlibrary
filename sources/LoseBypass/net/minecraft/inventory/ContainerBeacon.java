/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
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
        this.beaconSlot = new BeaconSlot(tileBeaconIn, 0, 136, 110);
        this.addSlotToContainer(this.beaconSlot);
        int i = 36;
        int j = 137;
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlotToContainer(new Slot(playerInventory, l + k * 9 + 9, i + l * 18, j + k * 18));
            }
        }
        int i1 = 0;
        while (i1 < 9) {
            this.addSlotToContainer(new Slot(playerInventory, i1, i + i1 * 18, 58 + j));
            ++i1;
        }
    }

    @Override
    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.func_175173_a(this, this.tileBeacon);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        this.tileBeacon.setField(id, data);
    }

    public IInventory func_180611_e() {
        return this.tileBeacon;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (playerIn == null) return;
        if (playerIn.worldObj.isRemote) return;
        ItemStack itemstack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit());
        if (itemstack == null) return;
        playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileBeacon.isUseableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot == null) return itemstack;
        if (!slot.getHasStack()) return itemstack;
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
            slot.putStack(null);
        } else {
            slot.onSlotChanged();
        }
        if (itemstack1.stackSize == itemstack.stackSize) {
            return null;
        }
        slot.onPickupFromSlot(playerIn, itemstack1);
        return itemstack;
    }

    class BeaconSlot
    extends Slot {
        public BeaconSlot(IInventory p_i1801_2_, int p_i1801_3_, int p_i1801_4_, int p_i1801_5_) {
            super(p_i1801_2_, p_i1801_3_, p_i1801_4_, p_i1801_5_);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            if (stack == null) {
                return false;
            }
            if (stack.getItem() == Items.emerald) return true;
            if (stack.getItem() == Items.diamond) return true;
            if (stack.getItem() == Items.gold_ingot) return true;
            if (stack.getItem() == Items.iron_ingot) return true;
            return false;
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}

