package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerChest extends Container {
   private int numRows;
   private IInventory lowerChestInventory;

   public boolean canInteractWith(EntityPlayer var1) {
      return this.lowerChestInventory.isUseableByPlayer(var1);
   }

   public ContainerChest(IInventory var1, IInventory var2, EntityPlayer var3) {
      this.lowerChestInventory = var2;
      this.numRows = var2.getSizeInventory() / 9;
      var2.openInventory(var3);
      int var4 = (this.numRows - 4) * 18;

      int var5;
      int var6;
      for(var5 = 0; var5 < this.numRows; ++var5) {
         for(var6 = 0; var6 < 9; ++var6) {
            this.addSlotToContainer(new Slot(var2, var6 + var5 * 9, 8 + var6 * 18, 18 + var5 * 18));
         }
      }

      for(var5 = 0; var5 < 3; ++var5) {
         for(var6 = 0; var6 < 9; ++var6) {
            this.addSlotToContainer(new Slot(var1, var6 + var5 * 9 + 9, 8 + var6 * 18, 103 + var5 * 18 + var4));
         }
      }

      for(var5 = 0; var5 < 9; ++var5) {
         this.addSlotToContainer(new Slot(var1, var5, 8 + var5 * 18, 161 + var4));
      }

   }

   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(var1);
      this.lowerChestInventory.closeInventory(var1);
   }

   public IInventory getLowerChestInventory() {
      return this.lowerChestInventory;
   }

   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 < this.numRows * 9) {
            if (!this.mergeItemStack(var5, this.numRows * 9, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.mergeItemStack(var5, 0, this.numRows * 9, false)) {
            return null;
         }

         if (var5.stackSize == 0) {
            var4.putStack((ItemStack)null);
         } else {
            var4.onSlotChanged();
         }
      }

      return var3;
   }
}
