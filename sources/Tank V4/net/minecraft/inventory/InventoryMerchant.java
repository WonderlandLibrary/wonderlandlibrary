package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory {
   private int currentRecipeIndex;
   private ItemStack[] theInventory = new ItemStack[3];
   private MerchantRecipe currentRecipe;
   private final EntityPlayer thePlayer;
   private final IMerchant theMerchant;

   public int getInventoryStackLimit() {
      return 64;
   }

   public ItemStack getStackInSlot(int var1) {
      return this.theInventory[var1];
   }

   public void setField(int var1, int var2) {
   }

   public void setCurrentRecipeIndex(int var1) {
      this.currentRecipeIndex = var1;
      this.resetRecipeAndSlots();
   }

   public ItemStack removeStackFromSlot(int var1) {
      if (this.theInventory[var1] != null) {
         ItemStack var2 = this.theInventory[var1];
         this.theInventory[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public int getSizeInventory() {
      return this.theInventory.length;
   }

   public String getName() {
      return "mob.villager";
   }

   public void clear() {
      for(int var1 = 0; var1 < this.theInventory.length; ++var1) {
         this.theInventory[var1] = null;
      }

   }

   public MerchantRecipe getCurrentRecipe() {
      return this.currentRecipe;
   }

   public int getField(int var1) {
      return 0;
   }

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.theInventory[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }

      if (var1 != 0) {
         this.resetRecipeAndSlots();
      }

   }

   public InventoryMerchant(EntityPlayer var1, IMerchant var2) {
      this.thePlayer = var1;
      this.theMerchant = var2;
   }

   public boolean isUseableByPlayer(EntityPlayer var1) {
      return this.theMerchant.getCustomer() == var1;
   }

   public void markDirty() {
      this.resetRecipeAndSlots();
   }

   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      return true;
   }

   public int getFieldCount() {
      return 0;
   }

   public ItemStack decrStackSize(int var1, int var2) {
      if (this.theInventory[var1] != null) {
         ItemStack var3;
         if (var1 == 2) {
            var3 = this.theInventory[var1];
            this.theInventory[var1] = null;
            return var3;
         } else if (this.theInventory[var1].stackSize <= var2) {
            var3 = this.theInventory[var1];
            this.theInventory[var1] = null;
            if (var1 != 0) {
               this.resetRecipeAndSlots();
            }

            return var3;
         } else {
            var3 = this.theInventory[var1].splitStack(var2);
            if (this.theInventory[var1].stackSize == 0) {
               this.theInventory[var1] = null;
            }

            if (var1 != 0) {
               this.resetRecipeAndSlots();
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   public void openInventory(EntityPlayer var1) {
   }

   public boolean hasCustomName() {
      return false;
   }

   public void closeInventory(EntityPlayer var1) {
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
         this.setInventorySlotContents(2, (ItemStack)null);
      } else {
         MerchantRecipeList var3 = this.theMerchant.getRecipes(this.thePlayer);
         if (var3 != null) {
            MerchantRecipe var4 = var3.canRecipeBeUsed(var1, var2, this.currentRecipeIndex);
            if (var4 != null && !var4.isRecipeDisabled()) {
               this.currentRecipe = var4;
               this.setInventorySlotContents(2, var4.getItemToSell().copy());
            } else if (var2 != null) {
               var4 = var3.canRecipeBeUsed(var2, var1, this.currentRecipeIndex);
               if (var4 != null && !var4.isRecipeDisabled()) {
                  this.currentRecipe = var4;
                  this.setInventorySlotContents(2, var4.getItemToSell().copy());
               } else {
                  this.setInventorySlotContents(2, (ItemStack)null);
               }
            } else {
               this.setInventorySlotContents(2, (ItemStack)null);
            }
         }
      }

      this.theMerchant.verifySellingItem(this.getStackInSlot(2));
   }
}
