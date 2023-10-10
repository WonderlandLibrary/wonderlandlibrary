package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;

public class SlotMerchantResult extends Slot {
   private EntityPlayer thePlayer;
   private final IMerchant theMerchant;
   private int field_75231_g;
   private final InventoryMerchant theMerchantInventory;

   public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
      this.onCrafting(var2);
      MerchantRecipe var3 = this.theMerchantInventory.getCurrentRecipe();
      if (var3 != null) {
         ItemStack var4 = this.theMerchantInventory.getStackInSlot(0);
         ItemStack var5 = this.theMerchantInventory.getStackInSlot(1);
         if (var5 == false || var4 != false) {
            this.theMerchant.useRecipe(var3);
            var1.triggerAchievement(StatList.timesTradedWithVillagerStat);
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

   protected void onCrafting(ItemStack var1, int var2) {
      this.field_75231_g += var2;
      this.onCrafting(var1);
   }

   public SlotMerchantResult(EntityPlayer var1, IMerchant var2, InventoryMerchant var3, int var4, int var5, int var6) {
      super(var3, var4, var5, var6);
      this.thePlayer = var1;
      this.theMerchant = var2;
      this.theMerchantInventory = var3;
   }

   protected void onCrafting(ItemStack var1) {
      var1.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75231_g);
      this.field_75231_g = 0;
   }

   public ItemStack decrStackSize(int var1) {
      if (this.getHasStack()) {
         this.field_75231_g += Math.min(var1, this.getStack().stackSize);
      }

      return super.decrStackSize(var1);
   }

   public boolean isItemValid(ItemStack var1) {
      return false;
   }
}
