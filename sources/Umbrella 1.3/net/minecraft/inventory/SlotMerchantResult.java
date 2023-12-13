/*
 * Decompiled with CFR 0.150.
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
    private final InventoryMerchant theMerchantInventory;
    private EntityPlayer thePlayer;
    private int field_75231_g;
    private final IMerchant theMerchant;
    private static final String __OBFID = "CL_00001758";

    public SlotMerchantResult(EntityPlayer p_i1822_1_, IMerchant p_i1822_2_, InventoryMerchant p_i1822_3_, int p_i1822_4_, int p_i1822_5_, int p_i1822_6_) {
        super(p_i1822_3_, p_i1822_4_, p_i1822_5_, p_i1822_6_);
        this.thePlayer = p_i1822_1_;
        this.theMerchant = p_i1822_2_;
        this.theMerchantInventory = p_i1822_3_;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int p_75209_1_) {
        if (this.getHasStack()) {
            this.field_75231_g += Math.min(p_75209_1_, this.getStack().stackSize);
        }
        return super.decrStackSize(p_75209_1_);
    }

    @Override
    protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
        this.field_75231_g += p_75210_2_;
        this.onCrafting(p_75210_1_);
    }

    @Override
    protected void onCrafting(ItemStack p_75208_1_) {
        p_75208_1_.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75231_g);
        this.field_75231_g = 0;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        ItemStack var5;
        ItemStack var4;
        this.onCrafting(stack);
        MerchantRecipe var3 = this.theMerchantInventory.getCurrentRecipe();
        if (var3 != null && (this.doTrade(var3, var4 = this.theMerchantInventory.getStackInSlot(0), var5 = this.theMerchantInventory.getStackInSlot(1)) || this.doTrade(var3, var5, var4))) {
            this.theMerchant.useRecipe(var3);
            playerIn.triggerAchievement(StatList.timesTradedWithVillagerStat);
            if (var4 != null && var4.stackSize <= 0) {
                var4 = null;
            }
            if (var5 != null && var5.stackSize <= 0) {
                var5 = null;
            }
            this.theMerchantInventory.setInventorySlotContents(0, var4);
            this.theMerchantInventory.setInventorySlotContents(1, var5);
        }
    }

    private boolean doTrade(MerchantRecipe trade, ItemStack firstItem, ItemStack secondItem) {
        ItemStack var4 = trade.getItemToBuy();
        ItemStack var5 = trade.getSecondItemToBuy();
        if (firstItem != null && firstItem.getItem() == var4.getItem()) {
            if (var5 != null && secondItem != null && var5.getItem() == secondItem.getItem()) {
                firstItem.stackSize -= var4.stackSize;
                secondItem.stackSize -= var5.stackSize;
                return true;
            }
            if (var5 == null && secondItem == null) {
                firstItem.stackSize -= var4.stackSize;
                return true;
            }
        }
        return false;
    }
}

