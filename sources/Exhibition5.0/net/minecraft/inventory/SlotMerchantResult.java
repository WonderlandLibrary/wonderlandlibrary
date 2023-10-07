// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.village.MerchantRecipe;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;

public class SlotMerchantResult extends Slot
{
    private final InventoryMerchant theMerchantInventory;
    private EntityPlayer thePlayer;
    private int field_75231_g;
    private final IMerchant theMerchant;
    private static final String __OBFID = "CL_00001758";
    
    public SlotMerchantResult(final EntityPlayer p_i1822_1_, final IMerchant p_i1822_2_, final InventoryMerchant p_i1822_3_, final int p_i1822_4_, final int p_i1822_5_, final int p_i1822_6_) {
        super(p_i1822_3_, p_i1822_4_, p_i1822_5_, p_i1822_6_);
        this.thePlayer = p_i1822_1_;
        this.theMerchant = p_i1822_2_;
        this.theMerchantInventory = p_i1822_3_;
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(final int p_75209_1_) {
        if (this.getHasStack()) {
            this.field_75231_g += Math.min(p_75209_1_, this.getStack().stackSize);
        }
        return super.decrStackSize(p_75209_1_);
    }
    
    @Override
    protected void onCrafting(final ItemStack p_75210_1_, final int p_75210_2_) {
        this.field_75231_g += p_75210_2_;
        this.onCrafting(p_75210_1_);
    }
    
    @Override
    protected void onCrafting(final ItemStack p_75208_1_) {
        p_75208_1_.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75231_g);
        this.field_75231_g = 0;
    }
    
    @Override
    public void onPickupFromSlot(final EntityPlayer playerIn, final ItemStack stack) {
        this.onCrafting(stack);
        final MerchantRecipe var3 = this.theMerchantInventory.getCurrentRecipe();
        if (var3 != null) {
            ItemStack var4 = this.theMerchantInventory.getStackInSlot(0);
            ItemStack var5 = this.theMerchantInventory.getStackInSlot(1);
            if (this.doTrade(var3, var4, var5) || this.doTrade(var3, var5, var4)) {
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
    }
    
    private boolean doTrade(final MerchantRecipe trade, final ItemStack firstItem, final ItemStack secondItem) {
        final ItemStack var4 = trade.getItemToBuy();
        final ItemStack var5 = trade.getSecondItemToBuy();
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
