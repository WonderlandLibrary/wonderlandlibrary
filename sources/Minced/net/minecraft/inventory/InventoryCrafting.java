// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryCrafting implements IInventory
{
    private final NonNullList<ItemStack> stackList;
    private final int inventoryWidth;
    private final int inventoryHeight;
    private final Container eventHandler;
    
    public InventoryCrafting(final Container eventHandlerIn, final int width, final int height) {
        this.stackList = NonNullList.withSize(width * height, ItemStack.EMPTY);
        this.eventHandler = eventHandlerIn;
        this.inventoryWidth = width;
        this.inventoryHeight = height;
    }
    
    @Override
    public int getSizeInventory() {
        return this.stackList.size();
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.stackList) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return (index >= this.getSizeInventory()) ? ItemStack.EMPTY : this.stackList.get(index);
    }
    
    public ItemStack getStackInRowAndColumn(final int row, final int column) {
        return (row >= 0 && row < this.inventoryWidth && column >= 0 && column <= this.inventoryHeight) ? this.getStackInSlot(row + column * this.inventoryWidth) : ItemStack.EMPTY;
    }
    
    @Override
    public String getName() {
        return "container.crafting";
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        return ItemStackHelper.getAndRemove(this.stackList, index);
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        final ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);
        if (!itemstack.isEmpty()) {
            this.eventHandler.onCraftMatrixChanged(this);
        }
        return itemstack;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.stackList.set(index, stack);
        this.eventHandler.onCraftMatrixChanged(this);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void markDirty() {
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return true;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
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
    public void clear() {
        this.stackList.clear();
    }
    
    public int getHeight() {
        return this.inventoryHeight;
    }
    
    public int getWidth() {
        return this.inventoryWidth;
    }
    
    public void fillStackedContents(final RecipeItemHelper helper) {
        for (final ItemStack itemstack : this.stackList) {
            helper.accountStack(itemstack);
        }
    }
}
