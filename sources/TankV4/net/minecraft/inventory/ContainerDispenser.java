package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerDispenser extends Container {
   private IInventory dispenserInventory;

   public ContainerDispenser(IInventory var1, IInventory var2) {
      this.dispenserInventory = var2;

      int var3;
      int var4;
      for(var3 = 0; var3 < 3; ++var3) {
         for(var4 = 0; var4 < 3; ++var4) {
            this.addSlotToContainer(new Slot(var2, var4 + var3 * 3, 62 + var4 * 18, 17 + var3 * 18));
         }
      }

      for(var3 = 0; var3 < 3; ++var3) {
         for(var4 = 0; var4 < 9; ++var4) {
            this.addSlotToContainer(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
         }
      }

      for(var3 = 0; var3 < 9; ++var3) {
         this.addSlotToContainer(new Slot(var1, var3, 8 + var3 * 18, 142));
      }

   }

   public boolean canInteractWith(EntityPlayer var1) {
      return this.dispenserInventory.isUseableByPlayer(var1);
   }

   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 < 9) {
            if (!this.mergeItemStack(var5, 9, 45, true)) {
               return null;
            }
         } else if (!this.mergeItemStack(var5, 0, 9, false)) {
            return null;
         }

         if (var5.stackSize == 0) {
            var4.putStack((ItemStack)null);
         } else {
            var4.onSlotChanged();
         }

         if (var5.stackSize == var3.stackSize) {
            return null;
         }

         var4.onPickupFromSlot(var1, var5);
      }

      return var3;
   }
}
