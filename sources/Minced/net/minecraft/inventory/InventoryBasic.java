// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.util.text.ITextComponent;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryBasic implements IInventory
{
    private String inventoryTitle;
    private final int slotsCount;
    private final NonNullList<ItemStack> inventoryContents;
    private List<IInventoryChangedListener> changeListeners;
    private boolean hasCustomName;
    
    public InventoryBasic(final String title, final boolean customName, final int slotCount) {
        this.inventoryTitle = title;
        this.hasCustomName = customName;
        this.slotsCount = slotCount;
        this.inventoryContents = NonNullList.withSize(slotCount, ItemStack.EMPTY);
    }
    
    public InventoryBasic(final ITextComponent title, final int slotCount) {
        this(title.getUnformattedText(), true, slotCount);
    }
    
    public void addInventoryChangeListener(final IInventoryChangedListener listener) {
        if (this.changeListeners == null) {
            this.changeListeners = (List<IInventoryChangedListener>)Lists.newArrayList();
        }
        this.changeListeners.add(listener);
    }
    
    public void removeInventoryChangeListener(final IInventoryChangedListener listener) {
        this.changeListeners.remove(listener);
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return (index >= 0 && index < this.inventoryContents.size()) ? this.inventoryContents.get(index) : ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        final ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
    }
    
    public ItemStack addItem(final ItemStack stack) {
        final ItemStack itemstack = stack.copy();
        for (int i = 0; i < this.slotsCount; ++i) {
            final ItemStack itemstack2 = this.getStackInSlot(i);
            if (itemstack2.isEmpty()) {
                this.setInventorySlotContents(i, itemstack);
                this.markDirty();
                return ItemStack.EMPTY;
            }
            if (ItemStack.areItemsEqual(itemstack2, itemstack)) {
                final int j = Math.min(this.getInventoryStackLimit(), itemstack2.getMaxStackSize());
                final int k = Math.min(itemstack.getCount(), j - itemstack2.getCount());
                if (k > 0) {
                    itemstack2.grow(k);
                    itemstack.shrink(k);
                    if (itemstack.isEmpty()) {
                        this.markDirty();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        if (itemstack.getCount() != stack.getCount()) {
            this.markDirty();
        }
        return itemstack;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        final ItemStack itemstack = this.inventoryContents.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.inventoryContents.set(index, ItemStack.EMPTY);
        return itemstack;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.inventoryContents.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }
    
    @Override
    public int getSizeInventory() {
        return this.slotsCount;
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.inventoryContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getName() {
        return this.inventoryTitle;
    }
    
    @Override
    public boolean hasCustomName() {
        return this.hasCustomName;
    }
    
    public void setCustomName(final String inventoryTitleIn) {
        this.hasCustomName = true;
        this.inventoryTitle = inventoryTitleIn;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void markDirty() {
        if (this.changeListeners != null) {
            for (int i = 0; i < this.changeListeners.size(); ++i) {
                this.changeListeners.get(i).onInventoryChanged(this);
            }
        }
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
        this.inventoryContents.clear();
    }
}
