// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBeacon extends Container
{
    private IInventory tileBeacon;
    private final BeaconSlot beaconSlot;
    private static final String __OBFID = "CL_00001735";
    
    public ContainerBeacon(final IInventory p_i45804_1_, final IInventory p_i45804_2_) {
        this.tileBeacon = p_i45804_2_;
        this.addSlotToContainer(this.beaconSlot = new BeaconSlot(p_i45804_2_, 0, 136, 110));
        final byte var3 = 36;
        final short var4 = 137;
        for (int var5 = 0; var5 < 3; ++var5) {
            for (int var6 = 0; var6 < 9; ++var6) {
                this.addSlotToContainer(new Slot(p_i45804_1_, var6 + var5 * 9 + 9, var3 + var6 * 18, var4 + var5 * 18));
            }
        }
        for (int var5 = 0; var5 < 9; ++var5) {
            this.addSlotToContainer(new Slot(p_i45804_1_, var5, var3 + var5 * 18, 58 + var4));
        }
    }
    
    @Override
    public void onCraftGuiOpened(final ICrafting p_75132_1_) {
        super.onCraftGuiOpened(p_75132_1_);
        p_75132_1_.func_175173_a(this, this.tileBeacon);
    }
    
    @Override
    public void updateProgressBar(final int p_75137_1_, final int p_75137_2_) {
        this.tileBeacon.setField(p_75137_1_, p_75137_2_);
    }
    
    public IInventory func_180611_e() {
        return this.tileBeacon;
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.tileBeacon.isUseableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack var3 = null;
        final Slot var4 = this.inventorySlots.get(index);
        if (var4 != null && var4.getHasStack()) {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (index == 0) {
                if (!this.mergeItemStack(var5, 1, 37, true)) {
                    return null;
                }
                var4.onSlotChange(var5, var3);
            }
            else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(var5) && var5.stackSize == 1) {
                if (!this.mergeItemStack(var5, 0, 1, false)) {
                    return null;
                }
            }
            else if (index >= 1 && index < 28) {
                if (!this.mergeItemStack(var5, 28, 37, false)) {
                    return null;
                }
            }
            else if (index >= 28 && index < 37) {
                if (!this.mergeItemStack(var5, 1, 28, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 1, 37, false)) {
                return null;
            }
            if (var5.stackSize == 0) {
                var4.putStack(null);
            }
            else {
                var4.onSlotChanged();
            }
            if (var5.stackSize == var3.stackSize) {
                return null;
            }
            var4.onPickupFromSlot(playerIn, var5);
        }
        return var3;
    }
    
    class BeaconSlot extends Slot
    {
        private static final String __OBFID = "CL_00001736";
        
        public BeaconSlot(final IInventory p_i1801_2_, final int p_i1801_3_, final int p_i1801_4_, final int p_i1801_5_) {
            super(p_i1801_2_, p_i1801_3_, p_i1801_4_, p_i1801_5_);
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            return stack != null && (stack.getItem() == Items.emerald || stack.getItem() == Items.diamond || stack.getItem() == Items.gold_ingot || stack.getItem() == Items.iron_ingot);
        }
        
        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}
