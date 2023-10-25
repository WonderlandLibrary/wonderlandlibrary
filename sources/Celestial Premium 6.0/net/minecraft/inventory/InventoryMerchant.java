/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant
implements IInventory {
    private final IMerchant theMerchant;
    private final NonNullList<ItemStack> theInventory = NonNullList.func_191197_a(3, ItemStack.field_190927_a);
    private final EntityPlayer thePlayer;
    private MerchantRecipe currentRecipe;
    private int currentRecipeIndex;

    public InventoryMerchant(EntityPlayer thePlayerIn, IMerchant theMerchantIn) {
        this.thePlayer = thePlayerIn;
        this.theMerchant = theMerchantIn;
    }

    @Override
    public int getSizeInventory() {
        return this.theInventory.size();
    }

    @Override
    public boolean func_191420_l() {
        for (ItemStack itemstack : this.theInventory) {
            if (itemstack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.theInventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = this.theInventory.get(index);
        if (index == 2 && !itemstack.isEmpty()) {
            return ItemStackHelper.getAndSplit(this.theInventory, index, itemstack.getCount());
        }
        ItemStack itemstack1 = ItemStackHelper.getAndSplit(this.theInventory, index, count);
        if (!itemstack1.isEmpty() && this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
        }
        return itemstack1;
    }

    private boolean inventoryResetNeededOnSlotChange(int slotIn) {
        return slotIn == 0 || slotIn == 1;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.theInventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.theInventory.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.func_190920_e(this.getInventoryStackLimit());
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
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.theMerchant.getCustomer() == player;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void markDirty() {
        this.resetRecipeAndSlots();
    }

    public void resetRecipeAndSlots() {
        this.currentRecipe = null;
        ItemStack itemstack = this.theInventory.get(0);
        ItemStack itemstack1 = this.theInventory.get(1);
        if (itemstack.isEmpty()) {
            itemstack = itemstack1;
            itemstack1 = ItemStack.field_190927_a;
        }
        if (itemstack.isEmpty()) {
            this.setInventorySlotContents(2, ItemStack.field_190927_a);
        } else {
            MerchantRecipeList merchantrecipelist = this.theMerchant.getRecipes(this.thePlayer);
            if (merchantrecipelist != null) {
                MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, this.currentRecipeIndex);
                if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                    this.currentRecipe = merchantrecipe;
                    this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                } else if (!itemstack1.isEmpty()) {
                    merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, this.currentRecipeIndex);
                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                        this.currentRecipe = merchantrecipe;
                        this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                    } else {
                        this.setInventorySlotContents(2, ItemStack.field_190927_a);
                    }
                } else {
                    this.setInventorySlotContents(2, ItemStack.field_190927_a);
                }
            }
            this.theMerchant.verifySellingItem(this.getStackInSlot(2));
        }
    }

    public MerchantRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }

    public void setCurrentRecipeIndex(int currentRecipeIndexIn) {
        this.currentRecipeIndex = currentRecipeIndexIn;
        this.resetRecipeAndSlots();
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.theInventory.clear();
    }
}

