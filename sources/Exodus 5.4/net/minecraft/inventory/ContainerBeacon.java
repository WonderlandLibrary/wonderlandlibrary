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
    private final BeaconSlot beaconSlot;
    private IInventory tileBeacon;

    @Override
    public void onCraftGuiOpened(ICrafting iCrafting) {
        super.onCraftGuiOpened(iCrafting);
        iCrafting.func_175173_a(this, this.tileBeacon);
    }

    @Override
    public void updateProgressBar(int n, int n2) {
        this.tileBeacon.setField(n, n2);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n == 0) {
                if (!this.mergeItemStack(itemStack2, 1, 37, true)) {
                    return null;
                }
                slot.onSlotChange(itemStack2, itemStack);
            } else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(itemStack2) && itemStack2.stackSize == 1 ? !this.mergeItemStack(itemStack2, 0, 1, false) : (n >= 1 && n < 28 ? !this.mergeItemStack(itemStack2, 28, 37, false) : (n >= 28 && n < 37 ? !this.mergeItemStack(itemStack2, 1, 28, false) : !this.mergeItemStack(itemStack2, 1, 37, false)))) {
                return null;
            }
            if (itemStack2.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemStack2.stackSize == itemStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(entityPlayer, itemStack2);
        }
        return itemStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        ItemStack itemStack;
        super.onContainerClosed(entityPlayer);
        if (entityPlayer != null && !entityPlayer.worldObj.isRemote && (itemStack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit())) != null) {
            entityPlayer.dropPlayerItemWithRandomChoice(itemStack, false);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.tileBeacon.isUseableByPlayer(entityPlayer);
    }

    public IInventory func_180611_e() {
        return this.tileBeacon;
    }

    public ContainerBeacon(IInventory iInventory, IInventory iInventory2) {
        this.tileBeacon = iInventory2;
        this.beaconSlot = new BeaconSlot(iInventory2, 0, 136, 110);
        this.addSlotToContainer(this.beaconSlot);
        int n = 36;
        int n2 = 137;
        int n3 = 0;
        while (n3 < 3) {
            int n4 = 0;
            while (n4 < 9) {
                this.addSlotToContainer(new Slot(iInventory, n4 + n3 * 9 + 9, n + n4 * 18, n2 + n3 * 18));
                ++n4;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 9) {
            this.addSlotToContainer(new Slot(iInventory, n3, n + n3 * 18, 58 + n2));
            ++n3;
        }
    }

    class BeaconSlot
    extends Slot {
        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public boolean isItemValid(ItemStack itemStack) {
            return itemStack == null ? false : itemStack.getItem() == Items.emerald || itemStack.getItem() == Items.diamond || itemStack.getItem() == Items.gold_ingot || itemStack.getItem() == Items.iron_ingot;
        }

        public BeaconSlot(IInventory iInventory, int n, int n2, int n3) {
            super(iInventory, n, n2, n3);
        }
    }
}

