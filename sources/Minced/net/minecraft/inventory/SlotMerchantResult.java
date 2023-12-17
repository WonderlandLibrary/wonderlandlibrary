// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.village.MerchantRecipe;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;

public class SlotMerchantResult extends Slot
{
    private final InventoryMerchant merchantInventory;
    private final EntityPlayer player;
    private int removeCount;
    private final IMerchant merchant;
    
    public SlotMerchantResult(final EntityPlayer player, final IMerchant merchant, final InventoryMerchant merchantInventory, final int slotIndex, final int xPosition, final int yPosition) {
        super(merchantInventory, slotIndex, xPosition, yPosition);
        this.player = player;
        this.merchant = merchant;
        this.merchantInventory = merchantInventory;
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(final int amount) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack, final int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack) {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        this.removeCount = 0;
    }
    
    @Override
    public ItemStack onTake(final EntityPlayer thePlayer, final ItemStack stack) {
        this.onCrafting(stack);
        final MerchantRecipe merchantrecipe = this.merchantInventory.getCurrentRecipe();
        if (merchantrecipe != null) {
            final ItemStack itemstack = this.merchantInventory.getStackInSlot(0);
            final ItemStack itemstack2 = this.merchantInventory.getStackInSlot(1);
            if (this.doTrade(merchantrecipe, itemstack, itemstack2) || this.doTrade(merchantrecipe, itemstack2, itemstack)) {
                this.merchant.useRecipe(merchantrecipe);
                thePlayer.addStat(StatList.TRADED_WITH_VILLAGER);
                this.merchantInventory.setInventorySlotContents(0, itemstack);
                this.merchantInventory.setInventorySlotContents(1, itemstack2);
            }
        }
        return stack;
    }
    
    private boolean doTrade(final MerchantRecipe trade, final ItemStack firstItem, final ItemStack secondItem) {
        final ItemStack itemstack = trade.getItemToBuy();
        final ItemStack itemstack2 = trade.getSecondItemToBuy();
        if (firstItem.getItem() == itemstack.getItem() && firstItem.getCount() >= itemstack.getCount()) {
            if (!itemstack2.isEmpty() && !secondItem.isEmpty() && itemstack2.getItem() == secondItem.getItem() && secondItem.getCount() >= itemstack2.getCount()) {
                firstItem.shrink(itemstack.getCount());
                secondItem.shrink(itemstack2.getCount());
                return true;
            }
            if (itemstack2.isEmpty() && secondItem.isEmpty()) {
                firstItem.shrink(itemstack.getCount());
                return true;
            }
        }
        return false;
    }
}
