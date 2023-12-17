// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.village.MerchantRecipeList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import java.util.List;
import java.util.Iterator;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.entity.IMerchant;

public class InventoryMerchant implements IInventory
{
    private final IMerchant merchant;
    private final NonNullList<ItemStack> slots;
    private final EntityPlayer player;
    private MerchantRecipe currentRecipe;
    private int currentRecipeIndex;
    
    public InventoryMerchant(final EntityPlayer thePlayerIn, final IMerchant theMerchantIn) {
        this.slots = NonNullList.withSize(3, ItemStack.EMPTY);
        this.player = thePlayerIn;
        this.merchant = theMerchantIn;
    }
    
    @Override
    public int getSizeInventory() {
        return this.slots.size();
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.slots) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.slots.get(index);
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        final ItemStack itemstack = this.slots.get(index);
        if (index == 2 && !itemstack.isEmpty()) {
            return ItemStackHelper.getAndSplit(this.slots, index, itemstack.getCount());
        }
        final ItemStack itemstack2 = ItemStackHelper.getAndSplit(this.slots, index, count);
        if (!itemstack2.isEmpty() && this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
        }
        return itemstack2;
    }
    
    private boolean inventoryResetNeededOnSlotChange(final int slotIn) {
        return slotIn == 0 || slotIn == 1;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        return ItemStackHelper.getAndRemove(this.slots, index);
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.slots.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if (this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
        }
    }
    
    @Override
    public String getName() {
        return "mob.villager";
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
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return this.merchant.getCustomer() == player;
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
    public void markDirty() {
        this.resetRecipeAndSlots();
    }
    
    public void resetRecipeAndSlots() {
        this.currentRecipe = null;
        ItemStack itemstack = this.slots.get(0);
        ItemStack itemstack2 = this.slots.get(1);
        if (itemstack.isEmpty()) {
            itemstack = itemstack2;
            itemstack2 = ItemStack.EMPTY;
        }
        if (itemstack.isEmpty()) {
            this.setInventorySlotContents(2, ItemStack.EMPTY);
        }
        else {
            final MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.player);
            if (merchantrecipelist != null) {
                MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack2, this.currentRecipeIndex);
                if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                    this.currentRecipe = merchantrecipe;
                    this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                }
                else if (!itemstack2.isEmpty()) {
                    merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack2, itemstack, this.currentRecipeIndex);
                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                        this.currentRecipe = merchantrecipe;
                        this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                    }
                    else {
                        this.setInventorySlotContents(2, ItemStack.EMPTY);
                    }
                }
                else {
                    this.setInventorySlotContents(2, ItemStack.EMPTY);
                }
            }
            this.merchant.verifySellingItem(this.getStackInSlot(2));
        }
    }
    
    public MerchantRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }
    
    public void setCurrentRecipeIndex(final int currentRecipeIndexIn) {
        this.currentRecipeIndex = currentRecipeIndexIn;
        this.resetRecipeAndSlots();
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
        this.slots.clear();
    }
}
