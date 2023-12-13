package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerHopper extends Container {
   private final IInventory hopperInventory;

   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(var1);
      this.hopperInventory.closeInventory(var1);
   }

   public ContainerHopper(InventoryPlayer var1, IInventory var2, EntityPlayer var3) {
      this.hopperInventory = var2;
      var2.openInventory(var3);
      byte var4 = 51;

      int var5;
      for(var5 = 0; var5 < var2.getSizeInventory(); ++var5) {
         this.addSlotToContainer(new Slot(var2, var5, 44 + var5 * 18, 20));
      }

      for(var5 = 0; var5 < 3; ++var5) {
         for(int var6 = 0; var6 < 9; ++var6) {
            this.addSlotToContainer(new Slot(var1, var6 + var5 * 9 + 9, 8 + var6 * 18, var5 * 18 + var4));
         }
      }

      for(var5 = 0; var5 < 9; ++var5) {
         this.addSlotToContainer(new Slot(var1, var5, 8 + var5 * 18, 58 + var4));
      }

   }

   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 < this.hopperInventory.getSizeInventory()) {
            if (!this.mergeItemStack(var5, this.hopperInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.mergeItemStack(var5, 0, this.hopperInventory.getSizeInventory(), false)) {
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

   public boolean canInteractWith(EntityPlayer var1) {
      return this.hopperInventory.isUseableByPlayer(var1);
   }
}
