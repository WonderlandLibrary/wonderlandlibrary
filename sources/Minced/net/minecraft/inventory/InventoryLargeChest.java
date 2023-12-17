// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.LockCode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.ILockableContainer;

public class InventoryLargeChest implements ILockableContainer
{
    private final String name;
    private final ILockableContainer upperChest;
    private final ILockableContainer lowerChest;
    
    public InventoryLargeChest(final String nameIn, ILockableContainer upperChestIn, ILockableContainer lowerChestIn) {
        this.name = nameIn;
        if (upperChestIn == null) {
            upperChestIn = lowerChestIn;
        }
        if (lowerChestIn == null) {
            lowerChestIn = upperChestIn;
        }
        this.upperChest = upperChestIn;
        this.lowerChest = lowerChestIn;
        if (upperChestIn.isLocked()) {
            lowerChestIn.setLockCode(upperChestIn.getLockCode());
        }
        else if (lowerChestIn.isLocked()) {
            upperChestIn.setLockCode(lowerChestIn.getLockCode());
        }
    }
    
    @Override
    public int getSizeInventory() {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }
    
    @Override
    public boolean isEmpty() {
        return this.upperChest.isEmpty() && this.lowerChest.isEmpty();
    }
    
    public boolean isPartOfLargeChest(final IInventory inventoryIn) {
        return this.upperChest == inventoryIn || this.lowerChest == inventoryIn;
    }
    
    @Override
    public String getName() {
        if (this.upperChest.hasCustomName()) {
            return this.upperChest.getName();
        }
        return this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name;
    }
    
    @Override
    public boolean hasCustomName() {
        return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return (index >= this.upperChest.getSizeInventory()) ? this.lowerChest.getStackInSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(index);
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        return (index >= this.upperChest.getSizeInventory()) ? this.lowerChest.decrStackSize(index - this.upperChest.getSizeInventory(), count) : this.upperChest.decrStackSize(index, count);
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        return (index >= this.upperChest.getSizeInventory()) ? this.lowerChest.removeStackFromSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.removeStackFromSlot(index);
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        if (index >= this.upperChest.getSizeInventory()) {
            this.lowerChest.setInventorySlotContents(index - this.upperChest.getSizeInventory(), stack);
        }
        else {
            this.upperChest.setInventorySlotContents(index, stack);
        }
    }
    
    @Override
    public int getInventoryStackLimit() {
        return this.upperChest.getInventoryStackLimit();
    }
    
    @Override
    public void markDirty() {
        this.upperChest.markDirty();
        this.lowerChest.markDirty();
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return this.upperChest.isUsableByPlayer(player) && this.lowerChest.isUsableByPlayer(player);
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
        this.upperChest.openInventory(player);
        this.lowerChest.openInventory(player);
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
        this.upperChest.closeInventory(player);
        this.lowerChest.closeInventory(player);
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
    public boolean isLocked() {
        return this.upperChest.isLocked() || this.lowerChest.isLocked();
    }
    
    @Override
    public void setLockCode(final LockCode code) {
        this.upperChest.setLockCode(code);
        this.lowerChest.setLockCode(code);
    }
    
    @Override
    public LockCode getLockCode() {
        return this.upperChest.getLockCode();
    }
    
    @Override
    public String getGuiID() {
        return this.upperChest.getGuiID();
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
    }
    
    @Override
    public void clear() {
        this.upperChest.clear();
        this.lowerChest.clear();
    }
}
