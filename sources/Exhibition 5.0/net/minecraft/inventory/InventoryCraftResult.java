// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.item.ItemStack;

public class InventoryCraftResult implements IInventory
{
    private ItemStack[] stackResult;
    private static final String __OBFID = "CL_00001760";
    
    public InventoryCraftResult() {
        this.stackResult = new ItemStack[1];
    }
    
    @Override
    public int getSizeInventory() {
        return 1;
    }
    
    @Override
    public ItemStack getStackInSlot(final int slotIn) {
        return this.stackResult[0];
    }
    
    @Override
    public String getName() {
        return "Result";
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.stackResult[0] != null) {
            final ItemStack var3 = this.stackResult[0];
            this.stackResult[0] = null;
            return var3;
        }
        return null;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(final int index) {
        if (this.stackResult[0] != null) {
            final ItemStack var2 = this.stackResult[0];
            this.stackResult[0] = null;
            return var2;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.stackResult[0] = stack;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void markDirty() {
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer playerIn) {
        return true;
    }
    
    @Override
    public void openInventory(final EntityPlayer playerIn) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer playerIn) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return true;
    }
    
    @Override
    public int getField(final int id) {
        return 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clearInventory() {
        for (int var1 = 0; var1 < this.stackResult.length; ++var1) {
            this.stackResult[var1] = null;
        }
    }
}
