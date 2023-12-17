// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Slot
{
    private final int slotIndex;
    public final IInventory inventory;
    public int slotNumber;
    public int xPos;
    public int yPos;
    
    public Slot(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition) {
        this.inventory = inventoryIn;
        this.slotIndex = index;
        this.xPos = xPosition;
        this.yPos = yPosition;
    }
    
    public void onSlotChange(final ItemStack p_75220_1_, final ItemStack p_75220_2_) {
        final int i = p_75220_2_.getCount() - p_75220_1_.getCount();
        if (i > 0) {
            this.onCrafting(p_75220_2_, i);
        }
    }
    
    protected void onCrafting(final ItemStack stack, final int amount) {
    }
    
    protected void onSwapCraft(final int p_190900_1_) {
    }
    
    protected void onCrafting(final ItemStack stack) {
    }
    
    public ItemStack onTake(final EntityPlayer thePlayer, final ItemStack stack) {
        this.onSlotChanged();
        return stack;
    }
    
    public boolean isItemValid(final ItemStack stack) {
        return true;
    }
    
    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.slotIndex);
    }
    
    public boolean getHasStack() {
        return !this.getStack().isEmpty();
    }
    
    public void putStack(final ItemStack stack) {
        this.inventory.setInventorySlotContents(this.slotIndex, stack);
        this.onSlotChanged();
    }
    
    public void onSlotChanged() {
        this.inventory.markDirty();
    }
    
    public int getSlotStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }
    
    public int getItemStackLimit(final ItemStack stack) {
        return this.getSlotStackLimit();
    }
    
    @Nullable
    public String getSlotTexture() {
        return null;
    }
    
    public ItemStack decrStackSize(final int amount) {
        return this.inventory.decrStackSize(this.slotIndex, amount);
    }
    
    public boolean isHere(final IInventory inv, final int slotIn) {
        return inv == this.inventory && slotIn == this.slotIndex;
    }
    
    public boolean canTakeStack(final EntityPlayer playerIn) {
        return true;
    }
    
    public boolean isEnabled() {
        return true;
    }
}
