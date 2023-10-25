/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant
implements IInventory {
    private ItemStack[] theInventory = new ItemStack[3];
    private final IMerchant theMerchant;
    private int currentRecipeIndex;
    private final EntityPlayer thePlayer;
    private MerchantRecipe currentRecipe;

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (this.theInventory[n] != null) {
            if (n == 2) {
                ItemStack itemStack = this.theInventory[n];
                this.theInventory[n] = null;
                return itemStack;
            }
            if (this.theInventory[n].stackSize <= n2) {
                ItemStack itemStack = this.theInventory[n];
                this.theInventory[n] = null;
                if (this.inventoryResetNeededOnSlotChange(n)) {
                    this.resetRecipeAndSlots();
                }
                return itemStack;
            }
            ItemStack itemStack = this.theInventory[n].splitStack(n2);
            if (this.theInventory[n].stackSize == 0) {
                this.theInventory[n] = null;
            }
            if (this.inventoryResetNeededOnSlotChange(n)) {
                this.resetRecipeAndSlots();
            }
            return itemStack;
        }
        return null;
    }

    public void resetRecipeAndSlots() {
        this.currentRecipe = null;
        ItemStack itemStack = this.theInventory[0];
        ItemStack itemStack2 = this.theInventory[1];
        if (itemStack == null) {
            itemStack = itemStack2;
            itemStack2 = null;
        }
        if (itemStack == null) {
            this.setInventorySlotContents(2, null);
        } else {
            MerchantRecipeList merchantRecipeList = this.theMerchant.getRecipes(this.thePlayer);
            if (merchantRecipeList != null) {
                MerchantRecipe merchantRecipe = merchantRecipeList.canRecipeBeUsed(itemStack, itemStack2, this.currentRecipeIndex);
                if (merchantRecipe != null && !merchantRecipe.isRecipeDisabled()) {
                    this.currentRecipe = merchantRecipe;
                    this.setInventorySlotContents(2, merchantRecipe.getItemToSell().copy());
                } else if (itemStack2 != null) {
                    merchantRecipe = merchantRecipeList.canRecipeBeUsed(itemStack2, itemStack, this.currentRecipeIndex);
                    if (merchantRecipe != null && !merchantRecipe.isRecipeDisabled()) {
                        this.currentRecipe = merchantRecipe;
                        this.setInventorySlotContents(2, merchantRecipe.getItemToSell().copy());
                    } else {
                        this.setInventorySlotContents(2, null);
                    }
                } else {
                    this.setInventorySlotContents(2, null);
                }
            }
        }
        this.theMerchant.verifySellingItem(this.getStackInSlot(2));
    }

    @Override
    public void clear() {
        int n = 0;
        while (n < this.theInventory.length) {
            this.theInventory[n] = null;
            ++n;
        }
    }

    private boolean inventoryResetNeededOnSlotChange(int n) {
        return n == 0 || n == 1;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void markDirty() {
        this.resetRecipeAndSlots();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.theMerchant.getCustomer() == entityPlayer;
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    public InventoryMerchant(EntityPlayer entityPlayer, IMerchant iMerchant) {
        this.thePlayer = entityPlayer;
        this.theMerchant = iMerchant;
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void setField(int n, int n2) {
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public MerchantRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }

    public void setCurrentRecipeIndex(int n) {
        this.currentRecipeIndex = n;
        this.resetRecipeAndSlots();
    }

    @Override
    public String getName() {
        return "mob.villager";
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return this.theInventory[n];
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.theInventory[n] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
        if (this.inventoryResetNeededOnSlotChange(n)) {
            this.resetRecipeAndSlots();
        }
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (this.theInventory[n] != null) {
            ItemStack itemStack = this.theInventory[n];
            this.theInventory[n] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public int getSizeInventory() {
        return this.theInventory.length;
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }
}

