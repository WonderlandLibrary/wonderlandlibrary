// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.village.MerchantRecipeList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.IMerchant;

public class InventoryMerchant implements IInventory
{
    private final IMerchant theMerchant;
    private ItemStack[] theInventory;
    private final EntityPlayer thePlayer;
    private MerchantRecipe currentRecipe;
    private int currentRecipeIndex;
    private static final String __OBFID = "CL_00001756";
    
    public InventoryMerchant(final EntityPlayer p_i1820_1_, final IMerchant p_i1820_2_) {
        this.theInventory = new ItemStack[3];
        this.thePlayer = p_i1820_1_;
        this.theMerchant = p_i1820_2_;
    }
    
    @Override
    public int getSizeInventory() {
        return this.theInventory.length;
    }
    
    @Override
    public ItemStack getStackInSlot(final int slotIn) {
        return this.theInventory[slotIn];
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.theInventory[index] == null) {
            return null;
        }
        if (index == 2) {
            final ItemStack var3 = this.theInventory[index];
            this.theInventory[index] = null;
            return var3;
        }
        if (this.theInventory[index].stackSize <= count) {
            final ItemStack var3 = this.theInventory[index];
            this.theInventory[index] = null;
            if (this.inventoryResetNeededOnSlotChange(index)) {
                this.resetRecipeAndSlots();
            }
            return var3;
        }
        final ItemStack var3 = this.theInventory[index].splitStack(count);
        if (this.theInventory[index].stackSize == 0) {
            this.theInventory[index] = null;
        }
        if (this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
        }
        return var3;
    }
    
    private boolean inventoryResetNeededOnSlotChange(final int p_70469_1_) {
        return p_70469_1_ == 0 || p_70469_1_ == 1;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(final int index) {
        if (this.theInventory[index] != null) {
            final ItemStack var2 = this.theInventory[index];
            this.theInventory[index] = null;
            return var2;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.theInventory[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
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
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer playerIn) {
        return this.theMerchant.getCustomer() == playerIn;
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
    public void markDirty() {
        this.resetRecipeAndSlots();
    }
    
    public void resetRecipeAndSlots() {
        this.currentRecipe = null;
        ItemStack var1 = this.theInventory[0];
        ItemStack var2 = this.theInventory[1];
        if (var1 == null) {
            var1 = var2;
            var2 = null;
        }
        if (var1 == null) {
            this.setInventorySlotContents(2, null);
        }
        else {
            final MerchantRecipeList var3 = this.theMerchant.getRecipes(this.thePlayer);
            if (var3 != null) {
                MerchantRecipe var4 = var3.canRecipeBeUsed(var1, var2, this.currentRecipeIndex);
                if (var4 != null && !var4.isRecipeDisabled()) {
                    this.currentRecipe = var4;
                    this.setInventorySlotContents(2, var4.getItemToSell().copy());
                }
                else if (var2 != null) {
                    var4 = var3.canRecipeBeUsed(var2, var1, this.currentRecipeIndex);
                    if (var4 != null && !var4.isRecipeDisabled()) {
                        this.currentRecipe = var4;
                        this.setInventorySlotContents(2, var4.getItemToSell().copy());
                    }
                    else {
                        this.setInventorySlotContents(2, null);
                    }
                }
                else {
                    this.setInventorySlotContents(2, null);
                }
            }
        }
        this.theMerchant.verifySellingItem(this.getStackInSlot(2));
    }
    
    public MerchantRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }
    
    public void setCurrentRecipeIndex(final int p_70471_1_) {
        this.currentRecipeIndex = p_70471_1_;
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
    public void clearInventory() {
        for (int var1 = 0; var1 < this.theInventory.length; ++var1) {
            this.theInventory[var1] = null;
        }
    }
}
