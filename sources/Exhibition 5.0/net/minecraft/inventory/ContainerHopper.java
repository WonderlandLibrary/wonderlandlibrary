// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerHopper extends Container
{
    private final IInventory field_94538_a;
    private static final String __OBFID = "CL_00001750";
    
    public ContainerHopper(final InventoryPlayer p_i45792_1_, final IInventory p_i45792_2_, final EntityPlayer p_i45792_3_) {
        (this.field_94538_a = p_i45792_2_).openInventory(p_i45792_3_);
        final byte var4 = 51;
        for (int var5 = 0; var5 < p_i45792_2_.getSizeInventory(); ++var5) {
            this.addSlotToContainer(new Slot(p_i45792_2_, var5, 44 + var5 * 18, 20));
        }
        for (int var5 = 0; var5 < 3; ++var5) {
            for (int var6 = 0; var6 < 9; ++var6) {
                this.addSlotToContainer(new Slot(p_i45792_1_, var6 + var5 * 9 + 9, 8 + var6 * 18, var5 * 18 + var4));
            }
        }
        for (int var5 = 0; var5 < 9; ++var5) {
            this.addSlotToContainer(new Slot(p_i45792_1_, var5, 8 + var5 * 18, 58 + var4));
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.field_94538_a.isUseableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack var3 = null;
        final Slot var4 = this.inventorySlots.get(index);
        if (var4 != null && var4.getHasStack()) {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (index < this.field_94538_a.getSizeInventory()) {
                if (!this.mergeItemStack(var5, this.field_94538_a.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, this.field_94538_a.getSizeInventory(), false)) {
                return null;
            }
            if (var5.stackSize == 0) {
                var4.putStack(null);
            }
            else {
                var4.onSlotChanged();
            }
        }
        return var3;
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        this.field_94538_a.closeInventory(p_75134_1_);
    }
}
