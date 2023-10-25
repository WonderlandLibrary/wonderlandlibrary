/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;

public class SlotMerchantResult
extends Slot {
    private EntityPlayer thePlayer;
    private int field_75231_g;
    private final InventoryMerchant theMerchantInventory;
    private final IMerchant theMerchant;

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int n) {
        if (this.getHasStack()) {
            this.field_75231_g += Math.min(n, this.getStack().stackSize);
        }
        return super.decrStackSize(n);
    }

    public SlotMerchantResult(EntityPlayer entityPlayer, IMerchant iMerchant, InventoryMerchant inventoryMerchant, int n, int n2, int n3) {
        super(inventoryMerchant, n, n2, n3);
        this.thePlayer = entityPlayer;
        this.theMerchant = iMerchant;
        this.theMerchantInventory = inventoryMerchant;
    }

    @Override
    protected void onCrafting(ItemStack itemStack) {
        itemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75231_g);
        this.field_75231_g = 0;
    }

    @Override
    protected void onCrafting(ItemStack itemStack, int n) {
        this.field_75231_g += n;
        this.onCrafting(itemStack);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
        ItemStack itemStack2;
        ItemStack itemStack3;
        this.onCrafting(itemStack);
        MerchantRecipe merchantRecipe = this.theMerchantInventory.getCurrentRecipe();
        if (merchantRecipe != null && (this.doTrade(merchantRecipe, itemStack3 = this.theMerchantInventory.getStackInSlot(0), itemStack2 = this.theMerchantInventory.getStackInSlot(1)) || this.doTrade(merchantRecipe, itemStack2, itemStack3))) {
            this.theMerchant.useRecipe(merchantRecipe);
            entityPlayer.triggerAchievement(StatList.timesTradedWithVillagerStat);
            if (itemStack3 != null && itemStack3.stackSize <= 0) {
                itemStack3 = null;
            }
            if (itemStack2 != null && itemStack2.stackSize <= 0) {
                itemStack2 = null;
            }
            this.theMerchantInventory.setInventorySlotContents(0, itemStack3);
            this.theMerchantInventory.setInventorySlotContents(1, itemStack2);
        }
    }

    private boolean doTrade(MerchantRecipe merchantRecipe, ItemStack itemStack, ItemStack itemStack2) {
        ItemStack itemStack3 = merchantRecipe.getItemToBuy();
        ItemStack itemStack4 = merchantRecipe.getSecondItemToBuy();
        if (itemStack != null && itemStack.getItem() == itemStack3.getItem()) {
            if (itemStack4 != null && itemStack2 != null && itemStack4.getItem() == itemStack2.getItem()) {
                itemStack.stackSize -= itemStack3.stackSize;
                itemStack2.stackSize -= itemStack4.stackSize;
                return true;
            }
            if (itemStack4 == null && itemStack2 == null) {
                itemStack.stackSize -= itemStack3.stackSize;
                return true;
            }
        }
        return false;
    }
}

