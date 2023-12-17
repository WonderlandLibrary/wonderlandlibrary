// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBeacon extends Container
{
    private final IInventory tileBeacon;
    private final BeaconSlot beaconSlot;
    
    public ContainerBeacon(final IInventory playerInventory, final IInventory tileBeaconIn) {
        this.tileBeacon = tileBeaconIn;
        this.addSlotToContainer(this.beaconSlot = new BeaconSlot(tileBeaconIn, 0, 136, 110));
        final int i = 36;
        final int j = 137;
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlotToContainer(new Slot(playerInventory, l + k * 9 + 9, 36 + l * 18, 137 + k * 18));
            }
        }
        for (int i2 = 0; i2 < 9; ++i2) {
            this.addSlotToContainer(new Slot(playerInventory, i2, 36 + i2 * 18, 195));
        }
    }
    
    @Override
    public void addListener(final IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBeacon);
    }
    
    @Override
    public void updateProgressBar(final int id, final int data) {
        this.tileBeacon.setField(id, data);
    }
    
    public IInventory getTileEntity() {
        return this.tileBeacon;
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!playerIn.world.isRemote) {
            final ItemStack itemstack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit());
            if (!itemstack.isEmpty()) {
                playerIn.dropItem(itemstack, false);
            }
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.tileBeacon.isUsableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack2, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(itemstack2) && itemstack2.getCount() == 1) {
                if (!this.mergeItemStack(itemstack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 1 && index < 28) {
                if (!this.mergeItemStack(itemstack2, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 28 && index < 37) {
                if (!this.mergeItemStack(itemstack2, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 1, 37, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack2.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack2.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack2);
        }
        return itemstack;
    }
    
    class BeaconSlot extends Slot
    {
        public BeaconSlot(final IInventory inventoryIn, final int index, final int xIn, final int yIn) {
            super(inventoryIn, index, xIn, yIn);
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            final Item item = stack.getItem();
            return item == Items.EMERALD || item == Items.DIAMOND || item == Items.GOLD_INGOT || item == Items.IRON_INGOT;
        }
        
        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}
