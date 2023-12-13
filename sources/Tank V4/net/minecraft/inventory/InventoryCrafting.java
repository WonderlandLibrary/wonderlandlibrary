package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryCrafting implements IInventory {
   private final int inventoryWidth;
   private final ItemStack[] stackList;
   private final Container eventHandler;
   private final int inventoryHeight;

   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.stackList[var1] = var2;
      this.eventHandler.onCraftMatrixChanged(this);
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public ItemStack decrStackSize(int var1, int var2) {
      if (this.stackList[var1] != null) {
         ItemStack var3;
         if (this.stackList[var1].stackSize <= var2) {
            var3 = this.stackList[var1];
            this.stackList[var1] = null;
            this.eventHandler.onCraftMatrixChanged(this);
            return var3;
         } else {
            var3 = this.stackList[var1].splitStack(var2);
            if (this.stackList[var1].stackSize == 0) {
               this.stackList[var1] = null;
            }

            this.eventHandler.onCraftMatrixChanged(this);
            return var3;
         }
      } else {
         return null;
      }
   }

   public void markDirty() {
   }

   public InventoryCrafting(Container var1, int var2, int var3) {
      int var4 = var2 * var3;
      this.stackList = new ItemStack[var4];
      this.eventHandler = var1;
      this.inventoryWidth = var2;
      this.inventoryHeight = var3;
   }

   public String getName() {
      return "container.crafting";
   }

   public int getHeight() {
      return this.inventoryHeight;
   }

   public boolean hasCustomName() {
      return false;
   }

   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      return true;
   }

   public void setField(int var1, int var2) {
   }

   public int getSizeInventory() {
      return this.stackList.length;
   }

   public ItemStack getStackInRowAndColumn(int var1, int var2) {
      return var1 >= 0 && var1 < this.inventoryWidth && var2 >= 0 && var2 <= this.inventoryHeight ? this.getStackInSlot(var1 + var2 * this.inventoryWidth) : null;
   }

   public int getWidth() {
      return this.inventoryWidth;
   }

   public void openInventory(EntityPlayer var1) {
   }

   public void closeInventory(EntityPlayer var1) {
   }

   public int getFieldCount() {
      return 0;
   }

   public int getField(int var1) {
      return 0;
   }

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
   }

   public ItemStack getStackInSlot(int var1) {
      return var1 >= this.getSizeInventory() ? null : this.stackList[var1];
   }

   public ItemStack removeStackFromSlot(int var1) {
      if (this.stackList[var1] != null) {
         ItemStack var2 = this.stackList[var1];
         this.stackList[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public boolean isUseableByPlayer(EntityPlayer var1) {
      return true;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.stackList.length; ++var1) {
         this.stackList[var1] = null;
      }

   }
}
